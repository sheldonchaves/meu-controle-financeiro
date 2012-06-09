/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.Testes;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import br.com.gbvbahia.financeiro.modelos.DetalheDespesa;
import br.com.gbvbahia.financeiro.modelos.DetalheReceita;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.superclass.DetalheProcedimento;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.testsuite.dataloader.CSVInitialDataSet;
import com.bm.utils.BasicDataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class AgendaProcedimentoFixoBeanCreateTest
        extends BaseSessionBeanFixture<AgendaProcedimentoFixoFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos
     * o Bean a ser testado.
     */
    private static final Class[] USED_BEANS = Testes.getUseBeans();
    private static final CSVInitialDataSet<Usuario> USUARIO_CSV =
            Testes.getUsuariosConjugeCSV();
    private static final CSVInitialDataSet<DetalheProcedimento> DET_CSV =
            Testes.getDetalhesCSV();

    public AgendaProcedimentoFixoBeanCreateTest() {
        super(AgendaProcedimentoFixoFacade.class, USED_BEANS,
                USUARIO_CSV, DET_CSV);
    }

    /**
     * Provedor do Facede de Teste
     *
     * @return
     */
    private AgendaProcedimentoFixoFacade getBean() {
        AgendaProcedimentoFixoFacade instance = this.getBeanToTest();
        assertNotNull("EJB Não pode ser nulo!", instance);
        return instance;
    }

    /**
     * Test of create method, of class AgendaProcedimentoFixoBean.
     */
    @Test
    public void testCreate() throws Exception {
        AgendaProcedimentoFixoFacade instance = getBean();
        Usuario user = getEntityManager().find(Usuario.class, "user03");
        DetalheDespesa dd = getEntityManager().find(DetalheDespesa.class, 1l);
        AgendaProcedimentoFixo entity = new AgendaProcedimentoFixo();
        entity.setValorFixo(new BigDecimal("10.00"));
        entity.setDataPrimeiroVencimento(new Date());
        entity.setObservacao("Teste de Agenda.");
        entity.setUsuario(user);
        entity.setDetalhe(dd);
        getEntityManager().getTransaction().begin();
        instance.create(entity);
        getEntityManager().getTransaction().commit();
        assertNotNull("Entidade com ID null após ser gravada.",
                entity.getCodigo());
    }

    /**
     * Testa um valor menor que 0.01
     */
    @Test(expected = NegocioException.class)
    public void testCreateValorErro() throws Exception {
        AgendaProcedimentoFixoFacade instance = getBean();
        Usuario user = getEntityManager().find(Usuario.class, "user01");
        DetalheReceita dr = getEntityManager().find(DetalheReceita.class, 3l);
        AgendaProcedimentoFixo entity = new AgendaProcedimentoFixo();
        entity.setValorFixo(new BigDecimal("0.00"));
        entity.setDataPrimeiroVencimento(new Date());
        entity.setObservacao("Teste de Agenda Erro Valor.");
        entity.setUsuario(user);
        entity.setDetalhe(dr);
        try {
            getEntityManager().getTransaction().begin();
            instance.create(entity);
            getEntityManager().getTransaction().commit();
            fail("Uma NegocioException deveria ter sido lançada!");
        } catch (NegocioException e) {
            if (getEntityManager().getTransaction().isActive()) {
                this.getEntityManager().getTransaction().rollback();
            }
            assertTrue("NegocioException Lançada", true);
        }
    }

    /**
     * Testa um valor menor que 0.01
     */
    @Test(expected = NegocioException.class)
    public void testCreateDataNula() throws Exception {
        AgendaProcedimentoFixoFacade instance = getBean();
        Usuario user = getEntityManager().find(Usuario.class, "user01");
        DetalheReceita dr = getEntityManager().find(DetalheReceita.class, 3l);

        AgendaProcedimentoFixo entity = new AgendaProcedimentoFixo();
        entity.setValorFixo(new BigDecimal("10.00"));
        entity.setObservacao("Teste de Agenda Data nula.");
        entity.setUsuario(user);
        entity.setDetalhe(dr);
        try {
            getEntityManager().getTransaction().begin();
            instance.create(entity);
            getEntityManager().getTransaction().commit();
            fail("Uma NegocioException deveria ter sido lançada!");
        } catch (NegocioException e) {
            if (getEntityManager().getTransaction().isActive()) {
                this.getEntityManager().getTransaction().rollback();
            }
            assertTrue("NegocioException Lançada", true);
        }
    }

    /**
     * Testa um valor menor que 0.01
     */
    @Test(expected = NegocioException.class)
    public void testCreateObsCurta() throws Exception {
        AgendaProcedimentoFixoFacade instance = getBean();
        Usuario user = getEntityManager().find(Usuario.class, "user01");
        DetalheReceita dr = getEntityManager().find(DetalheReceita.class, 3l);
        AgendaProcedimentoFixo entity = new AgendaProcedimentoFixo();
        entity.setValorFixo(new BigDecimal("10.00"));
        entity.setDataPrimeiroVencimento(new Date());
        entity.setObservacao("Test");
        entity.setUsuario(user);
        entity.setDetalhe(dr);
        try {
            getEntityManager().getTransaction().begin();
            instance.create(entity);
            getEntityManager().getTransaction().commit();
            fail("Uma NegocioException deveria ter sido lançada!");
        } catch (NegocioException e) {
            if (getEntityManager().getTransaction().isActive()) {
                this.getEntityManager().getTransaction().rollback();
            }
            assertTrue("NegocioException Lançada", true);
        }
    }

    /**
     * Testa um valor menor que 0.01
     */
    @Test(expected = NegocioException.class)
    public void testCreateObsLong() throws Exception {
        AgendaProcedimentoFixoFacade instance = getBean();
        Usuario user = getEntityManager().find(Usuario.class, "user01");
        DetalheReceita dr = getEntityManager().find(DetalheReceita.class, 3l);
        AgendaProcedimentoFixo entity = new AgendaProcedimentoFixo();
        entity.setValorFixo(new BigDecimal("10.00"));
        entity.setDataPrimeiroVencimento(new Date());
        entity.setObservacao("Testaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        entity.setUsuario(user);
        entity.setDetalhe(dr);
        try {
            getEntityManager().getTransaction().begin();
            instance.create(entity);
            getEntityManager().getTransaction().commit();
            fail("Uma NegocioException deveria ter sido lançada!");
        } catch (NegocioException e) {
            if (getEntityManager().getTransaction().isActive()) {
                this.getEntityManager().getTransaction().rollback();
            }
            assertTrue("NegocioException Lançada", true);
        }
    }

    /**
     * Se for uma base de dados a mesma deve ser limpa. Em memória não
     * ha necessidade.
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
