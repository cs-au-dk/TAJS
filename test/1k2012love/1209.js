/**
 * Canvas piano
 * 
 * To play on canvas piano just click on notes you see on canvas.
 * Also you can check how it works by pressing Für Elise button. It will play Ludwig van Beethoven's famoust composition.
 *
 * ♥♥♥
 *
 * @author Sergey Shchur (antyrat)
 */
(function( ctx, documentBody, canvasElement ){

	canvasElement.onclick = playNote; // add event listener to canvas element

	var noteWidth = 70, // white note width
	    middleCFrequency = 256, // frequency of middle C in scientific pitch
		fromCharCode = String.fromCharCode, // short function name to save bytes
		setTimeoutFunc = setTimeout, // short function name to save bytes
		notesWhite = [], // white notes freqency array
		notesMiddle=[], // seminotes freqency array
		player = [], // Audio array. Each note has own HTML5 Audio element
		freqFormula, // Frequency formula: Math.round(Math.round(110 * Math.pow(1.05946, noteNo)))
		i, // Incremental variable
		noteToPlay, // Current played note 
		header, // WAV header
		note, // note frequency
		noteWPoint, // Pointer for selected white note
		noteBPoint, // Pointer for selected seminote
		xPos, // X coordiane of click event
		eliseTmp, // tmp pointer for Für Elise notes
		elisePointer, // Für Elise current note pointer
		playButton, // html <button> element
		eliseTimeout; // Timeout handler for "Für Elise" song
	
	// genterate frequencys array's
	for( i = 3; i < 39; i++ ) {

		freqFormula = 0|( 0|( 110 * Math.pow( 1.05946, i ) ) ); // Math.round(Math.round(110 * Math.pow(1.05946, i)))
	
		if ( i%12 in {1: 0, 4: 0, 6: 0, 9: 0, 11: 0} ) {
			if( i in {4: 0, 9: 0, 16: 0, 21: 0, 28: 0, 33: 0} ) { 
				notesMiddle.push( 0 );
			}
			notesMiddle.push( freqFormula );
		} else { 
			notesWhite.push( freqFormula );
		}
	}
	
	// create and append to document Play Für Elise button
	playButton = document.createElement( "button" );
	playButton.innerHTML = "► Für Elise";
	playButton.style.display = "block"; // draw button on new line
	playButton.onclick = function () {
		// Stop Für Elise playing
		clearTimeout( eliseTimeout );	
		elisePointer = 0; // set Für Elise note pointer to first note
		playNote(); // play Für Elise sonata
	};
	documentBody.appendChild( playButton );
	
	// Render notes on canvas. Re-render presses notes
	function render ( keyNote, keyBlackNote ) {
		// set width and height of piano canvas (also clear canvas after re-render)
		canvasElement.width = 1470;
		canvasElement.height = 400;	

		// draw piano notes
		for( i = 0; i < 21; i++ ) {
			// white notes
			ctx.strokeRect( noteWidth * i, 0, noteWidth, 400 - ( keyNote == i ? 20 : 0 ) );
			// middle notes
			notesMiddle[ i ] && ctx.fillRect( noteWidth * i - 20, 0, 40, middleCFrequency - ( keyBlackNote == i ? 20 : 0 ) );
		}
	}
	
	// render piano keyboard at start  
	render();

	function playNote ( e ){
		
		noteWPoint = noteBPoint = -1; // clear pressed notes states
		
		if( !e ) { // if we didn'r recieve event than play Für Elise sonata

			eliseTmp = "*;*;*.)(-!#-.#>.(#*;*;*.)(-(*-.*(.-.()*,+*)$*)(#)(.#**1;*;*.(-".charCodeAt( elisePointer ); // fur elise notes in string presentation
			noteToPlay = eliseTmp > 50 ? notesMiddle[ noteBPoint = eliseTmp - 50 ] : notesWhite[ noteWPoint = eliseTmp - 33 ]; // if note pointer grather than 50 - we use middle note frequency

		} else { // function was called after click

			// Stop Für Elise playing
			clearTimeout( eliseTimeout );
			
			// get X coordiante
			xpos = e.offsetX;
			
			// we clicked on middle note
			if( e.offsetY < middleCFrequency ) {
				for( i = 21; i-- ; ) { 
					// check if middle note has frequency value
					if( xpos > ( i * noteWidth - 20 ) && xpos < ( i * noteWidth + 20 ) && notesMiddle[ i ] ) { 
						noteToPlay = notesMiddle[ noteBPoint = i ]; // set note to play and pressed note index
					}
				}
			} else { // white note
				noteToPlay = notesWhite[ noteWPoint = 0|(xpos / noteWidth) ];
			}
		}
		
		// change current note state to pressed
		render( noteWPoint, noteBPoint );
		
		// note didn't has own Audio element
		if( !player[ noteToPlay ] ) { 
		
			// create base header for note width 200 ms. length
			header = "RIFF" + fromCharCode( 12, 69, 0, 0 ) + "WAVEfmt " + fromCharCode( 16, 0, 0, 0, 1, 0, 1, 0, 68, 172, 0, 0, 136, 88, 1, 0, 2, 0, 16, 0 ) + "data" + fromCharCode( 232, 68, 0, 0 );
			
			// update header according to note freqnecy
			for( i = 8820; i-- ; ){
				note = Math.sin( 6.28 * i / ( 44100 / noteToPlay ) ) * 32767;
				note += note < 0 ? 65536 : 0;
				header += fromCharCode( note % middleCFrequency, 0|( note / middleCFrequency ) );
			}
			// create new Audio elemnt with note data in base64 format
			player[ noteToPlay ] = new Audio( 'data:audio/wav;base64,' + btoa( header ) );
		}
		
		// play note with specified frequency
		player[ noteToPlay ].play();
		
		// remove pressed state. (re-render piano keyboard)
		setTimeoutFunc( render, 300 );
		
		// Play Für Elise sonata if there is no click event
		if( !e && elisePointer++ != 62 ) eliseTimeout = setTimeoutFunc( playNote, 300 );
	}
})( a, b, c );