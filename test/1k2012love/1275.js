v = [[0,5],[2,3],[3,2]]//the number of broken hearts, 2's and 3's
			u = undefined
			h = Math
			z = h.round
			l = h.floor
			k = h.random
			i=20;
			p=22
			c.onclick =function(e)
			{
				x = z(e.pageY/p) - 1//get the clicked y position
				y = z(e.pageX/p) - 1//get the clicked x position
				w = y * p//get the screen x position of the tile
				g = x * p//get the screen y position of the tile
				//check if the tile exisits and if it needs to be checked again, if not leave the function
				if(f[x]==u||f[x][y]==u||f[x][y]==-1)
				return
				e=f[x][y]//get the value of the field
				
				//draw a heart
				a.strokeStyle = a.fillStyle = '#fff'
				a.beginPath()
				b=10;
				a.moveTo(w + b, g + 6)
				a.bezierCurveTo(w + 2, g, w, g + 12, w + b, g + 18)
				a.bezierCurveTo(w + i, g + 12, w + 18, g, w + b, g + 6)

				if(e>0)//if the heart contains a 1, 2 or 3 show it
					a.fillText(e, y * p + 7, x * p + 15)
				else
				{
					//draw the broken line
					a.moveTo(w + b, g + 6)
					a.lineTo(w+7, g + b)
					a.lineTo(w+13, g + 15)
					a.lineTo(w+b, g + i)
				}
				a.stroke()//put the heart on the screen
				if(e==0)//if its a broken heart tell the player he lost
						alert("game over")
				if(e>0)//if its a number calculate the new score
				{
					s*=e
					f[x][y]=-1
					if(m == s)//if the player has evertyhing display you win
					{
						alert("you win")
					}
				}
			}
			m=1
			s=1
			r =[[0,5],[0,5],[0,5],[0,5],[0,5]]//the row's number of broken hearts and points
			t =[[0,5],[0,5],[0,5],[0,5],[0,5]]//the column's number of broken hearts and points
			f = [
						[1,1,1,1,1],
						[1,1,1,1,1],
						[1,1,1,1,1],
						[1,1,1,1,1],
						[1,1,1,1,1]
					]//the playing field
			
			for(q = 0; q < v.length; q++)
			{
				o = 0
				while(o < v[q][1])
				{
					x = l(k()*5)//generate x pos
					y = l(k()*5)//generate y pos
					n = v[q][0]
					if(f[x][y] == 1)//if the field contains one heart
					{
						f[x][y] = n//place the three heart
						o++//increase the counter
						//if not a broken heart increase the row, column and target score
						if(n!=0)
						{
							r[y][1]+=n-1
							t[x][1]+=n-1
							m *= n
						}
						//add the broken heart value and reduce the total points
						else
						{
							r[y][0]++
							t[x][0]++
							r[y][1]--
							t[x][1]--
						}
					}
				}
			}
			a.fillStyle = "#000"//set the color
			for(x = 0; x < 5; x++)//loop over the x
			{
				for(y = 0; y < 5; y++)//loop over the y
				{
					a.fillRect(x * p, y * p,i,i)//draw the square
				}
				a.fillText(r[x][0]+"/"+r[x][1], x * p + 3, 117)//set the number of the row
				a.fillText(t[x][0]+"/"+t[x][1], 112, x * p + 15)//set the number of the column
			}