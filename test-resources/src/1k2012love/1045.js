// full source at https://github.com/darklight721/JS1K/blob/master/src.js
// Declare variables
var	t = ["Love.","Liebe.","Amor.","Cinta.","Pagibig.","愛。"],// texts
	p = 5, 		// pixel size
	s = 2,		// spacing between pixels
	img = [],	// image data
	r,g,v,		// rgb colors
	i=0,h,m;	// incrementors

// Set canvas size
c.width = 700;
c.height = 448;

// Set background color
b.style.backgroundColor = "#000";

// Format font
a.font = "200px Georgia";
a.textBaseline = "middle";
a.textAlign = "center";

for (var j = 0; j < t.length; j++)
{
	// Draw text
	a.fillStyle = "#fff";
	a.fillText(t[j],c.width/2,c.height/2);

	// Save image data
	img[j] = a.getImageData(0,0,c.width,c.height);
	
	// Clear the canvas
	a.clearRect(0,0,c.width,c.height);
}

// Get a random value between 0 to n
var getRandomValue = function(n){
	return Math.floor(Math.random()*n);
};

// Pick a random rgb color
var randomColor = function(){
	r = getRandomValue(256);
	g = getRandomValue(256);
	v = getRandomValue(256);
};

// Tests whether a pixel contains a text
var isTextOnPixel = function(w,x,y) {
	for (var i = 0; i < p;i++)
	{
		for (var j = 0; j < p; j++)
		{
			var k = ((y+i)*img[w].width*4) + ((x+j)*4);
			if (img[w].data[k] !== 0 || img[w].data[k+1] !== 0 || img[w].data[k+2] !== 0)
			{
				return 1;
			}
		}
	}
	return 0;
};

m = Math.ceil(img[0].width/(p+s)); // max number of pixelated columns

// Start rendering
(function render(){

	window.setTimeout(function(){
	
		if (i === 0) // only pick a random color once the whole canvas is painted with the current color
		{
			h = getRandomValue(t.length); // get random index of texts
			randomColor();
		}
		
		for (var y = 0; y < img[h].height; y+=p+s)
		{
			for (var x = 0; x < i*(p+s); x+=p+s)
			{
				if (isTextOnPixel(h,x,y) === 1)
				{
					// if pixel contains text, get the opposite color
					a.fillStyle = "rgb("+(255-r)+","+(255-g)+","+(255-v)+")";
				}
				else
				{
					a.fillStyle = "rgb("+r+","+g+","+v+")";
				}
				// Draw rect here
				a.fillRect(x,y,p,p);
			}
		}
		
		i = (i+1) % m; // increment
		
		render();
	},1);
})();