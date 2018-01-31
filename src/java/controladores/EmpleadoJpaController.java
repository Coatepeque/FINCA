/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import entidades.Empleado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Finca;
import entidades.TipoEmpleado;
import entidades.ReporteDiario;
import java.util.ArrayList;
import java.util.List;
import entidades.Planilla;
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
        if (empleado.getReporteDiarioList() == null) {
            empleado.setReporteDiarioList(new ArrayList<ReporteDiario>());
        }
        if (empleado.getPlanillaList() == null) {
            empleado.setPlanillaList(new ArrayList<Planilla>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Finca idFinca = empleado.getIdFinca();
            if (idFinca != null) {
                idFinca = em.getReference(idFinca.getClass(), idFinca.getIdFinca());
                empleado.setIdFinca(idFinca);
            }
            TipoEmpleado idTipoEmpleado = empleado.getIdTipoEmpleado();
            if (idTipoEmpleado != null) {
                idTipoEmpleado = em.getReference(idTipoEmpleado.getClass(), idTipoEmpleado.getIdTipoEmpleado());
                empleado.setIdTipoEmpleado(idTipoEmpleado);
            }
            List<ReporteDiario> attachedReporteDiarioList = new ArrayList<ReporteDiario>();
            for (ReporteDiario reporteDiarioListReporteDiarioToAttach : empleado.getReporteDiarioList()) {
                reporteDiarioListReporteDiarioToAttach = em.getReference(reporteDiarioListReporteDiarioToAttach.getClass(), reporteDiarioListReporteDiarioToAttach.getIdReporteDiario());
                attachedReporteDiarioList.add(reporteDiarioListReporteDiarioToAttach);
            }
            empleado.setReporteDiarioList(attachedReporteDiarioList);
            List<Planilla> attachedPlanillaList = new ArrayList<Planilla>();
            for (Planilla planillaListPlanillaToAttach : empleado.getPlanillaList()) {
                planillaListPlanillaToAttach = em.getReference(planillaListPlanillaToAttach.getClass(), planillaListPlanillaToAttach.getIdPlanilla());
                attachedPlanillaList.add(planillaListPlanillaToAttach);
            }
            empleado.setPlanillaList(attachedPlanillaList);
            em.persist(empleado);
            if (idFinca != null) {
                idFinca.getEmpleadoList().add(empleado);
                idFinca = em.merge(idFinca);
            }
            if (idTipoEmpleado != null) {
                idTipoEmpleado.getEmpleadoList().add(empleado);
                idTipoEmpleado = em.merge(idTipoEmpleado);
            }
            for (ReporteDiario reporteDiarioListReporteDiario : empleado.getReporteDiarioList()) {
                Empleado oldIdEmpleadoOfReporteDiarioListReporteDiario = reporteDiarioListReporteDiario.getIdEmpleado();
                reporteDiarioListReporteDiario.setIdEmpleado(empleado);
                reporteDiarioListReporteDiario = em.merge(reporteDiarioListReporteDiario);
                if (oldIdEmpleadoOfReporteDiarioListReporteDiario != null) {
                    oldIdEmpleadoOfReporteDiarioListReporteDiario.getReporteDiarioList().remove(reporteDiarioListReporteDiario);
                    oldIdEmpleadoOfReporteDiarioListReporteDiario = em.merge(oldIdEmpleadoOfReporteDiarioListReporteDiario);
                }
            }
            for (Planilla planillaListPlanilla : empleado.getPlanillaList()) {
                Empleado oldIdEmpleadoOfPlanillaListPlanilla = planillaListPlanilla.getIdEmpleado();
                planillaListPlanilla.setIdEmpleado(empleado);
                planillaListPlanilla = em.merge(planillaListPlanilla);
                if (oldIdEmpleadoOfPlanillaListPlanilla != null) {
                    oldIdEmpleadoOfPlanillaListPlanilla.getPlanillaList().remove(planillaListPlanilla);
                    oldIdEmpleadoOfPlanillaListPlanilla = em.merge(oldIdEmpleadoOfPlanillaListPlanilla);
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
            Finca idFincaOld = persistentEmpleado.getIdFinca();
            Finca idFincaNew = empleado.getIdFinca();
            TipoEmpleado idTipoEmpleadoOld = persistentEmpleado.getIdTipoEmpleado();
            TipoEmpleado idTipoEmpleadoNew = empleado.getIdTipoEmpleado();
            List<ReporteDiario> reporteDiarioListOld = persistentEmpleado.getReporteDiarioList();
            List<ReporteDiario> reporteDiarioListNew = empleado.getReporteDiarioList();
            List<Planilla> planillaListOld = persistentEmpleado.getPlanillaList();
            List<Planilla> planillaListNew = empleado.getPlanillaList();
            List<String> illegalOrphanMessages = null;
            for (ReporteDiario reporteDiarioListOldReporteDiario : reporteDiarioListOld) {
                if (!reporteDiarioListNew.contains(reporteDiarioListOldReporteDiario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ReporteDiario " + reporteDiarioListOldReporteDiario + " since its idEmpleado field is not nullable.");
                }
            }
            for (Planilla planillaListOldPlanilla : planillaListOld) {
                if (!planillaListNew.contains(planillaListOldPlanilla)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Planilla " + planillaListOldPlanilla + " since its idEmpleado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idFincaNew != null) {
                idFincaNew = em.getReference(idFincaNew.getClass(), idFincaNew.getIdFinca());
                empleado.setIdFinca(idFincaNew);
            }
            if (idTipoEmpleadoNew != null) {
                idTipoEmpleadoNew = em.getReference(idTipoEmpleadoNew.getClass(), idTipoEmpleadoNew.getIdTipoEmpleado());
                empleado.setIdTipoEmpleado(idTipoEmpleadoNew);
            }
            List<ReporteDiario> attachedReporteDiarioListNew = new ArrayList<ReporteDiario>();
            for (ReporteDiario reporteDiarioListNewReporteDiarioToAttach : reporteDiarioListNew) {
                reporteDiarioListNewReporteDiarioToAttach = em.getReference(reporteDiarioListNewReporteDiarioToAttach.getClass(), reporteDiarioListNewReporteDiarioToAttach.getIdReporteDiario());
                attachedReporteDiarioListNew.add(reporteDiarioListNewReporteDiarioToAttach);
            }
            reporteDiarioListNew = attachedReporteDiarioListNew;
            empleado.setReporteDiarioList(reporteDiarioListNew);
            List<Planilla> attachedPlanillaListNew = new ArrayList<Planilla>();
            for (Planilla planillaListNewPlanillaToAttach : planillaListNew) {
                planillaListNewPlanillaToAttach = em.getReference(planillaListNewPlanillaToAttach.getClass(), planillaListNewPlanillaToAttach.getIdPlanilla());
                attachedPlanillaListNew.add(planillaListNewPlanillaToAttach);
            }
            planillaListNew = attachedPlanillaListNew;
            empleado.setPlanillaList(planillaListNew);
            empleado = em.merge(empleado);
            if (idFincaOld != null && !idFincaOld.equals(idFincaNew)) {
                idFincaOld.getEmpleadoList().remove(empleado);
                idFincaOld = em.merge(idFincaOld);
            }
            if (idFincaNew != null && !idFincaNew.equals(idFincaOld)) {
                idFincaNew.getEmpleadoList().add(empleado);
                idFincaNew = em.merge(idFincaNew);
            }
            if (idTipoEmpleadoOld != null && !idTipoEmpleadoOld.equals(idTipoEmpleadoNew)) {
                idTipoEmpleadoOld.getEmpleadoList().remove(empleado);
                idTipoEmpleadoOld = em.merge(idTipoEmpleadoOld);
            }
            if (idTipoEmpleadoNew != null && !idTipoEmpleadoNew.equals(idTipoEmpleadoOld)) {
                idTipoEmpleadoNew.getEmpleadoList().add(empleado);
                idTipoEmpleadoNew = em.merge(idTipoEmpleadoNew);
            }
            for (ReporteDiario reporteDiarioListNewReporteDiario : reporteDiarioListNew) {
                if (!reporteDiarioListOld.contains(reporteDiarioListNewReporteDiario)) {
                    Empleado oldIdEmpleadoOfReporteDiarioListNewReporteDiario = reporteDiarioListNewReporteDiario.getIdEmpleado();
                    reporteDiarioListNewReporteDiario.setIdEmpleado(empleado);
                    reporteDiarioListNewReporteDiario = em.merge(reporteDiarioListNewReporteDiario);
                    if (oldIdEmpleadoOfReporteDiarioListNewReporteDiario != null && !oldIdEmpleadoOfReporteDiarioListNewReporteDiario.equals(empleado)) {
                        oldIdEmpleadoOfReporteDiarioListNewReporteDiario.getReporteDiarioList().remove(reporteDiarioListNewReporteDiario);
                        oldIdEmpleadoOfReporteDiarioListNewReporteDiario = em.merge(oldIdEmpleadoOfReporteDiarioListNewReporteDiario);
                    }
                }
            }
            for (Planilla planillaListNewPlanilla : planillaListNew) {
                if (!planillaListOld.contains(planillaListNewPlanilla)) {
                    Empleado oldIdEmpleadoOfPlanillaListNewPlanilla = planillaListNewPlanilla.getIdEmpleado();
                    planillaListNewPlanilla.setIdEmpleado(empleado);
                    planillaListNewPlanilla = em.merge(planillaListNewPlanilla);
                    if (oldIdEmpleadoOfPlanillaListNewPlanilla != null && !oldIdEmpleadoOfPlanillaListNewPlanilla.equals(empleado)) {
                        oldIdEmpleadoOfPlanillaListNewPlanilla.getPlanillaList().remove(planillaListNewPlanilla);
                        oldIdEmpleadoOfPlanillaListNewPlanilla = em.merge(oldIdEmpleadoOfPlanillaListNewPlanilla);
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
            List<ReporteDiario> reporteDiarioListOrphanCheck = empleado.getReporteDiarioList();
            for (ReporteDiario reporteDiarioListOrphanCheckReporteDiario : reporteDiarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the ReporteDiario " + reporteDiarioListOrphanCheckReporteDiario + " in its reporteDiarioList field has a non-nullable idEmpleado field.");
            }
            List<Planilla> planillaListOrphanCheck = empleado.getPlanillaList();
            for (Planilla planillaListOrphanCheckPlanilla : planillaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Planilla " + planillaListOrphanCheckPlanilla + " in its planillaList field has a non-nullable idEmpleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Finca idFinca = empleado.getIdFinca();
            if (idFinca != null) {
                idFinca.getEmpleadoList().remove(empleado);
                idFinca = em.merge(idFinca);
            }
            TipoEmpleado idTipoEmpleado = empleado.getIdTipoEmpleado();
            if (idTipoEmpleado != null) {
                idTipoEmpleado.getEmpleadoList().remove(empleado);
                idTipoEmpleado = em.merge(idTipoEmpleado);
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
