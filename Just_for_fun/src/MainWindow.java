import javax.swing.*;
import java.awt.*;
import java.time.*;

public class MainWindow{

    private JPanel sidebar;
    private JFrame frame;
    private JLabel monthLabel;
    private JPanel calendarPanel;
    private YearMonth currentMonth;

    public MainWindow(String username){
        init();
        calendarDisplay();
        updateCalendar();
        sidebarDisplay();
    }

    private void init(){
        frame = new JFrame("Fast Track");

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(600,400);
        this.frame.setLocationRelativeTo(null);
        this.frame.setLayout(new BorderLayout());
        this.frame.setVisible(true);
    }
    private void sidebarDisplay(){
        //sidebar basics, title and create event button
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200,0));
        JLabel title = new JLabel("User Options", JLabel.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(title);

        JButton menuButton = new JButton("Create Event");
        JPopupMenu menu = new JPopupMenu();

        JButton addevent = new JButton("create event");
        sidebar.add(menuButton);
        frame.add(sidebar, BorderLayout.WEST);
    }
    private void calendarDisplay(){

        currentMonth = YearMonth.now();
        // ===== TOP: Header with buttons =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");

        monthLabel = new JLabel("", JLabel.CENTER);
        monthLabel.setFont(new Font("Times", Font.BOLD, 20));

        headerPanel.add(prevButton, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);

        frame.add(headerPanel, BorderLayout.NORTH);

        // ===== CENTER: Calendar grid =====
        calendarPanel = new JPanel(new GridLayout(0, 7));
        frame.add(calendarPanel, BorderLayout.CENTER);

        // Button actions
        prevButton.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar();
        });
        nextButton.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar();
        });

        // Draw the initial calendar
        updateCalendar();
    }

    private void updateCalendar() {
        calendarPanel.removeAll();

        // Show month name
        monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());

        // Day names row
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            calendarPanel.add(new JLabel(day, JLabel.CENTER));
        }

        // Figure out the start day and number of days
        LocalDate firstDay = currentMonth.atDay(1);
        LocalDate todayDate = LocalDate.now();
        int today = todayDate.getDayOfMonth();
        int daysInMonth = currentMonth.lengthOfMonth();
        int startDay = firstDay.getDayOfWeek().getValue() % 7; // Sunday=0


        // Empty slots before first day
        for (int i = 0; i < startDay; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // Fill in days
        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setFocusPainted(false); //removes the dotted rectangle
            dayButton.setBorderPainted(true); //draws border
            dayButton.setBorder(BorderFactory.createLineBorder(Color.gray)); //sets border to a colored line
            dayButton.setContentAreaFilled(true); //interior of the button is filled
            dayButton.setOpaque(true); //button is non-transparent
            dayButton.setBackground(Color.white); //sets button background
            dayButton.setMargin(new Insets(4,6,4,6)); //padding

            if(day == today && currentMonth.equals(YearMonth.from(todayDate))){
                dayButton.setBackground(new Color(0xadd8e6));
            }

            calendarPanel.add(dayButton);
            Object[] user_options = {"Create Public Event", "Create Private Event", "Create Group Study"};

            // Example: action when clicking a day
            dayButton.addActionListener(e -> {
                //JOptionPane.showMessageDialog(frame, "You clicked " );//+ dayButton.getText());
                int choice = JOptionPane.showOptionDialog(frame, "Create New Event", "New Event", JOptionPane.YES_NO_OPTION, 1, dayButton.getIcon(), user_options , 2);
                if(choice == 0){
                    System.out.println("User selected create public event");
                } else if( choice ==1){
                    System.out.println("user selected create private event");
                } else if (choice ==2){
                    System.out.println("user selected create group study");
                } else {
                    System.out.println("Invalid choice");
                }
            });
        }

        // Refresh
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }
}