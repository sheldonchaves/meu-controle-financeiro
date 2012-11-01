/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.beans.*;
import br.com.gbvbahia.financeiro.Testes;
import br.com.gbvbahia.financeiro.TestesMake;
import org.junit.*;
import static org.junit.Assert.*;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.testsuite.dataloader.CSVInitialDataSet;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;

/**
 * * <strong>Lembre-se de iniciar o JavaDB ante de executar os
 * testes. </strong><br>
 *
 * @author Guilherme
 */
public class UsuarioBeanRemoveTest
        extends BaseSessionBeanFixture<UsuarioFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos
     * o Bean a ser testado.
     */
    private static final Class[] USED_BEANS = Testes.getUseBeans();

    public UsuarioBeanRemoveTest() {
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
     * Test of remove method, of class UsuarioBean.
     */
    @Test
    public void testRemove() throws Exception {
        UsuarioFacade instance = getBean();
        Usuario entity = TestesMake.makeEntityBD(getEntityManager(),
                Usuario.class, "test_1", false);
        getEntityManager().getTransaction().begin();
        instance.remove(entity);
        getEntityManager().getTransaction().commit();
        assertTrue("Removido encontrado", instance.find(entity.getUserId()) == null);
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
