var w=window.innerWidth,h=window.innerHeight,d=0,hrts=[],r=Math.random,m=Math.floor;
		c.width = w;
		c.height = h;

		function z(hrt,v,col) {
			hrt.z-=.5;
			a.save();
				a.fillStyle = hrt.c;
				a.font = 200/hrt.z+"px Arial"
				a.fillText('♥',hrt.x,hrt.y);
			a.restore();
			if(hrt.z < 1){col[v]=nH();}
		}
		function nH() {
			return {x:r()*w,y:r()*h,z:r()*100+75,c:'rgb('+m(r()*100+100)+','+m(r()*100)+','+m(r()*100)+')'};
		}

		setInterval(function(){
			d += 0.1;
			if(Date.now()%5 ==0 && hrts.length<200){hrts.push(nH())}
			a.clearRect(0,0,w,h);
			a.fillStyle = "#112233";
			a.fillRect(0,0,w,h);
			hrts.forEach(z);
		},30);