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
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import java.util.HashMap;

/**
 *
 * @author Guilherme
 */
public class ContaBancariaBeanCreateTest {

    private static ContaBancariaFacade instance = null;
    private static UsuarioFacade usuarioFacade = null;
    public static final int CONTAS_CRIADAS = 2;
    public ContaBancariaBeanCreateTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        instance = (ContaBancariaFacade) c.getContext().lookup("java:global/classes/ContaBancariaBean");
        usuarioFacade = (UsuarioFacade) c.getContext().lookup("java:global/classes/UsuarioBean");
    }

    /**
     * Test of create method, of class ContaBancariaBean.
     */
    @Test
    public void testCreate() throws Exception {
        ContaBancaria entity = new ContaBancaria();
        entity.setNomeConta("Banco do Brasil Gui CC");
        entity.setTipoConta(TipoConta.CORRENTE);
        entity.setSaldo(300.00d);
        entity.setUsuario(usuarioFacade.find("gbvbahia"));
        instance.create(entity);
        assertTrue("Contra criada não encontrada",
                !instance.findAll().isEmpty());
    }

    /**
     * Test of create method, of class ContaBancariaBean.
     */
    @Test
    public void testCreateBlock() throws Exception {
        ContaBancaria entity = new ContaBancaria();
        entity.setNomeConta("Banco do Brasil Gui POU");
        entity.setTipoConta(TipoConta.POUPANCA);
        entity.setSaldo(00.00d);
        entity.setUsuario(usuarioFacade.find("gbvbahia"));
        entity.setStatus(false);
        instance.create(entity);
        boolean erro = true;
        for (ContaBancaria cb : instance.findAll()) {
            if(!cb.isStatus() 
                    && cb.getNomeConta().equals("Banco do Brasil Gui POU")) {
                erro = false;
            }
        }
        if (erro) {
            fail("Não foi encontrada a conta bloqueada!");
        }
    }

    /**
     * Testa criar uma conta com nome muito curto
     *
     * @throws Exception
     */
    @Test(expected = NegocioException.class)
    public void testCreateNomeSizeSmall() throws Exception {
        ContaBancaria entity = new ContaBancaria();
        entity.setNomeConta("BB");
        entity.setTipoConta(TipoConta.CORRENTE);
        entity.setSaldo(300.00d);
        entity.setUsuario(usuarioFacade.find("gbvbahia"));
        instance.create(entity);
    }

    /**
     * Testa criar uma conta com nome muito long
     *
     * @throws Exception
     */
    @Test(expected = NegocioException.class)
    public void testCreateNomeSizeLarge() throws Exception {
        ContaBancaria entity = new ContaBancaria();
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i <= ContaBancaria.CARACTERES_MAX_NOME_CONTA; i++) {
            if (i % 2 == 0) {
                sb.append("a");
            } else {
                sb.append("b");
            }
        }
        entity.setNomeConta(sb.toString());
        entity.setTipoConta(TipoConta.CORRENTE);
        entity.setSaldo(300.00d);
        entity.setUsuario(usuarioFacade.find("gbvbahia"));
        instance.create(entity);
    }

    /**
     * Tenta criar uma conta sem usuário.
     *
     * @throws Exception
     */
    @Test(expected = NegocioException.class)
    public void criarContaSemUsuario() throws Exception {
        ContaBancaria entity = new ContaBancaria();
        entity.setNomeConta("Banco do Brasil Gui CC");
        entity.setTipoConta(TipoConta.CORRENTE);
        entity.setSaldo(300.00d);
        instance.create(entity);
    }

    /**
     * Tenta criar uma conta sem saldo.
     *
     * @throws Exception
     */
    @Test(expected = NegocioException.class)
    public void criarContaSemSaldo() throws Exception {
        ContaBancaria entity = new ContaBancaria();
        entity.setNomeConta("Banco do Brasil Gui CC");
        entity.setTipoConta(TipoConta.CORRENTE);
        entity.setUsuario(usuarioFacade.find("gbvbahia"));
        entity.setSaldo(null);
        instance.create(entity);
    }
}
