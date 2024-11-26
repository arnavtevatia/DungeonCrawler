package datastructures;

import java.util.*;

/**
 * An instance is a priority queue using a binary heap for efficient extraction of minimum values.
 */
public class HeapPQueue<E> implements PQueue<E> {
    private final List<E> elements;
    private final Map<E, Double> priorities;

    public HeapPQueue() {
        elements = new ArrayList<>();
        priorities = new HashMap<>();
    }

    @Override
    public void add(E element, double priority) {
        if (priorities.containsKey(element)) {
            throw new IllegalArgumentException("Element already exists in the priority queue.");
        }
        elements.add(element);
        priorities.put(element, priority);
        bubbleUp(elements.size() - 1);
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public E extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty.");
        }
        E min = elements.get(0);
        Collections.swap(elements, 0, elements.size() - 1);
        elements.remove(elements.size() - 1);
        priorities.remove(min);
        if (!isEmpty()) {
            bubbleDown(0);
        }
        return min;
    }

    @Override
    public double getPriority(E element) {
        if (!priorities.containsKey(element)) {
            throw new NoSuchElementException("Element not found in the priority queue.");
        }
        return priorities.get(element);
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!priorities.containsKey(element)) {
            throw new NoSuchElementException("Element not found in the priority queue.");
        }
        priorities.put(element, priority);
        int index = elements.indexOf(element);
        bubbleUp(index);
        bubbleDown(index);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (priorities.get(elements.get(index)) >= priorities.get(elements.get(parent))) {
                break;
            }
            Collections.swap(elements, index, parent);
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        int size = elements.size();
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && priorities.get(elements.get(left)) < priorities.get(elements.get(smallest))) {
                smallest = left;
            }
            if (right < size && priorities.get(elements.get(right)) < priorities.get(elements.get(smallest))) {
                smallest = right;
            }
            if (smallest == index) {
                break;
            }
            Collections.swap(elements, index, smallest);
            index = smallest;
        }
    }
}
