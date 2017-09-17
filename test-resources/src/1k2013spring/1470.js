// [x,y,type,size,color,speed,ttl]
// type:
//  0: bullet 10x10
//  1: shot speed upgrade 20x20 (speed x 2)
//  2: enemy 30x30
//  4: player 20x20
//  8: shot size upgrade 20x20 (size x 2)
//
// n=enemy spawn time
// A=actors
// B=actor
// C=collidable actor
// S=size (of actor)
// D=dimension (of canvas)
// $=shot cooldown (bonuses reduce)
// W=shot width
// u=score
A=[[x=90,y=180,4,20,'#fff']]
K={}
function onkey(v,e){K[e.which]=v}
//turn on mask on keydown
b.onkeydown=onkey.bind(null,1)
//turn off mask on keyup
b.onkeyup=onkey.bind(null,u=n=s=0)
D=c.height=c.width=200
$=50
N=75
W=10
G='splice'
setInterval(function () {
  //clear
  a[f='fillStyle']='#000'
  a[F='fillRect'](0,0,D,D)
  a[f]='#ff0'
  a.fillText(u,5,10)
  // move ship
  x+=K[39]|0-K[37]|0
  y+=K[40]|0-K[38]|0
  // keep ship bound
  x=A[0][0]=x<0?0:x>180?180:x
  y=A[0][1]=y<0?0:y>180?180:y
  // spawn enemy every N ticks
  ;(n=++n%N)||A.push([Math.random()*170,-30,2,30,'#00f',1])
  // countdown to shot
  ;(s&&--s)||K[32]&&A.push([x+(20-W)/2,y,0,W,'#f00',-1])&&(s=$)
  for(i=A.length;B=A[--i];){
    // draw entity
    a[f]=B[4]
    a[F](B[0],B[1]+=B[5]|0,S=B[3],S)
    // delete if out of bounds
    // delete if ttl == 0
    if(B[1]<-B[3]||B[1]>D||--B[6]==0) A[G](i,1)
    // Collision loop
    else for(j=A.length;C=A[--j];) {
      i!=j&&
      B[0]<C[0]+C[3]&&
      B[0]+S>C[0]&&
      B[1]<C[1]+C[3]&&
      B[1]+S>C[1]&&
      // colisions
      (
        //B==Enemy
        //C==Bullet
        B[2]&2&&
        !C[2]&&
        (
          A[G](i>j?i:j,1),A[G](i<j?i:j,1),
          j<i&&i--,
          j=0,
          //chance to spawn upgrade is 5% exclusive
          (
            //speed of shots
            (o=Math.random())<.05&&A.push([C[0],C[1],1,20,'#0f0',0,750])
            ||
            //size of shots
            o>.95&&A.push([C[0],C[1],8,20,'#f0f',0,750])
          ),
          u++
        )
        ||
        //B==Player
        B[2]&4&&
        (
          //C==Enemy
          C[2]&2&&
          (
            x=90,y=180,$=50,W=10,i=j=n=s=u=0,A=[A[0]]
          )
          ||
          //C==Shot Upgrade
          C[2]&1&&
          (
            A[G](j,1), $=$/2|0
          )
          ||
          //C==Shot Upgrade
          C[2]&8&&
          (
            A[G](j,1), W=20
          )
        )
      )
    }
  }
},4);