package Sudokuwork;

import java.util.Scanner;
/**
 * @author Lucas Li
 */
public class Sudoku {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        int linha = -1, coluna = -1, numeroEscolhido = -1, nivelDificuldade = -1;
        boolean keepAsking = true;      //garante que os numeros digitados sejam válidos
        String reset = "\u001B[0m";     //define cores
        String mensagemSistema = "\u001B[41;43m"; //define cores
        int[][] matriz = new int[9][9]; //matriz que será usada ao longo do jogo
        boolean[][] slots = new boolean[9][9]; //posiçoes fixas que não podem ser alteradas pelo jogador

        int[][] tabuleiroFacil = {{0,8,0,0,0,5,0,0,0},{0,3,9,2,0,1,0,8,7},{0,0,6,0,8,0,9,2,0},{7,2,0,0,0,0,0,3,0},
            {0,0,4,0,0,0,1,0,0},{0,6,0,0,0,0,0,7,2},{0,4,5,0,3,0,7,0,0},{8,7,0,1,0,9,2,6,0},{0,0,0,5,0,0,0,9,0}};
        int[][] tabuleiroMedio = {{7,8,0,0,0,0,0,3,0},{0,0,0,0,3,0,0,0,0},{0,6,3,5,0,2,0,0,0},{3,0,0,0,0,1,9,4,0},
            {0,0,0,4,0,5,0,0,0},{0,4,2,3,0,0,0,0,8},{0,0,0,2,0,9,3,6,0},{0,0,0,0,8,0,0,0,0},{0,5,0,0,0,0,0,1,4}};
        int[][] tabuleiroDificil = {{8,1,0,0,0,0,0,2,7},{0,0,4,0,0,0,1,0,0},{2,3,0,0,0,0,0,4,5},{0,0,0,1,7,4,0,0,0},
            {4,0,0,5,0,6,0,0,9},{0,7,0,0,3,0,0,1,0},{0,0,0,0,1,0,0,0,0},{0,4,3,0,0,0,6,5,0},{1,0,0,3,0,7,0,0,8}};
        int[][] tipicoSudoku = {{8,1,0,0,0,0,0,2,7},{0,0,4,0,0,0,1,0,0},{2,3,0,0,0,0,0,4,5},{0,0,0,1,7,4,0,0,0},
            {4,0,0,5,0,6,0,0,9},{0,7,0,0,3,0,0,1,0},{0,0,0,0,1,0,0,0,0},{0,4,3,0,0,0,6,5,0},{1,0,0,3,0,7,0,0,8}};
        
        
        
        System.out.println("*   Dificuldades disponívels:              *");
        System.out.println("*   1. Fácil                               *");
        System.out.println("*   2. Médio                               *");
        System.out.println("*   3. Dificil                             *");
                
        do {
            System.out.print("Digite o numero da opção desejada:\n> ");
            nivelDificuldade = Integer.parseInt(leitor.next());
            if (nivelDificuldade < 1 || nivelDificuldade > 3)
                System.out.println(mensagemSistema + "Opção inválida! Escolha 1, 2 ou 3" + reset);
            else
                keepAsking = false;
        } while (keepAsking);

        
        if (nivelDificuldade == 1) {
            matriz = tabuleiroFacil;
            System.out.print("     " + mensagemSistema + " Dificuldade: Fácil  " + reset);
        }
        if (nivelDificuldade == 2) {
            matriz = tabuleiroMedio;
            System.out.print("     " + mensagemSistema + " Dificuldade: Médio  " + reset);
        }
        if (nivelDificuldade == 3) {
            matriz = tabuleiroDificil;
            System.out.print("     " + mensagemSistema + " Dificuldade: Difícil" + reset);
        }
        
        slots = defineOcupacao(slots, matriz);        
        
        do {
            keepAsking = true;
            System.out.println();
            exibeMatriz(matriz);
            
            
            System.out.println("Informe números de 1 a 9 para linha, coluna e numero a ser inserido no tabuleiro.");
            do {
                System.out.print("Linha > ");
                linha = Integer.parseInt(leitor.next()) - 1;
            
                System.out.print("Coluna > ");
                coluna = Integer.parseInt(leitor.next()) - 1;
            
                System.out.print("Número > ");
                numeroEscolhido = Integer.parseInt(leitor.next());
                if(linha < 0 || linha > 8 || coluna < 0 || coluna > 8 || numeroEscolhido < 1 || numeroEscolhido > 9)
                    System.out.println(mensagemSistema + "Somente números de 1 a 9 são válidos!" + reset);
                else
                    keepAsking = false;
            } while (keepAsking);
            keepAsking = true; 
            
            
            if(slots[linha][coluna]) {
                System.out.println(mensagemSistema + " Essas posicões são fixas, você não pode alterá-las! " + reset);
                exibirPosicoesFixas(slots, matriz);
            } else {
                if (isPossivelPorNumeroAqui(matriz, linha, coluna, numeroEscolhido)) {
                    matriz[linha][coluna] = numeroEscolhido;
                } else {
                    exibeMovimentoBloqueado(matriz, linha, coluna, numeroEscolhido);
                }
            }
            
            if (verificaTabuleiroCompleto(tipicoSudoku))
                keepAsking = false;
            pressioneEnter();
        } while (keepAsking);
        System.out.println("Fim. Obrigado por jogar!");
    }
    
    public static boolean isPossivelPorNumeroAqui(int[][] matriz, int linha, int coluna, int numero) {
       
        for (int i = 0; i < matriz[0].length; i++) {
            if (matriz[linha][i] == numero)
                return false;
        }      
        
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i][coluna] == numero)
                return false;
        }
        
        
        
        
        int boxLinha = linha - linha % 3;
        int boxColuna = coluna - coluna % 3;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (matriz[boxLinha + i][boxColuna + j] == numero)
                    return false;
            }
        }
        
        
        return true;
    }
    
    public static void exibeMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            System.out.print("L" + (i+1) + " - ");
            for (int j = 0; j < matriz.length; j++) {
                System.out.print(matriz[i][j] + " ");
                if (j == 2 || j == 5) System.out.print("| ");
            }
            System.out.println();
            if (i == 2 || i == 5)
                System.out.print("     ------|-------|------\n");
        }
    }
    
    public static void exibeMovimentoBloqueado(int[][] matriz, int linha, int coluna, int numero) {
        int[][] novaMatriz = copiaMatriz(matriz);
        String reset = "\u001B[0m";
        String vermelhoFundoAzul = "\u001B[31;46m";
        
        String brancoFundoVermelho = "\u001B[41;43m";
        
       
        int[] posicoesDestaque = {linha, coluna, -1, -1, -1, -1, -1, -1};
        
        System.out.println("    " + brancoFundoVermelho + " Movimento inválido! " + reset);
        
        novaMatriz[linha][coluna] = numero;

        for (int i = 0; i < matriz.length; i++) {
            if (matriz[linha][i] == numero) {
                posicoesDestaque[2] = linha;
                posicoesDestaque[3] = i;
            }
        }
        
        
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i][coluna] == numero) {
                posicoesDestaque[4] = i;
                posicoesDestaque[5] = coluna;   
            }
        }

        int boxLinha = linha - linha % 3;
        int boxColuna = coluna - coluna % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (matriz[boxLinha + i][boxColuna + j] == numero) {
                    posicoesDestaque[6] = boxLinha + i;
                    posicoesDestaque[7] = boxColuna + j;
                }
            }
        }
        
        for (int i = 0; i < matriz.length; i++) {
            System.out.print((i+1) + " - ");
            for (int j = 0; j < matriz.length; j++) {
                    boolean destaques = defineDestaques(posicoesDestaque, i, j);
                    
                    
                    if (destaques)            
                        System.out.print(vermelhoFundoAzul + novaMatriz[i][j] + " " + reset);
                    else
                        System.out.print(novaMatriz[i][j] + " ");
                        
                if (j == 2 || j == 5) System.out.print("| "); 
            }
            System.out.println();
            if (i == 2 || i == 5)
                System.out.print("    ------|-------|-------\n"); 
        }
       
    }
    
    public static boolean defineDestaques(int[] posicoesDestaque, int i, int j){
        for (int k = 0; k < posicoesDestaque.length; k++) {
            int linha = k; int coluna = k + 1;
            if(posicoesDestaque[linha] == i && posicoesDestaque[coluna] == j) {
                return true;
            }
            k++;
        }
        return false;
    }

    public static int[][] copiaMatriz(int[][] matriz) {
        int[][] novaMatriz = new int[matriz.length][matriz[0].length];
        
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                novaMatriz[i][j] = matriz[i][j];
            }
        }
        
        return novaMatriz;
    }
    
    public static void exibirPosicoesFixas(boolean [][] slots, int[][] matriz) {
        String reset = "\u001B[0m";
        String vermelhoFundoAzul = "\u001B[31;46m";

        for (int i = 0; i < matriz.length; i++) {
            System.out.print((i+1) + " - ");
            for (int j = 0; j < matriz.length; j++) {
                    if (slots[i][j])            
                        System.out.print(vermelhoFundoAzul + matriz[i][j] + " " + reset);
                    else
                        System.out.print(matriz[i][j] + " ");
                        
                if (j == 2 || j == 5) System.out.print("| "); 
            }
            System.out.println();
            if (i == 2 || i == 5)
                System.out.print("    ------|-------|-------\n");
        }
    }
    
    private static boolean[][] defineOcupacao(boolean[][] slots, int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                if (matriz[i][j] != 0)
                    slots[i][j] = true;
            }
        }
        return slots;
    }
    
    public static boolean verificaTabuleiroCompleto(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                if(matriz[i][j] == 0)
                    return false;
            }
        }
        return true;
    }
    
    public  static void pressioneEnter() {
        Scanner leitor = new Scanner(System.in);
        System.out.print("Pressione Enter para continuar...");
        leitor.nextLine();
    }
}