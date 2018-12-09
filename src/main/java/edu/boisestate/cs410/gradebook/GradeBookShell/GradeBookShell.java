package edu.boisestate.cs410.gradebook.GradeBookShell;

import com.budhash.cliche.Command;
import com.budhash.cliche.ShellFactory;

import java.io.IOException;
import java.sql.*;

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
            sqlEx.printStackTrace();
        }
    }

    @Command
    public void selectClass(String courseNum, int year, String term){
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
            sqlEx.printStackTrace();
        }
    }

    @Command
    public void selectClass(String courseNum, int year, String term, int section){
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
            sqlEx.printStackTrace();
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
            System.out.println("No active class selected.");
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

                System.out.println("For class = " + currentCourseNum + " " + currentYear + " " +
                        currentTerm + " " + currentSection + "\nCategories:\n");
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

    }

    @Command
    public void showStudents(String str){

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
