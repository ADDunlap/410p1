package edu.boisestate.cs410.gradebook.GradeBookShell;

import com.budhash.cliche.Command;
import com.budhash.cliche.ShellFactory;

import java.io.IOException;
import java.sql.*;
import java.util.Formatter;

public class GradeBookShell {

    private final Connection db;
    private int currentClassId = 1;

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
        String dbUrl = "postgresql://localhost:31094/final_proj_db?user=tonyvonwolfe&password=RedMazdaMiata1995";

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

            ResultSet rs = statement.executeQuery();

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

            String selectStatement = "SELECT class_id FROM class " +
                                     "WHERE course_number = ?;";

            PreparedStatement stmt = db.prepareStatement(selectStatement);
            stmt.setString(1, courseNum);

            ResultSet rs = stmt.executeQuery();


        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    @Command
    public void selectClass(String courseNum, String year, String term){

    }

    @Command
    public void selectClass(String courseNum, String year, String term, int section){

    }

    @Command
    public void showClass(){

    }

    @Command
    public void showCategories(){

    }

    @Command
    public void addCategory(String name, float weight){

    }

    @Command
    public void showItems(){

    }

    @Command
    public void addItem(String itemName, String categoryName, String description, float points) {

    }

    @Command
    public void addStudent(String username, int studentID, String name) {

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
            sqlEx.printStackTrace();
        }
    }

    @Command
    public void showStudents(String str){
        try {
            db.createStatement();
            String showStudentsQuery = "SELECT student_name, username, student.student_id FROM student " +
                                       "JOIN enrollment e on student.student_id = e.student_id " +
                                       "WHERE class_id = ? " +
                                       "AND student_name ILIKE '%' || ? || '%';";

            PreparedStatement stmt = db.prepareStatement(showStudentsQuery);
            stmt.setInt(1, currentClassId);
            stmt.setString(2, str);

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
            sqlEx.printStackTrace();
        }
    }

    @Command
    public void grade(String itemName, String userName, double grade){

    }

    @Command
    public void studentGrades(String userName) {

    }

    @Command
    public void gradebook(){

    }

}
