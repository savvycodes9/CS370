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
    private JLabel groupTitleLabel;
    private JLabel addUserLabel;
    private JTextField titleText;
    private JTextField userText;
    private JButton saveBtn;
    private JButton addUserBtn;
    private JButton rmvUserBtn;
    private JPanel userList;
    private Group currentGroup = null;
    private CalendarFrame parent;

    private GroupController groupController = new GroupController();
    private UserController userController = new UserController();
    private DefaultListModel<String> memberListModel = new DefaultListModel<>();



    public CreateGroupFrame(CalendarFrame parent){
        this.parent = parent;
        init();
        requestForm();
    }

    public CreateGroupFrame() {
        this(null);
    }

    private void init(){
        groupFrame = new JFrame("Create Group");
        groupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        groupFrame.setSize(600,400);
        groupFrame.setLocationRelativeTo(null);
        groupFrame.setLayout(new BorderLayout());
        groupFrame.setVisible(true);
    }

    private void requestForm(){

        JPanel panel = new JPanel();
        panel.setLayout(null);

        groupTitleLabel = new JLabel("Group Title: ");
        groupTitleLabel.setBounds(200,20,200,25);
        panel.add(groupTitleLabel);

        titleText = new JTextField();
        titleText.setBounds(300,20,130,25);
        panel.add(titleText);

        addUserLabel = new JLabel("Add User: ");
        addUserLabel.setBounds(200,50,200,25);
        panel.add(addUserLabel);


        userText = new JTextField();
        userText.setBounds(300,50,130,25);
        panel.add(userText);

        JButton saveBtn = new JButton("Save Group");
        panel.add(saveBtn);
        saveBtn.setBounds(10,20,150,100);
        saveBtn.addActionListener(e-> {
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

        saveBtn.setBackground(new Color(0xadd8e6));

        JButton addUserBtn = new JButton("Add");
        addUserBtn.setBounds(450,50,90,20);
        addUserBtn.setFont(new Font("Arial", Font.PLAIN, 10));
        addUserBtn.addActionListener(e->{
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

        rmvUserBtn = new JButton("Remove user");
        rmvUserBtn.setBounds(450,100,90,20);
        rmvUserBtn.setFont(new Font("Arial", Font.PLAIN, 10));
        rmvUserBtn.addActionListener(e->{
            //something to verify user is in group and then displays that user being removed successfully
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


        panel.add(addUserBtn);
        panel.add(rmvUserBtn);

        groupFrame.add(panel);
        groupFrame.setVisible(true);
    }

    public static void main(String[] args){

        new CreateGroupFrame();

    }
}

