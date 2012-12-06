/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.utils;

import java.io.*;
import java.util.Enumeration;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * http://www.softblue.com.br/blog/home/postid/2/ZIP/UNZIP+EM+JAVA
 *
 * Java possui suporte a arquivos ZIP, logo não é necessário utilizar nenhuma API extra. As classes estão localizadas dentro do pacote java.util.zip. As classes
 * principais desse pacote são: ZipEntry, ZipFile, ZipInputStream e ZipOutputStream. Para demonstrar como a compactação e descompactação funcionam, vou mostrar a criação de uma
 * classe com dois métodos: zip() e unzip(). Todo o "trabalho sujo" fica por conta da classe. O código-fonte pode ser visualizado no final desta explicação. Vamos começar pelo
 * método zip(). O método zip() recebe dois parâmetros: o primeiro é a lista de arquivos a serem zipados; e o segundo é o arquivo ZIP que será gerado. O código deste método é
 * bastante simples porque toda a lógica de compactação se encontra no método zipFiles(). O método zipFiles() é reponsável por iterar sobre a lista de arquivos e adicioná-los ao
 * arquivo ZIP de saída. Esta tarefa seria simples, mas às vezes queremos compactar não apenas arquivos, mas também estruturas de diretórios dentro do nosso arquivo ZIP. E para
 * manter essa estrutura de diretórios de forma correta, devemos programar este comportamento manualmente. Observe que, caso uma das entradas que deve aparecer no arquivo ZIP seja
 * um diretório, o método zipFiles() é chamado recursivamente, passando como parâmetro a lista de arquivos do diretório. Esta abordagem possibilita que o método processe todos os
 * arquivos de cada diretório, de uma forma semelhante à busca em profundidade que aprendemos nas aulas de Estruturas de Dados. Junto com a lista de arquivos, também é fornecida
 * uma pilha com os nomes dos diretórios onde o arquivo se encontra. Essa informação é utilizada na reconstrução do caminho do arquivo dentro do arquivo ZIP. Já para descompactar,
 * temos o método unzip(). O método unzip() recebe dois parâmetros: o arquivo ZIP a ser descompactado e um diretório para a descompactação. No caso da descompactação é feito o
 * caminho inverso da compactação. Cada entrada do arquivo ZIP é lida e gravada no sistema de arquivos. Caso a entrada seja um diretório, a estrutura de diretórios deve
 * primeiramente ser criada e só então o arquivo deve ser descompactado (o Java não cria os diretórios automaticamente, ficando a cargo do programador garantir a criação dos
 * diretórios necessários). Esses dois métodos representam uma solução básica e bem completa para compactar e descompactar arquivos. Recomendo que você inclua esta funcionalidade
 * em um componente, a fim de que possa ser usado em diferentes projetos.
 * @since 2012/02/25
 * @author Guilherme Braga
 */
class ZipHelper {
/**
 * O método zip() recebe dois parâmetros: <br>
 * O primeiro é a lista de arquivos a serem zipados; <br> 
 * O segundo é o nome do arquivo ZIP que será gerado.
 * Reorna o arquivo(s) zipado(s)
 * @param files
 * @param outputFile
 * @return java.io.File
 * @throws IOException 
 */
    public File zip(File[] files, File dir, String nameToZip) throws IOException {
        File outputFile = new File(dir, nameToZip + ".zip");
        if (files != null && files.length > 0) {
            ZipOutputStream out = new ZipOutputStream(
                    new FileOutputStream(outputFile));
            Stack<File> parentDirs = new Stack<File>();
            zipFiles(parentDirs, files, out);
            out.close();
        }
        return outputFile;
    }

    private void zipFiles(Stack<File> parentDirs, File[] files,
            ZipOutputStream out) throws IOException {
        byte[] buf = new byte[1024];

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                //se a entrad é um diretório, empilha o diretório e
                //chama o mesmo método recursivamente
                parentDirs.push(files[i]);
                zipFiles(parentDirs, files[i].listFiles(), out);
                //após processar as entradas do diretório, desempilha
                parentDirs.pop();
            } else {
                FileInputStream in = new FileInputStream(files[i]);
                //itera sobre os itens da pilha para montar o caminho
                //completo do arquivo
                String path = "";
                for (File parentDir : parentDirs) {
                    path += parentDir.getName() + "/";
                }
                //grava os dados no arquivo zip
                out.putNextEntry(new ZipEntry(path + files[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
        }
    }

     public void unzip(File zipFile, File dir) throws IOException {
        ZipFile zip = null;
        File arquivo = null;
        InputStream is = null;
        OutputStream os = null;
        byte[] buffer = new byte[1024];
        try {
            // cria diretório informado, caso não exista
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!dir.exists() || !dir.isDirectory()) {
                throw new IOException("O diretório " + dir.getName() +
                        " não é um diretório válido");
            }
            zip = new ZipFile(zipFile);
            Enumeration e = zip.entries();
            while (e.hasMoreElements()) {
                ZipEntry entrada = (ZipEntry) e.nextElement();
                arquivo = new File(dir, entrada.getName());
                // se for diretório inexistente, cria a estrutura e pula
                // pra próxima entrada
                if (entrada.isDirectory() && !arquivo.exists()) {
                    arquivo.mkdirs();
                    continue;
                }
                // se a estrutura de diretórios não existe, cria
                if (!arquivo.getParentFile().exists()) {
                    arquivo.getParentFile().mkdirs();
                }
                try {
                    // lê o arquivo do zip e grava em disco
                    is = zip.getInputStream(entrada);
                    os = new FileOutputStream(arquivo);
                    int bytesLidos = 0;
                    if (is == null) {
                        throw new ZipException("Erro ao ler a entrada do zip: " +
                                entrada.getName());
                    }
                    while ((bytesLidos = is.read(buffer)) > 0) {
                        os.write(buffer, 0, bytesLidos);
                    }
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Exception ex) {
                        }
                    }
                    if (os != null) {
                        try {
                            os.close();
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
