package com.viva.controller;

import com.viva.dto.BaseResponseDTO;
import com.viva.dto.CompanyDTO;
import com.viva.dto.EmployeeListDTO;
import com.viva.dto.UserInfoDTO;
import com.viva.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/save-user")
    public ResponseEntity saveUser(@RequestBody UserInfoDTO dto, RedirectAttributes redirect) {
        boolean check = userService.saveUserService(dto);
        if(check == true){
            return ResponseEntity.ok().body(BaseResponseDTO.builder()
                    .status("SUCCESS")
                    .message("User saved successfully")
                    .build());
        }
        else
            return ResponseEntity.ok().body(BaseResponseDTO.builder()
                    .status("Failure")
                    .message("Repeated User Name")
                    .build());

    }
    @PostMapping("/authenticate-user")
    public UserInfoDTO authUser(@RequestBody UserInfoDTO dto){
        UserInfoDTO userDto = userService.getToken(dto);
        return userDto;
    }

    @PostMapping("/add-company")
    public ResponseEntity saveCompany(@RequestBody CompanyDTO dto){
        boolean check = userService.addCompanyService(dto);
        if(check == true) {
            return ResponseEntity.ok().body(BaseResponseDTO.builder()
                    .status("SUCCESS")
                    .message("Company Saved Successfully")
                    .build());
        }
        return ResponseEntity.ok().body(BaseResponseDTO.builder()
                .status("Failed to save")
                .message("Repeated Company Name")
                .build());
    }

    @PostMapping("/block-company")
    public String blockCompany(@RequestBody int companyId){
        int value = userService.blockCompany(companyId);
        if(value == 0){
            return "Active";
        }
        return "Block";
    }

    @PostMapping("/employee-list")
    public List<EmployeeListDTO> companyHome(@RequestBody String name){
        List<EmployeeListDTO> dtoList = userService.getEmployees(name);
        return dtoList;
    }

    @GetMapping("/company-list")
    public List<CompanyDTO> home(){
        List<CompanyDTO>dtoList = userService.getCompanies();
        return dtoList;
    }

    @PostMapping("/save-employee")
    public ResponseEntity saveEmployee(@RequestBody EmployeeListDTO dto){
        boolean check = userService.saveEmployeeService(dto);
        if(check == true) {
            return ResponseEntity.ok().body(BaseResponseDTO.builder()
                    .status("SUCCESS")
                    .message("Company Saved Successfully")
                    .build());
        }
        return ResponseEntity.ok().body(BaseResponseDTO.builder()
                .status("Failed to save")
                .message("Repeated Company Name")
                .build());
    }

}
