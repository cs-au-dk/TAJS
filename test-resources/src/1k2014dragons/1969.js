w=window
h=w.innerHeight
w=w.innerWidth

_=Math
mC=_.cos
mS=_.sin
R=_.random

F=512
Z=2048

N=L=X=Y=0

A=[]
B=[]
C=[]
D=[]

addCube=function(s) {
  k=N/3
  j='000100010110001101011111'.split('')
  for(i=0;i<24;i++) {
    A[N++]=(j[i]-.5)*2*s
  }

  l='011332200445514662577367'.split('')
  for(i=0;i<24;i++) {
    D[L++]=k+(l[i]|0)
  }
}

for(z=200;z--;){
  addCube(100+z*5)
}
anim=.004

setInterval(function(){
  Y+=anim*R()+anim
  X+=anim*R()+anim
  y=Y
  x=X
  //X+=.01
  b.style.background='#444'

  //clear screen
  a.width=w
  a.height=h

  k=0
  //dy rotation
  for(i=N;i--;) {
    if(k%24*3==0) y-=anim*2
    C[k+2]=A[k+2]*mC(y)-A[k]*mS(y)
    C[k  ]=A[k+2]*mS(y)+A[k]*mC(y)
    k+=3
  }

  k=0
  //dx rotation
  for(i=N;i--;) {
    if(k%24*3==0) x+=anim*2
    B[k+1]=A[k+1]*mC(x)-C[k+2]*mS(x)
    B[k+2]=A[k+1]*mS(x)+C[k+2]*mC(x)
    k+=3
  }

  //transform 3d-2d
  k=p=0
  for(i=N;i--;) {
    C[k++]=F*C[p  ]/(C[p+2]+Z)+w/2
    C[k++]=F*B[p+1]/(C[p+2]+Z)+h/2
    p+=3
  }


  p=0
  for(i=0;i<L/24;i++) {
    c.strokeStyle="#fff"
    c.globalAlpha=(i/(L/24))
    c.beginPath()
    for(k=24;k--;){
      j=D[p++]
      c.moveTo(C[j*2],C[j*2+1]);
      j=D[p++]
      c.lineTo(C[j*2],C[j*2+1]);
    }
    c.stroke()
  }
}, 20)