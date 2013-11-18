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

dumpState();
dumpObject(jQuery);
dumpValue(jQuery.get());
dumpValue(jQuery.post());