package pl.poker.model.cards;

public enum Rank {
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("J", 11),
    QUEEN("Q", 12),
    KING("K", 13),
    ACE("A", 14);

    private final String label;
    private final int power;

    Rank(String label, int power) {
        this.label = label;
        this.power = power;
    }

    /**
     * Zwraca siłę karty
     * @return siła karty jako liczba
     * */
    public int getPower() {
        return power;
    }

    /**
     * Zwraca nazwę karty
     * @return nazwa karty jako String
     * */
    @Override
    public String toString() {
        return label;
    }
}
