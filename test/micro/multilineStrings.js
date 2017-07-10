var s1 = 'asdf\
';

var s2 = '\
\nasdf\
';

var s3 = '\
\tasdf\
';

var s4 = '\
\\\
';

var s5 = '\
\
';

var s6 = 'asdf\
\nasdf\
\rasdf\
\tasdf\
\\\
\
';

var l1 = s1.length;
var l2 = s2.length;
var l3 = s3.length;
var l4 = s4.length;
var l5 = s5.length;
var l6 = s6.length;

TAJS_dumpValue(s1);
TAJS_dumpValue(s2);
TAJS_dumpValue(s3);
TAJS_dumpValue(s4);
TAJS_dumpValue(s5);
TAJS_dumpValue(s6);

TAJS_dumpValue(l1);
TAJS_dumpValue(l2);
TAJS_dumpValue(l3);
TAJS_dumpValue(l4);
TAJS_dumpValue(l5);
TAJS_dumpValue(l6);