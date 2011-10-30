/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//<! [CDATA [
//onkeypress="return(MascaraMoeda(this,'.',',',event))"
function MascaraMoeda(objTextBox, SeparadorMilesimo, SeparadorDecimal, e){
    var sep = 0;
    var key = '';
    var i = j = 0;
    var len = len2 = 0;
    var strCheck = '0123456789';
    var aux = aux2 = '';
    var whichCode = (window.Event) ? e.which : e.keyCode;
    if(whichCode == 13 || whichCode == 8 || whichCode == 0) return true;
    key = String.fromCharCode(whichCode); // Valor para o código da Chave
    if (strCheck.indexOf(key) == -1) return false; // Chave inválida
    len = objTextBox.value.length;
    for(i = 0; i < len; i++)
        if ((objTextBox.value.charAt(i) != '0') && (objTextBox.value.charAt(i) != SeparadorDecimal)) break;
    aux = '';
    for(; i < len; i++)
        if (strCheck.indexOf(objTextBox.value.charAt(i))!=-1) aux += objTextBox.value.charAt(i);
    aux += key;
    len = aux.length;
    if (len == 0) objTextBox.value = '';
    if (len == 1) objTextBox.value = '0'+ SeparadorDecimal + '0' + aux;
    if (len == 2) objTextBox.value = '0'+ SeparadorDecimal + aux;
    if (len > 2) {
        aux2 = '';
        for (j = 0, i = len - 3; i >= 0; i--) {
            if (j == 3) {
                aux2 += SeparadorMilesimo;
                j = 0;
            }
            aux2 += aux.charAt(i);
            j++;
        }
        objTextBox.value = '';
        len2 = aux2.length;
        for (i = len2 - 1; i >= 0; i--)
            objTextBox.value += aux2.charAt(i);
        objTextBox.value += SeparadorDecimal + aux.substr(len - 2, len);
    }
    return false;
}

function verificaEmail(msg){
    var i = document.getElementById("acessoEmbutido:cadUserForm:useremailCad").value;
    var ii = document.getElementById("acessoEmbutido:cadUserForm:useremailCad2").value; 
    if( i != ii){
        document.getElementById(msg).innerText = "***";
    }else{
        document.getElementById(msg).innerText = "";
    }
}

function getInternetExplorer()  
{  
    if (navigator.appName == 'Microsoft Internet Explorer')  
    {  
        alert('Sorry, this site is not compatible with Internet Explorer.\nPlease use Google Chrome or Mozilla FireFox.');
        return false;  
    }  
    return true;  
}  
function confirme(msg){
    apagar = confirm(msg)//a variavel apagar aguarda um comando ok ou cancelar retornando assim false ou true
    return apagar;
}

function infIE(idmsg){
    if(navigator.appName == 'Microsoft Internet Explorer'){
        document.getElementById(idmsg).innerText = "Desculpe, este site não suporta o Internet Explorer.\nPor favor, use o Google Chrome ou Mozilla FireFox";
    }
}

function Contar(Campo, idmsg, limite){
    //alert(document.getElementById(idmsg).id);
    var max = parseInt(limite);
    document.getElementById(idmsg).innerText = max - Campo.length
    if((max - Campo.length)<=0){
        document.getElementById(idmsg).style.color = "red";
    }else{
        document.getElementById(idmsg).style.color = "blue";
    }
}
function formatar(src, mask, e){
    var whichCode = (window.Event) ? e.which : e.keyCode;
    if(whichCode == 13 || whichCode == 8 || whichCode == 0) return true;
    var i = src.value.length;
    var saida = mask.substring(0,1);
    var texto = mask.substring(i)
    if (texto.substring(0,1) != saida)
    {
        src.value += texto.substring(0,1);
    }
}

//onkeypress='return SomenteNumero(event)'
function SomenteNumero(e){
    var tecla=(window.event)?event.keyCode:e.which;   
    if((tecla>47 && tecla<58)) return true;
    else{
        if (tecla==8 || tecla==0) return true;
        else  return false;
    }
}
function MascaraCNPJ(cnpj){
    if(mascaraInteiro(cnpj)==false){
        event.returnValue = false;
    }       
    return formataCampo(cnpj, '00.000.000/0000-00', event);
}

//adiciona mascara de cep
function MascaraCep(cep){
    if(mascaraInteiro(cep)==false){
        event.returnValue = false;
    }       
    return formataCampo(cep, '00.000-000', event);
}

//adiciona mascara de data
function MascaraData(data){
    if(mascaraInteiro(data)==false){
        event.returnValue = false;
    }
    return formataCampo(data, '00/00/0000', event);
}

//adiciona mascara ao telefone
function MascaraTelefone(tel){  
    if(mascaraInteiro(tel)==false){
        event.returnValue = false;
    }       
    return formataCampo(tel, '(00) 0000-0000', event);
}

//adiciona mascara ao CPF
function MascaraCPF(cpf){
    if(mascaraInteiro(cpf)==false){
        event.returnValue = false;
    }       
    return formataCampo(cpf, '000.000.000-00', event);
}

//valida telefone
function ValidaTelefone(tel){
    exp = /\(\d{2}\)\ \d{4}\-\d{4}/
    if(!exp.test(tel.value))
        alert('Numero de Telefone Invalido!');
}

//valida CEP
function ValidaCep(cep){
    exp = /\d{2}\.\d{3}\-\d{3}/
    if(!exp.test(cep.value))
        alert('Numero de Cep Invalido!');               
}

//valida data
function ValidaData(data){
    exp = /\d{2}\/\d{2}\/\d{4}/
    if(!exp.test(data.value)){
        data.value = "";
        alert('Data Invalida!');
        data.select();          
    }
}
//valida o CPF digitado
function ValidarCPF(Objcpf){
    var cpf = Objcpf.value;
    exp = /\.|\-/g
    cpf = cpf.toString().replace( exp, "" ); 
    var digitoDigitado = eval(cpf.charAt(9)+cpf.charAt(10));
    var soma1=0, soma2=0;
    var vlr =11;
        
    for(i=0;i<9;i++){
        soma1+=eval(cpf.charAt(i)*(vlr-1));
        soma2+=eval(cpf.charAt(i)*vlr);
        vlr--;
    }       
    soma1 = (((soma1*10)%11)==10 ? 0:((soma1*10)%11));
    soma2=(((soma2+(2*soma1))*10)%11);
        
    var digitoGerado=(soma1*10)+soma2;
    if(digitoGerado!=digitoDigitado)        
        alert('CPF Invalido!');         
}

//valida numero inteiro com mascara
function mascaraInteiro(){
    if (event.keyCode < 48 || event.keyCode > 57){
        event.returnValue = false;
        return false;
    }
    return true;
}

//valida o CNPJ digitado
function ValidarCNPJ(ObjCnpj){
    var cnpj = ObjCnpj.value;
    var valida = new Array(6,5,4,3,2,9,8,7,6,5,4,3,2);
    var dig1= new Number;
    var dig2= new Number;
        
    exp = /\.|\-|\//g
    cnpj = cnpj.toString().replace( exp, "" ); 
    var digito = new Number(eval(cnpj.charAt(12)+cnpj.charAt(13)));
                
    for(i = 0; i<valida.length; i++){
        dig1 += (i>0? (cnpj.charAt(i-1)*valida[i]):0);  
        dig2 += cnpj.charAt(i)*valida[i];       
    }
    dig1 = (((dig1%11)<2)? 0:(11-(dig1%11)));
    dig2 = (((dig2%11)<2)? 0:(11-(dig2%11)));
        
    if(((dig1*10)+dig2) != digito)  
        alert('CNPJ Invalido!');
                
}

//formata de forma generica os campos
function formataCampo(campo, Mascara, evento) { 
    var boleanoMascara; 
        
    var Digitato = evento.keyCode;
    exp = /\-|\.|\/|\(|\)| /g
    campoSoNumeros = campo.value.toString().replace( exp, "" ); 
   
    var posicaoCampo = 0;    
    var NovoValorCampo="";
    var TamanhoMascara = campoSoNumeros.length;
    ; 
        
    if (Digitato != 8) { // backspace 
        for(i=0; i<= TamanhoMascara; i++) { 
            boleanoMascara  = ((Mascara.charAt(i) == "-") || (Mascara.charAt(i) == ".")
                || (Mascara.charAt(i) == "/")) 
            boleanoMascara  = boleanoMascara || ((Mascara.charAt(i) == "(") 
                || (Mascara.charAt(i) == ")") || (Mascara.charAt(i) == " ")) 
            if (boleanoMascara) { 
                NovoValorCampo += Mascara.charAt(i); 
                TamanhoMascara++;
            }else { 
                NovoValorCampo += campoSoNumeros.charAt(posicaoCampo); 
                posicaoCampo++; 
            }              
        }      
        campo.value = NovoValorCampo;
        return true; 
    }else { 
        return true; 
    }
}
//]]>