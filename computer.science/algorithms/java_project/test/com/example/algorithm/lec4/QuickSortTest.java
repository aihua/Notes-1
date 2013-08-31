package com.example.algorithm.lec4;

import com.example.algorithm.Debug;
import com.example.algorithm.Utility;
import junit.framework.TestCase;

import java.util.Arrays;

public class QuickSortTest extends TestCase {
    public void testBoundary() throws Exception {
        QuickSort.sort(null);

        double[] array1 = {};
        double[] array2 = {};
        QuickSort.sort(array1);
        assertTrue(Arrays.equals(array1, array2));

        double[] array3 = {0};
        double[] array4 = {0};
        QuickSort.sort(array3);
        assertTrue(Arrays.equals(array3, array4));
    }

    public void testNormalSort() throws Exception {
        double[] array1 = {6, 10, 13, 5, 8, 3, 2, 11};
        double[] array2 = {6, 10, 13, 5, 8, 3, 2, 11};
        QuickSort.sort(array1);
        Arrays.sort(array2);
        assertTrue(Arrays.equals(array1, array2));

        double[] array3 = {8, 2, 4, 2, 3, 6};
        double[] array4 = {8, 2, 4, 2, 3, 6};
        QuickSort.sort(array3);
        Arrays.sort(array4);
        assertTrue(Arrays.equals(array3, array4));

        double[] array5 = {8, 8, 4, 2, 3, 6};
        double[] array6 = {8, 8, 4, 2, 3, 6};
        QuickSort.sort(array5);
        Arrays.sort(array6);
        assertTrue(Arrays.equals(array5, array6));
    }

    public void testRandomTest() throws Exception {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 500000; i++) {
            double[] array = Utility.randomDoubleArray();
            double[] array1 = Arrays.copyOf(array, array.length);
            assertTrue(Arrays.equals(array, array1));

            QuickSort.sort(array);
            Arrays.sort(array1);

            assertTrue(Arrays.equals(array, array1));
        }
        Debug.logTest("" + (System.currentTimeMillis() - begin));
    }

    public void testPerformance() throws Exception {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            double[] array = Utility.randomDoubleArray();
            QuickSort.sort(array);
        }
        Debug.logTest("" + (System.currentTimeMillis() - begin));
    }

    public void testStdPerformance() throws Exception {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            double[] array = Utility.randomDoubleArray();
            Arrays.sort(array);
        }
        Debug.logTest("" + (System.currentTimeMillis() - begin));
    }
}
