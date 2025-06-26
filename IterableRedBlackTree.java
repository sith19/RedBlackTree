import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * This class extends RedBlackTree into a tree that supports iterating over the values it
 * stores in sorted, ascending order.
 */
public class IterableRedBlackTree<T extends Comparable<T>>
                extends RedBlackTree<T> implements IterableSortedCollection<T> {
    private Comparable<T> max = null;
    private Comparable<T> min = null;
    //store current stopping and starting point
    /**
     * Allows setting the start (minimum) value of the iterator. When this method is called,
     * every iterator created after it will use the minimum set by this method until this method
     * is called again to set a new minimum value.
     * @param min the minimum for iterators created for this tree, or null for no minimum
     */
    public void setIteratorMin(Comparable<T> min) { this.min = min;}
    //set minimum private field
        
    /**
     * Allows setting the stop (maximum) value of the iterator. When this method is called,
     * every iterator created after it will use the maximum set by this method until this method
     * is called again to set a new maximum value.
     * @param min the maximum for iterators created for this tree, or null for no maximum
     */
    public void setIteratorMax(Comparable<T> max) {this.max = max; }
    //set maximum private field

    /**
     * Returns an iterator over the values stored in this tree. The iterator uses the
     * start (minimum) value set by a previous call to setIteratorMin, and the stop (maximum)
     * value set by a previous call to setIteratorMax. If setIteratorMin has not been called
     * before, or if it was called with a null argument, the iterator uses no minimum value
     * and starts with the lowest value that exists in the tree. If setIteratorMax has not been
     * called before, or if it was called with a null argument, the iterator uses no maximum
     * value and finishes with the highest value that exists in the tree.
     */
    public Iterator<T> iterator() { 
      return new RBTIterator<T>(super.root, min, max);
      //create a new iterator pass it current tree root and min max fields
    }

    /**
     * Nested class for Iterator objects created for this tree and returned by the iterator method.
     * This iterator follows an in-order traversal of the tree and returns the values in sorted,
     * ascending order.
     */
    protected static class RBTIterator<R> implements Iterator<R> {

         // stores the start point (minimum) for the iterator
         Comparable<R> min = null;
         // stores the stop point (maximum) for the iterator
         Comparable<R> max = null;
         // stores the stack that keeps track of the inorder traversal
         Stack<BinaryTreeNode<R>> stack = null;

        /**
         * Constructor for a new iterator if the tree with root as its root node, and
         * min as the start (minimum) value (or null if no start value) and max as the
         * stop (maximum) value (or null if no stop value) of the new iterator.
         * @param root root node of the tree to traverse
         * @param min the minimum value that the iterator will return
         * @param max the maximum value that the iterator will return 
         */
        public RBTIterator(BinaryTreeNode<R> root, Comparable<R> min, Comparable<R> max) { 
            this.min = min;
            this.max = max;
            this.stack = new Stack<BinaryTreeNode<R>>();
            buildStackHelper(root);
            //initiate fields and use build stack method to fill the stack
        }

        /**
         * Helper method for initializing and updating the stack. This method both
         * - finds the next data value stored in the tree (or subtree) that is 
         * between start(minimum) and stop(maximum) point (including start and stop points themselves), and
         * - builds up the stack of ancestor nodes that contain values between start(minimum) and stop(maximum) values 
         * (including start and stop values themselves) so that those nodes can be visited in the future.
         * @param node the root node of the subtree to process
         */
        private void buildStackHelper(BinaryTreeNode<R> node) { 
          if(node == null) {
            //base case, if current node is null
            return;
          }
          if(min != null && min.compareTo(node.data) > 0 ) {
            //if minimum is larger go to right subtree of node
            buildStackHelper(node.right);
          }else {
            //if minimum is smaller or equal push node into stack and go to left subtree
            stack.push(node);
            buildStackHelper(node.left);
          }
        }

        /**
         * Returns true if the iterator has another value to return, and false otherwise.
         */
        public boolean hasNext() { 
          if(!stack.isEmpty()) {
            //check that stack still has values
            if(max != null) {
              //check if max is set
              if(max.compareTo(stack.peek().getData()) >= 0) {
                //if top of stack is smaller or equal to max there is a next value
                return true;
              }else {
                //if top of stack is larger than max there is not another value to pass
                return false;
              }
            }
            //if there is no max and stack is not empty there is a next value
            return true;
          }
          //if stack is empty there is no value to pass
          return false;
          }

        /**
         * Returns the next value of the iterator.
         * @throws NoSuchElementException if the iterator has no more values to return
         */
        public R next() { 
          if(!hasNext()) {
            //if there is no next value throw NoSuchElementException
            throw new NoSuchElementException("No More Nodes To Visit");
          }
          BinaryTreeNode<R> node = stack.pop();
          //get next value for stack
          if((min == null || min.compareTo(node.getData()) <= 0) && (max == null || max.compareTo(node.getData()) >= 0)) {
            //check if node is between min and max
            buildStackHelper(node.right);
            //push right subtree of node to stack
            return node.getData();
            //get nodes data
          }
          return null;
          //otherwise return null
          
          }
        
    }
    /**
     * tests basic iterator functionality and iterator with duplicate values
     */
    @Test
    public void testIterator() {
      //create Integer tree
      IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<Integer>();
      //insert values into tree
      tree.insert(10);
      tree.insert(7);
      tree.insert(17);
      tree.insert(11);
      tree.insert(14);
      tree.insert(18);
      String result = "";
      for(Integer i: tree) {
        //get values using iterator
        result += i + " ";
      }
      if(!result.equals("7 10 11 14 17 18 ")) {
        //verify result
        Assertions.fail("iterator does not return correct order");
      }
      //clear the tree and insert new values including duplicate
      tree.clear();
      tree.insert(65);
      tree.insert(68);
      tree.insert(77);
      tree.insert(100);
      tree.insert(7000);
      tree.insert(66);
      tree.insert(68);
      result = "";
      for(Integer i: tree) {
        //get values with iterator
        result += i + " ";
      }
      if(!result.equals("65 66 68 68 77 100 7000 ")) {
        //verify result
        Assertions.fail("iterator does not contain duplicate values");
      }
      
    }
    /**
     * tests an iterator with a created starting point
     * tests when starting point is out of range as well
     */
    @Test
    public void testIteratorMin() {
      //create String tree
      IterableRedBlackTree<String> tree = new IterableRedBlackTree<String>();
      //insert values into tree
      tree.insert("Apple");
      tree.insert("Bicycle");
      tree.insert("Dolphin");
      tree.insert("Forest");
      tree.insert("Kite");
      tree.insert("Lantern");
      tree.setIteratorMin("Bicycle");
      //set starting point
      String result = "";
      for(String i: tree) {
        //get values with iterator
        result += i + " ";
      }
      if(!result.equals("Bicycle Dolphin Forest Kite Lantern ")) {
        //verify result
        Assertions.fail("iterator does not utillize minimum");
      }
      tree.setIteratorMin("Elephant");
      //test iterator on a value not in the tree
      result = "";
      for(String i: tree) {
        //get values with iterator
        result += i + " ";
      }
      if(!result.equals("Forest Kite Lantern ")) {
        //verify result
        Assertions.fail("iterator does not handle non existant min");
      }
      tree.setIteratorMin("Nest");
      //test iterator on a value outside tree range
      result = "";
      for(String i: tree) {
        //get values with iterator
        result += i + " ";
      }
      if(!result.equals("")) {
        //verify result
        Assertions.fail("iterator does not handle minimum outside range");
      }
    }
    /**
     * tests an iterator with a created end point as well as both an endpoint and a start point
     * also tests when endpoint is out of range and starting point is in range
     */
    @Test
    public void testIteratorMax() {
      //create character tree
      IterableRedBlackTree<Character> tree = new IterableRedBlackTree<Character>();
      //insert values to tree including duplicate
      tree.insert('B');
      tree.insert('C');
      tree.insert('H');
      tree.insert('A');
      tree.insert('G');
      tree.insert('D');
      tree.insert('D');
      tree.setIteratorMax('C');
      //set stopping point
      String result = "";
      for(Character i: tree) {
        //get values from iterator
        result += i + " ";
      }
      if(!result.equals("A B C ")) {
        //verify result
        Assertions.fail("iterator does not utillize maximum");
      }
      tree.setIteratorMax('F');
      //test iterator on a maximum value not in the tree
      result = "";
      for(Character i: tree) {
        //get values
        result += i + " ";
      }
      if(!result.equals("A B C D D ")) {
        //verify result using iterator
        Assertions.fail("iterator does not handle non existant max");
      }
      tree.setIteratorMin('B');
      //test iterator with both starting and stopping point
      result = "";
      for(Character i: tree) {
        //get values with iterator
        result += i + " ";
      }
      if(!result.equals("B C D D ")) {
        //verify result
        Assertions.fail("iterator does not handle starting and stopping point");
      }
      tree.setIteratorMax('A');
      //tests when min and max don't form a range
      result = "";
      for(Character i: tree) {
        //get values from iterator
        result += i + " ";
      }
      if(!result.equals("")) {
        //verify result
        Assertions.fail("iterator does not handle min in range and max not in range");
      }
    }
    

}
