$ = function( p ) {
	ñ = b.appendChild( document.createElement( "span" ) );
	t = ñ[ Ü ];
	t[ bg ] = "blue";
	t.position = "absolute";
	t.whiteSpace = "pre";
	t.top = p;
	return ñ;
}

rnd = function( A, B, C, D, E ) {
	return Ú.ceil( Ú.random() * A );
}

// arc
K = function( A, B, C, D, E ) {
	c[ Ä ]();
	c.arc( A, B, C, -D, -E, 1 );
	c.stroke();
}

// color
r = function( A, B, C, D, E ) {
	c.fillStyle = c.strokeStyle = "#" + γ[ A ];
}

// circle
o = function(A, B, C, D, E ) {
	c[ Ä ]();
	c.arc( A, B, C, 0, σ );
	c.fill();
}

// rectangle
Q = function( A, B, C, D, E ) {
	c[ Ä ]();
	c[ (E ? "clear" : "fill") + "Rect" ]( A - 1, B - 1, C + 2, D + 2 );
}

N = function( A, B, C, D, E ) {
	// init state
	S = 0;

	// init player number
	P = 0;

	// clear canvas
	Q( 0, 0, 999, 999, 1 );

	// draw city
	L = 2;
	j = 1;
	while( L < È ) {
		// Set width of building
		W = rnd( 37 ) + 37;

		// Set height of building
		H = Ú.max( rnd( 120 ) + ( rnd( 6 ) < 3 ? 130 : 15 ), 10 );

		// Set the coordinates of the building into the array
		J[ j++ ] = {
			x: L,
			y: δ - H,
			w: W
		};

		r( rnd(3) );
		Q( L, δ - H, W, H );

		// drawin' windows
		for ( e = L + 3, L += W + 2; e < L - 5; e += 10 ) {
			for( i = H - 3; i >= 0; i -= 15 ) {
				r( 5 - !( rnd(4) - 1 ) );
				Q( e, δ - i, 3, 6 );
			}
		}
	}

	// place gorillas
	for ( i = 0; i < 2; i++ ) {
		// determine building for gorilla
		L = J[ ( j - 2 ) * i + ( i ? - 2 : 2 ) + 1 ];

		// register
		G[ i ] = {
			x: L.x + L.w / 2,
			y: L.y - 30
		};

		// draw
		g( i, 3 );
	}
}

// drawGorilla
g = function( A, B, C, D, E ) {
	var p = G[ A ],
		x = p.x,
		y = p.y;

	// clear previous gorilla
	Q( x - 15, y - 3, 28, 30, 1 );

	r( 6 ); // white

	// draw head
	o( x, y + 2, 6 );

	// body
	Q( x - 7, y + 8, 13, 9 );
	
	for( A = x; A <= x+4; A += .1 ) {
		// legs
		K( A, y + 25, 10, 3*þ/4, 9*þ/8 );
		K( A - 6, y + 25, 10, 15*þ/8, þ/4 );

		// arms
		K( A - 5, y + (B & 1 ? 14 : 4), 9, 3*þ/4, 5*þ/4 ); // left
		K( A, y + (B & 2 ? 14 : 4), 9, 7*þ/4, þ/4 ); // right
	}

	// draw eyes/brow
	Q( x - 3, y + 2, 5, 0, 1 );
}

b.onmousemove = function( A, B, C, D, E ) {
	dx =( A.clientX - a.offsetLeft - px) || .1;
	dy = A.clientY - a.offsetTop - py;

	Ã = Ú.round( Ú.atan( dy / dx ) / σ * ( P ? l : -l ) );
	Ã += Ã < 0 ? l : 0;
}

md=0;
b.onmousedown = function( A, B, C, D, E ) {
	V = 0;
	md = md || T( function Ä( A, B, C, D, E ) {
		V++;
		md = T( Ä, 20 );
	});
}
b.onmouseup = function( A, B, C, D, E ) {
	md = +clearTimeout( md );

	if ( !S ) {
		// plotShot
		A = Ã / k * þ;

		Ö = Ú.cos( A ) * ( P ? -V : V );
		Ë = Ú.sin( A ) * V;

		g( P, 2 - P );

		xp = px;
		yp = py - 20;

		t = 0,

		S = 3;

		T(function( A, B, C, D, E ) {
			g( 0, 3 );
			g( 1, 3 );

			S = 1; // SHOT
		}, 200);

		// \plotshot

		V = 0; // reset velocity
		P = +!P; // pass turn
	}
}

var Ã,
	V,

	bg = "backgroundColor";
	Ä = "beginPath";

	P = 0,

	Ú = Math,
	þ = Ú.PI,
	σ = 2*þ,
	T = setTimeout;

	k = 180,
	l = 360;
Ü = "style";
d = b[ Ü ];
d.color = "#fff";

d = a[ Ü ];
d[ bg ] = "blue";
d.height = d.width = null;
a.height = δ = 350;
a.width = È = 640;
Ò = È / 2;

J = [];
G = [];
Z = [ 0, 0 ];

u = "globalCompositeOperation";
n = "ImageData";

// colors
γ = [
	"f00", // red
	"639", // purple
	"f61", // orange
	"ccc", // light grey
	"777", // grey
	"ff1", // yellow
	"fff"  // white
];

S = 0;
B = $( "10px" ); // status info
M = $( "320px" ); // score info
M[ Ü ].left = "280px"

N(); // init

/*
	SHOT = 1,
	EXPLODE = 2,
	SHOOTING = 3,
	VICTORY = 5
*/
(function F() {
	switch( S ) {
		case 1:  // SHOT
			// updateShot
			t && c["put"+n]( β, X - 4, Y - 4 );

			X = xp + (Ö * t),
			Y = yp + Ë * -t + 4.9 * t * t;

			( X < È && X > 3 && Y < δ ) // check if on screen
				? ( L = c["get"+n]( X, Y, 1, 1 ).data[ 2 ] ) // determine impact
				: ( S = 0 ); // off screen

			// store pre-banana data, so we can restore later
			β = c["get"+n]( X - 4, Y - 4, 8, 8 );

			// draw banana
			r( 5 );
			o( X, Y, 3 );

			t += .1;

			if ( L ) {
				// only white pixels have G > 250 with current colors
				R = 7;
				S = 2;
				s = -1;

				if ( L > 250 ) {
					s = +!(X < Ò);
					X = G[ s ].x;
					Y = G[ s ].y;
					R = 50; // bigger boom!
				}

				ex = X;
				ey = Y;
				É = R,
				Í = R / 12,
				er = R+.5;
			}
			break;
		case 2: // EXPLODE
			// updateExplosion();
			r( 0 );
			c[ u ] = "destination-out";
			o( ex, ey, É + Í );
			c[ u ] = "source-over";

			o( ex, ey, er );
			er -= Í;

			if ( er < 0 ) {
				S = 0;

				if ( s >= 0 ) {
					S = 5; // VICTORY
					Z[ +!s ]++;

					I = 0;
					(function B() {
						g( +!s, I % 2 + 1 );
						return T( I++ < 4 ? B : N, 800 );
					}());
				}
			}
	}

	px = G[ P ].x;
	py = G[ P ].y;

	// update input status
	B.innerHTML = 
		( !S && Ã ) ?
		( "Angle: " + Ã + "\n" + ( V ? "Velocity: " + V : "") ) :
		"";

	B[ Ü ].left = ( P ? 500 : 20 ) + "px";

	// update score
	M.innerHTML = Z[ 0 ] + ">Score<" + Z[ 1 ];
	

	requestAnimationFrame( F );
}());