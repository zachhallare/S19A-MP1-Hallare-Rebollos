package aMCO2;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuPage extends JPanel {
    public MenuPage(Router router, LogicController logic) {
        setLayout(null);
        setBackground(new Color(0xE0E0E0));     // Lighter grey.

        // Title.
        JLabel titleLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x36454F));
        titleLabel.setBounds(150, 20, 200, 40);
        add(titleLabel);

        // Option 1: Select Calendar.
        JButton selectButton = new JButton("Select Calendar");
        selectButton.setBounds(150, 80, 200, 40);
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
                        JOptionPane.showMessageDialog(this, "Selected: " + calName);
                    }
                }
            }
        });
        styleButton(selectButton);
        add(selectButton);

        // Option 2: Add Calendar.
        JButton addButton = new JButton("Add Calendar");
        addButton.setBounds(150, 130, 200, 40);
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
        styleButton(addButton);
        add(addButton);

        // Option 3: View Today.
        JButton viewTodayButton = new JButton("View Today");
        viewTodayButton.setBounds(150, 180, 200, 40);
        viewTodayButton.setBackground(new Color(0xFF9999));
        viewTodayButton.addActionListener(e -> {
            // idk yet lmao.
        });
        styleButton(viewTodayButton);
        add(viewTodayButton);

        // Option 4: Delete Account.
        JButton deleteAccButton = new JButton("Delete Account");
        deleteAccButton.setBounds(150, 230, 200, 40);
        deleteAccButton.setBackground(new Color(0xFF9999));
        deleteAccButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
            "Delete your account?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                logic.deactivateAccount();
                router.showLandingPage();
            }
        });
        styleButton(deleteAccButton);
        add(deleteAccButton);

        // Option 5: Logout.
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(150, 280, 200, 40);
        logoutButton.addActionListener(e -> {
            logic.logoutAccount();
            router.showLandingPage();
        });
        styleButton(logoutButton);
        add(logoutButton);
    }


    // For aesthetics. 
    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEtchedBorder());
    }
}
