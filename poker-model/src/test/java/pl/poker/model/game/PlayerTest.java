package pl.poker.model.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.poker.model.cards.Card;
import pl.poker.model.cards.Rank;
import pl.poker.model.cards.Suit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class PlayerTest {
    private Player player;
    private final int initialChips = 1000;
    private final UUID playerId = UUID.randomUUID();
    private final String playerName = "TestPlayer";
    private Card c(Rank rank, Suit suit) {
        return new Card(rank, suit);
    }

    @BeforeEach
    void setUp() {
        player = new Player(playerId, playerName, initialChips);
    }

    @Test
    @DisplayName("Konstruktor powinien poprawnie zainicjować gracza")
    void constructor_ShouldInitializeFields() {
        assertEquals(playerId, player.getId());
        assertEquals(playerName, player.getName());
        assertEquals(initialChips, player.getChips());
        assertTrue(player.getHand().isEmpty());
        assertEquals(0, player.getCurrentBet());
        assertFalse(player.hasFolded());
    }

    @Test
    @DisplayName("receiveCard powinien dodać kartę do ręki")
    void receiveCard_ShouldAddCardToHand() {
        Card card = c(Rank.ACE, Suit.SPADES);
        player.receiveCard(card);

        assertEquals(1, player.getHand().size());
        assertEquals(card, player.getHand().getFirst());
    }

    @Test
    @DisplayName("exchangeCards powinien poprawnie usunąć wskazane karty i dodać nowe")
    void exchangeCards_ShouldRemoveIndicesAndAddNewCards() {
        player.receiveCard(c(Rank.ACE, Suit.SPADES));
        player.receiveCard(c(Rank.KING, Suit.HEARTS));
        player.receiveCard(c(Rank.QUEEN, Suit.CLUBS));
        player.receiveCard(c(Rank.JACK, Suit.DIAMONDS));
        player.receiveCard(c(Rank.TEN, Suit.SPADES));

        List<Integer> indicesToRemove = new ArrayList<>(List.of(1, 3));
        List<Card> newCards = List.of(c(Rank.TWO, Suit.HEARTS), c(Rank.THREE, Suit.HEARTS));

        player.exchangeCards(indicesToRemove, newCards);

        List<Card> hand = player.getHand();
        assertEquals(5, hand.size());

        assertFalse(hand.contains(c(Rank.KING, Suit.HEARTS)));
        assertFalse(hand.contains(c(Rank.JACK, Suit.DIAMONDS)));

        assertTrue(hand.contains(c(Rank.ACE, Suit.SPADES)));
        assertTrue(hand.contains(c(Rank.QUEEN, Suit.CLUBS)));
        assertTrue(hand.contains(c(Rank.TEN, Suit.SPADES)));

        assertTrue(hand.contains(c(Rank.TWO, Suit.HEARTS)));
    }

    @Test
    @DisplayName("exchangeCards nie powinien rzucić błędu dla nieprawidłowych indeksów")
    void exchangeCards_ShouldIgnoreInvalidIndices() {
        player.receiveCard(c(Rank.ACE, Suit.SPADES));

        List<Integer> invalidIndices = new ArrayList<>(List.of(99, -1));
        List<Card> newCards = List.of(c(Rank.TWO, Suit.HEARTS));

        assertDoesNotThrow(() -> player.exchangeCards(invalidIndices, newCards));

        assertEquals(2, player.getHand().size());
    }

    @Test
    @DisplayName("bet powinien zmniejszyć liczbę żetonów i zaktualizować currentBet")
    void bet_ShouldDeductChipsAndUpdateCurrentBet() {
        int betAmount = 200;

        player.bet(betAmount);

        assertEquals(initialChips - betAmount, player.getChips());
        assertEquals(betAmount, player.getCurrentBet());
    }

    @Test
    @DisplayName("bet powinien rzucić wyjątek, gdy gracz nie ma wystarczająco żetonów")
    void bet_ShouldThrowException_WhenNotEnoughChips() {
        int betAmount = initialChips + 1;

        assertThrows(IllegalArgumentException.class, () -> player.bet(betAmount));
    }

    @Test
    @DisplayName("fold powinien ustawić flagę folded na true")
    void fold_ShouldSetFoldedFlag() {
        player.fold();
        assertTrue(player.hasFolded());
    }

    @Test
    @DisplayName("resetRound powinien wyzerować currentBet, ale nie żetony")
    void resetRound_ShouldResetCurrentBetOnly() {
        player.bet(100);
        player.resetRound();

        assertEquals(0, player.getCurrentBet());
        assertEquals(900, player.getChips());
    }

    @Test
    @DisplayName("resetForNewGame powinien wyczyścić rękę i zresetować stany")
    void resetForNewGame_ShouldResetEverythingExceptChips() {
        player.receiveCard(c(Rank.ACE, Suit.SPADES));
        player.bet(100);
        player.fold();

        player.resetForNewGame();

        assertTrue(player.getHand().isEmpty());
        assertFalse(player.hasFolded());
        assertEquals(0, player.getCurrentBet());
        assertEquals(900, player.getChips());
    }

    @Test
    @DisplayName("addChips powinien zwiększyć saldo gracza")
    void addChips_ShouldIncreaseBalance() {
        player.addChips(500);
        assertEquals(1500, player.getChips());
    }
}
