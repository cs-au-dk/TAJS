function product(name, value) {
  this.name = name;
  if (value > 1000)
    this.value = 999;
  else
    this.value = value;
}

function prod_dept(name, value, dept)
{
  this.dept = dept;
  product.apply(this, arguments);
}
prod_dept.prototype = new product();

var cheese = new prod_dept("feta", 5, "food");

var car = new prod_dept("honda", 5000, "auto");

dumpObject(cheese);
dumpObject(car);
