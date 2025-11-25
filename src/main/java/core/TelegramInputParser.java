package core;

public class TelegramInputParser {

    public ParsedCommand parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new ParsedCommand("unknown", "");
        }

        String trimmedInput = input.trim();

        if (trimmedInput.startsWith("/search ")) {
            String argument = trimmedInput.substring(8).trim();
            return new ParsedCommand("search", argument);
        } else if (trimmedInput.startsWith("/info ")) {
            String argument = trimmedInput.substring(6).trim();
            return new ParsedCommand("info", argument);
        } else if (trimmedInput.equals("/help")) {
            return new ParsedCommand("help", "");
        } else if (trimmedInput.equals("/start")) {
            return new ParsedCommand("start", "");
        } else if (trimmedInput.startsWith("/")) {
            String command = trimmedInput.substring(1).trim();
            return new ParsedCommand(command, "");
        } else {
            return new ParsedCommand("search", trimmedInput);
        }
    }

    public static class ParsedCommand {
        private final String command;
        private final String argument;

        public ParsedCommand(String command, String argument) {
            this.command = command;
            this.argument = argument;
        }

        public String getCommand() {
            return command;
        }

        public String getArgument() {
            return argument;
        }
    }
}