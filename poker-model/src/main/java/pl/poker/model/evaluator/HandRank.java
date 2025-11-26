package pl.poker.model.evaluator;

public enum HandRank {
    HIGH_CARD("Wysoka Karta"),
    ONE_PAIR("Para"),
    TWO_PAIR("Dwie Pary"),
    THREE_OF_A_KIND("Trójka"),
    STRAIGHT("Strit"),
    FLUSH("Kolor"),
    FULL_HOUSE("Full"),
    FOUR_OF_A_KINF("Kareta"),
    STRAIGHT_FLUSH("Poker"),
    ROYAL_FLUSH("Poker Królewski");

    private final String name;

    HandRank(String name) {
        this.name = name;
    }

    /**
     * Zwraca nazwę ręki
     * */
    @Override
    public String toString() {
        return name;
    }
}
