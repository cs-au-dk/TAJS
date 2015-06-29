Canvas = function (element, options) {
    this.init(element, options);
}
Canvas.prototype = {
    init: function (options) {
        this.options = options;
        this._createCanvasFromElement();
//        dumpValue(this.c)
//        dumpValue(this.cx)
    },
    // ...
    w: function() {
        return this.c.width;
    },
    h: function() {
        return this.c.height;
    },
    // ...
    _createCanvasFromElement: function () {
        var canvas = document.createElement('canvas'),
            element = this.options.element;
        canvas.className = 'heatmap';
        if (element) {
            canvas.width = element.offsetWidth;
            canvas.height = element.offsetHeight;
        } else {
            canvas.width = this.options.width;
            canvas.height = this.options.height;
        }
        this.c = canvas;
        this.cx = canvas.getContext('2d');
    }
}

Heatmap = function (c) {
    var buffer = new Canvas({width: c.w(), height: c.h()}),
        dot, scheme = [], current = 0;
    // ...
}
var canvas = new Canvas({element: document.createElement('div')});
var h = new Heatmap(canvas);

TAJS_dumpValue(canvas.c)
TAJS_dumpValue(canvas.cx)