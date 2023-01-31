package database.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
	/**
	 * Tạo kết nối với cơ sở dữ liệu - Trong trường hợp này sử dụng cơ sở dữ liệu MySQL với tài khoản và mật khẩu
	 * @Nguyen_Van
	 */
	public static Connection getConnection() {
		Connection c = null;
		try {
			// Đăng ký MySQL Driver với DriverManager
			DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

			// Các thông số 
			String url = "jdbc:mySQL://localhost:3306"; // Đường link kết nối với Mysql
			String username = "root"; // Tên của Mysql
			String password = "123456"; // Mật khẩu Mysql

			// Tạo kết nối tới Mysql
			c = DriverManager.getConnection(url, username, password);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	/**
	 * Đóng kết nối với MySQL
	 * @param close Đối tượng Connection được khởi tạo khi thực hiện tạo kết nối tới MySQL
	 * @Nguyen_Van
	 */
	public static void closeConnection(Connection close) {
		try {
			if (close != null) {
				close.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}