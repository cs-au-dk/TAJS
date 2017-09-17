_ = Math;
z = document;
$ = window;
A = _.abs;
S = _.sin;
C = _.cos;
P = _.pow;

Y = N = R = 360;		//x and y offset and 1/2 canvas size

// drawing params preset to make a heart
k = K = 	200;		// scale
V = M = E = 2;			// M number of sides, E = n1
F = 		-2.5;		// n2
o = H = G = .78;			// G = n3, o,H = rotation
// saving some space
z.c = z.createElement,
g = "value",

c.height = D = 0, 			// hide the canvas and use D to track mousedown for drag state,

Q = z.c(g); 				// create a dom node that will hold our svg that we can repeatedly call innerHTML on (reusing an arbitrary string for the node type works fine)
b.i = b.appendChild;		// save some space
b.i(Q);						// add the holder for our svg
b.style.background = "#cef";

// our string builder so that we can have a super cool bursting background behind the heart
s = function() {
	W = '<svg height=\"600\">';
	Z = T()+"</svg>";
	Q.innerHTML = W+T(777)+Z+"<a target='_new' href='data:image/svg+xml;utf8,"+W+Z+"'>ctrl+click > save as</a> "
};

T = function(X) {
	U = "";
	H+=0.02;
	// if given an arg for x draw a different shape than that defined by the main shape globals
	X ? (Y=R*2,V=k=X,w=H) : (Y=N,V=M,k=K,w=o);
	
	// draw loop
	for (i=0;i<=Y;i++) {

		// supershape formula thanks paul! http://paulbourke.net/geometry/supershape/
		// replaced PI*2 with an approximate value, replace a and b with 1
		q = v = 0, p = i*6.3/Y;
		r = P(P(A(C(V * p / 4-w)),F)+P(A(S(V * p / 4-w)),G),1/E);
		(A(r)) && (r = 1 / r, q = r * C(p)*k, v = r * S(p)*k);

		q += 430; // x + offset
		v += 280; // y + offset

		// draw it
		U += (i? "L" : "M") + q+" "+v; // build the svg path string
	}

	// transform="rotate(90,460,340)"
	return '<path fill="'+(X?"cyan":"deeppink")+'" stroke="white" d="'+(U||"M0 0L1 1")+'Z"/>';
};

// create the inputs
for (i=0;i<6;i++) {
	(function() {
		var t = z.c("input"), 					// create our input field
			B = "KNMEFG".split("")[t.i = i];	// what global do we update with this iteration

		t.v = B;								// record the id of our global for lookup like: window[id]
		t[g] = $[B]; 							// copy global to input
		t.style.cursor = "col-resize";
		b.i(t); 								// append the input

		//draggable inputs
		t.onkeyup = b.onmouseup = t.onmousedown = function(v) {
			(v.which-1) ? 						// which == 1 if we use the mouse and something bigger when typing
				$[B] = t[g] 				// keyup handler, update as you type
			:
				D ? 							// we dragging?
					D=0							// if so stop it
				:
					this==t && (D=t, e=D[g], X=v.screenX) // else start dragging, record our start x
		},
		b.onmousemove = function(v) {
			//update the input, the global and the shape, allow for shift+drag to do 10x faster adjustment
			D && ($[D.v] = D[g] = e*1 + (v.screenX-X) * (v.shiftKey ? 2.5 : .25))
		}

	}())
}

setInterval(s,60) // initial draw