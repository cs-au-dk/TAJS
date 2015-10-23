var gameState,
	lastClick,
	playerGameTable = [],
	pcGameTable = [],
	fillStyle = "fillStyle",
	fillRect = a.fillRect.bind(a),
	strokeStyle = "strokeStyle",
	strokeRect = "strokeRect",
	pcHits = 0,
	al = alert,
	playerHits = 0,
	M = Math.random,
	sortFn = function() { return 0.5 - M() },
	ships = [4,3,3,2,2,2,1,1,1,1].sort(sortFn),
	ships2 = ships.sort(sortFn),
	WandH = 660,
	start;
	
	
c.width = WandH;
c.height = WandH/2;

for (i = 10; i--;) {
	start = (M()*(10-ships[i]))|0;
	
	for	(l = ships[i]; l--;) {
		playerGameTable[start+l+i*10] = 1; // 1 means to add valid visible shippart
	}

	start = (M()*(10-ships2[i]))|0;
	
	for	(l = ships2[i]; l--;) {
		pcGameTable[start+l+i*10] = 1; // 1 means to add valid invisible shippart
	}
}

gameState = 1;

b.onmousedown = function(e){
	var x = e.clientX - 350;
	var y = e.clientY - 20;
		
	if (gameState < 2) {
		if	(x > 0 && x < 300 && y > 0 && y < 300) {
			lastClick = [ x/30|0, y/30|0 ];
		}
	}
};

setInterval(function(){

	//Player Move
	if (gameState == 1) {
		if (lastClick) {
			//check if we hit
			var index = lastClick[0] + lastClick[1] * 10;
			if (pcGameTable[index] == 1) {
				pcGameTable[index] = 9;
				playerHits++;
			}
			if (!pcGameTable[index]){
				pcGameTable[index] = 10;
				gameState = 2;
			}
			//check if we won already
			if (playerHits > 19) {
				al('Win');
				gameState = 9;
			}
		
			
			lastClick = 0;
		}
	}
	
	//PC move
	if (gameState == 2) {
	
		//check if we hit
		var x = M()*10|0;
		var y = M()*10|0;
		
		if (lastClick) { 
			if ( lastClick[0] < 9 ) {
				x = lastClick[0] + 1;
				y = lastClick[1];
			}
			lastClick = 0;
		}
		
		var index = x + y * 10;
		
		if (playerGameTable[index] == 1) {
			playerGameTable[index] = 7;
			lastClick = [ x, y ];
			pcHits++;
		}
		if (!playerGameTable[index]){
			playerGameTable[index] = 8;
			gameState = 1;
		}
		//check if PC won already
		if (pcHits > 19) {
			al('Lose');
			gameState = 9;
		}
	
	}
	
	//drawing player ships
	for (i = 100; i--;) {
		var temp = (i/10|0)*30+12, temp2 = (i%10)*30+12;
		
		if (playerGameTable[i] == 1 || playerGameTable[i] == 7) {
			a[fillStyle] = "#00f";
			fillRect(temp2, temp, 27, 27);
		}
		if (playerGameTable[i] > 6) {
			a[fillStyle] = "#000";
			fillRect(temp2+10, temp+10, 10, 10);
		}
		if (pcGameTable[i] == 9 || (gameState > 8 && pcGameTable[i] == 1)) {
			a[fillStyle] = "red";
			fillRect(temp2+330, temp, 27, 27);
		}
		if (pcGameTable[i] > 8) {
			a[fillStyle] = "#000";
			fillRect(temp2+340, temp+10, 10, 10);
		}
		
		a[strokeStyle] = "#000";
		a[strokeRect](temp2-2, temp-2, 30, 30);

		a[strokeStyle] = "#000";
		a[strokeRect](temp2+328, temp-2, 30, 30);
		
	}

}, 9)