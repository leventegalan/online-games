// Add event listener for 'click' events.
elem.addEventListener('click', function(event) {
	if(!vm.disable){
		vm.disable = true;

		var x = event.pageX - elem.offsetLeft;	// get canvas x,	elem.offsetLeft - canvas origo x
		var y = event.pageY - elem.offsetTop;	// get canvas y		elem.offsetTop - canvas origo y
		var field = man.getClickedField(vm.board, x, y);						// get clicked field by index

		if(field != null){
			field.value = match.fields[field.x][field.y].value;						// get value of field
			// console.log(field.x + "," + field.y + ' - ' + field.value);

			if([0,4].includes(field.value)){
				match.action = field;
				var data = man.getMatchReadyToSend(match);

				$http.post(baseUrl + '/fiveinarow/action', data)
				.then(function(result){
					var field = match.fields[match.action.x][match.action.y];
					
					if(match.action.value == 4){
						man.activateTrap(field, vm.board, ctx);
						field.value = 0;
					}

					if(match.action.value == 0){
						man.drawCharacter(field, vm.board, match.players.activePlayer, ctx);
					}

					if(result.data){
						checkAction();
					} else {
						vm.disable = false;
					}
				});
			}else {
				vm.disable = false;
			}
			
		}
	}
}, false);

function checkAction(){
	vm.promise = $interval(function(){
		// console.log(baseUrl + '/fiveinarow/checkaction');
		$http.get(baseUrl + '/fiveinarow/checkaction', {
			params: {
				matchId: match.id,
				turn: match.turn + 1
			}
		})
		.then(function(result){
			if(result.data != null && result.data != ""){
				vm.initMatch = result.data;
				match = man.reload(vm.initMatch, match, vm.board, ctx);
				
				if (match.players.activePlayer == 1) {
					vm.disable = $localStorage.currentUser.userid == match.players.player1.id ? false : true;
				} else {
					vm.disable = $localStorage.currentUser.userid == match.players.player2.id ? false : true;
				}

				stopcheckAction();
			}
		});
	}, 1000, false);
}

function stopcheckAction(){
	// console.log("checkAction OFF");
	$interval.cancel(vm.promise);
}

function initiateMatch(initMatch, match, board, ctx){

	if(typeof initMatch.options == "string"){
		// convert string to int array
		if(initMatch.options != null){
			var op = initMatch.options;
			op = op.substring(1, op.length-1);
			op = op.split(',').map(function(item) {
				return parseInt(item, 10);
			});
			initMatch.options = op;
		}
	}
	
	var tempFields = resetFields(board, 0);
	initMatch.fields = JSON.parse(initMatch.boardstate);
	
	for (var i = 0; i < initMatch.fields.length; i++) {
		for (var j = 0; j < initMatch.fields[i].length; j++) {
			tempFields[i][j].value = initMatch.fields[i][j].value;
		}
	}

	initMatch.fields = tempFields;
	match = initMatch;

	drawBoard(board, ctx);
	drawFields(match, board, ctx);

	return match;
}

function reload(initMatch, match, board, ctx){
	initMatch.fields = initMatch.boardstate == "" ? null : JSON.parse(initMatch.boardstate);
	initMatch.action = JSON.parse(initMatch.action);
	match = initMatch;
	// console.log("WIN: " + match.win);

	initMatch.fields.forEach(function(fieldsCol){
		fieldsCol.forEach(function(field){
			if(field.value == -1){
				field.value = 0;
			}
		});
	});

	// convert string to int array
	if(match.options != null){
		var op = match.options;
		op = op.substring(1, op.length-1);
		op = op.split(',').map(function(item) {
			return parseInt(item, 10);
		});
		match.options = op;
	}

	if(match.action){
		var field = match.fields[match.action.x][match.action.y];

		switch (match.action.value) {
			case 0:
				var player = match.players.activePlayer == 1 ? 2 : 1;
				drawCharacter(field, board, player, ctx);
				break;
			case 4:
				activateTrap(field, board, ctx);
				field.value = 0;
				break;
			default:
				break;
		}
	}

	if(match.win && match.win > 0){
		if(match.win == 1){
			$localStorage.currentUser.userid == match.players.player1.id ? winMsg = "Gratulalok, nyertel!" : winMsg = "Sajnos vesztettel!";
		} else {
			$localStorage.currentUser.userid == match.players.player2.id ? winMsg = "Gratulalok, nyertel!" : winMsg = "Sajnos vesztettel!";
		}
		alert(winMsg);
		$state.go('welcome');
	}

	return match;
}