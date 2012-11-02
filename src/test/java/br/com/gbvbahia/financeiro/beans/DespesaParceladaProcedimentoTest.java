/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.Testes;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DespesaParceladaProcedimento;
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
public class DespesaParceladaProcedimentoTest
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


    public DespesaParceladaProcedimentoTest() {
        super(ProcedimentoFacade.class, USED_BEANS, USUARIO_CSV,
                DET_CSV, CARTAO_CSV);
    }

    @Test
    public void testCreateDespesaParcelada() throws Exception {
        ProcedimentoFacade instance = getBean();
        Procedimento pro1 = instance.find(1l);
        assertNotNull("Procedimento 1 Não pode ser nulo!", pro1);
        getEntityManager().getTransaction().begin();
        instance.create(pro1, 6, 1, null);
        getEntityManager().getTransaction().commit();
        List<Procedimento> todos = instance.findAll();
        assertEquals("Quantidade de Procedimentos não confere.",
                12, todos.size());
        String ui = "";
        for (Procedimento p : todos) {
            if (p instanceof DespesaParceladaProcedimento) {
                if (ui.equals("")) {
                    ui = ((DespesaParceladaProcedimento) p).getIdentificador();
                } else {
                    if (!((DespesaParceladaProcedimento) p)
                            .getIdentificador().equals(ui)) {
                        fail("ID unico parcelamento não está"
                                + " repetindo corretamente.");
                    }
                }
            }
        }
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
