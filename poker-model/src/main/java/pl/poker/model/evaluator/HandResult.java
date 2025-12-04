package pl.poker.model.evaluator;

import pl.poker.model.cards.Card;

import java.util.List;

public record HandResult(HandRank rank, List<Card> kickers) implements Comparable<HandResult> {
    /**
     * Porównuje dwa układy, gdy uklady są takie same porównuje po kolei kickery
     * @param other układ, do którego aktualny jest porównywany
     * @return liczba ujemna (słabszy), zero (równe), liczba dodatnia (lepszy)
     * */
    @Override
    public int compareTo(HandResult other) {
        int rankComparison = this.rank.compareTo(other.rank);
        if (rankComparison != 0) {
            return rankComparison;
        }

        for  (int i = 0; i < kickers.size(); i++) {
            int powerComparison = Integer.compare(
                this.kickers.get(i).rank().getPower(),
                other.kickers.get(i).rank().getPower()
            );

            if (powerComparison != 0) {
                return powerComparison;
            }
        }
        return 0;
    }
}
