package jp.co.axa.apidemo.dto;

import jp.co.axa.apidemo.entities.Employee;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String name;
    private Integer salary;
    private String department;

    /**
     * Converts an Employee entity to the DTO representation.
     *
     * @param employee The employee to convert.
     */
    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.salary = employee.getSalary();
        this.department = employee.getDepartment();
    }

    /**
     * Converts a DTO to it's Entity representation.
     *
     * @return An Employee Entity.
     */
    public Employee toEntity() {
        return new Employee(id, name, salary, department);
    }
}
