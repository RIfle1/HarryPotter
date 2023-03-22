package Classes;

public class Color {
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_BACKGROUND_BLACK = "\u001B[40m";
    public static final String ANSI_BACKGROUND_RED = "\u001B[41m";
    public static final String ANSI_BACKGROUND_GREEN = "\u001B[42m";
    public static final String ANSI_BACKGROUND_YELLOW = "\u001B[43m";
    public static final String ANSI_BACKGROUND_BLUE = "\u001B[44m";
    public static final String ANSI_BACKGROUND_PURPLE = "\u001B[45m";
    public static final String ANSI_BACKGROUND_CYAN = "\u001B[46m";
    public static final String ANSI_BACKGROUND_WHITE = "\u001B[47m";

    public static String returnColoredText(String text, String textColor) {
        return textColor + "" + text + "" + ANSI_RESET;
    }

    public static String returnColoredText(String text, String textColor, String backgroundColor) {
        return textColor + "" + backgroundColor + text + "" + ANSI_RESET;
    }

}
