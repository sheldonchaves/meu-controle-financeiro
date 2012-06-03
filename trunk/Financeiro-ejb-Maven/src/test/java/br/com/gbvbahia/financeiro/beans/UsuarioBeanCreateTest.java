/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.Testes;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Grupo;
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
 * Teste exemplo:<br>
 * http://sabercertificacao.com.br/files/projetos/ejbunit.rar
 *
 * @author Guilherme
 * @since 07/04/2012
 */
public class UsuarioBeanCreateTest
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
    public UsuarioBeanCreateTest() {
        super(UsuarioFacade.class, USED_BEANS, USUARIO_CSV);
    }

    /**
     * Test of getMapParans method, of class UsuarioBean.
     *
     * @throws Exception Qualquer exceção é esperada.
     */
    @Test
    public void testGetMapParans() throws Exception {
        Map result = UsuarioBean.getMapParans();
        result.put("long", new Long("1"));
        assertEquals(result.get("long"), new Long("1"));
    }

    /**
     * Test of create method, of class UsuarioBean.
     *
     * @throws Exception Qualquer exceção é esperada.
     */
    @Test
    public void testCreate() throws Exception {
        Usuario entity = new Usuario();
        entity.setEmail("gbvbahia01@hotmail.com");
        entity.setFirstName("Guilherme");
        entity.setUserId("gbvbahia");
        entity.setPass("123456");
        entity.setLastName("Braga");
        UsuarioFacade instance = getBean();
        getEntityManager().getTransaction().begin();
        instance.create(entity);
        getEntityManager().getTransaction().commit();
        Usuario us = instance.find("gbvbahia");
        assertEquals(entity, us);
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

    /**
     * Deve haver pelo menos 9 usuários cadastrados do arquivo
     * usuarios.csv.
     *
     * @throws Exception Qualquer exceção é esperada.
     */
    @Test
    public void testFindRange() throws Exception {
        int[] range = {0, 5};
        UsuarioFacade instance = getBean();
        List result = instance.findRange(range);
        assertTrue("Tamanho da lista: " + result.size()
                + " diferente do esperado: " + (5)
                + " RANGE!", result.size() == 5);
    }

    /**
     * Cria usuário com login com mais de
     * LIMIT_MAX_CARACTERES_LOGIN_ID caracteres.
     *
     * * @throws Exception Qualquer exceção é esperada.
     */
    @Test(expected = NegocioException.class)
    public void criaUsuarioLoginInvalido() throws Exception {
        try {
            Usuario entity = new Usuario();
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i <= Usuario.LIMIT_MAX_CARACTERES_LOGIN_ID; i++) {
                if (i % 2 == 0) {
                    sb.append("a");
                } else {
                    sb.append("b");
                }
            }
            entity.setEmail("userloginerro@hotmail.com");
            entity.setFirstName("userLoginErro");
            entity.setUserId(sb.toString());
            entity.setPass("123456");
            entity.setLastName("userLoginErro");
            UsuarioFacade instance = getBean();
            getEntityManager().getTransaction().begin();
            instance.create(entity);
            getEntityManager().getTransaction().commit();
            fail("Uma NegocioException deveria ter sido lançada!");
        } catch (NegocioException e) {
            if (getEntityManager().getTransaction().isActive()) {
                this.getEntityManager().getTransaction().rollback();
            }
            assertTrue("NegocioException Lançada", true);
        }
    }

    /**
     * Cria usuário com login com mais de
     * LIMIT_MAX_CARACTERES_LOGIN_ID caracteres.
     *
     * * @throws Exception Qualquer exceção é esperada.
     */
    @Test(expected = NegocioException.class)
    public void criaUsuarioEmailInvalido() throws Exception {
        try {
            Usuario entity = new Usuario();
            entity.setEmail("user Login Errado@hotmail.com");
            entity.setFirstName("userLoginErro");
            entity.setUserId("emailErro");
            entity.setPass("123456");
            entity.setLastName("userLoginErro");
            UsuarioFacade instance = getBean();
            getEntityManager().getTransaction().begin();
            instance.create(entity);
            getEntityManager().getTransaction().commit();
            fail("Uma NegocioException deveria ter sido lançada!");
        } catch (NegocioException e) {
            if (getEntityManager().getTransaction().isActive()) {
                this.getEntityManager().getTransaction().rollback();
            }
            assertTrue("NegocioException Lançada", true);
        }
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
