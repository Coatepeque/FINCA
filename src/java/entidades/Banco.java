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
@Table(name = "banco")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Banco.findAll", query = "SELECT b FROM Banco b")
    , @NamedQuery(name = "Banco.findByIdBanco", query = "SELECT b FROM Banco b WHERE b.idBanco = :idBanco")
    , @NamedQuery(name = "Banco.findByNombreBanco", query = "SELECT b FROM Banco b WHERE b.nombreBanco = :nombreBanco")
    , @NamedQuery(name = "Banco.findByNoCuenta", query = "SELECT b FROM Banco b WHERE b.noCuenta = :noCuenta")
    , @NamedQuery(name = "Banco.findByTipoCuenta", query = "SELECT b FROM Banco b WHERE b.tipoCuenta = :tipoCuenta")
    , @NamedQuery(name = "Banco.findBySaldo", query = "SELECT b FROM Banco b WHERE b.saldo = :saldo")})
public class Banco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_banco")
    private Integer idBanco;
    @Basic(optional = false)
    @Column(name = "nombre_banco")
    private String nombreBanco;
    @Basic(optional = false)
    @Column(name = "no_cuenta")
    private String noCuenta;
    @Basic(optional = false)
    @Column(name = "tipo_cuenta")
    private String tipoCuenta;
    @Basic(optional = false)
    @Column(name = "saldo")
    private double saldo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bANCOidbanco")
    private List<DetalleCuentaBancaria> detalleCuentaBancariaList;

    public Banco() {
    }

    public Banco(Integer idBanco) {
        this.idBanco = idBanco;
    }

    public Banco(Integer idBanco, String nombreBanco, String noCuenta, String tipoCuenta, double saldo) {
        this.idBanco = idBanco;
        this.nombreBanco = nombreBanco;
        this.noCuenta = noCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldo = saldo;
    }

    public Integer getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Integer idBanco) {
        this.idBanco = idBanco;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public String getNoCuenta() {
        return noCuenta;
    }

    public void setNoCuenta(String noCuenta) {
        this.noCuenta = noCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @XmlTransient
    public List<DetalleCuentaBancaria> getDetalleCuentaBancariaList() {
        return detalleCuentaBancariaList;
    }

    public void setDetalleCuentaBancariaList(List<DetalleCuentaBancaria> detalleCuentaBancariaList) {
        this.detalleCuentaBancariaList = detalleCuentaBancariaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBanco != null ? idBanco.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Banco)) {
            return false;
        }
        Banco other = (Banco) object;
        if ((this.idBanco == null && other.idBanco != null) || (this.idBanco != null && !this.idBanco.equals(other.idBanco))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Banco[ idBanco=" + idBanco + " ]";
    }
    
}
