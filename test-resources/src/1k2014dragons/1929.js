with(Math) {                                                                   // get cos() instead of Math.cos()
  e=a.width;                                                                   // we fill e with the screen width;
  x=e/2;                                                                       // track mouseX (start at the middle of the canvas)
  t=0;                                                                         // track time
  p=[];                                                                        // track particles (particle : [x,y,vx,vy,ttl])
  c.globalCompositeOperation = "lighter";

  
  setInterval(function(z){                                                     // Main loop (z is set only so all function got the same signature)
    p.push([x+cos(t++)*9,0,0,2,.01*sin(t),0,e/3]);                             //   add particles and increase time
    c.fillStyle="hsl(0,99%,0%)";                                               //   background clr
    c.fillRect(0,0,e,e);                                                       //   background clr
    for(i=p.length;i--;){                                                      //   loop over particles
      if(p[i]){                                                                //     if we have a particle 
        c.fillStyle="hsl("+ceil(15+cos(t/i)*15)+",90%,35%)";                   //       draw particles
        c.fillRect((p[i][0]+=p[i][2]+=p[i][4]),                                //       draw particles & update x & vx
                   p[i][1]+=p[i][3]+=p[i][5],                                  //       draw particles & update y & vy
                   e/p[i][6]*.5,1);                                            //       draw particles & increase particles size
        p[i][6]--<0 &&                                                         //       decrease particles ttl and if ttl == 0
          p.splice(i,1);                                                       //         remove old particles
      }
    }
    
  },16);
}
function m(z,x,y,w){                                                           // distance function
  return (z-x)*(z-x)+(y-w)*(y-w)                                               // give us a² = b²+c² (dist² = xOffset²+yOffset²)
};
a.onmousemove=function(z){                                                     // Mousemove listener for the canvas
  x=z.clientX;                                                                 // we keep x
}

a.onclick=function(z){                                                         // clean the canvas
  c.globalCompositeOperation = "source-over";
  c.fillStyle ='#000';
  c.fillRect(0,0,e,e);
  c.globalCompositeOperation = "lighter";
  p=[];
}