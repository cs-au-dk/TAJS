c.width = 600,
c.height = h = 280,
a.font = "5ex y",

R = T = P = Q = W = 0,

// Combined function to handle keypresses, calculate the size of a vector or clamp a number between a minimum and maximum
C = onkeydown = onkeyup = function(i, j, k, l, m) {
    with (s) return (I = i.which) ?
        (e = i.type[5], I ^ 65 ? I ^ 68 ? I ^ 87 || (J = e) : R = e : L = e) : k ? Math.max(j, Math.min(k, i)) : Math.sqrt(i * i + j * j)
},

// Draw stuff on the canvas, including text, circles, semi-circles or rectangles
O = function(i, j, k, l, m) {
    with (a)
        fillStyle = "#"+"fff000f0".substr(i, 3),
        !m && fillText(j, k, l || 28),
        m > 2 ? fillRect(j, k, l, m) : (beginPath(), arc(j, k, l, Math.PI * m, 0, 0), fill())
},

// Draw a slime
S = function(i, j, k, l, m) {
    with (m)
        // Move slime using velocities from the previous frame
        x = C(x + v, k, l),
        y = C(y + w, 0, h),

        // Apply gravity and jumping
        w = y ^ h ? w + .5 : J ? -9 : 0,

        // Set intial slime velocity from keyboard input / AI
        v = L ? -6 : R ? 6 : 0,

        // Check collision with ball
        f = C(d = X - x, e = Y - y),
        f < 52 && (
            g = d / f,
            b = e / f,

            // Push the ball away to eliminate overlap
            X = x + 52 * g,
            Y = y + 52 * b,

            // Update the ball's velocity
            f = 2 * (g * (U - v) + b * (V - w)),

            // Fudge things slightly by doctoring the ball's velocity
            d = C(12 / C(U += v - f * g, V += w - f * b), 0, 1),
            U *= d * 1.2,
            V *= d
        ),

        // Draw body
        O(i, x, y, 40, 1),

        // Draw the eyeball
        O(0, g = x + 20 - j, b = y - 20, 8, 2),

        // Draw the pupil
        f = 4 / C(d = X - g, e = Y - b),
        O(3, g + d * f, b + e * f, 4, 2),

        // Draw the heart. y coordinate is floored to prevent flappy hearts in Opera on Mac.
        O(2, "❤", x + j / 2 - 28, ~~y),

        // AI
        j && (
            // Decide whether to move or jump the computer slime next frame
            // Calculate the x coordinate of the ball when it reaches a point near the slime's eye
            d = Math.sqrt(V * V + y - 40 - Y) - V,
            e = X + U * d * 2,
            f = x - 20 - e,
            g = e > 302,
            L = g & f > 6,
            R = g & f < -6,
            J = !J & g & d < 12 & !+(d+"")[5]
        )
},

setInterval(function(i, j, k, l, m) {
    P ? (
        // Clear the screen
        O(4, 0, 0, 600, h),

        // Update ball position from velocity and apply gravity to it
        Y += V += .5,

        // Bounce off walls
        d = C(X += U, 12, 588),
        X != d && (X = d, U = -U),

        // Check for end of point (ball hitting the ground)
        (P = Y < 268) ||
            ((Q = X > 302) ? ++R : ++T) ^ 6 && (W = 99),

        // For each slime, update its position from its velocity, check for collision with the ball and draw it
        S(1, 0, 40, 258, s),
        S(5, 40, 342, 560, t),

        // Check for a collision between ball and net
        X ^ C(X, 286, 314) || (Y > 240 ? (X = U > 0 ? 286 : 314, U = -U) : Y > 228 && (Y = 228, V = -V)),

        // Draw the net
        O(0, 298, 240, 4, 40),

        // Draw the ball
        O(0, X, Y, 12, 2),

        // Draw scores
        O(0, R, 9),
        O(0, T, 570)
    ):(P=!W--) && (
        // Start a new point
        s = {y: h, v: 0, w: 0, L: 0, R: 0, J: 0, x: Y = 99},
        t = {y: h, v: 0, w: 0, L: 0, R: 0, J: 0, x: 501},
        X = (Q ? t : s).x,
        U = V = 0
    )
},20)