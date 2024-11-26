package graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import datastructures.PQueue;
import org.junit.jupiter.api.Test;

public class SlowPQueueTest {
    @Test void reversed() {
        PQueue<Integer> q = new PQueue<>() {
            @Override
            public String toString() {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void add(Integer integer, double priority) throws IllegalArgumentException {

            }

            @Override
            public Integer peek() {
                return null;
            }

            @Override
            public Integer extractMin() {
                return null;
            }

            @Override
            public double getPriority(Integer element) {
                return 0;
            }

            @Override
            public void changePriority(Integer integer, double p) {

            }
        };
        assertTrue(q.isEmpty());
        assertEquals(0, q.size());
        for (int i = 10; i >= 0; i--) q.add(i, i);
        assertEquals(11, q.size());
        for (int i = 0; i <= 10; i++) {
            int k = q.peek();
            int j = q.extractMin();
            assertEquals(i, j, k);
        }
        assertTrue(q.isEmpty());
    }
    @Test void inorder() {
        PQueue<Integer> q = new PQueue<>() {
            @Override
            public String toString() {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void add(Integer integer, double priority) throws IllegalArgumentException {

            }

            @Override
            public Integer peek() {
                return null;
            }

            @Override
            public Integer extractMin() {
                return null;
            }

            @Override
            public double getPriority(Integer element) {
                return 0;
            }

            @Override
            public void changePriority(Integer integer, double p) {

            }
        };
        assertTrue(q.isEmpty());
        for (int i = 0; i < 10; i++) q.add(i, i);
        assertEquals(10, q.size());
        for (int i = 0; i < 10; i++) {
            int k = q.peek();
            int j = q.extractMin();
            assertEquals(i, j, k);
        }
        assertTrue(q.isEmpty());
    }
    @Test void throwTest() {
        PQueue<Integer> q = new PQueue<>() {
            @Override
            public String toString() {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void add(Integer integer, double priority) throws IllegalArgumentException {

            }

            @Override
            public Integer peek() {
                return null;
            }

            @Override
            public Integer extractMin() {
                return null;
            }

            @Override
            public double getPriority(Integer element) {
                return 0;
            }

            @Override
            public void changePriority(Integer integer, double p) {

            }
        };
        q.add(1,1);
        assertThrows(IllegalArgumentException.class, () -> q.add(1,2));
    }
}
