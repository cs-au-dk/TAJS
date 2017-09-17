//values are just trial & error
w=window
f=w.innerHeight/w.innerWidth
w=1343
h=w*f

_=Math
C=_.cos
S=_.sin
R=_.random

F=512
Z=2200
P=6.28

l=[]
A=[]
B=[]

L=54
p=P/L
Y=N=0

//generate lamp posts in a circle on both sides and pointing to the center
df='000100110010000001011010010110110010010011011010'.split('')
for(i=0;i<L;i+=.5){
  v=-.25
  for(k=4;k>=3;k--) {
    x=k*F*S(p*i)
    z=k*F*C(p*i)
    x2=(k+v)*F*S(p*i)
    z2=(k+v)*F*C(p*i)

    for(j=0;j<16;j++) {
      xv=x
      zv=z
      //shorter way to check for: j==10||j==11||j==14||j==15
      if(j>9&&j&2){
        xv=x2
        zv=z2
      }
      A[N++]=xv+df[j*3  ]*10
      A[N++]=-df[j*3+1]*512
      A[N++]=zv+df[j*3+2]*8
    }
    i+=.5
    v=-v
  }
}

//store where lamp posts end
K=N

//generate random lines
k=0
for(i=L*8;i--;) {
  l[k++]=R()*P
  l[k++]=R()
  l[k++]=-R()*128-64
  l[k++]=R()*.2+.2;
}

N+=L*96;	//8*12

setInterval(function(){
  Y+=.004
  b.style.background='#000'

  //clear screen
  a.width=w
  a.height=h

  //generate trail lines & move them
  j = K
  k=i=0;
  for(;i<L*8;i++) {
    d=l[k++]
    p=l[k++]
    y=l[k++]
    e=l[k++]
    r=(3.1+p*.8)*F

    v=4*S(y+i)
    g=[0,v,v,0]
    for(v=0;v<4;v++) {
      x=r*S(d)
      z=r*C(d)

      A[j++]=x
      A[j++]=y+g[v]
      A[j++]=z
      if(v&1) d+=e
    }
    l[k-4]+=.02

    // // move lines in both directions
    // if(i<L*2) {
    // 	l[k-4]-=.02
    // } else {
    // 	l[k-4]+=.02
    // }
  }

  //rotation & transform 3d-2d all points
  k=0
  for(i=N;i--;) {
    z=A[k+2]*C(Y)-A[k]*S(Y)+Z
    B[k  ]=F*(A[k+2]*S(Y)+A[k]*C(Y))/z+256
    B[k+1]=F*A[k+1]/z+h*0.9
    B[k+2]=z/Z
    k+=3
  }

  //draw everything, color is based on the poly index
  // index < K -> lamp post
  // index >= K - trail lines, half red #f22, half white #fff
  k=i=0
  for(;i<N/3;i++) {
    c.globalAlpha=(2-B[k+2])/2
    c.fillStyle="#444"

    if(i*12>=K) {
      if(i*12<K + L * 12*4) {
        c.fillStyle="#f22"
      } else {
        c.fillStyle="#fff"
      }
    }

    //draw all lines, clip lamp post passing just in front of "camera"
    c.beginPath()
    for(j=4;j--;) {
      if((i%4<2 && B[k+2] > 0.1)||i%4>=2) {
        c.lineTo(B[k],B[k+1]);
      }
      k+=3
    }
    c.fill()

    //draw lamp post light after drawing the last top horizontal quad
    //lamp posts are formed of 4 quads:
    //0 & 1 vertical
    //2 & 3 top horizontal
    //avoid drawing lights on trail lights
    if(i % 4 == 3 && i*12<K) {
      c.fillStyle="#fff"
      c.beginPath()
      c.arc(B[k-3], B[k-2], (2-B[k-1])*13,0,P)
      c.fill()
    }
  }
}, 20)