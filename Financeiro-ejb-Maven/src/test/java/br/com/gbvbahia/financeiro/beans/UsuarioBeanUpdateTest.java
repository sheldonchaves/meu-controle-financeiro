/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import org.junit.Test;
import org.junit.*;
import static org.junit.Assert.*;
import static br.com.gbvbahia.financeiro.beans.Testes.*;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;

/**
 * * <strong>Lembre-se de iniciar o JavaDB ante de executar os
 * testes. </strong><br>
 *
 * @author Guilherme
 */
public class UsuarioBeanUpdateTest {

    private static UsuarioFacade instance = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
        instance = (UsuarioFacade) c.getContext().lookup("java:global/classes/UsuarioBean");
    }

    /**
     * Test of update method, of class UsuarioBean.
     */
    @Test
    public void testUpdate_GenericType() throws Exception {
        Usuario ent = new Usuario();
        ent.setEmail("gbvbahia02@hotmail.com");
        ent.setFirstName("Guilhe");
        ent.setUserId("gbvb");
        ent.setPass("102030");
        ent.setLastName("Bra");
        instance.create(ent);
        Usuario entity = instance.find("gbvb");
        entity.setFirstName("Gui");
        instance.update(entity);
        Usuario us = instance.find("gbvb");
        assertEquals(entity.getFirstName(), us.getFirstName());
    }

    /**
     * Test of update method, of class UsuarioBean.
     */
    @Test
    public void testUpdate_String_Map() throws Exception {
        assertTrue("Sem query Update para teste aqui.", true);
    }

    @Test
    public void atualizarConjuge() throws Exception {
        Usuario entity = new Usuario();
        entity.setEmail("esposa@hotmail.com");
        entity.setFirstName("Esposa");
        entity.setUserId("esposa");
        entity.setPass("123456");
        entity.setLastName("Mulher");
        instance.create(entity);
        Usuario marido = instance.find("gbvbahia");
        marido.setConjuge(entity);
        entity.setConjuge(marido);
        instance.update(entity);
        instance.update(marido);
        Usuario maridoCheck = instance.find("gbvbahia");
        assertEquals("Esposa não é a mesma!",
                entity, maridoCheck.getConjuge());
    }

    /**
     * Tenta criar um usuário com login igual.
     *
     * @throws Exception Qualquer exceção é esperada.
     */
    @Test(expected=NegocioException.class)
    public void testCreate() throws Exception {
        Usuario entity = new Usuario();
        entity.setEmail("outro@hotmail.com");
        entity.setFirstName("Outro");
        entity.setUserId("gbvbahia");
        entity.setPass("123456");
        entity.setLastName("Outro");
        instance.create(entity);
    }
}
