import javax.swing.*;
import java.awt.*;

public class EventDetailsFrame{

    private BaseFrame eventFrame;
    private JLabel eventTitle;
    private JTextField eventTitleText;
    private JLabel dayLabel;
    private JComboBox<String> dayDropdown;
    private JLabel startTimeLabel;
    private JComboBox<String> startTimeDropdown;
    private JLabel endTimeLabel;
    private JComboBox<String> endTimeDropdown;
    private JLabel recurringLabel;
    private JComboBox<String> recurringDropdown;
    private JButton saveBtn;

    public EventDetailsFrame() {
        init();
        eventForm();
        eventFrame.showFrame();
    }

    private void init() {
        eventFrame = new BaseFrame("Untitled - Event");
        eventFrame.setCloseOperation(JFrame.DISPOSE_ON_CLOSE); // override default
        eventFrame.getFrame().setResizable(false);
    }

    private void eventForm() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        eventTitle = new JLabel("Event Title:");
        eventTitle.setBounds(180, 30, 100, 25);
        panel.add(eventTitle);

        eventTitleText = new JTextField();
        eventTitleText.setBounds(280, 30, 180, 25);
        panel.add(eventTitleText);

        dayLabel = new JLabel("Day:");
        dayLabel.setBounds(180, 70, 100, 25);
        panel.add(dayLabel);

        startTimeLabel = new JLabel("Start Time:");
        startTimeLabel.setBounds(180, 110, 100, 25);
        panel.add(startTimeLabel);

        String[] times = generateTimes();
        startTimeDropdown = new JComboBox<>(times);
        startTimeDropdown.setBounds(280, 110, 180, 25);
        panel.add(startTimeDropdown);

        endTimeLabel = new JLabel("End Time:");
        endTimeLabel.setBounds(180, 150, 100, 25);
        panel.add(endTimeLabel);

        endTimeDropdown = new JComboBox<>(times);
        endTimeDropdown.setBounds(280, 150, 180, 25);
        panel.add(endTimeDropdown);

        recurringLabel = new JLabel("Recurring:");
        recurringLabel.setBounds(180, 190, 100, 25);
        panel.add(recurringLabel);

        String[] recurrenceOptions = {"Weekly", "Monthly", "Yearly"};
        recurringDropdown = new JComboBox<>(recurrenceOptions);
        recurringDropdown.setBounds(280, 190, 180, 25);
        panel.add(recurringDropdown);

        saveBtn = new JButton("Save and Close");
        saveBtn.setBounds(20, 30, 135, 100);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setOpaque(true);
        saveBtn.setBackground(new Color(0xADD8E6));
        saveBtn.setForeground(Color.BLACK);
        panel.add(saveBtn);

        eventFrame.add(panel, BorderLayout.CENTER);
    }

    private String[] generateTimes() {
        String[] times = new String[48];
        int index = 0;
        for (int hour = 0; hour < 24; hour++) {
            for (int min = 0; min < 60; min += 30) {
                String ampm = (hour < 12) ? "AM" : "PM";
                int displayHour = (hour % 12 == 0) ? 12 : (hour % 12);
                String minuteStr = (min == 0) ? "00" : "30";
                times[index++] = String.format("%d:%s %s", displayHour, minuteStr, ampm);
            }
        }
        return times;
    }

    public static void main(String[] args) {
        new EventDetailsFrame();
    }
}