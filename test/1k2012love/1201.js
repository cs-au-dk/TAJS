c.width = c.height = d = 500;
M = Math;
R = M.random;
G = setTimeout;
q = t = -1;
s = 1e3;
o = l = m = j = 0;

// bit people
h = [];

// helper return random number from 0 to x
function W(x) {
	return 0|R()*x
}

// generate random Color
function C() {
	e = W(4095);
    return '#' + e.toString(16);
}

// generate BitPerson
function P() {
	h.push({
        c: [C(), C(), C()], // skin, body, pants
        p: {
        	l: W(440), // left
        	t: W(460) // top
        }
    });
    k = 0;
}

// draw body part
function B(w, h, c) {
    a.fillStyle = c;
    a.fillRect(x, y, w, h);
}

// draw chAr
function A(s, c) {
	a.font = s +"px Arial";
    a.fillStyle = 'red';
    a.fillText(c, x, y+s/2);
}

// draw BitPerson
function D(person, i, a, z) {
    v = person.p,
    w = person.c;

    x = z ? x + 22 : v.l + 3;
    !z && (y = v.t);
    B(5, 4, w[0]); // face

    y += 14;
    B(5, 5, w[2]); // left left

    x--;
    y -= 9;
    B(7, 9, w[1]); // body
}

// Find mate
function E() {
	L();
	m = 1;
	o = h.shift();
//	w = h.length;
	t = W( w = h.length );
	h.push(o);
	r = o.p;
	o = w;

	x = r.l + 15;
	y = r.t;// - 15;

	A(29, '♥');
	D(h[t],'','',1);

	G(function() {
		m=0;
	}, s*2);
}

// cLear
function L() {
	c.width = d;
}

// resUlt
function U() {
	m = 1;
	p = h[o].p;
	L();

	x = p.l + 15;
	y = p.t;// - 15;

	t ^ q ? A(60, '☹') : A(40, '♥');
	G(function() {
		h.splice(o, 1);
		q == t ? (h.splice(q,1), j++) : j--;
		q = -1;
		m=0;
		G(E, s);
	}, s*2);
}

// checK click
function K(p, i) {
	p=p.p, o ^ i && M.abs(p.l+6 - f) < 6 && M.abs(p.t+10 - g) < 10 && (q=i,U())
}

b.onclick = function(e) {
	f = e.pageX;
	g = e.pageY;

	!m && h.map(K)
}

// start
for (n=11; --n;) P();

setInterval(function() {
	m ?	(
		D(h[o]), q > -1 && D(h[q])
	):(
		L(), h.map(D), ++k > 6 && P()
	);
	x = y = 5;
	A(15, '♥ = ' + j)
}, 250);

G(E, s);