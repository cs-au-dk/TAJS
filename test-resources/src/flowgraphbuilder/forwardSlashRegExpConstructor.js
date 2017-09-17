TAJS_assert(new RegExp('/').source === '\\/');
TAJS_assert(new RegExp('\\/').source === '\\/');
TAJS_assert(new RegExp('\\/').source === new RegExp('/').source);
TAJS_assert(new RegExp('/').source === /\//.source);
TAJS_assert(new RegExp('\\\\/').source === /\\\//.source);
