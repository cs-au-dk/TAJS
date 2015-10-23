lives = 100;
width = c.width *= 3;
height = c.height *= 4;
squares = [];
move = rotate = shoot = scores = speed = angle = rot = ang = 0;
X = width / 2;
Y = height / 2;
setInterval(shootLoop, 200);
game = setInterval(gameLoop, 16);
//Keyboard hook
function shootLoop() {
    shoot && squares.push({
        x: X,
        y: Y,
        xspeed: 7 * Math.sin(angle),
        yspeed: -7 * Math.cos(angle),
        length: 2
    });
}
onkeydown = onkeyup = function (e) {
    E = e.which - 37;
    keyup = e.type == "keyup";
    if (E == 1) move = keyup ? 0 : 1;
    if (!E) rotate = keyup ? 0 : -1;
    if (E == 2) rotate = keyup ? 0 : 1;
    if (E == 28) shoot = keyup ? 0 : 1;
}
//Game loop
function gameLoop() {
    //Spawn square
    N = Math.sqrt(scores + 1) / 14;
    if (Math.random() < 16 / (100 + 46 / N)) {
        e = Math.random() * 4 | 0;
        t = [1, Math.random(), 0, Math.random()];
        D = [-3, Math.random() < .5 ? 3 : -3, 3];
        D[3] = D[1];
        squares.push({
            x: width * t[e],
            y: height * t[3 - e],
            xspeed: D[e] * Math.random() + N,
            yspeed: D[3 - e] * Math.random() + N,
            length: Math.random() * 100,
            rot: Math.random() / 14,
            ang: 1
        });
    }
    //Move triangle 94b
    angle += rotate / 14;
    speed += move && speed < 5 ? .03 : speed > .03 ? -.03 : 0;
    Y -= Y < 0 ? ~height : Y > height ? height : speed * Math.cos(angle);
    X += X < 0 ? width : X > width ? ~width : speed * Math.sin(angle);
    //Move squares
    for (i in squares) {
        with(squares[i]) {
            x += xspeed;
            y += yspeed;
            ang += rot;
            if (!ang)
            //Check collision bullets to squares
            for (j in squares) {
                Z = squares[j];
                //if collision
                if (Z.ang && Math.abs(Z.y - y) + Math.abs(Z.x - x) < Z.length) {
                    scores += 100 / Z.length | 0;
                    if (Z.length < 41) Z.x = ~100;
                    else Z.length /= 2;
                    x = ~100;
                }
            } else lives -= Math.abs(y - Y) + Math.abs(x - X) < length ? Math.sqrt(length / 10) | 0 : 0;
            if (x > width || x < 0 || y > height || y < 0) squares.splice(i, 1)
        }
    }
    //Draw section
    with(a) {
        clearRect(0, 0, width, height);
        strokeRect(0, 0, width, height);
        //Draw triangle
        save();
        translate(X, Y);
        rotate(angle - 1.57);
        fillText("\u27a4", -10, 11);
        restore();
        for (i in squares)
        with(squares[i]) save(), translate(x, y), rotate(ang), fillRect(length / -2, length / -2, length, length), restore();
        //Text section, 
        font = "2em Arial";
        fillText("%" + lives + "|$" + scores, 10, 30);
        //Check lives
        if (lives < 1) clearInterval(game)
    }
}