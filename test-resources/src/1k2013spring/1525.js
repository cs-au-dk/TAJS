P=[]//particles
S=[]//start positions
M=Math
w=c.width=window.innerWidth
h=c.height=window.innerHeight
f=0.95//friction
v=0.5//max speed
r=M.random
s="Type & press ENTER"
u=n=0
b.style.overflow="hidden"

//initialize a particle
function I(p)
{
	//get a start position
	g=2*M.floor(r()*S.length/2);
	p.x=S[g]
	p.y=S[g+1]
	
	//lifetime
	p.t=p.T=r()*r()*200
	//velocity
	p.X=p.Y=0
	//color
	p.h=80+r()*50
	p.s=100*r()
	p.l=100*r()
}

setInterval(function ()
{
	for(i=0;i<n&&u;i++)
	{
		p=P[i]
		//set velocity
		p.X=f*p.X+v*(r()*2-1)
		p.Y=f*p.Y+v*(r()*2-1.2)
		
		a.beginPath()
		a.lineCap="round"
		a.lineWidth=5*(p.t/p.T)
		a.strokeStyle="hsl("+p.h+","+p.s+"%,"+p.l+"%)"
		a.moveTo(p.x,p.y)
		a.lineTo(p.x+=p.X,p.y+=p.Y)
		a.stroke()
		--p.t<0?I(p):0//reset when lifetime is under 0
	}
},30)

b.onkeyup=function(e)
{
	k=e.keyCode
	c.width=c.width
	u=0
	//[a-z] space
	if(k==32||(k>64&&k<91))s+=String.fromCharCode(k)
	//backspace
	if(k==8)s=s.substr(0,s.length-1)
	//draw text
	a.font="bold "+(0.2*h)+"px sans-serif"
	a.fillText(s,M.max(0,0.5*(w-a.measureText(s).width)),0.7*h,w);
	if(!n)s="",n=300;
	//enter
	if(k==13)
	{
		//compute start positions
		d=a.getImageData(0,0,u=w,h).data
		for(S.length=i=0;i<d.length/4;i++)
			if(d[4*i+3]) S.push(i%w,M.floor(i/w))
		//init particles
		for(i=0;i<n;i++)I(P[i]={})
	}
}
b.onkeyup({})//init text