
package com.hallareandrebollos.widgets;

import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.YearMonth;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

    public class yyyymmddSelector extends JPanel {
        private JTextField yearField, monthField, dayField;
        private JButton yearBtn, monthBtn, dayBtn;

        // Empty selector constructor
        public yyyymmddSelector() {
            this(LocalDate.now());
            clearFields();
        }

        // Constructor with initial date
        // For editing existing entries
        public yyyymmddSelector(LocalDate date) {
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

            this.yearField = new JTextField(4);
            this.monthField = new JTextField(2);
            this.dayField = new JTextField(2);

            this.yearBtn = new JButton("...");
            this.monthBtn = new JButton("...");
            this.dayBtn = new JButton("...");

            add(new JLabel("Year:"));
            add(this.yearField);
            add(this.yearBtn);

            add(new JLabel("Month:"));
            add(this.monthField);
            add(this.monthBtn);

            add(new JLabel("Day:"));
            add(this.dayField);
            add(this.dayBtn);

            setDate(date);

            this.yearBtn.addActionListener(e -> showYearDialog());
            this.monthBtn.addActionListener(e -> showMonthDialog());
            this.dayBtn.addActionListener(e -> showDayDialog());
        }

        private void clearFields() {
            this.yearField.setText("");
            this.monthField.setText("");
            this.dayField.setText("");
        }

        private void setDate(LocalDate date) {
            this.yearField.setText(String.valueOf(date.getYear()));
            this.monthField.setText(String.format("%02d", date.getMonthValue()));
            this.dayField.setText(String.format("%02d", date.getDayOfMonth()));
        }

        private void showYearDialog() {
            int currentYear = LocalDate.now().getYear();
            DefaultListModel<Integer> model = new DefaultListModel<>();
            for (int y = currentYear - 100; y <= currentYear + 10; y++) {
                model.addElement(y);
            }
            JList<Integer> list = new JList<>(model);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            int result = JOptionPane.showConfirmDialog(
                this, new JScrollPane(list), "Select Year", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION && list.getSelectedValue() != null) {
                this.yearField.setText(String.valueOf(list.getSelectedValue()));
                updateDayFieldRange();
            }
        }

        private void showMonthDialog() {
            DefaultListModel<Integer> model = new DefaultListModel<>();
            for (int m = 1; m <= 12; m++) {
                model.addElement(m);
            }
            JList<Integer> list = new JList<>(model);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            int result = JOptionPane.showConfirmDialog(
                this, new JScrollPane(list), "Select Month", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION && list.getSelectedValue() != null) {
                monthField.setText(String.format("%02d", list.getSelectedValue()));
                updateDayFieldRange();
            }
        }

        private void showDayDialog() {
            int year = getInt(yearField.getText(), LocalDate.now().getYear());
            int month = getInt(monthField.getText(), LocalDate.now().getMonthValue());
            int maxDay = YearMonth.of(year, month).lengthOfMonth();

            DefaultListModel<Integer> model = new DefaultListModel<>();
            for (int d = 1; d <= maxDay; d++) {
                model.addElement(d);
            }
            JList<Integer> list = new JList<>(model);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            int result = JOptionPane.showConfirmDialog(
                this, new JScrollPane(list), "Select Day", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION && list.getSelectedValue() != null) {
                dayField.setText(String.format("%02d", list.getSelectedValue()));
            }
        }

        private void updateDayFieldRange() {
            int year = getInt(yearField.getText(), LocalDate.now().getYear());
            int month = getInt(monthField.getText(), LocalDate.now().getMonthValue());
            int maxDay = YearMonth.of(year, month).lengthOfMonth();
            int day = getInt(dayField.getText(), 1);
            if (day > maxDay) {
                dayField.setText(String.format("%02d", maxDay));
            }
        }

        private int getInt(String text, int defaultValue) {
            try {
                return Integer.parseInt(text.trim());
            } catch (NumberFormatException e) {
                // If parsing fails, return the default value
                // and outputs a dialog message
                JOptionPane.showMessageDialog(this, "Invalid input. Using default value: " + defaultValue, "Error", JOptionPane.ERROR_MESSAGE);
                return defaultValue;
            }
        }

        public LocalDate getSelectedDate() {
            int year = getInt(this.yearField.getText(), LocalDate.now().getYear());
            int month = getInt(this.monthField.getText(), LocalDate.now().getMonthValue());
            int day = getInt(this.dayField.getText(), 1);
            int maxDay = YearMonth.of(year, month).lengthOfMonth();
            if (day > maxDay) day = maxDay;
            return LocalDate.of(year, month, day);
        }

        // Add missing setter for EntryForm clearing
        public void setSelectedDate(LocalDate date) {
            setDate(date);
        }
    }
