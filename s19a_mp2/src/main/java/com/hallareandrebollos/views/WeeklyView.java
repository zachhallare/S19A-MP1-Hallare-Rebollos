
package com.hallareandrebollos.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.hallareandrebollos.controls.LogicController;
import com.hallareandrebollos.controls.Router;
import com.hallareandrebollos.models.Entry;
import com.hallareandrebollos.models.Theme;
import com.hallareandrebollos.widgets.entryList;


/**
 * WeeklyView represents a 7-day view panel of calendar entries.
 */
public class WeeklyView extends JPanel {

    /** Reference to the app's Router for navigation. */
    private final Router router;

    /** LogicController for handling data and operations. */
    private final LogicController logic;

    /** Starting date of the current week view (Sunday). */
    private LocalDate weekStart;

    /** Panel containing a horizontal list of daily entry panels. */
    private JPanel entryListsPanel;

    /** Scroll pane that allows horizontal scrolling of daily panels. */
    private JScrollPane horizontalScrollPane;

    /** Label displaying the currently viewed week range. */
    private JLabel weekLabel;


    /**
     * Constructs the weekly view with navigation, entry display, and return button.
     * @param router the Router used for navigation
     * @param logic the LogicController for fetching calendar data
     */
    public WeeklyView(Router router, LogicController logic) {
        this.router = router;
        this.logic = logic;
        setLayout(new BorderLayout());
        applyTheme();
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Default week start: SUNNNNNNNNNNDAAAAAAAAAAAAAAAAAAAAYYYYYYYYY
        LocalDate today = LocalDate.now();
        this.weekStart = today.minusDays(today.getDayOfWeek().getValue() % 7);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        updateWeekView();
    }

    /**
     * Applies the current theme to this panel.
     */
    private void applyTheme() {
        Theme theme = logic.getCurrentTheme();
        setBackground(theme.getBackgroundColor());
    }


    /**
     * Creates the top panel with navigation buttons and week label.
     * @return the top JPanel component
     */
    private JPanel createTopPanel() {
        Theme theme = logic.getCurrentTheme();
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navPanel.setOpaque(false);

        JButton prevWeekBtn = new JButton("<");
        JButton nextWeekBtn = new JButton(">");
        prevWeekBtn.setFont(theme.getButtonFont());
        nextWeekBtn.setFont(theme.getButtonFont());
        prevWeekBtn.setBackground(theme.getPrimaryButtonColor());
        nextWeekBtn.setBackground(theme.getPrimaryButtonColor());
        prevWeekBtn.setForeground(theme.getButtonTextColor());
        nextWeekBtn.setForeground(theme.getButtonTextColor());
        prevWeekBtn.setFocusPainted(false);
        nextWeekBtn.setFocusPainted(false);

        prevWeekBtn.addActionListener(e -> {
            this.weekStart = this.weekStart.minusWeeks(1);
            updateWeekView();
        });
        nextWeekBtn.addActionListener(e -> {
            this.weekStart = this.weekStart.plusWeeks(1);
            updateWeekView();
        });

        navPanel.add(prevWeekBtn);

        this.weekLabel = new JLabel();
        this.weekLabel.setFont(theme.getTitleFont());
        this.weekLabel.setForeground(theme.getForegroundColor());
        this.weekLabel.setHorizontalAlignment(SwingConstants.CENTER);

        navPanel.add(this.weekLabel);
        navPanel.add(nextWeekBtn);

        topPanel.add(navPanel, BorderLayout.CENTER);

        return topPanel;
    }


    /**
     * Creates the main panel containing horizontally scrollable daily entry lists.
     * @return the JScrollPane wrapping the daily entry panels
     */
    private JScrollPane createMainPanel() {
        this.entryListsPanel = new JPanel();
        this.entryListsPanel.setLayout(new BoxLayout(this.entryListsPanel, BoxLayout.X_AXIS));
        this.entryListsPanel.setOpaque(false);

        this.horizontalScrollPane = new JScrollPane(this.entryListsPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.horizontalScrollPane.setBorder(null);
        this.horizontalScrollPane.getHorizontalScrollBar().setUnitIncrement(24);
        this.horizontalScrollPane.setOpaque(false);
        this.horizontalScrollPane.getViewport().setOpaque(false);
        this.horizontalScrollPane.setBackground(logic.getCurrentTheme().getBackgroundColor());

        return this.horizontalScrollPane;
    }


    /**
     * Creates the bottom panel containing the return-to-calendar button.
     * @return the bottom JPanel component
     */
    private JPanel createBottomPanel() {
        Theme theme = logic.getCurrentTheme();
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JButton returnBtn = new JButton("Return to Calendar");
        returnBtn.setFont(theme.getRegularFont());
        returnBtn.setBackground(theme.getSecondaryButtonColor());
        returnBtn.setForeground(theme.getButtonTextColor());
        returnBtn.setFocusPainted(false);
        returnBtn.addActionListener(e -> router.showCalendarPage(logic.getCurrentCalendarObject()));

        bottomPanel.add(returnBtn);
        return bottomPanel;
    }


    /**
     * Updates the entry view and label based on the current weekStart value.
     */
    public void updateWeekView() {
        Theme theme = logic.getCurrentTheme();
        
        LocalDate weekEnd = weekStart.plusDays(6);
        String startStr = weekStart.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " +
                weekStart.getDayOfMonth() + " (" + weekStart.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + ")";
        String endStr = weekEnd.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " +
                weekEnd.getDayOfMonth() + " (" + weekEnd.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + ")";
        weekLabel.setText(startStr + " - " + endStr);

        entryListsPanel.removeAll();

        // For each day in the week, create an entryList and a cup of joe MINUMULTO AKOOOOOOOOO
        for (int i = 0; i < 7; i++) {
            LocalDate date = weekStart.plusDays(i);
            List<Entry> entries = logic.getEntriesForDate(date);
            entryList list = new entryList(date, entries, router, logic, false);

            JScrollPane entryScrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            entryScrollPane.setPreferredSize(new Dimension(220, 400));
            
            TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(theme.getBorderColor()), 
                date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
            );
            titledBorder.setTitleFont(theme.getRegularFont());
            titledBorder.setTitleColor(theme.getTextColor());
            entryScrollPane.setBorder(titledBorder);
            
            entryScrollPane.getVerticalScrollBar().setUnitIncrement(24);
            entryScrollPane.setOpaque(false);
            entryScrollPane.getViewport().setOpaque(false);
            entryScrollPane.setBackground(theme.getBackgroundColor());
            entryScrollPane.getViewport().setBackground(theme.getBackgroundColor());

            entryListsPanel.add(entryScrollPane);
        }

        entryListsPanel.revalidate();
        entryListsPanel.repaint();
    }


    /**
     * Sets the view to a specific week based on a date input.
     * @param startDate the LocalDate to base the week on
     */
    public void moveToSpecificWeek(LocalDate startDate) {
        this.weekStart = startDate.minusDays(startDate.getDayOfWeek().getValue() % 7);
        updateWeekView();
    }

    /**
     * Refreshes the UI to apply the current theme to all components.
     */
    public void refreshTheme() {
        applyTheme();
        
        removeAll();
        add(createTopPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
        
        updateWeekView();
        
        revalidate();
        repaint();
    }
}
