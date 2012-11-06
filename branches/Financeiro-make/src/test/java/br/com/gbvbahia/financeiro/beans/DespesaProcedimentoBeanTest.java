/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.Testes;
import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
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
     * Define as classes que serão utilizadas durante o testes, menos o
     * Bean a ser testado.
     */
    private static final Class[] USED_BEANS = Testes.getUseBeans(CartaoCreditoFacade.class);
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


    public DespesaProcedimentoBeanTest() {
        super(ProcedimentoFacade.class, USED_BEANS, USUARIO_CSV,
                DET_CSV, CARTAO_CSV);
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
        Testes.createAgendas(getEntityManager());
        Testes.criarDespProcedimentos(getEntityManager());
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
        final List<Procedimento> despesasT1 =
                instance.buscarCartaoStatusUsrTipoProcedimento(user1, null, null,  TipoProcedimento.DESPESA_FINANCEIRA);
        assertEquals("Quantidade de DespesaProcedimento não confere.",
                5, despesasT1.size());
        final List<Procedimento> despesasT2 =
                instance.buscarCartaoStatusUsrTipoProcedimento(user1,cc2, null, TipoProcedimento.DESPESA_FINANCEIRA);
        assertEquals("Quantidade de DespesaProcedimento não confere.",
                1, despesasT2.size());
        final List<Procedimento> despesasT3 =
                instance.buscarCartaoStatusUsrTipoProcedimento(user1, cc1, null, TipoProcedimento.DESPESA_FINANCEIRA);
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
