package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;
    
    public UserDAO() {
        try {
            String dbURL = "jdbc:mysql://localhost:3306/BBS";
            String dbID = "root";
            String dbPassword = ""; // 실제 비밀번호 사용
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
            System.out.println("Database connection successful.");
        } catch (Exception e) { 
            e.printStackTrace();
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
    
    public int join(User user) {
        String SQL = "INSERT INTO USER (userID, userPassword, userName, userGender, userEmail) VALUES (?, ?, ?, ?, ?)";
        try {
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, user.getUserID());
            pstmt.setString(2, user.getUserPassword());
            pstmt.setString(3, user.getUserName());
            pstmt.setString(4, user.getUserGender());
            pstmt.setString(5, user.getUserEmail());
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database query failed: " + e.getMessage());
        }
        return -1; // 데이터베이스 오류
    }
    
    public int login(String userID, String userPassword) {
        String SQL = "SELECT userPassword FROM USER WHERE userID = ?";
        try {
            if (conn == null) {
                System.out.println("Connection is null.");
                return -2; // 데이터베이스 오류
            }
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, userID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getString(1).equals(userPassword)) {
                    return 1; // 로그인 성공
                } else {
                    return 0; // 비밀번호 불일치
                }
            }
            return -1; // 아이디 없음
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database query failed: " + e.getMessage());
        }
        return -2; // 데이터베이스 오류
    }
}
