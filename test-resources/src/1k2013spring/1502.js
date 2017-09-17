c.width=c.height=r=1e3;t=0;
s=[];//bamboo
for(i=0;i<19;i++){
  s[i]=[Math.random()*50+i*50,475+Math.random()*175,0];
}
setInterval(function(x,y,i){
  t++;
  g=Math.cos(4.75+t*.025)*25+25;// we use a different sync for bg color
  l=Math.cos(3.25+t*.025)*25+25;// l:0 => midnight // l:50 => noon
  m=Math.sin(3.25+t*.025)*25+25;// l:50 => midnight // l:0 => noon
  n=50-l;     // l:0 => midnight // l:50 => noon
  o=50-m;     // l:50 => midnight // l:0 => noon
  a.fillStyle='hsla(200,80%,'+g+'%,.5)';
  a.fillRect(0,0,r,r);
  for(i=0;i<r;i+=10){
    for(j=0;j<r;j+=10){
      if(j>700+12*Math.cos(i*5-t*.01)){                            // brown cave
        a.fillStyle='hsl('+(25+10*Math.cos(i-j))+',80%,25%)';
        a.fillRect(i,j,10,10)
      }else if(j>400+25*Math.cos(i+t*.05)){                        // green herb
        a.fillStyle='hsl('+(100+10*Math.cos(i+j))+',80%,50%)';
        a.fillRect(i,j,10,10)
      }else if(j>300+25*Math.cos(i*5+t*.05)){                      // red mountain
        a.fillStyle='hsl('+(25+.5*g)+',80%,50%)';
        a.fillRect(i,j,10,10)
      }else if(Math.sqrt((t%r-i)*(t%r-i)+(50-j*3)*(50-j*3))<50){ // greyish clouds
        a.fillStyle='hsl('+(50+10*Math.cos(i|j))+',50%,80%)';
        a.fillRect(i%r,j,10,10);
        a.fillRect((i+2*t)%r,j+75,10,10);
        a.fillRect((i+t)%r,j+150,10,10)
      }else if(Math.sqrt((l*20-i)*(l*20-i)+(m*20-j)*(m*20-j))<40){ // yellow sun
        a.fillStyle='hsl('+(50+10*Math.cos(i|j))+',80%,50%)';
        a.fillRect(i,j,10,10)
      }else if(Math.sqrt((n*20-i)*(n*20-i)+(o*20-j)*(o*20-j))<40){ // white moon
        a.fillStyle='hsl('+(50+10*Math.cos(i|j))+',80%,90%)';
        a.fillRect(i,j,10,10)
      }
    }
  }
  for(i=0;i<19;i++){ // bamboo
    if((s[i][2]+=.25)>33+Math.random()*50)s[i][2]=0;
    for(j=0;j<s[i][2];j++){
      a.fillStyle='hsl(100,80%,50%)';
      a.fillRect(s[i][0]+10*Math.cos(j*.1),s[i][1]-j*10,10,10);
      if(!(j%10)&&s[i][1]-j*10<400){ // Green dots
        a.fillStyle='hsl(100,80%,25%)';
        a.fillRect(s[i][0]+10*Math.cos(j*.1),s[i][1]-j*10,10,10);
      }
    }
    z(s[i][0],s[i][1],i);
  }
  w(850);v();
},25);
function z(x,y,i){
    //white
    a.fillStyle='hsl(0,80%,99%)';
    a.fillRect(x+Math.cos(i+.3*Math.cos(t*.1))*25,y-20,10,10);
    
    //trunk
    a.fillStyle='hsl(100,80%,12%)';
    a.fillRect(x+Math.cos(i+.1*Math.cos(t*.1))*25,y-10,10,10);
    a.fillRect(x+Math.cos(i)*25,y,10,10);
    
    //petals
    a.fillStyle='hsl('+(40-i*20)%200+',80%,50%)';
    a.fillRect(x+Math.cos(i+.3*Math.cos(t*.1))*25,y-30+m/5,10,10);
    a.fillRect(x+Math.cos(i+.3*Math.cos(t*.1))*25,y-10-m/5,10,10);
    a.fillRect(x+Math.cos(i+.3*Math.cos(t*.1))*25+10-m/5,y-20,10,10);
    a.fillRect(x+Math.cos(i+.3*Math.cos(t*.1))*25-10+m/5,y-20,10,10);
}
function w(x,y,i){
  for(i=0;i<t;i+=10) {
    if(i>t-10){
      a.fillStyle='hsl(0,50%,60%)';//worm
    }else if(i>t-50) {
      a.fillStyle='hsl(0,50%,50%)';//worm
    }else {
      a.fillStyle='hsl(0,80%,10%)';//hole
    }
    a.fillRect(i,x+Math.cos(i*.1)*10,10,10);
  }
}
function v(x,y,z){ // heart
  a.fillRect(r-30,r-50,10,10);
  a.fillRect(r-50,r-50,10,10);
  
  a.fillRect(r-20,r-40,10,10);
  a.fillRect(r-30,r-40,10,10);
  a.fillRect(r-40,r-40,10,10);
  a.fillRect(r-50,r-40,10,10);
  a.fillRect(r-60,r-40,10,10);
  
  a.fillRect(r-30,r-30,10,10);
  a.fillRect(r-40,r-30,10,10);
  a.fillRect(r-50,r-30,10,10);
  
  a.fillRect(r-40,r-20,10,10);
}