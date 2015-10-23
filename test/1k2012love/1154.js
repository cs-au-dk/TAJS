// Renders gallery of generated Neo-Platicism artworks
            // By ewoudj for the js1k.com demo competition 

		    // NB: omit the following line of code when using the Closure Compiler service and
            // manually add the variables to the output
		    var d = "style", e = "fillRect";

            // Shortcut for math
            var m = Math;
		    // Color table
            var k = ["#000", "#FFF", "#F11", "#11D", "#FE0", "#F11", "#11D", "#FE0", "#FFF"];
		    // Width, height (as the canvas is always square the w var is used for both width and height
		    var w = c.width = c.height = 400;
            // Browser prefixes for style
		    var prefixes = ["O", "ms", "webkit", "Moz"];
		    // Canvas rotation in degrees
		    var deg = 0;
		    // Left is used to scroll the canvas from right to left
		    // by setting it beyond the leftmost limit we trigger rendering
		    // of the initial canvas
		    var left = -(w + 1);
		    // Indexer for looping, length for looping, random record (2), random row(2), arrays holding 
            // random columns and rows, clientWidth
		    var i, l, rc, rr, rc2, rr2, columns, rows, clientWidth;
		    
            // Hide any scroll bars
		    b[d].overflow = 'hidden';
            // Light gray background ('museum wall' color)
		    b[d].background = "#EEE";
		    // Drop shadow under canvas
		    // NB: Closure Compiler Service will incorrectly compress this to: c[d].a="0px 5px 10px #888";
		    c[d].boxShadow = "0px 5px 10px #888";
            
            // Function returns random number between (and including) 0 and provided max value
            function rnd(min, max) {
                return m.floor(m.random() * ((max - min) + 1)) + min;
            }

            // Function return random item from input array
            function rndFromArray(arr) {
                return arr[rnd(0, arr.length - 1)];
            }

            // Function generates the black lines, input cols true to generate columns, else generates rows,
            // return an array of numbers representing the start positions for the generated lines.
            function generateLines(cols) {
                var result = [-5];                
                for (i = 0, l = rnd(1, 10); i < l; i++) {
                    result.push(rnd(0, w));
                    a.fillStyle = k[0];
                    if (cols) {
                        // FillRect draws a vertical line
                        a[e](result[i + 1], 0, 5, w );
                    }
                    else {
                        // FillRect draws a horizontal line
                        a[e](0, result[i + 1], w , 5);
                    }
                }
                result.push(w);
                return result;
            }

            // Starts the animation
            setInterval(function () {
                // The canvas is moved from left to right by decreasing the left margin, 
                // the top margin is set to make the canvase center vertically
                c[d].margin = m.ceil((innerHeight - w) / 2) + 'px 0px 0px ' + left + 'px';
                // Shortcut for clientwidth
		        clientWidth = b.clientWidth;
		        if (left < -w) {
                    // Reset the left so the canvas is completly to the right of the screen
		            left = clientWidth;
		            // Canvas rotation
		            if (!rnd(0, 3)) {
                        // This deg value determines the rotation of the canvas element
		                deg = 45;
		                // Rotates the 2D drawing context 45 degrees the opposite of the canvas element
		                a.save();
		                a.translate(330, -400);
		                a.rotate(m.PI / 4);
		                w = 800;
		            }
		            else {
		                // This deg value determines the rotation of the canvas element
		                deg = 0;
		            }
                    // Make sure the canvas is rotated in the correct angle
		            for (i = 0; i < 4; i++) {
                        // Just bluntly try all browser prefixes
		                c[d][prefixes[i] + "Transform"] = "rotate(" + deg + "deg)";
		            };
		            // Actual rendering happens here
		            // Clear the canvas (white)
		            a.fillStyle = k[1];
		            // Fillrect
		            a[e](0, 0, w, w);
		            // Initialize rows and columns, including '-5' for the left or top edge and w (width / height)
		            columns = generateLines(1);
		            rows = generateLines();
                    // Fills random rects in the columns / rows grid, using colors from the color table
		            for (i = 0, l = rnd(1, 10); i < l; i++) {
		                a.fillStyle = k[rnd(0, 8)];
		                // Random columns
		                rc = rndFromArray(columns);
		                rc2 = rndFromArray(columns);
		                // Random rows
		                rr = rndFromArray(rows);
		                rr2 = rndFromArray(rows);
		                // Fillrect
		                a[e](
                            m.min(rc, rc2) + 5,
                            m.min(rr, rr2) + 5,
                            m.abs(rc - rc2) - 5,
                            m.abs(rr - rr2) - 5
                        );
		            }
                    // Make sure our working width / height is still 400 px
		            w = 400;
                    // Restore the 2D context (to the 'unrotated' version)
		            a.restore();
		        }
		        else {
                    // Calculate new left for the canvas animation
		            left -= m.ceil((m.abs(((clientWidth - w) / 2) - left) / 30) + 1);
		        }
		    }, 16);