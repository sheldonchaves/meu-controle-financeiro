/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import java.util.List;
import org.junit.Test;
import org.junit.*;
import static org.junit.Assert.*;
import static br.com.gbvbahia.financeiro.beans.Testes.*;
import java.util.Map;

/**
 * * <strong>Lembre-se de iniciar o JavaDB ante de executar os testes.
 * </strong><br>
 * @author Guilherme
 */
public class UsuarioBeanSearchTest {
    private static UsuarioFacade instance = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
        instance = (UsuarioFacade) c.getContext().lookup("java:global/classes/UsuarioBean");
    }
    /**
     * Test of find method, of class UsuarioBean.
     */
    @Test
    public void testFind() throws Exception {
        String id = "gbvbahia";
        Usuario result = instance.find(id);
        assertNotNull(result);
    }

    /**
     * Test of findAll method, of class UsuarioBean.
     */
    @Test
    public void testFindAll() throws Exception {
        List result = instance.findAll();
        assertTrue("Procurando todos os usuários, findAll!", !result.isEmpty());
    }

    /**
     * Test of count method, of class UsuarioBean.
     */
    @Test
    public void testCount() throws Exception {
        int result = instance.count();
        assertTrue("Contar Usuarios", result != 0);
    }

    /**
     * Test of listPesq method, of class UsuarioBean.
     */
    @Test
    public void testListPesq() throws Exception {
        String namedQuery = "Usuario.findAll";
        List<Usuario> result = instance.listPesq(namedQuery);
        assertTrue("Busca portodos usuários retornou vazia",
                result != null && !result.isEmpty());
    }

    /**
     * Test of listPesqParam method, of class UsuarioBean.
     */
    @Test
    public void testListPesqParam_String_Map() throws Exception {
        String namedQuery = "Usuario.findByBlocked";
        Map parans = UsuarioBean.getMapParans();
        parans.put("blocked", false);
        List<Usuario> result = instance.listPesqParam(namedQuery, parans);
        for (Usuario u : result) {
            if (u.getBlocked()) {
                fail("Busca por usuários desbloqueados retornou um bloqueado.");
            }
        }
        assertTrue("Busca por usuários desbloqueados retornou vazia",
                result != null && !result.isEmpty());
    }

    /**
     * Test of listPesqParam method, of class UsuarioBean.
     */
    @Test
    public void testListPesqParam_4args() throws Exception {
        String namedQuery = "Usuario.findByBlocked";
        Map parans = UsuarioBean.getMapParans();
        parans.put("blocked", false);
        List<Usuario> result = instance.listPesqParam(namedQuery, parans, 5, 0);
        for (Usuario u : result) {
            if (u.getBlocked()) {
                fail("Busca paginada por usuários desbloqueados "
                        + "retornou um bloqueado.");
            }
        }
        assertTrue("Busca por usuários desbloqueados retornou vazia",
                result != null && !result.isEmpty());
    }

    /**
     * Test of pesqParam method, of class UsuarioBean.
     */
    @Test
    public void testPesqParam_String_Map() throws Exception {
        String namedQuery = "Usuario.findByUserId";
        Map<String, Object> params = UsuarioBean.getMapParans();
        params.put("userId", "user01");
        Usuario result = instance.pesqParam(namedQuery, params);
        assertNotNull("Pesquisa por user01 retornou null.", result);
    }

    /**
     * Test of pesqParam method, of class UsuarioBean.
     */
    @Test
    public void testPesqParam_String() throws Exception {
        assertTrue("Sem query com unico resultado com parametros"
                + " para teste aqui.", true);
    }

    /**
     * Test of pesqCount method, of class UsuarioBean.
     */
    @Test
    public void testPesqCount() throws Exception {
          assertTrue("Sem query com unico resultado com parametros"
                + " para teste aqui.", true);
    }
}
