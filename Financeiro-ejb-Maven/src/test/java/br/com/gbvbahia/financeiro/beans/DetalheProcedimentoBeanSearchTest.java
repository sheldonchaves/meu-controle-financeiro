/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static br.com.gbvbahia.financeiro.beans.Testes.c;

/**
 *
 * @author Guilherme
 */
public class DetalheProcedimentoBeanSearchTest {

    private static ContaBancariaFacade contaBancariaFacade = null;
    private static UsuarioFacade usuarioFacade = null;
    private static DetalheProcedimentoFacade instance = null;

    public DetalheProcedimentoBeanSearchTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        contaBancariaFacade = (ContaBancariaFacade) c.getContext().lookup("java:global/classes/ContaBancariaBean");
        usuarioFacade = (UsuarioFacade) c.getContext().lookup("java:global/classes/UsuarioBean");
        instance = (DetalheProcedimentoFacade) c.getContext().lookup("java:global/classes/DetalheProcedimentoBean");
    }

    /**
     * Conta quantos DetalheProcedimentos existem, são 4, duas
     * receitas e duas despesas criados no teste anterior.
     */
    @Test
    public void testCount() throws Exception {
        int expResult = 4;
        int result = instance.count();
        assertEquals(expResult, result);
    }

    /**
     * Somente uma receita ativa foi criada, pela esposa. Procuro pelo
     * marido e deve ser encontrada, apenas uma.
     * DetalheProcedimentoBean.
     */
    @Test
    public void testFindAllDetalheReceitaAtivoMarido() throws Exception {
        Usuario user = usuarioFacade.find("gbvbahia");
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
        Usuario user = usuarioFacade.find("esposa");
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
        Usuario user = usuarioFacade.find("gbvbahia");
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
        Usuario user = usuarioFacade.find("gbvbahia");
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
        Usuario user = usuarioFacade.find("gbvbahia");
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
        Usuario user = usuarioFacade.find("gbvbahia");
        Boolean ativo = null;
        int expResult = 2;
        List result = instance.findAllDetalheReceita(user, ativo);
        assertEquals(expResult, result.size());
    }
}
