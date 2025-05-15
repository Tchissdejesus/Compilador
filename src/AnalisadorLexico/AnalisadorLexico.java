/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package analisadorlexico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;

/**
 *
 * @author tchis
 */
// Classe principal do analisador léxico
public class AnalisadorLexico {

    // Instância da tabela de símbolos
    private TabelaDeSimbolos tabelaDeSimbolos = new TabelaDeSimbolos();
    
    // Leitor de arquivo
    private BufferedReader leitor;
    
    // Controla o número da linha atual do arquivo
    private int linhaAtual = 1;
    
    // Armazena o caractere atual (não usado diretamente nesse código)
    private int caractereAtual;
    
    // Armazena a linha atual do arquivo
    private String linha;
    
    // Índice do caractere atual na linha
    private int indice;

    // Método principal que inicia a análise léxica de um arquivo
    public void analisarArquivo(String caminhoArquivo) {
        try {
            leitor = new BufferedReader(new FileReader(caminhoArquivo));

            // Lê linha por linha do arquivo
            while ((linha = leitor.readLine()) != null) {
                linha += " "; // Adiciona um espaço ao final para garantir separação de tokens
                // Posição do caractere dentro da linha.
                indice = 0;
                caractereAtual = 0;

                // Processa cada caractere da linha atual
                while (indice < linha.length()) {
                    analex(); // Chama o analisador léxico para processar um token
                }

                linhaAtual++; // Passa para a próxima linha
            }

            leitor.close(); // Fecha o leitor de arquivo
        } catch (IOException e) {
            System.out.println("Erro ao abrir o arquivo: " + e.getMessage());
        }

        // Imprime todos os tokens encontrados no final da análise
        tabelaDeSimbolos.imprimirTabela();
    }

    // Função principal do analisador léxico: identifica e classifica tokens
    private void analex() {
        int estado = 0;
        StringBuilder buffer = new StringBuilder(); // Guarda o lexema atual

        while (indice < linha.length()) {
            char c = lerCaractere(); // Lê o próximo caractere da linha

            switch (estado) {
                case 0:
                    // Estado inicial: decide para qual tipo de token seguir
                    if (Character.isLetter(c)) {
                        buffer.append(c);
                        estado = 1; // Possível identificador ou palavra-chave
                    } else if (Character.isDigit(c)) {
                        buffer.append(c);
                        estado = 2; // Número inteiro ou real
                    } else if ("<>=!".indexOf(c) != -1) {
                        buffer.append(c);
                        estado = 3; // Operadores relacionais
                    } else if ("(){}[]".indexOf(c) != -1) {
                        gravarTokenLexema("Delimitador", String.valueOf(c));
                        return;
                    } else if (".,:;".indexOf(c) != -1) {
                        gravarTokenLexema("Pontuação", String.valueOf(c));
                        return;
                    } else if ("+-*/%&|".indexOf(c) != -1) {
                        buffer.append(c);
                        gravarTokenLexema("Operador", buffer.toString());
                        return;
                    } else if (c == '"' || c == '\'') {
                        buffer.append(c);
                        estado = 5; // Cadeia de caracteres
                    } else if (c == ' ' || c == '\t') {
                        return; // Ignora espaços e tabulações
                    } else {
                        gravarTokenLexema("Símbolo Especial", String.valueOf(c));
                        return;
                    }
                    break;

                case 1:
                    // Identificador ou palavra-chave
                    if (Character.isLetterOrDigit(c)) {
                        buffer.append(c);
                    } else {
                        voltaCaractere(); // Volta um caractere para não perder
                        String lexema = buffer.toString();
                        if (isPalavraReservada(lexema)) {
                            gravarTokenLexema("Palavra-chave", lexema);
                        } else {
                            gravarTokenLexema("Identificador", lexema);
                        }
                        return;
                    }
                    break;

                case 2:
                    // Número inteiro ou real
                    if (Character.isDigit(c)) {
                        buffer.append(c);
                    } else if (c == '.') {
                        buffer.append(c);
                        estado = 6; // Possível número real
                    } else {
                        voltaCaractere(); // Volta um caractere
                        gravarTokenLexema("Número", buffer.toString());
                        return;
                    }
                    break;

                case 3:
                    // Operadores relacionais compostos como <=, !=, >=, &&
                    if (c == '=' || (buffer.toString().equals("&") && c == '&') || (buffer.toString().equals("|") && c == '|')) {
                        buffer.append(c);
                    } else {
                        voltaCaractere(); // Volta se não for operador válido
                    }
                    gravarTokenLexema("Operador Relacional", buffer.toString());
                    return;

                case 5:
                    // Cadeia de caracteres
                    buffer.append(c);
                    if (c == '"' || c == '\'') {
                        gravarTokenLexema("Cadeia de caracteres", buffer.toString());
                        return;
                    }
                    break;

                case 6:
                    // Parte decimal do número real
                    if (Character.isDigit(c)) {
                        buffer.append(c);
                    } else {
                        voltaCaractere();
                        gravarTokenLexema("Número Real", buffer.toString());
                        return;
                    }
                    break;
            }
        }

        // Finaliza token se acabar a linha
        if (buffer.length() > 0) {
            if (estado == 1) {
                String lexema = buffer.toString();
                if (isPalavraReservada(lexema)) {
                    gravarTokenLexema("Palavra-chave", lexema);
                } else {
                    gravarTokenLexema("Identificador", lexema);
                }
            } else if (estado == 2 || estado == 6) {
                gravarTokenLexema("Número", buffer.toString());
            } else if (estado == 5) {
                System.out.println("Erro léxico na linha " + linhaAtual + ": cadeia de caracteres não fechada.");
            } else {
                gravarTokenLexema("Desconhecido", buffer.toString());
            }
        }
    }

    // Lê o próximo caractere da linha
    private char lerCaractere() {
        return linha.charAt(indice++);
    }

    // Volta um caractere no índice atual
    private void voltaCaractere() {
        if (indice > 0) indice--;
    }

    // Registra o token e o lexema na tabela de símbolos
    private void gravarTokenLexema(String tipo, String lexema) {
        tabelaDeSimbolos.adicionarToken(lexema, tipo, linhaAtual);
    }

    // Verifica se a palavra é reservada
    private boolean isPalavraReservada(String lexema) {
    return lexema.matches(
        "abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|" +
        "else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|" +
        "interface|long|native|new|package|private|protected|public|return|short|static|strictfp|" +
        "super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while"
    );
}


    // Método principal para execução do programa
    public static void main(String[] args) {
        AnalisadorLexico analisador = new AnalisadorLexico();
        analisador.analisarArquivo("codigo.txt"); // Substitua pelo caminho do seu arquivo
    }
}
