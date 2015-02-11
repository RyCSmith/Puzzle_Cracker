package backtracking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import javax.swing.JFileChooser;

/**
 * BackTracking - Reads in puzzles from a file and cracks them using graph theory.
 * @author Ryan Smith
 */
public class Backtracking {
	
	/**
	 * Main method - prompts user for file of puzzles. Then runs findAllSolutions().
	 */
	public static void main(String[] args){
		try {
			BufferedReader bReader = getFileReader();
			String currentLine = bReader.readLine();
			//loops through a file reading in puzzles
			while (currentLine != null){
				//converts from String to int array, checks proper format
				char[] charArray = currentLine.toCharArray();
				int[] intArray = new int[charArray.length];
				for (int i = 0; i < charArray.length; i++){
					if (Character.isDigit(charArray[i]))
						intArray[i] = Character.getNumericValue(charArray[i]);
					else
						throw new ImproperFormatException("Your file contains a character that is not a digit.");
				}

				//solves the formatted puzzle
				System.out.println(findAllSolutions(intArray));
				currentLine = bReader.readLine();
			}
			bReader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ImproperFormatException e){
			System.out.println(e.toString());
		}
	}
	
	/**
	 * Evaluates a puzzle and attempts to find a successful path between beginning and ending vertices.
	 * @param puzzle The full puzzle.
	 * @return Stack<Character> The successful path. Returns null if no path could be found.
	 */
	public static Stack<Character> solve(int[] puzzle){
		Stack<Character> path = new Stack<Character>();
		Stack<Integer> indexPath = new Stack<Integer>();
		HashSet<Integer> visitedIndices = new HashSet<Integer>();
		int currentIndex = 0;
		indexPath.push(currentIndex);
		visitedIndices.add(currentIndex);
		
		while (indexPath.empty() == false){
			//checks if the current path is the target, returns path if so
			if (currentIndex == puzzle.length - 1)
				return path;
			//checks if there is a left child and if it has been visited
			int leftChildIndex = currentIndex - puzzle[currentIndex];
			if (leftChildIndex >= 0){
				if (visitedIndices.contains(leftChildIndex) == false){
					currentIndex = leftChildIndex;
					path.push('L');
					indexPath.push(currentIndex);
					visitedIndices.add(currentIndex);
					continue;
				}
			}
			//checks if there is a right child and if it has been visited
			int rightChildIndex = currentIndex + puzzle[currentIndex];
			if (rightChildIndex <= puzzle.length - 1){
				if (visitedIndices.contains(rightChildIndex) == false){
					currentIndex = rightChildIndex;
					path.push('R');
					indexPath.push(currentIndex);
					visitedIndices.add(currentIndex);
					continue;
				}
			}
			//if no child is available, currentIndex pops back up to last node
			indexPath.pop();
			//this if statement protects from Exception when the last node is popped
			if (!indexPath.empty()){
				currentIndex = indexPath.peek();
				path.pop();
			}
		}
		return null;
	}
	
	/**
	 * Finds all possible solution paths in a puzzle.
	 * @param puzzle The full puzzle
	 * @return Set<Stack<Character>> Set of successful paths. Returns null if no path could be found
	 */
	public static Set<Stack<Character>> findAllSolutions(int[] puzzle){
		Set<Stack<Character>> allPaths = new HashSet<Stack<Character>>();
		TreeMap<Integer, Vertex> verticesMap = new TreeMap<Integer, Vertex>();
		ArrayList<Integer> visitedIndices = new ArrayList<Integer>();
		processPuzzle(verticesMap, puzzle);
		visitedIndices.add(0);
		allPathsHelper(verticesMap, visitedIndices, puzzle, allPaths);
		if (allPaths.isEmpty())
			return null;
		return allPaths;
	}
	
	/**
	 * Private function uses DFS and backtracking to follow connections between Vertex objects and 
	 * find all possible paths between starting and ending index of puzzle. Use recursion.
	 * @param TreeMap<Integer, Vertex> Map of all Vertex objects generated from puzzle.
	 * @param ArrayList<Integer> List of all indices visited in current path upon entering the recursive function.
	 * @param int[] The full puzzle.
	 * @param Set<Stack<Character>> Object where all successful paths are stored.
	 */
	private static void allPathsHelper(TreeMap<Integer, Vertex> verticesMap, ArrayList<Integer> visitedIndices, int[] puzzle, Set<Stack<Character>> allPaths){
		//gets index of last vertex visited (key for map) and get's it's connections
		ArrayList<Integer> connections = verticesMap.get(visitedIndices.get(visitedIndices.size() -1)).getConnections();
		
		for (Integer index : connections){
			if (index == puzzle.length - 1){
				//invariant - if here = path found - make deep copy
				deepCopier(visitedIndices, allPaths, puzzle);
				break;
			}
		}
		for (Integer index : connections){
			//make sure not to evaluate connections from last index, do not revisit any indexes either
			if (index == puzzle.length - 1 || visitedIndices.contains(index))
				continue;
			else{
				//add non-terminal index to path and recurse
				visitedIndices.add(index);
				allPathsHelper(verticesMap, visitedIndices, puzzle, allPaths);
				//backtracking
				visitedIndices.remove(visitedIndices.size() - 1);
			}
		}
	}
	
	/**
	 * Processes the puzzle by creating Vertex objects for each index
	 * @param int[] The full puzzle to be solved
	 */
	private static void processPuzzle(TreeMap vertMap, int[] puzzle){
		Vertex newVertex;
		int leftConnection;
		int rightConnection;
		for (int i = 0; i < puzzle.length; i++){
			int currentIndex = i;
			//evaluates connections - condition prevents self connections (cycles)
			if (puzzle[currentIndex] == 0){
				leftConnection = -1;
				rightConnection = puzzle.length;
			}
			else{
				leftConnection = currentIndex - puzzle[currentIndex];
				rightConnection = currentIndex + puzzle[currentIndex];
			}
			//create Vertex with connections as allowed by puzzle constraints
			if (leftConnection >= 0 && rightConnection <= puzzle.length - 1)
				newVertex = new Vertex(currentIndex, leftConnection, rightConnection);
			else if (leftConnection >= 0 && rightConnection > puzzle.length - 1)
				newVertex = new Vertex(currentIndex, leftConnection);
			else if (leftConnection < 0 && rightConnection <= puzzle.length - 1)
				newVertex = new Vertex(currentIndex, rightConnection);
			else
				newVertex = new Vertex(currentIndex);
			//add to map with key == index in puzzle
			vertMap.put(currentIndex, newVertex);
		}
	}
	
	/**
	 * Produces a copy of the successful path for saving.
	 * @param ArrayList<Integer> List containing all indices in the puzzle that have been visited up to this point
	 * @param Set<Stack<Character>> Set where the successful path with be stored (as a Stack)
	 * @param int[] The full puzzle
	 */
	private static void deepCopier(ArrayList<Integer> visitedIndices, Set<Stack<Character>> allPaths, int[] puzzle){
		Stack<Character> foundPath = new Stack<Character>();
		for(int i = 0; i < visitedIndices.size() - 1; i++){
			if (visitedIndices.get(i) > visitedIndices.get(i + 1))
				foundPath.push('L');
			else
				foundPath.push('R');
		}
		//last move will always be right when solving these puzzles
		foundPath.push('R');
		allPaths.add(foundPath);
	}
	
	/**
	 * Opens JFileChooser and allows the user to select a file of puzzles.
	 * @return BufferedReader references the file stream 
	 */
	private static BufferedReader getFileReader() throws IOException {
        BufferedReader reader = null;
        String fileName;
        
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load which file?");
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null) {
                fileName = file.getCanonicalPath();
                reader = new BufferedReader(new FileReader(fileName));
            }
        }
        return reader;
    }
}
