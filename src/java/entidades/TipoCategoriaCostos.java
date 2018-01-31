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
@Table(name = "tipo_categoria_costos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoCategoriaCostos.findAll", query = "SELECT t FROM TipoCategoriaCostos t")
    , @NamedQuery(name = "TipoCategoriaCostos.findByIdTipoCategoriaCostos", query = "SELECT t FROM TipoCategoriaCostos t WHERE t.idTipoCategoriaCostos = :idTipoCategoriaCostos")
    , @NamedQuery(name = "TipoCategoriaCostos.findByDescripcion", query = "SELECT t FROM TipoCategoriaCostos t WHERE t.descripcion = :descripcion")})
public class TipoCategoriaCostos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_Tipo_Categoria_Costos")
    private Integer idTipoCategoriaCostos;
    @Basic(optional = false)
    @Column(name = "Descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoCategoriaCostos")
    private List<CategoriaCostos> categoriaCostosList;

    public TipoCategoriaCostos() {
    }

    public TipoCategoriaCostos(Integer idTipoCategoriaCostos) {
        this.idTipoCategoriaCostos = idTipoCategoriaCostos;
    }

    public TipoCategoriaCostos(Integer idTipoCategoriaCostos, String descripcion) {
        this.idTipoCategoriaCostos = idTipoCategoriaCostos;
        this.descripcion = descripcion;
    }

    public Integer getIdTipoCategoriaCostos() {
        return idTipoCategoriaCostos;
    }

    public void setIdTipoCategoriaCostos(Integer idTipoCategoriaCostos) {
        this.idTipoCategoriaCostos = idTipoCategoriaCostos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<CategoriaCostos> getCategoriaCostosList() {
        return categoriaCostosList;
    }

    public void setCategoriaCostosList(List<CategoriaCostos> categoriaCostosList) {
        this.categoriaCostosList = categoriaCostosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoCategoriaCostos != null ? idTipoCategoriaCostos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoCategoriaCostos)) {
            return false;
        }
        TipoCategoriaCostos other = (TipoCategoriaCostos) object;
        if ((this.idTipoCategoriaCostos == null && other.idTipoCategoriaCostos != null) || (this.idTipoCategoriaCostos != null && !this.idTipoCategoriaCostos.equals(other.idTipoCategoriaCostos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.TipoCategoriaCostos[ idTipoCategoriaCostos=" + idTipoCategoriaCostos + " ]";
    }
    
}
