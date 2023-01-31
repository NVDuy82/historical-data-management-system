package database.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.jdbc.JDBCUtil;
import database.models.Models;

public class ConnectDAO {
	/**
	 * Tạo đối tượng connectDAO
	 * @return Đối tượng connectDAO
	 */
	public static ConnectDAO getInstance() {
		return new ConnectDAO();
	}

	/**
	 * Phương thức này được sử dụng để tạo bảng lưu trữ dữ liệu liên kết trong MySQL.
	 * Có thể biết được bảng gồm những trường dữ liệu nào và kiểu dữ liệu của trường đó
	 * @author NGUYEN VAN
	 */
	public void createTable() {
		try {
			Connection con = JDBCUtil.getConnection();

			Statement st = con.createStatement();

			String url = "CREATE TABLE IF NOT EXISTS history.connect (name VARCHAR(200) PRIMARY KEY NOT NULL, connect_1 TEXT, connect_2 TEXT, connect_3 TEXT, "
					+ "connect_4 TEXT, connect_5 TEXT, connect_6 TEXT);";

			st.executeUpdate(url);

			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Phương thức này được sử dụng để truy vấn danh sách các liên kết của một đối tượng
	 * @param name Tên đối tượng cần lấy danh sách liên kết
	 * @return Danh sách liên kết của đối tượng cần truy vấn
	 */
	public ArrayList<String> selectById(String name) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			Connection con = JDBCUtil.getConnection();

			Statement st = con.createStatement();

			String url = "SElECT * FROM history.connect WHERE name = '" + name + "';";

			ResultSet rs = st.executeQuery(url);

			while (rs.next()) {
				for (int i = 2; i <= 6; i++) {
					list.add(rs.getString(i));
				}
			}

			JDBCUtil.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Tìm kiếm một đối tượng trong database
	 * @param nameSearch Tên đối tượng cần tìm kiếm
	 * @return Đối tượng cần tìm kiếm
	 */
	public Models search(String nameSearch) {
		Models ketQua = null;

		int index = 0;
		while (ketQua == null) {
			switch (index) {
			case 0:
				ketQua = CharacterDAO.getInstance().selectById(nameSearch);
				break;
			case 1:
				ketQua = DynastyDAO.getInstance().selectById(nameSearch);
				break;
			case 2:
				ketQua = EventDAO.getInstance().selectById(nameSearch);
				break;
			case 3:
				ketQua = FestivalDAO.getInstance().selectById(nameSearch);
				break;
			case 4:
				ketQua = PlaceDAO.getInstance().selectById(nameSearch);
				break;
			}
			index++;
			if(index>4) break;
		}
		return ketQua;
	}
}
