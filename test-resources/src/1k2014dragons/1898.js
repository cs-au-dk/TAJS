// main lib
(function(global){

    var di = {

        _dependencies: {},

        _toRegister: {},

        process: function(target) {
            var FN_ARGS = /^function\s*[^\(]*\(\s*([^\)]*)\)/m,
                FN_ARG_SPLIT = /,/,
                FN_ARG = /^\s*(_?)(\S+?)\1\s*$/,
                STRIP_COMMENTS = /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/mg,
                text = target.toString(),
                args = text.match(FN_ARGS)[1].split(/\s*,\s*/);

            return target.apply(target, this.getDependencies(args));
        },

        getDependencies: function(arr) {
            var self = this,
                dep = arr.map(function(value) {
                    // if the dependency has been queued for registration, but hasn't been registered yet,
                    // force the registration process
                    if(!self._dependencies.hasOwnProperty(value) && self._toRegister.hasOwnProperty(value)) {
                        self._register(value, self._toRegister[value]);
                    }

                    return self._dependencies[value];
                });
            return dep;
        },

        _register: function(name, dependency) {
            this._dependencies[name] = this.util.isFn(dependency) ? di.process(dependency) : dependency;
        },

        register: function(name, dependency) {
            this._toRegister[name] = dependency;
        },

        util: {
            isFn: function(fn) {
                return typeof fn == 'function';
            }
        }

    };

    // give back Ceaser what's Ceaser's
    global.dragonInjector = di;
})(this);

// demo
(function(di, global, undefined) {
	// an example that shows the DI magic

	// this is a dog
    di.register('dog', function dog() {
        return "I'm a dog";
    });

	// this is a cat
    di.register('cat', function cat() {
        return "I'm a cat";
    });

	// this is a smart dude, but he needs
	// a dog and acat in order to be smart
	// note: you can alter the number/order 
	// of the arguments and see it's still working
    di.register('smartyPants', function smartyPants(a, b, cat, dog, e, f) {
        return 'the dog says: "' + dog + '", \nwhile the cat says: "'+ cat +'"';
    });

    di.process(function(smartyPants) {
        console.log('The smartyPants guy sais:\n' + smartyPants);
    });


    // another example

    // you can hijack jQuery
    // by registering a function as $ 
    di.register('$', function() {
        return function() {
            alert('hijacked jQuery!');
        };
    });

    // and use that function instead of the normal jQuery
    di.process(function($) {
        $('selector');
    });

})(this.dragonInjector, this);