// Web Example with error

 function input(value) { 
    this.value = value;
    this.disabled = 0;
    this;
}

function form() { 
    this.onSubmit = onSubmit;this;
}

function onSubmit() {
    checkform(this);
}

function checkform(theform) {
    theform.submi.disabled=1;  
    theform.submi.value = "Calculating"		
}

function main() {
    var htmlform;
    var htmlinput;
    htmlform = new form();
    htmlinput = new input("Infer");
    htmlform.submit = htmlinput;
    htmlform.onSubmit();
};

main();
