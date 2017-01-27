import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private Item[] elements;

    public RandomizedQueue() { // construct an empty randomized queue
        size = 0;
        elements = (Item[]) new Object[4];
    }

    public boolean isEmpty() {                // is the queue empty?
        return size == 0;
    }

    public int size() {               // return the number of items on the queue
        return this.size;
    }

    public void enqueue(Item item) { // add the item
        if (item == null) throw new NullPointerException("Adding null element");
        if (size == elements.length)
            elements = Arrays.copyOf(elements, elements.length * 2);
        elements[size++] = item;
    }

    public Item dequeue() { // remove and return a random item
        if (isEmpty()) throw new NoSuchElementException("Empty queue");
        if (size - 1 < elements.length / 4)
            elements = Arrays.copyOf(elements, elements.length / 2);
        int index = StdRandom.uniform(size);
        Item item = elements[index];
        elements[index] = elements[--size];
        elements[size] = null;
        return item;
    }

    public Item sample() { // return (but do not remove) a random item
        if (isEmpty()) throw new NoSuchElementException("Empty queue");
        return elements[StdRandom.uniform(size)];
    }

    public Iterator<Item> iterator() { // return an independent iterator over items in random order
        return new QueueIterator();
    }

    public static void main(String[] args) {   // unit testing (optional)
    }

    private class QueueIterator implements Iterator<Item> {
        private int[] order;
        private int index = 0;

        private QueueIterator() {
            order = new int[size];
            for (int i = 0; i < order.length; i++)
                order[i] = i;

            for (int i = order.length - 1; i > 0; i--) {
                int x = StdRandom.uniform(i + 1);
                // Simple swap
                int a = order[x];
                order[x] = order[i];
                order[i] = a;
            }
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException("Iterator doesn't remove items");
        }

        public boolean hasNext() {
            return index < order.length;
        }

        public Item next() {
            if (!this.hasNext())
                throw new java.util.NoSuchElementException("Iterator hasn't the next element");
            return elements[order[index++]];
        }
    }
}