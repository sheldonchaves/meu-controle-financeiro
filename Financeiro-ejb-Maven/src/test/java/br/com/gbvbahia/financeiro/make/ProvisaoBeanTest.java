/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.TestesMake;
import br.com.gbvbahia.financeiro.beans.business.interfaces.ProvisaoBusiness;
import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.constantes.Periodo;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.maker.MakeEntity;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import java.util.Date;
import org.junit.Test;

/**
 *
 * @author Usuário do Windows
 */
public class ProvisaoBeanTest extends BaseSessionBeanFixture<ProvisaoBusiness> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos o
     * Bean a ser testado.
     */
    private static final Class[] USED_BEANS =
            TestesMake.getUseBeans(ProcedimentoFacade.class,
            AgendaProcedimentoFixoFacade.class);

    /**
     * Padrão.
     */
    public ProvisaoBeanTest() {
        super(ProvisaoBusiness.class, USED_BEANS);
    }

    /**
     * Verifica se a data inicial da conta existente está sendo
     * considerada.
     *
     * @throws Exception Qualquer problema.
     */
    @Test
    public void testProvisionar() throws Exception {
        AgendaProcedimentoFixo agenda = MakeEntity.makeEntity("test_2", AgendaProcedimentoFixo.class);
        agenda.setDataPrimeiroVencimento(new Date());
        agenda.setPeriodo(Periodo.MESES);
        agenda.setUsuario(TestesMake.makeEntityBD(getEntityManager(), Usuario.class, "test_1", false));
        DetalheProcedimento detalhe = MakeEntity.makeEntity("test_1", DetalheProcedimento.class);
        detalhe.setUsuario(agenda.getUsuario());
        getEntityManager().getTransaction().begin();
        TestesMake.getDetalheProcedimentoFacade().create(detalhe);
        getEntityManager().getTransaction().commit();
        agenda.setDetalhe(detalhe);
        getEntityManager().getTransaction().begin();
        TestesMake.getAgendaFacade().create(agenda);
        getBean().provisionar(agenda);
        getEntityManager().getTransaction().commit();
        int exp = 13;
        int result = TestesMake.getProcedimentoFacade().findAll().size();
        assertEquals("Quantidade PROCEDIMENTOS Provisionados não bate,"
                + " data agenda: "
                + DateUtils.getDateDiaMesAno(agenda.getDataPrimeiroVencimento()),
                exp, result);
        for (Procedimento pro : TestesMake.getProcedimentoFacade().findAll()) {
            Date hoje = DateUtils.zerarHora(new Date());
            assertTrue("Data anterior criado em provisão", hoje.compareTo(pro.getDataMovimentacao()) <= 0);
        }
    }

    @Test
    public void testCriarProvisoesEProvisionar() throws Exception {
        for (int i = 0; i < 20; i++) {
            AgendaProcedimentoFixo agenda = MakeEntity.makeEntity("test_2", AgendaProcedimentoFixo.class);
            agenda.setDataPrimeiroVencimento(new Date());
            agenda.setAtiva(true);
            agenda.setPeriodo(Periodo.MESES);
            agenda.setUsuario(TestesMake.makeEntityBD(getEntityManager(), Usuario.class, "test_1", false));
            DetalheProcedimento detalhe = MakeEntity.makeEntity("test_1", DetalheProcedimento.class);
            DetalheProcedimento detalhe2 = MakeEntity.makeEntity("test_1", DetalheProcedimento.class);
            detalhe.setUsuario(agenda.getUsuario());
            detalhe2.setUsuario(agenda.getUsuario());
            getEntityManager().getTransaction().begin();
            TestesMake.getDetalheProcedimentoFacade().create(detalhe);
            TestesMake.getDetalheProcedimentoFacade().create(detalhe2);
            getEntityManager().getTransaction().commit();
            agenda.setDetalhe(detalhe);
            getEntityManager().getTransaction().begin();
            getBean().criarAgendaEProvisionar(agenda);
            getEntityManager().getTransaction().commit();
            int exp = 13;
            int result = TestesMake.getProcedimentoFacade().findAll().size();
            assertEquals("Quantidade PROCEDIMENTOS Provisionados não bate,"
                    + " data agenda: "
                    + DateUtils.getDateDiaMesAno(agenda.getDataPrimeiroVencimento()),
                    exp, result);
            agenda.setDetalhe(detalhe2);
            getEntityManager().getTransaction().begin();
            getBean().atualizarProvisao(agenda);
            getEntityManager().getTransaction().commit();
            for (Procedimento p : TestesMake.getProcedimentoFacade().findAll()) {
                if (!p.getDetalhe().equals(detalhe2)) {
                    fail("Detalhe de provisão é diferente de detalhe2");
                }
            }
            tearDown();
        }
    }

    /**
     * Provedor do Facede de Teste
     *
     * @return
     */
    private ProvisaoBusiness getBean() {
        ProvisaoBusiness instance = this.getBeanToTest();
        assertNotNull("EJB Não pode ser nulo!", instance);
        return instance;
    }

    /**
     * Se for uma base de dados a mesma deve ser limpa. Em memória não ha
     * necessidade.
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
