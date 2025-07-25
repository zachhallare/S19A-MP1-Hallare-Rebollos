// package com.hallareandrebollos.legacy_code;

// import java.awt.BorderLayout;
// import java.awt.Color;
// import java.awt.FlowLayout;
// import java.awt.Font;
// import java.time.LocalDate;
// import java.time.LocalTime;
// import java.util.ArrayList;

// import javax.swing.BorderFactory;
// import javax.swing.Box;
// import javax.swing.BoxLayout;
// import javax.swing.JButton;
// import javax.swing.JLabel;
// import javax.swing.JOptionPane;
// import javax.swing.JPanel;
// import javax.swing.JScrollPane;
// import javax.swing.JTextArea;
// import javax.swing.JTextField;
// import javax.swing.SwingConstants;

// import com.hallareandrebollos.controls.LogicController;
// import com.hallareandrebollos.controls.Router;
// import com.hallareandrebollos.models.Entry;
// import com.hallareandrebollos.models.Event;
// import com.hallareandrebollos.models.Journal;
// import com.hallareandrebollos.models.Meeting;
// import com.hallareandrebollos.models.Task;

// public class EntryFormPage extends JPanel {

//     public EntryFormPage(Router router, LogicController logic, String entryType, String day, String month, String year) {
//         setLayout(new BorderLayout());
//         setBackground(new Color(0xE0E0E0));

//         JLabel titleLabel = new JLabel("Create " + entryType, SwingConstants.CENTER);
//         titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
//         add(titleLabel, BorderLayout.NORTH);

//         JPanel formPanel = new JPanel();
//         formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
//         formPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
//         formPanel.setBackground(Color.LIGHT_GRAY);

//         JTextField titleField = new JTextField();
//         JTextArea descriptionField = new JTextArea(3, 20);
//         descriptionField.setLineWrap(true);
//         descriptionField.setWrapStyleWord(true);

//         formPanel.add(new JLabel("Title:"));
//         formPanel.add(titleField);
//         formPanel.add(Box.createVerticalStrut(10));
//         formPanel.add(new JLabel("Description:"));
//         formPanel.add(new JScrollPane(descriptionField));
//         formPanel.add(Box.createVerticalStrut(10));

//         // Extra fields
//         JTextField field1 = new JTextField(); 
//         JTextField field2 = new JTextField();
//         JTextField field3 = new JTextField();
//         JTextField field4 = new JTextField();

//         if (entryType.equals("Task")) {
//             formPanel.add(new JLabel("Priority (High/Medium/Low):"));
//             formPanel.add(field1);
//             formPanel.add(new JLabel("Status (Pending/Done):"));
//             formPanel.add(field2);
//             formPanel.add(new JLabel("Created By:"));
//             formPanel.add(field3);
//             formPanel.add(new JLabel("Finished By (optional):"));
//             formPanel.add(field4);

//         } else if (entryType.equals("Event")) {
//             formPanel.add(new JLabel("Venue:"));
//             formPanel.add(field1);
//             formPanel.add(new JLabel("Organizer:"));
//             formPanel.add(field2);
//             formPanel.add(new JLabel("Start Time (HH:MM):"));
//             formPanel.add(field3);
//             formPanel.add(new JLabel("End Time (HH:MM):"));
//             formPanel.add(field4);
        
//         } else if (entryType.equals("Meeting")) {
//             formPanel.add(new JLabel("Modality (Online/Physical):"));
//             formPanel.add(field1);
//             formPanel.add(new JLabel("Venue (optional):"));
//             formPanel.add(field2);
//             formPanel.add(new JLabel("Link (optional):"));
//             formPanel.add(field3);
//         }

//         add(formPanel, BorderLayout.CENTER);


//         // Buttons bruh.
//         JPanel buttonPanel = new JPanel(new FlowLayout());
//         buttonPanel.setBackground(Color.LIGHT_GRAY);
//         JButton submitButton = new JButton("Submit");
//         JButton cancelButton = new JButton("Cancel");
//         buttonPanel.add(submitButton);
//         buttonPanel.add(cancelButton);
//         add(buttonPanel, BorderLayout.SOUTH);

//         // Date construction
//         LocalDate date = LocalDate.of(
//             Integer.parseInt(year),
//             java.time.Month.valueOf(month.toUpperCase()),
//             Integer.parseInt(day)
//         );

//         // cancel ts burhgv
//         // cancelButton.addActionListener(e -> router.showAddEntryPage(day, month, year));

//         // submist the siht.
//         submitButton.addActionListener(e -> {
//             String title = titleField.getText().trim();
//             String description = descriptionField.getText().trim();

//             Entry newEntry = null;

//             try {
//                 if (entryType.equals("Task")) {
//                     String priority = field1.getText().trim();
//                     String status = field2.getText().trim();
//                     String createdBy = field3.getText().trim();
//                     String finishedBy = field4.getText().trim();

//                     if (finishedBy.isEmpty()) {
//                         newEntry = new Task(title, date, description, priority, status, createdBy);
//                     } else {
//                         newEntry = new Task(title, date, description, priority, status, createdBy, finishedBy);
//                     }

//                 } else if (entryType.equals("Event")) {
//                     String venue = field1.getText().trim();
//                     String organizer = field2.getText().trim();
//                     String startStr = field3.getText().trim();
//                     String endStr = field4.getText().trim();

//                     LocalTime startTime = LocalTime.parse(startStr);
//                     LocalTime endTime = LocalTime.parse(endStr);

//                     newEntry = new Event(title, date, description, venue, organizer, startTime, endTime);

//                 } else if (entryType.equals("Meeting")) {
//                     String modality = field1.getText().trim();
//                     String venue = field2.getText().trim();
//                     String link = field3.getText().trim();

//                     if (venue.isEmpty() && link.isEmpty()) {
//                         newEntry = new Meeting(title, date, description, modality);
//                     } else {
//                         newEntry = new Meeting(title, date, description, modality, venue, link);
//                     }

//                 } else if (entryType.equals("Journal")) {
//                     newEntry = new Journal(title, date, description);
//                 }

//                 if (newEntry != null) {
//                     logic.getCurrentCalendarObject().addEntry(newEntry);
//                     JOptionPane.showMessageDialog(this, "Entry created!");

//                     // Reload entries for that day
//                     ArrayList<Entry> entriesForDay = new ArrayList<Entry>();
//                     for (Entry entry : logic.getCurrentCalendarObject().getEntries()) {
//                         LocalDate entryDate = entry.getDate();
//                         if (entryDate.equals(date)) {
//                             entriesForDay.add(entry);
//                         }
//                     }

//                     router.showEntriesPage(day, month, year, entriesForDay);
//                 }

//             } catch (Exception ex) {
//                 JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
//             }
//         });
//     }
// }
