/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.TestesMake;
import br.com.gbvbahia.financeiro.beans.*;
import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.maker.MakeEntity;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.testsuite.dataloader.CSVInitialDataSet;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class AgendaProcedimentoFixoBeanSearchTest
        extends BaseSessionBeanFixture<AgendaProcedimentoFixoFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos o
     * Bean a ser testado.
     */
    private static final Class[] USED_BEANS = TestesMake.getUseBeans();


    public AgendaProcedimentoFixoBeanSearchTest() {
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
     * Test of findRange method, of class AgendaProcedimentoFixoBean.
     */
    @Test
    public void testFindRange() throws Exception {
        List<AgendaProcedimentoFixo> list = criarAgendas();
        Usuario user = list.get(3).getUsuario();
        List<Long> ids = new ArrayList<Long>();
        assertEquals("Quantidade retornada diferente da esperada.",
                DETALHES_CRIADOS, list.size());
        for (AgendaProcedimentoFixo apf : list) {
            if (ids.contains(apf.getCodigo())) {
                fail("Id repetido em busca distinta.");
            }
            ids.add(apf.getCodigo());
        }
        assertTrue("Não existe ID repetidos.", true);
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
    private List<AgendaProcedimentoFixo> criarAgendas() throws Exception {
        List<Usuario> usuarios = TestesMake.makeEntitiesBD(getEntityManager(),
                Usuario.class, "test_1", 5, false);
        getEntityManager().getTransaction().begin();
        TestesMake.getUsuarioFacade().definirConjuge(usuarios.get(0), usuarios.get(1));
        TestesMake.getUsuarioFacade().definirConjuge(usuarios.get(2), usuarios.get(3));
        getEntityManager().getTransaction().commit();
        int count = 0;
        List<DetalheProcedimento> contas = MakeEntity.makeEntities("test_1", DetalheProcedimento.class, DETALHES_CRIADOS, false);
        List<AgendaProcedimentoFixo> agendas = MakeEntity.makeEntities("test_1", AgendaProcedimentoFixo.class, DETALHES_CRIADOS, false);
        count = 0;
        for (DetalheProcedimento detalhe : contas) {
            detalhe.setUsuario(usuarios.get(count));
            agendas.get(count).setDetalhe(detalhe);
            agendas.get(count).setUsuario(detalhe.getUsuario());
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(detalhe);
            getEntityManager().persist(agendas.get(count++));
            getEntityManager().getTransaction().commit();
        }
        return agendas;
    }
}
