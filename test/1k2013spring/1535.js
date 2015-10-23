var width = 400, height = 300;
c.width = width;
c.height = height;
var drop_radius = 0.2;
var drops = [
	[0, 0, -0.2],
	[0,0,0],
	[0,0,0],
	[0,0,0],
	[0,0,0],
	[0,0,0],
	[0,0,0]
];
var colors = [
	[1.0, 1.0, 1.0],
	[1.0, 0, 0],
	[0.0, 1.0, 0],
	[0, 0, 1.0],
	[1.0, 1.0, 0],
	[1.0, 0, 1.0],
	[0, 1.0, 1.0]
];
var eye = [0, 0, 2];
var planeZ = 1;
var plane_width = 1;
var plane_height = height/width;
var max_depth = 5;
var light = [0.5, -0.5, 3];
var image = a.createImageData(width, height);
var time = 0.0;

window.setInterval(update, 1000/25);

function update() {
	time += 25/1000;
	for(var i = 1; i < 5; i++) {
		drops[i][0] = 0.5 * Math.sin(2*time + 6.28/4 * i);
		drops[i][1] = -0.5 * Math.cos(2*time + 6.28/4 * i);
		drops[i][2] = 0.3 * Math.sin(3 * time * i);
	}
	drops[5][1] = Math.sin(2*time);
	drops[5][2] = Math.cos(2*time);
	drops[6][0] = Math.cos(2*time);
	drops[6][2] = Math.sin(2*time);
	render(image);
}

function render(image) {
	for(var y = 0; y < height; y++) {
		for(var x = 0; x < width; x++) {
			var plane_point = [plane_width * x/width - plane_width/2, plane_height * y/height - plane_height/2, planeZ];
			var ray = [eye, normalize(subtract(plane_point, eye))];
			var color = trace(ray, max_depth, 1.0);
			if(color === 0) {
				color = [0.95, 0.95, 0.95];
			}
			color = mult(255, color);
			var pos = 4 * (y * width + x);
			image.data[pos] = color[0];
			image.data[pos+1] = color[1];
			image.data[pos+2] = color[2];
			image.data[pos+3] = 255;
		}
	}
	a.putImageData(image, 0, 0);
}

function subtract(v1, v2) {
	return [
		v1[0] - v2[0],
		v1[1] - v2[1],
		v1[2] - v2[2]
	];
}

function mult(l, v) {
	return [
		l * v[0],
		l * v[1],
		l * v[2]
	];
}

function dot(v1, v2) {
	return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
}

function normalize(v) {
	var l = Math.sqrt(dot(v, v));
	return [v[0]/l, v[1]/l, v[2]/l];
}

function dist(p1, p2) {
	var l = subtract(p1, p2);
	return Math.sqrt(dot(l, l));
}

function add(v1, v2) {
	return subtract(v1, mult(-1, v2));
}

function trace(ray, depth, coef) {
	var min_dist = 10000;
	var p=0, center, j;
	for(var i = 0; i < drops.length; i++) {
		var q = intersection(ray, drops[i]);
		if(q !== 0) {
			var temp_dist = dist(q, ray[0]);
			if(min_dist > temp_dist) {
				p = q;
				min_dist = temp_dist;
				center = drops[i];
				j = i;
			}
		}
	}
	if(p !== 0) {
		var light_ray = [p, normalize(subtract(light, p))]
		var color = 0;
		for(var i = 0; i < drops.length; i++) {
			if(intersection(light_ray, drops[i]) !== 0) {
				color = [0, 0, 0];
				break;
			}
		}
		var normal = normalize(subtract(p, center));
		if(color == 0) {
			var lambert = dot(light_ray[1], normal) * coef;
			color = mult(lambert, colors[j]);

			// blinn phong specular
			var blinnDir = subtract(light_ray[1], ray[1]);
			var temp = Math.sqrt(dot(blinnDir, blinnDir));
			if(temp !== 0.0) {
				blinnDir = normalize(blinnDir);
				var blinnTerm = Math.max(dot(blinnDir, normal), 0.0);
				var specular = 0.3;
				blinnTerm = specular * Math.pow(blinnTerm, 40) * coef;
				color[0] += blinnTerm;
				color[1] += blinnTerm;
				color[2] += blinnTerm;
			}

		}
		// what does my reflection show?
		if(depth > 0) {
			var reflectiveness = 0.8;
			reflection_color = trace(outgoing_ray(ray, [p, normal]), depth - 1, coef * reflectiveness);
			if(reflection_color !== 0) {
				color = add(color, reflection_color);
			}
		}
		return color;
	} else {
		return 0;
	}
}

function outgoing_ray(ray, normal) {
	var reflet = 2.0 * dot(ray[1], normal[1]);
	return [normal[0], normalize(subtract(ray[1], mult(reflet, normal[1])))];
}

function intersection(ray, center) {
	var oc = subtract(ray[0], center),
		x = ray[1],
		a = dot(x, x),
		b = 2*dot(x, oc),
		c = dot(oc, oc) - drop_radius * drop_radius,
		radicand = b*b - 4 * a * c;
	if(radicand < 0) {
		return 0;
	}
	var root = Math.sqrt(radicand);
	var l1 = (-b - root) / 2 * a,
		l2 = (-b + root) / 2 * a;
	var l = l1 < 0.0001 ? l2 : l1;
	if(l < 0.0001) {
		return 0;
	}
	return subtract(ray[0], mult(-l, ray[1]));
}