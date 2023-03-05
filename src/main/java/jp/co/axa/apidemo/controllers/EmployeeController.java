package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.dto.EmployeeDTO;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Rest controller for the /api/v1/employees route.
 */
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private static final Logger logger = LogManager.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    /**
     * Gets all the employees.
     * 
     * @return The list of employees in the database.
     */
    @GetMapping("")
    public List<EmployeeDTO> getEmployees() {
        return employeeService
                .retrieveEmployees()
                .stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets an employee by its ID.
     *
     * @param employeeId The employee ID to search for.
     * @return The employee if it exists, 404 otherwise.
     * @throws ResponseStatusException if the employee is not found.
     */
    @GetMapping("/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        Optional<Employee> employee = employeeService.getEmployee(employeeId);
        if (employee.isPresent()) {
            return new EmployeeDTO(employee.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
    }

    /**
     * Creates a new employee in the database.
     * The new employee must not have an ID, otherwise, a bad request code is sent
     * back.
     * 
     * @param employee The employee to store
     * @throws ResponseStatusException if the inserted employee has an ID.
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveEmployee(@RequestBody EmployeeDTO employee) {
        if (employee.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A created user cannot have an ID");
        }
        employeeService.saveEmployee(employee.toEntity());
        logger.info("Employee saved successfully");
    }

    /**
     * Deletes an employee by its ID.
     * 
     * @param employeeId The employee ID to delete.
     */
    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        logger.info("Employee Deleted Successfully");
    }

    /**
     * Updates an employee given it's ID and the request body.
     * If the ID in the body is different from the ID in the URI, a bad request code
     * is sent back.
     *
     * @param employee   The employee data to update.
     * @param employeeId The employee ID to update.
     */
    @PutMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEmployee(@RequestBody EmployeeDTO employee,
            @PathVariable(name = "employeeId") Long employeeId) {
        if (employeeId != employee.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in the URI and in the body doesn't match");
        }
        Optional<Employee> emp = employeeService.getEmployee(employeeId);
        if (emp.isPresent()) {
            employeeService.updateEmployee(employee.toEntity());
            logger.info("Employee updated successfully");
        }

    }

}
