package io.mattmusc.salary;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The structure of the problem suggests a Tree data structure.
 */

enum EmployeeLevel {
    SELLER(e -> 500.0 + (e.getAmountSold() / 100 * 10)),
    TECHNICIAN(e -> 1400.0),
    MANAGER(e -> 1500.0 + 0.5 * e.getManagedEmployees()
            .stream()
            .map(managedEmployee -> managedEmployee.getLevel().calculateSalary.apply(managedEmployee))
            .mapToDouble(value -> value).sum());

    final Function<Employee, Double> calculateSalary;

    EmployeeLevel(Function<Employee, Double> calculateSalary) {
        this.calculateSalary = calculateSalary;
    }
}

@Data
class Employee {
    private final String name;
    private final EmployeeLevel level;

    private double amountSold;

    private List<Employee> managedEmployees = new ArrayList<>();

    public Employee(String name, EmployeeLevel level) {
        this.name = name;
        this.level = level;
    }

    public Employee(String name, EmployeeLevel level, double amountSold) {
        this.name = name;
        this.level = level;
        this.amountSold = amountSold;
    }

    public Employee(String name, EmployeeLevel level, List<Employee> managedEmployees) {
        this.name = name;
        this.level = level;
        this.managedEmployees = managedEmployees;
    }
}

public class Company {
    public static void main(String[] args) {
        Employee vittorioSeller = new Employee("Vittorio - Seller", EmployeeLevel.SELLER, 15000);
        Employee teresaTechnician = new Employee("Teresa - Technician", EmployeeLevel.TECHNICIAN);
        Employee marioManager = new Employee(
                "Mario - Manager",
                EmployeeLevel.MANAGER,
                List.of(teresaTechnician, vittorioSeller));
        Employee virnaSeller = new Employee("Virna - Seller", EmployeeLevel.SELLER, 17000);
        Employee mariaCeo = new Employee(
                "Maria - CEO",
                EmployeeLevel.MANAGER,
                List.of(marioManager, virnaSeller));

        List<Employee> employees = List.of(mariaCeo, virnaSeller, marioManager, teresaTechnician, vittorioSeller);
        System.out.printf("Total salary for the company %f%n", calculateSalary(employees));
    }

    public static double calculateSalary(List<Employee> employees) {
        Predicate<? super Employee> managers = e -> EmployeeLevel.MANAGER.equals(e.getLevel());
        Predicate<? super Employee> notManagers = e -> !managers.test(e);
        Function<Employee, Double> calculateSalary = e -> e.getLevel().calculateSalary.apply(e);

        double salaryForSellersAndTechnician = employees
                .stream()
                .filter(notManagers)
                .map(calculateSalary)
                .mapToDouble(value -> value)
                .sum();
        double salaryForManagers = employees
                .stream()
                .filter(managers)
                .map(calculateSalary)
                .mapToDouble(value -> value)
                .sum();

        return salaryForSellersAndTechnician + salaryForManagers;
    }
}
