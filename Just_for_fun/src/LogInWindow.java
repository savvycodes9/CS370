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
    private JButton button;

    public LogInWindow() {
        initateLoginUI();
        authActions();
    }

    private void initateLoginUI() {
        frame = new JFrame("Fast Track");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        button = new JButton("Log In");
        button.setBounds(140, 100, 80, 25);
        panel.add(button);

        success = new JLabel("");
        success.setBounds(10, 130, 300, 25);
        panel.add(success);

        frame.setVisible(true);
    }

    private void authActions() {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = passText.getText();

                Map<String, String[]> users = Database.loadUsers();

                if (Database.login(username, password, users)) {
                    success.setText("Log In Successful");
                    frame.dispose();
                    new MainWindow(username);
                } else {
                    success.setText("Invalid username or password");
                }
            }
        });
    }
}