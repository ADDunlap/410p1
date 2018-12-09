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
//            ShellFactory.createConsoleShell("charity", "", shell)
//                    .commandLoop();
        }
    }

    @Command
    public void new_class(String courseNum, String term, int year, int section, String meetTimes)
    {
        try {
            db.createStatement();

            String insertStatement = "INSERT INTO class (course_number, year, term, section, meeting_times)" +
                                     "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement statement = db.prepareStatement(insertStatement);
            statement.setString(1, courseNum);
            statement.setInt(2, year);
            statement.setString(3, term);
            statement.setInt(4, section);
            statement.setString(5, meetTimes);

            ResultSet rs = statement.executeQuery();

        }
        catch(SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
}
