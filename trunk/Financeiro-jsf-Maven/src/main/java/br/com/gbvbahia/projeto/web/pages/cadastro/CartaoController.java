/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.cadastro;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.projeto.logger.I18nLogger;
import br.com.gbvbahia.projeto.web.common.EntityController;
import br.com.gbvbahia.projeto.web.common.EntityPagination;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import br.com.gbvbahia.utils.MensagemUtils;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.apache.log4j.Logger;

/**
 *
 * @author Guilherme
 */
@ManagedBean
@ViewScoped
public class CartaoController extends EntityController<CartaoCredito> implements Serializable {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(CartaoController.class);
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private CartaoCreditoFacade cartaoFacade;
    private CartaoCredito current;
    
    private String cartaoFiltro;

    /**
     * Padrão
     */
    public CartaoController() {
    }

    //====================
    //Iniciadores
    //====================
    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...CartaoController");
    }

    /**
     * Executado antes do bean JSF ser destruído.
     */
    @PreDestroy
    public void end() {
        logger.info("end()...CartaoController");
    }

    //====================
    //Métodos Sobrescritos
    //====================
    
        @Override
    public EntityPagination getPagination() {
        if (pagination == null) {
            pagination = new EntityPagination() {
                @Override
                public int getItemsCount() {
                    return getFacade().contarCartoesUsuarioCartao(usuarioFacade.getUsuario(), cartaoFiltro).intValue();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().
                            buscarCartoesUsuarioCartaoPaginado(usuarioFacade.getUsuario(), cartaoFiltro,
                            new int[]{getPageFirstItem(), getPageFirstItem()
                                + getPageSize()}));
                }
            };
        }
        return pagination;
    }
    @Override
    public String clean() {
        super.clean();
        return JsfUtil.MANTEM;
    }

    @Override
    protected String create() {
        try {
            getFacade().create(current);
            MensagemUtils.messageFactoringFull("CartaoCreated",
                    new Object[]{current.getCartao()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            return clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("createError", current.toString()));
            return JsfUtil.MANTEM;
        }
    }

    @Override
    protected String update() {
        try {
            getFacade().update(current);
            MensagemUtils.messageFactoringFull("CartaoUpdated",
                    new Object[]{current.getCartao()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            return clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("updateError", current.toString()));
            return JsfUtil.MANTEM;
        }
    }

    @Override
    protected void performDestroy() {
        try {
            current.setAtivo(!current.isAtivo());
            getFacade().update(current);
            MensagemUtils.messageFactoringFull(current.isAtivo() ? "CartaoAtivada" : "CartaoDestivada",
                    new Object[]{current.getCartao()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("removeError", current.toString()));
        }
    }
    
    @Override
    public void setEntity(final CartaoCredito t) {
        this.current = t;
    }

    @Override
    protected CartaoCredito getNewEntity() {
        CartaoCredito cartao = new CartaoCredito();
        cartao.setUsuario(usuarioFacade.getUsuario());
        return cartao;
    }
    
    
    //====================
    //Getters AND Setters
    //====================
    /**
     * O Facade que representa a entidade current.
     *
     * @return AbstractEntityBeans
     */
    public CartaoCreditoFacade getFacade() {
        return cartaoFacade;
    }

    public CartaoCredito getCurrent() {
        return current;
    }

    public String getCartaoFiltro() {
        return cartaoFiltro;
    }

    public void setCartaoFiltro(String cartaoFiltro) {
        this.cartaoFiltro = cartaoFiltro;
    }
}
