/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos.commons;

import java.io.Serializable;

/**
 * Interface de Marcação de Entidades, se possui @Entity deve
 * implementar esta interface.
 * @param <T> Tipo da Classe que a implementa.
 * @author Guilherme
 */
public interface EntityInterface<T> extends Comparable<T> {

    /**
     * Determina o limite de caracteres a ser retornado no
     * getLabel();.
     */
    int CARACTERES_LABEL = 20;

    /**
     * Atributo anotado com @Id de javax.persistence.
     *
     * @return Id único do banco de dados.
     */
    Serializable getId();

    /**
     * Como a classe deve ser mostrada ao usuário, quando por exemplo,
     * for selecionado em um SelectItem.
     *
     * @return String que se exibe ao usuário.
     */
    String getLabel();

    /**
     * Algumas entidades possuem ID únicos, mas que não são gerados
     * pelo banco de dados, como sequence ou algoritmo incrementador
     * ou algo parecido. <br>Esse método informa a implementação,
     * AbstractFacade se deve verificar se a alguma entidade já existe
     * com o Id informado. Torna possivel lançar uma
     * NegocioExcepion com o problema bem detalhado ao usuário.
     *
     * @return booleano informado se antes de salva a entidade deve
     * ser verificado se já não existe uma entidade com o Id no banco
     * de dados.
     */
    boolean verificarId();
}
