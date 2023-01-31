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
import database.models.DynastyModels;

public class DynastyDAO implements IDataAccessObject<DynastyModels> {
	/**
	 * Tạo đối tượng dynastyDAO
	 * @return Đối tượng dynastyDAO
	 */
	public static DynastyDAO getInstance() {
		return new DynastyDAO();
	}
	
	/**
	 * Phương thức này được sử dụng để tạo bảng lưu trữ dữ liệu của đối tượng dynastyModels trong MySQL.
	 * Có thể biết được bảng gồm những trường dữ liệu và kiểu dữ liệu của trường đó
	 * @author NGUYEN VAN
	 */
	@Override
	public void createTable() {
		try {
			Connection con = JDBCUtil.getConnection();

			Statement st = con.createStatement();
	
			String url = "CREATE TABLE IF NOT EXISTS history.dynasty ( name VARCHAR(200) PRIMARY KEY NOT NULL, id TEXT, information TEXT);";

			st.executeUpdate(url);

			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Chèn đối tượng vào bảng dynasty.
	 * Phương thức sẽ đọc dữ liệu trong file Json sau đó phân tích dữ liệu và đưa vào một mảng đối tượng JSON.
	 * Duyệt từng đối tượng trong mảng sau đó chèn dữ liệu của đối tượng vào bảng dynasty trong MySQL.
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

				// Chèn dữ liệu của đối tượng vào bảng dynasty
				try {
					Connection con = JDBCUtil.getConnection();

					Statement st = con.createStatement();

					String url = "INSERT INTO history.dynasty(name, id, information) " + "VALUES( '" + name + "', '"
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
	 * Phương thức được sử dụng để cập nhật dữ liệu của 1 đối tượng dynastyModels. 
	 * Sử dụng tên của đối tượng để tìm kiếm trong MySQL và cập nhật các thông tin của đối tượng được truyền vào
	 * @param t Đối tượng cần cập nhật dữ liệu
	 * @return Số đối tượng được cập nhật thành công
	 */
	@Override
	public int update(DynastyModels t) {
		try {
			Connection con = JDBCUtil.getConnection();

			Statement st = con.createStatement();

			String sql = "UPDATE history.dynasty " + "SET " + "name = '" + t.getName() + "'', " + "id = '"
					+ t.getId() + "', " + "information = '" + t.getInfo() + "' "
					+ " WHERE name = '" + t.getName() + "';  ";

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
	 * @param t Đối tượng dynastyModels cần xóa 
	 * @return Số lượng đối tượng tìm được và đã xóa dữ liệu trong MySQL
	 */
	@Override
	public int delete(DynastyModels t) {
		try {
			Connection con = JDBCUtil.getConnection();

			Statement st = con.createStatement();

			String sql = "DELETE FROM history.dynasty WHERE name = " + t.getName() + "; ";

			System.out.println("Trạng thái: " + (st.executeUpdate(sql) != 0));

			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Phương thức được sử dụng truy vấn tất cả dữ liệu trong bảng dynasty
	 * @return ArrayList đối tượng dynastyModels với 3 trường dữ liệu name - id - information
	 */
	@Override
	public ArrayList<DynastyModels> selectAll() {
		ArrayList<DynastyModels> list = new ArrayList<DynastyModels>();
		try {
			Connection con = JDBCUtil.getConnection();

			Statement st = con.createStatement();

			String url = "SELECT * FROM history.dynasty ORDER BY name;";

			ResultSet rs = st.executeQuery(url);

			while (rs.next()) {
				String nameDynasty = rs.getString("name");
				String idDynasty = rs.getString("id");
				String informationDynasty = rs.getString("information");

				DynastyModels dynasty = new DynastyModels(nameDynasty, idDynasty, informationDynasty, null);
				list.add(dynasty);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Phương thức được sử dụng để truy vấn đối tượng dynastyModels theo tên của đối tượng
	 * @param nameModels Tên của đối tượng cần tìm kiếm
	 * @return Đối tượng dynastyModels có 3 trường dữ liệu name - id - information
	 */
	@Override
	public DynastyModels selectById(String nameModels) {
		DynastyModels ketQua = null;
		try {
			Connection con = JDBCUtil.getConnection();

			Statement st = con.createStatement();

			String sql = "SELECT * FROM history.dynasty WHERE name = '" +nameModels+ "' ;" ;

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				String name = rs.getString("name");
				String id = rs.getString("id");
				String info = rs.getString("information");

				DynastyModels dynasty = new DynastyModels(name, id, info, null);
				ketQua = dynasty;
			}

			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ketQua;
	}

	/**
	 * Phương thức được sử dụng để truy vấn đối tượng dynastyModels tùy thuộc vào điều kiện
	 * @param condition Điều kiện truy vấn
	 * @return ArrayList đối tượng dynastyModels có 3 trường dữ liệu name - id - information
	 */
	@Override
	public ArrayList<DynastyModels> selectByCondition(String condition) {
		ArrayList<DynastyModels> list = new ArrayList<DynastyModels>();
		try {
			Connection con = JDBCUtil.getConnection();

			Statement st = con.createStatement();

			String sql = "SELECT * FROM history.dynasty WHERE " + condition + " ORDER BY name;";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				String nameDynasty = rs.getString("name");
				String idDynasty = rs.getString("id");
				String informationDynasty = rs.getString("information");

				DynastyModels dynasty = new DynastyModels(nameDynasty, idDynasty, informationDynasty, null);
				list.add(dynasty);
			}

			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
