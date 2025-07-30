package mco2_final;

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

/**
 * A custom Swing component that allows users to select a date (year, month, and day)
 * via text fields and dialog-based selection. Useful for entry forms requiring date input.
 */
public class yyyymmddSelector extends JPanel {

    /** Field for entering/selecting the year */
    private JTextField yearField;
    
    /** Field for entering/selecting the month */
    private JTextField monthField;
    
    /** Field for entering/selecting the day */
    private JTextField dayField;

    /** Button to open year selection dialog */
    private JButton yearBtn;

    /** Button to open month selection dialog */
    private JButton monthBtn;

    /** Button to open day selection dialog */
    private JButton dayBtn;


    /**
     * Default constructor that initializes the selector with the current date,
     * but clears the input fields.
     */
    public yyyymmddSelector() {
        this(LocalDate.now());
        clearFields();
    }


    /**
     * Constructor that initializes the selector with a specific date.
     * @param date the initial date to populate the fields
     */
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


    /**
     * Clears all input fields (year, month, and day).
     */
    private void clearFields() {
        this.yearField.setText("");
        this.monthField.setText("");
        this.dayField.setText("");
    }


    /**
     * Sets the date fields using the provided LocalDate object.
     * @param date the date to populate the fields with
     */
    private void setDate(LocalDate date) {
        this.yearField.setText(String.valueOf(date.getYear()));
        this.monthField.setText(String.format("%02d", date.getMonthValue()));
        this.dayField.setText(String.format("%02d", date.getDayOfMonth()));
    }


    /**
     * Displays a dialog for selecting the year from a scrollable list.
     */
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


    /**
     * Displays a dialog for selecting the month from 1 to 12.
     */
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


    /**
     * Displays a dialog for selecting the day, adjusted based on the current month and year.
     */
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


    /**
     * Ensures that the selected day does not exceed the maximum days in the selected month/year.
     */
    private void updateDayFieldRange() {
        int year = getInt(yearField.getText(), LocalDate.now().getYear());
        int month = getInt(monthField.getText(), LocalDate.now().getMonthValue());
        int maxDay = YearMonth.of(year, month).lengthOfMonth();
        int day = getInt(dayField.getText(), 1);
        if (day > maxDay) {
            dayField.setText(String.format("%02d", maxDay));
        }
    }


    /**
     * Parses an integer from the given text or returns a default value if parsing fails.
     * @param text the text to parse
     * @param defaultValue the default value to use if parsing fails
     * @return the parsed integer or the default value
     */
    private int getInt(String text, int defaultValue) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Using default value: " + defaultValue, "Error", JOptionPane.ERROR_MESSAGE);
            return defaultValue;
        }
    }


    /**
     * Returns the selected date as a LocalDate object.
     * @return the selected LocalDate
     */
    public LocalDate getSelectedDate() {
        int year = getInt(this.yearField.getText(), LocalDate.now().getYear());
        int month = getInt(this.monthField.getText(), LocalDate.now().getMonthValue());
        int day = getInt(this.dayField.getText(), 1);
        int maxDay = YearMonth.of(year, month).lengthOfMonth();
        if (day > maxDay) day = maxDay;
        return LocalDate.of(year, month, day);
    }


    /**
     * Sets the selected date based on a provided LocalDate object.
     * 
     * @param date the LocalDate to set
     */
    public void setSelectedDate(LocalDate date) {
        setDate(date);
    }
}
