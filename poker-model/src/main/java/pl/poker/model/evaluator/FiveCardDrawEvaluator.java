package pl.poker.model.evaluator;

import pl.poker.model.cards.Card;
import pl.poker.model.cards.Rank;
import pl.poker.model.cards.Suit;

import java.util.*;
import java.util.stream.Collectors;

public class FiveCardDrawEvaluator implements HandEvaluator {
    @Override
    public HandResult evaluate(List<Card> cards) {
        if (cards.size() != 5) {
            throw new IllegalArgumentException("Do oceny ręki potrzebne jest 5 kart");
        }

        List<Card> sortedCards = new ArrayList<>(cards);
        Collections.sort(sortedCards, Collections.reverseOrder());

        Map<Rank, Long> rankCounts = sortedCards.stream().collect(Collectors.groupingBy(Card::rank, Collectors.counting()));

        boolean isFlush = checkFlush(sortedCards);
        boolean isStraight = checkStraight(sortedCards);

        if  (isFlush && isStraight) {
            return diffrentiateRoyalAndStraightFlush(sortedCards);
        }

        if (rankCounts.containsValue(4L)) {
            Rank quadRank = getKeyByValue(rankCounts, 4L);
            return new HandResult(HandRank.FOUR_OF_A_KIND, reorderBasedOnCounts(sortedCards, quadRank));
        }

        if  (rankCounts.containsValue(3L) && rankCounts.containsValue(2L)) {
            Rank threeRank = getKeyByValue(rankCounts, 3L);
            Rank twoRank = getKeyByValue(rankCounts, 2L);

            return new HandResult(HandRank.FULL_HOUSE, reorderForFullHouse(sortedCards, threeRank, twoRank));
        }

        if (isFlush) {
            return new HandResult(HandRank.FLUSH, sortedCards);
        }

        if (isStraight) {
            if (sortedCards.getFirst().rank() == Rank.ACE &&  sortedCards.getLast().rank() == Rank.TWO) {
                return new HandResult(HandRank.STRAIGHT, moveAceToEnd(sortedCards));
            }
            return new HandResult(HandRank.STRAIGHT, sortedCards);
        }

        if (rankCounts.containsValue(3L)) {
            Rank threeRank = getKeyByValue(rankCounts, 3L);
            return new HandResult(HandRank.THREE_OF_A_KIND, reorderBasedOnCounts(sortedCards, threeRank));
        }

        if (Collections.frequency(new ArrayList<>(rankCounts.values()), 2L) == 2) {
            List<Rank> pairRanks = rankCounts.entrySet().stream()
                    .filter(entry -> entry.getValue() == 2)
                    .map(Map.Entry::getKey)
                    .sorted(Comparator.comparingInt(Rank::getPower).reversed())
                    .toList();

            return new HandResult(HandRank.TWO_PAIR, reorderForTwoPairs(sortedCards, pairRanks.get(0), pairRanks.get(1)));
        }

        if (rankCounts.containsValue(2L)) {
            Rank pairRank = getKeyByValue(rankCounts, 2L);
            return new HandResult(HandRank.ONE_PAIR, reorderBasedOnCounts(sortedCards, pairRank));
        }

        return new HandResult(HandRank.HIGH_CARD, sortedCards);
    }

    /**
     * Metoda pomocnicza do sprawdzenia czy układ to Kolor
     * */
    private boolean checkFlush(List<Card> cards) {
        Suit firstSuit = cards.getFirst().suit();
        return cards.stream().allMatch(card -> card.suit() ==  firstSuit);
    }

    /**
     * Metoda pomocnicza do sprawdzenia czy układ to Strit
     * */
    private boolean checkStraight(List<Card> cards) {
        if (cards.get(0).rank() == Rank.ACE &&
            cards.get(1).rank() == Rank.FIVE &&
            cards.get(2).rank() == Rank.FOUR &&
            cards.get(3).rank() == Rank.THREE &&
            cards.get(4).rank() == Rank.TWO
            ) {
            return true;
        }

        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).rank().getPower() - 1 != cards.get(i+1).rank().getPower()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Rozróżnia Pokera Królewskiego i Pokera
     * */
    private HandResult diffrentiateRoyalAndStraightFlush(List<Card> cards) {
        if (cards.getFirst().rank() == Rank.ACE && cards.get(1).rank() == Rank.KING) {
            return new HandResult(HandRank.ROYAL_FLUSH, cards);
        }
        if (cards.getFirst().rank() == Rank.ACE && cards.getLast().rank() == Rank.TWO) {
            return new HandResult(HandRank.STRAIGHT_FLUSH, moveAceToEnd(cards));
        }
        return new HandResult(HandRank.STRAIGHT_FLUSH, cards);
    }

    /**
     * Przesuwa Asa z początku na koniec
     * */
    private List<Card> moveAceToEnd(List<Card> cards) {
        List<Card> result = new ArrayList<>(cards);
        Card ace = result.removeFirst();
        result.add(ace);
        return result;
    }

    /**
     * Pobiera klucz (Rank) z mapy na podstawie wartości (licznika)
     * */
    private Rank getKeyByValue(Map<Rank, Long> map, Long value) {
        return map.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
    }

    /**
     * Przesuwa karty o zadanej randze na początek listy.
     * Używane dla: Karety, Trójki, Pary
     * */
    private List<Card> reorderBasedOnCounts(List<Card> cards, Rank rank) {
        List<Card> main = new ArrayList<>();
        List<Card> kickers =  new ArrayList<>();

        for(Card card : cards) {
            if (card.rank() == rank) {
                main.add(card);
            } else {
                kickers.add(card);
            }
        }

        main.addAll(kickers);
        return main;
    }

    /**
     * Układa karty dla Fulla: najpierw Trójka, potem para
     * */
    private List<Card> reorderForFullHouse(List<Card> cards, Rank threeRank, Rank twoRank) {
        List<Card> triplet =  new ArrayList<>();
        List<Card> pair =  new ArrayList<>();

        for(Card card : cards) {
            if (card.rank() == threeRank) triplet.add(card);
            else if (card.rank() == twoRank) pair.add(card);
        }

        triplet.addAll(pair);
        return triplet;
    }

    /**
     * Układa karty dla Dwóch Par: wyższa para, niższa para, kicker
     * */
    private List<Card> reorderForTwoPairs(List<Card> cards, Rank highPair, Rank lowPair) {
        List<Card> high =  new ArrayList<>();
        List<Card> low = new ArrayList<>();
        List<Card> kicker = new ArrayList<>();

        for(Card card : cards) {
            if (card.rank() == highPair) high.add(card);
            else if (card.rank() == lowPair) low.add(card);
            else kicker.add(card);
        }

        high.addAll(low);
        high.addAll(kicker);

        return high;
    }
}
