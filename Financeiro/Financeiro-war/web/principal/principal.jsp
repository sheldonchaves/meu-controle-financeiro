<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<jsp:text>
    <![CDATA[ <?xml version="1.0" encoding="UTF-8" ?> ]]>
</jsp:text>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%-- RichFaces tag library declaration --%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%--
    This file is an entry point for JavaServer Faces application.
--%>
<f:view>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
            <link href="${pageContext.servletContext.contextPath}\imagens\icon.ico" rel="shortcut icon" type="image/icon">
            <link href="${pageContext.servletContext.contextPath}\imagens\icon.ico" rel='shortcut icon'/>
            <link href="${pageContext.servletContext.contextPath}\imagens\icon.ico" type="image/x-icon" rel="icon"/>
            <link href="${pageContext.servletContext.contextPath}\imagens\icon.ico" type="image/x-icon" rel='shortcut icon'/>
            <link href="${pageContext.servletContext.contextPath}\imagens\icon.ico" rel='apple-touch-icon'/>
            <link href="${pageContext.servletContext.contextPath}\imagens\icon.ico" type='image/x-icon'/>
            <a4j:loadStyle src="/CSS/css.css"></a4j:loadStyle>
            <title><h:outputText value="#{texto.principalTitle}"/></title>
            <a4j:loadScript src="/js/js.js"/>
        </head>
        <body>
            <h:outputText value="#{user_logado}" escape="false" styleClass="tablePricipalTitleProjeto"/>

            <h:panelGrid columns="2" styleClass="principal">

                <h:panelGroup id="logoCoelceGroup" styleClass="logoPrincipalCoelce">
                    <h:graphicImage value="/imagens/lucdiv4.png" id="logoMedium" />
                </h:panelGroup>

                <h:panelGroup id="tituloProjeto" >
                    <rich:spacer width="30px" height="2px"/>
                </h:panelGroup>

                <h:panelGroup id="menu"  >
                    <rich:spacer width="225" height="20"/>
                    <h:panelGroup id="submenu" styleClass="menuPrincipal" >
                        <h:form id="formmenu">
                            <rich:panelMenu mode="ajax" style="width:220px"  iconCollapsedGroup="disc" 
                                            iconExpandedTopGroup="chevronUp" iconGroupTopPosition="right"
                                            iconCollapsedTopGroup="chevronDown" hoveredGroupClass="linha3" hoveredItemClass="linha3" itemClass="coelcefontcolor">
                                <rich:panelMenuGroup label="#{texto.administracao}" expanded="true" hoverClass="linha3" rendered="#{ADMIN == 'ADMIN'}">
                                    <rich:panelMenuItem label="#{texto.usuarios}" action="#{FluxoExibicaoFaces.exibirCadastroUsuario}" icon="/imagens/user24.png" reRender="page" hoverClass="linha3"/>
                                </rich:panelMenuGroup>

                                <rich:panelMenuGroup label="#{texto.cadastro}" expanded="true">
                                    <rich:panelMenuItem label="#{texto.cadastrarContas}" action="#{FluxoExibicaoFaces.exibirCadastroContaBancaria}" icon="/imagens/pig.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.cartaoCreditoTitular}" action="#{FluxoExibicaoFaces.exibirCadastroCartaoCredito}" icon="/imagens/cc24.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.cartaoCreditoDependente}" action="#{FluxoExibicaoFaces.exibirCadastroCartaoCreditoDependnete}" icon="/imagens/cc47_dep.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.cadastrarConjuge}" action="#{FluxoExibicaoFaces.exibirCadastroConjuge}" icon="/imagens/conjuge.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.grupoGasto}" action="#{FluxoExibicaoFaces.exibirCadastroGrupoGasto}" icon="/imagens/gGasto24.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.grupoReceita}" action="#{FluxoExibicaoFaces.exibirCadastroGrupoReceita}" icon="/imagens/gReceita24.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.cadastrarConta}" action="#{FluxoExibicaoFaces.exibirCadastroContasDebito}" icon="/imagens/pg24.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.cadastrarReceita}" action="#{FluxoExibicaoFaces.exibirCadastroReceita}" icon="/imagens/rc24.png" reRender="page"/>
                                </rich:panelMenuGroup>

                                <rich:panelMenuGroup label="#{texto.gerecial}" expanded="true">
                                    <rich:panelMenuItem label="#{texto.paginaInicial}" action="#{FluxoExibicaoFaces.exibirInicial}" icon="/imagens/relatorio.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.contasConfirmadas}" action="#{FluxoExibicaoFaces.exibirPagas}" icon="/imagens/relatorio.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.pagarCartaoCredito}" action="#{FluxoExibicaoFaces.exibirPagarCartao}" icon="/imagens/cc347.png" reRender="page"/>
                                    
                                </rich:panelMenuGroup>

                                <rich:panelMenuGroup label="#{texto.realizadoXprovisionado}" expanded="true">
                                    <rich:panelMenuItem label="#{texto.acumuladoMensal}" action="#{FluxoExibicaoFaces.exibirAcumulado}" icon="/imagens/graf.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.cartaoCredito}" action="#{FluxoExibicaoFaces.exibirCartaoCredito}" icon="/imagens/cc224.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.grupoGastoGraf}" action="#{FluxoExibicaoFaces.exibirGrupoGastoDTO}" icon="/imagens/gragg.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.grupoGasto3}" action="#{FluxoExibicaoFaces.exibirGrupoGastoDetalheDTO}" icon="/imagens/grupoG.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.buscaObs}" action="#{FluxoExibicaoFaces.exibirObservacoes}" icon="/imagens/obs_search.png" reRender="page"/>
                                </rich:panelMenuGroup>

                                <rich:panelMenuGroup label="#{texto.pessoal}" expanded="true">
                                    <rich:panelMenuItem label="#{texto.avisoVencimento}" action="#{FluxoExibicaoFaces.exibirPessoalAviso}" icon="/imagens/clock24.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.alterarSenha}" action="#{FluxoExibicaoFaces.exibirPessoalSenha}" icon="/imagens/login.png" reRender="page"/>
                                    <rich:panelMenuItem label="#{texto.sair}" action="#{LoginCT.deslogar}" icon="/imagens/exit27.png" reRender="page"/>
                                </rich:panelMenuGroup>
                            </rich:panelMenu>
                        </h:form>
                    </h:panelGroup>
                </h:panelGroup>

                <h:panelGroup id="telaPrincipal" styleClass="tablePrincipal">
                    <h:panelGroup id="page">
                        <rich:panel header="#{FluxoExibicaoFaces.tituloTela}" >
                            <rich:spacer width="500" height="5"/>
                            <h:panelGrid columns="2">

                                <a4j:status id="status">
                                    <f:facet name="start">
                                        <h:graphicImage value="/imagens/dolar2.gif" />
                                    </f:facet>
                                    <f:facet name="stop">
                                        <h:graphicImage value="#{FluxoExibicaoFaces.img}" />
                                    </f:facet>
                                </a4j:status>
                                <rich:messages warnClass="orange" infoClass="green" errorClass="red" fatalClass="red" >
                                    <f:facet name="errorMarker">
                                        <h:graphicImage url="/imagens/erro18.png"/>
                                    </f:facet>
                                    <f:facet name="fatalMarker">
                                        <h:graphicImage url="/imagens/fatal18.png"/>
                                    </f:facet>
                                    <f:facet name="warnMarker">
                                        <h:graphicImage url="/imagens/cap18.png"/>
                                    </f:facet>
                                    <f:facet name="infoMarker">
                                        <h:graphicImage url="/imagens/ok18.png"/>
                                    </f:facet>
                                </rich:messages>
                            </h:panelGrid>
                            <rich:spacer height="10" width="100%"/>
                            <a4j:include ajaxRendered="true" viewId="/principal/inicial.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/principal/pagas.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/principal/pagarcartaocredito.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/provisoes/acumulado.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/provisoes/cartaocredito.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/provisoes/grupogasto.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/provisoes/grupogastodet.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/provisoes/observacao.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/cadastro/contabancaria.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/cadastro/cartaocredito.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/cadastro/cartaocreditodependente.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/cadastro/conjuge.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/cadastro/grupogasto.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/cadastro/gruporeceita.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/cadastro/usuarios.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/cadastro/contas/debito.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/cadastro/contas/receita.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/pessoal/senha.jsp" />
                            <a4j:include ajaxRendered="true" viewId="/pessoal/aviso.jsp" />
                        </rich:panel>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGrid>
        </body>
    </html>
</f:view>
