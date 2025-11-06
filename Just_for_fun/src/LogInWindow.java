import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Map;

public class LogInWindow {
    private JFrame frame;
    private JTextField userText;
    private JPasswordField passText;
    private JLabel success;
    private JButton loginbtn;
    private JButton createAccbtn;

    public LogInWindow() {
        initateLoginUI();
        authActions();
    }

    private void initateLoginUI() {
        frame = new JFrame("Fast Track");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        frame.add(panel);

        JLabel userLabel = new JLabel("User");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);
        userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(10, 60, 80, 25);
        panel.add(passLabel);
        passText = new JPasswordField();
        passText.setBounds(100, 60, 165, 25);
        panel.add(passText);

        loginbtn = new JButton("Log In");
        loginbtn.setBounds(10, 100, 80, 25);
        panel.add(loginbtn);

        createAccbtn = new JButton("Create Account");
        createAccbtn.setBounds(110, 100, 150, 25);
        panel.add(createAccbtn);

        success = new JLabel("");
        success.setBounds(10, 130, 300, 25);
        panel.add(success);

        frame.setVisible(true);
    }

    private void authActions() {
        loginbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = passText.getText();

                Map<String, String[]> users = Database.loadUsers();

                if (Database.login(username, password, users)) {
                    frame.dispose();
                    new MainWindow(username);
                } else {
                    success.setText("Invalid username or password");
                }
            }
        });
    }
}