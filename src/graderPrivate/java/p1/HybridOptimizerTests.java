package p1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.mockito.MockedConstruction;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.sort.ArraySortList;
import p1.sort.HybridOptimizer;
import p1.sort.HybridSort;
import p1.sort.SortList;
import p1.transformers.MethodInterceptor;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertCallEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class HybridOptimizerTests {

    private static final Comparator<Integer> COMPARATOR = Comparator.naturalOrder();
    private static HybridSort<Integer> hybridSort;

    @BeforeEach
    public void setup() {
        MethodInterceptor.reset();
        hybridSort = spy(new HybridSort<>(5, COMPARATOR));
    }

    @AfterEach
    public void checkIllegalMethods() {
        IllegalMethodsCheck.checkMethods(
            "^java/util/Arrays.+"
        );
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H5_HybridOptimizerTests.json", data = "sortCallTest")
    public void testSortCall(@Property("values") List<Integer> values,
                             @Property("reads") List<Integer> reads,
                             @Property("writes") List<Integer> writes,
                             @Property("expectedCalls") int expectedCalls) {

        Context context = contextBuilder()
            .subject("HybridOptimizer.optimize")
            .add("values", values)
            .build();

        AtomicInteger calls = new AtomicInteger(0);

        doAnswer(invocation -> {
            SortList<Integer> sortList = invocation.getArgument(0);

            assertEquals(calls.get(), hybridSort.getK(), context,
                result -> "The k-value of the hybridSort object is wrong at the %dth call.".formatted(calls.get() + 1));

            for (int i = 0; i < sortList.getSize(); i++) {
                int finalI = i;
                assertEquals(values.get(i), sortList.get(i), context,
                    result -> ("The sortList contains the wrong value at index %d. " +
                        "Note that the sortList might have been modified by an previous call to sort()").formatted(finalI));

                sortList.set(i, -1); // fake sorting to check if a new sortList ist created each time sort() is called
            }

            calls.incrementAndGet();

            return null;
        }).when(hybridSort).sort(any());

        //noinspection rawtypes
        try (MockedConstruction<ArraySortList> ignored = mockConstruction(ArraySortList.class, (mock, creationContext) -> {

            @SuppressWarnings("unchecked")
            SortList<Integer> sortList = (SortList<Integer>) mock;

            when(sortList.getReadCount()).thenAnswer(invocation -> calls.get() == 0 ? 0 : reads.get(calls.get() - 1));
            when(sortList.getWriteCount()).thenAnswer(invocation -> calls.get() == 0 ? 0 : writes.get(calls.get() - 1));

        })) {
            HybridOptimizer.optimize(hybridSort, values.toArray(Integer[]::new));
        }

        assertEquals(expectedCalls, calls.get(), context,
            result -> "the amount of calls to the sort method is not correct.");
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H5_HybridOptimizerTests.json", data = "nonMonotoneTest")
    public void testNonMonotone(@Property("values") List<Integer> values,
                                @Property("reads") List<Integer> reads,
                                @Property("writes") List<Integer> writes,
                                @Property("expected") int expected) {

        testOptimize(values, reads, writes, expected);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H5_HybridOptimizerTests.json", data = "strictlyMonotoneTest")
    public void testStrictlyMonotone(@Property("values") List<Integer> values,
                                     @Property("reads") List<Integer> reads,
                                     @Property("writes") List<Integer> writes,
                                     @Property("expected") int expected) {

        testOptimize(values, reads, writes, expected);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H5_HybridOptimizerTests.json", data = "monotoneTest")
    public void testMonotone(@Property("values") List<Integer> values,
                             @Property("reads") List<Integer> reads,
                             @Property("writes") List<Integer> writes,
                             @Property("expected") int expected) {

        testOptimize(values, reads, writes, expected);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H5_HybridOptimizerTests.json", data = "globalNotLocalTest")
    public void testGlobalNotLocal(@Property("values") List<Integer> values,
                                   @Property("reads") List<Integer> reads,
                                   @Property("writes") List<Integer> writes,
                                   @Property("expected") int expected) {

        testOptimize(values, reads, writes, expected);
    }

    private void testOptimize(List<Integer> values,
                              List<Integer> reads,
                              List<Integer> writes,
                              int expected) {

        Context context = contextBuilder()
            .subject("HybridOptimizer.optimize")
            .add("values", values)
            .add("reads", reads)
            .add("writes", writes)
            .build();

        AtomicInteger calls = new AtomicInteger(0);

        //noinspection rawtypes
        try (MockedConstruction<ArraySortList> ignored = mockConstruction(ArraySortList.class, (mock, creationContext) -> {

            @SuppressWarnings("unchecked")
            SortList<Integer> sortList = (SortList<Integer>) mock;

            when(sortList.getReadCount()).thenAnswer(invocation -> calls.get() == 0 ? 0 : reads.get(calls.get() - 1));
            when(sortList.getWriteCount()).thenAnswer(invocation -> calls.get() == 0 ? 0 : writes.get(calls.get() - 1));

        })) {

            doAnswer(invocation -> {
                calls.incrementAndGet();
                return null;
            }).when(hybridSort).sort(any());

            assertCallEquals(expected, () -> HybridOptimizer.optimize(hybridSort, values.toArray(Integer[]::new)), context,
                result -> "The return value of the optimize() method is wrong.");
        }
    }

}
