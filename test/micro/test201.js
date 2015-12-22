var jQuery = {};

function callback(method) {
  jQuery[method] = function() {
    return "I'm " + method;
  };
}
TAJS_addContextSensitivity(callback, 0);

var obj = { "0": "get", "1": "post" };
for (var name in obj) {
  callback.call(obj[name], obj[name]);
} 

TAJS_dumpState();
TAJS_dumpObject(jQuery);
TAJS_dumpValue(jQuery.get());
TAJS_dumpValue(jQuery.post());