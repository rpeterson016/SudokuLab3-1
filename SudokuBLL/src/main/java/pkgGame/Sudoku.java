package pkgGame;

import pkgEnum.ePuzzleViolation;
import pkgHelper.LatinSquare;
import pkgHelper.PuzzleViolation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

import org.apache.commons.lang.ArrayUtils;

/**
 * Sudoku - This class extends LatinSquare, adding methods, constructor to
 * handle Sudoku logic
 * 
 * @version 1.2
 * @since Lab #2
 * @author Bert.Gibbons
 *
 */
public class Sudoku extends LatinSquare {

	/**
	 * 
	 * iSize - the length of the width/height of the Sudoku puzzle.
	 * 
	 * @version 1.2
	 * @since Lab #2
	 */
	private int iSize;

	/**
	 * iSqrtSize - SquareRoot of the iSize. If the iSize is 9, iSqrtSize will be
	 * calculated as 3
	 * 
	 * @version 1.2
	 * @since Lab #2
	 */

	private int iSqrtSize;

	/**
	 * Sudoku - for Lab #2... do the following:
	 * 
	 * set iSize If SquareRoot(iSize) is an integer, set iSqrtSize, otherwise throw
	 * exception
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @param iSize-
	 *            length of the width/height of the puzzle
	 * @throws Exception
	 *             if the iSize given doesn't have a whole number square root
	 */
	
	private HashMap<Integer, Sudoku.Cell> hash_map = new HashMap<Integer, Sudoku.Cell>();
	
	public Sudoku(int iSize) throws Exception {
		this.iSize = iSize;

		double SQRT = Math.sqrt(iSize);
		if ((SQRT == Math.floor(SQRT)) && !Double.isInfinite(SQRT)) {
			this.iSqrtSize = (int) SQRT;
		} else {
			throw new Exception("Invalid size");
		}
	}

	/**
	 * Sudoku - pass in a given two-dimensional array puzzle, create an instance.
	 * Set iSize and iSqrtSize
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @param puzzle
	 *            - given (working) Sudoku puzzle. Use for testing
	 * @throws Exception will be thrown if the length of the puzzle do not have a whole number square root
	 */
	public Sudoku(int[][] puzzle) throws Exception {
		super(puzzle);
		this.iSize = puzzle.length;
		double SQRT = Math.sqrt(iSize);
		if ((SQRT == Math.floor(SQRT)) && !Double.isInfinite(SQRT)) {
			this.iSqrtSize = (int) SQRT;
		} else {
			throw new Exception("Invalid size");
		}

	}

	/**
	 * getPuzzle - return the Sudoku puzzle
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @return - returns the LatinSquare instance
	 */
	public int[][] getPuzzle() {
		return super.getLatinSquare();
	}

	/**
	 * getRegion - figure out what region you're in based on iCol and iRow and call
	 * getRegion(int)<br>
	 * 
	 * Example, the following Puzzle:
	 * 
	 * 0 1 2 3 <br>
	 * 1 2 3 4 <br>
	 * 3 4 1 2 <br>
	 * 4 1 3 2 <br>
	 * 
	 * getRegion(0,3) would call getRegion(1) and return [2],[3],[3],[4]
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @param iCol
	 *            given column
	 * @param iRow
	 *            given row
	 * @return - returns a one-dimensional array from a given region of the puzzle
	 */
	public int[] getRegion(int iCol, int iRow) {

		int i = (iCol / iSqrtSize) + ((iRow / iSqrtSize) * iSqrtSize);

		return getRegion(i);
	}

	/**
	 * getRegion - pass in a given region, get back a one-dimensional array of the
	 * region's content<br>
	 * 
	 * Example, the following Puzzle:
	 * 
	 * 0 1 2 3 <br>
	 * 1 2 3 4 <br>
	 * 3 4 1 2 <br>
	 * 4 1 3 2 <br>
	 * 
	 * getRegion(2) and return [3],[4],[4],[1]
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @param r
	 *            given region
	 * @return - returns a one-dimensional array from a given region of the puzzle
	 */

	public int[] getRegion(int r) {

		int[] reg = new int[super.getLatinSquare().length];


		int i = (r / iSqrtSize) * iSqrtSize;
		int j = (r % iSqrtSize) * iSqrtSize;		
		int jMax = j + iSqrtSize;
		int iMax = i + iSqrtSize;
		int iCnt = 0;

		for (; i < iMax; i++) {
			for (j = (r % iSqrtSize) * iSqrtSize; j < jMax; j++) {
				reg[iCnt++] = super.getLatinSquare()[i][j];
			}
		}

		return reg;
	}
	
 
	
	@Override
	public boolean hasDuplicates()
	{
		if (super.hasDuplicates())
			return true;
		
		for (int k = 0; k < this.getPuzzle().length; k++) {
			if (super.hasDuplicates(getRegion(k))) {
				super.AddPuzzleViolation(new PuzzleViolation(ePuzzleViolation.DupRegion,k));
			}
		}
	
		return (super.getPV().size() > 0);
	}

	/**
	 * isPartialSudoku - return 'true' if...
	 * 
	 * It's a LatinSquare If each region doesn't have duplicates If each element in
	 * the first row of the puzzle is in each region of the puzzle At least one of
	 * the elemnts is a zero
	 * 
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @return true if the given puzzle is a partial sudoku
	 */
	public boolean isPartialSudoku() {

		super.setbIgnoreZero(true);
		
		super.ClearPuzzleViolation();
		
		if (hasDuplicates())
			return false;

		if (!ContainsZero()) {
			super.AddPuzzleViolation(new PuzzleViolation(ePuzzleViolation.MissingZero, -1));
			return false;
		}
		return true;

	}

	/**
	 * isSudoku - return 'true' if...
	 * 
	 * Is a partialSudoku Each element doesn't a zero
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @return - returns 'true' if it's a partialSudoku, element match (row versus column) and no zeros
	 */
	public boolean isSudoku() {

		this.setbIgnoreZero(false);
		
		super.ClearPuzzleViolation();
		
		if (hasDuplicates())
			return false;
		
		if (!super.isLatinSquare())
			return false;
		
		for (int i = 1; i < super.getLatinSquare().length; i++) {

			if (!hasAllValues(getRow(0), getRegion(i))) {
				return false;
			}
		}

		if (ContainsZero()) {
			return false;
		}

		return true;
	}

	/**
	 * isValidValue - test to see if a given value would 'work' for a given column /
	 * row
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @param iCol
	 *            puzzle column
	 * @param iRow
	 *            puzzle row
	 * @param iValue
	 *            given value
	 * @return - returns 'true' if the proposed value is valid for the row and column
	 */
	public boolean isValidValue(int iCol, int iRow, int iValue) {
		
		if (doesElementExist(super.getRow(iRow),iValue))
		{
			return false;
		}
		if (doesElementExist(super.getColumn(iCol),iValue))
		{
			return false;
		}
		if (doesElementExist(this.getRegion(iCol, iRow),iValue))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * setRegion - Sets the values of the given region.
	 * 
	 * @param r
	 * 			integer representing region
	 */
	private void setRegion(int r) {

		int count = 1;

		int i = (r / iSqrtSize) * iSqrtSize;
		int j = (r % iSqrtSize) * iSqrtSize;		
		int jMax = j + iSqrtSize;
		int iMax = i + iSqrtSize;

		for (; i < iMax; i++) {
			for (j = (r % iSqrtSize) * iSqrtSize; j < jMax; j++) {
				this.getPuzzle()[i][j] = count++;
			}
		}
		//pointless comment for changes
		//updated
	}
	
	/**
	 * shuffleArray - Shuffles a given array.
	 * 
	 * @param arr
	 * 			One dimensional integer array
	 */
	private void shuffleArray(int[] arr) {
		
		ArrayList<Integer> arrL = new ArrayList<Integer>();
		
		for(int i=0; i<arr.length; i++) {
			arrL.add(arr[i]);
		}
		
		Collections.shuffle(arrL);
		Collections.shuffle(arrL);
		
		for(int i=0; i<arrL.size(); i++) {
			arr[i] = arrL.get(i);
		}
	}
	
	/**
	 * getRegionNbr - Return region number based on given column and row
	 * 
	 * @param iCol
	 * 			Given column number
	 * @param iRow
	 * 			Given row number
	 * @return - Returns region number based on given column and row
	 */
	public int getRegionNbr(int iCol, int iRow) {
		return (iCol / iSqrtSize) + ((iRow / iSqrtSize) * iSqrtSize);
	}
	
	/**
	 * shuffleRegion - Shuffles the values of a given region
	 * 
	 * @param r
	 * 		region number
	 */
	private void shuffleRegion(int r) {
		int[] reg = getRegion(r);
		shuffleArray(reg);
		
		int count = 0;
		int i = (r / iSqrtSize) * iSqrtSize;
		int j = (r % iSqrtSize) * iSqrtSize;		
		int jMax = j + iSqrtSize;
		int iMax = i + iSqrtSize;

		for (; i < iMax; i++) {
			for (j = (r % iSqrtSize) * iSqrtSize; j < jMax; j++) {
				this.getPuzzle()[i][j] = reg[count];
				count++;
			}
		}
	}
	
	/**
	 * FillDiagonalRegions - Set the diagonal regions with random values
	 */
	private void FillDiagonalRegions() {
		for(int i = 0; i < iSize; i += iSqrtSize) {
			int rNum = getRegionNbr(i,i);
			setRegion(rNum);
			shuffleRegion(rNum);
		}
	}
	
	/**
	 * PrintPuzzle - This method will print the puzzle to the console 
	 */
	public void PrintPuzzle() {
		int puzzle[][] = this.getPuzzle();
		for (int i = 0; i < puzzle.length; i++) {
			System.out.println("");
			for (int j = 0; j < puzzle.length; j++) {
				System.out.print(puzzle[i][j]);
				if ((j + 1) % iSqrtSize == 0)
					System.out.print(" ");
			}
			if ((i + 1) % iSqrtSize == 0)
				System.out.println(" ");
		}
	}
	
	/*
	 * getAllValidCellValues - gets iCol and iRow and returns a hashset containing
	 * all the valid values for that cell
	 * 
	 * @param iCol
	 * @param iRow
	 * 
	 * @return hsRange
	 */
	private HashSet<Integer> getAllValidCellValues(int iCol, int iRow) {
		HashSet<Integer> hsRange = new HashSet<Integer>();
		for (int i = 0; i < iSize; i++) {
			hsRange.add(i+1);
		}
		HashSet<Integer> hsUsedValues = new HashSet<Integer>();
		Collections.addAll(hsUsedValues, Arrays.stream(super.getRow(iRow)).boxed().toArray(Integer[]::new));
		Collections.addAll(hsUsedValues, Arrays.stream(super.getColumn(iCol)).boxed().toArray(Integer[]::new));
		Collections.addAll(hsUsedValues, Arrays.stream(this.getRegion(iCol, iRow)).boxed().toArray(Integer[]::new));
		hsRange.removeAll(hsUsedValues);
		return hsRange;
	}
	
	/*
	 * ShowAvailableValues - prints out all the available values
	 * 
	 */
	private void ShowAvailableValues() {
		for (int iRow = 0; iRow < iSize; iRow++) {
			for (int iCol = 0; iCol < iSize; iCol++) {
				Sudoku.Cell c = hash_map.get(Objects.hash(iRow, iCol));
				ArrayList<Integer> arrL = c.getLstValidValues();
				for (int i = 0; i < arrL.size(); i++) {
					System.out.print(" " + arrL.get(i) + " ");
				}
				System.out.println("");
			}
		}
		
	}
	
	/*
	 * fillRemaining - gets a cell and fills the remaining values
	 * 
	 * @param Cell c
	 * 
	 * @return boolean
	 */
	private boolean fillRemaing(Sudoku.Cell c) {
		if (c == null) {
			return true;
		}
		for (int num : c.getLstValidValues())
		{
			if (isValidValue(c.getiCol(),c.getiRow(), num)) {
				this.getPuzzle()[c.getiRow()][c.getiCol()] = num;
				if (fillRemaing(c.GetNextCell(c)))
					return true;
				this.getPuzzle()[c.getiRow()][c.getiCol()] = 0;
			}
		}
		return false;
	}
	
	/*
	 * setCells - creates and sets new instances of cells
	 * 
	 */
	private void setCells() {
		for (int iRow = 0; iRow < iSize; iRow++) {
			for (int iCol = 0; iCol < iSize; iCol++) {
				
				Sudoku.Cell c = new Cell(iRow, iCol);
				c.setlstValidValues(getAllValidCellValues(iCol, iRow));
				c.ShuffleValidValues();
			}
		}
	}
	
	private class Cell{
		private int iRow;
		private int iCol;
		private java.util.ArrayList<java.lang.Integer> lstValidValues;
		
		public Cell(int iRow, int iCol) {
			this.iRow = iRow;
			this.iCol = iCol;
		}
		
		/*
		 * getiRow - returns iRow
		 * 
		 * @return iRow
		 */
		public int getiRow() {
			return iRow;
		}
		
		/*
		 * getiCol - returns iCol
		 * 
		 * @return iCol
		 */
		public int getiCol() {
			return iCol;
		}
		
		//@Override
		public int hashCode() {
			return Objects.hash(iRow, iCol);
		}
		
		//@Override
		public boolean equals(java.lang.Object o) {
			if(o instanceof Cell) {
				Cell myCell = (Cell) o;
				if(myCell.iRow == this.iRow && myCell.iCol == this.iCol) {
					return true;
				}
			}
			else {
				return false;
			}
			return false;
		}
		
		public java.util.ArrayList<java.lang.Integer> getLstValidValues(){
			return lstValidValues;
		}
		
		/*
		 * setlstValidValues - gets a HashSet and implements it into
		 * the list of valid values
		 * 
		 * @param 
		 * 
		 * @return lstValidValues
		 */
		public void setlstValidValues(HashSet<Integer> hsValidValues) {

			lstValidValues = new ArrayList<Integer>(hsValidValues);

		}
		
		/* 
		 * ShuffleValidValues - shuffles the list of valid values
		 * 
		 */
		public void ShuffleValidValues() {
			Collections.shuffle(lstValidValues);
		}
		
		/*
		 * GetNextCell - get the next cell, return null if there isn't a next cell to
		 * find
		 * 
		 * @param c
		 * 
		 * @param iSize
		 * 
		 * @return Sudoku.Cell
		 */
		public Sudoku.Cell GetNextCell(Cell c){
			int row = c.getiRow();
			int col = c.getiCol();
			col++;
			if(col==iSize && row < iSize - 1) {
				row++;
				col = 0;
			}
			if(col == iSize && row == iSize) {
				return null;
			}
			if(row < iSqrtSize && col < iSqrtSize) {
				col = iSqrtSize;
			}
			else if(row < iSize - iSqrtSize && col < iSize - iSqrtSize) {
				if(col == (int)(row / iSqrtSize)* iSqrtSize) {
					col += iSqrtSize;
				}
			}
			else {
				if(col == iSize - iSqrtSize) {
					col = 0;
					row++;
					if(row >= iSize) {
						return null;
					}
				}
			}
			return hash_map.get(Objects.hash(row,col));
		}
	}
}
