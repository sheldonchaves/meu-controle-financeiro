/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.TestesMake;
import br.com.gbvbahia.financeiro.beans.*;
import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.maker.MakeEntity;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.testsuite.dataloader.CSVInitialDataSet;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class CartaoCreditoTest
        extends BaseSessionBeanFixture<CartaoCreditoFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos
     * o Bean a ser testado.
     */
    private static final Class[] USED_BEANS = TestesMake.getUseBeans();

    public CartaoCreditoTest() {
        super(CartaoCreditoFacade.class, USED_BEANS);
    }

    /**
     * Provedor do Facede de Teste
     *
     * @return
     */
    private CartaoCreditoFacade getBean() {
        CartaoCreditoFacade instance = this.getBeanToTest();
        assertNotNull("EJB Não pode ser nulo!", instance);
        return instance;
    }

    @Test
    public void testBuscarCartoesAtivo() throws Exception {
        Usuario user = TestesMake.makeEntityBD(getEntityManager(), Usuario.class, "test_1", false);
        popularBanco(getEntityManager(), user);
        CartaoCreditoFacade instance = getBean();
        assertEquals("Quantidade de cartões ativos incorreto",
                10, instance.buscarCartoesAtivos(user).size());
    }

    private void popularBanco(EntityManager manager, Usuario userCartao)
            throws Exception {
        for (int i = 0; i < 10; i++) {
            CartaoCredito cartaoCredito = MakeEntity.makeEntity("test_1", CartaoCredito.class, false);
            cartaoCredito.setAtivo(true);
            cartaoCredito.setUsuario(userCartao);
            manager.getTransaction().begin();
            manager.persist(cartaoCredito);
            manager.getTransaction().commit();
        }
  
    }
    
    /**
     * Se for uma base de dados a mesma deve ser limpa.
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
