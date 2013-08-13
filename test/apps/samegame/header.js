var	header_slider = null;
var header_x = 0;
var header_dx = 0;
var header_w = 0;
var header_dw = 0;
var header_moving = false;
var header_def_el = null;

function header_m_light(el) {
	if (el == null) return;
	if (header_def_el == null) {
		header_def_el = el;
	}
	if (header_slider == null) {
		header_slider = document.getElementById('header_slider');
	}
	header_dx = el.offsetLeft - 2;
	header_dw = el.offsetWidth + 4;
	header_slider.style.height = el.offsetHeight + 8 + 'px';
	if (!header_moving) header_move_slider();
}

function header_move_slider() {
	if (header_x != header_dx) {
		header_moving = true;
		var d = (header_dx > header_x) ? 1 : -1;
		var mx = Math.round((header_dx - header_x) / 4);
		if (mx == 0) mx = d;
		header_x += mx;
		header_slider.style.marginLeft = header_x + 'px';
		
		if (header_dw != header_w) {
			var d = (header_dw > header_w) ? 1 : -1;
			var mw = Math.round((header_dw - header_w) / 4);
			if (mw == 0) mw = d;
			header_w += mw;
			header_slider.style.width = header_w + 'px';
		}
		
		setTimeout('header_move_slider()', 30);
	} else {
		if (header_w != header_dw) {
			header_w = header_dw;
			header_slider.style.width = header_w + 'px';
		}
		header_moving = false;
	}
}