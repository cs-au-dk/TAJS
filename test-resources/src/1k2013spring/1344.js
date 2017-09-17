/**
         * Rebirth, by Jérémy Tuloup
         * js1k 2013-Spring entry
         * 
         * Description:
         * Every year, spring is the time for renewal.
         * It brings life back and buds grow again as the sun rise in the sky.
         * If you want new trees, just refresh the page.
         * Enjoy :)
         * 
         * It runs smoothly on a i7 computer, with Chrome 24, Firefox 18 and 
         * Internet Explorer on Windows, and also in Chrome on Linux, but a bit 
         * slower in Firefox on Linux (maybe because of my Firefox install). 
         * I noticed a little glitch on Safari on Mac with the leaves, just at 
         * the beginning of the demo. 
         * 
		 * About the code:
		 * - There are a lot of magic numbers, found after a lot of testing
		 * - 7 is used instead of 2*Math.PI for drawing circles, and 1.6 instead of Math.PI/2
		 * - Minification is done in two steps:
		 * 		1. With Closure Compiler (http://closure-compiler.appspot.com/home) to
		 * 		remove whitespaces and comments.
		 * 		2. With JS Crush (http://www.iteral.com/jscrush/) for a big compression!
		 * - The two functions use the same signature, because it allows more 
         * compression with JS Crush
         * 
		 */
	
        w = c.width = window.innerWidth;
        h = c.height = window.innerHeight;
        t = k = G = -1; // initialize time, wind and growth at the same time
		T = []; // list of trees

		// grow a tree
		// (posX, posY, radius, scale, depth, parent, self pointer)
        A = function(x,y,r,s,d,p,m) {
        
			// compute new coordinates
            x+=h/4*s*Math.cos(r);
            y-=h/4*s*Math.sin(r);
			
			// add the new node [x,y,scale,parent,radius]
            u=[x,y,(d>5)?0:s,p,r];
            m.n.push(u);

			// end of recursion
            if (d > 5) return; // only 6 for better performance

            var i = u; // remember self node
            if(Math.random() < 0.7) {
                A(x,y,r-Math.random()*0.3-0.2,s*0.6,d+1,i,m); // left branch
                A(x,y,r+Math.random()*0.3+0.2,s*0.6,d+1,i,m); // right branch
            } else
                A(x,y,r,s*0.8,d,i,m); // continue with main branch
				
			return m;
        }

		// create the trees
        for(i=3;i--;)
            T.push(A((i+0.5)*(w/3), h, 1.6,1,0,[(i+0.5)*(w/3),h,1.6,1,0],{n:[]}));
            
        // main
        setInterval(function(x,y,r,s,d,p,m) {
 
			// update t and G at the same time
			G = Math.sin((t+=0.005)-1)+1;
 
            // sky and sun
            a.fillStyle = a.createRadialGradient(
				w/2*(Math.cos(t)+1), h-0.8*Math.sin(t)*h, w/32, 
				w/2*(Math.cos(t)+1),  h-0.8*Math.sin(t)*h, w/2
			);
            a.fillStyle.addColorStop(0.01, '#FD4');
            a.fillStyle.addColorStop(0.05, '#FB0');
            a.fillStyle.addColorStop(0.6, '#CEF');
            a.fillRect(0, 0, w, h);
            
            a.strokeStyle = '#000';

            // render and update trees
            for(i=3;i--;)
                for (j in T[i].n)
                    if (T[i].n[j][2]) {
						// draw a branch from the current node to its parent
						a.lineWidth = T[i].n[j][2]*w/32;
						a.beginPath();
						a.moveTo(
							T[i].n[j][0]+= Math.cos(t*8+i), // update x at the same time
							T[i].n[j][1]+= Math.sin(t)/(8+i) // update y at the same time
						);
						a.lineTo(T[i].n[j][3][0],T[i].n[j][3][1]);
						a.stroke();
                        // draw a circle to make the tree look better
						a.fillStyle = '#000';
						a.beginPath();
						a.arc(T[i].n[j][0],T[i].n[j][1],w/64*T[i].n[j][2],0, 7);
						a.fill();
					} else {
						// drawing a text for the leaf helps to save a few bytes
                        a.fillStyle = '#261', // green
						a.font = 20*G+'px Arial',
						a.fillText('✿',T[i].n[j][3][0]-10*G,T[i].n[j][3][1]+8*G)
					}
            
			// render a little hill at the bottom
			a.fillStyle = '#000';
			a.beginPath();
			a.arc(w/2,h*6.44,h*5.55,0,7);
			a.fill();
			
			// compute a new wind, going from right to left
			if (k>0.7||k<0)
				for (k=0,i=5,W=[];i--;){
					y = Math.random()*h;
					W.push([
						w, // start x
						y, // start y
						w+Math.random()*99+99, // end x
						y+Math.random()*50 // end y
					]);
				}
			
			// render and update wind
			for (k+=0.02,i=5;i--;){
				dx = W[i][0]-W[i][2]; dy = W[i][1]-W[i][3];
				a.strokeStyle = 'rgba(255,255,255,'+(1.2-k/0.7)+')';
				a.beginPath();
				a.moveTo(W[i][0]+=dx*k,W[i][1]+=dy*k);
				a.lineTo(W[i][2]+=dx*k,W[i][3]+=dy*k);
				a.stroke();
			}

        }, 33);