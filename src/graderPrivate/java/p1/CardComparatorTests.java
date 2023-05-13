package p1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.card.Card;
import p1.card.CardColor;
import p1.comparator.CardComparator;
import p1.transformers.MethodInterceptor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class CardComparatorTests {

    private static final Comparator<Card> CARD_COMPARATOR = new CardComparator();

    @BeforeEach
    public void setup() {
        MethodInterceptor.reset();
    }

    @AfterEach
    public void checkIllegalMethods() {
        IllegalMethodsCheck.checkMethods("^java/lang/Integer.+");
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H1_CardComparatorTests.json", data = "valueTest")
    public void testValue(@Property("color") CardColor color,
                          @Property("values") List<Integer> values,
                          @Property("expectedSorting") List<String> expectedSorting) {
        Card[] cards = values.stream()
            .map(i -> new Card(color, i))
            .sorted(CARD_COMPARATOR)
            .toArray(Card[]::new);
        Context context = contextBuilder()
            .subject("CardComparator#compare()")
            .add("cardColor", color)
            .add("cardValues", values)
            .add("expectedSorting", expectedSorting.toString())
            .add("actualSorting", Arrays.stream(cards).map(this::cardToString).toList().toString())
            .build();
        assertTrue(isSorted(cards), context, result -> "The cards did not get sorted correctly using the CardComparator.");
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H1_CardComparatorTests.json", data = "colorTest")
    public void testColor(@Property("colors") List<CardColor> colors,
                          @Property("values") List<Integer> values,
                          @Property("expectedSorting") List<String> expectedSorting) {
        Card[] cards = IntStream.range(0, colors.size())
            .mapToObj(i -> new Card(colors.get(i), values.get(i)))
            .sorted(CARD_COMPARATOR)
            .toArray(Card[]::new);
        Context context = contextBuilder()
            .subject("CardComparator#compare()")
            .add("cardColor", colors)
            .add("cardValues", values)
            .add("expectedSorting", expectedSorting.toString())
            .add("actualSorting", Arrays.stream(cards).map(this::cardToString).toList().toString())
            .build();
        assertTrue(isSorted(cards), context, result -> "The cards did not get sorted correctly using the CardComparator.");
    }

    private static boolean isSorted(Card[] cards) {
        for (int i = 0; i < cards.length - 1; i++) {
            if (cards[i].cardValue() > cards[i + 1].cardValue() ||
                (cards[i].cardValue() == cards[i + 1].cardValue() && cards[i].cardColor().ordinal() > cards[i + 1].cardColor().ordinal())) {
                return false;
            }
        }

        return true;
    }

    private String cardToString(Card card) {

        String color = switch (card.cardColor()) {
            case CLUBS -> "C";
            case DIAMONDS -> "D";
            case HEARTS -> "H";
            case SPADES -> "S";
        };

        return color + card.cardValue();
    }
}
