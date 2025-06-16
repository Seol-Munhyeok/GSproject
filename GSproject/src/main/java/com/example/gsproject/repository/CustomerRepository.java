package com.example.gsproject.repository;

import com.example.gsproject.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.name = :name AND c.phone = :phone")
    List<Customer> findByNameAndPhone(@Param("name") String name, @Param("phone") String phone);

}
