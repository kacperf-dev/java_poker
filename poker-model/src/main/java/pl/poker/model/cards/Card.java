package pl.poker.model.cards;

public record Card(Rank rank, Suit suit) implements Comparable<Card> {
    /**
     * Porównuje dwie karty
     * @return czy aktualna karta jest silniejsza od innej
     * */
    @Override
    public int compareTo(Card other) {
        int rankComparison = Integer.compare(this.rank.getPower(), other.rank.getPower());
        if (rankComparison == 0) {
            return this.suit.compareTo(other.suit);
        }
        return rankComparison;
    }

    /**
     * Zwraca kartę w formacie ranga kolor
     * @return ranga kolor
     * */
    @Override
    public String toString() {
        return String.format("%s%s", rank.toString(), suit.toString());
    }
}
