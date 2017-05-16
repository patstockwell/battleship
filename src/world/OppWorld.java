package world;

import java.util.ArrayList;
import world.World.Coordinate;

/* 
 * Cams Class to store the state of the opponents world
 */

public class OppWorld {
	
	private World world = new World();
	
	enum cellState { Untested, Possible, Hit, Miss }; 
	/* 
	 * NOTE although there is a cell state for Possible, this is used to simplify
	 *   the checking process. The list below is to allow quick access without 
	 *   searching through the 2D array for every guess
	 */
	public ArrayList<Coordinate> possibleTargets = new ArrayList<>();
	
	// 2d Array representing the opponents world
	cellState[][] oppWorld = null; 
	
	// Private copy of the world boundaries
	private int numRows;
	private int numColumns;
	
	// Constructor Class
	public OppWorld( int numRow, int numColumn )
	{
		oppWorld = new cellState[numRow][numColumn];
		
		// Initialize ALL cells to untested
		for ( int j = 0; j < numRows; j++ )
		{
			for ( int i = 0; i < numColumns; i++ )
				oppWorld[j][i] = cellState.Untested;
		}
	}

	// Called from the player class, to update the state of the opponents world
	public void updateCell ( cellState state, int row, int column )
	{
		oppWorld[row][column] = state;
		
		// If a hit was detected, we need to update the surrounding cells to 
		//   check for possible ships
		if ( state == cellState.Hit )
			calculatePossibles( row, column );	
	}
	
	// Checks the state of the cells above, below, left and right of the target cell
	private void calculatePossibles( int row, int column )
	{
		int temp;
		
		// Check Up
		temp = row - 1;
		if ( temp >= 0 )
			checkCellUntested( temp, column );
		
		// Check Down
		temp = row + 1;
		if ( temp < numRows )
			checkCellUntested( temp, column );
		
		// Check Right
		temp = column + 1;
		if ( temp < numColumns )
			checkCellUntested( row, temp );
		
		// Check Left
		temp = column + 1;
		if ( temp >= 0 )
			checkCellUntested( row, temp );	
	}
	
	private void checkCellUntested( int row, int column )
	{
		switch( oppWorld[row][column] )
		{
			// If the cell is untested, it needs to become a "possible"
			case Untested:
			{
				Coordinate temp = world.new Coordinate();
				temp.row = row;
				temp.column = column;
				
				possibleTargets.add(temp);
			}
			// If the cell is already marked as hit or miss or possible, then it is ignored
			default: 
				return;
		}
	}
}