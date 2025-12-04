package pl.poker.model.evaluator;

import pl.poker.model.cards.Card;

import java.util.List;

public interface HandEvaluator {
    /**
     * Ocenia siłę ręki na podstawie listy kart.
     * @param cards lista kart
     * @return HandResult zawierający rangę układu i posortowane kickery
     * */
    HandResult evaluate(List<Card> cards);
}
