/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.enums.TipoConta;
import br.com.money.modelos.ContaBancaria;
import br.com.money.modelos.Usuario;
import javax.ejb.Local;
import java.util.List;
/**
 *
 * @author Guilherme
 */
@Local
public interface ContaBancariaBeanLocal extends InterfaceFacade<ContaBancaria, Long> {

    void salvarContaBancaria(ContaBancaria contaBancaria);

    ContaBancaria buscarContaBancariaPorNomeTipo(String nome, TipoConta tipo);

    List<ContaBancaria> buscarContaBancariasPorUsuario(Usuario usuario);
    
    public void apagarContaBancaria(Long id);

    public List<ContaBancaria> buscarContaBancariasPorUsuarioTipo(Usuario ususario, TipoConta tipo);
    

    /**
     * Busca todas as contas bancarias filtrando usuario, tipo e nome
     * da conta bancaria. Somente usuário é parametro obrigatório.
     * A quantidade de registros e a posição também devem ser
     * informados.<br>
     * Para contabilizar utilize <i>contarContaBancariasPorUsuarioTipo</i>
     * @param ususario Usuário proprietário. Obrigatório.
     * @param tipo Tipo de conta, corrente, poupança... Opcional, se não
     * iformado será enviado todos.
     * @param nomeConta Nome dado a conta quanto cadastrado, se não informado
     * será considerado todos.
     * @param inicial Ponto de partida da consulta.
     * @param total Quantidade de registros a retornar.
     * @return Uma lista com todas as contas no perfil ou vazia se
     * não encontrar.
     */
    List<ContaBancaria> buscarContaBancariasPorUsuarioTipoPaginado(
            Usuario ususario, TipoConta tipo, String nomeConta,
            int inicial, int total);

    /**
     * Contabiliza todas as contas bancarias filtrando usuario, tipo e nome
     * da conta bancaria. Somente usuário é parametro obrigatório.
     * Para buscar utilize <i>buscarContaBancariasPorUsuarioTipoPaginado</i>
     * @param ususario Usuário proprietário. Obrigatório.
     * @param tipo Tipo de conta, corrente, poupança... Opcional, se não
     * iformado será enviado todos.
     * @param nomeConta Nome dado a conta quanto cadastrado, se não informado
     * será considerado todos.
     * @return A quantidado total de linhas na tabela com o perfil
     * informado. Zero é retornado se não encontrar nenhum.
     */
    int contarContaBancariasPorUsuarioTipo(Usuario ususario,
            TipoConta tipo, String nomeConta);
}
