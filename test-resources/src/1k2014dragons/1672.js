with(Math) {
P = function (r,p,q) {
		x = sin(p);
		y = cos(p);
		u = sin(q);
		v = cos(q);
		return[r[0]*y*v+r[1]*x*v-r[2]*u,r[1]*y-r[0]*x,r[0]*y*u+r[1]*x*u+r[2]*v];
}

//vector cross product
C = function (p,q) {
	n=[];
	n[0] = p[1]*q[2]-p[2]*q[1];
	n[1] = p[2]*q[0]-p[0]*q[2];
	n[2] = p[0]*q[1]-p[1]*q[0];
	return n;
}

//subtract vectors
S = function (p,q) {
	r=[];
	for(x in p)r[x]=p[x]-q[x];
	return r;
};

(K=function(D){
s=99;I=D+1;O=2*D*I;
t=[];f=[];for(i=2*D;i--;){
	for(j=I;j--;){
		t.push(P([0,-s,0],PI*j/D,PI*i/D));
		if(j){
			G=(i+1)%(2*D)*I;
		    f.push([G+j,i*I+j,G+j-1]);
		    f.push([G+j-1,i*I+j,i*I+j-1]);
		}
	}
}
	 //compute face normals
	for(i in f) {
		x=C(S(t[f[i][0]],t[f[i][2]]),S(t[f[i][0]],t[f[i][1]]));
		y=sqrt(x[0]*x[0]+x[1]*x[1]+x[2]*x[2]);
		t.push([x[0]/y,x[1]/y,x[2]/y]);	
	}	
})(D=8);

b.onkeyup = function (e) {
	e=e?e.which:event.keyCode;
	m+=(e==37|e==39?.01*(e-38):0);
	l+=(e==38|e==40?.01*(e-39):0);
	K(D=(e>48&e<58)?(e-47):D);

}

for(i in c) {
	c[i[0]+(i[6]||'')]=c[i];
}

o=h=T=0;
l=m=.01;

W=a.width;
H=a.height;	

(M = function() {

	a.width=W

    A=sqrt(-2*s+H)
    B=T%(2*A)-A
    c.ta(abs(5*T%(2*W)-W),s+B*B-A)
	c.ce(W/-2,H/-2,W,H)
	
	q=[];
	for(i in t) {

		q[i]=P(t[i],o,h);
		// project to screen space
		g=4*s/(q[i][2]-4*s);
		q[i][0]*=g;
		q[i][1]*=g;
				
	}

	for (i in f) {
		J=[q[f[i][0]],q[f[i][1]],q[f[i][2]]];
		if(C(S(J[1],J[0]),S(J[2],J[0]))[2]<0) {

			c.fillStyle=("hsl(0," + (((.5*i/D|0)+(i/2)%D)%2|0)*s + "%," + (s*q[+i+O][2]) + "%)");			
			c.ba();
			c.m(J[0][0],J[0][1]);
			c.l(J[1][0],J[1][1]);
			c.l(J[2][0],J[2][1]);	
			c.fill();
		}
	}

	o+=l;
	h+=m;
	T+=.5;

	requestAnimationFrame(M);

})();

};