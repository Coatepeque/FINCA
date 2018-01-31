/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author josue
 */
@Entity
@Table(name = "categoria_costos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CategoriaCostos.findAll", query = "SELECT c FROM CategoriaCostos c")
    , @NamedQuery(name = "CategoriaCostos.findByIdCategoriaCostos", query = "SELECT c FROM CategoriaCostos c WHERE c.idCategoriaCostos = :idCategoriaCostos")
    , @NamedQuery(name = "CategoriaCostos.findByDescripcion", query = "SELECT c FROM CategoriaCostos c WHERE c.descripcion = :descripcion")})
public class CategoriaCostos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_Categoria_Costos")
    private Integer idCategoriaCostos;
    @Basic(optional = false)
    @Column(name = "Descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCategoriaCostos")
    private List<Costos> costosList;
    @JoinColumn(name = "Id_Tipo_Categoria_Costos", referencedColumnName = "Id_Tipo_Categoria_Costos")
    @ManyToOne(optional = false)
    private TipoCategoriaCostos idTipoCategoriaCostos;

    public CategoriaCostos() {
    }

    public CategoriaCostos(Integer idCategoriaCostos) {
        this.idCategoriaCostos = idCategoriaCostos;
    }

    public CategoriaCostos(Integer idCategoriaCostos, String descripcion) {
        this.idCategoriaCostos = idCategoriaCostos;
        this.descripcion = descripcion;
    }

    public Integer getIdCategoriaCostos() {
        return idCategoriaCostos;
    }

    public void setIdCategoriaCostos(Integer idCategoriaCostos) {
        this.idCategoriaCostos = idCategoriaCostos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Costos> getCostosList() {
        return costosList;
    }

    public void setCostosList(List<Costos> costosList) {
        this.costosList = costosList;
    }

    public TipoCategoriaCostos getIdTipoCategoriaCostos() {
        return idTipoCategoriaCostos;
    }

    public void setIdTipoCategoriaCostos(TipoCategoriaCostos idTipoCategoriaCostos) {
        this.idTipoCategoriaCostos = idTipoCategoriaCostos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCategoriaCostos != null ? idCategoriaCostos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoriaCostos)) {
            return false;
        }
        CategoriaCostos other = (CategoriaCostos) object;
        if ((this.idCategoriaCostos == null && other.idCategoriaCostos != null) || (this.idCategoriaCostos != null && !this.idCategoriaCostos.equals(other.idCategoriaCostos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.CategoriaCostos[ idCategoriaCostos=" + idCategoriaCostos + " ]";
    }
    
}
