package core;

public class TelegramInputParser {

    public ParsedCommand parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new ParsedCommand("unknown", "");
        }

        String trimmedInput = input.trim();

        if (trimmedInput.startsWith("/")) {
            String withoutSlash = trimmedInput.substring(1);
            String[] parts = withoutSlash.split(" ", 2);

            String command = parts[0].toLowerCase();
            String argument = parts.length > 1 ? parts[1].trim() : "";

            return new ParsedCommand(command, argument);
        }


        return new ParsedCommand("search", trimmedInput);
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