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
import entidades.TipoCategoriaCostos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author josue
 */
public class TipoCategoriaCostosJpaController implements Serializable {

    public TipoCategoriaCostosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoCategoriaCostos tipoCategoriaCostos) {
        if (tipoCategoriaCostos.getCategoriaCostosList() == null) {
            tipoCategoriaCostos.setCategoriaCostosList(new ArrayList<CategoriaCostos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<CategoriaCostos> attachedCategoriaCostosList = new ArrayList<CategoriaCostos>();
            for (CategoriaCostos categoriaCostosListCategoriaCostosToAttach : tipoCategoriaCostos.getCategoriaCostosList()) {
                categoriaCostosListCategoriaCostosToAttach = em.getReference(categoriaCostosListCategoriaCostosToAttach.getClass(), categoriaCostosListCategoriaCostosToAttach.getIdCategoriaCostos());
                attachedCategoriaCostosList.add(categoriaCostosListCategoriaCostosToAttach);
            }
            tipoCategoriaCostos.setCategoriaCostosList(attachedCategoriaCostosList);
            em.persist(tipoCategoriaCostos);
            for (CategoriaCostos categoriaCostosListCategoriaCostos : tipoCategoriaCostos.getCategoriaCostosList()) {
                TipoCategoriaCostos oldIdTipoCategoriaCostosOfCategoriaCostosListCategoriaCostos = categoriaCostosListCategoriaCostos.getIdTipoCategoriaCostos();
                categoriaCostosListCategoriaCostos.setIdTipoCategoriaCostos(tipoCategoriaCostos);
                categoriaCostosListCategoriaCostos = em.merge(categoriaCostosListCategoriaCostos);
                if (oldIdTipoCategoriaCostosOfCategoriaCostosListCategoriaCostos != null) {
                    oldIdTipoCategoriaCostosOfCategoriaCostosListCategoriaCostos.getCategoriaCostosList().remove(categoriaCostosListCategoriaCostos);
                    oldIdTipoCategoriaCostosOfCategoriaCostosListCategoriaCostos = em.merge(oldIdTipoCategoriaCostosOfCategoriaCostosListCategoriaCostos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoCategoriaCostos tipoCategoriaCostos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoCategoriaCostos persistentTipoCategoriaCostos = em.find(TipoCategoriaCostos.class, tipoCategoriaCostos.getIdTipoCategoriaCostos());
            List<CategoriaCostos> categoriaCostosListOld = persistentTipoCategoriaCostos.getCategoriaCostosList();
            List<CategoriaCostos> categoriaCostosListNew = tipoCategoriaCostos.getCategoriaCostosList();
            List<String> illegalOrphanMessages = null;
            for (CategoriaCostos categoriaCostosListOldCategoriaCostos : categoriaCostosListOld) {
                if (!categoriaCostosListNew.contains(categoriaCostosListOldCategoriaCostos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CategoriaCostos " + categoriaCostosListOldCategoriaCostos + " since its idTipoCategoriaCostos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<CategoriaCostos> attachedCategoriaCostosListNew = new ArrayList<CategoriaCostos>();
            for (CategoriaCostos categoriaCostosListNewCategoriaCostosToAttach : categoriaCostosListNew) {
                categoriaCostosListNewCategoriaCostosToAttach = em.getReference(categoriaCostosListNewCategoriaCostosToAttach.getClass(), categoriaCostosListNewCategoriaCostosToAttach.getIdCategoriaCostos());
                attachedCategoriaCostosListNew.add(categoriaCostosListNewCategoriaCostosToAttach);
            }
            categoriaCostosListNew = attachedCategoriaCostosListNew;
            tipoCategoriaCostos.setCategoriaCostosList(categoriaCostosListNew);
            tipoCategoriaCostos = em.merge(tipoCategoriaCostos);
            for (CategoriaCostos categoriaCostosListNewCategoriaCostos : categoriaCostosListNew) {
                if (!categoriaCostosListOld.contains(categoriaCostosListNewCategoriaCostos)) {
                    TipoCategoriaCostos oldIdTipoCategoriaCostosOfCategoriaCostosListNewCategoriaCostos = categoriaCostosListNewCategoriaCostos.getIdTipoCategoriaCostos();
                    categoriaCostosListNewCategoriaCostos.setIdTipoCategoriaCostos(tipoCategoriaCostos);
                    categoriaCostosListNewCategoriaCostos = em.merge(categoriaCostosListNewCategoriaCostos);
                    if (oldIdTipoCategoriaCostosOfCategoriaCostosListNewCategoriaCostos != null && !oldIdTipoCategoriaCostosOfCategoriaCostosListNewCategoriaCostos.equals(tipoCategoriaCostos)) {
                        oldIdTipoCategoriaCostosOfCategoriaCostosListNewCategoriaCostos.getCategoriaCostosList().remove(categoriaCostosListNewCategoriaCostos);
                        oldIdTipoCategoriaCostosOfCategoriaCostosListNewCategoriaCostos = em.merge(oldIdTipoCategoriaCostosOfCategoriaCostosListNewCategoriaCostos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoCategoriaCostos.getIdTipoCategoriaCostos();
                if (findTipoCategoriaCostos(id) == null) {
                    throw new NonexistentEntityException("The tipoCategoriaCostos with id " + id + " no longer exists.");
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
            TipoCategoriaCostos tipoCategoriaCostos;
            try {
                tipoCategoriaCostos = em.getReference(TipoCategoriaCostos.class, id);
                tipoCategoriaCostos.getIdTipoCategoriaCostos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoCategoriaCostos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<CategoriaCostos> categoriaCostosListOrphanCheck = tipoCategoriaCostos.getCategoriaCostosList();
            for (CategoriaCostos categoriaCostosListOrphanCheckCategoriaCostos : categoriaCostosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("Este campo no puede ser eliminado, porque hay categorias que lo utilizan.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoCategoriaCostos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoCategoriaCostos> findTipoCategoriaCostosEntities() {
        return findTipoCategoriaCostosEntities(true, -1, -1);
    }

    public List<TipoCategoriaCostos> findTipoCategoriaCostosEntities(int maxResults, int firstResult) {
        return findTipoCategoriaCostosEntities(false, maxResults, firstResult);
    }

    private List<TipoCategoriaCostos> findTipoCategoriaCostosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoCategoriaCostos.class));
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

    public TipoCategoriaCostos findTipoCategoriaCostos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoCategoriaCostos.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoCategoriaCostosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoCategoriaCostos> rt = cq.from(TipoCategoriaCostos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
