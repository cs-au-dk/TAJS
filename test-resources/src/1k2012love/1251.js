function load(){
	var clock=document.getElementById("clock");
	var context=clock.getContext("2d");

	//Get clock canvas width and height
	var width=clock.width;
	var height=clock.height;
	var centerX=width/2;
	var centerY=height/2;
	var radius=90;
    //Get current time
    var now=new Date();
    var hour=now.getHours()%12;
    var minute=now.getMinutes();
    var second=now.getSeconds();
    var date=now.getDate();
    
	//Start draw the clock face
	context.clearRect(0,0,width,height);
	context.beginPath();
	context.arc(centerX, centerY, radius+10, 0, 2 * Math.PI, false);
	context.lineWidth = 5;
    context.strokeStyle = "grey";
    context.stroke();
    context.closePath();
    
    context.beginPath();
	//context.globalAlpha = 0.5; // set global alpha
	context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
	context.lineWidth = 5;
    context.strokeStyle = "grey";
    context.stroke();
    // add linear gradient
    var grd = context.createLinearGradient(centerX-radius, centerY-radius,centerX+radius, centerY+radius);
    grd.addColorStop(0, "#FFFFFF"); // light blue
    grd.addColorStop(1, "#DDDDFF"); // dark blue
    context.fillStyle = grd;
    context.fill();
    context.closePath();
    
    context.beginPath();
    context.arc(centerX, centerY, 5, 0, 2 * Math.PI, false);
	context.lineWidth = 5;
    context.strokeStyle = "grey";
    context.stroke();
    context.closePath();
    
    //Draw mark
    drawMark(clock,radius);
    
    //Draw text
    context.beginPath();
    context.font="7.5pt";
    context.fillStyle = "#000000"; // text color
    context.fillText("12",centerX-7,centerY-radius+18);
    context.fillText("3",centerX+radius-18,centerY+4);
    context.fillText("6",centerX-3,centerY+radius-12);
    context.fillText("9",centerX-radius+12,centerY+4);

    context.beginPath();
    context.rect(centerX+radius-40,centerY-5,16,10);
    context.lineWidth = 1;
    context.strokeStyle = "black";
    context.stroke();
    context.fillStyle = "#FFFFFF"; // text color
    context.fill();
    context.closePath();

    context.font="7.5pt";
    context.fillStyle = "#000000"; // text color
    context.fillText(date,centerX+radius-36,centerY+3);
    
    //Draw hour hand
    var hourHandWidth=60;
    var hourHandHeight=5;
    var hourHandLongWidth=50;
    var hourHandShortWidth=hourHandWidth-hourHandLongWidth;
    var hourAmount=12*60*60;
	var hourFraction=(360/hourAmount)*Math.PI/180;
    var hourInSeconds=hour*60*60+minute*60+second;
	var hourAngle=hourFraction*hourInSeconds;

	var hourStartX=centerX-hourHandShortWidth*Math.sin(hourAngle);
	var hourStartY=centerY+hourHandShortWidth*Math.cos(hourAngle);
	var hourEndX=centerX+hourHandLongWidth*Math.sin(hourAngle);
	var hourEndY=centerY-hourHandLongWidth*Math.cos(hourAngle);
	context.beginPath();
	context.moveTo(hourStartX,hourStartY);
	context.lineTo(hourEndX,hourEndY);
	context.lineWidth = 5;
    context.strokeStyle = "black";
    context.lineCap = "round";
    context.stroke();

    //Draw minute hand
    var minuteHandWidth=80;
    var minuteHandHeight=5;
    var minuteHandLongWidth=60;
    var minuteHandShortWidth=minuteHandWidth-minuteHandLongWidth;
    var minuteAmount=60*60;
	var minuteFraction=(360/minuteAmount)*Math.PI/180;
    var minuteInSeconds=minute*60+second;
	var minuteAngle=minuteFraction*minuteInSeconds;

	var minuteStartX=centerX-minuteHandShortWidth*Math.sin(minuteAngle);
	var minuteStartY=centerY+minuteHandShortWidth*Math.cos(minuteAngle);
	var minuteEndX=centerX+minuteHandLongWidth*Math.sin(minuteAngle);
	var minuteEndY=centerY-minuteHandLongWidth*Math.cos(minuteAngle);
	context.beginPath();
	context.moveTo(minuteStartX,minuteStartY);
	context.lineTo(minuteEndX,minuteEndY);
	context.lineWidth = 5;
    context.strokeStyle = "grey";
    context.lineCap = "round";
    context.stroke();

    //Draw second hand
    var secondHandWidth=90;
    var secondHandHeight=3;
    var secondHandLongWidth=66;
    var secondHandShortWidth=secondHandWidth-secondHandLongWidth;
    var secondAmount=60;
	var secondFraction=(360/secondAmount)*Math.PI/180;
    var secondInSeconds=second;
	var secondAngle=secondFraction*secondInSeconds;

	var secondStartX=centerX-secondHandShortWidth*Math.sin(secondAngle);
	var secondStartY=centerY+secondHandShortWidth*Math.cos(secondAngle);
	var secondEndX=centerX+secondHandLongWidth*Math.sin(secondAngle);
	var secondEndY=centerY-secondHandLongWidth*Math.cos(secondAngle);
	context.beginPath();
	context.moveTo(secondStartX,secondStartY);
	context.lineTo(secondEndX,secondEndY);
	context.lineWidth = 3;
    context.strokeStyle = "red";
    context.stroke();
    context.closePath();
    
    // request new frame
    requestAnimFrame(function(){
        load();
    });
}
function drawMark(clock,radius){
	var context=clock.getContext("2d");	
	//Get clock canvas width and height
	var width=clock.width;
	var height=clock.height;
	var centerX=width/2;
	var centerY=height/2;
	
	var markLength=6;
	var markFraction=12*Math.PI/360;
	for(var i=0;i<=59;i++){
		var extraLength=0;
		if(i%5==0){
			extraLength=4;
		}
		var angle=i*markFraction;
		var startX=centerX+(radius-1-markLength-extraLength)*Math.sin(angle);
		var startY=centerY-(radius-1-markLength-extraLength)*Math.cos(angle);
		var endX=centerX+(radius-1)*Math.sin(angle);
		var endY=centerY-(radius-1)*Math.cos(angle);
		context.beginPath();
		context.moveTo(startX,startY);
		context.lineTo(endX,endY);
		context.lineWidth = 2;
		if(i%5==0){
			context.strokeStyle = "grey";
		}else{
			context.strokeStyle = "#9999FF";
		}
	    
	    context.stroke();
	    context.closePath();
	}
}
window.requestAnimFrame = (function(callback){
    return window.requestAnimationFrame ||
    window.webkitRequestAnimationFrame ||
    window.mozRequestAnimationFrame ||
    window.oRequestAnimationFrame ||
    window.msRequestAnimationFrame ||
    function(callback){
        window.setTimeout(callback, 1000);
    };
})();
window.onload=load;