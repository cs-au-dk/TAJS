var wait         = 0;

var json = 
{
	"cuerpo":{
			"x":360,
			"y":240,
			"neck":0,
			"leftarm":-30,
			"rightarm":30,
			"lefthand":10,
			"righthand":-10,
			"leftleg":-30,
			"rightleg":30,
			"leftfoot":10,
			"rightfoot":-10,
			"second":0
		}
}

function repetir_num_seg(num,time,fx)
{
	var idtime = setInterval(function()
		     {
			fx();

			num--;

			if (num ==0) clearInterval(idtime);

		     },time);
}


function control(parte,valor,f)
{
	valor = -(valor *f);
	switch (parte)
	{
		case "la": json.cuerpo.leftarm    = json.cuerpo.leftarm   + valor;
			   break;
		case "lh": json.cuerpo.lefthand   = json.cuerpo.lefthand  + valor;
			   break;
		case "ll": json.cuerpo.leftleg    = json.cuerpo.leftleg   + valor;
			   break;
		case "lf": json.cuerpo.leftfoot   = json.cuerpo.leftfoot + valor;
			   break;
		case "ra": json.cuerpo.rightarm   = json.cuerpo.rightarm  + (-valor);
			   break;
		case "rh": json.cuerpo.righthand  = json.cuerpo.righthand + (-valor);
			   break;
		case "rl": json.cuerpo.rightleg   = json.cuerpo.rightleg  + (-valor);
			   break;
		case "rf": json.cuerpo.rightfoot  = json.cuerpo.rightfoot + (-valor);
			   break;
		case "x" : json.cuerpo.x          = json.cuerpo.x         + (-valor);
			   break;
		case "y" : json.cuerpo.y          = json.cuerpo.y         + (-valor);
			   break;
	}

	update();
}


function recorrerArbol(arbol,tmp,end)
{

	for (var i=0; i<arbol.length; i++)
	if (typeof arbol[i].hijos=="undefined")
	{
		wait = wait + (parseInt(arbol[i].rot) *3);
		(function(c){repetir_num_seg(c.rot,8,function(){control(c.parte,c.mov,2);});})(arbol[i]);
	}
	else resultado = recorrerArbol(arbol[i].hijos,1,null);

	if (tmp==0)
	{
		setTimeout(end,wait);
		wait = 0;
	}
}

var a1 = [{"hijos":[{"parte":"la","rot":"45","mov": "1"},{"parte":"lh","rot":"45","mov": "1"}]},{"hijos":[{"parte":"rl","rot":"45","mov": "1"},{"parte":"rf","rot":"25","mov": "-1"}]}];
var a2 = [{"hijos":[{"parte":"la","rot":"45","mov":"-1"},{"parte":"lh","rot":"45","mov":"-1"}]},{"hijos":[{"parte":"rl","rot":"45","mov":"-1"},{"parte":"rf","rot":"25","mov": "1"}]}];
var a3 = [{"hijos":[{"parte":"ra","rot":"55","mov": "1"},{"parte":"rh","rot":"45","mov": "1"}]},{"hijos":[{"parte":"ll","rot":"45","mov": "1"},{"parte":"lf","rot":"25","mov": "-1"}]}];
var a4 = [{"hijos":[{"parte":"ra","rot":"55","mov":"-1"},{"parte":"rh","rot":"50","mov":"-1"}]},{"hijos":[{"parte":"ll","rot":"45","mov": "-1"},{"parte":"lf","rot":"25","mov": "1"}]}];


function draw(context, stickfigure)
{
	context.beginPath();
	context.moveTo(10, 355);
	context.lineTo(710, 355);
	context.stroke();
	context.strokeStyle="black";
	context.font="16pt Verdana";
	
	context.strokeText("Press any key!"  , 30,80);
	context.strokeStyle = "black";
	context.lineWidth   = 9;
	context.lineCap     = "round";
	context.translate(stickfigure.x, stickfigure.y);
	context.beginPath();
	context.moveTo(0,0);
	context.save();
	context.rotate(((stickfigure.leftleg) / 360) * (Math.PI * 2));
	context.lineTo(0, 60);
	context.translate(0,60);
	context.rotate(((stickfigure.leftfoot) / 360) * (Math.PI * 2));
	context.lineTo(0,60);
	context.restore();
	context.save();
	context.moveTo(0,0);
	context.rotate(((stickfigure.rightleg) / 360) * (Math.PI * 2));
	context.lineTo(0, 60);
	context.translate(0,60);
	context.rotate(((stickfigure.rightfoot) / 360) * (Math.PI * 2));
	context.lineTo(0,60);
	context.restore();
	context.moveTo(0,0);
	context.rotate(((stickfigure.neck) / 360) * (Math.PI * 2));
	context.lineTo(0, -100);
	context.translate(0, -100);
	context.moveTo(20,-20);
	context.arc(0, -30, 30, 0, Math.PI*2, true);
	context.moveTo(0,5);
	context.save();
	context.rotate(((stickfigure.rightarm) / 360) * (Math.PI * 2));
	context.lineTo(0,60);
	context.translate(0,60);
	context.rotate(((stickfigure.righthand) / 360) * (Math.PI * 2));
	context.lineTo(0,45);
	context.restore();
	context.moveTo(0,5);
	context.rotate(((stickfigure.leftarm) / 360) * (Math.PI * 2));
	context.lineTo(0,60);
	context.translate(0,60);
	context.rotate(((stickfigure.lefthand) / 360) * (Math.PI * 2));
	context.lineTo(0,45);
	context.restore();
	context.stroke();
}

function update()
{
	c.height=480
	c.width =720
	c.width = c.width;
	draw(a, json.cuerpo);
}

update()

document.onkeydown = function (event)
		     {
				recorrerArbol(a1,0,function(){recorrerArbol(a2,0,function(){recorrerArbol(a3,0,function(){recorrerArbol(a4,0,null)})})});
		     }