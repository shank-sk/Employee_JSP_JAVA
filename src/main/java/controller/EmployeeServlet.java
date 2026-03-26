package controller;

import dao.EmployeeDAO;
import model.Employee;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServlet.class);
    EmployeeDAO dao = new EmployeeDAO();

    @Override
    public void init() throws ServletException {
        System.out.println("Application URL: http://localhost:8080/");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        if ("list".equals(action)) {
            List<Employee> list = dao.getAllEmployee();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            writeEmployeesAsJson(response, list);
            return;
        }
        response.sendRedirect("index.html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect("employee");
            return;
        }
        log.info("Request received with action: {}", action);
        switch (action) {
            case "add":
                log.debug("Executing addEmployee action");
                addEmployee(request, response);
                break;
            case "update":
                log.debug("Executing updateEmployee action");
                updateEmployee(request, response);
                break;

            case "delete":
                log.debug("Executing deleteEmployee action");
                deleteEmployee(request, response);
                break;

            case "list":
                log.debug("Executing listEmployee action");
                listEmployee(request, response);
                break;
            default:
                response.sendRedirect("employee");
                break;
        }
    }

    private void addEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        int salary = Integer.parseInt(request.getParameter("salary"));
        String department = request.getParameter("department");

        log.info("Adding new employee - Name: {}, Email: {}, Department: {}", name, email, department);
        Employee emp = new Employee(name, email, salary, department);

        boolean result = dao.addEmployee(emp);
        if (result) {
            log.info("Employee added successfully");
        } else {
            log.error("Failed to add employee");
        }

        response.sendRedirect("employee");
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        int salary = Integer.parseInt(request.getParameter("salary"));
        String department = request.getParameter("department");

        Employee emp = new Employee(id, name, email, salary, department);
        dao.updateEmployee(emp);
        log.info("Fetching all employees");
        List<Employee> list = dao.getAllEmployee();
        log.info("Retrieved {} employees", list.size());
        response.sendRedirect("employee");
    }

    private void listEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.info("List action requested");
        response.sendRedirect("employee");
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        log.info("Deleting employee with ID: {}", id);

        boolean result = dao.deleteEmployee(id);
        if (result) {
            log.info("Employee deleted successfully");
        } else {
            log.error("Failed to delete employee with ID: {}", id);
        }

        response.sendRedirect("employee");
    }

    private void writeEmployeesAsJson(HttpServletResponse response, List<Employee> employees) throws IOException {
        PrintWriter out = response.getWriter();
        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            if (i > 0) {
                json.append(',');
            }

            json.append('{')
                    .append("\"id\":").append(employee.getId()).append(',')
                    .append("\"name\":\"").append(escapeJson(employee.getName())).append("\",")
                    .append("\"email\":\"").append(escapeJson(employee.getEmail())).append("\",")
                    .append("\"salary\":").append(employee.getSalary()).append(',')
                    .append("\"department\":\"").append(escapeJson(employee.getDepartment())).append("\"")
                    .append('}');
        }

        json.append(']');
        out.write(json.toString());
        out.flush();
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}