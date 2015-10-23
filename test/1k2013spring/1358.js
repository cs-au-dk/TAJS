function mDn(e) {
	a.clearRect (0, 0, c.width, c.height);
	a.fillStyle="black";
	x = 20;
	a.fillText("P=Pen | E=Rub | /\\=Big P/E | \\/=Small P/E",x,20);
	a.fillText("R=Red+ T=Red- ["+r+"]",x,40);
	a.fillText("G=Green+ H=Green- ["+g+"]",x,60);
	a.fillText("B=Blue+ N=Blue- ["+b+"]",x,80);
	for(k in dOs) {
		a.fillStyle = dOs[k][5];
		a.fillRect(dOs[k][1],dOs[k][2], dOs[k][3], dOs[k][4]);
	}
	hOX = e.offsetX;
	hOY = e.offsetY;
	delete dOs["z"];
	dOs["z"] = ["z", hOX, hOY, brS, brS, (dC==wh) ? bl: dC];
	n = hOX+"_"+hOY;
	if(mD) dOs[n] = [n, hOX, hOY, brS, brS, dC];
}
var w=window,brS=2, bl="black", wh="white", rC = "black", hOX, hOY, mD = 0,dOs = {},dC=rC,r = 0,g = 0,b = 0;
c.width = w.innerWidth;
c.height = w.innerHeight;
document.body.style.cursor = 'none';
w.onmousemove = function(e){mDn(e)}
w.onmousedown = function(e){mD = 1;mDn(e)}
w.onkeyup = function(e){
	k = e.which || e.keyCode;
	rC="rgb("+r+","+g+","+b+")";
	if(k==38) brS++;
	if(k==40) brS--;
	if(k==82) r+=10;
	if(k==71) g+=10;
	if(k==66) b+=10;
	if(k==84) r-=10;
	if(k==82) g-=10;
	if(k==88) b-=10;
	if(k==80) dC = rC; 
	if(k==69) rC=wh;
	dC=rC;
}
w.onmouseup = function(e) {mD = 0};