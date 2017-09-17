/* 
	'CPR' for js1k 2012-love by Vince Allen http://www.vinceallen.com @vinceallenvince
	Compressed using: 
	1. http://closure-compiler.appspot.com/home (Google Closure Compiler)
	2. http://www.iteral.com/jscrush/ (by Aivo Paas)
*/

/*
			
A = clone

B = random range
	
C = PVector.create 
D = PVector.add
E = PVector.multiply
F = PVector.divide

t = collection
c = color
d = fade direction
f = click event listener

*/
						
function A(a) { // clone
    function A(){}
    A.prototype=a;
    return new A
};			

function B(a,b) { // random range
	return Math.random()*(b-a)+a
};

// PVector
		
function C(a,b) { // create
	q = A(this);
	q.x = a;
	q.y = b;
	return q
};
function D(a) { // add
	this.x += a.x;
	this.y += a.y;
	return this
};
function E(a) { // multiply
	this.x *= a;
	this.y *= a;
	return this;
};		
function F(a) { // divide
	this.x /= a;
	this.y /= a;
	return this
};

H = { // Blood drop
	i: function(a){ // init
		
		this.l = C(a.pageX,a.pageY); // location
		this.v = C(0,0); // velocity
		this.a = C(0,0); // acceleration
		
		this.j(C(B(-9,9),B(0,9))); // apply random force
		
		s=B(4,32); // random size
		
		q = document.createElement("div");
		q.setAttribute("style","width:"+s+"px;height:"+s+"px;border-radius:"+s/2+"px;background:red;position:absolute;top:-"+s+"px");
		this.e = b.appendChild(q); // this.e = saves a reference to this dom element; used when referencing style
		
		return this
		
	},
	u: function(){ // update
		
		this.j(C(0,-.1)); // applyForce(gravity) reversed
		
		if(d<0) {
			this.j(C(0,1)); // applyForce(gravity)
		}
						
		this.l.D(this.v.D(this.a)); // velocity + acceleration // location + velocity
		
		if (this.l.y > window.innerHeight) {
			this.l.y = window.innerHeight;
			this.v.y *= B(-1,-.1) // random bounce
		}
		
		this.e.style.webkitTransform="translateX("+this.l.x+"px)translateY("+this.l.y+"px)translateZ(0)";
		this.e.style.MozTransform="translateX("+this.l.x+"px)translateY("+this.l.y+"px)";	
		this.e.style.OTransform="translateX("+this.l.x+"px)translateY("+this.l.y+"px)";
		this.e.style.msTransform="translateX("+this.l.x+"px)translateY("+this.l.y+"px)";

		this.a.E(0) // reset acceleration
		
	},
	j: function(a){ // apply force
		this.a.D(A(a).F(9)) // make a copy of the force so the original force vector is not altered by dividing by mass; // div by mass
	}
}

t = []; // collection
c = 261; // body color
d = -1; // fade direction

function f(g){

	t.push(A(H).i(g));
	t.push(A(H).i(g));
	t.push(A(H).i(g));

	b.style.color = "rgb("+(c+=9*d)+",0,0)";
	if (c>255||!c) {
		d*=-1
	}
}

// styles
b.setAttribute("style","font:"+window.innerHeight+"px/1em s;text-align:center;overflow:hidden;color:red;-webkit-user-select:none");
b.innerHTML="<div unselectable='on' onclick=f(event)>♥<div unselectable='on' style='position:absolute;top:0;width:100%;text-align:center;color:red;font:bold .1em/"+window.innerHeight+"px Arial;text-align:center;-moz-user-select:none'>js♥k";
			
setInterval(function(){ // loop
	for(a in t) {
		t[a].u()
	}
},16)