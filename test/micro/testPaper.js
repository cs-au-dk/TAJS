 function Person(n) {
    this.setName(n);
    Person.prototype.count++;
  }
  Person.prototype.count = 0;
  Person.prototype.setName = function(n) { this.name = n; }
  function Student(n,s) {
    this.b = Person;
    this.b(n);
    delete this.b;
    this.studentid = s.toString();
  }
  Student.prototype = new Person;
  
  var t = 100026.0;
  var x = new Student("Joe Average", t++);
  var y = new Student("John Doe", t)
  y.setName("John Q. Doe");
  dumpObject(x);
  dumpObject(y);
  assert(x.name === "Joe Average")
  assert(y.name === "John Q. Doe")
  assert(y.studentid === "100027");
  assert(x.count === 3)
  
dumpModifiedState();
