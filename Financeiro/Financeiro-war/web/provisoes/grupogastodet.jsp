<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.provisoesGrupoGastoDetalhe}">
    <h:panelGroup id="allpage">

        <h:panelGroup id="tableContasUser">
            <h:form id="contasTable">

                <a4j:include ajaxRendered="true" viewId="/auxiliar/enxertocontasbancarias.jsp" />
                <rich:spacer width="100%" height="15"/>

                <h:panelGrid columns="3">
                    <h:outputLabel value="#{texto.periodo}:" title="#{texto.titleMesAno}" id="dataLabel" for="dataShoice"/>
                    <rich:calendar id="dataShoice" label="#{texto.data}" datePattern="MMMM/yyyy" value="#{grupoGastoDetalheDTOFaces.mesAno}"
                                   required="true" requiredMessage="#{texto.campoObrigatorio}" locale="#{grupoGastoDetalheDTOFaces.locale}"
                                   enableManualInput="false" showWeeksBar="false"/>
                    <rich:message for="dataShoice" styleClass="red"/>

                    <h:outputLabel value="#{texto.tipo}:" for="tipoinp" id="tipolabel"/>
                    <rich:comboBox value="#{grupoGastoDetalheDTOFaces.grupoGasto}" id="tipoinp" label="tipolabel" enableManualInput="false"
                                   width="150" required="true" requiredMessage="#{texto.campoObrigatorio}" defaultLabel="#{texto.selecione}">
                        <f:converter converterId="GrupoGastoConverter"/>
                        <f:selectItems value="#{grupoGastoDetalheDTOFaces.grupoGastoItens}"/>
                    </rich:comboBox>
                    <rich:message for="tipoinp" styleClass="red"/>

                    <h:outputText value=""/>
                    <a4j:commandButton value="#{texto.atualizar}" reRender="allpage"
                                       actionListener="#{grupoGastoDetalheDTOFaces.buscarContas}">
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandButton>

                </h:panelGrid>
            </h:form>

            <rich:spacer height="15" width="500"/>

            <h:form id="contasTable2">
                <h:panelGrid columns="2">
                    <h:graphicImage id="mais" value="/imagens/mais.png" />
                    <a4j:commandLink value="#{texto.somarTodos}" actionListener="#{grupoGastoDetalheDTOFaces.totalizarAll}"
                                     id="somarTodos" reRender="allpage">
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandLink>
                </h:panelGrid>

                <rich:dataTable value="#{grupoGastoDetalheDTOFaces.contasModel}" var="dc" id="tableDC" align="center" rowClasses="linha1,linha2">
                    <f:facet name="header">
                        <h:outputText value="#{texto.contasGropoGasto} #{grupoGastoDetalheDTOFaces.grupoGasto.grupoGasto}"/>
                    </f:facet>

                    <rich:column id="totalizar" styleClass="center" >
                        <f:facet name="header">
                            <h:outputText value="#{texto.somar}"/>
                        </f:facet>
                        <h:selectBooleanCheckbox value="#{dc.marcado}">
                            <a4j:support actionListener="#{grupoGastoDetalheDTOFaces.totalizar}" reRender="allpage" event="onclick"/>
                        </h:selectBooleanCheckbox>
                        <f:facet name="footer">
                            <h:outputText value="#{texto.total}:" styleClass="totais"/>
                        </f:facet>
                    </rich:column>

                    <rich:column id="valor" styleClass="center">
                        <f:facet name="header">
                            <h:outputText value="#{texto.valor}"/>
                        </f:facet>
                        <h:outputText value="#{dc.valor}">
                            <f:converter converterId="MoneyConverter"/>
                        </h:outputText>
                            <f:facet name="footer">
                            <h:outputText value="#{grupoGastoDetalheDTOFaces.total}" styleClass="totais">
                                <f:convertNumber locale="#{grupoGastoDetalheDTOFaces.locale}" type="currency"/>
                            </h:outputText>
                        </f:facet>
                    </rich:column>

                    <rich:column id="dataVencimento" styleClass="center" >
                        <f:facet name="header">
                            <h:outputText value="#{texto.dataVencimento}"/>
                        </f:facet>
                        <h:outputText value="#{dc.dataVencimento}">
                            <f:convertDateTime pattern="dd/MM/yyyy"/>
                        </h:outputText>
                    </rich:column>

                    <rich:column id="parcelaAtual" styleClass="center" >
                        <f:facet name="header">
                            <h:outputText value="#{texto.parcelaAtual}"/>
                        </f:facet>
                        <h:outputText value="#{dc.parcelaAtual}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="parcelaTotal" styleClass="center" >
                        <f:facet name="header">
                            <h:outputText value="#{texto.parcelaTotal}"/>
                        </f:facet>
                        <h:outputText value="#{dc.parcelaTotal}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="formaPagamento" styleClass="center" >
                        <f:facet name="header">
                            <h:outputText value="#{texto.formaPagamento}"/>
                        </f:facet>
                        <h:outputText value="#{dc.formaPagamento}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="statusPagamento" styleClass="center" >
                        <f:facet name="header">
                            <h:outputText value="#{texto.status}"/>
                        </f:facet>
                        <h:outputText value="#{dc.statusPagamento}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="observacao" styleClass="center" >
                        <f:facet name="header">
                            <h:outputText value="#{texto.observacao}"/>
                        </f:facet>
                        <h:outputText value="#{dc.observacao}">
                        </h:outputText>
                    </rich:column>



                </rich:dataTable>

            </h:form>
        </h:panelGroup>
        <rich:spacer height="15" width="500"/>

    </h:panelGroup>
</f:subview>