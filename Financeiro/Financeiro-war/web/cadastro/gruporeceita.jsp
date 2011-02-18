<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.cadastroGrupoReceita}">
    <h:panelGroup id="allpage">
        <rich:tabPanel switchType="ajax" id="panelGrupoReceita" >
            <rich:tab label="#{texto.cadastro}">
                <h:panelGroup id="tabGasto">
                    <h:form id="formCadGrupoReceita">
                        <h:panelGrid columns="3">
                            <h:outputLabel value="#{texto.receita}:" for="receitainp" id="receitaout"/>
                            <h:inputText value="#{grupoFinanceiroFaces.receita.grupoReceita}" id="receitainp" label="receitaout" required="true"
                                         requiredMessage="#{texto.campoObrigatorio}" size="30" maxlength="255">
                                <f:validateLength maximum="255"/>
                            </h:inputText>
                            <rich:message for="receitainp" styleClass="red"/>

                            <h:outputLabel value="#{texto.status}" for="statusinp" id="statugruposout"/>
                            <h:selectBooleanCheckbox value="#{grupoFinanceiroFaces.receita.status}" id="statusinp" label="statugruposout"/>
                            <rich:message for="statusinp" styleClass="red"/>

                            <h:outputText value="" id="vazio"/>
                            <h:panelGroup id="buttonsGastoGroup">
                                <a4j:commandButton value="#{texto.salvar}" actionListener="#{grupoFinanceiroFaces.salvarGrupoReceita}" reRender="allpage"
                                                   id="buttonSalveReceita">
                                    <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                                </a4j:commandButton>

                                <a4j:commandButton value="#{texto.limpar}" actionListener="#{grupoFinanceiroFaces.clean}" reRender="allpage"
                                                   ajaxSingle="true" id="cleangroup">
                                    <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                                </a4j:commandButton>

                            </h:panelGroup>

                        </h:panelGrid>
                    </h:form>
                </h:panelGroup>
            </rich:tab>
            <%-- DIVISAO --%>
            <rich:tab label="#{texto.bloquearliberar}">
                <h:panelGroup id="tabeditReceita">
                    <h:form id="formEdGrupoReceita">
                        <h:panelGrid columns="3">
                            <h:outputLabel value="#{texto.receitas}:" id="labelreceitas" for="receitasout"/>
                            <h:selectOneListbox size="8" label="labelreceitas" id="receitasout" required="true"
                                                requiredMessage="#{texto.campoObrigatorio}" value="#{grupoFinanceiroFaces.receita}">
                                <f:selectItems value="#{grupoFinanceiroFaces.grupoReceitaItens}"/>
                                <f:converter converterId="GrupoReceitaConverter"/>
                                <a4j:support event="onchange" reRender="tabeditReceita" ajaxSingle="true" status="status"/>
                            </h:selectOneListbox>
                            <rich:message for="receitasout" styleClass="red"/>

                            <h:outputLabel value="#{texto.status}" for="statusinp2" id="statugruposout2"/>
                            <h:selectBooleanCheckbox value="#{grupoFinanceiroFaces.receita.status}" id="statusinp2" label="statugruposout2"/>
                            <rich:message for="statusinp2" styleClass="red"/>

                            <h:outputText value="" id="vazio"/>
                            <a4j:commandButton value="#{texto.salvar}" actionListener="#{grupoFinanceiroFaces.salvarGrupoReceita}" reRender="allpage"
                                               id="buttonAtualizarGasto">
                                <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                            </a4j:commandButton>
                        </h:panelGrid>
                    </h:form>
                </h:panelGroup>
            </rich:tab>
        </rich:tabPanel>

    </h:panelGroup>
</f:subview>