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
  insertBefore: function() {},
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
  value: TopString,
  localName: TopString,
  boxObject: { 
    width: TopNum,
    x: TopNum
  },
  id: TopString
};

function addEventListener(e, f) {
    globalFuncList.push(f);
}

function removeEventListener() {}

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

window.close = function() {}
window.openDialog = window.open;

document.body = document;
document.parentNode = document;
document.childNodes = [];
document.childNodes.push(document);
document.childNodes.push(document);
document.firstChild = document;
document.lastChild = document;
document.nextSibling = document;
document.previousSibling = document;
document.ownerDocument = document;
document.popupNode = document;
document.element = document;

document.attributes = [];
document.attributes.push(document);
document.attributes.push(document);

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
var sizeToContent = function() {}

var location = {
    href: TopString
};

var content = {
    document: document
};

var _content = content;

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
    stopPropagation: function() {},
    target: document,
    clientX: TopNum
};

function TransferData() {
    this.addDataForFlavour = function() {};
}

function FlavourSet() {
    this.appendFlavour = function() {}
} 
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



/**
 * $Id: liveprOverlay.js,v 1.11 2006/11/08 14:14:07 peter Exp $
 *
 * Script to load LivePR value for current URL and display on statusbar.
 * This script is part of the LivePR Firefox Extension.
 * It is licensed under the GPL. 
 * More details on <http://livepr.raketforskning.com/>.
 */

/** */
function hexdec(str) {
    return parseInt(str,16);
}

/** */
function zeroFill(a,b) {
    var z = hexdec(80000000);
    if (z & a) {
        a = a>>1;
        a &= ~z;
        a |= 0x40000000;
        a = a>>(b-1);
    } else {
        a = a >> b;
    }
    return (a);
}

/** */
function mix(a,b,c) {
    a -= b; a -= c; a ^= (zeroFill(c,13));
    b -= c; b -= a; b ^= (a<<8);
    c -= a; c -= b; c ^= (zeroFill(b,13));
    a -= b; a -= c; a ^= (zeroFill(c,12));
    b -= c; b -= a; b ^= (a<<16);
    c -= a; c -= b; c ^= (zeroFill(b,5));
    a -= b; a -= c; a ^= (zeroFill(c,3));
    b -= c; b -= a; b ^= (a<<10);
    c -= a; c -= b; c ^= (zeroFill(b,15));
    var ret = new Array((a),(b),(c));
    return ret;
}

/** */
function GoogleCH(url) {
    var init = 0xE6359A60;
    var length = url.length;   
    var a = 0x9E3779B9;
    var b = 0x9E3779B9;
    var c = 0xE6359A60;
    var k = 0;
    var len = length;
    var mixo = new Array();
    while (len >= 12) {
        a += (url[k+0] +(url[k+1]<<8) +(url[k+2]<<16) +(url[k+3]<<24));
        b += (url[k+4] +(url[k+5]<<8) +(url[k+6]<<16) +(url[k+7]<<24));
        c += (url[k+8] +(url[k+9]<<8) +(url[k+10]<<16)+(url[k+11]<<24));
        mixo = mix(a,b,c);
        a = mixo[0]; b = mixo[1]; c = mixo[2];
        k += 12;
        len -= 12;
    }
    c += length;
    switch(len)
    {
        case 11:
        c += url[k+10]<<24;
        case 10:
        c+=url[k+9]<<16;
        case 9 :
        c+=url[k+8]<<8;
        case 8 :
        b+=(url[k+7]<<24);
        case 7 :
        b+=(url[k+6]<<16);
        case 6 :
        b+=(url[k+5]<<8);
        case 5 :
        b+=(url[k+4]);
        case 4 :
        a+=(url[k+3]<<24);
        case 3 :
        a+=(url[k+2]<<16);
        case 2 :
        a+=(url[k+1]<<8);
        case 1 :
        a+=(url[k+0]);
    }
    mixo = mix(a,b,c);
//    if (mixo[2] < 0)
//        return (0x100000000 + mixo[2]);
//    else
    return mixo[2];
}

/** */
function strord(string) {
    var result = new Array();
    for (var i = 0; i < string.length; ++i) {
        result[i] = string[i].charCodeAt(0);
    }
    return result;
}

/** */
function c32to8bit(arr32) {
    var arr8 = new Array();	
    for (var i = 0; i < arr32.length; ++i) {
    	for (bitOrder = i*4; bitOrder <= i*4+3; ++bitOrder) {
            arr8[bitOrder] = arr32[i]&255;
            arr32[i] = zeroFill(arr32[i], 8);
        }
    }
    return arr8;
}

/** */
function myfmod(x,y) {
	var i = Math.floor(x/y);
    return (x - i*y);
}

/** */
function GoogleNewCH(url) {
    var ch = "6"+GoogleCH(strord(url));
    return ch;
}

/** */
function cleanURL(str) {
    return str.replace("http://", "");
}

var prefix;

/** Set LivePR value. */
function setLivePR() {
    var livepr = document.getElementById('livepr-status');
    var tooltip = document.getElementById('livepr-tooltip-value');
    var tmp = liveprreq.responseText;
    if (tmp.length == 0) return;

    var data = tmp.split(/\n/);
    var results_pr = new Array();
    var results_url = new Array();
    // Extract relevant data.
    if (prefix == "info:") {
        for (var i = 0; i < data.length; ++i) {
            var m = data[i].match(/^Rank_\d+:\d+:(\d+)/);
            if (m) {
                results_pr[1] = m[1];
            }
            if (m = data[i].match(/^URL_\d+:\d+:(.+)/)) {
                results_url[1] = m[1];
            }
        }
        if (results_pr[1]) {
            pr = results_pr[1];
        } else {
            pr = -1;
        }
    } else {
        for (var i = 0; i < data.length; ++i) {
            // Get level.
            var m = data[i].match(/^Level_(\d+):/);
            if (m) {
                // Group this entry.
                var level = m[1];
                while (data[i] != "") {
                    if ((m = data[i].match(/^Rank_\d+:\d+:(\d+)/))) {
                        results_pr[level] = m[1];
                    }
                    if ((m = data[i].match(/^URL_\d+:\d+:(.+)/))) {
                        results_url[level] = m[1];
                    }
                    ++i;
                }
            }
        }
        // Create url permutations.
        var url = new String(window._content.document.location);
        var nonwwwurl = url.replace('www.', '');
        var urlpermutations = new Array(url,
                                        "http://"+url, 
                                        "http://www."+url, 
                                        url+"/", 
                                        "http://"+url+"/", 
                                        "http://www."+url+"/", 
                                        nonwwwurl, 
                                        "http://"+nonwwwurl, 
                                        "http://www."+nonwwwurl, 
                                        nonwwwurl+"/", 
                                        "http://"+nonwwwurl+"/", 
                                        "http://www."+nonwwwurl+"/");
        // Fish out PR.
        var pr = -1;
        for (var i = 1; i < results_url.length; ++i) {
            if (pr != -1) break;
            for (var j = 0; j < urlpermutations.length; ++j) {
                if (urlpermutations[j].toLowerCase() == results_url[i].toLowerCase()) {
                    pr = results_pr[i];
                    break;
                }
            }
        }
    }
    // ToolTip text.
    var tt = (pr >= 0 && pr <= 10) ? pr+"/10" : "N/A";

    livepr.setAttribute("livepr", pr);
    tooltip.setAttribute("value", "PageRank: "+tt);
    liveprLastPR = pr;
}

/** Encode a URL. */
function URLencode(sStr) {
    //enc = encodeURIComponent(sStr).replace(/\+/g,"%2B").replace(/\//g,"%2F");
    return sStr;
}

/** Initialize LivePR. */
function initLivePR() {
    var livepr = document.getElementById('livepr-status');
    var tooltip = document.getElementById('livepr-tooltip-value');
    
    var url = new String(window._content.document.location);
    //url = url.replace(/\?.*$/g,'?');
    //var datacenter = "dc.livepr.raketforskning.com";
    var datacenter = "toolbarqueries.google.com";

    //prefix = "allinurl:";
    prefix = "info:";
    var reqgr = prefix + url;
    var reqgre = prefix + cleanURL(url);
    var feat = "";
    if (prefix == "info:") {
        feat = "Rank";
    }
    var gch = GoogleNewCH(reqgre);

    var querystring = "http://"+datacenter+"/search?client=navclient-auto&ch="+gch+"&features="+feat+"&q="+reqgre+"&num=100&filter=0";

    var pattern1 = /^http:/;
    var pattern2 = /^http:\/\/.*google\..*\/(search|images|groups|news).*/;
    var pattern3 = /^http:\/\/localhost.*/;
    var pattern4 = /^http:\/\/(127\.|10\.|172\.16|192\.168).*/; //local ip
    var pattern5 = /^about:/;

    if (!lprenabled) { 
        // Do nothing.
    } else if (!pattern1.test(url) || 
        pattern2.test(url) ||
        pattern3.test(url) || 
        pattern4.test(url) ||
        pattern5.test(url)) {
        livepr.setAttribute("livepr", -1);
        tooltip.setAttribute("value", "PageRank: N/A");
        liveprLastPR = -1;
    } else {
        //dump("\n"+querystring+"\n");
        
        liveprreq = new XMLHttpRequest();
        liveprreq.onload = setLivePR;
        liveprreq.open("GET", querystring);
        liveprreq.setRequestHeader("User-Agent","Mozilla/4.0 (compatible; GoogleToolbar 2.0.114-big; Windows XP 5.1)");
        liveprreq.send(null);
    }
    liveprLastURL = GoogleNewCH(url);
}

/** Reset LivePR. */
function resetLivePR() {
    var livepr = document.getElementById('livepr-status');
    var tooltip = document.getElementById('livepr-tooltip-value');
    var url = new String(window._content.document.location);
    
    //url = url.replace(/\?.*$/g,'?');
    if (!lprenabled) {
        livepr.setAttribute("livepr", "disabled");
        tooltip.setAttribute("value", "Disabled");
    } else if (GoogleNewCH(url) == liveprLastURL) {
        livepr.setAttribute("livepr", liveprLastPR);
        if (liveprLastPR == -1) {
            tooltip.setAttribute("value", "PageRank: N/A");
        } else {
            tooltip.setAttribute("value", "PageRank: "+liveprLastPR);
        }
    } else {
        liveprLastURL = GoogleNewCH(url);
        initLivePR();
    }
}

/** Switch state. Controlled by popup menu. */
function switchliveprstate(event) {
    var LEFT = 0;
    var tooltip = document.getElementById('livepr-tooltip-value');
    var livepr = document.getElementById('livepr-status');
    var menuitem = document.getElementById('livepr-main-menu-state');

    if (event.button == LEFT) {
        if (!lprenabled) {
            gLivePRPref.setIntPref("state", 1);
            lprenabled = 1;
            initLivePR();
        } else {
            gLivePRPref.setIntPref("state", 0);
            lprenabled = 0;
            livepr.setAttribute("livepr", "disabled");
            tooltip.setAttribute("value", "Disabled");
        }
        liveprSavePreferences();
    }
}

function liveprAboutDialog() {
    window.open('chrome://livepr/content/about.xul','','chrome,centerscreen');
}

function liveprGoURL(url) {
    window.open(url);
}

function liveprSavePreferences() {
    // Enabled/disabled?
    try {
        gLivePRPref.setIntPref("state", lprenabled);
    }
    catch (err) {
        dump("\nSetting preference for state failed: " + err + "\n");
    }

    // Position
    try {
        gLivePRPref.setCharPref("parent_element_id", gLivePRParentElementID);
    }
    catch (err) {
        dump("\nSetting preference for parent_element_id failed: " + err + "\n");
    }
    try {
        gLivePRPref.setCharPref("insert_before_element_id", gLivePRInsertBeforeElementId);
    }
    catch (err) {
        dump("\nSetting preference for insert_before_element_id failed: " + err + "\n");
    }
    try {
        gLivePRPref.setCharPref("insert_after_element_id", gLivePRInsertAfterElementId);
    }
    catch (err) {
        dump("\nSetting preference for insert_after_element_id failed: " + err + "\n");
    }

    // Save settings.
    try {
        gLivePRPrefService.savePrefFile(null);
    }
    catch (err) {
        dump("\nSaving preferences failed: " + err + "\n");
    }
    //dump("Preferences saved.\n");
}

function liveprLoadPreferences() {
    try {
        lprenabled = gLivePRPref.getIntPref("state");
    }
    catch (err) {
        lprenabled = 1;
    }
    try {
        gLivePRParentElementID = gLivePRPref.getCharPref("parent_element_id");
    }
    catch (err) {
        gLivePRParentElementID = '';
    }
    try {
        gLivePRInsertBeforeElementId = gLivePRPref.getCharPref("insert_before_element_id");
    }
    catch (err) {
        gLivePRInsertBeforeElementId = '';
    }
    try {
        gLivePRInsertAfterElementId = gLivePRPref.getCharPref("insert_after_element_id");
    }
    catch (err) {
        gLivePRInsertAfterElementId = '';
    }
    //dump("Preferences loaded. Applying preferences.\n");
    liveprSetLivePRPosition();
}

/** Preferences */
var gLivePRPrefService = Components.classes["@mozilla.org/preferences-service;1"]
                         .getService(Components.interfaces.nsIPrefService);
var gLivePRPref = gLivePRPrefService.getBranch("livepr.");

/** Vars */
var lprenabled;
var liveprreq;
var liveprLastURL;
var liveprLastPR;

/** Listeners */
window.addEventListener("load",function() {liveprLoadPreferences(); initLivePR();},true);
window.addEventListener("focus",function() {resetLivePR();},true); 
/**
 * $Id: liveprReposition.js,v 1.3 2006/02/12 20:00:31 martin Exp $
 *
 * Drag n' drop repositioning
 * Most of this code is from FoxyTunes, see license below.
 * This script is part of the LivePR Firefox Extension.
 * It is licensed under the GPL. 
 * More details on <http://livepr.raketforskning.com/>.
 */

/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is FoxyTunes Mozilla Extension and Engine.
 * The Initial Developer of the Original Code is Alex Sirota <alex@elbrus.com>. 
 * Portions created by Alex Sirota are Copyright (C) 2004 Alex Sirota. All Rights Reserved.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

function liveprInstallDragDropObserversForElementById(elementId, bInstall) {
   var elem = document.getElementById(elementId);
   if (elem == null) {
	//dump("\nAdding observer, cannot find element: " + elementId + "\n");
	return;
   }

   if (bInstall) {
  	elem.addEventListener("dragover", liveprOnTargetDragOver, false);
	elem.addEventListener("dragexit", liveprOnTargetDragExit, false);
	elem.addEventListener("dragdrop", liveprOnTargetDragDrop, false);
   } else {
  	elem.removeEventListener("dragover", liveprOnTargetDragOver, false);
	elem.removeEventListener("dragexit", liveprOnTargetDragExit, false);
	elem.removeEventListener("dragdrop", liveprOnTargetDragDrop, false);
   }
}

function liveprInstallUninstallDragDropObservers(bInstall) {
  liveprInstallDragDropObserversForElementById('status-bar', bInstall);

  // all the toolboxes:
  var toolboxes = document.getElementsByTagName('toolbox');
  for (var i = 0; i < toolboxes.length; i++) {
 	liveprInstallDragDropObserversForElementById(toolboxes[i].id, bInstall);
  }

  // liveprInstallDragDropObserversForElementById('navigator-toolbox', bInstall);
}

function liveprInstallDragDropObservers() {
   //dump("installing listeners for livepr");
   liveprInstallUninstallDragDropObservers(true);
}

function liveprUnInstallDragDropObservers() {
   liveprInstallUninstallDragDropObservers(false);
}


function liveprHasDropClass(className) {
  var classNames = className.split(" ");
  for (var i = 0; i < classNames.length; i++) {
	if (classNames[i].indexOf('livepr-drop-target-') != -1) {
		return true;
	}
  }
  return false;
}

function liveprRemoveDropClass(className) {
  var classNames = className.split(" ");

  if (classNames.length < 1) {
  	return className;
  }

  // see if the last class is LivePR drop class:
  if (classNames[classNames.length - 1].indexOf('livepr-drop-target-') != -1) {
	classNames.length--; // truncate the array
        if (classNames.length > 0) {
		className = classNames.join(" ");
	} else {
		className = "";
	}
  } 
  return className;
}

function liveprElementIsToolbarOrStatusbar(elem) {
  return (elem.localName == "toolbar") || (elem.localName == "statusbar") || (elem.localName == "menubar");
}

function liveprSetDropTargetMarker(node, bSet) {
  var target = node;
  var side = 'left';
  if (liveprElementIsToolbarOrStatusbar(node)) {
    target = node.lastChild;
    side = "right";
  }
  
  if (target == null) {
  	return;
  }

  if (bSet) {
	if (!liveprHasDropClass(target.className)) {
		var dropClassName = 'livepr-drop-target-' + side;
		target.className = target.className + " " + dropClassName;
		//dump("\n new Class " + target.className + "\n");
	}
  } else {
	target.className = liveprRemoveDropClass(target.className);
	//dump("\nRestored Class " + target.className + "\n");
  } 
}


function liveprOnTargetDragOver(event)
{
  nsDragAndDrop.dragOver(event, liveprTargetObserver);
}

function liveprOnTargetDragExit(event)
{
   if (gLivePRCurrentDropTarget != null) {
	   liveprSetDropTargetMarker(gLivePRCurrentDropTarget, false);
   }
}

function liveprOnTargetDragDrop(event)
{
  nsDragAndDrop.drop(event, liveprTargetObserver);
}

var liveprDragStartObserver =
{
  onDragStart: function (event, transferData, action) {
	liveprInstallDragDropObservers();
	transferData.data = new TransferData();
	transferData.data.addDataForFlavour('id/livepr-widget', 'livepr-status');
  }
}


var liveprTargetObserver =
{
  onDragOver: function (event, flavour, session)
  {
   //dump("over\n");
   var topElement = event.target;
   var target = event.target;
   while (topElement && !liveprElementIsToolbarOrStatusbar(topElement)) {
	      target = topElement;
	      topElement = topElement.parentNode;
   } 
   
   var previousDragItem = gLivePRCurrentDropTarget;

   if (liveprElementIsToolbarOrStatusbar(target)) {
	 gLivePRCurrentDropTarget = target;
   } else {
      	 var targetWidth = target.boxObject.width;
         var targetX = target.boxObject.x;

         gLivePRCurrentDropTarget = null;
         if (event.clientX > (targetX + (targetWidth / 2))) {
	        gLivePRCurrentDropTarget = target.nextSibling;
		//dump("\ncrossed\n");
        	if (gLivePRCurrentDropTarget == null) {
			  // last element in its parent, set target to parent
		          gLivePRCurrentDropTarget = topElement;
		}
      	 } else {
        	gLivePRCurrentDropTarget = target;
    	 }    
   }

   //dump("\nprev: " + previousDragItem.id + ", next: " + gLivePRCurrentDropTarget.id + "\n");
   if (previousDragItem && (gLivePRCurrentDropTarget != previousDragItem)) {
	liveprSetDropTargetMarker(previousDragItem, false);
   }
 
   if (gLivePRCurrentDropTarget.id.indexOf('livepr') == -1) { 
	   liveprSetDropTargetMarker(gLivePRCurrentDropTarget, true);
	   session.canDrop = true;
   } else {
   	   // cannot drop on myself: 
	   liveprSetDropTargetMarker(gLivePRCurrentDropTarget, false);
	   gLivePRCurrentDropTarget = null;
	   session.canDrop = false;
   }
  },
 
  onDragExit: function (event, session) 
  {
	//dump("exit\n");
  },

  onDrop: function (event, dropData, session)
  {
	//dump("drop\n");
	liveprUnInstallDragDropObservers();
        if (gLivePRCurrentDropTarget == null) {
		return; 
	}
        liveprSetDropTargetMarker(gLivePRCurrentDropTarget, false);
    
	var draggedItemId = dropData.data;
	// sanity, should never happen:
	if (gLivePRCurrentDropTarget.id == draggedItemId) {
	      	return;
	}

	var topElement = event.target;
        while (topElement && !liveprElementIsToolbarOrStatusbar(topElement)) {
	      topElement = topElement.parentNode;
        }

	// save the new settings:
	gLivePRParentElementID = topElement.id;
	gLivePRInsertBeforeElementId = gLivePRCurrentDropTarget.id;

	// for the case when the "insert before" element is a dynamic one, remember the 
	// "insert after" element
	if (gLivePRCurrentDropTarget.previousSibling) {	
	 	gLivePRInsertAfterElementId = gLivePRCurrentDropTarget.previousSibling.id;
	}
        //dump("writing prefs");
	liveprSavePreferences();
        //dump("setting pos");	
	liveprSetLivePRPosition();

	//dump("\nInserted to: " + topElement.id + ", before " + gLivePRCurrentDropTarget.id + "\n");
	gLivePRCurrentDropTarget = null;
  },
  
  
  getSupportedFlavours: function ()
  {
	var flavours = new FlavourSet();
	flavours.appendFlavour("id/livepr-widget");
	return flavours;
  }
}


function liveprRenameTagName(elem, newTagName) {
	var newElem = document.createElement(newTagName);

	// copy all the attributes of the element
	for (var i=0; i < elem.attributes.length; i++) {
		newElem.setAttribute(elem.attributes[i].nodeName, elem.attributes[i].nodeValue);
	}

	// move all the children
	var children = elem.childNodes;
	for (var i=children.length-1; i >=0 ; i--) {
        var currentNode = children[i];
		elem.removeChild(currentNode);
		newElem.insertBefore(currentNode, newElem.firstChild);
	} 

  	return newElem;	
}

function liveprSetLivePRPosition() {
    	if ((gLivePRParentElementID == '') ||
		(gLivePRInsertBeforeElementId == '')) {
			// nothing's set, return
			return;
	}

	var liveprWidget = document.getElementById('livepr-status');

	var parentElement = document.getElementById(gLivePRParentElementID);
	if (parentElement == null) {
		return;
	}
	//dump("\nParent element: " + gLivePRParentElementID + "\n");

	var insertBeforeElement = document.getElementById(gLivePRInsertBeforeElementId);
	var insertAfterElement = document.getElementById(gLivePRInsertAfterElementId);

	if ((insertBeforeElement == null) && (insertAfterElement == null)) {
		return;
	}

	//dump("\nInsert before element: " + gLivePRInsertBeforeElementId + "\n");
	//dump("\nInsert after element: " + gLivePRInsertAfterElementId + "\n");

	var oldParentNode = liveprWidget.parentNode;

	liveprWidget.parentNode.removeChild(liveprWidget);

	try {
		// make LivePR toolbaritem and not statubarpanel if needed:
		if ((parentElement.localName == 'toolbar') && (liveprWidget.localName == 'statusbarpanel')) {
			liveprWidget = liveprRenameTagName(liveprWidget, 'toolbaritem');
		}

		// make LivePR statusbarpanel and not toolbaritem if needed:
		if ((parentElement.localName == 'statusbar') && (liveprWidget.localName == 'toolbaritem')) {
			liveprWidget = liveprRenameTagName(liveprWidget, 'statusbarpanel');
		}

		// convention, if the parent equals insertbefore, insert as last
		if (parentElement != insertBeforeElement) {
			if (insertBeforeElement) {
				parentElement.insertBefore(liveprWidget, insertBeforeElement);
			} else {
				//dump('insert before failed try inserting after ' + gLivePRInsertAfterElementId + '\n');
				if (insertAfterElement.nextSibling) {
					parentElement.insertBefore(liveprWidget, insertAfterElement.nextSibling);
				} else {
					parentElement.appendChild(liveprWidget);
				}
			}
		} else {
			parentElement.appendChild(liveprWidget);
		}

	} catch (err) {
		//dump("\nCouldn't reposition LivePR: " + err + "\n");
		oldParentNode.appendChild(liveprWidget);
	}
}

// the current drop target:
var gLivePRCurrentDropTarget = null;




function one(event) { switchliveprstate(event);
}
document.addEventListener("command", one, false);

function two(event) { nsDragAndDrop.startDrag(event, liveprDragStartObserver)
}
document.addEventListener("command", two, false);

function three(event) { switchliveprstate();
}
document.addEventListener("command", three, false);

function four(event) { liveprAboutDialog();
}
document.addEventListener("command", four, false);

function five(event) { liveprGoURL('http://livepr.raketforskning.com/');
}
document.addEventListener("command", five, false);

function six(event) { liveprGoURL('http://livepr.raketforskning.com/about.html');
}
document.addEventListener("command", six, false);

function seven(event) { liveprGoURL('http://livepr.raketforskning.com/how-live-pagerank-is-calculated.html');
}
document.addEventListener("command", seven, false);

function eight(event) { liveprGoURL('http://livepr.raketforskning.com/livepr-indicator-for-your-website.html');
}
document.addEventListener("command", eight, false);

function nine(event) { liveprGoURL('http://www.raketforskning.com/');
}
document.addEventListener("command", nine, false);

function ten(event) { sizeToContent()
}
document.addEventListener("command", ten, false);

function eleven(event) { this.style.cursor='pointer';
}
document.addEventListener("command", eleven, false);

function twelve(event) { window.open('http://www.raketforskning.com/'); window.close();
}
document.addEventListener("command", twelve, false);

function thriteen(event) { this.style.cursor='pointer';
}
document.addEventListener("command", thriteen, false);

function fourteen(event) { window.open('http://livepr.raketforskning.com/'); window.close();
}
document.addEventListener("command", fourteen, false);

function fifteen(event) { window.close();
}
document.addEventListener("command", fifteen, false);

print("Before event loop");

while (true) {
  globalFuncList[TopNum](__evtObj);
}