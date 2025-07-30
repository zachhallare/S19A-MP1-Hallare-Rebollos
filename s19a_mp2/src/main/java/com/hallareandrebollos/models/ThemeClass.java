package com.hallareandrebollos.models;

import java.awt.Color;
import java.awt.Font;

/**
 * The Theme class defines color schemes and font styles for the application UI.
 * It provides various predefined themes including light, dark, and colorful variants.
 */
public class ThemeClass {
    
    public enum ThemeType {
        LIGHT_DEFAULT,
        DARK_DEFAULT,
        AMOLED_BLACK,
        DARK_BLUE_OCEAN,
        DARK_GREEN_PLANT,
        RED_YELLOW_SAND_DUNES
    }
    
    private Color backgroundColor;
    private Color foregroundColor;
    private Color primaryButtonColor;
    private Color secondaryButtonColor;
    private Color accentColor;
    private Color textColor;
    private Color subtitleColor;
    private Color buttonTextColor;
    private Color panelColor;
    private Color borderColor;
    
    private Font titleFont;
    private Font subtitleFont;
    private Font buttonFont;
    private Font regularFont;
    
    private ThemeType currentTheme;
    
    /**
     * Constructs a Theme with default light theme settings.
     */
    public ThemeClass() {
        this(ThemeType.LIGHT_DEFAULT);
    }
    
    /**
     * Constructs a Theme with the specified theme type.
     * @param themeType The type of theme to apply
     */
    public ThemeClass(ThemeType themeType) {
        initializeFonts();
        applyTheme(themeType);
    }
    
    /**
     * Initializes the default font styles used across all themes.
     */
    private void initializeFonts() {
        this.titleFont = new Font("SansSerif", Font.BOLD, 36);
        this.subtitleFont = new Font("SansSerif", Font.PLAIN, 20);
        this.buttonFont = new Font("SansSerif", Font.PLAIN, 20);
        this.regularFont = new Font("SansSerif", Font.PLAIN, 14);
    }
    
    /**
     * Applies the specified theme by setting all color properties.
     * @param themeType The theme type to apply
     */
    public void applyTheme(ThemeType themeType) {
        this.currentTheme = themeType;
        
        switch (themeType) {
            case LIGHT_DEFAULT -> applyLightDefaultTheme();
            case DARK_DEFAULT -> applyDarkDefaultTheme();
            case AMOLED_BLACK -> applyAmoledBlackTheme();
            case DARK_BLUE_OCEAN -> applyDarkBlueOceanTheme();
            case DARK_GREEN_PLANT -> applyDarkGreenPlantTheme();
            case RED_YELLOW_SAND_DUNES -> applyRedYellowSandDunesTheme();
        }
    }
    
    /**
     * Applies the light default theme colors.
     */
    private void applyLightDefaultTheme() {
        this.backgroundColor = new Color(0xE0E0E0);        // Light gray
        this.foregroundColor = new Color(0x36454F);        // Dark slate gray
        this.primaryButtonColor = Color.LIGHT_GRAY;        // Light gray
        this.secondaryButtonColor = new Color(0xF5F5F5);   // WhiteSmoke
        this.accentColor = new Color(0x4169E1);            // Royal blue
        this.textColor = new Color(0x36454F);              // Dark slate gray
        this.subtitleColor = Color.DARK_GRAY;              // Dark gray
        this.buttonTextColor = Color.BLACK;                // Black
        this.panelColor = Color.WHITE;                     // White
        this.borderColor = new Color(0xC0C0C0);            // Silver
    }
    
    /**
     * Applies the dark default theme colors.
     */
    private void applyDarkDefaultTheme() {
        this.backgroundColor = new Color(0x2B2B2B);        // Dark gray
        this.foregroundColor = new Color(0xE0E0E0);        // Light gray
        this.primaryButtonColor = new Color(0x404040);     // Darker gray
        this.secondaryButtonColor = new Color(0x505050);   // Medium gray
        this.accentColor = new Color(0x64B5F6);            // Light blue
        this.textColor = new Color(0xE0E0E0);              // Light gray
        this.subtitleColor = new Color(0xB0B0B0);          // Medium light gray
        this.buttonTextColor = Color.WHITE;                // White
        this.panelColor = new Color(0x363636);             // Dark gray
        this.borderColor = new Color(0x555555);            // Medium dark gray
    }
    
    /**
     * Applies the AMOLED black theme colors for deep blacks and high contrast.
     */
    private void applyAmoledBlackTheme() {
        this.backgroundColor = Color.BLACK;                // Pure black
        this.foregroundColor = Color.WHITE;                // Pure white
        this.primaryButtonColor = new Color(0x1A1A1A);     // Very dark gray
        this.secondaryButtonColor = new Color(0x0D0D0D);   // Almost black
        this.accentColor = new Color(0x00FF00);            // Bright green
        this.textColor = Color.WHITE;                      // Pure white
        this.subtitleColor = new Color(0xCCCCCC);          // Light gray
        this.buttonTextColor = Color.WHITE;                // Pure white
        this.panelColor = new Color(0x0A0A0A);             // Very dark gray
        this.borderColor = new Color(0x333333);            // Dark gray
    }
    
    /**
     * Applies the dark blue ocean theme colors inspired by deep ocean waters.
     */
    private void applyDarkBlueOceanTheme() {
        this.backgroundColor = new Color(0x0F1419);        // Very dark blue
        this.foregroundColor = new Color(0xE1F5FE);        // Light cyan
        this.primaryButtonColor = new Color(0x1565C0);     // Blue
        this.secondaryButtonColor = new Color(0x0D47A1);   // Dark blue
        this.accentColor = new Color(0x00BCD4);            // Cyan
        this.textColor = new Color(0xE1F5FE);              // Light cyan
        this.subtitleColor = new Color(0x81D4FA);          // Light blue
        this.buttonTextColor = Color.WHITE;                // White
        this.panelColor = new Color(0x1A237E);             // Indigo
        this.borderColor = new Color(0x3F51B5);            // Indigo
    }
    
    /**
     * Applies the dark green plant theme colors inspired by forest and plants.
     */
    private void applyDarkGreenPlantTheme() {
        this.backgroundColor = new Color(0x1B2F1B);        // Dark forest green
        this.foregroundColor = new Color(0xE8F5E8);        // Light green
        this.primaryButtonColor = new Color(0x388E3C);     // Green
        this.secondaryButtonColor = new Color(0x2E7D32);   // Dark green
        this.accentColor = new Color(0x66BB6A);            // Light green
        this.textColor = new Color(0xE8F5E8);              // Light green
        this.subtitleColor = new Color(0xA5D6A7);          // Medium light green
        this.buttonTextColor = Color.WHITE;                // White
        this.panelColor = new Color(0x2C5530);             // Forest green
        this.borderColor = new Color(0x4CAF50);            // Green
    }
    
    /**
     * Applies the red yellow sand dunes theme colors inspired by desert landscapes.
     */
    private void applyRedYellowSandDunesTheme() {
        this.backgroundColor = new Color(0x3E2723);        // Dark brown
        this.foregroundColor = new Color(0xFFF8E1);        // Light yellow
        this.primaryButtonColor = new Color(0xFF8F00);     // Orange
        this.secondaryButtonColor = new Color(0xF57C00);   // Dark orange
        this.accentColor = new Color(0xFFD54F);            // Yellow
        this.textColor = new Color(0xFFF8E1);              // Light yellow
        this.subtitleColor = new Color(0xFFCC02);          // Amber
        this.buttonTextColor = new Color(0x3E2723);        // Dark brown
        this.panelColor = new Color(0x5D4037);             // Brown
        this.borderColor = new Color(0x8D6E63);            // Light brown
    }
    
    /**
     * Creates a darker version of the given color by reducing RGB values.
     * @param color The original color
     * @param factor The darkening factor (0.0 to 1.0)
     * @return A darker version of the color
     */
    public static Color darkerColor(Color color, double factor) {
        return new Color(
            Math.max((int)(color.getRed() * factor), 0),
            Math.max((int)(color.getGreen() * factor), 0),
            Math.max((int)(color.getBlue() * factor), 0),
            color.getAlpha()
        );
    }
    
    /**
     * Creates a lighter version of the given color by increasing RGB values.
     * @param color The original color
     * @param factor The lightening factor (0.0 to 1.0)
     * @return A lighter version of the color
     */
    public static Color lighterColor(Color color, double factor) {
        return new Color(
            Math.min((int)(color.getRed() + (255 - color.getRed()) * factor), 255),
            Math.min((int)(color.getGreen() + (255 - color.getGreen()) * factor), 255),
            Math.min((int)(color.getBlue() + (255 - color.getBlue()) * factor), 255),
            color.getAlpha()
        );
    }
    
    /**
     * Returns a string representation of the current theme.
     * @return A string describing the current theme
     */
    @Override
    public String toString() {
        return "Theme{" +
                "currentTheme=" + currentTheme +
                ", backgroundColor=" + backgroundColor +
                ", foregroundColor=" + foregroundColor +
                ", accentColor=" + accentColor +
                '}';
    }
    
    /**
     * Checks if the current theme is a dark theme.
     * @return true if the theme is dark, false otherwise
     */
    public boolean isDarkTheme() {
        return this.currentTheme == ThemeType.DARK_DEFAULT ||
               this.currentTheme == ThemeType.AMOLED_BLACK ||
               this.currentTheme == ThemeType.DARK_BLUE_OCEAN ||
               this.currentTheme == ThemeType.DARK_GREEN_PLANT ||
               this.currentTheme == ThemeType.RED_YELLOW_SAND_DUNES;
    }
    
    /**
     * Returns an array of all available theme types.
     * @return Array of ThemeType values
     */
    public static ThemeType[] getAllThemes() {
        return ThemeType.values();
    }
    
    /**
     * Returns a user-friendly name for the given theme type.
     * @param themeType The theme type
     * @return A readable name for the theme
     */
    public static String getThemeName(ThemeType themeType) {
        return switch (themeType) {
            case LIGHT_DEFAULT -> "Light Default";
            case DARK_DEFAULT -> "Dark Default";
            case AMOLED_BLACK -> "AMOLED Black";
            case DARK_BLUE_OCEAN -> "Dark Blue Ocean";
            case DARK_GREEN_PLANT -> "Dark Green Plant";
            case RED_YELLOW_SAND_DUNES -> "Red Yellow Sand Dunes";
        };
    }

    /**
     * Returns the current background color.
     * @return The background color
     */
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }
    
    /**
     * Returns the current foreground color.
     * @return The foreground color
     */
    public Color getForegroundColor() {
        return this.foregroundColor;
    }
    
    /**
     * Returns the current primary button color.
     * @return The primary button color
     */
    public Color getPrimaryButtonColor() {
        return this.primaryButtonColor;
    }
    
    /**
     * Returns the current secondary button color.
     * @return The secondary button color
     */
    public Color getSecondaryButtonColor() {
        return this.secondaryButtonColor;
    }
    
    /**
     * Returns the current accent color.
     * @return The accent color
     */
    public Color getAccentColor() {
        return this.accentColor;
    }
    
    /**
     * Returns the current text color.
     * @return The text color
     */
    public Color getTextColor() {
        return this.textColor;
    }
    
    /**
     * Returns the current subtitle color.
     * @return The subtitle color
     */
    public Color getSubtitleColor() {
        return this.subtitleColor;
    }
    
    /**
     * Returns the current button text color.
     * @return The button text color
     */
    public Color getButtonTextColor() {
        return this.buttonTextColor;
    }
    
    /**
     * Returns the current panel color.
     * @return The panel color
     */
    public Color getPanelColor() {
        return this.panelColor;
    }
    
    /**
     * Returns the current border color.
     * @return The border color
     */
    public Color getBorderColor() {
        return this.borderColor;
    }
    
    /**
     * Returns the title font.
     * @return The title font
     */
    public Font getTitleFont() {
        return this.titleFont;
    }
    
    /**
     * Returns the subtitle font.
     * @return The subtitle font
     */
    public Font getSubtitleFont() {
        return this.subtitleFont;
    }
    
    /**
     * Returns the button font.
     * @return The button font
     */
    public Font getButtonFont() {
        return this.buttonFont;
    }
    
    /**
     * Returns the regular font.
     * @return The regular font
     */
    public Font getRegularFont() {
        return this.regularFont;
    }
    
    /**
     * Returns the current theme type.
     * @return The current theme type
     */
    public ThemeType getCurrentTheme() {
        return this.currentTheme;
    }

    /**
     * Sets the background color.
     * @param backgroundColor The new background color
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    /**
     * Sets the foreground color.
     * @param foregroundColor The new foreground color
     */
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }
    
    /**
     * Sets the primary button color.
     * @param primaryButtonColor The new primary button color
     */
    public void setPrimaryButtonColor(Color primaryButtonColor) {
        this.primaryButtonColor = primaryButtonColor;
    }
    
    /**
     * Sets the secondary button color.
     * @param secondaryButtonColor The new secondary button color
     */
    public void setSecondaryButtonColor(Color secondaryButtonColor) {
        this.secondaryButtonColor = secondaryButtonColor;
    }
    
    /**
     * Sets the accent color.
     * @param accentColor The new accent color
     */
    public void setAccentColor(Color accentColor) {
        this.accentColor = accentColor;
    }
    
    /**
     * Sets the text color.
     * @param textColor The new text color
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }
    
    /**
     * Sets the subtitle color.
     * @param subtitleColor The new subtitle color
     */
    public void setSubtitleColor(Color subtitleColor) {
        this.subtitleColor = subtitleColor;
    }
    
    /**
     * Sets the button text color.
     * @param buttonTextColor The new button text color
     */
    public void setButtonTextColor(Color buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
    }
    
    /**
     * Sets the panel color.
     * @param panelColor The new panel color
     */
    public void setPanelColor(Color panelColor) {
        this.panelColor = panelColor;
    }
    
    /**
     * Sets the border color.
     * @param borderColor The new border color
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
    
    /**
     * Sets the title font.
     * @param titleFont The new title font
     */
    public void setTitleFont(Font titleFont) {
        this.titleFont = titleFont;
    }
    
    /**
     * Sets the subtitle font.
     * @param subtitleFont The new subtitle font
     */
    public void setSubtitleFont(Font subtitleFont) {
        this.subtitleFont = subtitleFont;
    }
    
    /**
     * Sets the button font.
     * @param buttonFont The new button font
     */
    public void setButtonFont(Font buttonFont) {
        this.buttonFont = buttonFont;
    }
    
    /**
     * Sets the regular font.
     * @param regularFont The new regular font
     */
    public void setRegularFont(Font regularFont) {
        this.regularFont = regularFont;
    }
}
