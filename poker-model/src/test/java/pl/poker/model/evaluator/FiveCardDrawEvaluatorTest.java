package pl.poker.model.evaluator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.poker.model.cards.Card;
import pl.poker.model.cards.Rank;
import pl.poker.model.cards.Suit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pl.poker.model.cards.Rank.*;
import static pl.poker.model.cards.Suit.*;

class FiveCardDrawEvaluatorTest {
    private final FiveCardDrawEvaluator evaluator = new FiveCardDrawEvaluator();
    private Card c(Rank rank, Suit suit) {
        return new Card(rank, suit);
    }

    @Test
    @DisplayName("Rzuca wyjątek, gdy liczba kart != 5")
    void cardCountInvalid_ThrowsException() {
        List<Card> cards = List.of(
                c(ACE, SPADES), c(KING, SPADES), c(QUEEN, SPADES), c(JACK, SPADES)
        );

        assertThrows(IllegalArgumentException.class, () -> evaluator.evaluate(cards));
    }

    @Test
    @DisplayName("Wykrywanie Pokera Królewskiego")
    void royalFlush_IdentifiesCorrectly() {
        List<Card> hand = List.of(
                c(TEN, HEARTS), c(JACK, HEARTS), c(QUEEN, HEARTS), c(KING, HEARTS), c(ACE, HEARTS)
        );

        HandResult result = evaluator.evaluate(hand);
        assertEquals(HandRank.ROYAL_FLUSH, result.rank());
    }

    @Test
    @DisplayName("Wykrywanie Karety i ustawienie jej na początek listy")
    void fourOfKind_IdentifiesCorrectly_AndSortsCorrectly() {
        List<Card> hand = List.of(
                c(FIVE, HEARTS), c(FIVE, DIAMONDS), c(FIVE, CLUBS), c(FIVE, SPADES), c(ACE, SPADES)
        );

        HandResult result = evaluator.evaluate(hand);
        assertEquals(HandRank.FOUR_OF_A_KIND, result.rank());
        assertEquals(FIVE, result.kickers().get(0).rank());
        assertEquals(FIVE, result.kickers().get(3).rank());
        assertEquals(ACE, result.kickers().get(4).rank());
    }

    @Test
    @DisplayName("Wykrywanie Fulla i ustawienie Trójki przed Parą")
    void fullHouse_IdentifiesCorrectly_AndPlacesTripleFirst() {
        List<Card> hand = List.of(
                c(TWO, HEARTS), c(TWO, DIAMONDS), c(TWO, CLUBS), c(KING, SPADES), c(KING, HEARTS)
        );

        HandResult result = evaluator.evaluate(hand);

        assertEquals(HandRank.FULL_HOUSE, result.rank());
        assertEquals(TWO, result.kickers().get(0).rank());
        assertEquals(KING, result.kickers().get(4).rank());
    }

    @Test
    @DisplayName("Wykrywanie Koloru")
    void flush_IdentifiesCorrectly() {
        List<Card> hand = List.of(
                c(TWO, DIAMONDS), c(FIVE, DIAMONDS), c(NINE, DIAMONDS), c(JACK, DIAMONDS), c(ACE, DIAMONDS)
        );

        HandResult result = evaluator.evaluate(hand);

        assertEquals(HandRank.FLUSH, result.rank());
    }

    @Test
    @DisplayName("Wykrywanie Strita")
    void straight_IdentifiesCorrectly() {
        List<Card> hand = List.of(
                c(FIVE, CLUBS), c(SIX, DIAMONDS), c(SEVEN, HEARTS), c(EIGHT, SPADES), c(NINE, CLUBS)
        );

        HandResult result = evaluator.evaluate(hand);

        assertEquals(HandRank.STRAIGHT, result.rank());
        assertEquals(NINE, result.kickers().getFirst().rank());
    }

    @Test
    @DisplayName("Wykrywanie Strita Wheel i ustawienie 5 jako najwyższą kartę")
    void lowStraight_IdentifiesCorrectly_Places5AsHighestCard() {
        List<Card> hand = List.of(
                c(ACE, CLUBS), c(TWO, DIAMONDS), c(THREE, HEARTS), c(FOUR, SPADES), c(FIVE, CLUBS)
        );

        HandResult result = evaluator.evaluate(hand);

        assertEquals(HandRank.STRAIGHT, result.rank());
        assertEquals(FIVE, result.kickers().get(0).rank());
        assertEquals(ACE, result.kickers().get(4).rank());
    }

    @Test
    @DisplayName("Wykrywanie Trójki")
    void threeOfKind_IdentifiesCorrectly() {
        List<Card> hand = List.of(
                c(EIGHT, CLUBS), c(EIGHT, DIAMONDS), c(EIGHT, HEARTS), c(KING, SPADES), c(TWO, CLUBS)
        );

        HandResult result = evaluator.evaluate(hand);

        assertEquals(HandRank.THREE_OF_A_KIND, result.rank());
        assertEquals(EIGHT, result.kickers().getFirst().rank());
    }

    @Test
    @DisplayName("Wykrywanie Dwóch Par i posortowanie ich")
    void twoPairs_IdentifiesCorrectly_AndSortsCorrectly() {
        List<Card> hand = List.of(
                c(FOUR, CLUBS), c(FOUR, DIAMONDS), c(JACK, HEARTS), c(JACK, SPADES), c(ACE, CLUBS)
        );

        HandResult result = evaluator.evaluate(hand);

        assertEquals(HandRank.TWO_PAIR, result.rank());
        assertEquals(JACK, result.kickers().get(0).rank());
        assertEquals(FOUR, result.kickers().get(2).rank());
        assertEquals(ACE, result.kickers().get(4).rank());
    }

    @Test
    @DisplayName("Wykrywanie Pary")
    void pair_IdentifiesCorrectly() {
        List<Card> hand = List.of(
                c(TEN, CLUBS), c(TEN, DIAMONDS), c(KING, HEARTS), c(FOUR, SPADES), c(TWO, CLUBS)
        );

        HandResult result = evaluator.evaluate(hand);

        assertEquals(HandRank.ONE_PAIR, result.rank());
        assertEquals(TEN, result.kickers().getFirst().rank());
    }

    @Test
    @DisplayName("Brak układu - zwraca High Card")
    void highCard_IdentifiesCorrectly() {
        List<Card> hand = List.of(
                c(ACE, CLUBS), c(KING, DIAMONDS), c(JACK, HEARTS), c(NINE, SPADES), c(TWO, CLUBS)
        );

        HandResult result = evaluator.evaluate(hand);

        assertEquals(HandRank.HIGH_CARD, result.rank());
        assertEquals(ACE, result.kickers().getFirst().rank());
        assertEquals(TWO, result.kickers().get(4).rank());
    }
}
