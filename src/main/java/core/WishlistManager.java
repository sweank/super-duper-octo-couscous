package core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WishlistManager {
    private static final Map<Long, Set<String>> userWishlists = new HashMap<>();

    public synchronized String addToWishlist(long userId, String appId, String gameName) {
        Set<String> wishlist = userWishlists.computeIfAbsent(userId, k -> new HashSet<>());

        if (wishlist.contains(appId)) {
            return "Игра \"" + gameName + "\" уже в вашем списке желаемого!";
        }

        wishlist.add(appId);
        return "Игра \"" + gameName + "\" добавлена в список желаемого!";
    }

    public synchronized String showWishlist(long userId) {
        Set<String> wishlist = userWishlists.get(userId);

        if (wishlist == null || wishlist.isEmpty()) {
            return "Ваш список желаемого пуст!\n\nДобавьте игры с помощью команды /add [AppID]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Ваш список желаемого:\n\n");

        int i = 1;
        for (String appId : wishlist) {
            sb.append(i).append(". Игра с ID: ").append(appId).append("\n");
            sb.append("Чтобы узнать подробности: /info ").append(appId).append("\n\n");
            i++;
        }

        sb.append("Всего игр: ").append(wishlist.size()).append("\n");
        sb.append("Чтобы удалить игру, используйте: /remove [AppID]");

        return sb.toString();
    }

    public synchronized String removeFromWishlist(long userId, String appId) {
        Set<String> wishlist = userWishlists.get(userId);

        if (wishlist == null || wishlist.isEmpty()) {
            return "Ваш список желаемого пуст!";
        }

        if (wishlist.remove(appId)) {
            return "Игра с ID " + appId + " удалена из списка желаемого!";
        } else {
            return "Игра с ID " + appId + " не найдена в вашем списке.";
        }
    }
}