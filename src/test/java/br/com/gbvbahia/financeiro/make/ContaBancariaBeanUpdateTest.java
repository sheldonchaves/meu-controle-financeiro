/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.TestesMake;
import br.com.gbvbahia.financeiro.beans.*;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.constantes.TipoConta;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.maker.MakeEntity;
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
    private static final Class[] USED_BEANS = TestesMake.getUseBeans();


    public ContaBancariaBeanUpdateTest() {
        super(ContaBancariaFacade.class, USED_BEANS);
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
        List<ContaBancaria> contas = criarContas();
        try {
            ContaBancariaFacade instance = getBean();
            ContaBancaria entity = instance.find(contas.get(0).getCodigo());
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
        TestesMake.tearDown(con);
    }
    
        /**
     * Cria 3 contas e 5 usuários. 1 e 2 com usr 0 - 1 (Conjuges) 3 com usr 2
     *
     * @return Contas criadas
     */
    private List<ContaBancaria> criarContas() throws Exception {
        List<Usuario> usuarios = TestesMake.makeEntitiesBD(getEntityManager(),
                Usuario.class, "test_1", 5, false);
        getEntityManager().getTransaction().begin();
        TestesMake.getUsuarioFacade().definirConjuge(usuarios.get(0), usuarios.get(1));
        TestesMake.getUsuarioFacade().definirConjuge(usuarios.get(2), usuarios.get(3));
        getEntityManager().getTransaction().commit();
        int count = 0;
        List<ContaBancaria> contas = MakeEntity.makeEntities("test_1",
                ContaBancaria.class, 3, false);
        count = 0;
        for (ContaBancaria conta : contas) {
            conta.setUsuario(usuarios.get(count++));
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(conta);
            getEntityManager().getTransaction().commit();
        }
        return contas;
    }
}
