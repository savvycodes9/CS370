package ui;

import controller.EventController;
import model.Event;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class EditEventWindow {

    private JFrame frame;
    private JTextField titleField;
    private JTextField descField;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JTextField locationField;
    private JCheckBox privateCheck;

    private JButton saveButton;
    private JButton cancelButton;

    private Event event;
    private EventController eventController;

    public EditEventWindow(Event event, EventController eventController) {
        this.event = event;
        this.eventController = eventController;

        initUI();
    }

    private void initUI() {
        frame = new JFrame("Edit Event - " + event.getDate());
        frame.setSize(350, 320);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(8, 2));

        // --- Title ---
        frame.add(new JLabel("Title:"));
        titleField = new JTextField(event.getTitle());
        frame.add(titleField);

        // --- Description ---
        frame.add(new JLabel("Description:"));
        descField = new JTextField(event.getDescription());
        frame.add(descField);

        // --- Start time ---
        frame.add(new JLabel("Start Time (HH:MM):"));
        startTimeField = new JTextField(event.getStartTime().toString());
        frame.add(startTimeField);

        // --- End time ---
        frame.add(new JLabel("End Time (HH:MM):"));
        endTimeField = new JTextField(event.getEndTime().toString());
        frame.add(endTimeField);

        // --- Location ---
        frame.add(new JLabel("Location:"));
        locationField = new JTextField(event.getLocation());
        frame.add(locationField);

        // --- Private toggle ---
        privateCheck = new JCheckBox("Private Event");
        privateCheck.setSelected(event.isPrivate());
        frame.add(privateCheck);

        // Empty placeholder to keep grid alignment clean
        frame.add(new JLabel(""));

        // --- Buttons ---
        saveButton = new JButton("Save Changes");
        cancelButton = new JButton("Cancel");

        frame.add(saveButton);
        frame.add(cancelButton);

        saveButton.addActionListener(e -> saveChanges());
        cancelButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }

    private void saveChanges() {
        try {
            String newTitle = titleField.getText();
            String newDesc = descField.getText();
            LocalTime newStart = LocalTime.parse(startTimeField.getText());
            LocalTime newEnd = LocalTime.parse(endTimeField.getText());
            String newLoc = locationField.getText();
            boolean isPrivate = privateCheck.isSelected();
            LocalDate date = event.getDate(); // date doesn't change here

            boolean success = eventController.updateEvent(
                    event.getEventId(),
                    newTitle,
                    newDesc,
                    date,
                    newStart,
                    newEnd,
                    newLoc,
                    isPrivate
            );

            if (success) {
                JOptionPane.showMessageDialog(frame, "Event updated!");
                frame.dispose();

                // Re-open the view window to reflect changes
                new ViewEventsWindow(
                        event.getDate(),
                        eventController.getAllEvents()
                                .stream()
                                .filter(ev -> ev.getDate().equals(event.getDate()))
                                .toList(),
                        null, // not needed here because view window does not use user for editing
                        eventController
                );
            } else {
                JOptionPane.showMessageDialog(frame, "Error updating event.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input: " + ex.getMessage());
        }
    }
}
