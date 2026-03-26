package dao;

import model.Employee;
import util.DBConnection;

import java.sql.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EmployeeDAO {

    private static final Logger log = LoggerFactory.getLogger(EmployeeDAO.class);

    public boolean addEmployee(Employee employee) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO employee(name,email,salary,department) VALUES(?,?,?,?)";

            PreparedStatement pre = con.prepareStatement(query);

            pre.setString(1, employee.getName());
            pre.setString(2, employee.getEmail());
            pre.setInt(3, employee.getSalary());
            pre.setString(4, employee.getDepartment());

            int row = pre.executeUpdate();
            log.info("Data added successfully");
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Failed to add data");
            return false;
        }
    }

    public List<Employee> getAllEmployee(){
        List<Employee> list = new ArrayList<>();
        try{
            log.debug("Fetching all employees from database");
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM employee";

            PreparedStatement pre = con.prepareStatement(query);
            ResultSet rs = pre.executeQuery();
            while(rs.next()){
                Employee employee = new Employee();

                employee.setId(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                employee.setEmail(rs.getString("email"));
                employee.setSalary(rs.getInt("salary"));
                employee.setDepartment(rs.getString("department"));

                list.add(employee);
            }
            log.info("Retrieved {} employees from database", list.size());

        }catch(Exception e) {
            log.error("Error while fetching employees", e);
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateEmployee(Employee employee){

        try{
            log.debug("Updating employee with ID: {}", employee.getId());
            Connection con = DBConnection.getConnection();

            String query = "UPDATE employee SET name = ?, email = ?, salary = ?, department = ? WHERE id = ?";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setString(1, employee.getName());
            pre.setString(2, employee.getEmail());
            pre.setInt(3, employee.getSalary());
            pre.setString(4, employee.getDepartment());
            pre.setInt(5, employee.getId());

            int row = pre.executeUpdate();
            if (row > 0) {
                log.info("Employee updated successfully - ID: {}", employee.getId());
            }
            return row > 0;
        }catch (Exception e){
            log.error("Error while updating employee with ID: {}", employee.getId(), e);
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEmployee(int id){

        boolean delete = false;
        try{
            log.debug("Deleting employee with ID: {}", id);
            Connection con = DBConnection.getConnection();

            String query = "DELETE FROM employee WHERE id = ?";

            PreparedStatement pre = con.prepareStatement(query);

            pre.setInt(1,id);

            int row = pre.executeUpdate();
            if (row > 0) {
                log.info("Employee deleted successfully - ID: {}", id);
            }
            return row > 0;
        }catch(Exception e){
            log.error("Error while deleting employee with ID: {}", id, e);
            e.printStackTrace();
            return false;
        }
    }
}