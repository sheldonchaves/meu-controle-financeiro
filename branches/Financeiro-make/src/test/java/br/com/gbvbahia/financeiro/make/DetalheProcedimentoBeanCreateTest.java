/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.TestesMake;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.modelos.DetalheDespesa;
import br.com.gbvbahia.financeiro.modelos.DetalheReceita;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.superclass.DetalheProcedimento;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class DetalheProcedimentoBeanCreateTest
        extends BaseSessionBeanFixture<DetalheProcedimentoFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos
     * o Bean a ser testado.
     */
    private static final Class[] USED_BEANS = TestesMake.getUseBeans();


    public DetalheProcedimentoBeanCreateTest() {
        super(DetalheProcedimentoFacade.class, USED_BEANS);
    }

    /**
     * Provedor do Facede de Teste
     *
     * @return
     */
    private DetalheProcedimentoFacade getBean() {
        DetalheProcedimentoFacade instance = this.getBeanToTest();
        assertNotNull("EJB Não pode ser nulo!", instance);
        return instance;
    }

    /**
     * Test Criar Despesa ativa.
     */
    @Test
    public void testCreateDespesa() throws Exception {
        DetalheProcedimentoFacade instance = getBean();
        final Usuario user = TestesMake.makeEntityBD(getEntityManager(), Usuario.class, "test_1", false);
        DetalheProcedimento entity = new DetalheDespesa();
        entity.setDetalhe("Transporte");
        entity.setUsuario(user);
        getEntityManager().getTransaction().begin();
        instance.create(entity);
        getEntityManager().getTransaction().commit();
        assertTrue("Despesa criada não encontrada",
                instance.findAllDetalheDespesa(user,
                Boolean.TRUE).size() == 1);
    }

    /**
     * Test Criar Receita ativa.
     */
    @Test
    public void testCreateReceita() throws Exception {
        DetalheProcedimentoFacade instance = getBean();
        final Usuario user = TestesMake.makeEntityBD(getEntityManager(), Usuario.class, "test_1", false);
        DetalheProcedimento entity = new DetalheReceita();
        entity.setDetalhe("Salário Mensal");
        entity.setUsuario(user);
        getEntityManager().getTransaction().begin();
        instance.create(entity);
        getEntityManager().getTransaction().commit();
        assertTrue("Despesa criada não encontrada",
                instance.findAllDetalheReceita(user,
                Boolean.TRUE).size() == 1);
    }

    /**
     * Criar receita bloqueada.
     *
     * @throws Exception
     */
    @Test
    public void testCreateReceitaBlock() throws Exception {
        DetalheProcedimentoFacade instance = getBean();
        final Usuario user = TestesMake.makeEntityBD(getEntityManager(), Usuario.class, "test_1", false);
        DetalheProcedimento entity = new DetalheReceita();
        entity.setDetalhe("Salário Semanal");
        entity.setUsuario(user);
        entity.setAtivo(false);
        getEntityManager().getTransaction().begin();
        instance.create(entity);
        getEntityManager().getTransaction().commit();
        assertTrue("Despesa criada não encontrada",
                !instance.findAllDetalheReceita(user,
                Boolean.FALSE).isEmpty());
    }

    /**
     * Test Criar Despesa bloqueada.
     */
    @Test
    public void testCreateDespesaBlock() throws Exception {
        DetalheProcedimentoFacade instance = getBean();
        final Usuario user = TestesMake.makeEntityBD(getEntityManager(), Usuario.class, "test_1", false);
        DetalheProcedimento entity = new DetalheDespesa();
        entity.setDetalhe("Escola");
        entity.setUsuario(user);
        entity.setAtivo(false);
        getEntityManager().getTransaction().begin();
        instance.create(entity);
        getEntityManager().getTransaction().commit();
        assertTrue("Despesa criada não encontrada",
                !instance.findAllDetalheDespesa(user,
                Boolean.FALSE).isEmpty());
    }

    /**
     * Tenta cria um detalheProcedimento com detalhe com maior
     * quantidade de caracteres do que é permitido.
     *
     * @throws Exception
     */
    @Test(expected = NegocioException.class)
    public void testCreateLimiteMaxCaracteres() throws Exception {
        DetalheProcedimentoFacade instance = getBean();
        final Usuario user = TestesMake.makeEntityBD(getEntityManager(), Usuario.class, "test_1", false);
        DetalheProcedimento entity = new DetalheDespesa();
        StringBuilder sb = new StringBuilder("");
        for (int i = 0;
                i <= DetalheProcedimento.QUANTIDADE_MAX_CARACTERES_DETALHE;
                i++) {
            if (i % 2 == 0) {
                sb.append("a");
            } else {
                sb.append("b");
            }
        }
        entity.setDetalhe(sb.toString());
        entity.setUsuario(user);
        entity.setAtivo(false);
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
     * Tenta cria um detalheProcedimento com detalhe com menor
     * quantidade de caracteres do que é permitido.
     *
     * @throws Exception
     */
    @Test(expected = NegocioException.class)
    public void testCreateLimiteMinCaracteres() throws Exception {
        DetalheProcedimentoFacade instance = getBean();
         final Usuario user = TestesMake.makeEntityBD(getEntityManager(), Usuario.class, "test_1", false);
        DetalheProcedimento entity = new DetalheDespesa();
        entity.setDetalhe("ABC");
        entity.setUsuario(user);
        entity.setAtivo(true);
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
        TestesMake.tearDown(con);
    }
}
