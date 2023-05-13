package p1.sort;

/**
 * Optimizes the {@link HybridSort} by trying to find the k-value with the lowest number of read and write operations..
 */
public class HybridOptimizer {

    /**
     * Optimizes the {@link HybridSort} by trying to find the k-value with the lowest number of read and write operations.
     * The method will try all k-values starting from and return the k-value with the lowest number of read and write operations.
     * It will stop once if found the first local minimum or reaches the maximum possible k-value for the size of the given array.
     *
     * @param hybridSort the {@link HybridSort} to optimize.
     * @param array the array to sort.
     * @return the k-value with the lowest number of read and write operations.
     * @param <T> the type of the elements to be sorted.
     */
    public static <T> int optimize(HybridSort<T> hybridSort, T[] array) {
        int readAndWrites = Integer.MAX_VALUE;

        for (int k = 0; k <= array.length + 1; k++) {
            SortList<T> sortList = new ArraySortList<>(array);

            hybridSort.setK(k);
            hybridSort.sort(sortList);

            int newReadAndWrites = sortList.getReadCount() + sortList.getWriteCount();

            System.out.println("k: " + k +
                ", readCount: " + sortList.getReadCount() +
                ", writeCount: " + sortList.getWriteCount() +
                ", readAndWrites: " + newReadAndWrites +
                ", comparisons: " + hybridSort.getComparisonsCount());

            if (newReadAndWrites <= readAndWrites) {
                readAndWrites = newReadAndWrites;
            } else {
               return k - 1;
            }
        }

        return array.length + 1;
    }

}
