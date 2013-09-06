package com.example.algorithm.lec5;

import com.example.algorithm.Debug;
import com.example.algorithm.Utility;
import junit.framework.TestCase;

import java.util.Arrays;

public class CountingSortTest extends TestCase {

    public void testBoundary() throws Exception {
        CountingSort.sort(null);

        int[] array1 = {};
        int[] array2 = {};
        CountingSort.sort(array1);
        assertTrue(Arrays.equals(array1, array2));

        int[] array3 = {0};
        int[] array4 = {0};
        CountingSort.sort(array3);
        assertTrue(Arrays.equals(array3, array4));
    }

    public void testInternalMax() throws Exception {
        try {
            CountingSort.internalMax(null);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            Debug.logTest("Counting sort null array!");
        }
        try {
            CountingSort.internalMax(new int[] {});
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            Debug.logTest("Counting sort empty array!");
        }
        assertEquals(CountingSort.internalMax(new int[] {20}), 20);
        assertEquals(CountingSort.internalMax(new int[] {30, 20}), 30);
        assertEquals(CountingSort.internalMax(new int[] {20, 30}), 30);
        int[] array1 = {6, 10, 13, 5, 8, 3, 2, 11};
        int[] array2 = {19, 10, 6, 5, 8, 3, 2, 11};
        int[] array3 = {6, 10, 13, 5, 8, 3, 2, 15};
        assertEquals(CountingSort.internalMax(array1), 13);
        assertEquals(CountingSort.internalMax(array2), 19);
        assertEquals(CountingSort.internalMax(array3), 15);
    }

    public void testNormalSort() throws Exception {
        int[] array1 = {4,1,3,4,3};
        int[] array2 = {4,1,3,4,3};
        Arrays.sort(array2);
        assertTrue(Arrays.equals(CountingSort.sort(array1), array2));

        int[] array3 = {8, 2, 4, 2, 3, 6};
        int[] array4 = {8, 2, 4, 2, 3, 6};
        CountingSort.sort(array3);
        Arrays.sort(array4);
        assertTrue(Arrays.equals(CountingSort.sort(array3), array4));

        int[] array5 = {8, 8, 4, 2, 3, 6};
        int[] array6 = {8, 8, 4, 2, 3, 6};
        Arrays.sort(array6);
        assertTrue(Arrays.equals(CountingSort.sort(array5), array6));
    }

    public void testRandomTest() throws Exception {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 500000; i++) {
            int[] array = Utility.randomIntegerArray();
            int[] array1 = Arrays.copyOf(array, array.length);
            assertTrue(Arrays.equals(array, array1));

            // CountingSort.sort(array);
            Arrays.sort(array1);
            assertTrue(Arrays.equals(CountingSort.sort(array), array1));
        }
        Debug.logTest("" + (System.currentTimeMillis() - begin));
    }

    public void testPerformance() throws Exception {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            int[] array = Utility.randomIntegerArray();
            CountingSort.sort(array);
        }
        Debug.logTest("" + (System.currentTimeMillis() - begin));
    }
}
