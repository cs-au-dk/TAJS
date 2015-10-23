w=a.width
h=a.height
c.globalCompositeOperation='lighter'
p=[]
with(Math)z=2*PI,y=PI/3,R=random,C=cos,S=sin,Q=sqrt
s=(N=Date.now)()

m=a.cloneNode(0)
n=m.getContext('2d')
n.fillStyle='#2a2430'
n.fillRect(0,0,w,h)
n.translate(w/2,h/2)
n.beginPath()
n.arc(200,-250,30,0,z)
n.fillStyle='#fff9af'
n.fill()

function A(a,b){d=Q(a*a+b*b)
    p.push(a*C(y)-b*S(y),S(d/100)*100-C(d/25)*8,a*S(y)+b*C(y))}

for(j=120;j>=-120;j-=10)
    for(i=-120;i<=120;i+=10)A(i-5,j-5),A(i-5,j+5),A(i+5,j+5),A(i+5,j-5),p.push(0,0,0)

n.fillStyle='#2a2430'
n.strokeStyle='#50ffff'
n.beginPath()

for(i=0;i<p.length-2;i+=3)
    if(p[i])k=250/(p[i+2]+250),n.lineTo(k*p[i],k*p[i+1])
    else n.closePath(),n.fill(),n.stroke(),n.beginPath()

r=[0,z/3,z/3*2]

p=[]
n=c
n.strokeStyle='#c2ff14'
~function L(){
    requestAnimationFrame(L)
    t=N()-s
    s+=t
    t/=6

    for(i=t|0;i--;){
        u=R()*t-t/2
        v=-R()*t
        p.push({x:w/2,y:h/2,u:u/2,v:v,l:0})
    }

    t/=4
    n.clearRect(0,0,w,h)
    n.drawImage(m,0,0)

    for(i=p.length;i--;){
        q=p[i]
        q.x+=t*q.u
        q.y+=t*q.v

        k=2*q.l|0
        n.beginPath()
        n.arc(q.x,q.y,(60-q.l)/6+10,0,z)
        n.fillStyle='rgba('+[260-k,k+50,k,(120-k)/300]+')'
        n.fill()

        if((q.l+=t)>60)p.splice(i,1)
    }

    for(j=r.length;j--;){
        r[j]+=t/20

        n.beginPath()

        q=-30*j+10
        a=r[j]-.1
        k=250/(150*S(a)+250)
        n.moveTo(w/2+150*k*C(a),h/2+k*q)

        a=r[j]+.1
        k=250/(150*S(a)+250)
        n.lineTo(w/2+150*k*C(a),h/2+k*q)

        n.stroke()
    }
}()