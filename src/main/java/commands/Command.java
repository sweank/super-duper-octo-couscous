package commands;

public interface Command {
    String getName();
    String getDescription();
    String execute(long userId, String argument);
}