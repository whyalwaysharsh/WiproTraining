/*
Question 1: MYSQL - Scheduling Errors
-------------------------------------
Display the year in ascending order and find, for each semester, 
how many professors are involved in delivering the session for courses 1 to 10.
*/

SELECT 
    s.year,
    s.semester,
    COUNT(DISTINCT s.professor_id) AS no_of_professor
FROM 
    schedule AS s
WHERE 
    s.course_id BETWEEN 1 AND 10
GROUP BY 
    s.year, 
    s.semester
ORDER BY 
    s.year ASC;
