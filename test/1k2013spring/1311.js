/**
*	@author Felipe Alfonso
*	Just a simple ray casting demo
*	709 bytes the compressed version
*	1487 bytes the uncompressed version
*	http://shin.cl/
*/

//	Make a canvas.
var tc=document.createElement("canvas").getContext("2d");
//	Set main canvas size.
c.width=860,c.height=480;
//	Set created canvas size.
tc.canvas.width=43,tc.canvas.height=30;
//	Write the secret word, not so secret now.
tc.fillText("SPRING",2,16);
//	Hide cursor.
c.style.cursor="none";
//	Mouse move event.
c.addEventListener("mousemove",function(e){
	var mx = ((e.pageX-c.offsetLeft)/20)|0,my = ((e.pageY-c.offsetTop)/20)|0; //	Scaled mouse coords.
	a.webkitImageSmoothingEnabled = a.mozImageSmoothingEnabled = false; // Makeing it look all pixeling.
	a.clearRect(0,0,860,480);a.save();a.scale(20,20); // Clears, saves and scale main canvas.
	var r = Math.PI/180;a.fillStyle="rgba(0,0,0,.02)"; // rad to deg and set fillStyle.
	var pix=[]; // pixel position.
	for(var i=0;i<360;i++){ // 360°
		var rd=0; //initial radius.
		while(rd<=15){ // max radius.
			var x=Math.floor((mx)+Math.cos(i*r)*(rd)), y=Math.floor((my)+Math.sin(i*r)*(rd)); // ray's x & y position.
			if(tc.getImageData(x,y,1,1).data[3]>50)	break; //	check if alpha of current position is > 50
			pix.push({x:x,y:y}); // push coords to pixels position array.
			rd++;
		}
	}
	for(var w=0;w < pix.length;w++){a.fillRect(pix[w].x,pix[w].y,1,1);} //	draw all pixels.
	pix = null; // nullit.
	a.restore(); // restore main canvas.
},false);