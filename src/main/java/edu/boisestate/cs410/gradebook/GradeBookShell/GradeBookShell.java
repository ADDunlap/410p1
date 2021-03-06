package edu.boisestate.cs410.gradebook.GradeBookShell;

import com.budhash.cliche.Command;
import com.budhash.cliche.ShellFactory;
import org.postgresql.util.PSQLException;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Formatter;

import static java.lang.Thread.sleep;

public class GradeBookShell {

    private final Connection db;
    private boolean activeFlag = false;
    private int currentClassId;
    private String currentCourseNum;
    private int currentYear;
    private String currentTerm;
    private int currentSection;
    private String currentDescription;
    private String currentMeetTimes;
    private DecimalFormat dc = new DecimalFormat("00.00");

    public GradeBookShell(Connection cxn) {
        db = cxn;
    }

    public void setCurrentClassId(int newCurrentClassId) {
        currentClassId = newCurrentClassId;
    }

    public int getCurrentClassId() {
        return currentClassId;
    }

    public static void main(String[] args) throws IOException, SQLException {
        String dbUrl = args[0];

        try (Connection cxn = DriverManager.getConnection("jdbc:" + dbUrl)) {
            GradeBookShell shell = new GradeBookShell(cxn);
            ShellFactory.createConsoleShell("gradebook", "", shell)
                    .commandLoop();
        }
    }

    @Command
    public void newClass(String courseNum, String term, int year, int section, String class_description, String meetTimes)
    {
        try {
            db.createStatement();

            String insertStatement = "INSERT INTO class (course_number, year, term, section, class_description, meeting_times)" +
                                     "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = db.prepareStatement(insertStatement);
            statement.setString(1, courseNum);
            statement.setInt(2, year);
            statement.setString(3, term);
            statement.setInt(4, section);
            statement.setString(5, class_description);
            statement.setString(6, meetTimes);

            statement.executeUpdate();

        }
        catch(SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    @Command
    public void selectClass(String courseNum){
        try {
            db.createStatement();

            String getNumCourses = "SELECT COUNT(class_id) FROM class " +
                                   "WHERE course_number = ?;";

            PreparedStatement countStmt = db.prepareStatement(getNumCourses);
            countStmt.setString(1, courseNum);

            ResultSet rs1 = countStmt.executeQuery();

            if(rs1.next()) {
                int numCourses = rs1.getInt(1);
                if (numCourses > 1) {
                    System.out.println("ERROR: Multiple courses with course number: " + courseNum);
                    return;
                }
            }

            String selectStatement = "SELECT class_id,year,term,section,class_description,meeting_times FROM class " +
                                     "WHERE course_number = ?;";

            PreparedStatement stmt = db.prepareStatement(selectStatement);
            stmt.setString(1, courseNum);

            ResultSet rs = stmt.executeQuery();

            rs.next();
            currentClassId = rs.getInt(1);
            currentCourseNum = courseNum;
            currentYear = rs.getInt(2);
            currentTerm = rs.getString(3);
            currentSection = rs.getInt(4);
            currentDescription = rs.getString(5);
            currentMeetTimes = rs.getString(6);
            activeFlag = true;
            System.out.println("Selected " + courseNum);
        }
        catch (SQLException sqlEx) {
            System.out.println("Input error please use the following format:");
            System.out.println("select-class <Course Number>");
            return;
        }
    }

    @Command
    public void selectClass(String courseNum, String term, int year){
        try {
            db.createStatement();

            String getNumCourses = "SELECT COUNT(class_id) FROM class " +
                    "WHERE course_number = ? " +
                    "AND year = ? " +
                    "AND term = ?;";

            PreparedStatement countStmt = db.prepareStatement(getNumCourses);
            countStmt.setString(1, courseNum);
            countStmt.setInt(2,year);
            countStmt.setString(3,term);

            ResultSet rs1 = countStmt.executeQuery();

            if(rs1.next()) {
                int numCourses = rs1.getInt(1);
                if (numCourses > 1) {
                    System.out.println("Error: Multiple courses with course number: " + courseNum);
                    return;
                }
            }

            String selectStatement = "SELECT class_id,section,class_description,meeting_times FROM class " +
                    "WHERE course_number = ? " +
                    "AND year = ? " +
                    "AND term = ?;";

            PreparedStatement stmt = db.prepareStatement(selectStatement);
            stmt.setString(1, courseNum);
            stmt.setInt(2,year);
            stmt.setString(3,term);

            ResultSet rs = stmt.executeQuery();
            rs.next();
            currentClassId = rs.getInt(1);
            currentCourseNum = courseNum;
            currentYear = year;
            currentTerm = term;
            currentSection = rs.getInt(2);
            currentDescription = rs.getString(3);
            currentMeetTimes = rs.getString(4);
            activeFlag = true;
            System.out.println("Selected " + courseNum + " " + year + " " + term);

        }
        catch (SQLException sqlEx) {
            System.out.println("Input error please use the following format:");
            System.out.println("select-class <Course Number> <Year> <Term>");
            return;
        }
    }

    @Command
    public void selectClass(String courseNum, String term, int year, int section){
        try {
            db.createStatement();

            String getNumCourses = "SELECT COUNT(class_id) FROM class " +
                    "WHERE course_number = ? " +
                    "AND year = ? " +
                    "AND term = ? " +
                    "AND section = ?;";

            PreparedStatement countStmt = db.prepareStatement(getNumCourses);
            countStmt.setString(1, courseNum);
            countStmt.setInt(2,year);
            countStmt.setString(3,term);
            countStmt.setInt(4,section);

            ResultSet rs1 = countStmt.executeQuery();

            if(rs1.next()) {
                int numCourses = rs1.getInt(1);
                if (numCourses > 1) {
                    System.out.println("Error: Multiple courses with course number: " + courseNum);
                    return;
                }
            }

            String selectStatement = "SELECT class_id,class_description,meeting_times FROM class " +
                    "WHERE course_number = ? " +
                    "AND year = ? " +
                    "AND term = ? " +
                    "AND section = ?;";

            PreparedStatement stmt = db.prepareStatement(selectStatement);
            stmt.setString(1, courseNum);
            stmt.setInt(2,year);
            stmt.setString(3,term);
            stmt.setInt(4,section);

            ResultSet rs = stmt.executeQuery();
            rs.next();
            currentClassId = rs.getInt(1);
            currentCourseNum = courseNum;
            currentYear = year;
            currentTerm = term;
            currentSection = section;
            currentDescription = rs.getString(2);
            currentMeetTimes = rs.getString(3);
            activeFlag = true;
            System.out.println("Selected " + courseNum + " " + year + " " + term + " Section: " + section);

        }
        catch (SQLException sqlEx) {
            System.out.println("Input error please use the following format:");
            System.out.println("select-class <Course Number> <Year> <Term> <Section>");
            return;
        }
    }

    @Command
    public void showClass() throws SQLException{
        if(activeFlag){

            db.createStatement();
            String query = "SELECT year,term,section,class_description,meeting_times " +
                           "FROM class " +
                           "WHERE class_id = ? " +
                           "AND course_number = ?;";
            PreparedStatement stmt = db.prepareStatement(query);
            stmt.setInt(1,currentClassId);
            stmt.setString(2,currentCourseNum);

            ResultSet rs = stmt.executeQuery();

            rs.next();
            int year = rs.getInt(1);
            String term = rs.getString(2);
            int section = rs.getInt(3);
            String desc = rs.getString(4);
            String times = rs.getString(5);

            System.out.println("Active class: \n" +
                    "ID = " + currentClassId +
                    "\nCourse Number = " + currentCourseNum +
                    "\nYear = " + year +
                    "\nTerm = " + term +
                    "\nSection = " + section +
                    "\nCourse Description = " + desc +
                    "\nMeeting Time = " + times);
        }
        else {
            System.out.println("No active class selected. Please use one of the following commands to select a class");
            System.out.println("select-class <Class Number>");
            System.out.println("select-class <Class Number> <Year> <Term>");
            System.out.println("select-class <Class Number> <Year> <Term> <Section>");
        }
    }

    @Command
    public void showCategories(){
        if(activeFlag){
            try {
                db.createStatement();

                String query = "SELECT category_id,name,weight " +
                        "FROM category " +
                        "WHERE class_id = ?";
                PreparedStatement stmt = db.prepareStatement(query);
                stmt.setInt(1,currentClassId);

                ResultSet rs = stmt.executeQuery();

                System.out.println("For class " + currentCourseNum + " " + currentYear + " " +
                        currentTerm + " " + currentSection + "\nCategories:");
                while(rs.next()){
                    int catID = rs.getInt(1);
                    String name = rs.getString(2);
                    double weight = rs.getDouble(3);
                    System.out.println("Category ID = " + catID + ",  Name: " + name + ", Weight: " + weight);
                }

            } catch (SQLException sqlEx) {
                System.out.println("Exception: show cats");
            }
        }
        else{
            System.out.println("Please select a class and rerun command.");
        }

    }

    @Command
    public void addCategory(String name, double weight){
        try{
            db.createStatement();

            String query = "INSERT INTO category (class_id,name,weight) VALUES (?,?,?);";
            PreparedStatement stmt = db.prepareStatement(query);
            stmt.setInt(1,currentClassId);
            stmt.setString(2,name);
            stmt.setDouble(3,weight);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error");
        }
    }

    @Command
    public void showItems(){
        try {
            db.createStatement();

            String query = "SELECT item.name, point_value, category.name,category.category_id FROM item " +
                           "JOIN category ON item.category_id = category.category_id " +
                           "WHERE item.class_id = ? " +
                           "ORDER BY category.name";
            PreparedStatement stmt = db.prepareStatement(query);
            stmt.setInt(1,currentClassId);

            ResultSet rs = stmt.executeQuery();
            int currentCat;
            int prevCat = -1;
            rs.next();
            while(rs.next()){
                String itemName = rs.getString(1);
                double value = rs.getDouble(2);
                String catName = rs.getString(3);
                int catID = rs.getInt(4);
                currentCat = catID;
                if(currentCat != prevCat) {
                    System.out.println();
                    System.out.println("Category Name: " + catName + "\n*******************");
                    prevCat = currentCat;
                }
                System.out.println("Item Name: " + itemName + ", Point Value: " + value);
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error");
        }
    }

    @Command
    public void addItem(String itemName, String categoryName, String description, double points) {
        try {
            db.createStatement();

            String query = "INSERT INTO item (class_id, category_id, name, description, point_value) VALUES (?," +
                    "    (" +
                    "          SELECT category_id FROM category" +
                    "          WHERE category.class_id = ?" +
                    "          AND category.name = ?" +
                    "    ), ?, ?, ?);";
            PreparedStatement stmt = db.prepareStatement(query);
            stmt.setInt(1,currentClassId);
            stmt.setInt(2,currentClassId);
            stmt.setString(3,categoryName);
            stmt.setString(4,itemName);
            stmt.setString(5,description);
            stmt.setDouble(6,points);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error");
        }
    }

    @Command
    public void addStudent(String username, int studentID, String name) {
        try {
            db.createStatement();

            String query1 = "INSERT INTO student(student_id,student_name,username) VALUES (?,?,?);";
            PreparedStatement stmt1 = db.prepareStatement(query1);
            stmt1.setInt(1,studentID);
            stmt1.setString(2,name);
            stmt1.setString(3,username);
            stmt1.executeUpdate();


            String query2 = "INSERT INTO enrollment(student_id,class_id) VALUES (?,?);";
            PreparedStatement stmt2 = db.prepareStatement(query2);
            stmt2.setInt(1,studentID);
            stmt2.setInt(2,currentClassId);
            stmt2.executeUpdate();

        }catch (Exception e){
            System.out.println("Error");
        }
    }

    @Command
    public void showStudents(){
        try {
            db.createStatement();
            String showStudentsQuery = "SELECT student_name, username FROM student " +
                                       "JOIN enrollment e on student.student_id = e.student_id " +
                                       "WHERE class_id = ?;";

            PreparedStatement stmt = db.prepareStatement(showStudentsQuery);
            stmt.setInt(1, currentClassId);

            ResultSet rs = stmt.executeQuery();
            StringBuilder sb = new StringBuilder();
            Formatter f = new Formatter(sb);

            System.out.println("=============================================\n" +
                               "              Enrolled Students\n" +
                               "=============================================");

            while(rs.next()) {
                String studentName = rs.getString(1);
                String username = rs.getString(2);
                f.format("%-25s %-15s\n", studentName, username);
            }

            System.out.println(sb);
        }
        catch(SQLException sqlEx) {
            System.out.println("Error");
        }
    }

    @Command
    public void showStudents(String str){
        try {
            db.createStatement();
            String showStudentsQuery = "SELECT student_name, username, student.student_id FROM student " +
                                       "JOIN enrollment e on student.student_id = e.student_id " +
                                       "WHERE class_id = ? " +
                                       "AND student_name ILIKE '%' || ? || '%' OR username ILIKE '%' || ? || '%';";

            PreparedStatement stmt = db.prepareStatement(showStudentsQuery);
            stmt.setInt(1, currentClassId);
            stmt.setString(2, str);
            stmt.setString(3,str);

            ResultSet rs = stmt.executeQuery();
            StringBuilder sb = new StringBuilder();
            Formatter f = new Formatter(sb);

            System.out.println("=============================================\n" +
                               "              Enrolled Students\n" +
                               "=============================================\n" +
                               "ALL NAMES CONTAINING: " + str +
                               "\n--------------------------------------------");

            while(rs.next()) {
                String studentName = rs.getString(1);
                String username = rs.getString(2);
                f.format("%-25s %-15s\n", studentName, username);
            }

            System.out.println(sb);
        }
        catch(SQLException sqlEx) {
            System.out.println("Error");
        }
    }

    @Command
    public void grade(String itemName, String userName, double grade) throws SQLException {
    try {
        db.createStatement();
        String query = "INSERT INTO grade (student_id, item_id, assigned_grade) " +
                "VALUES (" +
                "           (SELECT student.student_id FROM student" +
                "            JOIN enrollment e on student.student_id = e.student_id" +
                "            WHERE username = ?" +
                "            AND class_id = ?" +
                "           ), " +
                "           (SELECT item_id FROM item" +
                "            JOIN class ON item.class_id = class.class_id" +
                "            WHERE item.class_id = ?" +
                "            AND item.name = ?" +
                "           ), " +
                "           ?)";
        PreparedStatement stmt = db.prepareStatement(query);
        stmt.setString(1,userName);
        stmt.setInt(2,currentClassId);
        stmt.setInt(3,currentClassId);
        stmt.setString(4,itemName);
        stmt.setDouble(5,grade);

        stmt.executeUpdate();

    } catch (SQLException e) {
        db.createStatement();
        String query = "UPDATE grade SET assigned_grade = ? WHERE student_id = (SELECT student.student_id FROM student" +
                "                           JOIN enrollment e on student.student_id = e.student_id " +
                "                           WHERE username = ?" +
                "                           AND class_id = ?" +
                "                           ) AND item_id = (SELECT item_id FROM item " +
                "                            JOIN class ON item.class_id = class.class_id " +
                "                            WHERE item.class_id = ?" +
                "                            AND item.name = ?" +
                "                           )";
        PreparedStatement stmt = db.prepareStatement(query);
        stmt.setDouble(1,grade);
        stmt.setString(2,userName);
        stmt.setInt(3,currentClassId);
        stmt.setInt(4,currentClassId);
        stmt.setString(5,itemName);
        stmt.executeUpdate();
    }

    }

    @Command
    public void studentGrades(String userName) {
        try {
            db.createStatement();

            String query = "SELECT item.name, category.category_id, category.name, COALESCE((grade.assigned_grade / item.point_value) * 100, 0) " +
                    "FROM item " +
                    "JOIN category ON item.category_id = category.category_id " +
                    "LEFT JOIN grade ON item.item_id = grade.item_id " +
                    "AND grade.student_id = (SELECT student_id FROM student WHERE student.username = ?)" +
                    "WHERE item.class_id = ?" +
                    "ORDER BY item.category_id ASC;";

            PreparedStatement statement = db.prepareStatement(query);

            statement.setString(1, userName);
            statement.setInt(2, currentClassId);

            ResultSet rs = statement.executeQuery();

            StringBuilder sb = new StringBuilder();

            Formatter f = new Formatter(sb);

            System.out.println("\nASSIGNED GRADES FOR STUDENT: " + userName +
                    "\n=====================================================");

            int currCategory;
            int prevCategory = -1;

            while(rs.next()) {
                currCategory = rs.getInt(2);
                if(currCategory != prevCategory) {

                    if(prevCategory != -1) {
                        String catAttemptQuery = "SELECT SUM(COALESCE(assigned_grade, 0) * (weight / 100)) / SUM(point_value * (weight / 100)) * 100\n" +
                                "FROM item\n" +
                                "JOIN grade ON item.item_id = grade.item_id\n" +
                                "JOIN category ON item.category_id = category.category_id\n" +
                                "WHERE student_id = (SELECT student_id FROM student WHERE student.username = ?)\n" +
                                "AND item.class_id = ?" +
                                "AND item.category_id = ?;";

                        PreparedStatement stmt = db.prepareStatement(catAttemptQuery);
                        stmt.setString(1, userName);
                        stmt.setInt(2, currentClassId);
                        stmt.setInt(3, currCategory - 1);

                        ResultSet rs2 = stmt.executeQuery();

                        String catTotalQuery = "SELECT SUM(COALESCE(assigned_grade, 0) * (weight / 100)) / SUM(point_value * (weight / 100)) * 100 AS overall_grade_percent\n" +
                                "FROM item\n" +
                                "LEFT JOIN grade ON item.item_id = grade.item_id\n" +
                                "AND student_id = (SELECT student_id FROM student WHERE student.username = ?)\n" +
                                "JOIN category ON item.category_id = category.category_id\n" +
                                "WHERE item.class_id = ? " +
                                "AND item.category_id = ?;";

                        PreparedStatement totalStmt =  db.prepareStatement(catTotalQuery);
                        totalStmt.setString(1, userName);
                        totalStmt.setInt(2, currentClassId);
                        totalStmt.setInt(3, currCategory - 1);

                        ResultSet rs3 = totalStmt.executeQuery();

                        if (rs2.next() && rs3.next()) {
                            sb.append("---------------------------------\n");
                            f.format( "%-25s %3.2f%1s\n", "Attempted Grade:", rs2.getDouble(1), "%");
                            f.format( "%-25s %3.2f%1s\n", "Overall Grade:", rs3.getDouble(1), "%");
                        }
                    }

                    sb.append("\n");
                    sb.append(rs.getString(3) + "\n*********************************\n");
                    prevCategory = currCategory;
                }

                f.format("%-19s %3.2f/%3.2f%n", rs.getString(1), rs.getDouble(4), 100.0);

                if(rs.isLast()) {
                    String catAttemptQuery = "SELECT SUM(COALESCE(assigned_grade, 0) * (weight / 100)) / SUM(point_value * (weight / 100)) * 100\n" +
                            "FROM item\n" +
                            "JOIN grade ON item.item_id = grade.item_id\n" +
                            "JOIN category ON item.category_id = category.category_id\n" +
                            "WHERE student_id = (SELECT student_id FROM student WHERE student.username = ?)\n" +
                            "AND item.class_id = ?" +
                            "AND item.category_id = ?;";

                    PreparedStatement stmt = db.prepareStatement(catAttemptQuery);
                    stmt.setString(1, userName);
                    stmt.setInt(2, currentClassId);
                    stmt.setInt(3, currCategory);

                    ResultSet rs2 = stmt.executeQuery();

                    String catTotalQuery = "SELECT SUM(COALESCE(assigned_grade, 0) * (weight / 100)) / SUM(point_value * (weight / 100)) * 100 AS overall_grade_percent\n" +
                            "FROM item\n" +
                            "LEFT JOIN grade ON item.item_id = grade.item_id\n" +
                            "AND student_id = (SELECT student_id FROM student WHERE student.username = ?)\n" +
                            "JOIN category ON item.category_id = category.category_id\n" +
                            "WHERE item.class_id = ? " +
                            "AND item.category_id = ?;";

                    PreparedStatement totalStmt = db.prepareStatement(catTotalQuery);
                    totalStmt.setString(1, userName);
                    totalStmt.setInt(2, currentClassId);
                    totalStmt.setInt(3, currCategory);

                    ResultSet rs3 = totalStmt.executeQuery();

                    if (rs2.next() && rs3.next()) {
                        sb.append("---------------------------------\n");
                        f.format("%-25s %3.2f%1s\n", "Attempted Grade:", rs2.getDouble(1), "%");
                        f.format("%-25s %3.2f%1s\n", "Overall Grade:", rs3.getDouble(1), "%");
                    }
                }
            }

            String classAttemptedGradeQuery = "SELECT (SUM((assigned_grade * (weight / 100))) / SUM(point_value * (weight / 100))) * 100 AS attempted_grade_percent\n" +
                    "FROM grade\n" +
                    "JOIN item ON grade.item_id = item.item_id\n" +
                    "JOIN category ON item.category_id = category.category_id\n" +
                    "WHERE student_id = (SELECT student_id FROM student WHERE student.username = ?)\n" +
                    "AND item.class_id = ?;";

            PreparedStatement ps = db.prepareStatement(classAttemptedGradeQuery);

            ps.setString(1, userName);
            ps.setInt(2, currentClassId);

            ResultSet attemptGradeResultSet = ps.executeQuery();

            if (attemptGradeResultSet.next()){
                sb.append("\n=================================\n");
                f.format( "%-25s %3.2f\n","TOTAL ATTEMPTED GRADE:", attemptGradeResultSet.getDouble(1));
            }

            String classTotalGradeQuery = "SELECT SUM(COALESCE(assigned_grade, 0) * (weight / 100)) / SUM(point_value * (weight / 100)) * 100 AS overall_grade_percent\n" +
                    "FROM item\n" +
                    "LEFT JOIN grade ON item.item_id = grade.item_id\n" +
                    "AND student_id = (SELECT student_id FROM student WHERE student.username = ?)\n" +
                    "JOIN category ON item.category_id = category.category_id\n" +
                    "WHERE item.class_id = ?;\n";

            PreparedStatement ps2 = db.prepareStatement(classTotalGradeQuery);
            ps2.setString(1, userName);
            ps2.setInt(2, currentClassId);

            ResultSet totalGradeResultSet = ps2.executeQuery();

            if(totalGradeResultSet.next()) {

                double val = totalGradeResultSet.getDouble(1);
                f.format( "%-25s %3.2f\n","TOTAL OVERALL GRADE:", val);
            }

            System.out.println(sb);
        }
        catch (SQLException sqlEx) {
            System.out.println("Error");
        }
    }

    @Command
    public void gradebook() {

        try {
            db.createStatement();
            String query = "SELECT student_id,student_name,username " +
                    "FROM enrollment " +
                    "JOIN student USING (student_id) " +
                    "WHERE class_id = ?;";

            PreparedStatement stmt = db.prepareStatement(query);
            stmt.setInt(1, currentClassId);
            ResultSet rs = stmt.executeQuery();

            String oldName = "NONE";
            String curName;
            double curGrade = -1;
            double totalPoints = 0;
            double curWeight = 0;
            while (rs.next()) {
                String sName = rs.getString(2);
                int sID = rs.getInt(1);
                db.createStatement();
                String query1 = "SELECT assigned_grade,student_id,item_id,item.name,category.weight,category.name " +
                        "FROM grade " +
                        "JOIN item USING (item_id)" +
                        "JOIN category USING (class_id) " +
                        "WHERE student_id = ? AND class_id = ?;";
                PreparedStatement stmt1 = db.prepareStatement(query1);
                stmt1.setInt(1, sID);
                stmt1.setInt(2, currentClassId);
                ResultSet rs1 = stmt1.executeQuery();
                curName = sName;
                String curCat;
                String oldCat = "NA";
                if (!curName.equals(oldName)) {
                    System.out.println("*********************************************");
                    System.out.println("Student Name: " + sName);
                    oldName = curName;
                    System.out.println("Total grade: " + dc.format((totalCalc(sID))));
                    System.out.println("Grade for Attempted Work(Based on category weight): " + dc.format(attemptCalc(sID)));
                    System.out.println("*********************************************\n");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error");
        }
    }
    private double attemptCalc(int sID){
        double total = 0;
        try {
            db.createStatement();
            String query = "SELECT category.name,category.weight, Count(item_id),Count(category.class_id),SUM(assigned_grade),COUNT(student_id) " +
                    "FROM category " +
                    "JOIN item i on category.category_id = i.category_id " +
                    "JOIN grade USING (item_id) " +
                    "WHERE category.class_id = ? AND student_id = ?" +
                    "GROUP BY category.name,category.weight;";
            PreparedStatement stmt = db.prepareStatement(query);
            stmt.setInt(1,currentClassId);
            stmt.setInt(2,sID);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                String catName = rs.getString(1);
                double weight = rs.getDouble(2);
                double sum = rs.getDouble(5);
                int count = rs.getInt(3);
                double partialTotal = (sum/(100*count))*100;
                total += (partialTotal * (weight/100));
//                System.out.println("Total for " + catName + " = " + partialTotal);
            }
        } catch (SQLException e) {
            System.out.println("Error");
        }
        return total;
    }
    private double totalCalc(int sID) {
        double total = 0;
        try {
            db.createStatement();
            String query = "SELECT SUM(COALESCE(assigned_grade,0) * (weight / 100)) / SUM(point_value * (weight / 100)) * 100 " +
                    "FROM item " +
                    "LEFT JOIN grade ON item.item_id = grade.item_id " +
                    "AND student_id = ? " +
                    "JOIN category ON item.category_id = category.category_id " +
                    "WHERE item.class_id = ?;";
            PreparedStatement stmt = db.prepareStatement(query);
            stmt.setInt(1,sID);
            stmt.setInt(2,currentClassId);

            ResultSet rs = stmt.executeQuery();

            rs.next();
            total = rs.getDouble(1);

        } catch (SQLException e) {
            System.out.println("Error");
        }
        return total;
    }
}
