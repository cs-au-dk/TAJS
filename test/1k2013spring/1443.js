/**
	 *   This is a pseudo-3D self-running animation, featuring ...
	 *
	 *   - a busy bee flying left and right, composed only of the simplest of primitives,
	 *   - fresh animated swaying grass with depth cueing,
	 *   - pretty flowers because everybody loves flowers,
	 *   - a camera moving in all directions (left, right, up, down and forward) for your entertainment.
	 *
	 *   Minimize:
	 *   The code has been minimized using closure compiler, then optimized further by hand
	 *   and afterwards crushed with JScrush by Aivo Paas (awesome stuff!).
	 *   The code is written with JScrush in mind: There are many repeating code sequences
	 *   from unrolled loops, which JScrush compresses marvelously.
	 *
	 *   Besides, I went through great lengths to throw out code that was not reeeaaally necessary.
	 *   Well, ... I killed most of the initialization code, so scripts needs to run for a few seconds before
	 *   all the objects are in place.
	 *
	 *   Compatibility:
	 *   It runs in all browsers(*) that have canvas support including IE9, Android (Gingerbread) and iPad. :)
	 *
	 *   (*) that is all the browsers I could lay may hands on.
	 *
	 */

	(function () {

		// variables
		var i,
				iChange,
				iColor,
				fPerspectiveMultplier,
				x2, y2,
				aVectorData,
				aColorFlowers = ['e5f', 'fc3', 'f52', 'fee', '', '', '#211', '#fc3', '#311', '#fd4', '#422'],
				aObjects = [],
				iCounter = 0,
		// constants
				TYPE_GRASS = 0,
				TYPE_FLOWER = 1,
				SCREEN_WIDTH = 1000,
				SCREEN_HEIGHT = 400,
				WINDOW_WIDTH = window.innerWidth,
				PIXEL = 'px',
		// init values
				iNearPlaneZ = 35,
				iFarPlaneZ = 500,
				fColorDivider = iFarPlaneZ / 14,
				sFillStyleSky = '#59f',
				sFillStyleGround = '#0a0',
				iNumObjGrass = 2000,
				iNumObjFlowers = 99
				;

		// create the objects in the world
		for (i = 0; i < iNumObjGrass + iNumObjFlowers; i++) {
			aObjects[i] = [
				0,
				0,
				i & 3,
				~~(i / iNumObjGrass)
			];
		}

		// init canvas, stretch it to fit screen
		a.canvas.width = SCREEN_WIDTH;
		a.canvas.height = SCREEN_HEIGHT - 25;
		c.style.width = WINDOW_WIDTH + PIXEL;
		c.style.height = (WINDOW_WIDTH * 3 / 8) + PIXEL;

		function drawObject(x, y, z, i, type) {
			fPerspectiveMultplier = iNearPlaneZ / z;
			x2 = x * fPerspectiveMultplier + 500;
			y2 = y * fPerspectiveMultplier + 200;
			if (z < iNearPlaneZ || z > iFarPlaneZ
					|| x2 < 0 || x2 > SCREEN_WIDTH
					) {
				return true;
			}

			iChange = (2 + i % 3) / 3;
			iColor = (16 - ~~(z / fColorDivider)).toString(16);

			if (!type) {
				// draw grass blade
				a.fillStyle = '#0' + iColor + '0';
				a.beginPath();
				a.lineTo((x - 50) * fPerspectiveMultplier + 500, (y) * fPerspectiveMultplier + 200);
				a.lineTo((x - 20 + 30 * iChange) * fPerspectiveMultplier + 490, (y - 400 * iChange) * fPerspectiveMultplier + 200);
				a.lineTo((x + 50) * fPerspectiveMultplier + 500, (y) * fPerspectiveMultplier + 200);
				a.fill();
			} else {
				// draw flower
				y2 = -99 - 200 * iChange;
				a.font = ~~((99 + 99 * iChange) * fPerspectiveMultplier) + 'px Arial';
				a.fillStyle = '#' + aColorFlowers[i];
				a.fillText('✿',
						(x) * fPerspectiveMultplier + 500,
						(y + y2) * fPerspectiveMultplier + 200
				);
				a.fillStyle = aColorFlowers[6];
				a.fillText('❀',
						(x) * fPerspectiveMultplier + 500,
						(y + y2) * fPerspectiveMultplier + 200
				);
			}
		}


		setInterval(function () {

			a.fillStyle = sFillStyleSky;
			a.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			a.fillStyle = sFillStyleGround;
			a.fillRect(0, 240, SCREEN_WIDTH, 500);

			aObjects.sort(function (a, b) {
				return b[1] - a[1];
			});
			for (i = 0; i < iNumObjGrass + iNumObjFlowers; i++) {
				if (drawObject(
						aObjects[i][0] + 5000 * Math.sin(iCounter / 600),
						620 + Math.sin(iCounter / SCREEN_HEIGHT) * 80,
						aObjects[i][1] + iCounter,
						aObjects[i][2],
						aObjects[i][3])
						) {
					aObjects[i][0] = 10000 - 20000 * Math.random();
					aObjects[i][1] = iFarPlaneZ - iCounter;
				}
			}

			// wings
			a.beginPath();
			a.fillStyle = '#6af';
			a.lineTo(500, 20 + 20 * Math.sin(iCounter / 99 - Math.PI / 8) + 70 + 5 * Math.sin(iCounter / 99));
			a.lineTo(590, 20 + 20 * Math.sin(iCounter / 99 - Math.PI / 8) + 55 + (20 + (iCounter % 2) * 40));
			a.lineTo(610, 20 + 20 * Math.sin(iCounter / 99 - Math.PI / 8) + 85 + (20 + (iCounter % 2) * 40));
			a.lineTo(500, 20 + 20 * Math.sin(iCounter / 99 - Math.PI / 8) + 70 + 5 * Math.sin(iCounter / 99));
			a.lineTo(410, 20 + 20 * Math.sin(iCounter / 99 - Math.PI / 8) + 55 + (20 + (iCounter % 2) * 40));
			a.lineTo(390, 20 + 20 * Math.sin(iCounter / 99 - Math.PI / 8) + 85 + (20 + (iCounter % 2) * 40));
			a.fill();

			a.beginPath();
			a.fillStyle = aColorFlowers[6];
			a.arc(500 + 4 * Math.sin(iCounter / 600 - 2 * Math.PI / 3), (20 + 20 * Math.sin(iCounter / 99 - Math.PI / 8) + 99) + 7 * Math.sin(iCounter / 99), 39, 0, 2 * Math.PI);
			a.fill();
			a.beginPath();
			a.fillStyle = aColorFlowers[7];
			a.arc(500 + 6 * Math.sin(iCounter / 600 - 2 * Math.PI / 3), (18 + 20 * Math.sin(iCounter / 99 - Math.PI / 8) + 99) + 10 * Math.sin(iCounter / 99), 33, 0, 2 * Math.PI);
			a.fill();
			a.beginPath();
			a.fillStyle = aColorFlowers[8];
			a.arc(500 + 7 * Math.sin(iCounter / 600 - 2 * Math.PI / 3), (17.5 + 20 * Math.sin(iCounter / 99 - Math.PI / 8) + 99) + 12 * Math.sin(iCounter / 99), 28, 0, 2 * Math.PI);
			a.fill();
			a.beginPath();
			a.fillStyle = aColorFlowers[9];
			a.arc(500 + 10 * Math.sin(iCounter / 600 - 2 * Math.PI / 3), (17 + 20 * Math.sin(iCounter / 99 - Math.PI / 8) + 99) + 13 * Math.sin(iCounter / 99), 21, 0, 2 * Math.PI);
			a.fill();
			iCounter -= 11;
		}, 40);
	})();