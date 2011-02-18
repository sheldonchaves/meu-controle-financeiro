/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.ejbbeans.interfaces;

import br.com.financeiro.entidades.CartaoCreditoUnico;
import br.com.financeiro.entidades.ContaPagar;
import br.com.financeiro.entidades.User;
import br.com.financeiro.excecoes.CartaoCreditoInformacaoIncompletaException;
import br.com.financeiro.excecoes.CartaoCreditoNumeroExisteException;
import br.com.financeiro.excecoes.CartaoCreditoNumeroInvalidoException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author gbvbahia
 */
@Local
public interface CartaoCreditoLocal {

    /**
     * Utilizado para salvar e atualizar o cartão de credito
     * Se o cartão não tiver id e seu número já estiver cadastrado no bando de
     * dados uma exceção CartaoCreditoNumeroExisteException será lançada.
     * Se o número do cartão não for um número válido um exceção será lançada.
     * @param cartaoCreditoUnico
     * @param proprietario
     * @exception CartaoCreditoNumeroExisteException
     */
    public void salvarCartaoCredito(CartaoCreditoUnico cartaoCreditoUnico, User proprietario)
            throws CartaoCreditoNumeroExisteException, CartaoCreditoNumeroInvalidoException,
            CartaoCreditoInformacaoIncompletaException;

    public List<CartaoCreditoUnico> buscaTodosCartoes(User proprietario);

    public CartaoCreditoUnico buscaCartaoPorNumero(String numeroCartaoCredito);

    public List<CartaoCreditoUnico> buscaCartaoPorStatus(boolean status,User proprietario);

    CartaoCreditoUnico buscarPorID(int id);

    List<CartaoCreditoUnico> buscarCartaoPorStatusTitular(boolean status, boolean dependentes, User user);

    List<CartaoCreditoUnico> buscarCartoesDependentes(CartaoCreditoUnico cartaoCreditoUnico);

    public User buscarUserCartao(CartaoCreditoUnico cartaoCreditoUnico);

    List<ContaPagar> buscarContasPorCartaoCredito(CartaoCreditoUnico cartaoCreditoUnico, Date dataReferencia);
}
