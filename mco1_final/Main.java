public class Main {
    public static void main(String[] args) {
        LogicController logicController = new LogicController();
        DisplayController displayController = new DisplayController(logicController);
        String currentPage = "landing"; // Start at the login page

        while (currentPage != null || currentPage != "exit") {
            switch (currentPage) {
                case "landing":
                    currentPage = displayController.displayLandingPage();
                    break;
                case "login":
                    currentPage = displayController.displayLoginPage();
                    break;
                case "signup":
                    currentPage = displayController.displaySignUpPage();
                    break;
                case "menu":
                    currentPage = displayController.displayMenuPage();
                    break;
                case "calendar":
                    displayController.displayCalendar();
                    break;
                default:
                    System.out.println("Unknown page. Returning to portal page...");
                    currentPage = "landing";
                    break;
            }
        }
    }
}
