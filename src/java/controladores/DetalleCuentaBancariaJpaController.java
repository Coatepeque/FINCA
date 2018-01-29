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
import entidades.Banco;
import entidades.DetalleCuentaBancaria;
import entidades.Empleado;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author josue
 */
public class DetalleCuentaBancariaJpaController implements Serializable {

    public DetalleCuentaBancariaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleCuentaBancaria detalleCuentaBancaria) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Banco BANCOidbanco = detalleCuentaBancaria.getBANCOidbanco();
            if (BANCOidbanco != null) {
                BANCOidbanco = em.getReference(BANCOidbanco.getClass(), BANCOidbanco.getIdBanco());
                detalleCuentaBancaria.setBANCOidbanco(BANCOidbanco);
            }
            Empleado EMPLEADOidempleado = detalleCuentaBancaria.getEMPLEADOidempleado();
            if (EMPLEADOidempleado != null) {
                EMPLEADOidempleado = em.getReference(EMPLEADOidempleado.getClass(), EMPLEADOidempleado.getIdEmpleado());
                detalleCuentaBancaria.setEMPLEADOidempleado(EMPLEADOidempleado);
            }
            em.persist(detalleCuentaBancaria);
            if (BANCOidbanco != null) {
                BANCOidbanco.getDetalleCuentaBancariaList().add(detalleCuentaBancaria);
                BANCOidbanco = em.merge(BANCOidbanco);
            }
            if (EMPLEADOidempleado != null) {
                EMPLEADOidempleado.getDetalleCuentaBancariaList().add(detalleCuentaBancaria);
                EMPLEADOidempleado = em.merge(EMPLEADOidempleado);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleCuentaBancaria detalleCuentaBancaria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleCuentaBancaria persistentDetalleCuentaBancaria = em.find(DetalleCuentaBancaria.class, detalleCuentaBancaria.getIdDetalleCuenta());
            Banco BANCOidbancoOld = persistentDetalleCuentaBancaria.getBANCOidbanco();
            Banco BANCOidbancoNew = detalleCuentaBancaria.getBANCOidbanco();
            Empleado EMPLEADOidempleadoOld = persistentDetalleCuentaBancaria.getEMPLEADOidempleado();
            Empleado EMPLEADOidempleadoNew = detalleCuentaBancaria.getEMPLEADOidempleado();
            if (BANCOidbancoNew != null) {
                BANCOidbancoNew = em.getReference(BANCOidbancoNew.getClass(), BANCOidbancoNew.getIdBanco());
                detalleCuentaBancaria.setBANCOidbanco(BANCOidbancoNew);
            }
            if (EMPLEADOidempleadoNew != null) {
                EMPLEADOidempleadoNew = em.getReference(EMPLEADOidempleadoNew.getClass(), EMPLEADOidempleadoNew.getIdEmpleado());
                detalleCuentaBancaria.setEMPLEADOidempleado(EMPLEADOidempleadoNew);
            }
            detalleCuentaBancaria = em.merge(detalleCuentaBancaria);
            if (BANCOidbancoOld != null && !BANCOidbancoOld.equals(BANCOidbancoNew)) {
                BANCOidbancoOld.getDetalleCuentaBancariaList().remove(detalleCuentaBancaria);
                BANCOidbancoOld = em.merge(BANCOidbancoOld);
            }
            if (BANCOidbancoNew != null && !BANCOidbancoNew.equals(BANCOidbancoOld)) {
                BANCOidbancoNew.getDetalleCuentaBancariaList().add(detalleCuentaBancaria);
                BANCOidbancoNew = em.merge(BANCOidbancoNew);
            }
            if (EMPLEADOidempleadoOld != null && !EMPLEADOidempleadoOld.equals(EMPLEADOidempleadoNew)) {
                EMPLEADOidempleadoOld.getDetalleCuentaBancariaList().remove(detalleCuentaBancaria);
                EMPLEADOidempleadoOld = em.merge(EMPLEADOidempleadoOld);
            }
            if (EMPLEADOidempleadoNew != null && !EMPLEADOidempleadoNew.equals(EMPLEADOidempleadoOld)) {
                EMPLEADOidempleadoNew.getDetalleCuentaBancariaList().add(detalleCuentaBancaria);
                EMPLEADOidempleadoNew = em.merge(EMPLEADOidempleadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detalleCuentaBancaria.getIdDetalleCuenta();
                if (findDetalleCuentaBancaria(id) == null) {
                    throw new NonexistentEntityException("The detalleCuentaBancaria with id " + id + " no longer exists.");
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
            DetalleCuentaBancaria detalleCuentaBancaria;
            try {
                detalleCuentaBancaria = em.getReference(DetalleCuentaBancaria.class, id);
                detalleCuentaBancaria.getIdDetalleCuenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleCuentaBancaria with id " + id + " no longer exists.", enfe);
            }
            Banco BANCOidbanco = detalleCuentaBancaria.getBANCOidbanco();
            if (BANCOidbanco != null) {
                BANCOidbanco.getDetalleCuentaBancariaList().remove(detalleCuentaBancaria);
                BANCOidbanco = em.merge(BANCOidbanco);
            }
            Empleado EMPLEADOidempleado = detalleCuentaBancaria.getEMPLEADOidempleado();
            if (EMPLEADOidempleado != null) {
                EMPLEADOidempleado.getDetalleCuentaBancariaList().remove(detalleCuentaBancaria);
                EMPLEADOidempleado = em.merge(EMPLEADOidempleado);
            }
            em.remove(detalleCuentaBancaria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleCuentaBancaria> findDetalleCuentaBancariaEntities() {
        return findDetalleCuentaBancariaEntities(true, -1, -1);
    }

    public List<DetalleCuentaBancaria> findDetalleCuentaBancariaEntities(int maxResults, int firstResult) {
        return findDetalleCuentaBancariaEntities(false, maxResults, firstResult);
    }

    private List<DetalleCuentaBancaria> findDetalleCuentaBancariaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleCuentaBancaria.class));
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

    public DetalleCuentaBancaria findDetalleCuentaBancaria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleCuentaBancaria.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleCuentaBancariaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleCuentaBancaria> rt = cq.from(DetalleCuentaBancaria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
