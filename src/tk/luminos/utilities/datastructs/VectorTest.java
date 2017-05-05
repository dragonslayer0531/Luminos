package tk.luminos.utilities.datastructs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VectorTest {
	
	private Vector<String> vec;
	
	public void setUp()
	{
		vec = new Vector<String>();
	}

	@Test
	public void testPush() {
		setUp();
		vec.push("test");;
		assertEquals(1, vec.size());
		assertEquals(vec.peek(), "test");
	}
	
	@Test
	public void testPush2() {
		setUp();
		vec.push("test", "test2");
		assertEquals(2, vec.size());
		assertEquals(vec.peek(), "test2");
	}
	
	@Test
	public void testPush3() {
		setUp();
		List<String> list = new ArrayList<String>();
		list.add("test");
		list.add("test2");
		vec.push(list);
		assertEquals(2, vec.size());
		assertEquals(vec.peek(), "test2");
	}
	
	@Test
	public void testPop() {
		setUp();
		vec.push("test", "test2");
		assertEquals(vec.pop(), "test2");
		assertEquals(vec.pop(), "test");
		assertEquals(vec.size(), 0);
		assertTrue(vec.isEmpty());
	}
	
	@Test
	public void testPop2() {
		setUp();
		try {
			vec.pop();
		}
		catch (Exception e)
		{
			// Empty Catch
		}
		assertEquals(vec.size(), 0);
	}
	
	@Test
	public void testPeek() {
		setUp();
		try {
			vec.peek();
		}
		catch (Exception e)
		{
			// Empty catch
		}
		
		assertEquals(vec.size(), 0);
	}
	
	@Test
	public void testPeek2() {
		setUp();
		vec.push("test");
		assertEquals(vec.peek(), "test");
		assertEquals(vec.size(), 1);
	}
	
	@Test
	public void testIsEmpty() {
		setUp();
		assertTrue(vec.isEmpty());
	}
	
	@Test
	public void testIsEmpty2() {
		setUp();
		vec.push("test");
		assertFalse(vec.isEmpty());
	}
	
	@Test
	public void testClear() {
		VectorADT<String> vec = new Vector<String>("test");
		vec.clear();
		assertEquals(vec.size(), 0);
	}
	
	@Test
	public void testEquals() {
		setUp();
		boolean equals = vec.equals(null);
		assertFalse(equals);
	}
	
	@Test
	public void testEquals2() {
		setUp();
		vec.push("test");
		assertFalse(vec.equals(new Vector<String>("test2", "test3")));
	}
	
	@Test
	public void testEquals3() {
		setUp();
		assertEquals(vec, new Vector<String>());
	}
	
	@Test
	public void testEquals4() {
		setUp();
		assertFalse(vec.equals("test"));
	}
	
	@Test
	public void testEquals5() {
		setUp();
		Vector<String> strVec = new Vector<String>();
		strVec.push("test");
		vec.push("test");
		assertEquals(strVec, vec);
	}
	
	@Test
	public void testEquals6() {
		setUp();
		Vector<String> strVec = new Vector<String>();
		strVec.push("test2");
		vec.push("test");
		assertFalse(vec.equals(strVec));
	}

}
