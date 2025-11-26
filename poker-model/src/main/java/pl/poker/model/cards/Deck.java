package pl.poker.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        initialize();
    }

    /**
     * Wypełnia talię wszystkimi kartami
     * */
    private void initialize() {
        cards.clear();
        for  (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    /**
     * Losowo tasuje karty
     * */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Pobiera kartę z góry talii
     * @return Karta
     * @throws IllegalStateException jeśli talia jest pusta
     * */
    public Card draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("W talii nie ma kart");
        }

        return cards.removeFirst();
    }

    /**
     * Zwraca rozmiar talii
     * */
    public int size() {
        return cards.size();
    }

    /**
     * Resetuje talię i tasuje
     * */
    public void reset() {
        initialize();
        shuffle();
    }
}
