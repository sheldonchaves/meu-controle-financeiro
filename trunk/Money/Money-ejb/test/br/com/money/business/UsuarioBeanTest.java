/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.UsuarioBeanLocal;
import br.com.money.modelos.Role;
import br.com.money.modelos.Usuario;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Guilherme
 */
public class UsuarioBeanTest {

    public UsuarioBeanTest() {
    }
    javax.ejb.embeddable.EJBContainer container = null;
    UsuarioBeanLocal instance = null;

    @Before
    public void setUp() {
        container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        try {
            instance = (UsuarioBeanLocal) container.getContext().lookup("java:global/classes/UsuarioBean");
        } catch (NamingException ex) {
            Logger.getLogger(UsuarioBeanTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
        container.close();
    }

    @Test
    public void testCriarRole() throws Exception {
        System.out.println("criarRole");
        Role role = new Role();
        role.setGroupDesc("Administrador");
        role.setGroupName("ADMIN");
        instance.criarRole(role);
        assertTrue("Não deu exceção", true);
    }

    /**
     * Test of criarUsuario method, of class UsuarioBean.
     */
    @Test
    public void testCriarUsuario() throws Exception {
        System.out.println("criarUsuario");
        Usuario usuario = new Usuario();
        usuario.setEmail("gbvbahia01@hotmail.com");
        usuario.setFirstName("Guilherme");
        usuario.setLastName("Braga");
        usuario.setLogin("gbvbahia");
        usuario.setPassword("3F296EBDCC478239A9BC572C21D1D2BB");
        usuario.getRoles().add(instance.buscarRoleByName("ADMIN"));
        instance.criarUsuario(usuario);
        assertTrue("Não deu exceção", true);
    }

    /**
     * Test of buscarUsuarioByLogin method, of class UsuarioBean.
     */
    @Test
    public void testBuscarUsuarioByLogin() throws Exception {
        System.out.println("buscarUsuarioByLogin");
        String login = "gbvbahia";
        Usuario result = instance.buscarUsuarioByLogin(login);
        assertNotNull(result);
    }

    /**
     * Test of buscarUsuarioByEmail method, of class UsuarioBean.
     */
    @Test
    public void testBuscarUsuarioByEmail() throws Exception {
        System.out.println("buscarUsuarioByEmail");
        String email = "gbvbahia01@hotmail.com";
        Usuario result = instance.buscarUsuarioByEmail(email);
        assertNotNull(result);;
    }

    /**
     * Test of criptografarSenha method, of class UsuarioBean.
     */
    @Test
    public void testCriptografarSenha() throws Exception {
        System.out.println("criptografarSenha");
        String senha = "102030";
        String role = "ADMIN";
        String expResult = "3F296EBDCC478239A9BC572C21D1D2BB";
        String result = instance.criptografarSenha(senha, role);
        assertEquals(expResult, result);
    }
}
