/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Costos;
import entidades.Empleado;
import entidades.ReporteDiario;
import entidades.SubActividad;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author josue
 */
public class ReporteDiarioJpaController implements Serializable {

    public ReporteDiarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ReporteDiario reporteDiario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Costos idCostos = reporteDiario.getIdCostos();
            if (idCostos != null) {
                idCostos = em.getReference(idCostos.getClass(), idCostos.getIdCostos());
                reporteDiario.setIdCostos(idCostos);
            }
            Empleado idEmpleado = reporteDiario.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                reporteDiario.setIdEmpleado(idEmpleado);
            }
            SubActividad idSubActividad = reporteDiario.getIdSubActividad();
            if (idSubActividad != null) {
                idSubActividad = em.getReference(idSubActividad.getClass(), idSubActividad.getIdSubActividad());
                reporteDiario.setIdSubActividad(idSubActividad);
            }
            em.persist(reporteDiario);
            if (idCostos != null) {
                idCostos.getReporteDiarioList().add(reporteDiario);
                idCostos = em.merge(idCostos);
            }
            if (idEmpleado != null) {
                idEmpleado.getReporteDiarioList().add(reporteDiario);
                idEmpleado = em.merge(idEmpleado);
            }
            if (idSubActividad != null) {
                idSubActividad.getReporteDiarioList().add(reporteDiario);
                idSubActividad = em.merge(idSubActividad);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ReporteDiario reporteDiario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ReporteDiario persistentReporteDiario = em.find(ReporteDiario.class, reporteDiario.getIdReporteDiario());
            Costos idCostosOld = persistentReporteDiario.getIdCostos();
            Costos idCostosNew = reporteDiario.getIdCostos();
            Empleado idEmpleadoOld = persistentReporteDiario.getIdEmpleado();
            Empleado idEmpleadoNew = reporteDiario.getIdEmpleado();
            SubActividad idSubActividadOld = persistentReporteDiario.getIdSubActividad();
            SubActividad idSubActividadNew = reporteDiario.getIdSubActividad();
            if (idCostosNew != null) {
                idCostosNew = em.getReference(idCostosNew.getClass(), idCostosNew.getIdCostos());
                reporteDiario.setIdCostos(idCostosNew);
            }
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                reporteDiario.setIdEmpleado(idEmpleadoNew);
            }
            if (idSubActividadNew != null) {
                idSubActividadNew = em.getReference(idSubActividadNew.getClass(), idSubActividadNew.getIdSubActividad());
                reporteDiario.setIdSubActividad(idSubActividadNew);
            }
            reporteDiario = em.merge(reporteDiario);
            if (idCostosOld != null && !idCostosOld.equals(idCostosNew)) {
                idCostosOld.getReporteDiarioList().remove(reporteDiario);
                idCostosOld = em.merge(idCostosOld);
            }
            if (idCostosNew != null && !idCostosNew.equals(idCostosOld)) {
                idCostosNew.getReporteDiarioList().add(reporteDiario);
                idCostosNew = em.merge(idCostosNew);
            }
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getReporteDiarioList().remove(reporteDiario);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getReporteDiarioList().add(reporteDiario);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            if (idSubActividadOld != null && !idSubActividadOld.equals(idSubActividadNew)) {
                idSubActividadOld.getReporteDiarioList().remove(reporteDiario);
                idSubActividadOld = em.merge(idSubActividadOld);
            }
            if (idSubActividadNew != null && !idSubActividadNew.equals(idSubActividadOld)) {
                idSubActividadNew.getReporteDiarioList().add(reporteDiario);
                idSubActividadNew = em.merge(idSubActividadNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reporteDiario.getIdReporteDiario();
                if (findReporteDiario(id) == null) {
                    throw new NonexistentEntityException("The reporteDiario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ReporteDiario reporteDiario;
            try {
                reporteDiario = em.getReference(ReporteDiario.class, id);
                reporteDiario.getIdReporteDiario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reporteDiario with id " + id + " no longer exists.", enfe);
            }
            Costos idCostos = reporteDiario.getIdCostos();
            if (idCostos != null) {
                idCostos.getReporteDiarioList().remove(reporteDiario);
                idCostos = em.merge(idCostos);
            }
            Empleado idEmpleado = reporteDiario.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getReporteDiarioList().remove(reporteDiario);
                idEmpleado = em.merge(idEmpleado);
            }
            SubActividad idSubActividad = reporteDiario.getIdSubActividad();
            if (idSubActividad != null) {
                idSubActividad.getReporteDiarioList().remove(reporteDiario);
                idSubActividad = em.merge(idSubActividad);
            }
            em.remove(reporteDiario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ReporteDiario> findReporteDiarioEntities() {
        return findReporteDiarioEntities(true, -1, -1);
    }

    public List<ReporteDiario> findReporteDiarioEntities(int maxResults, int firstResult) {
        return findReporteDiarioEntities(false, maxResults, firstResult);
    }

    private List<ReporteDiario> findReporteDiarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ReporteDiario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ReporteDiario findReporteDiario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ReporteDiario.class, id);
        } finally {
            em.close();
        }
    }

    public int getReporteDiarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReporteDiario> rt = cq.from(ReporteDiario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
