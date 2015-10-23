/**
 * Composed shims  from http://js1k.com/2013-spring/rules
 *
 * shim rovides a, b, c
 */
var b = document.body;
var c = document.getElementsByTagName('canvas')[0];
var a = c.getContext('2d');
document.body.clientWidth; // fix bug in webkit: http://qfox.nl/weblog/218
