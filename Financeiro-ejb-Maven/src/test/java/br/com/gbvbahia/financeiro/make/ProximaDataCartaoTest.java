/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make;

import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Guilherme
 */
public class ProximaDataCartaoTest {

    public ProximaDataCartaoTest() {
    }

    @Test
    public void testProximoVencimento() throws Exception {
        CartaoCredito cartao = new CartaoCredito();
        cartao.setDiaMesmoMes(7);
        cartao.setDiaVencimento(6);
        Calendar c = Calendar.getInstance();
        c.set(2010, 0, 9);
        Date d = cartao.getProximoVencimento(c.getTime());
        Calendar compare = Calendar.getInstance();
        compare.setTime(d);
        assertEquals(compare.get(Calendar.DAY_OF_MONTH), 6);
        assertEquals(1, compare.get(Calendar.MONTH));
        assertEquals(2010, compare.get(Calendar.YEAR));

        c.set(2009, 11, 24);
        d = cartao.getProximoVencimento(c.getTime());
        compare.setTime(d);
        assertEquals(compare.get(Calendar.DAY_OF_MONTH), 6);
        assertEquals(0, compare.get(Calendar.MONTH));
        assertEquals(2010, compare.get(Calendar.YEAR));

        c.set(2009, 11, 30);
        d = cartao.getProximoVencimento(c.getTime());
        compare.setTime(d);
        assertEquals(compare.get(Calendar.DAY_OF_MONTH), 6);
        assertEquals(1, compare.get(Calendar.MONTH));
        assertEquals(2010, compare.get(Calendar.YEAR));

        c.set(2009, 11, 01);
        d = cartao.getProximoVencimento(c.getTime());
        compare.setTime(d);
        assertEquals(compare.get(Calendar.DAY_OF_MONTH), 6);
        assertEquals(0, compare.get(Calendar.MONTH));
        assertEquals(2010, compare.get(Calendar.YEAR));

        c.set(2012, 10, 28);
        cartao = new CartaoCredito();
        cartao.setDiaMesmoMes(7);
        cartao.setDiaVencimento(6);
        d = cartao.getProximoVencimento(c.getTime());
        compare.setTime(d);
        assertEquals(compare.get(Calendar.DAY_OF_MONTH), 6);
        assertEquals(11, compare.get(Calendar.MONTH));
        assertEquals(2012, compare.get(Calendar.YEAR));

        c.set(2012, 11, 01);
        cartao = new CartaoCredito();
        cartao.setDiaMesmoMes(7);
        cartao.setDiaVencimento(11);
        d = cartao.getProximoVencimento(c.getTime());
        compare.setTime(d);
        assertEquals(compare.get(Calendar.DAY_OF_MONTH), 11);
        assertEquals(11, compare.get(Calendar.MONTH));
        assertEquals(2012, compare.get(Calendar.YEAR));

        c.set(2012, 10, 30);
        cartao = new CartaoCredito();
        cartao.setDiaMesmoMes(7);
        cartao.setDiaVencimento(6);
        d = cartao.getProximoVencimento(c.getTime());
        compare.setTime(d);
        assertEquals(compare.get(Calendar.DAY_OF_MONTH), 6);
        assertEquals(0, compare.get(Calendar.MONTH));
        assertEquals(2013, compare.get(Calendar.YEAR));
    }
}
