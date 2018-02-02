/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Actividad;
import entidades.ReporteDiario;
import entidades.SubActividad;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author josue
 */
public class SubActividadJpaController implements Serializable {

    public SubActividadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SubActividad subActividad) {
        if (subActividad.getReporteDiarioList() == null) {
            subActividad.setReporteDiarioList(new ArrayList<ReporteDiario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad idActividad = subActividad.getIdActividad();
            if (idActividad != null) {
                idActividad = em.getReference(idActividad.getClass(), idActividad.getIdActividad());
                subActividad.setIdActividad(idActividad);
            }
            List<ReporteDiario> attachedReporteDiarioList = new ArrayList<ReporteDiario>();
            for (ReporteDiario reporteDiarioListReporteDiarioToAttach : subActividad.getReporteDiarioList()) {
                reporteDiarioListReporteDiarioToAttach = em.getReference(reporteDiarioListReporteDiarioToAttach.getClass(), reporteDiarioListReporteDiarioToAttach.getIdReporteDiario());
                attachedReporteDiarioList.add(reporteDiarioListReporteDiarioToAttach);
            }
            subActividad.setReporteDiarioList(attachedReporteDiarioList);
            em.persist(subActividad);
            if (idActividad != null) {
                idActividad.getSubActividadList().add(subActividad);
                idActividad = em.merge(idActividad);
            }
            for (ReporteDiario reporteDiarioListReporteDiario : subActividad.getReporteDiarioList()) {
                SubActividad oldIdSubActividadOfReporteDiarioListReporteDiario = reporteDiarioListReporteDiario.getIdSubActividad();
                reporteDiarioListReporteDiario.setIdSubActividad(subActividad);
                reporteDiarioListReporteDiario = em.merge(reporteDiarioListReporteDiario);
                if (oldIdSubActividadOfReporteDiarioListReporteDiario != null) {
                    oldIdSubActividadOfReporteDiarioListReporteDiario.getReporteDiarioList().remove(reporteDiarioListReporteDiario);
                    oldIdSubActividadOfReporteDiarioListReporteDiario = em.merge(oldIdSubActividadOfReporteDiarioListReporteDiario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SubActividad subActividad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SubActividad persistentSubActividad = em.find(SubActividad.class, subActividad.getIdSubActividad());
            Actividad idActividadOld = persistentSubActividad.getIdActividad();
            Actividad idActividadNew = subActividad.getIdActividad();
            List<ReporteDiario> reporteDiarioListOld = persistentSubActividad.getReporteDiarioList();
            List<ReporteDiario> reporteDiarioListNew = subActividad.getReporteDiarioList();
            List<String> illegalOrphanMessages = null;
            for (ReporteDiario reporteDiarioListOldReporteDiario : reporteDiarioListOld) {
                if (!reporteDiarioListNew.contains(reporteDiarioListOldReporteDiario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ReporteDiario " + reporteDiarioListOldReporteDiario + " since its idSubActividad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idActividadNew != null) {
                idActividadNew = em.getReference(idActividadNew.getClass(), idActividadNew.getIdActividad());
                subActividad.setIdActividad(idActividadNew);
            }
            List<ReporteDiario> attachedReporteDiarioListNew = new ArrayList<ReporteDiario>();
            for (ReporteDiario reporteDiarioListNewReporteDiarioToAttach : reporteDiarioListNew) {
                reporteDiarioListNewReporteDiarioToAttach = em.getReference(reporteDiarioListNewReporteDiarioToAttach.getClass(), reporteDiarioListNewReporteDiarioToAttach.getIdReporteDiario());
                attachedReporteDiarioListNew.add(reporteDiarioListNewReporteDiarioToAttach);
            }
            reporteDiarioListNew = attachedReporteDiarioListNew;
            subActividad.setReporteDiarioList(reporteDiarioListNew);
            subActividad = em.merge(subActividad);
            if (idActividadOld != null && !idActividadOld.equals(idActividadNew)) {
                idActividadOld.getSubActividadList().remove(subActividad);
                idActividadOld = em.merge(idActividadOld);
            }
            if (idActividadNew != null && !idActividadNew.equals(idActividadOld)) {
                idActividadNew.getSubActividadList().add(subActividad);
                idActividadNew = em.merge(idActividadNew);
            }
            for (ReporteDiario reporteDiarioListNewReporteDiario : reporteDiarioListNew) {
                if (!reporteDiarioListOld.contains(reporteDiarioListNewReporteDiario)) {
                    SubActividad oldIdSubActividadOfReporteDiarioListNewReporteDiario = reporteDiarioListNewReporteDiario.getIdSubActividad();
                    reporteDiarioListNewReporteDiario.setIdSubActividad(subActividad);
                    reporteDiarioListNewReporteDiario = em.merge(reporteDiarioListNewReporteDiario);
                    if (oldIdSubActividadOfReporteDiarioListNewReporteDiario != null && !oldIdSubActividadOfReporteDiarioListNewReporteDiario.equals(subActividad)) {
                        oldIdSubActividadOfReporteDiarioListNewReporteDiario.getReporteDiarioList().remove(reporteDiarioListNewReporteDiario);
                        oldIdSubActividadOfReporteDiarioListNewReporteDiario = em.merge(oldIdSubActividadOfReporteDiarioListNewReporteDiario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = subActividad.getIdSubActividad();
                if (findSubActividad(id) == null) {
                    throw new NonexistentEntityException("The subActividad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SubActividad subActividad;
            try {
                subActividad = em.getReference(SubActividad.class, id);
                subActividad.getIdSubActividad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subActividad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ReporteDiario> reporteDiarioListOrphanCheck = subActividad.getReporteDiarioList();
            for (ReporteDiario reporteDiarioListOrphanCheckReporteDiario : reporteDiarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("Este campo no puede ser eliminado, porque esta registrado en reportes.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Actividad idActividad = subActividad.getIdActividad();
            if (idActividad != null) {
                idActividad.getSubActividadList().remove(subActividad);
                idActividad = em.merge(idActividad);
            }
            em.remove(subActividad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SubActividad> findSubActividadEntities() {
        return findSubActividadEntities(true, -1, -1);
    }

    public List<SubActividad> findSubActividadEntities(int maxResults, int firstResult) {
        return findSubActividadEntities(false, maxResults, firstResult);
    }

    private List<SubActividad> findSubActividadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SubActividad.class));
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

    public SubActividad findSubActividad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SubActividad.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubActividadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SubActividad> rt = cq.from(SubActividad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
