var U = !!Math.random();

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
    while (U) {
        ({});
    }

    MAYBE_FIRST(true);
    SECOND();
    MAYBE_FIRST(false);
}

RUN();
