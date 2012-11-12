/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.jsfutil;

import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.GrupoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
import br.com.gbvbahia.financeiro.modelos.Grupo;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Guilherme
 */
@ManagedBean
@RequestScoped
public class ConverterController {

    @EJB
    private GrupoFacade grupoFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private DetalheProcedimentoFacade detalheFacade;
    @EJB
    private CartaoCreditoFacade cartaoFacade;
    //====================
    // Conversor (Grupo)
    //====================

    /**
     * Conversor para Grupo.
     */
    @FacesConverter(forClass = Grupo.class, value = "grupoConverter")
    public static class GrupoControllerConverter implements Converter {

        /**
         *
         * @param facesContext Contexto JSF.
         * @param component Componente JSF que possui a entidade a ser
         * convertida.
         * @param value String que foi enviada pelo método getAsString.
         * @return O objeto convertido em getAsString.
         */
        @Override
        public Object getAsObject(final FacesContext facesContext,
                final UIComponent component, final String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConverterController controller =
                    JsfUtil.getController("converterController", facesContext);
            return controller.grupoFacade.find(value);
        }

        /**
         *
         * @param facesContext Contexto JSF.
         * @param component Componente JSF que possui a entidade a ser
         * convertida.
         * @param object Objeto a ser convertido em String para ir para a tela
         * do usuário.
         * @return String que representa o grupo, grupoId.
         */
        @Override
        public String getAsString(final FacesContext facesContext,
                final UIComponent component, final Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Grupo) {
                Grupo o = (Grupo) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("object " + object
                        + " is of type " + object.getClass().getName()
                        + "; expected type: "
                        + Grupo.class.getName());
            }
        }
    }

    //====================
    // Conversor (Usuário)
    //====================
    /**
     * Conversor para classe Usuario em SelectItem.
     */
    @FacesConverter(forClass = Usuario.class, value = "usuarioConverter")
    public static class UsuarioControllerConverter implements Converter {

        /**
         * Devolve um Objeto Usuário com base nos parâmetros.
         *
         * @param facesContext Contexto atual.
         * @param component Componente SelectMany, SelectOne do JSF.
         * @param value Usuário em forma de String que deve ser convertido em
         * objeto. A string aqui é o login do usuário.
         * @return java.lang.Object que pode ser feito cast para
         * Usuario.
         */
        @Override
        public Object getAsObject(final FacesContext facesContext,
                final UIComponent component, final String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConverterController controller =
                    JsfUtil.getController("converterController", facesContext);
            return controller.usuarioFacade.find(value);
        }

        /**
         * Retorna o objeto Usuário em formato de String, que representa um
         * Usuário.
         *
         * @param facesContext Contexto atual.
         * @param component Componente JSF que mostra o Usuário na tela.
         * @param object Um Usuario.
         * @return java.lang.String, login Usuario toString.
         */
        @Override
        public String getAsString(final FacesContext facesContext,
                final UIComponent component, final Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Usuario) {
                Usuario o = (Usuario) object;
                return o.getUserId();
            } else {
                throw new IllegalArgumentException("object " + object
                        + " is of type " + object.getClass().getName()
                        + "; expected type: "
                        + Usuario.class.getName());
            }
        }
    }

    //====================
    // Conversor (DetalheProcedimento)
    //====================
    /**
     * Conversor para classe DetalheProcedimento em SelectItem.
     */
    @FacesConverter(forClass = DetalheProcedimento.class, value = "detalheConverter")
    public static class DetalheControllerConverter implements Converter {

        /**
         * Devolve um Objeto DetalheProcedimento com base nos parâmetros.
         *
         * @param facesContext Contexto atual.
         * @param component Componente SelectMany, SelectOne do JSF.
         * @param value Usuário em forma de String que deve ser convertido em
         * objeto. A string aqui é o login do usuário.
         * @return java.lang.Object que pode ser feito cast para
         * DetalheProcedimento.
         */
        @Override
        public Object getAsObject(final FacesContext facesContext,
                final UIComponent component, final String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConverterController controller =
                    JsfUtil.getController("converterController", facesContext);
            return controller.detalheFacade.find(Long.parseLong(value));
        }

        /**
         * Retorna o objeto DetalheProcedimento em formato de String, que
         * representa um DetalheProcedimento.
         *
         * @param facesContext Contexto atual.
         * @param component Componente JSF que mostra o Usuário na tela.
         * @param object Um DetalheProcedimento.
         * @return
         */
        @Override
        public String getAsString(final FacesContext facesContext,
                final UIComponent component, final Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof DetalheProcedimento) {
                DetalheProcedimento o = (DetalheProcedimento) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("object " + object
                        + " is of type " + object.getClass().getName()
                        + "; expected type: "
                        + DetalheProcedimento.class.getName());
            }
        }
    }

    //====================
    // Conversor (CartaoCredito)
    //====================
    /**
     * Conversor para classe CartaoCredito em SelectItem.
     */
    @FacesConverter(forClass = CartaoCredito.class, value = "cartaoConverter")
    public static class CartaoControllerConverter implements Converter {

        /**
         * Devolve um Objeto Usuário com base nos parâmetros.
         *
         * @param facesContext Contexto atual.
         * @param component Componente SelectMany, SelectOne do JSF.
         * @param value Usuário em forma de String que deve ser convertido em
         * objeto. A string aqui é o login do usuário.
         * @return java.lang.Object que pode ser feito cast para
         * CartaoCredito.
         */
        @Override
        public Object getAsObject(final FacesContext facesContext,
                final UIComponent component, final String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConverterController controller =
                    JsfUtil.getController("converterController", facesContext);
            return controller.cartaoFacade.find(Long.parseLong(value));
        }

        /**
         * Retorna o objeto CartaoCredito em formato de String, que representa
         * um CartaoCredito.
         *
         * @param facesContext Contexto atual.
         * @param component Componente JSF que mostra o Usuário na tela.
         * @param object Um CartaoCredito
         * @return .
         */
        @Override
        public String getAsString(final FacesContext facesContext,
                final UIComponent component, final Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof CartaoCredito) {
                CartaoCredito o = (CartaoCredito) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("object " + object
                        + " is of type " + object.getClass().getName()
                        + "; expected type: "
                        + CartaoCredito.class.getName());
            }
        }
    }
}
