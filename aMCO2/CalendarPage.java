package aMCO2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class CalendarPage extends JPanel {
    private final JComboBox<String> monthCombo;
    private final JComboBox<Integer> yearCombo;
    private final JPanel calendarGrid;

    public CalendarPage(Router router, LogicController logic) {
        setLayout(new BorderLayout());
        setBackground(new Color(0xE0E0E0));

        // Top Panel.
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0xE0E0E0));

        // Top-Right: Previous and Next Buttons.
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setOpaque(false);
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        navPanel.add(prevButton);
        navPanel.add(nextButton);

        // Top-Center: Month and Year ComboBox.
        JPanel monthYearPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        monthYearPanel.setOpaque(false);
        monthCombo = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            String monthName = YearMonth.of(2000, i).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            monthCombo.addItem(monthName);
        }

        yearCombo = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 10; i <= currentYear + 10; i++) {
            yearCombo.addItem(i);
        }

        monthYearPanel.add(monthCombo);
        monthYearPanel.add(yearCombo);

        // Add to Top Panel.
        topPanel.add(monthYearPanel, BorderLayout.CENTER);
        topPanel.add(navPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center: Calendar Grid.
        calendarGrid = new JPanel();
        calendarGrid.setLayout(new GridLayout(0, 7));
        calendarGrid.setBackground(new Color(0xD0D0D0));
        calendarGrid.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(calendarGrid, BorderLayout.CENTER);

        // Bottom Panel.
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(0xE0E0E0));
        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backButton.addActionListener(e -> router.showMenuPage());
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners.
        monthCombo.addActionListener(e -> drawCalendar(router));
        yearCombo.addActionListener(e -> drawCalendar(router));

        // Previous Button Logic.
        prevButton.addActionListener(e -> {
            int month = monthCombo.getSelectedIndex();
            int year = (int) yearCombo.getSelectedItem();
            if (month == 0) {
                monthCombo.setSelectedIndex(11);
                yearCombo.setSelectedItem(year - 1);
            } else {
                monthCombo.setSelectedIndex(month - 1);
            }
        });

        // Next Button Logic.
        nextButton.addActionListener(e -> {
            int month = monthCombo.getSelectedIndex();
            int year = (int) yearCombo.getSelectedItem();
            if (month == 11) {
                monthCombo.setSelectedIndex(0);
                yearCombo.setSelectedItem(year + 1);
            } else {
                monthCombo.setSelectedIndex(month + 1);
            }
        });

        // Initialize Current Month by Default
        monthCombo.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        yearCombo.setSelectedItem(LocalDate.now().getYear());
        drawCalendar(router);
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

        int selectedMonth = monthCombo.getSelectedIndex() + 1;
        int selectedYear = (int) yearCombo.getSelectedItem();
        YearMonth yearMonth = YearMonth.of(selectedYear, selectedMonth);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        int startDay = firstOfMonth.getDayOfWeek().getValue() % 7;

        // Fill Empty Cells Before the First Day of the Month.
        for (int i = 0; i < startDay; i++) {
            calendarGrid.add(new JLabel(""));
        }

        LocalDate today = LocalDate.now(); 
        int daysInMonth = yearMonth.lengthOfMonth();  

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
