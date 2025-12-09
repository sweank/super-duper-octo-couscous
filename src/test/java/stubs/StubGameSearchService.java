package stubs;

import core.GameSearchService;

public class StubGameSearchService extends GameSearchService {

    public StubGameSearchService() {
        super(null);
    }

    @Override
    public String searchGame(String gameName) {
        return "STUB_SEARCH: " + gameName;
    }

    @Override
    public String getGameInfo(int appId) {
        return "STUB_INFO: " + appId;
    }

    @Override
    public String getWelcomeMessage() {
        return "STUB_WELCOME";
    }
}