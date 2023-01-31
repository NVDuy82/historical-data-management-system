package database.dao;

import java.util.ArrayList;

public interface IDataAccessObject<T> {
	/**
	 * Tạo bảng dữ liệu
	 */
	public void createTable();
	
	/**
	 * Chèn dữ liệu vào MySQL
	 * @param fileName Đường dẫn tới file Json chứa dữ liệu
	 */
	public void insertData(String fileName);

	/**
	 * Cập nhật dữ liệu trong MySQL
	 * @param t Đối tượng cần cập nhật
	 * @return Số lượng bản ghi được cập nhật
	 */
	public int update(T t);

	/**
	 * Xóa dữ liệu của đối tượng
	 * @param t Đối tượng cần xóa dữ liệu
	 * @return Số bản ghi được xóa
	 */
	public int delete(T t);

	/**
	 * Truy vấn toàn bộ dữ liệu
	 * @return ArrayList chứa các đối tượng
	 */
	public ArrayList<T> selectAll();

	/**
	 * Truy vấn đối tượng dự theo tên
	 * @param nameModels Tên đối tượng cần truy vấn
	 * @return Đối tượng cần truy vấn
	 */
	public T selectById(String nameModels);

	/**
	 * Truy vấn đối tượng theo điều kiện
	 * @param condition Điều kiện truy vấn
	 * @return ArrayList đối tượng thỏa mãn điều kiện
	 */
	public ArrayList<T> selectByCondition(String condition);

}
