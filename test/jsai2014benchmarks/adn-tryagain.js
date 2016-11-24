// STUB begin

var TopString = (45).toString(12);
var TopNum = Date.now();
var TopBool = !TopNum;

var globalFuncList = [];

var Components = {
  classes: {},
  interfaces: {
    nsIPrefService: {},
    nsIPrefBranch2: {},
    nsILoginManager: {},
    nsILoginInfo: {},
    nsIClipboardHelper: {},
    nsIConsoleService: {},
    nsIStringBundleService: {},
    nsITimer: {
        TYPE_REPEATING_SLACK: TopNum
    },
    nsIWindowMediator: {}
  },
  Constructor: function() {
    return function() {}
  },
  utils: {
    reportError: function() {}
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
  createElementNS: function() {return document; },
  setAttribute: function() {},
  getAttribute: function() { return TopString; },
  hasAttribute: function() { return TopBool; },
  removeAttribute: function() { },
  addEventListener: function(e, f) {
    globalFuncList.push(f);
  }, 
  removeEventListener: function() {},
  getString: function() { return TopString; },
  getFormattedString: function() { return TopString; },
  location: {
    href: TopString,
    reload: function() {},
    hostname: TopString,
    protocol: TopString
  },
  documentURI: TopString,
  title: TopString,
  click: function() {},
  nodeName: TopString,
  style: {},
  className: TopString
};

function addEventListener(e, f) {
    globalFuncList.push(f);
}

var __window = {
    document: document,
    getSelection: function() {
        return TopString;
    },
    focus: function() {},
    contentDocument: document,
    window: __window,
    currentURI: {
        asciiSpec: TopString
    }
}

__window.contentWindow = __window;
document.contentWindow = __window;

window.open = function() {
    return __window;
}

document.body = document;
document.parentNode = document;
document.commandDispatcher = {
    focusedWindow: __window
};
document.documentElement = document;

var gContextMenu = document;
var gBrowser = document;
gBrowser.browsers = [];
gBrowser.browsers.push(document);
gBrowser.getBrowserAtIndex = function() {
    return __window;
}
gBrowser.contentDocument = document;

var alert = function() {};
var prompt = function() { return TopString; }
var stop = function() {}
var location = {
    href: TopString
};

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

  setCharPref: function() {},

  setIntPref: function() {}
};

var __evtObj = {
    originalTarget: document,
    cancel: function() {}
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

Components.classes["@mozilla.org/consoleservice;1"] = {
    getService: function() {
        return {
            logStringMessage: function() {}
        }
    }
}

Components.classes["@mozilla.org/intl/stringbundle;1"] = {
    getService: function() {
        return {
            createBundle: function() {}
        }
    }
}

Components.classes["@mozilla.org/timer;1"] = {
    createInstance: function() {
        return {
            initWithCallback: function(evt) {
                globalFuncList.push(evt.notify);
            }
        }
    }
}

Components.classes["@mozilla.org/appshell/window-mediator;1"] = {
    getService: function() {
        return {
            getMostRecentWindow: function() {
                return __window;
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

window.addEventListener("load", function(e) { TryAgain.init(); }, false);

var TryAgain = {
    version: '3.4.2',
    STATUS_UNKNOWN: 0,
    STATUS_POLLING: 1,
    STATUS_LOCAL: 2,
    STATUS_GLOBAL: 3,
    iAmActive: true,
    strbundle: 0,
    downStatus: [],
    httpRequest: false,
    downCheckServers: {
            downforeojm: [ "http://downforeveryoneorjustme.com/%url%?src=%source%", "<title>([^<]*)</title>", "It's not just you!", "It's just you." ],
            uptimeauditor: [ "http://uptimeauditor.com/quicksitecheck.php?x=%url%&src=%source%", "/(fail|ok).gif", "fail", "ok" ],
        },
    console: Components.classes["@mozilla.org/consoleservice;1"].getService(Components.interfaces.nsIConsoleService),
    debug: function(msg) { TryAgain.console.logStringMessage(msg); },
    error: function(msg) { Components.utils.reportError(msg); },

    getString: function(str) {
        try {
            return TryAgain.strbundle.getString(str);
        } catch (e) {
            TryAgain.error("missing string: " + str + "; " + e);
            return str;
        }
    },
    getFormattedString: function(str, replacements) {
        try {
            return TryAgain.strbundle.getFormattedString(str, replacements);
        } catch (e) {
            TryAgain.error("missing string: " + str);
            return str;
        }  
    },  

    // Executed when Firefox loads
    init: function() {
        try {
            // Load string resource:
            TryAgain.strbundle = document.getElementById("tryagain_strings");
            if (!TryAgain.strbundle) {
                var extensionBundle = Components.classes["@mozilla.org/intl/stringbundle;1"].getService(Components.interfaces.nsIStringBundleService);
                TryAgain.strbundle = extensionBundle.createBundle("chrome://tryagain/locale/tryagain.properties");
            }
    
            // Add listener to the PageLoad event:
            var appcontent = document.getElementById("appcontent");
            if (appcontent) {
                appcontent.addEventListener("DOMContentLoaded", TryAgain.onPageLoad, true);
            }
    
            // Add listener to address bar
            var urlbar = document.getElementById("urlbar");
            urlbar.addEventListener("input", TryAgain.stop, true);
    
            // Add listener to ESC-key:
            var stop_key = document.getElementById("key_stop");
            stop_key.addEventListener("command", TryAgain.stop, true);
    
            // Show or hide the menu item:
            if (TryAgain_prefs.getPreference("showmenu")==1) {
                TryAgain.iAmActive = document.getElementById("TryAgainMenuItem").getAttribute("checked")=='true';
                var menu = document.getElementById('TryAgainMenuItem');
                menu.setAttribute("style","");
                menu.hidden = false;
            } else {
                TryAgain.iAmActive = true;
            }
        } catch (e) {
            alert(e);
        }
    },

    // Returns true if the 'Enable TryAgain' menu option is checked.
    isActive: function() {
        return TryAgain.iAmActive;
    },
    
    // Is called when user toggles the 'Enable TryAgain' menu option.
    toggleActive: function(menu) {
        if (menu.getAttribute('checked')=='true') {
            menu.setAttribute('checked',false);
            TryAgain.iAmActive = false;
        } else {
            menu.setAttribute('checked',true);
            TryAgain.iAmActive = true;
        }
    },

    // Returns the tab from which an onpageload event was fired
    getTabFromPageloadEvent: function(doc) {
        // Enumerate through tabs to find the tab where
        // the event came from:
        var num = gBrowser.browsers.length;
        for (var i = 0; i < num; i++) {
            var b = gBrowser.getBrowserAtIndex(i);
            if (b.contentDocument==doc) {
                return b;
            }
        }
        
        // No tab will be found if the pageload event was fired from within a frame or iframe:
        return false; //gBrowser.mCurrentTab;
    },
    
    // Returns the frame or iframe from which an onpageload event was fired
    getFrameFromPageloadEvent: function(doc) {
        // Enumerate through tabs to find the frame where
        // the event came from:
        var num = gBrowser.browsers.length;
        for (var i = 0; i < num; i++) {
            var b = gBrowser.getBrowserAtIndex(i);
            var result = TryAgain.checkFramesInDocument(b.contentDocument, doc);
            if (result!==false) {
                return result;
            }
        }
        return false;
    },
    
    // Searches through all browsers to find the frame or iframe that fired the pageload event.
    checkFramesInDocument: function(checkDoc, doc) {
        var frames = checkDoc.getElementsByTagName("frame");
        var i, result;
        if (frames) {
            for (i = 0; i < frames.length; i++) {
                if (frames[i].contentWindow.window.document.location == doc.location) {
                    return frames[i];
                } else {
                    result = TryAgain.checkFramesInDocument(frames[i].contentWindow.window.document, doc);
                    if (result!==false) {
                        return result;
                    }
                }
            }
        }
        var iframes = checkDoc.getElementsByTagName("iframe");
        if (iframes) {
            for (i = 0; i < iframes.length; i++) {
                if (iframes[i].contentWindow.window.document.location == doc.location) {
                    return iframes[i];
                } else {
                    result = TryAgain.checkFramesInDocument(iframes[i].contentWindow.window.document, doc);
                    if (result!==false) {
                        return result;
                    }
                }
            }
        }
        return false;
    },
    
    urlify: function(url, tab_uri, for_request) {
        url = url.replace('%source%', 'fx-tryagain-'+TryAgain.version);
        if (!tab_uri) {
            tab_uri = '';
        }
        url = url.replace('%url%', tab_uri);
        url = url.replace('%url_escaped%', escape(tab_uri));
        if (!for_request) {
            url = url.replace('&', '&amp;');
        }
        return url;
    },

    checkDownStatus: function(doc, tab_uri, id, url) {
        try {
            TryAgain.downStatus[id] = TryAgain.STATUS_POLLING;
            var httpRequest = new XMLHttpRequest();
            // Send a request
            url = TryAgain.urlify(url, tab_uri, true);
            httpRequest.open("GET", url, true, null, null);
            httpRequest.send("");
            TryAgain.updateDownStatus(httpRequest, doc, tab_uri, id);
        } catch (e) {
            // General error
            TryAgain.downStatus[id] = TryAgain.STATUS_UNKNOWN;
            Components.utils.reportError(e);
        }
        return httpRequest;
    },
    
    updateDownStatus: function(doc, url, id, httpRequest, regex, matchDown, matchUp) {
        if (httpRequest != false) {
            switch(httpRequest.readyState) {
            case 4:
                var status = httpRequest.status;
                if (status == 200) {
                    var response = httpRequest.responseText;
                    var regexp = new RegExp(regex, "gi");
                    var title = regexp.exec(response);
                    if (title.length == 2) {
                        switch (title[1]) {
                        case matchDown:
                            TryAgain.downStatus[id] = TryAgain.STATUS_GLOBAL;
                            break;
                        case matchUp:
                            TryAgain.downStatus[id] = TryAgain.STATUS_LOCAL;
                            break;
                        default:
                            // The website returned an unknown title
                            TryAgain.debug(id + ' returned unknown match "' + title[1] + '"');
                            TryAgain.downStatus[id] = TryAgain.STATUS_UNKNOWN;
                            break;
                        }
                    } else {
                        // Regular expression didn't match
                        TryAgain.debug(id + ' didn\'t match regular expression');
                        TryAgain.downStatus[id] = TryAgain.STATUS_UNKNOWN;
                    }
                } else {
                    // Bad status code
                    TryAgain.debug(id + ' returned HTTP ' + status);
                    TryAgain.downStatus[id] = TryAgain.STATUS_UNKNOWN;
                }
                break;
            default:
                // No response yet; will try again later
                return false;
            }
        }
        try {
            var server = TryAgain.downCheckServers[id];
            var status = doc.getElementById('status_'+id);
            if (!status) {
                Components.utils.reportError("Required page element missing: status_" + id);
                return;
            }
            var downStatus = TryAgain.downStatus[id];
            switch (downStatus) {
            case TryAgain.STATUS_POLLING:
                status.innerHTML =
                    '<a id="error_'+id+'" href="'+TryAgain.urlify(server[0], url)+'">' +
                    TryAgain.getString("text."+id) + '</a>' +
                    '<div>' + TryAgain.getString("text.site_down_checking") + '</div>';
                break;
            case TryAgain.STATUS_LOCAL:
                status.innerHTML =
                    '<a id="error_'+id+'" href="'+TryAgain.urlify(server[0], url)+'">' +
                    TryAgain.getString("text."+id) + '</a>' +
                    '<div style="color:red;"><b>' + TryAgain.getString("text.site_down_local") + '</b> ' +
                    '<a href="'+TryAgain.urlify('http://proxy.org/proxy.pl?url=%url_escaped%&proxy=proxify.com', url) + '">' +
                    TryAgain.getString("text.try_proxy") + '</a>' +
                    '</div>';
                break;
            case TryAgain.STATUS_GLOBAL:
                status.innerHTML =
                    '<a id="error_'+id+'" href="'+TryAgain.urlify(server[0], url)+'">' +
                    TryAgain.getString("text."+id) + '</a>' +
                    '<div><b>' + TryAgain.getString("text.site_down_global") + '</b></div>';
                var regexp2 = new RegExp("http[s]?://([^/]*)", "gi");
                var matches = regexp2.exec(url);
                if (matches != null && matches.length == 2) {
                    url = matches[1];
                }
                var error_div = doc.getElementById('errorShortDescText');
                error_div.innerHTML = TryAgain.getFormattedString("text.error_site_down", [url]);
                break;
            case TryAgain.STATUS_UNKNOWN:
            default:
                // The website returned an unknown title
                status.innerHTML =
                    TryAgain.getString("text.check_with") + ' <a id="error_'+id+'" href="'+TryAgain.urlify(server[0], url)+'">' +
                    TryAgain.getString("text."+id) + '</a>';
                break;
            }
        } catch (e) {
            Components.utils.reportError(e);
        }
        return true;
    },

    // Executed when the user presses ESC
    stop: function(stopType) {
        var doc = gBrowser.contentDocument;
        if (doc.documentURI.substr(0,14)=="about:neterror" || doc.title=="502 Bad Gateway") {
            if (TryAgain.isActive()) {
                var stopRetry_btn = doc.getElementById("errorStopRetry");
                stopRetry_btn.click();
            }
        }
    },

    // Executed on every pageload
    onPageLoad: function(anEvent) {
        print("Entering on page load");
        var errmessage = "";
        
        // Check if pageload concerns a document
        // (and not a favicon or something similar)
        var doc = anEvent.originalTarget;
        if (doc.nodeName != "#document") return;

        // Check if document is netError.xhtml
        if (doc.documentURI.substr(0,14)=="about:neterror") {
            var script1 = doc.getElementsByTagName("script")[0];
            var extraHTML = "var text_cancelled = '"+TryAgain.getString("text.cancelled")+"';\n"
                               + "var text_tryagain = '"+TryAgain.getString("text.tryagain")+"';\n"
                               + "var text_tried_times = '"+TryAgain.getString("text.tried_times")+"';\n";

            var tryAgain_btn = doc.getElementById("errorTryAgain");

            if (!TryAgain.isActive()) {
                // Hide the TryAgain part:
                var tryagainContainer = doc.getElementById("tryagainContainer");
                tryagainContainer.setAttribute("style", "display: none;");
                
                vars.innerHTML = "var p_timeout = -1; var p_max_repeat = 0; var p_repeat = 0;\n"
                               + extraHTML;
                tryAgain_btn.disabled = false;
                return;
            }

            var script2 = doc.createElement("script");
            script2.setAttribute("src", "chrome://tryagain/content/netError.js");
            script1.parentNode.appendChild(script2);

            var vars = doc.createElement("script");
            script1.parentNode.appendChild(vars);

            var stopRetry_btn = doc.createElementNS("http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul", "xul:button");
            stopRetry_btn.setAttribute("label", TryAgain.getFormattedString("text.stop_trying", []));
            stopRetry_btn.setAttribute("onclick", "stopRetry();");
            // STUB
            globalFuncList.push(stopRetry);

            stopRetry_btn.setAttribute("id", "errorStopRetry");
            tryAgain_btn.parentNode.appendChild(stopRetry_btn);

            var increment_btn = doc.createElement("button");
            increment_btn.setAttribute("id", "errorIncrement");
            increment_btn.setAttribute("onclick", "autoRetryThis();");
            // STUB
            globalFuncList.push(autoRetryThis);

            increment_btn.style.display = "none";
            tryAgain_btn.parentNode.appendChild(increment_btn);

            var retry_x_of_y = doc.createElement("p");
            retry_x_of_y.setAttribute("id", "retry_x_of_y");
            doc.getElementById("errorTitle").appendChild(retry_x_of_y);

            var page = doc.getElementById("errorPageContainer");
            if (page) {
                page = doc.getElementById("errorLongContent");
            }
            var tryagainCaches = doc.createElement("ul");
            tryagainCaches.setAttribute("id", "tryagainCaches");
            page.appendChild(tryagainCaches);

            var li = doc.createElement("li");
            li.setAttribute("id", "status_downforeojm");
            tryagainCaches.appendChild(li);

            li = doc.createElement("li");
            li.setAttribute("id", "status_uptimeauditor");
            tryagainCaches.appendChild(li);

            li = doc.createElement("li");
            li.innerHTML = TryAgain.getFormattedString("text.view_with", []) + " ";
            var a = doc.createElement("a");
            a.setAttribute("id", "errorGoogleCache");
            a.innerHTML = TryAgain.getFormattedString("text.cache_google", []);
            li.appendChild(a);
            tryagainCaches.appendChild(li);

            li = doc.createElement("li");
            li.innerHTML = TryAgain.getFormattedString("text.view_with", []) + " ";
            a = doc.createElement("a");
            a.setAttribute("id", "errorWebArchive");
            a.innerHTML = TryAgain.getFormattedString("text.cache_wayback", []);
            li.appendChild(a);
            tryagainCaches.appendChild(li);

            var tryagainContainer = doc.createElement("div");
            tryagainContainer.setAttribute("id", "tryagainContainer");
            page.appendChild(tryagainContainer);

            var errorAutoRetry1 = doc.createElement("div");
            errorAutoRetry1.setAttribute("id", "errorAutoRetry1");
            tryagainContainer.appendChild(errorAutoRetry1);
            errorAutoRetry1.innerHTML = TryAgain.getFormattedString("text.if_at_first", []);

            var errorAutoRetry2 = doc.createElement("span");
            errorAutoRetry2.setAttribute("id", "errorAutoRetry2");
            errorAutoRetry2.style.marginLeft = "1em";
            errorAutoRetry2.style.height = "12px";
            errorAutoRetry2.style.fontSize = "80%";
            errorAutoRetry2.style.color = "threedshadow";
            errorAutoRetry1.appendChild(errorAutoRetry2);

            var errorAutoRetry3 = doc.createElement("div");
            errorAutoRetry3.setAttribute("id", "errorAutoRetry3");
            errorAutoRetry3.style.height = "13px";
            tryagainContainer.appendChild(errorAutoRetry3);

            var tab;
            try {
                tab = TryAgain.getTabFromPageloadEvent(doc);
                var tab_uri = false;
                if (tab===false) {
                    tab = TryAgain.getFrameFromPageloadEvent(doc);
                    // Tab is now actually a FRAME or an IFRAME
                    if (tab===false) {
                        return;
                    } else {
                        tab_uri = tab.contentWindow.window.document.location.href;
                    }
                } else {
                    tab_uri = tab.currentURI.asciiSpec;
                }

                // Determine the desired timeout and max. repeats:
                var timeout = TryAgain_prefs.getPreference("timeout");
                var max_repeat = TryAgain_prefs.getPreference("repeat");
                var repeat  = 1;

                // If tab indicates that this page is *RE*loaded, update repeat-counter.
                if (tab.hasAttribute("tryagain_rep")) {
                    repeat = 1 + parseInt(tab.getAttribute("tryagain_rep"));
                }

                // If tab fails to load a webpage other than a previous one, reset the counter
                if (tab.hasAttribute("tryagain_uri")) {
                    if (tab_uri != tab.getAttribute("tryagain_uri")) {
                        repeat  = 1;
                        tab.setAttribute("tryagain_uri", tab_uri);
                    }
                } else {
                    tab.setAttribute("tryagain_uri", tab_uri);
                }

                var warningContent = doc.getElementById("securityOverrideContent");
                if (warningContent) {
                    warningContent.innerHTML = "Error...";
                }

                if (TryAgain_prefs.getPreference("hidetips")==1) {
                    var errorLongDesc = doc.getElementById("errorLongDesc");
                    if (errorLongDesc) errorLongDesc.setAttribute("style", "display: none;");
                }

                if (repeat<max_repeat || max_repeat<=0) {
                    vars.innerHTML = "var p_timeout = "+timeout+"; var p_max_repeat = "+max_repeat+"; var p_repeat = "+repeat+";\n"
                                   + extraHTML;
                    tab.setAttribute("tryagain_rep", repeat);

                    if (max_repeat==0) {
                        retry_x_of_y.innerHTML = TryAgain.getFormattedString("text.try_of_infinite", [repeat]);
                    } else {
                        retry_x_of_y.innerHTML = TryAgain.getFormattedString("text.try_of", [repeat, max_repeat]);
                    }
                } else {
                    retry_x_of_y.innerHTML = TryAgain.getFormattedString("text.tried_times", [repeat]);
                    retry_x_of_y.setAttribute("style", "color: red; font-weight: bold;");
                    var tryagainContainer = doc.getElementById("tryagainContainer");
                    tryagainContainer.setAttribute("style", "display: none;");

                    vars.innerHTML = "var p_timeout = -1; var p_max_repeat = "+max_repeat+"; var p_repeat = "+repeat+";\n"
                                   + extraHTML;
                }
                
                var errorGoogleCache = doc.getElementById("errorGoogleCache");
                if (errorGoogleCache) errorGoogleCache.setAttribute('href', TryAgain.urlify('http://72.14.209.104/search?q=cache:%url_escaped%', tab_uri));

                var errorWebArchive = doc.getElementById("errorWebArchive");
                if (errorWebArchive) errorWebArchive.setAttribute('href', TryAgain.urlify('http://web.archive.org/web/*/%url_escaped%', tab_uri));

                var timer1;
                try {
                    timer1 = Components.classes["@mozilla.org/timer;1"].createInstance(Components.interfaces.nsITimer);
                } catch (e) {
                    Components.utils.reportError(e);
                    return;
                }
                var event1 = {
                  notify: function(timer) {
                    try {
                      var errorIncrement = doc.getElementById("errorIncrement")
                      if (errorIncrement) errorIncrement.click();
                    } catch (e) {
                      Components.utils.reportError(e);
                      timer.cancel();
                    }
                  }
                }
                timer1.initWithCallback(event1, 1000, Components.interfaces.nsITimer.TYPE_REPEATING_SLACK);

                if (TryAgain_prefs.getPreference("useauditing")==1) {
                    var timers = [];
                    try {
                        for (id in TryAgain.downCheckServers) {
                            timers[id] = Components.classes["@mozilla.org/timer;1"].createInstance(Components.interfaces.nsITimer);
                        }
                    } catch (e) {
                        Components.utils.reportError(e);
                        return;
                    }
                    var events = [];
                    // First and every ten tries only
                    if (repeat == 1 || repeat % 10 == 0) {
                        for (id in TryAgain.downCheckServers) {
                            var server = TryAgain.downCheckServers[id];
                            var httpRequest = TryAgain.checkDownStatus(doc, tab_uri, id, server[0]);
                            TryAgain.downStatus[id] = TryAgain.STATUS_POLLING;
                            events[id] = {
                              serverId: '',
                              httpRequest: false,
                              regex: '',
                              matchDown: '',
                              matchUp: '',
                              register: function(id, httpRequest, regex, matchDown, matchUp) {
                                  this.id = id;
                                  this.regex = regex;
                                  this.httpRequest = httpRequest;
                                  this.matchDown = matchDown;
                                  this.matchUp = matchUp;
                              },
                              notify: function(timer) {
                                try {
                                  if (TryAgain.updateDownStatus(doc, tab_uri, this.id, this.httpRequest, this.regex, this.matchDown, this.matchUp)) {
                                    timer.cancel();
                                  }
                                } catch (e) {
                                  Components.utils.reportError(e);
                                  timer.cancel();
                                }
                              }
                            }
                            events[id].register(id, httpRequest, server[1], server[2], server[3]);
                            timers[id].initWithCallback(events[id], 100, Components.interfaces.nsITimer.TYPE_REPEATING_SLACK);
                        }
                    }
                }
                for (id in TryAgain.downCheckServers) {
                    if (TryAgain_prefs.getPreference("useauditing")==0) {
                        TryAgain.downStatus[id] = TryAgain.STATUS_UNKNOWN;
                    }
                    TryAgain.updateDownStatus(doc, tab_uri, id, false);
                }
            } catch (exception) {
                var error_div = doc.getElementById("errorLongDesc");
                error_div.innerHTML += "<div style=\"border: #F00 2px solid\">"
                                     + "<b>Unable to load TryAgain. Error message:</b>"
                                     + "<br />"+exception+"; "+errmessage+"</div>";
                Components.utils.reportError(exception);
            }
        } else {
            try {
                // A new webpage is loaded after the netError.xhtml page, so reset the counter to zero:
                tab = TryAgain.getTabFromPageloadEvent(doc);
                if (tab!==false)
                    tab.setAttribute("tryagain_rep", "0");
            } catch (exception) {
                // Just to make sure no errors occur on blank tabs.
            }
        }
        print("Finishing on page load");
    }
}
var TryAgain_prefs = {
   
    getPreference: function(s) {
        try {
            var pObj = Components.classes["@mozilla.org/preferences-service;1"]
                            .getService(Components.interfaces.nsIPrefService)
                            .getBranch("extensions.tryagain.");
            return pObj.getIntPref(s);
        } catch(e) {
            alert(e);
        }
    },

    savePreference: function(s, v) {
        try {
            var pObj = Components.classes["@mozilla.org/preferences-service;1"]
                            .getService(Components.interfaces.nsIPrefService)
                            .getBranch("extensions.tryagain.");
            pObj.setIntPref(s, parseInt(v));
        } catch(e) {
            alert(e);
        }
    },
    
    // Loads preferences into the options.xul window
    load: function() {
        try {
            var tb1 = document.getElementById("tryagainTimeout");
            tb1.value = TryAgain_prefs.getPreference("timeout");
            var tb2 = document.getElementById("tryagainRepeat");
            tb2.value = TryAgain_prefs.getPreference("repeat");
            var tb3 = document.getElementById("tryagainShowMenu");
            tb3.setAttribute('checked', (TryAgain_prefs.getPreference("showmenu")==1 ? 'true' : 'false'));
            var tb4 = document.getElementById("tryagainHideTips");
            tb4.setAttribute('checked', (TryAgain_prefs.getPreference("hidetips")==1 ? 'true' : 'false'));
            var tb5 = document.getElementById("tryagainUseAuditing");
            tb5.setAttribute('checked', (TryAgain_prefs.getPreference("useauditing")==1 ? 'true' : 'false'));
        } catch(e) {
            alert(e);
        }
    },

    // Save preferences from the options.xul window
    save: function() {
        try {
            var tb1 = document.getElementById("tryagainTimeout");
            if(parseInt(tb1.value) < 1) tb1.value = "1";
            TryAgain_prefs.savePreference("timeout", tb1.value);
            var tb2 = document.getElementById("tryagainRepeat");
            if(parseInt(tb2.value) < 0) tb2.value = "0";
            TryAgain_prefs.savePreference("repeat", tb2.value);
            
            var tb3 = document.getElementById("tryagainShowMenu");
            var win  = Components.classes["@mozilla.org/appshell/window-mediator;1"].getService(Components.interfaces.nsIWindowMediator).getMostRecentWindow("navigator:browser");
            var menu = win.document.getElementById('TryAgainMenuItem');

            if(tb3.getAttribute('checked')=="true") {
                menu.hidden = false;
                menu.removeAttribute('style'); // In case style='display:none;' (leftover from 3.0 alpha version)
                TryAgain_prefs.savePreference("showmenu", 1);
            } else {
                menu.hidden = true;
                menu.setAttribute("checked","true");
                win.TryAgain.iAmActive = true;
                TryAgain_prefs.savePreference("showmenu", 0);
            }

            var tb4 = document.getElementById("tryagainHideTips");
            if(tb4.getAttribute('checked')=="true") {
                TryAgain_prefs.savePreference("hidetips", 1);
            } else {
                TryAgain_prefs.savePreference("hidetips", 0);
            }

            var tb5 = document.getElementById("tryagainUseAuditing");
            if(tb5.getAttribute('checked')=="true") {
                TryAgain_prefs.savePreference("useauditing", 1);
            } else {
                TryAgain_prefs.savePreference("useauditing", 0);
            }
        } catch(e) {
            alert(e);
        }
    }
};
function one(event) { TryAgain.toggleActive(this);
}
document.addEventListener("command", one, false);

function two(event) { TryAgain_prefs.save();
}
document.addEventListener("command", two, false);

function three(event) { TryAgain_prefs.load();
}
document.addEventListener("command", three, false);

// Error url MUST be formatted like this:
//   moz-neterror:page?e=error&u=url&d=desc
//
// or optionally, to specify an alternate CSS class to allow for
// custom styling and favicon:
//
//   moz-neterror:page?e=error&u=url&s=classname&d=desc
//
// Note that this file uses document.documentURI to get
// the URL (with the format from above). This is because
// document.location.href gets the current URI off the docshell,
// which is the URL displayed in the location bar, i.e.
// the URI that the user attempted to load.

var RETRY_CANCEL = 0;
var RETRY_NORMAL = 1;
var RETRY_OTHER  = 2;
var RETRY_NONE   = 3;

var count           = -1;
var countdown       = "";
var auto_retry      = RETRY_NORMAL;

var retrying = false;

function disableTryAgain() {
    p_timeout = -1;
    auto_retry = RETRY_NONE;
    document.getElementById("tryagainContainer").style.display = "none";
    document.getElementById("errorTryAgain").style.display = "none";
    document.getElementById("errorStopRetry").style.display = "none";
    document.getElementById("errorGoogleCache").style.display = "none";
    document.getElementById("errorWebArchive").style.display = "none";
    document.getElementById("retry_x_of_y").style.display = "none";
}

function retryThisExtended() {
    document.getElementById("errorStopRetry").disabled = false;
    auto_retry = RETRY_OTHER;
    retryThis();
}

function autoRetryThis() {
    var strbundle = document.getElementById("strings");
    
    if (retrying || typeof p_timeout == 'undefined') return;
    
    if (auto_retry != RETRY_NORMAL) {
        if (auto_retry == RETRY_CANCEL && text_cancelled) {
            // User has pressed the cancel button
            document.getElementById("errorAutoRetry3").innerHTML = text_cancelled;
        }
        return;
    }
    if (p_timeout<0) {
        // Maximum number of retries reached
        document.getElementById("errorStopRetry").disabled=true;
        window.stop();
        return;
    }

    if (count < 0) {
        // Page has just loaded, and counter is still null.
        // Get the correct value from the p_timeout variable.
        count = p_timeout + 1;
    }
    if (count > 0) {
        // Countdown 1 second..
        count--;
        if(count > 0) {
            countdown += " "+count+"..";
        }
    }
    document.getElementById("errorAutoRetry2").innerHTML = countdown;
    if (count == 0){
        // Done counting down; reload.
        retrying = true;
        document.getElementById("errorAutoRetry3").innerHTML = text_tryagain;
        retryThis();
    }
}

function stopRetry() {
    window.stop();
    auto_retry = RETRY_CANCEL;
    document.getElementById("errorStopRetry").disabled=true;
    autoRetryThis();
}

function gotoGoogleCache() {
    window.stop();
    auto_retry = RETRY_OTHER;
    //location.replace("http://72.14.209.104/search?q=cache:" + document.location);
    location.href = "http://72.14.209.104/search?q=cache:" + document.location;
    autoRetryThis();
}

function gotoWayBackArchive() {
    window.stop();
    auto_retry = RETRY_OTHER;
    //location.replace("http://web.archive.org/web/*/" + document.location);
    location.href = "http://web.archive.org/web/*/" + document.location;
    autoRetryThis();
}

function gotoDownForEveryoneOrJustMe() {
    window.stop();
    auto_retry = RETRY_CANCEL;
    location.href = "http://downforeveryoneorjustme.com/" + document.location;
    autoRetryThis();
}

function getErrorCode() {
    var url = document.documentURI;
    var error = url.search(/e\=/);
    var duffUrl = url.search(/\&u\=/);
    return decodeURIComponent(url.slice(error + 2, duffUrl));
}

function getCSSClass() {
    var url = document.documentURI;
    var matches = url.match(/s\=([^&]+)\&/);
    // s is optional, if no match just return nothing
    if (!matches || matches.length < 2)
        return "";

    // parenthetical match is the second entry
    return decodeURIComponent(matches[1]);
}

function getDescription() {
    var url = document.documentURI;
    var desc = url.search(/d\=/);

    // desc == -1 if not found; if so, return an empty string
    // instead of what would turn out to be portions of the URI
    if (desc == -1)
        return "";

    return decodeURIComponent(url.slice(desc + 2));
}

function retryThis() {
    // Session history has the URL of the page that failed
    // to load, not the one of the error page. So, just call
    // reload(), which will also repost POST data correctly.
    try {
        location.reload();
    } catch (e) {
        // We probably tried to reload a URI that caused an exception to
        // occur;    e.g. a non-existent file.
    }
    document.getElementById("errorTryAgain").disabled = true;
}

function initPage() {
    var err = getErrorCode();
    
    // if it's an unknown error or there's no title or description
    // defined, get the generic message
    var errTitle = document.getElementById("et_" + err);
    var errDesc    = document.getElementById("ed_" + err);
    if (!errTitle || !errDesc)
    {
        errTitle = document.getElementById("et_generic");
        errDesc    = document.getElementById("ed_generic");
    }

    var title = document.getElementById("errorTitleText");
    if (title)
    {
        title.parentNode.replaceChild(errTitle, title);
        // change id to the replaced child's id so styling works
        errTitle.id = "errorTitleText";
    }

    var sd = document.getElementById("errorShortDescText");
    if (sd)
        sd.textContent = getDescription();

    var ld = document.getElementById("errorLongDesc");
    if (ld)
    {
        ld.parentNode.replaceChild(errDesc, ld);
        // change id to the replaced child's id so styling works
        errDesc.id = "errorLongDesc";
    }

    // remove undisplayed errors to avoid bug 39098
    var errContainer = document.getElementById("errorContainer");
    errContainer.parentNode.removeChild(errContainer);

    var className = getCSSClass();
    if (className && className != "expertBadCert") {
        // Associate a CSS class with the root of the page, if one was passed in,
        // to allow custom styling.
        // Not "expertBadCert" though, don't want to deal with the favicon
        document.documentElement.className = className;

        // Also, if they specified a CSS class, they must supply their own
        // favicon.    In order to trigger the browser to repaint though, we
        // need to remove/add the link element.
        var favicon = document.getElementById("favicon");
        var faviconParent = favicon.parentNode;
        faviconParent.removeChild(favicon);
        favicon.setAttribute("href", "chrome://global/skin/icons/" + className + "_favicon.png");
        faviconParent.appendChild(favicon);
    }
    if (className == "expertBadCert") {
        showSecuritySection();
    }

    if (err == "nssBadCert") {
        // Remove the "Try again" button for security exceptions, since it's
        // almost certainly useless.
        document.getElementById("errorTryAgain").style.display = "none";
        document.getElementById("errorPageContainer").setAttribute("class", "certerror");
        disableTryAgain();
        addDomainErrorLink();
    }
    else {
        // Remove the override block for non-certificate errors.    CSS-hiding
        // isn't good enough here, because of bug 39098
        var secOverride = document.getElementById("securityOverrideDiv");
        secOverride.parentNode.removeChild(secOverride);
    }
}

function showSecuritySection() {
    // Swap link out, content in
    document.getElementById('securityOverrideContent').style.display = '';
    document.getElementById('securityOverrideLink').style.display = 'none';
}

/* In the case of SSL error pages about domain mismatch, see if
     we can hyperlink the user to the correct site.    We don't want
     to do this generically since it allows MitM attacks to redirect
     users to a site under attacker control, but in certain cases
     it is safe (and helpful!) to do so.    Bug 402210
*/
function addDomainErrorLink() {
    // Rather than textContent, we need to treat description as HTML
    var sd = document.getElementById("errorShortDescText");
    if (sd)
        sd.innerHTML = getDescription();

    var link = document.getElementById('cert_domain_link');
    if (!link)
        return;

    var okHost = link.getAttribute("title");
    var thisHost = document.location.hostname;
    var proto = document.location.protocol;

    // If okHost is a wildcard domain ("*.example.com") let's
    // use "www" instead.    "*.example.com" isn't going to
    // get anyone anywhere useful. bug 432491
    okHost = okHost.replace(/^\*\./, "www.");

    /* case #1: 
     * example.com uses an invalid security certificate.
     *
     * The certificate is only valid for www.example.com
     *
     * Make sure to include the "." ahead of thisHost so that
     * a MitM attack on paypal.com doesn't hyperlink to "notpaypal.com"
     *
     * We'd normally just use a RegExp here except that we lack a
     * library function to escape them properly (bug 248062), and
     * domain names are famous for having '.' characters in them,
     * which would allow spurious and possibly hostile matches.
     */
    if (endsWith(okHost, "." + thisHost))
        link.href = proto + okHost;

    /* case #2:
     * browser.garage.maemo.org uses an invalid security certificate.
     *
     * The certificate is only valid for garage.maemo.org
     */
    if (endsWith(thisHost, "." + okHost))
        link.href = proto + okHost;
}

function endsWith(haystack, needle) {
    return haystack.slice(-needle.length) == needle;
}

print("Before event loop");

while (true) {
  globalFuncList[TopNum](__evtObj);
}