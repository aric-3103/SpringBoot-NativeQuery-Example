package com.demo.nativequery.controller;

import com.demo.nativequery.model.Employee;
import com.demo.nativequery.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/v1/api/emp")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * Get the employee by name
     *
     * @param name
     * @return ResponseEntity
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getEmployeeByName(@PathVariable("name") String name) {

        try {
            // retrieve the record from database
            Optional<Employee> emoObj = Optional.ofNullable(employeeRepository.findByName(name));

            //check if employee exist in database
            if (emoObj.isPresent()) {
                return new ResponseEntity<>(emoObj.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all the employees
     *
     * @return ResponseEntity
     */
    @GetMapping("/getEmp")
    public ResponseEntity<List<Employee>> getAllEmployee() {
        try {
            return new ResponseEntity<>(employeeRepository.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create new employee
     *
     * @param employee
     * @return ResponseEntity
     */
    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee newEmployee = employeeRepository
                .save(Employee.builder()
                        .empName(employee.getEmpName())
                        .role(employee.getRole())
                        .build());
        return new ResponseEntity<>(newEmployee, HttpStatus.OK);
    }

    /**
     * Update Employee record by it's id
     *
     * @param id
     * @return
     */
    @PutMapping("/update/{id}/name/{empName}")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable("id") int id,
                                                       @PathVariable("empName") String empName) {

        //check if employee exist in database
        Employee emmObj = getEmpRecord(id);

        if (emmObj != null) {
            employeeRepository.updateEmployeeById(empName, id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Delete Employee by Id
     *
     * @param id
     * @return ResponseEntity
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") int id) {
        try {
            Employee obj = getEmpRecord(id);

            if (obj != null) {
                employeeRepository.deleteEmployeeById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    /**
     * Method to get the employee record by id
     *
     * @param id
     * @return Employee
     */
    private Employee getEmpRecord(int id) {
        Optional<Employee> opEmp = employeeRepository.findById(id);

        if (opEmp.isPresent()) {
            return opEmp.get();
        }
        return null;
    }

    /**
     * Method to get the employee record by id
     *
     * @return Employee
     * @paramid
     */
    @GetMapping("/employee/pagination/sortby/{columnName}")
    public ResponseEntity<List<Employee>> getEmpPaginationAsc(@PathVariable("columnName") String columnName) {
        try {
            Pageable pageRequest = PageRequest.of(0, 5, Sort.by(columnName).ascending());

            // retrieve the record from database
            Optional<List<Employee>> empObj = Optional.ofNullable(
                    employeeRepository.getEmployeePageAcsByCol(pageRequest)
                            .getContent());

            if (empObj.isPresent()) {
                return new ResponseEntity<>(empObj.get(), HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
