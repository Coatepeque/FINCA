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
import entidades.Empleado;
import entidades.TipoEmpleado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author josue
 */
public class TipoEmpleadoJpaController implements Serializable {

    public TipoEmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoEmpleado tipoEmpleado) {
        if (tipoEmpleado.getEmpleadoList() == null) {
            tipoEmpleado.setEmpleadoList(new ArrayList<Empleado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Empleado> attachedEmpleadoList = new ArrayList<Empleado>();
            for (Empleado empleadoListEmpleadoToAttach : tipoEmpleado.getEmpleadoList()) {
                empleadoListEmpleadoToAttach = em.getReference(empleadoListEmpleadoToAttach.getClass(), empleadoListEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoList.add(empleadoListEmpleadoToAttach);
            }
            tipoEmpleado.setEmpleadoList(attachedEmpleadoList);
            em.persist(tipoEmpleado);
            for (Empleado empleadoListEmpleado : tipoEmpleado.getEmpleadoList()) {
                TipoEmpleado oldIdTipoEmpleadoOfEmpleadoListEmpleado = empleadoListEmpleado.getIdTipoEmpleado();
                empleadoListEmpleado.setIdTipoEmpleado(tipoEmpleado);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldIdTipoEmpleadoOfEmpleadoListEmpleado != null) {
                    oldIdTipoEmpleadoOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldIdTipoEmpleadoOfEmpleadoListEmpleado = em.merge(oldIdTipoEmpleadoOfEmpleadoListEmpleado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoEmpleado tipoEmpleado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoEmpleado persistentTipoEmpleado = em.find(TipoEmpleado.class, tipoEmpleado.getIdTipoEmpleado());
            List<Empleado> empleadoListOld = persistentTipoEmpleado.getEmpleadoList();
            List<Empleado> empleadoListNew = tipoEmpleado.getEmpleadoList();
            List<String> illegalOrphanMessages = null;
            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleado " + empleadoListOldEmpleado + " since its idTipoEmpleado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Empleado> attachedEmpleadoListNew = new ArrayList<Empleado>();
            for (Empleado empleadoListNewEmpleadoToAttach : empleadoListNew) {
                empleadoListNewEmpleadoToAttach = em.getReference(empleadoListNewEmpleadoToAttach.getClass(), empleadoListNewEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoListNew.add(empleadoListNewEmpleadoToAttach);
            }
            empleadoListNew = attachedEmpleadoListNew;
            tipoEmpleado.setEmpleadoList(empleadoListNew);
            tipoEmpleado = em.merge(tipoEmpleado);
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    TipoEmpleado oldIdTipoEmpleadoOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getIdTipoEmpleado();
                    empleadoListNewEmpleado.setIdTipoEmpleado(tipoEmpleado);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldIdTipoEmpleadoOfEmpleadoListNewEmpleado != null && !oldIdTipoEmpleadoOfEmpleadoListNewEmpleado.equals(tipoEmpleado)) {
                        oldIdTipoEmpleadoOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldIdTipoEmpleadoOfEmpleadoListNewEmpleado = em.merge(oldIdTipoEmpleadoOfEmpleadoListNewEmpleado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoEmpleado.getIdTipoEmpleado();
                if (findTipoEmpleado(id) == null) {
                    throw new NonexistentEntityException("The tipoEmpleado with id " + id + " no longer exists.");
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
            TipoEmpleado tipoEmpleado;
            try {
                tipoEmpleado = em.getReference(TipoEmpleado.class, id);
                tipoEmpleado.getIdTipoEmpleado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoEmpleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Empleado> empleadoListOrphanCheck = tipoEmpleado.getEmpleadoList();
            for (Empleado empleadoListOrphanCheckEmpleado : empleadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoEmpleado (" + tipoEmpleado + ") cannot be destroyed since the Empleado " + empleadoListOrphanCheckEmpleado + " in its empleadoList field has a non-nullable idTipoEmpleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoEmpleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoEmpleado> findTipoEmpleadoEntities() {
        return findTipoEmpleadoEntities(true, -1, -1);
    }

    public List<TipoEmpleado> findTipoEmpleadoEntities(int maxResults, int firstResult) {
        return findTipoEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<TipoEmpleado> findTipoEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoEmpleado.class));
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

    public TipoEmpleado findTipoEmpleado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoEmpleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoEmpleado> rt = cq.from(TipoEmpleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
