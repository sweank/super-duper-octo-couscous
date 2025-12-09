package stubs;

import implementations.TelegramGameService;

public class StubTelegramGameService extends TelegramGameService {

    public StubTelegramGameService() {
        super(null);
    }

    @Override
    public String getGameInfoWithImage(int appId) {
        return "STUB_TG_IMAGE: " + appId;
    }

    @Override
    public String getGameInfoForTelegram(int appId) {
        return "STUB_TG_TEXT: " + appId;
    }
}