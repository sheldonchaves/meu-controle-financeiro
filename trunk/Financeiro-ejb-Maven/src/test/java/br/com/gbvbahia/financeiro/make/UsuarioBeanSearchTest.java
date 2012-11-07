/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.TestesMake;
import br.com.gbvbahia.financeiro.beans.UsuarioBean;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 * * <strong>Lembre-se de iniciar o JavaDB ante de executar os testes.
 * </strong><br>
 *
 * @author Guilherme
 */
public class UsuarioBeanSearchTest
        extends BaseSessionBeanFixture<UsuarioFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos o Bean a
     * ser testado.
     */
    private static final Class[] USED_BEANS = TestesMake.getUseBeans();

    /**
     * Passa insumos para os testes para o construtor de
     * BaseSessionBeanFixture.
     */
    public UsuarioBeanSearchTest() {
        super(UsuarioFacade.class, USED_BEANS);
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
        List<Usuario> users = TestesMake.makeEntitiesBD(getEntityManager(),
                Usuario.class, "test_1", 8, false);
        String id = users.get(3).getUserId();
        UsuarioFacade instance = getBean();
        Usuario result = instance.find(id);
        assertNotNull(result);
    }

    /**
     * Test of findAll method, of class UsuarioBean.
     */
    @Test
    public void testFindAll() throws Exception {
        TestesMake.makeEntitiesBD(getEntityManager(), Usuario.class,
                "test_1", 8, false);
        UsuarioFacade instance = getBean();
        List result = instance.findAll();
        assertTrue("Procurando todos os usuários, findAll!", !result.isEmpty());
    }

    /**
     * Test of count method, of class UsuarioBean.
     */
    @Test
    public void testCount() throws Exception {
        TestesMake.makeEntitiesBD(getEntityManager(), Usuario.class,
                "test_1", 8, false);
        UsuarioFacade instance = getBean();
        int result = instance.count();
        assertTrue("Contar Usuarios", result == 8);
    }

    /**
     * Test of listPesq method, of class UsuarioBean.
     */
    @Test
    public void testListPesq() throws Exception {
        TestesMake.makeEntitiesBD(getEntityManager(), Usuario.class,
                "test_1", 8, false);
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
        List<Usuario> users = TestesMake.makeEntitiesBD(getEntityManager(),
                Usuario.class, "test_1", 4, false);
        UsuarioFacade instance = getBean();
        String namedQuery = "Usuario.findByFirstName";
        Map parans = UsuarioBean.getMapParans();
        parans.put("firstName", users.get(2).getFirstName());
        List<Usuario> result = instance.listPesqParam(namedQuery, parans);
        if (result.isEmpty()) {
            fail("Busca por userId MapParam não retornou resultado.");
        } else {
            assertTrue("Busca por userId MapParam retornou resultado", true);
        }
    }

    /**
     * Test of listPesqParam method, of class UsuarioBean.
     */
    @Test
    public void testListPesqParam_4args() throws Exception {
        List<Usuario> users = TestesMake.makeEntitiesBD(getEntityManager(),
                Usuario.class, "test_1", 5, false);
        UsuarioFacade instance = getBean();
        String namedQuery = "Usuario.findByFirstName";
        Map parans = UsuarioBean.getMapParans();
        parans.put("firstName", users.get(4).getFirstName());
        List<Usuario> result = instance.listPesqParam(namedQuery,
                parans, 5, 0);
        if (result.isEmpty()) {
            fail("Busca por userId MapParam não retornou resultado.");
        } else {
            assertTrue("Busca por userId MapParam retornou resultado", true);
        }
    }

    /**
     * Test of pesqParam method, of class UsuarioBean.
     */
    @Test
    public void testPesqParam_String_Map() throws Exception {
        List<Usuario> users = TestesMake.makeEntitiesBD(getEntityManager(),
                Usuario.class, "test_1", 4, false);
        String namedQuery = "Usuario.findByUserId";
        Map<String, Object> params = UsuarioBean.getMapParans();
        params.put("userId", users.get(3).getUserId());
        UsuarioFacade instance = getBean();
        Usuario result = instance.pesqParam(namedQuery, params);
        assertNotNull("Pesquisa por user01 retornou null.", result);
    }

    /**
     * Se for uma base de dados a mesma deve ser limpa. Em memória não ha
     * necessidade.
     *
     * @throws Exception
     */
    @Override
    public void tearDown() throws Exception {
        BasicDataSource ds = new BasicDataSource(Ejb3UnitCfg.getConfiguration());
        Connection con = ds.getConnection();
        TestesMake.tearDown(con);
    }
}
