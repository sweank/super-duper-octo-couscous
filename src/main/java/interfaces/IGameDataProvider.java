package interfaces;

public interface IGameDataProvider {
    String getGameInfo(int appId) throws Exception;
    String searchGame(String gameName) throws Exception;
}