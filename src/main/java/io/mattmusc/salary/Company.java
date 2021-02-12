package io.mattmusc.salary;

import lombok.Data;

import java.util.List;

enum EmployeeLevel {
    SELLER, TECHNICIAN, MANAGER
}

@Data
class Employee {
    private double salary;
    private EmployeeLevel level;
}

@Data
class Seller extends Employee {
    private double amountSold;
}

@Data
class Manager extends Employee {
    private List<Employee> managedEmployees;
}

public class Company {
    public static void main(String[] args) {
        System.out.println("Company");
    }
}
