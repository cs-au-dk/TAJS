try {
    eval("if(") //invalid syntax
    TAJS_assert(false); //unreachble code
} catch (e) {

}