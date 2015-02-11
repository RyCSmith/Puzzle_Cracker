package backtracking;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.junit.Before;
import org.junit.Test;

public class BacktrackingTest {
	Stack<Character> test1;
	Stack<Character> test2;
	Set<Stack<Character>> test3;
	int[] puzzle = {3, 6, 4, 1, 3, 4, 2, 5, 3, 0};
	int[] puzzle2 = {3, 6, 4, 1, 3, 4, 2, 5, 3, 1, 2, 9, 2};
	int[] puzzle3 = {3, 6, 4, 20, 3, 4, 2, 5, 3, 1, 2, 9, 2};
	int[] puzzle4 = {3, 6, 4, 6, 3, 4, 2, 5, 3, 6, 2, 9, 2};

	
	@Before
	public void setUp() throws Exception {
		test1 = new Stack<Character>();
		test1.add('R');test1.add('L');test1.add('R');test1.add('R');test1.add('L');test1.add('R');
		test2 = new Stack<Character>();
		test2.add('R');test2.add('L');test2.add('R');test2.add('L');test2.add('L');test2.add('R');test2.add('R');
		test3 = new HashSet<Stack<Character>>();
		Stack<Character> sol2 = new Stack<Character>();
		sol2.add('R');sol2.add('R');sol2.add('L');sol2.add('R');sol2.add('L');sol2.add('R');sol2.add('R');sol2.add('L');sol2.add('R');
		Stack<Character> sol3 = new Stack<Character>();
		sol3.add('R');sol3.add('R');sol3.add('R');sol3.add('L');sol3.add('R');sol3.add('R');sol3.add('L');sol3.add('R');
		test3.add(test1); test3.add(sol2); test3.add(sol3);
	}

	@Test
	public void testSolve() {
		assertEquals(test1, Backtracking.solve(puzzle));
		assertEquals(test2, Backtracking.solve(puzzle2));
		assertEquals(null, Backtracking.solve(puzzle3));
		assertEquals(null, Backtracking.solve(puzzle4));

	}

	@Test
	public void testFindAllSolutions() {
		assertEquals(test3, Backtracking.findAllSolutions(puzzle));
	}

}
