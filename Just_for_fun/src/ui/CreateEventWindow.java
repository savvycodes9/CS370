package ui;

import controller.EventController;
import model.Event;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class CreateEventWindow {

    private JFrame frame;
    private JTextField titleField;
    private JTextField descField;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JTextField locationField;
    private JCheckBox privateCheck;
    private JButton saveButton;

    private LocalDate selectedDate;
    private User currentUser;
    private EventController eventController;

    public CreateEventWindow(LocalDate date, User user) {
        this.selectedDate = date;
        this.currentUser = user;
        this.eventController = new EventController();

        initUI();
    }

    private void initUI() {
        frame = new JFrame("Create Event - " + selectedDate);
        frame.setSize(350, 300);
        frame.setLayout(new GridLayout(8, 2));
        frame.setLocationRelativeTo(null);

        frame.add(new JLabel("Title:"));
        titleField = new JTextField();
        frame.add(titleField);

        frame.add(new JLabel("Description:"));
        descField = new JTextField();
        frame.add(descField);

        frame.add(new JLabel("Start Time (HH:MM):"));
        startTimeField = new JTextField("09:00");
        frame.add(startTimeField);

        frame.add(new JLabel("End Time (HH:MM):"));
        endTimeField = new JTextField("10:00");
        frame.add(endTimeField);

        frame.add(new JLabel("Location:"));
        locationField = new JTextField();
        frame.add(locationField);

        privateCheck = new JCheckBox("Private Event");
        frame.add(privateCheck);

        saveButton = new JButton("Save Event");
        frame.add(saveButton);

        saveButton.addActionListener(e -> saveEvent());

        frame.setVisible(true);
    }

    private void saveEvent() {
        try {
            String title = titleField.getText();
            String desc = descField.getText();
            LocalTime start = LocalTime.parse(startTimeField.getText());
            LocalTime end = LocalTime.parse(endTimeField.getText());
            String location = locationField.getText();
            boolean isPrivate = privateCheck.isSelected();

            boolean success = eventController.createEvent(
                    title, desc, selectedDate,
                    start, end, location,
                    null, // OPTIONAL groupId
                    currentUser.getUserId(),
                    isPrivate
            );

            if (success) {
                JOptionPane.showMessageDialog(frame, "Event created!");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Error creating event.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input: " + ex.getMessage());
        }
    }
}
