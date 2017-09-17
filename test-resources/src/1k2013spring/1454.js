// start of submission //
c.width=c.height=w=410;W=w/2;cx=cy=h=99;cp=Z=0;ca=.9;hD=[];d=[];X=-20;
c.onmousemove=function(e){X=e.clientX-W;Y=e.clientY-W}
setInterval(function(){
	cx+=Math.cos(ca+=X/w/9);cy+=Math.sin(ca);h+=cp=h<4?.1:cp-Y/w/9; // Integrate movement
	if(hD[(cx|0)+(cy|0)*w+W]/7>h)cp=1; // Collision detection
	for(x=w;x--;){ // Raycasting
		L=w;R=ca+Math.asin((x-W)/w);
		for(y=700;y>W;y--){
			T=w*h/(y-W);tX=cx+T*Math.cos(R)|0;tY=cy+T*Math.sin(R)|0;i=tX<0||tY<0||tX>w||tY>w?0:tX+tY*w+W;k=hD[i];o=k*50/T|0;N=y-o<0?0:y-o;
			if(N<L){s=T/w;l=s>1?0:U/s;
				for(o=L-N,L=N;o--;){j=(x+N*w+o*w)*4;D[j+3]=l;D[j]=U;
					if(!k||o){D[j]=k==U?99:k&&!(o%9)?U:d[i*4];D[j+1]=!k?99:d[i*4+1];D[j+2]=i?d[i*4+2]:W;D[j+3]=o&&o<w/T&&L>1?U:i?l+=Math.cos(s):Math.sin(R*T)>.8?l*.8:l}}}}}
	for(i=3;i<w*w*4;i+=4)if(!D[i]){D[i]=(w*w*4-i)/w/9*(i/4%w)/i*3*w;D[i-1]=W;D[i-2]=D[i-3]=cx} // Sky
	a.putImageData(I,0,0);while(i--)D[i]=0;
	Z++;Z%=w;for(i=9;i--;){o=5+U*i+Z*w;if(Z<w-1)hD[o]=9;hD[o-w]=0} // Cars
	},U=Y=40)
for(i=w*w;i--;){ // City creation
	x=i%w;y=i/w;
	z=y%U>16&&x%U>11?Math.abs(Math.cos(x/U|0)/Math.cos(y/U|0)*h):0;hD[i]=z<U?0:x%74>U&&y%75>U?z/ca:z; // Buildings
	d[i*4]=d[i*4+1]=d[i*4+2]=(x-3)%U<4||(y-3)%U<9?h:x%8<2||y%9<2?0:Math.cos((x/8|0)+(y/9|0))<ca?hD[i]&&x%W>h&&y%W<h?U+h:U:0; // Tiling and roads
	if((x-8)%U>38&&(y-2)%U>38)hD[i+1]=U} // Traffic signs
I=a.getImageData(0,0,w,w);D=I.data;
// end of submission //