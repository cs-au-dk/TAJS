b.onclick=function(){v=v?clearInterval(v):setInterval(f,16)}
c.strokeStyle="#0ff";c.textBaseline="top";c.lineWidth=.5;
M=Math;S=M.sin;C=M.cos;W=a.width;H=a.height;
r=.7;t=15;P=M.PI/2;

function g(b,a){
	b={x:b.x+C(b.a+a)*b.l*r,y:b.y+S(b.a+a)*b.l*r,l:b.l*r,a:b.a+a};
	c.moveTo(b.x,b.y);c.lineTo(b.x-b.l*C(b.a),b.y-b.l*S(b.a));
	return b;
}

v=setInterval(f=function(){
	c.fillStyle="#000";c.fillRect(0,0,W,H);c.beginPath();
	i=15;t+=.01;a=.4-C(t)*P;d=(S(t)+1.4);
	bs=[{x:W/2,y:H/2,l:150,a:-P}];
	while(i--){nbs=[];while(b=bs.pop()){nbs.push(g(b,-a*.5/d),g(b,a*d/.5))}bs=nbs}
	c.stroke();c.fillStyle="#fff";c.fillText("click to pause",9,9)
},16)