/**
 * Composed shims  from http://js1k.com/2014-dragons/rules
 *
 * Shim1 provides a, b, c, d
 * Shim2 provides a, b, g
 *
 * The shims are composed to provide (a, b, c, d, g) under the assumption that it does not change the programs that depend on them
 */
var a = document.getElementsByTagName('canvas')[0];
var b = document.body;
var d = function (e) {
    return function () {
        e.parentNode.removeChild(e);
    };
}(a);

// unprefix some popular vendor prefixed things (but stick to their original name)
var AudioContext =
    window.AudioContext ||
    window.webkitAudioContext;
var requestAnimationFrame =
    window.requestAnimationFrame ||
    window.mozRequestAnimationFrame ||
    window.webkitRequestAnimationFrame ||
    window.msRequestAnimationFrame ||
    function (f) {
        setTimeout(f, 1000 / 30);
    };

// fix bug in safari: http://qfox.nl/weblog/218
document.body.clientWidth;

// stretch canvas to screen size (once, wont onresize!)
a.style.width = (a.width = innerWidth) + 'px';
a.style.height = (a.height = innerHeight) + 'px';

var c = a.getContext('2d');

var g = (function () {
    try {
        var o = { antialias: true, stencil: true };
        var gl = a.getContext('webgl', o) || a.getContext('experimental-webgl', o);

//        Not relevant + Array.map is unsupported at the moment
//        // keep in scope, must not be garbage collected
//        __glExts =
//            [ 'OES_texture_float', 'OES_texture_float_linear', 'OES_standard_derivatives',
//                'EXT_texture_filter_anisotropic', 'MOZ_EXT_texture_filter_anisotropic', 'WEBKIT_EXT_texture_filter_anisotropic',
//                'WEBGL_compressed_texture_s3tc', 'MOZ_WEBGL_compressed_texture_s3tc', 'WEBKIT_WEBGL_compressed_texture_s3tc'
//            ].map(function (ext) {
//                    return gl.getExtension(ext);
//                });

    } catch (e) {
        document.body.innerHTML = 'WebGL not supported.';
        throw e;
    }

    return gl;
})();

