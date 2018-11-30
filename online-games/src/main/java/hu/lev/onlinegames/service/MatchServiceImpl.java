package hu.lev.onlinegames.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.lev.onlinegames.model.GameType;
import hu.lev.onlinegames.model.MatchActive;
import hu.lev.onlinegames.model.MatchDone;
import hu.lev.onlinegames.model.MatchWaiting;
import hu.lev.onlinegames.model.User;
import hu.lev.onlinegames.model.request.MatchStartRq;
import hu.lev.onlinegames.model.request.MatchWaitingRq;
import hu.lev.onlinegames.model.response.MatchWaitingResp;
import hu.lev.onlinegames.persist.MatchDao;

@Service
public class MatchServiceImpl implements MatchService {
	
	@Autowired
	private MatchDao matchDao;
	
	@Autowired
	private FiveInARowService fiveInARowService;
	
	@Autowired
	private ConverterService converterService;
	
	public MatchServiceImpl() {
		super();
	}

	@Override
	public GameType[] getGameTypes() {
		 return matchDao.getGameTypes();
	}

	@Override
	public int createNewMatch(MatchWaitingRq req) {
		return matchDao.insertMatchWaiting(req);
	}

	
	@Override
	public MatchWaitingResp[] getMatchesWaiting() {
		MatchWaiting[] matchesRaw = matchDao.getMatchesWaiting();
		MatchWaitingResp[] matches = new MatchWaitingResp[matchesRaw.length];
		
		for (int i = 0; i < matchesRaw.length; i++) {
			matches[i] = matchesRaw[i].toMatchWaitingResp();
		}
		
		return matches;
	}

	@Override
	public boolean deleteMatchWaiting(int id) {
		return matchDao.deleteMatchWaiting(id);
	}

	@Override
	public MatchActive startMatch(MatchStartRq req) {		
		MatchActive matchActive = null;
		MatchWaiting matchWaiting = matchDao.getMatchWaiting(req.getMatchId());
		int [] options = converterService.stringToIntArray(matchWaiting.getOptions());
		
		String initFields = initFields(
				matchWaiting.getGameTypeId().getGameTypeId(), options);
		if(matchWaiting != null) {
			int id = matchDao.createAndInsertMatchActive(req.getUserid(), matchWaiting, initFields);
			if(id > 0) {
				matchActive = matchDao.getMatchActive(id);
				matchDao.deleteMatchWaiting(req.getMatchId());
			}
		}		
		return matchActive;
	}

	@Override
	public MatchActive checkStart(int userId) {
		MatchActive match = null;
		int matchId = matchDao.getMatchActiveId(userId);
		if(matchDao.isWinner(matchId)){
			matchDao.deleteMatchActive(matchId);
		} else {
			match = matchDao.getMatchActive(matchId);
		}
		
		return match;
	}

	@Override
	public void updateMatchActive(MatchActive match) {
		matchDao.updateMatchActive(match);
		
	}

	private String initFields(int gameTypeId, int[] options) {
		String fields = "";
		switch (gameTypeId) {
		case 1:
			fields = fiveInARowService.initFields(options);
			break;

		default:
			break;
		}
		return fields;
	}

	
	@Override
	public void saveStats(MatchActive matchActive) {
		boolean player1Win = false;
		if(matchActive.getWin() == 1) {
			player1Win = true;
		}
		
		User player1 = matchActive.getPlayers().getPlayer1();
		User player2 = matchActive.getPlayers().getPlayer2();
		GameType gameType = matchActive.getGameType();

		// stats for player 1			
		MatchDone matchDone = matchDao.getMatchDone(gameType, player1);
		if(matchDone == null) {
			matchDone = matchDao.createMatchDone(gameType, player1, player1Win);
			matchDao.insertMatchDone(matchDone);
		} else {
			matchDao.updateMatchDone(matchDone, player1Win);
		}
			
		// stats for player 2
		matchDone = matchDao.getMatchDone(gameType, player2);
		if(matchDone == null) {
			matchDone = matchDao.createMatchDone(gameType, player2, !player1Win);
			matchDao.insertMatchDone(matchDone);
		} else {
			matchDao.updateMatchDone(matchDone, !player1Win);
		}
			
	}
}
