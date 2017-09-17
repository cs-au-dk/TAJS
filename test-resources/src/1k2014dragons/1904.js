f = b = 0, 
      C="f00,f90,ff0,3f0,09f,63f,000,fc9,f9f,f39,999,036,f99,fff".split(','),
      T="g4a2gk2g2ag2k2g2ag2k2ga2g2k2a3g3a5g,a7g2a3gk2ga2gk2g3agk4a2g2k2a4g2,a7a9a7ga2g6k4gk3g2ag4,a7a9g2a2g2k2agk4gk2g3gk2ga3g2,a7ag4a2gk3g5k4ga2g4ka5g2,ag2a3gk2g3agk4a2g2k2a4g2".split(','),
      //L="a9a9a9ag9g9a7gk9a9a7gk2g2agk2ga4gk2gagk2ga3g4a2g3a6g3a2g2,a9a9a9a9a2g9a7gk9a9a7gk2gagk2ga5gk2gagk2ga3g4a2g3a6g3a2g3,a9a9a9a9a9a9agk9a9a7gk2gagk2ga5gk2gagk2ga3g3a3g3a6g3a2g3,a9a9a9a9a2g9a7gk9a9a7gk2gagk2ga5gk2gagk2ga3g4a2g3a6g3a2g3,a2g9a9a7g9a9a7gk3g6a9a7gk2gagk2ga5gk2gagk2ga3g3a3g3a6g3a2g3,a2g9a9a7gk8a9a7gk3g6a9a2kga3gk2gagk2ga5gk2gagk2ga3g3a2g3a6g3a3g3".split(',');
      M="a9g5agk5agk2g2a2g4,a9a7gk4a2gk2ga3g3,a9a8gk3a3gk2ga3g3,a9a7gk4a2gk2ga3g3,a2g5ag6gk3g3gk2ga3g3,a2g5agk5gk3g3gk2ga3g3".split(','),
      N="a2gk2ga5gk2gagk2ga4g3a6g3a2g3,a2gk2ga5gk2gagk2ga3g3a6g3a2g3,a3gk2ga5gk2gagk2ga3g3a6g3a2g3,a2gk2ga5gk2gagk2ga3g3a6g3a2g3,gk2ga5gk2gagk2ga3g3a6g3a2g3,gk2ga5gk2gagk2ga3g3a6g3a2g3".split(',');
  
  // insane sprite drawing method
  P=function(z,x,y,w,s){c.fillStyle="#"+C[z]}
  S=function(z,x,y,w,s){for(i=0,g='';z.length>i;i++)g+=new Array((o=z.charCodeAt(i)-97,n=+z[i+1],(n?(i++,n):1)+1)).join(o+',');z=g.split(',');for(i=0,g='';z.length>i;i++)if(v=+z[i])P(v),c.fillRect(x+i%w*s,s*(i/w|0)+y,s,s)}
      
  setInterval(function(z,x,y,w,s){
      S('l',0,0,1,80*5);
      
      // rainbow
      g= 36;
      d = b>1;
      for(i=0;g<210;g+=46,d=!d,i++)
        for(i=0,s=98;6>i;i++)
          P(i), c.fillRect(-g+146,150+d+i*17+d*4-1,46,17); // Can change 17 to 15 to shave a couple of bytes
      // Eliminating one for
      //for(i=0;21>i;i++)
        //S('abcdef',-i*46+146,150+d+d*4-1,1,17),d=!d; // To eliminate for
      b++;
      b%=4;
      
      d = 7*(f>1);
  
      // Tail
      S(T[f], -37-6*(f==4)+146,150+d+32,6+(f==4),6);
      
      // Legs
      //S(L[f], -20+146,150+d+77,26,6);
      S(M[f], -20+146,150+d+77,7,6);
      S(N[f], 10+146,150+d+95,21,6);
      
      // cat body
      // P(6); Unnecessary, color is already black
      c.fillRect(9+146,150+d+-8,s,s+5);
      c.fillRect(4+146,150+d+-2,s+10,91);
      c.fillRect(-2+146,150+d+3,120,80);
      c.fillRect(-2+146,150+d+3,s,80+6);
      
      P(7);
      c.fillRect(9+146,150+d+-2,s,90);
      c.fillRect(4+146,150+d+3,s+10,80);
      
      P(8);
      c.fillRect(21+146,150+d+3,74,80);
      c.fillRect(15+146,150+d+9,86,69);
      c.fillRect(9+146,150+d+15,s,57);
      
      //S('j',167,144+d+22,1,6);
      P(9);
      c.fillRect(21+146,150+d+15,6,6);
      c.fillRect(49+146,150+d+9,6,6);
      c.fillRect(49+146,150+d+49,6,6);
      c.fillRect(15+146,150+d+55,6,6);
      c.fillRect(38+146,150+d+66,6,6);
      c.fillRect(21+146,150+d+72,6,6);
      // Should be ,5,5), but we want to optimize...
      c.fillRect(89+146,150+d+21,6,6);
      // Should be ,5,6), but we want to optimize...
      c.fillRect(67+146,150+d+9,6,6);
      // Should be ,5,6), but we want to optimize...
      c.fillRect(36+146,150+d+32,6,6);
      c.fillRect(27+146,150+d+43,6,6);
          
      // cat head
      S("a2g2a8g2a3gk2ga6gk2ga2gk3ga4gk3ga2gk4g4k4ga2gk8k4gagk7k7g2k3ngk5ngk2g2k3g2k3gkg2k2g2km2k9m2g2km2kgk2gk2gkm2gagk3g7k2ga3gk6k4ga5g9g",6*(f<4-!f)+52+146,150+d+17-(f==5)*8,16,6);
            
      f++;
      f%=6;
  }, 70 // Should be 70, but 69 will save 1 byte and 80, 2 bytes
  );