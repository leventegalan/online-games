package hu.lev.onlinegames.controller;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hu.lev.onlinegames.model.MatchActive;
import hu.lev.onlinegames.service.MatchService;

@RestController
public class MatchController {
	
	@Autowired
	private MatchService matchService;


    // CHECK START
	@RequestMapping(value = "/match/start/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public MatchActive checkStart(@PathVariable int userId) {
		MatchActive match = matchService.checkStart(userId);
		return match;
	}

}
