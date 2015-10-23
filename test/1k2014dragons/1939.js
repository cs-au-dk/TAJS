//COMPRESSION:
//1. Shortened all names to 1 or 2 characters
//2. Google closure
//3. JScrush

//-------MAZE GENERATION CODE-------

//the maze itself, with a grid that has explored and unexplored cells
var maze = {
	cellSize: 30, //this number defines the density of the maze. Everything will dynamically update around it.
	getSize: function() {
		this.xSize = Math.floor(a.width / this.cellSize);
		this.ySize = Math.floor(a.height / this.cellSize);
	},
	makeExplored: function() {
		this.explored = new Array(this.ySize); //initial boolean 2d array with all false values
		for (var i = 0; i < this.ySize; i++) {
			this.explored[i] = new Array(this.xSize);
			for (var j = 0; j < this.xSize; j++) {
				this.explored[i][j] = false;
			}
		}
	},
	drawGrid: function() {
		c.lineWidth = 2;

		for (var i = 0; i <= this.cellSize * this.ySize; i += this.cellSize) {
			c.beginPath();
			c.moveTo(0, i);
			c.lineTo(this.cellSize * this.xSize, i);
			c.stroke();
			c.closePath();
		}
		
		for (var i = 0; i <= this.cellSize * this.xSize; i += this.cellSize) {
			c.beginPath();
			c.moveTo(i, 0);
			c.lineTo(i, this.cellSize * this.ySize);
			c.stroke();
			c.closePath();
		}
	}
};

//what will carve out the maze from the grid using the generic recursive backtracking method
var explorer = {
	//initial random cell
	initiateRandom: function() {
		var randX = Math.floor(Math.random() * maze.xSize);
		var randY = Math.floor(Math.random() * maze.ySize);
		this.x = (randX * maze.cellSize) + 1;
		this.y = (randY * maze.cellSize) + 1;
		maze.explored[randY][randX] = true;
	},
	stack: [],
	cellDim: maze.cellSize - 2, //dimensions of a cell
	randSort: function(array) {
		array.sort(function() {return 0.5 - Math.random()})
	},
	directions: ["N", "E", "S", "W"],
	toCell: function(num) { //convert pixel cords to cell index
		return ((num - 1) / maze.cellSize);
	},
	toExplored: function() { //set current cell to explored
		maze.explored[this.toCell(this.y)][this.toCell(this.x)] = true;
	},
	toStack: function() { //push to the stack
		this.stack.push(this.y);
		this.stack.push(this.x);
	},
	canMove: function(direction, isCarve) { //tells if at edge, and if carve, at explored
		switch (direction) {
			case "W":
				if (this.toCell(this.x) > 0) {
					if (isCarve) {
						return !maze.explored[this.toCell(this.y)][this.toCell(this.x) - 1];
					}
					else if (this.toCell(player.x) > 1 && c.getImageData(player.x - 7, player.y, 1, 1).data[0] !== 0) {
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}
				break;
			case "N":
				if (this.toCell(this.y) > 0) {
					if (isCarve) {
						return !maze.explored[this.toCell(this.y) - 1][this.toCell(this.x)];
					}
					else if (this.toCell(player.y) > 1 && c.getImageData(player.x, player.y - 7, 1, 1).data[0] !== 0) {
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}
				break;
			case "E":
				if (this.toCell(this.x) + 1 < maze.xSize) {
					if (isCarve) {
						return !maze.explored[this.toCell(this.y)][this.toCell(this.x) + 1];
					}
					else if (this.toCell(player.x) + 1 < maze.xSize && c.getImageData(player.x + player.size + 7, player.y, 1, 1).data[0] !== 0) {
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}
				break;
			case "S":
				if (this.toCell(this.y) + 1 < maze.ySize) {
					if (isCarve) {
						return !maze.explored[this.toCell(this.y) + 1][this.toCell(this.x)];
					}
					else if (this.toCell(player.y) + 1 < maze.ySize && c.getImageData(player.x, player.y + player.size + 7, 1, 1).data[0] !== 0) {
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}
				break;
		}
	},
	move: function(direction, isCarve) { //handles moving. Can't move off the maze or onto an explored cell. Makes moveWorked with appropriate bool
		this.moveWorked = true;
		c.fillStyle = "#FFF";
		switch (direction) {
			case "W":
				if (this.canMove(direction, isCarve)) {
					if (isCarve) {
						this.toStack();
						c.fillRect(this.x - 2, this.y, this.cellDim + 2, this.cellDim);
						this.x -= maze.cellSize;
						this.toExplored();
					}
					else {
						player.draw("FFF");
						player.x -= maze.cellSize;
						player.draw("C00");
					}
				}
				else {
					this.moveWorked = false;
				}
				break;
			case "N":
				if (this.canMove(direction, isCarve)) {
					if (isCarve) {
						this.toStack();
						c.fillRect(this.x, this.y - 2, this.cellDim, this.cellDim + 2);
						this.y -= maze.cellSize;
						this.toExplored();
					}
					else {
						player.draw("FFF");
						player.y -= maze.cellSize;
						player.draw("C00");
					}
				}
				else {
					this.moveWorked = false;
				}
				break;
			case "E":
				if (this.canMove(direction, isCarve)) {
					if (isCarve) {
						this.toStack();
						c.fillRect(this.x, this.y, this.cellDim + 2, this.cellDim);
						this.x += maze.cellSize;
						this.toExplored();
					}
					else {
						player.draw("FFF");
						player.x += maze.cellSize;
						player.draw("C00");
					}
				}
				else {
					this.moveWorked = false;
				}
				break;
			case "S":
				if (this.canMove(direction, isCarve)) {
					if (isCarve) {
						this.toStack();
						c.fillRect(this.x, this.y, this.cellDim, this.cellDim + 2);
						this.y += maze.cellSize;
						this.toExplored();
					}
					else {
						player.draw("FFF");
						player.y += maze.cellSize;
						player.draw("C00");
					}
				}
				else {
					this.moveWorked = false;
				}
				break;
		}
	},
	moveBack: function() { //pops the previous position from the stack as the current position
		this.x = this.stack.pop();
		this.y = this.stack.pop();
	},
	step: function() { //steps once, following recursive backtracking rules
		this.randSort(this.directions);
		this.moveWorked = false;
		for (var i = 0; i < 4; i++) {
			if (!this.moveWorked) {
				this.move(this.directions[i], true); //this is the carving move!
			}
		}
		if (!this.moveWorked) {
			this.moveBack();
		}
	}
};

//make the maze using that wonderful object oriented programming I did
maze.getSize(); //Sets size of maze to fit to window
maze.drawGrid(); //Draws the grid
maze.makeExplored(); //generates the maze.explored 2d array
explorer.initiateRandom(); //gives a random starting position for the explorer
do { //step once to put something on the stack, then step until the stack is empty
	explorer.step();
}
while (explorer.stack.length > 0);
//^^^^^^^MAZE GENERATION CODE^^^^^^^

//draw gold coin
c.beginPath();
c.fillStyle = "#FE1";
c.strokeStyle = "DC1";
c.font = parseInt(maze.cellSize - 10) + "px Arial";
c.textAlign = "center";
c.textBaseline = "middle";
c.arc((maze.xSize * maze.cellSize) - (maze.cellSize / 2), (maze.ySize * maze.cellSize) - (maze.cellSize / 2), (maze.cellSize / 2) - 4, 0, Math.PI * 2);
c.fill();
c.stroke();
c.fillStyle = "#FA0";
c.fillText("$", (maze.xSize * maze.cellSize) - (maze.cellSize / 2), (maze.ySize * maze.cellSize) - (maze.cellSize / 2));
c.closePath();

//-------GAMEPLAY CODE-------
var player = {
	x: 7,
	y: 7,
	getSize: function() {
		this.size = maze.cellSize - 14;
		this.draw("C00");
	},
	draw: function(color) {
		c.fillStyle = "#" + color;
		c.fillRect(player.x, player.y, player.size, player.size);
	},
	checkWin: function() {
		if (explorer.toCell(this.x - 6) + 1 === maze.xSize && explorer.toCell(this.y - 6) + 1 === maze.ySize) {
			alert("Win!");
			b.onkeydown = null; //stop user input
		}
	}
};

player.getSize();

var keypress = function (e) { //user input handler
	switch(e.keyCode) {
		case 37:
			explorer.move("W", false);
			break;
		case 38:
			explorer.move("N", false);
			break;
		case 39:
			explorer.move("E", false);
			break;
		case 40:
			explorer.move("S", false);
			break;
	}
	player.checkWin();
};

b.onkeydown = keypress; //start user input
//^^^^^^^GAMEPLAY CODE^^^^^^^