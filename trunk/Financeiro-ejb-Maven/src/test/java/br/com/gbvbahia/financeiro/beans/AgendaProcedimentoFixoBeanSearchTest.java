/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import java.util.List;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static br.com.gbvbahia.financeiro.beans.Testes.c;
import java.util.ArrayList;

/**
 *
 * @author Guilherme
 */
public class AgendaProcedimentoFixoBeanSearchTest {

    private static ContaBancariaFacade contaBancariaFacade = null;
    private static UsuarioFacade usuarioFacade = null;
    private static DetalheProcedimentoFacade detalheProcedimentoFacade = null;
    private static AgendaProcedimentoFixoFacade agendaProcedimentoFixoFacade = null;

    public AgendaProcedimentoFixoBeanSearchTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        contaBancariaFacade = (ContaBancariaFacade) c.getContext().lookup("java:global/classes/ContaBancariaBean");
        usuarioFacade = (UsuarioFacade) c.getContext().lookup("java:global/classes/UsuarioBean");
        detalheProcedimentoFacade = (DetalheProcedimentoFacade) c.getContext().lookup("java:global/classes/DetalheProcedimentoBean");
        agendaProcedimentoFixoFacade = (AgendaProcedimentoFixoFacade) c.getContext().lookup("java:global/classes/AgendaProcedimentoFixoBean");
    }

    /**
     * Test of findRange method, of class AgendaProcedimentoFixoBean.
     */
    @Test
    public void testFindRange() throws Exception {
        List<Long> ids = new ArrayList<Long>();
        for (int i = 0; i < 101;) {
            List<AgendaProcedimentoFixo> list = agendaProcedimentoFixoFacade.findRange(new int[]{i, i + 5});
            for (AgendaProcedimentoFixo apf : list) {
                if (ids.contains(apf.getCodigo())) {
                    fail("Id repetido em busca distinta.");
                }
                ids.add(apf.getCodigo());
            }
            i = i + 5;
        }
    }

    /**
     * Test of count method, of class AgendaProcedimentoFixoBean.
     */
    @Test
    public void testCount() throws Exception {
        int expResult = 101;
        int result = agendaProcedimentoFixoFacade.count();
        assertEquals("A quantidade " + result + " é diferente de " 
                + expResult, expResult, result);
    }
}
