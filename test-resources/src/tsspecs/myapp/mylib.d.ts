interface MyType {
    myfield: string;
    myfield2?: string;
}

interface MyType2 extends MyType {
    myfield3: Boolean;
    myfield4: number;
}

declare module 'mylib' {
    export function myfun(arg1: string, arg2: string): string;
    //export function myfun(arg1: number, arg2: number): number;
    export var mystr: string;
    export var myobj: MyType2;
    export function myfun2(arg1: MyType): void;
    export function myfun3(): Date;
    export var myarray1: number[];
    export var myarray2: Array<number>;
    export var maytuple: [string,number,boolean];
}
