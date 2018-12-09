package edu.boisestate.cs410.gradebook.GradeBookShell;

import com.budhash.cliche.Command;
import com.budhash.cliche.ShellFactory;

import java.io.IOException;
import java.sql.*;

public class GradeBookShell {

    private final Connection db;
    private int currentClassId;

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
        try (Connection cxn = DriverManager.getConnection("jdbc: " + dbUrl)) {
            GradeBookShell shell = new GradeBookShell(cxn);
            ShellFactory.createConsoleShell("charity", "", shell)
                    .commandLoop();
        }
    }

    @Command
    public void new_class(String courseNum, String term, int year, int section, String class_description, String meetTimes)
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
    public void select_class(String courseNum){
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
                    System.out.println("Error: Multiple courses with course number: " + courseNum);
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
    public void select_class(String courseNum, String year, String term){

    }

    @Command
    public void select_class(String courseNum, String year, String term, int section){

    }

    @Command
    public void show_class(){

    }

    @Command
    public void show_categories(){

    }

    @Command
    public void add_category(String name, float weight){

    }

    @Command
    public void show_items(){

    }

    @Command
    public void add_item(String itemName, String categoryName, String description, float points) {

    }

    @Command
    public void add_student(String username, int studentID, String name) {

    }

    @Command
    public void show_students(){

    }

    @Command
    public void show_students(String str){

    }

    @Command
    public void grade(String itemName, String userName, double grade){

    }

    @Command
    public void student_grades(String userName) {

    }

    @Command
    public void gradebook(){

    }

}
