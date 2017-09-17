/*
The script executes given L-system code and generates fractals on the screen. This is the Hilbert curve:

iters: 7
angle: 90
start: "L"
L: "-Rf+LfL+fR-"
R: "+Lf−RfR−fL+"

"iters" if the number of iterations. "angle" is the turning angle in degrees. "start" is the initial axiom. Other lines are the rewrite rules. Supported operators are +− for turning, lowercase letters for drawing, and [] for push and pop.

For more examples, see: http://web.itu.edu.tr/~aygunes/eser/Computer/LsystemFractalGeneratorForJS1K
*/

d = document;

c.setAttribute("style", "float:left;margin:9px");
e = d.createElement("div");
e.innerHTML = '<a target="_blank" href="http://x.vu/237908">L-systemˀ</a>:<br>'
		+ '<textarea id="l" rows=6 cols=34>iters:5\nangle:45\nstart:"X"\nX:"+ff++f++f--f++f++ff"\nf:"fff[-ffX][+fX]"</textarea><br>'
		+ '<input type="submit" onclick="g()">'
b.appendChild(e);

g = function () { // go
	with (Math) {
		// Clear and resize the canvas;
		w = c.width = c.height = 500; // width
		
		// Parse the L-system.
		s = eval("({" + d.getElementById("l").value.trim().replace(/\n/g, ",") + "})"); // lsystem
		t = s.angle / 180 * PI; // theta
		q = s.start; // sequence
		
		// Generate the sequence.
		for (i = 0; i < s.iters; ++i){
			m = ""; // temporary
			for (j = 0; j < q.length;) {
				r = s[h = q[j++]]; // rewrite, character
				m += r ? r : h;
			}
			q = m;
		}
		
		// Define the execute function.
		u = function (k, l) { // execute, line callback, move callback
			S = []; // stack
			x = y = p = i = 0; // x, y, alpha
			eval(l);
			for (; i < q.length;) {
				h = q[i++];
				h == "+" ? p += t
					: h == "-" | h == "−" ? p -= t
					: h == "[" ? S.push([x, y, p])
					: h == "]" ? (o = S.pop(), x = o[0], y = o[1], p = o[2], eval(l))
					: h == h.toLowerCase() ? (x += cos(p), y -= sin(p), eval(k))
					: 0;
			}
		}
		
		A = B = C = D = 0; // minX, maxX, minY, maxY
		u("A=min(x,A);B=max(x,B);C=min(y,C);D=max(y,D)", 0);
		
		P = B - A; // deltaX
		Q = D - C; // deltaY
		f = min(w / P, w / Q); // factor
		with (a) {
			strokeStyle = "#C03";
			scale(f, f);
			translate(
					-A - P / 2 + w / 2 / f,
					-C - Q / 2 + w / 2 / f);
			lineWidth = 1 / f;
			beginPath();
			u("a.lineTo(x,y)", "a.moveTo(x,y)");
			stroke();
		}
	}
}

g();