/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.superclass.DetalheProcedimento;
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
public class DetalheProcedimentoBeanSearchTest
        extends BaseSessionBeanFixture<DetalheProcedimentoFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos
     * o Bean a ser testado.
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
    private static final CSVInitialDataSet<ContaBancaria> CONTAS_CSV =
            Testes.getContasBancoCSV();
    /**
     * Cria dados com base no CSV X a classe informada.
     */
    private static final CSVInitialDataSet<DetalheProcedimento> DET_CSV =
            Testes.getDetalhesCSV();

    /**
     * Construtor padrão com informações para o teste.
     */
    public DetalheProcedimentoBeanSearchTest() {
        super(DetalheProcedimentoFacade.class, USED_BEANS,
                USUARIO_CSV, CONTAS_CSV, DET_CSV);
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
     * Conta quantos DetalheProcedimentos existem, são 4, duas
     * receitas e duas despesas criados no teste anterior.
     */
    @Test
    public void testCount() throws Exception {
        int expResult = Testes.LINHAS_DETALHE_CSV;
        DetalheProcedimentoFacade instance = getBean();
        int result = instance.count();
        assertEquals(expResult, result);
    }

    /**
     * Retorna todos os Detalhes como superclasse, são 4, duas
     * receitas e duas despesas criados no teste anterior.
     */
    @Test
    public void testAll() throws Exception {
        int expResult = Testes.LINHAS_DETALHE_CSV;
        DetalheProcedimentoFacade instance = getBean();
        List<DetalheProcedimento> result = instance.findAll();
        assertEquals(expResult, result.size());
    }

    /**
     * Somente uma receita ativa foi criada, pela esposa. Procuro pelo
     * marido e deve ser encontrada, apenas uma.
     * DetalheProcedimentoBean.
     */
    @Test
    public void testFindAllDetalheReceitaAtivoMarido() throws Exception {
        Usuario user = getEntityManager().find(Usuario.class, "user03");
        DetalheProcedimentoFacade instance = getBean();
        Boolean ativo = true;
        int expResult = 1;
        List result = instance.findAllDetalheReceita(user, ativo);
        assertEquals(expResult, result.size());
    }

    /**
     * Somente uma receita inativa foi criada, pela esposa. Procuro
     * pela mesma e deve ser encontrada, apenas uma.
     * DetalheProcedimentoBean.
     */
    @Test
    public void testFindAllDetalheReceitaInativoEsposa() throws Exception {
        Usuario user = getEntityManager().find(Usuario.class, "user04");
        DetalheProcedimentoFacade instance = getBean();
        Boolean ativo = false;
        int expResult = 1;
        List result = instance.findAllDetalheReceita(user, ativo);
        assertEquals(expResult, result.size());
    }

    /**
     * Duas receitas foram criadas, pela esposa. Procuro pelo marido e
     * todas duas devem ser encontradas. DetalheProcedimentoBean.
     */
    @Test
    public void testFindAllDetalheReceitaMarido() throws Exception {
        Usuario user = getEntityManager().find(Usuario.class, "user03");
        DetalheProcedimentoFacade instance = getBean();
        Boolean ativo = null;
        int expResult = 2;
        List result = instance.findAllDetalheReceita(user, ativo);
        assertEquals(expResult, result.size());
    }

    /**
     * Somente uma despesa ativa foi criada, pelo marido. Procuro pelo
     * mesmo e deve ser encontrada, apenas uma.
     * DetalheProcedimentoBean.
     */
    @Test
    public void testFindAllDetalheAtivoDespesaMarido() throws Exception {
        Usuario user = getEntityManager().find(Usuario.class, "user01");
        DetalheProcedimentoFacade instance = getBean();
        Boolean ativo = true;
        int expResult = 1;
        List result = instance.findAllDetalheDespesa(user, ativo);
        assertEquals(expResult, result.size());
    }

    /**
     * Somente uma despesa inativa foi criada, pelo marido. Procuro
     * pelo mesmo e deve ser encontrada, apenas uma.
     * DetalheProcedimentoBean.
     */
    @Test
    public void testFindAllDetalheDespesaInativoMarido() throws Exception {
        Usuario user = getEntityManager().find(Usuario.class, "user01");
        DetalheProcedimentoFacade instance = getBean();
        Boolean ativo = false;
        int expResult = 1;
        List result = instance.findAllDetalheDespesa(user, ativo);
        assertEquals(expResult, result.size());
    }

    /**
     * Duas despesas foram criadas, pelo marido. Procuro pelo marido e
     * todas duas devem ser encontradas. DetalheProcedimentoBean.
     */
    @Test
    public void testFindAllDetalheDespesaMarido() throws Exception {
        Usuario user = getEntityManager().find(Usuario.class, "user01");
        DetalheProcedimentoFacade instance = getBean();
        Boolean ativo = null;
        int expResult = 2;
        List result = instance.findAllDetalheDespesa(user, ativo);
        assertEquals(expResult, result.size());
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
