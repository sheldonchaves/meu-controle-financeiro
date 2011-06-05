/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.ejbbeans.interfaces;

import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.detalhes.GrupoGasto;
import br.com.financeiro.entidades.detalhes.GrupoReceita;
import br.com.financeiro.excecoes.GrupoFinanceiroDescricaoException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author gbvbahia
 */
@Local
public interface GrupoFinanceiroLocal {

    void cadastrarGrupoGasto(GrupoGasto grupoGasto) throws GrupoFinanceiroDescricaoException;

    void cadastrarGrupoReceita(GrupoReceita grupoReceita) throws GrupoFinanceiroDescricaoException;

    List<GrupoGasto> buscarGrupoGastoParaSelecao(User user);

    List<GrupoReceita> buscarGrupoReceitaParaSelecao(User user);

    GrupoGasto buscarGrupoGastoPorNome(String nomeGasto, User user);

    GrupoReceita buscarGrupoReceitaPorNome(String nomeReceita, User user);

    /**
     * Retorna todos os grupos gasto idependete do status.
     * @param user
     * @return
     */
    List<GrupoGasto> buscarGrupoGastoParaEdicao(User user);

    /**
     * Retorna todos os grupos receita idependete do status.
     * @param user
     * @return
     */
    List<GrupoReceita> buscarGrupoReceitaParaEdicao(User user);
}
