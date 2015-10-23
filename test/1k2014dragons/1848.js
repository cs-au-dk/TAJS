(function() {

    // Get rid of all var declarations if possible
    var w, h, boids, count, countZeroBased, movementFactor, 
        proximityFactor, boundary, minDistanceBetween, velocityLimit,
        abs, rand, tendToPositionX, tendToPositionY, frame, dragonImageData, dragonImage,
        sumPositionY, sumVelocityY, proximityVelocityFixY,
        bindVelocityY, distanceY, sumPositionX, sumVelocityX,
        proximityVelocityFixX, bindVelocityX, distanceX, distance,
        index, boid, _boid, i,
        perceivedCenterPositionX, perceivedCenterPositionY, perceivedVelocityX, perceivedVelocityY, 
        ruleX1, ruleY1, ruleX2, ruleY2, ruleX3, ruleY3, tendToVelocityX, tendToVelocityY;

    // Variables
    w = a.width;
    h = a.height;
    boids = [];
    count = 16;
    countZeroBased = count - 1;
    movementFactor = 1E4;
    proximityFactor = 1E3;
    boundary = 64;
    minDistanceBetween = 32;
    velocityLimit = 1;
    abs = Math.abs;
    rand = Math.random;
    tendToPositionX = tendToPositionY = frame = 0;
    dragonImageData = 'data:image/gif;base64,R0lGODlhEAAMAJEDAMzMzJmZmTMzAP///yH5BAEAAAMALAAAAAAQAAwAAAImnI+pJ7sCnWoyUQVgzGBo0Q1B8JVBaACkQK6jco4sKrXVYd/pUgAAOw==';
    dragonImage = new Image();

    // Create dragon image
    dragonImage.src = dragonImageData;

    // Generate the dragons
    for (i = count; i--;) boids.push({
        x: rand() * w,
        y: rand() * h,
        vx: rand(),
        vy: rand(),
        angle: 0
    });

    (function loop() {
        
        // Background
        c.fillStyle = '#fff';
        c.fillRect(0, 0, w, h);
        
        // Each boid
        for(index = count; index--;){
            boid = boids[index];
            // Render boid
            c.save();
            c.translate(boid.x + 8, boid.y + 5);
            c.rotate(boid.angle);
            c.drawImage(dragonImage, -8, -5);
            c.restore();

            // Render fireball
            c.fillStyle = 'hsl(' + boid.fireHue + ',100%,' + boid.fireLight + '%)';
            c.fillRect(boid.fireX, boid.fireY, 4, 4);

            //Update
            sumPositionY = sumVelocityY = proximityVelocityFixY = 
                bindVelocityY = distanceY = sumPositionX = sumVelocityX = 
                proximityVelocityFixX = bindVelocityX = distanceX = 0;

            // Measure other dragons distances
            for (i = count; i--;) {
                if (i ^ index) { // ^ is the same as != for integers
                    _boid = boids[i];
                    sumPositionY += _boid.y;
                    sumVelocityY += _boid.vy;
                    sumPositionX += _boid.x;
                    sumVelocityX += _boid.vx;
                    distanceX = boid.x - _boid.x;
                    distanceY = boid.y - _boid.y;
                    distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                    if (distance < minDistanceBetween) {
                        proximityVelocityFixX += distanceX;
                        proximityVelocityFixY += distanceY;
                    }
                }
            }

            // Bind to container
            bindVelocityX += boundary * (boid.x < boundary ? 1 : (boid.x > w - boundary) ? -1 : 0);
            bindVelocityY += boundary * (boid.y < boundary ? 1 : (boid.y > h - boundary) ? -1 : 0);

            // Calculate Perceived
            perceivedCenterPositionX = sumPositionX / countZeroBased;
            perceivedCenterPositionY = sumPositionY / countZeroBased;
            perceivedVelocityX = sumVelocityX / countZeroBased;
            perceivedVelocityY = sumVelocityY / countZeroBased;

            // Fly towards center of group
            ruleX1 = (perceivedCenterPositionX - boid.x) / movementFactor;
            ruleY1 = (perceivedCenterPositionY - boid.y) / movementFactor;

            // Boids try to keep a small distance away from other objects (including other boids)
            ruleX2 = proximityVelocityFixX / proximityFactor;
            ruleY2 = proximityVelocityFixY / proximityFactor;

            // Match velocity of nearby dragons
            ruleX3 = (perceivedVelocityX - boid.vx) / movementFactor;
            ruleY3 = (perceivedVelocityY - boid.vy) / movementFactor;

            // Fly towards tending position
            tendToVelocityX = (tendToPositionX - boid.x) / movementFactor;
            tendToVelocityY = (tendToPositionY - boid.y) / movementFactor;

            // Update Velocity
            boid.vx += ruleX1 + ruleX2 + ruleX3 + bindVelocityX + tendToVelocityX;
            boid.absoluteVelocityX = abs(boid.vx);
            boid.vy += ruleY1 + ruleY2 + ruleY3 + bindVelocityY + tendToVelocityY;
            boid.absoluteVelocityY = abs(boid.vy);

            // Limit velocity
            if (boid.absoluteVelocityY > velocityLimit) boid.vy *= velocityLimit / boid.absoluteVelocityY;
            if (boid.absoluteVelocityX > velocityLimit) boid.vx *= velocityLimit / boid.absoluteVelocityX;

            // Update angle
            boid.angle = Math.atan2(boid.vy, boid.vx) + Math.PI / 2;

            // Update position
            boid.x += boid.vx;
            boid.y += boid.vy;

            // Update fireball
            if (boid.fireX && frame % 2 == 0) {
                boid.fireX += boid.fireVx;
                boid.fireY += boid.fireVy;
                if (boid.fireHue < 50) boid.fireHue += 1.5;
                if (boid.fireLight < 100) boid.fireLight += 0.5;
            }

            // Shoot fireball
            if (rand() < 0.002) {
                boid.fireX = boid.x;
                boid.fireY = boid.y;
                boid.fireVx = boid.vx * 10;
                boid.fireVy = boid.vy * 10;
                boid.fireHue = 0;
                boid.fireLight = 50;
            }

        } // Each boid

        // Update tend to position
        if (frame % 100 == 0) {
            tendToPositionX = rand() * w;
            tendToPositionY = rand() * h;
        }

        // Increment frame
        frame++;

        requestAnimationFrame(loop);

    })(); //Loop

})();