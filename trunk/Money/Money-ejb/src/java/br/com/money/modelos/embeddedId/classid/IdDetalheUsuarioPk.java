/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.modelos.embeddedId.classid;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author gbvbahia
 */
@Embeddable
public class IdDetalheUsuarioPk implements Serializable{
    @Column(name = "fk_detalhe_movimentacao_id")
    private Long detalheId;
    @Column(name = "fk_usuario_id")
    private Long usuarioId;

    public IdDetalheUsuarioPk() {
    }

    public IdDetalheUsuarioPk(Long detalheId, Long usuarioId) {
        this.detalheId = detalheId;
        this.usuarioId = usuarioId;
    }

   /**
     * Utiliza a string que é devolvida no toString
     * Utilizado para agilizar a criação de construtores
     * @param toString 
     */
    public IdDetalheUsuarioPk(String toString) {
        this.detalheId = new Long(StringUtils.substringBefore(toString, "-"));
        this.usuarioId = new Long(StringUtils.substringAfter(toString, "-"));
    }

    public Long getDetalheId() {
        return detalheId;
    }

    public void setDetalheId(Long detalheId) {
        this.detalheId = detalheId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * ToString utilizando em um dos construtores
     * não alterar!
     * @return 
     */
    @Override
    public String toString() {
        return detalheId + "-" + usuarioId;
    }
    
    
}
