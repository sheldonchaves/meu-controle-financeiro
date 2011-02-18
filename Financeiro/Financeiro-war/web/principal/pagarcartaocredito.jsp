<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.pagarCartao}">
    <h:panelGroup id="allpage">
        <h:panelGroup id="tableContasUser">
            <h:form id="contasTable">
                <a4j:include ajaxRendered="true" viewId="/auxiliar/enxertocontasbancarias.jsp" />
            </h:form>
        </h:panelGroup>
        <rich:spacer width="100%" height="15"/>
        <h:panelGroup id="negocioPage">
            <h:form id="buscarContasCC">
                <h:panelGrid columns="3">
                    <h:outputLabel value="#{texto.cartaoCredito}:" id="cartaoLabel" for="cartaoCredito"/>
                    <h:selectOneMenu value="#{contasCartaoCreditoFaces.cartaoCreditoUnico}" id="cartaoCredito"
                                     required="true" requiredMessage="#{texto.campoObrigatorio}">
                        <f:converter converterId="CartaoCreditoUnicoConverter"/>
                        <f:validator validatorId="CartaoCreditoUnicoValidador"/>
                        <f:selectItems value="#{contasCartaoCreditoFaces.cartoesCredito}"/>
                    </h:selectOneMenu>
                    <rich:message for="cartaoCredito" styleClass="red"/>

                    <h:outputLabel value="#{texto.referenciaFatura}:" id="vencimentoMesLabel" for="vencimentoMes" title="#{texto.titlevencimentoMes}"/>
                    <rich:calendar value="#{contasCartaoCreditoFaces.mesCartao}" datePattern="MMMM/yyyy" locale="#{contasCartaoCreditoFaces.locale}"
                                   enableManualInput="true" id="vencimentoMes" required="true" requiredMessage="#{texto.campoObrigatorio}"
                                   showWeeksBar="false">
                    </rich:calendar>
                    <rich:message for="vencimentoMes" styleClass="red"/>

                    <h:outputText value=""/>
                    <a4j:commandButton value="#{texto.buscar}" id="buttonBuscarContasCartao" reRender="negocioPage" status="status"
                                       actionListener="#{contasCartaoCreditoFaces.atualizarListaContasCartaoCredito}">
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandButton>
                </h:panelGrid>
            </h:form>

            <h:panelGroup id="cartaoOK" rendered="#{contasCartaoCreditoFaces.cartaoCreditoUnico != null}">
                <rich:spacer width="100%" height="20"/>
                <h:form id="tablecontasForm">
                    <h:panelGrid columns="3">
                        <h:outputLabel value="#{texto.debitarDe}:" id="debitarDeLabel" for="debitarDe"/>
                        <rich:comboBox value="#{contasCartaoCreditoFaces.contaDebitar}" id="debitarDe" enableManualInput="false"
                                       defaultLabel="#{texto.selecione}" width="220" listWidth="300"
                                       required="true" requiredMessage="#{texto.campoObrigatorio}">
                            <f:converter converterId="ContaBancariaConverter"/>
                            <f:selectItems value="#{contasCartaoCreditoFaces.contaBancariasItens}"/>
                        </rich:comboBox>
                        <rich:message for="debitarDe" styleClass="red"/>
                        
                        <h:outputText value=""/>
                        <a4j:commandButton value="#{texto.confirmar}" id="buttonConfirmarContasCartao" reRender="negocioPage" status="status"
                                           actionListener="#{contasCartaoCreditoFaces.atualizaPagamento}">
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandButton>
                    </h:panelGrid>

                    <rich:spacer width="100%" height="10"/>
                    
                    <rich:dataTable value="#{contasCartaoCreditoFaces.dataModelContas}" var="conta" align="center" id="tablecontas" rowClasses="linha1,linha2">
                        <f:facet name="header">
                            <h:panelGroup>
                                <h:outputText value="#{texto.contasCartaoCreditoTable}: "/>
                                <h:outputText value="#{contasCartaoCreditoFaces.cartaoCreditoUnico.empresaCartao}"/>
                            </h:panelGroup>
                        </f:facet>
                        <rich:column id="dataVencimento" styleClass="center">
                            <f:facet name="header">
                                <h:outputText value="#{texto.vencimento}"/>
                            </f:facet>
                            <h:outputText value="#{conta.contaDataConta}">
                                <f:convertDateTime pattern="dd/MM/yyyy"/>
                            </h:outputText>
                            <f:facet name="footer">
                                <h:outputText value="#{texto.totalFatura}:" styleClass="totais"/>
                            </f:facet>
                        </rich:column>

                        <rich:column id="contaValor" styleClass="center" sortable="true" sortBy="#{conta.contaValor}">
                            <f:facet name="header">
                                <h:outputText value="#{texto.valor}">
                                </h:outputText>
                            </f:facet>
                            <h:outputText value="#{conta.contaValor}" id="valorinp">
                                <f:convertNumber type="currency" locale="#{contasCartaoCreditoFaces.locale}"/>
                            </h:outputText>
                            <f:facet name="footer">
                                <h:outputText value="#{contasCartaoCreditoFaces.totalFatura}" styleClass="totais">
                                    <f:convertNumber type="currency" locale="#{contasCartaoCreditoFaces.locale}"/>
                                </h:outputText>
                            </f:facet>
                        </rich:column>

                        <rich:column id="contaParcelaAtual" styleClass="center">
                            <f:facet name="header">
                                <h:outputText value="#{texto.parcelaAtual}"/>
                            </f:facet>
                            <h:outputText value="#{conta.contaParcelaAtual}">
                            </h:outputText>
                        </rich:column>

                        <rich:column id="contaParcelaTotal" styleClass="center">
                            <f:facet name="header">
                                <h:outputText value="#{texto.parcelaTotal}"/>
                            </f:facet>
                            <h:outputText value="#{conta.contaParcelaTotal}">
                            </h:outputText>
                            <f:facet name="footer">
                                <h:outputText value="#{texto.totalPagar}:" styleClass="totais"/>
                            </f:facet>
                        </rich:column>

                        <rich:column id="contaStatus" styleClass="center" sortable="true" sortBy="#{conta.contaStatus}">
                            <f:facet name="header">
                                <h:outputText value="#{texto.status}"/>
                            </f:facet>
                            <h:outputText value="#{conta.contaStatus}">
                            </h:outputText>
                            <f:facet name="footer">
                                <h:outputText value="#{contasCartaoCreditoFaces.totalPagar}" styleClass="totais">
                                    <f:convertNumber type="currency" locale="#{contasCartaoCreditoFaces.locale}"/>
                                </h:outputText>
                            </f:facet>
                        </rich:column>

                        <rich:column id="grupoGasto" styleClass="center" sortable="true" sortBy="#{conta.grupoGasto}">
                            <f:facet name="header">
                                <h:outputText value="#{texto.grupo}"/>
                            </f:facet>
                            <h:outputText value="#{conta.grupoGasto}">
                            </h:outputText>
                        </rich:column>

                        <rich:column id="contaObservacao" styleClass="center">
                            <f:facet name="header">
                                <h:outputText value="#{texto.observacao}"/>
                            </f:facet>
                            <h:outputText value="#{conta.observacao}">
                            </h:outputText>
                        </rich:column>

                    </rich:dataTable>
                </h:form>
            </h:panelGroup>
        </h:panelGroup>
        <rich:spacer width="100%" height="15"/>
    </h:panelGroup>
</f:subview>