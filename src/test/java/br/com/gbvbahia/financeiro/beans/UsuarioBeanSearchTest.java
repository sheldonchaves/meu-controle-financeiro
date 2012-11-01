/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.Testes;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.testsuite.dataloader.CSVInitialDataSet;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 * * <strong>Lembre-se de iniciar o JavaDB ante de executar os
 * testes. </strong><br>
 *
 * @author Guilherme
 */
public class UsuarioBeanSearchTest
        extends BaseSessionBeanFixture<UsuarioFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos
     * o Bean a ser testado.
     */
    private static final Class[] USED_BEANS = Testes.getUseBeans();
    /**
     * Cria dados com base no CSV X a classe informada.
     */
    private static final CSVInitialDataSet<Usuario> USUARIO_CSV =
            Testes.getUsuariosCSV();

    /**
     * Passa insumos para os testes para o construtor de
     * BaseSessionBeanFixture.
     */
    public UsuarioBeanSearchTest() {
        super(UsuarioFacade.class, USED_BEANS, USUARIO_CSV);
    }

    /**
     * Provedor do Facede de Teste
     *
     * @return
     */
    private UsuarioFacade getBean() {
        UsuarioFacade instance = this.getBeanToTest();
        assertNotNull("EJB Não pode ser nulo!", instance);
        return instance;
    }

    @Test
    public void testFind() throws Exception {
        String id = "user01";
        UsuarioFacade instance = getBean();
        Usuario result = instance.find(id);
        assertNotNull(result);
    }

    /**
     * Test of findAll method, of class UsuarioBean.
     */
    @Test
    public void testFindAll() throws Exception {
        UsuarioFacade instance = getBean();
        List result = instance.findAll();
        assertTrue("Procurando todos os usuários, findAll!", !result.isEmpty());
    }

    /**
     * Test of count method, of class UsuarioBean.
     */
    @Test
    public void testCount() throws Exception {
        UsuarioFacade instance = getBean();
        int result = instance.count();
        assertTrue("Contar Usuarios", result != 0);
    }

    /**
     * Test of listPesq method, of class UsuarioBean.
     */
    @Test
    public void testListPesq() throws Exception {
        UsuarioFacade instance = getBean();
        String namedQuery = "Usuario.findAll";
        List<Usuario> result = instance.listPesq(namedQuery);
        assertTrue("Busca portodos usuários retornou vazia",
                result != null && !result.isEmpty());
    }

    /**
     * Test of listPesqParam method, of class UsuarioBean.
     */
    @Test
    public void testListPesqParam_String_Map() throws Exception {
        UsuarioFacade instance = getBean();
        String namedQuery = "Usuario.findByBlocked";
        Map parans = UsuarioBean.getMapParans();
        parans.put("blocked", false);
        List<Usuario> result = instance.listPesqParam(namedQuery, parans);
        for (Usuario u : result) {
            if (u.getBlocked()) {
                fail("Busca por usuários desbloqueados retornou um bloqueado.");
            }
        }
        assertTrue("Busca por usuários desbloqueados retornou vazia",
                result != null && !result.isEmpty());
    }

    /**
     * Test of listPesqParam method, of class UsuarioBean.
     */
    @Test
    public void testListPesqParam_4args() throws Exception {
        UsuarioFacade instance = getBean();
        String namedQuery = "Usuario.findByBlocked";
        Map parans = UsuarioBean.getMapParans();
        parans.put("blocked", false);
        List<Usuario> result = instance.listPesqParam(namedQuery,
                parans, 5, 0);
        for (Usuario u : result) {
            if (u.getBlocked()) {
                fail("Busca paginada por usuários desbloqueados "
                        + "retornou um bloqueado.");
            }
        }
        assertTrue("Busca por usuários desbloqueados retornou vazia",
                result != null && result.size() == 5);
    }

    /**
     * Test of pesqParam method, of class UsuarioBean.
     */
    @Test
    public void testPesqParam_String_Map() throws Exception {
        String namedQuery = "Usuario.findByUserId";
        Map<String, Object> params = UsuarioBean.getMapParans();
        params.put("userId", "user01");
        UsuarioFacade instance = getBean();
        Usuario result = instance.pesqParam(namedQuery, params);
        assertNotNull("Pesquisa por user01 retornou null.", result);
    }

    /**
     * Test of pesqParam method, of class UsuarioBean.
     */
    @Test
    public void testPesqParam_String() throws Exception {
        assertTrue("Sem query com unico resultado com parametros"
                + " para teste aqui.", true);
    }

    /**
     * Test of pesqCount method, of class UsuarioBean.
     */
    @Test
    public void testPesqCount() throws Exception {
        assertTrue("Sem query com unico resultado com parametros"
                + " para teste aqui.", true);
    }

    /**
     * Se for uma base de dados a mesma deve ser limpa. Em memória não
     * ha necessidade.
     *
     * @throws Exception
     */
    @Override
    public void tearDown() throws Exception {
        BasicDataSource ds = new BasicDataSource(Ejb3UnitCfg.getConfiguration());
        Connection con = ds.getConnection();
        Testes.tearDown(con);
    }
}
