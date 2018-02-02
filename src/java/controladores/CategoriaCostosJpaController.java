/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import entidades.CategoriaCostos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.TipoCategoriaCostos;
import entidades.Costos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author josue
 */
public class CategoriaCostosJpaController implements Serializable {

    public CategoriaCostosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CategoriaCostos categoriaCostos) {
        if (categoriaCostos.getCostosList() == null) {
            categoriaCostos.setCostosList(new ArrayList<Costos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoCategoriaCostos idTipoCategoriaCostos = categoriaCostos.getIdTipoCategoriaCostos();
            if (idTipoCategoriaCostos != null) {
                idTipoCategoriaCostos = em.getReference(idTipoCategoriaCostos.getClass(), idTipoCategoriaCostos.getIdTipoCategoriaCostos());
                categoriaCostos.setIdTipoCategoriaCostos(idTipoCategoriaCostos);
            }
            List<Costos> attachedCostosList = new ArrayList<Costos>();
            for (Costos costosListCostosToAttach : categoriaCostos.getCostosList()) {
                costosListCostosToAttach = em.getReference(costosListCostosToAttach.getClass(), costosListCostosToAttach.getIdCostos());
                attachedCostosList.add(costosListCostosToAttach);
            }
            categoriaCostos.setCostosList(attachedCostosList);
            em.persist(categoriaCostos);
            if (idTipoCategoriaCostos != null) {
                idTipoCategoriaCostos.getCategoriaCostosList().add(categoriaCostos);
                idTipoCategoriaCostos = em.merge(idTipoCategoriaCostos);
            }
            for (Costos costosListCostos : categoriaCostos.getCostosList()) {
                CategoriaCostos oldIdCategoriaCostosOfCostosListCostos = costosListCostos.getIdCategoriaCostos();
                costosListCostos.setIdCategoriaCostos(categoriaCostos);
                costosListCostos = em.merge(costosListCostos);
                if (oldIdCategoriaCostosOfCostosListCostos != null) {
                    oldIdCategoriaCostosOfCostosListCostos.getCostosList().remove(costosListCostos);
                    oldIdCategoriaCostosOfCostosListCostos = em.merge(oldIdCategoriaCostosOfCostosListCostos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CategoriaCostos categoriaCostos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CategoriaCostos persistentCategoriaCostos = em.find(CategoriaCostos.class, categoriaCostos.getIdCategoriaCostos());
            TipoCategoriaCostos idTipoCategoriaCostosOld = persistentCategoriaCostos.getIdTipoCategoriaCostos();
            TipoCategoriaCostos idTipoCategoriaCostosNew = categoriaCostos.getIdTipoCategoriaCostos();
            List<Costos> costosListOld = persistentCategoriaCostos.getCostosList();
            List<Costos> costosListNew = categoriaCostos.getCostosList();
            List<String> illegalOrphanMessages = null;
            for (Costos costosListOldCostos : costosListOld) {
                if (!costosListNew.contains(costosListOldCostos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Costos " + costosListOldCostos + " since its idCategoriaCostos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTipoCategoriaCostosNew != null) {
                idTipoCategoriaCostosNew = em.getReference(idTipoCategoriaCostosNew.getClass(), idTipoCategoriaCostosNew.getIdTipoCategoriaCostos());
                categoriaCostos.setIdTipoCategoriaCostos(idTipoCategoriaCostosNew);
            }
            List<Costos> attachedCostosListNew = new ArrayList<Costos>();
            for (Costos costosListNewCostosToAttach : costosListNew) {
                costosListNewCostosToAttach = em.getReference(costosListNewCostosToAttach.getClass(), costosListNewCostosToAttach.getIdCostos());
                attachedCostosListNew.add(costosListNewCostosToAttach);
            }
            costosListNew = attachedCostosListNew;
            categoriaCostos.setCostosList(costosListNew);
            categoriaCostos = em.merge(categoriaCostos);
            if (idTipoCategoriaCostosOld != null && !idTipoCategoriaCostosOld.equals(idTipoCategoriaCostosNew)) {
                idTipoCategoriaCostosOld.getCategoriaCostosList().remove(categoriaCostos);
                idTipoCategoriaCostosOld = em.merge(idTipoCategoriaCostosOld);
            }
            if (idTipoCategoriaCostosNew != null && !idTipoCategoriaCostosNew.equals(idTipoCategoriaCostosOld)) {
                idTipoCategoriaCostosNew.getCategoriaCostosList().add(categoriaCostos);
                idTipoCategoriaCostosNew = em.merge(idTipoCategoriaCostosNew);
            }
            for (Costos costosListNewCostos : costosListNew) {
                if (!costosListOld.contains(costosListNewCostos)) {
                    CategoriaCostos oldIdCategoriaCostosOfCostosListNewCostos = costosListNewCostos.getIdCategoriaCostos();
                    costosListNewCostos.setIdCategoriaCostos(categoriaCostos);
                    costosListNewCostos = em.merge(costosListNewCostos);
                    if (oldIdCategoriaCostosOfCostosListNewCostos != null && !oldIdCategoriaCostosOfCostosListNewCostos.equals(categoriaCostos)) {
                        oldIdCategoriaCostosOfCostosListNewCostos.getCostosList().remove(costosListNewCostos);
                        oldIdCategoriaCostosOfCostosListNewCostos = em.merge(oldIdCategoriaCostosOfCostosListNewCostos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categoriaCostos.getIdCategoriaCostos();
                if (findCategoriaCostos(id) == null) {
                    throw new NonexistentEntityException("The categoriaCostos with id " + id + " no longer exists.");
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
            CategoriaCostos categoriaCostos;
            try {
                categoriaCostos = em.getReference(CategoriaCostos.class, id);
                categoriaCostos.getIdCategoriaCostos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoriaCostos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Costos> costosListOrphanCheck = categoriaCostos.getCostosList();
            for (Costos costosListOrphanCheckCostos : costosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("Este campo no puede ser eliminado por que es utilizado en otro registro.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoCategoriaCostos idTipoCategoriaCostos = categoriaCostos.getIdTipoCategoriaCostos();
            if (idTipoCategoriaCostos != null) {
                idTipoCategoriaCostos.getCategoriaCostosList().remove(categoriaCostos);
                idTipoCategoriaCostos = em.merge(idTipoCategoriaCostos);
            }
            em.remove(categoriaCostos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CategoriaCostos> findCategoriaCostosEntities() {
        return findCategoriaCostosEntities(true, -1, -1);
    }

    public List<CategoriaCostos> findCategoriaCostosEntities(int maxResults, int firstResult) {
        return findCategoriaCostosEntities(false, maxResults, firstResult);
    }

    private List<CategoriaCostos> findCategoriaCostosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CategoriaCostos.class));
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

    public CategoriaCostos findCategoriaCostos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CategoriaCostos.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaCostosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CategoriaCostos> rt = cq.from(CategoriaCostos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
