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
import entidades.TipoEmp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author josue
 */
public class TipoEmpJpaController implements Serializable {

    public TipoEmpJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoEmp tipoEmp) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado EMPLEADOidempleado = tipoEmp.getEMPLEADOidempleado();
            if (EMPLEADOidempleado != null) {
                EMPLEADOidempleado = em.getReference(EMPLEADOidempleado.getClass(), EMPLEADOidempleado.getIdEmpleado());
                tipoEmp.setEMPLEADOidempleado(EMPLEADOidempleado);
            }
            em.persist(tipoEmp);
            if (EMPLEADOidempleado != null) {
                EMPLEADOidempleado.getTipoEmpList().add(tipoEmp);
                EMPLEADOidempleado = em.merge(EMPLEADOidempleado);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoEmp tipoEmp) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoEmp persistentTipoEmp = em.find(TipoEmp.class, tipoEmp.getIdTipoEmp());
            Empleado EMPLEADOidempleadoOld = persistentTipoEmp.getEMPLEADOidempleado();
            Empleado EMPLEADOidempleadoNew = tipoEmp.getEMPLEADOidempleado();
            if (EMPLEADOidempleadoNew != null) {
                EMPLEADOidempleadoNew = em.getReference(EMPLEADOidempleadoNew.getClass(), EMPLEADOidempleadoNew.getIdEmpleado());
                tipoEmp.setEMPLEADOidempleado(EMPLEADOidempleadoNew);
            }
            tipoEmp = em.merge(tipoEmp);
            if (EMPLEADOidempleadoOld != null && !EMPLEADOidempleadoOld.equals(EMPLEADOidempleadoNew)) {
                EMPLEADOidempleadoOld.getTipoEmpList().remove(tipoEmp);
                EMPLEADOidempleadoOld = em.merge(EMPLEADOidempleadoOld);
            }
            if (EMPLEADOidempleadoNew != null && !EMPLEADOidempleadoNew.equals(EMPLEADOidempleadoOld)) {
                EMPLEADOidempleadoNew.getTipoEmpList().add(tipoEmp);
                EMPLEADOidempleadoNew = em.merge(EMPLEADOidempleadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoEmp.getIdTipoEmp();
                if (findTipoEmp(id) == null) {
                    throw new NonexistentEntityException("The tipoEmp with id " + id + " no longer exists.");
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
            TipoEmp tipoEmp;
            try {
                tipoEmp = em.getReference(TipoEmp.class, id);
                tipoEmp.getIdTipoEmp();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoEmp with id " + id + " no longer exists.", enfe);
            }
            Empleado EMPLEADOidempleado = tipoEmp.getEMPLEADOidempleado();
            if (EMPLEADOidempleado != null) {
                EMPLEADOidempleado.getTipoEmpList().remove(tipoEmp);
                EMPLEADOidempleado = em.merge(EMPLEADOidempleado);
            }
            em.remove(tipoEmp);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoEmp> findTipoEmpEntities() {
        return findTipoEmpEntities(true, -1, -1);
    }

    public List<TipoEmp> findTipoEmpEntities(int maxResults, int firstResult) {
        return findTipoEmpEntities(false, maxResults, firstResult);
    }

    private List<TipoEmp> findTipoEmpEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoEmp.class));
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

    public TipoEmp findTipoEmp(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoEmp.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoEmpCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoEmp> rt = cq.from(TipoEmp.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
