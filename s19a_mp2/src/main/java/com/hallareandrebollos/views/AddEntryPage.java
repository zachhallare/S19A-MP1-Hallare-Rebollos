package com.hallareandrebollos.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.hallareandrebollos.controls.LogicController;
import com.hallareandrebollos.controls.Router;
import com.hallareandrebollos.models.CalendarObject;
import com.hallareandrebollos.models.Entry;

public class AddEntryPage extends JPanel {

    public AddEntryPage(Router router, LogicController logic, String day, String month, String year) {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0xE0E0E0));
        setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JLabel title = new JLabel("Add New Entry", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        JButton taskButton = new JButton("Add Task");
        JButton eventButton = new JButton("Add Event");
        JButton meetingButton = new JButton("Add Meeting");
        JButton journalButton = new JButton("Add Journal");

        buttonPanel.add(taskButton);
        buttonPanel.add(eventButton);
        buttonPanel.add(meetingButton);
        buttonPanel.add(journalButton);
        add(buttonPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        JButton backButton = new JButton("Back");

        backButton.addActionListener(e -> {
            CalendarObject calendar = logic.getCurrentCalendarObject();
            ArrayList<Entry> entries = new ArrayList<>();
            for (Entry entry : calendar.getEntries()) {
                LocalDate date = entry.getDate();
                if (date.getDayOfMonth() == Integer.parseInt(day)
                        && date.getMonth().toString().equalsIgnoreCase(month)
                        && date.getYear() == Integer.parseInt(year)) {
                    entries.add(entry);
                }
            }
            router.showEntriesPage(day, month, year, entries);
        });

        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        taskButton.addActionListener(e -> router.showEntryFormPage("Task", day, month, year));
        eventButton.addActionListener(e -> router.showEntryFormPage("Event", day, month, year));
        meetingButton.addActionListener(e -> router.showEntryFormPage("Meeting", day, month, year));
        journalButton.addActionListener(e -> router.showEntryFormPage("Journal", day, month, year));
    }
}
