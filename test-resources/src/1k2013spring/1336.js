//** No eval included in this source **//

//Caching 500 and 1000 due to its frequently (Saving few bytes =p)
J=500;D=J*2;
c.width=c.height=J;

//q = main x coordinate, w = main y coordinate, j= elapsed seconds   r = number of game loops 
//k= monster bullets array  t = spring warrior bullets  y= monster arc radius   
// o=monster x    p = monster y    l=monster speed
z=1;
o=q=250;
w=420;
X=h=m=j=e=r=0;	
k=[];
t=[];
u=Math.random;	//caching Math.random
p=60;
y=45;
O=D*p;
U=10;
l=6;
//Tracking mouse and assign Spring warrior x coordinate value
onmousemove=function(b){
    g=b.pageX-5,q=g>10&&g<J?g:q
};

//Adding projectiles for Spring warrior
c.onclick=function(){
    t.push([q,w])
};
//Method for rending any shapes required in this game
function B(b,n,C,x,i,v){
    a.fillStyle='#'+n,
	a.beginPath(),
	b?(a.font=v+'px Arial',
	a.fillText(C,x,i)):a.arc(C,x,i,0,7),
	a.fill(),
	a.stroke()
}
setInterval(function(b,f,d,s,v,n){
	//Loop stops when z becomes 0 (negative number)
    if(z){
        a.clearRect(0,0,J,J);
        o-=l*=o<=5||o>=430?-1:1;
        if(j%10>7||r%p>45){
            k.push([o,p,u()*D,750])
        }
		//Drawing background
        B(0,'82c0ff',0,0,D);
		//Drawing boss, me health texts
        B(1,'000','Boss:'+O+' Me:'+U,150,30,20);
		//Drawing boss body
        B(0,m,o+y,p+y,y);
		//Drawing his grumpy face
        B(1,'000','ಠ益ಠ',(l>4?10:42)+o,99,15);
		//Caching boss and spring warrior projectiles array size
        Q=k.length;W=t.length;
        for(b=0;b<Q+W;b++){
            b<Q?(E=k[b],E[0]-=(E[0]-E[2])/p,E[1]-=(E[1]-E[3])/p,B(1,'fff','❆',E[0],E[1],20)):(R=t[b%W],R[1]-=10,B(1,'f0f','❀',R[0],R[1],20))
        }
        m='ffe6b0';
		//Insane collision testing and defining win or lose
        for(h=0;h<k.length+t.length;h++){
            h<k.length?(f=k,d=h,s=q,v=w,n=10):(f=t,d=h%t.length,s=o,v=p,n=y*2),P=f[d],(P[1]>J||P[1]<10||(!(P[0]>s+n||s>P[0]+25||P[1]>v+n||v>P[1]+10)&&(X=1)))&&(f.splice(d,1),X&&(n<11?--U:(O-=396,m='f00'),(U<0||O<-J)&&(z=0),X=0))
        }
		//Draw Spring warrior
        B(0,'00f',q,w,10);
		//Measuring times
        ++r%33>31&&j++
    }
},30)