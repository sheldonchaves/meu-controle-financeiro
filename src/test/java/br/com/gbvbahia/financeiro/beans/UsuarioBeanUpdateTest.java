/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.Testes;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.testsuite.dataloader.CSVInitialDataSet;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import org.junit.Test;

/**
 * * <strong>Lembre-se de iniciar o JavaDB ante de executar os
 * testes. </strong><br>
 *
 * @author Guilherme
 */
public class UsuarioBeanUpdateTest
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

    public UsuarioBeanUpdateTest() {
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

    /**
     * Test of update method, of class UsuarioBean.
     */
    @Test
    public void testUpdate_GenericType() throws Exception {
        UsuarioFacade instance = getBean();
        Usuario ent = new Usuario();
        ent.setEmail("gbvbahia02@hotmail.com");
        ent.setFirstName("Guilhe");
        ent.setUserId("gbvb");
        ent.setPass("102030");
        ent.setLastName("Bra");
        getEntityManager().getTransaction().begin();
        instance.create(ent);
        getEntityManager().getTransaction().commit();
        Usuario entity = instance.find("gbvb");
        entity.setFirstName("Gui");
        getEntityManager().getTransaction().begin();
        instance.update(entity);
        getEntityManager().getTransaction().commit();
        Usuario us = instance.find("gbvb");
        assertEquals(entity.getFirstName(), us.getFirstName());
    }

    @Test
    public void atualizarConjuge() throws Exception {
        UsuarioFacade instance = getBean();
        Usuario entity = new Usuario();
        entity.setEmail("esposa@hotmail.com");
        entity.setFirstName("Esposa");
        entity.setUserId("esposa");
        entity.setPass("123456");
        entity.setLastName("Mulher");
        getEntityManager().getTransaction().begin();
        instance.create(entity);
        getEntityManager().getTransaction().commit();
        Usuario marido = instance.find("user01");
        marido.setConjuge(entity);
        entity.setConjuge(marido);
        getEntityManager().getTransaction().begin();
        instance.update(entity);
        instance.update(marido);
        getEntityManager().getTransaction().commit();
        Usuario maridoCheck = instance.find("user01");
        assertEquals("Esposa não é a mesma!",
                entity, maridoCheck.getConjuge());
    }

    /**
     * Tenta criar um usuário com login igual.
     *
     * @throws Exception Qualquer exceção é esperada.
     */
    @Test(expected = NegocioException.class)
    public void testCreate() throws Exception {
        try {
            UsuarioFacade instance = getBean();
            Usuario entity = new Usuario();
            entity.setEmail("outro@hotmail.com");
            entity.setFirstName("Outro");
            entity.setUserId("user01");
            entity.setPass("123456");
            entity.setLastName("Outro");
            getEntityManager().getTransaction().begin();
            instance.create(entity);
            getEntityManager().getTransaction().commit();
            fail("Negócio exception deve ser lançada.");
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
