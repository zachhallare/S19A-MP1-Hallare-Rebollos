package mco2_final;

import java.awt.FlowLayout;
import java.time.LocalTime;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;


/**
 * A custom Swing widget that allows users to select or input time in hour and minute format.
 * Uses buttons that display prompt dialogs to select hour and minute from lists.
 */
public class hhmmSelector extends JPanel {

    /** Text field for entering the hour. */
    private JTextField hourField;

    /** Text field for entering the minute. */
    private JTextField minuteField;

    /** Button to open the hour selection prompt. */
    private JButton hourButton;

    /** Button to open the minute selection prompt. */
    private JButton minuteButton;


    /**
     * Constructs a time selector with default time set to 00:00.
     */
    public hhmmSelector() {
        this(0, 0);
    }

    
    /**
     * Constructs a time selector with specified initial hour and minute.
     * @param hour   Initial hour (0–23)
     * @param minute Initial minute (0–59)
     */
    public hhmmSelector(int hour, int minute) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

        this.hourField = new JTextField(String.format("%02d", hour), 2);
        this.minuteField = new JTextField(String.format("%02d", minute), 2);

        this.hourButton = new JButton("...");
        this.minuteButton = new JButton("...");

        add(new JLabel("Hour:"));
        add(this.hourField);
        add(this.hourButton);

        add(new JLabel("Minute:"));
        add(this.minuteField);
        add(this.minuteButton);

        this.hourButton.addActionListener(e -> showHourPrompt());
        this.minuteButton.addActionListener(e -> showMinutePrompt());
    }


    /**
     * Displays a prompt for the user to select an hour value from a list.
     */
    private void showHourPrompt() {
        JList<Integer> hourList = new JList<>(getHourChoices());
        hourList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(hourList);

        int result = JOptionPane.showConfirmDialog(
                this, scrollPane, "Select Hour", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION && hourList.getSelectedValue() != null) {
            hourField.setText(String.format("%02d", hourList.getSelectedValue()));
        }
    }


    /**
     * Displays a prompt for the user to select a minute value from a list.
     */
    private void showMinutePrompt() {
        JList<Integer> minuteList = new JList<>(getMinuteChoices());
        minuteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(minuteList);

        int result = JOptionPane.showConfirmDialog(
                this, scrollPane, "Select Minute", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION && minuteList.getSelectedValue() != null) {
            minuteField.setText(String.format("%02d", minuteList.getSelectedValue()));
        }
    }


    /**
     * Returns the array of possible hour values (0–23).
     * @return An array of integers representing valid hours.
     */
    private Integer[] getHourChoices() {
        Integer[] hours = new Integer[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = i;
        }
        return hours;
    }


    /**
     * Returns the array of possible minute values (0–59).
     * @return An array of integers representing valid minutes.
     */
    private Integer[] getMinuteChoices() {
        Integer[] minutes = new Integer[60];
        for (int i = 0; i < 60; i++) {
            minutes[i] = i;
        }
        return minutes;
    }


    /**
     * Returns the selected time as a LocalTime object.
     * @return The selected time, defaulting to 00:00 if inputs are invalid.
     */
    public LocalTime getSelectedTime() {
        int hour = parseField(this.hourField, 0, 23);
        int minute = parseField(this.minuteField, 0, 59);
        return LocalTime.of(hour, minute, 0);
    }


    /**
     * Sets the selector to the given LocalTime value.
     * @param time The time to set. If null, no change is made.
     */
    public void setSelectedTime(LocalTime time) {
        if (time != null) {
            this.hourField.setText(String.format("%02d", time.getHour()));
            this.minuteField.setText(String.format("%02d", time.getMinute()));
        }
    }


    /**
     * Parses and validates a numeric value from the given text field.
     * If invalid, shows a dialog and returns the minimum allowed value.
     * @param field The text field to parse.
     * @param min   Minimum allowed value.
     * @param max   Maximum allowed value.
     * @return Parsed integer between min and max, or min if invalid.
     */
    private int parseField(JTextField field, int min, int max) {
        try {
            int value = Integer.parseInt(field.getText());
            if (value < min || value > max) {
                JOptionPane.showMessageDialog(this, "Invalid input. Using default value: " + min);
                return min;
            }
            return value;
        } catch (NumberFormatException e) {
            // If parsing fails, return the minimum value
            // and display a dialog message
            JOptionPane.showMessageDialog(this, "Invalid input. Using default value: " + min);
            return min;
        }
    }
}
