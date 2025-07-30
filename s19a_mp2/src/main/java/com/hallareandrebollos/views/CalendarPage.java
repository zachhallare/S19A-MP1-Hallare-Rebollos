package com.hallareandrebollos.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hallareandrebollos.controls.LogicController;
import com.hallareandrebollos.controls.Router;
import com.hallareandrebollos.widgets.calendarTile;


/**
 * Represents the main monthly calendar view, displaying a grid of days,
 * with navigation controls and buttons for switching views.
 */
public class CalendarPage extends JPanel {

    /** Handles calendar logic such as selected date and entry retrieval. */
    private final LogicController logic;

    /** Panel that displays the grid of day tiles. */
    private JPanel calendarGrid;

    /** Button that displays and updates the selected month and year. */
    private JButton datePickerButton;


    /**
     * Constructs the CalendarPage view and initializes components.
     * @param router Router for page navigation
     * @param logic LogicController for date and entry management
     */
    public CalendarPage(Router router, LogicController logic) {
        this.logic = logic;
        setLayout(new BorderLayout());
        setBackground(new Color(0xE0E0E0));
        initializeDate();
        add(createTopPanel(router), BorderLayout.NORTH);
        this.calendarGrid = createCalendarGrid();
        add(this.calendarGrid, BorderLayout.CENTER);
        add(createBottomPanel(router), BorderLayout.SOUTH);
        drawCalendar(router);
    }


    /** Initializes the selected month and year if not already set. */
    private void initializeDate() {
        if (logic.getSelectedMonth() == 0 || logic.getSelectedYear() == 0) {
            logic.setSelectedMonth(LocalDate.now().getMonthValue());
            logic.setSelectedYear(LocalDate.now().getYear());
        }
    }


    /**
     * Creates the top panel with navigation buttons and date picker.
     *
     * @param router Router used for calendar updates and navigation
     * @return Configured JPanel for the top section
     */
    private JPanel createTopPanel(Router router) {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(0xE0E0E0));

        JButton prevYearButton = new JButton("<<");
        JButton prevMonthButton = new JButton("<");
        JButton nextMonthButton = new JButton(">");
        JButton nextYearButton = new JButton(">>");

        this.datePickerButton = new JButton();
        this.datePickerButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        this.datePickerButton.setFocusPainted(false);
        this.datePickerButton.setBackground(Color.WHITE);
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
        topPanel.add(this.datePickerButton);
        topPanel.add(nextMonthButton);
        topPanel.add(nextYearButton);

        datePickerButton.addActionListener(e -> showDatePicker(router));
        setupNavigationButtons(router, prevMonthButton, nextMonthButton, prevYearButton, nextYearButton);

        return topPanel;
    }


    /**
     * Creates the calendar grid panel that displays days of the month.
     * @return A JPanel arranged in a 7-column grid layout
     */
    private JPanel createCalendarGrid() {
        JPanel calendarGrid = new JPanel();
        calendarGrid.setLayout(new GridLayout(0, 7));
        calendarGrid.setBackground(new Color(0xD0D0D0));
        calendarGrid.setBorder(new EmptyBorder(10, 10, 10, 10));
        return calendarGrid;
    }

    
    /**
     * Creates the bottom panel with buttons for help, weekly view, and back navigation.
     * @param router Router for handling navigation actions
     * @return Configured JPanel for the bottom section
     */
    private JPanel createBottomPanel(Router router) {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(0xE0E0E0));
        
        JButton weeklyViewButton = new JButton("Weekly View");
        weeklyViewButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        weeklyViewButton.setFocusPainted(false);
        weeklyViewButton.setBackground(Color.WHITE);
        weeklyViewButton.addActionListener(e -> {
            // Set the weekly view to start from the first day of the selected month
            LocalDate firstDayOfMonth = LocalDate.of(logic.getSelectedYear(), logic.getSelectedMonth(), 1);
            router.showWeeklyView(firstDayOfMonth);
        });
        
        JButton deleteCalendarButton = new JButton("Delete Calendar");
        deleteCalendarButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        deleteCalendarButton.setFocusPainted(false);
        deleteCalendarButton.setBackground(Color.WHITE);
        deleteCalendarButton.addActionListener(e -> {
            // Show confirmation dialog.
            String calendarName = logic.getCurrentCalendarObject() != null ? 
                logic.getCurrentCalendarObject().getCalendarName() : "this calendar";

            int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete \"" + calendarName + "\"?\nThis action cannot be undone.",
                "Confirm Delete Calendar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (result == JOptionPane.YES_OPTION) {
                logic.removeCurrentCalendarObject();
                router.showCalendarListPage();
            }
        });

        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backButton.setFocusPainted(false);
        backButton.setBackground(Color.WHITE);
        backButton.addActionListener(e -> router.showMenuPage());

        JButton addInfoButton = new JButton("Help");
        addInfoButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        addInfoButton.setFocusPainted(false);
        addInfoButton.setBackground(Color.WHITE);
        addInfoButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Red = Task Entry\nBlue = Event Entry\nYellow = Meeting Entry\nGreen = Journal Entry", "Help", JOptionPane.INFORMATION_MESSAGE);
        });

        bottomPanel.add(addInfoButton);
        bottomPanel.add(deleteCalendarButton);
        bottomPanel.add(weeklyViewButton);
        bottomPanel.add(backButton);
        return bottomPanel;
    }


    /**
     * Sets up navigation actions for month and year navigation buttons.
     * @param router Router used to trigger calendar redraw
     * @param prevMonth Button to go to previous month
     * @param nextMonth Button to go to next month
     * @param prevYear Button to go to previous year
     * @param nextYear Button to go to next year
     */
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


    /**
     * Shows a dialog allowing users to pick a specific month and year.
     * @param router Router used to redraw the calendar after date change
     */
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


    /** Updates the label on the date picker button to reflect the selected date. */
    private void updateDatePickerLabel() {
        String monthName = Month.of(logic.getSelectedMonth()).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        datePickerButton.setText(monthName + " " + logic.getSelectedYear());
    }


    /**
     * Renders the calendar grid based on the selected month and year.
     * @param router Router used for interaction in calendar tiles
     */
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
            calendarTile dayButton = new calendarTile(
                calendarGrid, day, month, year,
                day == today.getDayOfMonth() && month == today.getMonthValue() && year == today.getYear(),
                logic, router
            );

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
