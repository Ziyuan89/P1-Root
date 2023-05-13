package p1;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.Grader;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;
import p1.card.CardColor;
import p1.transformers.MethodInterceptorTransformer;
import p1.transformers.SuperPartitionTransformer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("unused")
public class P1_RubricProvider_Private implements RubricProvider {
    //TODO add changes to the criterions from public tests
    @SafeVarargs
    private static Criterion createCriterion(String shortDescription, int maxPoints, Callable<Method>... methodReferences) {

        if (methodReferences.length == 0) {
            return Criterion.builder()
                .shortDescription(shortDescription)
                .maxPoints(maxPoints)
                .build();
        }

        Grader.TestAwareBuilder graderBuilder = Grader.testAwareBuilder();

        for (Callable<Method> reference : methodReferences) {
            graderBuilder.requirePass(JUnitTestRef.ofMethod(reference));
        }

        return Criterion.builder()
            .shortDescription(shortDescription)
            .grader(graderBuilder
                .pointsFailedMin()
                .pointsPassedMax()
                .build())
            .maxPoints(maxPoints)
            .build();
    }

    private static Criterion createParentCriterion(String task, String shortDescription, Criterion... children) {
        return Criterion.builder()
            .shortDescription("H" + task + " | " + shortDescription)
            .addChildCriteria(children)
            .build();
    }

    public static final Criterion H1_1_1 = createCriterion("Die Methode [[[compare]]] der Klasse CardComparator funktioniert korrekt wenn die Farben der verglichenen Karten unterschiedlich sind", 1,
        () -> CardComparatorTests.class.getMethod("testColor", List.class, List.class, List.class));

    public static final Criterion H1_1_2 = createCriterion("Die Methode [[[compare]]] der Klasse CardComparator funktioniert korrekt wenn die Farben der verglichenen Karten gleich sind", 1,
        () -> CardComparatorTests.class.getMethod("testValue", CardColor.class, List.class, List.class));

    public static final Criterion H1_1 = createParentCriterion("1 a)", "CardComparator", H1_1_1, H1_1_2);

    public static final Criterion H1_2_1 = createCriterion("Die Methode [[[compare]]] der Klasse CountingComparator funktioniert korrekt", 1,
        () -> CountingComparatorTest.class.getMethod("testCompare", Integer.class, Integer.class));

    public static final Criterion H1_2_2 = createCriterion("Die Methoden [[[reset]]] und [[[getComparisonsCount]]] der Klasse CountingComparator funktionieren korrekt", 1,
        () -> CountingComparatorTest.class.getMethod("testReset", Integer.class),
        () -> CountingComparatorTest.class.getMethod("testGetComparisonsCount", Integer.class));

    public static final Criterion H1_2 = createParentCriterion("1 b)", "CountingComparator", H1_2_1, H1_2_2);

    public static final Criterion H1_3_1 = createCriterion("Die Methode [[[insertionSort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe bereits sortiert ist", 1,
        () -> InsertionSortTests.class.getMethod("testAlreadySorted", List.class));

    public static final Criterion H1_3_2 = createCriterion("Die Methode [[[insertionSort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe nur ein Element enthält", 1,
        () -> InsertionSortTests.class.getMethod("testOneItem", Integer.class));

    public static final Criterion H1_3_3 = createCriterion("Die Methode [[[insertionSort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe zwei Elemente enthält", 1,
        () -> InsertionSortTests.class.getMethod("testTwoItems", List.class));

    public static final Criterion H1_3_4 = createCriterion("Die Methode [[[insertionSort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe mehr als zwei Elemente enthält", 1,
        () -> InsertionSortTests.class.getMethod("testMultipleItems", List.class));

    public static final Criterion H1_3_5 = createCriterion("Die Methode [[[insertionSort]]] der Klasse HybridSort verändert nur Werte im Indexbereich [left, right]", 1,
        () -> InsertionSortTests.class.getMethod("testBounds", List.class, Integer.class, Integer.class, List.class));

    public static final Criterion H1_3_6 = createCriterion("Die Methode [[[insertionSort]]] der Klasse HybridSort verwendet die korrekten Lese- und Schreiboperationen in der korrekten Reihenfolge", 1,
        () -> InsertionSortTests.class.getMethod("testOperationOrder", List.class, Integer.class, Integer.class, List.class));

    public static final Criterion H1_3 = createParentCriterion("1 c)", "InsertionSort", H1_3_1, H1_3_2, H1_3_3, H1_3_4, H1_3_5, H1_3_6);

    public static final Criterion H1_4_1 = createCriterion("Die Methode [[[quickSort]]] der Klasse HybridSort ruft, wenn notwendig, die Methode insertionSort mit den korrekten Werten auf", 1,
        () -> QuickSortTests.class.getMethod("testInsertionCall", List.class, Integer.class, Integer.class, Integer.class, Boolean.class));

    public static final Criterion H1_4_2 = createCriterion("Die Methode [[[quickSort]]] der Klasse HybridSort ruft, wenn notwendig, die Methode [[[partition]]] und sich selber mit den korrekten Werten auf", 1,
        () -> QuickSortTests.class.getMethod("testQuickSortRecursion", List.class, Integer.class, Integer.class, Integer.class, Boolean.class, Integer.class));

    public static final Criterion H1_4_3 = createCriterion("Die Methode [[[partition]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe bereits partitioniert ist", 1,
        () -> QuickSortTests.class.getMethod("testAlreadyPartitioned", List.class, Integer.class, Integer.class));

    public static final Criterion H1_4_4 = createCriterion("Die Methode [[[partition]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe nicht partitioniert ist und zwei Elemente enthält", 1,
        () -> QuickSortTests.class.getMethod("testPartitionTwoItems", List.class, Integer.class, Integer.class, List.class));

    public static final Criterion H1_4_5 = createCriterion("Die Methode [[[partition]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe nicht partitioniert ist und drei Elemente enthält", 1,
        () -> QuickSortTests.class.getMethod("testPartitionThreeItems", List.class, Integer.class, Integer.class, Integer.class, List.class));

    public static final Criterion H1_4_6 = createCriterion("Die Methode [[[partition]]] der Klasse HybridSort funktioniert vollständig korrekt", 1,
        () -> QuickSortTests.class.getMethod("testAlreadyPartitioned", List.class, Integer.class, Integer.class),
        () -> QuickSortTests.class.getMethod("testPartitionTwoItems", List.class, Integer.class, Integer.class, List.class),
        () -> QuickSortTests.class.getMethod("testPartitionThreeItems", List.class, Integer.class, Integer.class, Integer.class, List.class),
        () -> QuickSortTests.class.getMethod("testPartitionMultipleItems", List.class, Integer.class, Integer.class, Integer.class, List.class));

    public static final Criterion H1_4 = createParentCriterion("1 d)", "Quicksort", H1_4_1, H1_4_2, H1_4_3, H1_4_4, H1_4_5, H1_4_6);

    public static final Criterion H1_5_1 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer ruft die [[[sort]]] Methode mit korrekten Werten in der richtigen Reihenfolge auf", 1,
        () -> HybridOptimizerTests.class.getMethod("testSortCall", List.class, List.class, List.class, int.class));

    public static final Criterion H1_5_2 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte nicht monoton sind und es nur ein Minimum gibt", 1,
        () -> HybridOptimizerTests.class.getMethod("testNonMonotone", List.class, List.class, List.class, int.class));

    public static final Criterion H1_5_3 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte streng monoton fallend sind", 1,
        () -> HybridOptimizerTests.class.getMethod("testStrictlyMonotone", List.class, List.class, List.class, int.class));

    public static final Criterion H1_5_4 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte monoton fallend sind", 1,
        () -> HybridOptimizerTests.class.getMethod("testMonotone", List.class, List.class, List.class, int.class));

    public static final Criterion H1_5_5 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte nicht monoton sind und es mehrere Minima gibt", 1,
        () -> HybridOptimizerTests.class.getMethod("testGlobalNotLocal", List.class, List.class, List.class, int.class));

    public static final Criterion H1_5 = createParentCriterion("1 e)", "HybridOptimizer", H1_5_1, H1_5_2, H1_5_3, H1_5_4, H1_5_5);

    public static final Criterion H1_6_1 = createCriterion("Die Methode [[[partition]]] der Klasse HybridSortRandomPivot vertauscht korrekt ein Element mit dem Element an der linken Grenze wenn die Grenzen der gesamten Liste entsprechen", 1,
        () -> HybridSortRandomPivotTests.class.getMethod("testSwapWithoutBounds", List.class));

    public static final Criterion H1_6_2 = createCriterion("Die Methode [[[partition]]] der Klasse HybridSortRandomPivot vertauscht korrekt ein Element mit dem Element an der linken Grenze wenn die Grenzen nicht der gesamten Liste entsprechen", 1,
        () -> HybridSortRandomPivotTests.class.getMethod("testSwapWithBounds", List.class, int.class, int.class));

    public static final Criterion H1_6_3 = createCriterion("Die Methode [[[partition]]] der Klasse HybridSortRandomPivot ruft die Methode partition Methode der Superklasse mit den korrekten Werten auf und gibt deren Rückgabe zurück", 1,
        () -> HybridSortRandomPivotTests.class.getMethod("testSuperCall", List.class, int.class, int.class, int.class));

    public static final Criterion H1_6 = createParentCriterion("1 f)", "HybridSortRandomPivot", H1_6_1, H1_6_2, H1_6_3);

    public static final Criterion H1 = createParentCriterion("1", "Sortieralgorithmen", H1_1, H1_2, H1_3, H1_4, H1_5, H1_6);

    public static final Criterion H2_1_1 = createCriterion("Die Methode [[[sort]]] der Klasse StoneSort funktioniert korrekt wenn die Eingabe bereits sortiert ist", 1,
        () -> StoneSortTests.class.getMethod("testAlreadySorted", List.class));

    public static final Criterion H2_1_2 = createCriterion("Die Methode [[[sort]]] der Klasse StoneSort funktioniert korrekt wenn die Eingabe leer ist oder nur ein Element enthält", 1,
        () -> StoneSortTests.class.getMethod("testEmpty"),
        () -> StoneSortTests.class.getMethod("testOneItem", List.class));

    public static final Criterion H2_1_3 = createCriterion("Die Methode [[[sort]]] der Klasse StoneSort funktioniert korrekt wenn die Eingabe zwei Elemente enthält", 1,
        () -> StoneSortTests.class.getMethod("testTwoItems", List.class));

    public static final Criterion H2_1_4 = createCriterion("Die Methode [[[sort]]] der Klasse StoneSort funktioniert korrekt wenn die Eingabe mehrere Elemente enthält", 1,
        () -> StoneSortTests.class.getMethod("testMultipleItems", List.class));

    public static final Criterion H2_1_5 = createCriterion("Die Methode [[[sort]]] der Klasse StoneSort verwendet die korrekten Lese- und Schreiboperationen in der korrekten Reihenfolge", 2,
        () -> StoneSortTests.class.getMethod("testOperationOrder", List.class, List.class));

    public static final Criterion H2_1 = createParentCriterion("2 a)", "StoneSort", H2_1_1, H2_1_2, H2_1_3, H2_1_4, H2_1_5);

    public static final Criterion H2 = createParentCriterion("2", "Weniger effiziente Sortieralgorithmen", H2_1);

    public static final Rubric RUBRIC = Rubric.builder()
        .title("P1")
        .addChildCriteria(H1, H2)
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    @Override
    public void configure(RubricConfiguration configuration) {
        configuration.addTransformer(new SuperPartitionTransformer());
        configuration.addTransformer(new MethodInterceptorTransformer());
    }
}
