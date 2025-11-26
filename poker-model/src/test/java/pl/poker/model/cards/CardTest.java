package pl.poker.model.cards;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CardTest {
    @Test
    @DisplayName("Porównanie mocy kart")
    void compareTo_CorrectlyComparesCards() {
        List<Card> worseCards = new ArrayList<>();
        List<Card> betterCards = new ArrayList<>();

        worseCards.add(new Card(Rank.TWO, Suit.CLUBS));
        worseCards.add(new Card(Rank.THREE, Suit.CLUBS));
        worseCards.add(new Card(Rank.JACK, Suit.DIAMONDS));
        worseCards.add(new Card(Rank.QUEEN, Suit.SPADES));

        betterCards.add(new Card(Rank.THREE, Suit.SPADES));
        betterCards.add(new Card(Rank.QUEEN, Suit.SPADES));
        betterCards.add(new Card(Rank.KING, Suit.HEARTS));
        betterCards.add(new Card(Rank.ACE, Suit.CLUBS));

        for (int i = 0; i < worseCards.size(); ++i) {
            assertTrue(worseCards.get(i).compareTo(betterCards.get(i)) < 0);
        }
    }
}
