package com.hallareandrebollos.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.hallareandrebollos.controls.LogicController;
import com.hallareandrebollos.controls.Router;
import com.hallareandrebollos.models.Entry;


public class CalendarPage extends JPanel {
    private final LogicController logic;
    private JPanel calendarGrid;
    private JButton datePickerButton;

    public CalendarPage(Router router, LogicController logic) {
        this.logic = logic;
        setLayout(new BorderLayout());
        setBackground(new Color(0xE0E0E0));

        initializeDate();
        add(createTopPanel(router), BorderLayout.NORTH);
        calendarGrid = createCalendarGrid();
        add(calendarGrid, BorderLayout.CENTER);
        add(createBottomPanel(router), BorderLayout.SOUTH);
        drawCalendar(router);
    }


    // Set Initial Month/Year Using LogicController.
    private void initializeDate() {
        if (logic.getSelectedMonth() == 0 || logic.getSelectedYear() == 0) {
            logic.setSelectedMonth(LocalDate.now().getMonthValue());
            logic.setSelectedYear(LocalDate.now().getYear());
        }
    }


    // Creates the Top Panel of the Calendar Page.
    private JPanel createTopPanel(Router router) {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(0xE0E0E0));

        JButton prevYearButton = new JButton("<<");
        JButton prevMonthButton = new JButton("<");
        JButton nextMonthButton = new JButton(">");
        JButton nextYearButton = new JButton(">>");

        datePickerButton = new JButton();
        datePickerButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        datePickerButton.setFocusPainted(false);
        datePickerButton.setBackground(Color.WHITE);
        updateDatePickerLabel();

        // Dimension and Font for the Buttons.
        Dimension squareSize = new Dimension(50, 30);
        Font buttonFont = new Font("SansSerif", Font.BOLD, 14);
        JButton[] navButtons = {prevYearButton, prevMonthButton, nextMonthButton, nextYearButton};
        for (JButton button : navButtons) {
            button.setPreferredSize(squareSize);
            button.setFont(buttonFont);
            button.setFocusPainted(false);
            button.setBackground(Color.WHITE);
        }

        // Add the Buttons to the Top Panel.
        topPanel.add(prevYearButton);
        topPanel.add(prevMonthButton);
        topPanel.add(datePickerButton);
        topPanel.add(nextMonthButton);
        topPanel.add(nextYearButton);

        datePickerButton.addActionListener(e -> showDatePicker(router));
        setupNavigationButtons(router, prevMonthButton, nextMonthButton, prevYearButton, nextYearButton);

        return topPanel;
    }


    // Creates the Calendar Grid of the Calendar Page.
    private JPanel createCalendarGrid() {
        JPanel calendarGrid = new JPanel();
        calendarGrid.setLayout(new GridLayout(0, 7));
        calendarGrid.setBackground(new Color(0xD0D0D0));
        calendarGrid.setBorder(new EmptyBorder(10, 10, 10, 10));
        return calendarGrid;
    }

    
    // Creates the Bottom Panel of the Calendar Page.
    private JPanel createBottomPanel(Router router) {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(0xE0E0E0));
        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backButton.setFocusPainted(false);
        backButton.setBackground(Color.WHITE);
        backButton.addActionListener(e -> router.showMenuPage());
        bottomPanel.add(backButton);
        return bottomPanel;
    }


    // Sets up the Navigation Buttons at the Top Panel.
    private void setupNavigationButtons(Router router, JButton prevMonth, JButton nextMonth, JButton prevYear, JButton nextYear) {
        prevMonth.addActionListener(e -> {
            int month = logic.getSelectedMonth(); 
            int year = logic.getSelectedYear();
            if (month == 1) {
                logic.setSelectedMonth(12);
                logic.setSelectedYear(year - 1);
            } else logic.setSelectedMonth(month - 1);
            updateDatePickerLabel();
            drawCalendar(router);
        });

        nextMonth.addActionListener(e -> {
            int month = logic.getSelectedMonth(); 
            int year = logic.getSelectedYear();
            if (month == 12) {
                logic.setSelectedMonth(1);
                logic.setSelectedYear(year + 1);
            } else logic.setSelectedMonth(month + 1);
            updateDatePickerLabel();
            drawCalendar(router);
        });

        prevYear.addActionListener(e -> {
            logic.setSelectedYear(logic.getSelectedYear() - 1);
            updateDatePickerLabel();
            drawCalendar(router);
        });

        nextYear.addActionListener(e -> {
            logic.setSelectedYear(logic.getSelectedYear() + 1);
            updateDatePickerLabel();
            drawCalendar(router);
        });
    }


    // A Button at the Top Panel to Manually Change the Date.
    private void showDatePicker(Router router) {
        String[] months = new DateFormatSymbols().getMonths();
        JComboBox<String> monthBox = new JComboBox<>(Arrays.copyOf(months, 12));
        monthBox.setSelectedIndex(logic.getSelectedMonth() - 1);
        SpinnerNumberModel yearModel = new SpinnerNumberModel(logic.getSelectedYear(), 1900, 2100, 1);
        JSpinner yearSpinner = new JSpinner(yearModel);
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Month:"));
        panel.add(monthBox);
        panel.add(new JLabel("Year:"));
        panel.add(yearSpinner);

        int result = JOptionPane.showConfirmDialog(null, panel, "Select Month and Year",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            logic.setSelectedMonth(monthBox.getSelectedIndex() + 1);
            logic.setSelectedYear((int) yearSpinner.getValue());
            updateDatePickerLabel();
            drawCalendar(router);
        }
    }


    // Updates the DatePickerLabel.
    private void updateDatePickerLabel() {
        String monthName = Month.of(logic.getSelectedMonth()).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        datePickerButton.setText(monthName + " " + logic.getSelectedYear());
    }


    // Draws the Individual Calendar Per Month.
    private void drawCalendar(Router router) {
        calendarGrid.removeAll();

        DayOfWeek[] weekDays = {
            DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
        };

        for (DayOfWeek day : weekDays) {
            JLabel label = new JLabel(day.getDisplayName(TextStyle.FULL, Locale.ENGLISH), SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 13));
            label.setOpaque(true);
            label.setBackground(new Color(0xC0C0C0));
            label.setPreferredSize(new Dimension(0, 24));
            calendarGrid.add(label);
        }

        int year = logic.getSelectedYear();
        int month = logic.getSelectedMonth();
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        int startDay = firstOfMonth.getDayOfWeek().getValue() % 7;

        // Fill Empty Cells Before the First Day of the Month.
        for (int i = 0; i < startDay; i++) {
            JButton filler = new JButton();
            filler.setEnabled(false);
            filler.setBackground(new Color(0xD8D8D8));
            calendarGrid.add(filler);
        }

        int daysInMonth = yearMonth.lengthOfMonth();  
        LocalDate today = LocalDate.now(); 

        // Displays Each Button for Each Day.
        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
            dayButton.setHorizontalAlignment(SwingConstants.LEFT);
            dayButton.setVerticalAlignment(SwingConstants.TOP);
            dayButton.setMargin(new Insets(4, 6, 4, 4));
            dayButton.setFocusPainted(false);
            dayButton.setBackground(Color.WHITE);
            dayButton.setOpaque(true);

            // Highlight the Day Today.
            if (year == today.getYear() && month == today.getMonthValue() && day == today.getDayOfMonth()) {
                dayButton.setFont(new Font("SansSerif", Font.BOLD, 15));
            } 

            int selectedDay = day;
            dayButton.addActionListener(e -> {
                String dayString = String.valueOf(selectedDay);
                String monthString = String.valueOf(month);
                String yearString = String.valueOf(year);

                // // Get entries from Logic Controller.
                // ArrayList<Entry> entriesForDay = logic.getEntriesForDate(LocalDate.of(year, month, selectedDay));

                // // Create EntriesPage.
                // router.showEntriesPage(dayString, monthString, yearString, entriesForDay);


                LocalDate selectedDate = LocalDate.of(year, month, selectedDay);
                ArrayList<Entry> entriesForDay = logic.getEntriesForDate(selectedDate);

                StringBuilder message = new StringBuilder();
                if (entriesForDay.isEmpty()) {
                    message.append("No entries for this day.");
                } else {
                    for (int i = 0; i < entriesForDay.size(); i++) {
                        Entry entry = entriesForDay.get(i);
                        message.append(i + 1).append(". ").append(entry.getTitle()).append(" - ");
                        message.append(entry.getClass().getSimpleName()).append("\n");
                    }
                }

                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());

                JTextArea textArea = new JTextArea(message.toString());
                textArea.setEditable(false);
                textArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
                panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton addBtn = new JButton("Add Entry");
                JButton editBtn = new JButton("Edit Entry");
                JButton deleteBtn = new JButton("Delete Entry");
                JButton closeBtn = new JButton("Close");

                buttonPanel.add(addBtn);
                buttonPanel.add(editBtn);
                buttonPanel.add(deleteBtn);
                buttonPanel.add(closeBtn);

                panel.add(buttonPanel, BorderLayout.SOUTH);

                JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(calendarGrid), 
                                            "Entries for " + selectedDate.toString(), true);
                dialog.setContentPane(panel);
                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(calendarGrid);

                // Action buttons
                closeBtn.addActionListener(ae -> dialog.dispose());

                addBtn.addActionListener(ae -> {
                    dialog.dispose();
                    router.showAddEntryPage(dayString, monthString, yearString);
                });

                editBtn.addActionListener(ae -> {
                    dialog.dispose();
                    // router.showEditEntryPage(dayString, monthString, yearString, entriesForDay); // You can customize this
                });

                deleteBtn.addActionListener(ae -> {
                    dialog.dispose();
                    // router.showDeleteEntryPage(dayString, monthString, yearString, entriesForDay); // You can customize this
                });

                dialog.setVisible(true);
            });

            calendarGrid.add(dayButton);
        }

        // Fill remaining cells after last day to complete the week row.
        int totalCells = startDay + daysInMonth;
        int remainingCells = (7 - (totalCells % 7)) % 7;
        for (int i = 0; i < remainingCells; i++) {
            JButton filler = new JButton();
            filler.setEnabled(false);
            filler.setBackground(new Color(0xD8D8D8));
            calendarGrid.add(filler);
        }

        revalidate();
        repaint();
    }
}
