Object.defineProperty(Error, 'captureStackTrace', {
    configurable: true,
    enumerable: false, // default for V8 but not GraalVM
    writable: true,
    value: function (targetObject, constructorOpt) {
        if (Error.prepareStackTrace) {
            var structuredStackTrace = [];
            var callSite = {
                getThis : TAJS_join(TAJS_makePartial('FUNCTION', '<callSite.getThis>callSite.getThis'), undefined),
                getTypeName : function() { return TAJS_make('AnyStr'); },
                getFunction : TAJS_join(TAJS_makePartial('FUNCTION', '<callSite.getFunction>callSite.getFunction'), undefined),
                getFunctionName : function() { return TAJS_join(TAJS_make('AnyStr'), null); },
                getMethodName : function () { return TAJS_join(TAJS_make('AnyStr'), null); },
                getFileName : function () { return TAJS_make('AnyStr'); },
                getLineNumber : function () { return TAJS_make('AnyNumUInt'); },
                getColumnNumber : function () { return TAJS_make('AnyNumUInt'); },
                getEvalOrigin : function () { return callSite; },
                isTopLevel : function () { return TAJS_make('AnyBool'); },
                isEval : function () { return TAJS_make('AnyBool'); },
                isNative : function () { return TAJS_make('AnyBool'); },
                isConstructor : function () { return TAJS_make('AnyBool'); },
                toString : function () { return TAJS_make('AnyStr'); }
            };

            structuredStackTrace[TAJS_make('AnyNumUInt')] = callSite;
            targetObject.stack = Error.prepareStackTrace(targetObject, structuredStackTrace);
        } else {
            targetObject.stack = TAJS_make('AnyStr');
        }
    }
});
