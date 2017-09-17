c.width=600;		
M=Math
R=M.random
F=M.floor
T=a.translate
u=a.bezierCurveTo
V=a.globalCompositeOperation;

function H(x,y,s)
{
	this.x=x;
	this.y=y;
	this.s=s;
	
	this.draw=function(){
		x+=2+2*s;
		B=M.sin(x/20)*s*10+y;
		
		T.call(a,x,B);	
		q = [75,37,70,25,50,25,20,25,20,62.6,20,62.5,20,80,40,102,72,120,110,102,130,80,130,62.5,130,62.5,130,25,100,25,85,25,75,37,75,40].map(function(a){return(a*s)});
		
		a.moveTo(75*s,37*s);
		g = a.createRadialGradient(45*s,45*s,10*s,52*s,50*s,30*s);
		z = g.addColorStop
		z.call(g,0,'#A55');
		z.call(g,0.9,'#A11');
		z.call(g,1,'#A00');
		a.fillStyle = g;
		a.shadowOffsetX=a.shadowOffsetY=a.shadowBlur=5*s;
		a.shadowColor='rgba(0,0,0,0.5)';
	
		for(i=0;i<6;i++) u.apply(a,q.splice(0,6));
			
		a.fill(x*s,y*s);  
		T.call(a,-x,-B);	
		x>c.width?x=-s*130:0;
	}
}

h=[];
for(i=0;i<10;i++)h.push(new H(-300+F(R()*900),-200+F(R()*300),R()*2));
setInterval(function(){
		c.width=c.width;
		h.forEach(function(f){
				
				a.beginPath();
				f.draw();
				a.stroke();
				a.font='bold 50px verdana';
				a.fillStyle='#FFF';
				a.shadowColor='rgba(0,0,0,0)';
				
				a.fillText("I LOVE YOU ALL!",90,100);
			})
	},100)