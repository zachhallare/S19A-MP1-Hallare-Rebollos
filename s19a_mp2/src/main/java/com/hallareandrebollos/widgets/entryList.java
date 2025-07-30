
package com.hallareandrebollos.widgets;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
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
import com.hallareandrebollos.models.Theme;


/**
 * A widget that displays a list of entries (Journal, Task, Meeting, Event) for a specific date.
 * It includes a header, a scrollable list of entries, and a footer with an "Add Entry" button.
 */
public class entryList extends JPanel {

    /** The date for which entries are being displayed. */
    private final LocalDate date;
    
    /** The list of entries to display. */
    private final List<Entry> entries;

    /** The application's router to navigate views. */
    private final Router router;

    /** The logic controller to handle entry operations. */
    private final LogicController logicController;

    /** Flag that determines whether to close the dialog when adding an entry. */
    private final boolean closeDialogOnAdd;


    /**
     * Constructs an entryList panel for the given date and list of entries
     * @param date             The date the entries belong to.
     * @param entries          The list of entries for the date.
     * @param router           The router used for view navigation.
     * @param logicController  The logic controller for entry manipulation.
     * @param closeDialogOnAdd Whether to close the dialog when adding an entry.
     */
    public entryList(LocalDate date, List<Entry> entries, Router router, LogicController logicController, boolean closeDialogOnAdd) {
        this.date = date;
        this.entries = entries;
        this.router = router;
        this.logicController = logicController;
        this.closeDialogOnAdd = closeDialogOnAdd;
        setLayout(new BorderLayout());
        setOpaque(false);

        add(createHeader(), BorderLayout.NORTH);
        add(createListView(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }


    /**
     * Creates the header panel that displays the formatted date.
     * @return the header panel.
     */
    private JPanel createHeader() {
        Theme theme = logicController.getCurrentTheme();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
        panel.setOpaque(false);

        String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String headerText = String.format("%s %d (%s)", month, date.getDayOfMonth(), dayOfWeek);

        JLabel label = new JLabel(headerText);
        label.setFont(theme.getSubtitleFont());
        label.setForeground(theme.getTextColor());
        panel.add(label, BorderLayout.WEST);

        return panel;
    }


    /**
     * Creates a scrollable list view panel that organizes and displays entry tiles.
     * @return the scroll pane containing the entry list.
     */
    private JScrollPane createListView() {
        Theme theme = logicController.getCurrentTheme();
        
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setBackground(theme.getBackgroundColor());

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
        scrollPane.setBackground(theme.getBackgroundColor());
        scrollPane.getViewport().setBackground(theme.getBackgroundColor());
        return scrollPane;
    }


    /**
     * Creates a tile (panel) representing a single entry.
     * Displays basic info and includes action buttons.
     * @param entry The entry to represent.
     * @return the JPanel representing the entry tile.
     */
    private JPanel createTile(Entry entry) {
        Theme theme = logicController.getCurrentTheme();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(8, 16, 8, 16));
        panel.setBackground(theme.getPanelColor());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel(entry.getTitle());
        titleLabel.setFont(theme.getSubtitleFont());
        titleLabel.setForeground(theme.getTextColor());
        JLabel typeLabel = new JLabel(entry.getType());
        typeLabel.setFont(theme.getRegularFont());
        typeLabel.setForeground(theme.getSubtitleColor());
        infoPanel.add(titleLabel);
        infoPanel.add(typeLabel);

        if (entry instanceof Meeting m) {
            JLabel timeLabel = new JLabel(m.getStartTime() + " - " + m.getEndTime());
            timeLabel.setFont(theme.getRegularFont());
            timeLabel.setForeground(theme.getSubtitleColor());
            infoPanel.add(timeLabel);
        } else if (entry instanceof Event e) {
            JLabel timeLabel = new JLabel(e.getStartTime() + " - " + e.getEndTime());
            timeLabel.setFont(theme.getRegularFont());
            timeLabel.setForeground(theme.getSubtitleColor());
            infoPanel.add(timeLabel);
        }

        panel.add(infoPanel, BorderLayout.WEST);

        // Right: Menu button
        JButton menuBtn = new JButton("...");
        menuBtn.setFont(theme.getButtonFont());
        menuBtn.setFocusPainted(false);
        menuBtn.setBorderPainted(false);
        menuBtn.setContentAreaFilled(false);
        menuBtn.setForeground(theme.getTextColor());
        menuBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        menuBtn.addActionListener((var e) -> {
            JPanel dialogPanel = new JPanel();
            dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
            JButton editBtn = new JButton("Edit Entry");
            JButton deleteBtn = new JButton("Delete Entry");
            editBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);
            deleteBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);
            dialogPanel.add(editBtn);
            dialogPanel.add(deleteBtn);

            Window window = SwingUtilities.getWindowAncestor(panel);
            JDialog dialog;
            if (window instanceof Frame frame) {
                dialog = new JDialog(frame, "Entry Actions", true);
            } else if (window instanceof Dialog dialog1) {
                dialog = new JDialog(dialog1, "Entry Actions", true);
            } else {
                dialog = new JDialog((Frame) null, "Entry Actions", true);
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
                    router.showWeeklyView(entry.getDate());
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
                if (entry instanceof Event ev) {
                    dialogPanel.add(new JLabel("Venue: " + ev.getVenue()));
                    dialogPanel.add(new JLabel("Organizer: " + ev.getOrganizer()));
                    dialogPanel.add(new JLabel("Start Time: " + ev.getStartTime()));
                    dialogPanel.add(new JLabel("End Time: " + ev.getEndTime()));
                } else if (entry instanceof Meeting mt) {
                    dialogPanel.add(new JLabel("Modality: " + mt.getModality()));
                    dialogPanel.add(new JLabel("Venue: " + (mt.getVenue() != null ? mt.getVenue() : "")));
                    dialogPanel.add(new JLabel("Link: " + (mt.getLink() != null ? mt.getLink() : "")));
                    dialogPanel.add(new JLabel("Start Time: " + mt.getStartTime()));
                    dialogPanel.add(new JLabel("End Time: " + mt.getEndTime()));
                } else if (entry instanceof Task tk) {
                    dialogPanel.add(new JLabel("Priority: " + tk.getPriority()));
                    dialogPanel.add(new JLabel("Status: " + tk.getStatus()));
                    dialogPanel.add(new JLabel("Created By: " + tk.getCreatedBy()));
                    dialogPanel.add(new JLabel("Finished By: " + (tk.getFinishedBy() != null ? tk.getFinishedBy() : "")));
                }
                JDialog dialog;
                Window window = SwingUtilities.getWindowAncestor(panel);
                if (window instanceof Frame frame) {
                    dialog = new JDialog(frame, "Entry Details", true);
                } else if (window instanceof Dialog dialog1) {
                    dialog = new JDialog(dialog1, "Entry Details", true);
                } else {
                    dialog = new JDialog((Frame) null, "Entry Details", true);
                }
                dialog.setContentPane(dialogPanel);
                dialog.setSize(320, 320);
                dialog.setLocationRelativeTo(panel);
                dialog.setVisible(true);
            }
        });

        return panel;
    }


    /**
     * Creates the footer panel with the "Add Entry" button.
     *
     * @return the footer panel.
     */
    private JPanel createFooter() {
        Theme theme = logicController.getCurrentTheme();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
        panel.setOpaque(false);

        JButton addBtn = new JButton("Add Entry");
        addBtn.setFont(theme.getButtonFont());
        addBtn.setBackground(theme.getAccentColor());
        addBtn.setForeground(theme.getButtonTextColor());
        addBtn.setFocusPainted(false);
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener((var e) -> {
            // Show EntryForm for this date
            if (closeDialogOnAdd) {
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window instanceof JDialog jDialog) {
                    jDialog.dispose();
                }
            }
            router.showEntryForm(null, this.date);
        });

        panel.add(addBtn, BorderLayout.CENTER);
        return panel;
    }
}
