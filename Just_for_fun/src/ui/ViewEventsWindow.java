package ui;

import controller.EventController;
import model.Event;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ViewEventsWindow {

    private JFrame frame;
    private LocalDate date;
    private List<Event> events;
    private User currentUser;
    private EventController eventController;

    public ViewEventsWindow(LocalDate date, List<Event> events, User currentUser, EventController eventController) {
        this.date = date;
        this.events = events;
        this.currentUser = currentUser;
        this.eventController = eventController;

        initUI();
    }

    private void initUI() {
        frame = new JFrame("Events on " + date);
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        for (Event e : events) {
            JPanel eventPanel = createEventCard(e);
            mainPanel.add(eventPanel);
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

    private JPanel createEventCard(Event e) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(360, 180));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.GRAY)
        ));

        JLabel title = new JLabel(e.getTitle());
        title.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel time = new JLabel("Time: " + e.getStartTime() + " - " + e.getEndTime());
        JLabel desc = new JLabel("Description: " + e.getDescription());
        JLabel loc = new JLabel("Location: " + e.getLocation());
        JLabel priv = new JLabel("Private: " + (e.isPrivate() ? "Yes" : "No"));

        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.add(editButton);
        buttonRow.add(deleteButton);

        // Add components
        panel.add(title);
        panel.add(time);
        panel.add(desc);
        panel.add(loc);
        panel.add(priv);
        panel.add(buttonRow);

        // Delete Event
        deleteButton.addActionListener(ev -> {
            int confirmed = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to delete this event?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmed == JOptionPane.YES_OPTION) {
                if (eventController.deleteEvent(e.getEventId())) {
                    JOptionPane.showMessageDialog(frame, "Event deleted.");
                    frame.dispose();
                    reloadWindow();
                } else {
                    JOptionPane.showMessageDialog(frame, "Error deleting event.");
                }
            }
        });

        // Edit Event
        editButton.addActionListener(ev -> {
            if (eventController.isUserAuthorizedToModifyEvent(currentUser.getUserId(), e.getEventId())) {
                new EditEventWindow(e, eventController);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "You do not have permission to edit this event.");
            }
        });

        return panel;
    }

    private void reloadWindow() {
        List<Event> updatedEvents = eventController.getAllEvents()
                .stream()
                .filter(ev -> ev.getDate().equals(date))
                .toList();

        new ViewEventsWindow(date, updatedEvents, currentUser, eventController);
    }
}
