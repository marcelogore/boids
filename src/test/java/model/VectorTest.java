package model;

import model.Vector;

import org.junit.Test;
import static org.junit.Assert.*;

public class VectorTest {

	@Test
	public void testAdd() {

		Vector a = new Vector(2,3);
		Vector b = new Vector(7,8);
		
		a.add(b);
		
		assertEquals(9, a.x);
		assertEquals(11, a.y);
		
		assertEquals(7, b.x);
		assertEquals(8, b.y);
	}

	@Test
	public void testSubtract() {

		Vector a = new Vector(2,3);
		Vector b = new Vector(7,8);
		
		b.subtract(a);
		
		assertEquals(2, a.x);
		assertEquals(3, a.y);
		
		assertEquals(5, b.x);
		assertEquals(5, b.y);
	}
	
	@Test
	public void testMultiply() {

		Vector a = new Vector(2,3);
		
		a.multiply(10);
		
		assertEquals(20, a.x);
		assertEquals(30, a.y);
	}

	@Test
	public void testDivide() {

		Vector a = new Vector(2,3);
		
		a.divide(2);
		
		assertEquals(1, a.x);
		assertEquals(1, a.y);
	}
	
	@Test
	public void testLength() {

		Vector a = new Vector(2,3);
		
		assertEquals(3.6055, a.length(), 0.0001);
	}


}
