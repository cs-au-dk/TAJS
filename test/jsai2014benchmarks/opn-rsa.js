/* (original disclaimer from JSBN, on which this file is based)
 *
 * Copyright (c) 2003-2005  Tom Wu
 * All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS-IS" AND WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS, IMPLIED OR OTHERWISE, INCLUDING WITHOUT LIMITATION, ANY 
 * WARRANTY OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  
 *
 * IN NO EVENT SHALL TOM WU BE LIABLE FOR ANY SPECIAL, INCIDENTAL,
 * INDIRECT OR CONSEQUENTIAL DAMAGES OF ANY KIND, OR ANY DAMAGES WHATSOEVER
 * RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER OR NOT ADVISED OF
 * THE POSSIBILITY OF DAMAGE, AND ON ANY THEORY OF LIABILITY, ARISING OUT
 * OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 * In addition, the following condition applies:
 *
 * All redistributions must retain an intact copy of this copyright notice
 * and disclaimer.
 */

/**
 * @namespace High-precision arithmetic
 * @author Tom Wu (original author of JSBN)
 * @author Anonymized
 * @description
 * <p>Minimal set of high precision arithmetic operations
 * for RSA encryption and decryption.</p>
 * <p>To preserve both defensiveness and performance, this
 * is not an arbitrary precision library! Each number is
 * represented by a constant length array of 256 elements.
 * Because of tagging optimizations, each number stores 28
 * bits, hence the maximal precision is 7168 bits. 128 was
 * not chosen to allow RSA on a 2048 bit modulus, and it is
 * highly preferred to use a power of 2 to use the short
 * dynamic accessor notation.</p>
 * @requires encoding
 */
 var BigInteger =
 {
  BI_DB: 28,
  BI_DM: 268435455,
  BI_DV: 268435456,
  BI_FP: 52,
  BI_FV: 4503599627370496,
  BI_F1: 24,
  BI_F2: 4,

/** Create a new BigInteger initialized from the given hex value.
  * @param {string} v Hex representation of initial value in a string.
  * @returns {BigInteger} A BigInteger structure.
  */
  create: function(v)
  {
   var neg = false, p = '', b = '', s = v+'', i = s.length, j = 0, a =
   [ 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 ],
       res = {array:a, t:0, s:0};

   while(--i >= 0)
   {
    b = (i>>>=0)<s.length?s[i]:"0";
    if(i==0 && b=='-'){neg = true; continue;}
    p = b + p;
    if(j++%7==6)
    {
     a[res.t++&255] = +('0x'+p); p = '';
    }
   }
   if(!!p) a[res.t++&255] = +('0x'+p); p = '';

   if(neg) res = this.negate(res);
   this.clamp(res);
   return res;
  },

  am: function(th,i,x,w,j,c,n)
  {
   var a = th.array, b = w.array, l = 0, m = 0,
      xl = x&0x3fff, xh = x>>14, h = 0;

   while(--n >= 0)
   {
    l = a[i&255]&0x3fff;i
    h = a[i++&255]>>14;
    m = xh*l+h*xl;
    l = xl*l+((m&0x3fff)<<14)+b[j&255]+c;
    c = (l>>28)+(m>>14)+xh*h;
    b[j++&255] = l&0xfffffff;
   }

   return c;
  },

/** Copy the value of a BigInteger to another.
  * @param {BigInteger} source Integer to copy.
  * @param {BigInteger} target Target of copy.
  * @returns {BigInteger} Returns the target of the copy.
  */
  copyTo: function(th, r)
  {
   var ta = th.array, ra = r.array, i = 0;

   for(i = th.t-1; i >= 0; --i)
    ra[i&255] = ta[i&255];

   r.t = th.t; r.s = th.s;
   return r;
  },

  clamp: function(th)
  {
   var a = th.array, c = th.s & this.BI_DM;
   while(th.t > 0 && a[(th.t-1)&255] == c) --th.t;
  },

/** Convert BigInteger to its hex representation.
  * @param {BigInteger} n Number to convert
  * @returns {string} Hex representation of n, as a string.
  */
  toString: function(th)
  {
   var a = th.array, c = 0, i = 0, j = 0,
       hex = encoding.hex, k = 0,
       nz = false, h = '', res = '';

   if(th.s < 0)
    return "-"+this.toString(this.negate(th));

   for(i=th.t-1; i>=0; i--)
   {
    c = a[i&255];
    for(j=24; j>=0; j-=4)
    {
     k = (c>>j) & 15;
     h = (k>>>=0)<hex.length?hex[k]:"0";
     if(h != '0') nz = true;
     if(nz) res += h;
    }
   }

   return !res ? '0' : res;
  },

/** Change sign of number.
  * @param {BigInteger} n Input number
  * @returns {BigInteger} A newly allocated BigInteger with opposite value
  */
  negate: function(th)
  {
   var t = this.create('0'), z = this.create('0');
   this.subTo(z, th, t);
   return t;
  },

/** Absolute value.
  * @param {BigInteger} n Input number
  * @returns {BigInteger} If n is positive, returns n, otherwise return negate(n)
  */
  abs: function(th)
  {
   return th.s<0 ? this.negate(th) : th;
  },

/** Exclusive OR of two numbers
  * @param {BigInteger} n First operand
  * @param {BigInteger} m Second operand
  * @returns {BigInteger} n xor m
  */
  xor: function(th, a)
  {
   var x = th.array, y = a.array,
       r = this.create('0'), z = r.array,
       i = (th.t > a.t) ? th.t : a.t;
   r.t = i;
   while(--i >= 0) z[i&255] = x[i&255]^y[i&255];
   return r;
  },

/** Comparison of BigInteger.
  * @param {BigInteger} n First value
  * @param {BigInteger} m Second value
  * @returns {number} A negative value if n<m, 0 if n=m and a positive value otherwise.
  */
  compareTo: function(th,a)
  {
   var x = th.array, y = a.array, i = th.t,
       r = th.s-a.s, s = th.t-a.t;

   if(!!r) return r; if(!!s) return s;
   while(--i >= 0)
    if((r = (x[i&255]-y[i&255]))!=0) return r;
   return 0;
  },

/** Index of the first non-zero bit starting from the least significant bit.
  * @param {number} n  Input number
  * @returns {number} the bit length of n. Can behave strangely on negative and float values.
  */
  nbits: function(x)
  {
   var r = 1, t = 0;
   if((t=x>>>16) != 0) { x = t; r += 16; }
   if((t=x>>8) != 0) { x = t; r += 8; }
   if((t=x>>4) != 0) { x = t; r += 4; }
   if((t=x>>2) != 0) { x = t; r += 2; }
   if((t=x>>1) != 0) { x = t; r += 1; }
   return r;
  },

/** Index of first non-zero bit starting from the LSB of the given BigInteger.
  * @param {BigInteger} n Input BigInteger
  * @returns {number} the bit length of n.
  */
  bitLength: function(th)
  {
   var a = th.array;
   if(th.t <= 0) return 0;
   return this.BI_DB*(th.t-1)+this.nbits(a[(th.t-1)&255]^(th.s&this.BI_DM));
  },

  DLshiftTo: function(th,n,r)
  {
   var a = th.array, b = r.array, i = 0;
   for(i = th.t-1; i >= 0; --i) b[(i+n)&255] = a[i&255];
   for(i = n-1; i >= 0; --i) b[i&255] = 0;
   r.t = th.t+n; r.s = th.s;
  },

  DRshiftTo: function(th,n,r)
  {
   var a = th.array, b = r.array, i = 0;
   for(i = n; i < th.t; ++i) b[(i-n)&255] = a[i&255];
   r.t = th.t>n?th.t-n:0; r.s = th.s;
  },

/** Logical shift to the left
  * @param {BigInteger} n Input number
  * @param {number} k Number of positions to shift
  * @param {BigInteger} r Target number to store the result to
  */
  LshiftTo: function(th,n,r)
  {
   var a = th.array, b = r.array,
      bs = n%this.BI_DB, cbs = this.BI_DB-bs,
      bm = (1<<cbs)-1,  ds = (n/this.BI_DB)|0,
       c = (th.s<<bs)&this.BI_DM, i = 0;

   for(i = th.t-1; i >= 0; --i)
    b[(i+ds+1)&255] = (a[i&255]>>cbs)|c, c = (a[i&255]&bm)<<bs;
   for(i = ds-1; i >= 0; --i) b[i&255] = 0;

   b[ds&255] = c; r.t = th.t+ds+1;
   r.s = th.s; this.clamp(r);
  },

/** Logical shift to the right.
  * @param {BigInteger} n Input number
  * @param {number} k Number of positions to shift
  * @param {BigInteger} r Target number to store the result to
  */
  RshiftTo: function(th,n,r)
  {
   var a = th.array, b = r.array, i = 0,
      bs = n%this.BI_DB, cbs = this.BI_DB-bs,
      bm = (1<<bs)-1,  ds = (n/this.BI_DB)|0;

   r.s = th.s;
   if(ds >= th.t) { r.t = 0; return; }
   b[0] = a[ds&255]>>bs;

   for(i = ds+1; i < th.t; ++i)
    b[(i-ds-1)&255] |= (a[i&255]&bm)<<cbs,
    b[(i-ds)&255] = a[i&255]>>bs;
   if(bs > 0) b[(th.t-ds-1)&255] |= (th.s&bm)<<cbs;

   r.t = th.t-ds; this.clamp(r);
  },

/** Subtraction of BigIntegers.
  * @param {BigInteger} n First operand
  * @param {BigInteger} m Second operand
  * @param {BigInteger} r Target number to store the result (n-m) to.
  */
  subTo: function(th, y, r)
  {
   var a = th.array, z = r.array, b = y.array,
       i = 0, c = 0, m = y.t<th.t?y.t:th.t;

   while(i < m)
   {
    c += a[i&255]-b[i&255];
    z[i++&255] = c&this.BI_DM;
    c >>= this.BI_DB;
   }

   if(y.t < th.t)
   {
    c -= y.s;
    while(i < th.t)
    {
     c += a[i&255];
     z[i++&255] = c&this.BI_DM;
     c >>= this.BI_DB;
    }
    c += th.s;
   }
   else
   {
    c += th.s;
    while(i < y.t)
    {
     c -= b[i&255];
     z[i++&255] = c&this.BI_DM;
     c >>= this.BI_DB;
    }
    c -= y.s;
   }

   r.s = (c<0)?-1:0;
   if(c < -1) z[i++&255] = this.BI_DV+c;
   else if(c > 0) z[i++&255] = c;
   r.t = i; this.clamp(r);
  },

/** Multiplication of BigIntegers.
  * @param {BigInteger} n First operand
  * @param {BigInteger} m Second operand
  * @param {BigInteger} r Target number to store the result (n*m) to.
  */
  multiplyTo: function(th,a,r)
  {
   var u = th.array, v = r.array,
       x = this.abs(th), y = this.abs(a),
       w = y.array, i = x.t;

   r.t = i+y.t;
   while(--i >= 0) v[i&255] = 0;
   for(i = 0; i < y.t; ++i)
    v[(i+x.t)&255] = this.am(x,0,w[i&255],r,i,0,x.t);

   r.s = 0; this.clamp(r);
   if(th.s != a.s) this.subTo(this.create('0'),r,r);
  },

/** Squaring of a BigInteger.
  * @param {BigInteger} n First operand
  * @param {BigInteger} r Target number to store the result (n*n) to.
  */
  squareTo: function(th, r)
  {
   var x = this.abs(th), u = x.array, v = r.array,
       i = (r.t = 2*x.t), c = 0;

   while(--i >= 0) v[i&255] = 0;
   for(i = 0; i < x.t-1; ++i)
   {
    c = this.am(x,i,u[i&255],r,2*i,0,1);
    if((v[(i+x.t)&255] += this.am(x,i+1,2*u[i&255],r,2*i+1,c,x.t-i-1)) >= this.BI_DV)
     v[(i+x.t)&255] -= this.BI_DV, v[(i+x.t+1)&255] = 1;
   }

   if(r.t > 0) v[(r.t-1)&255] += this.am(x,i,u[i&255],r,2*i,0,1);
   r.s = 0; this.clamp(r);
  },

/** Euclidean division of two BigIntegers.
  * @param {BigInteger} n First operand
  * @param {BigInteger} m Second operand
  * @returns {BigInteger array[2]} Returns an array of two BigIntegers: first element is the quotient, second is the remainder.
  */
  divRem: function(th, div)
  {
   var m = this.abs(div), t = this.abs(th), ma = m.array, ta = th.array,
       ts = th.s, ms = m.s, nsh = this.BI_DB-this.nbits(ma[(m.t-1)&255]),
       q = this.create('0'), r = this.create('0'),
       qa = q.array, ra = r.array, qd = 0,
       y = this.create('0'), ya = y.array, ys = 0, y0 = 0,
       yt = 0, i = 0, j = 0, d1 = 0, d2 = 0, e = 0;

   if(t.t < m.t) this.copyTo(th,r);
   if(!m.t || t.t < m.t) return [q,r];

   if(nsh > 0){ this.LshiftTo(m,nsh,y); this.LshiftTo(t,nsh,r); }
   else{ this.copyTo(m,y); this.copyTo(m,r); }

   ys = y.t; y0 = ya[(ys-1)&255];
   if(y0 == 0) return [q,r];

   yt = y0*(1<<this.BI_F1)+((ys>1)?ya[(ys-2)&255]>>this.BI_F2:0);
   d1 = this.BI_FV/yt, d2 = (1<<this.BI_F1)/yt, e = 1<<this.BI_F2;
   i = r.t, j = i-ys;
   this.DLshiftTo(y,j,q);

   if(this.compareTo(r,q) >= 0)
   {
    ra[r.t++ & 255] = 1;
    this.subTo(r,q,r);
   }

   this.DLshiftTo(this.create('1'),ys,q);
   this.subTo(q,y,y);
   while(y.t < ys) ya[y.t++&255] = 0;

   while(--j >= 0)
   {
    qd = (ra[--i&255]==y0)?this.BI_DM:(ra[i&255]*d1+(ra[(i-1)&255]+e)*d2)|0;
    if((ra[i&255]+=this.am(y,0,qd,r,j,0,ys)) < qd)
    {
     this.DLshiftTo(y,j,q);
     this.subTo(r,q,r);
     while(ra[i&255] < --qd) this.subTo(r,q,r);
    }
   }

   this.DRshiftTo(r,ys,q);
   if(ts != ms) this.subTo(this.create('0'),q,q);
   r.t = ys; this.clamp(r);

   if(nsh > 0) this.RshiftTo(r,nsh,r);
   if(ts < 0) this.subTo(this.create('0'),r,r);
   return [q,r];
  },

/** Modular remainder of an integer division.
  * @param {BigInteger} n First operand
  * @param {BigInteger} m Second operand
  * @returns {BigInteger} n mod m
  */
  mod: function(th, a)
  {
   var r = this.divRem(this.abs(th),a)[1];
   if(th.s < 0 && this.compareTo(r,this.create('0')) > 0) this.subTo(a,r,r);
   return r;
  },

  invDigit: function(th)
  {
   var a = th.array, x = a[0], y = x&3;
   if(th.t < 1 || !(x&1)) return 0;
   y = (y*(2-(x&0xf)*y))&0xf;
   y = (y*(2-(x&0xff)*y))&0xff;
   y = (y*(2-(((x&0xffff)*y)&0xffff)))&0xffff;
   y = (y*(2-x*y%this.BI_DV))%this.BI_DV;
   return (y>0)?this.BI_DV-y:-y;
  },

/** Modular exponentiation using Montgomery reduction. 
  * @param {BigInteger} x Value to exponentiate
  * @param {BigInteger} e Exponent
  * @param {BigInteger} n Modulus - must be odd
  * @returns {BigInteger} x^e mod n
  */
  expMod: function(th, e, m)
  {
   var r = this.create('1'), r2 = this.create('0'), eb = e.array[(e.t-1)&255],
       g = this.Mconvert(th,m), i = this.bitLength(e)-1, j = 0, t = r;

   if(this.compareTo(e,r)<0) return r;
   this.copyTo(g,r);

   while(--i >= 0)
   {
    j = i%this.BI_DB;
    this.squareTo(r,r2); this.Mreduce(r2,m);
    if((eb&(1<<j)) != 0){ this.multiplyTo(r2,g,r); this.Mreduce(r,m); }
    else { t = r; r = r2; r2 = t; }
    if(!j) eb = e.array[(i/this.BI_DB-1)&255];
   }

   return this.Mrevert(r,m);
  },

  Mconvert: function(th, m)
  {
   var s = this.create('0'),
       r = (this.DLshiftTo(this.abs(th),m.t,s),this.divRem(s,m))[1];

   if(th.s < 0 && this.compareTo(r,this.create('0')) > 0) this.subTo(m,r,r);
   return r;
  },

  Mreduce: function(th, m)
  {
   var mp = this.invDigit(m), mpl = mp&0x7fff, mph = mp>>15, a = th.array,
       um = (1<<(this.BI_DB-15))-1, mt2 = 2*m.t, i = 0, j = 0, u0 = 0;

   while(th.t <= mt2) a[th.t++&255] = 0;
   for(i = 0; i < m.t; ++i)
   {
    j = a[i&255]&0x7fff;
    u0 = (j*mpl+(((j*mph+(a[i&255]>>15)*mpl)&um)<<15))&this.BI_DM;
    j = i+m.t;
    a[j&255] += this.am(m,0,u0,th,i,0,m.t);
    while(a[j&255] >= this.BI_DV) { a[j&255] -= this.BI_DV; a[++j&255]++; }
   }

   this.clamp(th); this.DRshiftTo(th, m.t, th);
   if(this.compareTo(th,m) >= 0) this.subTo(th,m,th);
   return th;
  },

  Mrevert: function(th, m)
  {
   var c = this.create('0');
   this.copyTo(th, c);
   return this.Mreduce(c,m);
  }
 };

/**
 * @namespace Encoding functions
 * @author Anonymized
 * @description
 * <p>Support for ASCII, UTF-8, Base64 and Hex encoding.</p>
 * <p>DJS is simply not good at encoding, because it lacks the
 * built-in functions to get the character code at a given offset
 * from a string (charCodeAt), and its inverse, String.fromCharCode.</p>
 *
 * <p>This means we have to use a literal object whose field names are
 * ASCII characters and values are the associated codes, and a string
 * containing every character sorted by code for the inverse operation.</p>
 *
 * <p>For UTF-8, such a table would be too large and instead, we use
 * binary search on the string containing all UTF-8 characters,
 * using the built in lexicographic order of JavaScript.
 * Since the complete UTF-8 alphabet is itself 200KB, it is loaded
 * from its own file, utf8.js. Loading this file is optional: without
 * it every non-ASCII character is treated like the null byte.</p>
 */
 var encoding =
 {
/** Hex alphabet. */
  hex: "0123456789abcdef",

/** UTF-8 alphabet. Initially contains the null byte, actual value is in utf8.js */
  utf8: "\x00",
  utf8_table: {},

/** The ASCII alphabet, can be used directly */
  ascii: "\x00\x01\x02\x03\x04\x05\x06\x07\x08\x09\x0a\x0b\x0c\x0d\x0e\x0f\x10\x11\x12\x13\x14\x15\x16\x17\x18\x19\x1a\x1b\x1c\x1d\x1e\x1f !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\x7f\x80\x81\x82\x83\x84\x85\x86\x87\x88\x89\x8a\x8b\x8c\x8d\x8e\x8f\x90\x91\x92\x93\x94\x95\x96\x97\x98\x99\x9a\x9b\x9c\x9d\x9e\x9f\xa0\xa1\xa2\xa3\xa4\xa5\xa6\xa7\xa8\xa9\xaa\xab\xac\xad\xae\xaf\xb0\xb1\xb2\xb3\xb4\xb5\xb6\xb7\xb8\xb9\xba\xbb\xbc\xbd\xbe\xbf\xc0\xc1\xc2\xc3\xc4\xc5\xc6\xc7\xc8\xc9\xca\xcb\xcc\xcd\xce\xcf\xd0\xd1\xd2\xd3\xd4\xd5\xd6\xd7\xd8\xd9\xda\xdb\xdc\xdd\xde\xdf\xe0\xe1\xe2\xe3\xe4\xe5\xe6\xe7\xe8\xe9\xea\xeb\xec\xed\xee\xef\xf0\xf1\xf2\xf3\xf4\xf5\xf6\xf7\xf8\xf9\xfa\xfb\xfc\xfd\xfe\xff",

/** ASCII code table. Has its own dynamic accessor. */
  ascii_table: {"0":48,"1":49,"2":50,"3":51,"4":52,"5":53,"6":54,"7":55,"8":56,"9":57,"\x00":0,"\x01":1,"\x02":2,"\x03":3,"\x04":4,"\x05":5,"\x06":6,"\x07":7,"\b":8,"\t":9,"\n":10,"\x0b":11,"\f":12,"\r":13,"\x0e":14,"\x0f":15,"\x10":16,"\x11":17,"\x12":18,"\x13":19,"\x14":20,"\x15":21,"\x16":22,"\x17":23,"\x18":24,"\x19":25,"\x1a":26,"\x1b":27,"\x1c":28,"\x1d":29,"\x1e":30,"\x1f":31," ":32,"!":33,'"':34,"#":35,"$":36,"%":37,"&":38,"'":39,"(":40,")":41,"*":42,"+":43,",":44,"-":45,".":46,"/":47,":":58,";":59,"<":60,"=":61,">":62,"?":63,"@":64,"A":65,"B":66,"C":67,"D":68,"E":69,"F":70,"G":71,"H":72,"I":73,"J":74,"K":75,"L":76,"M":77,"N":78,"O":79,"P":80,"Q":81,"R":82,"S":83,"T":84,"U":85,"V":86,"W":87,"X":88,"Y":89,"Z":90,"[":91,"\\":92,"]":93,"^":94,"_":95,"`":96,"a":97,"b":98,"c":99,"d":100,"e":101,"f":102,"g":103,"h":104,"i":105,"j":106,"k":107,"l":108,"m":109,"n":110,"o":111,"p":112,"q":113,"r":114,"s":115,"t":116,"u":117,"v":118,"w":119,"x":120,"y":121,"z":122,"{":123,"|":124,"}":125,"~":126,"\x7f":127,"\x80":128,"\x81":129,"\x82":130,"\x83":131,"\x84":132,"\x85":133,"\x86":134,"\x87":135,"\x88":136,"\x89":137,"\x8a":138,"\x8b":139,"\x8c":140,"\x8d":141,"\x8e":142,"\x8f":143,"\x90":144,"\x91":145,"\x92":146,"\x93":147,"\x94":148,"\x95":149,"\x96":150,"\x97":151,"\x98":152,"\x99":153,"\x9a":154,"\x9b":155,"\x9c":156,"\x9d":157,"\x9e":158,"\x9f":159,"\xa0":160,"\xa1":161,"\xa2":162,"\xa3":163,"\xa4":164,"\xa5":165,"\xa6":166,"\xa7":167,"\xa8":168,"\xa9":169,"\xaa":170,"\xab":171,"\xac":172,"\xad":173,"\xae":174,"\xaf":175,"\xb0":176,"\xb1":177,"\xb2":178,"\xb3":179,"\xb4":180,"\xb5":181,"\xb6":182,"\xb7":183,"\xb8":184,"\xb9":185,"\xba":186,"\xbb":187,"\xbc":188,"\xbd":189,"\xbe":190,"\xbf":191,"\xc0":192,"\xc1":193,"\xc2":194,"\xc3":195,"\xc4":196,"\xc5":197,"\xc6":198,"\xc7":199,"\xc8":200,"\xc9":201,"\xca":202,"\xcb":203,"\xcc":204,"\xcd":205,"\xce":206,"\xcf":207,"\xd0":208,"\xd1":209,"\xd2":210,"\xd3":211,"\xd4":212,"\xd5":213,"\xd6":214,"\xd7":215,"\xd8":216,"\xd9":217,"\xda":218,"\xdb":219,"\xdc":220,"\xdd":221,"\xde":222,"\xdf":223,"\xe0":224,"\xe1":225,"\xe2":226,"\xe3":227,"\xe4":228,"\xe5":229,"\xe6":230,"\xe7":231,"\xe8":232,"\xe9":233,"\xea":234,"\xeb":235,"\xec":236,"\xed":237,"\xee":238,"\xef":239,"\xf0":240,"\xf1":241,"\xf2":242,"\xf3":243,"\xf4":244,"\xf5":245,"\xf6":246,"\xf7":247,"\xf8":248,"\xf9":249,"\xfa":250,"\xfb":251,"\xfc":252,"\xfd":253,"\xfe":254,"\xff":255},

/** Base64 alphabet. Is missing the last two characters to support URL style */
  base64: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz",

/** Binary search of a character inside a sorted string.
  * @param {string} char character to search
  * @param {string} alphabet string whose characters are sorted in lexicographic order
  * @returns {number} the position where char occurs in alphabet, or 0 if not found.
  */
  _searchCharTable: function(a, table)
  {
   var a = a+'', table = table+'', min = 0,
       max=table.length, m = 0, b = "";
   while(m != (m = (min+max)>>1))
   {
    b = (m>>>=0)<table.length ? table[m] : "";
    if(a == b) return m;
    if(a > b) min = m; else max = m;
   } 
   return 0;
  },

/** Hex representation of a byte.
  * @param {number} input byte
  * @returns {string} hex representation of the input, has always length 2.
  */
  b2h: function(c)
  {
   var t = this.hex+'', a = (c>>4)&15, b = c&15;
   return ((a>>>=0)<t.length?t[a]:"0")+((b>>>=0)<t.length?t[b]:"0");
  },

/** The code of a character in the base64 alphabet. Accept +- or /_, fallback is 0.
  * @param {string} input base64 character
  * @returns {number} base64 code from 0 to 63
  */
  b64c: function(s)
  {
   if(s=='+' || s=='-') return 62;
   if(s=='/' || s=='_') return 63;
   if(s >= "0" && s <= "9") return +s + 52;
   return this._searchCharTable(s,this.base64);
  },

/** charCodeAt emulation. Returns the code point of the input character
  * @param {string} input character
  * @returns {number} 16 bit character point. If input if not ASCII and utf8.js is not loaded, will return 0.
  */
  charCode: function(a)
  {
   var t = this.ascii_table;
   return (a.length == 1 && a <= "\xFF" ? t[a]
    : this._searchCharTable(a, this.utf8));
  },

/** ASCII code of input character.
  * @param {string} input character
  * @returns {number} ASCII code of input. Unlike encoding.charCode, will always return 0 if input is non-ASCII.
  */
  a2b: function(a)
  {
   var t = this.ascii_table;
   return (a.length==1 && a <= "\xFF" ? t[a] : 0);
  },

/** ASCII character from its code.
  * @param {number} input ASCII code, only first 8 bits are taken into account.
  * @returns {string} associated ASCII character.
  */
  b2a: function(n)
  {
   var a = this.ascii+'';
   return (n>>>=0)<a.length ? a[n] : "\x00";
  },

/** fromCharCode emulation. Can create unicode characters.
  * @param {number} input code point (truncated to 16 bits)
  * @returns {string} UCS-2 character of requested code point.
  */
  fromCharCode: function(n)
  {
   var a = this.ascii+'', u = this.utf8+'';
   return (n>>>=0)<a.length ? a[n]
    : (n>>>=0)<u.length ? u[n] : "\x00";
  },

/** Convert an ASCII string to an hexadecimal string
  * @param {string} input must be ASCII (uses a2b internally)
  * @returns {string} hex representation of input
  */
  astr2hstr: function(s)
  {
   var res = '', i = 0, s=s+'';
   for(i=0; i<s.length; i++)
    res += this.b2h(this.a2b(s[i]));
   return res;
  },

/** Convert an hexadecimal string to ASCII.
  * @param {string} input hex string
  * @returns {string} ASCII equivalent
  */
  hstr2astr: function(s)
  {
   var i = 0, u = 0, c = '', res = "",
       t = this.ascii+'', s = s + '';

   for(i=0; i<s.length; i++)
   {
    if(!(i&1)) c = s[i];
    else
    {
     u = +('0x'+c+s[i]);
     res += (u>>>=0)<t.length ? t[u] : "\x00";
    }
   }
   return res;
  },

/** Encode a raw ASCII string back into a JavaScript string
  * @param {string} input ASCII
  * @returns {string} JavaScript unicode string representing the UTF-8 input.
  */
  utf8_encode: function(s)
  {
   var res = "", i = 0, c = 0, p = '',
       buffer = [0,0,0,0], expected = [0,0];

   for(i=0; i < s.length; i++)
   {
    c = this.a2b(p = s[i]);
    if(expected[0] != 0)
    {
     // Invalied continuation
     if(c<128 || c > 191){expected=[0,0]; continue}
     buffer[(expected[1]+1-expected[0]--)&3] = c;

     if(!expected[0])
     {
      res += this.fromCharCode(
        expected[1]==1 ? (buffer[0]&31)<<6 | (buffer[1] & 63)
         : (buffer[0] & 15)<<12 | (buffer[1] & 63)<<6 | (buffer[2] & 63));
     }
     else continue;
    }
    buffer[0] = c;

    if(c<128) res += p;
    else if(c>191 && c<224) expected = [1,1];
    else if(c>=224 && c<240) expected = [2,2];
    // Otherwise, invalid head (ignored)
   }

   return res;
  },

/** Decode an UTF-8 string to its raw ASCII equivalent.
  * @param {string} input JavaScript string (containing unicode characters)
  * @returns {string} decoded ASCII string
  */
  utf8_decode: function(s)
  {
   var res = "", i = 0, c = 0, s = s+'',
       x = this.b2a(0);

   for(i=0; i<s.length; i++)
   {
    c = this.charCode(x = s[i]);
    if(c < 128) res += x;
    else if(c < 2048)
     res += this.b2a((c>>6)|192)+this.b2a((c&63)|128);
    else
     res += this.b2a((c>>12)|224)+this.b2a(128|(c>>6)&63)+this.b2a(128|c&63);
   }

   return res;
  },

/** Encode an ASCII string to base64
  * @param {string} input ASCII
  * @returns {string} base64 encoding of input.
  */
  base64_encode: function(s)
  {
   return this._base64_encode(s, false);
  },

/** Encode an ASCII string to url-compatible base64
  * @param {string} input ASCII
  * @returns {string} url-base64 encoding of input.
  */
  base64_urlencode: function(s)
  {
   return this._base64_encode(s, true);
  },

  _base64_encode: function(s, url)
  {
   var res = "", i = 0, c = 0, s = s+'',
       buf = [0,0], pad = !url ? '=' : '', p = '',
       table = this.base64 + "0123456789" + (url ? '-_' : '+/'),
       chr = function(i){return (i>>>=0)<table.length?table[i]:"A"};

   for(i=0; i < s.length; i++)
   {
    c = this.a2b(s[i]);

    if(i%3 == 2)
    {
     c += (buf[1]<<8) + (buf[0]<<16);
     res += chr((c>>>18)&63);
     res += chr((c>>>12)&63);
     res += chr((c>>>6)&63);
     res += chr(c&63);
     buf = [0, 0];
    }
    else buf[(i%3)&1] = c;
   }

   // Padding
   if(i%3 != 0)
   {
    c = (buf[1]<<8) + (buf[0]<<16);
    res += chr((c>>>18)&63);
    res += chr((c>>>12)&63);
    res += (i%3==2)?chr((c>>>6)&63):pad;
    res += pad;
   }

   return res;
  },

/** Decode a base64-encoded string to ASCII
  * @param {string} input base64 (can be url-safe or not)
  * @returns {string} the decoded ASCII string.
  */
  base64_decode: function(s)
  {
   var s = s+'', res = "", buf = [0,0,0,0],
       i = 0, x = '', c = 0;

   if((s.length&3) != 0) s+='=';
   for(i=0; i < s.length; i++)
   {
    if((x = s[i]) == "=") break;
    c = this.b64c(x);

    if((i&3) == 3)
    {
     c += (buf[2]<<6) + (buf[1]<<12) + (buf[0]<<18);
     res += this.b2a((c>>>16)&255);
     res += this.b2a((c>>>8)&255);
     res += this.b2a(c&255);
     buf = [0,0,0,0];
    }
    else buf[(i%4)&3] = c;
   }

   // Padding
   if((i&3)>1)
   {
    c = (buf[2]<<6) + (buf[1]<<12) + (buf[0]<<18);
    res += this.b2a((c>>>16)&255);
    if((i&3) == 3) res += this.b2a((c>>>8)&255);
   }

   return res;
  },
 };

/**
 * @namespace Hash functions
 * @author Anonymized
 * @description
 * <p>Hash functions and hashing.</p>
 * @requires encoding
 */
 var hashing = (function()
 {
  var sha1 =
  {
   name: 'sha1',
   identifier: '2b0e03021a',
   size: 20,
   block: 64,
  
   hash: function(s)
   {
    var len = (s+='\x80').length, blocks = len >> 6,
        chunck = len&63, res = "", i = 0, j = 0,
        H = [0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0],
        w = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
             0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
             0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
             0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];

    while(chunck++ != 56)
    {
     s+="\x00";
     if(chunck == 64){ blocks++; chunck = 0; }
    }

    for(s+="\x00\x00\x00\x00", chunck=3, len=8*(len-1); chunck >= 0; chunck--)
     s += encoding.b2a(len >> (8*chunck) &255);

    for(i=0; i < s.length; i++)
    {
     j = (j<<8) + encoding.a2b(s[i]);
     if((i&3)==3){ w[(i>>2)&15] = j; j = 0; }
     if((i&63)==63) this._round(H, w);
    }

    for(i=0; i < H.length; i++)
     for(j=3; j >= 0; j--)
      res += encoding.b2a(H[i] >> (8*j) & 255);

    return res;
   },

   _round: function(H, w)
   {
    var a = H[0], b = H[1], c = H[2], d = H[3], e = H[4], i = 0,
        k = [0x5A827999, 0x6ED9EBA1, 0x8F1BBCDC, 0xCA62C1D6],
        S = function(n,x){return (x << n)|(x >>> 32-n)}, tmp = 0,

    f = function(r, b, c, d)
    {
     if(r < 20) return (b & c) | (~b & d);
     if(r < 40) return b ^ c ^ d;
     if(r < 60) return (b & c) | (b & d) | (c & d);
     return b ^ c ^ d;
    }

    for(i=0; i < 80; i++)
    {
     if(i >= 16) w[i&127] = S(1, w[(i-3)&127]  ^ w[(i-8)&127]
                               ^ w[(i-14)&127] ^ w[(i-16)&127]);
     tmp = (S(5, a) + f(i, b, c, d) + e + w[i&127] + k[(i/20)&3])|0;
     e = d; d = c; c = S(30, b); b = a; a = tmp;
    }

    H[0] = (H[0]+a)|0; H[1] = (H[1]+b)|0; H[2] = (H[2]+c)|0;
    H[3] = (H[3]+d)|0; H[4] = (H[4]+e)|0;
   }
  };

  var sha256 =
  {
   name: 'sha-256',
   identifier: '608648016503040201',
   size: 32,
   block: 64,

   key: [0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
         0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
         0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
         0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
         0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
         0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
         0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
         0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2],

   hash: function(s)
   {
    var s = s + '\x80', len = s.length, blocks = len >> 6, chunck = len & 63,
       res = '', p = '', i = 0, j = 0, k = 0, l = 0,
       H = [0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a, 0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19],
       w = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];

    while(chunck++ != 56)
    {
     s+="\x00";
     if(chunck == 64){ blocks++; chunck = 0; }
    }

    for(s+="\x00\x00\x00\x00", chunck=3, len=8*(len-1); chunck >= 0; chunck--)
     s += encoding.b2a(len >> (8*chunck) &255);

    for(i=0; i < s.length; i++)
    {
     j = (j<<8) + encoding.a2b(s[i]);
     if((i&3)==3){ w[(i>>2)&15] = j; j = 0; }
     if((i&63)==63) this._round(H,w);
    }

    for(i=0; i < H.length; i++)
     for(j=3; j >= 0; j--)
      res += encoding.b2a(H[i] >> (8*j) & 255);

    return res;
   },

   _round: function(H,w)
   {
    var a = H[0], b = H[1], c = H[2], d = H[3], e = H[4],
        f = H[5], g = H[6], h = H[7], t = 0, u = 0, v = 0, tmp = 0;

    for(t=0; t < 64; t++)
    {
     if(t < 16) tmp = w[t&15];
     else
     {
      u = w[(t+1)&15]; v = w[(t+14)&15];
      tmp = w[t&15] = ((u>>>7  ^ u>>>18 ^ u>>>3  ^ u<<25 ^ u<<14) +
                       (v>>>17 ^ v>>>19 ^ v>>>10 ^ v<<15 ^ v<<13) +
                       w[t&15] + w[(t+9)&15]) | 0;
     }

     tmp = (tmp + h + (e>>>6 ^ e>>>11 ^ e>>>25 ^ e<<26 ^ e<<21 ^ e<<7)
            + (g ^ e & (f^g)) + this.key[t&63]);
     h = g; g = f; f = e; e = d + tmp | 0; d = c; c = b; b = a;
     a = (tmp + ((b&c) ^ (d&(b^c))) + (b>>>2 ^ b>>>13 ^ b>>>22 ^ b<<30 ^ b<<19 ^ b<<10)) | 0;
    }

    H[0]=H[0]+a|0; H[1]=H[1]+b|0; H[2]=H[2]+c|0; H[3]=H[3]+d|0;
    H[4]=H[4]+e|0; H[5]=H[5]+f|0; H[6]=H[6]+g|0; H[7]=H[7]+h|0;
   }
  };

  return {
/** SHA-256 hash function wrapper. This object can be used
  * to configure primitives that rely on a hash function,
  * for instance hashing.hmac_hash = hashing.sha256
  */
   sha256: sha256,

/** SHA1 hash function wrapper. This object can be used
  * to configure primitives that rely on a hash function,
  * for instance rsa.pss_hash = hashing.sha1
  */
   sha1: sha1,

/** SHA-256 helper function (hex output)
  * @param {string} m ASCII message to digest with SHA-256.
  * @returns {string} Hex string representing the hash.
  */
   SHA256: function(s)
   {
    return encoding.astr2hstr(this.sha256.hash(s));
   },

/** SHA1 helper function (hex output)
  * @param {string} m ASCII message to digest with SHA1.
  * @returns {string} Hex string representing the hash.
  */
   SHA1: function(s)
   {
    return encoding.astr2hstr(this.sha1.hash(s));
   },

/** The hash function to use for HMAC, hashing.sha256 by default
  */
   hmac_hash: sha256,

/** Hash-based message authentication code
  * @param {string} key key of the authentication
  * @param {string} msg message to authenticate
  * @returns {string} authentication code, as an hex string.
  */
   HMAC: function(key, msg)
   {
    var key = key+'', msg = msg+'', i = 0, h = this.hmac_hash,
        c = 0, p = '', inner = "", outer = "";

    if(key.length > h.block) key = h.hash(key);
    while(key.length < h.block) key += "\x00";

    for(i=0; i < key.length; i++)
    {
     c = encoding.a2b(key[i]);
     inner += encoding.b2a(c ^ 0x36);
     outer += encoding.b2a(c ^ 0x5C);
    }

    return encoding.astr2hstr(h.hash(outer + h.hash(inner + msg)));
   }
  };
 })();

/**
 * @namespace RSA Public Key cryptography
 * @author Anonymized
 * @description
 * <p>An implementation of PKCS#1 v2.1.</p>
 * <p>The main difference with other PKCS#1 implementations
 * is the format of the keys. Instead of using ASN.1 for
 * encoding, the keys are stored in an equivalent JSON object.
 * For a public key, the fields are 'n' for the modulus and
 * 'e' for the public exponent. In addition, a private key must
 * contain the CRT values 'dmp1', 'dmq1', 'p', 'q' and 'iqmp'
 * (the private exponent 'd' is not required because it is not
 * used for decryption; using BigInteger it is easy to compute
 * 'dmp1', 'dmq1' and 'iqmp' from 'd', 'p' and 'q').</p>
 * <p>Use the following PHP script (requires the openssl extension)
 * to convert a PKCS#1 key to JSON:</p>
 * <pre>#!/usr/bin/env php
 * &lt;?
 * if(count($argv)&lt;2) die("Usage: {$argv[0]} file.pem\n");
 * $f = "file://{$argv[1]}";
 * if(!($k = openssl_pkey_get_private($f)))
 *  dir("Failed to import private key {$argv[1]}.\n");
 * $d = openssl_pkey_get_details($k);
 * $pk = $d['rsa'];
 * foreach($pk as $p=&gt;$v) $pk[$p] = bin2hex($v);
 * echo json_encode($pk)."\n";</pre>
 * @requires BigInteger
 * @requires encoding
 * @requires hashing
 */
 var rsa =
 {
/** Label of OAEP encryption, an ASCII string empty by default.
  * Can be of any length since it will be hash using rsa.encryption_hash
  */
  label: '',

/** Salt of PSS signature, an ASCII string empty by default.
  * The max length is n-h-2 where n is the modulus size in bytes and h the
  * size in bytes of the output of the hash function.
  */
  salt: '',

/** Hash function to use for OAEP label (hashing.sha256 by default) */
  encryption_hash: hashing.sha256,

/** Hash function to use for MGF function (hashing.sha256 by default) */
  mgf_hash: hashing.sha256,

/** Hash function to use for PSS signature (hashing.sha256 by default) */
  signature_hash: hashing.sha256,

/** If something fails, this code provides information about the error.
  * <table width="100%"><tr><th>Code</th><th>Description</th></tr>
  * <tr><th>0</td><td>No error.</td></tr>
  * <tr><th>1</td><td>Message is too long for the modulus.</td></tr>
  * <tr><th>2</td><td>Invalid length of the input to decrypt or verify.</td></tr>
  * <tr><th>3</td><td>Top byte/bit is not zero after decryption/verification.</td></tr>
  * <tr><th>4</td><td>Incorrect padding of encrypted/signature data.</td></tr>
  * <tr><th>5</td><td>Bad label of OAEP encryption.</td></tr>
  * <tr><th>6</td><td>PSS salt is too long for modulus.</td></tr>
  * <tr><th>7</td><td>Invalid PSS padding byte in PSS signature.</td></tr>
  * </table> */
  error_code: 0,

/** RSAES-OAEP-ENCRYPT encryption.
  * @param {string} m Message to encode, an ASCII string
  * @param {publicKey} pub Public key
  * @returns {string} Hex string representing the encrypted message
  */
  encrypt: function(message, pub)
  {
   var m = encoding.astr2hstr(message)+'', l = m.length>>1,
       N = BigInteger.create(pub.n+''), E = BigInteger.create(pub.e+''),
       h = this.encryption_hash, n = BigInteger.bitLength(N)>>3,
       i = 0, DB = '', pad = '', sm = '', hs = h.size, w = this.label+'',
       seed = encoding.astr2hstr(h.hash(message+w));// Should be random

   print(n);    
   print(n-2*hs-2);
   print(l);    
   if(n-2*hs-2 < l){this.error_code = 1; return '' }
   for(i=0; i < n-2*hs-2-l; i++) pad += '00';
   DB = encoding.astr2hstr(h.hash(w)) + pad + '01' + m;

   // Mask
   pad = this.MGF(seed, n-hs-1);
   DB = BigInteger.toString(BigInteger.xor(BigInteger.create(DB),BigInteger.create(pad)));
   if(!!(DB.length&1)) DB = '0'+DB;

   // Final message
   sm = BigInteger.toString(BigInteger.xor(BigInteger.create(seed), BigInteger.create(this.MGF(DB, hs))));
   DB = BigInteger.toString(BigInteger.expMod(BigInteger.create(sm+DB), E, N));
   if(!!(DB.length&1)) DB = '0'+DB;

   this.error_code = 0;
   return DB;
  },

/** RSADP/RSASP1 - Computes m^d mod n using CRT coefficients.
  * @private
  * @param {string} message Hex-encoded message
  * @param {privateKey} priv Private key object
  * @returns {string} Hex string representing m^d mod n
  */
  _private: function(message, priv)
  {
   var C = BigInteger.create(message), dP = BigInteger.create(priv.dmp1),
       dQ = BigInteger.create(priv.dmq1), P = BigInteger.create(priv.p),
       Q = BigInteger.create(priv.q), qInv = BigInteger.create(priv.iqmp),
       M = BigInteger.create("0");

   // CRT decryption
   dP = BigInteger.expMod(C,dP,P); // m1 = c ^ dP mod p
   dQ = BigInteger.expMod(C,dQ,Q);// m2 = c ^ dQ mod q
   BigInteger.subTo(dP, dQ, M);
   BigInteger.multiplyTo(M, qInv, C);
   BigInteger.multiplyTo(Q, BigInteger.mod(C,P), M); // h = qInv * (m1 - m2) mod p
   BigInteger.subTo(dQ, BigInteger.negate(M), C); // m = m2 + h * q
   return BigInteger.toString(C);
  },

/** RSAES-OAEP-DECRYPT decryption.
  * @param {string} message Hex string containing the encrypted data
  * @param {privateKey} priv Private Key
  * @returns {string} ASCII string representing the original message, or an empty string if decryption failed.
  */
  decrypt: function(message, priv)
  {
   var m = message+'', l = m.length>>1,
       n = BigInteger.bitLength(BigInteger.create(priv.n+''))>>3,
       f = false, DB = '', sm = '', pad = '', i = 0,
       h = this.encryption_hash, hs = h.size;

   if(n != l){ this.error_code = 2; return "" }
   DB = this._private(m,priv);
   for(i = (n<<1)-DB.length; i>0; i--) DB = '0'+DB;

   // Parsing and unmasking
   for(i=0; i < DB.length; i++)
   {
    if(i<2){ if(DB[i] != '0'){ this.error_code = 3; return ''}}
    else if(i < 2*(hs+1)) sm += DB[i];
    else pad += DB[i];
   }

   DB = this.MGF(pad, hs);
   sm = BigInteger.toString(BigInteger.xor(BigInteger.create(sm), BigInteger.create(DB)));
   DB = this.MGF(sm, n-hs-1);
   DB = BigInteger.toString(BigInteger.xor(BigInteger.create(pad),BigInteger.create(DB)));
   if(!!(DB.length&1)) DB='0'+DB;

   // Unpadding
   m = ''; f = false; sm = '';
   for(i=0; i < DB.length; i++)
   {
    if(i < 2*hs){sm += DB[i]; continue;}
    pad = DB[i];
    if(f) m += pad;
    else
    {
     if(pad == "1"){ if(!(i&1)) break; else f = true; }
     else if(pad != "0") break;
    }
   }
   if(!sm){this.error_code = 4; return "" }
   if(sm != encoding.astr2hstr(h.hash(this.label))){ this.error_code = 5; return "" }

   this.error_code = 0;
   return encoding.hstr2astr(m);
  },

/** RSASSA-PSS-SIGN signature using rsa.signature_hash.
  * @param {string} message ASCII string containing the data to sign
  * @param {privateKey} priv Private Key
  * @returns {string} Hex string representing a PSS signature for the data
  */
  sign: function(message, priv)
  {
   var h = this.signature_hash, m = h.hash(message+''),
       DB = '', sm = '', pad = '', salt = this.salt+'',
       sl = salt.length, i = 0, hs = h.size,
       n = BigInteger.bitLength(BigInteger.create(priv.n+''))>>3;

   if(n-hs-2 < sl){this.error_code = 6; return ""}
   m = encoding.astr2hstr(h.hash("\x00\x00\x00\x00\x00\x00\x00\x00"+m+salt));
   sm = "01"+encoding.astr2hstr(salt);
   for(i = sm.length>>1; i < n-sl-hs-2; i++) pad+="00";
   DB = this.MGF(m, n-hs-1);

   // Most significant bit - PSS could be using a byte like OAEP...
   sm = (+('0x'+(0<DB.length?DB[0]:"0"))>>3==0?"00":"80") + pad + sm;
   DB = BigInteger.toString(BigInteger.xor(BigInteger.create(DB), BigInteger.create(sm)));
   DB += m+'bc';

   DB = this._private(DB, priv);
   if(!!(DB.length&1)) DB='0'+DB;
   this.error_code = 0;
   return DB;
  },

/** EMSA-PKCS1-v1_5-ENCODE
  * @private
  */
  _pkcs1_sig_pad: function(m, n)
  {
   var h = this.signature_hash, m = h.hash(m+''),
       res = '', pad = '', i = 0;

   // DER octet string of hash
   m = "04"+encoding.b2h(h.size)+encoding.astr2hstr(m);
   res = h.identifier + '';
   res = '06'+encoding.b2h(res.length>>1)+res+'0500';
   res = '30'+encoding.b2h(res.length>>1)+res+m;
   res = '0030'+encoding.b2h(res.length>>1)+res;
   for(i=res.length>>1; i < n-2; i++) pad += "ff";
   return '0001'+pad+res;
  },

/** RSASSA-PKCS1-V1_5-SIGN signature using rsa.signature_hash.
  * @param {string} message ASCII string containing the data to sign
  * @param {privateKey} priv Private Key
  * @returns {string} Hex string representing a PKCS1v1.5 signature for the data
  */
  sign_pkcs1_v1_5: function(message, priv)
  {
   var res = '',
       n = BigInteger.bitLength(BigInteger.create(priv.n+''))>>3;

   res = this._private(this._pkcs1_sig_pad(message, n), priv);
   if(!!(res.length&1)) res = '0'+res;

   this.error_code = 0;
   return res;
  },

/** RSASSA-PSS-VERIFY signature verification using rsa.signature_hash.
  * @param {string} data ASCII string containing the signed data
  * @param {string} signature Hex string containing the signature of the data
  * @param {publicKey} pub Public key of the expected sender
  * @returns {boolean} whether s is a valid signature for m from pub
  */
  verify: function(data, signature, pub)
  {
   var h = this.signature_hash, hs = h.size,
       m = h.hash(data+''), s = signature+'',
       N = BigInteger.create(pub.n+''), k = s.length>>1,
       E = BigInteger.create(pub.e+''), n = BigInteger.bitLength(N)>>3,
       i = 0, DB = '', sm = '', pad = '', f = false;

   if(k != n){this.error_code = 2; return false }
   s = BigInteger.toString(BigInteger.expMod(BigInteger.create(s), E, N));

   while(s.length != 2*n) s='0'+s;
   if(+(0<s.length?s[0]:'0')>>3 != 0){this.error_code = 3; return false }

   for(i=0; i<s.length; i++)
   {
    if(i < 2*(n-hs-1)) DB += s[i];
    else if(i < 2*(n-1)) sm += s[i];
    else pad += s[i];
   }

   if(pad != "bc"){ this.error_code = 7; return false }
   s = sm; sm = this.MGF(sm, n-hs-1);

   DB = BigInteger.toString(BigInteger.xor(BigInteger.create(DB), BigInteger.create(sm)));
   if(!!(DB.length&1)) DB='0'+DB;

   sm = "";
   for(i=0; i < DB.length; i++)
   {
    pad = DB[i];
    if(!i){ if(pad != "0" && pad != "8") return false; }
    else if(f) sm += pad;
    else
    {
     if(pad == "1" && !!(i&1)){f = true; continue;}
     if(pad != "0"){ this.error_code = 4; return false }
    }
   }

   sm = encoding.hstr2astr(sm);
   this.error_code = 0;
   return encoding.astr2hstr(h.hash("\x00\x00\x00\x00\x00\x00\x00\x00"+m+sm)) == s;
  },

/** RSASSA-PKCS1-V1_5-VERIFY signature verification using rsa.signature_hash.
  * @param {string} data ASCII string containing the signed data
  * @param {string} signature Hex string containing the signature of the data
  * @param {publicKey} pub Public key of the expected sender
  * @returns {boolean} whether s is a valid signature for m from pub
  */
  verify_pkcs1_v1_5: function(data, signature, pub)
  {
   var N = BigInteger.create(pub.n+''), E = BigInteger.create(pub.e+''),
       s = signature+'', k = s.length >> 1, n = BigInteger.bitLength(N)>>3,
       res = this._pkcs1_sig_pad(data, n);

   if(k != n){this.error_code = 2; return false }
   s = BigInteger.toString(BigInteger.expMod(BigInteger.create(s), E, N));
   while(s.length != 2*n) s='0'+s;
   return s == res;
  },

/** MGF1 message generating function. Underlying hash function is rsa.mgf_hash
  * @param {string} seed Hex string containing the seed for message generation
  * @param {number} length Length n of the requested message in bytes
  * @returns {string} Hex string of the desired length
  */
  MGF: function(seed, length)
  {
   var res = '', c = '', i = 0, j = 0, h = this.mgf_hash,
       len = length<<1, hs = h.size,
       n = (length/hs |0) + (!(length%hs) ? 0 :1);

   for(i=0; i<n; i++)
   {
    for(c = '', j = 0; j < 4; j++)
     c += encoding.b2h((i>>(24-8*j))&255);

    c = encoding.astr2hstr(h.hash(encoding.hstr2astr(seed+c)));
    for(j=0; j < c.length; j++)
    {
     res += c[j];
     if(res.length == len) return res;
    }
   }
   return res;
  }
 };

 function _asnhex_getByteLengthOfL_AtObj(s, pos) {
  if (s.substring(pos + 2, pos + 3) != '8') return 1;
  var i = parseInt(s.substring(pos + 3, pos + 4));
  if (i == 0) return -1;
  if (0 < i && i < 10) return i + 1;
  return -2;
}

function _asnhex_getHexOfL_AtObj(s, pos)
{
  var len = _asnhex_getByteLengthOfL_AtObj(s, pos);
  if (len < 1) return '';
  return s.substring(pos + 2, pos + 2 + len * 2);
}

function _asnhex_getIntOfL_AtObj(s, pos)
{
  var hLength = _asnhex_getHexOfL_AtObj(s, pos);
  if (hLength == '') return -1;
  var bi;
  if (parseInt(hLength.substring(0, 1)) < 8) {
     bi = parseInt(hLength,16);
  } else {
     bi = parseInt(hLength.substring(2), 16);
  }
  return bi;
}

function _asnhex_getStartPosOfV_AtObj(s, pos) 
{
  var l_len = _asnhex_getByteLengthOfL_AtObj(s, pos);
  if (l_len < 0) return l_len;
  return pos + (l_len + 1) * 2;
}

function _asnhex_getHexOfV_AtObj(s, pos) 
{
  var pos1 = _asnhex_getStartPosOfV_AtObj(s, pos);
  var len = _asnhex_getIntOfL_AtObj(s, pos);
  return s.substring(pos1, pos1 + len * 2);
}

function _asnhex_getPosOfNextSibling_AtObj(s, pos) 
{
  var pos1 = _asnhex_getStartPosOfV_AtObj(s, pos);
  var len = _asnhex_getIntOfL_AtObj(s, pos);
  return pos1 + len * 2;
}

function _asnhex_getPosArrayOfChildren_AtObj(h, pos) 
{
  var a = new Array();
  var p0 = _asnhex_getStartPosOfV_AtObj(h, pos);
  a.push(p0);

  var len = _asnhex_getIntOfL_AtObj(h, pos);
  var p = p0;
  var k = 0;
  while(1)
  {
    var pNext = _asnhex_getPosOfNextSibling_AtObj(h, p);
    if (pNext == null || (pNext - p0  >= (len * 2))) break;
    if (k >= 200) break;

    a.push(pNext);
    p = pNext;
    k++;
  }

  return a;
}

parsePK = function(s)
{
  s = s.replace("-----BEGIN RSA PRIVATE KEY-----", "");
  s = s.replace("-----END RSA PRIVATE KEY-----", "");
  s = encoding.base64_decode(s);
  var hPrivateKey = encoding.astr2hstr(s);
  var v1 = _asnhex_getStartPosOfV_AtObj(hPrivateKey, 0);
  var n1 = _asnhex_getPosOfNextSibling_AtObj(hPrivateKey, v1);
  var e1 = _asnhex_getPosOfNextSibling_AtObj(hPrivateKey, n1);
  var d1 = _asnhex_getPosOfNextSibling_AtObj(hPrivateKey, e1);
  var p1 = _asnhex_getPosOfNextSibling_AtObj(hPrivateKey, d1);
  var q1 = _asnhex_getPosOfNextSibling_AtObj(hPrivateKey, p1);
  var dp1 = _asnhex_getPosOfNextSibling_AtObj(hPrivateKey, q1);
  var dq1 = _asnhex_getPosOfNextSibling_AtObj(hPrivateKey, dp1);
  var co1 = _asnhex_getPosOfNextSibling_AtObj(hPrivateKey, dq1);
  var v =  _asnhex_getHexOfV_AtObj(hPrivateKey, v1);
  var n =  _asnhex_getHexOfV_AtObj(hPrivateKey, n1);
  var e =  _asnhex_getHexOfV_AtObj(hPrivateKey, e1);
  var d =  _asnhex_getHexOfV_AtObj(hPrivateKey, d1);
  var p =  _asnhex_getHexOfV_AtObj(hPrivateKey, p1);
  var q =  _asnhex_getHexOfV_AtObj(hPrivateKey, q1);
  var dp = _asnhex_getHexOfV_AtObj(hPrivateKey, dp1);
  var dq = _asnhex_getHexOfV_AtObj(hPrivateKey, dq1);
  var co = _asnhex_getHexOfV_AtObj(hPrivateKey, co1);
  return {n:(n), e:(e), d:(d), p:(p), q:(q), dmp1:(dp), dmq1:(dq), iqmp:(co)};
}

var pk_str = "-----BEGIN RSA PRIVATE KEY-----" + 
"MIICXAIBAAKBgQDSbCFDl5kOdzPU0WY5hsIgnOE788JjktUu/qqDbtebr6J2Tj5s" +
"4QZsgFMu/QXIdkK/qEFJ3kkdJ3MPrEH8ZPkqq2bOLVAC+pWZ9oMWy3ke1NNbtqPb" +
"xGMXIfPD8rfNn4a8vXhoiJuyLn2hSENw14qBCLn7o8NaNvXehP7IWi/QHwIDAQAB" +
"AoGAO6O4Hd9G20m/4A7lqWJffTzZvdNOAkjosWQu0gfFhnPWWS++E8AHwRLyALxo" +
"OjQCoS3AK36cPG4k94k8Ppwj3odMbRMyTMG70ZB1+mXK2OXShdIyFZ3Oa/9/kBmh" +
"CKZT/6bV3Zr8/Ml+KqrCjm8QCntNLPYkHAo2VB+Er4QhTeECQQD4g8oNUWNbvIrK" +
"bfT1x9GUPUxe53JYt1D1SJiwL7ApiR6LnoLHw5ApPJUZJxaHngDV8e+WnzY2luLt" +
"jbwcOnaPAkEA2MKfk7PtCIiqOq8XqrQI7QUz3momW+rs/9eJ5eMyPTa+laqzsv++" +
"z7KxBHBPUcchX6yMZf4WDLbvRck89rxVcQJACJiudZV6JWM5PdVd4t6dnk4chS/m" +
"YbE9qK5xMa8EnfszRkseZQCbzQFeevdCLUDG8J+k1QX+3xaLFQKRHjzbewJAHC3x" +
"IPqbLijWOJrasM6G+olanOef5QM9nGUhAEnxFhQv4rU2d2bYH5hTewg+x5rjs9Ry" +
"zC+kHjMKGEB5NHp3gQJBAIWCIuTG7lgQjA3DiY+lOWgBH4CQbt1z0sKaQBpTl45C" +
"E2g/H3V4oJhpxlosAfqlIV8GKfyar3MLXSD+0/YsdW4=" + 
"-----END RSA PRIVATE KEY-----"

function decode(s, type)
{
 switch(+type)
 {
  case 1: return encoding.utf8_decode(s);
  case 2: return encoding.hstr2astr(s);
  case 3: return encoding.base64_decode(s);
  default: return s;
 }
}

function encode(s, type)
{
 switch(+type)
 {
  case 1: return encoding.utf8_encode(s);
  case 2: return encoding.astr2hstr(s);
  case 3: return encoding.base64_encode(s);
  case 4: return encoding.base64_urlencode(s);
  default: return s;
 }
}

var pk = parsePK(pk_str);

var d = decode("this is a secret message", 1);

var e = encode(encoding.hstr2astr(rsa.encrypt(d, pk)), 3);

print(e);
