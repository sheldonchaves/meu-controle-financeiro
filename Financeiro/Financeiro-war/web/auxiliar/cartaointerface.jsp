<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%-- PARAMETROS
1º titleTable          :: Title da Tabela
2º meses               :: Quantidade de meses selecionados, sem calculos, isso já é realizado
3º titleTableTop       :: Titulo superior da tabela
4º acumuladointerface  :: Lista com objetos que implementam essa interface
5º localizacao         :: Locale com localização monetária
6º tipoValores         :: Lista com tipoValores para popular as linhas utilize LinhasReportHelper para ajudar
 <f:param name="acumuladointerface" value="#{acumulado.acumuladoHelperDataModel}"/>
<f:param name="titleTable" value="#{texto.realizadoXprovisionado}"/>
<f:param name="titleTableTop" value="#{texto.realizadoXprovisionado}"/>
<f:param name="localizacao" value="#{acumulado.locale}"/>
<f:param name="meses" value="#{acumulado.meses}"/>
<f:param name="tipoValores" value="#{acumulado.linhasReportHelper.tipoValores}"/>
--%>
<f:subview id="inicialView" >
    <h:outputText escape="false" value="<table title='"/>
    <h:outputText value="#{texto.realizadoXprovisionado}"/>
    <h:outputText value="' class='rich-table ' align='left'  width='1050'>" escape="false"/>
    <h:outputText escape="false" value="<thead class='rich-table-thead'>"/>
    <h:outputText escape="false" value="<tr class='rich-table-header  '>"/>
    <h:outputText escape="false" value="<th class='rich-table-headercell  ' colspan='"/>
    <h:outputText value="#{cartaoCreditoFaces1.meses * 2 * 2 + 2}"/>
    <h:outputText escape="false" value="'scope='colgroup'>"/>
    <h:outputText value="#{texto.realizadoXprovisionado}"/>
    <h:outputText escape="false" value="</th>"/>
    <h:outputText escape="false" value="</tr>"/>
    <h:outputText escape="false" value="<tr class='rich-table-subheader '>"/>
    <a4j:repeat value="#{cartaoCreditoFaces1.cartaoDataModel}" var="titulo" id="titulolist">
        <h:outputText escape="false" value="<th class='rich-table-subheadercell  ' scope='col' colspan='2'>"/>
        <h:outputText value="#{titulo.mesAno}" escape="false" rendered="#{!titulo.mesAtual}" styleClass="zzz">
            <f:convertDateTime pattern="MMMM/yyyy" locale="#{cartaoCreditoFaces1.locale}"/>
        </h:outputText>
        <h:outputText value="#{titulo.mesAno}" escape="false" rendered="#{titulo.mesAtual}" styleClass="zzy">
            <f:convertDateTime pattern="MMMM/yyyy" locale="#{cartaoCreditoFaces1.locale}"/>
        </h:outputText>
        <h:outputText escape="false" value="</th>"/>
    </a4j:repeat>
    <h:outputText escape="false" value="</tr>"/>
    <h:outputText escape="false" value="</thead>"/>
    <h:outputText escape="false" value="<tbody>"/>
    <a4j:repeat value="#{cartaoCreditoFaces1.linhasReportHelper.tipoValores}" var="linha" id="linhalist">
        <h:outputText escape="false" value="<tr>"/>
        <a4j:repeat value="#{linha}" var="coluna" id="colunalist">
            <h:outputText escape="false" value="<td class='rich-subtable-cell left'>"/>
            <h:outputText value="#{coluna.tipo}" />
            <h:outputText escape="false" value="</td>"/>
            <h:outputText escape="false" value="<td class='rich-subtable-cell right'>"/>
            <h:outputText value="#{coluna.valor}" >
                <f:convertNumber type="currency" locale="#{cartaoCreditoFaces1.locale}"/>
            </h:outputText>
            <h:outputText escape="false" value="</td>"/>
        </a4j:repeat>
        <h:outputText escape="false" value="</tr>"/>
    </a4j:repeat>
    <h:outputText escape="false" value="</tbody>"/>
    <h:outputText escape="false" value="</table>"/>

</f:subview>