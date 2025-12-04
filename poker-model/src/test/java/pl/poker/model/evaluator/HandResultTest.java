package pl.poker.model.evaluator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.poker.model.cards.Card;
import pl.poker.model.cards.Rank;
import pl.poker.model.cards.Suit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HandResultTest {
    @Test
    @DisplayName("Porównanie układów - mocniejszy układ")
    void compareTo_StrongerHand_ReturnsPositiveNumber() {
        HandResult fullHouse = new HandResult(HandRank.FULL_HOUSE, List.of());
        HandResult flush = new HandResult(HandRank.FLUSH, List.of());

        assertTrue(fullHouse.compareTo(flush) > 0);
    }

    @Test
    @DisplayName("Porównanie układów - słabszy układ")
    void compareTo_WeakerHand_ReturnsNegativeNumber() {
        HandResult twoPair = new HandResult(HandRank.TWO_PAIR, List.of());
        HandResult threeOfAKind = new HandResult(HandRank.THREE_OF_A_KIND, List.of());

        assertTrue(twoPair.compareTo(threeOfAKind) < 0);
    }

    @Test
    @DisplayName("Równe układy, powinna decydować nasilniejsza karta")
    void compareTo_EqualHands_StrongestKickerDecides() {
        List<Card> player1Cards = List.of(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS),
                new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.THREE, Suit.SPADES),
                new Card(Rank.TWO, Suit.CLUBS)
        );
        List<Card> player2Cards = List.of(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS),
                new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.TEN, Suit.CLUBS),
                new Card(Rank.THREE, Suit.SPADES)
        );

        HandResult player1HandResult = new HandResult(HandRank.THREE_OF_A_KIND, player1Cards);
        HandResult player2HandResult = new HandResult(HandRank.THREE_OF_A_KIND, player2Cards);

        assertTrue(player1HandResult.compareTo(player2HandResult) < 0);
    }
}
