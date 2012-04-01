/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static br.com.gbvbahia.financeiro.beans.Testes.c;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import java.util.Date;

/**
 *
 * @author Guilherme
 */
public class AgendaProcedimentoFixoBeanCreateTest {

    private static ContaBancariaFacade contaBancariaFacade = null;
    private static UsuarioFacade usuarioFacade = null;
    private static DetalheProcedimentoFacade detalheProcedimentoFacade = null;
    private static AgendaProcedimentoFixoFacade agendaProcedimentoFixoFacade = null;

    public AgendaProcedimentoFixoBeanCreateTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        contaBancariaFacade = (ContaBancariaFacade) c.getContext().lookup("java:global/classes/ContaBancariaBean");
        usuarioFacade = (UsuarioFacade) c.getContext().lookup("java:global/classes/UsuarioBean");
        detalheProcedimentoFacade = (DetalheProcedimentoFacade) c.getContext().lookup("java:global/classes/DetalheProcedimentoBean");
        agendaProcedimentoFixoFacade = (AgendaProcedimentoFixoFacade) c.getContext().lookup("java:global/classes/AgendaProcedimentoFixoBean");
    }

    /**
     * Test of create method, of class AgendaProcedimentoFixoBean.
     */
    @Test
    public void testCreate() throws Exception {
        AgendaProcedimentoFixo entity = new AgendaProcedimentoFixo();
        entity.setValorFixo(10.00);
        entity.setDataPrimeiroVencimento(new Date());
        entity.setObservacao("Teste de Agenda.");
        entity.setUsuario(usuarioFacade.find("gbvbahia"));
        entity.setDetalhe(detalheProcedimentoFacade.find(1l));
        agendaProcedimentoFixoFacade.create(entity);
        assertNotNull("Entidade com ID null após ser gravada.",
                entity.getCodigo());
    }

    /**
     * Testa um valor menor que 0.01
     */
    @Test(expected = NegocioException.class)
    public void testCreateValorErro() throws Exception {
        AgendaProcedimentoFixo entity = new AgendaProcedimentoFixo();
        entity.setValorFixo(0.00);
        entity.setDataPrimeiroVencimento(new Date());
        entity.setObservacao("Teste de Agenda Erro Valor.");
        entity.setUsuario(usuarioFacade.find("gbvbahia"));
        entity.setDetalhe(detalheProcedimentoFacade.find(1l));
        agendaProcedimentoFixoFacade.create(entity);
    }

    /**
     * Testa um valor menor que 0.01
     */
    @Test(expected = NegocioException.class)
    public void testCreateDataNula() throws Exception {
        AgendaProcedimentoFixo entity = new AgendaProcedimentoFixo();
        entity.setValorFixo(10.00);
        entity.setObservacao("Teste de Agenda Data nula.");
        entity.setUsuario(usuarioFacade.find("gbvbahia"));
        entity.setDetalhe(detalheProcedimentoFacade.find(1l));
        agendaProcedimentoFixoFacade.create(entity);
    }

    /**
     * Testa um valor menor que 0.01
     */
    @Test(expected = NegocioException.class)
    public void testCreateObsCurta() throws Exception {
        AgendaProcedimentoFixo entity = new AgendaProcedimentoFixo();
        entity.setValorFixo(10.00);
        entity.setDataPrimeiroVencimento(new Date());
        entity.setObservacao("Test");
        entity.setUsuario(usuarioFacade.find("gbvbahia"));
        entity.setDetalhe(detalheProcedimentoFacade.find(1l));
        agendaProcedimentoFixoFacade.create(entity);
    }

    /**
     * Testa um valor menor que 0.01
     */
    @Test(expected = NegocioException.class)
    public void testCreateObsLong() throws Exception {
        AgendaProcedimentoFixo entity = new AgendaProcedimentoFixo();
        entity.setValorFixo(10.00);
        entity.setDataPrimeiroVencimento(new Date());
        entity.setObservacao("Testaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        entity.setUsuario(usuarioFacade.find("gbvbahia"));
        entity.setDetalhe(detalheProcedimentoFacade.find(1l));
        agendaProcedimentoFixoFacade.create(entity);
    }
    
     /**
      * Criar varias agendas para pesquisa de range e cont.
     * Test of create method, of class AgendaProcedimentoFixoBean.
     */
    @Test
    public void testCreateVarias() throws Exception {
        for (int i = 0; i < 100; i++) {
        AgendaProcedimentoFixo entity = new AgendaProcedimentoFixo();
        entity.setValorFixo(10.00 + i);
        entity.setDataPrimeiroVencimento(new Date());
        entity.setObservacao("Teste de Agenda " + i);
        entity.setUsuario(usuarioFacade.find("gbvbahia"));
        entity.setDetalhe(detalheProcedimentoFacade.find(1l));
        agendaProcedimentoFixoFacade.create(entity);
        }
    }
}
