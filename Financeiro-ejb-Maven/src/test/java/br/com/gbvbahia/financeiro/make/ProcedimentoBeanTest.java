/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.TestesMake;
import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.make.factories.PreparaProcedimentoWorkTest;
import br.com.gbvbahia.financeiro.make.factories.PrepareDespesaProcedimentoWorkTest;
import br.com.gbvbahia.financeiro.modelos.DespesaParceladaProcedimento;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.maker.MakeEntity;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import java.util.Calendar;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class ProcedimentoBeanTest
        extends BaseSessionBeanFixture<ProcedimentoFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos o Bean a
     * ser testado.
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
    public void testBuscarProcedimento() throws Exception {
        PreparaProcedimentoWorkTest.manager = getEntityManager();
        ProcedimentoFacade facade = getBean();
        Procedimento receita1 = MakeEntity.makeEntity("test_1", Procedimento.class);
        Procedimento receita2 = MakeEntity.makeEntity("test_1", Procedimento.class);
        receita2.setUsuario(TestesMake.makeEntityBD(getEntityManager(), Usuario.class, "test_1", false));
        getEntityManager().getTransaction().begin();
        facade.create(receita1);
        facade.create(receita2);
        getEntityManager().getTransaction().commit();
        int exp = 1;
        int result = facade.buscarStatusUsrTipo(receita1.getUsuario(),
                null, TipoProcedimento.RECEITA_FINANCEIRA).size();
        assertEquals("Quantidade INDIVIDUAL RECEITA_FINANCEIRA não bate.", exp, result);
        exp = 2;
        result = facade.findAll().size();
        assertEquals("Quantidade TOTAL RECEITA_FINANCEIRA não bate.", exp, result);
    }

    @Test
    public void testBuscarCartaoStatusUsrTipoProcedimento() throws Exception {
        PreparaProcedimentoWorkTest.manager = getEntityManager();
        ProcedimentoFacade facade = getBean();
        Procedimento receita1 = MakeEntity.makeEntity("test_1", Procedimento.class);
        Procedimento receita2 = MakeEntity.makeEntity("test_1", Procedimento.class);
        PrepareDespesaProcedimentoWorkTest.manager = getEntityManager();
        DespesaParceladaProcedimento despesa = MakeEntity.makeEntity("test_1", DespesaParceladaProcedimento.class);
        //Nesta posicao garante que receita1 e despesa tenham o mesmo usuario.
        receita2.setUsuario(TestesMake.makeEntityBD(getEntityManager(), Usuario.class, "test_1", false));
        getEntityManager().getTransaction().begin();
        facade.create(receita1);
        facade.create(receita2);
        //Cria duas por causa do parcelamento
        facade.create(despesa, despesa.getParcelaTotal(), despesa.getParcelaAtual(), despesa.getCartaoCredito());
        getEntityManager().getTransaction().commit();
        int exp = 4;
        int result = facade.findAll().size();
        assertEquals("Quantidade PROCEDIMENTOS RCEITA E DESPESA não bate.", exp, result);
        exp = 2;//Query de buscarCartaoStatusUsrTipoProcedimento roda sobre DespesaParceladaProcedimento ignorando as duas receitas.
        result = facade.buscarPorUsuarioCartaoStatusData(despesa.getUsuario(), null, null, null, null).size();
        assertEquals("Quantidade PROCEDIMENTOS DESPESA não bate.", exp, result);
        exp = 1;
        result = facade.buscarPorUsuarioCartaoStatusData(despesa.getUsuario(), null, StatusPagamento.NAO_PAGA, null, null).size();
        assertEquals("Quantidade PROCEDIMENTOS DESPESA NAO PAGA não bate.", exp, result);
        result = facade.buscarPorUsuarioCartaoStatusData(despesa.getUsuario(), null, null,
                DateUtils.incrementar(despesa.getDataCartao(), 1, Calendar.DAY_OF_MONTH), null).size();
        exp = 2;
        assertEquals("Quantidade PROCEDIMENTOS DESPESA FILTRO DATAI não bate.", exp, result);
        result = facade.buscarPorUsuarioCartaoStatusData(despesa.getUsuario(), null, null,
                null, DateUtils.incrementar(despesa.getDataCartao(), 1, Calendar.DAY_OF_MONTH)).size();
        exp = 0;
        assertEquals("Quantidade PROCEDIMENTOS DESPESA FILTRO DATAF não bate.", exp, result);
        exp = 2;
        result = facade.buscarPorUsuarioCartaoStatusData(despesa.getUsuario(), despesa.getCartaoCredito(), null, null, null).size();
        assertEquals("Quantidade PROCEDIMENTOS DESPESA FILTRO CARTAO não bate.", exp, result);
        exp = 0;
        result = facade.buscarPorUsuarioCartaoStatusData(despesa.getUsuario(), despesa.getCartaoCredito(), null, null,
                DateUtils.incrementar(despesa.getDataCartao(), 1, Calendar.DAY_OF_MONTH)).size();
        assertEquals("Quantidade PROCEDIMENTOS DESPESA FILTRO CARTAO E DATAF não bate.", exp, result);
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
