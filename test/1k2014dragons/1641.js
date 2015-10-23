M=Math;W=a.width;H=a.height;
X=W/2;Y=H/2;t=0;r=M.random;

B=[0,0,3,-1,-3,-4,10,0,10,5,0,5];
O=[];

b.bgColor="#000";
b.onmousemove=function(e){X=e.pageX;Y=e.pageY}
for(i=S=20,a=[];i--;a.push({x:-10*i,y:H/2,a:0})){}
c.fillStyle="#FFF";
c.globalCompositeOperation="xor";


g=function(){return {x:~~(r()*(W-50)),y:-100,w:~~(r()*90)+10,h:10}}

iv=setInterval(function(){with(c){

	for(clearRect(0,0,W,H),beginPath(),i=a.length;i--;){
		b=a[i];n=a[i-1];s=(S-i+5)/5;
		d=M.atan2(y=(i?n.y:Y)-b.y,x=(i?n.x:X)-b.x);
		e=M.sqrt(x*x+y*y);

		moveTo(b.x,b.y);save();translate(b.x,b.y);rotate(d);
		for(j=B.length;j-=2;){lineTo(B[j]*s,B[j+1]*s)}
		if(!i){moveTo(10*s,0);lineTo(15*s,0);lineTo(15*s,3*s);lineTo(10*s,5*s);fillRect(S,0,5,5);}
		restore();

		if(e<9*s){continue}
		b.x+=M.cos(d)*9;b.y+=M.sin(d)*9;
	}

	fill();fillRect(0,0,W,H);

	for(i=(O=O.filter(function(v){return v})).length,d=0;i--;){
		b=O[i];fillRect(b.x,b.y,b.w,b.h);
		d=M.max.apply(this,getImageData(b.x,b.y,b.w,b.h).data);
		if(d||b.y>H)O[i]=0;if(d){break}
		b.y+=5;
	}

	if(d){clearInterval(iv);confirm("Score: "+(t/16)+"\nReplay?")&&top.location.reload()}fillRect(0,0,W,H);
	if(t++%60==0){O.push(g())}
	
}},16);