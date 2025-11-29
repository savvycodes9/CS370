package ui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.Timer;

import model.Group;
import model.User;
import controller.GroupController;
import controller.UserController;
import model.Availability;
import model.GroupAvailabilityService;

public class GroupEditFrame extends JFrame {

    private JFrame groupFrame;
    private JLabel groupTitleLabel;
    private JLabel addUserLabel;
    private JTextField titleText;
    private JTextField userText;
    private JButton syncBtn;
    private JButton deletegroupBtn;
    private JButton saveBtn;
    private JButton addUserBtn;
    private JButton rmvUserBtn;
    private JPanel userList;
    private Group currentGroup = null;

    private CalendarFrame parent;
    private int groupId;

    private GroupController groupController = new GroupController();
    private UserController userController = new UserController();
    private DefaultListModel<String> memberListModel = new DefaultListModel<>();

    // --------- DB FILES ---------
    public static final String DB_FOLDER =
            System.getProperty("user.dir") + File.separator + "Just_for_fun" + File.separator + "Database";

    // groups.txt: e.g. "3,Math Exam,0"
    public static final String GROUP_FILE =
            DB_FOLDER + File.separator + "groups.txt";

    // availability.txt: teammate's file with Availability rows:
    // 1,1,2025-11-20,14:00,16:00,true
    public static final String AVAILABILITY_FILE =
            DB_FOLDER + File.separator + "availability.txt";

    // events.txt: your events file with:
    // id,title,desc,date,start,end,location,groupId,userId,isPrivate
    public static final String EVENTS_FILE =
            DB_FOLDER + File.separator + "events.txt";

    // group_members.txt: e.g. "3,1"
    public static final String GROUP_MEMBERS_FILE =
            DB_FOLDER + File.separator + "group_members.txt";


    public GroupEditFrame(int groupId, CalendarFrame parent) {
        this.groupId = groupId;
        this.parent = parent;

        ensureDatabaseFilesExist();
        init();
        requestForm();
    }

    private void init() {
        this.groupFrame = this; // so all JOptionPane calls using groupFrame are safe

        setTitle("Edit Group #" + groupId);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void requestForm() {

        JPanel panel = new JPanel();
        panel.setLayout(null);

        groupTitleLabel = new JLabel("Group Title: ");
        groupTitleLabel.setBounds(200, 20, 200, 25);
        panel.add(groupTitleLabel);

        titleText = new JTextField();
        titleText.setBounds(300, 20, 130, 25);
        panel.add(titleText);

        addUserLabel = new JLabel("Add User: ");
        addUserLabel.setBounds(200, 50, 200, 25);
        panel.add(addUserLabel);

        userText = new JTextField();
        userText.setBounds(300, 50, 130, 25);
        panel.add(userText);

        syncBtn = new JButton("Make Schedule");
        syncBtn.setBounds(200, 100, 200, 25);
        panel.add(syncBtn);

        // =========================
        //   SYNC / AVAILABILITY
        // =========================
        syncBtn.addActionListener(e -> {
            try {
                // 1. Load this group (including members)
                Group group = loadGroupById(groupId);

                // 2. Ask for the date (must match format in events.txt: YYYY-MM-DD)
                String date = JOptionPane.showInputDialog(
                        this,
                        "Enter date (YYYY-MM-DD):",
                        "2025-11-20"
                );
                if (date == null || date.isBlank()) {
                    return; // user cancelled
                }

                // 3. Load BUSY intervals for each group member on that date from events.txt
                Map<Integer, List<GroupAvailabilityService.BusyInterval>> busyPerUser =
                        loadBusyForGroupOnDate(group, date);

                // 4. Convert BUSY -> FREE per user
                GroupAvailabilityService service = new GroupAvailabilityService();
                Map<Integer, List<GroupAvailabilityService.Interval>> freePerUser =
                        new HashMap<>();

                for (int uid : group.getMembers()) {
                    List<GroupAvailabilityService.BusyInterval> busy =
                            busyPerUser.getOrDefault(uid, new ArrayList<>());
                    List<GroupAvailabilityService.Interval> free =
                            service.invertBusyToFree(busy);
                    freePerUser.put(uid, free);
                }

                // 5. Intersect FREE intervals across all members
                List<GroupAvailabilityService.Interval> slots =
                        service.computeFromFreeMap(freePerUser, group.getMembers());

                // 6. Clamp to 6 AM – 10 PM and format in 12-hour time
                LocalTime windowStart = LocalTime.of(6, 0);   // 6:00 AM
                LocalTime windowEnd   = LocalTime.of(22, 0);  // 10:00 PM
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("h:mm a");

                List<GroupAvailabilityService.Interval> clamped = new ArrayList<>();

                for (GroupAvailabilityService.Interval interval : slots) {
                    LocalTime s = interval.start.isBefore(windowStart) ? windowStart : interval.start;
                    LocalTime es = interval.end.isAfter(windowEnd) ? windowEnd : interval.end;

                    if (!s.isBefore(es)) {
                        continue;
                    }

                    clamped.add(new GroupAvailabilityService.Interval(s, es));
                }

                // 7. Show result based on clamped list
                if (clamped.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "No common free time between 6:00 AM and 10:00 PM on " + date,
                            "Group Availability",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    StringBuilder sb = new StringBuilder(
                            "Common FREE time on " + date + " (6:00 AM – 10:00 PM):\n\n"
                    );
                    for (GroupAvailabilityService.Interval interval : clamped) {
                        sb.append(interval.start.format(fmt))
                                .append(" - ")
                                .append(interval.end.format(fmt))
                                .append("\n");
                    }
                    JOptionPane.showMessageDialog(
                            this,
                            sb.toString(),
                            "Group Availability",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Error computing availability: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ---------------- DELETE GROUP ----------------
        deletegroupBtn = new JButton("Delete Group");
        deletegroupBtn.setBounds(200, 150, 200, 25);
        panel.add(deletegroupBtn);

        deletegroupBtn.addActionListener(e -> onDeleteGroup());

        // ---------------- SAVE GROUP ----------------
        saveBtn = new JButton("Save and Close");
        saveBtn.setBounds(10, 20, 150, 100);
        saveBtn.setBackground(new Color(0xadd8e6));
        saveBtn.addActionListener(e -> {
            String groupName = titleText.getText().trim();

            if (groupName.isEmpty()) {
                JOptionPane.showMessageDialog(groupFrame, "Group name cannot be empty.");
                return;
            }

            boolean ok;

            if (currentGroup == null) {
                // CREATE GROUP WITH NO OWNER (ownerUserId = 0 for now)
                ok = groupController.createGroup(groupName, 0);

                if (!ok) {
                    JOptionPane.showMessageDialog(groupFrame, "Error saving group.");
                    return;
                }

                currentGroup = groupController.getGroupByName(groupName);

            } else {
                // UPDATE EXISTING GROUP NAME
                ok = groupController.updateGroup(currentGroup.getGroupId(), groupName);

                if (!ok) {
                    JOptionPane.showMessageDialog(groupFrame, "Error updating group.");
                    return;
                }
                JOptionPane.showMessageDialog(groupFrame, "Group saved! You may now add members.");

                saveBtn.setText("Close");
                saveBtn.addActionListener(ev -> groupFrame.dispose());
            }
        });
        panel.add(saveBtn);

        // ---------------- ADD USER ----------------
        addUserBtn = new JButton("Add");
        addUserBtn.setBounds(450, 50, 90, 20);
        addUserBtn.setFont(new Font("Arial", Font.PLAIN, 10));
        addUserBtn.addActionListener(e -> {
            String username = userText.getText().trim();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(groupFrame, "Enter a username.");
                return;
            }

            if (currentGroup == null) {
                JOptionPane.showMessageDialog(groupFrame, "Save the group first.");
                return;
            }

            User u = userController.getUserByUsername(username);

            if (u == null) {
                JOptionPane.showMessageDialog(groupFrame, "User does not exist.");
                return;
            }

            if (currentGroup.getMembers().contains(u.getUserId())) {
                JOptionPane.showMessageDialog(groupFrame, "User already in group.");
                return;
            }

            boolean ok = groupController.addUserToGroup(currentGroup.getGroupId(), u.getUserId());

            if (!ok) {
                JOptionPane.showMessageDialog(groupFrame, "Error adding user.");
                return;
            }

            currentGroup.getMembers().add(u.getUserId());
            memberListModel.addElement(username);

            JOptionPane.showMessageDialog(groupFrame, "User added!");
            userText.setText("");
        });
        panel.add(addUserBtn);

        // ---------------- REMOVE USER ----------------
        rmvUserBtn = new JButton("Remove user");
        rmvUserBtn.setBounds(450, 100, 90, 20);
        rmvUserBtn.setFont(new Font("Arial", Font.PLAIN, 10));
        rmvUserBtn.addActionListener(e -> {
            String username = userText.getText().trim();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(groupFrame, "Enter a username.");
                return;
            }

            if (currentGroup == null) {
                JOptionPane.showMessageDialog(groupFrame, "Save group first.");
                return;
            }

            User u = userController.getUserByUsername(username);

            if (u == null) {
                JOptionPane.showMessageDialog(groupFrame, "User not found.");
                return;
            }

            if (!currentGroup.getMembers().contains(u.getUserId())) {
                JOptionPane.showMessageDialog(groupFrame, "User is not in this group.");
                return;
            }

            boolean ok = groupController.removeUserFromGroup(currentGroup.getGroupId(), u.getUserId());

            if (!ok) {
                JOptionPane.showMessageDialog(groupFrame, "Error removing user.");
                return;
            }

            currentGroup.getMembers().remove((Integer) u.getUserId());
            memberListModel.removeElement(username);

            JOptionPane.showMessageDialog(groupFrame, "User removed!");
            userText.setText("");
        });
        panel.add(rmvUserBtn);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    // ---------------- DELETE GROUP HANDLER ----------------
    private void onDeleteGroup() {
        Object[] options = {"Yes", "No"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Are you sure you want to delete this group?",
                "Delete Group",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.YES_OPTION) {
            try {
                List<String> lines = Files.readAllLines(
                        Paths.get(GROUP_FILE)
                );
                List<String> newLines = new ArrayList<>();

                for (String line : lines) {
                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0].trim());
                    if (id != groupId) {
                        newLines.add(line);
                    }
                }

                Files.write(Paths.get(GROUP_FILE), newLines);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Error deleting group.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (parent != null) {
                parent.sidebarDisplay();
            }

            dispose();

        } else {
            JOptionPane selectedNoPane = new JOptionPane(
                    "Your group has not been deleted",
                    JOptionPane.INFORMATION_MESSAGE
            );

            JDialog dialog = selectedNoPane.createDialog(this, "Notice");
            javax.swing.Timer timer = new javax.swing.Timer(
                5000,
                new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent ev) {
                        dialog.dispose();
                    }
                }
            )   ;
            timer.setRepeats(false);
            timer.start();
            dialog.setVisible(true);
        }
    }

    // ---------------- LOAD GROUP (from groups.txt + group_members.txt) ----------------
    private Group loadGroupById(int groupId) throws IOException {
        String name = null;
        int ownerUserId = -1;

        // 1. Read basic group info from groups.txt
        try (BufferedReader br = new BufferedReader(
                new FileReader(GROUP_FILE))) {

            String line;
            while ((line = br.readLine()) != null) {
                // Example groups.txt: 3,Math Exam,0
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());

                if (id == groupId) {
                    name = parts[1].trim();
                    if (parts.length >= 3) {
                        ownerUserId = Integer.parseInt(parts[2].trim());
                    }
                    break;
                }
            }
        }

        if (name == null) {
            throw new IllegalArgumentException("Group with id " + groupId + " not found");
        }

        // 2. Read members from group_members.txt
        List<Integer> members = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new FileReader(GROUP_MEMBERS_FILE))) {

            String line;
            while ((line = br.readLine()) != null) {
                // Example group_members.txt: 3,1
                String[] parts = line.split(",");
                if (parts.length < 2) continue;

                int gId = Integer.parseInt(parts[0].trim());
                int userId = Integer.parseInt(parts[1].trim());

                if (gId == groupId) {
                    if (userId > 0 && !members.contains(userId)) {
                        members.add(userId);
                    }
                }
            }
        }

        // 3. Ensure owner is in member list (if valid)
        if (ownerUserId > 0 && !members.contains(ownerUserId)) {
            members.add(ownerUserId);
        }

        return new Group(groupId, name, ownerUserId, members);
    }

    // (Optional, still here if you ever use availability.txt-based logic)
    private List<Availability> loadAllAvailabilities() throws IOException {
        List<Availability> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new FileReader(AVAILABILITY_FILE))) {

            String line;
            while ((line = br.readLine()) != null) {
                // Example availability.txt: 1,1,2025-11-20,14:00,16:00,true
                String[] p = line.split(",");

                int availabilityId = Integer.parseInt(p[0].trim());
                int userId = Integer.parseInt(p[1].trim());
                String date = p[2].trim();
                String startTime = p[3].trim();
                String endTime = p[4].trim();
                boolean isAvailable = Boolean.parseBoolean(p[5].trim());

                result.add(new Availability(
                        availabilityId,
                        userId,
                        date,
                        startTime,
                        endTime,
                        isAvailable
                ));
            }
        }

        return result;
    }

    // Ensure DB folder + files exist
    private void ensureDatabaseFilesExist() {
        try {
            Files.createDirectories(Paths.get(DB_FOLDER));

            if (!Files.exists(Paths.get(GROUP_FILE))) {
                Files.write(Paths.get(GROUP_FILE), new ArrayList<>());
            }

            if (!Files.exists(Paths.get(AVAILABILITY_FILE))) {
                Files.write(Paths.get(AVAILABILITY_FILE), new ArrayList<>());
            }

            if (!Files.exists(Paths.get(EVENTS_FILE))) {
                Files.write(Paths.get(EVENTS_FILE), new ArrayList<>());
            }

            if (!Files.exists(Paths.get(GROUP_MEMBERS_FILE))) {
                Files.write(Paths.get(GROUP_MEMBERS_FILE), new ArrayList<>());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // -------- load busy intervals from events.txt for a given group & date --------
    private Map<Integer, List<GroupAvailabilityService.BusyInterval>>
    loadBusyForGroupOnDate(Group group, String date) throws IOException {

        Set<Integer> memberIds = new HashSet<>(group.getMembers());
        Map<Integer, List<GroupAvailabilityService.BusyInterval>> busyPerUser =
                new HashMap<>();

        try (BufferedReader br = new BufferedReader(
                new FileReader(EVENTS_FILE))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                // Example event in events.txt:
                // 1,hello there,,2025-11-03,09:00,10:00,,-1,1,false
                String[] p = line.split(",");

                if (p.length < 9) continue; // safety

                String evDate = p[3].trim();
                if (!evDate.equals(date)) continue; // only this date

                String start = p[4].trim(); // 09:00
                String end   = p[5].trim(); // 10:00

                int userId = Integer.parseInt(p[8].trim()); // userId column in your file
                if (!memberIds.contains(userId)) continue;   // only group members

                GroupAvailabilityService.BusyInterval busy =
                        new GroupAvailabilityService.BusyInterval(
                                LocalTime.parse(start),
                                LocalTime.parse(end)
                        );

                busyPerUser.putIfAbsent(userId, new ArrayList<>());
                busyPerUser.get(userId).add(busy);
            }
        }

        return busyPerUser;
    }
}
