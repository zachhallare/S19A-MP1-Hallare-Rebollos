
package com.hallareandrebollos.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.hallareandrebollos.controls.LogicController;
import com.hallareandrebollos.controls.Router;
import com.hallareandrebollos.models.Entry;
import com.hallareandrebollos.models.Event;
import com.hallareandrebollos.models.Meeting;
import com.hallareandrebollos.models.Task;

public class entryList extends JPanel {
    private final LocalDate date;
    private final List<Entry> entries;
    private final Router router;
    private final LogicController logicController;

    public entryList(LocalDate date, List<Entry> entries, Router router, LogicController logicController) {
        this.date = date;
        this.entries = entries;
        this.router = router;
        this.logicController = logicController;
        setLayout(new BorderLayout());
        setOpaque(false);

        add(createHeader(), BorderLayout.NORTH);
        add(createListView(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
        panel.setOpaque(false);

        String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String headerText = String.format("%s %d (%s)", month, date.getDayOfMonth(), dayOfWeek);

        JLabel label = new JLabel(headerText);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label, BorderLayout.WEST);

        return panel;
    }

    private JScrollPane createListView() {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        // Separate entries
        List<Entry> journalsAndTasks = new ArrayList<>();
        List<Entry> meetingsAndEvents = new ArrayList<>();

        for (Entry entry : entries) {
            String type = entry.getType();
            if (type.equals("Journal") || type.equals("Task")) {
                journalsAndTasks.add(entry);
            } else if (type.equals("Meeting") || type.equals("Event")) {
                meetingsAndEvents.add(entry);
            }
        }

        // Sort meetings/events by start time
        meetingsAndEvents.sort((a, b) -> {
            LocalTime aStart = null, bStart = null;
            if (a instanceof Meeting meeting) aStart = meeting.getStartTime();
            if (a instanceof Event event) aStart = event.getStartTime();
            if (b instanceof Meeting meeting) bStart = meeting.getStartTime();
            if (b instanceof Event event) bStart = event.getStartTime();
            if (aStart == null && bStart == null) return 0;
            if (aStart == null) return -1;
            if (bStart == null) return 1;
            return aStart.compareTo(bStart);
        });

        // Add journals/tasks first
        for (Entry entry : journalsAndTasks) {
            listPanel.add(createTile(entry));
        }

        for (Entry entry : meetingsAndEvents) {
            listPanel.add(createTile(entry));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        return scrollPane;
    }

    private JPanel createTile(Entry entry) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(8, 16, 8, 16));
        panel.setBackground(new Color(245, 245, 245));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel(entry.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel typeLabel = new JLabel(entry.getType());
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(titleLabel);
        infoPanel.add(typeLabel);

        if (entry instanceof Meeting) {
            Meeting m = (Meeting) entry;
            JLabel timeLabel = new JLabel(m.getStartTime() + " - " + m.getEndTime());
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            infoPanel.add(timeLabel);
        } else if (entry instanceof Event) {
            Event e = (Event) entry;
            JLabel timeLabel = new JLabel(e.getStartTime() + " - " + e.getEndTime());
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            infoPanel.add(timeLabel);
        }

        panel.add(infoPanel, BorderLayout.WEST);

        // Right: Menu button
        JButton menuBtn = new JButton("⋮");
        menuBtn.setFont(new Font("Arial", Font.BOLD, 16));
        menuBtn.setFocusPainted(false);
        menuBtn.setBorderPainted(false);
        menuBtn.setContentAreaFilled(false);
        menuBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        menuBtn.addActionListener(e -> {
            JPanel dialogPanel = new JPanel();
            dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
            JButton editBtn = new JButton("Edit Entry");
            JButton deleteBtn = new JButton("Delete Entry");
            editBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);
            deleteBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);
            dialogPanel.add(editBtn);
            dialogPanel.add(deleteBtn);

            java.awt.Window window = SwingUtilities.getWindowAncestor(panel);
            JDialog dialog;
            if (window instanceof java.awt.Frame) {
                dialog = new JDialog((java.awt.Frame) window, "Entry Actions", true);
            } else if (window instanceof java.awt.Dialog) {
                dialog = new JDialog((java.awt.Dialog) window, "Entry Actions", true);
            } else {
                dialog = new JDialog((java.awt.Frame) null, "Entry Actions", true);
            }
            dialog.setContentPane(dialogPanel);
            dialog.setSize(220, 120);
            dialog.setLocationRelativeTo(panel);

            editBtn.addActionListener(ae -> {
                dialog.dispose();
                router.showEntryForm(entry);
            });
            deleteBtn.addActionListener(ae -> {
                dialog.dispose();
                int confirm = JOptionPane.showConfirmDialog(panel, "Delete entry '" + entry.getTitle() + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    logicController.getCurrentCalendarObject().removeEntry(entry);
                    JOptionPane.showMessageDialog(panel, "Entry deleted.");
                    router.showEntriesPage(String.valueOf(date.getDayOfMonth()), String.valueOf(date.getMonthValue()), String.valueOf(date.getYear()), logicController.getEntriesForDate(date));
                }
            });
            dialog.setVisible(true);
        });
        panel.add(menuBtn, BorderLayout.EAST);

        // Clickable tile
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel dialogPanel = new JPanel();
                dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
                dialogPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
                dialogPanel.add(new JLabel("Title: " + entry.getTitle()));
                dialogPanel.add(new JLabel("Type: " + entry.getType()));
                dialogPanel.add(new JLabel("Date: " + entry.getDate()));
                if (entry.getDescription() != null && !entry.getDescription().isEmpty()) {
                    dialogPanel.add(new JLabel("Description: " + entry.getDescription()));
                }
                if (entry instanceof Event) {
                    Event ev = (Event) entry;
                    dialogPanel.add(new JLabel("Venue: " + ev.getVenue()));
                    dialogPanel.add(new JLabel("Organizer: " + ev.getOrganizer()));
                    dialogPanel.add(new JLabel("Start Time: " + ev.getStartTime()));
                    dialogPanel.add(new JLabel("End Time: " + ev.getEndTime()));
                } else if (entry instanceof Meeting) {
                    Meeting mt = (Meeting) entry;
                    dialogPanel.add(new JLabel("Modality: " + mt.getModality()));
                    dialogPanel.add(new JLabel("Venue: " + (mt.getVenue() != null ? mt.getVenue() : "")));
                    dialogPanel.add(new JLabel("Link: " + (mt.getLink() != null ? mt.getLink() : "")));
                    dialogPanel.add(new JLabel("Start Time: " + mt.getStartTime()));
                    dialogPanel.add(new JLabel("End Time: " + mt.getEndTime()));
                } else if (entry instanceof Task) {
                    Task tk = (Task) entry;
                    dialogPanel.add(new JLabel("Priority: " + tk.getPriority()));
                    dialogPanel.add(new JLabel("Status: " + tk.getStatus()));
                    dialogPanel.add(new JLabel("Created By: " + tk.getCreatedBy()));
                    dialogPanel.add(new JLabel("Finished By: " + (tk.getFinishedBy() != null ? tk.getFinishedBy() : "")));
                }
                JDialog dialog;
                java.awt.Window window = SwingUtilities.getWindowAncestor(panel);
                if (window instanceof java.awt.Frame) {
                    dialog = new JDialog((java.awt.Frame) window, "Entry Details", true);
                } else if (window instanceof java.awt.Dialog) {
                    dialog = new JDialog((java.awt.Dialog) window, "Entry Details", true);
                } else {
                    dialog = new JDialog((java.awt.Frame) null, "Entry Details", true);
                }
                dialog.setContentPane(dialogPanel);
                dialog.setSize(320, 320);
                dialog.setLocationRelativeTo(panel);
                dialog.setVisible(true);
            }
        });

        return panel;
    }

    private JPanel createFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
        panel.setOpaque(false);

        JButton addBtn = new JButton("Add Entry");
        addBtn.setFont(new Font("Arial", Font.BOLD, 16));
        addBtn.setBackground(new Color(66, 133, 244));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> {
            // Show EntryForm for this date
            router.showEntryForm(null, this.date);
        });

        panel.add(addBtn, BorderLayout.CENTER);
        return panel;
    }
}
