package main.java.com.wordsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WordSearch {
	
	private int rows;
	private int columns;
	private ArrayList<String> puzzleLines;
	private ArrayList<String> words;
	
	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("Please pass the path to the input file as a program argument.");
		}
		else {
			String inputFilePath = args[0];
			WordSearch wordSearch = new WordSearch(inputFilePath);
			wordSearch.solve();
		}
	}
	
	public WordSearch(String inputFilePath) {
		// Read the text-based input file line by line, parse into individual fields,
		//  pass fields to Puzzle and solve
		File file = new File(inputFilePath);
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			String dimensionsLine = bufferedReader.readLine();
			
			// Separate first line using "x" as the delimeter
			String dimensionsString[] = dimensionsLine.split("x");
			
			// Parse rows and columns to appropriate variables.
			rows = Integer.parseInt(dimensionsString[0]);
			columns = Integer.parseInt(dimensionsString[1]);

			// Create puzzle rows to search (remove spaces between letters)
			puzzleLines = new ArrayList<String>();
			for(int currentRow = 0; currentRow < rows; currentRow++) {
				String newLine = bufferedReader.readLine();
				newLine = newLine.replaceAll("\\s", "");
				puzzleLines.add(newLine);
			}
			
			// Read dictionary words in to array list
			words = new ArrayList<>();
			String word = "";
			while((word = bufferedReader.readLine()) != null) {
				words.add(word);
			}
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			// Finished reading file, now close.
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void solve() {
		Puzzle puzzle = new Puzzle(rows, columns, puzzleLines, words);
		puzzle.solve();
	}
	
}
