//Some initializations
with(m=Math) P=pow;
c.width = c.height = f = 600;
e=f/5;
t=0;

//Very complicated 4 dimensional array
//It's kinda the thing that make this all work
R = [[[[0,1],[1,0]],[[-1,0],[0,-1]]],[[[-1,0],[0,1]],[[0,-1],[1,0]]]];

//Setting initial colors using the double for loop function
C = F( function(u){
		u[i][j] = '#' + ((B(i,j)?200:140) + m.floor(55*m.random())).toString(16) + '0000';
	});
	
//Draw function using the double for loop function and ignoring return value
setInterval(
function(){
/*  C = */F( function(){
			//Canvas stuff
			a.fillStyle = C[i][j];
			a.fillRect(5*i, 5*j, 5, 5);
		});
	t = ++t%2;
	
	//Next iteration using the double for loop function
	C = F( function(u,z){
		//Selecting correct rule
		r = z[i%2][j%2];
		//Retrieve color from previous state by applying rule
		u[i][j] = C[(i+r[0]+e)%e][(j+r[1]+e)%e];
	}, R[t]);
}, 66);

//The famous double for loop function I talked about earlier on
//This lambda will generate a gird by applying the given function on each cell
// v is a function
// w a part from the 4d-array (which is omitted in case of init and draw)
function F(v,w){
	//Create square array of size e with double for loop
	u = Array(e);
	for(i=0;i<e;i++){
		u[i] = Array(e);
		for(j=0;j<e;j++){
			//Execute function from parameter inside double for loop
			v(u,w);
		}
	}
	return u;
}

//Inside or outside the heart; that's the question
function B(i,j){
	//Mapping of array coordinates to canvas coordinates
	x = 8*(i%60)/e-2;
	y = 2-8*(j%60)/e;
	//Using the famous equation of (x^2+y^2-1)^3 < x^2*y^3
 	return P(P(x,2)+P(y,2)-1,3) < (P(x,2)*P(y,3));
}