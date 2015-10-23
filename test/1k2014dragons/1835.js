/*
	  nanoPhysics by Fredrik Karlsson (fredrik.karlsson@kaiba.se)
	  
	  Interactive physics demo. Initially, the objects are floating weightless in space. Click to toggle gravity on or off.
	  	This small physics engine was made for physics lovers by inspiration from "The Little Engine" by Emory Hufbauer. My ambition was to create an engine for real physics simulation of soft bodies according to the actual laws of motion.
	  	
	  	The engine has the following features:
	  	1) Elastic objects.
	  	2) Non-elastic collisions between objects and between objects and walls.
	  	3) Viscous damping of collisions.
	  	4) Kinetic friction force between objects. Its magnitude proportional to normal force.
	  	5) Perfect conservation of linear momentum.
	  	6) Approximate conservation of angular momentum.
	  	7) Gravity can be toggeled on or off.
	  	
	  	Known bugs and limitations:
	  	x) High speed collisions may cause objects to penetrate each other and become entangled. This is however very unlikely to happen with the parameters chosen for this demo.
	  	x) Static friction not implemented.
	  	x) The angular momentum not completely conserved, it would require about 100 bytes more code.
	  	x) Runs slow on firefox due to the use of eval based packer.
	  	
	  	The provided uncompressed code is very hairy since I had the aim to reach 1kB without packing it. I failed. The uncompressed closure compiled code is 1231B. I could get it below 1kB only by final compression with RegPack v3 by Siorki. 
	  */
	  D=p=M=0; W = 425; P=[]; _=Math; $=_.sqrt; I=V=k=52; C=20;
	      
	  b.onmousedown=function(){M=M?0:1};
	  
	  H = a.height *= W/a.width;
	  a.width = W;
	  c.shadowColor = c.fillStyle="#0CF";
	  c.shadowBlur = C;
	  for (;I--;) { //GENERATE OBJECTS: random quadrilaterals 
	   		  i = I&3;
	   		  j = I>>2;
	   		  
	          e = .5+_.random();              
	          
	          U = k*e;
	          
	          u = i==0||i==3?0:1; v = i>1?0:1;
	          P[p++] = {x : j%4*99+C+U*u, y : j*C+V*v, v : C*(e-1), b : 0, f : 0, g : 0, o : j, l : U, n:$(U*U + V*V), i : i };
	  }
	  
	  
	  setInterval(function() {
	      for (I=C; I--;) {              
	              for (i=p; i--;) {
	                      m=P[i]; 
	                      for (j=p; j--;) {
	                              n=P[j];
	                              
	                              U = m.x-n.x;
	                              V = m.y-n.y;
	                              F=f=g=0; e=1;
								  l = $(U*U + V*V);
								  
	                              if (m.o==n.o&j>i) { //INTERNAL FORCES   
	                                      U = U/l; V = V/l;
	                                      b =  (m.v-n.v)*U + (m.b-n.b)*V;
	                                      T = l-(m.i-n.i == 2?m.n:m.i == 1||n.i == 2?m.l:k);
	                                      T = -C*T-5*b; f=T*U; g=T*V;  //spring force with viscous damping
	                                      
	                              } else { //EXTERNAL FORCES: object-object collisions 
	                              		
	                              		if (l<4&j>i) {			//corner-corner collisions
	                              		 	T = C*4/l - C;
	                              		 	f=T*U; g=T*V;   //spring force
	                              		 }
	                              		 
	                                      o = P[n.i == 3?j+3:j-1];
	                                      u = o.x - n.x;
	                                      v = o.y - n.y;
	                                      l = u*u + v*v;
	              
	                                      d = (U*u + V*v)/l;
	                                      l=$(l);
	                                      u = u/l; v = v/l;
	                                      q= 8 + 2*(U*v - V*u); 
	                                              
	                                      if (d>0&d<1&q>0&q<C) {	//side-corner collisions and friction
	                                      		  e -= d;
	                                              V = m.v-n.v*e-o.v*d; U = m.b-n.b*e-o.b*d;
	                                              
	                                              T = C*(q + M*V*v - M*U*u); //spring force with viscous damping
	                                              V = V*u + U*v;
	                                              
	                                              F = V*_.min(_.abs(T/V), k)/2; //friction force proportional to normal force
	                                              
	                                              f = -T*v - F*u;
	                                              g =  T*u - F*v;
	                                              
	                                              o.f -= f*d; //apply forces to objects according to Newton's III law
	                                              o.g -= g*d;
	                                      } 
	                              }
	                              
	                              m.f += f; //apply forces to objects according to Newton's III law
	                              m.g += g;
	                              
	                              n.f -= f*e; //apply forces to objects according to Newton's III law
	                              n.g -= g*e;		
	                      }
	              }
	      
	              for (i=p; i--;) {  //EXTERNAL FORCES, WALL COLLISIONS & POSITION UPDATE
	                      m=P[i];
	                                              
	                      m.g -= M*5; //gravity force
	                      
	                      if (m.y<M*5) {m.g -=C*(m.y+M*m.b-M*5);  if (M) m.f=m.v=0;} //object-wall collisions
	                      if (m.y>W) m.g -=C*(m.y+M*m.b-W);
	                      if (m.x<0) m.f -=C*(m.x+M*m.v);
	                      if (m.x>W) m.f -=C*(m.x+M*m.v-W);
	
	                      m.v += m.f/k; //Newton's II law: update new velocities and positions
	                      m.b += m.g/k;	
	                      m.x += m.v/k;
	                      m.y += m.b/k;
	                      m.f = m.g = 0;
	              }
	      }

	      c.clearRect(0,0,W,H);
	      c.beginPath();
	      for (i=p; i--;) { //DRAW OBJECTS
	              m=P[i];
	              D?c.lineTo(m.x,H-m.y):c.moveTo(m.x,H-m.y); D++; 
	              i&&m.o!=P[i-1].o&&(D=0);
	      }
	      c.fill();
	      
	      
	  },C);