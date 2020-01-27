package com.viva.service;

import com.viva.authentication.TokenProvider;
import com.viva.configure.PasswordService;
import com.viva.dto.CompanyDTO;
import com.viva.dto.EmployeeListDTO;
import com.viva.dto.UserInfoDTO;
import com.viva.entity.Company;
import com.viva.entity.CompanyUser;
import com.viva.entity.User;
import com.viva.repository.CompanyRepository;
import com.viva.repository.EmployeeListRepository;
import com.viva.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.viva.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static com.viva.entity.Constants.COMPANY_NAME;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    PasswordService passwordService;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    EmployeeListRepository employeeListRepository;

    public UserInfoDTO getToken(UserInfoDTO dto){
        User user = userRepository.findByUserName(dto.getUserName());
        if(passwordService.matches(dto.getPassword(),user.getPassword())){
            List<SimpleGrantedAuthority> userRole = user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole())).collect(Collectors.toList());
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserName(),null,userRole);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
            StringBuilder sb = new StringBuilder();
            for(SimpleGrantedAuthority ch : userRole) {
                sb.append(ch);
            }
            String auth = sb.toString();
            String token = tokenProvider.generateToken(authentication);
            UserInfoDTO userDto = UserInfoDTO.builder()
                                    .token(token)
                                    .role(auth)
                                    .build();
            return userDto;
        }
        return null;
    }

    public List<CompanyDTO> getCompanies(){
        List < Company > companies = companyRepository.findAll();
        List < CompanyDTO > dtoList = new ArrayList<>();

        for(Company company : companies){
            dtoList.add(CompanyDTO.builder()
                    .companyId(company.getCompanyId())
                    .company(company.getCompany())
                    .contact(company.getContact())
                    .status(company.getStatus())
                    .build());
        }
        return dtoList;
    }

    public boolean saveUserService(UserInfoDTO dto){
        String name = dto.getUserName();
        Company company = companyRepository.findAllByAdmin(name);
        String role;
        if (company != null){
            dto.setCompanyName(company.getCompany());
            role = "COMPANY_ADMIN";
        }
        else {
            role = "USER";
        }
        if(userRepository.findByUserName(name)==null){
            userRepository.save(User.builder()
                    .userName(dto.getUserName())
                    .password(passwordService.encode(dto.getPassword()))
                    .roles(roleRepository.findAllByRole(role))
                    .companyName(dto.getCompanyName())
                    .build());
            return true;
        }
        else
            return false;
    }

    public boolean addCompanyService(CompanyDTO dto) {
        User user = userRepository.findByUserName(dto.getAdmin());
        if(user != null) {
            user.setRoles(roleRepository.findAllByRole("COMPANY_ADMIN"));
            userRepository.save(user);
        }
        if(companyRepository.findAllByCompany(dto.getCompany())==null){
            companyRepository.save(Company.builder()
                    .company(dto.getCompany())
                    .contact(dto.getContact())
                    .admin(dto.getAdmin())
                    .build());
            return true;
        }
        return false;
    }

    public int blockCompany(int companyId) {
        Company company = companyRepository.findAllByCompanyId(companyId);
        if(company.getStatus()== 1 ) {
            company.setStatus(0);
            companyRepository.save(company);
            return 0;
        }
        else {
            company.setStatus(1);
            companyRepository.save(company);
            return 1;
        }
    }

    public List<EmployeeListDTO> getEmployees(String name) {
        Company company = companyRepository.findAllByAdmin(name);
        int id = company.getCompanyId();
        List< CompanyUser > userList = employeeListRepository.findAllByCompanyId(id);
        List< EmployeeListDTO > dtoList = new ArrayList<>();
        for( CompanyUser companyUser : userList){
            dtoList.add(EmployeeListDTO.builder()
                    .employeeId(companyUser.getEmployeeId())
                    .employeeContactNo(companyUser.getEmployeeContactNo())
                    .employeeName(companyUser.getCompanyEmployeeName())
                    .companyId(companyUser.getCompanyId())
                    .build());
        }
        return dtoList;
    }

    public boolean saveEmployeeService(EmployeeListDTO dto) {
        Company company = companyRepository.findAllByAdmin(dto.getAdmin());
        if(employeeListRepository.findAllByCompanyEmployeeName(dto.getEmployeeName()) == null){
            employeeListRepository.save(CompanyUser.builder()
                    .companyEmployeeName(dto.getEmployeeName())
                    .employeeContactNo(dto.getEmployeeContactNo())
                    .companyId(company.getCompanyId())
                    .build());
            return true;
        }
        return false;
    }
}
