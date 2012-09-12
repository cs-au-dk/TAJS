// From sinuous in the chrome experiments.
//
// Crashes with Reading undefined temporary v8 in BlockState.java.
//
// Problem appears to be a property-non-null-undef node that is misplaced
// in the meeting block instead of the then-branch.

b = this.m
a = this && (b ? this.m : 0)
