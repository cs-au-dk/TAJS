// A function for determining how far horizontally the browser is scrolledfunction scrollX() {    // A shortcut, in case we’re using Internet Explorer 6 in Strict Mode    var de = document.documentElement;    // If the pageXOffset of the browser is available, use that    return self.pageXOffset ||        // Otherwise, try to get the scroll left off of the root node        ( de && de.scrollLeft ) ||        // Finally, try to get the scroll left off of the body element        document.body.scrollLeft;}
    
// A function for determining how far vertically the browser is scrolledfunction scrollY() {    // A shortcut, in case we’re using Internet Explorer 6 in Strict Mode    var de = document.documentElement;    // If the pageYOffset of the browser is available, use that    return self.pageYOffset ||        // Otherwise, try to get the scroll top off of the root node        ( de && de.scrollTop ) ||        // Finally, try to get the scroll top off of the body element        document.body.scrollTop;}

// Find the height of the viewportfunction windowHeight() {    // A shortcut, in case we’re using Internet Explorer 6 in Strict Mode    var de = document.documentElement;    // If the innerHeight of the browser is available, use that    return self.innerHeight ||        // Otherwise, try to get the height off of the root node        ( de && de.clientHeight ) ||        // Finally, try to get the height off of the body element        document.body.clientHeight;}// Find the width of the viewportfunction windowWidth() {    // A shortcut, in case we’re using Internet Explorer 6 in Strict Mode    var de = document.documentElement;    // If the innerWidth of the browser is available, use that    return self.innerWidth ||        // Otherwise, try to get the width off of the root node        ( de && de.clientWidth ) ||        // Finally, try to get the width off of the body element        document.body.clientWidth;}

// Get the actual height (using the computed CSS) of an elementfunction getHeight( elem ) {    // Gets the computed CSS value and parses out a usable number    return parseInt( getStyle( elem, "height" ) );}// Get the actual width (using the computed CSS) of an elementfunction getWidth( elem ) {    // Gets the computed CSS value and parses out a usable number    return parseInt( getStyle( elem, "width" ) );}

// A function for setting the horizontal position of an elementfunction setX(elem, pos) {    // Set the ‘left’ CSS property, using pixel units    elem.style.left = pos + "px";}// A function for setting the vertical position of an elementfunction setY(elem, pos) {    // Set the ‘left’ CSS property, using pixel units    elem.style.top = pos + "px";}
// Get a style property (name) of a specific element (elem)function getStyle( elem, name ) {    // If the property exists in style[], then it’s been set recently (and is current)    if (elem.style[name])        return elem.style[name];    // Otherwise, try to use IE’s method    else if (elem.currentStyle)        return elem.currentStyle[name];    // Or the W3C’s method, if it exists    else if (document.defaultView && document.defaultView.getComputedStyle) {        // It uses the traditional ‘text-align’ style of rule writing, instead of textAlign        name = name.replace(/([A-Z])/g,"-$1");        name = name.toLowerCase();        // Get the style object and get the value of the property (if it exists)        var s = document.defaultView.getComputedStyle(elem,"");        return s && s.getPropertyValue(name);    // Otherwise, we’re using some other browser    } else        return null;}

// Set an opacity level for an element// (where level is a number 0-100)function setOpacity( elem, level ) {    // If filters exist, then this is IE, so set the Alpha filter    if ( elem.filters )        elem.filters.alpha.opacity = level;    // Otherwise use the W3C opacity property    else        elem.style.opacity = level / 100;}
// A function for hiding (using display) an elementfunction hide( elem ) {    // Find out what it’s current display state is    var curDisplay = getStyle( elem, "display" );    //  Remember its display state for later    if ( curDisplay != "none" )        elem.$oldDisplay = curDisplay;    // Set the display to none (hiding the element)    elem.style.display = "none";}// A function for showing (using display) an elementfunction show( elem ) {    // Set the display property back to what it use to be, or use    // ‘block’, if no previous display had been saved    elem.style.display = elem.$oldDisplay || "block";}

// Returns the height of the web page// (could change if new content is added to the page)function pageHeight() {    return document.body.scrollHeight;}// Returns the width of the web pagefunction pageWidth() {    return document.body.scrollWidth;}

function fadeIn( elem, to, speed ) {    // Start the opacity at  0    setOpacity( elem, 0 );    // Show the element (but you can see it, since the opacity is 0)    show( elem );    // We’re going to do a 20 ‘frame’ animation that takes    // place over one second    for ( var i = 0; i <= 100; i += 5 ) {        // A closure to make sure that we have the right ‘i’        (function(){
        		var opacity = i;
        		            // Set the timeout to occur at the specified time in the future            setTimeout(function(){                // Set the new opacity of the element                setOpacity( elem, ( opacity / 100 ) * to );            }, ( i + 1 ) * speed );        })();    }}

function fadeOut( elem, to, speed ) {    // Start the opacity at 1    //setOpacity( elem, 1 );    // We’re going to do a 20 ‘frame’ animation that takes    // place over one second    for ( var i = 0; i < 100; i += 5 ) {        // A closure to make sure that we have the right ‘i’        (function(){
        		var opacity = i;
        		            // Set the timeout to occur at the specified time in the future            setTimeout(function(){
                // Set the new opacity of the element                setOpacity( elem, 100 - opacity );
                
                if ( opacity == 95 )
                    hide( elem );            }, ( i + 1 ) * speed );        })();    }}

function id( name ) {
    return document.getElementById( name );
}

function tag( name, root ) {
    return ( root || document ).getElementsByTagName( name );
}

function byClass(name,type) {    var r = [];    // Locate the class name (allows for multiple class names)    var re = new RegExp("(^|\\s)" + name + "(\\s|$)");    // Limit search by type, or look through all elements    var e = document.getElementsByTagName(type || "*");    for ( var j = 0; j < e.length; j++ )        // If the element has the class, add it for return        if ( re.test(e[j].className) ) r.push( e[j] );    // Return the list of matched elements    return r;}

function next( elem ) {    do {        elem = elem.nextSibling;    } while ( elem && elem.nodeType != 1 );    return elem;}

function prev( elem ) {    do {        elem = elem.previousSibling;    } while ( elem && elem.nodeType != 1 );    return elem;}