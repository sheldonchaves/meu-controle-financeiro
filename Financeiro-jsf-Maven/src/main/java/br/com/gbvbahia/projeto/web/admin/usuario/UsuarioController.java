/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.admin.usuario;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.GrupoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Grupo;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.utils.Base64Encoder;
import br.com.gbvbahia.projeto.logger.I18nLogger;
import br.com.gbvbahia.projeto.web.common.EntityController;
import br.com.gbvbahia.projeto.web.common.EntityPagination;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import br.com.gbvbahia.utils.MensagemUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;

/**
 * CRUD Usuário.
 *
 * @author Guilherme
 * @since 2012/02/22
 */
@ManagedBean
@ViewScoped
public class UsuarioController extends EntityController<Usuario>
        implements Serializable {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(UsuarioController.class);
    /**
     * Facade injetado pelo Contâiner para CRUD com usuário.
     */
    @EJB
    private UsuarioFacade usuarioFacade;
    /**
     * Facade injetado pelo Contâiner para CRUD com Grupo.
     */
    @EJB
    private GrupoFacade grupoFacade;
    /**
     * Filtro na tabela de usuários por primeiro nome.
     */
    private String nome;
    /**
     * Filtro na tabela de usuários por login.
     */
    private String login;
    /**
     * Utilizado para alteração de senha.
     */
    private String senha;
    /**
     * Usuário que está sendo atualizado, criado ou apagado.
     */
    private Usuario current;

    /**
     * Construtor padrão.
     */
    public UsuarioController() {
    }

    //====================
    //Iniciadores
    //====================
    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...");
    }

    /**
     * Executado antes do bean JSF ser destruído.
     */
    @PreDestroy
    public void end() {
        logger.info("end()...");
    }

    //====================
    //Métodos Sobrescritos
    //====================
    @Override
    public String clean() {
        super.clean();
        this.senha = null;
        return JsfUtil.MANTEM;
    }

    @Override
    public EntityPagination getPagination() {
        if (pagination == null) {
            pagination = new EntityPagination() {

                @Override
                public int getItemsCount() {
                    return getFacade().contarPorNomeLogin(nome, login);
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().
                            buscarUsuarioPorNomeLogin(nome, login,
                            new int[]{getPageFirstItem(), getPageFirstItem()
                                + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    @Override
    protected String create() {
        try {
            current.setPass(Base64Encoder.encryptPassword(senha));
            getFacade().create(current);
            MensagemUtils.messageFactoringFull("UsuarioCreated",
                    new Object[]{current.getUserId()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            return clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("createError", current.getUserId()));
            return JsfUtil.MANTEM;
        }
    }

    @Override
    protected String update() {
        try {
            if (senha != null) {
                current.setPass(senha);
            }
            getFacade().update(current);
            MensagemUtils.messageFactoringFull("UsuarioUpdated",
                    new Object[]{current.getFirstName()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            return clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("updateError", current.getUserId()));
            return JsfUtil.MANTEM;
        }
    }

    @Override
    protected void performDestroy() {
        try {
            getFacade().remove(current);
            MensagemUtils.messageFactoringFull("UsuarioDeleted",
                    new Object[]{current.getFirstName()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("removeError", current.getUserId()));
        }
    }

    @Override
    public void setEntity(final Usuario t) {
        this.current = t;
    }

    @Override
    protected Usuario getNewEntity() {
        Usuario user = new Usuario();
        for (Grupo g : grupoFacade.findAll()){
            if(g.getUsuarioPadrao()){
                user.getGrupos().add(g);
            }
        }
        return user;
    }

    /**
     * Altera a senha do usuário selecinado.
     *
     * @param newSenha A nova senha do usuário editado
     * @throws EntityException lanca excessao caso aconteça algum erro
     */
    public void alterarSenha(final String newSenha) throws NegocioException {
        usuarioFacade.alterarSenha(current, newSenha);
        MensagemUtils.messageFactoringFull("SucessUpdatePassword",
                new Object[]{current.getFirstName()},
                FacesMessage.SEVERITY_INFO,
                FacesContext.getCurrentInstance());
        clean();
    }

    //====================
    //Métodos Filtros Paginação
    //====================
    /**
     * Retorna o login utilizado para filtro na paginação.
     *
     * @return nome java.lang.String
     */
    public String getLogin() {
        return login;
    }

    /**
     * Define o login do Usuário para filtrar na paginação.
     *
     * @param loginParam java.lang.String
     */
    public void setLogin(final String loginParam) {
        this.login = loginParam;
    }

    /**
     * Retorna o nome utilizado para filtro na paginação.
     *
     * @return nome java.lang.String
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do Usuário para filtrar na paginação.
     *
     * @param nomeParam java.lang.String
     */
    public void setNome(final String nomeParam) {
        this.nome = nomeParam;
    }

    //====================
    // Select Itens
    //====================
    /**
     * Todos os Grupos para multipla escolha.
     *
     * @return Array de javax.faces.model.SelectItem
     */
    public SelectItem[] getGruposAvailableSelectMany() {
        return JsfUtil.getSelectItems(grupoFacade.findAll(), false,
                FacesContext.getCurrentInstance());
    }

    /**
     * Todos os Grupos para unica escolha.
     *
     * @return Array de javax.faces.model.SelectItem
     */
    public SelectItem[] getGruposAvailableSelectOne() {
        return JsfUtil.getSelectItems(grupoFacade.findAll(), true,
                FacesContext.getCurrentInstance());
    }
    //====================
    //Getters AND Setters
    //====================

    /**
     * O Facade que representa a entidade current.
     *
     * @return AbstractEntityBeans
     */
    public UsuarioFacade getFacade() {
        return usuarioFacade;
    }

    /**
     * O Facade que representa a entidade Grupo.
     *
     * @return AbstractEntityBeans
     */
    public GrupoFacade getEjbGrupoFacade() {
        return grupoFacade;
    }

    /**
     * Utilizado pelo JSF para realizar Injeção.
     *
     * @param facade Facade da camada de negócio.
     */
    public void setGrupoFacade(final GrupoFacade facade) {
        this.grupoFacade = facade;
    }

    /**
     * Utilizado pelo JSF para realizar Injeção.
     *
     * @param facade Facade da camada de negócio.
     */
    public void setUsuarioFacade(final UsuarioFacade facade) {
        this.usuarioFacade = facade;
    }

    /**
     * Usuário atual, pode ser null, pode ser um novo ou um já existente.
     *
     * @return Usuario
     */
    public Usuario getCurrent() {
        return current;
    }

    /**
     * Primeira senha digitado pelo usuário. Deve ser igual a senhaConfirm.
     *
     * @return java.lang.String
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define a senha do usuário. Deve ser igual a senhaConfirm.
     *
     * @param senhaParam java.lang.String
     */
    public void setSenha(final String senhaParam) {
        this.senha = senhaParam;
    }

    /**
     * SelectManyMenu do JSF não pode apontar para um SET, por isso essa
     * mascara para que funcione sem problemas.
     *
     * @return List com o grupos do usuário selecionado.
     */
    public List<Grupo> getGruposUserEdit() {
        if (current == null) {
            return null;
        }
        return new ArrayList<Grupo>(this.current.getGrupos());
    }

    /**
     * SelectManyMenu do JSF não pode apontar para um SET, por isso essa
     * mascara para que funcione sem problemas.
     *
     * @param gruposUserEdit Grupo selecionado na tela.
     */
    public void setGruposUserEdit(final List<Grupo> gruposUserEdit) {
        if (this.current != null) {
            this.current.setGrupos(new HashSet<Grupo>(gruposUserEdit));
        }
    }
}
