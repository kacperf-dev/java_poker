package pl.poker.model.cards;

public enum Suit {
    HEARTS("♥"),
    DIAMONDS("♦"),
    CLUBS("♣"),
    SPADES("♠");

    private final String symbol;

    Suit(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Zwraca symbol karty
     * @return symbol karty Unicode
     * */
    @Override
    public String toString() {
        return symbol;
    }
}
