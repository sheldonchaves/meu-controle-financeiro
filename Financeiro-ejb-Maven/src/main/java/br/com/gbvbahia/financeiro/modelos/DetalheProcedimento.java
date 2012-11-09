package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * Representa o motivo de uma conta ou receita financeira. Não utilizei
 *
 * @MappedSuperclass porque iria obrigar a escrever um bean para despesa e um
 * para receita.<br> Por ser uma Entidade,
 * @Entity, existe uma critica pela IDE do construtor,
 * <strong>desconsidere</strong>, já que a classe é abstrata e nunca poderá
 * ser instânciada diretamente. Cabe a sub-classes ter esse construtor e
 * informar seu tipo a superclasse.
 *
 * @author Guilherme
 * @since v.3 01/04/2012
 */
@Entity
@Table(name = "fin_detalhe")
@NamedQueries({
    @NamedQuery(name = "DetalheProcedimento.findAllProcedimento",
    query = "SELECT distinct a FROM DetalheProcedimento a "
    + " WHERE (a.usuario = :usuario OR a.usuario.conjuge = :usuario) "
    + " AND (:ativo2 = 'todos' OR a.ativo = :ativo) "
    + " AND (:tipo2 = 'todos' OR a.tipo = :tipo) "),
    @NamedQuery(name = "DetalheProcedimento.countUser",
    query = "SELECT count(a) FROM DetalheProcedimento a "
    + " WHERE (a.usuario = :usuario OR a.usuario.conjuge = :usuario) "),
    @NamedQuery(name = "DetalheProcedimento.selectUser",
    query = "SELECT a FROM DetalheProcedimento a "
    + " WHERE (a.usuario = :usuario OR a.usuario.conjuge = :usuario)"
    + " ORDER BY a.tipo, a.detalhe ")
})
public class DetalheProcedimento
        implements EntityInterface<DetalheProcedimento>, Serializable {

    /**
     * Quantidade máxima de caracteres para o detalhe.
     */
    public static final int QUANTIDADE_MAX_CARACTERES_DETALHE = 100;
    /**
     * Quantidade minima de caracteres para o detalhe.
     */
    public static final int QUANTIDADE_MIN_CARACTERES_DETALHE = 5;
    /**
     * ID único no banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * String que representa o detalhe propriamente dito.
     */
    @Column(name = "ds_detalhe",
    length = QUANTIDADE_MAX_CARACTERES_DETALHE,
    unique = false, nullable = false)
    @NotNull
    @Size(max = QUANTIDADE_MAX_CARACTERES_DETALHE,
    min = QUANTIDADE_MIN_CARACTERES_DETALHE)
    private String detalhe;
    /**
     * Usuário que cria o detalhe, seu conjuge também verá o detalhe e poderá
     * alterar o mesmo.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_usuario", referencedColumnName = "user_id")
    @NotNull
    private Usuario usuario;
    /**
     * Se true será utilizado.<br> Se false não será exibido.
     */
    @Column(name = "fl_ativo", nullable = false)
    private boolean ativo = true;
    /**
     * Recupera o tipo de procedimento.<br> Retirada determina uma
     * DESPESA.<br> Deposito determina uma RECEITA.
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "tipo")
    private TipoProcedimento tipo;

    /**
     * Construtor nunca executado, se for uma runtime será lançada.
     */
    public DetalheProcedimento() {
    }

    public DetalheProcedimento(TipoProcedimento tipo) {
        this.tipo = tipo;
    }

    public DetalheProcedimento(Usuario usuario, TipoProcedimento tipo) {
        this.usuario = usuario;
        this.tipo = tipo;
    }

    public DetalheProcedimento(String detalhe, Usuario usuario, TipoProcedimento tipo) {
        this.detalhe = detalhe;
        this.usuario = usuario;
        this.tipo = tipo;
    }

    /**
     * Visibilidade e utilização, True para utilizar, false para não.
     *
     * @return boolean.
     */
    public boolean isAtivo() {
        return ativo;
    }

    /**
     * Visibilidade e utilização, True para utilizar, false para não.
     *
     * @param visivel boolean.
     */
    public void setAtivo(final boolean visivel) {
        this.ativo = visivel;
    }

    /**
     * Detalhe do procedimento.
     *
     * @return String detalhe.
     */
    public String getDetalhe() {
        return detalhe;
    }

    /**
     * String que define o detalhe.
     *
     * @param det String
     */
    public void setDetalhe(final String det) {
        this.detalhe = det;
    }

    /**
     * ID único BD.
     *
     * @return Long.
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * ID unico BD.
     *
     * @param idDB Long.
     */
    public void setId(final Long idDB) {
        this.id = idDB;
    }

    /**
     * Usuário que cria o detalhe, seu conjuge também verá o detalhe e poderá
     * alterar o mesmo.
     *
     * @return Usuario responsável.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Recupera o tipo de procedimento.<br> Retirada determina uma
     * DESPESA.<br> Deposito determina uma RECEITA.
     *
     * @return TipoProcedimento.
     */
    public TipoProcedimento getTipo() {
        return tipo;
    }

    /**
     * Recupera o tipo de procedimento.<br> Retirada determina uma
     * DESPESA.<br> Deposito determina uma RECEITA.
     *
     * @return TipoProcedimento.
     */
    public void setTipo(TipoProcedimento tipo) {
        this.tipo = tipo;
    }

    /**
     * Usuário que cria o detalhe, seu conjuge também verá o detalhe e poderá
     * alterar o mesmo.
     *
     * @param user Usuario responsável.
     */
    public void setUsuario(final Usuario user) {
        this.usuario = user;
    }

    @Override
    public String getLabel() {
        return StringUtils.substring(getDetalhe(), 0, CARACTERES_LABEL);
    }

    @Override
    public boolean verificarId() {
        return false;
    }

    @Override
    public int compareTo(final DetalheProcedimento o) {
        return getDetalhe().compareTo(o.getDetalhe());
    }

    @Override
    public String toString() {
        return "DetalheProcedimento{" + "id=" + id + ", detalhe=" + detalhe + '}';
    }
}
