/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.superclass.Procedimento;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Representa uma receita única, como um recebimento de um bico, um
 * emprestimo, salário mensal receita fixa.<br> Não possui parcelas.
 *
 * @since 14/04/2012
 * @author Guilherme
 */
@Entity
@Table(name = "fin_procedimento_receita_unica")
@NamedQueries({
    @NamedQuery(name = "Receita.StatusUsuario",
    query = " SELECT r From ReceitaProcedimento r "
    + " WHERE (:status2 = 'todos' OR r.statusPagamento = :status) "
    + " AND (r.usuario = :usuario OR r.usuario.conjuge = :usuario)")
})
@DiscriminatorValue("RECEITA_UNICA")
public class ReceitaProcedimento extends Procedimento
        implements Serializable {

    /**
     * Construtor padrão que informa ao Procedimento que está extensão
     * é uma Receita.
     */
    public ReceitaProcedimento() {
        super(TipoProcedimento.RECEITA_FINANCEIRA);
    }
}
