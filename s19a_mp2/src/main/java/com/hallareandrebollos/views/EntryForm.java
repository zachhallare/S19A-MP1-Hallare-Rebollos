package com.hallareandrebollos.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.BorderFactory;
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
    private JPanel headerPanel;
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
    private JButton[] typeButtons;


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
        this.typeButtons = new JButton[4];

        initializeLayout();

        // Default to Event Type or Editing Entry Type.
        if (this.editingEntry != null) {
            int editIdx = getTabIndexForType(editingEntry.getType());
            this.currentIdx = editIdx;
            redrawForm(editIdx);
            populateFieldsForEdit();
        } else {
            this.currentIdx = 0;
            redrawForm(0);
        }
        
        updateButtonStyles();
    }


    private void initializeLayout() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Create header panel with instruction and buttons
        this.headerPanel = createHeaderPanel();
        add(this.headerPanel, BorderLayout.NORTH);
        
        // Create scrollable form panel
        this.formPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(this.formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create bottom action buttons
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(248, 249, 250));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        // Title and instruction
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(this.editingEntry == null ? "Create New Entry" : "Edit Entry");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        
        JLabel instructionLabel = new JLabel("Select the type of entry you want to create:");
        instructionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        instructionLabel.setForeground(new Color(102, 102, 102));
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        titlePanel.add(instructionLabel, BorderLayout.CENTER);
        
        header.add(titlePanel, BorderLayout.NORTH);
        
        // Button panel
        this.buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        this.buttonPanel.setOpaque(false);
        
        String[] entryTypes = {"Event", "Journal", "Meeting", "Task"};
        
        for (int i = 0; i < entryTypes.length; i++) {
            JButton typeBtn = new JButton(entryTypes[i]);
            final int idx = i;
            
            typeBtn.setFocusPainted(false);
            typeBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
            typeBtn.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
            typeBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            
            typeBtn.addActionListener(e -> {
                this.currentIdx = idx;
                redrawForm(idx);
                updateButtonStyles();
            });
            
            this.typeButtons[i] = typeBtn;
            this.buttonPanel.add(typeBtn);
        }
        
        header.add(this.buttonPanel, BorderLayout.CENTER);
        return header;
    }

    private void updateButtonStyles() {
        Color[] activeColors = {
            new Color(59, 130, 246),   // Blue for Event
            new Color(34, 197, 94),    // Green for Journal  
            new Color(251, 191, 36),   // Yellow for Meeting
            new Color(239, 68, 68)     // Red for Task
        };
        
        for (int i = 0; i < this.typeButtons.length; i++) {
            if (i == this.currentIdx) {
                this.typeButtons[i].setBackground(activeColors[i]);
                this.typeButtons[i].setForeground(Color.WHITE);
            } else {
                this.typeButtons[i].setBackground(Color.WHITE);
                this.typeButtons[i].setForeground(new Color(107, 114, 128));
                this.typeButtons[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                    BorderFactory.createEmptyBorder(11, 23, 11, 23)
                ));
            }
        }
    }


    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        bottomPanel.setBackground(new Color(248, 249, 250));
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton submitBtn = new JButton(this.editingEntry == null ? "Create Entry" : "Update Entry");
        submitBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        submitBtn.setBackground(new Color(16, 185, 129));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setBorder(BorderFactory.createEmptyBorder(12, 32, 12, 32));
        submitBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        submitBtn.addActionListener(e -> handleSubmitAndReturnToEntries());
        bottomPanel.add(submitBtn);

        JButton returnBtn = new JButton("Return");
        returnBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        returnBtn.setBackground(new Color(107, 114, 128));
        returnBtn.setForeground(Color.WHITE);
        returnBtn.setFocusPainted(false);
        returnBtn.setBorder(BorderFactory.createEmptyBorder(12, 32, 12, 32));
        returnBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        returnBtn.addActionListener(e -> handleReturnToCalendar());
        bottomPanel.add(returnBtn);

        return bottomPanel;
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

        // Set Background Color Based on Entry Type.
        Color[] bgColors = {
            new Color(239, 246, 255),   // Event - Light Blue
            new Color(240, 253, 244),   // Journal - Light Green
            new Color(255, 251, 235),   // Meeting - Light Yellow
            new Color(254, 242, 242)    // Task - Light Red
        };

        Color bgColor = bgColors[idx];
        formPanel.setBackground(bgColor);
        setBackground(bgColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 40, 15, 40);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 40, 15, 40);

        // Common Fields.
        addSectionHeader("Basic Information", gbc);
        titleField = addLabeledTextField("Title:", gbc, true);

        // Desc Field: required for Journal, optional for the rest.
        boolean descRequired = (idx == 1);
        descField = addLabeledTextArea("Description:", gbc, descRequired);
        
        addDateField("Date:", gbc);

        // Type-Specific Fields.
        switch (idx) {
            case 0 -> {
                addSectionHeader("Event Details", gbc);
                addEventFields(gbc);
            }
            case 1 -> {
                // Journal has no extra fields, but we can add a note
                addSectionNote("Journals capture your daily thoughts and experiences.", gbc);
            }
            case 2 -> {
                addSectionHeader("Meeting Details", gbc);
                addMeetingFields(gbc);
            }
            case 3 -> {
                addSectionHeader("Task Details", gbc);
                addTaskFields(gbc);
            }
        }

        formPanel.revalidate();
        formPanel.repaint();
    }


    private void addSectionHeader(String headerText, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 40, 10, 40);
        
        JLabel header = new JLabel(headerText);
        header.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        header.setForeground(new Color(75, 85, 99));
        formPanel.add(header, gbc);
        
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 40, 8, 40);
    }


    private void addSectionNote(String noteText, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 40, 20, 40);
        
        JLabel note = new JLabel("<html><i>" + noteText + "</i></html>");
        note.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 13));
        note.setForeground(new Color(107, 114, 128));
        formPanel.add(note, gbc);
        
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 40, 8, 40);
    }


    private JTextField addLabeledTextField(String labelText, GridBagConstraints gbc, boolean required) {
        JLabel label = new JLabel(labelText + (required ? " *" : ""));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        label.setForeground(new Color(55, 65, 81));
        gbc.gridx = 0;
        gbc.weightx = 0;
        formPanel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        JTextField field = new JTextField(30);
        field.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1), 
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(Color.WHITE);
        formPanel.add(field, gbc);
        gbc.gridy++;
        
        return field;
    }


    private JTextField addLabeledTextField(String labelText, GridBagConstraints gbc) {
        return addLabeledTextField(labelText, gbc, false);
    }


    private JTextArea addLabeledTextArea(String labelText, GridBagConstraints gbc, boolean required) {
        JLabel label = new JLabel(labelText + (required ? " *" : ""));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        label.setForeground(new Color(55, 65, 81));
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        JTextArea area = new JTextArea(4, 30);
        area.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(Color.WHITE);
        
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219), 1));
        scroll.setPreferredSize(new Dimension(350, 100));
        formPanel.add(scroll, gbc);
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        return area;
    }


    private void addDateField(String labelText, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText + " *");
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        label.setForeground(new Color(55, 65, 81));
        gbc.gridx = 0;
        gbc.weightx = 0;
        formPanel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        this.dateSelector = new yyyymmddSelector();
        
        // Set date based on context
        if (this.editingEntry != null) {
            this.dateSelector.setSelectedDate(this.editingEntry.getDate());
        } else if (this.preselectedDate != null) {
            this.dateSelector.setSelectedDate(this.preselectedDate);
        } else {
            this.dateSelector.setSelectedDate(LocalDate.now());
        }
        
        formPanel.add(this.dateSelector, gbc);
        gbc.gridy++;
    }


    private JComboBox<String> addLabeledComboBox(String labelText, String[] options, GridBagConstraints gbc, boolean required) {
        JLabel label = new JLabel(labelText + (required ? " *" : ""));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        label.setForeground(new Color(55, 65, 81));
        gbc.gridx = 0;
        gbc.weightx = 0;
        formPanel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        JComboBox<String> combo = new JComboBox<>(options);
        combo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219), 1));
        formPanel.add(combo, gbc);
        gbc.gridy++;
        
        return combo;
    }


    private void addTimeField(String labelText, hhmmSelector selector, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        label.setForeground(new Color(55, 65, 81));
        gbc.gridx = 0;
        gbc.weightx = 0;
        formPanel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(selector, gbc);
        gbc.gridy++;
    }



    private void addEventFields(GridBagConstraints gbc) {
        venueField = addLabeledTextField("Venue:", gbc, true);
        
        // Organizer field.
        JLabel orgLabel = new JLabel("Organizer: *");
        orgLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        orgLabel.setForeground(new Color(55, 65, 81));
        gbc.gridx = 0;
        gbc.weightx = 0;
        formPanel.add(orgLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        String organizerName = logic.getCurrentAccount() != null ? logic.getCurrentAccount().getUsername() : "";
        this.organizerField = new JTextField(organizerName, 30);
        this.organizerField.setEditable(false);
        this.organizerField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        this.organizerField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        this.organizerField.setBackground(new Color(249, 250, 251));
        formPanel.add(this.organizerField, gbc);
        gbc.gridy++;

        this.startTimeSelector = new hhmmSelector();
        addTimeField("Start Time:", this.startTimeSelector, gbc);
        
        this.endTimeSelector = new hhmmSelector();
        addTimeField("End Time:", this.endTimeSelector, gbc);
    }


    private void addMeetingFields(GridBagConstraints gbc) {
        this.modalityField = addLabeledComboBox("Modality:", new String[]{"Online", "Onsite", "Hybrid"}, gbc, true);
        this.meetingVenueField = addLabeledTextField("Venue (optional):", gbc);
        this.linkField = addLabeledTextField("Link (optional):", gbc);
        
        this.startTimeSelector = new hhmmSelector();
        addTimeField("Start Time:", this.startTimeSelector, gbc);
        
        this.endTimeSelector = new hhmmSelector();
        addTimeField("End Time:", this.endTimeSelector, gbc);
    }


    private void addTaskFields(GridBagConstraints gbc) {
        this.priorityBox = addLabeledComboBox("Priority:", new String[]{"High", "Medium", "Low"}, gbc, true);
        this.statusBox = addLabeledComboBox("Status:", new String[]{"Pending", "Done"}, gbc, true);
        this.createdByField = addLabeledTextField("Created By:", gbc, true);
        this.finishedByField = addLabeledTextField("Finished By (optional):", gbc);
    }


    private void populateFieldsForEdit() {
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
                // No extra fields.
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


    private boolean validateRequiredFields() {
        ArrayList<String> missingFields = new ArrayList<>();
        boolean isValid = true;

        // Common required fields.
        if (titleField.getText().trim().isEmpty()) {
            missingFields.add("Title");
        }
        
        if (dateSelector.getSelectedDate() == null) {
            missingFields.add("Date");
        }

        // Type-specific validation.
        switch (this.currentIdx) {
            case 0: // Event
                if (venueField.getText().trim().isEmpty()) {
                    missingFields.add("Venue");
                }
                if (organizerField.getText().trim().isEmpty()) {
                    missingFields.add("Organizer");
                }
                break;
                
            case 1: // Journal
                if (descField.getText().trim().isEmpty()) {
                    missingFields.add("Description");
                }
                break;
                
            case 2: // Meeting
                if (modalityField.getSelectedItem() == null || 
                    modalityField.getSelectedItem().toString().trim().isEmpty()) {
                    missingFields.add("Modality");
                }
                break;
                
            case 3: // Task
                if (priorityBox.getSelectedItem() == null || 
                    priorityBox.getSelectedItem().toString().trim().isEmpty()) {
                    missingFields.add("Priority");
                }
                if (statusBox.getSelectedItem() == null || 
                    statusBox.getSelectedItem().toString().trim().isEmpty()) {
                    missingFields.add("Status");
                }
                if (createdByField.getText().trim().isEmpty()) {
                    missingFields.add("Created By");
                }
                break;
        }
        
        if (!missingFields.isEmpty()) {
            String message = "Please fill in the following required fields:\n* " + 
                            String.join("\n* ", missingFields);
            JOptionPane.showMessageDialog(this, message, "Required Fields Missing", JOptionPane.WARNING_MESSAGE);
            isValid = false;
        }

        return isValid;
    }


    private void handleSubmitAndReturnToEntries() {
        boolean fieldsValid = validateRequiredFields();

        if (fieldsValid) {
            String title = this.titleField.getText().trim();
            String desc = this.descField.getText().trim();
            LocalDate date = this.dateSelector.getSelectedDate();
            if (date == null) date = LocalDate.now();

            Entry newEntry = null;
            boolean canProceed = true;
            String errorMessage = "";

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
                        canProceed = false;
                    } else if (journalExists) {
                        JOptionPane.showMessageDialog(this, "A journal already exists for this date.", "Error", JOptionPane.ERROR_MESSAGE);
                        canProceed = false;
                    } else {
                        newEntry = new Journal(title, date, desc);
                    }
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

            if (canProceed && newEntry != null) {
                if (this.editingEntry == null) {
                    logic.getCurrentCalendarObject().addEntry(newEntry);
                    JOptionPane.showMessageDialog(this, "Entry created!");
                } else {
                    // Replace logic: remove old, add new
                    logic.getCurrentCalendarObject().removeEntry(this.editingEntry);
                    logic.getCurrentCalendarObject().addEntry(newEntry);
                    JOptionPane.showMessageDialog(this, "Entry updated!");
                }
                clearFields();
                router.showWeeklyView(date);
            } else if (!canProceed) {
                JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            }
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
