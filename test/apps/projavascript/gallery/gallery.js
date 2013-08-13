// Keep track of which image we're current looking at
var curImage = null;

// Reposition the gallery to be at the center of the page
// even when the page has been scrolled
function adjust(){
		// Locate the gallery
		var obj = id("gallery");
		
		// Make sure that the gallery exists
		if ( !obj ) return;
		
		// Find its current height and width
		var w = getWidth( obj );
		var h = getHeight( obj );
		
		// Position the box, vertically, in the middle of the window
		var t = scrollY() + ( windowHeight() / 2 ) - ( h / 2 );
		
		// But no heigher than the top of the page
		if ( t < 0 ) t = 0;
		
		// Position the box, horizontally, in the middle of the window
		var l = scrollX() + ( windowWidth() / 2 ) - ( w / 2 );
		
		// But no less than the left of the page
		if ( l < 0 ) l = 0;
		
		// Set the adjusted position of the element
		setY( obj, t );
		setX( obj, l );
};

// Readjust the position of the gallery every time
// the user scrolls the page or resizes the browser
window.onresize = document.onscroll = adjust;

// Start a slideshow of all the images within a particular gallery
function startShow(obj) {
		// Locate all the individual images of the gallery
		var elem = tag( "li", obj );
		
		// Locate the overall display gallery
		var gallery = id("gallery");
		
		// Go through each of the matched gallery images
		for ( var i = 0; i < elem.length; i++ )  new function() {
				// Remember which current element is being referenced
				var cur = elem[i];
				
				// We're going to show a new image every 5 seconds
				setTimeout(function(){
						// Show the specific image
						showImage( cur );
						
						// And start fading it out after 3.5 seconds
						// (for a 1 second fade)
						setTimeout(function(){
								fadeOut( gallery, 0, 10 );
						}, 3500 );
				}, i * 5000 );
						
		};
		
		// And then hide the overlay when it's all over
		setTimeout( hideOverlay, 5000 * elem.length );
		
		// But show the overlay, as the slideshow is just starting
		showOverlay();
}

// Hide the grey overlay and the current gallery
function hideOverlay() {
		// Make sure that we reset the current image
		curImage = null;
		
		// and hide the overlay and gallery
		hide( id("overlay") );
		hide( id("gallery") );
}

// Show the grey overlay
function showOverlay() {
		// Find the overlay
		var over = id("overlay");
		
		// Make it as tall and wide as the entire page
		// (this helps with scrolling)
		over.style.height = pageHeight() + "px";
		over.style.width = pageWidth() + "px";
		
		// And fade it in
		fadeIn( over, 50, 10 );
}

// Find the previous image and show it
function prevImage() {
		// Locate the previous gallery image and show it
		showImage( prev( curImage ) );
		
		// Prevent the link from operating as normal
		return false;
}

// Find the next image and show it
function nextImage() {
		// Locate the next gallery image and show it
		showImage( next( curImage ) );
		
		// Prevent the link from operating as normal
		return false;
}

// Show the current gallery image
function showImage(cur) {
		// Remember which image we're currently dealing with
		curImage = cur;
		
		// Find the gallery image
		var img = id("gallery_image");
		
		// Remove the image, if there's one already there
		if ( img.firstChild )
				img.removeChild( img.firstChild );
				
		// And add our new image in, instead
		img.appendChild( cur.firstChild.cloneNode( true ) );

		// We're setting the caption of the gallery image to
		// the 'alt' contents of the regular image
		id("gallery_title").innerHTML = cur.firstChild.firstChild.alt;
		
		// Hide the next link if we're at the end of the slideshow
		if ( !next(cur) )
				hide( id("gallery_next") );
				
		// Otherwise, make sure that it's visible
		else
				show( id("gallery_next") );
		
		// Hide the previous link if we're at the start of the slideshow
		if ( !prev(cur) )
				hide( id("gallery_prev") );
				
		// Otherwise, we need to be sure that it's visible
		else
				show( id("gallery_prev") );
		
		// Locate the main gallery
		var gallery = id("gallery");
		
		// Set the correct class (so that it's the correct size)
		gallery.className = cur.className;
		
		// Then fade it in smoothly
		fadeIn( gallery, 100, 10 );
		
		// Make sure that the gallery is positioned in the right place
		// on the screen
		adjust();
}

// Wait for the document to finish loading before modifying
// or traversing the DOM
window.onload = function() {
		/*
		 * Create the following DOM structure:
		 * <div id="overlay"></div>
		 * <div id="gallery">
		 *     <div id="gallery_image"></div>
		 *     <div id="gallery_prev"><a href="">&laquo; Prev</a></div>
		 *     <div id="gallery_next"><a href="">Next &raquo;</a></div>
		 *     <div id="gallery_title"></div>
		 * </div>
		 */
		 
		// Create the transparent, gray, overlay
		var overlay = document.createElement("div");
		overlay.id = "overlay";

		// Make it so that when the grey background is clicked,
		// the gallery and background are hidden
		overlay.onclick = hideOverlay;
		
		// Add the overlay into the DOM
		document.body.appendChild( overlay );
		
		// Create the overall gallery holder
		var gallery = document.createElement("div");
		gallery.id = "gallery";
		
		// And add in all the organization divs
		gallery.innerHTML = '<div id="gallery_image"></div>' +
				'<div id="gallery_prev"><a href="">&laquo; Prev</a></div>' +
				'<div id="gallery_next"><a href="">Next &raquo;</a></div>' +
				'<div id="gallery_title"></div>';
		
		// Add the gallery into the DOM
		document.body.appendChild( gallery );
		
		// Handle support for which the next and previous links
		// are clicked within the gallery
		id("gallery_next").onclick = nextImage;
		id("gallery_prev").onclick = prevImage;
		
		// Locate all the galleries on the site
		var g = byClass( "gallery", "ul" );
		
		// Go through all of them
		for ( var i = 0; i < g.length; i++ ) {
				// And locate all the links to the slideshow images
				var link = tag( "a", g[i] );
				
				// Go through each of the image links
				for ( var j = 0; j < link.length; j++ ) {
						// Make it such that, when clicked, they display the
						// image gallery instead of going to the imagae
						link[j].onclick = function(){
								// Show the gray background
								showOverlay();
								
								// Show the image, in the gallery
								//showImage( this.parentNode );
								
								// Make sure that the browser doesn't go the
								// image, like it normally would
								return false;
						};
				}
				
				// We're going to create some extra contextual information
				// surrounding the slideshow
				
				// Create the slideshow header, wrapper
				var div = document.createElement("div");
				div.className = "slideshow";
				
				// Show the name of the slideshow, based upon the 
				// title of the gallery
				var span = document.createElement("span");
				span.innerHTML = g[i].title;
				div.appendChild( span );
				
				// Create a link so that we can view all the
				// gallery images as a slideshow
				var a = document.createElement("a");
				a.href = "";
				a.innerHTML = "&raquo; View as a Slideshow";
				
				// Make it so that it starts the slideshow
				// whenever it's clicked
				a.onclick = function(){
						startShow( this.parentNode.nextSibling );
						return false;
				};
				
				// Add the new navigation and header to the page
				div.appendChild( a );
				g[i].parentNode.insertBefore( div, g[i] );
		}
};