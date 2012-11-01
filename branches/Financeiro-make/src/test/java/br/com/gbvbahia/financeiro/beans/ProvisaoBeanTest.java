/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.Testes;
import br.com.gbvbahia.financeiro.beans.business.interfaces.ProvisaoFacade;
import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.superclass.DetalheProcedimento;
import br.com.gbvbahia.financeiro.modelos.superclass.Procedimento;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.testsuite.dataloader.CSVInitialDataSet;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class ProvisaoBeanTest
        extends BaseSessionBeanFixture<ProvisaoFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos o
     * Bean a ser testado.
     */
    private static final Class[] USED_BEANS =
            Testes.getUseBeans(ProcedimentoFacade.class,
            AgendaProcedimentoFixoFacade.class);
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
     * Padrão.
     */
    public ProvisaoBeanTest() {
        super(ProvisaoFacade.class, USED_BEANS,
                USUARIO_CSV, DET_CSV, CARTAO_CSV);
    }

    /**
     * Verifica se a data inicial da conta existente está sendo
     * considerada.
     *
     * @throws Exception Qualquer problema.
     */
    @Test
    public void testProvisionar() throws Exception {
        Testes.createAgendas(getEntityManager());
        ProvisaoFacade instance = getBean();
        AgendaProcedimentoFixo ag8 = Testes.getAgenda(Testes.getAgendaFacade(), "Salario Mensal");
        assertNotNull("ag1 Não pode ser nulo!", ag8);
        getEntityManager().getTransaction().begin();
        instance.provisionar(ag8);
        getEntityManager().getTransaction().commit();
        ProcedimentoFacade procedimentoFacade = lookupProcedimento();
        assertNotNull("lookup procedimento não pode ser nulo!",
                procedimentoFacade);
        List<Procedimento> procedimentos = procedimentoFacade.findAll();
        int totalCriados = 0;
        for (Procedimento p : procedimentos) {
            if (p.getObservacao().equals("Salario Mensal")) {
                totalCriados++;
            }
        }
        assertEquals("Total Procedimento criados não bate com esperado.",
                12, totalCriados);
//        assertEquals("Total Procedimento criados não bate com esperado.",
//                calcularContas(), totalCriados);
    }

    /**
     * Verifica se 12 contas são criadas.
     *
     * @throws Exception
     */
    @Test
    public void testProvisionar2() throws Exception {
        Testes.createAgendas(getEntityManager());
        ProvisaoFacade instance = getBean();
        AgendaProcedimentoFixo ag7 = Testes.getAgenda(Testes.getAgendaFacade(), "Condominio");
        assertNotNull("ag1 Não pode ser nulo!", ag7);
        getEntityManager().getTransaction().begin();
        instance.provisionar(ag7);
        getEntityManager().getTransaction().commit();
        ProcedimentoFacade procedimentoFacade = lookupProcedimento();
        assertNotNull("lookup procedimento não pode ser nulo!",
                procedimentoFacade);
        List<Procedimento> procedimentos = procedimentoFacade.findAll();
        int totalCriados = 0;
        for (Procedimento p : procedimentos) {
            if (p.getObservacao().equals("Condominio")) {
                totalCriados++;
            }
        }
        assertEquals("Total Procedimento criados não bate com esperado.",
                12, totalCriados);
    }

    /**
     * Realiza a conta de quantas contas devem ser criadas com base na
     * utltima conta criada no arquivo receita_procedimento com agenda id
     * 8. A maior data 08/01/2012
     *
     * @return Quantidade de contas que devem ter sido criadas.
     */
    private int calcularContas() {
        int toReturn = 0;
        Date conta = new Date();
        conta = DateUtils.setDays(conta, 1);
        conta = DateUtils.setYears(conta, 2012);
        conta = DateUtils.setMonths(conta, 8);
        Date hoje = DateUtils.addYears(new Date(), 1);
        while (conta.before(hoje)) {
            toReturn++;
            conta = DateUtils.addMonths(conta, 1);
        }
        return toReturn;
    }

    /**
     * Provedor do Facede de Teste
     *
     * @return
     */
    private ProvisaoFacade getBean() {
        ProvisaoFacade instance = this.getBeanToTest();
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
        Testes.tearDown(con);
    }

    private ProcedimentoFacade lookupProcedimento() {
        try {
            Context c = new InitialContext();
            return (ProcedimentoFacade) c.lookup("EJB3Unit/procedimentoFacade/remote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
