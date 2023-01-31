package database.dao;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import database.jdbc.JDBCUtil;
import database.models.CharacterModels;

public class CharacterDAO implements IDataAccessObject<CharacterModels> {
	/**
	 * Tạo đối tượng characterDAO
	 * @return Đối tượng characterDAO
	 */
	public static CharacterDAO getInstance() {
		return new CharacterDAO();
	}

	/**
	 * Phương thức này được sử dụng để tạo bảng lưu trữ dữ liệu của đối tượng characterModels trong MySQL.
	 * Có thể biết được bảng gồm những trường dữ liệu và kiểu dữ liệu của trường đó
	 * @author NGUYEN VAN
	 */
	@Override
	public void createTable() {
		try {
			// Tạo đối tượng kết nối với MySQL
			Connection con = JDBCUtil.getConnection();
			// Tạo đối tượng Statement
			Statement st = con.createStatement();
			// Khởi tạo câu lệnh truy vấn MySQL
			String url = "CREATE TABLE IF NOT EXISTS history.character ( name VARCHAR(200) PRIMARY KEY NOT NULL, id TEXT, information TEXT);";
			// Thực hiện truy vấn
			st.executeUpdate(url);
			// Đóng kết nối với MySQL
			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Chèn đối tượng vào bảng character.
	 * Phương thức sẽ đọc dữ liệu trong file Json sau đó phân tích dữ liệu và đưa vào một mảng đối tượng JSON.
	 * Duyệt từng đối tượng trong mảng sau đó chèn dữ liệu của đối tượng vào bảng character trong MySQL.
	 * Đối với các liên kết của đối tượng sẽ được chèn vào bảng Connect trong MySQL
	 * @param fileName Đường dẫn tới file Json chứa dữ liệu 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void insertData(String fileName) {
		// Tạo đối tượng JSONParse
		JSONParser jsonParser = new JSONParser();

		// Đọc file
		try (FileReader reader = new FileReader(fileName)) {
			// Phân tích dữ liệu file Json
			Object obj = jsonParser.parse(reader);

			// Mảng chứa các đối tượng kiểu JSON
			JSONArray listArray = (JSONArray) obj;

			// Duyệt mảng JSON
			for (int i = 0; i < listArray.size(); i++) {
				JSONObject object = (JSONObject) listArray.get(i);

				String name = (String) object.get("name");
				String id = (String) object.get("id");
				String information = (String) object.get("info");
				ArrayList<String> connect = (ArrayList<String>) object.get("connect");

				// Chèn dữ liệu vào bảng Character
				try {
					Connection con = JDBCUtil.getConnection();

					Statement st = con.createStatement();

					String url = "INSERT INTO history.character(name, id, information) " + "VALUES( '" + name + "', '"
							+ id + "', '" + information + "') ON DUPLICATE KEY UPDATE name = '" + name + "';";

					st.executeUpdate(url);

					JDBCUtil.closeConnection(con);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Ghi các liên kết vào bảng Connect
				try {
					Connection con = JDBCUtil.getConnection();

					Statement st = con.createStatement();

					String url0 = "INSERT INTO history.connect(name) " + "VALUES('" + name + "');";
					st.executeUpdate(url0);

					for (int j = 0; j < connect.size(); j++) {
						String urlConnect = "UPDATE history.connect SET connect_" + (j + 1) + " = '" + connect.get(j)
								+ "' WHERE name = '" + name + "' ;";

						st.executeUpdate(urlConnect);
					}
					
					JDBCUtil.closeConnection(con);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Phương thức được sử dụng để cập nhật dữ liệu của 1 đối tượng characterModels
	 * Sử dụng tên của đối tượng để tìm kiếm trong MySQL và cập nhật các thông tin của đối tượng được truyền vào
	 * @param t Đối tượng cần cập nhật dữ liệu
	 * @return Số đối tượng được cập nhật thành công
	 */
	@Override
	public int update(CharacterModels t) {
		try {
			Connection con = JDBCUtil.getConnection();

			Statement st = con.createStatement();

			String sql = "UPDATE history.character " + "SET " + "name = '" + t.getName() + "'', " + "id = '" + t.getId()
					+ "', " + "information = '" + t.getInfo() + "' " + " WHERE name = '" + t.getName() + "';  ";

			System.out.println("Trạng thái: " + (st.executeUpdate(sql) != 0));

			JDBCUtil.closeConnection(con);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Phương thức được sử dụng để xóa dữ liệu của đối tượng trong MySQL 
	 * Xác định tên của đối tượng, tìm kiếm trong MySQL dựa theo tên và xóa dữ liệu của đối tượng tìm được trong MySQL
	 * @param t Đối tượng character cần xóa 
	 * @return Số lượng đối tượng tìm được và đã xóa dữ liệu trong MySQL
	 */
	@Override
	public int delete(CharacterModels t) {
		try {
			Connection con = JDBCUtil.getConnection();

			Statement st = con.createStatement();

			String sql = "DELETE FROM history.character WHERE name = " + t.getName() + "; ";

			System.out.println("Trạng thái: " + (st.executeUpdate(sql) != 0));

			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Phương thức được sử dụng truy vấn tất cả dữ liệu trong bảng Character
	 * @return ArrayList đối tượng CharacterModels với 3 trường dữ liệu name - id - information
	 */
	@Override
	public ArrayList<CharacterModels> selectAll() {
		ArrayList<CharacterModels> list = new ArrayList<CharacterModels>();
		try {
			Connection con = JDBCUtil.getConnection();

			Statement st = con.createStatement();

			String url = "SELECT * FROM history.character ORDER BY name;";

			ResultSet rs = st.executeQuery(url);

			while (rs.next()) {
				String name = rs.getString("name");
				String id = rs.getString("id");
				String information = rs.getString("information");

				CharacterModels character = new CharacterModels(name, id, information, null);
				list.add(character);
			}
			JDBCUtil.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Phương thức được sử dụng để truy vấn đối tượng Character theo tên của đối tượng
	 * @param nameModels Tên của đối tượng cần tìm kiếm
	 * @return Đối tượng CharacterModels có 3 trường dữ liệu name - id - information
	 */
	@Override
	public CharacterModels selectById(String nameModels) {
		CharacterModels ketQua = null;
		try {
			Connection con = JDBCUtil.getConnection();

			Statement st = con.createStatement();

			String sql = "SELECT * FROM history.character WHERE name = '" + nameModels + "' ;";

			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next()) {
				String name = rs.getString("name");
				String id = rs.getString("id");
				String information = rs.getString("information");

				CharacterModels character = new CharacterModels(name, id, information, null);
				ketQua = character;
			}
			
			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ketQua;
	}

	/**
	 * Phương thức được sử dụng để truy vấn đối tượng CharacterModels tùy thuộc vào điều kiện
	 * @param Condition Điều kiện truy vấn
	 * @return ArrayList đối tượng CharacterModels có 3 trường dữ liệu name - id - information
	 */
	@Override
	public ArrayList<CharacterModels> selectByCondition(String condition) {
		ArrayList<CharacterModels> list = new ArrayList<CharacterModels>();
		try {
			Connection con = JDBCUtil.getConnection();

			Statement st = con.createStatement();

			String sql = "SELECT * FROM history.character WHERE " + condition + " ORDER BY name;";

			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next()) {
				String name = rs.getString("name");
				String id = rs.getString("id");
				String information = rs.getString("information");

				CharacterModels character = new CharacterModels(name, id, information, null);
				list.add(character);
			}

			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}