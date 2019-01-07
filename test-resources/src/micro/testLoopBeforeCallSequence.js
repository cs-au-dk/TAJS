function FIRST() {
    TARGET = 0;
}
function SECOND() {
    TARGET;
}
function MAYBE_FIRST(maybe) {
    if (maybe) FIRST();
}

function RUN() {
    while (!TAJS_make('AnyBool')) {
        ({});
    }

    MAYBE_FIRST(true);
    SECOND();
    MAYBE_FIRST(false);
}

RUN();
