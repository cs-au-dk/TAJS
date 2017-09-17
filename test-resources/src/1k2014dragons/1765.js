//
// Variables:
// f,U,V,G,J,Q,Z,R,N - shortcuts for functions
// W,H - window size
// j,u - magic numbers
// _ - colors for objects (index is type)
// S,D - timer holders
//
// M - objects arrays, A - for strips, B - for cars
// P - player object
// l - player lives
// v - player score
//
// Functions:
// C - Keyboard processor
// r - restart
// E - object generator, generates objects until some conditions reached
// L - main loop, inner function p is shortcut for forEach call
//
// O - game object:
// Variables:
// s - `this` variable
// w,h,x,y (s.z,s.Z,s.p,s.P) - width, height, position x, position y
// X (s.v) - velocity on the x axis
// s.V - velocity on the y axis, only player using this
// r - array to hold object
// F - object type: 0 - strip, 1 - enemy, 2 - player
// Functions
// T - main tick processor
// E - remove event processor
//

U = 'forEach',
V = 'length',
G = 'fill',
f = 'fillStyle',
J = 'fillRect',
Q = 'fillText',
Z = Math.abs,
R = Math.random;
N = Date.now;
W = a.width,
H = a.height,
j = 120,
u = 65;
_ = ['#FFF','#FF3','red'];
P = 0;
function O(w,h,x,y,X,r,F){
	var s={
		v:X,V:0,
		z:w,Z:h,
		p:x,P:y,
	};
	r.push(s);
	s.T=function(t){
			s.p += s.v*t;
			s.P += s.V*t;
			s.p < -w && (s==P ? s.p =- w+1 : s.E());
			F == 1 &&
				l > 0 && Z(P.p - s.p) < w/2 + P.z/2 &&
				Z(P.P - s.P) < h/2 + P.Z/2 &&
				(P.E() || s.E());
			F == 2 && s.p > W-w && (s.p = W - w) || (s.P < -h || s.P > H) && s.E();
			c[f] = _[F];
			c[J](s.p,s.P,w,h);
			c[f] = '#06F';
			F && c[J](s.p+(X<-0.4?20:80),s.P+5,9,u-9);
			c[G]()
	}
	s.E=function(){
			F == 2 && 0 < --l && (s.p=x,s.P=y) ||
			r.splice(r.indexOf(s),1);
			0 < l && (v += P.p/9)
	}
	return s;
};
M = [A=[],B=[]];
C = function(d) {
	return function(e){
		k = e.keyCode;
		d && k == 82 && r();
		P.v = d * (k - 68 ? k - u ? 0 : -1 : 1);
		P.V = d * (k - 83 ? k - 87 ? 0 : -1 : 1);
		z++
	}
}
b.onkeydown = C(1);
b.onkeyup = C(0);
S = N();
(r = function(){
	P && (l=0, P.E());
	P=O(j,u,9,H/2,0,B,2);
	z = v = 0;
	l = 3
})();
E = function(){
	if(!A[V])
		while(A[V]<H/j)
			O(j,8,W,u+j*A[V],-0.4,A,0);
	z > 2 && S % R() > 0.8 && B[V] < 8 && O(j,u,W,R()*H,-R(),B,1)
}
c.font='35px Sans';
(function L(){
	requestAnimationFrame(L);
	D=N()-S;
	S+=D;
	E();
	c.clearRect(0,0,W,H);
	c[f]='gray';
	c[J](0,0,W,H);
	c[G]();
	p=function(o){ o.T(D) }
	A[U](p);
	B[U](p);
	c[f] = _[0];
	c[Q]('♥'+l,9,40);
	0 >= l && c[Q]('Score: '+~~v,W/2.5,250)
})()