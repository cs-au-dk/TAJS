var D = Date,
title = "DidThis",
storage = localStorage,
tasks = JSON.parse(storage.getItem(title)) || {}, // structure: {date: [task1, task2, ...]}
element = document.createElement("p"),
formatDate = function(timestamp) { return new D(timestamp).toDateString() },
redraw = function() {
	var dates = [], j = 1,
	pie = {}, pieColors = {}, pieTotal = 0;
	a.strokeStyle = "#0";
	n = new D().getTime();
	// get earliest date
	for(k in tasks) dates.push(new D(k).getTime());
	dates.sort();
	w = dates.length * 15 + 100;
	c.height = w + 100;
	d = dates[0];
	// traw task matrix
	for(; d < n; d += 86400000, j++) {
		k = formatDate(d);
		if (tasks[k]) tasks[k].forEach(function(value, i) {
			// translate task into color by simply taking 1st three characters
			// in lower case and map them to a range between 0 and 255
			var color = "rgba(",
			t = tasks[k][i];
			for(z = 0; z < 3; z++) color += parseInt(255 * ((t.charCodeAt(z) - 97) / 25)) + ",";
			color += "1)";
			// draw tasks done per day
			a.fillStyle = color;
			a.fillRect(100 + 10 * i, 15 * j - 8, 9, 9);
			// count tasks for pie
			if(!pie[t]) pie[t] = 0;
			pie[t]++;
			pieTotal++;
			pieColors[t] = color;
		});
		// date label
		a.strokeText(k, 0, 15 * j);
	}
	// draw pie
	s = 0, j=0;
	for(t in pie) {
		e = s + pie[t] / pieTotal * 6.28;
		a.beginPath();
		a.moveTo(60, w);
		a.arc(60, w, 50, s, e, false);
		a.closePath();
		a.fillStyle = a.strokeStyle = pieColors[t];
		a.fill();
		a.strokeText(pie[t] + "x " + t, 120, w - 35 + 15 * j++);
		s = e;
	}
};
c.width = 300;
element.innerHTML = title + ":<input/><button>1x</button>";
b.appendChild(element);
elements = b.children[3].children;
// add new task
elements[1].onclick = function() {
	var date = formatDate(n),
	t = elements[0].value;
	if(!tasks[date]) tasks[date] = [];
	if(!t) return;
	tasks[date].push(t);
	storage.setItem(title, JSON.stringify(tasks));
	redraw();
};
redraw();