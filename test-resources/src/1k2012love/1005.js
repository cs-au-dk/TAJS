d=e=1;
f=oy=u=0;

q=50;
r=25;
o=62;
k='#fff';
m=Math;
mr=m.random;
mp=m.PI;
l=830;
n=480;
z=[];
w=[];

	for(i=0;i<20;i++){
		z[i]=[];
		for(j=0;j<15;j++){
			z[i][j]=(mr()*(0xFFFFFF+1)<<0).toString(16);
			while(z[i][j].length<6)z[i][j]+='0';
		}
	}
	w=[1+0|mr()*(14),0|mr()*(18)];
    c.width=l;
    c.height=n;
	drawLayer();

document.onkeydown=function(x){
    E=x.keyCode;
    if(!u)
    ((E==39||E==68)&&d+1<15)?d++:((E==37||E==65)&&d-1)?d--:((E==40||E==83)&&e+1<20)?e++:((E==38||E==87)&&e-1>=0)?e--:(E==32&&f++)?z[e][d]=0:0;
    h();
}
function h(){
	drawLayer();
	ox=(e%2)?0:r;
	a.beginPath();
	a.strokeStyle=k;
	a.arc(d*q+ox+r,e*17.6+oy+o,r/2,0,2*mp,0);
	a.stroke();
}
function drawLayer(){
	x=22;
	a.fillStyle='#111';
	a.fillRect(0,0,l,n);
	for (i=0;i<20;i++){  
        for (j=14;j>0;j--){
			ox=(i%2)?0:r;
			if(!z[w[1]][w[0]]){
				a.fillStyle=k;
				a.font='30px serif';
				a.fillText('Love after '+f+' dates',290,250);
				u=1;
				break;
			}
			if(z[i][j]){
				cl=z[i][j];
				dl=(('0x'+cl & 0xFEFEFE) >> 1).toString(16);
				while(dl.length<6)dl='0'+dl;
				a.fillStyle='#'+dl;
				t=(j*q)+ox;
				s=(i*x)*.8+oy;
				a.beginPath();
				a.rect(t+r/2,s+o,r,10);
				a.arc(t+r,s+74,r/2,0,2*mp,0);
				a.fill();
    			a.fillStyle='#'+cl;
				a.beginPath();
				a.arc(t+r,s+o,r/2,0,2*mp,0);
				a.fill();
			}
		}
	}
}