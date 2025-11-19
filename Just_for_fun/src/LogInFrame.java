import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class LogInFrame {
    private JFrame frame;
    private JPanel sidePanel;
    private JPanel loginPanel;
    private JTextField userText;
    private JPasswordField passText;
    private JLabel success;
    private JButton loginbtn;
    private JButton createAccbtn;

    public LogInFrame() {
        initateLoginUI();
        authActions();
    }

    private void initateLoginUI() {
        frame = new JFrame("Fast Track");
        frame.setSize(650, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        sidePanel = new JPanel(null);
        sidePanel.setBackground(new Color(0X3C3C3C));
        sidePanel.setBounds(0,0,250,700);

        JLabel appName1 = new JLabel("Fast");
        appName1.setBounds(45, 40, 200, 35);
        Font appFont1 = new Font("Monospaced", Font.PLAIN, 40);
        appName1.setForeground(new Color(0xf9ebcd));
        appName1.setFont(appFont1);
        sidePanel.add(appName1);

        JLabel appName2 = new JLabel("Track");
        appName2.setBounds(65, 75, 200, 35);
        Font appFont = new Font("Monospaced", Font.PLAIN, 40);
        appName2.setForeground(new Color(0xf9ebcd));
        appName2.setFont(appFont);
        sidePanel.add(appName2);

        ImageIcon trainIcon = new ImageIcon("train.png");

        Image img = trainIcon.getImage().getScaledInstance(250, 700, Image.SCALE_SMOOTH);
        trainIcon = new ImageIcon(img);

        JLabel trainLabel = new JLabel(trainIcon);
        trainLabel.setBounds(0, 0, 250, 700);
        sidePanel.add(trainLabel);

        frame.add(sidePanel);

        loginPanel = new JPanel(null);
        loginPanel.setBackground(new Color(0X1A1A1A));
        loginPanel.setBounds(250,0,400,700);
        frame.add(loginPanel);

        JLabel loginText = new JLabel("Member Log In");
        loginText.setBounds(115, 150, 250, 35);
        Font font = new Font("arial", Font.PLAIN, 25);
        loginText.setForeground(new Color(0x6a6a6a));
        loginText.setFont(font);
        loginPanel.add(loginText);

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(70, 195, 80, 25);
        userLabel.setForeground(new Color(0x6a6a6a)); //sets text color
        loginPanel.add(userLabel);
        userText = new JTextField(20);
        userText.setBounds(70, 220, 250, 25);
        userText.setBackground(new Color(0x3C3C3C)); //dark gray background
        userText.setForeground(Color.WHITE);         //white text
        userText.setCaretColor(Color.WHITE);         //white cursor
        userText.setBorder(BorderFactory.createEmptyBorder()); //removes white border
        loginPanel.add(userText);

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(70, 255, 80, 25);
        passLabel.setForeground(new Color(0x6a6a6a));
        loginPanel.add(passLabel);
        passText = new JPasswordField();
        passText.setBounds(70, 280, 250, 25);
        passText.setBackground(new Color(0x3C3C3C));
        passText.setForeground(Color.WHITE);
        passText.setBorder(BorderFactory.createEmptyBorder());
        loginPanel.add(passText);

        JCheckBox rememberMe = new JCheckBox("Remember me");
        rememberMe.setBounds(70, 305, 130, 30);
        rememberMe.setForeground(new Color(0x6a6a6a));
        rememberMe.setBackground(new Color(0x1A1A1A));
        rememberMe.setOpaque(true);
        rememberMe.setBorderPainted(false);
        rememberMe.setFocusPainted(false);
        rememberMe.setContentAreaFilled(false);
        loginPanel.add(rememberMe);

        JButton forgotPassword = new JButton("Forgot Password?");
        forgotPassword.setBounds(189, 309, 140, 25);
        forgotPassword.setForeground(new Color(0x6a6a6a));
        forgotPassword.setBackground(new Color(0x1A1A1A));
        forgotPassword.setBorderPainted(false);
        forgotPassword.setFocusPainted(false);
        forgotPassword.setContentAreaFilled(false);
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR)); //hand cursor on hover
        loginPanel.add(forgotPassword);

        loginbtn = new JButton("Log In");
        loginbtn.setBounds(70, 350, 250, 25);
        loginbtn.setFocusPainted(false);
        loginbtn.setBorderPainted(false);
        loginbtn.setContentAreaFilled(true);
        loginbtn.setOpaque(true);
        loginbtn.setBackground(new Color(0xde631e));
        loginPanel.add(loginbtn);

        createAccbtn = new JButton("Do not have an account? Register");
        createAccbtn.setBounds(70, 390, 250, 25);
        createAccbtn.setFocusPainted(false);
        createAccbtn.setBorderPainted(false);
        createAccbtn.setContentAreaFilled(true);
        createAccbtn.setOpaque(true);
        createAccbtn.setBackground(new Color(0x2a2a2a));
        createAccbtn.setForeground(new Color(0x6a6a6a));
        loginPanel.add(createAccbtn);

        success = new JLabel("");
        success.setBounds(450, 390, 300, 25);
        success.setForeground(Color.white);
        loginPanel.add(success);

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
                    new CalendarFrame(username);
                } else {
                    success.setText("Invalid username or password");
                }
            }
        });
    }
}