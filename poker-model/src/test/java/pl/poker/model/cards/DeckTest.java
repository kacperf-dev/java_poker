package pl.poker.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    private Deck deck;
    @BeforeEach
    void setUp() {
        deck = new Deck();
    }
    @Test
    @DisplayName("Inicjalizacja talii - talia wypełniona kartami")
    void initialize_DeckHas52Cards() {
        assertEquals(52, deck.size());
    }

    @Test
    @DisplayName("Inicjalizacja talii - talia ma unikalne karty")
    void initialize_DeckHasUniqueCards() {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 1; i <= 52; ++i) {
            Card drawnCard = deck.draw();
            assertFalse(drawnCards.contains(drawnCard));
            drawnCards.add(drawnCard);
        }
    }

    @Test
    @DisplayName("Tasowanie - talia potasowana")
    void shuffle_DeckIsShuffled() {
        deck.reset();
        List<Card> drawnCardsBeforeShuffle = new ArrayList<>();
        for (int i = 1; i <= 52; ++i) {
            Card drawnCard = deck.draw();
            drawnCardsBeforeShuffle.add(drawnCard);
        }
        deck.reset();
        List<Card> drawnCardsAfterShuffle = new ArrayList<>();
        for (int i = 1; i <= 52; ++i) {
            Card drawnCard = deck.draw();
            drawnCardsAfterShuffle.add(drawnCard);
        }
        assertNotEquals(drawnCardsBeforeShuffle, drawnCardsAfterShuffle);
    }

    @Test
    @DisplayName("Próba pobrania karty z pustej talii - wyrzuca wyjątek")
    void draw_EmptyDeck_ThrowsException() {
        deck.reset();
        for (int i = 1; i <= 52; ++i) {
            deck.draw();
        }
        assertThrows(IllegalStateException.class, () -> deck.draw());
    }
}
