/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.TestesMake;
import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.maker.MakeEntity;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManager;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class CartaoCreditoTest
        extends BaseSessionBeanFixture<CartaoCreditoFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos o Bean a
     * ser testado.
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

    public void testProximoVencimento() throws Exception {
        CartaoCredito cartao = new CartaoCredito();
        cartao.setDiaMesmoMes(7);
        cartao.setDiaVencimento(6);
        Calendar c = Calendar.getInstance();
        c.set(2010, 0, 9);
        Date d = cartao.getProximoVencimento(c.getTime());
        Calendar compare = Calendar.getInstance();
        compare.setTime(d);
        assertEquals(compare.get(Calendar.DAY_OF_MONTH), 6);
        assertEquals(1, compare.get(Calendar.MONTH));
        assertEquals(2010, compare.get(Calendar.YEAR));

        c.set(2009, 11, 24);
        d = cartao.getProximoVencimento(c.getTime());
        compare.setTime(d);
        assertEquals(compare.get(Calendar.DAY_OF_MONTH), 6);
        assertEquals(0, compare.get(Calendar.MONTH));
        assertEquals(2010, compare.get(Calendar.YEAR));

        c.set(2009, 11, 30);
        d = cartao.getProximoVencimento(c.getTime());
        compare.setTime(d);
        assertEquals(compare.get(Calendar.DAY_OF_MONTH), 6);
        assertEquals(1, compare.get(Calendar.MONTH));
        assertEquals(2010, compare.get(Calendar.YEAR));

        c.set(2009, 11, 01);
        d = cartao.getProximoVencimento(c.getTime());
        compare.setTime(d);
        assertEquals(compare.get(Calendar.DAY_OF_MONTH), 6);
        assertEquals(0, compare.get(Calendar.MONTH));
        assertEquals(2010, compare.get(Calendar.YEAR));
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
