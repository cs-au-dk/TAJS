W=innerWidth-32;
            H=innerHeight-32;
                var N = 0|(W/7);
                c.width = W;
                c.height = H;

            var data = c.toDataURL();
            a.fillRect(0,0,W,H);
                
            setInterval(function(){

                
                var countX = Math.round(W/N);
                var countY = Math.round(H/N);
                var C = countX * countY;

                for (var j = 0; j < countY; ++j)
                    for (var i = 0; i < countX; ++i)
                    {
                        var index = (i + j * countX);                        
                        var epaisseur =  (255 & (data.charCodeAt(index*2 + 1) | 0)) / 255;
                        var height = 255 & (data.charCodeAt(index*2) | 0);

                        var couleur = (function (strength)
                        {
                            var col = Math.round(strength + 0.1);
                            var r = col + (i * j);
                            a.fillStyle = "rgb(" + r + "," + col + "," +col + ")";

                            a.strokeStyle = '#200';
                        });

                        function point(i, j, k)
                        {
                            var x =  (i / countX - 0.5);
                            var y =  (-k / 255 + 0.5);
                            var z =  (-j / countY + 1.01);

                            x = (x + 0.5 * z) / (1 + z * 1.05);
                            y = (y - 0.9 * z) / (1 + z * 1.05);

                            a.lineTo(W/2 + W * x, H/2 + H * y);
                        }

                        
                        var shadowValue = (N / 4);

                        a.shadowBlur = shadowValue;
                        a.shadowColor = 'rgba(0,0,12,0.5)';

                        
                        var S = 0.8;
                         var T = epaisseur;
                         var base = 0;
                         var heie = height;

                        couleur(height/2);
                        a.beginPath();
                        point(i, j, base+heie);
                        point(i, j+T, base+heie);
                        point(i, j+T, base);
                        point(i, j, base);
                         a.closePath();
                        a.fill();

                        a.shadowBlur = 0;
                        a.stroke();
                        a.shadowBlur = shadowValue;
           

                        couleur(height * 0.9);
                          a.beginPath();
                        point(i  , j+T, base+heie);
                        point(i-S, j+T, base+heie);
                        point(i-S, j+T, base);
                        point(i  , j+T, base);
                         a.closePath();
                          a.fill();


                          a.shadowBlur = 0;
                        a.stroke();
                        a.shadowBlur = shadowValue;

                                         

                        couleur(height*2);
                        a.beginPath();
                        point(i, j, base+heie);
                        point(i-S, j, base+heie);
                        point(i-S, j+T, base+heie);
                        point(i, j+T, base+heie);
                         a.closePath();
                        a.fill();
                        a.shadowBlur = 0;
                        a.stroke();
                       
                    }                

                a.drawImage(c, 0, 0, W, H, -0.75, -1, W+1.5,H+6);

            },50);