import javax.swing.*;
import java.awt.*;
import java.time.*;



public class CalendarApp extends JFrame {
    private JLabel monthLabel;
    private JPanel calendarPanel;
    private YearMonth currentMonth;

    public CalendarApp() {
        setTitle("Calendar Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Start with current month
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

        add(headerPanel, BorderLayout.NORTH);

        // ===== CENTER: Calendar grid =====
        calendarPanel = new JPanel(new GridLayout(0, 7));
        add(calendarPanel, BorderLayout.CENTER);

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
        int daysInMonth = currentMonth.lengthOfMonth();
        int startDay = firstDay.getDayOfWeek().getValue() % 7; // Sunday=0

        // Empty slots before first day
        for (int i = 0; i < startDay; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // Fill in days
        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            calendarPanel.add(dayButton);
            Object[] user_options = {"Create Public Event", "Create Private Event", "Create Group Study"};

            // Example: action when clicking a day
            dayButton.addActionListener(e -> {
                //JOptionPane.showMessageDialog(this, "You clicked " );//+ dayButton.getText());
                int choice = JOptionPane.showOptionDialog(this, "Create New Event", "New Event", JOptionPane.YES_NO_OPTION, 1, dayButton.getIcon(), user_options , 2);
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

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new CalendarApp().setVisible(true);
        });
    }
}
