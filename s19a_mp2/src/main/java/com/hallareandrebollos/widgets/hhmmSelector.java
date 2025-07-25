package com.hallareandrebollos.widgets;

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

public class hhmmSelector extends JPanel {

    private JTextField hourField;
    private JTextField minuteField;
    private JButton hourButton;
    private JButton minuteButton;

    // Empty selector constructor
    public hhmmSelector() {
        this(0, 0);
    }

    // Constructor with initial hour and minute
    // For editing existing entries
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

    private Integer[] getHourChoices() {
        Integer[] hours = new Integer[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = i;
        }
        return hours;
    }

    private Integer[] getMinuteChoices() {
        Integer[] minutes = new Integer[60];
        for (int i = 0; i < 60; i++) {
            minutes[i] = i;
        }
        return minutes;
    }

    // Returns the selected time as a LocalTime object (seconds are zero)
    public LocalTime getSelectedTime() {
        int hour = parseField(this.hourField, 0, 23);
        int minute = parseField(this.minuteField, 0, 59);
        return LocalTime.of(hour, minute, 0);
    }

    // Add missing setter for EntryForm clearing
    public void setSelectedTime(LocalTime time) {
        if (time != null) {
            this.hourField.setText(String.format("%02d", time.getHour()));
            this.minuteField.setText(String.format("%02d", time.getMinute()));
        }
    }

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
