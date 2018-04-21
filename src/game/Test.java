package game;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
	
	@org.junit.jupiter.api.Test
	void TreeMethodIterationTest() {
		TreeNode<Path> a = new TreeNode<Path>(new Path());
		TreeNode<Path> b = a.addChild(new Path());
		TreeNode<Path> c = a.addChild(new Path());
		TreeNode<Path> d = b.addChild(new Path());
		TreeNode<Path> e = b.addChild(new Path());
		TreeNode<Path> f = c.addChild(new Path());
		TreeNode<Path> g = c.addChild(new Path());
		
		ArrayList<TreeNode<Path>> expected = new ArrayList<TreeNode<Path>>();
		expected.add(a);
		expected.add(b);
		expected.add(d);
		expected.add(e);
		expected.add(c);
		expected.add(f);
		expected.add(g);
		
		assertEquals(expected, a.toArray());
		assertEquals(expected, b.toArray());
		assertEquals(expected, g.toArray());
	}
	
	@org.junit.jupiter.api.Test
	void loadGameTest() {
		String loadFile = "testing";
		String current_dir = null;
		try {
			current_dir = new java.io.File(".").getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file = new File(current_dir + "\\src\\" + loadFile);
		GameReader gr = new TextFileReader();
		TreeNode<Path> beginning = gr.read(file);

		ArrayList<TreeNode<Path>> arr = beginning.toArray();
		
		TreeNode<Path> node = arr.get(1);
		assertEquals("welcome to b", node.getData().getPathText());
		assertEquals("b", node.getData().getChoiceText());
		assertTrue(node.parent == beginning);
		TreeNode<Path> b = node;
		
		node = arr.get(2);
		assertEquals("d loses", node.getData().getPathText());
		assertEquals("d", node.getData().getChoiceText());
		assertTrue(node.getData().isDeathNode());
		assertTrue(node.parent == b);
		
		node = arr.get(3);
		assertEquals("e wins", node.getData().getPathText());
		assertEquals("e", node.getData().getChoiceText());
		assertTrue(node.getData().isWinNode());
		assertTrue(node.parent == b);
		
		node = arr.get(4);
		assertEquals("welcome to c", node.getData().getPathText());
		assertEquals("c", node.getData().getChoiceText());
		assertTrue(node.parent == beginning);
		TreeNode<Path> c = node;
		
		node = arr.get(5);
		assertEquals("f loses", node.getData().getPathText());
		assertEquals("f", node.getData().getChoiceText());
		assertTrue(node.getData().isDeathNode());
		assertTrue(node.parent == c);
		
		node = arr.get(6);
		assertEquals("g wins", node.getData().getPathText());
		assertEquals("g", node.getData().getChoiceText());
		assertTrue(node.getData().isWinNode());
		assertTrue(node.parent == c);
	}
}


