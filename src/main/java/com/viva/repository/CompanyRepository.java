package com.viva.repository;

import com.viva.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company , Integer> {
    List<Company> findAll();
    Company findAllByCompany(String name);
    Company findAllByCompanyId(int id);
    Company findAllByAdmin(String name);
}
