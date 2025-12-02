package interfaces;

public interface GameDataProvider {
    String getGameInfo(int appId) throws Exception;
    String searchGame(String gameName) throws Exception;
}