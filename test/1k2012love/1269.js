// Score is on the topleft, and lives left are on the topright.
// I hope you enjoy my first javascript game :)
//The code below is slightly different, I did some last minute change to the minimized code to make it opera/safari/chrome friendly

W=c.width=c.height=600;

// creates abbreviations for context functions
for($ in a)a[$[0]+($[6]||'')]=a[$];

M=Math;
I=M.PI;
p=P=[];
Q=S=T=p.v=p.V=0;
p.x=p.y=W/2;

q=20;
z=40;
w="px A";
U=3;

//friction
F=0.85;

document.onmousemove=function(e){J=e.pageX-c.offsetLeft;K=e.pageY-c.offsetTop};

A="M.ceil(M.random()*(W-z))";
b=eval(A);
e=eval(A);

setInterval(function(){
	with(a){
		// had to put this alert on top because of some differences in the browsers
		if(Q){alert("Again?");Q=0}
		fillStyle="#9CF";
		fc(0,0,W,W);
		
		// free letters (B,C,Z)!

		P=p;
		t=80;

		E=P.x-J;
		D=P.y-K;
		d=M.sqrt(E*E+D*D);
		E/=d;
		D/=d;
		
		L=b-p.x;
		N=e-p.y;
		O=M.sqrt(L*L+N*N);
		// Check for collision between heart and arrow
		T+=1;
		if(O<z&T>0|T==0){
			b=eval(A);
			e=eval(A);
			T>1?S+=1:0;
			T=0
		}

		// Set max speed //
		d>W?d=W:0;

		// Moves towards the mouse //
		H=d/70;
		P.v-=E*H;
		P.V-=D*H;

		// Slows it down //
		P.v*=F;
		P.V*=F;

		// Get the playerobject to rotate towards the mouse //
		x=p.x;
		y=p.y;
		K>y?D=K-y:D=y-K;
		J>x?E=J-x:E=x-J;
		R=M.atan2(D,E)*180/I;
		c=1;
		// The part below are 4 if's minimized into one nice little sentence //
		K>y?R+=90:R-=90;J<x&K>y?c*=-1:0;J>x&K<y?c*=-1:0;

		R/=180/I*c;
		p=P;
		G=P.x+P.v;
		g=P.y+P.V;
		P.x=G;
		P.y=g;

		fillStyle="#F00";
		
		// Save, translate, rotate, translate, draw, restore
		ba();
		save();
		ta(x,y);
		rotate(R);
		ta(-x,-y);

		// Draw the arrow
		fc(G-1,g-30,2,60);
		font=12+w;
		fx("▲",G-6,g-25);
		
		// Draw the string
		m(G-z,g);
		bC(G+z,g+z,G-z,g+z,G+z,g);
		stroke();
		
		// Draw the bow
		//fillStyle="#C60"; //dont have room for this code, sadly.
		font=94+w;
		fx("︵",G-47,g+9);

		re();
		// END Save, translate, rotate, translate, draw, restore
		
		// Print the score
		font=z+w;
		fx(S,9,z);
				
		// update lives
		T>z?U-=1:0;
		
		// Print lives
		fx(U,570,z);

		// Set the timer
		if(T>z|T<0){
			T>z?T=-z:0;
			t=20
		}
		// Draw the heart
		font=t+w;
		fx("♥",b-q,e+q);
		
		if(U<1){Q=U=3;S=0;T=-1}
	}

},q)