package com.demo.nativequery.repository;

import com.demo.nativequery.model.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Integer>,
        PagingAndSortingRepository<Employee, Integer> {

    //it's linked with the native query from Employee Entity Class
    Employee findByName(String name);

    //using positional parameters with question mark prefix followed by parameter sequence number like ?1,?2
    @Query(value = "select * from employees where id = ?1", nativeQuery = true)
    Employee findById(String name);


    @Query(value="select * from employees",
            countQuery = "select count(id) from employees",
            nativeQuery = true)
    Page<Employee> getEmployeePageAcsByCol(Pageable page);

    @Modifying
    @Query(value="update employees set name= ?1 where id = ?2", nativeQuery=true)
    void updateEmployeeById(String empName, int id);

    @Modifying
    // or can use named parameters with colon prefix followed by parameter like :id
    @Query(value="delete from employees e where e.id= :id", nativeQuery = true)
    void deleteEmployeeById(@Param("id") int id);
}
