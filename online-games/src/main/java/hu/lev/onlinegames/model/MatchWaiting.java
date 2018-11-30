package hu.lev.onlinegames.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hu.lev.onlinegames.model.response.MatchWaitingResp;

@Entity
@Table(name = "match_waiting")
public class MatchWaiting {
	
	@Id
	@GeneratedValue(generator="increment")
	@Column(name = "id")
	private int id;	
	
	@ManyToOne
	@JoinColumn(name="player_fk")
//    @JsonIgnore
	private User user;

	@ManyToOne
	@JoinColumn(name="game_type_fk")
//    @JsonIgnore
	private GameType gameType;

	@Column(name = "options")
	private String options;
	

	// extra functions
	public MatchWaitingResp toMatchWaitingResp() {
		MatchWaitingResp match = new MatchWaitingResp();
		match.setId(id);
		match.setUserid(user.getId());
		match.setUsername(user.getUsername());
		match.setGameTypeId(gameType.getGameTypeId());
		match.setOptions(options);
		
		return match;
	}
	
	
	// constructors
	public MatchWaiting() {
		super();
	}
	public MatchWaiting(int id, User userid, GameType gameType, String options) {
		super();
		this.id = id;
		this.user = userid;
		this.gameType = gameType;
		this.options = options;
	}

	public MatchWaiting(User userid, GameType gameType, String options) {
		super();
		this.user = userid;
		this.gameType = gameType;
		this.options = options;
	}
	
	// toString
	@Override
	public String toString() {
		return "MatchWaiting [id=" + id 
				+ ", userid=" + user 
				+ ", gameType=" + gameType.getGameTypeName() 
				+ ", options=" + options
				+ "]";
	}
	
	// getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public GameType getGameTypeId() {
		return gameType;
	}
	public void setGameTypeId(GameType gameType) {
		this.gameType = gameType;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	
}
