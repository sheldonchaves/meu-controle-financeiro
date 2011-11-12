/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.utils;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;

/**
 *Classe utilizada para conter métodos de utilidade publica.
 * @author Guilherme
 */
public class UtilBeans {

    /**
     * Retira qualquer tipo de aspas, evitando problemas na persistencia no BD
     * @param String
     * @return Retorna String sem aspas
     */
    public static String limpaString(String string) {
        if (string != null) {
            String assuntotemp = string.replace("\"", "").replace("'", "").replace("“", "").replace("‘", "");
            assuntotemp = assuntotemp.replace((char) 147, ' ').replace((char) 148, ' ').replace((char) 150, ' ');
            return assuntotemp;
        } else {
            return null;
        }
    }

    public static String limpaStringCSV(String string) {
        return string.replace("\n", " ").replace("\r", " ").replace(";", ",").replace("\t", " ");
    }

     /**
     * Código Java de uma classe com os métodos de validação de CNPJ de acordo com as regras da Receita Federal.
     * @param str_cnpj
     * @return retorna verdadeiro (true) para CNPJ válido e falso (false) para CNPJ inválido
     */
    static public boolean CNPJ(String str_cnpj) {
        if (str_cnpj == null) {
            return false;
        }
        if (str_cnpj.length() != 14) {
            return false;
        }
        int soma = 0, aux, dig;
        String cnpj_calc = str_cnpj.substring(0, 12);
        char[] chr_cnpj = str_cnpj.toCharArray();

        /* Primeira parte */
        for (int i = 0; i < 4; i++) {
            if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
            }
        }
        for (int i = 0; i < 8; i++) {
            if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
                soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
            }
        }
        dig = 11 - (soma % 11);

        cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

        /* Segunda parte */
        soma = 0;
        for (int i = 0; i < 5; i++) {
            if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
            }
        }
        for (int i = 0; i < 8; i++) {
            if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
                soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
            }
        }
        dig = 11 - (soma % 11);
        cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

        return str_cnpj.equals(cnpj_calc);
    }

    /**
     * Verifica se a String possui o minimo necessário de letras, a quantidade é definida pelo segundo parametro
     * numeros, espaços e caracteres especiais são ignorados.
     * @param String nome
     * @param int letras
     * @return retorna true se o nome estiver dentro do padrão especificado pelo método.
     */
    public static boolean nomeValido(String nome, int letras) {
        if (nome == null || nome.length() < letras) {
            return false;
        }
        int r = 0;
        for (int i = 0; i < nome.length(); i++) {
            Character c = nome.charAt(i);
            if (Character.isLetter(c)) {
                r++;
            }
        }
        if (r < letras) {
            return false;
        }
        return true;
    }

    /**
     * Passe um nome completo e ele retorna apenas os dois primeiros nomes, caso o nome tenha um segundo nome muito curto, até 3 caracteres,
     * ele retorna o 1º, 2º e 3º nome. Ex: Maria Dalva Pereira Retorna: Maria Dalva, José dos Dias Silveira Retorna: José dos Dias
     * @param String nomeCompleto
     * @return String com os dois primeiros nomes
     */
    public static String doisNomes(String nomeCompleto) {
        int i = 0;
        int b = 0;
        int a = 0;
        String nome = null;
        for (int x = 0; x < nomeCompleto.length(); x++) {
            Character z = nomeCompleto.charAt(x);
            if (Character.isSpaceChar(z)) {
                if (a == 0) {
                    a = x;
                    i++;
                } else {
                    b = x;
                    i++;
                }
                //Caso o 2º Nome seja muito curto
                if (b != 0 && (b - a) <= 4) {
                    for (int j = b + 1; j < nomeCompleto.length(); j++) {
                        Character zz = nomeCompleto.charAt(j);
                        if (Character.isSpaceChar(zz)) {
                            x = j;
                            break;
                        } else if (j == nomeCompleto.length() - 1) {
                            x = j + 1;
                            break;
                        }
                    }
                }
            }
            if (i == 2) {
                nome = nomeCompleto.substring(0, x);
                return nome;
            }
        }
        return nomeCompleto;
    }

    /**
     * Metodo validacaoCPF - Responsavel em validar o CPF
     *
     * @return Boolean
     * @since 29/12/2006
     */
    public static boolean validacaoCPF(String cpf) {
        cpf = cpf.replace(".", "").replace("-", "").replace(" ", "");
        if (cpf.length() != 11) {
            return false;
        }
        int d1, d2;
        int digito1, digito2, resto;
        int digitoCPF;
        String nDigResult;
        d1 = d2 = 0;
        digito1 = digito2 = resto = 0;
        for (int nCount = 1; nCount < cpf.length() - 1; nCount++) {
            digitoCPF = Integer.valueOf(cpf.substring(nCount - 1, nCount)).intValue();
            //multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.
            d1 = d1 + (11 - nCount) * digitoCPF;
            //para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.
            d2 = d2 + (12 - nCount) * digitoCPF;
        }
        //Primeiro resto da divisão por 11.
        resto = (d1 % 11);
        //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.
        if (resto < 2) {
            digito1 = 0;
        } else {
            digito1 = 11 - resto;
        }
        d2 += 2 * digito1;
        //Segundo resto da divisão por 11.
        resto = (d2 % 11);
        //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.
        if (resto < 2) {
            digito2 = 0;
        } else {
            digito2 = 11 - resto;
        }
        //Digito verificador do CPF que está sendo validado.
        String nDigVerific = cpf.substring(cpf.length() - 2, cpf.length());
        //Concatenando o primeiro resto com o segundo.
        nDigResult = String.valueOf(digito1) + String.valueOf(digito2);
        //comparar o digito verificador do cpf com o primeiro resto + o segundo resto.
        return nDigVerific.equals(nDigResult);
    }

    /**
     * Retona a data passada em um string com dd/mm/aaaa
     * @param date
     * @return String dd/MM/aaaa
     */
    public static String getDataString(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Integer dia = c.get(Calendar.DAY_OF_MONTH);
        Integer mes = c.get(Calendar.MONTH) + 1;
        Integer ano = c.get(Calendar.YEAR);
        return dia.toString() + "/" + mes.toString() + "/" + ano.toString();
    }

    public static String currencyFormat(Double n) {
        Locale locale = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        return nf.format(n);
    }

    public static String currencyFormat(Long n) {
        Locale locale = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        return nf.format(n);
    }

      /**
     * Retorna um array de java.util.Date com o primeiro dia do mês
     * e o ultimo dia do mês, com base na data de referência passada
     * @param referencia
     * @return
     */
    public static Date[] primeiroUltimoDia(Date referencia){
        Date[] toReturn = new Date[2];
        Calendar c1 = Calendar.getInstance();
        c1.setTime(referencia);
        c1.set(Calendar.DAY_OF_MONTH, 1);
        c1.set(Calendar.HOUR_OF_DAY, 00);
        c1.set(Calendar.MINUTE, 00);
        toReturn[0] = c1.getTime();

        Calendar c2 = Calendar.getInstance();
        c2.setTime(referencia);
        c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH));
        c2.set(Calendar.HOUR_OF_DAY, 23);
        c2.set(Calendar.MINUTE, 59);
        toReturn[1] = c2.getTime();
        return toReturn;
    }

        /**
     * Retorna uma String que pode ser utilizada como único identificador
     * @param idUser
     * @param dataVencimento
     * @return
     */
    public synchronized static String getIdentificadorUnico(long idUser, Date dataVencimento) {
        if (dataVencimento == null) {
            dataVencimento = new Date();
        }
        UUID uuid = new UUID(dataVencimento.getTime(), new Date().getTime());
        String toReturn = idUser + "-" + uuid.toString();
        toReturn = StringUtils.substring(toReturn, 0, 50);
        return toReturn;
    }
    
    
    public static Date aumentaMesDate(Date dataVencimento, int qtdade) {
        Calendar c = Calendar.getInstance();
        c.setTime(dataVencimento);
        c.add(Calendar.MONTH, qtdade);
        return c.getTime();
    }
}
