/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.utils;

import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author Guilherme
 */
public class I18nTest extends TestCase{
     @Test
    public void testInternacionalizacao() throws Exception {
        assertEquals("Internacionalização não está funcionando",
                "Valor", I18N.getMsg("chave"));
    }
}
