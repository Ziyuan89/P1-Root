package p1;

import p1.sort.ArraySortList;
import p1.sort.HybridOptimizer;
import p1.sort.HybridSort;
import p1.sort.StoneSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main entry point in executing the program.
 */
public class Main {

    /**
     * Main entry point in executing the program.
     *
     * @param args program arguments, currently ignored
     */
    public static void main(String[] args) {
        int size = 100;

        HybridSort<Integer> hybridSort = new HybridSort<>(1, Integer::compareTo);
        StoneSort<Integer> stoneSort = new StoneSort<>(Integer::compareTo);

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        Collections.shuffle(list);

        System.out.println("first local minimum: " +
            HybridOptimizer.optimize(hybridSort, list.toArray(new Integer[0])));

        hybridSort.sort(new ArraySortList<>(list));

        System.out.println("hybridSort comparisons: " + hybridSort.getComparisonsCount());

        stoneSort.sort(new ArraySortList<>(list));

        System.out.println("stoneSort comparisons: " + stoneSort.getComparisonsCount());
    }
}
