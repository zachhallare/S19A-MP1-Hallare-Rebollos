package aMCO2;

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

public class CalendarPage extends JPanel {
    private final JPanel calendarGrid;
    private final JButton datePickerButton;
    private int selectedYear;
    private int selectedMonth;

    public CalendarPage(Router router, LogicController logic) {
        setLayout(new BorderLayout());
        setBackground(new Color(0xE0E0E0));
        selectedYear = LocalDate.now().getYear();
        selectedMonth = LocalDate.now().getMonthValue();

        // Top Panel.
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0xE0E0E0));

        // Top-Left: Previous Year and Month
        JPanel navPanelLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navPanelLeft.setOpaque(false);
        JButton prevYearButton = new JButton("<<");
        JButton prevMonthButton = new JButton("<");
        navPanelLeft.add(prevYearButton);
        navPanelLeft.add(prevMonthButton);

        // Top-Right: Next Month and Year.
        JPanel navPanelRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanelRight.setOpaque(false);
        JButton nextMonthButton = new JButton(">");
        JButton nextYearButton = new JButton(">>");
        navPanelRight.add(nextMonthButton);
        navPanelRight.add(nextYearButton);

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

        // Top-Center: Date Picker Button.
        JPanel monthYearPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        monthYearPanel.setOpaque(false);
        datePickerButton = new JButton();
        datePickerButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        datePickerButton.setFocusPainted(false);
        datePickerButton.setBackground(Color.WHITE);
        updateDatePickerLabel();
        monthYearPanel.add(datePickerButton);

        // Date Picker Logic.
        datePickerButton.addActionListener(e -> {
            String[] months = new DateFormatSymbols().getMonths();
            JComboBox<String> monthBox = new JComboBox<>(Arrays.copyOf(months, 12));
            monthBox.setSelectedIndex(selectedMonth - 1);
            SpinnerNumberModel yearModel = new SpinnerNumberModel(selectedYear, 1900, 2100, 1);
            JSpinner yearSpinner = new JSpinner(yearModel);

            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("Month:"));
            panel.add(monthBox);
            panel.add(new JLabel("Year:"));
            panel.add(yearSpinner);

            int result = JOptionPane.showConfirmDialog(null, panel, "Select Month and Year",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                selectedMonth = monthBox.getSelectedIndex() + 1;
                selectedYear = (int) yearSpinner.getValue();
                updateDatePickerLabel();
                drawCalendar(router);
            }
        });


        // Add to Top Panel.
        topPanel.add(navPanelLeft, BorderLayout.WEST);
        topPanel.add(monthYearPanel, BorderLayout.CENTER);
        topPanel.add(navPanelRight, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

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
        backButton.addActionListener(e -> router.showMenuPage());
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);


        // Previous Month Button Logic.
        prevMonthButton.addActionListener(e -> {
            if (selectedMonth == 1) {
                selectedMonth = 12;
                selectedYear--;
            } else {
                selectedMonth--;
            }
            updateDatePickerLabel();
            drawCalendar(router);
        });

        // Next Month Button Logic.
        nextMonthButton.addActionListener(e -> {
            if (selectedMonth == 12) {
                selectedMonth = 1;
                selectedYear++;
            } else {
                selectedMonth++;
            }
            updateDatePickerLabel();
            drawCalendar(router);
        });

        // Previous Year Button Logic.
        prevYearButton.addActionListener(e -> {
            selectedYear--;
            updateDatePickerLabel();
            drawCalendar(router);
        });

        // Previous Year Button Logic.
        nextYearButton.addActionListener(e -> {
            selectedYear++;
            updateDatePickerLabel();
            drawCalendar(router);
        });

        // Initialize.
        drawCalendar(router);
    }


    private void updateDatePickerLabel() {
        String monthName = Month.of(selectedMonth).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        datePickerButton.setText(monthName + " " + selectedYear);
    }


    private void drawCalendar(Router router) {
        calendarGrid.removeAll();

        DayOfWeek[] orderOfDays = {
            DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
        };

        for (DayOfWeek day : orderOfDays) {
            JLabel label = new JLabel(day.getDisplayName(TextStyle.FULL, Locale.ENGLISH), SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 13));
            calendarGrid.add(label);
        }

        YearMonth yearMonth = YearMonth.of(selectedYear, selectedMonth);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        int startDay = firstOfMonth.getDayOfWeek().getValue() % 7;

        // Fill Empty Cells Before the First Day of the Month.
        for (int i = 0; i < startDay; i++) {
            calendarGrid.add(new JLabel(""));
        }

        int daysInMonth = yearMonth.lengthOfMonth();  
        LocalDate today = LocalDate.now(); 

        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
            dayButton.setHorizontalAlignment(SwingConstants.LEFT);
            dayButton.setVerticalAlignment(SwingConstants.TOP);
            dayButton.setMargin(new Insets(4, 6, 4, 4));
            dayButton.setFocusPainted(false);
            dayButton.setBackground(Color.WHITE);
            dayButton.setOpaque(true);

            // Highlight the Day Today.
            if (selectedYear == today.getYear() && selectedMonth == today.getMonthValue() && day == today.getDayOfMonth()) {
                dayButton.setFont(new Font("SansSerif", Font.BOLD, 12));
            } 

            int selectedDay = day;
            dayButton.addActionListener(e -> {
                LocalDate selectedDate = LocalDate.of(selectedYear, selectedMonth, selectedDay);
                JOptionPane.showMessageDialog(this, "Show entries for: " + selectedDate);
                // To-Do: Add/edit/delete Entries here. 
            });

            calendarGrid.add(dayButton);
        }

        revalidate();
        repaint();
    }
}
