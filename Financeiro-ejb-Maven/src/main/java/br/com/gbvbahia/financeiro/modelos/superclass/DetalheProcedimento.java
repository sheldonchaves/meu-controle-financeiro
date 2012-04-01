package br.com.gbvbahia.financeiro.modelos.superclass;

import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import br.com.gbvbahia.financeiro.utils.I18N;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;

/**
 *
 * Representa o motivo de uma conta ou receita financeira. Não
 * utilizei @MappedSuperclass porque iria obrigar a escrever um bean
 * para despesa e um para receita.<br> Por ser uma Entidade, @Entity,
 * existe uma critica pela IDE do construtor,
 * <strong>desconsidere</strong>, já que a classe é abstrata e nunca
 * poderá ser instânciada diretamente. Cabe a sub-classes ter esse
 * construtor e informar seu tipo a superclasse.
 *
 * @author Guilherme
 * @since v.3 01/04/2012
 */
@Entity
@Table(name = "fin_detalhe")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo",
discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PROCEDIMENTO")
public abstract class DetalheProcedimento
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
     * Usuário que cria o detalhe, seu conjuge também verá o detalhe e
     * poderá alterar o mesmo.
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
    @Column(name = "fl_tipo_procedimento", nullable = false)
    private TipoProcedimento tipoProcedimento;

    /**
     * Obrigatório informar o tipo de procedimento.<br> Retirada
     * determina uma DESPESA.<br> Deposito determina uma RECEITA.
     *
     * @param tipo Tipo de Procedimento.
     */
    public DetalheProcedimento(final TipoProcedimento tipo) {
        this.tipoProcedimento = tipo;
    }

    /**
     * Construtor nunca executado, se for uma runtime será lançada.
     */
    public DetalheProcedimento() {
        throw new IllegalArgumentException(
                I18N.getMsg("DetalheConstrutorErro"));
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
     * Usuário que cria o detalhe, seu conjuge também verá o detalhe e
     * poderá alterar o mesmo.
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
    public TipoProcedimento getTipoProcedimento() {
        return tipoProcedimento;
    }

    /**
     * Usuário que cria o detalhe, seu conjuge também verá o detalhe e
     * poderá alterar o mesmo.
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
}
