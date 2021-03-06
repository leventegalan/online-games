package hu.lev.onlinegames.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckersController {

    // USER ACTION
    @RequestMapping(value = "/checkers/action", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> action() {
    	return null;
	}

    // CHECK ACTION
    @RequestMapping(value = "/checkers/checkaction", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> checkAction() {
    	return null;
	}

    // TIMEOUT
    @RequestMapping(value = "/checkers/timeout", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> timeOut() {
    	return null;
	}

    // GIVE UP
    @RequestMapping(value = "/checkers/giveup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> giveUp() {
    	return null;
	}
}
