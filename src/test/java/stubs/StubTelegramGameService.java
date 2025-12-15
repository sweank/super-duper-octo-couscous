package stubs;

import implementations.TelegramGameService;

public class StubTelegramGameService extends TelegramGameService {

    public StubTelegramGameService() {
        super(null, null);
    }

    @Override
    public String getGameInfoWithImage(long userId, int appId) {
        return "STUB_TG_IMAGE: " + appId;
    }

    @Override
    public String getGameInfoForTelegram(long userId, int appId) {
        return "STUB_TG_TEXT: " + appId;
    }
}