/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.superclass.DetalheProcedimento;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Detalhe de despesa financeira.
 * @since v.3 01/04/2012
 * @author Guilherme
 */
@Entity
@Table(name = "fin_detalhe")
@DiscriminatorColumn(name = "tipo",
discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("DESPESA")
public class DetalheDespesa extends DetalheProcedimento
        implements Serializable {

    /**
     * Informa ao supertipo qual tipo ele é.
     */
    public DetalheDespesa() {
        super(TipoProcedimento.DESPESA_FINANCEIRA);
    }

}
