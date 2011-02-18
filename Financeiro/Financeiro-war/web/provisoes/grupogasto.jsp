<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.provisoesGrupoGasto}">
    <h:panelGroup id="allpage">

        <h:panelGroup id="tableContasUser">
            <h:form id="contasTable">

                <a4j:include ajaxRendered="true" viewId="/auxiliar/enxertocontasbancarias.jsp" />
                <rich:spacer width="100%" height="15"/>

                <h:panelGrid columns="3">
                    <h:outputLabel value="#{texto.intervalo}:" id="intervalolable" for="intervalo"/>
                    <rich:inputNumberSpinner value="#{grupoGastoPagamentosDTOFaces.meses}" minValue="1" maxValue="3" label="intervalolable"
                                             id="intervalo" required="true" requiredMessage="#{texto.campoObrigatorio}"/>
                    <rich:message for="intervalo" styleClass="red"/>

                    <h:outputText value=""/>
                    <a4j:commandButton value="#{texto.atualizar}" reRender="allpage" id="buttonatualizar" actionListener="#{grupoGastoPagamentosDTOFaces.atualizaValoresGrupos}">
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandButton>
                </h:panelGrid>
                <rich:spacer height="15" width="100%"/>
                <h:panelGrid columns="1">
                    <a4j:include ajaxRendered="true" viewId="/auxiliar/grupogastointerface.jsp" >
                    </a4j:include>
                    <rich:spacer height="15" width="100%"/>
                    <h:panelGroup id="graff" styleClass="center">
                        <h:graphicImage value="#{grupoGastoPagamentosDTOFaces.graficoAcumulado}" height="320" width="1050" styleClass="center"/>
                    </h:panelGroup>
                </h:panelGrid>
            </h:form>
        </h:panelGroup>
        <rich:spacer height="15" width="500"/>

    </h:panelGroup>
</f:subview>