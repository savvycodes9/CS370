package ui;
import javax.swing.*;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.*;
import java.awt.*;

public class GroupEditFrame {


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


        public GroupEditFrame(){
            init();
            requestForm();
        }

        private void init(){
            groupFrame = new JFrame("Edit Group");
            this.groupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.groupFrame.setSize(600,400);
            this.groupFrame.setLocationRelativeTo(null);
            this.groupFrame.setLayout(new BorderLayout());
            this.groupFrame.setVisible(true);
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

            JButton syncBtn = new JButton("Make Schedule");
            syncBtn.setBounds(200,100,200,25);
            panel.add(syncBtn);
            syncBtn.addActionListener(e->{
                // syncing algorithm
            });

            JButton deletegroupBtn = new JButton("Delete Group");
            deletegroupBtn.setBounds(200,150,200,25);
            panel.add(deletegroupBtn);
            deletegroupBtn.addActionListener(e->{

                JFrame deleteGroupFrame = new JFrame("Delete Group");
                deleteGroupFrame.setSize(200,200);
                deleteGroupFrame.setLayout(new BorderLayout());
                deleteGroupFrame.setLocationRelativeTo(groupFrame);
                Object[] options = {"Yes","No"};
                int choice = JOptionPane.showOptionDialog(deleteGroupFrame,"Are you sure you want to delete this group?", "Choose", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if( choice == 0){
                    // user selected yes
                    // group needs to be removed from database
                    // and removed from sidebar
                    deleteGroupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }else if (choice ==1 ){

                        JOptionPane selectedNoPane = new JOptionPane(
                                "Your group has not been deleted",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        // Create a JDialog from the JOptionPane
                        JDialog dialog = selectedNoPane.createDialog(deleteGroupFrame, "Notice");

                        // Set up a timer to close it after 5 seconds (5000 ms)
                        Timer timer = new Timer(5000, ev -> dialog.dispose());
                        timer.setRepeats(false); // Only run once
                        timer.start();

                        dialog.setVisible(true); // Show the dialog



                    deleteGroupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }




                //delete from sidebar and the database
            });

            JButton saveBtn = new JButton("Save and Close");
            panel.add(saveBtn);
            saveBtn.setBounds(10,20,150,100);
            saveBtn.addActionListener(e->{
                // here is where people click save and then it saves to the database
            });

            saveBtn.setBackground(new Color(0xadd8e6));

            JButton addUserBtn = new JButton("Add");
            addUserBtn.setBounds(450,50,90,20);
            addUserBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            addUserBtn.addActionListener(e->{
                //something needs to be added for entering the username and linking it to database and then returning to the this frame and displaying the user name at the bottom

            });

            rmvUserBtn = new JButton("Remove user");
            rmvUserBtn.setBounds(450,100,90,20);
            rmvUserBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            rmvUserBtn.addActionListener(e->{
                //something to verify user is in group and then displays that user being removed successfully
            });

            panel.add(saveBtn);
            panel.add(addUserBtn);
            panel.add(rmvUserBtn);

            groupFrame.add(panel);
            groupFrame.setVisible(true);
        }

        public static void main(String[] args){

            new ui.GroupEditFrame();

        }
    }


