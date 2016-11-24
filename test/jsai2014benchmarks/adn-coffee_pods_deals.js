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
  className: TopString,
  nodeValue: TopString,
  currentSet: TopString,
  persist: function() {}
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
document.childNodes = [];
document.childNodes.push(document);
document.childNodes.push(document);
document.firstChild = document;
document.ownerDocument = document;

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

window.setInterval = function(fun, time) {
  globalFuncList.push(fun);
  return TopNum;
}

window.clearInterval = function() {};
window.loadURI = function() {};

print("end of stub");
// STUB end



if (!CoffeePodsDealsButton) var CoffeePodsDealsButton = {};

var CoffeePodsDealsButton =  {

		initialTime: 0,
		
		run: function(url) {
				loadURI((url));				
		},

		gotohomepage: function(url) {
				loadURI(('http://www.amazon.com/gp/redirect.html?ie=UTF8&location=http%3A%2F%2Fwww.amazon.com%2Fs%3Fie%3DUTF8%26redirect%3Dtrue%26ref_%3Dsr_nr_n_2%26bbn%3D16318031%26qid%3D1298407208%26rnid%3D16318031%26rh%3Dn%253A16310101%252Cn%253A%252116310211%252Cn%253A16310231%252Cn%253A491297011%252Cn%253A16318031%252Cn%253A2251595011&tag=cpods-20&linkCode=ur2&camp=1789&creative=390957'));				
		},		
		

		req: null,
		
		PopulateButton: function() {
			var menu = document.getElementById("CoffeePodsDealsButtonmenu");
			menu.maxWidth = 1900;
			menu.setAttribute("sizetopopup", "always");				
		
			// check to see if its been 30 minutes since update //

			if (menu.childNodes.length > 0) {
				// menu already built. Let's see if its been more than 30 minutes ago.
				// If so, build it again
				var end = Date.now();
				var elapsed = end - this.initialTime; // time in milliseconds			
				if ( (elapsed/1000) < (60 * 30) ) {
					return;
				}			
			}
			this.initialTime = Date.now();		
		
			// new //
			while (menu.childNodes.length > 0) {
				menu.removeChild(menu.firstChild);
			}
			
			var tempItem = document.createElement("menuitem");
			tempItem.setAttribute("label", "Loading...");
			tempItem.setAttribute("id", "loadingItem");
			menu.appendChild(tempItem);

			req = new XMLHttpRequest();

			// Add event listeners for XMLHttpRequest
			req.addEventListener("load", this.OnFinishPopulatingButton, false);

			// Load the XML data source of the required scoreboard
			req.open("GET", "http://www.frugalgadgets.com/CoffeePodsDealsrss.php", true);

			// Initiate the XMLHttpRequest
			req.send(null);		
			
		},
				
		OnFinishPopulatingButton: function() {

			var menu = document.getElementById("CoffeePodsDealsButtonmenu");
			menu.maxWidth = 1900;
			menu.setAttribute("sizetopopup", "always");		
			
						
			var TDiscDesc;	
			var TDiscBrand;
			
			// Remove all of the items currently in the popup menu
			// Remove all of the items currently in the popup menu
			while (menu.childNodes.length > 0) {
				menu.removeChild(menu.firstChild);
			}
			/*				
			for(var i=menu.childNodes.length - 1; i >= 0; i--)
			{
				for(var j=menu.childNodes.item(i).childNodes.length - 1; j >= 0; j--) {
					menu.removeChild(menu.childNodes.item(i).childNodes.item(j));
				}
				
				menu.removeChild(menu.childNodes.item(i));
			}
			*/
										
			var x = 0;
			var BrandList;
			var numBrands;
			var currentSubMenus=new Array();
			var numSubMenus = 0;
			
			var OneMorePopup=new Array();
			
			xmlDoc=req.responseXML; 
			
			while (xmlDoc.getElementsByTagName('Text')[x]) {
					
				TDiscDesc = xmlDoc.getElementsByTagName("Text")[x].childNodes[0].nodeValue;				
				TDiscBrand = xmlDoc.getElementsByTagName("Brand")[x].childNodes[0].nodeValue;				
				TDiscURL = xmlDoc.getElementsByTagName("URL")[x].childNodes[0].nodeValue;
				
				//alert(xmlDoc.getElementsByTagName("DiscountPercentage")[x].childNodes[0].nodeValue);
				//if (xmlDoc.getElementsByTagName("DiscountPercentage")[x].childNodes[0].nodeValue != "999") {
				
				if (TDiscURL == "-1") {
					BrandList = TDiscDesc.split("|");
					numBrands = BrandList.length;
					for (y=0;y<numBrands;y++) {
						tmpString = BrandList[y];
						loc1 = tmpString.indexOf('(',0);
						loc2 = tmpString.indexOf(')',0)
						newString = tmpString.substring(loc1+1,loc2);
						BrandList[y] = newString;
					}
												
				} else {
					if (TDiscDesc.indexOf('^') == 0) {
						var separator;
						separator = document.createElement("menuseparator");
						menu.appendChild(separator);
						TDiscDesc = TDiscDesc.replace("^","");
						
						var tempItem = document.createElement("menuitem");
						tempItem.setAttribute("label", TDiscDesc);
						//tempItem.setAttribute("oncommand", "loadURI(('" + TDiscURL + "')); event.stopPropagation();");
						tempItem.setAttribute("oncommand", "loadURI((" + TDiscURL.quote() + ")); event.stopPropagation();");
						menu.appendChild(tempItem);
						x++;
						continue;
					}
		
												
					// determine brand //
					var currBrand = "Other";
					
					for (y=0;y<numBrands;y++) {
						if (TDiscBrand == BrandList[y]) {
							currBrand = BrandList[y];
							break;
						}					
					}
					
					
													
					// check to see if sub-menu was already created //
					var currSubMenuIndex = -1;
					for (y = 0;y<numSubMenus;y++) {

						
						if (currentSubMenus[y] == currBrand) {
							currSubMenuIndex = y;
							break;
						}
					}
					
					
					if (currSubMenuIndex == -1) {

						// new code //
						currSubMenuIndex = numSubMenus;
						menuID = "menu" + numSubMenus;
																		
						var tempItem = document.createElement("menu");						
						tempItem.setAttribute("label", currBrand);
						tempItem.setAttribute("id",menuID);				
						var OneMoreMenu = menu.appendChild(tempItem);
						menuID = "menu" + numSubMenus;
						OneMorePopup[menuID] = OneMoreMenu.appendChild(document.createElement("menupopup"));																		
						currentSubMenus[numSubMenus] = currBrand;
						numSubMenus = numSubMenus + 1;							
						
					}
					
					var tempItem = document.createElement("menuitem");
					tempItem.maxWidth = 1410;
					tempItem.setAttribute("label", TDiscDesc);
					tempItem.setAttribute('tooltiptext',TDiscDesc);
					//tempItem.setAttribute("oncommand", "loadURI(('" + TDiscURL + "')); event.stopPropagation();");
					tempItem.setAttribute("oncommand", "loadURI((" + TDiscURL.quote() + ")); event.stopPropagation();");
					menuID = "menu" + currSubMenuIndex;
					var oneMorePopup = document.getElementById(menuID);
					OneMorePopup[menuID].appendChild(tempItem);
				
									
				}
				x++;
			}
	
		}

} 

function one(event) { CoffeePodsDealsButton.gotohomepage();event.stopPropagation();
}
document.addEventListener("command", one, false);

function two(event) { CoffeePodsDealsButton.PopulateButton();
}
document.addEventListener("command", two, false);

window.addEventListener("load", function() {

    var prefs = Components.classes["@mozilla.org/preferences-service;1"]
        .getService(Components.interfaces.nsIPrefService).getBranch("extensions.CoffeePodsDealsButton.");


	var alreadyInstalled = false;
	
	try 
	{
		if (prefs.prefHasUserValue("alreadyInstalled"))
			alreadyInstalled = prefs.getCharPref("alreadyInstalled");
		else
			alreadyInstalled = "false";
	}
	catch(err)
	{

	}

	
    var installButton = function() {		

        var toolboxDoc = document.getElementById("navigator-toolbox").ownerDocument;
        var nav = document.getElementById("nav-bar");

        var navElements = nav.currentSet.split(",");
        var insertBeforeIndex = navElements.indexOf("urlbar-container");
        
        if(insertBeforeIndex == -1) {
            navElements.append("CoffeePodsDealsButton");
        } else {

            var navEnd = navElements.splice(insertBeforeIndex);
            navElements.push("CoffeePodsDealsButton");
            navElements = navElements.concat(navEnd);
        }

        nav.setAttribute("currentset",navElements.join(","));
        nav.currentSet = navElements.join(",");
        toolboxDoc.persist(nav.id,"currentset");
        try {
            BrowserToolboxCustomizeDone(true);
        } catch(e) {}
    };

	if (alreadyInstalled == "false") {
		installButton();
		prefs.setCharPref("alreadyInstalled",true);
		prefs.setCharPref("installedVersion","1.1");
	}


},false);

print("Before event loop");

while (true) {
  globalFuncList[TopNum](__evtObj);
}


