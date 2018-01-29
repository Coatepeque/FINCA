/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import entidades.Banco;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.DetalleCuentaBancaria;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author josue
 */
public class BancoJpaController implements Serializable {

    public BancoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Banco banco) {
        if (banco.getDetalleCuentaBancariaList() == null) {
            banco.setDetalleCuentaBancariaList(new ArrayList<DetalleCuentaBancaria>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<DetalleCuentaBancaria> attachedDetalleCuentaBancariaList = new ArrayList<DetalleCuentaBancaria>();
            for (DetalleCuentaBancaria detalleCuentaBancariaListDetalleCuentaBancariaToAttach : banco.getDetalleCuentaBancariaList()) {
                detalleCuentaBancariaListDetalleCuentaBancariaToAttach = em.getReference(detalleCuentaBancariaListDetalleCuentaBancariaToAttach.getClass(), detalleCuentaBancariaListDetalleCuentaBancariaToAttach.getIdDetalleCuenta());
                attachedDetalleCuentaBancariaList.add(detalleCuentaBancariaListDetalleCuentaBancariaToAttach);
            }
            banco.setDetalleCuentaBancariaList(attachedDetalleCuentaBancariaList);
            em.persist(banco);
            for (DetalleCuentaBancaria detalleCuentaBancariaListDetalleCuentaBancaria : banco.getDetalleCuentaBancariaList()) {
                Banco oldBANCOidbancoOfDetalleCuentaBancariaListDetalleCuentaBancaria = detalleCuentaBancariaListDetalleCuentaBancaria.getBANCOidbanco();
                detalleCuentaBancariaListDetalleCuentaBancaria.setBANCOidbanco(banco);
                detalleCuentaBancariaListDetalleCuentaBancaria = em.merge(detalleCuentaBancariaListDetalleCuentaBancaria);
                if (oldBANCOidbancoOfDetalleCuentaBancariaListDetalleCuentaBancaria != null) {
                    oldBANCOidbancoOfDetalleCuentaBancariaListDetalleCuentaBancaria.getDetalleCuentaBancariaList().remove(detalleCuentaBancariaListDetalleCuentaBancaria);
                    oldBANCOidbancoOfDetalleCuentaBancariaListDetalleCuentaBancaria = em.merge(oldBANCOidbancoOfDetalleCuentaBancariaListDetalleCuentaBancaria);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Banco banco) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Banco persistentBanco = em.find(Banco.class, banco.getIdBanco());
            List<DetalleCuentaBancaria> detalleCuentaBancariaListOld = persistentBanco.getDetalleCuentaBancariaList();
            List<DetalleCuentaBancaria> detalleCuentaBancariaListNew = banco.getDetalleCuentaBancariaList();
            List<String> illegalOrphanMessages = null;
            for (DetalleCuentaBancaria detalleCuentaBancariaListOldDetalleCuentaBancaria : detalleCuentaBancariaListOld) {
                if (!detalleCuentaBancariaListNew.contains(detalleCuentaBancariaListOldDetalleCuentaBancaria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleCuentaBancaria " + detalleCuentaBancariaListOldDetalleCuentaBancaria + " since its BANCOidbanco field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<DetalleCuentaBancaria> attachedDetalleCuentaBancariaListNew = new ArrayList<DetalleCuentaBancaria>();
            for (DetalleCuentaBancaria detalleCuentaBancariaListNewDetalleCuentaBancariaToAttach : detalleCuentaBancariaListNew) {
                detalleCuentaBancariaListNewDetalleCuentaBancariaToAttach = em.getReference(detalleCuentaBancariaListNewDetalleCuentaBancariaToAttach.getClass(), detalleCuentaBancariaListNewDetalleCuentaBancariaToAttach.getIdDetalleCuenta());
                attachedDetalleCuentaBancariaListNew.add(detalleCuentaBancariaListNewDetalleCuentaBancariaToAttach);
            }
            detalleCuentaBancariaListNew = attachedDetalleCuentaBancariaListNew;
            banco.setDetalleCuentaBancariaList(detalleCuentaBancariaListNew);
            banco = em.merge(banco);
            for (DetalleCuentaBancaria detalleCuentaBancariaListNewDetalleCuentaBancaria : detalleCuentaBancariaListNew) {
                if (!detalleCuentaBancariaListOld.contains(detalleCuentaBancariaListNewDetalleCuentaBancaria)) {
                    Banco oldBANCOidbancoOfDetalleCuentaBancariaListNewDetalleCuentaBancaria = detalleCuentaBancariaListNewDetalleCuentaBancaria.getBANCOidbanco();
                    detalleCuentaBancariaListNewDetalleCuentaBancaria.setBANCOidbanco(banco);
                    detalleCuentaBancariaListNewDetalleCuentaBancaria = em.merge(detalleCuentaBancariaListNewDetalleCuentaBancaria);
                    if (oldBANCOidbancoOfDetalleCuentaBancariaListNewDetalleCuentaBancaria != null && !oldBANCOidbancoOfDetalleCuentaBancariaListNewDetalleCuentaBancaria.equals(banco)) {
                        oldBANCOidbancoOfDetalleCuentaBancariaListNewDetalleCuentaBancaria.getDetalleCuentaBancariaList().remove(detalleCuentaBancariaListNewDetalleCuentaBancaria);
                        oldBANCOidbancoOfDetalleCuentaBancariaListNewDetalleCuentaBancaria = em.merge(oldBANCOidbancoOfDetalleCuentaBancariaListNewDetalleCuentaBancaria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = banco.getIdBanco();
                if (findBanco(id) == null) {
                    throw new NonexistentEntityException("The banco with id " + id + " no longer exists.");
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
            Banco banco;
            try {
                banco = em.getReference(Banco.class, id);
                banco.getIdBanco();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The banco with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleCuentaBancaria> detalleCuentaBancariaListOrphanCheck = banco.getDetalleCuentaBancariaList();
            for (DetalleCuentaBancaria detalleCuentaBancariaListOrphanCheckDetalleCuentaBancaria : detalleCuentaBancariaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Banco (" + banco + ") cannot be destroyed since the DetalleCuentaBancaria " + detalleCuentaBancariaListOrphanCheckDetalleCuentaBancaria + " in its detalleCuentaBancariaList field has a non-nullable BANCOidbanco field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(banco);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Banco> findBancoEntities() {
        return findBancoEntities(true, -1, -1);
    }

    public List<Banco> findBancoEntities(int maxResults, int firstResult) {
        return findBancoEntities(false, maxResults, firstResult);
    }

    private List<Banco> findBancoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Banco.class));
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

    public Banco findBanco(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Banco.class, id);
        } finally {
            em.close();
        }
    }

    public int getBancoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Banco> rt = cq.from(Banco.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
