-- new-class command
INSERT INTO class (class_id, course_number, year, term, section, class_description, meeting_times) VALUES (1, 'ENG101',2018,'Spring',001, 'English 101', '3:15');

-- select-class (grab the class id to store in the program)
SELECT class_id FROM class
WHERE course_number = 'ENG101'
AND term = 'Spring'
AND year = 2018
AND section = 001;

-- show-categories command
SELECT name, weight FROM category
WHERE category.class_id = 1; -- want to use the currently active class for class_id

-- add-category command
INSERT INTO category (class_id, name, weight) VALUES (1, "Extra Credit", 5); --again, use currently active class_id

-- show-items command
SELECT name, point_value FROM item
WHERE class_id = 1
GROUP BY category_id, name, point_value;

-- add-item command
INSERT INTO item (class_id, category_id, name, description, point_value) VALUES (1,
    (
          SELECT category_id FROM category
          WHERE category.class_id = item.class_id
          AND category.name = "Homework"
    ), "New Category", "This is an extra category", 50);



-- add-student command
INSERT INTO student (student_id, student_name, username) VALUES (104051094, 'Von Wolfe, Tony', 'tvonwolfe');
INSERT INTO enrollment (student_id, class_id) VALUES (104051094, 1); -- save student_id parameter, use currently active class

-- show-students command
SELECT student_name, username, student.student_id FROM student
JOIN enrollment ON student.student_id = enrollment.student_id
WHERE class_id = 1;

-- show-students command with parameter
SELECT student_name, username, student.student_id FROM student
JOIN enrollment e on student.student_id = e.student_id
WHERE class_id = 1
AND student_name ILIKE '%' || 'A' || '%';

-- grade command
INSERT INTO grade (student_id, item_id, assigned_grade)
VALUES (
           (SELECT student.student_id FROM student
            JOIN enrollment e on student.student_id = e.student_id
            WHERE username = 'tvonwolfe'
            AND class_id = 1
           ),

           (SELECT item_id FROM item
            JOIN class ON item.class_id = class.class_id
            WHERE item.class_id = 1
            AND item.name = 'HW1'
           ),

           75

);

