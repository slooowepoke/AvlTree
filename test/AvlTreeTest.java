import org.junit.Assert;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import static org.junit.Assert.*;

public class AvlTreeTest {
    @Test
    public void iterator() throws Exception {
        AvlTree<Integer> avlTree = new AvlTree<>();
        final int size = 20;

        Random rnd = new Random();
        for ( int i = 0 ; i < size; ++i) {
            avlTree.add(50 - rnd.nextInt(100));
        }

        Assert.assertTrue("Tree is not balanced", avlTree.checkBalance());

        boolean success = true;

        Iterator<Integer> iter = avlTree.iterator();

        if (!iter.hasNext()) {
            success = false;
        } else {
            int elementWas = iter.next();
            while (iter.hasNext()) {
                int elementIs = iter.next();
                if (elementIs <= elementWas) {
                    success = false;
                }
                elementWas = elementIs;
            }
        }

        Assert.assertTrue("Test for 'iterator()' failed", success);
    }

    @Test
    public void equals() throws Exception {
        AvlTree<Integer> avlTree1 = new AvlTree<>();
        AvlTree<Integer> avlTree2 = new AvlTree<>();
        final int size = 20;

        Random rnd = new Random();
        for ( int i = 0 ; i < size; ++i) {
            Integer curr = 50 - rnd.nextInt(100);
            avlTree1.add(curr);
            avlTree2.add(curr);
        }

        Assert.assertTrue("Test for 'equals() failed: not equal'",
                avlTree1.equals(avlTree2));

        avlTree1.remove(avlTree1.iterator().next());

        Assert.assertFalse("Test for 'equals() failed: equal'",
                avlTree1.equals(avlTree2));
    }

    @Test
    public void isEmpty() throws Exception {
        AvlTree<Integer> avlTree = new AvlTree<>();
        Assert.assertTrue("Test for 'isEmpty()' failed: not empty", avlTree.isEmpty());

        avlTree.add(5);
        Assert.assertFalse("Test for 'isEmpty()' failed: empty", avlTree.isEmpty());

        avlTree.remove(5);
        Assert.assertTrue("Test for 'isEmpty()' failed: not empty after remove", avlTree.isEmpty());
    }

    @Test
    public void toArray() throws Exception {
        AvlTree<Integer> avlTree = new AvlTree<>();
        final int size = 20;

        Vector<Integer> arrayIn = new Vector<>();

        Random rnd = new Random();
        for ( int i = 0 ; i < size; ++i) {
            Integer curr = 50 - rnd.nextInt(100);
            if (avlTree.add(curr)) {
                arrayIn.add(curr);
            }
        }

        arrayIn.sort(null);

        Object[] arrayOut = avlTree.toArray();
        Assert.assertTrue("Test for 'toArray()' failed",
                Arrays.equals(arrayIn.toArray(), arrayOut));
    }

    @Test
    public void addContains() throws Exception {
        AvlTree<Integer> avlTree = new AvlTree<>();
        avlTree.add(5);

        Assert.assertTrue("Test for 'addContains()' failed", avlTree.contains(5));
        Assert.assertFalse("Test for 'addContains()' failed", avlTree.contains(7));
        Assert.assertFalse("Test for 'addContains()' failed", avlTree.add(5));
    }

    @Test
    public void remove() throws Exception {
        AvlTree<Integer> avlTree = new AvlTree<>();

        final int size = 20;

        Random rnd = new Random();
        for ( int i = 0 ; i < size; ++i) {
            Integer curr = 50 - rnd.nextInt(100);
            avlTree.add(curr);
        }

        Assert.assertTrue("Tree is not balanced", avlTree.checkBalance());
        Assert.assertTrue("Size check failed", avlTree.checkSize());

        Assert.assertTrue("Test for 'remove()' failed: not removed",
                avlTree.remove(avlTree.getMinValue()));

        Assert.assertTrue("Tree is not balanced", avlTree.checkBalance());
        Assert.assertTrue("Size check failed", avlTree.checkSize());

        Assert.assertTrue("Test for 'remove()' failed: not removed",
                avlTree.remove(avlTree.getMaxValue()));

        Assert.assertTrue("Tree is not balanced", avlTree.checkBalance());
        Assert.assertTrue("Size check failed", avlTree.checkSize());

        int rootValue = avlTree.getRootValue();

        Assert.assertTrue("Test for 'remove()' failed: not removed",
                avlTree.remove(rootValue));

        Assert.assertTrue("Tree is not balanced", avlTree.checkBalance());
        Assert.assertTrue("Size check failed", avlTree.checkSize());

        Assert.assertFalse("Test for 'remove()' failed: removed non-existing",
                avlTree.remove(rootValue));
    }

    @Test
    public void clear() throws Exception {
        AvlTree<Integer> avlTree = new AvlTree<>();
        avlTree.add(5);
        avlTree.clear();

        Assert.assertTrue("Tree is not balanced", avlTree.checkBalance());

        assertTrue("Test for 'clear()' failed", avlTree.isEmpty());
    }

    @Test
    public void size() throws Exception {
        AvlTree<Integer> avlTree = new AvlTree<>();
        Assert.assertTrue("Size check failed", avlTree.checkSize());
        Assert.assertFalse("Test for 'size()' failed: not 0", avlTree.size() != 0);

        avlTree.add(5);
        avlTree.add(2);
        avlTree.add(10);

        Assert.assertTrue("Size check failed", avlTree.checkSize());
        Assert.assertFalse("Test for 'size()' failed: not 1", avlTree.size() != 3);

        avlTree.remove(5);
        Assert.assertTrue("Size check failed", avlTree.checkSize());
        Assert.assertFalse("Test for 'size()' failed: not 0 after remove", avlTree.size() != 2);
    }

    @Test
    public void checkHashCode() throws Exception {
        AvlTree<Integer> avlTree1 = new AvlTree<>();
        AvlTree<Integer> avlTree2 = new AvlTree<>();
        final int size = 20;

        Random rnd = new Random();
        for ( int i = 0 ; i < size; ++i) {
            Integer curr = 50 - rnd.nextInt(100);
            avlTree1.add(curr);
            avlTree2.add(curr);
        }

        Assert.assertTrue(
                "Test for 'hashCode()' failed: codes for equal trees are not equal",
                avlTree1.hashCode() == avlTree2.hashCode());

        avlTree1.remove(avlTree1.iterator().next());

        Assert.assertFalse(
                "Test for 'hashCode()' failed: codes for different trees are equal",
                avlTree1.hashCode() == avlTree2.hashCode());
    }

}