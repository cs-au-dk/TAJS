// STUB begin

var TopString = (45).toString(12);
var TopNum = Date.now();
var globalFuncList = [];

var Components = {
  classes: {},
  interfaces: {
    nsIPrefService: {},
    nsIPrefBranch2: {},
    nsILoginManager: {},
    nsILoginInfo: {},
    nsIClipboardHelper: {}
  },
  Constructor: function() {
    return function() {}
  }
};

var document = {
  getElementById: function() { return document; },
  addTab: function() { return document; },
  getElementsByTagName: function() { 
    var a = [];
    a.push(document);
    a.push(document);
    return a;
  },
  textContent: TopString,
  appendChild: function() { return document; },
  removeChild: function() { return document; },
  createElement: function() {return document; },
  setAttribute: function() {},
  addEventListener: function(e, f) {
    globalFuncList.push(f);
  }, 
  removeEventListener: function() {},
  getString: function() { return TopString; }
};

function addEventListener(e, f) {
    globalFuncList.push(f);
}

var __window = {
    document: document,
    getSelection: function() {
        return TopString;
    },
    focus: function() {}
}

window.open = function() {
    return __window;
}

document.body = document;
document.commandDispatcher = {
    focusedWindow: __window
};

var gContextMenu = document;
var gBrowser = document;
var alert = function() {};
var prompt = function() { return TopString; }

var content = {
    document: document
};

window.XMLHttpRequest = function() {
  this.open = function(method, url) {
    this["__domain"] = url;
    this.responseText = TopString;
    this.responseXML = document;
    this.readyState = TopNum;
    this.status = TopNum;
    this.statusText = TopString;
    this.timeout = TopNum;
  };

  this.abort = function() {};
  this.getAllResponseHeaders = function() { return TopString; };
  this.getResponseHeader = function(hdr) { return TopString; };
  this.overrideMimeType = function() {};
  this.setRequestHeader = function() {};

  this.sendAsBinary = function(msg) {};

  this.send = function(msg) {};
}

var __prefObj = {
  QueryInterface: function() {},
  
  addObserver: function(a, obj, ignore) {
    globalFuncList.push(obj.observe);
  },

  removeObserver: function() {},
  
  getCharPref: function() { return TopString; },

  getIntPref: function() { return TopNum; },

  setCharPref: function() {}  
};

Components.classes["@mozilla.org/preferences-service;1"] = {
  getService: function() {
    return {
      getBranch: function() {
        return __prefObj;
      }
    }
  }
}

window.setInterval = function(fun, time) {
  globalFuncList.push(fun);
  return TopNum;
}

window.clearInterval = function() {};

print("end of stub");
// STUB end

var chesscomnotifier = {
    prefs : 0,
    updateInterval : 10,
    userName : "",
    userId : 0,
    myMove : false,
    panel : null,
    strbundle : null,
    intervalId : 0,

    onLoad:
    function(e) {
        // get panel
        chesscomnotifier.panel = document.getElementById('chesscomnotifier-status-bar');

        // get preferences
        chesscomnotifier.prefs = Components.classes["@mozilla.org/preferences-service;1"]
            .getService(Components.interfaces.nsIPrefService)
            .getBranch("extensions.chesscomnotifier.");
        chesscomnotifier.updateInterval = chesscomnotifier.prefs.getIntPref("updateInterval");
        chesscomnotifier.userName = chesscomnotifier.prefs.getCharPref("userName");

        // get localization string bundle
        chesscomnotifier.strbundle = document.getElementById("chesscomnotifier-strings");

        // check that preferences are sane
        if (chesscomnotifier.updateInterval < 1)
            chesscomnotifier.updateInterval = 1;
        if (chesscomnotifier.userName == "")
            chesscomnotifier.panel.tooltipText = chesscomnotifier.strbundle.getString("errorUserName");


        // initialize update timer
        chesscomnotifier.intervalId = window.setInterval(chesscomnotifier.refreshStatus, chesscomnotifier.updateInterval*1000*60);
        chesscomnotifier.refreshStatus();
    },

    refreshStatus:
    function() {
        // check if user name has changed
        if (chesscomnotifier.userName != chesscomnotifier.prefs.getCharPref("userName")) {
            chesscomnotifier.userName = chesscomnotifier.prefs.getCharPref("userName");
            chesscomnotifier.userId = 0;  //< this will trigger a new lookup of the user id
        }
        // check if update interval has changed
        if (chesscomnotifier.updateInterval != chesscomnotifier.prefs.getIntPref("updateInterval")) {
            // save new update interval value
            chesscomnotifier.updateInterval = chesscomnotifier.prefs.getIntPref("updateInterval");

            // clear old update timer
            window.clearInterval(chesscomnotifier.intervalId);
            // initialize new update timer, update interval is in minutes
            chesscomnotifier.intervalId = window.setInterval(chesscomnotifier.refreshStatus, chesscomnotifier.updateInterval*1000*60);
        }

        // update userid if add-on just loaded or username has changed
        if (chesscomnotifier.userId == 0) {
            if (chesscomnotifier.userName != "")
                chesscomnotifier.getUserId(chesscomnotifier.userName);
            else
                chesscomnotifier.setStatus(chesscomnotifier.strbundle.getString("errorUserName"));

            return;
        }

        // callback function for status retrieval
        function infoReceived()
        {
            var output = httpRequest.responseText;

            // the response either contains a single '0' (meaning currently no move pending)
            // or a single '1' (at least one move pending)
            chesscomnotifier.mymove = (output == '1');

            // update display
            chesscomnotifier.panel.setAttribute("myMove", chesscomnotifier.mymove);
            if (chesscomnotifier.mymove)
                chesscomnotifier.panel.tooltipText = chesscomnotifier.strbundle.getString("mymove");
            else
                chesscomnotifier.panel.tooltipText = chesscomnotifier.strbundle.getString("nomove") + " "
                    + chesscomnotifier.userName + " (" + chesscomnotifier.userId + ")";
        }

        // retrieve status
        var httpRequest = null;
        var fullUrl = "http://www.chess.com/echess/is_game_ready.html?user_id=" + chesscomnotifier.userId;
        chesscomnotifier.setStatus(chesscomnotifier.strbundle.getString("retrievingStatus"));

        httpRequest = new XMLHttpRequest();
        httpRequest.open("GET", fullUrl, true);
        httpRequest.onload = infoReceived;
        httpRequest.send(null);
    },

    getUserId:
    function(userName) {
        // try cached entry first
        var cachedUserId = chesscomnotifier.prefs.getCharPref("cachedUserId");
        if (cachedUserId.indexOf(',') != -1) {
           var username_userid = cachedUserId.split(',');	
           if (username_userid[0] == userName && username_userid[1] != 0) {
               chesscomnotifier.userId = username_userid[1]
                   return;
           }
        }

        // callback function for user id retrieval
        function infoReceived()
        {
            var output = httpRequest.responseText;

            if (output.length) {
                // cut out the userid from "/home/alerts.html?track_user_id=[0-9]+&return_url=%2Fmembers%2Fsearch.html%3Fname%3DuserName%26country%3D"
                // or from "userName ... /home/send_message.html?id=userId"
                var myre1 = new RegExp("track_user_id=([0-9]+)\\S+" + chesscomnotifier.userName, "i");
                var myre2 = new RegExp(chesscomnotifier.userName + "[\\S\\s]*send_message.html.id=([0-9]+)", "i");
                print("before matches");
                var matches = myre1.exec(output);
                if (!matches) {
                    matches = myre2.exec(output);
                }
                print("after matches");
                if (matches) {
                    chesscomnotifier.userId = matches[1];

                    // save in cache
                    chesscomnotifier.prefs.setCharPref("cachedUserId",
                            chesscomnotifier.userName + "," + chesscomnotifier.userId);
                    chesscomnotifier.refreshStatus();
                }
                else {
                    chesscomnotifier.setStatus(chesscomnotifier.strbundle.getString("ErrorUserNotFound") + " " + chesscomnotifier.userName);
                }
            }
        }

        // retrieve user id
        var httpRequest = null;
        var fullUrl = "http://www.chess.com/members/search.html?name=" + userName + "&country=";
        chesscomnotifier.setStatus(chesscomnotifier.strbundle.getString("retrievingUserId"));

        httpRequest = new XMLHttpRequest();
        httpRequest.open("GET", fullUrl, true);
        httpRequest.onload = infoReceived;
        // STUB
        globalFuncList.push(httpRequest.onload);

        httpRequest.send(null);
    },

    setStatus:
    function(str) {
        chesscomnotifier.panel = document.getElementById('chesscomnotifier-status-bar');
        chesscomnotifier.panel.tooltipText = str;
    },

    gotoNextReadyGame:
    function() {
        if (chesscomnotifier.mymove)
            gBrowser.selectedTab = gBrowser.addTab("http://www.chess.com/echess/goto_ready_game.html");
        else
            gBrowser.selectedTab = gBrowser.addTab("http://www.chess.com/echess");
    }
};

window.addEventListener("load", function(e) { chesscomnotifier.onLoad(); }, false);

function one(event) { chesscomnotifier.gotoNextReadyGame()
}
document.addEventListener("command", one, false);

print("Before event loop");

while (true) {
  globalFuncList[TopNum]();
}