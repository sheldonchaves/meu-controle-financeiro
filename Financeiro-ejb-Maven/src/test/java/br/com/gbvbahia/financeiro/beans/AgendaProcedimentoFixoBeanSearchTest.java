/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.superclass.DetalheProcedimento;
import com.bm.cfg.Ejb3UnitCfg;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.testsuite.dataloader.CSVInitialDataSet;
import com.bm.utils.BasicDataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class AgendaProcedimentoFixoBeanSearchTest
        extends BaseSessionBeanFixture<AgendaProcedimentoFixoFacade> {

    /**
     * Define as classes que serão utilizadas durante o testes, menos
     * o Bean a ser testado.
     */
    private static final Class[] USED_BEANS = Testes.getUseBeans();
    private static final CSVInitialDataSet<Usuario> USUARIO_CSV =
            Testes.getUsuariosConjugeCSV();
    private static final CSVInitialDataSet<DetalheProcedimento> DET_CSV =
            Testes.getDetalhesCSV();
    private static final CSVInitialDataSet<AgendaProcedimentoFixo> AGENDA_CSV =
            Testes.getAgendaCSV();

    public AgendaProcedimentoFixoBeanSearchTest() {
        super(AgendaProcedimentoFixoFacade.class, USED_BEANS,
                USUARIO_CSV, DET_CSV, AGENDA_CSV);
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
        AgendaProcedimentoFixoFacade instance = getBean();
        List<Long> ids = new ArrayList<Long>();
        List<AgendaProcedimentoFixo> list = instance.findRange(new int[]{0, Testes.LINHAS_AGENDA_CSV});
        assertEquals("Quantidade retornada diferente da esperada.",
                Testes.LINHAS_AGENDA_CSV, list.size());
        for (AgendaProcedimentoFixo apf : list) {
            if (ids.contains(apf.getCodigo())) {
                fail("Id repetido em busca distinta.");
            }
            ids.add(apf.getCodigo());
        }
        assertTrue("Não existe ID repetidos.", true);
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
