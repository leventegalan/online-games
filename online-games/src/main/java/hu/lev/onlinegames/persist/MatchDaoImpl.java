package hu.lev.onlinegames.persist;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.persistence.PersistenceException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hu.lev.onlinegames.manager.SessionManager;
import hu.lev.onlinegames.model.GameType;
import hu.lev.onlinegames.model.MatchActive;
import hu.lev.onlinegames.model.MatchWaiting;
import hu.lev.onlinegames.model.Players;
import hu.lev.onlinegames.model.User;
import hu.lev.onlinegames.model.request.MatchWaitingRq;

@Repository
public class MatchDaoImpl implements MatchDao {

	@Autowired
	SessionManager sm;
	
	
	public MatchDaoImpl() {
		super();
	}

	@Override
	public GameType[] getGameTypes() {

		GameType[] gameTypes = null;
		
		try {
			Session session = sm.getNewSession();
			Transaction tx = session.beginTransaction();
						
				Query q = session.createQuery("from GameType");
				
				List<Object> objects = q.list();			
				gameTypes = new GameType[objects.size()];
				objects.toArray(gameTypes);
			
			tx.commit();
			// session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gameTypes;
	}

	@Override
	public int insertMatchWaiting(MatchWaitingRq req) {
		int id = 0;

		try {
			Session session = sm.getNewSession();
			Transaction tx = session.beginTransaction();	
				
				GameType gameType = session.load(GameType.class, req.getGameTypeId());
				User user = session.load(User.class, req.getUserid());
				String ops = Arrays.toString(req.getOptions());
				
				MatchWaiting match = new MatchWaiting(user, gameType, ops);
				
				id = (int) session.save(match);
			tx.commit();
			 session.close();
		} catch (PersistenceException e) {
			id = -1;
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return id;
	}

	@Override
	public MatchWaiting[] getMatchesWaiting() {
		MatchWaiting[] matchesWaiting = null;
		
		try {
			Session session = sm.getNewSession();
			Transaction tx = session.beginTransaction();
						
				Query q = session.createQuery("from MatchWaiting");
				
				List<Object> objects = q.list();	
				matchesWaiting = new MatchWaiting[objects.size()];
				objects.toArray(matchesWaiting);
			
			tx.commit();
			 session.close();
									
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matchesWaiting;
	}

	@Override
	public boolean deleteMatchWaiting(int id) {
		boolean success = false;
		
		try {
			Session session = sm.getNewSession();
			Transaction tx = session.beginTransaction();
				
			MatchWaiting match = new MatchWaiting();
			match.setId(id);
			session.remove(match);
			
			tx.commit();
			 session.close();
			
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	@Override
	public MatchWaiting getMatchWaiting(int matchId) {
		
		MatchWaiting match = null;
		
		try {
			Session session = sm.getNewSession();
			Transaction tx = session.beginTransaction();
			
			match = session.get(MatchWaiting.class, matchId);
			
			tx.commit();
			 session.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
		return match;
	}
	
	@Override
	public int createMatchActive(int acceptingUserId, MatchWaiting matchWaiting, String fields) {

		int matchId = 0;
		Random rand = new Random();
		int mixer = rand.nextInt(2) + 1;

		System.out.println("");
		System.out.println("DAO");
		try {
			Session session = sm.getNewSession();
			Transaction tx = session.beginTransaction();

			MatchActive match = new MatchActive();
			match.setGameType(matchWaiting.getGameTypeId());
			match.setOptions(matchWaiting.getOptions());
			match.setBoardstate(fields);
			match.setTurn(1);
			
			matchId = (int) session.save(match);

			System.out.println("new matchActive id: " + matchId);
			tx.commit();
			tx = session.beginTransaction();
			Players players = new Players();

			User acceptingUser = session.load(User.class, acceptingUserId);
			User challengingUser = session.load(User.class, matchWaiting.getUser().getId());
			
			if(mixer == 1) {
				players.setPlayer1(acceptingUser);
				players.setPlayer2(challengingUser);
			} else {
				players.setPlayer1(challengingUser);
				players.setPlayer2(acceptingUser);
			}
			players.setActivePlayer(1);
			players.setMatchId(session.get(MatchActive.class, matchId));
			
			session.save(players);
			
			tx.commit();
			 session.close();
			
		} catch (Exception e) {
			matchId = 0;
			e.printStackTrace();
		}
		
		return matchId;
	}

	/*
	@Override
	public MatchActive checkStart(int userId) {
		MatchActive match = null;
		
		try {
			Session session = sm.getNewSession();
			Transaction tx = session.beginTransaction();

			Query q = session.createSQLQuery("select * from match_players where player1_fk = :a or player2_fk = :a");
			q.setParameter("a", userId);
			Object[] result = (Object[]) q.uniqueResult();
			
			if(result != null) {
				match = getMatchActive((int)result[3]);
				
				q = session.createSQLQuery("select * from match_active where id = :a and win > 0");
				q.setParameter("a", match);
				result = (Object[]) q.uniqueResult();
				
				if(result != null) {
					deleteMatchActive((int)result[6]);
				}
			}
			
			tx.commit();
			 session.close();
			
		} catch (Exception e) {
			match = null;
			e.printStackTrace();
		}

		return match;
	}
*/
	
	@Override
	public MatchActive getMatchActive(int matchId) {

		MatchActive match = null;
		
		Transaction tx = null;
		try {
			Session session = sm.getNewSession();
			tx = session.beginTransaction();
			
			match = session.get(MatchActive.class, matchId);
			
			tx.commit();
			 session.close();
			
		} catch (Exception e) {
			match = null;
			e.printStackTrace();
			tx.rollback();
		}
		return match;
	}

	@Override
	public int getMatchActiveId(int userId) {
		
		int matchId = 0;
		
		try {
			Session session = sm.getNewSession();
			Transaction tx = session.beginTransaction();

			Query q = session.createSQLQuery("select * from match_players where player1_fk = :a or player2_fk = :a");
			q.setParameter("a", userId);
			Object[] result = (Object[]) q.uniqueResult();
			
			if(result != null) {
				matchId = (int) result[3];
			}
			
			tx.commit();
			 session.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matchId;
	}

	@Override
	public void updateMatchActive(MatchActive match) {
				
		try {
			Session session = sm.getNewSession();
			Transaction tx = session.beginTransaction();

			session.update(match);
			session.update(match.getPlayers());
			
			tx.commit();
			 session.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkAction(int matchId, int turn) {

		boolean isAction = false;
		
		try {
			Session session = sm.getNewSession();
			Transaction tx = session.beginTransaction();

			Query q = session.createSQLQuery("select * from match_active where id = :a and (turn > :b || win > 0)");
			q.setParameter("a", matchId);
			q.setParameter("b", turn);
			Object[] result = (Object[]) q.uniqueResult();
						
			if(result != null) {
				isAction = true;
			}
			
			tx.commit();
			 session.close();
			
		} catch (Exception e) {
			isAction = false;
			e.printStackTrace();
		}

		return isAction;
	}

	
	@Override
	public boolean deleteMatchActive(int id) {
		boolean success = false;
		
		try {
			Session session = sm.getNewSession();
			Transaction tx = session.beginTransaction();
			
			Players players = new Players();
			Query q = session.createSQLQuery("delete from match_players where match_fk = :a");
			q.setParameter("a", id);
			q.executeUpdate();
			
			MatchActive match = new MatchActive();
			match.setId(id);
			session.remove(match);
			
			tx.commit();
			 session.close();
			
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	@Override
	public boolean isWinner(int matchId) {
		boolean isWinner = false;
		try {
			Session session = sm.getNewSession();
			Transaction tx = session.beginTransaction();
				
			Query q = session.createSQLQuery("select * from match_active where id = :a and win > 0");
			q.setParameter("a", matchId);
			Object[] result = (Object[]) q.uniqueResult();
			
			if(result != null) {
				isWinner = true;
				System.out.println("");
				System.out.println("VAN NYERTES");
			}
			tx.commit();
			session.close();
			
		} catch (Exception e) {
			isWinner = false;
			e.printStackTrace();
		}
		
		return isWinner;
	}
}
