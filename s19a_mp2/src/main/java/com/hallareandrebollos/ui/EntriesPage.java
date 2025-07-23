package com.hallareandrebollos.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.w3c.dom.events.MouseEvent;

import com.hallareandrebollos.models.CalendarObject;

public class EntriesPage extends JPanel {

    final private String day;
    final private String month;
    final private String year;
    final private List<String> entries;

    public EntriesPage(String day, String month, String year, List<String> entries) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.entries = entries;

        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel(day + " " + month + " " + year, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Entries List
        DefaultListModel<String> listModel = new DefaultListModel<>();
        if (entries != null && !entries.isEmpty()) {
            for (String entry : entries) {
                listModel.addElement(entry);
            }
        } else {
            listModel.addElement("Entry 1: Placeholder");
            listModel.addElement("Entry 2: Placeholder");
            listModel.addElement("Entry 3: Placeholder");
            listModel.addElement("Entry 4: Placeholder");
        }
        JList<String> entryList = new JList<>(listModel);
        entryList.setVisibleRowCount(6);
        JScrollPane scrollPane = new JScrollPane(entryList);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addEntryButton = new JButton("Add Entry"); // Placeholder
        JButton returnButton = new JButton("Return"); // Placeholder
        buttonPanel.add(addEntryButton);
        buttonPanel.add(returnButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

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
}
