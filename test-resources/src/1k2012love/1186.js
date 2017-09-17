/*
 * Classic snake game. Collect hearts before your opponent do. Use
 * arrow keys to control your snake.
 */

c.width = c.height = 600;
m = Math;
//offset between moves
d = [[-1,0],//left
     [0, -1],//up
     [1,0],//right
     [0,1]];//down
i = j = 2; // i is current offset index, j isopponent snake offset index
s = o = x = 0; // s is varaible that holds our snake o is reference to opponent snake, x used for heart animation

// colors that was used in game
k = '#000';
//draws a black rectangle to given location
function fillRect(x,y,height,width,color){
    a.fillStyle = color;
    a.fillRect(x*20,y*20,width*20,height*20);
}

//construct a new node for linked list
function cons(x,y,next){
    return {
	x:x,
	y:y,
	n:next};
}

//remove last node from snake and erease it from screen
function removeLast(current, last){
    if(!last.n){
	current.n = 0;
	fillRect(last.x,last.y,1,1,k);
    }else
	removeLast(last,last.n);
}

// check if given node memver of given list
// comparison is done by location only
function hasItem(list,node){
    return !list?0:node.x == list.x && node.y == list.y?1:hasItem(list.n,node);
}

function drawHeart(){
    fillRect(p.x,p.y,1,1,k);
    x = ++x%4;
    a.font = 15 + x + "pt Calibri";
    a.fillStyle = '#f00';
    a.fillText('♥', p.x*20, (p.y+1)*20);
}


//create a random point on screen for snake to collect 
function createPoint(){
    p ={x : m.floor(m.random()*30),
	y : m.floor(m.random()*30)};
    //if point is on snake create another one else draw it on screen
    hasItem(s,p)||hasItem(o,p)?createPoint():drawHeart();
}


function getHead(snake,offset){
    var res = cons(((snake.x + d[offset][0])+30)%30, // mod 30 makes sure that if we get out of the screen get it snake back from index 0
		   (((snake.y + d[offset][1])+30)%30),
		   snake);
    return (hasItem(s,res) || hasItem(o,res))?0:res;
}

function movesnake(snake,offset,color){
    // if we are starting the game do just set starting values
    // pass main logic
	//add a new node to snake
	snake = snake?getHead(snake,offset):0;
	if(snake){
	    //if we eat point create another point and not remove last node
	    (snake.x == p.x) && (snake.y == p.y)?createPoint():removeLast(snake,snake.n);
	    //draw newly created node
	    fillRect(snake.x,snake.y,1,1,color);
	    }

    return snake;
}

//main loop
function loop(){
    s = movesnake(s,i,'#00f');
    // move opponent
    if(s){
	var minimum = 99,
            min_index;
	
	for(val=j+3;val<j+6;++val){
	    var head=getHead(o,val%4),
	        distance=head?m.abs(head.x-p.x)+m.abs(head.y-p.y):99;
	    
	    if(minimum>=distance){
		minimum=distance;
		min_index=val;
	    }
	}//for
	j = min_index%4;

	// j = [((j-1)+4)%4, j ,(j+1)%4].map(function(x){
	//     var head = getHead(o,x);
	//     return [x,
	// 	    hasItem(head.n,head) || hasItem(s,head)?99:m.abs(head.x-p.x)+m.abs(head.y-p.y)];}).sort(function(a,b){return a[1]-b[1];
	// 						  })[0][0];
    }
    o = movesnake(o,j,'#0f0');
    //move old
    //set default values for game
    if(!s || !o){
	i = j= 2;
        s= cons(5,9,0);
	o = cons(5,21,0);
	fillRect(0,0,30,30,k);
	createPoint();
    }
    drawHeart();
    h=0;//this variable makes sure that we are doing only one move on every turn //set system unchanged
    setTimeout(loop,100);    
}


(function(){
    document.addEventListener('keydown', function(e){
	//37 = left arrow
	//38 = up arrow
	//39 = right arrow
	//40 = down arrow
	var val = e.keyCode - 37;
	
	if(((val - i) % 2)&& //new value is not same of opposite direction
	   d[val] &&
	   !h){//there was no move at current loop
	    h=1;// set system changed
	    i = val;
	}
    }, false);
    loop();
}());