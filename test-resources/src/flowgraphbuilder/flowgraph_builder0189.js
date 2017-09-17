f(1)
x.f(2)
x.y.f(3)
x.y[p](4)
(f(5))(6)
(x.f)(7)
with(x){(m).f(8)}
with(x){f(9)}
with(x){(f)(10)}
(x.m = foo)(11) // should call without base
(x.m = x.m)(11) // should call without base


