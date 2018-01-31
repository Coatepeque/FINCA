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
import entidades.CategoriaCostos;
import entidades.Costos;
import entidades.ReporteDiario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author josue
 */
public class CostosJpaController implements Serializable {

    public CostosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Costos costos) {
        if (costos.getReporteDiarioList() == null) {
            costos.setReporteDiarioList(new ArrayList<ReporteDiario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CategoriaCostos idCategoriaCostos = costos.getIdCategoriaCostos();
            if (idCategoriaCostos != null) {
                idCategoriaCostos = em.getReference(idCategoriaCostos.getClass(), idCategoriaCostos.getIdCategoriaCostos());
                costos.setIdCategoriaCostos(idCategoriaCostos);
            }
            List<ReporteDiario> attachedReporteDiarioList = new ArrayList<ReporteDiario>();
            for (ReporteDiario reporteDiarioListReporteDiarioToAttach : costos.getReporteDiarioList()) {
                reporteDiarioListReporteDiarioToAttach = em.getReference(reporteDiarioListReporteDiarioToAttach.getClass(), reporteDiarioListReporteDiarioToAttach.getIdReporteDiario());
                attachedReporteDiarioList.add(reporteDiarioListReporteDiarioToAttach);
            }
            costos.setReporteDiarioList(attachedReporteDiarioList);
            em.persist(costos);
            if (idCategoriaCostos != null) {
                idCategoriaCostos.getCostosList().add(costos);
                idCategoriaCostos = em.merge(idCategoriaCostos);
            }
            for (ReporteDiario reporteDiarioListReporteDiario : costos.getReporteDiarioList()) {
                Costos oldIdCostosOfReporteDiarioListReporteDiario = reporteDiarioListReporteDiario.getIdCostos();
                reporteDiarioListReporteDiario.setIdCostos(costos);
                reporteDiarioListReporteDiario = em.merge(reporteDiarioListReporteDiario);
                if (oldIdCostosOfReporteDiarioListReporteDiario != null) {
                    oldIdCostosOfReporteDiarioListReporteDiario.getReporteDiarioList().remove(reporteDiarioListReporteDiario);
                    oldIdCostosOfReporteDiarioListReporteDiario = em.merge(oldIdCostosOfReporteDiarioListReporteDiario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Costos costos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Costos persistentCostos = em.find(Costos.class, costos.getIdCostos());
            CategoriaCostos idCategoriaCostosOld = persistentCostos.getIdCategoriaCostos();
            CategoriaCostos idCategoriaCostosNew = costos.getIdCategoriaCostos();
            List<ReporteDiario> reporteDiarioListOld = persistentCostos.getReporteDiarioList();
            List<ReporteDiario> reporteDiarioListNew = costos.getReporteDiarioList();
            List<String> illegalOrphanMessages = null;
            for (ReporteDiario reporteDiarioListOldReporteDiario : reporteDiarioListOld) {
                if (!reporteDiarioListNew.contains(reporteDiarioListOldReporteDiario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ReporteDiario " + reporteDiarioListOldReporteDiario + " since its idCostos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategoriaCostosNew != null) {
                idCategoriaCostosNew = em.getReference(idCategoriaCostosNew.getClass(), idCategoriaCostosNew.getIdCategoriaCostos());
                costos.setIdCategoriaCostos(idCategoriaCostosNew);
            }
            List<ReporteDiario> attachedReporteDiarioListNew = new ArrayList<ReporteDiario>();
            for (ReporteDiario reporteDiarioListNewReporteDiarioToAttach : reporteDiarioListNew) {
                reporteDiarioListNewReporteDiarioToAttach = em.getReference(reporteDiarioListNewReporteDiarioToAttach.getClass(), reporteDiarioListNewReporteDiarioToAttach.getIdReporteDiario());
                attachedReporteDiarioListNew.add(reporteDiarioListNewReporteDiarioToAttach);
            }
            reporteDiarioListNew = attachedReporteDiarioListNew;
            costos.setReporteDiarioList(reporteDiarioListNew);
            costos = em.merge(costos);
            if (idCategoriaCostosOld != null && !idCategoriaCostosOld.equals(idCategoriaCostosNew)) {
                idCategoriaCostosOld.getCostosList().remove(costos);
                idCategoriaCostosOld = em.merge(idCategoriaCostosOld);
            }
            if (idCategoriaCostosNew != null && !idCategoriaCostosNew.equals(idCategoriaCostosOld)) {
                idCategoriaCostosNew.getCostosList().add(costos);
                idCategoriaCostosNew = em.merge(idCategoriaCostosNew);
            }
            for (ReporteDiario reporteDiarioListNewReporteDiario : reporteDiarioListNew) {
                if (!reporteDiarioListOld.contains(reporteDiarioListNewReporteDiario)) {
                    Costos oldIdCostosOfReporteDiarioListNewReporteDiario = reporteDiarioListNewReporteDiario.getIdCostos();
                    reporteDiarioListNewReporteDiario.setIdCostos(costos);
                    reporteDiarioListNewReporteDiario = em.merge(reporteDiarioListNewReporteDiario);
                    if (oldIdCostosOfReporteDiarioListNewReporteDiario != null && !oldIdCostosOfReporteDiarioListNewReporteDiario.equals(costos)) {
                        oldIdCostosOfReporteDiarioListNewReporteDiario.getReporteDiarioList().remove(reporteDiarioListNewReporteDiario);
                        oldIdCostosOfReporteDiarioListNewReporteDiario = em.merge(oldIdCostosOfReporteDiarioListNewReporteDiario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = costos.getIdCostos();
                if (findCostos(id) == null) {
                    throw new NonexistentEntityException("The costos with id " + id + " no longer exists.");
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
            Costos costos;
            try {
                costos = em.getReference(Costos.class, id);
                costos.getIdCostos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The costos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ReporteDiario> reporteDiarioListOrphanCheck = costos.getReporteDiarioList();
            for (ReporteDiario reporteDiarioListOrphanCheckReporteDiario : reporteDiarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Costos (" + costos + ") cannot be destroyed since the ReporteDiario " + reporteDiarioListOrphanCheckReporteDiario + " in its reporteDiarioList field has a non-nullable idCostos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CategoriaCostos idCategoriaCostos = costos.getIdCategoriaCostos();
            if (idCategoriaCostos != null) {
                idCategoriaCostos.getCostosList().remove(costos);
                idCategoriaCostos = em.merge(idCategoriaCostos);
            }
            em.remove(costos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Costos> findCostosEntities() {
        return findCostosEntities(true, -1, -1);
    }

    public List<Costos> findCostosEntities(int maxResults, int firstResult) {
        return findCostosEntities(false, maxResults, firstResult);
    }

    private List<Costos> findCostosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Costos.class));
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

    public Costos findCostos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Costos.class, id);
        } finally {
            em.close();
        }
    }

    public int getCostosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Costos> rt = cq.from(Costos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
