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
@Table(name = "sub_actividad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubActividad.findAll", query = "SELECT s FROM SubActividad s")
    , @NamedQuery(name = "SubActividad.findByIdSubActividad", query = "SELECT s FROM SubActividad s WHERE s.idSubActividad = :idSubActividad")
    , @NamedQuery(name = "SubActividad.findByCodigo", query = "SELECT s FROM SubActividad s WHERE s.codigo = :codigo")
    , @NamedQuery(name = "SubActividad.findByDescripcion", query = "SELECT s FROM SubActividad s WHERE s.descripcion = :descripcion")})
public class SubActividad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_Sub_Actividad")
    private Integer idSubActividad;
    @Basic(optional = false)
    @Column(name = "Codigo")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "Descripcion")
    private String descripcion;
    @JoinColumn(name = "Id_Actividad", referencedColumnName = "Id_Actividad")
    @ManyToOne(optional = false)
    private Actividad idActividad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSubActividad")
    private List<ReporteDiario> reporteDiarioList;

    public SubActividad() {
    }

    public SubActividad(Integer idSubActividad) {
        this.idSubActividad = idSubActividad;
    }

    public SubActividad(Integer idSubActividad, String codigo, String descripcion) {
        this.idSubActividad = idSubActividad;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Integer getIdSubActividad() {
        return idSubActividad;
    }

    public void setIdSubActividad(Integer idSubActividad) {
        this.idSubActividad = idSubActividad;
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

    public Actividad getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(Actividad idActividad) {
        this.idActividad = idActividad;
    }

    @XmlTransient
    public List<ReporteDiario> getReporteDiarioList() {
        return reporteDiarioList;
    }

    public void setReporteDiarioList(List<ReporteDiario> reporteDiarioList) {
        this.reporteDiarioList = reporteDiarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSubActividad != null ? idSubActividad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubActividad)) {
            return false;
        }
        SubActividad other = (SubActividad) object;
        if ((this.idSubActividad == null && other.idSubActividad != null) || (this.idSubActividad != null && !this.idSubActividad.equals(other.idSubActividad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return codigo;
    }
    
}
