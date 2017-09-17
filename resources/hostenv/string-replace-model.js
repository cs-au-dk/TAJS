Object.defineProperty(String.prototype, 'replace', {
    writable: true, enumerable: false, configurable: true,
    value: function replace(searchValue, replaceValue) {
        // Call CheckObjectCoercible passing the this value as its argument.
        if (this === null || this === undefined) {
            throw new TypeError();
        }
        var receiver = this;
        // Let string be the result of calling ToString, giving it the this value as its argument.
        var string = receiver + '';
        if (typeof replaceValue !== 'function') {
            // fast path through Nashorn
            return TAJS_firstOrderStringReplace(string, searchValue, replaceValue + "");
        }

        var searchValueIsRegExp = searchValue instanceof RegExp;

        var matches = TAJS_newArray();
        if (searchValueIsRegExp) { // If searchValue is a regular expression (an object whose [[Class]] internal property is "RegExp"), do the following:
            // this models: "Do the search in the same manner as in String.prototype.match, including the update of searchValue.lastIndex. Let m be the number of left capturing parentheses in searchValue (using NcapturingParens as specified in 15.10.2.1)."
            var matchThatIsFound;
            do {
                matchThatIsFound = searchValue.exec(string);
                if (matchThatIsFound !== null) {
                    var callbackArguments = TAJS_newArray();
                    for (var i = 0; i < matchThatIsFound.length; i++) {
                        callbackArguments[i] = matchThatIsFound[i];
                    }
                    callbackArguments.push(matchThatIsFound.index);
                    callbackArguments.push(matchThatIsFound.input);
                    matches.push(callbackArguments);
                }
            } while (matchThatIsFound !== null && matchThatIsFound[0] != "" && searchValue.global)
        } else {
            searchValue = searchValue + '';
            var callbackArguments = TAJS_newArray();
            var indexMatch = string.indexOf(searchValue);
            if (indexMatch !== -1) {
                callbackArguments[0] = searchValue;
                callbackArguments.push(indexMatch);
                callbackArguments.push(string);
                matches.push(callbackArguments);
            }
        }
        var replacementSubstrings = TAJS_newArray();

        for (var j = 0; j < matches.length; j++) {
            var matchToUse = matches[j];
            var startIndex = matchToUse[matchToUse.length - 2];
            var endIndex = startIndex + matchToUse[0].length;
            var replacementSubstring = replaceValue.apply(undefined, matchToUse) + "";
            var replacement = TAJS_newObject();
            replacement.startIndex = startIndex;
            replacement.substring = replacementSubstring;
            replacement.endIndex = endIndex;
            replacementSubstrings.unshift(replacement);
        }

        for (var k = 0; k < replacementSubstrings.length; k++) {
            var replacement = replacementSubstrings[k];
            var prefix = string.substring(0, replacement.startIndex);
            var suffix = string.substring(replacement.endIndex);
            var result = prefix + replacement.substring + suffix;
            string = result;
        }
        return string;
    }
});
TAJS_makeContextSensitive(String.prototype.replace, -1);
TAJS_makeContextSensitive(String.prototype.replace, 0);
TAJS_makeContextSensitive(String.prototype.replace, 1);