/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.TestesMake;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.maker.MakeEntity;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.utils.BasicDataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class AgendaProcedimentoFixoBeanCreateTest
        extends BaseSessionBeanFixture<AgendaProcedimentoFixoFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos o
     * Bean a ser testado.
     */
    private static final Class[] USED_BEANS = TestesMake.getUseBeans();

    public AgendaProcedimentoFixoBeanCreateTest() {
        super(AgendaProcedimentoFixoFacade.class, USED_BEANS);
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
        List<DetalheProcedimento> dd = criarDetalhes();
        Usuario user = dd.get(0).getUsuario();
        AgendaProcedimentoFixo entity = MakeEntity.makeEntity("test_1", AgendaProcedimentoFixo.class);
        entity.setUsuario(user);
        entity.setDetalhe(dd.get(0));
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
        List<DetalheProcedimento> dd = criarDetalhes();
        Usuario user = dd.get(1).getUsuario();
        AgendaProcedimentoFixo entity = MakeEntity.makeEntity("test_1", AgendaProcedimentoFixo.class);
        entity.setValorFixo(new BigDecimal("0.00"));
        entity.setUsuario(user);
        entity.setDetalhe(dd.get(1));
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
        List<DetalheProcedimento> dd = criarDetalhes();
        Usuario user = dd.get(2).getUsuario();
        AgendaProcedimentoFixo entity = MakeEntity.makeEntity("test_1", AgendaProcedimentoFixo.class);
        entity.setUsuario(user);
        entity.setDetalhe(dd.get(2));
        entity.setDataPrimeiroVencimento(null);
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
        List<DetalheProcedimento> dd = criarDetalhes();
        Usuario user = dd.get(3).getUsuario();
        AgendaProcedimentoFixo entity = MakeEntity.makeEntity("test_1", AgendaProcedimentoFixo.class);
        entity.setObservacao("Test");
        entity.setUsuario(user);
        entity.setDetalhe(dd.get(3));
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
        List<DetalheProcedimento> dd = criarDetalhes();
        Usuario user = dd.get(0).getUsuario();
        AgendaProcedimentoFixo entity = MakeEntity.makeEntity("test_1", AgendaProcedimentoFixo.class);
        entity.setObservacao("Testaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        entity.setUsuario(user);
        entity.setDetalhe(dd.get(0));
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
    private static final int DETALHES_CRIADOS = 4;

    /**
     * Cria 3 contas e 5 usuários. 1 e 2 com usr 0 - 1 (Conjuges) 3 com usr
     * 2
     *
     * @return Contas criadas
     */
    private List<DetalheProcedimento> criarDetalhes() throws Exception {
        List<Usuario> usuarios = TestesMake.makeEntitiesBD(getEntityManager(),
                Usuario.class, "test_1", 5, false);
        getEntityManager().getTransaction().begin();
        TestesMake.getUsuarioFacade().definirConjuge(usuarios.get(0), usuarios.get(1));
        TestesMake.getUsuarioFacade().definirConjuge(usuarios.get(2), usuarios.get(3));
        getEntityManager().getTransaction().commit();
        int count = 0;
        List<DetalheProcedimento> contas = MakeEntity.makeEntities("test_1",
                DetalheProcedimento.class, DETALHES_CRIADOS, false);
        count = 0;
        for (DetalheProcedimento detalhe : contas) {
            detalhe.setUsuario(usuarios.get(count++));
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(detalhe);
            getEntityManager().getTransaction().commit();
        }
        return contas;
    }
}
