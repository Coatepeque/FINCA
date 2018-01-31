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
@Table(name = "reporte_diario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReporteDiario.findAll", query = "SELECT r FROM ReporteDiario r")
    , @NamedQuery(name = "ReporteDiario.findByIdReporteDiario", query = "SELECT r FROM ReporteDiario r WHERE r.idReporteDiario = :idReporteDiario")
    , @NamedQuery(name = "ReporteDiario.findByDescripcion", query = "SELECT r FROM ReporteDiario r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "ReporteDiario.findByFecha", query = "SELECT r FROM ReporteDiario r WHERE r.fecha = :fecha")
    , @NamedQuery(name = "ReporteDiario.findByHorainicio", query = "SELECT r FROM ReporteDiario r WHERE r.horainicio = :horainicio")
    , @NamedQuery(name = "ReporteDiario.findByHorafinal", query = "SELECT r FROM ReporteDiario r WHERE r.horafinal = :horafinal")})
public class ReporteDiario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_Reporte_Diario")
    private Integer idReporteDiario;
    @Column(name = "Descripcion")
    private String descripcion;
    @Column(name = "Fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "Hora_inicio")
    @Temporal(TemporalType.TIME)
    private Date horainicio;
    @Column(name = "Hora_final")
    @Temporal(TemporalType.TIME)
    private Date horafinal;
    @JoinColumn(name = "Id_Costos", referencedColumnName = "Id_Costos")
    @ManyToOne(optional = false)
    private Costos idCostos;
    @JoinColumn(name = "Id_Empleado", referencedColumnName = "Id_Empleado")
    @ManyToOne(optional = false)
    private Empleado idEmpleado;
    @JoinColumn(name = "Id_Sub_Actividad", referencedColumnName = "Id_Sub_Actividad")
    @ManyToOne(optional = false)
    private SubActividad idSubActividad;

    public ReporteDiario() {
    }

    public ReporteDiario(Integer idReporteDiario) {
        this.idReporteDiario = idReporteDiario;
    }

    public ReporteDiario(Integer idReporteDiario, Date horainicio) {
        this.idReporteDiario = idReporteDiario;
        this.horainicio = horainicio;
    }

    public Integer getIdReporteDiario() {
        return idReporteDiario;
    }

    public void setIdReporteDiario(Integer idReporteDiario) {
        this.idReporteDiario = idReporteDiario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(Date horainicio) {
        this.horainicio = horainicio;
    }

    public Date getHorafinal() {
        return horafinal;
    }

    public void setHorafinal(Date horafinal) {
        this.horafinal = horafinal;
    }

    public Costos getIdCostos() {
        return idCostos;
    }

    public void setIdCostos(Costos idCostos) {
        this.idCostos = idCostos;
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public SubActividad getIdSubActividad() {
        return idSubActividad;
    }

    public void setIdSubActividad(SubActividad idSubActividad) {
        this.idSubActividad = idSubActividad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReporteDiario != null ? idReporteDiario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReporteDiario)) {
            return false;
        }
        ReporteDiario other = (ReporteDiario) object;
        if ((this.idReporteDiario == null && other.idReporteDiario != null) || (this.idReporteDiario != null && !this.idReporteDiario.equals(other.idReporteDiario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.ReporteDiario[ idReporteDiario=" + idReporteDiario + " ]";
    }
    
}
