public class Main {
    public static void main(String[] args) {
        LogicController logicController = new LogicController();
        DisplayController displayController = new DisplayController(logicController);
        String currentPage = "landing";     // Start at the login page
        boolean isRunning = true;           // Loop control variable

        while (isRunning) {
            switch (currentPage) {
                case "landing" -> currentPage = displayController.displayLandingPage();
                case "login" -> currentPage = displayController.displayLoginPage();
                case "signup" -> currentPage = displayController.displaySignUpPage();
                case "menu" -> currentPage = displayController.displayMenuPage();
                case "calendar" -> {
                    displayController.displayMonthSelection();
                    currentPage = "menu";
                }
                case "exit" -> {
                    System.out.println("Thank you for using our Digital Calendar. Goodbye!");
                    isRunning = false; 
                }
                default -> {
                    System.out.println("Unknown page. Returning to portal page...");
                    currentPage = "landing";
                }
            }
        }
    }
}
