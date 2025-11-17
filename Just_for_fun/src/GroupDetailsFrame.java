import javax.swing.*;
import java.awt.*;
import javax.swing.JButton;

public class GroupDetailsFrame {

    private BaseFrame groupFrame;
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
        groupFrame.showFrame();
    }

    private void init(){
        groupFrame = new BaseFrame("Create Group");
        groupFrame.setCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        groupFrame.getFrame().setResizable(false);
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

        saveBtn = new JButton("Save and Close");
        saveBtn.setBounds(10,20,135,100);

        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setContentAreaFilled(true);
        saveBtn.setOpaque(true);
        saveBtn.setBackground(new Color(0xadd8e6));
        saveBtn.setForeground(Color.BLACK);

        addUserBtn = new JButton("Add");
        addUserBtn.setBounds(389,50,60,20);
        addUserBtn.setFont(new Font("Arial", Font.PLAIN, 10));

        rmvUserBtn = new JButton("-");
        rmvUserBtn.setBounds(389,100,60,20);
        rmvUserBtn.setFont(new Font("Arial", Font.PLAIN, 10));

        panel.add(saveBtn);
        panel.add(addUserBtn);
        panel.add(rmvUserBtn);

        groupFrame.add(panel,BorderLayout.CENTER);
    }

    public static void main(String[] args){
        new GroupDetailsFrame();
    }
}
