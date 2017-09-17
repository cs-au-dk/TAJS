var x=y=0, 
  p1 = -2.25, p2 = 0.75,
  q1 = -1.5, q2 = 1.5,
  w = c.width, h=c.height
  wd = (p2-p1)/h,
  ht = (q2-q1)/w,
  mc = 100

var mcol = function(x, y){
	var p = x * wd + p1,
          q= y * ht +q1,
  		prev_i = prev_r = 0;
  		
      for (var n =0; n < 255; n++){   		
      		var r = prev_r * prev_r - prev_i * prev_i +p
      		var i = 2 * prev_r * prev_i +q;
      		if ((r*r + i*i) < mc){
				prev_r = r
				prev_i = i
			}	
      		else{
				return n
			}	
      }
	return n;
}

a.fillStyle = "#555";
a.fillRect (0, 0, c.width, c.height);
a.strokeStyle = "#000";

var l = function(){
   for (var chunk=0; chunk <500; chunk++){
  	   a.fillStyle = "hsl(20,100%,"+ (mcol(x,y))%100 + "%)";
  	   a.fillRect (y,c.height-x,1,1);//a.fillRect (x,c.height-y,1,1);
  	   if (x >=c.height){
  	       continue;
  	   }
  	   if (y >= c.width){
  	       x+=1;
  	       y=0;
  	       continue;
  	   }  
  	   y++;
      } 	     
}

var q = setInterval(l, 2);