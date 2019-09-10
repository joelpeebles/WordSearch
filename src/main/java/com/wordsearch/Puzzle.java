package main.java.com.wordsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Puzzle {

	int rows = 0;
	int columns = 0;
	int currentRow = 0;
	int currentColumn = -1;
	Direction direction;
	ArrayList<String> puzzleLines = new ArrayList<String>();
	ArrayList<String> words = new ArrayList<String>();
	Stack <Location> foundWord = new Stack<>();
	Location foundWordBegin;
	Location foundWordEnd;

	public Puzzle(int rows, int columns, ArrayList<String> puzzleLines, ArrayList<String> words) {
		this.rows = rows;
		this.columns = columns;
		this.puzzleLines = puzzleLines;
		this.words = words;
		this.direction = Direction.MAIN;
		foundWordBegin = null;
		foundWordEnd = null;
	}
	
	public Location nextLetter(){
		// letterLocation will remain null if there is no letter in the next rule-based position.
		Location letterLocation = null;
		
		if(foundWord.empty()) {
			// Have not found the first letter of the current word.  
			// Go to the next letter in the puzzle.
			if(currentColumn == columns-1) {
				currentColumn = 0;
				currentRow++;
			}
			else {
				currentColumn++;
			}
			letterLocation = new Location(currentRow, currentColumn);
		}
		else {
			// Based on current direction, grab the letter in the next rule-based position.
			switch(direction) {
			case MAIN:
				if(currentColumn == columns-1) {
					if(currentRow == rows) {
						// at the end, leave letterLocation null
					}
					else {
						currentRow++;
						currentColumn = 0;
					}
				}
				else {
					currentColumn++;
				}
				letterLocation = new Location(currentRow, currentColumn);
				break;
			case RIGHT:
				if(currentColumn == columns) {
					// at the end, leave letterLocation null
				}
				else {
					currentColumn++;
					letterLocation = new Location(currentRow, currentColumn);
				}
				break;
			case DOWN_RIGHT:
				if((currentColumn == columns) || (currentRow == rows)) {
					// at the end, leave letterLocation null
				}
				else {
					currentColumn++;
					currentRow++;
					letterLocation = new Location(currentRow, currentColumn);
				}
				break;
			case DOWN:
				if(currentRow == rows) {
					// at the end, leave letterLocation null
				}
				else {
					currentRow++;
					letterLocation = new Location(currentRow, currentColumn);
				}
				break;
			case DOWN_LEFT:
				if((currentColumn < 1) || (currentRow == rows)) {
					// at the end, leave letterLocation null
				}
				else {
					currentColumn--;
					currentRow++;
					letterLocation = new Location(currentRow, currentColumn);
				}
				break;
			case LEFT:
				if(currentColumn < 1) {
					// at the end, leave letterLocation null
				}
				else {
					currentColumn--;
					letterLocation = new Location(currentRow, currentColumn);
				}
				break;
			case UP_LEFT:
				if((currentColumn < 1) || (currentRow < 1)){
					// at the end, leave letterLocation null
				}
				else {
					currentColumn--;
					currentRow--;
					letterLocation = new Location(currentRow, currentColumn);
				}
				break;
			case UP:
				if(currentRow < 1) {
					// at the end, leave letterLocation null
				}
				else {
					currentRow--;
					letterLocation = new Location(currentRow, currentColumn);
				}
				break;
			case UP_RIGHT:
				if((currentRow < 1) || currentColumn == columns) {
					// at the end, leave letterLocation null
				}
				else {
					currentRow--;
					currentColumn++;
					letterLocation = new Location(currentRow, currentColumn);
				}
			}
		}
		return letterLocation;
	}
	
	// This member method is recursively called as we evaluate each rule-based letter location
	//  as to whether it matches the currently seeking word.
	public boolean checkIsMatch(String word, int wordPosition) {
		boolean isMatch = false;
		Location puzzleLetterLocation = nextLetter();
		if(puzzleLetterLocation != null) {
			// is the current letter equal to the current puzzle letter?
			char letter = word.toCharArray()[wordPosition];
			String currentLine = puzzleLines.get(puzzleLetterLocation.row);
			//char currentLetter = currentLine.toCharArray()[puzzleLetterLocation.column];
			char puzzleLetter = puzzleLines.get(puzzleLetterLocation.row).toCharArray()[puzzleLetterLocation.column];
			//System.out.println(letter);
			//System.out.println(puzzleLetter);
			if(letter == puzzleLetter) {
				if(foundWord.isEmpty()) {
					// Found the first letter of a word.  
					// Set to move to the right as the first check for the next letter.
					direction = Direction.RIGHT;
					foundWordBegin = new Location(currentRow, currentColumn);
				}
				// letter matches. get next position
				//System.out.println("letter matches " + letter);
				foundWord.push(puzzleLetterLocation);
				if(word.length() == foundWord.size()) {
					// found whole word
					foundWordEnd = new Location(currentRow, currentColumn);
					isMatch = true;
					System.out.println( word + " " + foundWordBegin.row + ":" + foundWordBegin.column 
							+ " " + foundWordEnd.row + ":" + foundWordEnd.column);
				}
				else {
					// Check the next letter.
					isMatch = checkIsMatch(word, wordPosition + 1);
					if(!isMatch) {
						// Look at each surrounding letter in a clockwise format 
						//  starting with down-right.
						// This only occurs when looking for the second letter of each word.
						while((direction != Direction.UP_RIGHT) && !isMatch) {
							nextDirection();
							currentRow = foundWordBegin.row;
							currentColumn = foundWordBegin.column;
							if(direction == Direction.MAIN) {
								// Looking for the first letter of a word, not found.
								foundWord.pop();
								//if(foundWord.isEmpty()) {
								//	System.out.println("foundWord empty");
								//}
							}
							else {
								isMatch = checkIsMatch(word, wordPosition + 1);
							}
						}
						currentRow = foundWordBegin.row;
						currentColumn = foundWordBegin.column;
						foundWord.clear();
					}
				}
			}
		}
		return isMatch;
	}
	
	private void nextDirection() {
		switch(direction) {
		case MAIN:
			direction = Direction.RIGHT;
			break;
		case RIGHT:
			direction = Direction.DOWN_RIGHT;
			break;
		case DOWN_RIGHT:
			direction = Direction.DOWN;
			break;
		case DOWN:
			direction = Direction.DOWN_LEFT;
			break;
		case DOWN_LEFT:
			direction = Direction.LEFT;
			break;
		case LEFT:
			direction = Direction.UP_LEFT;
			break;
		case UP_LEFT:
			direction = Direction.UP;
			break;
		case UP:
			direction = Direction.UP_RIGHT;
			break;
		case UP_RIGHT:
			direction = Direction.MAIN;
			break;
		}
	}
	
	public void solve() {
		for(String word : words) {
			// flow through puzzle and find occurance
			int wordPosition = 0;
			boolean wordFound = false;
			wordPosition = 0;
			while(wordPosition < word.length()) {
				wordFound = checkIsMatch(word, wordPosition);
				if(wordFound) {
					break;
				}
				else {
					direction = Direction.MAIN;
				}
			}
			currentRow = 0;
			currentColumn = -1;
			foundWord.clear();
			foundWordBegin = null;
			foundWordEnd = null;
			if(!wordFound) {
				System.out.println("word not found");
			}
		}
		
	}


}
