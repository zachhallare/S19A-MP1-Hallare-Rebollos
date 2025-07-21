package aMCO2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

public class MenuPage extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(0xE0E0E0);
    private static final Color ACCENT_COLOR = new Color(0xFF9999);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.PLAIN, 16);

    public MenuPage(Router router, LogicController logic) {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);   

        // Title Label.
        JLabel titleLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(new Color(0x36454F));
        add(titleLabel, BorderLayout.NORTH);

        // Vertical Button Panel (Center).
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Button 1: Select Calendar.
        JButton selectButton = createStyledButton("Select Calendar");
        selectButton.addActionListener(e -> {
            ArrayList<CalendarObject> privateCalendars = logic.getPrivateCalendarObjects();
            ArrayList<CalendarObject> publicCalendars = logic.getPublicCalendarObjects();
            ArrayList<String> calendarNames = new ArrayList<>();

            for (CalendarObject cal : privateCalendars) { 
                calendarNames.add("Private: " + cal.getCalendarName());
            }
            for (CalendarObject cal : publicCalendars) {
                calendarNames.add("Public: " + cal.getCalendarName());
            }

            if (calendarNames.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No calendar available.");
            } else {
                String selected = (String) JOptionPane.showInputDialog(
                    this, "Choose calendar:", "Select Calendar",
                    JOptionPane.PLAIN_MESSAGE, null,
                    calendarNames.toArray(), calendarNames.get(0)
                );
                if (selected != null) {
                    boolean isPublic = selected.startsWith("Public: ");
                    String calName = selected.substring(selected.indexOf(": ") + 2);
                    int index = logic.getCalendarFromName(calName, isPublic);
                    if (index != -1) {
                        logic.setCalendarObjectIndex(index);
                        router.showCalendarPage();
                    }
                }
            }
        });
        buttonPanel.add(selectButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));


        // Button 2: Add Calendar.
        JButton addButton = createStyledButton("Add Calendar");
        addButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter calendar name:");
            if (name != null && !name.isBlank()) {
                String[] options = {"Private", "Public"};
                int type = JOptionPane.showOptionDialog(this,
                    "Choose calendar type:", "Calendar Type",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);
                
                boolean isPublic = (type == 1);
                boolean exists = logic.checkCalendarDuplicate(name, isPublic, logic.getCurrentAccount().getUsername());
                if (!exists) {
                    logic.addCalendarObject(logic.getCurrentAccount().getUsername(), name, isPublic);
                    JOptionPane.showMessageDialog(this, "Calendar added!");
                } else {
                    JOptionPane.showMessageDialog(this, "Calendar already exists.");
                }
            }
        });
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));


        // Button 3: Delete Account.
        JButton deleteAccButton = createStyledButton("Delete Account");
        deleteAccButton.setBackground(ACCENT_COLOR);
        deleteAccButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete your account?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                logic.deactivateAccount();
                router.showLandingPage();
            }
        });
        buttonPanel.add(deleteAccButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));


        // Button 4: Logout.
        JButton logoutButton = createStyledButton("Logout");
        logoutButton.addActionListener(e -> {
            logic.logoutAccount();
            router.showLandingPage();
        });
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.CENTER);

    }


    // For aesthetics. 
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(160, 40));
        button.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        return button;
    }
}
