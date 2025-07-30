
package com.hallareandrebollos.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.hallareandrebollos.controls.LogicController;
import com.hallareandrebollos.controls.Router;
import com.hallareandrebollos.models.CalendarObject;
import com.hallareandrebollos.models.FamilyCalendar;

/**
 * CalendarListPage displays two list views: public calendars (horizontal) and private calendars (vertical).
 * Both lists are scrollable and items are clickable (placeholder actions). Action buttons are at the bottom.
 */
public class CalendarListPage extends JPanel {

    /** Handles all logic for accounts, calendars, and entries. */
    private final LogicController logicController;

    /** Manages navigation between GUI pages. */
    private Router router;


    /**
     * Constructs a CalendarListPage with the given router and logic controller.
     * @param router       The application's router for navigation.
     * @param logicCon     The logic controller that manages data and operations.
     */
    public CalendarListPage(Router router, LogicController logicCon) {
        this.logicController = logicCon;
        this.router = router;
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Add padding to the main panel
    }


    /**
     * Rebuilds the contents of the panel to reflect any updated calendar data.
     */
    public void redrawContents() {
        removeAll();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        // Public Calendars Title
        JLabel publicTitle = new JLabel("Public Calendars");
        publicTitle.setFont(new Font("Arial", Font.BOLD, 16));
        publicTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(publicTitle);

        // Public Calendars Horizontal List
        JScrollPane publicScrollPane = createPublicCalendarList(router);
        publicScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        publicScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Padding above/below list
        mainPanel.add(publicScrollPane);

        // Private Calendars Title
        String username = logicController.getCurrentAccount().getUsername();
        JLabel privateTitle = new JLabel(username + "'s Private Calendars");
        privateTitle.setFont(new Font("Arial", Font.BOLD, 16));
        privateTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(privateTitle);

        // Private Calendars Vertical List
        JScrollPane privateScrollPane = createPrivateCalendarList();
        privateScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        privateScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Padding above/below list
        mainPanel.add(privateScrollPane);

        // Bottom Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton addCalendarBtn = new JButton("Add Calendar");
        addCalendarBtn.addActionListener(e -> onAddCalendar(this.logicController));
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> onBack(router));

        buttonPanel.add(addCalendarBtn);
        buttonPanel.add(backBtn);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }


    /**
     * Creates a horizontally scrollable panel displaying all public calendars.
     * @param router The router used to navigate on click.
     * @return JScrollPane containing calendar tiles.
     */
    private JScrollPane createPublicCalendarList(Router router) {
        ArrayList<CalendarObject> publicCalendars = logicController.getPublicCalendarObjects();
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.X_AXIS));
        listPanel.setOpaque(false);

        if (publicCalendars.isEmpty()) {
            JLabel emptyLabel = new JLabel("No Calendars.");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            listPanel.add(emptyLabel);
        } else {
            for (CalendarObject cal : publicCalendars) {
                JPanel item = createPublicCalendarTile(cal, router);
                listPanel.add(item);
                listPanel.add(Box.createHorizontalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setPreferredSize(new Dimension(760, 100));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return scrollPane;
    }


    /**
     * Creates a single tile representing a public calendar.
     * @param cal    The CalendarObject to display.
     * @param router The router for navigation when clicked.
     * @return JPanel tile component.
     */
    private JPanel createPublicCalendarTile(CalendarObject cal, Router router) {
        JPanel tile = new JPanel();
        tile.setLayout(new GridBagLayout());
        tile.setPreferredSize(new Dimension(75, 100)); // 3:4 aspect ratio
        tile.setMaximumSize(new Dimension(75, 100));
        tile.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        tile.setBackground(new Color(240, 248, 255));

        JLabel nameLabel = new JLabel(cal.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        tile.add(nameLabel);

        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onPublicCalendarClicked(cal, router);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                tile.setBackground(new Color(220, 235, 255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                tile.setBackground(new Color(240, 248, 255));
            }
        });
        return tile;
    }


    /**
     * Creates a vertically scrollable panel displaying all private calendars.
     * @return JScrollPane containing calendar tiles.
     */
    private JScrollPane createPrivateCalendarList() {
        ArrayList<CalendarObject> privateCalendars = logicController.getPrivateCalendarObjects();
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        if (privateCalendars.isEmpty()) {
            JLabel emptyLabel = new JLabel("No Calendars.");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            listPanel.add(emptyLabel);
        } else {
            for (CalendarObject cal : privateCalendars) {
                JPanel item = createPrivateCalendarTile(cal);
                listPanel.add(item);
                listPanel.add(Box.createVerticalStrut(5));
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setPreferredSize(new Dimension(760, 250));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return scrollPane;
    }


    /**
     * Creates a single tile representing a private calendar.
     * @param cal The CalendarObject to display.
     * @return JPanel tile component.
     */
    private JPanel createPrivateCalendarTile(CalendarObject cal) {
        JPanel tile = new JPanel(new BorderLayout());
        tile.setPreferredSize(new Dimension(740, 40));
        tile.setMaximumSize(new Dimension(740, 40));
        tile.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        tile.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(cal.getName());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        tile.add(nameLabel, BorderLayout.CENTER);

        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onPrivateCalendarClicked(cal);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                tile.setBackground(new Color(220, 235, 255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                tile.setBackground(Color.WHITE);
            }
        });
        return tile;
    }

    
    /**
     * Handles the click event for a public calendar. If it's a family calendar, requests a passcode.
     * @param cal    The calendar clicked.
     * @param router The router for page navigation.
     */
    private void onPublicCalendarClicked(CalendarObject cal, Router router) {
        if (cal instanceof FamilyCalendar familyCal) {
            int passcode = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter family calendar passcode:"));
            if (familyCal.isPasscodeCorrect(passcode)) {
                router.showCalendarPage(cal);
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect passcode.");
            }
        } else {
            router.showCalendarPage(cal);
        }
    }


    /**
     * Handles the click event for a private calendar.
     * @param cal The calendar clicked.
     */
    private void onPrivateCalendarClicked(CalendarObject cal) {
        router.showCalendarPage(cal);
    }


    /**
     * Opens a dialog for the user to add a new calendar and adds it to the system.
     * @param logic The logic controller used to create the calendar.
     */
    private void onAddCalendar(LogicController logic) {
        String name = JOptionPane.showInputDialog(this, "Enter calendar name:");
        if (name != null && !name.isBlank()) {
            // default assumes public is true
            // personal assumes public is false
            // family assumes public is true and requires a passcode
            String[] options = {"Default", "Personal", "Family"};
            int choice = JOptionPane.showOptionDialog(this, "Select calendar type:", "Add Calendar",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            switch (choice) {
                case 0:
                    // Default calendar
                    logic.addCalendarObject(logic.getCurrentAccount().getUsername(), name, true);
                    break;
                case 1:
                    // Personal calendar
                    logic.addCalendarObject(logic.getCurrentAccount().getUsername(), name, false);
                    break;
                case 2:
                    // Family calendar
                    try {
                        int passcode = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter family calendar passcode:"));
                        logic.addFamilyCalendar(logic.getCurrentAccount().getUsername(), name, passcode);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid passcode. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }   break;
                default:
                    break;
            }
        }
    }


    /**
     * Navigates back to the main menu.
     *
     * @param router The router used for navigation.
     */
    private void onBack(Router router) {
        router.showMenuPage();
    }
}
