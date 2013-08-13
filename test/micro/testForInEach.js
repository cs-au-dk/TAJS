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
        assertSingleStr(name);
        assertSingleStr(prop);
    }
);

eachWithCall(
    {a: 'a'}, 
    function(name){
        assertSingleNum(this.length /* check for a String-object property */);
        assertSingleStr(name);
    }
);
