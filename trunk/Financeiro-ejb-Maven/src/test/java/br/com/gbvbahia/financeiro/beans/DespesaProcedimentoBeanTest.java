/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.Testes;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.superclass.DetalheProcedimento;
import br.com.gbvbahia.financeiro.modelos.superclass.Procedimento;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.testsuite.dataloader.CSVInitialDataSet;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class DespesaProcedimentoBeanTest
        extends BaseSessionBeanFixture<ProcedimentoFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos
     * o Bean a ser testado.
     */
    private static final Class[] USED_BEANS = Testes.getUseBeans();
    /**
     * Cria dados com base no CSV X a classe informada.
     */
    private static final CSVInitialDataSet<Usuario> USUARIO_CSV =
            Testes.getUsuariosConjugeCSV();
    /**
     * Cria dados com base no CSV X a classe informada.
     */
    private static final CSVInitialDataSet<DetalheProcedimento> DET_CSV =
            Testes.getDetalhesCSV();
    /**
     * Cria dados com base no CSV X a classe informada.
     */
    private static final CSVInitialDataSet<CartaoCredito> CARTAO_CSV =
            Testes.getCartaoCSV();
    /**
     * Cria dados com base no CSV X a classe informada.
     */
    private static final CSVInitialDataSet<DespesaProcedimento>
            DESP_PROCEDIMENTO_CSV = Testes.getDespProcimentoCSV();

    public DespesaProcedimentoBeanTest() {
        super(ProcedimentoFacade.class, USED_BEANS, USUARIO_CSV,
                DET_CSV, CARTAO_CSV, DESP_PROCEDIMENTO_CSV);
    }

    /**
     * Provedor do Facede de Teste.
     *
     * @return
     */
    private ProcedimentoFacade getBean() {
        ProcedimentoFacade instance = this.getBeanToTest();
        assertNotNull("EJB Não pode ser nulo!", instance);
        return instance;
    }

    @Test
    public void testBuscarDespesaProcedimento() throws Exception {
        ProcedimentoFacade instance = getBean();
        Usuario user1 = getEntityManager().find(Usuario.class, "user01");
        assertNotNull("Usuario Não pode ser nulo!", user1);
        CartaoCredito cc2 = getEntityManager().find(CartaoCredito.class, 2l);
        assertNotNull("Cartão ID 2 Não pode ser nulo!", cc2);
        CartaoCredito cc1 = getEntityManager().find(CartaoCredito.class, 1l);
        assertNotNull("Cartão ID 1 Não pode ser nulo!", cc1);
        List<Procedimento> todos = instance.findAll();
        assertEquals("Quantidade de DespesaProcedimento não confere.",
                6, todos.size());
        final List<DespesaProcedimento> despesasT1 =
                instance.buscarDespesaProcedimento(null, null, user1);
        assertEquals("Quantidade de DespesaProcedimento não confere.",
                5, despesasT1.size());
        final List<DespesaProcedimento> despesasT2 =
                instance.buscarDespesaProcedimento(cc2, null, user1);
        assertEquals("Quantidade de DespesaProcedimento não confere.",
                1, despesasT2.size());
        final List<DespesaProcedimento> despesasT3 =
                instance.buscarDespesaProcedimento(cc1, null, user1);
        assertEquals("Quantidade de DespesaProcedimento não confere.",
                2, despesasT3.size());
        
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
