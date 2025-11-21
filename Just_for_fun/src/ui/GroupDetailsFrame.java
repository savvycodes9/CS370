package ui;
import javax.swing.*;
import java.awt.*;
import javax.swing.JButton;

public class GroupDetailsFrame {

    private JFrame groupFrame;
    private JLabel groupTitleLabel;
    private JLabel addUserLabel;
    private JTextField titleText;
    private JTextField userText;
    private JButton saveBtn;
    private JButton addUserBtn;
    private JButton rmvUserBtn;
    private JPanel userList;


    public GroupDetailsFrame(){
        init();
        requestForm();
    }

    private void init(){
        groupFrame = new JFrame("Create Group");
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
        groupTitleLabel.setBounds(150,20,200,25);
        panel.add(groupTitleLabel);

        titleText = new JTextField();
        titleText.setBounds(230,20,130,25);
        panel.add(titleText);

        addUserLabel = new JLabel("Add User: ");
        addUserLabel.setBounds(150,50,200,25);
        panel.add(addUserLabel);

        userText = new JTextField();
        userText.setBounds(230,50,130,25);
        panel.add(userText);

        JButton saveBtn = new JButton("Save and Close");
        panel.add(saveBtn);
        saveBtn.setBounds(10,20,150,100);

        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setContentAreaFilled(true);
        saveBtn.setOpaque(true);
        saveBtn.setBackground(new Color(0xadd8e6));
        saveBtn.setForeground(Color.BLACK);

        JButton addUserBtn = new JButton("Add");
        addUserBtn.setBounds(389,50,90,20);
        addUserBtn.setFont(new Font("Arial", Font.PLAIN, 10));

        rmvUserBtn = new JButton("Remove user");
        rmvUserBtn.setBounds(389,100,90,20);
        rmvUserBtn.setFont(new Font("Arial", Font.PLAIN, 10));

        panel.add(saveBtn);
        panel.add(addUserBtn);
        panel.add(rmvUserBtn);

        groupFrame.add(panel);
        groupFrame.setVisible(true);
    }

    public static void main(String[] args){

        new GroupDetailsFrame();

    }
}
