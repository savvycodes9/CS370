package ui;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import model.Group;
import model.Availability;
import model.GroupAvailabilityService;

import javax.swing.*;
import java.awt.*;

public class GroupEditFrame extends JFrame{


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

        private CalendarFrame parent;
        private int groupId;


        public GroupEditFrame(int groupId, CalendarFrame parent){
            this.groupId = groupId;
        this.parent = parent;

        init();
        requestForm();
        }

        private void init(){
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

        JButton syncBtn = new JButton("Make Schedule");
        syncBtn.setBounds(200, 100, 200, 25);
        panel.add(syncBtn);

        syncBtn.addActionListener(e -> {
            try {
                // 1. Load this group from the groups DB
                Group group = loadGroupById(groupId);

                // 2. Load all availabilities from the availability DB
                java.util.List<Availability> allAvailabilities = loadAllAvailabilities();

                // 3. Ask which date to compute for
                String date = JOptionPane.showInputDialog(
                        this,
                        "Enter date (YYYY-MM-DD):",
                        "2025-11-20"
                );
                if (date == null || date.isBlank()) {
                    return; // user cancelled
                }

                // 4. Compute common intervals
                GroupAvailabilityService service = new GroupAvailabilityService();
                java.util.List<GroupAvailabilityService.Interval> slots =
                        service.computeGroupAvailability(group, allAvailabilities, date);

                // 5. Show result
                if (slots.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "No common availability on " + date,
                            "Group Availability",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    StringBuilder sb = new StringBuilder("Common availability on " + date + ":\n\n");
                    for (GroupAvailabilityService.Interval interval : slots) {
                        sb.append(interval.start).append(" - ").append(interval.end).append("\n");
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

        JButton deletegroupBtn = new JButton("Delete Group");
        deletegroupBtn.setBounds(200, 150, 200, 25);
        panel.add(deletegroupBtn);

        deletegroupBtn.addActionListener(e -> onDeleteGroup());

        JButton saveBtn = new JButton("Save and Close");
        saveBtn.setBounds(10, 20, 150, 100);
        saveBtn.setBackground(new Color(0xadd8e6));
        saveBtn.addActionListener(e -> {
            // save to database here
            dispose();
        });
        panel.add(saveBtn);

        JButton addUserBtn = new JButton("Add");
        addUserBtn.setBounds(450, 50, 90, 20);
        addUserBtn.setFont(new Font("Arial", Font.PLAIN, 10));
        addUserBtn.addActionListener(e -> {
            // add user logic
        });
        panel.add(addUserBtn);

        rmvUserBtn = new JButton("Remove user");
        rmvUserBtn.setBounds(450, 100, 90, 20);
        rmvUserBtn.setFont(new Font("Arial", Font.PLAIN, 10));
        rmvUserBtn.addActionListener(e -> {
            // remove user logic
        });
        panel.add(rmvUserBtn);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

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
                        Paths.get("Just_for_fun\\Database\\groups.txt")
                );
                List<String> newLines = new ArrayList<>();

                for (String line : lines) {
                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0].trim());
                    if (id != groupId) {
                        newLines.add(line);
                    }
                }

                Files.write(Paths.get("Just_for_fun\\Database\\groups.txt"), newLines);

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

            // refresh main calendar sidebar
            if (parent != null) {
                parent.sidebarDisplay();   // or parent.refreshGroups();
            }

            dispose(); // closes this GroupEditFrame

        } else {
            // user chose No
            JOptionPane selectedNoPane = new JOptionPane(
                    "Your group has not been deleted",
                    JOptionPane.INFORMATION_MESSAGE
            );

            JDialog dialog = selectedNoPane.createDialog(this, "Notice");
            Timer timer = new Timer(5000, ev -> dialog.dispose());
            timer.setRepeats(false);
            timer.start();
            dialog.setVisible(true);
        }
    }

    private Group loadGroupById(int groupId) throws java.io.IOException {
    try (java.io.BufferedReader br = new java.io.BufferedReader(
            new java.io.FileReader("Just_for_fun\\Database\\groups.txt"))) {

        String line;
        while ((line = br.readLine()) != null) {
            // Example: 1,CS370 Study Group,1;2;3
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0].trim());

            if (id == groupId) {
                String name = parts[1].trim();

                java.util.List<Integer> members = new java.util.ArrayList<>();
                int ownerUserId = -1;

                if (parts.length >= 3) {
                    String[] memberParts = parts[2].split(";");
                    for (String m : memberParts) {
                        m = m.trim();
                        if (!m.isEmpty()) {
                            int userId = Integer.parseInt(m);
                            members.add(userId);
                        }
                    }
                }

                if (!members.isEmpty()) {
                    ownerUserId = members.get(0); // assume first member is owner
                } else {
                    ownerUserId = -1; // or handle however you want
                }

                return new Group(id, name, ownerUserId, members);
            }
        }
    }

    throw new IllegalArgumentException("Group with id " + groupId + " not found");
    }



    private java.util.List<Availability> loadAllAvailabilities() throws java.io.IOException {
        java.util.List<Availability> result = new java.util.ArrayList<>();

        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.FileReader("Just_for_fun\\Database\\availability.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                // Example: 1,1,2025-11-20,14:00,16:00,true
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

}

/* 
        public static void main(String[] args){

            new ui.GroupEditFrame(null);

        }
            */


