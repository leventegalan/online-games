package hu.lev.onlinegames.service;

import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.lev.onlinegames.model.MatchActive;
import hu.lev.onlinegames.model.fiveinarow.FiveInARowAction;
import hu.lev.onlinegames.model.fiveinarow.FiveInARowField;
import hu.lev.onlinegames.model.request.MatchActiveRq;
import hu.lev.onlinegames.persist.MatchDao;

@Service
public class FiveInARowServiceImpl implements FiveInARowService {
		
	@Autowired
	private MatchDao matchDao;

	@Override
	public boolean validateAction(FiveInARowAction action, FiveInARowField[][] fields, int[] options) {
		if(fields[action.getX()][action.getY()].getValue() == 0) {
			return true;
		}
		if(fields[action.getX()][action.getY()].getValue() == 4
				&& (ArrayUtils.contains(options, 1)	|| ArrayUtils.contains(options, 3))) {
			return true;
		}
		return false;
	}
	
	@Override
	public MatchActiveRq applyAction(MatchActiveRq matchRq) {
		// System.out.println(
		// 		"INDEXEK: " + matchRq.getAction().getX() + ", " + matchRq.getAction().getY()
		// 		+ " ÉRTÉK: " + matchRq.getAction().getValue()
		// 		);
		int x = matchRq.getAction().getX();
		int y = matchRq.getAction().getY();
		int originalValue = matchRq.getAction().getValue();
		int newValue = 0;
		
		switch (originalValue) {
		case 0:
			newValue = matchRq.getActivePlayer();
			break;
		case 4:
			newValue = 0;
			break;
		default:
			break;
		}
		
		matchRq.getFields()[x][y].setValue(newValue);
		// System.out.println("ÚJ ÉRTÉK: " + matchRq.getFields()[matchRq.getAction().getX()][matchRq.getAction().getY()].getValue());
		
		return matchRq;
	}
	

	@Override
	public boolean checkWin(FiveInARowField[][] fields, int player, FiveInARowAction action) {
//		FiveInARowField[][] fields = fieldsObj.getFields();
		boolean win = false;
		
		int startX = action.getX() - 4;			// init min and max indexes, so we check fields in board
		int startY = action.getY() - 4;
		int endX = action.getX() + 4;
		int endY = action.getY() + 4;
		
//		System.out.println("startX: " + startX);
//		System.out.println("startY: " + startY);
//		System.out.println("endX: " + endX);
//		System.out.println("endY: " + endY);
		
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
	

	@Override
	public MatchActive convertMatchRq(MatchActiveRq matchRq) {
//		System.out.println(matchRq.getMatchId());
		MatchActive match = matchDao.getMatchActive(matchRq.getMatchId());
//		System.out.println(match);

		String boardstate = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			boardstate = mapper.writeValueAsString(matchRq.getFields());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		};
		match.setBoardstate(boardstate);
		match.setWin(matchRq.getWin());
		match.setAction(matchRq.getAction().toString());
		
		return match;
	}

	
	@Override
	public String initFields(int[] options) {
		FiveInARowField[][] fields = new FiveInARowField[25][25];
		for (int i = 0; i < fields.length; i++) {
			fields[i] = new FiveInARowField[25];
			for (int j = 0; j < fields[i].length; j++) {
				fields[i][j] = new FiveInARowField();
			}
		}
		
		if (ArrayUtils.contains(options, 1)){
			fields = initTraps(fields);
		}
		if (ArrayUtils.contains(options, 2)){
			fields = initWalls(fields);
		}
		String boardstate = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			boardstate = mapper.writeValueAsString(fields);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		};
		
		return boardstate;
	}
	
	
	private FiveInARowField[][] initTraps(FiveInARowField[][] fields){
		Random rand = new Random();
		int numOfTraps = 50;
		int i = 0;
		int x;
		int y;

		while (i < numOfTraps) {
			x = rand.nextInt(25);
			y = rand.nextInt(25);
			if(fields[x][y].getValue() != 3 && fields[x][y].getValue() != 4) {
				fields[x][y].setValue(4);
				i++;
			}
		}		
		return fields;
	}
	
	
	private FiveInARowField[][] initWalls(FiveInARowField[][] fields){
		Random rand = new Random();
		int numOfWalls = 50;
		int i = 0;
		int x;
		int y;
		
		while (i < numOfWalls) {
			x = rand.nextInt(25);
			y = rand.nextInt(25);
			if(fields[x][y].getValue() != 3 && fields[x][y].getValue() != 4) {
				fields[x][y].setValue(3);
				i++;
			}
		}
		return fields;
	}
	

	@Override
	public MatchActive checkAction(int matchId, int turn) {
		MatchActive match = null;
		if(matchDao.checkAction(matchId, turn)) {
			match =  matchDao.getMatchActive(matchId);
		}
		return match;
	}
}









