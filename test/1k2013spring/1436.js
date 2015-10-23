D=c.height=c.width=720
T=10, //Character Size
L=D/T, //# of Characters in a dimension
M = [], //Current state
Z=[-1,0,1], 
R=Math.round;
a.shadowBlur = 2;

for(i=0;i<L*L;++i)M[i]=0; //initialize everything to dead
C=1;
function A(r,c) { //Set current column and row r to alive, increment C by c if exists
	if(c)C+=c;
	M[r*L+C]=2;
	return A;
}

//Setting up a "Gosper glider gun"
A(5)(6) //Current column = 1
 (5,1)(6)
 (5,9)(6)(7)
 (4,1)(8)
 (3,1)(9)
 (3,1)(9)
 (6,1)
 (4,1)(8)
 (5,1)(6)(7)
 (6,1)
 (3,3)(4)(5) //c=21
 (3,1)(4)(5)
 (2,1)(6)
 (1,2)(2)(6)(7)
 (4,10)(5)
 (4,1)(5);

setInterval(function() {
	//Clear canvas
	function f(p){a.fillStyle = a.shadowColor = p;}
	f('#000')
    a.fillRect(0, 0, D, D);
    
	M = M.map(function(v,i) { //Map current state into new state while drawing current one
		f(v > 1 ? (M[i]=v=1)&&'#AFA' : "#0F0"); //if(v>1) {M[i]=v=1; f('#AFA') } else f("#0F0");
		
        v&&a.fillText(String.fromCharCode(12448 + Math.random() * 96), i%L*T, T*(i-i%L)/L); //i%L is the column,(i-i%L)/L is the row. Draw Random matrix character at c*T,r*T
		
		//sums the number of living cells that are neighbouring this cell including itself
		s=0;
		Z.map(function(p) {
			Z.map(function(q) {
				x=i%L+p;
				s+=(x>=0 && x<L && M[i+q*L+p] && 1)||0 //1 iff the cell at i with the steps r in rows and c in columns exist and is alive(>0), 0 otherwise
			})
		}); //'Map' is shorter than 'forEach'
		
		return s-3?s==4&&v: //Equals to (s==3?1:v&&s==4)?1:0 which equals to (v?s==3||s==4:s==3)?1:0; which means if I'm alive I'll stay alive if the sum of my vicinity is 3/4 (me+2/3 neighbours)
				v?1:2 //changed to only return color for new cells
			//or if I'm dead then exactly 3 neighbours.
			//new cells are colored white 
	});
},99);

c.onmousemove=function(e){
	e.offsetY<D && e.offsetX<D && (M[R(e.offsetY/T)*L+R(e.offsetX/T)]=2);
}