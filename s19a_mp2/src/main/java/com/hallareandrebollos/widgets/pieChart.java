package com.hallareandrebollos.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.hallareandrebollos.models.Entry;

public class pieChart extends JPanel {

    private static final long serialVersionUID = 1L;
    private final int[] values;
    private final Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE};
    private final String[] types = {"Event", "Meeting", "Task", "Journal"};
    private final int size;

    public pieChart(ArrayList<Entry> entries, int sizeX, int sizeY) {
        // Count entries by type
        int[] counts = new int[4]; // Event, Meeting, Task, Journal

        for (Entry entry : entries) {
            String type = entry.getType();
            switch (type) {
                case "Event" ->
                    counts[0]++;
                case "Meeting" ->
                    counts[1]++;
                case "Task" ->
                    counts[2]++;
                case "Journal" ->
                    counts[3]++;
                default -> {
                    /* ignore unknown types */ }
            }
        }

        this.values = counts;
        this.size = Math.min(sizeX, sizeY);
        setPreferredSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size));
        setMaximumSize(new Dimension(size, size));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(size, size);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(size, size);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(size, size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int drawSize = Math.min(getWidth(), getHeight());
        g2d.translate((getWidth() - drawSize) / 2, (getHeight() - drawSize) / 2);
        drawPieChart(g2d, drawSize);
        g2d.dispose();
    }

    private void drawPieChart(Graphics2D g2d, int drawSize) {
        int total = 0;
        for (int value : values) {
            total += value;
        }
        if (total > 0) {
            int startAngle = 0;
            for (int i = 0; i < values.length; i++) {
                if (values[i] > 0) {
                    int arcAngle = (int) Math.round(((double) values[i] / total) * 360);
                    g2d.setColor(colors[i]);
                    g2d.fillArc(0, 0, drawSize, drawSize, startAngle, arcAngle);
                    startAngle += arcAngle;
                }
            }
            int centerSize = drawSize / 3;
            int centerX = (drawSize - centerSize) / 2;
            int centerY = (drawSize - centerSize) / 2;
            g2d.setColor(Color.WHITE);
            g2d.fillOval(centerX, centerY, centerSize, centerSize);
        }
    }

    // Static method for creating BufferedImage (if needed elsewhere)
    public static BufferedImage constructPieChartImage(ArrayList<Entry> entries, int sizeX, int sizeY) {
        pieChart pie = new pieChart(entries, sizeX, sizeY);
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        pie.setSize(sizeX, sizeY);
        pie.paintComponent(g2d);

        g2d.dispose();
        return image;
    }

    public int getTotalEntries() {
        int total = 0;
        for (int value : values) {
            total += value;
        }
        return total;
    }

    public int getEntriesOfType(String type) {
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(type)) {
                return values[i];
            }
        }
        return 0;
    }
}
