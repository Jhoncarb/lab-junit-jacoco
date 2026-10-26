package br.ufrgs.inf.lab.nivel3;

import static org.junit.jupiter.api.Assertions.*;

import java.beans.Transient;

import br.ufrgs.inf.lab.nivel1.Conta;
import br.ufrgs.inf.lab.nivel3.AnalisadorEmprestimo;
import br.ufrgs.inf.lab.nivel3.DecisaoEmprestimo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Análise de pedido de empréstimo (NÍVEL 3: sem nenhum teste pronto!).
 *
 * ENTRADAS: score (0..1000), renda mensal, valor solicitado, número de parcelas (1..360).
 *
 * REGRAS (nesta ordem):
 *  1. Entradas inválidas -> DADOS_INVALIDOS
 *       score fora de 0..1000, renda <= 0, valor <= 0, parcelas < 1 ou > 360.
 *  2. score < 300 -> NEGADO (nem calcula parcela).
 *  3. Taxa de juros ANUAL conforme o score:
 *       score >= 800 -> 12%   |   score >= 600 -> 24%   |   demais -> 36%
 *  4. Parcela mensal (tabela Price) com taxa mensal = anual / 12:
 *       parcela = valor * i / (1 - (1 + i)^(-n))
 *  5. comprometimento = parcela / renda
 *       comprometimento > 0,50 -> NEGADO
 *       comprometimento > 0,30 -> ANALISE_MANUAL
 *  6. Caso contrário (comprometimento <= 0,30):
 *       score >= 600 -> APROVADO
 *       score <  600 -> ANALISE_MANUAL
 */

class AnalisadorEmprestimoTest {
    private static final double DELTA = 0.001;
    
    private AnalisadorEmprestimo analisador;

    @BeforeEach
    void preparar() {
        analisador = new AnalisadorEmprestimo();
    }

    @Test
    void analiseEmprestimoComEntradaInvalidaDeveSerRecusada() {
        DecisaoEmprestimo decisao = analisador.analisar(1001, 0.0, 0.0, 1);

        assertEquals(DecisaoEmprestimo.DADOS_INVALIDOS, decisao);
    }

    @Test
    void analiseEmprestimoComScoreBaixoDeveSerRecusada() {
        DecisaoEmprestimo decisao = analisador.analisar(299, 1.0, 1.0, 1);

        assertEquals(DecisaoEmprestimo.NEGADO, decisao);
    }

    @Test
    void analiseEmprestimoComScore800DeveTerTaxa12() {
        DecisaoEmprestimo decisao = analisador.analisar(299, 1.0, 1.0, 1);

        assertEquals(DecisaoEmprestimo.NEGADO, decisao);
    }
}
