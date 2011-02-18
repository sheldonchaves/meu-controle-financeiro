<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="assuntoView" rendered="#{PageIniControl.login}" >
    <h:form>
        <h:panelGrid columns="2">
            <a4j:status id="status">
                <f:facet name="start">
                    <h:graphicImage value="/imagens/dolar2.gif" />
                </f:facet>
                <f:facet name="stop">
                    <h:graphicImage value="/imagens/login.png" />
                </f:facet>
            </a4j:status>
            <h:outputText value="" id="outnullo2"/>
            <!-- Login -->
            <h:outputText value="#{texto.login}:" id="outlogin"/>
            <h:inputText value="#{LoginCT.proprietario.login}" id="inplogin" size="20" />
            <!-- Senha -->
            <h:outputText value="#{texto.senha}:" id="outsenha"/>
            <h:inputSecret value="#{LoginCT.proprietario.password}" id="inpsenha" size="20"/>
            <!-- botão -->
            <h:outputText value="" id="outnullo"/>
            <a4j:commandButton action="#{LoginCT.logar}" value="#{texto.logar}" id="botaologar" >
            </a4j:commandButton>
        </h:panelGrid>
        <%-- Utilizado para inserir dados no banco      
        <rich:spacer width="100%" height="20px"/>
        <a4j:commandButton value="GerarDadosNoBanco" actionListener="#{LoginCT.criaBanco}" />
        --%>
    </h:form>

</f:subview>