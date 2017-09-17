var characterEncoding = '(?:\\\\.|[-\\w]|[^\\x00-\\xa0])+';
var tag1 = new RegExp( '^(' + characterEncoding.replace( '[-', '[-\\*' ) + ')' );
var tag1 = /^((?:\\.|[-\*\w]|[^\x00-\xa0])+)/;
TAJS_assert(tag1.test('x'));
TAJS_assert(!tag1.test('\x01'));

var tag2 = /^((?:\\.|[-\*\w]|[^\x00-\xa0])+)/;
TAJS_assert(tag2.test('x'));
TAJS_assert(!tag2.test('\x01'));