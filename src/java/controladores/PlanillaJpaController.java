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
import entidades.Empleado;
import entidades.Planilla;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author josue
 */
public class PlanillaJpaController implements Serializable {

    public PlanillaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Planilla planilla) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado idEmpleado = planilla.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                planilla.setIdEmpleado(idEmpleado);
            }
            em.persist(planilla);
            if (idEmpleado != null) {
                idEmpleado.getPlanillaList().add(planilla);
                idEmpleado = em.merge(idEmpleado);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Planilla planilla) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Planilla persistentPlanilla = em.find(Planilla.class, planilla.getIdPlanilla());
            Empleado idEmpleadoOld = persistentPlanilla.getIdEmpleado();
            Empleado idEmpleadoNew = planilla.getIdEmpleado();
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                planilla.setIdEmpleado(idEmpleadoNew);
            }
            planilla = em.merge(planilla);
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getPlanillaList().remove(planilla);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getPlanillaList().add(planilla);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = planilla.getIdPlanilla();
                if (findPlanilla(id) == null) {
                    throw new NonexistentEntityException("The planilla with id " + id + " no longer exists.");
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
            Planilla planilla;
            try {
                planilla = em.getReference(Planilla.class, id);
                planilla.getIdPlanilla();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The planilla with id " + id + " no longer exists.", enfe);
            }
            Empleado idEmpleado = planilla.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getPlanillaList().remove(planilla);
                idEmpleado = em.merge(idEmpleado);
            }
            em.remove(planilla);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Planilla> findPlanillaEntities() {
        return findPlanillaEntities(true, -1, -1);
    }

    public List<Planilla> findPlanillaEntities(int maxResults, int firstResult) {
        return findPlanillaEntities(false, maxResults, firstResult);
    }

    private List<Planilla> findPlanillaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Planilla.class));
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

    public Planilla findPlanilla(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Planilla.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlanillaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Planilla> rt = cq.from(Planilla.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
