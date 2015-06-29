function eachSimple(obj, callback) {
    var name, prop;
    for (name in obj) {
        if(obj.hasOwnProperty(name)){
            prop = obj[name];
            callback(name, prop);
        }
    }
}


function eachWithCall(obj, callback) {
    var name, prop;
    for (name in obj) {
        if(obj.hasOwnProperty(name)){
            prop = obj[name];
            callback.call(prop, name);
        }
    }
}

eachSimple(
    {a: 'a'}, 
    function(name, prop){
        TAJS_assert(name, 'isMaybeSingleStr', true);
        TAJS_assert(name, 'isMaybeSingleStr', true);
    }
);

eachWithCall(
    {a: 'a'}, 
    function(name){
        TAJS_assert(this.length /* check for a String-object property */, 'isMaybeSingleNum', true);
        TAJS_assert(name, 'isMaybeSingleStr', true);
    }
);
