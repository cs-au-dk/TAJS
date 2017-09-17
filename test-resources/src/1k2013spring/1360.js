var p=Math.PI/6;


window.onload = function () {

	setInterval(tic, 1000)
}


function tic(){
	a.clearRect(0,0,500,500); 		
    var now = new Date(),
   		s = now.getSeconds(),
   		m = now.getMinutes(),
  		h  = now.getHours();
	a.lineWidth = 3; 	
	a.save();

	a.translate(198,272);
 	
 	for (var i=0;i<60;i++){
    	if (i%5!=0) {
      		a.beginPath();
      		a.moveTo(128,0);
      		a.lineTo(138,0);
      		a.stroke()
    	} else {
    		a.beginPath();
	    	a.moveTo(150,0);
    		a.lineTo(130,0);
			a.stroke()
    	}
    	a.rotate(p/5)
  	}


 	a.lineCap = "round";
	a.strokeStyle = "black";
	a.rotate(-1*p*3);  	
	a.save();	
	a.save();

  	a.rotate( h*(p) + (p/60)*m + (p/3600)*s );
  	arrow(45,16);

  	a.beginPath();
  	a.strokeStyle = "red";
  	a.arc(0, 0, 30, 0, p * 12, false);
  	a.stroke();

	a.restore();
  	a.rotate( (p/5)*m + (p/300)*s );
  	arrow(75,10);	

	a.restore();
  	a.rotate((p/5)*s );
  	a.beginPath();
  	a.moveTo(30,0);
  	a.lineTo(140,0);
  	a.stroke();  
	a.restore();

	a.save();
	a.translate(45,120);
 	a.lineWidth = 2; 	
	a.strokeRect(0,0,305,305);
	a.beginPath();
	a.moveTo(-30,0);
  	a.lineTo(305+30,0);
  	a.lineTo(306/2,-110);
  	a.closePath();
  	a.stroke(); 
	a.restore()
}


function arrow(height,width){
	a.strokeRect(30, -1*width/2, height, width);	
	a.beginPath();
	a.moveTo(height+30, -1*width);
	a.lineTo(height+30,width);
	a.lineTo(height+50,0);
	a.closePath();
	a.stroke()
}