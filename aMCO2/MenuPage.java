package aMCO2;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuPage extends JPanel {
    public MenuPage(Router router, LogicController logic) {
        setLayout(null);
        setBackground(new Color(0xE0E0E0));     // Lighter grey.

        JLabel label = new JLabel("Main Menu", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setForeground(new Color(0x36454F));
        label.setBounds(200, 30, 400, 40);
        add(label);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(300, 100, 160, 40);
        logoutButton.addActionListener(e -> router.showLandingPage());
        styleButton(logoutButton);
        add(logoutButton);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEtchedBorder());
    }
}
