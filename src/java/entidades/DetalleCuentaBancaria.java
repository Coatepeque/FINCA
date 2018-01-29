/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author josue
 */
@Entity
@Table(name = "detalle_cuenta_bancaria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleCuentaBancaria.findAll", query = "SELECT d FROM DetalleCuentaBancaria d")
    , @NamedQuery(name = "DetalleCuentaBancaria.findByIdDetalleCuenta", query = "SELECT d FROM DetalleCuentaBancaria d WHERE d.idDetalleCuenta = :idDetalleCuenta")
    , @NamedQuery(name = "DetalleCuentaBancaria.findByFecha", query = "SELECT d FROM DetalleCuentaBancaria d WHERE d.fecha = :fecha")
    , @NamedQuery(name = "DetalleCuentaBancaria.findByCantidad", query = "SELECT d FROM DetalleCuentaBancaria d WHERE d.cantidad = :cantidad")
    , @NamedQuery(name = "DetalleCuentaBancaria.findByDescripcion", query = "SELECT d FROM DetalleCuentaBancaria d WHERE d.descripcion = :descripcion")})
public class DetalleCuentaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_detalle_cuenta")
    private Integer idDetalleCuenta;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private double cantidad;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "BANCO_id_banco", referencedColumnName = "id_banco")
    @ManyToOne(optional = false)
    private Banco bANCOidbanco;
    @JoinColumn(name = "EMPLEADO_id_empleado", referencedColumnName = "id_empleado")
    @ManyToOne(optional = false)
    private Empleado eMPLEADOidempleado;

    public DetalleCuentaBancaria() {
    }

    public DetalleCuentaBancaria(Integer idDetalleCuenta) {
        this.idDetalleCuenta = idDetalleCuenta;
    }

    public DetalleCuentaBancaria(Integer idDetalleCuenta, Date fecha, double cantidad, String descripcion) {
        this.idDetalleCuenta = idDetalleCuenta;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
    }

    public Integer getIdDetalleCuenta() {
        return idDetalleCuenta;
    }

    public void setIdDetalleCuenta(Integer idDetalleCuenta) {
        this.idDetalleCuenta = idDetalleCuenta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Banco getBANCOidbanco() {
        return bANCOidbanco;
    }

    public void setBANCOidbanco(Banco bANCOidbanco) {
        this.bANCOidbanco = bANCOidbanco;
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
        hash += (idDetalleCuenta != null ? idDetalleCuenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleCuentaBancaria)) {
            return false;
        }
        DetalleCuentaBancaria other = (DetalleCuentaBancaria) object;
        if ((this.idDetalleCuenta == null && other.idDetalleCuenta != null) || (this.idDetalleCuenta != null && !this.idDetalleCuenta.equals(other.idDetalleCuenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.DetalleCuentaBancaria[ idDetalleCuenta=" + idDetalleCuenta + " ]";
    }
    
}
