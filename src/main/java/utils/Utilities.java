package utils;

public class Utilities
{
    public static String escapeString(String input) {
        if (input.contains(",") || input.contains("\"") || input.contains("\n")) {
            input = input.replace("\"", "\"\""); // escape internal quotes
            return "\"" + input + "\"";          // wrap in quotes
        }
        return input;
    }
}
