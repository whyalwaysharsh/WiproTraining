/*
Question 3: Employment Salary Analysis
--------------------------------------
Retrieve employees who earn a higher salary than the average 
salary in their respective departments.
*/

SELECT 
    e.FirstName,
    e.LastName,
    d.DepartmentName,
    e.Salary
FROM 
    Employees AS e
JOIN 
    Departments AS d
    ON e.DepartmentID = d.DepartmentID
WHERE 
    e.Salary > (
        SELECT 
            AVG(Salary)
        FROM 
            Employees
        WHERE 
            DepartmentID = e.DepartmentID
    )
ORDER BY 
    e.Salary ASC;
