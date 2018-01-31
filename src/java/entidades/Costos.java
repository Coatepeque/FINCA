/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "costos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Costos.findAll", query = "SELECT c FROM Costos c")
    , @NamedQuery(name = "Costos.findByIdCostos", query = "SELECT c FROM Costos c WHERE c.idCostos = :idCostos")
    , @NamedQuery(name = "Costos.findByPrecio", query = "SELECT c FROM Costos c WHERE c.precio = :precio")
    , @NamedQuery(name = "Costos.findByCantidad", query = "SELECT c FROM Costos c WHERE c.cantidad = :cantidad")})
public class Costos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_Costos")
    private Integer idCostos;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "Precio")
    private BigDecimal precio;
    @Column(name = "Cantidad")
    private BigDecimal cantidad;
    @JoinColumn(name = "Id_Categoria_Costos", referencedColumnName = "Id_Categoria_Costos")
    @ManyToOne(optional = false)
    private CategoriaCostos idCategoriaCostos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCostos")
    private List<ReporteDiario> reporteDiarioList;

    public Costos() {
    }

    public Costos(Integer idCostos) {
        this.idCostos = idCostos;
    }

    public Costos(Integer idCostos, BigDecimal precio) {
        this.idCostos = idCostos;
        this.precio = precio;
    }

    public Integer getIdCostos() {
        return idCostos;
    }

    public void setIdCostos(Integer idCostos) {
        this.idCostos = idCostos;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public CategoriaCostos getIdCategoriaCostos() {
        return idCategoriaCostos;
    }

    public void setIdCategoriaCostos(CategoriaCostos idCategoriaCostos) {
        this.idCategoriaCostos = idCategoriaCostos;
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
        hash += (idCostos != null ? idCostos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Costos)) {
            return false;
        }
        Costos other = (Costos) object;
        if ((this.idCostos == null && other.idCostos != null) || (this.idCostos != null && !this.idCostos.equals(other.idCostos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Costos[ idCostos=" + idCostos + " ]";
    }
    
}
