/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.ejbbeans;

import br.com.financeiro.ejbbeans.interfaces.UserLocal;
import br.com.financeiro.ejbbeans.timeservices.interfaces.TimerContasLocal;
import br.com.financeiro.ejbbeans.timeservices.utilitarias.SimpleEmail;
import br.com.financeiro.entidades.Grups;
import br.com.financeiro.entidades.LembreteConta;
import br.com.financeiro.entidades.User;
import br.com.financeiro.excecoes.LembreteContasException;
import br.com.financeiro.excecoes.ProprietarioConjugeException;
import br.com.financeiro.excecoes.ProprietarioException;
import br.com.financeiro.excecoes.ProprietarioLoginException;
import br.com.financeiro.excecoes.ProprietarioNomeException;
import br.com.financeiro.excecoes.ProprietarioSenhaException;
import br.com.financeiro.utils.Criptografia;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.validator.EmailValidator;

/**
 *
 * @author Guilherme
 */
@Stateless
public class UserBean implements UserLocal {

    @EJB
    private TimerContasLocal timerContas;

    @PersistenceContext(unitName = "financeiro")
    private EntityManager entityManager;

    @Override
    @Interceptors(br.com.financeiro.ejbbeans.interceptadores.MethodExecutionTime.class)
    public void salvarproprietario(User user, String urlFinanceiro) throws ProprietarioSenhaException,
            ProprietarioNomeException, ProprietarioLoginException, ProprietarioException {
        verificaProprietario(user);
        if (user.getId() == null) {
            if (this.verificaLoginProprietario(user.getLogin())) {
                throw new ProprietarioLoginException("Já existe um login cadastrado, informe outro!");
            }
        }
        String newPass = null;
        if (user.getPassword() == null || user.getPassword().equals("")) {
            newPass = this.geraSenha();
            user.setPassword(Criptografia.encodePassword(newPass, user.stringAMIN()));
        }
        user = entityManager.merge(user);
        entityManager.flush();
        if (newPass != null) {
            avisaSenhaEmail(newPass, urlFinanceiro, user);
        }
    }

    @Override
    @Interceptors(br.com.financeiro.ejbbeans.interceptadores.MethodExecutionTime.class)
    public void alteraSenhaUser(User user, String urlFinanceiro, boolean resetarSenha) throws ProprietarioSenhaException,
            ProprietarioNomeException, ProprietarioLoginException, ProprietarioException {
        verificaProprietario(user);
        if (user.getId() == null) {
            throw new ProprietarioException("Novos usuários devem ser cadastrados utilizando o método salvarProprietario()!");
        }
        String newSenha = null;
        if (resetarSenha || user.getPassword() == null || user.getPassword().equals("")) {
            newSenha = this.geraSenha();
            user.setPassword(newSenha);
        }
        user.setPassword(Criptografia.encodePassword(user.getPassword(), user.stringAMIN()));
        entityManager.merge(user);
        if (newSenha != null) {
            avisaSenhaEmail(newSenha, urlFinanceiro, user);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Interceptors(br.com.financeiro.ejbbeans.interceptadores.MethodExecutionTime.class)
    public User buscaProprietarioLogin(String login, boolean contas) {
        Query query = entityManager.createNamedQuery("buscaProprietarioLogin");
        query.setParameter("login", login);
        User toReturn = null;
        try {
            toReturn = (User) query.getSingleResult();
            if (contas) {
                toReturn.getContasBancarias().size();
            }
        } catch (NoResultException nr) {
            //Não precisa de tratmento...
        }
        return toReturn;
    }

    @Override
    @Interceptors(br.com.financeiro.ejbbeans.interceptadores.MethodExecutionTime.class)
    public List<User> todosProprietarios() {
        Query q = this.entityManager.createQuery("Select u From User u");
        return q.getResultList();
    }

    @Override
    @Interceptors(br.com.financeiro.ejbbeans.interceptadores.MethodExecutionTime.class)
    public List<Grups> todosGrups() {
        Query q = this.entityManager.createQuery("Select u From Grups u");
        return q.getResultList();
    }

    @Override
    @Interceptors(br.com.financeiro.ejbbeans.interceptadores.MethodExecutionTime.class)
    public Grups getBrupBayRole(String role) {
        Query q = this.entityManager.createNamedQuery("buscaGrupsByRole");
        q.setParameter("role", role);
        try {
            return (Grups) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Interceptors(br.com.financeiro.ejbbeans.interceptadores.MethodExecutionTime.class)
    public String criptografarSenha(String senha, String role) {
        return Criptografia.encodePassword(senha, role);
    }

    @Override
    @Interceptors(br.com.financeiro.ejbbeans.interceptadores.MethodExecutionTime.class)
    public void salvarLembreteContas(LembreteConta lembreteConta) throws LembreteContasException {
        if (lembreteConta.getId() != null) {
            entityManager.merge(lembreteConta);
        } else {
            entityManager.persist(lembreteConta);
        }
        entityManager.flush();
    }

    @Override
    @Interceptors(br.com.financeiro.ejbbeans.interceptadores.MethodExecutionTime.class)
    public List<LembreteConta> busrcarLembreteContas(boolean status) {
        Query q = entityManager.createNamedQuery("buscarUserLembreteConta");
        q.setParameter("status", status);
        return q.getResultList();
    }

    @Override
    @Interceptors(br.com.financeiro.ejbbeans.interceptadores.MethodExecutionTime.class)
    public LembreteConta busrcarLembreteContas(User user) {
        if (user.getConjugeUser() == null) {
            Query q = entityManager.createNamedQuery("buscarUserLembreteConta2");
            q.setParameter("user", user);
            LembreteConta lembrete = null;
            try {
                lembrete = (LembreteConta) q.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
            return lembrete;
        }else{
            Query q = entityManager.createNamedQuery("buscarUserLembreteConta2Conjuge");
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user);
            LembreteConta lembrete = null;
            try {
                lembrete = (LembreteConta) q.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
            return lembrete;
        }
    }

    @Override
    public void adicionarConjuge(User userLogado, User userConjuge) throws ProprietarioConjugeException {
        if (userLogado.getId() == null || userConjuge.getId() == null) {
            throw new ProprietarioConjugeException("Os usuários devem ser cadastrados antes da vinculação de cônjuges!");
        } else if (userLogado.getConjugeUser() != null || userConjuge.getConjugeUser() != null) {
            throw new ProprietarioConjugeException("Todos os dois usuários não podem ter cônjuges cadastrados!");
        }
        userConjuge.setConjugeUser(userLogado);
        userLogado.setConjugeUser(userConjuge);
        entityManager.merge(userLogado);
        entityManager.merge(userConjuge);
    }

    @Override
    public void removerConjuge(User user) throws ProprietarioConjugeException {
        if (user.getConjugeUser() == null) {
            throw new ProprietarioConjugeException("Este usuário não possui cônjuge!");
        }
        User uC = user.getConjugeUser();
        user.setConjugeUser(null);
        uC.setConjugeUser(null);
        entityManager.merge(uC);
        entityManager.merge(user);
    }

    private void avisaSenhaEmail(String newPass, String url, User user) {
        String body = this.bodyEmail(newPass, url, user);
        SimpleEmail simple = new SimpleEmail();
        simple.setBody(body);
        simple.setEmail(user.getEmail());
        simple.setSubject("Fianceiro :: Controle de Acesso");
        this.timerContas.enviarLembreteContaEmail(simple);
    }

    private void verificaProprietario(User user) throws ProprietarioSenhaException,
            ProprietarioNomeException, ProprietarioLoginException, ProprietarioException {
        if (user.getLogin() == null) {
            throw new ProprietarioLoginException("Um login deve ser informado!");
        }
        if (user.getFirst_name() == null) {
            throw new ProprietarioNomeException("Um nome deve ser informado!");
        }
        if (user.getLast_name() == null) {
            throw new ProprietarioNomeException("O último nome deve ser informado!");
        }
        if (user.getEmail() == null) {
            throw new ProprietarioException("Um email deve ser cadastrado!");
        }
        if (!(EmailValidator.getInstance().isValid(user.getEmail()))) {
            throw new ProprietarioException("O e-mail informado não é válido!");
        }
    }

    /**
     * Se proprietario não existir retorna false
     * @param longin
     * @return false se proprietario não existir
     */
    private boolean verificaLoginProprietario(String longin) {
        if (this.buscaProprietarioLogin(longin, false) == null) {
            return false;
        } else {
            return true;
        }
    }
    /*
    @AroundInvoke
    @SuppressWarnings("LoggerStringConcat")
    public Object testInterceptador(InvocationContext ctx) throws Exception {
    Logger.getLogger(UserBean.class.getName()).log(Level.INFO, "Método Interceptador chamado!!!! Classe:" + this.getClass().getSimpleName() + " Método:" + ctx.getMethod().toString());
    return ctx.proceed();
    }
     */

    private String bodyEmail(String pass, String caminhoFinanciero, User user) {
        String toReturn = "";
        String quebra = "<br>";
        toReturn += "<h3>Bem vindo ao Controle Financeiro!</h3>";
        toReturn += "Seu login é: ";
        toReturn += "<strong>" + user.getLogin() + "</strong>";
        toReturn += quebra;
        toReturn += "Sua senha é: ";
        toReturn += "<strong>" + pass + "</strong>";
        toReturn += quebra;
        toReturn += "Guarde este e-mail, sua senha é criptografada e não é possível recuperá-la!";
        toReturn += quebra;
        toReturn += caminhoFinanciero;
        return toReturn;
    }

    private String geraSenha() {
        Random r = new Random();
        String[] a = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String[] b = {"!", "#", "@", "&", "$"};
        String[] c = {"A", "a", "B", "b", "C", "c", "D", "d", "E", "e", "F", "f", "G", "g", "H", "h", "I", "i", "J", "j", "K", "k", "L", "l", "Z", "z"};
        String[] d = {"K", "k", "L", "l", "M", "m", "N", "n", "O", "o", "P", "p", "Q", "q", "R", "r", "S", "s", "T", "t", "U", "u", "V", "v", "X", "x", "Z", "z"};
        int A = r.nextInt(a.length);
        int A2 = r.nextInt(a.length);
        int B = r.nextInt(b.length);
        int C = r.nextInt(c.length);
        int C2 = r.nextInt(c.length);
        int D = r.nextInt(d.length);
        int D2 = r.nextInt(d.length);
        String senha = "";
        senha += c[C];
        senha += d[D];
        senha += a[A2];
        senha += c[C2];
        senha += b[B];
        senha += d[D2];
        senha += a[A];
        return senha;
    }
}
