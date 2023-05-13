package p1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.comparator.CountingComparator;
import p1.transformers.MethodInterceptor;

import java.util.Comparator;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class CountingComparatorTest {

    private final static Comparator<Integer> DELEGATE = Comparator.reverseOrder();

    private static CountingComparator<Integer> countingComparator;

    @BeforeEach
    public void setup() {
        MethodInterceptor.reset();
        countingComparator = new CountingComparator<>(DELEGATE);
    }

    @AfterEach
    public void checkIllegalMethods() {
        IllegalMethodsCheck.checkMethods();
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H2_CountingComparatorTests.json", data = "compareTest")
    public void testCompare(@Property("value1") Integer value1, @Property("value2") Integer value2) {
        Context context = contextBuilder()
            .subject("CountingComparator#compare()")
            .add("value1", value1)
            .add("value2", value2)
            .add("delegate", "reverse_order")
            .build();

        assertEquals(DELEGATE.compare(value1, value2), countingComparator.compare(value1, value2), context,
            result -> "The methode compare() should return the same value as the delegate.");
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H2_CountingComparatorTests.json", data = "getComparisonsCountTest")
    public void testGetComparisonsCount(@Property("comparisons") Integer comparisons) {
        Context context = contextBuilder()
            .subject("CountingComparator#getComparisonsCount()")
            .add("comparisons", comparisons)
            .build();

        for (int i = 0; i < comparisons; i++) {
            //noinspection EqualsWithItself,ResultOfMethodCallIgnored
            countingComparator.compare(0,0);
        }

        assertEquals(comparisons, countingComparator.getComparisonsCount(), context,
            result -> "The methode getComparisonsCount() should return the number of comparisons made.");
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H2_CountingComparatorTests.json", data = "resetTest")
    public void testReset(@Property("comparisons") Integer comparisons) {
        Context context = contextBuilder()
            .subject("CountingComparator#reset()")
            .add("comparisons", comparisons)
            .build();

        for (int i = 0; i < comparisons; i++) {
            //noinspection EqualsWithItself,ResultOfMethodCallIgnored
            countingComparator.compare(0,0);
        }

        countingComparator.reset();

        assertEquals(0, countingComparator.getComparisonsCount(), context,
            result -> "getComparisonsCount should return 0 after reset() got called.");
    }

}
