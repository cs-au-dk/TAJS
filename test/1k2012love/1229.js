var g_body = b;
		var g_canvas = c;
		var g_context = a;

        var g_ballArray = new Array();
        var g_2PI = Math.PI * 2;

        init();

        function init() {
            g_canvas.width = window.innerWidth;
            g_canvas.height = window.innerHeight;

            for (var i = 0; i < 100; i++) {
                var x = g_canvas.width / 2;
                var y = g_canvas.height / 2;
                var xInc = -10 + Math.random() * 20;
                var yInc = -10 + Math.random() * 20;
                var width = g_canvas.height / 10;
                width += Math.random() * width;
                var color = getColor(Math.round(Math.random() * 255), Math.round(Math.random() * 255), Math.round(Math.random() * 255), 0.01);

                g_ballArray.push(new Ball(x, y, xInc, yInc, width, color));
            }

            clearScreen("#000");
            setInterval(animate, 30);
        }

        function animate() {
            for (var i in g_ballArray) {
                var ball = g_ballArray[i];
                ball.manage();
                ball.draw();
            }
        }

        function clearScreen(color) {
            g_context.fillStyle = color;
            g_context.fillRect(0, 0, g_canvas.width, g_canvas.height);
        }

        /**
        * @constructor
        */
        function Ball(x, y, xInc, yInc, width, color) {
            this.x = x;
            this.y = y;

            this.xInc = xInc;
            this.yInc = yInc;

            this.width = width;
            this.color = color;
        }

        Ball.prototype.manage = function () {
            if ((this.x < 0) || (this.x > (g_canvas.width - 1)))
                this.xInc *= -1;

            if ((this.y < 0) || (this.y > (g_canvas.height - 1)))
                this.yInc *= -1;

            this.x += this.xInc;
            this.y += this.yInc;
        }

        Ball.prototype.draw = function () {
            g_context.fillStyle = this.color;
            g_context.beginPath();
            g_context.arc(this.x, this.y, this.width, 0, g_2PI, true);
            g_context.fill();
        }

        function getColor(r, g, b, a) {
            return "rgba(" + r + ", " + g + ", " + b + ", " + a + ")";
        }