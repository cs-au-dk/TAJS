var $ = function(id) { return document.getElementById(id); },d = $('d');
var x = d.getContext('2d'),t,l = [],z=1,u = 1,r=20,f=Math;
 
function M(){
this.d = function(){

 if (this.J == 0) this.y+=u;
 else this.y-=u;

 x.fillStyle = '#f90';
 x.beginPath();
 x.arc(this.x,this.y,r,0,f.PI*2,1);
 x.closePath(); 
 x.fill();
 x.stroke();
 
 for (var j=0;j<l.length;j++)
  {
  if (((this.y+r))==l[j].y) 
     {
	  this.J = 1;
	  this.y = (l[j].y-r);
	 }
  if (((this.x-r)>=(l[j].H*50) && (this.x+r)<=(l[j].H*50)+50) && (this.y+r)==l[j].y) 
	  this.J = 0;
  }
};

this.J = 0;
this.x = 20;
this.y = 20;
}

function T(){
this.d = function(){
this.y-=u;
if (this.y < -1) l.shift();

x.fillStyle = '#000';
x.fillRect(0,this.y,300,4);
x.fillStyle = 'blue';
x.fillRect(this.H*50,this.y,50,4);
};

this.y = 500,this.H = f.floor(f.random()*6);
}



function L(){
x.clearRect(0,0,400,800);
z++;
if (z % 50 == 0) l.push(new T());
for (j=0;j<l.length;j++)
l[j].d();
o.d();

	  switch(z)
		{
		case 700:
		clearInterval(t);
		t = setInterval("L()", 40);
		break;
		case 1400:
		clearInterval(t);
		t = setInterval("L()", 30);
		break;
		case 2000:
		clearInterval(t);
		t = setInterval("L()", 20);
		break;
		case 3000:
		clearInterval(t);
		t = setInterval("L()", 10);
		break;
		case 5000:
		clearInterval(t);
		t = setInterval("L()", 5);
		break;
		}
		
	
};

onload = function(){
o = new M();
document.onkeydown = function(e){
v = e.keyCode;
if (v == 37) if (o.x>20) o.x-=10;
if (v == 39) if (o.x<280)o.x+=10;
if (v == 13) clearInterval(t);
};
l.push(new T());
t = setInterval("L()", 50);
}