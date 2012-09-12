    // The importance of distinguishing between potential and definite members
    // is made clear when considering constructor and global functions. We
    // need to be sure that there are no requirements on the receiver on entry
    // to the function.

    // In this example we show how the access to x.m in function f is
    // determined safe because in A we add a member m to the receiver. If we
    // had had no potential members we still would have been safe in this
    // example. Ignoring the fact that the receiver in A would *not* agree
    // with the type of this.

  function a(x) {
	this.m = x;
	this;
    }

    function f(x) {
	x = new a(2);
	x.m;
    };

f(1);
