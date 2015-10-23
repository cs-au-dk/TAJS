//repo: https://github.com/Titani/Chain-Reaction/
//an explanation can be found in game-explained.js
//note the minified size

m = Math, R = m.random, P = m.pow, D = c.width = c.height = 600, T = D - 12, O = 256, l = e = E = N = 0;

setInterval(function() {
	if (!E && (e >= N || (L && e < N && l--))) {
		e = L = 0;
		A = [];
		n = (N = ++l * (l + 1) / 2) + 5;
	}
	a.fillStyle = e < N ? '#a41' : '#c73';
	a.fillRect(0, 0, D, D);
	a.strokeText(e + '/' + N, 9, 9);

	i = n;
	while (i--) {
		with (A[i] || (A[i] = {
			s : L,
			M : R() * 40 + 20,
			x : L ? L.clientX : R() * T + 6,
			y : L ? L.clientY : R() * T + 6,
			v : R() * 12 - 6,
			z : R() * 12 - 6,
			C : 'rgb(' + [R() * O | 0, R() * O | 0, R() * O | 0] + ')',
			r : 6,
			t : 30 / l | 0
		})) {
			if (!s) {
				A.some(function(o) {
					return o.s && P(r + o.r, 2) > P(x - o.x, 2) + P(y - o.y, 2) && (s = 1, E++, e++);
				});
				x += v *= 1 - 2 * (x < r | x+r > D);
				y += z *= 1 - 2 * (y < r | y+r > D);
			} else if (!t) {
				if (!--r) {
					A.splice(i, 1);
					n--;
					E--;
				}
			} else if (s > 1) {
				t--;
			} else {
				if (++r > M) {
					s = 2;
				}
			}

			a.beginPath();
			a.fillStyle = C;
			a.arc(x, y, r, 0, 7);
			a.fill();
		}};
}, 50);

c.onclick = function(e) {
	L || (L = e, E++, n++);
};