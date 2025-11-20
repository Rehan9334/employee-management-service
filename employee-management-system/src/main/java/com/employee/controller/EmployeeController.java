package com.employee.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.employee.entity.Employee;
import com.employee.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService svc;
    public EmployeeController(EmployeeService svc) { this.svc = svc; }

    @GetMapping
    public ResponseEntity<Page<Employee>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(svc.findAllPaged(page, size, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> get(@PathVariable Long id) {
        return ResponseEntity.ok(svc.findById(id));
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee e) {
        return ResponseEntity.ok(svc.create(e));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee e) {
        return ResponseEntity.ok(svc.update(id, e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

}
