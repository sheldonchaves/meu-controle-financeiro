/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.TestesMake;
import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.make.factories.PreparaProcedimentoWorkTest;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.maker.MakeEntity;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class ProcedimentoBeanTest
        extends BaseSessionBeanFixture<ProcedimentoFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos o
     * Bean a ser testado.
     */
    private static final Class[] USED_BEANS = TestesMake.getUseBeans(CartaoCreditoFacade.class);

    public ProcedimentoBeanTest() {
        super(ProcedimentoFacade.class, USED_BEANS);
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
        PreparaProcedimentoWorkTest.manager = getEntityManager();
       Procedimento receita = MakeEntity.makeEntity("test_1", Procedimento.class);
        System.out.println(receita);

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
