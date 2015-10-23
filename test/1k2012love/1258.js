c.width=c.height=201;
		with(c.style)width=height=innerHeight-25+'px';
		document.body.style.textAlign='center';
		var t=10,M=Math,e=M.random,f=M.floor,o=M.abs,X=Y=5,I=J=0;
		a.C=a.clearRect;
		a.F=a.fillRect;
		function m(x,y,i,j){
			var g=f((i-x)/t)-1,h=f((j-y)/t)-1;
			if(g && h){
				var u=((f(e()*(g-1))+1)*t)+x,v=((f(e()*(h-1))+1)*t)+y,r=f(e()*4);
				a.F(u,y,1,o(y-j));
				a.F(x,v,o(x-i),1);
				if(r!=0)
					d(u,y,u,v);
				if(r!=1)
					d(u,v,i,v);
				if(r!=2)
					d(u,v,u,j);
				if(r!=3)
					d(x,v,u,v);
				m(x,y,u,v);
				m(u,y,i,v);
				m(x,v,u,j);
				m(u,v,i,j);
			}
		}
		function d(x,y,i,j){
			var g=f((i-x)/t),h=f((j-y)/t);
			if(g){
				var u=(f(e()*g)*t)+x;
				a.C(u+1,y-2,t-1,4);
			}
			if(h){
				var v=(f(e()*h)*t)+y;
				a.C(x-2,v+1,4,t-1);
			}
		}
		m(0,0,200,200);
		a.strokeRect(.5,.5,200,200);
		a.fillStyle='red';
		a.fillText('❤',191,200);
		a.fillStyle='blue';
		onkeydown=function(E){
			I=J=0;
			W=E.which;
			if(W==87)
				J=-1;
			if(W==83)
				J=1;
			if(W==65)
				I=-1;
			if(W==68)
				I=1;
		}
		T=0;
		S=setInterval(function(){
			D=a.getImageData(X+I,Y+J,1,1).data;
			if(D[0]){
				alert('You found love in '+T/1000+' seconds.');
				clearInterval(S);
			}
			if(!D[3]){
				a.C(X,Y,1,1);
				X+=I;
				Y+=J;
				a.F(X,Y,1,1);
			}
			T+=20;
		},20);