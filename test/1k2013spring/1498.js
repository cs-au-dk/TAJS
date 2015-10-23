s=c.width=s=c.height=innerHeight-64;T=48;gs=s/T+5;
		P=[];m=Math;G=[];
		S="FF0000FF2F00FF5F00FF8F00FFBF00FFEF00DFFF00AFFF007FFF004FFF001FFF0000FF0F00FF3F00FF6F00FF9F00FFCF00FFFF00CFFF009FFF006FFF003FFF000FFF1F00FF4F00FF7F00FFAF00FFDF00FFFF00EFFF00BFFF008FFF005FFF002F"
		for (x = 0; x < gs; x++){g=[];for (y = 0; y < gs; y++){g[y] = {x:0,y:0};}G.push(g);}
		t=0;window.setInterval(function()
		{a.fillStyle="#111";a.fillRect(0,0,s,s);
			t+=.025;if(t>36){t=0}
			for (x = 0; x < gs-1; x++){for (y = 0; y < gs-1; y++)
			{
				if(G[x][y].x != x) G[x][y].x -= G[x][y].x/x;if(G[x][y].y != y) G[x][y].y -= G[x][y].y/y;
				a.beginPath();
				a.moveTo(x * T + G[x][y].x - T, y * T + G[x][y].y - T);
				a.lineTo((x+1) * T + G[x+1][y].x - T, (y) * T + G[x + 1][y].y - T);
				a.strokeStyle= "#"+S.substr(m.floor((x/gs+t)*32%32)*6,6);
				a.stroke();
				
				a.beginPath();
				a.moveTo(x * T + G[x][y].x - T, y * T + G[x][y].y - T);
				a.lineTo((x) * T + G[x][y+1].x - T, (y + 1) * T + G[x][y + 1].y - T);
				a.strokeStyle= "#"+S.substr(m.floor((y/gs+t)*32%32)*6,6);
				a.stroke();F=m.sin(t)
				dx = ((x * (T+2)) - s/2)*F;dy = ((y * (T+2)) - s/2)*F;d = m.sqrt(dx * dx + dy * dy);r=s/3;Q=(d/r)/3
				if (d < r){G[x][y].x = dx*Q;G[x][y].y = dy*Q;}
			}}
		},66)