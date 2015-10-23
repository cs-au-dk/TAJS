// Welcome in my js1k program ! :D 
//some var declarations
 var W = c.width = 0, H = c.height = 32,p = {}, y = {}, l={"true":"purple","false":"grey","!":"gold"},D = document.createElement("div");
function k(e) // return the key pressed
{
	return e.keyCode;
}
function u(e) // when a key is unpressed
{
	var s = k(e);
	p[s]=false;
	t();
}
function n(e) // wen a key is pressed
{
	var s = k(e);
	p[s]=true;
	t();
}
function t() // visual render
{
	c.width = W;
	var j = 0, d = [],q = g(p);
	for(var i in p)
	{
		c.width += 32;
		if(i != q)
		{
			d.push([  l[p[i]],y[i],i  ]);
		}
		else
		{
			d.push( [ l['!'], y[i],i ]);
		}
	}
	for(var h=0; h < d.length; h++)
	{
		if(d[h][0])
		{
			a.fillStyle = d[h][0];
		}
		a.fillRect(h*32,0,32,32);
		a.font = "20px Arial";
		a.textBaseline = "top";
		a.fillStyle = "black";
		a.fillText(String.fromCharCode((d[h][2])),h*32+8,0);
		a.fillText(d[h][1],h*32+8,16,20);
	}
}
function g(p) // calculate the score
{
	var r = [];
	for(var i in p)
	{
		if(p[i])
		{
			r.push(i);
		}
		if(!y[i])
		{
			y[i]=0;
		}
	}
	if(r.length == 1)
	{
		y[r[0]]++;
		return r[0];
	}
	return false;
}
t(); // start
window.addEventListener("keydown",n); // key press event
window.addEventListener("keyup",u);// key unpressed event
// show game description and rules
D.innerHTML = "<div><strong>1Keyboard 100Friends</strong> - Play with your friends !<br/>Each has a key and the only who press earns points.<br/>Use your mental and physical skills!</div>";
b.appendChild(D);