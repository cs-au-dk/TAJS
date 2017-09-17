// inspired by a picwin ( mIRC ) contest i saw many years ago
// the contest was a 512b picwin snippet. 
// source : http://scriptsdb.org/comments.php?id=1125
b.bgColor='#000';
var L="♥", 
	iWidth = c.width = window.innerWidth,  // canvas width
	iHeight=c.height=window.innerHeight, // canvas height
	oPos = { x : 50, y : 50 }, // heart position
	oSize = { w : 30, h : 50 }, // thing width and height
	iLum = 255, // luminosity
	aThings=[], // all things container

	oCurrentEllipsis = { x: 130, y : 200},
	oNextEllipsis = { x: 130, y : 200},
	iStep=0;

// calc all things positions
var m = -oSize.w, q =0; // m and q are x and y pos for things
while(m<iWidth||q<iHeight)
{
	aThings.push( { x : m, y : q } ); // add a thing
	if (m>iWidth) { // if we are at the max right
		m=-oSize.w; // return to left
		q+=oSize.w*1.5; // next line
	}
	m+=oSize.h;
}
function process() {
	// move the heart
	fnMove();

	// pick just visible things
	var aVisible = [];
	for(var i = aThings.length; i--; ) 
	{
		var oCurrent = aThings[i], 
			hypo = Math.sqrt(Math.abs(Math.pow(oPos.x-oCurrent.x,2)+Math.abs(Math.pow(oPos.y-oCurrent.y,2)))); // calc dist from thing to heart
		if (hypo<iLum+oSize.h && hypo>oSize.w) { // if thing is not too far (255px+thingHeight) and not too close ( thig width )
			aVisible.push({dist:hypo, x:oCurrent.x,y:oCurrent.y}); // set thing as visible
		}
	}
	// sort thing by dist to have a "pretty" print
	aVisible.sort(sortByDist);

	with(a) { // use canvas
		clearRect(0, 0, iWidth, iHeight); // clear canvas
		for(i=aVisible.length;i--;){ // for all visible things
			// save current canvas state to manipulate it
			save(); 
			var oVisible = aVisible[i],
				radius = Math.acos((oPos.y-oVisible.y)/oVisible.dist); // calc horizontal radius between thing and heart

			radius *= (oPos.x-oVisible.x>0) ? -1 : 1;
			translate(oVisible.x,oVisible.y); // translate the canvas
			rotate(radius); // rotate canvas 

				// calc luminosity for this thing
			var iCurrentLum = -Math.floor(oVisible.dist-iLum);
			if ( iCurrentLum > iLum ) { // avoid luminosity > 255
				iCurrentLum = iLum;
			} else if (iCurrentLum < 0 ) { 	// avoid luminosity < 0 
				iCurrentLum = 0; 
			}
			// draw the thing
			beginPath();
			lineWidth=oSize.w;
			lineCap='round';
			moveTo(0,0);
			lineTo(0,oSize.h);
			strokeStyle='rgba('+iCurrentLum+',0,0,1)';
			stroke();
			closePath();

			// restore state
			restore();
		}

		//draw the heart
		textAlign="center",
		font="42px a",          
		fillStyle="red",        
		fillText(L,oPos.x,oPos.y);
	}

	//loop process
	setTimeout(process, 33);
};

// function to sort thing by DESC dist
function sortByDist(a, b){
	return b.dist-a.dist
};

// function which set new heart coord "randomly" around an elipse
function fnMove() {
	// check if we are at the new coords
	if (oCurrentEllipsis.x==oNextEllipsis.x) {
		oNextEllipsis.x= Math.round(Math.random()*iWidth/2);
	}

	if ( oCurrentEllipsis.y==oNextEllipsis.y ) {
		oNextEllipsis.y= Math.round(Math.random()*iHeight/2);
	}

	//set new dir.x
	if ( oCurrentEllipsis.x < oNextEllipsis.x ) {
		oCurrentEllipsis.x++;
	} else {
		oCurrentEllipsis.x--;
	}

	// set new dir.y
	if ( oCurrentEllipsis.y<oNextEllipsis.y ) {
		oCurrentEllipsis.y++;
	} else {
		oCurrentEllipsis.y--;
	}
	// we change the radius counting on steps
	radius=iStep/30;
	iStep++;

	// the point where we are suppose to go next
	var dir = { 
		x : Math.cos(radius)*oCurrentEllipsis.x+iWidth/2,
		y : Math.sin(radius)*oCurrentEllipsis.y+iHeight/2
	};

	// check if we change direction
	var oDiff = { 
		x : oPos.x-dir.x,
		y : oPos.y-dir.y
	};

	if ( oDiff.x != 0 || oDiff.y !=0 ) {
		oDiff.x*=-1;
		oDiff.y*=-1;
	}

	oPos.x += oDiff.x;
	oPos.y += oDiff.y;
};
process();