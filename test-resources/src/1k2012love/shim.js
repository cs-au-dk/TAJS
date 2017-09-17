/**
 * Composed shims  from http://js1k.com/2012-love/rules
 *
 * shim rovides a, b, c
 */
(function () {
    var canvas = document.createElement('canvas');
    var body = document.body;
    body.appendChild(canvas);
    window.a = canvas.getContext('2d');
    window.b = body;
    window.c = canvas;
})();