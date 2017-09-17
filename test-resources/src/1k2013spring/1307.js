var canvas = document.getElementsByTagName('canvas')[0];
var ctx = canvas.getContext('2d');
//Making the canvas filling the screen
canvas.width=width=screen.width;
canvas.height=height=screen.height;

//Translating the origin of the canvas to the center of the screen
ctx.translate(width/2,height/2);

//The smaller the offset is, the better
radial_offset = Math.PI/720;

//And now, we draw a stick and rotate the frame of a tiny angle over and over until we make a full circle
for(i=0;i<2*Math.PI;i+=radial_offset) {
		ctx.rotate(radial_offset);
		ctx.fillRect(-width,1,width,1);
}