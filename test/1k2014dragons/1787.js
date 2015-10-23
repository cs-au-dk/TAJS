u=[innerWidth/2,0];
touchEvents=function(e){
	e.preventDefault();
	if(e.targetTouches){
		e=e.targetTouches[0];
	}
	u[0]=e.clientX;
	u[1]=e.clientY;
};
b.onmousemove=touchEvents;
b.ontouchmove=touchEvents;

var ball=function(){
	this.g=.1;
	this.s();
}
ball.prototype.s = function(){
	this.x=u[0];
	this.y=u[1];
	this.vx=Math.random()*4-2;
	this.dy=Math.random()*4-1;
	this.r= Math.random()*4+2;
}
ball.prototype.draw = function(){
	this.x+=this.vx;
	this.y+=this.dy;
	this.dy+=this.g;
	if(this.y>=innerHeight || this.y<=0) {
		this.y-=this.dy*2;
		this.dy=-this.dy*Math.random()+.5;
		this.vx*=.8;
	}
	if(this.x>=innerWidth || this.x<=0) {
		this.x-=this.vx;
		this.vx=-this.vx*Math.random()+.5;
		this.dy*=.8;
	}
	hit=this.hitTest(d);
	if(hit) this.dy*=-.5;
	hit*=2;
	c.fillStyle='rgb('+Math.round(this.dy*100)+','+Math.round(this.vx*100)+',150)';
	c.fillRect(this.x,this.y,this.r+hit,this.r+hit);
	if(this.dy<.02 && this.dy>-.02) {
		this.s();
	}
}
ball.prototype.hitTest = function(obj){
	var vx = this.x - obj.x;
	var dy = this.y - obj.y;
	var distance = (vx * vx) + (dy * dy);

	var area = (this.r + obj.r)*(this.r + obj.r);
	return (area / distance)>1;
}
var z = [];
for (i=0;i!=1400;i++){
	z.push(new ball());
}

var m,i=0;
var dragon = function(x,y){
	this.r=Math.random()*40+20;
	this.x=x;
	this.y=y;
}

dragon.prototype.draw = function(){
	c.strokeStyle='#0f0';
	c.beginPath();
	c.arc(this.x,this.y,this.r,0,2*Math.PI);
	c.stroke();
	this.y+=Math.sin(i/300)/2;
	this.r+=Math.cos(i/300);
	if (this.r<1) this.r=1;
	i++;
}

d=new dragon(innerWidth*.5,innerHeight/2);
r = function(){
	c.globalAlpha=.2;
	w.drawImage(a,0,0,v.width,v.height);
	c.drawImage(v,0,0,a.width,a.height);
	c.fillStyle='rgb(20,10,20)';
	c.fillRect(0,0,innerWidth,innerHeight);
	d.draw();
	for(j in z) {
		z[j].draw();
	}
	requestAnimationFrame(r);
}
v=document.createElement('canvas');
v.width=a.width/8;
v.height=a.height/8;
w=v.getContext('2d');
requestAnimationFrame(r);