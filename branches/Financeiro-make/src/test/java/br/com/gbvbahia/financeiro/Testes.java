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
import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.ClassificacaoProcedimento;
import br.com.gbvbahia.financeiro.constantes.Periodo;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.constantes.TipoConta;
import br.com.gbvbahia.financeiro.modelos.*;
import br.com.gbvbahia.financeiro.modelos.superclass.DetalheProcedimento;
import br.com.gbvbahia.financeiro.modelos.superclass.Procedimento;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.financeiro.utils.I18nTest;
import com.bm.testsuite.dataloader.CSVInitialDataSet;
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
 * TESTES SEM MAKE
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
//    DetalheProcedimentoBeanSearchTest.class,
//    AgendaProcedimentoFixoBeanCreateTest.class,
//    AgendaProcedimentoFixoBeanSearchTest.class,
//    CartaoCreditoTest.class,
//    DespesaProcedimentoBeanTest.class,
//    ProcedimentoBeanTest.class,
//    ProvisaoBeanTest.class
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

    public static void createAgendas(EntityManager manager) throws Exception {
        try {
            AgendaProcedimentoFixo agenda1 = criateAgenda("33.59",
                    "20120110", "Conta de Água", "user01",
                    4L, Periodo.MESES, 1);
            AgendaProcedimentoFixo agenda2 = criateAgenda("133.59",
                    "20120105", "Conta de Luz", "user02",
                    4L, Periodo.MESES, 1);
            AgendaProcedimentoFixo agenda3 = criateAgenda("150.00",
                    "20120101", "Supermercado", "user02",
                    2L, Periodo.DIAS, 7);
            AgendaProcedimentoFixo agenda4 = criateAgenda("80.00",
                    "20120101", "Gasolina", "user01",
                    1L, Periodo.DIAS, 7);
            AgendaProcedimentoFixo agenda5 = criateAgenda("50.00",
                    "20120101", "Lavagem Carro", "user01",
                    1L, Periodo.DIAS, 15);
            AgendaProcedimentoFixo agenda6 = criateAgenda("50.00",
                    "20120101", "Almoços Rua", "user01",
                    2L, Periodo.DIAS, 7);
            AgendaProcedimentoFixo agenda7 = criateAgenda("360.00",
                    "20120110", "Condominio", "user02",
                    4L, Periodo.MESES, 1);
            AgendaProcedimentoFixo agenda8 = criateAgenda("3360.00",
                    "20120101", "Salario Mensal", "user01",
                    4L, Periodo.MESES, 1);

            manager.getTransaction().begin();
            manager.persist(agenda1);
            manager.persist(agenda2);
            manager.persist(agenda3);
            manager.persist(agenda4);
            manager.persist(agenda5);
            manager.persist(agenda6);
            manager.persist(agenda7);
            manager.persist(agenda8);
            manager.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
                String message = violation.getMessage();
                System.out.println("ATENÇÃO: ***   CRIAÇÃO DE AGENDAS ABORTADA!!!");
                System.out.println(message);
            }
        } finally {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
        }
    }

    private static AgendaProcedimentoFixo criateAgenda(String valor,
            String data, String observacao, String userId,
            long idDetProcedimento, Periodo periodo,
            int quantidadePeriodo) throws Exception {
        AgendaProcedimentoFixo agenda = new AgendaProcedimentoFixo();
        agenda.setValorFixo(new BigDecimal(valor));
        agenda.setDataPrimeiroVencimento(DateUtils.convertStringToCalendar(data, "yyyyMMdd"));
        agenda.setObservacao(observacao);
        agenda.setUsuario(getUsuarioFacade().find(userId));
        agenda.setDetalhe(getDetalheProcedimentoFacade().find(idDetProcedimento));
        agenda.setPeriodo(periodo);
        agenda.setQuantidadePeriodo(quantidadePeriodo);
        return agenda;
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
     * createAgendas() deve ser chamado antes.
     *
     * @param manager
     * @throws Exception
     */
    public static void criarDespProcedimentos(EntityManager manager) throws Exception {
        DespesaProcedimento rp1 = criaDespesa("10/01/2012", "158.65",
                "158.65", 1L, ClassificacaoProcedimento.VARIAVEL,
                StatusPagamento.NAO_PAGA, "Despesa Teste 1", "user01", null);
        DespesaProcedimento rp2 = criaDespesa("05/01/2012", "55.00",
                "65.00", 2L, ClassificacaoProcedimento.FIXA,
                StatusPagamento.NAO_PAGA, "Despesa Teste 2", "user01", 2L);
        DespesaProcedimento rp3 = criaDespesa("07/01/2012", "471.00",
                "471.00", 2L, ClassificacaoProcedimento.VARIAVEL,
                StatusPagamento.NAO_PAGA, "Despesa Teste 3", "user02", null);
        DespesaProcedimento rp4 = criaDespesa("19/01/2012", "622.41",
                "622.41", 2L, ClassificacaoProcedimento.VARIAVEL,
                StatusPagamento.PAGA, "Despesa Teste 4", "user01", 1L);
        DespesaProcedimento rp5 = criaDespesa("12/01/2012", "22.45",
                "22.45", 3L, ClassificacaoProcedimento.VARIAVEL,
                StatusPagamento.PAGA, "Despesa Teste 5", "user03", 4L);
        DespesaProcedimento rp6 = criaDespesa("15/01/2012", "22.45",
                "22.45", 3L, ClassificacaoProcedimento.VARIAVEL,
                StatusPagamento.PAGA, "Despesa Teste 6", "user02", 1L);

        try {
            manager.getTransaction().begin();
            manager.persist(rp1);
            manager.persist(rp2);
            manager.persist(rp3);
            manager.persist(rp5);
            manager.persist(rp6);
            manager.persist(rp4);
            manager.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
                String message = violation.getMessage();
                System.out.println("ATENÇÃO: ***   CRIAÇÃO DE DESPESA PROCEDIMENTO ABORTADA!!!");
                System.out.println(message);
            }
        } finally {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
        }
    }

    private static DespesaProcedimento criaDespesa(String data,
            String vrEstimadno, String vrReal, Long idDetProc,
            ClassificacaoProcedimento clas, StatusPagamento status,
            String obs, String user, Long cartaoId) throws Exception {
        DespesaProcedimento rp = new DespesaProcedimento();
        rp.setDataVencimento(DateUtils.convertStringToCalendar(data, "dd/MM/yyyy"));
        rp.setValorEstimado(new BigDecimal(vrEstimadno));
        rp.setValorReal(new BigDecimal(vrReal));
        rp.setDetalhe(getDetalheProcedimentoFacade().find(idDetProc));
        rp.setClassificacaoProcedimento(clas);
        rp.setStatusPagamento(status);
        rp.setObservacao(obs);
        rp.setUsuario(getUsuarioFacade().find(user));
        if (cartaoId != null) {
            rp.setCartaoCredito(getCartaoFacade().find(cartaoId));
        }
        return rp;
    }

    /**
     * createAgendas() deve ser chamado antes. criarDespProcedimentos()
     * deve ser chamado antes.
     *
     * @param manager
     * @throws Exception
     */
    public static void criarReceitaProcedimentos(EntityManager manager) throws Exception {
        ReceitaProcedimento rp1 = criaReceita("01/07/2012", "3458.65",
                "3458.65", 3L, ClassificacaoProcedimento.FIXA,
                StatusPagamento.NAO_PAGA, "Despesa Teste 1", "user01",
                getAgenda(Testes.getAgendaFacade(), "Salario Mensal").getCodigo());
        ReceitaProcedimento rp2 = criaReceita("01/08/2012", "3555.00",
                "3465.00", 4L, ClassificacaoProcedimento.FIXA,
                StatusPagamento.NAO_PAGA, "Despesa Teste 2", "user01",
                getAgenda(Testes.getAgendaFacade(), "Salario Mensal").getCodigo());
        ReceitaProcedimento rp3 = criaReceita("01/04/2012", "3471.00",
                "3671.00", 3L, ClassificacaoProcedimento.FIXA,
                StatusPagamento.NAO_PAGA, "Despesa Teste 3", "user02", null);
        ReceitaProcedimento rp4 = criaReceita("01/03/2012", "3622.41",
                "3222.41", 4L, ClassificacaoProcedimento.FIXA,
                StatusPagamento.NAO_PAGA, "Despesa Teste 4", "user01", null);
        ReceitaProcedimento rp5 = criaReceita("01/01/2012", "3222.45",
                "3422.45", 4L, ClassificacaoProcedimento.FIXA,
                StatusPagamento.PAGA, "Despesa Teste 5", "user03", null);
        ReceitaProcedimento rp6 = criaReceita("01/02/2012", "3122.45",
                "3422.45", 4L, ClassificacaoProcedimento.FIXA,
                StatusPagamento.PAGA, "Despesa Teste 6", "user02", null);
        ReceitaProcedimento rp7 = criaReceita("01/03/2012", "3622.41",
                "3222.41", 4L, ClassificacaoProcedimento.FIXA,
                StatusPagamento.NAO_PAGA, "Despesa Teste 7", "user04", null);
        try {
            manager.getTransaction().begin();
            manager.persist(rp1);
            manager.persist(rp2);
            manager.persist(rp3);
            manager.persist(rp5);
            manager.persist(rp6);
            manager.persist(rp4);
            manager.persist(rp7);
            manager.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
                String message = violation.getMessage();
                System.out.println("ATENÇÃO: ***   CRIAÇÃO DE RECEITA PROCEDIMENTO ABORTADA!!!");
                System.out.println(message);
            }
        } finally {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
        }
    }

    private static ReceitaProcedimento criaReceita(String data,
            String vrEstimadno, String vrReal, Long idDetProc,
            ClassificacaoProcedimento clas, StatusPagamento status,
            String obs, String user, Long agendaId) throws Exception {
        ReceitaProcedimento rp = new ReceitaProcedimento();
        rp.setDataVencimento(DateUtils.convertStringToCalendar(data, "dd/MM/yyyy"));
        rp.setValorEstimado(new BigDecimal(vrEstimadno));
        rp.setValorReal(new BigDecimal(vrReal));
        rp.setDetalhe(getDetalheProcedimentoFacade().find(idDetProc));
        rp.setClassificacaoProcedimento(clas);
        rp.setStatusPagamento(status);
        rp.setObservacao(obs);
        rp.setUsuario(getUsuarioFacade().find(user));
        if (agendaId != null) {
            rp.setAgenda(getAgendaFacade().find(agendaId));
        }
        return rp;
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
     * Busca a AgendaProcedimento em memória pela observação.
     * @param facade
     * @param obs
     * @return Agenda com a bservacao passada, nulo se não encontrar
     */
    public static AgendaProcedimentoFixo getAgenda(AgendaProcedimentoFixoFacade facade,
            String obs) {
        for (AgendaProcedimentoFixo ag : facade.findAll()) {
            if (ag.getObservacao().equalsIgnoreCase(obs)) {
                return ag;
            }
        }
        return null;
    }
}
