<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.cadastroUsuarios}">
    <h:panelGroup id="allpage">
        <h:form id="formUserCad">
            <h:panelGrid columns="3" id="cadUserGrid" styleClass="espacoEsquerda">
                <h:outputLabel value="#{texto.nome}" for="nomeinp" id="nomelabel"/>
                <h:inputText value="#{usuarioFaces.proprietario.first_name}" id="nomeinp"
                             label="nomelabel"maxlength="15" size="28" required="true"
                             requiredMessage="#{texto.campoObrigatorio}">
                             <f:validateLength maximum="15"/>
                </h:inputText>
                <rich:message for="nomeinp" styleClass="red"/>

                <h:outputLabel value="#{texto.sobreNome}" for="sobreNomeinp" id="sobreNomelabel"/>
                <h:inputText value="#{usuarioFaces.proprietario.last_name}" id="sobreNomeinp"
                             label="sobreNomelabel"maxlength="20" size="28" required="true"
                             requiredMessage="#{texto.campoObrigatorio}">
                             <f:validateLength maximum="20"/>
                </h:inputText>
                <rich:message for="sobreNomeinp" styleClass="red"/>

                <h:outputLabel value="#{texto.login}" for="logininp" id="loginlabel"/>
                <h:inputText value="#{usuarioFaces.proprietario.login}" id="logininp"
                             label="loginlabel"maxlength="10" size="28" required="true"
                             requiredMessage="#{texto.campoObrigatorio}">
                             <f:validateLength maximum="10"/>
                </h:inputText>
                <rich:message for="logininp" styleClass="red"/>

                <h:outputLabel value="#{texto.email}" for="passwordinp" id="passwordlabel"/>
                <h:inputText value="#{usuarioFaces.proprietario.email}" id="passwordinp"
                             label="passwordlabel"maxlength="100" size="28" required="true"
                             requiredMessage="#{texto.campoObrigatorio}">
                             <f:validateLength maximum="100"/>
                </h:inputText>
                <rich:message for="passwordinp" styleClass="red"/>

                <h:outputLabel value="#{texto.grupo}" for="grupsPickList" id="piklabel"/>
                <rich:pickList value="#{usuarioFaces.grups}" id="grupsPickList" label="piklabel" sourceListWidth="50" listsHeight="85"
                               required="true" requiredMessage="#{texto.campoObrigatorio}" targetListWidth="50" switchByClick="true"
                               showButtonsLabel="false">
                    <f:selectItems value="#{usuarioFaces.roles}" id="roleList"/>
                    <f:converter converterId="GrupsConverter"/>
                </rich:pickList>
                <rich:message for="grupsPickList" styleClass="red"/>

                <h:outputText value=""/>
                <h:panelGroup id="buttonsGroup">
                <a4j:commandButton value="#{texto.salvar}" actionListener="#{usuarioFaces.salvaUser}" reRender="allpage"
                                   id="buttonsave" status="status">
                    <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                </a4j:commandButton>
                <rich:spacer height="1" width="3"/>
                    <a4j:commandButton value="#{texto.limpar}" actionListener="#{usuarioFaces.clean}" reRender="allpage"
                                       id="buttonclean" status="status" ajaxSingle="true">
                    <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                </a4j:commandButton>
                </h:panelGroup>
            </h:panelGrid>
        </h:form>
        <rich:spacer height="15" width="100%"/>

        <h:panelGroup id="tableUserGroup">
            <h:form id="tableUserForm">
                <rich:dataTable value="#{usuarioFaces.dataModel}" id="userTable" var="user" align="center">
                    <f:facet name="header">
                        <h:outputText value="#{texto.userTable}"/>
                    </f:facet>

                    <rich:column id="first_name" styleClass="center" sortable="true" sortBy="#{user.first_name}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.nome}"/>
                        </f:facet>
                        <h:outputText value="#{user.first_name}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="last_name" styleClass="center" sortable="true" sortBy="#{user.last_name}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.sobreNome}"/>
                        </f:facet>
                        <h:outputText value="#{user.last_name}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="login" styleClass="center" sortable="true" sortBy="#{user.login}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.login}"/>
                        </f:facet>
                        <h:outputText value="#{user.login}"/>
                    </rich:column>

                    <rich:column id="email" styleClass="center" sortable="true" sortBy="#{user.email}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.email}"/>
                        </f:facet>
                        <h:outputText value="#{user.email}"/>
                    </rich:column>

                    <rich:column id="editarUser" styleClass="center">
                        <f:facet name="header">
                            <h:outputText value="#{texto.editar}"/>
                        </f:facet>
                        <a4j:commandButton image="/imagens/edit.png" actionListener="#{usuarioFaces.pegaUser}" reRender="allpage"
                                           status="status" id="buttonedituser" >
                            <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                        </a4j:commandButton>
                    </rich:column>
                    <rich:column id="senhaUser" styleClass="center">
                        <f:facet name="header">
                            <h:outputText value="#{texto.senhaReset}"/>
                        </f:facet>
                        <a4j:commandButton image="/imagens/login27.png" actionListener="#{usuarioFaces.resetarSenha}" reRender="allpage"
                                           status="status" id="buttonsenhatuser" >
                            <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                        </a4j:commandButton>
                    </rich:column>
                </rich:dataTable>
            </h:form>
        </h:panelGroup>
    </h:panelGroup>
</f:subview>