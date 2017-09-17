!function (
  //! (C) WebReflection
  // single var declaration in the whole function, these are simply shortcuts
  FORTY_FIVE, window, document, random, PI, animationFrame, EventListener, add, remove, click, mousemove, touchstart, type, container, timer
) {"use strict";
  // create a canvas and starts growing while fading out
  function init(x, y, r) {
    var
      canvas = container.appendChild(document.createElement("canvas")),
      context = canvas.getContext("2d"),
      style = canvas.style,
      size = random() * 20 + (r || 4),
      square = size / 10,
      fade = 1,
      factor = .05
    ;
    // canvas is positioned statically where the pointer was
    style.cssText = "position:absolute;top:" + (y + (r ? 0 : random() * size + 20)) + "px;left:" + (x + (r ? 0 : random() * size + 10)) + "px;";
    // draw the little hart ...
    !function love() {
      draw(canvas, context, style, square, fade, square / 2, square / 4);
      // ease out both square size and fade
      square += (size - square) * factor;
      fade += fade * -factor;
      // call the timer for the next size or remove the node from the dom
      fade < factor ? container.removeChild(canvas) : timer(love);
    }();
  }
  // the hart is drawn here via dimensions
  function draw(canvas, context, style, square, fade, halfSquare, quarterSquare) {
    canvas.width = canvas.height = square;
    context.beginPath();
    context.arc(quarterSquare, quarterSquare, quarterSquare, 0, PI * FORTY_FIVE, 1);
    context.arc(halfSquare + quarterSquare, quarterSquare, quarterSquare, 0, PI * FORTY_FIVE, 1);
    context.moveTo(0, quarterSquare);
    context.lineTo(halfSquare, square);
    context.lineTo(square, quarterSquare);
    context.fillStyle = "rgba(180,0,0," + fade + ")"; 
    context.fill();
    // the hart is centered in the pointer
    style.marginTop = style.marginLeft = -halfSquare + "px";
  }
  // used for both mouse move and down ... when down, the size is bigger
  function mouseEvent(e) {
    init(e.pageX, e.pageY, FORTY_FIVE * (e.type == click));
  }
  // used for both touchstart and touchmove ... when touchstart the size is bigger
  function touchEvent(e) {
    type = e.type;
    e = e.touches || [e];
    init(e[0].pageX, e[0].pageY, FORTY_FIVE * (type == click));
  }
  // the best place to append little hart, no z-index necessary
  container = document.documentElement;
  // just the right timer function to boost performances where possible
  timer = window["r" + animationFrame] ||
          window["webkitR" + animationFrame] ||
          window["mozR" + animationFrame] ||
          window["msR" + animationFrame] ||
          window["oR" + animationFrame] ||
          // fallback to setTimeout for older browsers
          setTimeout;
  // when touch is detected, fired before mouse fallbacks ...
  container[add + EventListener](touchstart, function detect(e) {
    // all listeners mouse related are dropped and right touch related are added
    container[remove + EventListener](mousemove, mouseEvent, 1);
    container[remove + EventListener](click, mouseEvent, 1);
    container[remove + EventListener](touchstart, detect, 1);
    container[add + EventListener]("touchmove", touchEvent, 1);
    container[add + EventListener](click, touchEvent, 1);
    // do not miss a single event, fire the current one too
    touchEvent(e);
  }, 1);
  // mouse events for all desktop browsers and older devices
  container[add + EventListener](mousemove, mouseEvent, 1);
  container[add + EventListener](click, mouseEvent, 1);
}(45, this, document, Math.random, Math.PI, "equestAnimationFrame", "EventListener", "add", "remove", "click", "mousemove", "touchstart");