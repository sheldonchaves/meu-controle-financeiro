/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.constantes.TipoConta;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.Grupo;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.testsuite.dataloader.CSVInitialDataSet;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class ContaBancariaBeanCreateTest
        extends BaseSessionBeanFixture<ContaBancariaFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos
     * o Bean a ser testado.
     */
    private static final Class[] USED_BEANS = Testes.getUseBeans();
    /**
     * Cria dados com base no CSV X a classe informada.
     */
    private static final CSVInitialDataSet<Usuario> USUARIO_CSV =
            Testes.getUsuariosCSV();

    public ContaBancariaBeanCreateTest() {
        super(ContaBancariaFacade.class, USED_BEANS, USUARIO_CSV);
    }

    /**
     * Provedor do Facede de Teste
     *
     * @return
     */
    private ContaBancariaFacade getBean() {
        ContaBancariaFacade instance = this.getBeanToTest();
        assertNotNull("EJB Não pode ser nulo!", instance);
        return instance;
    }

    /**
     * Test of create method, of class ContaBancariaBean.
     */
    @Test
    public void testCreate() throws Exception {
        Usuario user = getEntityManager().find(Usuario.class, "user01");
        assertNotNull("Usuario para criar conta não pode ser nulo.",
                user);
        ContaBancaria entity = new ContaBancaria();
        entity.setNomeConta("Banco do Brasil Gui CC");
        entity.setTipoConta(TipoConta.CORRENTE);
        entity.setSaldo(300.00d);
        entity.setUsuario(user);
        ContaBancariaFacade instance = getBean();
        getEntityManager().getTransaction().begin();
        instance.create(entity);
        getEntityManager().getTransaction().commit();
        assertTrue("Contra criada não encontrada",
                instance.findAll(user, true).size() == 1);
    }

    /**
     * Test of create method, of class ContaBancariaBean.
     */
    @Test
    public void testCreateBlock() throws Exception {
        Usuario user = getEntityManager().find(Usuario.class, "user01");
        assertNotNull("Usuario para criar conta não pode ser nulo.",
                user);
        ContaBancaria entity = new ContaBancaria();
        entity.setNomeConta("Banco do Brasil Gui POU");
        entity.setTipoConta(TipoConta.POUPANCA);
        entity.setSaldo(00.00d);
        entity.setUsuario(user);
        entity.setStatus(false);
        ContaBancariaFacade instance = getBean();
        getEntityManager().getTransaction().begin();
        instance.create(entity);
        getEntityManager().getTransaction().commit();
        assertTrue("Contra criada não encontrada",
                instance.findAll(user, false).size() == 1);
    }

    /**
     * Testa criar uma conta com nome muito curto
     *
     * @throws Exception
     */
    @Test(expected = NegocioException.class)
    public void testCreateNomeSizeSmall() throws Exception {
        try {
            Usuario user = getEntityManager().find(Usuario.class, "user01");
            ContaBancaria entity = new ContaBancaria();
            entity.setNomeConta("BB");
            entity.setTipoConta(TipoConta.CORRENTE);
            entity.setSaldo(300.00d);
            entity.setUsuario(user);
            ContaBancariaFacade instance = getBean();
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
     * Testa criar uma conta com nome muito long
     *
     * @throws Exception
     */
    @Test(expected = NegocioException.class)
    public void testCreateNomeSizeLarge() throws Exception {
        try {
            Usuario user = getEntityManager().find(Usuario.class, "user01");
            ContaBancaria entity = new ContaBancaria();
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i <= ContaBancaria.CARACTERES_MAX_NOME_CONTA; i++) {
                if (i % 2 == 0) {
                    sb.append("a");
                } else {
                    sb.append("b");
                }
            }
            entity.setNomeConta(sb.toString());
            entity.setTipoConta(TipoConta.CORRENTE);
            entity.setSaldo(300.00d);
            entity.setUsuario(user);
            ContaBancariaFacade instance = getBean();
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
     * Tenta criar uma conta sem usuário.
     *
     * @throws Exception
     */
    @Test(expected = NegocioException.class)
    public void criarContaSemUsuario() throws Exception {
        try {
            ContaBancaria entity = new ContaBancaria();
            entity.setNomeConta("Banco do Brasil Gui CC");
            entity.setTipoConta(TipoConta.CORRENTE);
            entity.setSaldo(300.00d);
            ContaBancariaFacade instance = getBean();
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
     * Tenta criar uma conta sem saldo.
     *
     * @throws Exception
     */
    @Test(expected = NegocioException.class)
    public void criarContaSemSaldo() throws Exception {
        try {
            Usuario user = getEntityManager().find(Usuario.class, "user01");
            ContaBancaria entity = new ContaBancaria();
            entity.setNomeConta("Banco do Brasil Gui CC");
            entity.setTipoConta(TipoConta.CORRENTE);
            entity.setUsuario(user);
            entity.setSaldo(null);
            ContaBancariaFacade instance = getBean();
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
