package hu.lev.onlinegames.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.lev.onlinegames.model.MatchActive;
import hu.lev.onlinegames.persist.MatchDao;

@Service
public class MatchServiceImpl implements MatchService {
	
	@Autowired
	private MatchDao matchDao;
	
	public MatchServiceImpl() {
		super();
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

}
