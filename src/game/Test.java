package game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

class Test {

	@org.junit.jupiter.api.Test
	void BasicIteratorTest() {
		TreeNode<Path> node = new TreeNode<Path>(new Path());
		TreeNode<Path> child1 = node.addChild(new Path());
		TreeNode<Path> child2 = node.addChild(new Path());
		
		Iterator<TreeNode<Path>> iter = node.iterator();
		assertTrue(iter.hasNext());
		assertEquals(node, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(child1, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(child2, iter.next());
		assertTrue(!iter.hasNext());
	}
	
	@org.junit.jupiter.api.Test
	void AdvancedIteratorTest() {
		TreeNode<Path> n1 = new TreeNode<Path>(new Path());
		
		TreeNode<Path> n2 = n1.addChild(new Path());
		TreeNode<Path> n3 = n1.addChild(new Path());
		
		TreeNode<Path> n4 = n2.addChild(new Path());
		TreeNode<Path> n5 = n2.addChild(new Path());
		TreeNode<Path> n6 = n3.addChild(new Path());
		TreeNode<Path> n7 = n3.addChild(new Path());
		
		Iterator<TreeNode<Path>> iter = n1.iterator();
		assertTrue(iter.hasNext());
		assertEquals(n1, iter.next());
		
		assertTrue(iter.hasNext());
		assertEquals(n2, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(n4, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(n5, iter.next());
		
		assertTrue(iter.hasNext());
		assertEquals(n3, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(n6, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(n7, iter.next());
		
		assertTrue(!iter.hasNext());
	}

}


