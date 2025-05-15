/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analisadorlexico;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tchis
 */
public class TabelaDeSimbolos {
     private List<Token> tokens = new ArrayList<>();

    public void adicionarToken(String lexema, String tipo, int linha) {
        tokens.add(new Token(lexema, tipo, linha));
    }

    public void imprimirTabela() {
        System.out.println("                     Tabela de Token :");
        System.out.println("-----------------------------------------------------");
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    
    
}
