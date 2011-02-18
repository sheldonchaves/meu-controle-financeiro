/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.ejbbeans.interfaces;

import br.com.financeiro.entidades.Grups;
import br.com.financeiro.entidades.LembreteConta;
import br.com.financeiro.entidades.User;
import br.com.financeiro.excecoes.LembreteContasException;
import br.com.financeiro.excecoes.ProprietarioConjugeException;
import br.com.financeiro.excecoes.ProprietarioException;
import br.com.financeiro.excecoes.ProprietarioLoginException;
import br.com.financeiro.excecoes.ProprietarioNomeException;
import br.com.financeiro.excecoes.ProprietarioSenhaException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Guilherme
 */
@Local
public interface UserLocal {
    /**
     * Salva o proprietário passado, uma exceção será lançada caso alguma informação seja incompleta ou nulla.
     * @param proprietario
     * @throws ProprietarioSenhaException
     * @throws ProprietarioNomeException
     * @throws ProprietarioLoginException
     */
    void salvarproprietario(User proprietario, String urlFinanceiro)throws ProprietarioSenhaException,
            ProprietarioNomeException, ProprietarioLoginException,ProprietarioException;

    /**
     * Busca o proprietario pelo login informado, se contas for true irá retornar todas as contas bancarias
     * do proprietario dentro do Set ContasBancarias do proprietario encontrado
     * Se for false não realiza a procura
     * @param login
     * @param contas
     * @return Null se não encontrar, Proprietario com propriedade Login informada
     */
    User buscaProprietarioLogin(String login, boolean contas);

    List<User> todosProprietarios();
    List<Grups> todosGrups();
    Grups getBrupBayRole(String role);

    void salvarLembreteContas(LembreteConta lembreteConta) throws LembreteContasException;

    List<LembreteConta> busrcarLembreteContas(boolean status);

    LembreteConta busrcarLembreteContas(User user);

    String criptografarSenha(String senha, String role);

    void alteraSenhaUser(User user, String urlFinanceiro,boolean resetarSenha)throws ProprietarioSenhaException,
            ProprietarioNomeException, ProprietarioLoginException, ProprietarioException;

    void adicionarConjuge(User userLogado, User userConjuge) throws ProprietarioConjugeException;

    void removerConjuge(User user) throws ProprietarioConjugeException;

}
