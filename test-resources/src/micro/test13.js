// big object + field access test


var g = {
    "glossary": {
        "title": "example glossary",
        "GlossDiv": {
	    	"title": "S",
	    	"GlossList": {
                	"GlossEntry": {
		    			"ID": "SGML",
		    			"SortAs": "SGML",
		    			"GlossTerm": "Standard Generalized Markup Language",
		    			"Acronym": "SGML",
		    			"Abbrev": "ISO 8879:1986",
		    			"GlossDef": {
                        		"para": "A meta-markup language, used to create markup languages such as DocBook.",
                        		"GlossSeeAlso": ["GML", "XML"]
		    			},
		    			"GlossSee": "markup"
                	}
	    	}
        }
    }
}


var dak = g.glossary.title;
var qwerty = dak + "hat"
TAJS_dumpValue(dak);
TAJS_dumpValue(qwerty);
TAJS_dumpObject(g);
TAJS_dumpObject(g.glossary);
TAJS_dumpObject(g["glossary"]);
TAJS_dumpObject(g.glossary["GlossDiv"]);
