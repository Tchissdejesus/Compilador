/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analisadorlexico;

/**
 *
 * @author tchis
 */
public class Token {

    private String lexema;
    private String tipo;
    private int linha;

    public Token(String lexema, String tipo, int linha) {
        this.lexema = lexema;
        this.tipo = tipo;
        this.linha = linha;
    }

    public String getLexema() {
        return lexema;
    }

    public String getTipo() {
        return tipo;
    }

    public int getLinha() {
        return linha;
    }

    @Override
    public String toString() {
        return "Token |" + "lexema='" + lexema + "' | tipo='" + tipo + "'| linha=" + linha + "|";
    }

}
