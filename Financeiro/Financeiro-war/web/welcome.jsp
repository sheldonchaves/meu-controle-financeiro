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
            <title><h:outputText value="#{texto.loginPageTitle}"/></title>
            <a4j:loadScript src="/js/js.js"/>
        </head>
        <body>

            <h:panelGroup id="logoCoelceGroup" styleClass="logoPrincipalCoelce">
                    <h:graphicImage value="/imagens/lucdiv5.png" id="logoMedium" />
            </h:panelGroup>

            <h:panelGrid columns="2">

                <h:panelGroup id="pageLogin" styleClass="inerpaginalogin">
                    <rich:panel header="#{PageIniControl.tituloTela}" >
                        <rich:spacer width="500" height="5"/>
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
                        <a4j:include ajaxRendered="true" viewId="/login/login.jsp" />
                    </rich:panel>
                </h:panelGroup>
            </h:panelGrid>
        </body>
    </html>
</f:view>
