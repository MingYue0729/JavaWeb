**学院：** 省级示范性学院

**题目：** 《作业四：SQL练习》

**姓名：** 翁未未

**学号：** 2200770273

**班级：** 软工2202

**日期：** 2024-10-11



# SQL练习



## 一、**员工信息练习题**



11. 计算所有员工的工资总和。

```
SELECT SUM(salary) AS total_salary 
FROM employees;
```

12. 查询姓"Smith"的员工信息。

```
SELECT * 
FROM employees 
WHERE last_name = 'Smith';
```

13. 查询即将在半年内到期的项目。

```
SELECT * 
FROM projects
WHERE end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 6 MONTH);
```

14. 查询至少参与了两个项目的员工。

```
SELECT e.emp_id, e.first_name, e.last_name
FROM employees e
JOIN employee_projects ep ON e.emp_id = ep.emp_id
GROUP BY e.emp_id, e.first_name, e.last_name
HAVING COUNT(ep.project_id) >= 2;
```

15. 查询没有参与任何项目的员工。

```
SELECT *
FROM employees e
LEFT JOIN employee_projects ep ON e.emp_id = ep.emp_id
WHERE ep.emp_id IS NULL;
```

16. 计算每个项目参与的员工数量。

```
SELECT p.project_id, p.project_name, COUNT(ep.emp_id) AS total_employees
FROM projects p
LEFT JOIN employee_projects ep ON p.project_id = ep.project_id
GROUP BY p.project_id, p.project_name;
```

17. 查询工资第二高的员工信息。

```
SELECT *
FROM employees
WHERE salary = (
	SELECT MAX(salary) 
    FROM employees 
    WHERE salary < (
		SELECT MAX(salary) 
        FROM employees
	)
);
```

18. 查询每个部门工资最高的员工。

```
SELECT d.dept_name, SUM(e.salary) AS total_salary
FROM employees e
INNER JOIN departments d ON e.dept_id = d.dept_id
GROUP BY d.dept_name
ORDER BY total_salary DESC;
```

19. 计算每个部门的工资总和,并按照工资总和降序排列。

```
SELECT d.dept_name, SUM(e.salary) AS total_salary
FROM employees e
JOIN departments d ON e.dept_id = d.dept_id
GROUP BY d.dept_name
ORDER BY total_salary DESC;
```

20. 查询员工姓名、部门名称和工资。

```
SELECT e.first_name, e.last_name, d.dept_name, e.salary
FROM employees e
JOIN departments d ON e.dept_id = d.dept_id;
```

21. 查询每个员工的上级主管(假设emp_id小的是上级)。

```
SELECT 
    e1.first_name AS employee_first_name, 
    e1.last_name AS employee_last_name, 
    e2.first_name AS manager_first_name, 
    e2.last_name AS manager_last_name
FROM employees e1
LEFT JOIN employees e2 ON e1.dept_id = e2.dept_id AND e1.emp_id > e2.emp_id
WHERE e2.emp_id IS NOT NULL;
```

22. 查询所有员工的工作岗位,不要重复。

```
SELECT DISTINCT job_title 
FROM employees;
```

23. 查询平均工资最高的部门。

```
SELECT dept_id, AVG(salary) AS avg_salary
FROM employees
GROUP BY dept_id
ORDER BY avg_salary DESC
LIMIT 1;
```

24. 查询工资高于其所在部门平均工资的员工。

```
SELECT e.*
FROM employees e
JOIN (
    SELECT dept_id, AVG(salary) AS avg_salary
    FROM employees
    GROUP BY dept_id
) AS dept_avg ON e.dept_id = dept_avg.dept_id
WHERE e.salary > dept_avg.avg_salary;
```

25. 查询每个部门工资前两名的员工。

```
SELECT 
    d.dept_name,
    e.first_name,
    e.last_name,
    e.salary
FROM 
    employees e
JOIN 
    departments d ON e.dept_id = d.dept_id
WHERE 
    (
        SELECT COUNT(DISTINCT emp_sub.salary) 
        FROM employees emp_sub 
        WHERE emp_sub.salary > e.salary AND emp_sub.dept_id = e.dept_id
    ) < 2;
```

26. 查询跨部门的项目(参与员工来自不同部门)。

```
SELECT 
    p.project_id,
    p.project_name
FROM projects p
WHERE p.project_id IN (
        SELECT ep.project_id
        FROM employee_projects ep
        JOIN employees e ON ep.emp_id = e.emp_id
        GROUP BY ep.project_id
        HAVING COUNT(DISTINCT e.dept_id) > 1
);
```

27. 查询每个员工的工作年限,并按工作年限降序排序。

```
SELECT 
    first_name,
    last_name,
    job_title,
    hire_date,
    salary,
    CONCAT(FLOOR(DATEDIFF(CURRENT_DATE, hire_date) / 365), ' years ', 
           LPAD(MOD(DATEDIFF(CURRENT_DATE, hire_date) % 365, 30), 2, '0'), ' days') AS work_years
FROM employees
ORDER BY DATEDIFF(CURRENT_DATE, hire_date) DESC;
```

28. 查询本月过生日的员工(假设hire_date是生日)。

```
SELECT 
    first_name,
    last_name,
    email,
    hire_date
FROM employees
WHERE MONTH(hire_date) = MONTH(CURRENT_DATE());
```

29. 查询即将在90天内到期的项目和负责该项目的员工。

```
SELECT 
    p.project_id,
    p.project_name,
    p.end_date,
    e.first_name,
    e.last_name,
    e.email
FROM projects p
JOIN employee_projects ep ON p.project_id = ep.project_id
JOIN employees e ON ep.emp_id = e.emp_id
WHERE DATEDIFF(p.end_date, CURRENT_DATE) BETWEEN 1 AND 90;
```

30. 计算每个项目的持续时间(天数)。

```
SELECT 
    project_id,
    project_name,
    DATEDIFF(end_date, start_date) AS duration_days
FROM 
    projects
WHERE 
    end_date IS NOT NULL;
```

31. 查询没有进行中项目的部门。

```
SELECT 
    d.dept_id,
    d.dept_name
FROM departments d
WHERE d.dept_id NOT IN (
        SELECT DISTINCT e.dept_id
        FROM employees e
        JOIN employee_projects ep ON e.emp_id = ep.emp_id
        JOIN projects p ON ep.project_id = p.project_id
        WHERE p.start_date <= CURRENT_DATE AND (p.end_date IS NULL OR p.end_date >= CURRENT_DATE)
);
```

32. 查询员工数量最多的部门。

```
SELECT departments.dept_name, COUNT(employees.emp_id) AS num_employees
FROM departments
LEFT JOIN employees ON departments.dept_id = employees.dept_id
GROUP BY departments.dept_id
ORDER BY num_employees DESC
LIMIT 1;
```

33. 查询参与项目最多的部门。

```
SELECT departments.dept_name, COUNT(employee_projects.project_id) AS total_projects
FROM departments
LEFT JOIN employees ON departments.dept_id = employees.dept_id
LEFT JOIN employee_projects ON employees.emp_id = employee_projects.emp_id
GROUP BY departments.dept_id
ORDER BY total_projects DESC
LIMIT 1;
```

34. 计算每个员工的薪资涨幅(假设每年涨5%)。

```
SELECT 
    employees.first_name,
    employees.last_name,
    employees.salary,
    employees.hire_date,
    (employees.salary * (1 + 0.05 * TIMESTAMPDIFF(YEAR, employees.hire_date, CURDATE()))) AS future_salary
FROM employees;
```

35. 查询入职时间最长的3名员工。

```
SELECT *
FROM employees
ORDER BY hire_date ASC
LIMIT 3;
```



## 二、**学生选课题**



11. 查询C001比C002课程成绩高的学生信息及课程分数。

```
SELECT 
    s1.student_id, 
    s1.name AS student_name, 
    s1.gender, 
    s1.birth_date, 
    s1.my_class,
    c1.course_name AS course1_name,
    c2.course_name AS course2_name,
    sc1.score AS score_c001,
    sc2.score AS score_c002
FROM score AS sc1
INNER JOIN score AS sc2 ON sc1.student_id = sc2.student_id AND sc2.course_id = 'C002'
INNER JOIN student AS s1 ON sc1.student_id = s1.student_id
INNER JOIN course AS c1 ON sc1.course_id = 'C001' AND c1.course_id = sc1.course_id
INNER JOIN course AS c2 ON sc2.course_id = 'C002' AND c2.course_id = sc2.course_id
WHERE sc1.score > sc2.score;
```

12. 统计各科成绩各分数段人数：课程编号，课程名称，[100-85]，[85-70]，[70-60]，[60-0] 及所占百分比。

```
SELECT 
    course.course_id,
    course.course_name,
    COUNT(CASE WHEN score >= 85 AND score <= 100 THEN 1 END) AS score_85_100,
    COUNT(CASE WHEN score >= 70 AND score < 85 THEN 1 END) AS score_70_85,
    COUNT(CASE WHEN score >= 60 AND score < 70 THEN 1 END) AS score_60_70,
    COUNT(CASE WHEN score < 60 THEN 1 END) AS score_below_60,
    CONCAT(ROUND(COUNT(CASE WHEN score >= 85 AND score <= 100 THEN 1 END) / COUNT(*) * 100, 2), '%') AS percent_85_100,
    CONCAT(ROUND(COUNT(CASE WHEN score >= 70 AND score < 85 THEN 1 END) / COUNT(*) * 100, 2), '%') AS percent_70_85,
    CONCAT(ROUND(COUNT(CASE WHEN score >= 60 AND score < 70 THEN 1 END) / COUNT(*) * 100, 2), '%') AS percent_60_70,
    CONCAT(ROUND(COUNT(CASE WHEN score < 60 THEN 1 END) / COUNT(*) * 100, 2), '%') AS percent_below_60
FROM score
INNER JOIN course ON score.course_id = course.course_id
GROUP BY course.course_id, course.course_name;
```

13. 查询选择C002课程但没选择C004课程的成绩情况(不存在时显示为 null )。

```
SELECT 
    s.student_id, 
    s.name AS student_name, 
    c.course_name,
    sc.score
FROM score AS sc
INNER JOIN student AS s ON sc.student_id = s.student_id
INNER JOIN course AS c ON sc.course_id = c.course_id
WHERE sc.course_id = 'C002'
AND s.student_id NOT IN (SELECT student_id FROM score AS sc2 WHERE sc2.course_id = 'C004');
```

14. 查询平均分数最高的学生姓名和平均分数。

```
SELECT 
    s.name AS student_name,
    AVG(sc.score) AS average_score
FROM score AS sc
INNER JOIN 
    student AS s ON sc.student_id = s.student_id
GROUP BY 
    sc.student_id
ORDER BY 
    average_score DESC
LIMIT 1;
```

15. 查询总分最高的前三名学生的姓名和总分。

```
SELECT 
    s.name AS student_name,
    SUM(sc.score) AS total_score
FROM score AS sc
INNER JOIN student AS s ON sc.student_id = s.student_id
GROUP BY sc.student_id
ORDER BY total_score DESC
LIMIT 3;
```

16. 查询各科成绩最高分、最低分和平均分。要求如下：
    以如下形式显示：课程 ID，课程 name，最高分，最低分，平均分，及格率，中等率，优良率，优秀率
    及格为>=60，中等为：70-80，优良为：80-90，优秀为：>=90
    要求输出课程号和选修人数，查询结果按人数降序排列，若人数相同，按课程号升序排列

```
SELECT 
    course.course_id,
    course.course_name,
    MAX(score.score) AS max_score,
    MIN(score.score) AS min_score,
    AVG(score.score) AS avg_score,
    SUM(CASE WHEN score.score >= 60 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS pass_rate,
    SUM(CASE WHEN score.score >= 70 AND score.score < 80 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS average_rate,
    SUM(CASE WHEN score.score >= 80 AND score.score < 90 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS good_rate,
    SUM(CASE WHEN score.score >= 90 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS excellent_rate,
    COUNT(DISTINCT score.student_id) AS students_count
FROM score
INNER JOIN course ON score.course_id = course.course_id
GROUP BY course.course_id
ORDER BY students_count DESC, 
         course.course_id ASC;
```

17. 查询男生和女生的人数。

```
SELECT 
    s.gender,
    COUNT(*) AS gender_count
FROM student s
GROUP BY s.gender;
```

18. 查询年龄最大的学生姓名。

```
SELECT name
FROM student
ORDER BY birth_date ASC
LIMIT 1;
```

19. 查询年龄最小的教师姓名。

```
SELECT name
FROM teacher
ORDER BY teacher.birth_date DESC
LIMIT 1;
```

20. 查询学过「张教授」授课的同学的信息。

```
SELECT DISTINCT s.*
FROM student s
INNER JOIN score sc ON s.student_id = sc.student_id
INNER JOIN course c ON sc.course_id = c.course_id
WHERE c.teacher_id = (SELECT teacher_id FROM teacher WHERE name = '张教授');
```

21. 查询查询至少有一门课与学号为"2021001"的同学所学相同的同学的信息 。

```
SELECT DISTINCT s.*
FROM student s
INNER JOIN score sc ON s.student_id = sc.student_id
WHERE sc.course_id IN (
	SELECT course_id 
    FROM score 
    WHERE student_id = '2021001'
    )
AND s.student_id != '2021001';
```

查询每门课程的平均分数，并按平均分数降序排列。

```
SELECT 
    course_id,
    AVG(score) AS average_score
FROM score
GROUP BY course_id
ORDER BY average_score DESC;
```

23. 查询学号为"2021001"的学生所有课程的分数。

```
SELECT 
    c.course_name,
    sc.score
FROM score sc
INNER JOIN course c ON sc.course_id = c.course_id
WHERE sc.student_id = '2021001';
```

24. 查询所有学生的姓名、选修的课程名称和分数。

```
SELECT 
    s.name,
    c.course_name,
    sc.score
FROM score sc
INNER JOIN student s ON sc.student_id = s.student_id
INNER JOIN course c ON sc.course_id = c.course_id;
```

25. 查询每个教师所教授课程的平均分数。

```
SELECT 
    t.name AS teacher_name,
    c.course_name,
    AVG(sc.score) AS average_score
FROM course c
INNER JOIN teacher t ON c.teacher_id = t.teacher_id
LEFT JOIN score sc ON c.course_id = sc.course_id
GROUP BY c.course_id;
```

26. 查询分数在80到90之间的学生姓名和课程名称。

```
SELECT 
    s.name AS student_name,
    c.course_name
FROM score
INNER JOIN student s ON score.student_id = s.student_id
INNER JOIN course c ON score.course_id = c.course_id
WHERE score.score BETWEEN 80 AND 90;
```

27. 查询每个班级的平均分数。

```
SELECT 
    s.my_class,
    AVG(score.score) AS avg_score
FROM student s
INNER JOIN score ON s.student_id = score.student_id
GROUP BY s.my_class;
```

28. 查询没学过"王讲师"老师讲授的任一门课程的学生姓名。

```
SELECT 
    s.name AS student_name
FROM student s
WHERE 
    s.student_id NOT IN (
        SELECT score.student_id
        FROM score
        INNER JOIN course c ON score.course_id = c.course_id
        WHERE c.teacher_id = (SELECT teacher_id FROM teacher WHERE name = '王讲师')
    );
```

29. 查询两门及其以上小于85分的同学的学号，姓名及其平均成绩 。

```
SELECT 
    s.student_id,
    s.name AS student_name,
    AVG(sc.score) AS avg_score
FROM student s
INNER JOIN score sc ON s.student_id = sc.student_id
WHERE sc.score < 85
GROUP BY s.student_id
HAVING COUNT(sc.course_id) >= 2;
```

30. 查询所有学生的总分并按降序排列。

```
SELECT 
    s.student_id,
    s.name AS student_name,
    SUM(score.score) AS total_score
FROM student s
INNER JOIN score ON s.student_id = score.student_id
GROUP BY s.student_id
ORDER BY total_score DESC;
```

31. 查询平均分数超过85分的课程名称。

```
SELECT c.course_name
FROM course c
INNER JOIN (
	SELECT course_id, AVG(score) AS avg_score 
    FROM score 
    GROUP BY course_id
    ) AS avg_scores ON c.course_id = avg_scores.course_id
WHERE avg_scores.avg_score > 85;
```

32. 查询每个学生的平均成绩排名。

```
SELECT 
    s.name AS student_name,
    AVG(sc.score) AS avg_score,
    RANK() OVER (ORDER BY AVG(sc.score) DESC) AS ranking
FROM student s
INNER JOIN score sc ON s.student_id = sc.student_id
GROUP BY s.student_id;
```

33. 查询每门课程分数最高的学生姓名和分数。

```
SELECT 
    c.course_name,
    s.name AS student_name,
    sc.max_score
FROM 
    (SELECT course_id,MAX(score) AS max_score
     FROM score
     GROUP BY course_id) AS sc
INNER JOIN course c ON sc.course_id = c.course_id
INNER JOIN score sc2 ON sc.course_id = sc2.course_id AND sc.max_score = sc2.score
INNER JOIN student s ON sc2.student_id = s.student_id
ORDER BY sc.max_score DESC;
```

34. 查询选修了"高等数学"和"大学物理"的学生姓名。

```
SELECT s.name AS student_name
FROM student s
INNER JOIN score sc ON s.student_id = sc.student_id
WHERE sc.course_id IN ('C001', 'C002')
GROUP BY s.student_id
HAVING COUNT(DISTINCT sc.course_id) = 2;
```

35. 按平均成绩从高到低显示所有学生的所有课程的成绩以及平均成绩（没有选课则为空）。

```
SELECT 
    s.name AS student_name,
    c.course_name,
    sc.score,
    AVG(sc.score) OVER (PARTITION BY s.student_id) AS avg_score
FROM student s
LEFT JOIN score sc ON s.student_id = sc.student_id
LEFT JOIN course c ON sc.course_id = c.course_id
ORDER BY avg_score DESC, s.name;
```

36. 查询分数最高和最低的学生姓名及其分数。

```
SELECT 
    student_name, 
    score
FROM (
    SELECT 
        s.name AS student_name, 
        sc.score, 
        ROW_NUMBER() OVER (ORDER BY sc.score DESC) AS ranking
    FROM 
        score sc
    JOIN 
        student s ON sc.student_id = s.student_id
    UNION ALL
    SELECT 
        s.name AS student_name, 
        sc.score,
        ROW_NUMBER() OVER (ORDER BY sc.score ASC) AS ranking
    FROM 
        score sc
    JOIN 
        student s ON sc.student_id = s.student_id
) AS ranked_scores
WHERE 
    ranking = 1;
```

37. 查询每个班级的最高分和最低分。

```
SELECT 
    s.my_class,
    MAX(sc.score) AS max_score,
    MIN(sc.score) AS min_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY s.my_class;
```

38. 查询每门课程的优秀率（优秀为90分）。

```
SELECT 
    course_id,
    COUNT(*) / (SELECT COUNT(*) FROM score) * 100 AS excellent_rate
FROM score
WHERE score >= 90
GROUP BY course_id;
```

39. 查询平均分数超过班级平均分数的学生。

```
SELECT 
    s.name AS student_name,
    ROUND(sa.avg_score, 2) AS student_avg_score,
    ROUND(cla.avg_class_score, 2) AS class_avg_score
FROM 
    (SELECT 
         student_id, 
         ROUND(AVG(score), 2) AS avg_score
     FROM score
     GROUP BY student_id) AS sa
JOIN student s ON sa.student_id = s.student_id
JOIN (SELECT 
         my_class, 
         ROUND(AVG(avg_score), 2) AS avg_class_score
     FROM (
		  SELECT 
              student_id, 
              ROUND(AVG(score), 2) AS avg_score
          FROM score
          GROUP BY student_id) AS sa2
     JOIN student st ON sa2.student_id = st.student_id
     GROUP BY st.my_class) AS cla ON s.my_class = cla.my_class
WHERE sa.avg_score > cla.avg_class_score;
```

40. 查询每个学生的分数及其与课程平均分的差值。

```
SELECT 
    s.name AS student_name,
    c.course_name,
    sc.score,
    sc.score - AVG(sc.score) OVER (PARTITION BY sc.course_id) AS score_diff
FROM score sc
JOIN student s ON sc.student_id = s.student_id
JOIN course c ON sc.course_id = c.course_id;
```

41. 查询至少有一门课程分数低于80分的学生姓名。

```
SELECT s.name AS student_name
FROM student s
JOIN score sc ON s.student_id = sc.student_id
WHERE sc.score < 80
GROUP BY s.student_id
HAVING COUNT(sc.course_id) >= 1;
```

42. 查询所有课程分数都高于85分的学生姓名。

```
SELECT s.name AS student_name
FROM student s
WHERE NOT EXISTS (
        SELECT * 
        FROM score sc 
        WHERE sc.student_id = s.student_id AND sc.score <= 85
    );
```

43. 查询查询平均成绩大于等于90分的同学的学生编号和学生姓名和平均成绩。

```
SELECT 
    s.student_id,
    s.name AS student_name,
    AVG(sc.score) AS avg_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY s.student_id
HAVING AVG(sc.score) >= 90;
```

44. 查询选修课程数量最少的学生姓名。

```
SELECT s.name AS student_name
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY s.student_id
ORDER BY COUNT(sc.course_id)
LIMIT 1;
```

45. 查询每个班级的第2名学生（按平均分数排名）。

```
SELECT 
    class_rankings.name AS student_name,
    class_rankings.my_class,
    class_rankings.avg_score
FROM (
    SELECT 
        s.name,
        s.my_class,
        AVG(sc.score) AS avg_score,
        DENSE_RANK() OVER (PARTITION BY s.my_class ORDER BY AVG(sc.score) DESC) AS class_rank
    FROM student s
    JOIN score sc ON s.student_id = sc.student_id
    GROUP BY s.student_id, s.my_class
) AS class_rankings
WHERE class_rankings.class_rank = 2;
```

46. 查询每门课程分数前三名的学生姓名和分数。

```
SELECT 
    s.name AS student_name,
    c.course_name,
    sc.score
FROM score sc
JOIN student s ON sc.student_id = s.student_id
JOIN course c ON sc.course_id = c.course_id
WHERE (
    sc.course_id, sc.score
) IN (
    SELECT 
        sc.course_id, 
        MAX(sc.score)
    FROM score sc
    GROUP BY sc.course_id
    ORDER BY sc.course_id
) 
ORDER BY c.course_name, sc.score DESC;
```

47. 查询平均分数最高和最低的班级。

```
SELECT 
    max_class.my_class, 
    max_class.avg_score AS max_avg_score, 
    min_class.my_class, 
    min_class.avg_score AS min_avg_score
FROM 
    (
        SELECT my_class, AVG(score) AS avg_score
        FROM score
        JOIN student ON score.student_id = student.student_id
        GROUP BY my_class
        ORDER BY avg_score DESC
        LIMIT 1
    ) AS max_class,
    (
        SELECT my_class, AVG(score) AS avg_score
        FROM score
        JOIN student ON score.student_id = student.student_id
        GROUP BY my_class
        ORDER BY avg_score ASC
        LIMIT 1
    ) AS min_class;
```

48. 查询每个学生的总分和他所在班级的平均分数。

```
SELECT 
    s.name AS student_name,
    SUM(sc.score) AS total_score,(
		SELECT AVG(sc2.score) 
		FROM score sc2 
        JOIN student s2 ON sc2.student_id = s2.student_id 
        WHERE s2.my_class = s.my_class
        ) AS class_avg_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY s.student_id;
```

49. 查询每个学生的最高分的课程名称, 学生名称，成绩。

```
SELECT 
    c.course_name,
    s.name AS student_name,
    sc.score
FROM score sc
JOIN student s ON sc.student_id = s.student_id
JOIN course c ON sc.course_id = c.course_id
WHERE (
    sc.student_id,
    sc.score
) IN (
    SELECT 
        sc.student_id, 
        MAX(sc.score)
    FROM score sc
    GROUP BY sc.student_id
);
```

50. 查询每个班级的学生人数和平均年龄。

```
SELECT 
    s.my_class,
    COUNT(s.student_id) AS student_count,
    AVG(TIMESTAMPDIFF(YEAR, s.birth_date, CURDATE())) AS avg_age
FROM student s
GROUP BY s.my_class;
```





























