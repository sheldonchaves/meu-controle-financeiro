package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface para as entidades DetalheDespesa e DetalheReceita.
 *
 * @author Guilherme
 * @since v.3 01/04/2012
 */
@Local
public interface DetalheProcedimentoFacade
        extends InterfaceFacade<DetalheProcedimento, Long> {

        /**
         * Recupera todos os detalhes com base nos parâmetros
         * @param user Usuário proprietario ou conjuge do detalhe. Obrigatorio 
         * @param ativo Status ativo ou não ativo. Se null todos.
         * @param tipo TipoProcedimento,Receita ou Despesa, se null tudo.
         * @return Lista com todos detalhes.
         */
      public List<DetalheProcedimento> findAllDetalhe(
            final Usuario user, final Boolean ativo, TipoProcedimento tipo);

}
