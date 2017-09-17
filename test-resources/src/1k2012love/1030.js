/**
         * An almost fully implemented markdown parser in less than 1025 bytes
         * @example markdown(node.textContent || textarea.value || genericString)
         * @param   {string} input the generic text to parse via markdown
         * @return  {string} the input as html
         */
        function markdown(input) {"use strict";
          /*!
           * @license (C) WebReflection - Mit Style
           */
          for(var
            // some common RegExp shortcut
            CODE_PRE = "</code></pre>",
            BLOCKQUOTE = "blockquote>",
            NL = "(?:\\r\\n|\\r|\\n|$)",
            ANYTHING = "(.+?)" + NL,
            // temporary container for code blocks ... these won't be parsed
            L = [],
            // list of search, replacement for the given input
            re = [
              // make HTML safe
              "&(?!#?[a-z0-9]+;)", "&amp;", "<", "&lt;", ">", "&gt;",
              // drop code blocks from the input, no parsing required
              "^(?:\\t| {4})" + ANYTHING, function(m, s){return L.push(s + "\n") && "\0"},
              // parse H1 and H2
              "^" + ANYTHING + "=+" + NL, "<h1>$1</h1>\n",
              "^" + ANYTHING + "-+" + NL, "<h2>$1</h2>\n",
              // parse H1, H2, H3, H4, H5, H6 with other notation
              "^(#+)\\s*" + ANYTHING, function(m, c, s, t){
                return "<h" + (t = c.length) + ">" + s.replace(/#+$/, "") + "</h" + t + ">\n"
              },
              // parse HR lines
              "(?:\\* \\* |- - |\\*\\*|--)[-*][-* ]*" + NL, "<hr/>\n",
              // parse manual br
              "  +" + NL, "<br/>",
              // parse ordered and unordered lists
              "^ *(\\* |\\+ |- |\\d+. )" + ANYTHING, function(m, c, s, t){
                return "<" + (t = /^\d/.test(c) ? "ol>" : "ul>") + "<li>" + markdown(s) + "</li></" + t
              },
              // remove superflous parsing
              "</(ul|ol)>\\s*<\\1>", "",
              // parse strong and em
              "([_*]{1,2})([^\\2]+?)(\\1)", function(m, c, s, t){
                return "<" + (t = c.length == 2 ? "strong>" : "em>") + s + "</" + t
              },
              // parse blockquotes
              "\\[(.+?)\\]\\((.+?) (\"|')(.+?)(\\3)\\)", '<a href="$2" title="$4">$1</a>',
              "^&gt; " + ANYTHING, function(m, s){
                return "<" + BLOCKQUOTE + markdown(s)+ "</" + BLOCKQUOTE
              },
              // remove superflous parsing
              "</" + BLOCKQUOTE + "\\s*<" + BLOCKQUOTE, "",
              // parse backtick for inline code
              "(`{1,2})([^\\r\\n]+?)\\1", "<code>$2</code>",
              // put back blocks of code
              "\\0", function(s){
                return "<pre><code>" + L.shift() + CODE_PRE
              },
              // clean up superflous parsing
              CODE_PRE + "\\s*<pre><code>", ""
            ],
            i = 0; i < re.length;
          ) input = input.replace(RegExp(re[i++], "gm"), re[i++]);
          return input;
        }