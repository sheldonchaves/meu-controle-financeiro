/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.constantes.TipoConta;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static br.com.gbvbahia.financeiro.beans.Testes.c;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;

/**
 *
 * @author Guilherme
 */
public class ContaBancariaBeanUpdateTest {

    private static ContaBancariaFacade instance = null;
    private static UsuarioFacade usuarioFacade = null;

    public ContaBancariaBeanUpdateTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        instance = (ContaBancariaFacade) c.getContext().lookup("java:global/classes/ContaBancariaBean");
        usuarioFacade = (UsuarioFacade) c.getContext().lookup("java:global/classes/UsuarioBean");
    }

    /**
     * Tenta atualizar a conta bancaria para usuario nulo.
     */
    @Test(expected = NegocioException.class)
    public void testUpdate_UserNull() throws Exception {
        Usuario proprietario = usuarioFacade.find("gbvbahia");
        Boolean status = false;
        List<ContaBancaria> result = instance.buscarTipoConta(TipoConta.POUPANCA, proprietario, status);
        ContaBancaria entity = result.get(0);
        entity.setUsuario(null);
        instance.update(entity);
    }
}
