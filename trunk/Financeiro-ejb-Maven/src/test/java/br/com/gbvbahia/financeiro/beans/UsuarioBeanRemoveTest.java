/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import org.junit.*;
import static org.junit.Assert.*;
import static br.com.gbvbahia.financeiro.beans.Testes.*;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;

/**
 * * <strong>Lembre-se de iniciar o JavaDB ante de executar os testes.
 * </strong><br>
 * @author Guilherme
 */
public class UsuarioBeanRemoveTest {
    private static UsuarioFacade instance = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
        instance = (UsuarioFacade) c.getContext().lookup("java:global/classes/UsuarioBean");
    }
    /**
     * Test of remove method, of class UsuarioBean.
     */
    @Test
    public void testRemove() throws Exception {
        Usuario entity = instance.find("user01");
        instance.remove(entity);
        assertTrue("Removido encontrado", instance.find("user01") == null);
    }
}
