package dao;

import model.Employee;
import util.Hibernate;

import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeDAO {

    private static final Logger log = LoggerFactory.getLogger(EmployeeDAO.class);

    public boolean addEmployee(Employee employee) {
        try (Session session = Hibernate.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(employee);

            tx.commit();
            log.info("Employee added successfully - ID: {}", employee.getId());
            return true;
        } catch (Exception e) {
            log.error("Failed to add employee", e);
            return false;
        }
    }

    public List<Employee> getAllEmployee() {
        try (Session session = Hibernate.getSessionFactory().openSession()) {
            log.debug("Fetching all employees from database");
            List<Employee> list = session.createQuery("from Employee", Employee.class).list();
            log.info("Retrieved {} employees from database", list.size());
            return list;

        } catch (Exception e) {
            log.error("Error while fetching employees", e);
            return Collections.emptyList();
        }
    }

    public boolean updateEmployee(Employee employee) {
        try (Session session = Hibernate.getSessionFactory().openSession()) {
            log.debug("Updating employee with ID: {}", employee.getId());

            Transaction tx = session.beginTransaction();

            session.merge(employee);

            tx.commit();
            return true;
        } catch (Exception e) {
            log.error("Error while updating employee with ID: {}", employee.getId(), e);
            return false;
        }
    }

    public boolean deleteEmployee(int id) {

        boolean delete = false;
        try (Session session = Hibernate.getSessionFactory().openSession()) {
            log.debug("Deleting employee with ID: {}", id);
            Transaction tx = session.beginTransaction();

            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.remove(employee);
                tx.commit();
                delete = true;
            } else {
                log.warn("Employee with ID: {} not found for deletion", id);
                tx.rollback();
            }
            return delete;
        } catch (Exception e) {
            log.error("Error while deleting employee with ID: {}", id, e);
            return false;
        }
    }
}