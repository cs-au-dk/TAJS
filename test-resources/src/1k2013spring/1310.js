w=540;h=540;o=[];t=0;u=0;v=0;_d=3500;
			n=Math.cos;m=Math.sin;b=Math.PI;
			gw = 50;l=1;d=0;tt=1;kl=1;_o=a.lineTo;

			p = function(_p) {
                f = 400 / (_d + _p.z);
                return {
                	x:(_p.x*f+(w/2))-u, 
                	y:(_p.y*f+(h/2))-v,
                	z:_p.z 
               	};
            }
            
            rx = function(t,k) {
                _t = t*b/180
                y1 = k.y * n(_t) - k.z * m(_t)
                z1 = k.y * m(_t) + k.z * n(_t)
                return {x:k.x, y:y1, z:z1};
            }
            ry = function(t,k) {
                _t = t*b/180
                z2 = k.z * n(_t) - k.x * m(_t)
                x2 = k.z * m(_t) + k.x * n(_t)
                return {x:x2,y:k.y,z:z2}
            }
            rz = function(t,k){
                _t = t*b/180
                x3 = k.x * n(_t) - k.y * m(_t)
                y3 = k.x * m(_t) + k.y * n(_t)
                return {x:x3, y:y3,z:k.z};
            }
            
            df = function(f) {
                a.beginPath();
                a.moveTo( p( rz(t, f[0]) ).x, p( rz(t, f[0]) ).y);
                if(d%3!=0)a.lineTo( p(rz(t,f[1])).x, p( rz(t, f[1])).y );
                a.lineTo( p(rz(t,f[2])).x, p( rz(t, f[2])).y);
                a.lineTo( p(rz(t,f[3])).x, p( rz(t, f[3])).y);
                if(_d<0)a.stroke();
                if(_d>=0)a.fill();
            }
            rr = function(p,x,y,z) {
            	xr = rx(x,p);
            	yr = ry(y,xr); 
            	zr = rz(z,yr); 
            	return zr;
            }

            da = function() {
            	var x,y=0;
            	for(y=0; y<gw;++y){
            		for(x=0; x<gw;++x){
            			var f = o[y][x];
		            	df(f);
            		}
            	}
            }

            mg=function(s,sp) {
                o = [];
            	var x,y = 0;
            	for(y=-gw/2; y<gw/2;++y){
            		var r = [];
            		for(x=-gw/2; x<gw/2;++x){
            			var z = (m(y*x)*(x*y*(_d/1000)))*s;
            			r.push([
            					{ x:(x*s)+sp, 			y:(y*s)+s, 	z:(z) },
            					{ x:(x*s)+s,			y:(y*s)+s, 	z:(z) },
            					{ x:(x*s)+s, 			y:(y*s)+sp, z:(z) },
            					{ x:(x*s)+sp, 			y:(y*s)+sp, z:(z) }
            				   ]);
            		}
            		o.push(r);
            	}

            }

            rg = function(_x,_y,_z){
            	var x,y=0;
            	for(y=0; y<gw;++y){
            		for(x=0; x<gw;++x){
            			_f = o[y][x];
                        o[y][x] = [rr(_f[0],_x,_y,_z), rr(_f[1],_x,_y,_z), rr(_f[2],_x,_y,_z),  rr(_f[3],_x,_y,_z)];
            		}
            	}
            }

            c.width = w; c.height = h;
            
            	mg(10,2);
            	rg(0,0,-90);
            setInterval(function(){ 
                // mg(10,2);rg(0,0,-90);
            	a.clearRect(0,0,w,h);
            	a.fillStyle = '#f6007b';
            	a.fillRect(0,0,w,h);
            	a.fillStyle = 'rgba(255,255,255,0.8)';
            	d+=l;
            	t+=kl/4; tt+=1;
            	_d-=l*16;
            	if(_d>3500){_d=3500;l=0-l;}
            	if(_d<-6000){
            		_d=-6000;l=0-l;
            		};            	
            	da();

            },33);