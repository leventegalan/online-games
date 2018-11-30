package hu.lev.onlinegames;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import hu.lev.onlinegames.manager.SessionManager;
import hu.lev.onlinegames.model.MatchActive;
import hu.lev.onlinegames.model.Players;
import hu.lev.onlinegames.persist.MatchDao;
import hu.lev.onlinegames.persist.MatchDaoImpl;

public class App {
	public static void main(String[] args) {
		

		
	}
}






// user insert test
//User u = new User("lék", "titok", "áél@vmi.huzat");
//
//Configuration con = new Configuration()
//		.configure()
//		.addAnnotatedClass(User.class);
//ServiceRegistry reg = new StandardServiceRegistryBuilder().applySettings(con.getProperties()).build();
//SessionFactory sf = con.buildSessionFactory(reg);
//Session session = sf.openSession();
//Transaction tx = session.beginTransaction();
//
//int id = (int) session.save(u);
//System.out.println("id: " + id);
//
//tx.commit();
//session.close();

//Trying to save user to DB, not worked in app mode
//AuthService authService = new AuthServiceImpl();
//UserService userService = new UserServiceImpl();
//
//RegisterRq req = new RegisterRq();
//req.setEmail("x@y");
//req.setPassword("qwe");
//req.setPasswordConfirmed("qwe");
//req.setUsername("qwe");
//
//boolean success = false;
//
//if(req.getUsername() != null
//		&& req.getPassword() != null
//		&& req.getPasswordConfirmed() != null
//		&& req.getPassword().equals(req.getPasswordConfirmed())
//		&& userService.validateEmail(req.getEmail())){
//	
//	String password = authService.getHash(req.getPassword()); // get hashcode of password
//	User user = new User(
//			req.getUsername(),
//			password,
//			req.getEmail());
//	int id = userService.registerUser(user);
//	
//	if(id > 0) {
//		success = true;
//	}
//}

//save new match
//GameType[] gameTypes = null;
//try {
//	Configuration con = new Configuration()
//			.configure()
//			.addAnnotatedClass(GameType.class)
//			.addAnnotatedClass(GameTypeOption.class);
//	ServiceRegistry reg = new StandardServiceRegistryBuilder()
//			.applySettings(con.getProperties())
//			.build();
//
//	System.out.println("alma1");
//	
//	SessionFactory sf = con.buildSessionFactory(reg);
//	
//	System.out.println("alma2");
//
//	Session session = sf.openSession();
//	Transaction tx = session.beginTransaction();
//				
//		System.out.println("alma3");
//		Query q = session.createQuery("from GameType");
//		System.out.println("eper");
//		
//		List<Object> objects = q.list();			
//		gameTypes = new GameType[objects.size()];
//		objects.toArray(gameTypes);
//	
//	tx.commit();
//	session.close();
//	
//	System.out.println("dinnye");
//	for(GameType g : gameTypes) {
//		System.out.println(g.toString());
//	}
//	System.out.println(gameTypes[0].getOptions().size());
//				
//} catch (Exception e) {
//	e.printStackTrace();
//}

//insert new match waiting
//int[] o = {1,2,3};
//NewMatchRq req = new NewMatchRq(1, "test", 1, o);
//
//
//
//try {
//	Configuration con = new Configuration()
//			.configure()
//			.addAnnotatedClass(GameType.class)
//			.addAnnotatedClass(GameTypeOption.class)
//			.addAnnotatedClass(User.class)
//			.addAnnotatedClass(NewMatch.class);
//	ServiceRegistry reg = new StandardServiceRegistryBuilder()
//			.applySettings(con.getProperties())
//			.build();
//	System.out.println("alma1");			
//	SessionFactory sf = con.buildSessionFactory(reg);	
//	System.out.println("alma2");
//	Session session = sf.openSession();
//	Transaction tx = session.beginTransaction();						
//		System.out.println("alma3");
//		
//		GameType gameType = session.load(GameType.class, req.getGameTypeId());
//		User user = session.load(User.class, req.getUserid());
//		String ops = Arrays.toString(req.getOptions());
//		
//		NewMatch match = new NewMatch(user, gameType, ops);
//		
//		System.out.println("instert_start");
//		int id = (int) session.save(match);
//		System.out.println("insert_done");
//		System.out.println("new_match_id: " + id);
//		
//		System.out.println("eper");
//	tx.commit();
//	session.close();			
//	System.out.println("dinnye");
//
//				
//} catch (Exception e) {
//	e.printStackTrace();
//}

//get waiting matches
// DONT FORGET TO SET TYPES AND NAMES as newMatch became matchWaiting
//NewMatch[] newMatches = null;
//
//try {
//	Configuration con = new Configuration()
//			.configure()
//			.addAnnotatedClass(GameType.class)
//			.addAnnotatedClass(GameTypeOption.class)
//			.addAnnotatedClass(User.class)
//			.addAnnotatedClass(NewMatch.class);
//	ServiceRegistry reg = new StandardServiceRegistryBuilder()
//			.applySettings(con.getProperties())
//			.build();			
//	SessionFactory sf = con.buildSessionFactory(reg);
//
//	Session session = sf.openSession();
//	Transaction tx = session.beginTransaction();
//				
//		Query q = session.createQuery("from NewMatch");
//		
//		List<Object> objects = q.list();	
//		System.out.println("result size: " + objects.size());
//		newMatches = new NewMatch[objects.size()];
//		objects.toArray(newMatches);
//	
//	tx.commit();
//	session.close();
//	
//	for (NewMatch newMatch : newMatches) {
//		System.out.println(newMatch.toString());
//	}
//				
//} catch (Exception e) {
//	e.printStackTrace();
//}

//public int createMatchActive(int acceptingUserId, MatchWaiting match);
//MatchDao dao = new MatchDaoImpl();
//
//int matchId = 0;
//int acceptingUserId = 1;
//MatchWaiting matchWaiting = dao.getMatchWaiting(1);
//
//Random rand = new Random();
//int mixer = rand.nextInt(2) + 1;
//SessionManager sm = new SessionManager();
//
//try {
//	Session session = sm.getSession();
//	Transaction tx = session.beginTransaction();
//	System.out.println("alma");
//
//	MatchActive match = new MatchActive();
//	match.setGameType(matchWaiting.getGameTypeId());
//	match.setOptions(matchWaiting.getOptions());
//	match.setTurn(1);
//	
//	matchId = (int) session.save(match);
//
//	System.out.println("körte");
//	
//	Players players = new Players();
//
//	User acceptingUser = session.load(User.class, acceptingUserId);
//	User challengingUser = session.load(User.class, matchWaiting.getUser().getId());
//	
//	if(mixer == 1) {
//		players.setPlayer1(acceptingUser);
//		players.setPlayer2(challengingUser);
//	} else {
//		players.setPlayer1(challengingUser);
//		players.setPlayer2(acceptingUser);
//	}
//	players.setActivePlayer(1);
//	players.setMatchId(session.get(MatchActive.class, matchId));
//	
//	session.save(players);
//	tx.commit();
//	System.out.println("dinnye");
//	session.close();
//	
//} catch (Exception e) {
//	matchId = 0;
//	e.printStackTrace();
//}
