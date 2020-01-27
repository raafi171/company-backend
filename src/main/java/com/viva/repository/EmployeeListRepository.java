package com.viva.repository;

import com.viva.entity.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeListRepository extends JpaRepository <CompanyUser,Integer>{
    List<CompanyUser> findAllByCompanyId(int id);
    CompanyUser findAllByCompanyEmployeeName(String name);
}
