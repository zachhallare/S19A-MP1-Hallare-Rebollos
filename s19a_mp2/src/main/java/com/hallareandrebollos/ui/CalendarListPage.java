
package com.hallareandrebollos.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import com.hallareandrebollos.models.CalendarObject;
import com.hallareandrebollos.services.Router;
import com.hallareandrebollos.services.LogicController;

/**
 * CalendarListPage displays two list views: public calendars (horizontal) and private calendars (vertical).
 * Both lists are scrollable and items are clickable (placeholder actions). Action buttons are at the bottom.
 */
public class CalendarListPage extends JPanel {
    private final LogicController logicController;

    public CalendarListPage(Router router, LogicController logicController) {
        this.logicController = logicController;
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        // Public Calendars Title
        JLabel publicTitle = new JLabel("Public Calendars");
        publicTitle.setFont(new Font("Arial", Font.BOLD, 16));
        publicTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(publicTitle);

        // Public Calendars Horizontal List
        JScrollPane publicScrollPane = createPublicCalendarList();
        publicScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(publicScrollPane);

        // Private Calendars Title
        JLabel privateTitle = new JLabel("Your Private Calendars");
        privateTitle.setFont(new Font("Arial", Font.BOLD, 16));
        privateTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(privateTitle);

        // Private Calendars Vertical List
        JScrollPane privateScrollPane = createPrivateCalendarList();
        privateScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(privateScrollPane);

        // Bottom Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton addCalendarBtn = new JButton("Add Calendar");
        addCalendarBtn.addActionListener(e -> onAddCalendar());
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> onBack());

        buttonPanel.add(addCalendarBtn);
        buttonPanel.add(backBtn);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates a horizontal scrollable list of public calendars.
     */
    private JScrollPane createPublicCalendarList() {
        List<CalendarObject> publicCalendars = logicController.getPublicCalendarObjects();
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.X_AXIS));
        listPanel.setOpaque(false);

        for (CalendarObject cal : publicCalendars) {
            JPanel item = createPublicCalendarTile(cal);
            listPanel.add(item);
            listPanel.add(Box.createHorizontalStrut(10));
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
     * Creates a tile for a public calendar (3:4 aspect ratio, name centered).
     */
    private JPanel createPublicCalendarTile(CalendarObject cal) {
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
                onPublicCalendarClicked(cal);
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
     * Creates a vertical scrollable list of private calendars.
     */
    private JScrollPane createPrivateCalendarList() {
        List<CalendarObject> privateCalendars = logicController.getPrivateCalendarObjects();
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        for (CalendarObject cal : privateCalendars) {
            JPanel item = createPrivateCalendarTile(cal);
            listPanel.add(item);
            listPanel.add(Box.createVerticalStrut(5));
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
     * Creates a tile for a private calendar (list tile with name).
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

    // TODO: add functionality
    private void onPublicCalendarClicked(CalendarObject cal) {
        JOptionPane.showMessageDialog(this, "Clicked public calendar: " + cal.getName(), "Public Calendar", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onPrivateCalendarClicked(CalendarObject cal) {
        JOptionPane.showMessageDialog(this, "Clicked private calendar: " + cal.getName(), "Private Calendar", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onAddCalendar() {
        JOptionPane.showMessageDialog(this, "Add Calendar button clicked (placeholder)", "Add Calendar", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onBack() {
        JOptionPane.showMessageDialog(this, "Back button clicked (placeholder)", "Back", JOptionPane.INFORMATION_MESSAGE);
    }
}
