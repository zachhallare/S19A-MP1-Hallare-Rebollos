package aMCO2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * CalendarTimeline is a GUI component that displays a scrollable list of Entry tiles.
 * Each tile is clickable and runs a placeholder function when clicked.
 */
public class CalendarTimeline extends JPanel {
    final private int width;
    final private int height;
    final private JPanel listPanel;
    final private JScrollPane scrollPane;
    
    private List<Entry> entries;

    /**
     * Constructs a CalendarTimeline with the given dimensions and list of entries.
     * @param width  the width of the component
     * @param height the height of the component
     * @param entries the list of Entry objects to display
     */
    public CalendarTimeline(int width, int height, List<Entry> entries) {
        this.width = width;
        this.height = height;
        this.entries = entries;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(this.width, this.height));

        this.listPanel = new JPanel();
        this.listPanel.setLayout(new BoxLayout(this.listPanel, BoxLayout.Y_AXIS));
        populateList();

        this.scrollPane = new JScrollPane(this.listPanel);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.scrollPane.setPreferredSize(new Dimension(this.width, this.height));

        add(this.scrollPane, BorderLayout.CENTER);
    }

    /**
     * Populates the list panel with entry tiles.
     */
    private void populateList() {
        listPanel.removeAll();
        if (this.entries != null) {
            for (Entry entry : this.entries) {
                JPanel tile = createEntryTile(entry);
                listPanel.add(tile);
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    /**
     * Creates a JPanel representing a single entry tile.
     * @param entry the Entry to display
     * @return a JPanel for the entry
     */
    private JPanel createEntryTile(Entry entry) {
        JPanel tile = new JPanel();
        tile.setLayout(new BorderLayout());
        tile.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        tile.setMaximumSize(new Dimension(this.width - 20, 60));
        tile.setPreferredSize(new Dimension(this.width - 20, 60));
        tile.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(entry.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel descLabel = new JLabel(entry.getDescription());
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(descLabel);

        tile.add(textPanel, BorderLayout.CENTER);

        // Make the tile clickable
        tile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onEntryTileClicked(entry);
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
     * Click function for the entry tile.
     * Displays a message dialog with the entry title.
     * @param entry the Entry that was clicked
     */
    private void onEntryTileClicked(Entry entry) {
        JOptionPane.showMessageDialog(this, "Clicked: " + entry.getTitle(), "Entry Clicked", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Updates the list of entries displayed in the timeline.
     * @param newEntries the new list of entries
     */
    public void setEntries(List<Entry> newEntries) {
        this.entries = newEntries;
        populateList();
    }
}
