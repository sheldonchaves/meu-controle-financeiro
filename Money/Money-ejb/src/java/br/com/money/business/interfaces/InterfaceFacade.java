/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.Usuario;
import br.com.money.modelos.commons.EntityInterface;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @param <T> Deve ser uma entidade de persistencia.
 * @param <ID> Deve ser o tipo do Id da entidade de persistencia.
 * @author Guilherme
 */
public interface InterfaceFacade<T extends EntityInterface,
        ID extends Serializable> {

    /**
     * Normalmente é necessário contabilizar a quantidade de elementos
     * da tabela durante a paginação, utilize em conjunto com
     * findRange(int[] range).
     *
     * @return A quantidade de dados da tabela de uma Entidade.
     */
    int count();

    /**
     * Método genérico para criação de um novo objeto no banco de
     * dados.<br>
     *
     * @param entity (Um objeto com a anotação
     * javax.persistence.Entity)
     * @throws ValidacaoException Caso alguma validação de negócio
     * seja encontrada em alguma imlementação.
     */
    void create(T entity) throws ValidacaoException;

    /**
     * Método genérico para edição de entidades, se a entidade não
     * existir ele pode ou não criar, depende do negócio.
     *
     * @param entity (Um objeto com a anotação
     * javax.persistence.Entity)
     * @throws ValidacaoException Caso alguma validação de negócio
     * seja encontrada em alguma imlementação.
     */
    void update(T entity) throws ValidacaoException;

    /**
     * Método genérico para executar querys de atualização com
     * parâmetros, caso não haxa parâmetros necessários enviar null em
     * patams.
     *
     * @param namedQuery String com o nome da Query anotada na
     * entidade.
     * @param params Map chave valor com parâmetros ou null se não
     * houver.
     * @throws ValidacaoException Caso alguma validação de negócio
     * seja encontrada em alguma imlementação.
     */
    void update(String namedQuery, Map<String, Object> params)
            throws ValidacaoException;

    /**
     * Encontra objetos por ID, se objeto tiver ID composto, <br>
     * Utilize sua classe ID para a busca, como em Servidor:<br>
     * find(new IdServidor(orgaoId, matriculaId));.
     *
     * @param id (Algumas classes requerem Id compostos, como
     * Servidor)
     * @return A entidade com ID informado ou null se não encontrar
     */
    T find(ID id);

    /**
     * Retorna todos os objetos de uma determinada entidade.
     *
     * @see InterfaceFacade#findRange(int[]) para paginação.<br>
     * @see InterfaceFacade#listPesq(java.lang.String) para Query
     * personalizada <br>
     * @see InterfaceFacade#listPesqParam(java.lang.String,
     * java.util.Map) para Query personalizada com parâmetros<br>
     * @see InterfaceFacade#listPesqParam(java.lang.String,
     * java.util.Map, int, int) para Query personalizada com
     * parâmetros paginada.<br>
     * @return Uma List com os objetos solicitados.<br> Uma List vazia
     * é retornada se não houver dados.
     */
    List<T> findAll();

    /**
     * Retorna todos os objetos de forma paginada<br> A quantidade
     * máxima é definida pela subtração: range[1] - range[0].
     *
     * @see InterfaceFacade#listPesq(String) para Query
     * personalizada<br>
     * @see InterfaceFacade#listPesqParam(java.lang.String,
     * java.util.Map) para Query personalizada com parâmetros<br>
     * @see InterfaceFacade#listPesqParam(java.lang.String,
     * java.util.Map, int, int) para Query personalizada com
     * parâmetros paginada<br>
     * @see InterfaceFacade#count() para quantificação da tabela a ser
     * paginada.
     * @param range Array com dois números, primeiro representa o
     * ponto de inicio da pesquisa e o segundo representa o ultima
     * posição da tabela.
     * @return Uma List com os objetos solicitados, limitado a
     * quantidade máxima pela subtração: range[1] - range[0]<br> Uma
     * List vazia é retornada se não houver dados.
     */
    List<T> findRange(int[] range);

    /**
     * Método genérico para remoção de entidades.
     *
     * @param entity (Um objeto com a anotação
     * javax.persistence.Entity)
     * @throws ValidacaoException Caso alguma validação de negócio
     * seja encontrada em alguma imlementação.
     */
    void remove(T entity) throws ValidacaoException;

    /**
     * Quando necessita encontrar um único objeto utilizando uma
     * NamedQuery com parâmtros.<br> Se encontrar mais de um resultado
     * irá lançar NonUniqueResultException
     *
     * @param namedQuery Nome da query, está em uma Entidade ao qual
     * consulta ou em um arquivo XML
     * @param params Map&ltString,Object&gt com os parametros da
     * pesquisa.
     * @return A Entidade esperada ou null se não encontrar
     */
    T pesqParam(String namedQuery, Map<String, Object> params);

    /**
     * Quando necessita encontrar um único objeto utilizando uma
     * NamedQuery Se encontrar mais de um resultado irá lançar
     * NonUniqueResultException.
     *
     * @param namedQuery Nome da query, está em uma Entidade ao qual
     * consulta ou em um arquivo XML
     * @return A Entidade esperada ou null se não encontrar
     */
    T pesqParam(String namedQuery);

    /**
     * Quando necessita encontrar alguns objetos utilizando uma
     * NamedQuery.
     *
     * @param namedQuery Nome da query, está em uma Entidade ao qual
     * consulta ou em um arquivo XML.
     * @return Uma List&ltT&gt com várias entidades ou vazia se não
     * encontrar.
     */
    List<T> listPesq(String namedQuery);

    /**
     * Quando necessita encontrar alguns objetos utilizando uma
     * NamedQuery com parâmtros.
     *
     * @param namedQuery Nome da query, está em uma Entidade ao qual
     * consulta ou em um arquivo XML
     * @param params Um Map de nome-valor, onde nome é referente ao
     * parâmetro na NamedQuery e valor é o objeto valor propriamente
     * dito
     * @return Uma List com várias entidades ou vazia se não
     * encontrar.
     */
    List<T> listPesqParam(String namedQuery, Map<String, Object> params);

    /**
     * Quando necessita encontrar alguns objetos utilizando uma
     * NamedQuery com parâmtros e realizar paginação.
     *
     * @param namedQuery Nome da query, está em uma Entidade ao qual
     * consulta ou em um arquivo XML.
     * @param params Um Map de nome-valor, onde nome é referente ao
     * parâmetro na NamedQuery e valor é o objeto valor propriamente
     * dito.
     * @param max Quantidade máxima de linhas do resultado da
     * consulta.
     * @param atual Posição inicial da pesquisa.
     * @see InterfaceFacade#pesqCount(java.lang.String, java.util.Map)
     * para contabilizar a quantidade total de objetos dentro dos
     * parâmetros.
     * @return Uma List com várias entidades, limitado pela quantidade
     * ou vazia se não encontrar.
     */
    List<T> listPesqParam(String namedQuery, Map<String, Object> params,
            int max, int atual);

    /**
     * Utilize para consultas que necessitam de Count(*) e condições,
     * não havendo condições passe null.
     *
     * @param query NamedQuery escrita na entidade.
     * @param params Map&ltString, Object&gt com os parametros da
     * pesquisa.
     * @return Quantidade de casos encontrados.
     */
    Long pesqCount(String query, Map<String, Object> params);
}
