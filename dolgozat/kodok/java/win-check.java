@Override
	public boolean checkWin(FiveInARowField[][] fields, int player, FiveInARowAction action) {
		
		boolean win = false;
		
		int startX = action.getX() - 4;			// init min and max indexes, so we check fields in board
		int startY = action.getY() - 4;
		int endX = action.getX() + 4;
		int endY = action.getY() + 4;
		
		// diagonals first, there is a bigger chance to win this way, save some energy
		// diagonal from top-left
		int lengthSoFar = 0;
		for(int i = startX, j = startY; i<=endX && j<=endY; i++){
			if (i >= 0 && i < fields.length && j >= 0 && j < fields[0].length){
				if (fields[i][j].getValue() == player){
					lengthSoFar++;
				} else if (lengthSoFar < 5){
					lengthSoFar = 0;
				}
			}
			j++;
		}
		if (lengthSoFar >= 5){ win = true; }

		// diagolal from bottom-left
		lengthSoFar = 0;
		for(int i = startX, j = endY; i<=endX && j>=startY; i++){
			if (i >= 0 && i < fields.length && j >= 0 && j < fields[0].length){
				if (fields[i][j].getValue() == player){
					lengthSoFar++;
				} else if (lengthSoFar < 5){
					lengthSoFar = 0;
				}
			}
			j--;
		}
		if (lengthSoFar >= 5){ win = true; }

		// vertical
		lengthSoFar = 0;
		for(int j=startY; j<=endY ; j++){
			if (j >= 0 && j < fields[0].length){
				if (fields[action.getX()][j].getValue() == player){
					lengthSoFar++;
				} else if (lengthSoFar < 5){
					lengthSoFar = 0;
				}
			}
		}
		if (lengthSoFar >= 5){ win = true; }

		// horizontal
		lengthSoFar = 0;
		for(int i=startX; i<=endX ; i++){
			if (i >= 0 && i < fields.length){
				if (fields[i][action.getY()].getValue() == player){
					lengthSoFar++;
				} else if (lengthSoFar < 5){
					lengthSoFar = 0;
				}
			}
		}
		if (lengthSoFar >= 5){ win = true; }

		// no winning found
		return win;
	}
	