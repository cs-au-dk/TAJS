// CanvasPaint r1
// by Christopher Clay <canvas@c3o.org>
//
// The code in this file is dedicated to the Public Domain
// http://creativecommons.org/licenses/publicdomain/
//

//some global vars
var canvas, c, canvastemp, ctemp, canvassel, csel, canvasundo, cundo, wsp, imgd, co, check;
var iface = { dragging:false, resizing:false, status:null, xy:null, txy:null }
var prefs = { pretty:false, controlpoints:false }


window.onload = function() { 	

	wsp = document.getElementById('workspace');

	//set up canvas
	canvas = document.getElementById('canvas');
	if(canvas.getContext) {
		c = canvas.getContext("2d");
		//dumpValue()
		iface.status = document.getElementById('status').firstChild;
		iface.xy = document.getElementById('xy').firstChild;
		iface.txy = document.getElementById('txy').firstChild;
	
		//set up defaults
		c.tool = new tool.pencil(); 
		c.lineWidth = 1;
		c.strokeStyle = '#000';	
		c.fillStyle = '#FFF';
		c.tertStyle = '#DDD';
		c.strokeFill = 1; //outline shapes
		prefs.pretty = document.getElementById('pretty').checked;
		prefs.controlpoints = document.getElementById('controlpoints').checked;

		c.fillRect(0, 0, canvas.width, canvas.height); //fill with background color (=not transparent)
    //alert(getPixel(0, 0));

		//set up overlay canvas (for preview when drawing lines, rects etc)
		canvastemp = document.getElementById("canvastemp");
		ctemp = canvastemp.getContext("2d");	

		//set up selection canvas (invisible, used for selections)
		canvassel = document.getElementById("canvassel");
		csel = canvassel.getContext("2d");

		//set up undo canvas (invisible)
		canvasundo = document.getElementById("canvasundo");
		cundo = canvasundo.getContext("2d");

		//draw title bar gradient
		gradientcanvas = document.getElementById('gradient');
		if(gradientcanvas.getContext){
			var g = gradientcanvas.getContext('2d');
			grad = g.createLinearGradient(0, 0, 200, 15);	
			grad.addColorStop(0, '#036');
			grad.addColorStop(1, '#ACF');
			g.fillStyle = grad;
			g.fillRect(0, 0, 300, 17);
		}

		//set up events
		window.onmouseup = bodyUp;
		window.onmousemove = bodyMove;
		window.onkeydown = shortcuts;
		canvas.onmousedown = canvastemp.onmousedown = c_down;
		canvas.onmousemove = canvastemp.onmousemove = c_move;
		canvas.onmouseout = canvastemp.onmouseout = c_out;
		canvas.onmouseup = canvastemp.onmouseup = c_up;
		
		if(document.location.search) {
		  paint.open(document.location.search.substring(1));
		}

	} else { //go away, IE!
	
    document.getElementById('overlaybg').style.display = 'block';	
    document.getElementById('overlay').style.display = 'block';	
    
	}

}


function shortcuts(e) {
	if(e.keyCode == 46) { //delete
		if(c.tool.name == 'select' && c.tool.status > 0) { //del selection
			c.tool.del();
		}
	} else if(e.ctrlKey || e.metaKey) {
		var letter = String.fromCharCode(e.keyCode);
		switch(letter) {	
			case 'A':
        sel_all();
				break;
			case 'C':
				copy();
				break;
			case 'V':
        paste();
				break;
			case 'X':
        cut();
				break;
			case 'Z':
        undo();
				break;
		}
	}
	return false;
}

function sel_all() {
	selTool(document.getElementById('select'));
	c.tool.all();
	e.preventDefault();		
	e.stopPropagation();
}

function sel_cancel() {
  var temp = c.tool;
  if(temp.status == 2) { 
	  c.drawImage(canvassel, 
			  		Math.floor(ctemp.start.x), 
			  		Math.floor(ctemp.start.y)); }
  if(c.tool.status != 4) { canvastemp.style.display='none'; }
}

function copy() {
  if(c.tool.name == 'select' && c.tool.status > 0) {  //copy selection
		c.tool.copy();
	}
}

function cut() {
	if(c.tool.name == 'select' && c.tool.status > 0) {  //cut selection
		c.tool.copy();
		c.tool.del();
	}
}

function paste() {
	//todo only if something on canvassel
	selTool(document.getElementById('select'));
	c.tool.paste();
}

function undo() {
  if(c.tool.name == 'select') {	//reset all info about current selection
		activateTempCanvas(); 
		canvastemp.style.display='none';
		c.tool.status = 0;
	}
	undoLoad();
}

function getxy(e, o) {
//gets mouse position relative to object o

	if(c) {
		var bo = getpos(o);
		var x = e.clientX - bo.x + wsp.scrollLeft;	//correct for canvas position, workspace scroll offset
		var y = e.clientY - bo.y + wsp.scrollTop;									
		x += document.documentElement.scrollLeft;	//correct for window scroll offset
		y += document.documentElement.scrollTop;									
		x = (c.zoom) ? x/c.zoom : x; 	//correct for zoom
		y = (c.zoom) ? y/c.zoom : y;
		return { x: x-.5, y: y-.5 }; //-.5 prevents antialiasing of stroke lines
	}
}

function getpos(o) {
//gets position of object o
	var bo, x, y, b; x = y = 0;
	if(document.getBoxObjectFor) {	//moz
		bo = document.getBoxObjectFor(o);
		x = bo.x; y = bo.y;
	} else if (o.getBoundingClientRect) { //ie (??)
		bo = o.getBoundingClientRect();
		x = bo.left; y = bo.top;
	} else { //opera, safari etc
		while(o && o.nodeName != 'BODY') {
			x += o.offsetLeft;
			y += o.offsetTop;
			b = parseInt(document.defaultView.getComputedStyle(o,null).getPropertyValue('border-width'));
			if(b > 0) { x += b; y +=b; }
			o = o.offsetParent;
		}
	}
	return { x:x, y:y }
}


function zoomTo(level) {
//changes zoom level (by css-sizing canvas)

	var lastzoom = c.zoom;
	if(lastzoom) { //to diff zoom level: scale back first
		canvas.style.width=canvastemp.style.width=canvas.offsetWidth/c.zoom+'px';
		canvas.style.height=canvastemp.style.height=canvas.offsetHeight/c.zoom+'px';
		c.zoom=null;
		//todo: fix scrollbars
	}
	if(lastzoom != level && level > 1) {
		c.zoom=level;
		canvas.style.width=canvastemp.style.width=level*canvas.offsetWidth+'px';
		canvas.style.height=canvastemp.style.height=level*canvas.offsetHeight+'px';
	}
	
	//todo: move resize indicator
}

var tool = {

	_shapes: function() {

		this.down = this._down = function() {
			undoSave();
			activateTempCanvas();
			this.start = { x:m.x, y:m.y } 
			this.status = 1;
			c.beginPath();
		}
		this._move = function() {
			ctemp.clearRect(0, 0, canvastemp.width, canvastemp.height);
			iface.txy.innerHTML = Math.round(m.x-this.start.x)+'x'+Math.round(m.y-this.start.y);
		}
		this._up = function() {
			canvastemp.style.display='none';
			this.status = 0;
			iface.txy.innerHTML = '&nbsp;';
		}

	},

	_brushes: function() {

		this.down = function() {
			this.last = null;
			this.cp = null;
			this.lastcp = null;
			this.disconnected = null;
			c.beginPath();

			undoSave();
			this.sstart = this.last = { x:m.x, y:m.y } //extra s in sstart to not affect status bar display
			this.status = 1;
		}
		this.move = function(e) { 

			if(this.disconnected) {	//re-entering canvas: dont draw a line
				iface.status.innerHTML = 'reentering';
				this.disconnected = null;
				this.last = { x:m.x, y:m.y }
			} else {				//draw connecting line
				this.draw();
			}
			c.moveTo(m.x, m.y);

		}
		this.up = function() {
			if(this.sstart && this.sstart.x == m.x && this.sstart.y == m.y) {
				drawDot(m.x, m.y, c.lineWidth, c.strokeStyle);
			}
			this.sstart = null;
			this.status = 0;
		}
		this.draw = function() {

			if(prefs.pretty) { 

				//calculate control point
				this.cp = { x:m.x, y:m.y } //default: no bezier	
				var deltax = Math.abs(m.x-this.last.x);
				var deltay = Math.abs(m.y-this.last.y);
				if(this.last && (deltax+deltay > 10)) { //long line

					//had no control point last time: use last vertex
					var lx = (this.lastcp) ? this.lastcp.x : this.last.x;	//should be last2x?
					var ly = (this.lastcp) ? this.lastcp.y : this.last.y;
					var delta2x = this.last.x-lx;	var delta2y = this.last.y-ly;
 					this.cp = { x:lx+delta2x*1.4, y:ly+delta2y*1.4 }

				}
				this.lastcp = { x:this.cp.x, y:this.cp.y }

				c.bezierCurveTo(this.cp.x, this.cp.y, m.x, m.y, m.x, m.y);  //make pretty curve, first two params =control pt
				c.stroke();	
				c.beginPath();
				if(prefs.controlpoints) { 
					if(!(this.cp.x==m.x && this.cp.y==m.y)) { drawDot(this.cp.x, this.cp.y, 3, 'blue'); }
					drawDot(this.last.x, this.last.y, 3, 'red');
				}

			} else { //unpretty
				c.lineTo(m.x, m.y);
				c.stroke();	
				c.beginPath();
				if(prefs.controlpoints) { 
					drawDot(m.x, m.y, 3, 'red');
				}
			}
				
			this.last = { x:m.x, y:m.y }	
		}

	},



	pencil: function() {

		this.name = 'pencil';
		this.status = 0;
		this.inherit = tool._brushes; this.inherit();

		c.lineCap = 'butt';
		c.lineWidth = 1;

	},


	brush: function() {

		this.name = 'brush';
		this.status = 0;
		this.inherit = tool._brushes; this.inherit();

	},


	eraser: function() {

		this.name = 'eraser';
		this.status = 0;
		this.inherit = tool._brushes; this.inherit();

		c.lineCap = 'square';
		c.lineWidth = 7;
		c.lastStrokeStyle = c.strokeStyle;
		c.strokeStyle = c.fillStyle; //'#FFF'; //selCol('#FFF');

	},


	line: function() {

		this.name = 'line';
		this.status = 0;
		this.inherit = tool._shapes; this.inherit();

		c.lineCap = 'round';
		c.lineWidth = 1;

		this.move = function(e) {
			this._move();
			drawLine(this.start.x, this.start.y, m.x, m.y, e.shiftKey, ctemp);
		}
		this.up = function(e) {
			this._up();
			drawLine(this.start.x, this.start.y, m.x, m.y, e.shiftKey, c);
		}

	},


	rectangle: function() {

		this.name = 'rectangle';
		this.status = 0;
		this.inherit = tool._shapes; this.inherit();

		this.move = function(e) {
			this._move();
			drawRectangle(this.start.x, this.start.y, m.x, m.y, e.shiftKey, ctemp);
		}
		this.up = function(e) {
			this._up();
			drawRectangle(this.start.x, this.start.y, m.x, m.y, e.shiftKey, c);
		}

	},


	ellipse: function() {

		this.name = 'ellipse';
		this.status = 0;
		this.inherit = tool._shapes; this.inherit();

		this.down = function(e) {
			this._down();
			this.lastLineWidth = c.lineWidth;
			if(c.strokeFill == 3) { c.lineWidth+=1.1; ctemp.lineWidth+=1.1; } //hm
		}
		this.move = function(e) {
			this._move();
			drawEllipse(this.start.x, this.start.y, m.x, m.y, e.shiftKey, ctemp);
		}
		this.up = function(e) {
			this._up();
			drawEllipse(this.start.x, this.start.y, m.x, m.y, e.shiftKey, c);
			if(c.strokeFill == 3) { c.lineWidth = this.lastLineWidth; ctemp.lineWidth = this.lastLineWidth; }
		}

	},


	rounded: function() {

		this.name = 'rounded';
		this.status = 0;
		this.inherit = tool._shapes; this.inherit();
		
		this.move = function(e) {
			this._move();
			drawRounded(this.start.x, this.start.y, m.x, m.y, e.shiftKey, ctemp);
		}
		this.up = function(e) {
			this._up();
			drawRounded(this.start.x, this.start.y, m.x, m.y, e.shiftKey, c);
		}

	},


	curve: function() {

		this.name = 'curve';
		this.status = 0;

		c.lineCap = 'round';
		c.lineWidth = 1;

		this.down = function() {
			if(this.status==0) { //starting
				undoSave();
				activateTempCanvas();
				this.start = { x:m.x, y:m.y } 
				this.end = null;
				this.bezier1 = null;
				this.status = 5;
				c.beginPath();
			} else if(this.status==4 || this.status==2) { //continuing
				this.status--;
			}
		}
		this.move = function(e) { 
			if(this.status==5) {

				ctemp.clearRect(0, 0, canvastemp.width, canvastemp.height);
				drawLine(this.start.x, this.start.y, m.x, m.y, e.shiftKey, ctemp);
				ctemp.stroke();
				iface.txy.innerHTML = Math.round(m.x-this.start.x)+'x'+Math.round(m.y-this.start.y);

			} else if(this.status == 3 || this.status == 1) {

				ctemp.clearRect(0, 0, canvastemp.width, canvastemp.height);

				ctemp.moveTo(this.start.x, this.start.y);
				var b1x = (this.bezier1) ? this.bezier1.x : m.x;
				var b1y = (this.bezier1) ? this.bezier1.y : m.y;
				var b2x = (this.bezier1) ? m.x : this.end.x;
				var b2y = (this.bezier1) ? m.y : this.end.y;

				ctemp.bezierCurveTo(b1x, b1y, b2x, b2y, this.end.x, this.end.y);
				ctemp.stroke();
				iface.txy.innerHTML = Math.round(this.end.x-this.start.x)+'x'+Math.round(this.end.y-this.start.y);
			}
		}
		this.up = function() {
			if(this.status==5) { //setting endpoint     // && source.id != 'body'
				this.end = { x:m.x, y:m.y }
				this.status = 4;
			} else if(this.status==3) { //setting bezier1  && source.id != 'body'
				this.bezier1 = { x:m.x, y:m.y }
				ctemp.clearRect(0, 0, canvastemp.width, canvastemp.height);
				ctemp.moveTo(this.start.x, this.start.y);
				ctemp.bezierCurveTo(m.x, m.y, this.end.x, this.end.y, this.end.x, this.end.y);
				ctemp.stroke();
				this.status = 2;
			} else if(this.status==1) { //setting bezier2  && source.id != 'body'
				canvastemp.style.display='none';
				c.moveTo(this.start.x, this.start.y);
				c.bezierCurveTo(this.bezier1.x, this.bezier1.y,  m.x, m.y, this.end.x, this.end.y);
				c.stroke();
				this.status = 0;
				iface.txy.innerHTML = '&nbsp;';
			}
		}
	
	},

	
	polygon: function() {

		this.name = 'polygon';
		this.status = 0;
		this.points = new Array();

		this.down = function() {
			if(this.status==0) { //starting poly
				undoSave();
				activateTempCanvas();
				this.start = { x:m.x, y:m.y } 
				this.last = null;
				this.status = 3;
				this.points = new Array();
				c.beginPath();
			} else if(this.status==1) { //adding points
				this.status = 2;
			}	
		}
		this.move = function(e) { 
			if(this.status == 3) { //first polyline
				ctemp.clearRect(0, 0, canvastemp.width, canvastemp.height);
				drawLine(this.start.x, this.start.y, m.x, m.y, e.shiftKey, ctemp);
			} else if(this.status == 2) { // next polyline
				ctemp.clearRect(0, 0, canvastemp.width, canvastemp.height);
				drawLine(this.last.x, this.last.y, m.x, m.y, e.shiftKey, ctemp);
			}
			iface.txy.innerHTML = Math.round(m.x-this.start.x)+'x'+Math.round(m.y-this.start.y);
		}
		this.up = function(e) {
			if(Math.abs(m.x-this.start.x) < 4 && Math.abs(m.y-this.start.y) < 4) { //closing
				this.close();
			} else {
				ctemp.clearRect(0, 0, canvastemp.width, canvastemp.height);
				var fromx = (this.status==2) ? this.last.x : this.start.x;
				var fromy = (this.status==2) ? this.last.y : this.start.y;
				var end = drawLine(fromx, fromy, m.x, m.y, e.shiftKey, c); //TODO cant drawline on c yet...3rd canvas??
				this.last = { x:m.x, y:m.y };
				this.points[this.points.length] = { x:m.x, y:m.y };
				this.status = 1;
			}
			trg.stroke();
		}
		this.close = function() {
			if(this.last.x) { // not just started			
				c.beginPath();
				c.moveTo(this.start.x, this.start.y);
				for(var i=0; i<this.points.length; i++) {
					c.lineTo(this.points[i].x, this.points[i].y);
				}
				c.lineTo(this.last.x, this.last.y);
				c.lineTo(this.start.x, this.start.y);
				if(c.strokeFill == 2 || c.strokeFill == 3) { c.fill(); }
				if(c.strokeFill == 1 || c.strokeFill == 3) { c.stroke(); }

				c.fill();
			} else {
				//iface.txy.innerHTML = 'aborted';
			}
			canvastemp.style.display='none';
			this.status = 0;
		}

	},


	airbrush: function() {
	
		this.name = 'airbrush';
		this.status = 0;

		c.lineCap = 'square';

		this.down = function() {
			undoSave();
			this.drawing = setInterval('c.tool.draw()', 50);
			this.last = { x:m.x, y:m.y }
			this.lineCap = 'square';
			this.status = 1;
		}
		this.move = function(e) { 
			this.last = { x:m.x, y:m.y }
		}
		this.up = function(e) {
			clearInterval(this.drawing);
			this.status = 0;
		}
		
		this.draw = function() {
			//iface.txy.innerHTML = this.last.x+'/'+this.last.y;
			c.save();
			c.beginPath();
			c.arc(this.last.x, this.last.y, c.lineWidth*4, 0, Math.PI*2, true);
			c.clip();
			for(var i=c.lineWidth*15; i>0; i--) {
				var rndx = c.tool.last.x + Math.round(Math.random()*(c.lineWidth*8)-(c.lineWidth*4));
				var rndy = c.tool.last.y + Math.round(Math.random()*(c.lineWidth*8)-(c.lineWidth*4));
				drawDot(rndx, rndy, 1, c.strokeStyle);
			}
			c.restore();
		}


	},


	zoom: function() {

		this.name = 'zoom';
		this.status = 0;

		//c.lastTool = c.tool.name;

		this.down = function() {
			zoomTo(c.selectedzoom);
		}
		this.move = function() { }
		this.up = function() { }

	},

	picker: function() { 

		this.name = 'picker';
		this.status = 0;

		this.down = function(e) {
			csel.drawImage(canvas, m.x, m.y, 1, 1, 0, 0, canvassel.width, canvassel.height);
			var pat = c.createPattern(canvassel, 'repeat');
      selCol2(pat, e);
			//selTool(document.getElementById(c.lastTool));
		}
		this.move = function() { }
		this.up = function() { }

	},
	
	
	floodfill: function() {
	  
		this.name = 'floodfill';
		this.status = 0;
		
		this.down = function(e) {
		    
  			undoSave();
		  
        //var imgd = c.getImageData(0, 0, canvas.width, canvas.height);
        var x = Math.round(m.x);
        var y = Math.round(m.y);

        var oldColor = getPixel(x, y);
        if(!oldColor) { alert('Sorry, your browser doesn\'t support flood fill.'); return false; } 
        if(oldColor == c.strokeStyle) { return; }
        
        iface.status.innerHTML = 'Filling... please wait.';
        iface.xy.innerHTML = oldColor;
        
        var stack = [{x:x, y:y}];
                       
        //var n = 0;
        while(popped = stack.pop()) {
            //n++;
            //iface.txy.innerHTML = 'while'+n;
            var x = popped.x;   
            var y1 = popped.y;
            while(getPixel(x, y1) == oldColor && y1 >= 0) { y1--; }
            y1++;
            var spanLeft = false;
            var spanRight = false;
            while(getPixel(x, y1) == oldColor && y1 < canvas.height) {
                //iface.xy.innerHTML = x+'/'+y1;
                if(window.opera) { 
                  co.setPixel(x, y1, c.strokeStyle);
                } else {
                  //c.beginPath();
                  c.fillStyle = c.strokeStyle;
                  c.fillRect(x, y1, 1, 1);
                  //drawDot(x, y1, 1, c.strokeStyle, c);
                  //document.getElementById('info').innerHTML += '<br />'+x+'/'+y1;
                }
                if(!spanLeft && x > 0 && getPixel(x-1, y1) == oldColor) {
                  //break;
                   stack.push({x:x-1, y:y1});        
                    spanLeft = true;
                } else if(spanLeft && x > 0 && getPixel(x-1, y1) != oldColor) {
                    spanRight = false;
                } else if(spanRight && x <= 0) { spanRight = false; }
                if(!spanRight && x < canvas.width-1 && getPixel(x+1, y1) == oldColor) {
                  //break;
                  stack.push({x:x+1, y:y1});
                    spanRight = true;
                } else if(spanRight && x < canvas.width-1 && getPixel(x+1, y1) != oldColor) {
                    spanRight = false;
                } else if(spanRight && x >= canvas.width) { spanRight = false; }
                y1++;                   
            }
        }        
        
        
        if(window.opera) {
          co.lockCanvasUpdates(false);
          co.updateCanvas();
        }
        //document.getElementById('info').innerHTML = check;
        
        iface.status.innerHTML = 'Finished filling.';
  		  		   
		}
		
    this.move = function() { }
		this.up = function() { }
	  
	},

	select: function() {

		this.name = 'select';
		this.status = 0;

		//c.lastTool = c.tool.name;
		c.lineWidth = 1;
		var dashed = new Image();
		dashed.src = 'icons/dashed2.gif';
		c.lastStrokeStyle = c.strokeStyle;
		c.strokeStyle = c.createPattern(dashed, 'repeat');
		c.strokeFill = 1;
		c.beginPath();

		this.down = function(e) { 
			if(this.status==0) { //starting select
				var dashed = new Image(); //doing this here instead of in selTool to ignore user changing color
				dashed.src = 'icons/dashed2.gif';
				c.strokeStyle = c.createPattern(dashed, 'repeat');
				activateTempCanvas();
				this.start = { x:m.x, y:m.y } 
				this.status = 4;
			} else if(this.status==3 || this.status==2) { //moving selection
				if(intersects(m, this.start, this.dimension)) {
					this.offset = { x:m.x-this.start.x, y:m.y-this.start.y } 
					if(this.status == 3 && !e.ctrlKey && !e.shiftKey) { //when first moving (and not in stamp mode), clear original pos and paint on tempcanvas
						undoSave();
						var pos = { x:m.x-this.offset.x, y:m.y-this.offset.y }
						drawRectangle(pos.x-1, pos.y-1, pos.x+this.dimension.x, pos.y+this.dimension.y, null, ctemp);
						ctemp.drawImage(canvassel, Math.floor(pos.x), Math.floor(pos.y));
						c.fillRect(this.start.x-.5, this.start.y-.5, this.dimension.x, this.dimension.y);
					}
					this.status = 1;
				} else {  //starting new selection
					if(this.status < 3) { //actually draw last moved selection to canvas TODO also do this when switching tools
						c.drawImage(canvassel, Math.floor(this.start.x), Math.floor(this.start.y));
					}
					activateTempCanvas();
					this.start = { x:m.x, y:m.y } 
					this.status = 4;
				}
			}
		}
		this.move = function(e) {
			if(this.status==4) { //selecting

				ctemp.clearRect(0, 0, canvastemp.width, canvastemp.height);
				var constrained = { x:constrain(m.x, 0, canvas.width), y:constrain(m.y, 0, canvas.height-5) }
				drawRectangle(this.start.x-1, this.start.y-1, constrained.x, constrained.y, null, ctemp);
				iface.txy.innerHTML = Math.round(constrained.x-this.start.x)+'x'+Math.round(constrained.y-this.start.y);	

			} else if(this.status==1) { //moving selection

				ctemp.clearRect(0, 0, canvastemp.width, canvastemp.height);
				var pos = { x:m.x-this.offset.x, y:m.y-this.offset.y }
				drawRectangle(pos.x-1, pos.y-1, pos.x+this.dimension.x, pos.y+this.dimension.y, null, ctemp);
				ctemp.drawImage(canvassel, Math.floor(pos.x), Math.floor(pos.y));
				if(e.shiftKey) { //dupli mode
					c.drawImage(canvassel, Math.floor(pos.x), Math.floor(pos.y));
				}
			} else if(this.start) {
				if(c.tool.status == 1 || (c.tool.dimension && intersects(m, c.tool.start, c.tool.dimension))) {
					canvastemp.style.cursor = 'move';
				} else {
					canvastemp.style.cursor = '';		
				}
			}

		}
		this.up = function(e) {
			if(this.status == 4) { //finished selecting

				this.status = 3;
				this.dimension = { x:constrain(m.x, 0, canvas.width)-this.start.x,
								   y:constrain(m.y, 0, canvas.height)-this.start.y }
				if(this.dimension.x == 0 && this.dimension.y == 0) { //nothing selected, abort
					this.status = 0;
					canvastemp.style.display='none';
					csel.clearRect(0, 0, canvassel.width, canvassel.height);
				} else { //save on selection canvas
					csel.clearRect(0, 0, canvassel.width, canvassel.height);
					if(this.dimension.x < 0) { this.start.x = this.start.x + this.dimension.x; this.dimension.x *= -1; } //correct for selections not drawn from top left
					if(this.dimension.y < 0) { this.start.y = this.start.y + this.dimension.y; this.dimension.y *= -1; }
					//todo check for >max
					csel.drawImage(canvas, Math.floor(this.start.x), Math.floor(this.start.y), this.dimension.x, this.dimension.y, 0, 0, this.dimension.x, this.dimension.y);
					csel.dimension = this.dimension;
				}	
				iface.txy.innerHTML = '&nbsp;';

			} else if(this.status == 1) { //finished moving selection
				this.status = 2;
				this.start = { x:m.x-this.offset.x, y:m.y-this.offset.y }
				if(e.ctrlKey) { //stamp mode
					c.drawImage(canvassel, Math.floor(this.start.x), Math.floor(this.start.y));
				}
			}
		}

		this.del = function() { 
			undoSave();
			c.fillRect(this.start.x-.5, this.start.y-.5, this.dimension.x, this.dimension.y);
			activateTempCanvas(); 
			canvastemp.style.display = 'none';
			this.status = 0;
		}
		this.all = function() { 
			csel.clearRect(0, 0, canvassel.width, canvassel.height);
			csel.drawImage(canvas, 0, 0);
			activateTempCanvas();
			this.start = { x:0.5, y:0.5 }
			this.dimension = { x:canvas.width, y:canvas.height }		
			ctemp.strokeRect(0.5, 0.5, canvas.width-1, canvas.height-1);
			this.status = 3;
		}
		this.copy = function() {
			csel.drawImage(canvas, Math.floor(this.start.x), Math.floor(this.start.y), this.dimension.x, this.dimension.y, 0, 0, this.dimension.x, this.dimension.y);
			csel.dimension = this.dimension;
		}
		this.paste = function() {
			activateTempCanvas();
			ctemp.drawImage(canvassel, 0, 0);
			this.status = 3;
			this.start = { x:.5, y:.5 }
			this.dimension = csel.dimension;
			ctemp.strokeRect(this.start.x-.5, this.start.y-.5, this.dimension.x+.5, this.dimension.y+.5);

		}

	}

}


function getPixel(x, y) {
 
  if(imgd || c.getImageData) {
    //if(!imgd) {
    //  imgd = c.getImageData(0, 0, canvas.width, canvas.height);
    //  alert('getting img data');
    // }
    //var pos = (y*canvas.width+x)*4;
    //if(imgd.data[pos]) {
    //  var col = 'rgb('+imgd.data[pos]+', '+imgd.data[pos+1]+', '+imgd.data[pos+2]+')'; //rgba: ', '+imgd.data[pos+3]+')';
    //  check += '<br />'+x+'/'+y+': '+col;
    //  //drawDot(x, y, 1, 'red', c);   
    //  return col;
    //} else {
      return false;
    //}
  } else if (window.opera) {
    if(!co) { co = canvas.getContext('opera-2dgame');	}
    col = co.getPixel(x, y);
    //check += '<br />'+x+'/'+y+': '+col;
    return col;
  } else {
    return false; 
  }
  
}  


function c_down(e) {
//handles mousedown on the canvas depending on tool selected

	var source = e.currentTarget;
	m = getxy(e, canvas);

	if(c.tool.name != 'select' && c.tool.name != 'eraser' && c.tool.name != 'picker') { //no color switching for these
		if(e.ctrlKey) {							 //ctrl: switch tert & stroke
			var temp = c.tertStyle;
			c.tertStyle = c.strokeStyle;
			c.strokeStyle = temp;
		}
		if(e.button == 2 && c.tool.name != 'eraser') { //right: switch stroke & fill
			var temp = c.strokeStyle;
			c.strokeStyle = c.fillStyle;
			c.fillStyle = temp;
		}
	}

	c.tool.down(e);
	c.moveTo(m.x, m.y); //?

	//iface.status.innerHTML = 'c_down: '+m.x+'/'+m.y+' - status:'+c.tool.status+' - source:'+source.id+' - tool:'+c.tool.name;
	return false;
}


function c_up(e) {
//handles mouseup on the canvas depending on tool selected

	m = getxy(e, canvas);

	e.stopPropagation();
	if(iface.dragging || iface.resizing || c.resizing) { bodyUp(e); } //but not if dragging

	c.tool.up(e);
	
	if(c.tool.name != 'select' && c.tool.name != 'eraser' && c.tool.name != 'picker') { //no color switching for these
		if(e.button == 2 && c.tool.name != 'eraser') { //right: switch stroke & fill back
			var temp = c.fillStyle;
			c.fillStyle = c.strokeStyle;
			c.strokeStyle = temp;
		}
		if(e.ctrlKey) { 
			var temp = c.strokeStyle;
			c.strokeStyle = c.tertStyle;
			c.tertStyle = temp;
		}
	}

	//iface.status.innerHTML = 'c_up - status: '+c.tool.status;
	return false;
}

function c_move(e) {
	m = getxy(e, canvas);
	e.stopPropagation();
	if(iface.dragging || iface.resizing || c.resizing) { bodyMove(e); } //don't stop propagation if dragging

	if(c.tool.status > 0) {
		c.tool.move(e);
	}

	if(c.tool.start && c.tool.status > 0) {
		iface.xy.innerHTML = Math.round(c.tool.start.x)+', '+Math.round(c.tool.start.y);
	} else {
		iface.xy.innerHTML = Math.round(m.x)+', '+Math.round(m.y);
	}
	return false;
}

function c_out(e) {
	//var source = e.currentTarget;

	if(c && (c.tool.name=='pencil' || c.tool.name=='eraser' || c.tool.name=='brush') && c.tool.status==1) { 
		c.tool.disconnected = 1;
		m = getxy(e, canvas);
		c.tool.draw();
		//iface.status.innerHTML = 'c_out: '+m.x+'/'+m.y+' '+c.fillStyle;
	}

	iface.xy.innerHTML = '&nbsp;';
}



function activateTempCanvas() {
//resets and shows overlay canvas

	if(m) { ctemp.moveTo(m.x, m.y); }							//copy context from main
	ctemp.lineCap = c.lineCap;								
	ctemp.lineWidth = c.lineWidth;
	ctemp.strokeStyle = c.strokeStyle;
	ctemp.fillStyle = c.fillStyle;
	ctemp.clearRect(0, 0, canvastemp.width, canvastemp.height);	//clear
	canvastemp.style.display='block';							//show

}



function undoSave() {
//sets an undo point
	//cundo.clearRect(0, 0, canvas.width, canvas.height); //this doesn't help with the bg..
	if(imgd) { imgd = null; }
	if(canvas.width != canvasundo.width || canvas.height != canvasundo.height) { 
		canvasundo.width = canvas.width;
		canvasundo.height = canvas.height;
	}
	cundo.drawImage(canvas, 0, 0);
}
function undoLoad() {
//reverts to last undo point
	if(canvas.width != canvasundo.width || canvas.height != canvasundo.height) { 
		clipResize(canvasundo.width, canvasundo.height);
	}
	//ctemp.clearRect(0, 0, canvas.width, canvas.height);
	ctemp.drawImage(canvas, 0, 0);
	//c.clearRect(0, 0, canvas.width, canvas.height);
	c.drawImage(canvasundo, 0, 0);
	//cundo.clearRect(0, 0, canvas.width, canvas.height);
	cundo.drawImage(canvastemp, 0, 0);
}


function windowDrag(e) {
	iface.dragging = true;
	var win = wsp.parentNode.parentNode.parentNode;
	var pos = getpos(win);
	c.start = { x:e.clientX-pos.x+1, y:e.clientY-pos.y+1 };
}
function windowResize(e, o) {
	iface.resizing = true;
	var pos = getpos(o);
	c.start = { x:o.offsetWidth+3-(e.clientX-pos.x), y:o.offsetHeight+3-(e.clientY-pos.y) };
	e.stopPropagation();
	document.body.style.cursor = 'nw-resize';
}

function canvasResize(e) {
	c.resizing = true;
	document.body.style.cursor = 'nw-resize';
	canvastemp.lastCursor = canvastemp.style.cursor;
	canvastemp.style.cursor = 'nw-resize';
	activateTempCanvas();
	var dotted = new Image(); dotted.src = 'icons/dotted.gif';
	ctemp.strokeStyle = ctemp.createPattern(dotted, 'repeat');
}


function clipResize(w, h) { 
//resizes all the canvases by clipping/extending, moves resize handle
	undoSave();
	cundo.fillStyle = c.fillStyle; //save
	canvas.width = canvastemp.width = canvassel.width = w;
	canvas.height = canvastemp.height = canvassel.height = h;
	canvas.style.width = canvastemp.style.width = w+'px';
	canvas.style.height = canvastemp.style.height = h+'px';
	var cresizer = document.getElementById('canvasresize');
	cresizer.style.left = w+cresizer.offsetWidth+'px'; cresizer.style.top = h+cresizer.offsetHeight+'px';
	c.fillStyle = cundo.fillStyle; //restore
	c.fillRect(0, 0, canvas.width, canvas.height); //so that if expanding it's filled with bg col
	c.drawImage(canvasundo, 0, 0);
}


function bodyMove(e) {
//lets the user move outside the canvas while drawing shapes and lines

	if(c.tool.status > 0) { c_move(e); }	

	if(c.resizing) {	
		m = getxy(e, document.body);
		var win = wsp.parentNode.parentNode.parentNode;
		ctemp.clearRect(0, 0, canvastemp.width, canvastemp.height);
		ctemp.strokeRect(0, 0, m.x, m.y); //dotted line
	} else if(iface.dragging) {
		m = getxy(e, document.body);
		var win = wsp.parentNode.parentNode.parentNode;
		win.style.left = e.clientX-c.start.x+'px';
		win.style.top = e.clientY-c.start.y+'px';
	} else if(iface.resizing) {
		m = getxy(e, document.body);
		var win = wsp.parentNode.parentNode.parentNode;
		win.style.width = e.clientX-win.offsetLeft+c.start.x+3+'px';
		win.style.height = e.clientY-win.offsetTop+c.start.y+3+'px';
		//todo prevent selecting statusbar text.. how?
	}

}


function bodyUp(e) {
//stops drawing even if mouseup happened outside canvas
//closes menus if clicking somewhere else

	if(c.resizing) {
		c.resizing = false; document.body.style.cursor = 'auto'; canvastemp.style.cursor = canvastemp.lastCursor;
		m = getxy(e, wsp);
		clipResize(m.x-3, m.y-3);
	}
	if(iface.dragging) { iface.dragging = false; }
	if(iface.resizing) { iface.resizing = false; document.body.style.cursor = 'auto'; }

	if(c.tool.name == 'select') { //cancel selection or finalize selection move
    sel_cancel();
	}

	if(c && c.tool.name != 'polygon' && c.tool.status > 0) {
		c_up(e);
	}
	if(document.getElementById('menubar').className=='open') {
		document.getElementById('menubar').className='';
		e.stopPropagation();
	}

}

function drawDot(x, y, size, col, trg) {

	x = Math.floor(x)+1; //prevent antialiasing of 1px dots
	y = Math.floor(y)+1;

	if(x>0 && y>0) {

		if(!trg) { trg = c; }
		if(col || size) { var lastcol = trg.fillStyle; var lastsize = trg.lineWidth; }
		if(col)  { trg.fillStyle = col;  }
		if(size) { trg.lineWidth = size; }	
		if(trg.lineCap == 'round') {
			trg.arc(x, y, trg.lineWidth/2, 0, (Math.PI/180)*360, false);
			trg.fill();
		} else {
			var dotoffset = (trg.lineWidth > 1) ? trg.lineWidth/2 : trg.lineWidth;
			trg.fillRect((x-dotoffset), (y-dotoffset), trg.lineWidth, trg.lineWidth);
		}
		if(col || size) { trg.fillStyle = lastcol; trg.lineWidth = lastsize; }

	}
}



function drawLine(x1, y1, x2, y2, mod, trg) { 

	if(trg.lineWidth % 2 == 0) { x1 = Math.floor(x1); y1 = Math.floor(y1); x2 = Math.floor(x2); y2 = Math.floor(y2); } //no antialiasing

	trg.beginPath();
	trg.moveTo(x1, y1);
	if(mod) {
		var dx = Math.abs(x2-x1);
		var dy = Math.abs(y2-y1);	
		var dd = Math.abs(dx-dy);
		if(dx > 0 && dy > 0 && dx != dy) {
			if(dd < dx && dd < dy) { //diagonal
				if(dx < dy) {
					y2 = y1+(((y2-y1)/dy)*dx);
				} else {
					x2 = x1+(((x2-x1)/dx)*dy);
				}
			} else if(dx < dy) {
				x2 = x1;
			} else if(dy < dx) {
				y2 = y1;
			}
		}
	}
	trg.lineTo(x2, y2);
	trg.stroke();
	trg.beginPath();
	return { x:x2, y:y2 }
}

function drawEllipse(x1, y1, x2, y2, mod, trg) {
	//bounding box. this maybe isn't the best idea?
	 
	var dx = Math.abs(x2-x1);
	var dy = Math.abs(y2-y1);
	
	if(mod && !(dx==dy)) { 	//shift held down: constrain
		if(dx < dy) {
			x2 = x1+(((x2-x1)/dx)*dy);
		} else {
  		y2 = y1+(((y2-y1)/dy)*dx);
		} 
	}

  var KAPPA = 4 * ((Math.sqrt(2) -1) / 3);
	var rx = (x2-x1)/2;
	var ry = (y2-y1)/2;	
  var cx = x1+rx;
  var cy = y1+ry;

	trg.beginPath();
  trg.moveTo(cx, cy - ry);
  trg.bezierCurveTo(cx + (KAPPA * rx), cy - ry,  cx + rx, cy - (KAPPA * ry), cx + rx, cy);  
  trg.bezierCurveTo(cx + rx, cy + (KAPPA * ry), cx + (KAPPA * rx), cy + ry, cx, cy + ry); 
  trg.bezierCurveTo(cx - (KAPPA * rx), cy + ry, cx - rx, cy + (KAPPA * ry), cx - rx, cy); 
  trg.bezierCurveTo(cx - rx, cy - (KAPPA * ry), cx - (KAPPA * rx), cy - ry, cx, cy - ry); 

	if(c.strokeFill == 1 || c.strokeFill == 3) { trg.stroke(); }
	if(c.strokeFill == 2 || c.strokeFill == 3) { trg.fill();   }
}


function drawRectangle(x1, y1, x2, y2, mod, trg) {

	var dx = Math.abs(x2-x1);
	var dy = Math.abs(y2-y1);

	if(mod && dx != dy) {	//shift held down: constrain
		if(dx < dy) {
			y2 = y1+(((y2-y1)/dy)*dx);
		} else {
			x2 = x1+(((x2-x1)/dx)*dy);
		}
	}
	
	if(c.strokeFill == 2 || trg.lineWidth % 2 == 0) {    //no antialiasing
		x1 = Math.floor(x1); y1 = Math.floor(y1); x2 = Math.floor(x2); y2 = Math.floor(y2);
	}
	trg.rect(x1, y1, (x2-x1), (y2-y1));
	if(c.strokeFill == 2 || c.strokeFill == 3) { trg.fill(); }
	if(c.strokeFill == 1 || c.strokeFill == 3) { trg.stroke(); }

}

function drawRounded(x1, y1, x2, y2, mod, trg) {

	var dx = Math.abs(x2-x1);
	var dy = Math.abs(y2-y1);

	if(mod && dx != dy) {	//shift held down: constrain
		if(dx < dy) {
			y2 = y1+(((y2-y1)/dy)*dx);
			dy = dx;
		} else {
			x2 = x1+(((x2-x1)/dx)*dy);
			dx = dy;
		}
	}
	var dmin = (dx < dy) ? dx : dy;
	var cornersize = (dmin/2 >= 15) ? 15 : dmin/2;
	
	var xdir = (x2 > x1) ? cornersize : -1*cornersize;
	var ydir = (y2 > y1) ? cornersize : -1*cornersize;

	drawRounded2(trg, x1, x2, y1, y2, xdir, ydir);
	if(c.strokeFill == 2 || c.strokeFill == 3) { trg.fill(); }
	if(c.strokeFill == 1 || c.strokeFill == 3) { trg.stroke(); }

}
function drawRounded2(trg, x1, x2, y1, y2, xdir, ydir) {
	trg.beginPath();
	trg.moveTo(x1, y1+ydir);
	trg.quadraticCurveTo(x1, y1, x1+xdir, y1);
	trg.lineTo(x2-xdir, y1);
	trg.quadraticCurveTo(x2, y1, x2, y1+ydir);
	trg.lineTo(x2, y2-ydir);
	trg.quadraticCurveTo(x2, y2, x2-xdir, y2);
	trg.lineTo(x1+xdir, y2);
	trg.quadraticCurveTo(x1, y2, x1, y2-ydir);
	trg.closePath();
}



function constrain(n, min, max) {
	if(n > max) return max;
	if(n < min) return min;
	return n;
}

function intersects(m, start, dim) {
//checks if m(x,y) is between start(x,y) and start+dim(x,y)
	if(	m.x >= start.x && m.y >= start.y &&
		m.x <= (start.x+dim.x) && m.y <= (start.y+dim.y)) {
		return true;
	} else {
		return false;
	}
}



function selCol(o, e, context) {
//context because silly safari doesnt capture right click, apparently.. thus oncontextmenu
//  alert(typeof(o));
	col = (typeof(o) == 'string') ? o : o.style.backgroundColor;
  selCol2(col, e, context);
//	if(e) e.preventDefault();

}

function selCol2(col, e, context) {
	if(e && e.ctrlKey) {	//tertiary
    var whichcanvas = document.getElementById('currcoltert');
		c.tertStyle = col;
	} else if(context == 1 || (e && e.button == 2)) { //right
    var whichcanvas = document.getElementById('currcolback');
		c.fillStyle = col;
		ctemp.fillStyle=col;
		if(c.tool.name=='eraser') { c.strokeStyle = col; }
	} else {
    var whichcanvas = document.getElementById('currcolfore');
		c.strokeStyle=col;
		ctemp.strokeStyle=col;
		if(c.lastStrokeStyle) { c.lastStrokeStyle = col; } //allows color changing during select/eraser
	}
  
  if(whichcanvas) {
    whichcontext = whichcanvas.getContext('2d');
    whichcontext.fillStyle = col;
    whichcontext.fillRect(0, 0, whichcanvas.width, whichcanvas.height);
  }
  
	if(e) e.preventDefault();

}

function selTool(o) {

	c.tool.status = 0;
	canvastemp.style.display='none';
	var newtool = o.id;
	iface.status.innerHTML=newtool;

	document.getElementById('workspace').className = newtool;

	//button highlighting
	var toolbarbtns = document.getElementById('buttons').getElementsByTagName('li');
	for(var i=0; i<toolbarbtns.length;i++) {
		if(toolbarbtns[i].className == 'sel') { toolbarbtns[i].className=''; }
	}
	o.className = 'sel';
	 
	//reset color (after eraser and select)
	if(c.lastStrokeStyle) { selCol(c.lastStrokeStyle); c.lastStrokeStyle = null }
	
	if(c.tool.name == 'polygon') { c.tool.close(); }

	c.lastTool = c.tool.name;
	c.tool = new tool[newtool]();

	var newtool = shareSettingsPanels(c.tool.name);

	//settings panel switching
	var settingpanels = document.getElementById('settings').getElementsByTagName('div');
	for(var i=0; i<settingpanels.length;i++) {
		if(settingpanels[i].style.display == 'block') { settingpanels[i].style.display='none'; }
	}
	if(document.getElementById(newtool+'-settings')) {
		document.getElementById(newtool+'-settings').style.display = 'block';
		if(newtool != 'zoom') { //cause this would switch back
			var settingbtns = document.getElementById(newtool+'-settings').childNodes;
			for(var i=0; i<settingbtns.length;i++) { //reapply last selection
				if(settingbtns[i].className == 'sel') { settingbtns[i].onclick(); }
			}
		}
	}

}

function selSetting(o, sett) {

	c.tool.status = 0;
	canvastemp.style.display='none';

	var newtool = shareSettingsPanels(c.tool.name);
	
	if(document.getElementById(newtool+'-settings')) {
		var settingbtns = document.getElementById(newtool+'-settings').childNodes;
		for(var i=0; i<settingbtns.length;i++) {
			if(settingbtns[i].className == 'sel') { settingbtns[i].className=''; }
		}
		o.className = 'sel';
		eval(sett);
	}

	if(c.tool.name=='zoom') {	//switch back
		selTool(document.getElementById(c.lastTool));
	}
}

function shareSettingsPanels(tool) {
	if(tool=='select' || tool=='text')  { return 'select';  }
	if(tool=='line'   || tool=='curve') { return 'line'; }
	if(tool=='rectangle' || tool=='polygon'|| tool=='ellipse' || tool=='rounded') { return 'shape'; }
	if(tool=='ffselect' || tool=='select'|| tool=='text') { return 'trans'; }
	return tool;
}


function buttonReset(o) {
	iface.status.innerHTML='&nbsp;';
	if(o.className=='down') { o.className=''; }  //todo why isn't this working
}
function buttonDown(e, o) {
	if(e.button != 2 && o.className != 'sel') { o.className='down'; } //not on rightclick
}


function save(onserver) {
    
  if(canvas.toDataURL) {
    
    var dataurl = canvas.toDataURL();

    if(onserver == true) {
       saveonline(dataurl);
    } else {
       overlay('Please right-click/save-as to save your drawing. <a href="#" onclick="overlay_hide()">close</a><br /><img src="'+dataurl+'" title="Please right-click/save-as" />'); 
    }
    
  } else {
  	alert('Sorry, your browser does not implement the toDataURL() method required to save images.');
  }
}


function saveonline(dataurl) {

	var req = null;
	iface.status.innerHTML="Saving to server...";

	if (window.XMLHttpRequest) {
	  req = new XMLHttpRequest();
		//if (req.overrideMimeType) { req.overrideMimeType('text/xml'); }
	} else {
	  return;
	}

	req.onreadystatechange = function() { 
		if(req.readyState == 4) {
			if(req.status == 200) {
				var response = req.responseText;
        overlay('Your image has been saved as <input size="50" value="http://canvaspaint.org/'+response+'"> <a href="#" onclick="overlay_hide()">close</a><br /><img src="http://canvaspaint.org/'+response+'" />'); 
			}	else {
				iface.status.innerHTML="Post error code " + req.status + " " + req.statusText;
			}	
		} 
	}; 
	req.open("POST", "save.php", true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.send('u='+dataurl);

}

function overlay(content) {
  var obg = document.getElementById('overlaybg');
  obg.style.display='block';
  var o = document.getElementById('overlay');
  o.innerHTML = content;
  o.style.display = 'block';
}
function overlay_hide() { 
  var o = document.getElementById('overlay');
  o.innerHTML = '';
  o.style.display = 'none';
  document.getElementById('overlaybg').style.display = 'none';
}

function menuOpen(e, o) {
	iface.txy.innerHTML = o.parentNode.className; //todo remove after fixing menu bug
	o.parentNode.className = (o.parentNode.className != 'open') ? 'open' : '';
	e.stopPropagation();
}


var paint = {
 
  open: function(url) {
    if(url.indexOf('.') == -1) { url += '.png' }
    if(url.indexOf('://') == -1) { url = 'http://canvaspaint.org/'+url; }
    var src = (url.indexOf('http://canvaspaint.org/') > -1) ? url.replace('http://canvaspaint.org', '') : 'open.php?u='+url;
    var img = document.createElement('img');
    img.src = src;
    img.style.visibility='none';
    iface.status.innerHTML = 'Opening '+src+'...';
    img.onload = function() {
      clipResize(this.width, this.height);
      c.drawImage(this, 0, 0);
      iface.status.innerHTML = 'Opened '+url+'.';
    }
    img.onerror = function() {
      alert('Error opening '+this.src);
      iface.status.innerHTML = '';
    }
  }
    
}
// 1337, dude.
