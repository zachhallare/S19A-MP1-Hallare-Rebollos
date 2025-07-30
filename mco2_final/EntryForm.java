package mco2_final;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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

/**
 * EntryForm is a JPanel used to create or edit various types of entries including
 * Events, Journals, Meetings, and Tasks. It dynamically adjusts the form fields
 * based on the selected entry type and handles validation, submission, and navigation.
 */
public class EntryForm extends JPanel {

    /** Controller to manage logic and model operations */
    private final LogicController logic;

    /** Router for navigation between views */
    private final Router router;

    /** Panel that holds the form's header (title, instructions, buttons) */
    private JPanel headerPanel;

    /** Button group for selecting entry types */
    private JPanel buttonPanel;

    /** Main form panel holding the dynamic input fields */
    private JPanel formPanel;

    /** If editing, this holds the entry to edit */
    private Entry editingEntry;

    // Common fields
    /** Field for entry title */
    private JTextField titleField;

    /** Text area for entry description */
    private JTextArea descField;

    /** Date selector widget */
    private yyyymmddSelector dateSelector;

    /** If provided, pre-fills the date selector */
    private LocalDate preselectedDate = null;

    // Event fields
    /** Venue of the event. */
    private JTextField venueField;

    /** Organizer of the event. */
    private JTextField organizerField;

    /** Start time of the event. */
    private hhmmSelector startTimeSelector;

    /** End time of the event. */
    private hhmmSelector endTimeSelector;

    // Meeting fields
    /** Modality of the meeting (e.g., online or face-to-face). */
    private JComboBox<String> modalityField;

    /** Venue of the meeting (if physical). */
    private JTextField meetingVenueField;
    
    /** Online meeting link (if applicable). */
    private JTextField linkField;

    // Task fields
    /** Priority level of the task. */
    private JComboBox<String> priorityBox;

    /** Current status of the task. */
    private JComboBox<String> statusBox;

    /** Creator of the task. */
    private JTextField createdByField;

    /** Person assigned to finish the task. */
    private JTextField finishedByField;

    /** Currently selected entry type index (0 = Event, 1 = Journal, ...) */
    private int currentIdx;

    /** Array of entry type buttons */
    private JButton[] typeButtons;


    /**
     * Constructs an empty EntryForm for creating a new entry.
     *
     * @param router the navigation router
     * @param logic the logic controller
     */
    public EntryForm(Router router, LogicController logic) {
        this(router, logic, null, null);
    }


    /**
     * Constructs a pre-filled EntryForm for editing an existing entry.
     *
     * @param router the navigation router
     * @param logic the logic controller
     * @param entryToEdit the entry to edit
     */
    public EntryForm(Router router, LogicController logic, Entry entryToEdit) {
        this(router, logic, entryToEdit, null);
    }


    /**
     * Constructs a new EntryForm with optional entry and preselected date.
     *
     * @param router the navigation router
     * @param logic the logic controller
     * @param entryToEdit entry to edit, or null
     * @param preselectedDate date to pre-fill the date selector
     */
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


    /**
     * Initializes layout structure of EntryForm.
     */
    private void initializeLayout() {
        Theme theme = logic.getCurrentTheme();
        
        setLayout(new BorderLayout());
        setBackground(theme.getBackgroundColor());
        
        // Create header panel with instruction and buttons
        this.headerPanel = createHeaderPanel();
        add(this.headerPanel, BorderLayout.NORTH);
        
        // Create scrollable form panel
        this.formPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(this.formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(theme.getBackgroundColor());
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create bottom action buttons
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }


    /**
     * Creates the top header panel with entry type selection buttons.
     * @return the header JPanel
     */
    private JPanel createHeaderPanel() {
        Theme theme = logic.getCurrentTheme();
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(theme.getPanelColor());
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, theme.getBorderColor()),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        // Title and instruction
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(this.editingEntry == null ? "Create New Entry" : "Edit Entry");
        titleLabel.setFont(theme.getTitleFont());
        titleLabel.setForeground(theme.getTextColor());
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        
        JLabel instructionLabel = new JLabel("Select the type of entry you want to create:");
        instructionLabel.setFont(theme.getRegularFont());
        instructionLabel.setForeground(theme.getSubtitleColor());
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
            typeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
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


    /**
     * Updates the button styles to reflect the currently selected type.
     */
    private void updateButtonStyles() {
        Theme theme = logic.getCurrentTheme();
        Color[] activeColors = {
            theme.getAccentColor(),     // Event
            theme.getAccentColor(),     // Journal  
            theme.getAccentColor(),     // Meeting
            theme.getAccentColor()      // Task
        };
        
        for (int i = 0; i < this.typeButtons.length; i++) {
            if (i == this.currentIdx) {
                this.typeButtons[i].setBackground(activeColors[i]);
                this.typeButtons[i].setForeground(theme.getButtonTextColor());
            } else {
                this.typeButtons[i].setBackground(theme.getPanelColor());
                this.typeButtons[i].setForeground(theme.getSubtitleColor());
                this.typeButtons[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(theme.getBorderColor(), 1),
                    BorderFactory.createEmptyBorder(11, 23, 11, 23)
                ));
            }
        }
    }


    /**
     * Creates bottom panel with "Create/Update" and "Return" buttons.
     * @return the JPanel to be added to the south
     */
    private JPanel createBottomPanel() {
        Theme theme = logic.getCurrentTheme();
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        bottomPanel.setBackground(theme.getPanelColor());
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, theme.getBorderColor()));

        JButton submitBtn = new JButton(this.editingEntry == null ? "Create Entry" : "Update Entry");
        submitBtn.setFont(theme.getButtonFont());
        submitBtn.setBackground(theme.getAccentColor());
        submitBtn.setForeground(theme.getButtonTextColor());
        submitBtn.setFocusPainted(false);
        submitBtn.setBorder(BorderFactory.createEmptyBorder(12, 32, 12, 32));
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.addActionListener(e -> handleSubmitAndReturnToEntries());
        bottomPanel.add(submitBtn);

        JButton returnBtn = new JButton("Return");
        returnBtn.setFont(theme.getButtonFont());
        returnBtn.setBackground(theme.getSecondaryButtonColor());
        returnBtn.setForeground(theme.getButtonTextColor());
        returnBtn.setFocusPainted(false);
        returnBtn.setBorder(BorderFactory.createEmptyBorder(12, 32, 12, 32));
        returnBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        returnBtn.addActionListener(e -> handleReturnToCalendar());
        bottomPanel.add(returnBtn);

        return bottomPanel;
    }


    /**
     * Returns the index for a given entry type string.
     * @param type the entry type string
     * @return the index of the type
     */
    private int getTabIndexForType(String type) {
        switch (type) {
            case "Event": return 0;
            case "Journal": return 1;
            case "Meeting": return 2;
            case "Task": return 3;
            default: return 0;
        }
    }


    /**
     * Redraws the form fields based on the selected entry type.
     * @param idx index of the selected entry type
     */
    private void redrawForm(int idx) {
        formPanel.removeAll();
        formPanel.setLayout(new GridBagLayout());

        // Set Background Color Based on Entry Type.
        Theme theme = logic.getCurrentTheme();
        Color bgColor = theme.getBackgroundColor();
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


    /**
     * Adds a section header label to the form.
     * @param headerText the header text
     * @param gbc the layout constraints
     */
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


    /**
     * Adds a section note label (italic) to the form.
     * @param noteText the note text
     * @param gbc the layout constraints
     */
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



    /**
     * Adds a labeled JTextField to the form.
     * @param labelText the label
     * @param gbc constraints
     * @param required if the field is required
     * @return the created JTextField
     */
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


    /**
     * Overload for non-required text field.
     */
    private JTextField addLabeledTextField(String labelText, GridBagConstraints gbc) {
        return addLabeledTextField(labelText, gbc, false);
    }


    /**
     * Adds a labeled JTextArea to the form.
     * @param labelText the label
     * @param gbc constraints
     * @param required whether it's required
     * @return the created JTextArea
     */
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


    /**
     * Adds a labeled date field using a yyyymmddSelector.
     */
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


    /**
     * Adds a labeled JComboBox to the form.
     * @param labelText label text
     * @param options options to select
     * @param gbc constraints
     * @param required if field is required
     * @return the JComboBox created
     */
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


    /**
     * Adds a labeled hhmmSelector time input to the form.
     */
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


    /**
     * Adds input fields specific to Event entries.
     */
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


    /**
     * Adds input fields specific to Meeting entries.
     */
    private void addMeetingFields(GridBagConstraints gbc) {
        this.modalityField = addLabeledComboBox("Modality:", new String[]{"Online", "Onsite", "Hybrid"}, gbc, true);
        this.meetingVenueField = addLabeledTextField("Venue (optional):", gbc);
        this.linkField = addLabeledTextField("Link (optional):", gbc);
        
        this.startTimeSelector = new hhmmSelector();
        addTimeField("Start Time:", this.startTimeSelector, gbc);
        
        this.endTimeSelector = new hhmmSelector();
        addTimeField("End Time:", this.endTimeSelector, gbc);
    }


    /**
     * Adds input fields specific to Task entries.
     */
    private void addTaskFields(GridBagConstraints gbc) {
        this.priorityBox = addLabeledComboBox("Priority:", new String[]{"High", "Medium", "Low"}, gbc, true);
        this.statusBox = addLabeledComboBox("Status:", new String[]{"Pending", "Done"}, gbc, true);
        this.createdByField = addLabeledTextField("Created By:", gbc, true);
        this.finishedByField = addLabeledTextField("Finished By (optional):", gbc);
    }


    /**
     * Populates fields with data from the entry being edited.
     */
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


    /**
     * Validates the required fields based on entry type.
     * @return true if all required fields are valid, false otherwise
     */
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


    /**
     * Submits the entry (create or update) and returns to entries view.
     */
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

    
    /**
     * Returns to the calendar view without saving changes.
     */
    private void handleReturnToCalendar() {
        clearFields();
        router.showCalendarPage(logic.getCurrentCalendarObject());
    }


    /**
     * Clears all input fields and resets form state.
     */
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
