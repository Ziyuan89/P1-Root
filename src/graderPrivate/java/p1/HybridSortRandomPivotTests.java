package p1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.sort.ArraySortList;
import p1.sort.HybridSortRandomPivot;
import p1.sort.SortList;
import p1.transformers.MethodInterceptor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertCallEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.call;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.fail;

@TestForSubmission
public class HybridSortRandomPivotTests {

    private static final Comparator<Integer> COMPARATOR = Comparator.naturalOrder();
    private static final HybridSortRandomPivot<Integer> HYBRID_SORT_RANDOM_PIVOT = new HybridSortRandomPivot<>(5, COMPARATOR);

    private static int returnValue = 0;
    private static int partitionCallCount = 0;
    private static SortList<Integer> partitionSortList;
    private static int partitionLeft;
    private static int partitionRight;

    @BeforeEach
    public void setup() {
        MethodInterceptor.reset();
        partitionCallCount = 0;
        partitionSortList = null;
        partitionLeft = -1;
        partitionRight = -1;
    }

    @AfterEach
    public void checkIllegalMethods() {
        IllegalMethodsCheck.checkMethods("^java/util/Random.+");
    }

    @SuppressWarnings("unused")//This method will be called instead of HybridSort#partition() in HybridSortRandomPivot after applying the SuperPartitionTransformer
    public static int partition(SortList<Integer> sortList, int left, int right) {
        partitionCallCount++;
        partitionSortList = sortList;
        partitionLeft = left;
        partitionRight = right;
        return returnValue;
    }

    @ParameterizedTest
    @ExtendWith(JagrExecutionCondition.class)
    @JsonClasspathSource(value = "H6_HybridSortRandomPivotTests.json", data = "swapWithoutBoundsTest")
    public void testSwapWithoutBounds(@Property("values") List<Integer> values) {

        testSwap(values, 0, values.size() - 1);
    }

    @ParameterizedTest
    @ExtendWith(JagrExecutionCondition.class)
    @JsonClasspathSource(value = "H6_HybridSortRandomPivotTests.json", data = "swapWithBoundsTest")
    public void testSwapWithBounds(@Property("values") List<Integer> values,
                                   @Property("left") int left,
                                   @Property("right") int right) {

        testSwap(values, left, right);
    }

    @ParameterizedTest
    @ExtendWith(JagrExecutionCondition.class)
    @JsonClasspathSource(value = "H6_HybridSortRandomPivotTests.json", data = "superCallTest")
    public void testSuperCall(@Property("values") List<Integer> values,
                              @Property("left") int left,
                              @Property("right") int right,
                              @Property("returnValue") int returnValue) {

        Context context = contextBuilder()
            .subject("HybridSortRandomPivot#partition()")
            .add("values", values)
            .add("left", left)
            .add("right", right)
            .build();

        HybridSortRandomPivotTests.returnValue = returnValue;

        SortList<Integer> sortList = new ArraySortList<>(values);

        assertCallEquals(returnValue, () -> HYBRID_SORT_RANDOM_PIVOT.partition(sortList, left, right), context,
            result -> "The partition() method should return the same value as the super class' partition() method.");

        assertEquals(1, partitionCallCount, context, result -> "The partition() method should call the super class' partition() method exactly once.");
        assertEquals(sortList, partitionSortList, context, result -> "The partition() method should call the super class' partition() method with the same SortList.");
        assertEquals(left, partitionLeft, context, result -> "The partition() method should call the super class' partition() method with the same left index.");
        assertEquals(right, partitionRight, context, result -> "The partition() method should call the super class' partition() method with the same right index.");
    }

    private void testSwap(List<Integer> values, int left, int right) {

        int iterations = 1000;
        Map<Integer, Integer> indexSwappedCounts = new HashMap<>();

        for (int i = 0; i < iterations; i++) {


            SortList<Integer> sortList = new ArraySortList<>(values);

            call(() -> HYBRID_SORT_RANDOM_PIVOT.partition(sortList, left, right), contextBuilder()
                .subject("HybridSortRandomPivot#partition()")
                .add("values", values)
                .add("i", 0)
                .add("j", 1)
                .build(), result -> "partition() should not throw an exception.");

            int swappedIndex = findSwappedIndices(values, sortList, left, right);
            indexSwappedCounts.put(swappedIndex, indexSwappedCounts.getOrDefault(swappedIndex, 0) + 1);
        }

        int expected = iterations / (right - left + 1);
        expected = (int) (0.8 * expected);

        for (int i = left; i <= right; i++) {
            int finalI = i;
            int finalExpected = expected;
            assertTrue(indexSwappedCounts.getOrDefault(i, 0) >= expected, contextBuilder()
                .subject("HybridSortRandomPivot#partition()")
                .add("values", values)
                .add("left", left)
                .add("right", right)
                .build(), result -> ("The value at index %d was not swapped with the value at the left bound at least %d times after calling the partition() method %d times. Actual swap count: %d")
                .formatted(finalI, finalExpected, iterations, indexSwappedCounts.getOrDefault(finalI, 0)));
        }

    }

    private int findSwappedIndices(List<Integer> values, SortList<Integer> sortList, int left, int right) {

        Context context = contextBuilder()
            .subject("HybridSortRandomPivot#partition()")
            .add("original values", values)
            .add("actual values", sortList)
            .build();

        int changedIndex = -1;

        for (int i = left + 1; i <= right; i++) {
            if (!values.get(i).equals(sortList.get(i))) {
                if (changedIndex != -1) {
                    int finalI = i;
                    int finalChangedIndex = changedIndex;
                    fail(context, result -> "More than one value were changed (at index %d and %d."
                        .formatted(finalChangedIndex, finalI));
                }
                changedIndex = i;
            }
        }

        if (changedIndex != -1) {
            int finalChangedIndex = changedIndex;
            assertEquals(values.get(left), sortList.get(changedIndex), context,
                result -> "The value at index %d is not equal to previous value at the left bound."
                    .formatted(finalChangedIndex));
            assertEquals(values.get(changedIndex), sortList.get(left), context,
                result -> "The value at the left bound is not equal to the value that was at index %d."
                    .formatted(finalChangedIndex));
        }

        return changedIndex == -1 ? left : changedIndex;
    }

}
