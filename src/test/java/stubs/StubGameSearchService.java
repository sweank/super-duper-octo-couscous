package stubs;

import core.GameSearchService;

public class StubGameSearchService extends GameSearchService {

    public StubGameSearchService() {
        super(null, null);
    }

    @Override
    public String searchGame(long userId, String gameName) {
        return "STUB_SEARCH: " + gameName;
    }

    @Override
    public String getGameInfo(long userId, int appId) {
        return "STUB_INFO: " + appId;
    }

    @Override
    public String getSearchHistory(long userId, String filter) {
        return "STUB_HISTORY: " + filter;
    }

    @Override
    public String getWelcomeMessage() {
        return "STUB_WELCOME";
    }
}