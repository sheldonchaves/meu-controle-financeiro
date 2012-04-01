package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.superclass.DetalheProcedimento;
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
     * Recupera todos os DetalhesProcedimento que tem DetalheReceita
     * como implementador.
     *
     * @param user Usuário proprietario do tetalhe, os detalhes do
     * conjuge também serão incluídos. Obrigatório.
     * @param ativo True retorno todos ativos, False define todos
     * bloqueados e nulo retorno os dois status.
     * @return Lista de DetalheProcedimento somente Receita.
     */
    List<DetalheProcedimento> findAllDetalheReceita(Usuario user,
            Boolean ativo);

    /**
     * Recupera todos os DetalhesProcedimento que tem DetalheDespesa
     * como implementador.
     *
     * @param user Usuário proprietario do tetalhe, os detalhes do
     * conjuge também serão incluídos. Obrigatório.
     * @param ativo True retorno todos ativos, False define todos
     * bloqueados e nulo retorno os dois status.
     * @return Lista de DetalheProcedimento somente Despesa.
     */
    List<DetalheProcedimento> findAllDetalheDespesa(Usuario user,
            Boolean ativo);
}
