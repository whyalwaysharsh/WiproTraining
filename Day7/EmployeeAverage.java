/*
Question 1: Employee Class - Average Salary Calculation
-------------------------------------------------------
Create a class Employee with attributes id, name, salary, and department.
Create a collection of Employee objects and find the average salary of employees
who belong to the "development" department.
*/

import java.util.ArrayList;

class EmployeeAverage {
    public static void main(String[] args) {

        Employee e1 = new Employee(1, "abc", 55000.00, "development");
        Employee e2 = new Employee(11, "xyz", 43500.00, "development");
        Employee e3 = new Employee(5, "fname", 100000.00, "production");
        Employee e4 = new Employee(21, "name123", 75000, "support");
        Employee e5 = new Employee(17, "someName", 85000, "development");

        ArrayList<Employee> empList = new ArrayList<>();
        empList.add(e1);
        empList.add(e2);
        empList.add(e3);
        empList.add(e4);
        empList.add(e5);

        double totalSalary = 0;
        int count = 0;

        for (Employee emp : empList) {
            if (emp.getDepartment().equalsIgnoreCase("development")) {
                totalSalary += emp.getSalary();
                count++;
            }
        }

        if (count > 0) {
            double avgSalary = totalSalary / count;
            System.out.println(avgSalary);
        } else {
            System.out.println("No employees found in development department.");
        }
    }
}

class Employee {
    int id;
    String name;
    double salary;
    String department;

    public Employee() {}

    public Employee(int id, String name, double salary, String department) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.department = department;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
