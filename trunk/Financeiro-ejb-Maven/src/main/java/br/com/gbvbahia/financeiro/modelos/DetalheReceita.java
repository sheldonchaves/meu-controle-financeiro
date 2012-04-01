/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.modelos.superclass.DetalheProcedimento;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Detalhe de receita financeira.
 * @since v.3 01/04/2012
 * @author Guilherme
 */
@Entity
@Table(name = "fin_detalhe")
@DiscriminatorColumn(name = "tipo",
discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("RECEITA")
public class DetalheReceita extends DetalheProcedimento
        implements Serializable {

}
