(function(size, angle, minimum, canvas, context, requestAnimationFrame){
    context.translate(minimum(90*size, 2*canvas.width/3), minimum(45*size, 2*canvas.height/3));
    context.strokeStyle = 'black';

    var draw = function(){
	context.beginPath();
	context.moveTo(0,0);
	context.lineTo(size, 0);
	context.stroke();
    }

    var forward = function(){
	context.translate(size, 0);
    }

    var left = function(){
	context.rotate(-angle);
    }

    var right = function(){
	context.rotate(angle);
    }

    var move = function(goRight){
	forward();
	if (goRight) {
	    right();
	} else {
	    left();
	}
    }

    var direction = function(n){
	return (((n & -n) << 1) & n) !== 0;
    }

    var n = 1; var max = Math.pow(2, 15);
    var continous = function(){
	draw();
	move(direction(n++));
	requestAnimationFrame(continous);
    }
    continous();
})(5, Math.PI/2, Math.min, a, c, requestAnimationFrame);