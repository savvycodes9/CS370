package ui;

import controller.EventController;
import model.Event;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class CalendarFrame {

    private JFrame frame;
    private JPanel calendarPanel;
    private JLabel monthLabel;

    private YearMonth currentMonth;
    private User currentUser;

    private EventController eventController;

    public CalendarFrame(User currentUser) {
        this.currentUser = currentUser;
        this.eventController = new EventController();

        init();
        calendarDisplay();
        updateCalendar();
        sidebarDisplay();
    }

    private void init() {
        frame = new JFrame("Fast Track");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(600, 400);
        this.frame.setLocationRelativeTo(null);
        this.frame.setLayout(new BorderLayout());
        this.frame.setVisible(true);
    }

    private void sidebarDisplay() {
        final int sidebarWidth = 180;

        JPanel sidebarContainer = new JPanel(new BorderLayout());
        sidebarContainer.setPreferredSize(new Dimension(sidebarWidth, frame.getHeight()));
        frame.add(sidebarContainer, BorderLayout.WEST);

        JButton toggleButton = new JButton("☰ Menu");
        sidebarContainer.add(toggleButton, BorderLayout.NORTH);

        JPanel sidebarContent = new JPanel();
        sidebarContent.setLayout(new BoxLayout(sidebarContent, BoxLayout.Y_AXIS));
        sidebarContent.setBackground(new Color(245, 245, 245));
        sidebarContent.setVisible(false);

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBorder(BorderFactory.createTitledBorder("My Groups"));
        topSection.add(new JButton("Study Group A"));
        topSection.add(new JButton("Study Group B"));
        sidebarContent.add(topSection);

        sidebarContent.add(Box.createVerticalGlue());

        JPanel middleSection = new JPanel();
        middleSection.setLayout(new BoxLayout(middleSection, BoxLayout.Y_AXIS));
        middleSection.setBorder(BorderFactory.createTitledBorder("Reminders"));
        middleSection.add(new JLabel("Assignment due tomorrow"));
        middleSection.add(new JLabel("Team meeting at 5 PM"));
        sidebarContent.add(middleSection);

        sidebarContent.add(Box.createVerticalGlue());

        JPanel bottomSection = new JPanel();
        bottomSection.setLayout(new BoxLayout(bottomSection, BoxLayout.Y_AXIS));
        bottomSection.setBorder(BorderFactory.createTitledBorder("Create Event"));
        bottomSection.add(new JButton("➕ New Event"));
        sidebarContent.add(bottomSection);

        sidebarContainer.add(sidebarContent, BorderLayout.CENTER);

        toggleButton.addActionListener(e -> {
            sidebarContent.setVisible(!sidebarContent.isVisible());
            frame.revalidate();
            frame.repaint();
        });
    }

    private void calendarDisplay() {
        currentMonth = YearMonth.now();

        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");

        monthLabel = new JLabel("", JLabel.CENTER);
        monthLabel.setFont(new Font("Times", Font.BOLD, 20));

        headerPanel.add(prevButton, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);

        frame.add(headerPanel, BorderLayout.NORTH);

        calendarPanel = new JPanel(new GridLayout(0, 7));
        frame.add(calendarPanel, BorderLayout.CENTER);

        prevButton.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar();
        });
        nextButton.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar();
        });

        updateCalendar();
    }

    private void updateCalendar() {
        calendarPanel.removeAll();

        monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            calendarPanel.add(new JLabel(day, JLabel.CENTER));
        }

        LocalDate firstDay = currentMonth.atDay(1);
        LocalDate todayDate = LocalDate.now();
        int today = todayDate.getDayOfMonth();
        int daysInMonth = currentMonth.lengthOfMonth();
        int startDay = firstDay.getDayOfWeek().getValue() % 7;

        for (int i = 0; i < startDay; i++) {
            calendarPanel.add(new JLabel(""));
        }

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

            // --- EVENT CLICK HANDLING ---
            dayButton.addActionListener(e -> {

                int clickedDay = Integer.parseInt(dayButton.getText());
                LocalDate clickedDate = LocalDate.of(
                        currentMonth.getYear(),
                        currentMonth.getMonth(),
                        clickedDay
                );

                List<Event> events = eventController.getAllEvents()
                        .stream()
                        .filter(ev -> ev.getDate().equals(clickedDate))
                        .toList();

                // If there are events → show popup
                if (!events.isEmpty()) {
                    new ViewEventsWindow(clickedDate, events, currentUser, eventController);
                    return;
                }


                // Otherwise → normal create event choices
                Object[] user_options = {"Create Event"};
                int choice = JOptionPane.showOptionDialog(frame,
                        "Create New Event",
                        "New Event",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        user_options,
                        0);

                if (choice == 0 || choice == 1) {
                    boolean isPrivate = (choice == 1);
                    new CreateEventWindow(clickedDate, currentUser);

                } else if (choice == 2) {
                    System.out.println("User selected group event");
                }

            });

            calendarPanel.add(dayButton);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private void showEventsPopup(LocalDate date, List<Event> events) {
        StringBuilder msg = new StringBuilder("Events on " + date + ":\n\n");

        for (Event e : events) {
            msg.append("• ").append(e.getTitle())
                    .append(" (").append(e.getStartTime())
                    .append(" - ").append(e.getEndTime()).append(")\n")
                    .append("  Location: ").append(e.getLocation()).append("\n")
                    .append("  Private: ").append(e.isPrivate() ? "Yes" : "No")
                    .append("\n\n");
        }

        JOptionPane.showMessageDialog(frame, msg.toString());
    }
}
