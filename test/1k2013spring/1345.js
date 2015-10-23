/*
 * By: Zolmeister
 * http://zolmeister.com
 */

mouseX = mouseY = 0
flowers = []

// Z
letter = [[.3579, .1628], [.6421, .1599], [.3512, .7326], [.6488, .7297]]//Z

onmousemove = function(e) {
    mouseX = e.pageX
    mouseY = e.pageY
}

cHi = c.height = innerHeight - 21
setInterval(function() {
    if (mouseX) {
        cWid = c.width = innerWidth - 21

        //with with is with
        with (a) {
            with (Math) {
                rand = random

                //Align text so that it rotates cleanly
                textBaseline = 'middle'
                textAlign = 'center'
                shadowColor = '#222'

                if (rand() > .6 & flowers.length < 2) {

                    //spawn flower
                    flow = rand() * 27 + 10025
                    dir = rand() * PI * 2
                    flower = {
                        x : mouseX,
                        y : mouseY,
                        dir : dir,
                        xSpeed : cos(dir),
                        ySpeed : sin(dir),
                        flower : String.fromCharCode(flow), //unicode flowers
                        rot : 0,
                        poptime : 20,
                        size : 90,
                        color : '#' + ['FA7A79', 'A266AC', 'E38F3D', '81A63F', '619CD8'][~~(rand() * 5)]
                    }
                    flowers.push(flower)
                }
                for (i in flowers) {
                    with (flowers[i]) {
                        dir %= 2 * PI
                        if (x < 70 | x > cWid - 70) {
                            xSpeed *= -1
                            if (x < 70)
                                x = 70
                            if (x > cWid - 70)
                                x = cWid - 70
                        }
                        if (y < 70 | y > cHi - 70) {
                            ySpeed *= -1
                            if (y < 70)
                                y = 70
                            if (y > cHi - 70)
                                y = cHi - 70
                        }
                        x += xSpeed * 16
                        y += ySpeed * 16

                        last = letter[0]

                        for ( j = 1; j < 4; j++) {
                            x1 = cWid * last[0]
                            y1 = cHi * last[1]
                            x2 = cWid * letter[j][0]
                            y2 = cHi * letter[j][1]
                            m = (y1 - y2) / (x1 - x2)
                            yy = m * (x - x1) + y1
                            if (x < max(x1, x2) & x > min(x1, x2)) {
                                if (abs(y - yy) < 10) {
                                    x -= xSpeed * 15.9
                                    y -= ySpeed * 15.9
                                    j=4
                                }
                            }
                            last = letter[j]
                        }
                        rot += PI / 9
                        poptime -= 1
                        if (flowers.length < 150 & poptime < 0 && size && abs(x - mouseX) < size / 2 & abs(y - mouseY) < size / 2) {
                            size *= .9
                            poptime = 30
                            dir = rand() * PI * 2
                            xSpeed = cos(dir)
                            ySpeed = sin(dir)
                            for ( i = 0; i < 10; i++) {
                                dd = rand() * PI * 2
                                xx = mouseX + cos(dd) * 45
                                yy = mouseY + sin(dd) * 45

                                flowers.push({
                                    x : xx,
                                    y : yy,
                                    xSpeed : cos(dd),
                                    ySpeed : sin(dd),
                                    dir : dd,
                                    flower : String.fromCharCode(rand() * 27 + 10025), //unicode flowers
                                    rot : 0,
                                    poptime : 20,
                                    size : size,
                                    color : '#' + ['FA7A79', 'A266AC', 'E38F3D', '81A63F', '619CD8'][~~(rand() * 5)]
                                })
                            }

                        }

                        //draw rotated flower
                        font = size + 'px sans'
                        shadowBlur = 3;
                        fillStyle = color
                        save()
                        translate(x, y)
                        rotate(rot)
                        fillText(flower, 0, 0)
                        restore()
                    }
                }
            }
        }
    }
}, 33)