/**
 * Entry point of the Digital Calendar application.
 * Initializes controllers and handles the main navigation loop.
 */
public class Main {
    /**
     * Main method that starts the program and manages user interface flow.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        LogicController logicController = new LogicController();        // Handles backend logic.
        DisplayController displayController = new DisplayController(logicController);       // Manages UI.
        String currentPage = "landing";     // Start at the login page
        boolean isRunning = true;           // Loop control variable

        // Main application loop.
        while (isRunning) {
            switch (currentPage) {
                case "landing" -> currentPage = displayController.displayLandingPage();
                case "login" -> currentPage = displayController.displayLoginPage();
                case "signup" -> currentPage = displayController.displaySignUpPage();
                case "menu" -> currentPage = displayController.displayMenuPage();
                case "calendar" -> {
                    displayController.displayMonthSelection();
                    currentPage = "menu";       // Return to menu after calendar is shown.
                }
                case "entry" -> {
                    currentPage = displayController.displayEntryOptions(logicController.getSelectedMonth());
                }
                case "exit" -> {
                    System.out.println("Thank you for using our Digital Calendar. Goodbye!");
                    isRunning = false;      // Exit the loop and terminate
                }
                default -> {
                    System.out.println("Unknown page. Returning to portal page...");
                    currentPage = "landing";        // Fallback in case of invalid state
                }
            }
        }
    }
}
