package core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WishlistManagerTest {

    private WishlistManager manager;
    private final long userId = 1L;

    @BeforeEach
    void setUp() {
        manager = new WishlistManager();
    }

    @Test
    void testAddAndRemove() {
        manager.addToWishlist(userId, "10", "Game 1");
        String listWithGame = manager.showWishlist(userId);

        assertTrue(listWithGame.contains("10"), "Список должен содержать ID добавленной игры");

        manager.removeFromWishlist(userId, "10");
        String emptyList = manager.showWishlist(userId);

        assertFalse(emptyList.contains("10"), "Удаленная игра не должна быть в списке");

        assertNotNull(emptyList);
        assertFalse(emptyList.isEmpty());
    }
}