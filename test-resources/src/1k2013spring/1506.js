// =========================================================
// 1024 byte expression based webaudio sequencer and sampler
// by: Per-Olov Jernberg
// @possan http://twitter.com/possan
// =========================================================
//
// Your song is stored in the location hash, so you can give the link to
// someone else and they should be able to hear the same song.
//
//
// Instructions:
//
//   The UI consists of a number of rows, one per track, each row contains
//   two columns, the first one contains the 'gate' expression / when to
//   trigger sounds, the second one contains the expression for the actual
//   sample beeing triggered.
//
//
// Expressions:
//
//   Gate expressions contains of 128 steps
//   Sample expressions contains of 44100 samples, one second long
//
//   You have two convenience functions available in your expressions:
//
//   C(time, curvature)
//      Exponential falloff function, great for drum sounds
//
//   N(time):
//      Deterministic noise, for chiptune noises
//
//   m:
//      so m.sin will call Math.sin etc.
//
//
// Technical:
//
//   Code, packer and build scripts is available on github:
//   https://github.com/possan/jsintros/tree/master/a
//
//   The packer is a custom jscrush-ish packer.
//
//   There is no pause function, sorry for that, but there simply was
//   no space left :)
//


m = Math;
var ev = eval;
var	step = 0;
var	SAMPLELENGTH = 44100;
var	wl = window.location;

var	context = new webkitAudioContext();
var comp = context.createDynamicsCompressor();
comp.connect(context.destination);

// var	DIV='div';
var EMPTY='';
var BPM_AND_STEPS = 128;
var	INPUT='input';

function createElement(parent, tagname, fn) {
	var el = document.createElement(tagname || 'div');
	parent.appendChild(el);
	fn && el.addEventListener('change', fn);
	return el;
}

var noisetable = [];
var i = SAMPLELENGTH;
while (i--)
	 noisetable.push(m.random());

var tracksel = createElement(b);

var config = wl.hash ? ev(wl.hash.substr(1)) : default_hash;

setInterval(function() {
	config.map(function(x) { x.f(); });
	step++;step%=BPM_AND_STEPS;
}, BPM_AND_STEPS);

config.map(function(o) {
	new Track(o);
});

for(var i=config.length; i<9; i++) {
	var o = {g: 0,  w:0 };
	new Track(o);
	config.push(o);
}

function Track(data) {
	data.t = this;
	var node = createElement(tracksel);
	var __buffer = context.createBuffer(1, SAMPLELENGTH, SAMPLELENGTH);
	var gs=[];

	var renderwave = function() {

		C = function(t, power) {
			if (t<0) return 1;
			t/=SAMPLELENGTH;
			if (t>1) return 0;
			return m.pow(1-t, power);
		}

		N = function(t) {
			return noisetable[~~t % SAMPLELENGTH];
		}

		var buf = __buffer.getChannelData(0);
		s = SAMPLELENGTH;
		while(--s) {
			buf[s] = ev(data.w);
			gs[s] = ev(data.g);
		}

		wl.hash = JSON.stringify(config);
	}

	var _b = createElement(node, INPUT, function() {
		data.g = _b.value;
		renderwave();
	});
	_b.size = 40;
	_b.value = data.g;

	var _c = createElement(node, INPUT, function() {
		data.w = _c.value;
		renderwave();
	});
	_c.size = 99;
	_c.value = data.w;

	renderwave();

	data.f = function() {
		if (gs[step]) {
			with(context.createBufferSource()) {
				buffer = __buffer;
				connect(comp);
				start(0);
			}
		}
	}
}