package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

public class TestStrict {

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void callStringThisNonStrict() {
        Misc.init();
        Misc.runSource(
                "try { ",
                "  function f(){TAJS_assert(this instanceof String)};",
                "  f.call('foo');",
                "} catch (e) { ",
                "  TAJS_assert(false);",
                "}"
        );
    }

    @Test
    public void callStringThisStrict() {
        Misc.init();
        Misc.runSource(
                "(function(){",
                "   'use strict';",
                "   try { ",
                "     function f(){TAJS_assertEquals('foo', this)};",
                "     f.call('foo');",
                "   } catch (e) { ",
                "     TAJS_assert(false);",
                "   }",
                "})();"
        );
    }

    @Test
    public void callStrictWithoutReceiver() {
        Misc.init();
        Misc.runSource(
                "(function(){",
                "   'use strict';",
                "   try { ",
                "     function f(){TAJS_assertEquals(undefined, this)};",
                "     f();",
                "   } catch (e) { ",
                "     TAJS_assert(false);",
                "   }",
                "})();"
        );
    }

    @Test
    public void inheritStrict() {
        Misc.init();
        Misc.runSource(
                "(function(){",
                "   'use strict';",
                "   try { ",
                "     function f(){TAJS_assertEquals(undefined, this)};",
                "     f();",
                "   } catch (e) { ",
                "     TAJS_assert(false);",
                "   }",
                "})();"
        );
    }

    @Test
    public void deepInheritStrict() {
        Misc.init();
        Misc.runSource(
                "(function(){",
                "   'use strict';",
                "   (function(){",
                "       try { ",
                "           function f(){TAJS_assertEquals(undefined, this)};",
                "           f();",
                "       } catch (e) { ",
                "           TAJS_assert(false);",
                "       }",
                "   })();",
                "})();"
        );
    }

    @Test
    public void selfStrict() {
        Misc.init();
        Misc.runSource(
                "(function(){",
                "   try { ",
                "     function f(){'use strict'; TAJS_assertEquals(undefined, this)};",
                "     f();",
                "   } catch (e) { ",
                "     TAJS_assert(false);",
                "   }",
                "})();"
        );
    }

    @Test(expected = AssertionError.class /* Top level 'use strict' is not yet supported (for architectural reasons */)
    public void topLevelStrict() {
        Misc.init();
        Misc.runSource(
                "'use strict';",
                "try { ",
                "  function f(){TAJS_assertEquals(undefined, this)};",
                "  f();",
                "} catch (e) { ",
                "  TAJS_assert(false);",
                "}"
        );
    }

    @Test
    public void strictUndefined() {
        Misc.init();
        Misc.runSource(
                new String[]{
                        "(function () {",
                        "   'use strict';",
                        "   function f() {}",
                        "   f();",
                        "})();",
                });
    }

    @Test
    public void strictUndefinedNullReceivers() {
        Misc.init();
        Misc.runSource(
                new String[]{
                        "(function () {",
                        "   'use strict';",
                        "   TAJS_assertEquals(undefined, this);",
                        "   function f() {",
                        "       TAJS_assertEquals(undefined, this);",
                        "   }",
                        "   function g() {",
                        "       TAJS_assertEquals(null, this);",
                        "   }",
                        "   f();",
                        "   g.call(null);",
                        "})();",
                });
    }
}
