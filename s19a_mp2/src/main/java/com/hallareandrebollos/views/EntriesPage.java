package com.hallareandrebollos.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hallareandrebollos.controls.LogicController;
import com.hallareandrebollos.controls.Router;
import com.hallareandrebollos.models.Entry;
import com.hallareandrebollos.models.Event;
import com.hallareandrebollos.models.Journal;
import com.hallareandrebollos.models.Meeting;
import com.hallareandrebollos.models.Task;

public class EntriesPage extends JPanel {
    private final Router router;
    private final LogicController logic;
    private String day;
    private String month;
    private String year;
    private ArrayList<Entry> entries;
    private JPanel entriesPanel;
    private JLabel titleLabel;
    
    public EntriesPage(Router router, LogicController logic, String day, String month, String year, ArrayList<Entry> entries) {
        this.router = router;
        this.logic = logic;
        this.day = day;
        this.month = month;
        this.year = year;
        this.entries = entries;

        setLayout(new BorderLayout(10, 10));

        // Convert day/month/year to LocalDate.
        int dayInt = Integer.parseInt(day);
        int monthInt = Integer.parseInt(month);
        int yearInt = Integer.parseInt(year);
        LocalDate date = LocalDate.of(yearInt, monthInt, dayInt);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH);
        String formattedDate = date.format(formatter);


        // Title Label
        JLabel titleLabel = new JLabel(formattedDate, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Entries List.
        entriesPanel = new JPanel();
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));
        redrawEntries();

        JScrollPane scrollPane = new JScrollPane(entriesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addEntryButton = new JButton("Add Entry"); 
        addEntryButton.addActionListener(e -> router.showAddEntryPage(day, month, year));

        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> router.showCalendarPage(router.getLogicController().getCurrentCalendarObject()));

        buttonPanel.add(addEntryButton);
        buttonPanel.add(returnButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }


    // Refresh the entries and title.
    public void refresh() {
        // Reload entries.
        LocalDate date = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        this.entries = logic.getEntriesForDate(date);

        titleLabel.setText(formatDateString(day, month, year));
        redrawEntries();    // redraw.
    }


    // format date.
    private String formatDateString(String day, String month, String year) {
        LocalDate date = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH);
        return date.format(formatter);
    }
    
    // Redraw entries.
    private void redrawEntries() {
        entriesPanel.removeAll();

        for (Entry entry : this.entries) {
            JPanel entryTile = createEntryTile(entry);
            entriesPanel.add(entryTile);
        }

        entriesPanel.revalidate();
        entriesPanel.repaint();
    }


    // Create entry.
    private JPanel createEntryTile(Entry entry) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Entry Information Panel.
        JPanel infoPanel = switch (entry.getType()) {
            case "Event" -> createEventListTile((Event) entry);
            case "Journal" -> createJournalListTile((Journal) entry);
            case "Meeting" -> createMeetingListTile((Meeting) entry);
            case "Task" -> createTaskListTile((Task) entry);
            default -> new JPanel();    // if unknown type.
        };

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        // Edit Entry.
        editButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Edit Entry: " + entry.toDisplayString());
            // EDIT ENTRY UHRUNSFJ
        });

        // Delete Entry.
        deleteButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Delete this entry?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                entries.remove(entry);
                redrawEntries();
            }
        });

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }


    // Event.
    private JPanel createEventListTile(Event event) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(event.toDisplayString());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel detailsLabel = new JLabel("Organizer: " + event.getOrganizer() + " | Venue: " + event.getVenue());
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(detailsLabel, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return panel;
    }


    // Journal.
    private JPanel createJournalListTile(Journal journal) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(journal.toDisplayString());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel descLabel = new JLabel("Description: " + journal.getDescription());
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(descLabel, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return panel;
    }


    // Meeting.
    private JPanel createMeetingListTile(Meeting meeting) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(meeting.toDisplayString());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        String venueOrLink = (meeting.getVenue() != null ? "Venue: " + meeting.getVenue() : "")
                + (meeting.getLink() != null ? " | Link: " + meeting.getLink() : "");
        JLabel detailsLabel = new JLabel(venueOrLink);
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(detailsLabel, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return panel;
    }


    // Task.
    private JPanel createTaskListTile(Task task) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(task.toDisplayString());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        String finishedBy = (task.getFinishedBy() != null) ? " | Finished by: " + task.getFinishedBy() : "";
        JLabel detailsLabel = new JLabel("Created by: " + task.getCreatedBy() + finishedBy);
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(detailsLabel, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return panel;
    }
}
