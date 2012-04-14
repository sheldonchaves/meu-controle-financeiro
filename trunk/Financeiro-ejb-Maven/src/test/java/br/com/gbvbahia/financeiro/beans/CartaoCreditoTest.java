/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.testsuite.dataloader.CSVInitialDataSet;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
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
    private static final Class[] USED_BEANS = Testes.getUseBeans();
    /**
     * Cria dados com base no CSV X a classe informada.
     */
    private static final CSVInitialDataSet<Usuario> USUARIO_CSV =
            Testes.getUsuariosCSV();
    /**
     * Cria dados com base no CSV X a classe informada.
     */
    private static final CSVInitialDataSet<CartaoCredito> CARTAO_CSV =
            Testes.getCartaoCSV();

    public CartaoCreditoTest() {
        super(CartaoCreditoFacade.class, USED_BEANS, USUARIO_CSV,
                CARTAO_CSV);
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
        Usuario user = getEntityManager().find(Usuario.class, "user01");
        CartaoCreditoFacade instance = getBean();
        assertEquals("Quantidade de cartões ativos incorreto",
                1, instance.buscarCartoesAtivos(user).size());
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
        Testes.tearDown(con);
    }
}
