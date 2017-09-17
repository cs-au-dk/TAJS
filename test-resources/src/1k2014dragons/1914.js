c.font="15px Arial";

function drawCharacter(c,y,o){
	c.fillStyle=o.c;
	c.fillRect(c,y,10,10);
	c.strokeRect(c,y,10,10);
}
/*
function triangle(x,y){
	c.beginPath();
	c.moveTo(x,y-5);
	c.lineTo(x+5,y);
	c.lineTo(x,y+5);
	c.fill();
}
*/
function showSkills(sy){
	for(i=2;i--;sy-=20, c.fillText(C[currentCharacter].s[i].n, 100, sy)){} //Show all the skills

	c.beginPath();
	c.moveTo(92,sy-6+(selectedSkill*20)-5);
	c.lineTo(97,sy-6+(selectedSkill*20));
	c.lineTo(92,sy-6+(selectedSkill*20)+5);
	c.fill();
	
	//triangle(92, sy-6+(selectedSkill*20)); //Show a triangle at the selected skill
}

function textPanel(){
	var px=0, py=100,h=150;

	c.fillStyle="#CCC";
	c.fillRect(px,py,300,h/3);
	c.strokeRect(px+1,py,298,h/3);

	if (A){ //If ally turn, show the current character's HP and skills
		c.fillStyle="#000";
		c.fillText(C[currentCharacter].n,px+5,py+20);

		c.fillText("HP: "+C[currentCharacter].h,px+5,py+40);
	
		showSkills(py+60);
	}
}

function doSkill(){
	//Subtract damage from enemy health and set damage text
	ehp-=F=C[currentCharacter].s[selectedSkill].damage; 
	
	W=(ehp<=0); //if ehp<=0, win
	
	damageShowAlpha=1;
	G=120;
	H=40;
}

function enemy(){
	t=getRandomAliveCharacter();
	d=getRandom(10,30); //Get random damage from 10-30
	
	C[t].h-=d; //Subtract damage from targeted health
	
	if (C[t].h<0) //Keep character health from going below 0
		C[t].h=0;
	
	damageShowAlpha=1;
	F=d;
	G=250;
	H=20+(t*30);
	
	L=!(C[0].h + C[1].h + C[2].h); //Checks HP of all characters, if all <0 then lose
}

function getRandomAliveCharacter(){
	t=getRandom(0,2);
	while(!C[t].h){
		t=getRandom(0,2);
	}
	
	return t;
}

function getRandom(x,y){
	return x+(new Date%(y+1));
}

function sDText(){
	c.fillStyle="rgba(255,0,0,"+damageShowAlpha+")";
	c.fillText("-"+F,G,H);
	
	damageShowAlpha=damageShowAlpha>.2?damageShowAlpha-.1:0;
}

b.addEventListener("keyup", function(e){
	if (A){
		if(e.which==40){
			if (++selectedSkill>1)
				selectedSkill=0;
		}
		
		if(e.which==13){
			doSkill();

			selectedSkill=0;

			currentCharacter++;
			
			if (A && !L){
				while(!C[currentCharacter] || !C[currentCharacter].h){
					currentCharacter++;
					if (currentCharacter>2){
						//Switch to enemy's turn
						//A=0;
						J=20;
						A=currentCharacter=0;
					}
				}
			}
		}
	}
});

function K(){
	enemy();

	if (!L){
		while(!C[currentCharacter] || !C[currentCharacter].h){
			currentCharacter++;
		}
	}
	
	A=1;
}

var selectedSkill=0,
	currentCharacter=0,
	ehp=1e3,
	damageShowAlpha=0;
	F=0, //damageText
	G=0, //damageTextX
	H=0, //damageTextY
	J=0, //frameTimerTime
	A=1, //allyTurn
	W=0, //win
	L=0, //lose
	C=[
	{
		n:"Warrior",
		c:"#CCC",
		h:100,
		s:[
			{
				n:"Slash",
				damage:80
			},
			{
				n:"Pierce",
				damage:50
			}
		]
	},
	{
		n:"Mage",
		c:"#00F",
		h:75,
		s:[
			{
				n:"Fire",
				damage:50
			},
			{
				n:"Bolt",
				damage:60
			}
		]
	},
	{
		n:"Thief",
		c:"#0F0",
		h:70,
		s:[
			{
				n:"Stab",
				damage:40
			},
			{
				n:"Bow",
				damage:50
			}
		]
	}
];
	
setInterval(function(){
	c.clearRect(0,0,300,150);

	W && c.fillText("You Win!", 125,100);
	L && c.fillText("You Lose!", 125,100);
	
	if (W==L){ //!W && !L
		textPanel();
		
		for(i in C)
			if (C[i].h){
				c.fillStyle=C[i].c;
				c.fillRect(280,10+i*30,10,10);
				c.strokeRect(280,10+i*30,10,10);
			}
			//C[i].h && drawCharacter(280,10+(i*30),C[i]);
		
		//Draw enemy
		c.fillStyle="#0A0";
		c.fillRect(45,20,40,15); //Head
		c.fillRect(55,10,8,10); //Ear
		c.fillRect(50,35,10,15); //Neck
		c.fillRect(10,50,60,25); //Body
		c.fillRect(20,75,10,5); //Feet
		c.fillRect(50,75,10,5);
		c.fillRect(0,65,10,5); //Tail
		
		c.fillStyle="#000";
		c.fillText("HP: "+ehp,90,20);
		
		//Showing damage
		damageShowAlpha && sDText();
		
		//Enemy turn timer
		if (J>0){
			J--;
		}else{
			!A && K(); //Do enemy turn
		}
	}
}, 100);