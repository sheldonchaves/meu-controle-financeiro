/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.exceptions;

import javax.ejb.ApplicationException;

/**
 * Classe que representa um erros de negócio, quando algo não estiver
 * da forma que se espera o método do bean EJB irá lançar essa exceção
 * com seus atributos informando o que está incorreto.
 * @since v.3 31/03/2012
 * @author Guilherme
 */
@ApplicationException(rollback = true)
public class NegocioException extends Exception {

    /**
     * Atributo que recebe um código inputado pelo desenvolvedor que
     * deve mapear uma frase ou varias frases no resouce bundle da
     * aplicação.<br> Será um código que pode ser visto no arquivo
     * codigos_execoes.properties dentro deste mesmo pacote. Argumento
     * obrigatório para instanciação da classe.
     */
    private String message;
    /**
     * Algumas informações são dinâmicas e devem ser inseridas neste
     * Array de String, o que estiver entre chaves {0} {1} {2} será
     * substituído de acordo com a posição no Array.<br> Argumento
     * opcional para instanciação da classe.
     */
    private String[] variacoes;

    /**
     * Construtor que recebe somente a mensagem como argumento, não
     * são necessários variações.
     *
     * @param messageException será um código que pode ser visto no
     * arquivo codigos_execoes.properties dentro deste mesmo pacote.
     */
    public NegocioException(final String messageException) {
        this.message = messageException;
    }

    /**
     * Construtor que recebe a mensagem e as variações.
     *
     * @param messageException será um código que pode ser visto no
     * arquivo codigos_execoes.properties dentro deste mesmo pacote.
     * @param variacoesException Informações que são definidas
     * dinamicamente.
     */
    public NegocioException(final String messageException,
            final String[] variacoesException) {
        super();
        this.message = messageException;
        this.variacoes = variacoesException;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Caso seja necessário alterar a mensagem passada como argumento
     * no construtor.
     *
     * @param messageException será um código que pode ser visto no
     * arquivo codigos_execoes.properties dentro deste mesmo pacote.
     */
    public void setMessage(final String messageException) {
        this.message = messageException;
    }

    /**
     * Possibilidade dinâmicas de exibição ao usuário.
     *
     * @return Array de String com as variações ou nulo se não houver.
     */
    public String[] getVariacoes() {
        return variacoes;
    }

    /**
     * Define ou altera as variações.
     *
     * @param variacoesArray Informações defiidas dinamicamente.
     */
    public void setVariacoes(final String[] variacoesArray) {
        this.variacoes = variacoesArray;
    }
}
