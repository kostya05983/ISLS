package SQL.Lib.AdditionalInstruments;

import java.io.Serializable;

public class BTree<Key extends Comparable<Key>,Value> implements Serializable {
    //Колличество веток в узле,должно быть больше двух
    private static final int M=4;
    private Node root;
    private int height;
    private int amountPairs;

    private final static class Node{
        private int amountChildren;//колличество веток
        private Entry[] children=new Entry[M];

        private Node(int amountChildren){
            this.amountChildren=amountChildren;
        }
    }

    private static class Entry{
        private Comparable key;
        private final Object value;
        private Node next;
        Entry(Comparable key, Object value, Node next){
            this.key=key;
            this.value=value;
            this.next=next;
        }
    }

    public BTree(){
        root=new Node(0);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return amountPairs;
    }

    public int height() {
        return height;
    }

    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        return search(root, key, height);
    }

    private Value search(Node x, Key key, int ht) {
        Entry[] children = x.children;

        // external node
        if (ht == 0) {
            for (int j = 0; j < x.amountChildren; j++) {
                if (eq(key, children[j].key)) return (Value) children[j].value;
            }
        }

        // internal node
        else {
            for (int j = 0; j < x.amountChildren; j++) {
                if (j+1 == x.amountChildren || less(key, children[j+1].key))
                    return search(children[j].next, key, ht-1);
            }
        }
        return null;
    }

    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("argument key to put() is null");
        Node u = insert(root, key, val, height);
        amountPairs++;
        if (u == null) return;

        // need to split root
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        height++;
    }

    private Node insert(Node h, Key key, Value val, int ht) {
        int j;
        Entry t = new Entry(key, val, null);

        // external node
        if (ht == 0) {
            for (j = 0; j < h.amountChildren; j++) {
                if (less(key, h.children[j].key)) break;
            }
        }

        // internal node
        else {
            for (j = 0; j < h.amountChildren; j++) {
                if ((j+1 == h.amountChildren) || less(key, h.children[j+1].key)) {
                    Node u = insert(h.children[j++].next, key, val, ht-1);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }

        System.arraycopy(h.children, j, h.children, j + 1, h.amountChildren - j);
        h.children[j] = t;
        h.amountChildren++;
        if (h.amountChildren < M) return null;
        else         return split(h);
    }

    private Node split(Node h) {
        Node t = new Node(M/2);
        h.amountChildren = M/2;
        System.arraycopy(h.children, 2, t.children, 0, M / 2);
        return t;
    }

    public String toString() {
        return toString(root, height, "") + "\n";
    }

    private String toString(Node h, int ht, String indent) {
        StringBuilder s = new StringBuilder();
        Entry[] children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.amountChildren; j++) {
                s.append(indent).append(children[j].key).append(" ").append(children[j].value).append("\n");
            }
        }
        else {
            for (int j = 0; j < h.amountChildren; j++) {
                if (j > 0) s.append(indent).append("(").append(children[j].key).append(")\n");
                s.append(toString(children[j].next, ht-1, indent + "     "));
            }
        }
        return s.toString();
    }

    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }
}
