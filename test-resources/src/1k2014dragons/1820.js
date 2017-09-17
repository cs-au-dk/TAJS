/** Globals **/
// a: init function
// b: body
// c, d, e: board size
// f: rules (0: reversi / 1: find 4 / 2: XnO / 3: XnO 3D)
// g: game state  (0: game over / 1: playing)
// h: HTML
// i, j, k, l: loop vars
// m: model (array filled with -1/0/1)
// p: current player(1: O / -1: X)
// q: onclick

/** Specific vars for init function **/
// i: lines
// j: columns
// k: cell number

/** Specific vars for play function **/
// c,d,e: params

// XnO, XnO3D, Find 4
// r: sum of the marks that are needed to win

// Reversi:
// s: cell is playable (0: not playable / 1: playable)
// t: direction is playable
// u, v: loop vars
// w: score


// Draw function (redraw everything after each move)
// c: reset (if == 0)
a=function(c,d,e){
  
  // Init cell number, HTML
  l=0;
  h="<center><font face=arial><p><button onclick=m=[];f=2;a(p=g=s=1)>XnO<button onclick=m=[];f=3;a(p=g=s=1)>XnO3D<button onclick=m=[];f=1;a(p=g=s=1)>Find4<button onclick=m=[];f=w=0;m[27]=m[36]=-1;m[28]=m[35]=1;a(p=g=s=1)>Flip</button><p>";
  
  // Show HTML and game status  
  
  if(c){

    // Loop on tables, write table HTML
    for(i="1113"[f];i--;){
    
      h+="</table><p><table border>";
    
      // Loop on lines
      for(j="8633"[f];j--;)

        // Write line HTML, loop on cells
        for(h+="<tr>",k="8733"[f];k--;)
        
          // Write cell HTML
          h+="<th width=20 onclick=q("+[l,7-j,7-k]+") id=t"+l+">"+"X\xa0O"[1+(m[l]=m[l++]||0)];
    }
  
    h+="</table><p>";
    
    // Board not full
    if(~m.indexOf(0)){
    
      h+=(
        g
        ?
          
        // Game not over: show next
        "XnO"[p+1]+" next<p>"
        
        :
        
        // Game over (not reversi): show winner
        "XnO"[-p+1]+" won"
      )
      
      // Reversi
      if(!f){
      
        // Leader, pass button
        h+=(w?w>0?"O>X":"X>O":"O=X")+"<p><button onclick=a(p=-p)>pass";
      }
    }
    
    // Board full
    else{

      h+=
      
      f
      
      ?
      
      // Not reversi: show winner or draw
      (g?"draw":"XnO"[-p+1]+" won")
      
      :
        
      // Reversi: show winner or draw
      (w?w>0?"O won":"X won":"draw")
    }
  }

  b.innerHTML=h;
}

a();

// onclick
// c: current cell number
// d: current cell's line
// e: current cell's column
q=function(c,d,e){
  
  // If the game is not over and the cell is empty
  if(g&&!m[c]){
  
    // XnO / XnO 3D
    if(f>1){
    
      // Put a mark
      // Update model, set total
      // Test victory
      for(i=3;i--;)
        for(j=3;j--;)
          if(
            k=i*9,
            l=j*3,
            ~[
              m[k+j]+m[k+j+3]+m[k+j+6],   // Columns 2D
              m[k+l]+m[k+l+1]+m[k+l+2],   // Lines 2D
              m[k+4]+m[k+0]+m[k+8],       // Diagonals 2D
              m[k+4]+m[k+2]+m[k+6],       // Diagonals 2D
              m[l+10]+m[l]+m[l+20],       // Lines 3D
              m[l+10]+m[l+2]+m[l+18],     // Lines 3D
              m[i+12]+m[i]+m[i+24],       // Columns 3D
              m[i+12]+m[i+6]+m[i+18],     // Columns 3D
              m[13]+m[0]+m[26],           // Diagonals 3D
              m[13]+m[2]+m[24],           // Diagonals 3D
              m[13]+m[6]+m[20],           // Diagonals 3D
              m[13]+m[8]+m[18],           // Diagonals 3D
              m[l+i]+m[l+i+9]+m[l+i+18]   // Same cell in all tables
            ].indexOf((m[c]=p)*3)
          )
          g=0;
    }
    
    // Find 4
    if(f==1){
    
      // If a wrong cell is clicked, apply gravity
      for(;35>c&&!m[c+7];c+=7);

      // Test if 4 marks are aligned
      for(i=6;i--;)
        for(j=7;j--;)
          if(
            k=i*7+j,
            ~[
              j<4&&m[k]+m[k+1]+m[k+2]+m[k+3],         // Horizontally
              i<3&&m[k]+m[k+7]+m[k+14]+m[k+21],       // Vertically
              i<3&&j<4&&m[k]+m[k+8]+m[k+16]+m[k+24],  // Diagonally 1
              i<3&&j>2&&m[k]+m[k+6]+m[k+12]+m[k+18]   // Diagonally 2
            ].indexOf((m[c]=p)*4)
          )
          g=0;
    }

    // Reversi
    if(!f){

      // Reset cell's playability
      s=0;

      // For each direction
      for(i=2;~i--;){
        for(j=2;~j--;){

          // Reset that direction's playability
          // If the neighbour is the opponent
          if(i|j&&(t=0,m[8*(d+i)+e+j]==-p)){

            // Loop on the next neighbours in that direction
            // If current color is found, stop, good direction
            // If an empty cell is found, stop, bad direction
            for(
              k=d+i,l=e+j;
              ~k&&k<9&&~l&&l<9&&(m[8*k+l]!=p||!(s=t=1))&&m[8*k+l];
              k+=i,l+=j
            );

            // If this direction is playable
            if(t){

              // Loop on the opposite neighbours
              for(
                u=d,v=e;
                u!=k||v!=l;
                u+=i,v+=j
              ){

                // Toggle them
                m[u*8+v]=p;
              }
            }
          }
        }
      }
      
      // Compute score
      w=0;
      for(i=64;i--;){
        w+=m[i];
      }
    }

    // Change player
    if(s)p=-p;
    
    // Redraw board
    a(1);
  }  
}