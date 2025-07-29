package com.hallareandrebollos.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

import com.hallareandrebollos.controls.LogicController;
import com.hallareandrebollos.controls.Router;
import com.hallareandrebollos.models.Entry;

public class calendarTile extends JPanel implements MouseListener {

    private static final long serialVersionUID = 1L;
    private int day;
    private int month;
    private int year;
    private LogicController logic;
    private boolean isToday;
    private Router router;
    private final JPanel parentPanel;

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

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(120, 100));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel dayLabel = new JLabel(String.valueOf(day));
        dayLabel.setFont(isToday ? new Font("SansSerif", Font.BOLD, 16) : new Font("SansSerif", Font.PLAIN, 14));
        dayLabel.setHorizontalAlignment(SwingConstants.LEFT);
        dayLabel.setVerticalAlignment(SwingConstants.TOP);
        dayLabel.setOpaque(false);
        
        ArrayList<Entry> entries = logic.getEntriesForDate(LocalDate.of(year, month, day));
        pieChart pieChartPanel = new pieChart(entries, 35, 35);
        pieChartPanel.setOpaque(false);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        
        contentPanel.add(dayLabel, BorderLayout.WEST);
        contentPanel.add(pieChartPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        addMouseListener(this);
    }

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

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    }
}
