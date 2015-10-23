c.setAttribute('width','1000');c.setAttribute('height','800');

lg=a.createLinearGradient(0,0,1000,800);  
lg.addColorStop(0,'#f00');  
lg.addColorStop(1,'#00f');  
  
a.fillStyle=lg;
a.fillRect(0,0,c.width,c.height);

a.shadowColor='#000';
a.shadowOffsetX=20;
a.shadowOffsetY=20;
a.shadowBlur=10;

a.font='99px sans';

a.lineWidth=9;
a.lineJoin='round';
int_=50;w=1000;v=800;Ax=0;Ay=0;Bx=w;By=0;Cx=w;Cy=v;Dx=int_;Dy=v;
a.beginPath();
for(i=int_;i<=v/2;i+=int_){ a.lineTo(Ax+i,Ay+i);a.lineTo(Bx-i,By+i);a.lineTo(Cx-i,Cy-i);a.lineTo(Dx+i,Dy-i);};
a.strokeStyle='#88f';
a.stroke();

a.fillStyle='#fff';
m=new Array('Love Me Tender','Love Me True','All My Dreams Fulfill','For My Darlin','I Love You','And I Always Will');
for(i=0;i<6;i++){a.fillText(m[i],65,(i+1)*100+80)};

a.fillStyle='#fff';
a.shadowOffsetX=0;
a.shadowOffsetY=0;
a.shadowBlur=0;
a.font='22px sans';

l=0;j=0;k=1;sl='';s();
function s() 
{
w=m[l].split(' ');
sl=sl+w[j]+' ';
a.fillText(sl,50,40);

t=setTimeout('s()',750);
j++;k++;
if(j>=w.length){j=0;l++};
if(l>=m.length){clearTimeout(t)};
}