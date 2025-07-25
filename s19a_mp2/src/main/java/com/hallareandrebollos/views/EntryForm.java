
package com.hallareandrebollos.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.hallareandrebollos.controls.LogicController;
import com.hallareandrebollos.controls.Router;
import com.hallareandrebollos.models.Entry;
import com.hallareandrebollos.models.Event;
import com.hallareandrebollos.models.Journal;
import com.hallareandrebollos.models.Meeting;
import com.hallareandrebollos.models.Task;
import com.hallareandrebollos.widgets.hhmmSelector;
import com.hallareandrebollos.widgets.yyyymmddSelector;

public class EntryForm extends JPanel {
    private final LogicController logic;
    private final Router router;
    private JPanel buttonPanel;
    private JPanel formPanel;
    private Entry editingEntry;

    // Common fields
    private JTextField titleField;
    private JTextArea descField;
    private yyyymmddSelector dateSelector;
    private LocalDate preselectedDate = null;

    // Event fields
    private JTextField venueField, organizerField;
    private hhmmSelector startTimeSelector, endTimeSelector;

    // Meeting fields
    private JComboBox<String> modalityField;
    private JTextField meetingVenueField, linkField;

    // Task fields
    private JComboBox<String> priorityBox, statusBox;
    private JTextField createdByField, finishedByField;

    private int currentIdx;

    public EntryForm(Router router, LogicController logic) {
        this(router, logic, null, null);
    }

    public EntryForm(Router router, LogicController logic, Entry entryToEdit) {
        this(router, logic, entryToEdit, null);
    }

    public EntryForm(Router router, LogicController logic, Entry entryToEdit, LocalDate preselectedDate) {
        this.logic = logic;
        this.router = router;
        this.editingEntry = entryToEdit;
        this.preselectedDate = preselectedDate;
        setLayout(new BorderLayout());

        // Top button panel for entry type selection
        this.buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        String[] entryTypes = {"Event", "Journal", "Meeting", "Task"};
        for (int i = 0; i < entryTypes.length; i++) {
            JButton typeBtn = new JButton(entryTypes[i]);
            final int idx = i;
            typeBtn.addActionListener(e -> {
                this.currentIdx = idx;
                redrawForm(idx);
            });
            this.buttonPanel.add(typeBtn);
        }
        add(this.buttonPanel, BorderLayout.NORTH);

        this.formPanel = new JPanel();
        add(this.formPanel, BorderLayout.CENTER);

        // Default to Event type
        redrawForm(0);

        JPanel bottomPanel = new JPanel();
        JButton submitBtn = new JButton(this.editingEntry == null ? "Create Entry" : "Update Entry");
        submitBtn.addActionListener(e -> handleSubmitAndReturnToEntries());
        bottomPanel.add(submitBtn);

        JButton returnBtn = new JButton("Return");
        returnBtn.addActionListener(e -> handleReturnToCalendar());
        bottomPanel.add(returnBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        if (this.editingEntry != null) {
            redrawForm(getTabIndexForType(editingEntry.getType()));
            populateFieldsForEdit();
        }
    }

    private int getTabIndexForType(String type) {
        switch (type) {
            case "Event": return 0;
            case "Journal": return 1;
            case "Meeting": return 2;
            case "Task": return 3;
            default: return 0;
        }
    }

    private void redrawForm(int idx) {
        formPanel.removeAll();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        // Title
        gbc.gridx = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        this.titleField = new JTextField(20);
        formPanel.add(this.titleField, gbc);
        gbc.gridy++;

        // Description
        gbc.gridx = 0;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        this.descField = new JTextArea(3, 20);
        this.descField.setLineWrap(true);
        this.descField.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(this.descField);
        formPanel.add(descScroll, gbc);
        gbc.gridy++;

        // Date
        gbc.gridx = 0;
        formPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        this.dateSelector = new yyyymmddSelector();
        // If editing, use entry date; else, use preselectedDate if set
        if (this.editingEntry != null) {
            this.dateSelector.setSelectedDate(this.editingEntry.getDate());
        } else if (this.preselectedDate != null) {
            this.dateSelector.setSelectedDate(this.preselectedDate);
        } else {
            this.dateSelector.setSelectedDate(LocalDate.now());
        }
        formPanel.add(this.dateSelector, gbc);
        gbc.gridy++;

        switch (idx) {
            case 0: // Event
                gbc.gridx = 0;
                formPanel.add(new JLabel("Venue:"), gbc);
                gbc.gridx = 1;
                this.venueField = new JTextField(20);
                formPanel.add(this.venueField, gbc);
                gbc.gridy++;

                gbc.gridx = 0;
                formPanel.add(new JLabel("Organizer:"), gbc);
                gbc.gridx = 1;
                String organizerName = logic.getCurrentAccount() != null ? logic.getCurrentAccount().getUsername() : "";
                this.organizerField = new JTextField(organizerName, 20);
                this.organizerField.setEditable(false);
                formPanel.add(this.organizerField, gbc);
                gbc.gridy++;

                gbc.gridx = 0;
                formPanel.add(new JLabel("Start Time:"), gbc);
                gbc.gridx = 1;
                this.startTimeSelector = new hhmmSelector();
                formPanel.add(this.startTimeSelector, gbc);
                gbc.gridy++;

                gbc.gridx = 0;
                formPanel.add(new JLabel("End Time:"), gbc);
                gbc.gridx = 1;
                this.endTimeSelector = new hhmmSelector();
                formPanel.add(this.endTimeSelector, gbc);
                gbc.gridy++;
                break;
            case 1: // Journal
                // No extra fields
                break;
            case 2: // Meeting
                gbc.gridx = 0;
                formPanel.add(new JLabel("Modality:"), gbc);
                gbc.gridx = 1;
                this.modalityField = new JComboBox<>(new String[]{"Online", "Onsite", "Hybrid"}); 
                formPanel.add(this.modalityField, gbc);
                gbc.gridy++;

                gbc.gridx = 0;
                formPanel.add(new JLabel("Venue (optional):"), gbc);
                gbc.gridx = 1;
                this.meetingVenueField = new JTextField(20);
                formPanel.add(this.meetingVenueField, gbc);
                gbc.gridy++;

                gbc.gridx = 0;
                formPanel.add(new JLabel("Link (optional):"), gbc);
                gbc.gridx = 1;
                this.linkField = new JTextField(20);
                formPanel.add(this.linkField, gbc);
                gbc.gridy++;

                gbc.gridx = 0;
                formPanel.add(new JLabel("Start Time:"), gbc);
                gbc.gridx = 1;
                this.startTimeSelector = new hhmmSelector();
                formPanel.add(this.startTimeSelector, gbc);
                gbc.gridy++;

                gbc.gridx = 0;
                formPanel.add(new JLabel("End Time:"), gbc);
                gbc.gridx = 1;
                this.endTimeSelector = new hhmmSelector();
                formPanel.add(this.endTimeSelector, gbc);
                gbc.gridy++;
                break;
            case 3: // Task
                gbc.gridx = 0;
                formPanel.add(new JLabel("Priority:"), gbc);
                gbc.gridx = 1;
                this.priorityBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
                formPanel.add(this.priorityBox, gbc);
                gbc.gridy++;

                gbc.gridx = 0;
                formPanel.add(new JLabel("Status:"), gbc);
                gbc.gridx = 1;
                this.statusBox = new JComboBox<>(new String[]{"Pending", "Done"});
                formPanel.add(this.statusBox, gbc);
                gbc.gridy++;

                gbc.gridx = 0;
                formPanel.add(new JLabel("Created By:"), gbc);
                gbc.gridx = 1;
                this.createdByField = new JTextField(20);
                formPanel.add(this.createdByField, gbc);
                gbc.gridy++;

                gbc.gridx = 0;
                formPanel.add(new JLabel("Finished By (optional):"), gbc);
                gbc.gridx = 1;
                this.finishedByField = new JTextField(20);
                formPanel.add(this.finishedByField, gbc);
                gbc.gridy++;
                break;
        }
        formPanel.revalidate();
        formPanel.repaint();
    }

    private void populateFieldsForEdit() {
        int idx = getTabIndexForType(this.editingEntry.getType());
        redrawForm(idx);
        this.titleField.setText(this.editingEntry.getTitle());
        this.descField.setText(this.editingEntry.getDescription() != null ? this.editingEntry.getDescription() : "");
        this.dateSelector.setSelectedDate(this.editingEntry.getDate());

        switch (this.editingEntry.getType()) {
            case "Event":
                Event ev = (Event) this.editingEntry;
                this.venueField.setText(ev.getVenue());
                this.organizerField.setText(ev.getOrganizer());
                this.startTimeSelector.setSelectedTime(ev.getStartTime());
                this.endTimeSelector.setSelectedTime(ev.getEndTime());
                break;
            case "Journal":
                // No extra fields
                break;
            case "Meeting":
                Meeting mt = (Meeting) editingEntry;
                if (this.modalityField != null) {
                    this.modalityField.setSelectedItem(mt.getModality());
                }
                this.meetingVenueField.setText(mt.getVenue() != null ? mt.getVenue() : "");
                this.linkField.setText(mt.getLink() != null ? mt.getLink() : "");
                if (this.startTimeSelector != null) this.startTimeSelector.setSelectedTime(mt.getStartTime());
                if (this.endTimeSelector != null) this.endTimeSelector.setSelectedTime(mt.getEndTime());
                break;
            case "Task":
                Task tk = (Task) editingEntry;
                this.priorityBox.setSelectedItem(tk.getPriority());
                this.statusBox.setSelectedItem(tk.getStatus());
                this.createdByField.setText(tk.getCreatedBy());
                this.finishedByField.setText(tk.getFinishedBy() != null ? tk.getFinishedBy() : "");
                break;
        }
    }

    private void handleSubmitAndReturnToEntries() {
        String title = this.titleField.getText().trim();
        String desc = this.descField.getText().trim();
        LocalDate date = this.dateSelector.getSelectedDate();
        if (date == null) date = LocalDate.now();

        Entry newEntry = null;

        switch (this.currentIdx) {
            case 0: // Event
                String venue = this.venueField.getText().trim();
                String organizer = logic.getCurrentAccount() != null ? logic.getCurrentAccount().getUsername() : "";
                LocalTime start = this.startTimeSelector.getSelectedTime();
                LocalTime end = this.endTimeSelector.getSelectedTime();
                if (start == null) start = LocalTime.now();
                if (end == null) end = LocalTime.now();
                newEntry = new Event(title, date, desc, venue, organizer, start, end);
                break;
            case 1: // Journal
                // Only allow if calendar name matches account username
                String calendarName = logic.getCurrentCalendarObject() != null ? logic.getCurrentCalendarObject().getCalendarName() : "";
                String accountName = logic.getCurrentAccount() != null ? logic.getCurrentAccount().getUsername() : "";
                boolean isEditingJournal = (this.editingEntry != null && this.editingEntry instanceof Journal);
                boolean journalExists = false;
                if (!isEditingJournal) {
                    ArrayList<Entry> entries = logic.getCurrentCalendarObject() != null ? logic.getCurrentCalendarObject().getEntries() : new ArrayList<>();
                    for (Entry e : entries) {
                        if (e instanceof Journal && e.getDate().equals(date)) {
                            journalExists = true;
                            break;
                        }
                    }
                }
                if (!calendarName.equals(accountName)) {
                    JOptionPane.showMessageDialog(this, "Journals can only be created in a calendar named after your account.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (journalExists) {
                    JOptionPane.showMessageDialog(this, "A journal already exists for this date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                newEntry = new Journal(title, date, desc);
                break;
            case 2: // Meeting
                String modality = this.modalityField instanceof JComboBox ? (String)((JComboBox<?>)this.modalityField).getSelectedItem() : "";
                String mVenue = this.meetingVenueField.getText().trim();
                String link = this.linkField.getText().trim();
                LocalTime meetingStart = this.startTimeSelector != null ? this.startTimeSelector.getSelectedTime() : null;
                LocalTime meetingEnd = this.endTimeSelector != null ? this.endTimeSelector.getSelectedTime() : null;
                if (meetingStart == null) meetingStart = LocalTime.now();
                if (meetingEnd == null) meetingEnd = LocalTime.now();
                if (!mVenue.isEmpty() || !link.isEmpty())
                    newEntry = new Meeting(title, date, meetingStart, meetingEnd, desc, modality, mVenue.isEmpty() ? null : mVenue, link.isEmpty() ? null : link);
                else
                    newEntry = new Meeting(title, date, meetingStart, meetingEnd, desc, modality, null, null);
                break;
            case 3: // Task
                String priority = (String) this.priorityBox.getSelectedItem();
                String status = (String) this.statusBox.getSelectedItem();
                String createdBy = this.createdByField.getText().trim();
                String finishedBy = this.finishedByField.getText().trim();
                newEntry = !finishedBy.isEmpty()
                    ? new Task(title, date, desc, priority, status, createdBy, finishedBy)
                    : new Task(title, date, desc, priority, status, createdBy);
                break;
        }

        if (newEntry != null) {
            if (this.editingEntry == null) {
                logic.getCurrentCalendarObject().addEntry(newEntry);
                JOptionPane.showMessageDialog(this, "Entry created!");
            } else {
                // Replace logic: remove old, add new
                logic.getCurrentCalendarObject().removeEntry(this.editingEntry);
                logic.getCurrentCalendarObject().addEntry(newEntry);
                JOptionPane.showMessageDialog(this, "Entry updated!");
            }
            // Route to entries list page after submit
            String day = String.valueOf(date.getDayOfMonth());
            String month = String.valueOf(date.getMonthValue());
            String year = String.valueOf(date.getYear());
            ArrayList<Entry> entries = logic.getEntriesForDate(date);
            clearFields();
            router.showEntriesPage(day, month, year, entries);
        }
    }

    private void handleReturnToCalendar() {
        clearFields();
        router.showCalendarPage(logic.getCurrentCalendarObject());
    }

    private void clearFields() {
        this.titleField.setText("");
        this.descField.setText("");
        this.dateSelector.setSelectedDate(LocalDate.now());
        if (this.venueField != null) this.venueField.setText("");
        if (this.organizerField != null) this.organizerField.setText("");
        if (this.startTimeSelector != null) this.startTimeSelector.setSelectedTime(LocalTime.of(0,0));
        if (this.endTimeSelector != null) this.endTimeSelector.setSelectedTime(LocalTime.of(0,0));
        if (this.modalityField != null) this.modalityField.setSelectedIndex(0);
        if (this.meetingVenueField != null) this.meetingVenueField.setText("");
        if (this.linkField != null) this.linkField.setText("");
        if (this.priorityBox != null) this.priorityBox.setSelectedIndex(0);
        if (this.statusBox != null) this.statusBox.setSelectedIndex(0);
        if (this.createdByField != null) this.createdByField.setText("");
        if (this.finishedByField != null) this.finishedByField.setText("");
    }
}
