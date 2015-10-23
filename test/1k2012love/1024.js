//Globals
var _g, _x;

//Main function
var ƒ = (function(el,globals,_x,key,axies) {			
	_g = globals;_x="x";
	//Local version of ƒ
	var ƒ = function(str, xstr) {				
		var parse = str.replace(/([0-9])(\([^)]+\)|[a-zA-Z])/g, "$1*$2")										
		//Change coeff to multiplication
				   .replace(/(\([^)]+\)|\w+)\^(\([^)]+\)|\S+)(\s|$)/g, "pow($1, $2)$3");						
				   //Exponential Function			
			
		//Once ƒ is constructed with a string, ƒ becomes a function of a value -- with the same prototype
		var ƒ = new Function(_x, "with(_g) return ["+(xstr||_x)+", "+parse+"]");								
		//with and eval in one line!!
		ƒ.p = ƒ.prototype = window.ƒ.p;																			
		//it's like jQuery.fn
		//Apply prototype to constructor
		for( key in ƒ.p ) ƒ[key] = ƒ.p[key];										
		//Extend the object with the proto
		return ƒ;																	
		//Return a new ƒ - a function of x
	};
	ƒ.p = ƒ.prototype = {															
	//jQuery.fn again
		constructor: ƒ,																
		//Constructor makes this thing act like a function
		init: function(over, e) {													
		//Draw x-axis, y-axis
			ƒ('0').graph(0,1),ƒ(_x,'0').graph(0,1);	
		},
		graph: function( win, ax ) {												
		//Main event e.g., ƒ("2x").graph()
			var f = thiz = this;													
			//Cache function being called on 

			var prec = 1000,														
			//Steps in loop
			    win = [
					-8, //xMin
					8, 	//xMax
					-8, //yMin
					8 	//yMax
			    ];

			win[3] = [-win[2], (win[2] = -win[3])][0];								
			//Swap Max/Min to make more intuitive

			axies = {																
			//Pixels to axis
				y: (el.width*(0-win[0])/(win[1]-win[0])),														
				x: (el.height*(0-win[2])/(win[3]-win[2]))
			};
			if (context) {	
				!ax&&ƒ.init(el);													
				//Prevent infinite loop
				context.beginPath();	

				for( var i = axies.y/el.width * -prec; i < (el.width-axies.y)/el.width * prec; i++ ) {			
				//Loop through steps, with neg and pos matching axis
					var fraction = function(n) { return n/prec - -axies.y/el.width; },							
					//D.R.Y.
						xMin	 = win[0],																								
						yMin	 = win[2],
						xRange 	 = win[1] - win[0],
						yRange	 = win[3] - win[2], 															
						p; //Declare for use below
					
					//Make a miniature line segment
				    context.moveTo(
				    	axies.y + 																				
				    	f(
				    		p=xMin+fraction(i)*xRange								
				    		//Convert pixels > x, shift to start at min
				    	)[0]/xRange*el.width, 										
				    	//Convert x back to pixels for drawing
				    	axies.x - 
				    	f(p)[1]/yRange*el.height									
				    	//Take y-value from previous evaluation and scale
				    ); 
				    context.lineTo(
				    	axies.y + 
				    	f(
				    		p=xMin+fraction(i+1)*xRange								
				    		//Convert pixels > x, shift to start at min
				    	)[0]/xRange*el.width, 										
				    	//Convert x back to pixels for drawing
				    	axies.x - 
				    	f(p)[1]/yRange*el.height									
				    	//Take y-value from previous evaluation and scale
				    );	
				}		
				   		
				context.stroke();
				context.closePath();
			}
		}
	};
	for( key in ƒ.p ) ƒ[key] = ƒ.p[key];											
	//Extend object with prototype
	return ƒ;
})(cvs,Math);																		
//Pass canvas and Math Object for constants + functions

//Calling of ƒ
ƒ(prompt("f(x)?")).graph();															
//Graph user-inputted function