import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node head;
    private Node last;

    public Deque() {                      // construct an empty deque
        size = 0;
    }

    public boolean isEmpty() {                 // is the deque empty?
        return this.size == 0;
    }

    public int size() {                        // return the number of items on the deque
        return size;
    }

    public void addFirst(Item item) { // add the item to the front
        if (item == null) throw new java.lang.NullPointerException("Adding first null");
        Node node = new Node(head, null, item);
        if (isEmpty()) last = node;
        else head.previous = node;
        head = node;
        size++;
    }

    public void addLast(Item item) { // add the item to the end
        if (item == null) throw new java.lang.NullPointerException("Adding last null");
        Node node = new Node(null, last, item);
        if (isEmpty()) head = node; // if deque is empty
        else last.next = node;
        last = node;
        size++;
    }

    public Item removeFirst() { // remove and return the item from the front
        if (isEmpty()) throw new java.util.NoSuchElementException("removing first from empty deck");
        Item item = head.item;
        if (size == 1) {
            head = null;
            last = null;
        } else {
            head = head.next;
            head.previous = null;
        }
        size--;
        return item;
    }

    public Item removeLast() { // remove and return the item from the end
        if (isEmpty()) throw new java.util.NoSuchElementException("removing last from empty deck");
        Item item = last.item;
        if (size == 1) {
            head = null;
            last = null;
        } else {
            last = last.previous;
            last.next = null;
        }
        size--;
        return item;
    }

    public Iterator<Item> iterator() {    // return an iterator over items in order from front to end
        return new Iterator<Item>() {
            private Node current = head;

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                if (!this.hasNext())
                    throw new java.util.NoSuchElementException("Iterator hasn't the next element");
                Item item = current.item;
                current = current.next;
                return item;
            }

            public void remove() {
                throw new java.lang.UnsupportedOperationException("Iterator doesn't remove items");
            }
        };
    }

    public static void main(String[] args) {   // unit testing (optional)
        Deque<Integer> deque = new Deque<Integer>();
        for (int i = 0; i < 10; i++)
            deque.addFirst(i);
        for (int i = 0; i < 5; i++)
            deque.removeFirst();

        Iterator<Integer> it = deque.iterator();
        while (it.hasNext()) {
            StdOut.print(it.next());
        }

    }

    private class Node {
        private Node next;
        private Node previous;
        private Item item;

        Node(Node next, Node previous, Item item) {
            this.next = next;
            this.previous = previous;
            this.item = item;
        }
    }

}
