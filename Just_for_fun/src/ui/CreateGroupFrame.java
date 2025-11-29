package ui;
import model.Group;
import model.User;
import controller.GroupController;
import controller.UserController;

import javax.swing.*;
import java.awt.*;
import javax.swing.JButton;

public class CreateGroupFrame {

    private JFrame groupFrame;
    private JTextField titleText;
    private JTextField userText;
    private JButton saveBtn;
    private JButton addUserBtn;
    private Group currentGroup = null;
    private CalendarFrame parent;

    private GroupController groupController = new GroupController();
    private UserController userController = new UserController();
    private DefaultListModel<String> memberListModel = new DefaultListModel<>();
    private JList<String> memberList;

    public CreateGroupFrame(CalendarFrame parent) {
        this.parent = parent;
        init();
        requestForm();
    }

    public CreateGroupFrame() {
        this(null);
    }

    private void init() {
        groupFrame = new JFrame("Create Group");
        groupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        groupFrame.setSize(600, 400);
        groupFrame.setLocationRelativeTo(null);
        groupFrame.setLayout(new BorderLayout());
    }

    private void requestForm() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        saveBtn = new JButton("Save Group");
        saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveBtn.setBackground(new Color(0xADD8E6));
        saveBtn.setFocusPainted(false);

        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(saveBtn);
        leftPanel.add(Box.createVerticalGlue());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleRow.add(new JLabel("Group Title:"));
        titleText = new JTextField(15);
        titleRow.add(titleText);
        centerPanel.add(titleRow);

        JPanel userRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userRow.add(new JLabel("Username:"));
        userText = new JTextField(15);
        userRow.add(userText);

        addUserBtn = new JButton("Add");
        userRow.add(addUserBtn);
        centerPanel.add(userRow);

        memberList = new JList<>(memberListModel);
        memberList.setCellRenderer(new MemberRenderer());

        JScrollPane scroll = new JScrollPane(memberList);
        scroll.setPreferredSize(new Dimension(350, 200));

        JPanel listWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        listWrapper.add(scroll);

        centerPanel.add(listWrapper);
        saveBtn.addActionListener(e -> {
            // here is where people click save and then it saves to the database
            String groupName = titleText.getText().trim();

            if (groupName.isEmpty()) {
                JOptionPane.showMessageDialog(groupFrame, "Group name cannot be empty.");
                return;
            }

            boolean ok;

            if (currentGroup == null) {
                // CREATE GROUP WITH NO OWNER
                ok = groupController.createGroup(groupName, 0);

                if (!ok) {
                    JOptionPane.showMessageDialog(groupFrame, "Error saving group.");
                    return;
                }

                currentGroup = groupController.getGroupByName(groupName);

                JOptionPane.showMessageDialog(groupFrame, "Group saved! You may now add members.");
                saveBtn.setText("Close");

                for (var al : saveBtn.getActionListeners()) saveBtn.removeActionListener(al);

                saveBtn.addActionListener(ev -> {
                    if (parent != null) parent.sidebarDisplay();
                    groupFrame.dispose();
                });

            } else {
                // UPDATE EXISTING GROUP NAME
                ok = groupController.updateGroup(currentGroup.getGroupId(), groupName);

                if (!ok) {
                    JOptionPane.showMessageDialog(groupFrame, "Error updating group.");
                    return;
                }
                JOptionPane.showMessageDialog(groupFrame, "Group saved! You may now add members.");

                saveBtn.setText("Close");
                saveBtn.addActionListener(ev -> {
                    if (parent != null) parent.sidebarDisplay();
                    groupFrame.dispose();
                });
            }
        });

        addUserBtn.addActionListener(e -> {
            //something needs to be added for entering the username and linking it to database and then returning to the this frame and displaying the user name at the bottom
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

        memberList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int index = memberList.locationToIndex(e.getPoint());
                if (index == -1) return;

                Rectangle cellBounds = memberList.getCellBounds(index, index);
                int clickX = e.getX();

                if (clickX > cellBounds.x + cellBounds.width - 45) { //removes users
                    String username = memberListModel.get(index);
                    User u = userController.getUserByUsername(username);

                    if (u != null) {
                        boolean ok = groupController.removeUserFromGroup(currentGroup.getGroupId(), u.getUserId());
                        if (!ok) {
                            JOptionPane.showMessageDialog(groupFrame, "Error removing user.");
                            return;
                        }

                        currentGroup.getMembers().remove((Integer) u.getUserId());
                        memberListModel.remove(index);
                    }
                }
            }
        });

        groupFrame.add(leftPanel, BorderLayout.WEST);
        groupFrame.add(centerPanel, BorderLayout.CENTER);
        groupFrame.setVisible(true);
    }

    class MemberRenderer extends JPanel implements ListCellRenderer<String> {

        private JLabel nameLabel = new JLabel();
        private JButton removeBtn = new JButton("-"); //

        public MemberRenderer() {
            setLayout(new BorderLayout(5, 0));
            removeBtn.setPreferredSize(new Dimension(40, 20));
            removeBtn.setFocusPainted(false);
            removeBtn.setBackground(new Color(255, 180, 180));
            add(nameLabel, BorderLayout.CENTER);
            add(removeBtn, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends String> list,
                String value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            nameLabel.setText(value);

            if (isSelected) {
                setBackground(new Color(220, 220, 255));
            } else {
                setBackground(Color.WHITE);
            }

            return this;
        }
    }

    public static void main(String[] args) {
        new CreateGroupFrame();
    }
}