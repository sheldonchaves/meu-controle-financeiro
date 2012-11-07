/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.Testes;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
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
public class ProcedimentoBeanTest
        extends BaseSessionBeanFixture<ProcedimentoFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos o
     * Bean a ser testado.
     */
    private static final Class[] USED_BEANS = Testes.getUseBeans();
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


    public ProcedimentoBeanTest() {
        super(ProcedimentoFacade.class, USED_BEANS, USUARIO_CSV,
                DET_CSV, CARTAO_CSV);
    }

    @Test
    public void testBuscarPorTipoProcedimento() throws Exception {
        Testes.createAgendas(getEntityManager());
        Testes.criarDespProcedimentos(getEntityManager());
        Testes.criarReceitaProcedimentos(getEntityManager());
        ProcedimentoFacade instance = getBean();
        Usuario user = getEntityManager().find(Usuario.class, "user01");
        assertNotNull("Usuario Não pode ser nulo!", user);
        List<Procedimento> despesas =
                instance.buscarPorUsuarioTipoProcedimento(user,
                TipoProcedimento.DESPESA_FINANCEIRA);
        assertEquals("Despesas valor incorreto.", 5, despesas.size());
    }

    /**
     * Provedor do Facede de Teste.
     *
     * @return Bean a ser testado.
     */
    private ProcedimentoFacade getBean() {
        ProcedimentoFacade instance = this.getBeanToTest();
        assertNotNull("EJB Não pode ser nulo!", instance);
        return instance;
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
        Testes.tearDown(con);
    }
}
