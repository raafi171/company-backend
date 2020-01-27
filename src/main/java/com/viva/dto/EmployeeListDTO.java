package com.viva.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeListDTO {
    private int employeeId;
    public String employeeName;
    public String employeeContactNo;
    public int companyId;
    public String admin;
}
