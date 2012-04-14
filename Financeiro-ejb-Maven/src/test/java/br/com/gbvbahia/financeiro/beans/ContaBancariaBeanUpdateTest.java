/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.constantes.TipoConta;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.Usuario;
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
public class ContaBancariaBeanUpdateTest
        extends BaseSessionBeanFixture<ContaBancariaFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos
     * o Bean a ser testado.
     */
    private static final Class[] USED_BEANS = Testes.getUseBeans();
    private static final CSVInitialDataSet<Usuario> USUARIO_CSV =
            Testes.getUsuariosConjugeCSV();
    private static final CSVInitialDataSet<ContaBancaria> CONTAS_CSV =
            Testes.getContasBancoCSV();

    public ContaBancariaBeanUpdateTest() {
        super(ContaBancariaFacade.class, USED_BEANS, USUARIO_CSV,
                CONTAS_CSV);
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
     * Tenta atualizar a conta bancaria para usuario nulo.
     */
    @Test(expected = NegocioException.class)
    public void testUpdate_UserNull() throws Exception {
        try {
            Usuario proprietario =
                    this.getEntityManager().find(Usuario.class,
                    "user01");
            ContaBancariaFacade instance = getBean();
            Boolean status = false;
            List<ContaBancaria> result =
                    instance.buscarTipoConta(TipoConta.POUPANCA,
                    proprietario, status);
            ContaBancaria entity = result.get(0);
            entity.setUsuario(null);
            getEntityManager().getTransaction().begin();
            instance.update(entity);
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
