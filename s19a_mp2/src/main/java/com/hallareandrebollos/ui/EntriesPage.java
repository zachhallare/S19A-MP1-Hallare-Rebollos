package com.hallareandrebollos.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.hallareandrebollos.models.Entry;
import com.hallareandrebollos.models.Event;
import com.hallareandrebollos.models.Journal;
import com.hallareandrebollos.models.Meeting;
import com.hallareandrebollos.models.Task;

public class EntriesPage extends JPanel {

    final private String day;
    final private String month;
    final private String year;
    final private List<Entry> entries;

    public EntriesPage(String day, String month, String year, List<Entry> entries) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.entries = entries;

        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel(this.day + " " + this.month + " " + this.year, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // SOMEBODY SAVE ME FROM THIS ATROCITY OMDDDD
        JPanel entriesPanel = new JPanel();
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));

        for (Entry entry : this.entries) {
            JPanel entryTile;
            switch (entry.getType()) {
                case "Event":
                    entryTile = createEventListTile((Event) entry);
                    break;
                case "Journal":
                    entryTile = createJournalListTile((Journal) entry);
                    break;
                case "Meeting":
                    entryTile = createMeetingListTile((Meeting) entry);
                    break;
                case "Task":
                    entryTile = createTaskListTile((Task) entry);
                    break;
                default:
                    entryTile = new JPanel();
                    entryTile.add(new JLabel("Unknown entry type"));
            }
            entriesPanel.add(entryTile);
        }

        JScrollPane scrollPane = new JScrollPane(entriesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addEntryButton = new JButton("Add Entry"); // Placeholder
        JButton returnButton = new JButton("Return"); // Placeholder
        buttonPanel.add(addEntryButton);
        buttonPanel.add(returnButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

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
