package models;

public class GameInfo {
    private final String name;
    private final int appId;
    private final Double finalPrice;
    private final Double originalPrice;
    private final String currency;
    private final Integer discountPercent;
    private final boolean isFree;
    private final String imageUrl;
    private final String description;
    private final String releaseDate;
    private final String developers;
    private final String publishers;
    private final String[] categories;

    public GameInfo(String name, int appId, Double finalPrice, Double originalPrice,
                    String currency, Integer discountPercent, boolean isFree,
                    String imageUrl, String description, String releaseDate,
                    String developers, String publishers, String[] categories) {
        this.name = name;
        this.appId = appId;
        this.finalPrice = finalPrice;
        this.originalPrice = originalPrice;
        this.currency = currency;
        this.discountPercent = discountPercent;
        this.isFree = isFree;
        this.imageUrl = imageUrl;
        this.description = description;
        this.releaseDate = releaseDate;
        this.developers = developers;
        this.publishers = publishers;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public int getAppId() {
        return appId;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public boolean isFree() {
        return isFree;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getDevelopers() {
        return developers;
    }

    public String getPublishers() {
        return publishers;
    }

    public String[] getCategories() {
        return categories;
    }

    public String formatForTelegram() {
        StringBuilder info = new StringBuilder();
        info.append("*").append(name).append("*\n\n");

        if (description != null && !description.isEmpty()) {
            String shortDesc = description.length() > 300 ?
                    description.substring(0, 300) + "..." : description;
            info.append(shortDesc.replace("\n", " ")).append("\n\n");
        }

        if (isFree) {
            info.append("*Бесплатно*\n");
        } else if (finalPrice != null) {
            String currencySymbol = getCurrencySymbol(currency);
            if (discountPercent != null && discountPercent > 0 && originalPrice != null) {
                info.append("Цена: ~~").append(currencySymbol).append(String.format("%.2f", originalPrice))
                        .append("~~ → *").append(currencySymbol).append(String.format("%.2f", finalPrice))
                        .append("* (скидка ").append(discountPercent).append("%)\n");
            } else {
                info.append("Цена: *").append(currencySymbol).append(String.format("%.2f", finalPrice)).append("*\n");
            }
        }

        if (developers != null && !developers.isEmpty()) {
            info.append("Разработчик: ").append(developers).append("\n");
        }

        if (publishers != null && !publishers.isEmpty()) {
            info.append("Издатель: ").append(publishers).append("\n");
        }

        if (releaseDate != null && !releaseDate.isEmpty()) {
            info.append("Дата выхода: ").append(releaseDate).append("\n");
        }

        if (categories != null && categories.length > 0) {
            info.append("Категории: ");
            for (int i = 0; i < Math.min(categories.length, 3); i++) {
                if (i > 0) info.append(", ");
                info.append(categories[i]);
            }
            info.append("\n");
        }

        info.append("\nAppID: `").append(appId).append("`\n");
        info.append("[Страница в Steam](https://store.steampowered.com/app/").append(appId).append(")");

        return info.toString();
    }

    public String formatForConsole() {
        StringBuilder info = new StringBuilder();
        info.append("Игра: ").append(name).append("\n");
        info.append("AppID: ").append(appId).append("\n\n");

        if (isFree) {
            info.append("Бесплатно\n");
        } else if (finalPrice != null) {
            String currencySymbol = getCurrencySymbol(currency);
            if (discountPercent != null && discountPercent > 0 && originalPrice != null) {
                info.append("Цена: ").append(currencySymbol).append(String.format("%.2f", finalPrice))
                        .append(" (было: ").append(currencySymbol).append(String.format("%.2f", originalPrice))
                        .append(", скидка ").append(discountPercent).append("%)\n");
            } else {
                info.append("Цена: ").append(currencySymbol).append(String.format("%.2f", finalPrice)).append("\n");
            }
        }

        if (description != null && !description.isEmpty()) {
            info.append("\nОписание: ").append(description.length() > 200 ?
                    description.substring(0, 200) + "..." : description).append("\n");
        }

        if (developers != null && !developers.isEmpty()) {
            info.append("Разработчик: ").append(developers).append("\n");
        }

        info.append("\nСсылка: https://store.steampowered.com/app/").append(appId);
        return info.toString();
    }

    public String format() {
        return formatForConsole();
    }

    private String getCurrencySymbol(String currencyCode) {
        if (currencyCode == null) return "";
        switch (currencyCode) {
            case "USD": return "$";
            case "EUR": return "€";
            case "RUB": return "₽";
            case "UAH": return "₴";
            case "KZT": return "₸";
            case "GBP": return "£";
            default: return currencyCode + " ";
        }
    }

    @Override
    public String toString() {
        return format();
    }
}