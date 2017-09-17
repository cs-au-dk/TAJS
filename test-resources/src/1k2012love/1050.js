w=c.width=c.height=300;
function ti(f){setTimeout(f,1000/30)}
function cl(){a.fillStyle="rgba(255,255,255,.015)";a.fillRect(0,0,w,w)}
function r(x){with(Math)return floor(random()*x)}
function p(x){with(Math)return(round(random()))?(-x):x;}
function dr(){
	with(a)
		strokeStyle='rgba('+(150+r(105))+','+r(255)+','+r(255)+',.5)',
		textAlign='center',
		textBaseline='middle',
		font=r(w/2)+'px a',
		fillStyle='rgba(255,0,0,.3)',
		lineCap='round',
		lineWidth=r(10)+1,
		save(),
		//scaleX,skewX,skewY,scaleY,translateX,translateY
		setTransform(1,p(0.01),p(0.3),1,1,1),
		beginPath(),
		moveTo(w/2,w/2),
		lineTo(r(w),r(w)),
		moveTo(w/2,w/2),
		fillText('❤',w/2,w/2),
		closePath(),
		stroke(),
		restore()
}
function run(){cl();dr();ti(run)}
ti(run)