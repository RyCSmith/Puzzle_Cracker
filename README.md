# Puzzle_Cracker
Overview: Uses graph theory and DFS to find successful paths in numerical puzzles.

Design: This concept was assigned by Dr. Dave Matuszek at the University of Pennsylvania. All structure and implementation created by Ryan Smith.

Use: This program is run from the Backtracking class. I launches a JFileChooser that can be used to select a .txt file containing puzzles. Puzzles take the form of strings of numbers with one puzzle per line. An example of a puzzle would be 3641342531292. Beginning with index zero, it is possible to move to the right or left commensurate with the number contained in the index you are currently viewing. An example solution for this problem would be Right, Right, Right, Right which would result in landing on the last index of the puzzle which is the goal. 

This program provides two methods; solve() and findAllSolutions(). solve() takes a simple iterative DFS approach using the puzzle (in an array) directly and returns any one possible path if the puzzle is solvable and null if not. findAllSolutions first processes the puzzle into a directed graph using the Vertex class. It then uses recursive DFS to find all possible solutions (paths to the ending index or last Vertex). It makes deep copies of these paths along the way and returns a Set or Stacks containing the paths. 

Other Info: This program also includes ImproperFormatException.java. This is a custom exception which is thrown from main() if the puzzle file does not meet format criteria. A puzzle file for demonstration puzzles.txt has also been included in this rep. 