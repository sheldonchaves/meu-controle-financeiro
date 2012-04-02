/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import com.sun.appserv.security.ProgrammaticLogin;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.embeddable.EJBContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * <strong>Lembre-se de iniciar o JavaDB antes de executar os testes.
 * </strong> Este deve ser o mesmo que a unidade de persistencia do
 * GlassFish jdbc/sample aponta.<br><strong>Lembre-se de configurar o
 * local do GlassFish</strong> no arquivo
 * ConfiguracaoTestes.properties propriedade ValorGlassFishLocalDisck
 * antes de executar os testes. Veja que para cada barra são inseridas
 * 4. Suite de testes para garantir ordem na execução, os métodos
 * detas classes poderão ser executados em qualquer ordem, mas as
 * classes serão executadas na ordem que aparecem.<br>
 *
 * @author Guilherme Braga
 * @since 31/03/2012
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    UsuarioBeanCreateTest.class,
    UsuarioBeanSearchTest.class,
    UsuarioBeanUpdateTest.class,
    UsuarioBeanRemoveTest.class,
    ContaBancariaBeanCreateTest.class,
    ContaBancariaBeanSearchTest.class,
    ContaBancariaBeanUpdateTest.class,
    DetalheProcedimentoBeanCreateTest.class,
    DetalheProcedimentoBeanSearchTest.class,
    AgendaProcedimentoFixoBeanCreateTest.class,
    AgendaProcedimentoFixoBeanSearchTest.class
})
public class Testes {

    /**
     * Container EJB (GlassFish) para testes.
     */
    public static EJBContainer c;

    /**
     * Carrega o container antes da suite de testes.
     *
     * @throws Exception Se algum problema ocorrer.
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        Map p = new HashMap();
        p.put(getConfig("keyGlassFishLocalDisck"),
                getConfig("ValorGlassFishLocalDisck"));
        Testes.c = javax.ejb.embeddable.EJBContainer.createEJBContainer(p);
        System.out.println("Opening the container");
    }

    /**
     * Descarrega o container após a suite de testes.
     *
     * @throws Exception Se algum problema ocorrer.
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
        Testes.c.close();
        System.out.println("Close the container");
    }

    /**
     * Recupera uma informação do arquivo de configuração do teste.
     *
     * @param key Chave que representa o valor solicitado.
     * @return O valor da chave solicitada.
     */
    public static String getConfig(final String key) {
        return MAP.get(key);
    }

    /**
     * MAP com propriedades de teste.
     */
    private static final Map<String, String> MAP =
            new HashMap<String, String>();
    static {
        MAP.put("keyGlassFishLocalDisck",
                "org.glassfish.ejb.embedded.glassfish.installation.root");
        MAP.put("ValorGlassFishLocalDisck",
                "D:\\Java\\GlassFish\\glassfish");
    }

}
