/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro;

import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.make.*;
import br.com.gbvbahia.financeiro.modelos.*;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.maker.MakeEntity;
import java.sql.Connection;
import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Usuário do Windows
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
    AgendaProcedimentoFixoBeanSearchTest.class,
    CartaoCreditoTest.class,
    ProcedimentoBeanTest.class,
    ProvisaoBeanTest.class
})
public class TestesMake {

    /**
     * Todas as entidades devem ser declaradas neste array, isso garante que
     * todas as tabelas serão criadas para deleção em tearDown.
     *
     * @param noEntityes Classes que não são entidades mas precisam ser
     * declaradas para utilização, como outros beans.
     * @return Classes de Entidades utilizadas na aplicação.
     */
    public static Class[] getUseBeans(final Class... noEntityes) {
        Class[] entityes = new Class[]{
            Usuario.class, Grupo.class, ContaBancaria.class,
            DetalheProcedimento.class, DetalheProcedimento.class,
            AgendaProcedimentoFixo.class,
            CartaoCredito.class, DespesaProcedimento.class,
            Procedimento.class, DespesaParceladaProcedimento.class
        };
        return (Class[]) ArrayUtils.addAll(entityes, noEntityes);
    }

    /**
     * Todas as entidades devem ser declaradas neste array, isso garante que
     * todas as tabelas serão criadas para deleção em tearDown. ser declaradas
     * para utilização, como outros beans.
     *
     * @return Classes de Entidades utilizadas na aplicação.
     */
    public static Class[] getUseBeans() {
        return getUseBeans(null);
    }

    /**
     * Cria a entidade com Make e salva no banco.
     *
     * @param <T> Tipo da entidade
     * @param manager EntityManager JPA
     * @param clazz Classe da entidade.
     * @param testName Nome do teste.
     * @param relation Define se é para cria relacionamentos.
     * @return A entidade solicitada.
     */
    public static <T> T makeEntityBD(EntityManager manager, Class<T> clazz,
            String testName, boolean relation) {
        T t = MakeEntity.makeEntity(testName, clazz, relation);
        if (manager != null) {
            manager.getTransaction().begin();
            manager.persist(t);
            manager.getTransaction().commit();
        }
        return t;
    }

    /**
     *
     * Cria as entidades com Make e salva no banco.
     *
     * @param <T> Tipo da entidade
     * @param manager EntityManager JPA
     * @param clazz Classe da entidade.
     * @param testName Nome do teste.
     * @param relation Define se é para cria relacionamentos.
     * @return A lista de entidades solicitadas.
     */
    public static <T> List<T> makeEntitiesBD(EntityManager manager, Class<T> clazz,
            String testName, int qtd, boolean relation) {
        List<T> ts = MakeEntity.makeEntities(testName, clazz, qtd, relation);
        if (manager != null) {
            manager.getTransaction().begin();
            for (T t : ts) {
                manager.persist(t);
            }
            manager.getTransaction().commit();
        }
        return ts;
    }

    /**
     * As classes de teste devem sobrescrever tearDown() de
     * BaseSessionBeanFixture. Bug do EJB3Unit que não realiza a limpeza dos
     * dados entre um teste e outro.<br> Esse método unifica o local de
     * deleção das tabelas.
     *
     * @param con
     * @throws Exception
     */
    public static void tearDown(Connection con) throws Exception {
        con.setAutoCommit(true);
        con.prepareStatement("DELETE from fin_procedimento_despesa_unica").executeUpdate();
        con.prepareStatement("DELETE from fin_procedimento_despesa_parcelada").executeUpdate();
        con.prepareStatement("DELETE from fin_procedimento").executeUpdate();
        con.prepareStatement("DELETE from fin_agenda_procedimento_fixo").executeUpdate();
        con.prepareStatement("DELETE from fin_detalhe").executeUpdate();
        con.prepareStatement("DELETE from fin_conta_bancaria").executeUpdate();
        con.prepareStatement("DELETE from fin_cartao_credito").executeUpdate();
        con.prepareStatement("DELETE from fin_usuario_grupo").executeUpdate();
        con.prepareStatement("DELETE from fin_usuario").executeUpdate();
        con.prepareStatement("DELETE from fin_grupo").executeUpdate();
        con.close();
    }

    /**
     * Retorna o UsuarioFacade através de lookup.
     *
     * @return UsuarioFacade.
     * @throws Exception
     */
    public static UsuarioFacade getUsuarioFacade() throws Exception {
        InitialContext context = new InitialContext();
        return (UsuarioFacade) context.lookup("EJB3Unit/usuarioFacade/remote");
    }

    /**
     * Retorna o DetalheProcedimentoFacade através de lookup.
     *
     * @return DetalheProcedimentoFacade.
     * @throws Exception
     */
    public static DetalheProcedimentoFacade getDetalheProcedimentoFacade()
            throws Exception {
        InitialContext context = new InitialContext();
        return (DetalheProcedimentoFacade) context.lookup("EJB3Unit/detalheProcedimentoFacade/remote");
    }

    /**
     * Retorna o AgendaProcedimentoFixoFacade através de lookup.
     *
     * @return AgendaProcedimentoFixoFacade.
     * @throws Exception
     */
    public static AgendaProcedimentoFixoFacade getAgendaFacade()
            throws Exception {
        InitialContext context = new InitialContext();
        return (AgendaProcedimentoFixoFacade) context.lookup("EJB3Unit/agendaProcedimentoFixoFacade/remote");
    }

    /**
     * Retorna o CartaoCreditoFacade através de lookup.
     *
     * @return CartaoCreditoFacade.
     * @throws Exception
     */
    public static CartaoCreditoFacade getCartaoFacade() throws Exception {
        InitialContext context = new InitialContext();
        return (CartaoCreditoFacade) context.lookup("EJB3Unit/cartaoCreditoFacade/remote");
    }

    /**
     * Retorna o ProcedimentoFacade através de lookup.
     *
     * @return ProcedimentoFacade.
     * @throws Exception
     */
    public static ProcedimentoFacade getProcedimentoFacade() throws Exception {
        InitialContext context = new InitialContext();
        return (ProcedimentoFacade) context.lookup("EJB3Unit/procedimentoFacade/remote");
    }
}
