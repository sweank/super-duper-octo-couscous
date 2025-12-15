package implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.GameInfo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SteamApiClientTest {

    private final SteamApiClient client = new SteamApiClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testParseGameInfoFull() throws Exception {
        String json = "{" +
                "\"steam_appid\": 730," +
                "\"name\": \"Counter-Strike 2\"," +
                "\"is_free\": true," +
                "\"header_image\": \"http://img.url/cs.jpg\"," +
                "\"short_description\": \"Shooter game\"," +
                "\"developers\": [\"Valve\"]," +
                "\"publishers\": [\"Valve\"]" +
                "}";

        JsonNode node = mapper.readTree(json);

        GameInfo info = client.parseGameInfo(node);

        assertEquals("Counter-Strike 2", info.getName());
        assertEquals(730, info.getAppId());
        assertTrue(info.isFree());
        assertEquals("http://img.url/cs.jpg", info.getImageUrl());
        assertEquals("Valve", info.getDevelopers());
    }

    @Test
    void testParseGameInfoPaid() throws Exception {
        String json = "{" +
                "\"steam_appid\": 10," +
                "\"name\": \"Paid Game\"," +
                "\"is_free\": false," +
                "\"price_overview\": {" +
                "\"final\": 1000," +
                "\"initial\": 2000," +
                "\"currency\": \"RUB\"," +
                "\"discount_percent\": 50" +
                "}" +
                "}";

        JsonNode node = mapper.readTree(json);
        GameInfo info = client.parseGameInfo(node);

        assertFalse(info.isFree());
        assertEquals(10.0, info.getFinalPrice());
        assertEquals(50, info.getDiscountPercent());
    }
}