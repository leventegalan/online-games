package hu.lev.onlinegames.persist;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hu.lev.onlinegames.manager.SessionManager;
import hu.lev.onlinegames.model.MatchActive;
import hu.lev.onlinegames.model.Players;
import hu.lev.onlinegames.model.User;

@Repository
public class MatchDaoImpl implements MatchDao {

	@Autowired
	SessionManager sm;
	
	
	public MatchDaoImpl() {
		super();
	}

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
