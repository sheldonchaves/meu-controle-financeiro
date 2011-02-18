<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.cadastroConjuge}">
    <h:panelGroup id="allpage">

        <h:panelGroup id="buscaConjuge" rendered="#{conjugeCadFaces.proprietario.conjugeUser == null}">
            <h:form>
                <h:panelGrid columns="2" styleClass="espacoEsquerda">
                    <!-- Login Conjuge-->
                    <h:outputText value="#{texto.loginConjuge}:" id="outlogin" title="#{texto.titleloginconjuge}"/>
                    <h:inputText value="#{conjugeCadFaces.loginConjuge}" id="inplogin" size="20" title="#{texto.titleloginconjuge}"/>
                    <!-- Senha Conjuge-->
                    <h:outputText value="#{texto.senhaConjuge}:" id="outsenha" title="#{texto.titlesenhaconjuge}"/>
                    <h:inputSecret value="#{conjugeCadFaces.senhaConjuge}" id="inpsenha" size="20" title="#{texto.titlesenhaconjuge}"/>
                    <!-- botão -->
                    <h:outputText value="" id="outnullo"/>
                    <a4j:commandButton actionListener="#{conjugeCadFaces.addConjuge}" value="#{texto.vincular}" id="botaologar" title="#{texto.titlevincularconjuge}"
                                       reRender="allpage">
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandButton>
                </h:panelGrid>
            </h:form>
        </h:panelGroup>

        <h:panelGroup id="desvinculaConjuge" rendered="#{conjugeCadFaces.proprietario.conjugeUser != null}">
            <h:form>
                <h:panelGrid columns="2">
                    <f:facet name="header">
                        <h:outputText value="#{texto.facetConjuge}"/>
                    </f:facet>
                    <h:outputLabel value="#{texto.login}:" for="loginConjuge"/>
                    <h:outputText value="#{conjugeCadFaces.proprietario.conjugeUser.login}" id="loginConjuge"/>

                    <h:outputLabel value="#{texto.nome}:" for="nomeConjuge"/>
                    <h:outputText value="#{conjugeCadFaces.proprietario.conjugeUser.first_name}" id="nomeConjuge"/>

                    <h:outputLabel value="#{texto.sobreNome}:" for="sobrenomeConjuge"/>
                    <h:outputText value="#{conjugeCadFaces.proprietario.conjugeUser.last_name}" id="sobrenomeConjuge"/>

                    <h:outputLabel value="#{texto.email}:" for="emailConjuge"/>
                    <h:outputText value="#{conjugeCadFaces.proprietario.conjugeUser.email}" id="emailConjuge"/>

                    <h:outputText value=""/>
                    <a4j:commandButton value="#{texto.removerConjuge}" actionListener="#{conjugeCadFaces.removerConjuge}"
                                       title="#{texto.titleremoverconjuge}" reRender="allpage">
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandButton>
                </h:panelGrid>
            </h:form>
        </h:panelGroup>

        <rich:spacer height="15" width="100%"/>
    </h:panelGroup>
</f:subview>