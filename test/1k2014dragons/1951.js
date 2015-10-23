// Create track
for(T=[B=F=Z=D=i=0];i<600;i++){
	if(i%30<1)m=D,D=1800-(i%4-2)*600;
	T[i]=m+(D-m)*(.5-Math.cos(i%30/9)/2)
}
// Timer loop
setInterval(function(){
	m=Z/5|0;
	
	// Reset canvas and fill with black
	c.fillRect(0,0,a.width=a.height=600,600);
	
	// Dragon flight waving and tilting
	c.translate(300,300+Math.cos(Z/99)*99);
	c.rotate(i>540?Z/50%6:Math.cos(i/9)/9);
	
	// Sky with some polar light effects
	c.fillStyle="hsl("+99+m%99+",50%,9%)";
	c.fillRect(-600,-600,1200,600);

	// Flickering stars
	for(c.fillStyle=c.shadowColor="#FFF",i=600;i--;)c.fillRect(400-i*i%800,-i%600,1,Z*i%.9);
	
	// Moon
	c.fillText("(",99,-99);

	// Loop through visible distance of the track
	for (i=99+m;i>m;i--){
		D=i-Z/5;
		
		// Hills
		y=Math.cos(Math.cos(i/9+i/50)+8)*300;
		
		// Calculate road and camera direction from track data
		O=T[m]+(T[m+1]-T[m])*(Z%5)/5-T[i];
		
		if(i%3<1){
		
			// Draw ground and hills
			c.rotate(Math.cos(i)/9); // Do we need to rotate at all?
			c.fillRect(-600,300/D+y/D+9,1200,Math.cos(i));
			c.rotate(Math.cos(i)/-9);
						
			// Draw white lines
			c.shadowBlur=D<9?30:0;
			if(i%2){
				c.beginPath();
				c.moveTo(-30/D+O/D,300/D+y/D);
				c.lineTo(30/D+O/D,300/D+y/D)
			}
			if(i%2<1){
				c.lineTo(30/D+O/D,300/D+y/D);
				c.lineTo(-30/D+O/D,300/D+y/D);
				c.fill()
			}
			c.shadowBlur=0
		} 
		
		// Road
		c.fillStyle="#000";
		c.fillRect(-600/D+O/D,300/D+y/D,1200/D,60/D);
		
		if(i%8<1){ // Draw street or tunnel lights
			c.fillRect(-800/D+O/D,300/D+y/D-800/D,30/D,800/D);
			c.fillStyle="#FFA";
			i%400<80?c.fillRect(-100/D+O/D,300/D+y/D-600/D,200/D,50/D):c.fillRect(-600/D+O/D,300/D+y/D-800/D,80/D,20/D)
		}
		if(i%400<80){ // Tunnel walls
			c.fillStyle=i%2?0:"#222";
			c.fillRect(600/D+O/D,300/D+y/D-900/D,1400/D,900/D);
			c.fillRect(-2e3/D+O/D,300/D+y/D-900/D,1400/D,900/D);
			c.fillRect(-2e3/D+O/D,300/D+y/D-900/D,4e3/D,300/D)
		}
		
		// Draw cars
		c.fillStyle="#000";
		if((i+~~B)%30==0){
			c.fillRect(150/D+O/D,300/D+y/D-200/D,260/D,160/D);
			c.fillStyle="red";
			c.fillRect(180/D+O/D,300/D+y/D-90/D,60/D,30/D);
			c.fillRect(340/D+O/D,300/D+y/D-90/D,60/D,30/D);
			// Turn signal
			c.fillStyle="#FFA";
			i%300<99&&i%9<5?c.fillRect(140/D+O/D,300/D+y/D-90/D,60/D,30/D):0
		}
		if((i+F)%40<1){
			c.fillStyle="#000";
			c.fillRect(-400/D+O/D,300/D+y/D-200/D,260/D,1/D*160);
			c.fillStyle="#FFA";
			c.fillRect(-240/D+O/D,300/D+y/D-90/D,60/D,30/D);
			c.fillRect(-400/D+O/D,300/D+y/D-90/D,60/D,30/D);
			// Police car
			if(i%200>99)
				c.fillStyle=i%2?"red":"#00F",
				c.fillRect(i%3-(i%2*80+260)/D+O/D,300/D+y/D-220/D,60/D,30/D)
		}
			
		// City
		if(i>500&&i%4<1)for(j=i%9*8;j--;)
				c.fillStyle=j%5?"#FFA":"#999",
				c.fillRect((i%12?-2e3:2e3)/D-j%5*200/D+O/D,300/D+y/D-(j-j%5)*60/D,200/D,200/D);
		
		c.fillStyle="#999"
	}
	// Integrate car movement
	B-=.3;F+=.5;
	// Move forward and loop back to start if track ends
	Z++;Z%=3e3
},20)