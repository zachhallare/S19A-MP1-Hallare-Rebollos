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

        // Navigation Buttons Logic.
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

        prevYearButton.addActionListener(e -> {
            selectedYear--;
            updateDatePickerLabel();
            drawCalendar(router);
        });

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
            label.setOpaque(true);
            label.setBackground(new Color(0xC0C0C0));
            label.setPreferredSize(new Dimension(0, 24));
            calendarGrid.add(label);
        }

        YearMonth yearMonth = YearMonth.of(selectedYear, selectedMonth);
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
            if (selectedYear == today.getYear() && selectedMonth == today.getMonthValue() && day == today.getDayOfMonth()) {
                dayButton.setFont(new Font("SansSerif", Font.BOLD, 16));
            } 

            int selectedDay = day;
            dayButton.addActionListener(e -> {
                LocalDate selectedDate = LocalDate.of(selectedYear, selectedMonth, selectedDay);
                JOptionPane.showMessageDialog(this, "Show entries for: " + selectedDate);
                // To-Do: Add/edit/delete Entries here. 
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
