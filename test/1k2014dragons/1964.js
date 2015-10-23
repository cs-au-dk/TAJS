/**
 * This is the code I had before I started minifying by hand.
 * I changed some values during my final tweaks but I think this source gives you a rough idea of how I've set things up.
 *
 * If you have any questions just send me a tweet: @rvmook
 **/

function drawSquare(charString,offsetX,offsetY,fakeDirection){


	fakeDirection=fakeDirection||direction;
	i=0;
	while(square=charString.match(/\w\d*/g)[i++]) {
		c.fillStyle=objReference[square[0]];
		c.f((+square[1]*fakeDirection+((fakeDirection-1)*-5)+offsetX)*4,4*(+square[2]+offsetY),(fakeDirection*((square[3]|0)+1))*4,4*((square[4]|0)+1))
	}

	return drawSquare
}

worldOffset=isJumping=velY=velX=destY=posY=0;

objReference={

	//key handling
	37:-1,
	39:counter=direction=1,
	32:'if(!isJumping)isJumping=velY=-4;0',

	// colors
	b:'#000',
	w:'#fff',

	// tiles
	1:'057',
	2:'1702w26w354w542w63b64',
	3:'6702w56w054',
	4:'057w022w1104b12w432w5202b53w34',
	5:'057w1004w012b11'
};



b.onkeyup=b.onkeydown=function(e){
	direction=(velX=e.type[6]?eval(objReference[e.keyCode])^0||velX:e.keyCode>36?0:velX)||direction;
}

c.f=c.fillRect;
feetPart='w18w7';
tiles='00245300241115430000254300243';
setInterval(function(){

	cWidth=a.width/4;
	cHeight=a.height/4|0;
	worldWidth =Math.floor(Math.max(tiles.length*8,cWidth+32)/8);
	SWIM_DEST=cHeight/2-6;

	// hit detection

	newWorldOffset = worldOffset+velX;
	actualX=Math.floor(1+(posX+newWorldOffset)/8)%worldWidth;
	while(actualX<0) actualX = worldWidth+actualX;


	isWater=!+tiles[actualX];

	//if in water, don't move if next tile == land
	if(isWater && posY==SWIM_DEST || posY < SWIM_DEST) {
		worldOffset=newWorldOffset;
		destY=isWater?SWIM_DEST:SWIM_DEST-11;
	}

	posY+=velY++;
	if(posY>destY&&velY>0) {
		isJumping=velY=0;
		posY=destY;
	}


	// clear (last draw thing should make fill color black
	c.f(index=0,0,cWidth*4,cHeight*4);

	// robot
	drawSquare('w1063b4101b6101'+['w29w69',feetPart+7,feetPart+8,feetPart+9][step=isJumping?2:velX?counter%4:0],posX-4,ry=+'6645'[step]+posY)
		('w106w2142b51b421',posX-4,+'5455'[step]+ry)
		('w00w80',posX-4,+'7676'[step]+ry);

	// world
	while(index<worldWidth){
		tileX=index++*8-worldOffset;
		while(tileX<-16)tileX+=worldWidth*8;
		while(tileX>cWidth+8)tileX-=worldWidth*8;
		eval('drawSquare(\"b0777w'+(objReference[tiles[index]]||['081w271w481w671','291w081w691w481'][+(counter%18<9)])+'b\",tileX,cHeight/2-5,1)');
	}


	counter++;
},posX=90);