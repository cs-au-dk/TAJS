




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
 * @namespace AES-256 encryption and associated modes
 * @author Anonymized
 * @description
 * <p>Implementation of AES on 256 bit keys.</p>
 * @requires encoding
 */
 var aes =
 {
  Stables: (function()
  {
   var a256 = function()
   {
    return [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
   },

   t5 = function(){return [a256(), a256(), a256(), a256(), a256()]},
   encTable = t5(), decTable = t5(),
   sbox = encTable[4], sboxInv = decTable[4],
   i = 0, x = 0, xInv = 0, x2 = 0, x4 = 0, x8 = 0,
   tEnc = 0, tDec = 0, s = 0, d = a256(), th = a256();

   for(i=0; i < 256; i++)
    th[((d[i & 255] = i<<1 ^ (i>>7)*283)^i) & 255] = i;

   for(x=xInv=0; !sbox[x&255]; x^=(!x2?1:x2), xInv=th[xInv&255], xInv=(!xInv?1:xInv))
   {
    s = xInv ^ xInv<<1 ^ xInv<<2 ^ xInv<<3 ^ xInv<<4;
    s = s>>8 ^ s&255 ^ 99;
    sbox[x&255] = s; sboxInv[s&255] = x;

    x8 = d[(x4 = d[(x2 = d[x&255])&255])&255];
    tDec = x8*0x1010101 ^ x4*0x10001 ^ x2*0x101 ^ x*0x1010100;
    tEnc = d[s&255]*0x101 ^ s*0x1010100;

    for (i=0; i<4; i++)
    {
     encTable[i&3][x&255] = tEnc = tEnc<<24 ^ tEnc>>>8;
     decTable[i&3][s&255] = tDec = tDec<<24 ^ tDec>>>8;
    }
   }

   return [encTable, decTable];
  })(),

  key: (function()
  {
   var a = function(){ return [
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   ]};
   return [a(),a()];
  })(),

/** Set the key to use for encryption and decryption.
  * @param {string} key ASCII key (32 bytes long)
  */
  setKey: function(key)
  {
   var pad = function(s){var s=s+'';while(s.length<64) s+="0"; return s},
       key = this._blockGen(pad(key),true), i = 0, j = 0,
       k = [0,0,0,0,0,0,0,0], block = [0,0,0,0];

   for(i=0; i<2; i++)
   {
    block = key.gen();
    for(j=0; j<4; j++) k[(4*i+j)&7] = block[j&3];
   }

   this._setKey(k);
  },

  _setKey: function(key)
  {
   var i = 0, j = 0, rcon = 1, tmp = 0,
      encKey = this.key[0],
      decKey = this.key[1],
    decTable = this.Stables[1],
        sbox = this.Stables[0][4];

   for(i = 0; i < 60; i++)
   {
    if(i < 8)
    {
     encKey[i & 63] = key[i & 7];
     continue;
    }

    tmp = encKey[(i-1) & 63];
    if(!(i%4))
    {
     tmp = sbox[tmp>>>24 & 255]<<24
         ^ sbox[tmp>>16  & 255]<<16
         ^ sbox[tmp>>8   & 255]<<8
         ^ sbox[tmp & 255];

     if(!(i%8))
     {
      tmp = tmp<<8 ^ tmp>>>24 ^ rcon<<24;
      rcon = rcon<<1 ^ (rcon>>7)*283;
     }
    }

    encKey[i & 63] = encKey[(i-8) & 63] ^ tmp;
   }
  
   for(j = 0; i>0; j++, i--)
   {
    tmp = encKey[(!(j&3) ? i-4 : i)&63];

    decKey[j & 63] =
     (i<=4 || j<4) ? tmp :
     decTable[0][sbox[tmp>>>24 & 255] & 255] ^
     decTable[1][sbox[tmp>>16  & 255] & 255] ^
     decTable[2][sbox[tmp>>8   & 255] & 255] ^
     decTable[3][sbox[tmp      & 255] & 255];
   }
  },

/** Internal AES block function.
  * @param {number array[4]} input array of four 32-bit words to process
  * @param {boolean} dir false for encryption, true for decryption
  * @returns {number array[8]} result of the encryption
  */
  _aes: function(input, dir)
  {
   var key = this.key[(!dir ? 0 : 1) & 1],
         a = input[0] ^ key[0],
         b = input[(!dir ? 1 : 3) & 3] ^ key[1],
         c = input[2] ^ key[2],
         d = input[(!dir ? 3 : 1) & 3] ^ key[3],
        a2 = 0, b2 = 0, c2 = 0, i = 0, kIndex = 4,
       out = [0, 0, 0, 0],
     table = this.Stables[(!dir ? 0 : 1 ) & 1],
        t0 = table[0], t1 = table[1], t2 = table[2],
        t3 = table[3], sbox = table[4];

   for(i = 0; i < 13; i++)
   {
    a2 = t0[a>>>24 & 255] ^ t1[b>>16 & 255] ^ t2[c>>8 & 255] ^ t3[d & 255] ^ key[kIndex & 63];
    b2 = t0[b>>>24 & 255] ^ t1[c>>16 & 255] ^ t2[d>>8 & 255] ^ t3[a & 255] ^ key[(kIndex + 1) & 63];
    c2 = t0[c>>>24 & 255] ^ t1[d>>16 & 255] ^ t2[a>>8 & 255] ^ t3[b & 255] ^ key[(kIndex + 2) & 63];
    d  = t0[d>>>24 & 255] ^ t1[a>>16 & 255] ^ t2[b>>8 & 255] ^ t3[c & 255] ^ key[(kIndex + 3) & 63];
    kIndex += 4; a = a2; b = b2; c = c2;
   }
        
   for(i = 0; i < 4; i++)
   {
    out[(!dir ? i : (3&-i)) & 3] =
    sbox[a>>>24 & 255]<<24 ^ 
    sbox[b>>16  & 255]<<16 ^
    sbox[c>>8   & 255]<<8  ^
    sbox[d      & 255]     ^
    key[kIndex++ & 63];
    a2=a; a=b; b=c; c=d; d=a2;
   }

   return out;
  },

/** Block generator function, with PKCS#5 support for padding.
  * @private
  * @param {string} s input string to process in blocks
  * @param {boolean} dir false for encryption, true for decryption (no padding)
  * @returns {blocks:number,gen:()->number array[4]} A record containing the number of blocks and the block generating function
  */
  _blockGen: function(s, dir)
  {
   var s = s+'', len = s.length, block = 0, i = 0, e = len&15,
       blocks = (!dir&&!e?1:0)+(!e?0:1)+(len>>4), pad = (blocks<<4)-len,

   gen = function()
   {
    var res = [0,0,0,0], i = 0, j = 0,
        m = 0, base = block++ << 4, tmp = 0;

    for(i = 0; i < 4; i++)
    {
     for(tmp = 0, j = base+4*i, m = j+4; j < m; j++)
      tmp = (tmp<<8)+encoding.a2b((j>>>=0)<s.length ? s[j] : "\x00");
     res[i&3] = tmp;
    }
    return res;
   };

   if(!dir) for(i=0; i<pad; i++) s += encoding.b2a(pad);
   else while(!!(e++%16)) s += "\x00";

   return {blocks: blocks, gen: gen};
  },

/** Output processing. By default, returns an ASCII string.
  * @private
  * @param {number array[4]} block internal block to output
  * @param {boolean} last true if this is the last block, false otherwise
  * @returns {string} ASCII string representing the input block. Will unpad if this is the last block.
  */
  _output: function(block, last)
  {
   var res = "", i = 0, j = 0, c = 0, pad = 16; 

   if(last) pad -= 1+block[3]&255;

   for(i=0; i < 4; i++)
    for(c = block[i&3], j=0; j<4 && res.length <= pad; j++)
     res += encoding.b2a(c >> (24-8*j) &255);

   return res;
  },

  _xor4: function(x,y)
  {
   return [x[0]^y[0], x[1]^y[1], x[2]^y[2], x[3]^y[3]];
  },

/** CBC mode encryption and decryption using AES.
  * @param {string} s input plaintext or ciphertext (ASCII string)
  * @param {string} iv initial vector of the encryption, a 16 bytes ASCII string
  * @param {boolean} dir false for encryption, true for encryption
  * @returns {string} result as an ASCII string
  */
  CBC: function(s, iv, dir)
  {
   var  i = 0, res = "", last = false,
    input = this._blockGen(s, dir),
       iv = this._blockGen(iv, true).gen(),
    block = [0,0,0,0],
      xor = this._xor4;

   for(i=0; i<input.blocks; i++)
   {
    block = input.gen();

    if(!dir)
    {
     iv = this._aes(xor(iv,block), false);
     res += this._output(iv, false);
    }
    else
    {
     res += this._output(xor(iv, this._aes(block,true)), i+1 == input.blocks);
     iv = block;
    }
   }

   return res;
  },

  /** Authenticated encryption in CCM mode (provides ciphertext integrity).
   * @param {string} s input plaintext (ASCII string)
   * @param {string} iv Random initialization vector, 16 byte ASCII string
   * @param {string} adata Optional authentication data (not secret but integrity protected)
   * @param {number} tlen tag length in bytes (2 to 16) - high values make it harder to tamper with the ciphertext
   * @return {string} the encrypted data, an ASCII string
   */
  CCM_encrypt: function(s, iv2, adata, tlen)
  {
   var tlen = (tlen<4 || tlen>16 || !!(tlen&1)) ? 8 : tlen,
       s=s+'', sl=s.length, ol=sl>>3, L=0, i=0, iv = '',
       tag = '', res = {data:'', tag:''};

   for(L=2; L<4 && !!(ol>>>8*L); L++);
   for(iv2+='';iv2.length < 16; iv2+="\x00");
   for(i=0; i<iv2.length; i++){ iv += iv2[i]; if(i>13-L) break }
 
   tag = this._ccmTag(s, iv, adata, tlen, L);
   res = this._ctrMode(s, iv, tag, tlen, L);

   return res.data + res.tag;
  },
  
  /** Decryption in CCM mode.
   * @param {string} s input ciphertext
   * @param {string} iv random initialization vector (ASCII string)
   * @param {string} adata Optional authenticated data (ASCII)
   * @param {number} tlen tag length in bytes
   * @return {valid:bolean,data:string} Object containing the decrypted data and authentication status
   */
  CCM_decrypt: function(s, iv2, adata, tlen)
  {
   var tlen = (tlen<4 || tlen>16 || !!(tlen&1)) ? 8 : tlen,
       s=s+'', sl=s.length, c = '', ol=(sl-tlen)>>3, L=0,
       i=0, res = {data:'',tag:''}, tag = '', iv = '';

   for(i=0; i<s.length; i++)
   {
    if(i < sl-tlen) c += s[i];
    else tag += s[i];
   }

   for(L=2; L<4 && !!(ol>>>8*L); L++);
   for(iv2+='';iv2.length < 16; iv2+="\x00");
   for(i=0; i<iv2.length; i++){ iv += iv2[i]; if(i>13-L) break }

   res = this._ctrMode(c, iv, tag, tlen, L);
   s = this._ccmTag(res.data, iv, adata, tlen, L);

   return {valid: s==res.tag, data: res.data};
  },

  _ccmTag: function(s, iv, adata, tlen, L)
  {
   var i=0, s=s+'', sl=s.length, xor = this._xor4, c = [0,0,0,0],
       ad = (function(x){var x=x+'', n=x.length, c=function(n){
       return encoding.b2a(n)}; if(!n) return x; if(n<=0xFEFF)
       return c(n>>16)+c(n&255)+x; return "\xff\xfe"+c(n>>>24)+
       c(n>>16&255)+c(n>>8&255)+c(n&255)+x})(adata), res = '', T = '',
       p = this._blockGen(s, true), q = this._blockGen(ad, true);

   T = encoding.b2a(((adata==''?0:1)<<6) | (((tlen-2)>>1)<<3) | (L-1))+iv;
   for(i=15-T.length; i>=0; i--) T += encoding.b2a(i>3?0:sl>>>8*i);
   c = this._aes(this._blockGen(T,true).gen(), false);

   if(!!ad) // Additional data
    for(i=0; i<q.blocks; i++) c = this._aes(xor(c, q.gen()), false);
   for(i=0; i<p.blocks; i++) c = this._aes(xor(c, p.gen()), false);

   T = this._output(c, false);
   for(i=0; i<T.length; i++)
   {
    res += T[i];
    if(i+1 == tlen) break;
   }

   return res;
  },

  _ctrMode: function(s, iv, tag, tlen, L)
  {
   var ctr = this._blockGen(encoding.b2a(L-1)+iv, true).gen(), tag0=tag,
       xor = this._xor4, res = "", D = this._blockGen(s, true), sl=s.length,
       tag = xor(this._blockGen(tag, true).gen(), this._aes(ctr, false)),
       ts = '', i = 0, c = "", j = 0;

   c = this._output(tag, false);
   for(i=0; i<c.length; i++){ ts+=c[i]; if(i+1==tlen) break; }

   for (i=0; i<D.blocks; i++)
   {
    ctr[3]++;
    c = this._output(xor(D.gen(), this._aes(ctr, false)), false);
    for(j=0; j<c.length; j++)
    {
     res += c[j];
     if(res.length == sl) break;
    }
   }

   return {tag:ts, data:res};
  }
 };



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

var k = decode("f1f991b0e5bd505a39385709d26e777b8578b59efcddfaaffa2e6bfa50c5f863", 2);
aes.setKey(k);
var data = decode("this is a secret message", 0);
var iv = decode("iVmTuZpSBILIb+xJ1cT0Gg==", 3);

print(encode(aes.CBC(data, iv, true), 1));