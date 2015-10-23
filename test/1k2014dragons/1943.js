// Crushed with http://www.iteral.com/jscrush/

S = 1;
M = [];
C = [];
u = [];
v = l = q = 0;
A = 'r';

// Draws a color to the canvas and puts it in the matrix
R = function(p, e){
	c.fillStyle = "rgb(" + e.r + "," + e.g + "," + e.b + ")";
	c.fillRect(p.x * S - Math.floor(a.width / 2 * (S - 1)), p.y * S - Math.floor(a.height / 2 * (S - 1)), S, S);
	M[p.x][p.y] = {r: e.r, g: e.g, b: e.b};
}

// Returns a free neighbor to the 'p' coordinate
N = function(p, e){
	for(j = -1; j < 2; j++){
		for(k = -1; k < 2; k++){

			if((p.x - j >= a.width || p.y - k >= a.height || p.x - j < 0 || p.y - k < 0))
				continue;
			
			if(!M[p.x - j][p.y - k])
				return {x: p.x - j, y: p.y - k};
		}
	}
}

// We make all the colors, alltogether 889610
for(j = 0; j < 255; j++){
	for(k = 0; k < j * 0.4; k++){
		for(i = 0; i < j * 0.4; i++){
			C.push({r: j, g: k, b: i});
		}
	}
}

// Shuffle
for(i = C.length; i; j = Math.floor(Math.random() * i), k = C[--i], C[i] = C[j], C[j] = k);


// Make the matrix for the colors
for(j = 0; j < a.width; j++){
	M.push([]);
	for(k = 0; k < a.height; k++){
		M[j].push(0);
	}
}

setInterval(o = function(p, e){
	if(o){
		
		// Find the most similar already drawn color
		d = 999;
		for(i = 0; i < u.length; i++){
			// If adjacent with the last drawn color, see if it has a free neighbor, if not we delete it.
			// (It would be very slow without this)
			if(	l &&
				(u[i].x - 1 == l.x || u[i].x == l.x || u[i].x + 1 == l.x) &&
				(u[i].y - 1 == l.y || u[i].y == l.y || u[i].y + 1 == l.y) &&
				!N(u[i])){
					u.splice(i--, 1);
					continue;
			}

			if(Math.abs(C[0].r - M[u[i].x][u[i].y].r) + Math.abs(C[0].g - M[u[i].x][u[i].y].g) + Math.abs(C[0].b - M[u[i].x][u[i].y].b) < d){
				d = Math.abs(C[0].r - M[u[i].x][u[i].y].r) + Math.abs(C[0].g - M[u[i].x][u[i].y].g) + Math.abs(C[0].b - M[u[i].x][u[i].y].b);
				v = N(u[i]);
			}
		}
		
		// If this is the first color we put it in the middle
		v = v || {x: Math.floor(a.width / 2), y: Math.floor(a.height / 2)}

		// If there was a click, we put the next color there
		if(q){
			v = q;
			q = 0;
		}

		// Render the color
		R(v, C.shift());
		u.push(v);
		l = v;

		if(!C.length){
			o = 0;
		}
	}
}, 0);

onclick = function(p, e){
	q = {
		x: Math.floor(a.width / 2 + ((p.clientX - a.width / 2) / S)),
		y: Math.floor(a.height / 2 + ((p.clientY - a.height / 2) / S))
	}
}

// Zoom or change the canvas color
onkeyup = function(p, e){
	
	// 1-9: zoom. Clear the canvas and redraw it with the new size
	if(p.which - 48 > 0 && p.which - 48 < 10){
		S = p.which - 48;

		a.width+=0

		for(j = 0; j < a.width; j++){
			for(k = 0; k < a.height; k++){
				if(M[j][k]){
					R({x: j, y: k}, M[j][k]);
				}
			}
		}
	}
	
	// r/g/b: change the colors.
	e = 0;
	if(p.which == 82){
		e = 'r';
	}
	if(p.which == 71){
		e = 'g';
	}
	if(p.which == 66){
		e = 'b';
	}

	// If the new color is not the same as the present color 
	if(e && e != A){

		// Reform the colors in the matrix and redraw them
		for(j = 0; j < a.width; j++){
			for(k = 0; k < a.height; k++){
				if(M[j][k]){
					t = M[j][k][A];
					M[j][k][A] = M[j][k][e];
					M[j][k][e] = t;
					R({x: j, y: k}, M[j][k]);
				}
			}
		}

		// Reform the colors which have not been put down
		for(j = 0; j < C.length; j++){
			t = C[j][A];
			C[j][A] = C[j][e];
			C[j][e] = t;
		}

		A = e;
	}
}