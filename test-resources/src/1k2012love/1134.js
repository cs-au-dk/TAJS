(function(){
  // Demo sting
  var text = "\n<p2><f90>I<p1><cred>♥<p1><c#000>J<p1>S<p2><f10>1k<p1>\nX<p1>D ";
  var fontsize;
  var timeout;
  var fontcolor = '#000';
  // farming bytes
  var sa = "setAttribute";
  var ac = "appendChild";
  var ce = "createElement";
  var docu = document;

  var cmds = {
    // Font size
    f:function(params) {
      fontsize = parseInt(params.c.slice(1));
    },
    // Color
    c:function(params) {
      fontcolor = params.c.slice(1);
    },
    // Pause
    p:function(token) {
      if(!token.p) {
        var time = new Date().getTime();
        if(!token.i) {
          token.i = time;
        } else {
          if(time>(token.i+parseFloat(token.c.slice(1))*1000)) {
            return token.p = true;
          }
        }
      }
    }
  }

  function render(commands,ini,end) {
    var index = 0, ele, i, node, new_end, ii, tmp;
    // Find first pause
    while(!end) {
      ele = commands[index++];
      if(ele) {
        if((ele["c"])&&(ele["c"][0]=="p"))
          end = index;
      } else {
        end = index-1;
      }
    }
    for(i=ini;i<end;i++) {
      node = commands[i];
      if(node.t) {
        if((node['t']!="\n")) {
          if(!node.txt) {
            tmp = b[ac](docu[ce]('span'));
            node.txt = tmp
            tmp.textContent = node.t;
            tmp[sa]("style", "font:" + fontsize + "px 'Helvetica';color:"+fontcolor);
          }
        } else {
          if(!node.txt) {
             node.txt = b[ac](docu[ce]('br'));
          }
        }
      } else {
        if(cmds[node['c'][0]](node)) {
          new_end;
          for(ii=end+1;ii<commands.length;ii++) {
            tmp = commands[ii];
            if((tmp["c"])&&(tmp["c"][0]=="p")) {
              new_end = ii+1;
              break;
            }
          }
          if(!new_end) {
            new_end = commands.length;
          }
          end = new_end;
        }
      }
    }
    timeout = setTimeout(function() {
      render(commands,ini,end);
    },99);
  }

  // Align body
  b[sa]("style", "text-align:center");
  
  // Process string data
  // function parse(text) 20 bytes
  var ret = [], finding = "<", current = "",to_insert,i,ele;
  for(i=0;i<text.length;i++) {
    ele = text[i];
    if(ele == finding || i==text.length-1) {
      if(finding == "<") {
        to_insert = {t:current};
        finding = ">";
      } else {
        to_insert = {c:current};
        finding = "<";
      }
      if(current != "") {
        ret.push(to_insert);
      }
      current = "";
    } else {
      if(ele=="\n"){
        if(current != "") {
          ret.push({t:current});
        }
        ret.push({t:ele});
        current = "";
      } else {
        current += ele;
      }
    }
  }
  // GO!!
  render(ret,0);
})();