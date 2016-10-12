package my.battleships.ships;


public enum ShipTypes {
    FOUR_DECKER(4, 1, "four-decker ship"),
    THREE_DECKER(3, 2, "three-decker ship"),
    TWO_DECKER(2, 3, "two-decker ship"),
    SINGLE_DECKER(1, 4, "single-decker ship"),
    MINE(1, 2, "mine");

    private int decks;
    private int count;
    private String name;

    ShipTypes(int decks, int count, String name) {
        this.decks = decks;
        this.count = count;
        this.name = name;
    }

    public int getDecks() {
        return decks;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }
}
