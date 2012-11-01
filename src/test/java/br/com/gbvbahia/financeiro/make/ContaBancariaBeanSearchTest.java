/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.beans.*;
import br.com.gbvbahia.financeiro.Testes;
import br.com.gbvbahia.financeiro.TestesMake;
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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class ContaBancariaBeanSearchTest
        extends BaseSessionBeanFixture<ContaBancariaFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos o Bean a
     * ser testado.
     */
    private static final Class[] USED_BEANS = Testes.getUseBeans();

    public ContaBancariaBeanSearchTest() {
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
     * Busca todas as contas, parâmetro proprietario passado esposa. Status
     * deve ser ignorado. Uma conta bloqueada poupança e uma conta corrente
     * foram criadas é para retornar duas contas.
     */
    @Test
    public void testFindAll_Usuario_Boolean() throws Exception {
        List<ContaBancaria> contas = criarContas();
        Usuario proprietario = contas.get(0).getUsuario();
        ContaBancariaFacade instance = getBean();
        Boolean status = null;
        List<ContaBancaria> result = instance.findAll(proprietario, status);
        assertTrue("Conta conjuge não encontrada",
                result.size() == 2);
    }

    /**
     * Busca contas somente ativas, parâmetro proprietário esposa.
     */
    @Test
    public void testFindAll_Usuario_Boolean2() throws Exception {
        List<ContaBancaria> contas = criarContas();
        Usuario esposa = contas.get(1).getUsuario();
        int expected = 0;
        expected = (contas.get(0).isStatus()) ? expected + 1 : expected;
        expected = (contas.get(1).isStatus()) ? expected + 1 : expected;
        ContaBancariaFacade instance = getBean();
        Boolean status = true;
        List<ContaBancaria> result = instance.findAll(esposa, status);
        assertTrue("Conta marido ativa não encontrado.",
                result.size() == expected);
    }

    /**
     * Busca somente contas canceladas, proprietário o proprio.
     */
    @Test
    public void testFindAll_Usuario_Boolean3() throws Exception {
        List<ContaBancaria> contas = criarContas();
        Usuario proprietario = contas.get(0).getUsuario();
        ContaBancariaFacade instance = getBean();
        int expected = 0;
        expected = (!contas.get(0).isStatus()) ? expected + 1 : expected;
        expected = (!contas.get(1).isStatus()) ? expected + 1 : expected;
        Boolean status = false;
        List<ContaBancaria> result = instance.findAll(proprietario, status);
        assertTrue("Conta proprietario inativa não encontrado.",
                result.size() == expected);
    }

    /**
     * Busca conta corrente em qualquer status, deve retornar uma conta a
     * outra criada foi poupança
     */
    @Test
    public void testBuscarTipoConta() throws Exception {
        List<ContaBancaria> contas = criarContas();
        Usuario proprietario = contas.get(2).getUsuario();
        ContaBancariaFacade instance = getBean();
        Boolean status = null;
        List result = instance.buscarTipoConta(contas.get(2).getTipoConta(), proprietario, status);
        assertTrue("Busca conta corrente", result.size() == 1);
    }

    /**
     * Busca conta corrente em status cancelada, não deve retornar a conta
     * corrente criada está ativa.
     */
    @Test
    public void testBuscarTipoConta2() throws Exception {
        List<ContaBancaria> contas = criarContas();
        Usuario proprietario = contas.get(2).getUsuario();
        ContaBancariaFacade instance = getBean();
        List result = instance.buscarTipoConta(contas.get(2).getTipoConta(), proprietario, !contas.get(2).isStatus());
        assertTrue("Busca conta corrente", result.isEmpty());
    }

    /**
     * Busca conta pupança em status cancelada, deve retornar uma a conta
     * poupança criada está cancelada.
     */
    @Test
    public void testBuscarTipoConta3() throws Exception {
        List<ContaBancaria> contas = criarContas();
        Usuario proprietario = contas.get(2).getUsuario();
        ContaBancariaFacade instance = getBean();
        List result = instance.buscarTipoConta(contas.get(2).getTipoConta(), proprietario, contas.get(2).isStatus());
        assertTrue("Busca conta poupança", result.size() == 1);
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
