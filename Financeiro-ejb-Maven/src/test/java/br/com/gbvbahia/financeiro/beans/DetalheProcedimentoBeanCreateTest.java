/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import static br.com.gbvbahia.financeiro.beans.Testes.c;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.DetalheDespesa;
import br.com.gbvbahia.financeiro.modelos.DetalheReceita;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.superclass.DetalheProcedimento;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class DetalheProcedimentoBeanCreateTest {

    private static ContaBancariaFacade contaBancariaFacade = null;
    private static UsuarioFacade usuarioFacade = null;
    private static DetalheProcedimentoFacade instance = null;

    public DetalheProcedimentoBeanCreateTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        contaBancariaFacade = (ContaBancariaFacade) c.getContext().lookup("java:global/classes/ContaBancariaBean");
        usuarioFacade = (UsuarioFacade) c.getContext().lookup("java:global/classes/UsuarioBean");
        instance = (DetalheProcedimentoFacade) c.getContext().lookup("java:global/classes/DetalheProcedimentoBean");
    }

    /**
     * Test Criar Despesa ativa.
     */
    @Test
    public void testCreateDespesa() throws Exception {
        DetalheProcedimento entity = new DetalheDespesa();
        entity.setDetalhe("Transporte");
        final Usuario user = usuarioFacade.find("gbvbahia");
        entity.setUsuario(user);
        instance.create(entity);
        assertTrue("Despesa criada não encontrada",
                !instance.findAllDetalheDespesa(user,
                Boolean.TRUE).isEmpty());
    }

    /**
     * Test Criar Receita ativa.
     */
    @Test
    public void testCreateReceita() throws Exception {
        DetalheProcedimento entity = new DetalheReceita();
        entity.setDetalhe("Salário Mensal");
        final Usuario user = usuarioFacade.find("esposa");
        entity.setUsuario(user);
        instance.create(entity);
        assertTrue("Despesa criada não encontrada",
                !instance.findAllDetalheReceita(user,
                Boolean.TRUE).isEmpty());
    }

    /**
     * Criar receita bloqueada.
     *
     * @throws Exception
     */
    @Test
    public void testCreateReceitaBlock() throws Exception {
        DetalheProcedimento entity = new DetalheReceita();
        entity.setDetalhe("Salário Semanal");
        final Usuario user = usuarioFacade.find("esposa");
        entity.setUsuario(user);
        entity.setAtivo(false);
        instance.create(entity);
        assertTrue("Despesa criada não encontrada",
                !instance.findAllDetalheReceita(user,
                Boolean.FALSE).isEmpty());
    }

    /**
     * Test Criar Despesa bloqueada.
     */
    @Test
    public void testCreateDespesaBlock() throws Exception {
        DetalheProcedimento entity = new DetalheDespesa();
        entity.setDetalhe("Escola");
        final Usuario user = usuarioFacade.find("gbvbahia");
        entity.setUsuario(user);
        entity.setAtivo(false);
        instance.create(entity);
        assertTrue("Despesa criada não encontrada",
                !instance.findAllDetalheDespesa(user,
                Boolean.FALSE).isEmpty());
    }

    /**
     * Tenta cria um detalheProcedimento com detalhe com maior
     * quantidade de caracteres do que é permitido.
     * @throws Exception 
     */
    @Test(expected = NegocioException.class)
    public void testCreateLimiteMaxCaracteres() throws Exception {
        DetalheProcedimento entity = new DetalheDespesa();
        StringBuilder sb = new StringBuilder("");
        for (int i = 0;
                i <= DetalheProcedimento.QUANTIDADE_MAX_CARACTERES_DETALHE;
                i++) {
            if (i % 2 == 0) {
                sb.append("a");
            } else {
                sb.append("b");
            }
        }
        entity.setDetalhe(sb.toString());
        final Usuario user = usuarioFacade.find("gbvbahia");
        entity.setUsuario(user);
        entity.setAtivo(false);
        instance.create(entity);
    }
    
     /**
     * Tenta cria um detalheProcedimento com detalhe com menor
     * quantidade de caracteres do que é permitido.
     * @throws Exception 
     */
    @Test(expected = NegocioException.class)
    public void testCreateLimiteMinCaracteres() throws Exception {
        DetalheProcedimento entity = new DetalheDespesa();
        entity.setDetalhe("ABC");
        final Usuario user = usuarioFacade.find("gbvbahia");
        entity.setUsuario(user);
        entity.setAtivo(true);
        instance.create(entity);
    }
}
