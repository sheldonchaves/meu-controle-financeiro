/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro;

import br.com.gbvbahia.financeiro.beans.AgendaProcedimentoFixoBeanCreateTest;
import br.com.gbvbahia.financeiro.beans.AgendaProcedimentoFixoBeanSearchTest;
import br.com.gbvbahia.financeiro.beans.CartaoCreditoTest;
import br.com.gbvbahia.financeiro.beans.ContaBancariaBeanCreateTest;
import br.com.gbvbahia.financeiro.beans.ContaBancariaBeanSearchTest;
import br.com.gbvbahia.financeiro.beans.ContaBancariaBeanUpdateTest;
import br.com.gbvbahia.financeiro.beans.DespesaProcedimentoBeanTest;
import br.com.gbvbahia.financeiro.beans.DetalheProcedimentoBeanCreateTest;
import br.com.gbvbahia.financeiro.beans.DetalheProcedimentoBeanSearchTest;
import br.com.gbvbahia.financeiro.beans.ProcedimentoBeanTest;
import br.com.gbvbahia.financeiro.beans.ProvisaoBeanTest;
import br.com.gbvbahia.financeiro.beans.UsuarioBeanCreateTest;
import br.com.gbvbahia.financeiro.beans.UsuarioBeanRemoveTest;
import br.com.gbvbahia.financeiro.beans.UsuarioBeanSearchTest;
import br.com.gbvbahia.financeiro.beans.UsuarioBeanUpdateTest;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.TipoConta;
import br.com.gbvbahia.financeiro.modelos.*;
import br.com.gbvbahia.financeiro.modelos.superclass.DetalheProcedimento;
import br.com.gbvbahia.financeiro.modelos.superclass.Procedimento;
import br.com.gbvbahia.financeiro.utils.I18nTest;
import com.bm.testsuite.dataloader.CSVInitialDataSet;
import com.bm.testsuite.dataloader.DateFormats;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Set;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.apache.commons.lang.ArrayUtils;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * <strong>Lembre-se de iniciar o JavaDB antes de executar os testes.
 * </strong> Este deve ser o mesmo que a unidade de persistencia do
 * GlassFish jdbc/sample aponta.<br><strong>Lembre-se de configurar o local
 * do GlassFish</strong> no arquivo ConfiguracaoTestes.properties
 * propriedade ValorGlassFishLocalDisck antes de executar os testes. Veja
 * que para cada barra são inseridas 4. Suite de testes para garantir ordem
 * na execução, os métodos detas classes poderão ser executados em qualquer
 * ordem, mas as classes serão executadas na ordem que aparecem.<br>
 *
 * @author Guilherme Braga
 * @since 31/03/2012
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    I18nTest.class,
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
    DespesaProcedimentoBeanTest.class,
    ProcedimentoBeanTest.class,
    ProvisaoBeanTest.class
})
public class Testes {

    static {
        getUseBeans(new Class[]{Integer.class});
    }

    /**
     * Todas as entidades devem ser declaradas neste array, isso garante
     * que todas as tabelas serão criadas para deleção em tearDown.
     *
     * @param noEntityes Classes que não são entidades mas precisam ser
     * declaradas para utilização, como outros beans.
     * @return Classes de Entidades utilizadas na aplicação.
     */
    public static Class[] getUseBeans(final Class... noEntityes) {
        Class[] entityes = new Class[]{
            Usuario.class, Grupo.class, ContaBancaria.class,
            DetalheProcedimento.class, DetalheDespesa.class,
            DetalheReceita.class, AgendaProcedimentoFixo.class,
            CartaoCredito.class, DespesaProcedimento.class,
            Procedimento.class, DespesaParceladaProcedimento.class,
            ReceitaProcedimento.class
        };
        return (Class[]) ArrayUtils.addAll(entityes, noEntityes);
    }

    /**
     * Todas as entidades devem ser declaradas neste array, isso garante
     * que todas as tabelas serão criadas para deleção em tearDown. ser
     * declaradas para utilização, como outros beans.
     *
     * @return Classes de Entidades utilizadas na aplicação.
     */
    public static Class[] getUseBeans() {
        return getUseBeans(null);
    }

    /**
     * Cria dados com base no CSV X a classe informada.
     *
     * @return Representacao do arquivo usuarios.csv em um
     * CSVInitialDataSet.
     */
    public static CSVInitialDataSet<Usuario> getUsuariosCSV() {
        return new CSVInitialDataSet<Usuario>(Usuario.class,
                "usuarios.csv", "userId", "pass", "email",
                "firstName", "lastName", "blocked");
    }

    /**
     * Cria dados com base no CSV X a classe informada.
     *
     * @return Representacao do arquivo usuariosconjuge.csv em um
     * CSVInitialDataSet.
     */
    public static CSVInitialDataSet<Usuario> getUsuariosConjugeCSV() {
        return new CSVInitialDataSet<Usuario>(Usuario.class,
                "usuariosconjuje.csv", "userId", "pass", "email",
                "firstName", "lastName", "blocked", "conjuge");
    }

    /**
     * Cria contas que antes estavam no getContasBancoCSV
     *
     * @param manager EntityManager
     * @throws Exception
     */
    public static void createContasBancarias(EntityManager manager)
            throws Exception {
        try {
            ContaBancaria conta1 = new ContaBancaria();
            conta1.setUsuario(getUsuarioFacade().find("user01"));
            conta1.setNomeConta("Banco do Brasil");
            conta1.setTipoConta(TipoConta.CORRENTE);
            conta1.setSaldo(new BigDecimal("1200.00"));
            conta1.setStatus(true);
            ContaBancaria conta2 = new ContaBancaria();
            conta2.setUsuario(getUsuarioFacade().find("user01"));
            conta2.setNomeConta("Bradesco");
            conta2.setTipoConta(TipoConta.POUPANCA);
            conta2.setSaldo(new BigDecimal("200.00"));
            conta2.setStatus(false);
            ContaBancaria conta3 = new ContaBancaria();
            conta3.setUsuario(getUsuarioFacade().find("user03"));
            conta3.setNomeConta("Itaú");
            conta3.setTipoConta(TipoConta.INVESTIMENTO);
            conta3.setSaldo(new BigDecimal("1500.00"));
            conta3.setStatus(true);
            manager.getTransaction().begin();
            manager.persist(conta3);
            manager.persist(conta2);
            manager.persist(conta1);
            manager.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
                String message = violation.getMessage();
                System.out.println("ATENÇÃO: ***   CRIAÇÃO DE CONTAS ABORTADA!!!");
                System.out.println(message);
            }
        } finally {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
        }
    }
    /**
     * Quantidade linhas do arquivo detalheprocedimentos.csv.
     */
    public static final int LINHAS_DETALHE_CSV = 4;

    /**
     * Cria dados com base no CSV X a classe informada.
     *
     * @return Representacao do arquivo contasbancarias.csv em um
     * CSVInitialDataSet.
     */
    public static CSVInitialDataSet<DetalheProcedimento> getDetalhesCSV() {
        return new CSVInitialDataSet<DetalheProcedimento>(DetalheProcedimento.class,
                "detalheprocedimentos.csv", "id", "detalhe",
                "usuario", "ativo", "tipo");
    }
    /**
     * Quantidade linhas do arquivo agendaprocedimentofixo.csv.
     */
    public static final int LINHAS_AGENDA_CSV = 7;

    /**
     * Cria dados com base no CSV X a classe informada.
     *
     * @return Representacao do arquivo agendaprocedimentofixo.csv em um
     * CSVInitialDataSet.
     */
    public static CSVInitialDataSet<AgendaProcedimentoFixo> getAgendaCSV() {
        return new CSVInitialDataSet<AgendaProcedimentoFixo>(AgendaProcedimentoFixo.class,
                "agendaprocedimentofixo.csv", "codigo", "valorFixo",
                "dataPrimeiroVencimento", "observacao", "usuario",
                "detalhe", "periodo",
                "quantidadePeriodo").addDateFormat(
                DateFormats.USER_DATE.setUserDefinedFomatter("yyyy MM dd"));
    }

    /**
     * Cria dados com base no CSV X a classe informada.
     *
     * @return Representacao do arquivo cartao_credito.csv em um
     * CSVInitialDataSet.
     */
    public static CSVInitialDataSet<CartaoCredito> getCartaoCSV() {
        return new CSVInitialDataSet<CartaoCredito>(CartaoCredito.class,
                "cartao_credito.csv", "id", "cartao",
                "diaVencimento", "diaMesmoMes", "ativo", "usuario");
    }

    /**
     * Cria dados com base no CSV X a classe informada.
     *
     * @return Representacao do arquivo procedimento.csv em um
     * CSVInitialDataSet.
     */
    public static CSVInitialDataSet<DespesaProcedimento> getDespProcimentoCSV() {
        return new CSVInitialDataSet<DespesaProcedimento>(DespesaProcedimento.class,
                "desp_procedimento.csv", "id", "dataVencimento",
                "valorEstimado", "valorReal", "detalhe",
                "classificacaoProcedimento", "statusPagamento",
                "observacao", "usuario", "tipo", "tipoProcedimento",
                "cartaoCredito").addDateFormat(
                DateFormats.USER_DATE.setUserDefinedFomatter("yyyy MM dd"));
    }

    /**
     * Cria dados com base no CSV X a classe informada.
     *
     * @return Representacao do arquivo procedimento.csv em um
     * CSVInitialDataSet.
     */
    public static CSVInitialDataSet<ReceitaProcedimento> getRececProcimentoCSV() {
        return new CSVInitialDataSet<ReceitaProcedimento>(ReceitaProcedimento.class,
                "receita_procedimento.csv", "id", "dataVencimento",
                "valorEstimado", "valorReal", "detalhe",
                "classificacaoProcedimento", "statusPagamento",
                "observacao", "usuario", "tipo", "tipoProcedimento",
                "agenda").addDateFormat(
                DateFormats.USER_DATE.setUserDefinedFomatter("yyyy MM dd"));
    }

    /**
     * As classes de teste devem sobrescrever tearDown() de
     * BaseSessionBeanFixture. Bug do EJB3Unit que não realiza a limpeza
     * dos dados entre um teste e outro.<br> Esse método unifica o local de
     * deleção das tabelas.
     *
     * @param con
     * @throws Exception
     */
    public static void tearDown(Connection con) throws Exception {
        con.setAutoCommit(true);
        con.prepareStatement("DELETE from fin_procedimento_despesa_unica").executeUpdate();
        con.prepareStatement("DELETE from fin_procedimento_despesa_parcelada").executeUpdate();
        con.prepareStatement("DELETE from fin_procedimento_receita_unica").executeUpdate();
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
}
