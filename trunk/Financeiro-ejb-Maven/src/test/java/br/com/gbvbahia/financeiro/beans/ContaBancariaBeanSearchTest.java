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
import javax.ejb.embeddable.EJBContainer;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static br.com.gbvbahia.financeiro.beans.Testes.c;
import static br.com.gbvbahia.financeiro.beans.ContaBancariaBeanCreateTest.*;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import java.util.HashMap;

/**
 *
 * @author Guilherme
 */
public class ContaBancariaBeanSearchTest {

    private static ContaBancariaFacade instance = null;
    private static UsuarioFacade usuarioFacade = null;

    public ContaBancariaBeanSearchTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        instance = (ContaBancariaFacade) c.getContext().lookup("java:global/classes/ContaBancariaBean");
        usuarioFacade = (UsuarioFacade) c.getContext().lookup("java:global/classes/UsuarioBean");
    }

    /**
     * Busca todas as contas, parâmetro proprietario passado esposa.
     * Status deve ser ignorado.
     * Uma conta bloqueada poupança e uma conta corrente foram criadas
     * é para retornar duas contas.
     */
    @Test
    public void testFindAll_Usuario_Boolean() throws Exception {
        Usuario proprietario = usuarioFacade.find("esposa");
        Boolean status = null;
        List<ContaBancaria> result = instance.findAll(proprietario, status);
        assertTrue("Conta conjuge não encontrada",
                result.size() == CONTAS_CRIADAS);
    }

    /**
     * Busca contas somente ativas, parâmetro proprietário esposa.
     */
    @Test
    public void testFindAll_Usuario_Boolean2() throws Exception {
        Usuario proprietario = usuarioFacade.find("esposa");
        Boolean status = true;
        List<ContaBancaria> result = instance.findAll(proprietario, status);
        for (ContaBancaria cb : result) {
            if (!cb.isStatus()) {
                fail("Busca por conta ativa retornou conta bloqueada.");
            }
        }
    }

    /**
     * Busca somente contas canceladas, proprietário o proprio.
     */
    @Test
    public void testFindAll_Usuario_Boolean3() throws Exception {
        Usuario proprietario = usuarioFacade.find("gbvbahia");
        Boolean status = false;
        List<ContaBancaria> result = instance.findAll(proprietario, status);
        for (ContaBancaria cb : result) {
            if (cb.isStatus()) {
                fail("Busca por conta bloqueada retornou conta ativa.");
            }
        }
    }

    /**
     * Busca conta corrente em qualquer status, deve retornar uma
     * conta a outra criada foi poupança
     */
    @Test
    public void testBuscarTipoConta() throws Exception {
        Usuario proprietario = usuarioFacade.find("gbvbahia");
        Boolean status = null;
        List result = instance.buscarTipoConta(TipoConta.CORRENTE, proprietario, status);
        assertTrue("Busca conta corrente", result.size() == 1);
    }

    /**
     * Busca conta corrente em status cancelada, não deve retornar a
     * conta corrente criada está ativa.
     */
    @Test
    public void testBuscarTipoConta2() throws Exception {
        Usuario proprietario = usuarioFacade.find("gbvbahia");
        Boolean status = false;
        List result = instance.buscarTipoConta(TipoConta.CORRENTE, proprietario, status);
        assertTrue("Busca conta corrente", result.isEmpty());
    }

    /**
     * Busca conta pupança em status cancelada, deve retornar uma a
     * conta poupança criada está cancelada.
     */
    @Test
    public void testBuscarTipoConta3() throws Exception {
        Usuario proprietario = usuarioFacade.find("gbvbahia");
        Boolean status = false;
        List result = instance.buscarTipoConta(TipoConta.POUPANCA, proprietario, status);
        assertTrue("Busca conta corrente", result.size() == 1);
    }
}
