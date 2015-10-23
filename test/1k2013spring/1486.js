//in 1k the statement is `var s=6,S=30,m=[],Q=[],I=[],P=0,x,y,i,j,J=Math,r=J.random,f=J.floor,K="#",Z=window;`
//the additional 1k definitions "J=Math,r=J.random,f=J.floor" are shorthands to save space, k="#" is present as a inset for a string inside a stringified function
var size = 6, //block size, in 1k "s"
	margin = 30, // makes the canvas smaller than the document size, in 1k "S"
	maze = [], //the maze as 0 and 1s, 0 =NW-SE tile, 1 = NE-SW, in 1k "m"
	cmazewalls = [], //connectivity data of the maze borders, [0,0] is top left corner of maze[0][0], connected walls have the same value, in 1k "Q"
	wallcolorarray = [], // maps an integer to a color string, in 1k "I"
	cindex = 0, // counts number of discrete wall lines, in 1k "P"
	xsize, ysize, // size of the maze, in 1k "x" , "y"
	i, j; // general loop indexes

//colorval returns a random three digit hex colour eg "#FF0" is yellow, pute white values are rejected
//in 1k replaced inline in the main program loop as `for(var z=3,G=K;z--;)G=G+f(r()*15).toString(16);` - slightly different function - no hex F values instead of a check for "#FFF"
function colorval() {
	do {
		var c = "#",
			i;
		for (i = 0; i < 3; i = i + 1) {
			c = c + "0123456789ABCDEF".charAt(Math.floor(Math.random() * 16));
		}
	} while (c === "#FFF"); //avoid pure white
	return c;
}

//checks for a map tile at x,y - if 'out of bounds' returns -1, otherwise returns the type of that tile , 0 or 1
//in 1k version the code is `function T(i,j){if(i<0||j<0||i>=x||j>=y)return-1;else return m[i][j]}`
function testpoint(x, y) {
	if (x < 0 || y < 0 || x >= xsize || y >= ysize) {
		return -1;
	} else {
		return maze[x][y];
	}
}

//test if an adjacent tile may connect
//Note - the connectivity is tested from the ends of the diagonals - ie at the nodes - each node can hae 0 to 4 connections - each diagonal has 2 nodes one at each end (shared with adjacent diagonals)
//in 1k version these four test functions are folded into the function walkwalls
function wallconnectsNW(x, y) {
	return (testpoint(x - 1, y - 1) === 0);
}
function wallconnectsNE(x, y) {
	return (testpoint(x, y - 1) === 1);
}
function wallconnectsSW(x, y) {
	return (testpoint(x - 1, y) === 1);
}
function wallconnectsSE(x, y) {
	return (testpoint(x, y) === 0);
}


//prints the maze to the canvas
//does not print unconnected nodes (these would be a dot)
//in the 1k version the main double for-loop is called via a utility loop function "l" (also used elsewhere)
//where "l"" is defined `function l(p,q,e,f){for(i=p;i--;){e();for(j=q;j--;)f()}}`
//	in this case with the parameters `l(x,y,e,u("a.beginPath();k(T(i,j));a.stroke()"))`
//u is a function returning anonymous functions - the code is passed to "u" as a string- ie `function u(v){return function(){eval(v)}}`
//	"e" is an empty function created with `e=u("")`
//l & u are implemented to reduced file size
//in the 1k version the if else is avoided by using the value of 'testpoint(i,j)'  ie `T(i,j)` , 0 or 1, to modify the canvas drawing parameteres
//	function "k", `function k(A){a.strokeStyle=I[Q[i+A][j]];a.moveTo((i+A)*s,j*s);a.lineTo((i+1-A)*s,s+j*s)}` , with T(i,j) passed to A
function mazprint() {
	a.lineWidth = size / 2; // in 1k `a.lineWidth=s/2`
	a.lineCap = "round"; // in 1k `a.lineCap="round"`

	for (i = 0; i < xsize; i = i + 1) { // in 1k `l(x,y,e,u("a.beginPath();k(T(i,j));a.stroke()"))`
		for (j = 0; j < ysize; j = j + 1) {
			a.beginPath();
			if (testpoint(i, j) === 0) {
				a.strokeStyle = wallcolorarray[cmazewalls[i][j]];
				a.moveTo(i * size, j * size);
				a.lineTo(size + i * size, size + j * size);
			} else {
				a.strokeStyle = wallcolorarray[cmazewalls[i + 1][j]];
				a.moveTo(size + i * size, j * size);
				a.lineTo(i * size, size + j * size);
			}
			a.stroke();
		}
	}
}

//Walkwalls recursively visits connected walls, marking them all with the same color index value
//	this is a standard maze recursion algorithm, which will visit all connected points before returning
//	eg search (intenet) for "recursive solver algorithm maze"
//	the function will be called multiple times as there will be more than one set of connected walls.
function walkwalls(x, y) {
	cmazewalls[x][y] = cindex; // set the wall color index to the same color index as connected walls

	//the four if statements check for connectivity in the four possible directions
	//only univisted nodes will be recursed
	//the functions 'wallconnectsXX' check out of bounds conditions as wel as rejecting impossible connections eg a NW-SE map tile can not connect at the SW or NW corner
	//	in 1k the if statements are modified/removed to reduce size
	//		instead the proceedure for-loops over two variables, both from -1 to +1 (9 iterations), rejecting any that have a iterator value of 0, giving 4 remaining values corresponding to NW,NE,SW,SE ie +/-1,+/-1
	//		the function "R(V){return((V-1)/2)}" is used to convert parameters where -1 maps to -1 and +1 to 0
	//	the replacement function for walkwalls is :
	//		function W(i,j){Q[i][j]=P;for(var q=-1;q<2;q++)for(var n=-1;n<2;n++){if(n==0||q==0)continue;if(T(i+R(n),j+R(q))==-R(n/q)&&Q[i+n][j+q]==0)W(i+n,j+q)}}
	if (wallconnectsNW(x, y) && (cmazewalls[x - 1][y - 1] === 0)) {
		walkwalls(x - 1, y - 1); // walk NW
	}
	if (wallconnectsNE(x, y) && (cmazewalls[x + 1][y - 1] === 0)) {
		walkwalls(x + 1, y - 1); // walk NE
	}
	if (wallconnectsSW(x, y) && (cmazewalls[x - 1][y + 1] === 0)) {
		walkwalls(x - 1, y + 1); // walk SW
	}
	if (wallconnectsSE(x, y) && (cmazewalls[x + 1][y + 1] === 0)) {
		walkwalls(x + 1, y + 1); // walk SE
	}
}

//in 1k the entire function is inlined in to the main loop
//initialise the maze with 1s and 0s'
//0 indicates a (square) tile with a NW-SE line/wall, 1 indicates a NE-SW line/wall
//in 1k the function is called via a loop template funcion using `l(x,y,u("m[i]=[]"),u("m[i][j]=f(r()*2)"))` - note both f(r()*2) and f(r()+.5) produce 50:50 random 1 and 0
//	the loop template function "l" is described in the comment for "function mazprint"
function initialise() {
	for (i = 0; i < xsize; i = i + 1) {
		maze[i] = [];
		for (j = 0; j < ysize; j = j + 1) {
			maze[i][j] = Math.floor(Math.random() + 0.5);
		}
	}

	//initialise the maze color index array  -the colors of the maze nodes
	//this array is 1 larger n both dimensions than the maze tile array - there are potential nodes on both top/bottom & left/right sides of a tile
	//in 1k the function is called via a loop template function using `l(x+1,y+1,u("Q[i]=[]"),u("Q[i][j]=0"))`
	for (i = 0; i <= xsize; i = i + 1) {
		cmazewalls[i] = [];
		for (j = 0; j <= ysize; j = j + 1) {
			cmazewalls[i][j] = 0;
		}
	}
}

//Loops over the array of nodes - if a node is uncolored (ie unvisited) - the self recursive function "walkwalls" is called, which colors connected nodes
//in 1k the function is inlined into the main loop "J"
//in 1k the main loop is called via a loop template function l (see comments in function "mazprint")

//	l(x+1,y+1,e,u("if(Q[i][j]==0){P++;for(var z=3,G=K;z--;)G=G+f(r()*15).toString(16);I[P]=G;W(i,j)}"));` - note that the function "colorval" has been slightly modified
function iteratepaths() {
	for (i = 0; i <= xsize; i = i + 1) {
		for (j = 0; j <= ysize; j = j + 1) {
			if (cmazewalls[i][j] === 0) { //Note some nodes will be colored by function "walkwalls" before they are reached by the for loop - these are tested for and skipped
				cindex = cindex + 1; //new set of nodes found - increment color index
				wallcolorarray[cindex] = colorval(); // get a new random color
				walkwalls(i, j);
			}
		}
	}
}

//////////////////////
// ** MAIN  LOOP ** //
//////////////////////

//in 1k the entire function and its subfunctions are inlined into a single function called "J" - see below

//	J=u('x=f((Z.innerWidth-S)/s),y=f((Z.innerHeight-S)/s);c.height=y*s;c.width=x*s;l(x,y,u("m[i]=[]"),u("m[i][j]=f(r()*2)"));l(x+1,y+1,u("Q[i]=[]"),u("Q[i][j]=0"));l(x+1,y+1,e,u("if(Q[i][j]==0){P++;for(var z=3,G=K;z--;)G=G+f(r()*15).toString(16);I[P]=G;W(i,j)}"));a.lineWidth=s/2;a.lineCap="round";l(x,y,e,u("a.beginPath();k(T(i,j));a.stroke()"));')
//	     --get the screen size then calculate and set the approiate canvas size--- ---the inlined version of initialise()--- ---2nd part of inlined initialise()--- ----the inlined version of function iteratepaths() , includes an inlined version of colorval() ---- --------------------- inlined version of function mazprint() ---------------------
//	function "J" annoted above^^^
function makeanddraw() {
	xsize = Math.floor((window.innerWidth - margin) / size); // calculate the map x size from the window size, tile size, and margin - in 1k `x=f((Z.innerWidth-S)/s)`
	ysize = Math.floor((window.innerHeight - margin) / size); // as above for the y dimensio - in 1k `y=f((Z.innerHeight-S)/s)`

	c.width = xsize * size; // set the canvas width, in 1k `c.width=x*s`
	c.height = ysize * size; // as above for the height, in 1k `c.height=ys*s`

	initialise(); // make a (random) map
	iteratepaths(); // find the paths, and assign (random) colors
	mazprint(); // output the maze to canvas
}

/////////////////////
//** MAIN PRORAM **//
/////////////////////
b.style.textAlign = "center"; // center the canvas
makeanddraw(); // make a new map and output it, in 1k `J()`;

//add a listener to redo if the window changes size, in 1k `Z.onresize=J`
window.addEventListener("resize", function () {
	makeanddraw();
});

// in 1k the entire function is `b.onkeydown=function(e){L=e.keyCode;if(L==38)s++;if(L==40&&s>2)s--;J()}`
b.addEventListener("keydown", function (event) {
	var keypresscode = event.keyCode;
	if (keypresscode === 38) {
		size = size + 1;
	} // DOWN ARROW - decrease zoom
	if ((keypresscode === 40) && (size > 2)) {
		size = size - 1;
	} // UP ARROW - increase zoom
	makeanddraw(); // make a new map and redraw
});
// Finalised 29th March 2013 - License = Public domain. Enjoy! //