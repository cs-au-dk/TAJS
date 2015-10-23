e=atob('BTgONnYBOA82VhU1aBBWIjYEsBMiOwQAEyJQo7ECIhAjuwI1TjgwEjROOTESQ103MwFDPTczAVEvuFICUX8GuQIibThIEiINmEQScEw4OBJkYytwImQUrGEiZFwtUSJkDZxbIg=='); 
js1k = function(){
	j=l=360;
	k=[];
	r=[];
	a.onmousemove=function(u,v,w,x,y,z){
		j=(u.pageX);
		l=(u.pageY);
	};
	m=function(u,v,w,x,y,z){
		if(u%2) 
			return (e.charCodeAt(i*5+~~(u/2))>>4);
		else 
			return (e.charCodeAt(i*5+~~(u/2))&15);
	};
	for(x=0;x<76760;x++)
		r.push(Math.sin(x*.0176));
	n=function(u,v,w,x,y,z){
		h=u*r[z+90]-v*r[z];
		z=u*r[z]+v*r[z+90];
		u=w*r[y+90]-h*r[y];
		v=w*r[y]+h*r[y+90];
		h=z*r[x+90]-u*r[x];
		w=z*r[x]+u*r[x+90];
		return ({x:v,y:h,z:w});
	};
	p=function(u,v,w,x,y,z){
		return ({x:v+u.x,z:u.z+x,r:y,g:z,y:u.y+w});
	};
	c.font=56+'px x';
	c.shadowBlur=1;
	c.shadowColor='red';
	for(i=20;i--;){;
		c.fillStyle='#000';c.fillRect(0,0,a.width*1,a.height*1);
		c.fillStyle='#f00';
		c.fillText('ζςשДθ~个ㄩ'[1*m(1)],0,45);
		f=c.getImageData(0,0,76,76).data;
		for(y=76;y--;){
			for(x=76;x--;){
				if(f[(y*76+x)*4]>20)
					k.push(
						p(n(x*m(8)-30*m(8),y*m(8)-30*m(8),f[(y*76+x)*4]/100*(1*m(9)+1),		30*m(5),30*m(6),30*m(7)),
						30*(1*m(2)-7),(1*m(4)-6)*20,(1*m(3)-3)*20,1*m(9),1*m(0)),
						p(n(x*m(8)-30*m(8),y*m(8)-30*m(8),f[(y*76+x)*4]/100*(1*m(9)+1)*-1,	30*m(5),30*m(6),30*m(7)),
						30*(1*m(2)-7),(1*m(4)-6)*20,(1*m(3)-3)*20,1*m(9),1*m(0))
					);
			};
		}
	}
	
	s=[];
	i=76;for(x=i;x--;){
		s.push({
			x:a.width*Math.random(),
			y:a.height*Math.random(),
			z:Math.random()*.8+.2
		});
	};;
	c.shadowBlur=0;
	g=360;
	setInterval(A=function(u,v,w,x,y,z){
		_=~~((j-a.width*.5)/a.width*40*1);
		$=~~((l-a.height*.5)/a.height*30);
		
		x=c.createRadialGradient(
			a.width*.20-_,
			a.height*.20-$,
			76,
			a.width*.20-_,
			a.height*.20-$,
			a.width
		);
		x.addColorStop(0.02,'#eee');
		x.addColorStop(0.03,'#115');
		x.addColorStop(01,'#112');
		c.fillStyle=x;
		c.fillRect(0,0,a.width*1,a.height*1);
		
		c.fillStyle='#eee';
		i=76;for(x=i;x--;){;
			c.font=(s[x].z*8)+'px x';
			c.fillText('★',s[x].x-_/((s[x].z*8)),s[x].y-$/(s[x].z*8));
		};
		
		o=[];
		i=k.length;for(x=i;x--;){
			f=n(k[x].x,k[x].y,k[x].z,+~~($*2+360),+~~(110-_*3),0);
			f.r=k[x].r;
			f.g=k[x].g;
			o.push(f);
		};
		o.sort(h=function(u,v,w,x,y,z){
			return u.z-v.z;
		});
		
		for(x=i;x--;){;
			c.fillStyle='rgba('+
				~~((-o[x].z/360+1)*([555,760,820,999,140,151][o[x].g])/4)+','+
				~~((-o[x].z/360+1)*([555,760,820,999,140,151][o[x].g]%100)*2.5)+','+
				~~((-o[x].z/360+1)*([555,760,820,999,140,151][o[x].g]%10)*25)+
			',1)';
			c.fillRect(
				a.width*.6+o[x].x,
				a.height*.360+o[x].y,
				(o[x].r+1.0)*2,
				(o[x].r+1.0)*2
			);
		};
		
		i=360;for(x=i;x--;){
			c.putImageData(
				c.getImageData(
					0,
					a.height*.76-x*2+r[(g*20+x)%360]*20,
					a.width*1,
					1
				),
				(r[x*20+g*17]*(5+x/20)),
				a.height*.76+x+$/5
			);
		};
		if(!--g)
			g=360;
		
		c.fillStyle='rgba(0,0,0,0.50)';
		c.fillRect(0,0+a.height*.76+$/5,
			a.width*1,
			a.height*1
		);
		
	},25);
	
};

js1k();