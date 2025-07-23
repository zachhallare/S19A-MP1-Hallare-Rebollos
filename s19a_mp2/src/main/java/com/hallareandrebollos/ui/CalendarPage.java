package com.hallareandrebollos.ui;

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
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hallareandrebollos.models.Entry;
import com.hallareandrebollos.models.Event;
import com.hallareandrebollos.models.Journal;
import com.hallareandrebollos.models.Meeting;
import com.hallareandrebollos.models.Task;
import com.hallareandrebollos.services.LogicController;
import com.hallareandrebollos.services.Router;

public class CalendarPage extends JPanel {
    private final LogicController logic;
    private final JPanel calendarGrid;
    private final JButton datePickerButton;

    public CalendarPage(Router router, LogicController logic) {
        this.logic = logic;
        setLayout(new BorderLayout());
        setBackground(new Color(0xE0E0E0));

        // Set Initial Month/Year Using LogicController.
        if (logic.getSelectedMonth() == 0 || logic.getSelectedYear() == 0) {
            logic.setSelectedMonth(LocalDate.now().getMonthValue());
            logic.setSelectedYear(LocalDate.now().getYear());
        }

        // Top Panel.
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
        add(topPanel, BorderLayout.NORTH);

        // Date Picker Logic.
        datePickerButton.addActionListener(e -> {
            String[] months = new DateFormatSymbols().getMonths();
            JComboBox<String> monthBox = new JComboBox<>(Arrays.copyOf(months, 12));
            monthBox.setSelectedIndex(logic.getSelectedMonth() - 1);
            SpinnerNumberModel yearModel = new SpinnerNumberModel(logic.getSelectedYear(), 1900, 2100, 1);
            JSpinner yearSpinner = new JSpinner(yearModel);
            NumberEditor editor = new JSpinner.NumberEditor(yearSpinner, "#");    // removes the comma from the year.
            yearSpinner.setEditor(editor);

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
        });

        // Center: Calendar Grid.
        calendarGrid = new JPanel();
        calendarGrid.setLayout(new GridLayout(0, 7));
        calendarGrid.setBackground(new Color(0xD0D0D0));
        calendarGrid.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(calendarGrid, BorderLayout.CENTER);


        // Bottom Panel: Back Button.
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(0xE0E0E0));
        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backButton.setFocusPainted(false);
        backButton.setBackground(Color.WHITE);
        backButton.addActionListener(e -> router.showMenuPage());
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Navigation Buttons Logic.
        prevMonthButton.addActionListener(e -> {
            int month = logic.getSelectedMonth();
            int year = logic.getSelectedYear();
            if (month == 1) {
                logic.setSelectedMonth(12);
                logic.setSelectedYear(year - 1);
            } else {
                logic.setSelectedMonth(month - 1);
            }
            updateDatePickerLabel();
            drawCalendar(router);
        });

        nextMonthButton.addActionListener(e -> {
            int month = logic.getSelectedMonth();
            int year = logic.getSelectedYear();
            if (month == 12) {
                logic.setSelectedMonth(1);
                logic.setSelectedYear(year + 1);
            } else {
                logic.setSelectedMonth(month + 1);
            }
            updateDatePickerLabel();
            drawCalendar(router);
        });

        prevYearButton.addActionListener(e -> {
            logic.setSelectedYear(logic.getSelectedYear() - 1);
            updateDatePickerLabel();
            drawCalendar(router);
        });

        nextYearButton.addActionListener(e -> {
            logic.setSelectedYear(logic.getSelectedYear() + 1);
            updateDatePickerLabel();
            drawCalendar(router);
        });

        // Initialize.
        drawCalendar(router);
    }





    private void updateDatePickerLabel() {
        String monthName = Month.of(logic.getSelectedMonth()).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        datePickerButton.setText(monthName + " " + logic.getSelectedYear());
    }


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
                // Entry System.
                LocalDate selectedDate = LocalDate.of(year, month, selectedDay);
                ArrayList<Entry> entries = logic.getEntriesForDate(selectedDate);

                StringBuilder message = new StringBuilder("Entries for " + selectedDate + ":\n\n");
                if (entries.isEmpty()) {
                    message.append("No entries.\n");
                } else {
                    for (int i = 0; i < entries.size(); i++) {
                        Entry entry = entries.get(i);
                        message.append(i + 1).append(". ").append(entry.getType()).append("\n");
                    }
                }

                Object[] options = {"Add", "Edit", "Delete", "Cancel"};
                int choice = JOptionPane.showOptionDialog(this, message.toString(), "Manage Entries", 
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                if (choice == 0) {
                    // add entry.
                    String type = JOptionPane.showInputDialog("Enter type (task, event, meeting, journal):");
                    String title = JOptionPane.showInputDialog("Enter title:");
                    String description = JOptionPane.showInputDialog("Enter description:");

                    if (type != null && title != null && description != null) {
                        type = type.toLowerCase();
                        switch (type) {
                            case "task" -> {
                                String priority = JOptionPane.showInputDialog("Enter priority:");
                                String status = JOptionPane.showInputDialog("Enter status:");
                                String createdBy = JOptionPane.showInputDialog("Enter createdBy:");
                                String finishedBy = JOptionPane.showInputDialog("Enter finishedBy (optional):");
                                logic.addEntryToCurrentCalendarObject(type, title, description, selectedDate,
                                        priority, status, createdBy, finishedBy,
                                        null, null, null, null, null, null);
                            }
                            case "event" -> {
                                String venue = JOptionPane.showInputDialog("Enter venue:");
                                String organizer = JOptionPane.showInputDialog("Enter organizer:");
                                String start = JOptionPane.showInputDialog("Enter start time (HH:mm):");
                                String end = JOptionPane.showInputDialog("Enter end time (HH:mm):");
                                try {
                                    LocalTime startTime = LocalTime.parse(start);
                                    LocalTime endTime = LocalTime.parse(end);
                                    logic.addEntryToCurrentCalendarObject(type, title, description, selectedDate,
                                            null, null, null, null,
                                            venue, organizer, null, null, startTime, endTime);
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(this, "Invalid time format.");
                                }
                            }
                            case "meeting" -> {
                                String modality = JOptionPane.showInputDialog("Enter modality:");
                                String venue = JOptionPane.showInputDialog("Enter venue (optional):");
                                String link = JOptionPane.showInputDialog("Enter link (optional):");
                                logic.addEntryToCurrentCalendarObject(type, title, description, selectedDate,
                                        null, null, null, null,
                                        venue, null, modality, link, null, null);
                            }
                            case "journal" -> {
                                logic.addEntryToCurrentCalendarObject(type, title, description, selectedDate,
                                        null, null, null, null,
                                        null, null, null, null, null, null);
                            }
                        }
                        drawCalendar(router);
                    }

                } else if (choice == 1 && !entries.isEmpty()) {
                    // edit entry.
                    String input = JOptionPane.showInputDialog("Enter entry number to edit:");
                    if (input != null && input.matches("\\d+")) {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx >= 0 && idx < entries.size()) {
                            Entry oldEntry = entries.get(idx);
                            String newTitle = JOptionPane.showInputDialog("New title:", oldEntry.getTitle());
                            String newDesc = JOptionPane.showInputDialog("New description:", oldEntry.getDescription());
                            Entry newEntry = null;

                            // Build the correct entry subclass again
                            switch (oldEntry.getType().toLowerCase()) {
                                case "task" -> {
                                    Task t = (Task) oldEntry;
                                    newEntry = new Task(newTitle, t.getDate(), newDesc, t.getPriority(), t.getStatus(), t.getCreatedBy(), t.getFinishedBy());
                                }
                                case "event" -> {
                                    Event ev = (Event) oldEntry;
                                    newEntry = new Event(newTitle, ev.getDate(), newDesc, ev.getVenue(), ev.getOrganizer(), ev.getStartTime(), ev.getEndTime());
                                }
                                case "meeting" -> {
                                    Meeting m = (Meeting) oldEntry;
                                    newEntry = new Meeting(newTitle, m.getDate(), newDesc, m.getModality(), m.getVenue(), m.getLink());
                                }
                                case "journal" -> {
                                    Journal j = (Journal) oldEntry;
                                    newEntry = new Journal(newTitle, j.getDate(), newDesc);
                                }
                            }

                            if (newEntry != null) {
                                logic.editEntryInCurrentCalendarObject(oldEntry, newEntry);
                                drawCalendar(router);
                            }
                        }
                    }

                } else if (choice == 2 && !entries.isEmpty()) {
                    // delete entry.
                    String input = JOptionPane.showInputDialog("Enter entry number to delete:");
                    if (input != null && input.matches("\\d+")) {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx >= 0 && idx < entries.size()) {
                            logic.removeEntryFromCurrentCalendarObject(entries.get(idx).getTitle());
                            drawCalendar(router); 
                        }
                    }
                }
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
