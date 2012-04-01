/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import static br.com.gbvbahia.financeiro.beans.Testes.c;
import static br.com.gbvbahia.financeiro.beans.Testes.getConfig;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <strong>Lembre-se de iniciar o JavaDB ante de executar os testes.
 * </strong><br> Teste exemplo:
 * http://www.hascode.com/2011/01/enterprise-java-bean-ejb-3-1-
 * testing-using-maven-and-embedded-glassfish/ Exemplo Netbeans
 * http://netbeans.org/kb/docs/javaee/javaee-entapp-junit.html
 *
 * @author Guilherme
 */
public class UsuarioBeanCreateTest {

    private static boolean ejbInit = false;
    private static UsuarioFacade instance = null;
    /**
     * Default.
     */
    public UsuarioBeanCreateTest() {
    }

    /**
     * Caso seja necessário executar o teste somente nesta classe,
     * aqui garante que o contaienr seja carergado.
     *
     * @throws Exception Se algum problema ocorrer.
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        if (Testes.c == null) {
            Map p = new HashMap();
            p.put(getConfig("keyGlassFishLocalDisck"),
                    getConfig("ValorGlassFishLocalDisck"));
            c = javax.ejb.embeddable.EJBContainer.createEJBContainer(p);
            ejbInit = true;
            System.out.println("Opening the container");
        }
        instance = (UsuarioFacade)
                c.getContext().lookup("java:global/classes/UsuarioBean");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        if (ejbInit) {
            Testes.c.close();
            System.out.println("Close the container");
        }

    }

    /**
     * Test of getMapParans method, of class UsuarioBean.
     *
     * @throws Exception Qualquer exceção é esperada.
     */
    @Test
    public void testGetMapParans() throws Exception {
        Map result = UsuarioBean.getMapParans();
        result.put("long", new Long("1"));
        assertEquals(result.get("long"), new Long("1"));
    }

    /**
     * Test of create method, of class UsuarioBean.
     *
     * @throws Exception Qualquer exceção é esperada.
     */
    @Test
    public void testCreate() throws Exception {
        Usuario entity = new Usuario();
        entity.setEmail("gbvbahia01@hotmail.com");
        entity.setFirstName("Guilherme");
        entity.setUserId("gbvbahia");
        entity.setPass("123456");
        entity.setLastName("Braga");
        assertNotNull("EJB Não pode ser nulo!", instance);
        instance.create(entity);
        Usuario us = instance.find("gbvbahia");
        assertEquals(entity.getUserId(), us.getUserId());
    }

    /**
     * Test of findRange method, of class UsuarioBean.
     *
     * @throws Exception Qualquer exceção é esperada.
     */
    @Test
    public void testFindRange() throws Exception {
        int users = criarUsuarios() + 1;
        int[] range = {0, users - 3};
        List result = instance.findRange(range);
        assertTrue("Tamanho da lista: " + result.size()
                + " diferente do esperado: " + (users - 3)
                + " RANGE!", result.size() <= users - 3);
    }

    /**
     * Criação alguns usuários com informações validas.
     *
     * @return Quantidade de usuários criados.
     * @throws Exception Qualquer exceção é esperada.
     */
    @Ignore
    private int criarUsuarios() throws Exception {
        int toReturn = 9;
        for (int i = 1; i < toReturn; i++) {
            Usuario entity = new Usuario();
            entity.setEmail("user0" + i + "@hotmail.com");
            entity.setFirstName("user0" + i);
            entity.setUserId("user0" + i);
            entity.setPass("123456");
            entity.setLastName("user0" + i);
            instance.create(entity);
        }
        return toReturn;
    }

    /**
     * Cria usuário com login com mais de
     * LIMIT_MAX_CARACTERES_LOGIN_ID caracteres.
     *
     * * @throws Exception Qualquer exceção é esperada.
     */
    @Test(expected = NegocioException.class)
    public void criaUsuarioLoginInvalido() throws Exception {
        Usuario entity = new Usuario();
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i <= Usuario.LIMIT_MAX_CARACTERES_LOGIN_ID; i++) {
            if (i % 2 == 0) {
                sb.append("a");
            } else {
                sb.append("b");
            }
        }
        entity.setEmail("userloginerro@hotmail.com");
        entity.setFirstName("userLoginErro");
        entity.setUserId(sb.toString());
        entity.setPass("123456");
        entity.setLastName("userLoginErro");
        instance.create(entity);
    }
    
     /**
     * Cria usuário com login com mais de
     * LIMIT_MAX_CARACTERES_LOGIN_ID caracteres.
     *
     * * @throws Exception Qualquer exceção é esperada.
     */
    @Test(expected = NegocioException.class)
    public void criaUsuarioEmailInvalido() throws Exception {
        Usuario entity = new Usuario();
        entity.setEmail("user Login Errado@hotmail.com");
        entity.setFirstName("userLoginErro");
        entity.setUserId("emailErro");
        entity.setPass("123456");
        entity.setLastName("userLoginErro");
        instance.create(entity);
    }
}
