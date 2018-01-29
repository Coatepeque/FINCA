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
import entidades.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author josue
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado EMPLEADOidempleado = usuario.getEMPLEADOidempleado();
            if (EMPLEADOidempleado != null) {
                EMPLEADOidempleado = em.getReference(EMPLEADOidempleado.getClass(), EMPLEADOidempleado.getIdEmpleado());
                usuario.setEMPLEADOidempleado(EMPLEADOidempleado);
            }
            em.persist(usuario);
            if (EMPLEADOidempleado != null) {
                EMPLEADOidempleado.getUsuarioList().add(usuario);
                EMPLEADOidempleado = em.merge(EMPLEADOidempleado);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Empleado EMPLEADOidempleadoOld = persistentUsuario.getEMPLEADOidempleado();
            Empleado EMPLEADOidempleadoNew = usuario.getEMPLEADOidempleado();
            if (EMPLEADOidempleadoNew != null) {
                EMPLEADOidempleadoNew = em.getReference(EMPLEADOidempleadoNew.getClass(), EMPLEADOidempleadoNew.getIdEmpleado());
                usuario.setEMPLEADOidempleado(EMPLEADOidempleadoNew);
            }
            usuario = em.merge(usuario);
            if (EMPLEADOidempleadoOld != null && !EMPLEADOidempleadoOld.equals(EMPLEADOidempleadoNew)) {
                EMPLEADOidempleadoOld.getUsuarioList().remove(usuario);
                EMPLEADOidempleadoOld = em.merge(EMPLEADOidempleadoOld);
            }
            if (EMPLEADOidempleadoNew != null && !EMPLEADOidempleadoNew.equals(EMPLEADOidempleadoOld)) {
                EMPLEADOidempleadoNew.getUsuarioList().add(usuario);
                EMPLEADOidempleadoNew = em.merge(EMPLEADOidempleadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Empleado EMPLEADOidempleado = usuario.getEMPLEADOidempleado();
            if (EMPLEADOidempleado != null) {
                EMPLEADOidempleado.getUsuarioList().remove(usuario);
                EMPLEADOidempleado = em.merge(EMPLEADOidempleado);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
