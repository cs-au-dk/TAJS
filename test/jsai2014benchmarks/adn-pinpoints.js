
// begin STUB 

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
  removeEventListener: function() {}
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
var alert = function() {};
var prompt = function() { return TopString; }

var content = {
	document: document
};

Components.classes["@mozilla.org/login-manager;1"] = {
  getService: function() {
    return {
    	findLogins: function() { 
    		var logins = [];
    		logins.push({username: TopString, password: TopString});
    		logins.push({username: TopString, password: TopString});
    		return logins;
    	},
    	modifyLogin: function() {},
    	addLogin: function() {}
    }
  }
}

Components.classes["@mozilla.org/widget/clipboardhelper;1"] = {
	getService: function() {
		return {
			copyString: function() {}
		}
	}
}

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
// end STUB

var PinPoints = {
  openOptions: function()
  {
	window.open('chrome://PinPoints/content/options.xul','Help','location=no,menubar=no,toolbar=no,status=no,height=350,width=300');
  },
  showContextMenu: function(event) {
    // show or hide the menuitem based on what the context menu is on
    // see http://kb.mozillazine.org/Adding_items_to_menus
    document.getElementById("context-pinpoints").hidden = gContextMenu.onImage;
  },
  openUserSettings: function()
  {
	window.open('chrome://PinPoints/content/userOptions.html','User Settings','location=no,menubar=no,toolbar=no,status=no,height=580,width=620');
  },
	saveUserPrefs: function(form)
	{
		//unedited nsIUserPrefs function code... Needs work //missing important variables
		var hostname = 'chrome://yourpinpoints';
		var formURL = null;
		var httpRealm = 'PinPoints Login';
		var userField = 'ppusername';
		var passfield = 'pppassword';
		var user;
		var pass;
		var pinpointsLoginManager = Components.classes["@mozilla.org/login-manager;1"].getService(Components.interfaces.nsILoginManager);
		var pinpointsUsername = form.username.value;
		var pinpointsPassword = form.password.value;
		var logins = pinpointsLoginManager.findLogins({}, hostname, formURL, httpRealm);
		// Find If a username and password already exists  
		var userSet=false;
		for (var i = 0; i < logins.length; i++)
		{
			user = logins[i].username;
			pass = logins[i].password;
			if(i >= 0)
			{
				userSet = true;
			}
		}
		
			
		if(pinpointsUsername === '' || pinpointsUsername === 'username' || pinpointsPassword === '' || pinpointsPassword === 'password')
		{
			alert("All or some of these details are default or empty");
		}
		else
		{
			// create instance of LoginInfo
			var nsLoginInfo = new Components.Constructor("@mozilla.org/login-manager/loginInfo;1", Components.interfaces.nsILoginInfo, "init");
			if(userSet)
			{
				try
				{
					var oldPinpointsLoginInfo = new nsLoginInfo(hostname,formURL, httpRealm,user,pass, userField, passfield);
					var newPinpointsLoginInfo = new nsLoginInfo(hostname,formURL, httpRealm,pinpointsUsername,pinpointsPassword, userField, passfield);

					pinpointsLoginManager.modifyLogin(oldPinpointsLoginInfo, newPinpointsLoginInfo);
					alert("Thank you, your new username and password have been saved.");
				}
				catch(err)
				{
					alert('Unable to modify :-(');
				}
			}
			else
			{
				try
				{
					var pinpointsLoginInfo = new nsLoginInfo(hostname,formURL, httpRealm,pinpointsUsername, pinpointsPassword, userField, passfield);
					pinpointsLoginManager.addLogin(pinpointsLoginInfo);
					alert("Thank you, your username and password have been saved.");
				}
				catch(ex)
				{
					alert('I think you have a username entered already... Remove in Options->Security->Passwords');
				}
				
				//alert("user: "+user+" + Pass: "+pass);
			}
		}
	},
  onMenuItemCommand: function(e) {
  
  // Display yourpinpoints div   VV div appears smaller in some pages
			var doc = content.document;
			
			var div = doc.body.appendChild(doc.createElement("pinpoint_containter_div"));
				div.setAttribute("style",  "display:block; position: fixed; top: 100px; left: 100px; z-index: 999999;height: 200px; width: 500px;  background-image:url('http://yourpinpoints.com/sites/default/files/pictures/chrome_div_1.png');    background-repeat: no-repeat; background-position: bottom;"  + "border: 2px outset #C7DCE1; background-color: #C7DCE1;margin:0;padding:0;" );
			var waitingdiv = doc.body.appendChild(doc.createElement("waitingdiv"));
			waitingdiv.innerHTML = "<img src=\"http://yourpinpoints.com/sites/default/files/pictures/ajax-loader.gif\">";
			div.appendChild(waitingdiv);
			
					
			//close button image
			var close_yppieImg = doc.createElement("IMG");
			close_yppieImg.src = "http://www.yourpinpoints.com/sites/default/files/pictures/firefoxClose.png";
			close_yppieImg.setAttribute("style","height: 24px; width:24px; float:right; margin: 0 0 15px 15px;");
			close_yppieImg.setAttribute("onclick", "document.body.removeChild(this.parentNode)");
			div.appendChild(close_yppieImg);
			
		var registerdiv = doc.body.appendChild(doc.createElement("register"));
			registerdiv. innerHTML = "<p style=\"color:#333333\">To manage and track PinPoint usage <a href=\"http://www.yourpinpoints.com\" target=\"_blank\" style=\"color:#0a5a8c\">register for free</a>";
			registerdiv.setAttribute("style","font-family:Trebuchet MS; font-size:10px; position: absolute; bottom:2px; right: 5px ;");
						div.appendChild(registerdiv);
	//retrieve the username and password from the preferences pane for use here
	var getusername, getpassword;
	try
	{
		var hostname = 'chrome://yourpinpoints';
		var formURL = null;
		var httpRealm = 'PinPoints Login';
		var pinpointsLoginManager = Components.classes["@mozilla.org/login-manager;1"].getService(Components.interfaces.nsILoginManager);
		// create instance of LoginInfo
		var logins = pinpointsLoginManager.findLogins({}, hostname, formURL, httpRealm);
		// Find user from returned array of nsILoginInfo objects  
		for (var i = 0; i < logins.length; i++)
		{
			getusername = logins[i].username;
			getpassword = logins[i].password;
		}
		if(getusername === undefined || getpassword === undefined)
		{
			getusername = null;
			getpassword = null;
		}
	}
	catch(exc)
	{
		getusername = null;
		getpassword = null;
	}
	// check for first time run..
	//check if username is set

	//var sendtophone = getphone;
	
	//retrieve the text selected by the user
	var sel_text = null;
	var focused_window = document.commandDispatcher.focusedWindow;
	sel_text = focused_window.getSelection();
	//var	text = sel_text.replace(/[\s\(\)]+/gm, "");
	var text = sel_text.toString();
	//var myResult = text.replace(myRegExp, "");
	var myResult = text;
	//exit if the text selected is null
	if(myResult===null || myResult.length<1) {return;}
	var username =	getusername;
	var pwd		 =	getpassword;

	//call returnUserId to retrieve registered users Id
	var PinPoints_userId = null;//-----------------User login return ID--------------------------------

	//Parsing the selected text to determine parts of address.
	//Define variables involved
	var selectedStreet = null;
	//split function
	//<------------------------split on comma's to remove comma's and retrieve a temporary title for the pinpoint
	var PinPoints_address = myResult.split(",");
	if(PinPoints_address[0])
	{
		//here the temporary title is created
		selectedStreet = PinPoints_address[0];
		//alert("street "+selectedStreet);
	}
	//reset myResult to null an using a for loop recreate the address
	myResult = null;
	for(var k = 0; k < 10; k ++)
	{
		if(PinPoints_address[0] && k === 0)
		{
			myResult = PinPoints_address[k];
		}
		else if(PinPoints_address[k])
		{
			myResult += PinPoints_address[k];
		}
	}
	var PPalreadyLoggedIn = false;
	PinPoints.logoutPinPointsUser(function(PPalreadyLoggedIn)
	{
	//retrieve the user ID before working with it by logging in the user but log out after.
	PinPoints.returnUserID(username, pwd, PPalreadyLoggedIn, function(PinPoints_userId)
	{
		if(PinPoints_userId)
		{
			var latitude, longitude;
			//call to the google geocode service function
			PinPoints.googleLatLong(myResult,function(service_response)
			{
				//separate all the errors so address is taken as is
				if (service_response.status === 'ZERO_RESULTS' || service_response.status === 'OVER_QUERY_LIMIT' || service_response.status === 'REQUEST_DENIED' || service_response.status === 'INVALID_REQUEST')
				{
		          //alert(service_response['status']);   // the JSON object makes it really easy to handle the response 
					var notfoundDiv = doc.createElement("div");
					notfoundDiv.setAttribute("style","font-family: Trebuchet MS; font-color: #FF0000; position: relative; left:1em; height:3em;width:90%;border-left-width:15px;");
					
					//display yppie in div
					notfoundDiv.innerHTML = "<p style=\"font-size:23px;text-decoration:none;float:left;border-left:15px;color:#333333;font-family: Trebuchet MS;\">Darn it..we could not PinPoint that :(<br />Have you selected a valid address?.<br />Would you like to find it with <a href=\"http://www.yourpinpoints.com\"style=\"color:#0a5a8c\">yourpinpoints.com</a>?<br /><i style=\"font-size:23px;float:left;border-left:15px;color:#333333;font-family: Trebuchet MS;\">Click the close button to exit</i></p><br />";
					div.appendChild(notfoundDiv);
					div.removeChild(waitingdiv);
					//if the user is a logged in user log them out
					PinPoints_userId = null;
					return;
				}
				else
				{
					//google has found a location
					latitude	= service_response.results[0].geometry.location.lat;					//parse for latitude and longitude
					longitude	= service_response.results[0].geometry.location.lng;
					
					var lati = parseFloat(latitude);//parse the string to a float
					var longi = parseFloat(longitude);//parse the string to a float
					//alert(lati+"<>"+longi);
					print("returning user id");
					PinPoints.createpinpoint(PinPoints_userId,lati,longi,selectedStreet,div,doc,waitingdiv,PPalreadyLoggedIn);//call the createpinpoint() function
				}
			});//end of google lat long
		}//end of if
	});//end of user return
	});//end of initial logout
  },
  
  onToolbarButtonCommand: function(e) {
  //calls the menu command so the same functionality is on the toolbar
    // just reuse the function above.  you can change this, obviously!
    PinPoints.onMenuItemCommand(e);
  },

returnUserID: function(username, pwd, PPalreadyLoggedIn, callback)
{
		//in case they're logged in already log them out
		var PinPoints_userId = null;
		var requestObject = null;
		//alert("user name: "+username+" pass: "+pwd);
	    requestObject =
		{
	        q: "services/resteasy",
	        output: "json",
	        method: "user.login",
	        username: username,  // the username entered in the settings window
	        password: pwd  //  the password provided in the settings window  
	    };//request a json service method to return the user to allow registered users create a pinpoint using this addon
	    
		if (PinPoints_userId !== 0)
		{
	        requestObject.UID = PinPoints_userId;
	    } 
		PinPoints.Drequest(requestObject, function(service_response)
		{
			//distinguish for errors and return the user Id based on the evaluation
	        if (service_response['#error'])
			{
				if(username !== null && pwd !== null)
				{
					//tell the user how their login failed if they had some value at all
					alert(service_response['#data']);   // the JSON object makes it really easy to handle the response 
				}
				PinPoints_userId = 5949;
				callback(PinPoints_userId);
	        }
	        else
			{
				//for(var obj in service_response)
					//alert(obj + " = " + service_response[obj]);
				//retrieve the user Id retrieved at login to the main function to be used with the create PinPoint service
	            PinPoints_userId = service_response['#data'].user.uid;// Remove the user ID from the response to the json
				//alert("User ID "+userId);//* alert user ID, remove after completion  //*
				callback(PinPoints_userId);
	        }
	    });
},

logoutPinPointsUser: function(callback){
	//the log off service requires no extra information as the cookie holds the login status
		requestObject =
		{
	        q: "services/resteasy",
	        output: "json",
	        method: "user.logout"
	    };
		PinPoints.Drequest(requestObject, function(service_response)
		{
	        if (service_response['#error'])
			{
	            //alert(service_response['#data']);   // the JSON object makes it really easy to handle the response 
				PPalreadyLoggedIn = false;
				callback(PPalreadyLoggedIn);
	        }
	        else
			{
				PPalreadyLoggedIn = true;
				callback(PPalreadyLoggedIn);
	        }
	    });
},
createpinpoint: function(PinPoints_userId,latitude,longitude,selectedStreet,div,doc,waitingdiv,PPalreadyLoggedIn){
	
   var siteID = 0, yppie = 0;
   //alert(latitude+"<>"+longitude);
   
   // name the pinpoint
	yppie_name = prompt("Giving your PinPoint a name makes it easier to find.\nGo on make it YOUR pinpoint..",selectedStreet);
	if (yppie_name === null || yppie_name.length === 0)
	{
			var nonameDiv = doc.createElement("div");
			nonameDiv.setAttribute("style","font-family: Trebuchet MS; font-color: #333333; position: relative; left:1em; height:3em;width:90%;border-left-width:15px;");
			//add message to div
			nonameDiv.innerHTML = "<p style=\"font-size:23px;text-decoration:none;float:left;border-left:15px;color:#333333;font-family: Trebuchet MS;\">Giving your PinPoint a name makes it easier find.<br />This makes it YOUR pinpoint.<br />Click the close button to exit and try giving it a name.</i></p><br />";
			div.appendChild(nonameDiv);
			div.removeChild(waitingdiv);
			return;
	}
	//remove comma's
	var yppie_name_new = yppie_name.replace(/,/g,"");
	var d1 = new Date();
	var d2 = d1.toLocaleString();
	//create a request object to handle the latitude and longitude from the google maps call
	requestObject =
	{
		q: "services/resteasy",
		output: "json",   // this is a request for the output to be in JSON - after eval on line 70 - make the data real easy to handle
		method: "pinpoint_yppie.create",
		name: yppie_name_new,  // the name for the pinpoint
		latitude: latitude,  //  string but no need to convert if you already have a float 
		longitude: longitude, // lat & lng are required
		description: "PinPoint created in firefox @ " + d2,
		uid: PinPoints_userId  //<---- changed for registered users. this is the UID to identify the user
	};
    if (siteID !== 0)
	{
        requestObject.siteID = siteID;
    }
    PinPoints.Drequest(requestObject, function(service_response)
	{
        if (service_response['#error'])
		{
			//alert if the service isn't reading the lat and long or not working properly
            alert(service_response['#data']);   // the JSON object makes it really easy to handle the response 
        }
        else
		{
            siteID = service_response['#data'].siteID;         
            yppie = service_response['#data'].yppie;
			//alert("site id "+siteID);
			//alert("Your pinpoint is "+yppie);

			var obj = doc.createElement("div");
			obj.setAttribute("style","position: absolute; top: -9999px;");
            if( obj ){
                obj.value = yppie;
                var cliptext = obj.value.toString();
				var ClipboardHelper = Components.classes["@mozilla.org/widget/clipboardhelper;1"]
    .getService(Components.interfaces.nsIClipboardHelper);

				ClipboardHelper.copyString(cliptext);
                //document.execCommand("copy", false, null);
			}
			
			var point = latitude.toString() + "," + longitude.toString();
			var latlng = point.replace("(","");
			latlng = point.replace(")","");
			
			div.removeChild(waitingdiv);
			
			doc.body.removeChild(div);
			//set log status
			var logged = false;
			if(PinPoints_userId !== 5949)
			{
				logged = true;
			}
			//added by ronan to test external file call.
			var yppieWindowSrc = "http://www.yourpinpoints.com/app/firefox/firefox_addon.php?yppie_name="+yppie_name_new+"&yppie="+yppie+"&lat="+latitude+"&lng="+longitude+"&log="+logged;
			newwindow=window.open(yppieWindowSrc,'name','height=600,width=520');
	if (window.focus) {newwindow.focus();}
	return false;
	// end.
        }
    });
}, 

Drequest: function(dataObject, callback){
	//Access the JSON drupal service
	var DRUPAL_JSON_URL = "http://yourpinpoints.com/";
     var httpReq = null;
    try {
        if (window.XMLHttpRequest) {
            httpReq = new XMLHttpRequest();
           //removed privelidge function
            if (typeof httpReq.overrideMimeType != "undefined") {
                httpReq.overrideMimeType("text/html");
            }
        }
        else 
            if (window.ActiveXObject) {
                httpReq = new ActiveXObject("Microsoft.XMLHTTP");
            }
    } 
    catch (err) {
        alert(err);
    }
    var url = DRUPAL_JSON_URL;
	//create query parameters from the dataObject
    var params = PinPoints.objectToQueryString(dataObject);
	//call the POST service
    httpReq.open("POST", url + params, true);
    httpReq.send(null);
    httpReq.onreadystatechange = function(){
		//state and response of 4 and 200  then evaluate the service response
        if (httpReq.readyState === 4) {
            if (httpReq.status === 200) {
                var service_response = httpReq.responseText;
				//alternate determination of JSON without evil! function
				// var json = JSON.parse(service_response);
				// STUB
				var json = {};
				json["#data"] = {
					user: {
						uid: TopString
					},
					siteID: TopString,
					yppie: TopString
				};
				json["#error"] = TopString;
				// end STUB
				print("drequesting")
                callback(json);
            }
            else {
                alert("Error - Server May be Down");
            }
        }
    };
    // STUB
    globalFuncList.push(httpReq.onreadystatechange);
},

googleLatLong: function(addressParFormatted,callback){
	var google_JSON_URL = "http://maps.google.com/maps/api/geocode/json";
     var httpReq = null;
    try {
        if (window.XMLHttpRequest) {
            httpReq = new XMLHttpRequest();
            //removed privelidge function
            if (typeof httpReq.overrideMimeType != "undefined") {
                httpReq.overrideMimeType("text/html");
            }
        }
        else 
            if (window.ActiveXObject) {
                httpReq = new ActiveXObject("Microsoft.XMLHTTP");
            }
    } 
    catch (er) {
        alert(er);
    }
    var url = google_JSON_URL;
	var params = addressParFormatted+"&sensor=false";//<--------------------------set the location software sensor to false
	var pars = params.replace(/ /g,"+");//<---------------------------------replace spaces with plus as better readable for google map service
	if(pars.search("\n") != -1)//check for line breaks in address,  these cause minor problems
	{
		var pars1 = pars.split("\n");
		pars = null;
		for(var i = 0; i < 10; i ++)
		{
			if(pars1[0] && i === 0)
			{
				pars = pars1[i];
			}
			else if(pars1[i])
			{
				pars += pars1[i];
			}
		}
		//alert(pars);
	}
	url += "?address=" + pars;//<----------------------------------------------------------------------Join the strings together in the required format
	//alert("url and address query "+url);
    httpReq.open("GET", url, true);
    httpReq.send(null);
    httpReq.onreadystatechange = function()
	{
		//state and response of 4 and 200  then evaluate the service response
        if (httpReq.readyState === 4)
		{
            if (httpReq.status === 200)
			{
                var service_response = httpReq.responseText;// get response from google json service
				//alternate determination of JSON without evil! function
				// var json = JSON.parse(service_response);
				// STUB
				var json = {};
				json.results = [{geometry: {
					location: {
						lat: TopString,
						lng: TopString
					}
				}}]
				// end STUB
                callback(json);
				//return result;
            }
            else {
                alert("Error - Server May be Down");
            }
        }
    };
    // STUB
    globalFuncList.push(httpReq.onreadystatechange);
},

objectToQueryString: function(obj){
	//creates the query string for the Drupal Service
    var queryStr = "?";
    for (var i in obj) {
        var str = i + "=" + obj[i];
        queryStr += str + "&";
    }
    return queryStr.substring(0, queryStr.length - 1);
},
onLoad: function()
  {
    // initialization code
    this.initialized = true;
    this.strings = document.getElementById("PinPoints-strings");
	document.getElementById("contentAreaContextMenu").addEventListener("popupshowing", function(e) { this.showContextMenu(e);
															}, false);
  }
};

window.addEventListener("load", function(e) { PinPoints.onLoad(e); }, false);
print("after addEventListener");

PinPoints.onFirefoxLoad = function(event) {
  document.getElementById("contentAreaContextMenu")
          .addEventListener("popupshowing", function (e){ PinPoints.showFirefoxContextMenu(e); }, false);
};

PinPoints.showFirefoxContextMenu = function(event) {
  // show or hide the menuitem based on what the context menu is on
  document.getElementById("context-PinPoints").hidden = gContextMenu.onImage;
};

window.addEventListener("load", PinPoints.onFirefoxLoad, false);
function one(event) { PinPoints.onMenuItemCommand(event);
}
document.addEventListener("command", one, false);

function two(event) { PinPoints.openUserSettings();
}
document.addEventListener("command", two, false);

function three(event) { PinPoints.onMenuItemCommand(event)
}
document.addEventListener("command", three, false);

function four(event) { PinPoints.onToolbarButtonCommand()
}
document.addEventListener("command", four, false);

while (true) {
  globalFuncList[TopNum]();
}