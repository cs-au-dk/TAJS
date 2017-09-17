function Ptcl(obj){
	
	this.x = 0;
	this.y = 0;
	this.originX = 0;
	this.originY = 0;
	this.vx = 0;
	this.vy = 0;
	this.dt = 0;
	this.size = 0;
	this.bound = 0;
	this.init = function(){//SET INITIAL POSITION / SIZE /VELOCITY
		this.x  = getRandom(true) * 1000;
		this.y  = getRandom(true) * 1000;
		this.originX = obj.originX
		this.originY = obj.originY
		
		this.vx = getRandom(true) * 10;
		this.vy = getRandom(true) * 10;
		this.size = 2 + getRandom(false) * 10;
		this.dt = .05;
		this.color = '#f77';
		this.bound = 10 + getRandom(false) * 10;
	}	
	this.draw = function(){//DRAW HEART SHAPE
		var x0,x1,x2,x3,x4,x5,y0,y1,y2,y3,y4,y5,width=this.size,height=this.size
		x0 = this.x + 0.5 * width, y0 = this.y + 0.3 * height;
  		x1 = this.x + 0.1 * width, y1 = this.y + 0.0 * height;
    	x2 = this.x + 0.0 * width, y2 = this.y + 0.6 * height;
    	x3 = this.x + 0.5 * width, y3 = this.y + 0.9 * height;
    	x4 = this.x + 1.0 * width, y4 = this.y + 0.6 * height;
    	x5 = this.x + 0.9 * width, y5 = this.y + 0.0 * height;
		a.fillStyle = this.color;
		a.beginPath();
		a.moveTo(x0,y0);
	    a.bezierCurveTo(x1,y1,x2,y2,x3,y3);
	    a.bezierCurveTo(x4,y4,x5,y5,x0,y0);
		a.closePath();
		a.fill();
	}
	this.checkBound = function(){//CHECK IF PARTICUL IS OUT OF HIS BOUND
		if(this.x < this.originX - this.bound){ this.x = this.originX - this.bound; this.vx *= -1} ;
		if(this.x > this.originX + this.bound){ this.x = this.originX + this.bound; this.vx *= -1 } ;
		if(this.y  < this.originY - this.bound){ 
			this.y = this.originY - this.bound; 
			this.vy *= -1;			
		} 
		if(this.y > this.originY + this.bound){ 
			this.y = this.originY + this.bound; 
			this.vy *= -1;
		} 	
	}
	this.update = function(){//UPDATE POSITION 	
		if(this.vx>100)this.vx=100;
		if(this.vy>100)this.vy=100;
		this.x += this.vx * this.dt;
		this.y += this.vy * this.dt;		
		this.checkBound();
	}	
}	
	//
	function enterFrame(){// DO EVERY FRAME
		c.width=800
		c.height=800
		a.fillStyle = '#333';		
   		a.fillRect(0, 0, 800, 800); //CLEAN CANVAS
		for(i=0;i<p_array.length;i++){//UPDATE AND DRAW EACH PARTICULE
			var p = p_array[i];
			p.update();
			p.draw();
		}		
	}
	function checkBlackPoint(data){//CHEK IF THE PIXEL IS BLACK
		var collision = 0;
		p_array = []
		for (var i = 0; n = data.length, i < n; i += 4) {
			if (data[i] == 0) { // IF BLACK SET A PARTICULE WITH X AND Y POS
				collision++;
				if(collision%30==0){
				p = new Ptcl({originX:((i/4)%800),originY:Math.floor((i/4)/800)});
				p.init();
				p.draw();
				//
				p_array.push(p);
				}				
			}    
	   }
	}
	//
	function getRandom(bool){ 
		var rand = Math.random()
		if(bool){
			rand =  rand * 2 - 1
		}
		return rand;
	}
	function startApp(){
		var interval;	
		c.width=800
		c.height=800	
		a.fillStyle = 'rgba(30,30,30,1)';		
   		a.fillRect(0, 0, 800, 800);
		a.fillStyle = "black"  
		a.font = '300px sans-serif'	
	    a.fillText('LOVE', 0, 300,800);
	   	imageData = a.getImageData(0, 0, 800, 800);
	   	checkBlackPoint(imageData.data);
		interval = setInterval(enterFrame,1000/60);
	}

	

	 
		startApp();