package hu.lev.onlinegames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hu.lev.onlinegames.model.GameType;
import hu.lev.onlinegames.model.MatchActive;
import hu.lev.onlinegames.model.request.MatchStartRq;
import hu.lev.onlinegames.model.request.MatchWaitingRq;
import hu.lev.onlinegames.model.response.MatchWaitingResp;
import hu.lev.onlinegames.service.MatchService;

@RestController
public class MatchController {
	
	@Autowired
	private MatchService matchService;

    // GET MATCH LIST
    @RequestMapping(
    		value = "/match", 
    		method = RequestMethod.GET,
			produces = {"application/json"})
	@ResponseBody
	public MatchWaitingResp[] getList() {
    	MatchWaitingResp[] matches = matchService.getMatchesWaiting();
    	return matches;
	}

    // CREATE MATCH WAITING
    @RequestMapping(
    		value = "/match", 
    		method = RequestMethod.POST,
			produces = {"application/json"})
	@ResponseBody
	public int createNewMatch(@RequestBody MatchWaitingRq req) {    
    	int success = 0;
    	
    	// validate
    	if(req.getUserid() > 0
    			&& req.getUsername() != null
    			&& req.getUsername() != ""
    			&& req.getGameTypeId() > 0) {
    		if(req.getOptions() == null) {
    			req.setOptions(new int[] {});
    		}
    		
    		// insert
    		int id = matchService.createNewMatch(req);
    		
        	// check success
    		if(id > 0) {
    			success = 1;
    		}
    	}
    	return success;
	}

    // DELETE MATCH
    @RequestMapping(value = "/match/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public boolean delete(@PathVariable int id) {
		boolean success = false;
		
    	if(id > 0) {
        	success = matchService.deleteMatchWaiting(id);
    	}
    	return success;
	}

    // ACCEPT AND START NEW MATCH
    @RequestMapping(value = "/match/start", method = RequestMethod.POST)
	@ResponseBody
	public MatchActive start(@RequestBody MatchStartRq req) {    	
    	MatchActive match = matchService.startMatch(req);
    	return match;
	}

    // CHECK ACTION HAPPENING
	@RequestMapping(value = "/match/start/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public MatchActive checkStart(@PathVariable int userId) {
		MatchActive match = matchService.checkStart(userId);
		return match;
	}

	// GET GAMETYPE LIST
	@RequestMapping(
			value = "/gametypes", 
			method = RequestMethod.GET,
			produces = {"application/json"})
	@ResponseBody
	public GameType[] getGameTypes() {
		GameType[] gameTypes = matchService.getGameTypes();
		return gameTypes;
	}
}
