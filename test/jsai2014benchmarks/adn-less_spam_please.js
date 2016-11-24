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
    nsIWindowMediator: {},
    nsIIOService: {},
    nsICookieManager2: {}
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
  className: TopString,
  nodeValue: TopString,
  currentSet: TopString,
  persist: function() {},
  type: TopString,
  textContent: TopString,
  textLength: TopNum,
  selectionStart: TopNum,
  selectionEnd: TopNum,
  value: TopString
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

window.openDialog = window.open;

document.body = document;
document.parentNode = document;
document.childNodes = [];
document.childNodes.push(document);
document.childNodes.push(document);
document.firstChild = document;
document.ownerDocument = document;
document.popupNode = document;
document.element = document;

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
var ContextHelper = {
  popupNode: document,
  popupState: document
};

var ContextHandler = {
  registerType: function() {}
}

var alert = function() {};
var dump = function() {};
var prompt = function() { return TopString; }
var stop = function() {}
var location = {
    href: TopString
};

var content = {
    document: document
};

var messageManager = {
  addMessageListener: function() {},
  sendAsyncMessage: function() {}
};
var sendAsyncMessage = function() {};
var addMessageListener = function() {};

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

  getBoolPref: function() { return TopBool; },

  setCharPref: function() {},

  setIntPref: function() {},

  prefHasUserValue: function() { return TopBool; }
};

var __evtObj = {
    originalTarget: document,
    cancel: function() {},
    stopPropagation: function() {}
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

Components.classes["@mozilla.org/network/io-service;1"] = {
  getService: function() {
    return {
      newURI: function() {
        return {
          host: TopString
        };
      }
    }
  }  
}

Components.classes["@mozilla.org/cookiemanager;1"] = {
  getService: function() {
    return {
      add: function() {}
    }
  }
}

Components.classes["@mozilla.org/moz/jssubscript-loader;1"] = {
  getService: function() {
    return {
      loadSubScript: function() {}
    }
  }
}

window.setInterval = function(fun, time) {
  globalFuncList.push(fun);
  return TopNum;
}

window.clearInterval = function() {};
window.loadURI = function() {};

print("end of stub");
// STUB end


/***************************************************************************
Name: Less Spam, please
Version: 0.6.1mail address generator
Author: Benoit Bailleux
Homepage: -
Email:  skrypz42@yahoo.fr

Copyright (C) 2010 Benoit Bailleux

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to:

Free Software Foundation, Inc.
51 Franklin Street
Fifth Floor
Boston, MA  02110-1301
USA
***************************************************************************/

var lessspamplease = {
  
  _prefManager:Components.classes["@mozilla.org/preferences-service;1"]
                         .getService(Components.interfaces.nsIPrefService)
                         .getBranch("extensions.lessspamplease."),
  
  _providers: Array(),
  
  _mailboxes: Array(),
  
  _defaultProvider: "yopmail",
  
  strings: null,
  
  // ------------------------------------------------------------------------
  log: function(txt) {
    //Components.utils.reportError("[lessSpam] " + txt);
    dump("[lessSpam] " + txt + "\n");
  },
  
  // ------------------------------------------------------------------------
  shortText: function(txt) {
    return txt.charAt(0) + txt.length
  },
  
  // ------------------------------------------------------------------------
  scrambleText: function(txt) {
    // First, keep only alphabetical characters
    var a = txt.replace(/[^a-zA-Z]/g, "");
    if (a.length < 5) a += a;
    // then apply a ROT13 on the string
    a = a.replace(/[a-zA-Z]/g, function(c){
             return String.fromCharCode((c <= "Z" ? 90 : 122) >= (c = c.charCodeAt(0) + 13) ? c : c - 26);
             });
    // Then, sort the string
    return a.split("").sort().join("");
  },
  
  // ------------------------------------------------------------------------
  onFirefoxLoad: function() {
    this._providers['yopmail']    = ["yopmail.fr",
                                     "yopmail.net",
                                     "cool.fr.nf",
                                     "jetable.fr.nf",
                                     "nospam.ze.tc",
                                     "nomail.xl.cx",
                                     "mega.zik.dj",
                                     "speed.1s.fr",
                                     "courriel.fr.nf",
                                     "moncourrier.fr.nf",
                                     "monemail.fr.nf",
                                     "monmail.fr.nf",
                                     "mail.mezimages.net"];
    this._providers['humaility']  = ["humaility.com",
                                     "ignoremail.com",
                                     "bugmenever.com",
                                     "losemymail.com",
                                     "nobugmail.com",
                                     "nobuma.com",
                                     "nabuma.com"];
    this._providers['mailinator'] = ["mailinator.com",
                                     "binkmail.com",
                                     "bobmail.info",
                                     "zippymail.info",
                                     "thisisnotmyrealemail.com",
                                     "safetymail.info",
                                     "suremail.info"];
    this._providers['trashmail'] = ["trash-mail.com"];
    this._providers['dispostable'] = ["dispostable.com"];
    this._mailboxes= {'yopmail':   'http://www.yopmail.com?%%%',
                      'humaility': 'http://humaility.com/l?email=%%%',
                      'mailinator':'http://www.mailinator.com/maildir.jsp?email=%%%',
                      'trashmail':'http://www.trash-mail.com/index.php?mail=%%%',
                      'dispostable':'http://www.dispostable.com/inbox/%%%/',
                      '10minutemail':'http://10minutemail.com/10MinuteMail/index.html'};
    
    this.initialized = true;
    this.strings = document.getElementById("lessspamplease-strings");
    document.getElementById("contentAreaContextMenu")
            .addEventListener("popupshowing", function(e) {lessspamplease.showFirefoxContextMenu(e);}, false);
    document.getElementById("contentAreaContextMenu")
            .addEventListener("popupshowing", function(e) {lessspamplease.showFirefoxContextMenu(e);}, false);
  },
  
  // ------------------------------------------------------------------------
  showFirefoxContextMenu: function(event) {
    // show or hide the menuitem based on what the context menu is on
    document.getElementById("context-lessspamplease").hidden = !gContextMenu.onTextInput;
    document.getElementById("context-lessspampleaselink").hidden = (this._prefManager.getBoolPref("hidedirectaccess") || document.popupNode.ownerDocument.location == "about:blank");
  },
  
  // ------------------------------------------------------------------------
  onMenuItemOpenCommand: function() {
    var prefix = this._prefManager.getCharPref("userprefix");
    if (prefix == "") {
      alert(this.strings.getString("noprefix"));
      window.openDialog("chrome://lessspamplease/content/options.xul", "",
              "chrome, dialog, modal, resizable=no").focus();
      return;
    }
    var provider = this._prefManager.getCharPref("mailprovider");
    var mail = "";
    if (provider != "10minutemail") {
      var formatOption = this._prefManager.getIntPref("addresstype");
      var idx = 0;
      var site = "";
      var ioService = Components.classes["@mozilla.org/network/io-service;1"].getService(Components.interfaces.nsIIOService);
      // Host of current page
      var host = "";
      if (document.popupNode.ownerDocument.location.toString().match(/^about/g)) {
        host = "about";
      } else {
        host = ioService.newURI(document.popupNode.ownerDocument.location, null, null).host;
      }
      // Get domain name for that host:
      var firstDot = host.indexOf(".");
      var lastDot = host.lastIndexOf('.');
      if (firstDot != lastDot) { // There is a subdomain
        idx = host.length - (firstDot + 1);
        site = host.substring(firstDot + 1, lastDot);
      } else if (lastDot > 0) {
        idx = host.length;
        site = host.substring(0, lastDot);
      } else {
        idx = host.length;
        site = host;
      }
      if (! provider in this._providers) {
        provider = this._defaultProvider;
      }
      // Build mail address left value
      switch (formatOption) {
        case 1: // Plain format
          break;
        case 2:
          site = this.shortText(site);
          break;
        case 3:
          site = this.scrambleText(site);
          break;
      }
      mail = prefix + "." + site;
    }
    gBrowser.selectedTab = gBrowser.addTab(this._mailboxes[provider].replace('%%%', mail));
  },

  // ------------------------------------------------------------------------
  onMenuItemCommand: function(event) {
    var elt = document.popupNode;
    var prefix = this._prefManager.getCharPref("userprefix");
    if (prefix == "") {
      alert(this.strings.getString("noprefix"));
      window.openDialog("chrome://lessspamplease/content/options.xul", "",
              "chrome, dialog, modal, resizable=no").focus();
      return;
    }    
    var provider = this._prefManager.getCharPref("mailprovider");
    var mail = "";
    if (provider == "10minutemail") {
      // Request send to http://10minutemail.com/10MinuteMail/index.html
      // Then find the value of the "input" field which ID = addyForm:addressSelect
      var req = new XMLHttpRequest(); 
      req.withCredentials = "true";
      req.open('GET', 'http://10minutemail.com/10MinuteMail/index.html', false);   
      req.send(null);  
      if(req.status == 200)  {
        // gets the cookie :
        var cook = req.getResponseHeader("Set-Cookie");
        if (cook) {
          var s = cook.indexOf("=");
          var e = cook.indexOf(";", s);
          alert(cook + " / " + cook.substring(s + 1, e));
          cook = cook.substring(s + 1, e);
          var cm = Components.classes["@mozilla.org/cookiemanager;1"]
                       .getService(Components.interfaces.nsICookieManager2); // JSESSIONID=D55F9768D14470F34276BE199B343CA5; Path=/
          cm.add("10minutemail.com", "/", "JSESSIONID", cook, false,
                 false, true, 0);
        }
        // gets the content :
        var i = req.responseText.indexOf("addyForm:addressSelect");
        var e = req.responseText.indexOf(">", i);
        var re = /.*value="([^"]*)".*/;
        mail = req.responseText.substring(i, e).replace(re, "$1");
      }
    } else {
      var formatOption = this._prefManager.getIntPref("addresstype");
      var domain=  "";
      var site = "";
      var idx = 0;
      var ioService = Components.classes["@mozilla.org/network/io-service;1"].getService(Components.interfaces.nsIIOService);
      // Host of current page
      var host = "";
      if (elt.ownerDocument.location.toString().match(/^about/g)) {
        host = "about";
      } else {
        host = ioService.newURI(elt.ownerDocument.location, null, null).host;
      }
      // Get domain name for that host:
      var firstDot = host.indexOf(".");
      var lastDot = host.lastIndexOf('.');
      if (firstDot != lastDot) {
        idx = host.length - (firstDot + 1);
        site = host.substring(firstDot + 1, lastDot);
      } else if (lastDot > 0) {
        idx = host.length;
        site = host.substring(0, lastDot);
      } else {
        idx = host.length;
        site = host;
      }
      if (provider in this._providers) {
        idx = idx % this._providers[provider].length;
      } else {
        provider = this._defaultProvider;
      }
      // Get an alternate mail domain for that host:
      domain= this._providers[provider][idx];
      // Buid mail address
      switch (formatOption) {
        case 1: // Plain format
          break;
        case 2:
          site = this.shortText(site);
          break;
        case 3:
          site = this.scrambleText(site);
          break;
      }
      // For YopMail, use the "alias" if requested :
      if (provider == "yopmail" && this._prefManager.getBoolPref("ymalias")) {
        // As the request can be long, show to user that something is in progress
        var obgImg = elt.style.backgroundImage;
        var obgRep = elt.style.backgroundRepeat;
        var obgPos = elt.style.backgroundPosition;
        // A throbber in the input field :
        elt.style.backgroundImage = "url('data:image/gif;base64,R0lGODlhDAAMAPMAAP///8zMzJmZmWZmZjMzM+7u7t3d3bu7u6qqqoiIiHd3d1VVVURERCIiIhEREQAAACH/C05FVFNDQVBFMi4wAwEAAAAh/h1HaWZCdWlsZGVyIDAuNSBieSBZdmVzIFBpZ3VldAAh+QQJBQAAACwAAAAADAAMAAAERxDIAs6QWBpSEAGIgYnDIDDBAVDFkgRKehiBwCZCllJHL2KFgBAhQFCAqYDkoMSkJohQgBIwHGwAUbKnMhwLtmcoswI8C8cIACH5BAkFAAAALAAAAAAMAAwAAARIEMgChAKFSmBWSQvQHBuVnIrSYJiCBIJhDIeBUIWAbEBwUAGfZhIMHn68gi8gCfJ8EwTCEAAaDgKKoXfwHTmanJI05bGgGUkEACH5BAkFAAAALAAAAAAMAAwAAARGEMgCkACFSqyA6MSxUUiZJAyWIYeBGEZyJI4IHMEGJCEQ/BqJy/A7HIKSAC6Z2ygnJSIlYDgIKAYfzigyaArX50uH0aokEQAh+QQJBQAAACwAAAAADAAMAAAERhDIAg4CVM57BBiBRh0kIigYhhxGYBjCkTSGdISasNRBn0kwBCJA+gEMC8ZA0tNUDhtEi+LqUHg3EtSQKQgKRIDUmQoXMhEAIfkECQUAAAAsAAAAAAwADAAABEUQyAJCAFROVMIBiaFRx+EhAoYhhxEUhSksImBqAKJQ1osXiYTAc8hMFAOFxIJLKFcIFwW1IIhEHksplykIOp8oTuXBZCIAIfkECQUAAAAsAAAAAAwADAAABEUQyALCAVROVCxAhkYdh+VRBXIYQdEZiBICpUYLVKBnm4BYBx5GkBBIdDaBAoUABTAJxKARCpkQiwEtU8DtHE9b7tKSRAAAIfkECQUAAAAsAAAAAAwADAAABEcQyALCAVROVCxAhkYdh+VRBXIYQdGxQlZq1UEFeLaRlk2nCIQER0MkUCrDqiA4JBahUAAxUCgWlUwKMBgAGgEaBpBQAAyZCAAh+QQJBQAAACwAAAAADAAMAAAERxDIAsIBVE5ULECGRh2HFSAYhhxGUHStkJVadVBBnk15TtcFUkASENQOHBUCkTAUECcFJaQYJBQCBcBJbAASC8BiKAplt5kIACH5BAkFAAAALAAAAAAMAAwAAARFEMgCwgFUTlQsQIZGHYcVIBiGHEZQdK2QlVp1UEGeTXnP1raAZDCoeQAGBwMhK7AOzUtigUgwkRlDw4BQABQhkbCKyUQAACH5BAkFAAAALAAAAAAMAAwAAARGEMgCwgFUTlQsQIZGHYcVIBiGBF3RGYGQHahmUckwBBoW/DlFSGSSJBI9D4bAoLkChoMMcEEoaCSAIVNYRAUAwVATomEyEQAh+QQJBQAAACwAAAAADAAMAAAERRDIAsIBVE5ULzJTAByHFXBUQSyIUBSBEbiSMmgdJSgJqMGBQGKg8IVKEoEAZ5kMBhZK7EATUS0kgCFTUMA8Rgmo+ZJEAAAh+QQJBQAAACwAAAAADAAMAAAERhBIA0wARWrjjDkAQk3AwBxHAGYGMRxDUASWkBWDogFqhgii3SwQECgSGc0sJUHtVJJCIkHM0A42Hi+FAhmSBRs0KNxikhEAOw==')";
        elt.style.backgroundRepeat = "no-repeat";
        elt.style.backgroundPosition = "left center";
        // gets the alias for this address :
        var req = new XMLHttpRequest(); 
        var url = "http://www.yopmail.com/get_alt.php?c=" + prefix + "." + site;
        req.open('GET', url, false);
        try {
          req.send(null);
        } catch(e) {
          // Request failed
          // Nothing to do ...
        }
        if(req.status == 200)  {
          mail = req.responseText + "@" + domain;
        }
        elt.style.backgroundImage = obgImg;
        elt.style.backgroundRepeat = obgRep;
        elt.style.backgroundPosition = obgPos;
      } else {
        mail = prefix + "." + site + "@" + domain;
      }
    }
    if (elt.type == 'textarea') {
      if (elt.textLength == elt.selectionStart) elt.value = elt.value + mail;
      else elt.value = elt.value.substring(0, elt.selectionStart) + mail + elt.value.substring(elt.selectionEnd, elt.textLength);
    } else { // input fields
      elt.value = mail;
    }
  }
  
};

window.addEventListener("load", function(e) { lessspamplease.onFirefoxLoad(e); }, false);
function one(event) { lessspamplease.onMenuItemCommand(event)
}
document.addEventListener("command", one, false);

function two(event) { lessspamplease.onMenuItemOpenCommand(event)
}
document.addEventListener("command", two, false);

function three(event) { Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch).setIntPref('extensions.lessspamplease.addresstype', this.value)
}
document.addEventListener("command", three, false);

function four(event) { this.value=Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch).getIntPref('extensions.lessspamplease.addresstype')
}
document.addEventListener("command", four, false);

function five(event) { Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch).setCharPref('extensions.lessspamplease.mailprovider', this.value);
}
document.addEventListener("command", five, false);

function six(event) { this.value=Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch).getCharPref('extensions.lessspamplease.mailprovider');
}
document.addEventListener("command", six, false);

print("End of first phase registration");

/***************************************************************************
Name: Less Spam, please
Version: 0.6.1
Description: Temporary mail address generator
Author: Benoit Bailleux
Homepage: -
Email:  skrypz42@yahoo.fr

Copyright (C) 2010 Benoit Bailleux

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to:

Free Software Foundation, Inc.
51 Franklin Street
Fifth Floor
Boston, MA  02110-1301
USA
***************************************************************************/

var lsp = (function() {
  window.addEventListener("UIReady", function() {
    if (window.messageManager) {
      messageManager.loadFrameScript("chrome://lessspamplease/content/fennec-logic.js", true);
    } else {
      var loader = Components.classes["@mozilla.org/moz/jssubscript-loader;1"]
                     .getService(Ci.mozIJSSubScriptLoader);
      loader.loadSubScript("chrome://lessspamplease/content/fennec-content.js");
    }
  }, false);
  
  var debug = {
    receiveMessage: function receiveMessage(msg) {
      dump("LSP:" + msg.json.msg + '\n');
    }
  };
  messageManager.addMessageListener("lsp:debug", debug);
  
  var openPage = {
    receiveMessage: function receiveMessage(msg) {
      Browser.addTab(msg.json.URL, true, Browser.selectedTab);
    }
  };
  messageManager.addMessageListener("lsp:openPage", openPage);

})();

print("End of second phase");

function seven(event) { (function() {
          var state = ContextHelper.popupState || ContextHelper.popupNode;
          messageManager.sendAsyncMessage('lsp:setInputVal', { id: state.element });
        })();
}
document.addEventListener("command", seven, false);

function eight(event) { (function() {
          var state = ContextHelper.popupState || ContextHelper.popupNode;
          messageManager.sendAsyncMessage('lsp:openMailPage', { id: state.element });
        })();
}
document.addEventListener("command", eight, false);

print("End of second phase registration");

/***************************************************************************
Name: Less Spam, please
Version: 0.6.1
Description: Temporary mail address generator
Author: Benoit Bailleux
Homepage: -
Email:  skrypz42@yahoo.fr

Copyright (C) 2010 Benoit Bailleux

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to:

Free Software Foundation, Inc.
51 Franklin Street
Fifth Floor
Boston, MA  02110-1301
USA
***************************************************************************/

(function() {
var dbg = function(s) {
    sendAsyncMessage('lsp:debug', { msg: s });
};

var mainLogic = {
  _prefManager:Components.classes["@mozilla.org/preferences-service;1"]
                         .getService(Components.interfaces.nsIPrefService)
                         .getBranch("extensions.lessspamplease."),
  _providers: Array(),
  _mailboxes: Array(),
  _defaultProvider: "yopmail",
  // ------------------------------------------------------------------------
  shortText: function(txt) {
    return txt.charAt(0) + txt.length
  },
  // ------------------------------------------------------------------------
  scrambleText: function(txt) {
    // First, keep only alphabetical characters
    var a = txt.replace(/[^a-zA-Z]/g, "");
    if (a.length < 5) a += a;
    // then apply a ROT13 on the string
    a = a.replace(/[a-zA-Z]/g, function(c){
             return String.fromCharCode((c <= "Z" ? 90 : 122) >= (c = c.charCodeAt(0) + 13) ? c : c - 26);
             });
    // Then, sort the string
    return a.split("").sort().join("");
  },
  // ------------------------------------------------------------------------
  init: function() {
    this._providers['yopmail']    = ["yopmail.fr",
                                     "yopmail.net",
                                     "cool.fr.nf",
                                     "jetable.fr.nf",
                                     "nospam.ze.tc",
                                     "nomail.xl.cx",
                                     "mega.zik.dj",
                                     "speed.1s.fr",
                                     "courriel.fr.nf",
                                     "moncourrier.fr.nf",
                                     "monemail.fr.nf",
                                     "monmail.fr.nf",
                                     "mail.mezimages.net"];
    this._providers['humaility']  = ["humaility.com",
                                     "ignoremail.com",
                                     "bugmenever.com",
                                     "losemymail.com",
                                     "nobugmail.com",
                                     "nobuma.com",
                                     "nabuma.com"];
    this._providers['mailinator'] = ["mailinator.com",
                                     "binkmail.com",
                                     "bobmail.info",
                                     "zippymail.info",
                                     "thisisnotmyrealemail.com",
                                     "safetymail.info",
                                     "suremail.info"];
    this._providers['trashmail'] = ["trash-mail.com"];
    this._providers['dispostable'] = ["dispostable.com"];
    this._mailboxes= {'yopmail':   'http://www.yopmail.com?%%%',
                      'humaility': 'http://humaility.com/l?email=%%%',
                      'mailinator':'http://www.mailinator.com/maildir.jsp?email=%%%',
                      'trashmail':'http://www.trash-mail.com/index.php?mail=%%%',
                      'dispostable':'http://www.dispostable.com/inbox/%%%/',
                      '10minutemail':'http://10minutemail.com/10MinuteMail/index.html'};
    
    this.initialized = true;
    },
  // ------------------------------------------------------------------------
  insertAddress: function(elt) {
    var elt = document.popupNode;
    var prefix = this._prefManager.getCharPref("userprefix");
    var provider = this._prefManager.getCharPref("mailprovider");
    var withYopmailAlias = false;
    if (provider == "yopmailalias") {
      provider = "yopmail";
      withYopmailAlias = true;
    }
    var mail = "";
    var formatOption = this._prefManager.getIntPref("addresstype");
    var domain=  "";
    var site = "";
    var idx = 0;
    var ioService = Components.classes["@mozilla.org/network/io-service;1"].getService(Components.interfaces.nsIIOService);
    // Host of current page
    var host = "";
    if (elt.ownerDocument.location.toString().match(/^about/g)) {
      host = "about";
    } else {
      host = ioService.newURI(elt.ownerDocument.location, null, null).host;
    }
    // Get domain name for that host:
    var firstDot = host.indexOf(".");
    var lastDot = host.lastIndexOf('.');
    if (firstDot != lastDot) {
      idx = host.length - (firstDot + 1);
      site = host.substring(firstDot + 1, lastDot);
    } else if (lastDot > 0) {
      idx = host.length;
      site = host.substring(0, lastDot);
    } else {
      idx = host.length;
      site = host;
    }
    if (provider in this._providers) {
      idx = idx % this._providers[provider].length;
    } else {
      provider = this._defaultProvider;
    }
    // Get an alternate mail domain for that host:
    domain= this._providers[provider][idx];
    // Buid mail address
    switch (formatOption) {
      case 1: // Plain format
        break;
      case 2:
        site = this.shortText(site);
        break;
      case 3:
        site = this.scrambleText(site);
        break;
    }
    // For YopMail, use the "alias" if requested :
    if (withYopmailAlias) {
      // As the request can be long, show to user that something is in progress
      var obgImg = elt.style.backgroundImage;
      var obgRep = elt.style.backgroundRepeat;
      var obgPos = elt.style.backgroundPosition;
      // A throbber in the input field :
      elt.style.backgroundImage = "url('data:image/gif;base64,R0lGODlhDAAMAPMAAP///8zMzJmZmWZmZjMzM+7u7t3d3bu7u6qqqoiIiHd3d1VVVURERCIiIhEREQAAACH/C05FVFNDQVBFMi4wAwEAAAAh/h1HaWZCdWlsZGVyIDAuNSBieSBZdmVzIFBpZ3VldAAh+QQJBQAAACwAAAAADAAMAAAERxDIAs6QWBpSEAGIgYnDIDDBAVDFkgRKehiBwCZCllJHL2KFgBAhQFCAqYDkoMSkJohQgBIwHGwAUbKnMhwLtmcoswI8C8cIACH5BAkFAAAALAAAAAAMAAwAAARIEMgChAKFSmBWSQvQHBuVnIrSYJiCBIJhDIeBUIWAbEBwUAGfZhIMHn68gi8gCfJ8EwTCEAAaDgKKoXfwHTmanJI05bGgGUkEACH5BAkFAAAALAAAAAAMAAwAAARGEMgCkACFSqyA6MSxUUiZJAyWIYeBGEZyJI4IHMEGJCEQ/BqJy/A7HIKSAC6Z2ygnJSIlYDgIKAYfzigyaArX50uH0aokEQAh+QQJBQAAACwAAAAADAAMAAAERhDIAg4CVM57BBiBRh0kIigYhhxGYBjCkTSGdISasNRBn0kwBCJA+gEMC8ZA0tNUDhtEi+LqUHg3EtSQKQgKRIDUmQoXMhEAIfkECQUAAAAsAAAAAAwADAAABEUQyAJCAFROVMIBiaFRx+EhAoYhhxEUhSksImBqAKJQ1osXiYTAc8hMFAOFxIJLKFcIFwW1IIhEHksplykIOp8oTuXBZCIAIfkECQUAAAAsAAAAAAwADAAABEUQyALCAVROVCxAhkYdh+VRBXIYQdEZiBICpUYLVKBnm4BYBx5GkBBIdDaBAoUABTAJxKARCpkQiwEtU8DtHE9b7tKSRAAAIfkECQUAAAAsAAAAAAwADAAABEcQyALCAVROVCxAhkYdh+VRBXIYQdGxQlZq1UEFeLaRlk2nCIQER0MkUCrDqiA4JBahUAAxUCgWlUwKMBgAGgEaBpBQAAyZCAAh+QQJBQAAACwAAAAADAAMAAAERxDIAsIBVE5ULECGRh2HFSAYhhxGUHStkJVadVBBnk15TtcFUkASENQOHBUCkTAUECcFJaQYJBQCBcBJbAASC8BiKAplt5kIACH5BAkFAAAALAAAAAAMAAwAAARFEMgCwgFUTlQsQIZGHYcVIBiGHEZQdK2QlVp1UEGeTXnP1raAZDCoeQAGBwMhK7AOzUtigUgwkRlDw4BQABQhkbCKyUQAACH5BAkFAAAALAAAAAAMAAwAAARGEMgCwgFUTlQsQIZGHYcVIBiGBF3RGYGQHahmUckwBBoW/DlFSGSSJBI9D4bAoLkChoMMcEEoaCSAIVNYRAUAwVATomEyEQAh+QQJBQAAACwAAAAADAAMAAAERRDIAsIBVE5ULzJTAByHFXBUQSyIUBSBEbiSMmgdJSgJqMGBQGKg8IVKEoEAZ5kMBhZK7EATUS0kgCFTUMA8Rgmo+ZJEAAAh+QQJBQAAACwAAAAADAAMAAAERhBIA0wARWrjjDkAQk3AwBxHAGYGMRxDUASWkBWDogFqhgii3SwQECgSGc0sJUHtVJJCIkHM0A42Hi+FAhmSBRs0KNxikhEAOw==')";
      elt.style.backgroundRepeat = "no-repeat";
      elt.style.backgroundPosition = "left center";
      // gets the alias for this address :
      var url = "http://www.yopmail.com/get_alt.php?c=" + prefix + "." + site;
      var req = new XMLHttpRequest(); 
      req.open('GET', url, false);
      try {
        req.send(null);
      } catch(e) {
        // Request failed
        // Nothing to do ...
      }
      if(req.status == 200)  {
        mail = req.responseText + "@" + domain;
      }
      elt.style.backgroundImage = obgImg;
      elt.style.backgroundRepeat = obgRep;
      elt.style.backgroundPosition = obgPos;
    } else {
      mail = prefix + "." + site + "@" + domain;
    }
    return mail;
  },
  // ------------------------------------------------------------------------
  getProviderUrl: function(elt) {
    var prefix = this._prefManager.getCharPref("userprefix");
    var provider = this._prefManager.getCharPref("mailprovider");
    if (provider == 'yopmailalias') {
      provider = 'yopmail';
    }
    var mail = "";
    if (provider != "10minutemail") {
      var formatOption = this._prefManager.getIntPref("addresstype");
      var idx = 0;
      var site = "";
      var ioService = Components.classes["@mozilla.org/network/io-service;1"].getService(Components.interfaces.nsIIOService);
      // Host of current page
      var host = "";
      if (elt.ownerDocument.location.toString().match(/^about/g)) {
        host = "about";
      } else {
        host = ioService.newURI(elt.ownerDocument.location, null, null).host;
      }
      // Get domain name for that host:
      var firstDot = host.indexOf(".");
      var lastDot = host.lastIndexOf('.');
      if (firstDot != lastDot) { // There is a subdomain
        idx = host.length - (firstDot + 1);
        site = host.substring(firstDot + 1, lastDot);
      } else if (lastDot > 0) {
        idx = host.length;
        site = host.substring(0, lastDot);
      } else {
        idx = host.length;
        site = host;
      }
      if (! provider in this._providers) {
        provider = this._defaultProvider;
      }
      // Build mail address left value
      switch (formatOption) {
        case 1: // Plain format
          break;
        case 2:
          site = this.shortText(site);
          break;
        case 3:
          site = this.scrambleText(site);
          break;
      }
      mail = prefix + "." + site;
    }
    var url = this._mailboxes[provider].replace('%%%', mail);
    return(url);
  }
};

print("End of phase 3");
  
var lessSpam = {
  receiveMessage: function(msg) {
    var id = msg.json.id;
    var tgt = content.document.getElementById(id);
    if ( ! mainLogic.initialized) mainLogic.init();
    if (msg.name == "lsp:setInputVal") {
      if (tgt != null) {
        var mail = mainLogic.insertAddress(tgt);
        //tgt.value = mainLogic.scrambleText("benoit bailleux");
        //tgt.value = mainLogic._providers['yopmail'][3];
        //tgt.value = mainLogic.insertAddress(tgt);
        if (tgt.type == 'textarea') {
          if (tgt.textLength == tgt.selectionStart) tgt.value = tgt.value + mail;
          else tgt.value = tgt.value.substring(0, tgt.selectionStart) + mail + tgt.value.substring(tgt.selectionEnd, tgt.textLength);
        } else { // input fields
          tgt.value = mail;
        }

      } else {
        dbg("nop");
      }
    } else if (msg.name == "lsp:openMailPage") {
      sendAsyncMessage('lsp:openPage', { URL: mainLogic.getProviderUrl(tgt) });
    }
  }
};

print("End of phase 4");

addMessageListener("lsp:setInputVal", lessSpam);
// STUB
globalFuncList.push(function () { lessSpam.receiveMessage({json: {id: TopString}}) })
addMessageListener("lsp:openMailPage", lessSpam);

var idCount = 0;

ContextHandler.registerType("show-mail", function(state, element) {
  if (element.type && (element.type == "text" || element.type == "textarea")) {
    if (element.id == '') {
      element.id = 'lsp:' + idCount++;
    }
    state.element = element.id;
    return true;
  }
  return false;
});

})();


print("Before event loop");

while (true) {
  globalFuncList[TopNum](__evtObj);
}