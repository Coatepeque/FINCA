/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author josue
 */
@Entity
@Table(name = "tipo_emp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoEmp.findAll", query = "SELECT t FROM TipoEmp t")
    , @NamedQuery(name = "TipoEmp.findByIdTipoEmp", query = "SELECT t FROM TipoEmp t WHERE t.idTipoEmp = :idTipoEmp")
    , @NamedQuery(name = "TipoEmp.findByCodigo", query = "SELECT t FROM TipoEmp t WHERE t.codigo = :codigo")
    , @NamedQuery(name = "TipoEmp.findByDescripcion", query = "SELECT t FROM TipoEmp t WHERE t.descripcion = :descripcion")})
public class TipoEmp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_emp")
    private Integer idTipoEmp;
    @Basic(optional = false)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "EMPLEADO_id_empleado", referencedColumnName = "id_empleado")
    @ManyToOne(optional = false)
    private Empleado eMPLEADOidempleado;

    public TipoEmp() {
    }

    public TipoEmp(Integer idTipoEmp) {
        this.idTipoEmp = idTipoEmp;
    }

    public TipoEmp(Integer idTipoEmp, String codigo, String descripcion) {
        this.idTipoEmp = idTipoEmp;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Integer getIdTipoEmp() {
        return idTipoEmp;
    }

    public void setIdTipoEmp(Integer idTipoEmp) {
        this.idTipoEmp = idTipoEmp;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Empleado getEMPLEADOidempleado() {
        return eMPLEADOidempleado;
    }

    public void setEMPLEADOidempleado(Empleado eMPLEADOidempleado) {
        this.eMPLEADOidempleado = eMPLEADOidempleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoEmp != null ? idTipoEmp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoEmp)) {
            return false;
        }
        TipoEmp other = (TipoEmp) object;
        if ((this.idTipoEmp == null && other.idTipoEmp != null) || (this.idTipoEmp != null && !this.idTipoEmp.equals(other.idTipoEmp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.TipoEmp[ idTipoEmp=" + idTipoEmp + " ]";
    }
    
}
