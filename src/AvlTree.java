import java.util.*;

public class AvlTree<K extends Comparable<K>> extends AbstractSet<K> {

    private class AvlIterator implements Iterator<K> {

        Stack<Node<K>> stack;

        AvlIterator() {
            Node<K> curr = root;
            stack = new Stack<>();
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }

        public K next() {
            if (stack.size() == 0) {
                throw new NoSuchElementException();
            }

            Node<K> node = stack.pop();
            K result = node.key;

            if (node.right != null) {
                node = node.right;
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
            }

            return result;
        }
    }

    private static class Node<K> {
        private final K key;
        private int height;
        private Node<K> left, right;

        public Node(K key) {
            this.key = key;
            left = right = null;
            height = 1;
        }
    }

    private Node<K> root;
    private int size;

    public AvlTree() {
        root = null;
        size = 0;
    }

    /* PUBLIC METHODS */

    @Override
    public Iterator<K> iterator() {
        return new AvlIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof AvlTree)) {
            return false;
        }
        AvlTree newTree = (AvlTree) o;
        if (newTree.size() != size()) {
            return false;
        }
        Iterator iter = newTree.iterator();
        while (iter.hasNext()) {
            if (!contains(iter.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        K key = (K) o;
        Node<K> node = root;
        while (node != null) {
            if (key.compareTo(node.key) < 0) {
                node = node.left;
            } else if (key.compareTo(node.key) > 0) {
                node = node.right;
            } else return true;
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Iterator<K> iter = this.iterator();
        int idx = 0;
        while (iter.hasNext()) {
            array[idx] = iter.next();
            idx++;
        }

        return array;
    }

    @Override
    public boolean add(K k) {
        if (k == null || contains(k)) {
            return false;
        }
        root = add(root, k);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || !contains(o)) {
            return false;
        }
        root = erase(root, (K) o);
        size--;
        return true;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        toString(root, str);
        return str.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toArray());
    }

    /* PACKAGE PRIVATE METHODS */

    boolean checkSize() {
        return size(root) == size;
    }

    boolean checkBalance() {
        return checkBalance(root);
    }

    K getRootValue() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return root.key;
    }

    K getMinValue() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return findMin(root).key;
    }

    K getMaxValue() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return findMax(root).key;
    }

    /* PRIVATE METHODS */

    private int height(Node<K> node) {
        return (node == null ? 0 : node.height);
    }

    private void fixHeight(Node<K> node) {
        int hr = height(node.right);
        int hl = height(node.left);
        node.height = (hr > hl ? hr : hl) + 1;
    }

    /*
    *  getBalance() возвращает локальный баланс для каждого узла.
    *  Контроль баланса с исключением - в методе balance() - проходит.
    *  Высота каждого узла назначается при инициализации (единицей)
    *  Поэтому разность никогда не будет больше 2х по модулю
    */

    private int getBalance(Node<K> node) {
        return (height(node.right) - height(node.left));
    }

    private Node<K> rotateRight(Node<K> node) {
        Node<K> lchild = node.left;
        node.left = lchild.right;
        lchild.right = node;
        fixHeight(node);
        fixHeight(lchild);
        return lchild;
    }

    private Node<K> rotateLeft(Node<K> node) {
        Node<K> rchild = node.right;
        node.right = rchild.left;
        rchild.left = node;
        fixHeight(node);
        fixHeight(rchild);
        return rchild;
    }

    private Node<K> balance(Node<K> node) {
        fixHeight(node);

        int balance = getBalance(node);
        if (Math.abs(balance) > 2) {
            throw new AssertionError("Incorrect balance: " + balance);
        }

        if (balance == 2) {
            if (getBalance(node.right) < 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        if (balance == -2) {
            if (getBalance(node.left) > 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        return node;
    }

    private Node<K> add(Node<K> node, K k) {
        if (node == null) {
            return new Node<>(k);
        }
        if (k.compareTo(node.key) < 0) {
            node.left = add(node.left, k);
        } else if (k.compareTo(node.key) > 0) {
            node.right = add(node.right, k);
        }// else return node;
        return balance(node);
    }

    private Node<K> findMin(Node<K> node) {
        return (node.left != null ? findMin(node.left) : node);
    }

    private Node<K> findMax(Node<K> node) {
        return (node.right != null ? findMax(node.right) : node);
    }

    private Node<K> cutMin(Node<K> node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = cutMin(node.left);
        return balance(node);
    }

    private Node<K> erase(Node<K> node, K key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) < 0) {
            node.left = erase(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            node.right = erase(node.right, key);
        } else {
            Node<K> l = node.left, r = node.right;
            if (r == null) {
                return l;
            }
            Node<K> min = findMin(r);
            min.right = cutMin(r);
            min.left = l;
            return balance(min);
        }
        return balance(node);
    }

    private void toString(Node<K> node, StringBuilder str) {
        if (node != null) {
            toString(node.left, str);
            str.append(node.key).append(" ");
            toString(node.right, str);
        }
    }

    private int size(Node<K> node) {
        return (node == null ? 0
                : size(node.left) + 1 + size(node.right));
    }

    private boolean checkBalance(Node<K> node) {
        return (node == null || (checkBalance(node.left) &&
                Math.abs(getBalance(node)) <= 1
                && checkBalance(node.right)));
    }
}
