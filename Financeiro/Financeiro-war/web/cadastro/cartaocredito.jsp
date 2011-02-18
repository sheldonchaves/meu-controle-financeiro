<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.cadastroCartaoCredito}">
    <h:panelGroup id="allpage">
        <h:form id="formCartaoCad">
            <h:panelGrid columns="3" id="cadCartaoGrid" styleClass="espacoEsquerda">
                <h:outputLabel value="#{texto.status}" for="statusinp" id="statuslabel"/>
                <h:selectBooleanCheckbox value="#{cartaoCreditoFaces.cartaoCreditoUnico.statusCartao}" id="statusinp"/>
                <rich:message for="statusinp" styleClass="red"/>

                <h:outputLabel value="#{texto.numero}" for="numeroinp" id="numerolabel"/>
                <h:inputText value="#{cartaoCreditoFaces.cartaoCreditoUnico.numeroCartao}"
                             label="#{texto.numero}" id="numeroinp"
                             maxlength="8" size="25" required="true" requiredMessage="#{texto.campoObrigatorio}">
                    <f:validateLength maximum="8" minimum="5"/>
                    <f:validator validatorId="CartaoCreditoValidador"/>
                    <f:converter converterId="NumeroCartaoCreditoConversor"/>
                </h:inputText>
                <rich:message for="numeroinp" styleClass="red"/>

                <h:outputLabel value="#{texto.vencimento}" for="vencimentoinp" id="vencimentolabel"/>
                <rich:inputNumberSpinner id="vencimentoinp" label="#{texto.vencimento}" maxValue="31" minValue="1"
                                         enableManualInput="true" value="#{cartaoCreditoFaces.cartaoCreditoUnico.diaVencimento}"
                                         required="true" requiredMessage="#{texto.campoObrigatorio}"/>
                <rich:message for="vencimentoinp" styleClass="red"/>

                <h:outputLabel value="#{texto.fechamento}" for="fechamentoinp" id="fechamentolabel"/>
                <rich:inputNumberSpinner id="fechamentoinp" label="#{texto.fechamento}" maxValue="31" minValue="1"
                                         enableManualInput="true" value="#{cartaoCreditoFaces.cartaoCreditoUnico.diaMesmoMes}"
                                         required="true" requiredMessage="#{texto.campoObrigatorio}"/>
                <rich:message for="fechamentoinp" styleClass="red"/>

                <h:outputLabel value="#{texto.empresa}" for="empresainp" id="empresalabel"/>
                <h:inputText value="#{cartaoCreditoFaces.cartaoCreditoUnico.empresaCartao}"
                             label="#{texto.empresa}" id="empresainp"
                             maxlength="100" size="25" required="true" requiredMessage="#{texto.campoObrigatorio}">
                    <f:validateLength maximum="100" minimum="5"/>
                </h:inputText>
                <rich:message for="empresainp" styleClass="red"/>

                <h:outputText value=""/>
                <a4j:commandButton value="#{texto.salvar}" actionListener="#{cartaoCreditoFaces.salvaCartaoCredito}" reRender="allpage"
                                   id="buttonsave" status="status">
                    <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                </a4j:commandButton>
            </h:panelGrid>
        </h:form>
        <rich:spacer height="15" width="100%"/>

        <h:panelGroup id="tableCartoesGroup">
            <h:form id="tableCartoesForm">
                <rich:dataTable value="#{cartaoCreditoFaces.dataModel}" id="cartoesTable" var="cartao" align="center">
                    <f:facet name="header">
                        <h:outputText value="#{texto.cartoesTable}"/>
                    </f:facet>

                    <rich:column id="empresa" styleClass="center" sortable="true" sortBy="#{cartao.empresaCartao}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.empresa}"/>
                        </f:facet>
                        <h:outputText value="#{cartao.empresaCartao}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="numeroCartao" styleClass="center" sortable="true" sortBy="#{cartao.numeroCartao}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.numero}"/>
                        </f:facet>
                        <h:outputText value="#{cartao.numeroCartao}">
                            <f:converter converterId="NumeroCartaoCreditoConversor"/>
                        </h:outputText>
                    </rich:column>

                    <rich:column id="statusCartao" styleClass="center" sortable="true" sortBy="#{cartao.statusCartao}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.status}"/>
                        </f:facet>
                        <h:outputText value="#{texto.cancelado}" rendered="#{!cartao.statusCartao}" styleClass="red"/>
                        <h:outputText value="#{texto.ativo}" rendered="#{cartao.statusCartao}" styleClass="blue"/>
                    </rich:column>

                    <rich:column id="diaVencimento" styleClass="center" sortable="true" sortBy="#{cartao.proximoVencimento}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.dataVencimento}"/>
                        </f:facet>
                        <h:outputText value="#{cartao.proximoVencimento}">
                            <f:convertDateTime pattern="dd/MM/yyyy"/>
                        </h:outputText>
                    </rich:column>

                    <rich:column id="diaMesmoMes" styleClass="center" sortable="true" sortBy="#{cartao.diaMesmoMes}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.diaMesmoMes}"/>
                        </f:facet>
                        <h:outputText value="#{cartao.diaMesmoMes}">
                        </h:outputText>
                    </rich:column>

                     <rich:column id="titularDependete" styleClass="center">
                        <f:facet name="header">
                            <h:outputText value="#{texto.tipo}"/>
                        </f:facet>
                         <h:graphicImage value="/imagens/cc47.png" id="titularImg" rendered="#{cartao.cartaoCreditoTitular == null}" width="35" height="35"/>
                        <h:graphicImage value="/imagens/cc47_dep.png" id="depImg" rendered="#{cartao.cartaoCreditoTitular != null}" width="35" height="35"/>
                    </rich:column>

                    <rich:column id="editarCartao" styleClass="center">
                        <f:facet name="header">
                            <h:outputText value="#{texto.editar}"/>
                        </f:facet>
                        <a4j:commandButton image="/imagens/edit.png" actionListener="#{cartaoCreditoFaces.pegarCartao}" reRender="allpage"
                                           status="status" id="buttoneditcartao"rendered="#{cartao.cartaoCreditoTitular == null}" >
                            <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                        </a4j:commandButton>
                    </rich:column>
                </rich:dataTable>
            </h:form>
        </h:panelGroup>
    </h:panelGroup>
</f:subview>