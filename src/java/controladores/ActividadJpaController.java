/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import entidades.Actividad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Centro;
import entidades.SubActividad;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author josue
 */
public class ActividadJpaController implements Serializable {

    public ActividadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Actividad actividad) {
        if (actividad.getSubActividadList() == null) {
            actividad.setSubActividadList(new ArrayList<SubActividad>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Centro idCentro = actividad.getIdCentro();
            if (idCentro != null) {
                idCentro = em.getReference(idCentro.getClass(), idCentro.getIdCentro());
                actividad.setIdCentro(idCentro);
            }
            List<SubActividad> attachedSubActividadList = new ArrayList<SubActividad>();
            for (SubActividad subActividadListSubActividadToAttach : actividad.getSubActividadList()) {
                subActividadListSubActividadToAttach = em.getReference(subActividadListSubActividadToAttach.getClass(), subActividadListSubActividadToAttach.getIdSubActividad());
                attachedSubActividadList.add(subActividadListSubActividadToAttach);
            }
            actividad.setSubActividadList(attachedSubActividadList);
            em.persist(actividad);
            if (idCentro != null) {
                idCentro.getActividadList().add(actividad);
                idCentro = em.merge(idCentro);
            }
            for (SubActividad subActividadListSubActividad : actividad.getSubActividadList()) {
                Actividad oldIdActividadOfSubActividadListSubActividad = subActividadListSubActividad.getIdActividad();
                subActividadListSubActividad.setIdActividad(actividad);
                subActividadListSubActividad = em.merge(subActividadListSubActividad);
                if (oldIdActividadOfSubActividadListSubActividad != null) {
                    oldIdActividadOfSubActividadListSubActividad.getSubActividadList().remove(subActividadListSubActividad);
                    oldIdActividadOfSubActividadListSubActividad = em.merge(oldIdActividadOfSubActividadListSubActividad);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Actividad actividad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad persistentActividad = em.find(Actividad.class, actividad.getIdActividad());
            Centro idCentroOld = persistentActividad.getIdCentro();
            Centro idCentroNew = actividad.getIdCentro();
            List<SubActividad> subActividadListOld = persistentActividad.getSubActividadList();
            List<SubActividad> subActividadListNew = actividad.getSubActividadList();
            List<String> illegalOrphanMessages = null;
            for (SubActividad subActividadListOldSubActividad : subActividadListOld) {
                if (!subActividadListNew.contains(subActividadListOldSubActividad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SubActividad " + subActividadListOldSubActividad + " since its idActividad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCentroNew != null) {
                idCentroNew = em.getReference(idCentroNew.getClass(), idCentroNew.getIdCentro());
                actividad.setIdCentro(idCentroNew);
            }
            List<SubActividad> attachedSubActividadListNew = new ArrayList<SubActividad>();
            for (SubActividad subActividadListNewSubActividadToAttach : subActividadListNew) {
                subActividadListNewSubActividadToAttach = em.getReference(subActividadListNewSubActividadToAttach.getClass(), subActividadListNewSubActividadToAttach.getIdSubActividad());
                attachedSubActividadListNew.add(subActividadListNewSubActividadToAttach);
            }
            subActividadListNew = attachedSubActividadListNew;
            actividad.setSubActividadList(subActividadListNew);
            actividad = em.merge(actividad);
            if (idCentroOld != null && !idCentroOld.equals(idCentroNew)) {
                idCentroOld.getActividadList().remove(actividad);
                idCentroOld = em.merge(idCentroOld);
            }
            if (idCentroNew != null && !idCentroNew.equals(idCentroOld)) {
                idCentroNew.getActividadList().add(actividad);
                idCentroNew = em.merge(idCentroNew);
            }
            for (SubActividad subActividadListNewSubActividad : subActividadListNew) {
                if (!subActividadListOld.contains(subActividadListNewSubActividad)) {
                    Actividad oldIdActividadOfSubActividadListNewSubActividad = subActividadListNewSubActividad.getIdActividad();
                    subActividadListNewSubActividad.setIdActividad(actividad);
                    subActividadListNewSubActividad = em.merge(subActividadListNewSubActividad);
                    if (oldIdActividadOfSubActividadListNewSubActividad != null && !oldIdActividadOfSubActividadListNewSubActividad.equals(actividad)) {
                        oldIdActividadOfSubActividadListNewSubActividad.getSubActividadList().remove(subActividadListNewSubActividad);
                        oldIdActividadOfSubActividadListNewSubActividad = em.merge(oldIdActividadOfSubActividadListNewSubActividad);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = actividad.getIdActividad();
                if (findActividad(id) == null) {
                    throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.");
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
            Actividad actividad;
            try {
                actividad = em.getReference(Actividad.class, id);
                actividad.getIdActividad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SubActividad> subActividadListOrphanCheck = actividad.getSubActividadList();
            for (SubActividad subActividadListOrphanCheckSubActividad : subActividadListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Actividad (" + actividad + ") cannot be destroyed since the SubActividad " + subActividadListOrphanCheckSubActividad + " in its subActividadList field has a non-nullable idActividad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Centro idCentro = actividad.getIdCentro();
            if (idCentro != null) {
                idCentro.getActividadList().remove(actividad);
                idCentro = em.merge(idCentro);
            }
            em.remove(actividad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Actividad> findActividadEntities() {
        return findActividadEntities(true, -1, -1);
    }

    public List<Actividad> findActividadEntities(int maxResults, int firstResult) {
        return findActividadEntities(false, maxResults, firstResult);
    }

    private List<Actividad> findActividadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Actividad.class));
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

    public Actividad findActividad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Actividad.class, id);
        } finally {
            em.close();
        }
    }

    public int getActividadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Actividad> rt = cq.from(Actividad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
