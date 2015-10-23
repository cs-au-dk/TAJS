// start of submission //

P=J=I=0;
b.style.overflow = "hidden";
W=window;
w=c.width = W.innerWidth;
h=c.height = W.innerHeight;
D=50; // natural diameter
M=Math;
//R=2*M.PI;
f=M.floor;
n=f(w/D); //number of columns
m=f(h/D); //number of rows
P=J=I=0; //the column and row the mouse is on
var i, j, t, T=[];
/**
* @constructor
*/
function cell(i,j)
{
	var self = this;
	self.p = 0; //natural position
	self.v = 0; //velocity
}

function E(i,j)
{
	T[i][j].p+=1.2;
	var X="";
	for (var k=0; k<1599; k++)
	{
		X+=String.fromCharCode(128+f(M.exp(-k/400)*i*127/m*M.sin(.3455*k*M.pow(2,(2*j-11)/n))));
	}
	var audio = new Audio('data:audio/wav;base64,UklGRmQGAABXQVZFZm10IBAAAAABAAEAQB8AABAAAAABAAgAZGF0YUAGAACA'+btoa(X));
	//slightly incorrectly encoded header to wav file to get it to work in opera. Should be UklGRmQGAABXQVZFZm10IBAAAAABAAEAQB8AAAAAAAABAAgAZGF0YUAGAACA Not really sure why it works, but it does.
	audio.play();
	return 1;
}

//what to do on mouse click
c.onmousedown = function(e)
{
	P=1;
	I=f(e.pageY/D);
	J=f(e.pageX/D);
	I<m && J<n && E(I,J);
}

//what to do on mouse unclick
c.onmouseup = function(e)
{
	P=0;
}

//what to do when the mouse moves
c.onmousemove=function(e)
{
	i=f(e.pageY/D);
	j=f(e.pageX/D);
	if(i<m && j<n && (I != i || J !=j))
	{
		I=i;
		J=j;
		P && E(I,J);
	}
}


//update the T function
function update()
{
	M.random()<.1 && E(f(M.random()*m),f(M.random()*n));
	//calculate the velocity of a point based on the current velocity and positions of surrounding points
	for(i = 0; i<m; i++)
	{
		for(j=0; j<n; j++)
		{
			T[i][j].v += (T[i-1][j].p+T[i][j-1].p+T[i+1][j].p+T[i][j+1].p)/4-T[i][j].p;
		}
	}
	//update the position of the points based on current position and velocity. 0.95 is a scaling factor to make everything settle down.
	for(i = 0; i <m; i++)
	{
		for(j=0; j < n; j++)
		{
			T[i][j].p += T[i][j].v;
			T[i][j].p *=.95;
		}
	}
	draw(); //draw after the update
}


//draw the T function
function draw()
{
	a.clearRect(0, 0, w, h);
	a.save();
	for(i = 0; i<m;i++)
	{
		for(j=0;j<n;j++)
		{
			a.fillStyle="rgba(0,35,"+15+f(240*M.max(T[i][j].p,0))+",.5)";
			a.beginPath();
			a.arc(j*D+D/2,i*D+D/2,M.abs(T[i][j].p)*D+D/2,0,2*M.PI,1);
			a.closePath();
			a.fill();
		}
	}
	//draw the red circle where the mouse is
	a.strokeStyle="blue";
	a.lineWidth=5;
	a.beginPath();
	a.arc(J*D+D/2,I*D+D/2,M.abs(T[I][J].p)*D+D/2,0,2*M.PI,1);
	a.closePath();
	a.stroke();
	a.restore();
}


main();

function main()
{
for(i = -1; i <=m; i++)
{
	T[i] = new Array(n);
	for(j=-1; j <=n; j++)
	{
		T[i][j]=new cell(i,j);
	}
}

setInterval(update, 33);
}
// end of submission //