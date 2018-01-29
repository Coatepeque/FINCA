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
import entidades.TipoEmp;
import java.util.ArrayList;
import java.util.List;
import entidades.DetalleCuentaBancaria;
import entidades.Empleado;
import entidades.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author josue
 */
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) {
        if (empleado.getTipoEmpList() == null) {
            empleado.setTipoEmpList(new ArrayList<TipoEmp>());
        }
        if (empleado.getDetalleCuentaBancariaList() == null) {
            empleado.setDetalleCuentaBancariaList(new ArrayList<DetalleCuentaBancaria>());
        }
        if (empleado.getUsuarioList() == null) {
            empleado.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TipoEmp> attachedTipoEmpList = new ArrayList<TipoEmp>();
            for (TipoEmp tipoEmpListTipoEmpToAttach : empleado.getTipoEmpList()) {
                tipoEmpListTipoEmpToAttach = em.getReference(tipoEmpListTipoEmpToAttach.getClass(), tipoEmpListTipoEmpToAttach.getIdTipoEmp());
                attachedTipoEmpList.add(tipoEmpListTipoEmpToAttach);
            }
            empleado.setTipoEmpList(attachedTipoEmpList);
            List<DetalleCuentaBancaria> attachedDetalleCuentaBancariaList = new ArrayList<DetalleCuentaBancaria>();
            for (DetalleCuentaBancaria detalleCuentaBancariaListDetalleCuentaBancariaToAttach : empleado.getDetalleCuentaBancariaList()) {
                detalleCuentaBancariaListDetalleCuentaBancariaToAttach = em.getReference(detalleCuentaBancariaListDetalleCuentaBancariaToAttach.getClass(), detalleCuentaBancariaListDetalleCuentaBancariaToAttach.getIdDetalleCuenta());
                attachedDetalleCuentaBancariaList.add(detalleCuentaBancariaListDetalleCuentaBancariaToAttach);
            }
            empleado.setDetalleCuentaBancariaList(attachedDetalleCuentaBancariaList);
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : empleado.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdUsuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            empleado.setUsuarioList(attachedUsuarioList);
            em.persist(empleado);
            for (TipoEmp tipoEmpListTipoEmp : empleado.getTipoEmpList()) {
                Empleado oldEMPLEADOidempleadoOfTipoEmpListTipoEmp = tipoEmpListTipoEmp.getEMPLEADOidempleado();
                tipoEmpListTipoEmp.setEMPLEADOidempleado(empleado);
                tipoEmpListTipoEmp = em.merge(tipoEmpListTipoEmp);
                if (oldEMPLEADOidempleadoOfTipoEmpListTipoEmp != null) {
                    oldEMPLEADOidempleadoOfTipoEmpListTipoEmp.getTipoEmpList().remove(tipoEmpListTipoEmp);
                    oldEMPLEADOidempleadoOfTipoEmpListTipoEmp = em.merge(oldEMPLEADOidempleadoOfTipoEmpListTipoEmp);
                }
            }
            for (DetalleCuentaBancaria detalleCuentaBancariaListDetalleCuentaBancaria : empleado.getDetalleCuentaBancariaList()) {
                Empleado oldEMPLEADOidempleadoOfDetalleCuentaBancariaListDetalleCuentaBancaria = detalleCuentaBancariaListDetalleCuentaBancaria.getEMPLEADOidempleado();
                detalleCuentaBancariaListDetalleCuentaBancaria.setEMPLEADOidempleado(empleado);
                detalleCuentaBancariaListDetalleCuentaBancaria = em.merge(detalleCuentaBancariaListDetalleCuentaBancaria);
                if (oldEMPLEADOidempleadoOfDetalleCuentaBancariaListDetalleCuentaBancaria != null) {
                    oldEMPLEADOidempleadoOfDetalleCuentaBancariaListDetalleCuentaBancaria.getDetalleCuentaBancariaList().remove(detalleCuentaBancariaListDetalleCuentaBancaria);
                    oldEMPLEADOidempleadoOfDetalleCuentaBancariaListDetalleCuentaBancaria = em.merge(oldEMPLEADOidempleadoOfDetalleCuentaBancariaListDetalleCuentaBancaria);
                }
            }
            for (Usuario usuarioListUsuario : empleado.getUsuarioList()) {
                Empleado oldEMPLEADOidempleadoOfUsuarioListUsuario = usuarioListUsuario.getEMPLEADOidempleado();
                usuarioListUsuario.setEMPLEADOidempleado(empleado);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldEMPLEADOidempleadoOfUsuarioListUsuario != null) {
                    oldEMPLEADOidempleadoOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldEMPLEADOidempleadoOfUsuarioListUsuario = em.merge(oldEMPLEADOidempleadoOfUsuarioListUsuario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getIdEmpleado());
            List<TipoEmp> tipoEmpListOld = persistentEmpleado.getTipoEmpList();
            List<TipoEmp> tipoEmpListNew = empleado.getTipoEmpList();
            List<DetalleCuentaBancaria> detalleCuentaBancariaListOld = persistentEmpleado.getDetalleCuentaBancariaList();
            List<DetalleCuentaBancaria> detalleCuentaBancariaListNew = empleado.getDetalleCuentaBancariaList();
            List<Usuario> usuarioListOld = persistentEmpleado.getUsuarioList();
            List<Usuario> usuarioListNew = empleado.getUsuarioList();
            List<String> illegalOrphanMessages = null;
            for (TipoEmp tipoEmpListOldTipoEmp : tipoEmpListOld) {
                if (!tipoEmpListNew.contains(tipoEmpListOldTipoEmp)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TipoEmp " + tipoEmpListOldTipoEmp + " since its EMPLEADOidempleado field is not nullable.");
                }
            }
            for (DetalleCuentaBancaria detalleCuentaBancariaListOldDetalleCuentaBancaria : detalleCuentaBancariaListOld) {
                if (!detalleCuentaBancariaListNew.contains(detalleCuentaBancariaListOldDetalleCuentaBancaria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleCuentaBancaria " + detalleCuentaBancariaListOldDetalleCuentaBancaria + " since its EMPLEADOidempleado field is not nullable.");
                }
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioListOldUsuario + " since its EMPLEADOidempleado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<TipoEmp> attachedTipoEmpListNew = new ArrayList<TipoEmp>();
            for (TipoEmp tipoEmpListNewTipoEmpToAttach : tipoEmpListNew) {
                tipoEmpListNewTipoEmpToAttach = em.getReference(tipoEmpListNewTipoEmpToAttach.getClass(), tipoEmpListNewTipoEmpToAttach.getIdTipoEmp());
                attachedTipoEmpListNew.add(tipoEmpListNewTipoEmpToAttach);
            }
            tipoEmpListNew = attachedTipoEmpListNew;
            empleado.setTipoEmpList(tipoEmpListNew);
            List<DetalleCuentaBancaria> attachedDetalleCuentaBancariaListNew = new ArrayList<DetalleCuentaBancaria>();
            for (DetalleCuentaBancaria detalleCuentaBancariaListNewDetalleCuentaBancariaToAttach : detalleCuentaBancariaListNew) {
                detalleCuentaBancariaListNewDetalleCuentaBancariaToAttach = em.getReference(detalleCuentaBancariaListNewDetalleCuentaBancariaToAttach.getClass(), detalleCuentaBancariaListNewDetalleCuentaBancariaToAttach.getIdDetalleCuenta());
                attachedDetalleCuentaBancariaListNew.add(detalleCuentaBancariaListNewDetalleCuentaBancariaToAttach);
            }
            detalleCuentaBancariaListNew = attachedDetalleCuentaBancariaListNew;
            empleado.setDetalleCuentaBancariaList(detalleCuentaBancariaListNew);
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            empleado.setUsuarioList(usuarioListNew);
            empleado = em.merge(empleado);
            for (TipoEmp tipoEmpListNewTipoEmp : tipoEmpListNew) {
                if (!tipoEmpListOld.contains(tipoEmpListNewTipoEmp)) {
                    Empleado oldEMPLEADOidempleadoOfTipoEmpListNewTipoEmp = tipoEmpListNewTipoEmp.getEMPLEADOidempleado();
                    tipoEmpListNewTipoEmp.setEMPLEADOidempleado(empleado);
                    tipoEmpListNewTipoEmp = em.merge(tipoEmpListNewTipoEmp);
                    if (oldEMPLEADOidempleadoOfTipoEmpListNewTipoEmp != null && !oldEMPLEADOidempleadoOfTipoEmpListNewTipoEmp.equals(empleado)) {
                        oldEMPLEADOidempleadoOfTipoEmpListNewTipoEmp.getTipoEmpList().remove(tipoEmpListNewTipoEmp);
                        oldEMPLEADOidempleadoOfTipoEmpListNewTipoEmp = em.merge(oldEMPLEADOidempleadoOfTipoEmpListNewTipoEmp);
                    }
                }
            }
            for (DetalleCuentaBancaria detalleCuentaBancariaListNewDetalleCuentaBancaria : detalleCuentaBancariaListNew) {
                if (!detalleCuentaBancariaListOld.contains(detalleCuentaBancariaListNewDetalleCuentaBancaria)) {
                    Empleado oldEMPLEADOidempleadoOfDetalleCuentaBancariaListNewDetalleCuentaBancaria = detalleCuentaBancariaListNewDetalleCuentaBancaria.getEMPLEADOidempleado();
                    detalleCuentaBancariaListNewDetalleCuentaBancaria.setEMPLEADOidempleado(empleado);
                    detalleCuentaBancariaListNewDetalleCuentaBancaria = em.merge(detalleCuentaBancariaListNewDetalleCuentaBancaria);
                    if (oldEMPLEADOidempleadoOfDetalleCuentaBancariaListNewDetalleCuentaBancaria != null && !oldEMPLEADOidempleadoOfDetalleCuentaBancariaListNewDetalleCuentaBancaria.equals(empleado)) {
                        oldEMPLEADOidempleadoOfDetalleCuentaBancariaListNewDetalleCuentaBancaria.getDetalleCuentaBancariaList().remove(detalleCuentaBancariaListNewDetalleCuentaBancaria);
                        oldEMPLEADOidempleadoOfDetalleCuentaBancariaListNewDetalleCuentaBancaria = em.merge(oldEMPLEADOidempleadoOfDetalleCuentaBancariaListNewDetalleCuentaBancaria);
                    }
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Empleado oldEMPLEADOidempleadoOfUsuarioListNewUsuario = usuarioListNewUsuario.getEMPLEADOidempleado();
                    usuarioListNewUsuario.setEMPLEADOidempleado(empleado);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldEMPLEADOidempleadoOfUsuarioListNewUsuario != null && !oldEMPLEADOidempleadoOfUsuarioListNewUsuario.equals(empleado)) {
                        oldEMPLEADOidempleadoOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldEMPLEADOidempleadoOfUsuarioListNewUsuario = em.merge(oldEMPLEADOidempleadoOfUsuarioListNewUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empleado.getIdEmpleado();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
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
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getIdEmpleado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TipoEmp> tipoEmpListOrphanCheck = empleado.getTipoEmpList();
            for (TipoEmp tipoEmpListOrphanCheckTipoEmp : tipoEmpListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the TipoEmp " + tipoEmpListOrphanCheckTipoEmp + " in its tipoEmpList field has a non-nullable EMPLEADOidempleado field.");
            }
            List<DetalleCuentaBancaria> detalleCuentaBancariaListOrphanCheck = empleado.getDetalleCuentaBancariaList();
            for (DetalleCuentaBancaria detalleCuentaBancariaListOrphanCheckDetalleCuentaBancaria : detalleCuentaBancariaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the DetalleCuentaBancaria " + detalleCuentaBancariaListOrphanCheckDetalleCuentaBancaria + " in its detalleCuentaBancariaList field has a non-nullable EMPLEADOidempleado field.");
            }
            List<Usuario> usuarioListOrphanCheck = empleado.getUsuarioList();
            for (Usuario usuarioListOrphanCheckUsuario : usuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Usuario " + usuarioListOrphanCheckUsuario + " in its usuarioList field has a non-nullable EMPLEADOidempleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
