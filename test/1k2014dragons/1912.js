var NW = 100,   // initial number of columns
	P = 0.4,    // density of the population
	S,          // cell's size
	NH,         // number of rows
	C,          // array to keep state of each cell
	N,          // array to keep state of each cell
	CLRS = ["#5E6D70", "#FF6600","#1B1D1E"],
	AN = false; // mark that animation's on

function init() {
	var i = 0,
		t = 1,  //"tribes" are represented by "ones" and "twos"
		j;
	S = Math.ceil(a.width/NW);
	NH = Math.ceil(a.height/S);
	C = [];
	N = [];
	for ( ; i<NW; i++ ) {
		C[i] = [];
		N[i] = [];
		if ( i > NW/2-1 ) { t = 2; } // next half for another tribe
		for ( j = 0; j<NH; j++ ) {
			C[i][j] = ( Math.random() > P) ? 0 : t;
			N[i][j] = 0;
			drawCell(i*S, j*S, C[i][j]);
		}
	}
}

b.onkeyup = function(e) {
	switch (e.keyCode) {
		case 32: showMustGoOn(); break;
		case 39: if ( !AN ) { NW+=6; init(); }; break;
		case 37: if ( !AN ) { NW-=6; init(); }; break;
		case 38: if ( !AN ) { P+=0.02; init(); }; break;
		case 40: if ( !AN ) { P-=0.02; init(); }; break;
	}
};

init();

function drawCell(x, y, t) {
	c.fillStyle = CLRS[t];
	c.fillRect(x, y, S, S);
}

function showMustGoOn() {
	AN = !AN;
	if ( AN ) { requestAnimationFrame(generation); }
	function generation() {
		var m = 0, n;
		for ( ; m<NW; m++ ) {
			for ( n = 0; n<NH; n++ ) {
				N[m][n] = calcState(m, n);
				drawCell(m*S, n*S, N[m][n]);
			}
		}
		N = [C, C = N][0];   // swap the arrays
		if ( AN ) { requestAnimationFrame(generation); }
	}
}

function calcState(i,j) {

	// The battlefield essentially is a torus.
	var mi = ( i-1 < 0 ) ? NW-1 : i-1,
		pi = ( i+1 > NW-1 ) ? 0 : i+1,
		mj = ( j-1 < 0 ) ? NH-1 : j-1,
		pj = ( j+1 > NH-1 ) ? 0 : j+1,
	// all the neighbours are welcome to the special array
		neighbours = [
			C[mi][mj],
			C[mi][j],
			C[mi][pj],
			C[i][mj],
			C[i][pj],
			C[pi][mj],
			C[pi][j],
			C[pi][pj]
		],
		zerro = 0, ones = 0, twos = 0, z = 0, alive;
	for ( ; z<8; z++ ) {
		if ( neighbours[z] ) {
			if ( neighbours[z]===1 ) {
				ones++;
			}else {
				twos++;
			}
		}else {
			zerro++;
		}
	}
	alive = ones + twos;
	// usual rules of the Conway's Game Of Life
	if ( C[i][j] ) {
		return ( alive<2 || alive>3 ) ? 0 : C[i][j];
	}
	//but if the dead cell was made to came to life, it appears as a member of the predominant (in the neighbourhood) tribe
	return ( alive!==3 ) ? 0 : ( twos > ones ) ? 2 : ( ones > twos ) ? 1 : (Math.floor(Math.random()+0.5));
}