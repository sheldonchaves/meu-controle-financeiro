<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.cadastroGrupoGasto}">
    <h:panelGroup id="allpage">
        <rich:tabPanel switchType="ajax" id="panelGrupoGasto" >
            <rich:tab label="#{texto.cadastro}">
                <h:panelGroup id="tabGasto">
                    <h:form id="formCadGrupoGasto">
                        <h:panelGrid columns="3">
                            <h:outputLabel value="#{texto.gasto}:" for="gastoinp" id="gastoout"/>
                            <h:inputText value="#{grupoFinanceiroFaces.gasto.grupoGasto}" id="gastoinp" label="gastoout" required="true"
                                         requiredMessage="#{texto.campoObrigatorio}" size="30" maxlength="255">
                                <f:validateLength maximum="255"/>
                            </h:inputText>
                            <rich:message for="gastoinp" styleClass="red"/>

                            <h:outputLabel value="#{texto.status}" for="statusinp" id="statugruposout"/>
                            <h:selectBooleanCheckbox value="#{grupoFinanceiroFaces.gasto.status}" id="statusinp" label="statugruposout"/>
                            <rich:message for="statusinp" styleClass="red"/>

                            <h:outputText value="" id="vazio"/>
                            <h:panelGroup id="buttonsGastoGroup">
                                <a4j:commandButton value="#{texto.salvar}" actionListener="#{grupoFinanceiroFaces.salvarGrupoGasto}" reRender="allpage"
                                                   id="buttonSalveGasto">
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
                <h:panelGroup id="tabeditGasto">
                    <h:form id="formEdGrupoGasto">
                        <h:panelGrid columns="3">
                            <h:outputLabel value="#{texto.gastos}:" id="labelgastos" for="gastosout"/>
                            <h:selectOneListbox size="8" label="labelgastos" id="gastosout" required="true"
                                                requiredMessage="#{texto.campoObrigatorio}" value="#{grupoFinanceiroFaces.gasto}">
                                <f:selectItems value="#{grupoFinanceiroFaces.grupoGastoItens}"/>
                                <f:converter converterId="GrupoGastoConverter"/>
                                <a4j:support event="onchange" reRender="tabeditGasto" ajaxSingle="true" status="status"/>
                            </h:selectOneListbox>
                            <rich:message for="gastosout" styleClass="red"/>

                            <h:outputLabel value="#{texto.status}" for="statusinp2" id="statugruposout2"/>
                            <h:selectBooleanCheckbox value="#{grupoFinanceiroFaces.gasto.status}" id="statusinp2" label="statugruposout2"/>
                            <rich:message for="statusinp2" styleClass="red"/>

                            <h:outputText value="" id="vazio"/>
                            <a4j:commandButton value="#{texto.salvar}" actionListener="#{grupoFinanceiroFaces.salvarGrupoGasto}" reRender="allpage"
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