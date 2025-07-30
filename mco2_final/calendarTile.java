package mco2_final;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Represents a single day tile in the calendar view with visual info and click handling.
 */
public class calendarTile extends JPanel implements MouseListener {
    
    /** Day of the month represented by this tile. */
    private int day;

    /** Month of the year represented by this tile. */
    private int month;

    /** Year represented by this tile. */
    private int year;

    /** LogicController used to fetch entry data. */
    private LogicController logic;

    /** Whether the tile represents the current day. */
    private boolean isToday;

    /** Router used for navigation between views. */
    private Router router;

    /** Reference to the parent panel to position modal dialogs. */
    private final JPanel parentPanel;


    /**
     * Constructs a calendar tile with date, visual style, and controller logic.
     * @param parentPanel the parent panel for modal positioning
     * @param day the day of the month
     * @param month the month (1-based)
     * @param year the year
     * @param isToday true if this tile represents today
     * @param logic the LogicController for data access
     * @param router the Router for navigation
     */
    public calendarTile(JPanel parentPanel, int day, int month, int year, boolean isToday, LogicController logic, Router router) {
        this.parentPanel = parentPanel;
        this.day = day;
        this.month = month;
        this.year = year;
        this.logic = logic;
        this.router = router;
        this.isToday = isToday;

        initializeComponents();
    }


    /**
     * Initializes the layout, style, and components of the calendar tile.
     */
    private void initializeComponents() {
        Theme theme = logic.getCurrentTheme();
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(120, 100));
        setBackground(theme.getPanelColor());
        setBorder(BorderFactory.createLineBorder(theme.getBorderColor(), 1));

        JLabel dayLabel = new JLabel(String.valueOf(day));
        dayLabel.setFont(isToday ? theme.getTitleFont() : theme.getRegularFont());
        dayLabel.setHorizontalAlignment(SwingConstants.LEFT);
        dayLabel.setVerticalAlignment(SwingConstants.TOP);
        dayLabel.setOpaque(false);
        dayLabel.setForeground(theme.getTextColor());
        
        ArrayList<Entry> entries = logic.getEntriesForDate(LocalDate.of(year, month, day));
        pieChart pieChartPanel = new pieChart(entries, 35, 35, logic);
        pieChartPanel.setOpaque(false);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        
        contentPanel.add(dayLabel, BorderLayout.WEST);
        contentPanel.add(pieChartPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        addMouseListener(this);
    }


    /**
     * Handles click events on the tile by showing a popup with that day's entries.
     * @param e the MouseEvent triggered by click
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        LocalDate selectedDate = LocalDate.of(year, month, day);
        ArrayList<Entry> entriesForDay = logic.getEntriesForDate(selectedDate);

        entryList entryListWidget = new entryList(selectedDate, entriesForDay, router, logic, true);
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(parentPanel),
                    "Entries for " + selectedDate.toString(), true);
            dialog.setContentPane(entryListWidget);
            dialog.setSize(500, 600);
            dialog.setLocationRelativeTo(parentPanel);
            dialog.setVisible(true);
    }


    /**
     * Highlights tile border when mouse is pressed.
     * @param e the MouseEvent triggered by hover
     */
    @Override
    public void mousePressed(MouseEvent e) {
    }


    /**
     * Highlights tile border when mouse is released.
     * @param e the MouseEvent triggered by hover
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }


    /**
     * Highlights tile border when mouse enters.
     * @param e the MouseEvent triggered by hover
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        Theme theme = logic.getCurrentTheme();
        setBorder(BorderFactory.createLineBorder(theme.getAccentColor(), 2));
    }


    /**
     * Resets tile border when mouse exits.
     * @param e the MouseEvent triggered by hover exit
     */
    @Override
    public void mouseExited(MouseEvent e) {
        Theme theme = logic.getCurrentTheme();
        setBorder(BorderFactory.createLineBorder(theme.getBorderColor(), 1));
    }
}
