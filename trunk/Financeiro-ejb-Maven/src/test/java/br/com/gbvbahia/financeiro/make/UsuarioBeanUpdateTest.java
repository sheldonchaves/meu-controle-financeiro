/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.TestesMake;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.maker.MakeEntity;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import java.util.List;
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
    private static final Class[] USED_BEANS = TestesMake.getUseBeans();

    public UsuarioBeanUpdateTest() {
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

    /**
     * Test of update method, of class UsuarioBean.
     */
    @Test
    public void testUpdate_GenericType() throws Exception {
        UsuarioFacade instance = getBean();
        Usuario ent = TestesMake.makeEntityBD(getEntityManager(), Usuario.class, "test_1", false);
        Usuario entity = instance.find(ent.getUserId());
        entity.setFirstName("Gui");
        getEntityManager().getTransaction().begin();
        instance.update(entity);
        getEntityManager().getTransaction().commit();
        Usuario us = instance.find(ent.getUserId());
        assertEquals(entity.getFirstName(), us.getFirstName());
    }

    @Test
    public void atualizarConjuge() throws Exception {
        UsuarioFacade instance = getBean();
        List<Usuario> usrs = TestesMake.makeEntitiesBD(getEntityManager(),
                Usuario.class, "test_1", 5, false);
        Usuario marido = usrs.get(0);
        Usuario esposa = usrs.get(1);
        marido.setConjuge(esposa);
        esposa.setConjuge(marido);
        getEntityManager().getTransaction().begin();
        instance.update(esposa);
        instance.update(marido);
        getEntityManager().getTransaction().commit();
        Usuario maridoCheck = instance.find(marido.getUserId());
        assertEquals("Esposa não é a mesma!",
                esposa, maridoCheck.getConjuge());
        assertNull("Este Usuario não pode ter conjuge", usrs.get(2).getConjuge());
        assertNull("Este Usuario não pode ter conjuge", usrs.get(3).getConjuge());
        assertNull("Este Usuario não pode ter conjuge", usrs.get(4).getConjuge());
    }

    /**
     * Tenta criar um usuário com login igual.
     *
     * @throws Exception Qualquer exceção é esperada.
     */
    @Test(expected = NegocioException.class)
    public void testCreate() throws Exception {
        try {
            Usuario criado = TestesMake.makeEntityBD(getEntityManager(),
                    Usuario.class, "test_1", true);
            UsuarioFacade instance = getBean();
            Usuario entity = MakeEntity.makeEntity("test_1", Usuario.class);
            entity.setUserId(criado.getUserId());//ID ja existe!!!
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
        TestesMake.tearDown(con);
    }
}
