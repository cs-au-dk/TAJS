a = a
b = b
c = c
sr = /\[[^\/][^\[\]]*\]/
function create_tree(_working_string){
    var _output = {ch: []}
    var _t, _tp, _tep, _ts, _twa, _to;
    while(sr.test(_working_string)){
        //Find out what the first tag in the string is
        _t = sr.exec(_working_string)[0]
        //Find out where exactly it is.
        _tp = _working_string.search(sr)
        _to = _tp + _t.length
        //If there's text between the start of this tag, and the end of the last one, parse it.
        if (_tp > 0) _output.ch.push({t: 't', c: _working_string.substring(0, _tp)})
        
        //If the bbcode has an attribute, retreive it. (Like [url=http://artvoyagers.com]this[/url])
        _ts = _t.search("=")
        _twa = _ts > 0 ? _t.substring(0, _ts) + "]" : _t

        //Check for an end tag
        _tep = find_end_tag(_working_string.substr(_to))  + _to
        //Recursively parse the children of the tag
        _output.ch.push({t: _twa.substr(1,_twa.length-2), v: _t.substring(_ts + 1, _t.length - 1), c: create_tree(_working_string.substring(_to, _tep))})
        //Remove the parsed section from the working string
        _working_string = _working_string.substr(_tep + _twa.length + 1)
    }
    _output.ch.push({t: 't', c: _working_string})
    return _output;
}
function find_end_tag(input){
    var _level = 1
    for (var _i = 0; _i < input.length; _i++){
        t = input.substr(_i)
        _level += (t.search(sr) == 0) - (t.search(/\[\/[^\[\]]+\]/) == 0)
        if (_level == 0) return _i
    }
    return false;
}
function parse_tree(tree){
    var _html = "";
    var b_ = {"b": "<b>^</b>","i": "<i>^</i>","u": "<u>^</u>","quote": '<div class=quote>^</div>',"img" : '<img src=^>', "url":'<a href=&>^</a>',"size":'<a style="font-size: &%">^</a>',"t" : "^"}
    if (tree.t == "t") return tree.c
    var _t = tree.ch ? tree.ch : tree.c.ch
    for (var _i = 0; _i < _t.length; _i++) {
        _html += (_t[_i].t in b_ ? b_[_t[_i].t].replace("&", _t[_i].v).replace("^", parse_tree(_t[_i]).replace(/\n/g,  "<br>")) : _t[_i].t);
    }
    return _html;
}

z = "[size=180]Type BBCode.[/size]"
b.innerHTML = ("<div id=i>" + parse_tree(create_tree(z)) + "</div><br><textarea cols=80 rows=10 onkeyup=\"b.firstChild.innerHTML=n(m(this.value))\">" + z + "");