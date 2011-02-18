/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//<! [CDATA [
function Contar(Campo, idmsg, limite){
    var max = parseInt(limite);
    document.getElementById(idmsg).innerText = max - Campo.length
    if((max - Campo.length)<=0){
        document.getElementById(idmsg).style.color = "red";
    }else{
        document.getElementById(idmsg).style.color = "blue";
    }
}
function Numero(valor, idmsg, msg){
    if(isNaN(valor)){
        document.getElementById(idmsg).innerHTML = msg;
        document.getElementById(idmsg).style.color = "red";
    }else{
        document.getElementById(idmsg).innerHTML = "";
    }
}

function alerta(teste){
    alert(teste);
}

function getRightTop(ref) {
    var position = new Object();
    position.top = 0; //ref.offsetTop;
    position.left =0; // ref.offsetLeft+ref.clientWidth+6;
    return position;
}

function confirme(msg){
    apagar = confirm(msg)//a variavel apagar aguarda um comando ok ou cancelar retornando assim false ou true
    return apagar;
}


function valida_cnpj(cnpj, idmsg) {
    cnpj = cnpj.replace(".", "").replace("-", "").replace("/", "").replace(".", "").replace(".", "");
    //alert(cnpj);
    var numeros, digitos, soma, i, resultado, pos, tamanho, digitos_iguais;
    digitos_iguais = 1;
    if (cnpj.length < 14 && cnpj.length < 15){
        document.getElementById(idmsg).style.color = "red";
        document.getElementById(idmsg).innerText = "CNPJ Invalido";
        return false;
    }
          
    for (i = 0; i < cnpj.length - 1; i++)
        if (cnpj.charAt(i) != cnpj.charAt(i + 1))
        {
            digitos_iguais = 0;
            break;
        }
    if (!digitos_iguais)
    {
        tamanho = cnpj.length - 2
        numeros = cnpj.substring(0,tamanho);
        digitos = cnpj.substring(tamanho);
        soma = 0;
        pos = tamanho - 7;
        for (i = tamanho; i >= 1; i--)
        {
            soma += numeros.charAt(tamanho - i) * pos--;
            if (pos < 2)
                pos = 9;
        }
        resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
        if (resultado != digitos.charAt(0)){
            document.getElementById(idmsg).style.color = "red";
            document.getElementById(idmsg).innerText = "CNPJ Invalido";
            return false;
        }
        tamanho = tamanho + 1;
        numeros = cnpj.substring(0,tamanho);
        soma = 0;
        pos = tamanho - 7;
        for (i = tamanho; i >= 1; i--)
        {
            soma += numeros.charAt(tamanho - i) * pos--;
            if (pos < 2)
                pos = 9;
        }
        resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
        if (resultado != digitos.charAt(1)){
            document.getElementById(idmsg).style.color = "red";
            document.getElementById(idmsg).innerText = "CNPJ Invalido";
            return false;
        }
        document.getElementById(idmsg).innerText = "";
        return true;
    }
    else{
        document.getElementById(idmsg).style.color = "red";
        document.getElementById(idmsg).innerText = "CNPJ Invalido";
        return false;
    }
}

function valida_cpf(cpf, idmsg){
    cpf = cpf.replace(".", "").replace("-", "").replace("/", "").replace(".", "").replace(".", "");
    var numeros, digitos, soma, i, resultado, digitos_iguais;
    digitos_iguais = 1;
    if (cpf.length < 11){
        document.getElementById(idmsg).style.color = "red";
        document.getElementById(idmsg).innerText = "CPF Invalido";
        return false;
    }
    for (i = 0; i < cpf.length - 1; i++)
        if (cpf.charAt(i) != cpf.charAt(i + 1))
        {
            digitos_iguais = 0;
            break;
        }
    if (!digitos_iguais)
    {
        numeros = cpf.substring(0,9);
        digitos = cpf.substring(9);
        soma = 0;
        for (i = 10; i > 1; i--)
            soma += numeros.charAt(10 - i) * i;
        resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
        if (resultado != digitos.charAt(0)){
            document.getElementById(idmsg).style.color = "red";
            document.getElementById(idmsg).innerText = "CPF Invalido";
            return false;
        }
        numeros = cpf.substring(0,10);
        soma = 0;
        for (i = 11; i > 1; i--)
            soma += numeros.charAt(11 - i) * i;
        resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
        if (resultado != digitos.charAt(1)){
            document.getElementById(idmsg).style.color = "red";
            document.getElementById(idmsg).innerText = "CPF Invalido";
            return false;
        }
        document.getElementById(idmsg).innerText = "";
        return true;
    }
    else{
        document.getElementById(idmsg).style.color = "red";
        document.getElementById(idmsg).innerText = "CPF Invalido";
        return false;
    }
}



//]]>