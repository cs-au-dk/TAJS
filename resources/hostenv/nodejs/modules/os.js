function getTotalMem() {
    return TAJS_make('AnyNum');
}

function getFreeMem() {
    return TAJS_make('AnyNum');
}

function getHostname() {
    return TAJS_make('AnyStr');
}

module.exports = {
    hostname : getHostname,
    totalmem : getTotalMem,
    freemem : getFreeMem
}