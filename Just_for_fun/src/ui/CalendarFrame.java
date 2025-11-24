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
        sidebarContent.setVisible(true); // start open

        // --- Top section: Groups ---
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBorder(BorderFactory.createTitledBorder("My Groups"));

        JButton addgroupButton = new JButton("➕ Add Group");
        topSection.add(addgroupButton);
        addgroupButton.addActionListener(e -> new GroupDetailsFrame());

        topSection.add(new JButton("Study Group A"));
        topSection.add(new JButton("Study Group B"));
        sidebarContent.add(topSection);

        sidebarContent.add(Box.createVerticalGlue());

        // --- Middle section: Reminders ---
        JPanel middleSection = new JPanel();
        middleSection.setLayout(new BoxLayout(middleSection, BoxLayout.Y_AXIS));
        middleSection.setBorder(BorderFactory.createTitledBorder("Reminders"));
        middleSection.add(new JLabel("Assignment due tomorrow"));
        middleSection.add(new JLabel("Team meeting at 5 PM"));
        sidebarContent.add(middleSection);

        sidebarContent.add(Box.createVerticalGlue());

        // --- Bottom section: Create Event ---
        JPanel bottomSection = new JPanel();
        bottomSection.setLayout(new BoxLayout(bottomSection, BoxLayout.Y_AXIS));
        bottomSection.setBorder(BorderFactory.createTitledBorder("Create Event"));

        JButton event_creator = new JButton("➕ Create Event");
        bottomSection.add(event_creator);
        sidebarContent.add(bottomSection);

        // Create event from sidebar button
        event_creator.addActionListener(e -> new CreateEventWindow(this.currentUser, this));

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

    // Made public so CreateEventWindow can call this
    public void updateCalendar() {
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

        // Empty cells before the first day of the month
        for (int i = 0; i < startDay; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // Day buttons
        for (int day = 1; day <= daysInMonth; day++) {

            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setFocusPainted(false);
            dayButton.setBorderPainted(true);
            dayButton.setBorder(BorderFactory.createLineBorder(Color.gray));
            dayButton.setContentAreaFilled(true);
            dayButton.setOpaque(true);
            dayButton.setBackground(Color.white);
            dayButton.setMargin(new Insets(4, 6, 4, 6));

            LocalDate date = currentMonth.atDay(day);

            // Highlight today
            if (date.equals(todayDate)) {
                dayButton.setBackground(new Color(0xADD8E6));
            }

            // ---- Mark days that have events ----
            List<Event> eventsForDay = eventController.getAllEvents()
                    .stream()
                    .filter(ev -> ev.getDate().equals(date))
                    .toList();

            if (!eventsForDay.isEmpty()) {
                // change background so it's obvious
                dayButton.setBackground(new Color(198, 224, 255)); // light blue

                // show number of events in small text under the day number
                dayButton.setText("<html>" + day + "<br><span style='font-size:9px;'>"
                        + eventsForDay.size() + " event(s)</span></html>");

                // tooltip with the event titles
                StringBuilder tip = new StringBuilder("<html>");
                for (Event ev : eventsForDay) {
                    tip.append("• ").append(ev.getTitle()).append("<br>");
                }
                tip.append("</html>");
                dayButton.setToolTipText(tip.toString());
            }

            // store the exact date on the button so the listener can read it
            dayButton.putClientProperty("date", date);

            // --- EVENT CLICK HANDLING ---
            dayButton.addActionListener(e -> {
                LocalDate clickedDate = (LocalDate) dayButton.getClientProperty("date");

                List<Event> events = eventController.getAllEvents()
                        .stream()
                        .filter(ev -> ev.getDate().equals(clickedDate))
                        .toList();

                // If there are events → show popup / separate window
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
                    // isPrivate available if you decide to use it later
                    new CreateEventWindow(clickedDate, currentUser, this);
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
