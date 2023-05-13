package p1.comparator;

import p1.card.Card;
import p1.card.CardColor;

import java.util.Comparator;

/**
 * Compares two {@link Card}s.
 * <p>
 * The cards are first compared by their value and then by their {@link CardColor}.
 *
 * @see Card
 */
public class CardComparator implements Comparator<Card> {

    private static final Comparator<Card> COMPARATOR = Comparator.comparingInt(Card::cardValue).thenComparing(Card::cardColor);

    /**
     * Compares two {@link Card}s.
     * <p>
     * The cards are first compared by their value and then by their {@link CardColor}.
     * <p>
     * The value of the cards compared by the natural order of the {@link Integer} class.
     * <p>
     * The color of the cards compared using the following order: {@link CardColor#CLUBS} > {@link CardColor#SPADES} >.{@link CardColor#HEARTS} > {@link CardColor#DIAMONDS}.
     *
     * @param o1 the first {@link Card} to compare.
     * @param o2 the second {@link Card} to compare.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     * @throws NullPointerException if either of the {@link Card}s is null.
     *
     * @see Card
     * @see CardColor
     * @see Comparator#compare(Object, Object)
     */
    @Override
    public int compare(Card o1, Card o2) {
        return COMPARATOR.compare(o1, o2);
    }
}
