// To compress:
// http://closure-compiler.appspot.com/home
// http://marijnhaverbeke.nl/uglifyjs (allow non-ascii)
// http://www.iteral.com/jscrush/

(function main(round) {

    var gameSpeed = 80 - (round || 1) * -10;
    var gameWidth = 50 - (round || 1) * 7;
    var gameRounds = 5;

    var playerPos = 1;

    var cpuPos = 1;
    var cpuAiKeepShooting = 0;
    var cpuAiHunting = 0;

    var end;

    var charHit = '✴';
    var killedChar = '†';
    var texts = ';ARROW KEYS;YOU DIED;YOU WON!!;NEXT ROUND;# ROUND '.split(';');
    var braille = '⠀⠉⠒⠛⠤⠭⠶/⣀⣉⣒/⣤'.split('');

    var playerBullets = new Array(gameWidth);
    var cpuBullets = new Array(gameWidth);


    onkeydown = function(event) {
        if (end || !round) {
            main(1);
        }

        var w = event.which;
        if (w == 38) {
            playerPos = Math.max(1, playerPos - 1);
        } else if (w == 40) {
            playerPos = Math.min(4, playerPos + 1);
        } else if (w == 32 || w == 39) {
            playerBullets[0] = playerPos;
        }
    };


    (function loop() {
        var hashStr, message = 0,

            // CPU MOVE VARS
            random = Math.random(),
            variance = Math.random(),
            spamFactor = (playerBullets.join('').replace(/0/g, '').length || 0.05) / gameWidth;

        if (round) {

            // Detect spam in current lane
            if ((playerBullets.join('') + cpuPos).match(new RegExp(cpuPos, 'g')).length > gameWidth / 4) {
                cpuAiKeepShooting = gameWidth / 4;
            }

            // Bullets are close-by in same lane, defensive shooting
            else if (playerBullets.slice(gameWidth * 0.9).indexOf(cpuPos) > 0) {
                cpuAiKeepShooting = gameWidth / 10;
            }

            // Shoot in current lane
            if (random < cpuAiKeepShooting + spamFactor) {
                cpuAiKeepShooting -= 1;
                if (cpuAiKeepShooting && variance > 0.2) { // Hickup
                    cpuBullets[gameWidth] = cpuPos; // Shoot
                }
            }

            // Move to player lane and shoot a couple of times while going there
            else if (random < cpuAiHunting + spamFactor + 0.02) {
                cpuAiHunting = 1;
                if (cpuPos == playerPos) {
                    // Reached player
                    cpuAiHunting = 0;
                } else {
                    // Move towards player, shoot while moving
                    cpuPos += cpuPos > playerPos ? -1 : 1;
                }
                cpuAiKeepShooting = variance * gameWidth / 10;
            }

            // Move around randomly
            else if (random < spamFactor + 0.12) {
                // min,max: Keep in bounds
                cpuPos = Math.min(4, Math.max(1, cpuPos + random > 0.5 ? -1 : 1));
                cpuAiKeepShooting = variance * 3 * round;
            }

        } else {
            message = 1; // No round started: Arrow keys instruction
        }

        // Move bullets and truncate excess
        playerBullets.unshift(0);
        playerBullets.length = gameWidth;
        cpuBullets.shift();

        // Player and CPU braille char
        var player = braille[Math.pow(2, playerPos - 1)],
            cpu = braille[Math.pow(2, cpuPos - 1)];

        // Player segment
        if (cpuBullets[0] == playerPos) {
            player = killedChar;
            message = 2; // Killed
            setTimeout(function() {
                end = 1;
            }, 2000);
        }

        // CPU segment and round/game winning
        if (playerBullets[gameWidth - 1] == playerPos) {
            cpu = killedChar;
            if (round == gameRounds) {
                message = 3; // Won
                setTimeout(function() {
                    end = 1;
                }, 2000);
            } else {
                message = 4; // Round
                round += 1;
                setTimeout(function() {
                    main(round);
                }, 2000);
            }
        }

        // Put hash segments together
        hashStr = texts[5] + round + '/' + gameRounds + ' [' + player + ':';

        // Append bullets
        for (var i = 0; ++i < gameWidth;) {
            var playerBullet = playerBullets[i] ? Math.pow(2, playerBullets[i] - 1) : 0;

            // Collision even
            if (playerBullet && playerBullets[i] == cpuBullets[i]) {
                playerBullets[i] = cpuBullets[i] = 0;
                hashStr += charHit;
            }

            // Collision odd
            else if (playerBullet && playerBullets[i] == cpuBullets[i - 1]) {
                playerBullets[i] = cpuBullets[i - 1] = 0;
                hashStr += charHit;
            }

            // No collission, draw combined bullets
            else {
                hashStr += braille[playerBullet + (cpuBullets[i] ? Math.pow(2, cpuBullets[i] - 1) : 0)];
            }
        }

        // Append CPU segment
        // Prevent %20 showing in Safari
        hashStr = (hashStr + ':' + cpu + '] ' + texts[message]).replace(/ /g, braille[0]);

        // Use replaceState in Webkit
        // Oldschool location.replace for Opera (security error) or MSIE9 (shitty)
        if (navigator.userAgent.match(/Kit/)) {
            top.history.replaceState(0, 0, hashStr);
        } else {
            top.location.replace(hashStr);
        }

        // Pause if we show a message
        if (!message) {
            setTimeout(loop, gameSpeed);
        }
    })();

})(0);