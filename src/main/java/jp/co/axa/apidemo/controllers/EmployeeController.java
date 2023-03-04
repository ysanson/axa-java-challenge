package jp.co.axa.apidemo.controllers;

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

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    private static final Logger logger = LogManager.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeService.retrieveEmployees();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        Optional<Employee> employee = employeeService.getEmployee(employeeId);
        if (employee.isPresent()) {
            return employee.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveEmployee(Employee employee){
        employeeService.saveEmployee(employee);
        logger.info("Employee saved successfully");
    }

    @DeleteMapping("/employees/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        employeeService.deleteEmployee(employeeId);
        logger.info("Employee Deleted Successfully");
    }

    @PutMapping("/employees/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){
        Optional<Employee> emp = employeeService.getEmployee(employeeId);
        if(emp.isPresent()){
            employeeService.updateEmployee(employee);
            logger.info("Employee updated successfully");
        }

    }

}
