// Comments and variables in portuguese

      // Classe que representa o ambiente do jogo
      // Receb a largura e altura do campo, e o tamanho de cada celula
      // Construtor recebe: Int width,
      //                    Int height,
      //                    Int tamanhoCelula
      function GameOfLife(width,height,tamanhoCelula){
        this.width         = width;    // Atencao! Essas variaveis armazenam o tamanho do campo,
        this.height        = height;   // e nao do canvas!!!
        this.tamanhoCelula = tamanhoCelula;
        this.grid          = new Array();

        // ----- Funcoes de campo ----- //

        // Funcao que inicializa o grid do jogo
        // Recebe: Nada
        // Retorna: Nada
        this.start = function(){
          for(var i = 0; i < this.width; i++){
            this.grid[i] = new Array();
            for(var j = 0; j < this.height; j++){
              this.grid[i][j] = false;
            }
          }
        };

        // Funcao que cria celulas vivas aleatoriamente
        // Recebe um valor que representa qual a porcentagem media de celulas vivas
        // Recebe: Int mediaVivas
        // Retorna: Nada
        this.campoAleatorio = function(mediaVivas){
          for(var i = 0; i < this.width; i++){
            for(var j = 0; j < this.height; j++){
              this.grid[i][j] = Math.floor(Math.random() * 100/mediaVivas) == 0;
            }
          }
        }

        // ----- Funcoes de ponto ----- //

        // Funcao que transforma uma celula em viva ou morta
        // Recebe a localizacao da celula e o estado novo da mesma
        // Recebe: Int x,
        //         Int y,
        //         Boolean viva
        // Retorna: Nada
        this.setarPonto = function(x,y,viva){
          this.grid[x][y] = viva;
        }

        // Funcao que transforma a posicao do pixel no canvas em posicao no grid
        // Recebe a posicao do pixel no canvas (sendo 0 o primeiro pixel e (b*a) -1 o último, sendo b a base
          // do canvas e a a altura) e a largura do canvas
        // Recebe: Int i
        //         Int widthCanvas
        // Retorna: Objeto = { Int x, Int y }
        this.posicaoNoGrid = function(i, widthCanvas){
          var gridX = Math.floor((i%widthCanvas)/this.tamanhoCelula),
              gridY = Math.floor((i/widthCanvas)/this.tamanhoCelula);
          return {x: gridX, y: gridY};
        }

        // Funcao que calcula quantas vizinhas uma celula tem
        // Recebe: Int x,
        //         Int y
        // Retorna: Int
        this.vizinhas = function(x,y){
          var quantidade = 0;
          // Checagem para bordas superior e esquerda
          if(x == 0){
            x = this.width;
          }
          if(y == 0){
            y = this.height;
          }

          for(var i = x-1; i < x+2; i++){
            for(var j = y-1; j < y+2; j++){
              if(!(i == x && j == y)){
                var posX = i % this.width,
                  posY = j % this.height;
                if(this.grid[posX][posY]){
                  quantidade++;
                }
              }
            }
          }
          return quantidade;
        }

        // ----- Funcoes estrututrais ----- //

        // Funcao que executa um passo no sistema
        // Recebe: Nada
        // Retorna: Nada
        this.step  = function(){
          var novoGrid = new Array();
          for(var i = 0; i < this.width; i++){
            novoGrid[i] = new Array();
            for(var j = 0; j < this.height; j++){
              novoGrid[i][j] = false;
            }
          }
          for(var i = 0; i < this.width; i++){
            for(var j = 0; j < this.height; j++){
              var numVizinhas = this.vizinhas(i,j);
              if(this.grid[i][j]){
                if(numVizinhas == 2 || numVizinhas == 3){
                  novoGrid[i][j] = true;
                }
              }else{
                if(numVizinhas == 3){
                  novoGrid[i][j] = true;
                }
              }
            }
          }
          this.grid = novoGrid;
        }

        // Funcao que desenha o campo
        // Recebe: CanvasRenderingContext2D ctx
        // Retorna: Nada
        this.desenhaGrid = function(ctx){
          var pixels = ctx.createImageData(this.width*this.tamanhoCelula,this.height*this.tamanhoCelula),
              tamanho = pixels.data.length,
              widthCanvas = this.width * this.tamanhoCelula;
          for(var i = 0; i < (tamanho/4); i++){
            var posicao = this.posicaoNoGrid(i, widthCanvas),
              gridX   = posicao.x,
              gridY   = posicao.y;
            if(this.grid[gridX][gridY]){
              pixels.data[(4*i) + 0] = 0;
              pixels.data[(4*i) + 1] = 0;
              pixels.data[(4*i) + 2] = 0;
              pixels.data[(4*i) + 3] = 255;
            }
          }
          ctx.putImageData(pixels,0,0);
        }

      }
    
      function $(id){
        return document.getElementById(id);
      }

      // Ciclo do Game Of Life
      function animacaoJogo(){
        __life.step();
        __life.desenhaGrid(window.ctx);
        window.idTimeoutJogo = setTimeout(animacaoJogo, 10);
      }

      // Funcao que inicia/pausa o Game Of Life
      function toggleGame(){
        if(window.idTimeoutJogo){
          clearTimeout(window.idTimeoutJogo);
          window.idTimeoutJogo = undefined;
        }else{
          window.idTimeoutJogo = setTimeout(animacaoJogo, 10);
        }
      }

      // Funcao principal
      function main(){
        var mensagem = document.createElement("p");
        mensagem.style.cssText="position:absolute;top:0;background:gray";
        mensagem.id = "toggleJogo";
        mensagem.innerHTML = "Click anywhere to stop/continue";
        b.appendChild(mensagem);

        // Tamanho de cada celula (deve sempre ser um divisor comum da altura e largura)
        var tamanhoCelula = 2; 

        var canvas = a;
        window.ctx = c;

        // Especificando o tamanho do campo, considerando as dimensoes de cada celula e do canvas
        var largura = canvas.width/tamanhoCelula,
            altura  = canvas.height/tamanhoCelula;
        window.__life = new GameOfLife(largura, altura, tamanhoCelula);
        __life.start();
        __life.campoAleatorio(10);

        __life.desenhaGrid(ctx);

        window.onclick = toggleGame;
      }

      // Main deve ser chamada assim que a janela for carregada
      window.onload = main;