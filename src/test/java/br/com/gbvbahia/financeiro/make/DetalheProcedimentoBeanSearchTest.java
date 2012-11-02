/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.TestesMake;
import br.com.gbvbahia.financeiro.beans.*;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
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
public class DetalheProcedimentoBeanSearchTest
        extends BaseSessionBeanFixture<DetalheProcedimentoFacade> {

    private static final int DETALHES_CRIADOS = 4;
    /**
     * Define as classes que serão utilizadas durante o testes, menos o
     * Bean a ser testado.
     */
    private static final Class[] USED_BEANS = TestesMake.getUseBeans();

    /**
     * Construtor padrão com informações para o teste.
     */
    public DetalheProcedimentoBeanSearchTest() {
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
     * Conta quantos DetalheProcedimentos existem, são 4, duas receitas e
     * duas despesas criados no teste anterior.
     */
    @Test
    public void testCount() throws Exception {
        criarDetalhes();
        int expResult = DETALHES_CRIADOS;
        DetalheProcedimentoFacade instance = getBean();
        int result = instance.count();
        assertEquals(expResult, result);
    }

    /**
     * Retorna todos os Detalhes como superclasse, são 4, duas receitas e
     * duas despesas criados no teste anterior.
     */
    @Test
    public void testAll() throws Exception {
        int expResult = DETALHES_CRIADOS;
        criarDetalhes();
        DetalheProcedimentoFacade instance = getBean();
        List<DetalheProcedimento> result = instance.findAll();
        assertEquals(expResult, result.size());
    }

    /**
     * Somente uma receita ativa foi criada, pela esposa. Procuro pelo
     * marido e deve ser encontrada, apenas uma. DetalheProcedimentoBean.
     */
    @Test
    public void testFindAllDetalheReceitaAtivoMarido() throws Exception {
        List<DetalheProcedimento> detalhes = criarDetalhes();
        Usuario user = detalhes.get(0).getUsuario();
        Boolean ativo = detalhes.get(0).isAtivo();
        int expResult = 1;
        List result = getBean().findAllDetalhe(user, ativo, detalhes.get(0).getTipo());
        assertTrue("Quantidade de detalhes menor que o esperado.", expResult <= result.size());
    }

    /**
     * Somente uma receita inativa foi criada, pela esposa. Procuro pela
     * mesma e deve ser encontrada, apenas uma. DetalheProcedimentoBean.
     */
    @Test
    public void testFindAllDetalheReceitaInativoEsposa() throws Exception {
        List<DetalheProcedimento> detalhes = criarDetalhes();
        Usuario user = detalhes.get(1).getUsuario();
        Boolean ativo = detalhes.get(1).isAtivo();
        int expResult = 1;
        List result = getBean().findAllDetalhe(user, ativo, detalhes.get(1).getTipo());
        assertTrue("Quantidade de detalhes menor que o esperado.", expResult <= result.size());
    }

    /**
     * Duas receitas foram criadas, pela esposa. Procuro pelo marido e
     * todas duas devem ser encontradas. DetalheProcedimentoBean.
     */
    @Test
    public void testFindAllDetalheReceitaMarido() throws Exception {
        List<DetalheProcedimento> detalhes = criarDetalhes();
        Usuario user = detalhes.get(1).getUsuario();
        Boolean ativo = null;
        int expResult = 2;
        List result = getBean().findAllDetalhe(user, ativo, null);
        assertTrue("Quantidade de detalhes menor que o esperado.", expResult >= result.size());
    }

    /**
     * Somente uma despesa ativa foi criada, pelo marido. Procuro pelo
     * mesmo e deve ser encontrada, apenas uma. DetalheProcedimentoBean.
     */
    @Test
    public void testFindAllDetalheAtivoDespesaMarido() throws Exception {
        List<DetalheProcedimento> detalhes = criarDetalhes();
        Usuario user = detalhes.get(2).getUsuario();
        Boolean ativo = detalhes.get(2).isAtivo();
        int expResult = 1;
        List result = getBean().findAllDetalhe(user, ativo, null);
        assertTrue("Quantidade de detalhes menor que o esperado.", expResult <= result.size());
    }

    /**
     * Somente uma despesa inativa foi criada, pelo marido. Procuro pelo
     * mesmo e deve ser encontrada, apenas uma. DetalheProcedimentoBean.
     */
    @Test
    public void testFindAllDetalheDespesaInativoMarido() throws Exception {
        List<DetalheProcedimento> detalhes = criarDetalhes();
        Usuario user = detalhes.get(3).getUsuario();
        Boolean ativo = detalhes.get(3).isAtivo();;
        int expResult = 1;
        List result = getBean().findAllDetalhe(user, ativo, null);
        assertTrue("Quantidade de detalhes menor que o esperado.", expResult <= result.size());
    }

    /**
     * Duas despesas foram criadas, pelo marido. Procuro pelo marido e
     * todas duas devem ser encontradas. DetalheProcedimentoBean.
     */
    @Test
    public void testFindAllDetalheDespesaMarido() throws Exception {
        List<DetalheProcedimento> detalhes = criarDetalhes();
        Usuario user = detalhes.get(1).getUsuario();
        Boolean ativo = null;
        int expResult = 2;
        List result = getBean().findAllDetalhe(user, ativo, null);
        assertTrue("Quantidade de detalhes menor que o esperado.", expResult <= result.size());
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
