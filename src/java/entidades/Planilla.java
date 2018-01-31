/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "planilla")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Planilla.findAll", query = "SELECT p FROM Planilla p")
    , @NamedQuery(name = "Planilla.findByIdPlanilla", query = "SELECT p FROM Planilla p WHERE p.idPlanilla = :idPlanilla")
    , @NamedQuery(name = "Planilla.findBySalarioBase", query = "SELECT p FROM Planilla p WHERE p.salarioBase = :salarioBase")
    , @NamedQuery(name = "Planilla.findByDescuentos", query = "SELECT p FROM Planilla p WHERE p.descuentos = :descuentos")
    , @NamedQuery(name = "Planilla.findByIgss", query = "SELECT p FROM Planilla p WHERE p.igss = :igss")
    , @NamedQuery(name = "Planilla.findByBonificaci\u00f3n", query = "SELECT p FROM Planilla p WHERE p.bonificaci\u00f3n = :bonificaci\u00f3n")
    , @NamedQuery(name = "Planilla.findBySalarioLiquido", query = "SELECT p FROM Planilla p WHERE p.salarioLiquido = :salarioLiquido")})
public class Planilla implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_Planilla")
    private Integer idPlanilla;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "Salario_Base")
    private BigDecimal salarioBase;
    @Column(name = "Descuentos")
    private BigDecimal descuentos;
    @Column(name = "IGSS")
    private BigDecimal igss;
    @Column(name = "Bonificaci\u00f3n")
    private BigDecimal bonificación;
    @Basic(optional = false)
    @Column(name = "Salario_Liquido")
    private BigDecimal salarioLiquido;
    @JoinColumn(name = "Id_Empleado", referencedColumnName = "Id_Empleado")
    @ManyToOne(optional = false)
    private Empleado idEmpleado;

    public Planilla() {
    }

    public Planilla(Integer idPlanilla) {
        this.idPlanilla = idPlanilla;
    }

    public Planilla(Integer idPlanilla, BigDecimal salarioBase, BigDecimal salarioLiquido) {
        this.idPlanilla = idPlanilla;
        this.salarioBase = salarioBase;
        this.salarioLiquido = salarioLiquido;
    }

    public Integer getIdPlanilla() {
        return idPlanilla;
    }

    public void setIdPlanilla(Integer idPlanilla) {
        this.idPlanilla = idPlanilla;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(BigDecimal salarioBase) {
        this.salarioBase = salarioBase;
    }

    public BigDecimal getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(BigDecimal descuentos) {
        this.descuentos = descuentos;
    }

    public BigDecimal getIgss() {
        return igss;
    }

    public void setIgss(BigDecimal igss) {
        this.igss = igss;
    }

    public BigDecimal getBonificación() {
        return bonificación;
    }

    public void setBonificación(BigDecimal bonificación) {
        this.bonificación = bonificación;
    }

    public BigDecimal getSalarioLiquido() {
        return salarioLiquido;
    }

    public void setSalarioLiquido(BigDecimal salarioLiquido) {
        this.salarioLiquido = salarioLiquido;
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlanilla != null ? idPlanilla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Planilla)) {
            return false;
        }
        Planilla other = (Planilla) object;
        if ((this.idPlanilla == null && other.idPlanilla != null) || (this.idPlanilla != null && !this.idPlanilla.equals(other.idPlanilla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Planilla[ idPlanilla=" + idPlanilla + " ]";
    }
    
}
