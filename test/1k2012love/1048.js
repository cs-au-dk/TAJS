var g=30,z=[],f=Math.floor,x,y,t,l=0,n=[-9,-8,-7,-1,1,7,8,9],tn=[-8,-7,1,8,9],bn=[-9,-8,-1,7,8],d=0,v;
		c.width = 8*g;
		c.height = 8*g;

		function rC(q,i,r,e) {
			x=f(i/8);y=i%8;
			if(e&&q.s!='d'){q.s='d';d++;if(d==56){alert('You Win');u();return;}}
			if(e&&q.m){alert('You Lose');q.s='m';u();return;}
			
			a.clearRect(x*g,y*g,g,g);
			a.fillStyle = '#833';
			switch(q.s) {
				case 'd':
					a.fillText(q.c,x*g+10,y*g+12);
					break;
				case 'u':
					a.fillRect(x*g+2,y*g+2,g-4,g-4);
			}
			if(e&&q.c==0){
				rq(cf,i);
			}
		}

		function cf(c,i) {
			c.forEach(function(q){v=q+i;if((v>-1&&v<64)&&z[v].s!='d'&&!z[v].m){rC(z[v],v,[],true);}})
		}
		function pf(c,i) {
			c.forEach(function(q){v=q+i;if(v>-1&&v<64){z[v].c++;}});
		}
		function rq(q,l){
			if(l%8==0)q(tn,l);
			else if(l%8==7)q(bn,l);
			else q(n,l);
		}
		function pl(){
			l=f(Math.random()*63);
			if(z[l].m) 
				return pl();
			return l;
		}
		function u() {
			z=[];d=0;
			for(var i = 0; i < 64; i++){z.push({s:'u',c:0,m:0})}
			for(var i = 0; i < 8; i++){l=pl();z[l].m=1;rq(pf,l);}
			z.forEach(rC);
		}
		
		u();
		c.onmousedown = function(e){t=f(e.clientX/g)*8+f(e.clientY/g); rC(z[t],t,[],true);};