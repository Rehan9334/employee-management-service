package com.employee.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.employee.entity.Employee;
import com.employee.repository.EmployeeRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository repo;

    public EmployeeService(EmployeeRepository repo) { this.repo = repo; }

    @Cacheable(value = "employees", key = "#id")
    public Employee findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public Page<Employee> findAllPaged(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repo.findAll(pageable);
    }

    @CacheEvict(value = "employees", allEntries = true) // clear caches after mutation
    public Employee create(Employee e) { return repo.save(e); }

    @CacheEvict(value = "employees", key = "#id")
    public Employee update(Long id, Employee e) {
        var existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        existing.setFirstName(e.getFirstName());
        existing.setLastName(e.getLastName());
        existing.setEmail(e.getEmail());
        existing.setDesignation(e.getDesignation());
        existing.setSalary(e.getSalary());
        return repo.save(existing);
    }

    @CacheEvict(value = "employees", key = "#id")
    public void delete(Long id) { repo.deleteById(id); }
}
