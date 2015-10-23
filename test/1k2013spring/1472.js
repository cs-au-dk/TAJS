/**
 *
 * Springy - a simple Doodle Jump clone in less than 1kB of JavaScript! Written for JS1K 2013 (http://js1k.com/2013-spring).
 *
 * To control your spring, simply move the mouse to the left or right. White platforms are normal, gold platforms are extra-springy.
 * The game runs until you fall. See how high you can get!
 *
 * You can now play on your mobile/tablet! Just tilt the device to the left or right to move. Works best on iOS and older Android devices
 * (some newer android devices seem to mix up the axes)
 *
 * Assumes that the following variables are globally available (see springy.html):
 *
 *   - b = document.body
 *   - c = document.getElementsByTagName("canvas")[0]
 *   - a = c.getContext("2d")
 *
 */

var e, f, platformY, springX, springY, jumping, falling, jumpSpeed, fallSpeed, i, score, ma, alive, rotation, createPlatform, init, moveSpring, g,
    loopCounter = alive = rotation = 0, // rotation = mobile device rotation, -1 == left, 0 == static, 1 == right
    platforms = [];

c.width = 320;
c.height = 500;

createPlatform = function (e, f) {
    platforms[e] = {
        x: Math.random() * 250, // 250 == canvas width (320) - platform width (70)
        y: f,
        t: Math.random() * 6 < 1, // type of platform (normal or bouncy, normal more likely)
        d: Math.random() * 3 | 0 - 1 // which direction does it move in (-1 = left, 0 = static, 1 = right)?
    };
};

init = function () {
    for (platformY = i = score = falling = fallSpeed = 0; i < 7;) { // populate random platforms array, 7 == number of platforms
        createPlatform(i++, platformY);
        platformY < 480 && (platformY += 71); // 71 == canvas height / num platforms (7), 480 == canvas height - platform height (20)
    }
    springX = 155;
    springY = 463; // 463 == canvas height (500) - spring height (37)

    jumping = 1,
    jumpSpeed = 17;
};
init();

moveSpring = function (e) {
    e < 0 ? springX > 0 && (springX -= 6) : e > 0 ? springX + 10 < 320 && (springX += 6) : 0;
};

b.onmousemove = function (e) { // move the mouse to the left and right to move the spring as appropriate
    ma = e.pageX;
    moveSpring(ma < springX ? -1 : ma > springX ? 1 : 0);
};

this["ondevicemotion"] = function (e) { // iOS requires context for the property
    A = e["accelerationIncludingGravity"].x;
    rotation = A < 0 ? -1 : A > 0 ? 1 : 0;
    isNaN(this.orientation) && (rotation = -rotation); // Opera Mobile doesn't support orientation and seems to reverse the axis
};

c.onclick = function () { // click on the canvas to start the game
    !alive && (alive = !init());
};

setInterval(function () {
    a.fillStyle = "#79F";
    a.fillRect(0, 0, 320, 500); // clear the canvas
    if (alive) {
        moveSpring(rotation);
        if (jumping) { // if jumping, move up appropriately
            if (springY > 250) { // if spring is in bottom half of screen...
                springY -= jumpSpeed; // move spring up
            } else { // if spring is in top half of screen...
                jumpSpeed > 10 && score++; // increase score

                platforms.forEach(function (e, f) { // check if any platforms are no longer in view
                    if ((e.y += jumpSpeed) > 500) {
                        createPlatform(f, e.y - 500);
                    }
                });
            }
            !--jumpSpeed && (jumping = 0, falling = fallSpeed = 1); // decrease jump speed to simulate the effect of gravity
        }
        falling && (springY < 463 ? springY += fallSpeed++ : score ? alive = 0 : falling = fallSpeed = 0); // 37 == spring height
        !falling && !jumping && (jumping = 1, jumpSpeed = 17); // finished falling, start jumping again
        platforms.forEach(function (e, f) {
            with (e) {
                d *= x < 0 || x > 250 ? -1 : 1; // move the platform horizontally if it's a moving one, 70 == platform width
                x += d * (f / 2) * score / 100 | 0;
                a.fillStyle = t ? "#FD3" : "#FFF";
                a.fillRect(x, y, 70, 20); // 70 == platform width, 20 == platform height
                falling && // check for collisions if the spring is falling
                    springX < x + 70 && // 70 == platform width
                    springX + 10 > x && // 10 == spring width
                    springY + 37 > y &&  // 37 == spring height
                    springY + 37 < y + 20 && // 37 == spring height, 20 = platform height
                    (falling = fallSpeed = 0, !jumping && (jumping = 1, jumpSpeed = 17), t && (jumpSpeed = 50));
            }
        });

        a.beginPath(); // draw the spring
        for (i = 0; i < 5;) {
            a.arc(springX, springY + i++ * (7 - jumpSpeed / 2), 9, 0, Math.PI * 2);
        }
        a.stroke();
    }
    a.fillStyle = "#000";
    a.font = "20px arial";
    a.fillText("Score: " + score, 9, 491);
    !alive && (++loopCounter % 25 < 15 && a.fillText("Click/tap to play", 80, 250), a.font = "13px arial", a.fillText("Use the mouse or tilt device to move", 40, 290));
}, 20);