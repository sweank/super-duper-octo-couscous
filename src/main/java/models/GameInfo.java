package models;

public class GameInfo {
    private final String name;
    private final int appId;
    private final Double finalPrice;
    private final Double originalPrice;
    private final String currency;
    private final Integer discountPercent;
    private final boolean isFree;

    public GameInfo(String name, int appId, Double finalPrice, Double originalPrice,
                    String currency, Integer discountPercent, boolean isFree) {
        this.name = name;
        this.appId = appId;
        this.finalPrice = finalPrice;
        this.originalPrice = originalPrice;
        this.currency = currency;
        this.discountPercent = discountPercent;
        this.isFree = isFree;
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

    public String format() {
        StringBuilder info = new StringBuilder();
        info.append("Игра: ").append(name).append("\n\n");

        if (isFree) {
            info.append("Бесплатно");
        } else if (finalPrice != null) {
            String currencySymbol = getCurrencySymbol(currency);

            if (discountPercent != null && discountPercent > 0) {
                info.append("Цена: ").append(currencySymbol).append(finalPrice)
                        .append(" (скидка ").append(discountPercent).append("%)");
            } else {
                info.append("Цена: ").append(currencySymbol).append(finalPrice);
            }
        }

        info.append("\n\nhttps://store.steampowered.com/app/").append(appId);
        return info.toString();
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