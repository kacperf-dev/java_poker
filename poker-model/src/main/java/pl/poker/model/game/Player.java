package pl.poker.model.game;

import pl.poker.model.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Player {
    private final UUID id;
    private final String name;
    private final List<Card> hand;
    private int chips;
    private int currentBet;
    private boolean folded;

    public Player(UUID id, String name, int startingChips) {
        this.id = id;
        this.name = name;
        this.chips = startingChips;
        this.hand = new ArrayList<>();
        this.currentBet = 0;
        this.folded = false;
    }

    /**
     * Dodaje kartę do ręki gracza
     * */
    public void receiveCard(Card card) {
        hand.add(card);
    }

    /**
     * Wymienia karty w ręce gracza
     * @param indicesToRemove indeksy kart do usunięcia
     * @param newCards karty do dodania
     * */
    public void exchangeCards(List<Integer> indicesToRemove, List<Card> newCards) {
        indicesToRemove.sort(Collections.reverseOrder());
        for (int index : indicesToRemove) {
            if (index >= 0 && index < hand.size()) {
                hand.remove(index);
            }
        }
        hand.addAll(newCards);
    }

    /**
     * Gracz stawia pieniądze
     * @param amount ile złotych
     * @throws IllegalArgumentException gdy gracz chce postawić więcej niż ma
     * */
    public int bet(int amount) {
        if (amount > chips) {
            throw new IllegalArgumentException("Nie masz wystarczająco żetonów");
        }
        chips -= amount;
        currentBet += amount;
        return amount;
    }

    /**
     * Gracz pasuje
     * */
    public void fold() {
        this.folded = true;
    }

    /**
     * Resetuje to co gracz postawił
     * */
    public void resetRound() {
        this.currentBet = 0;
    }

    /**
     * Resetuje dane gracza do nowej gry
     * */
    public void resetForNewGame() {
        this.hand.clear();
        this.folded = false;
        this.currentBet = 0;
    }

    /**
     * Dodaje żetony graczowi
     * */
    public void addChips(int amount) {
        chips += amount;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public List<Card> getHand() { return hand; }
    public int getChips() { return chips; }
    public int getCurrentBet() { return currentBet; }
    public boolean hasFolded() { return folded; }

    @Override
    public String toString() {
        return """
                Gracz %s (id: %s) - %dzł
                """
                .formatted(id, name, chips);
    }
}
