package Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.imageio.ImageIO;

public class DBImageReader {

	public class DBImageData {
		public String ContentType;
		public byte[] Data;

	}

	/***
	 * Считывает указанное изображение из справочника знаков в дескриптор
	 * (ContentType+Byte[])
	 * 
	 * @param deviceType
	 * @param imageCode
	 * @return
	 */
	public static DBImageData ReadImageData(String deviceType, String imageCode) {
		DBImageData result = (new DBImageReader()).new DBImageData();
		Connection c = null;
		PreparedStatement stmt = null;
		String actions_pwd;

		try {
			// Class.forName("org.postgresql.Driver");
			actions_pwd = Application.GetProperties().getProperty("actions_pwd");

			c = DriverManager.getConnection("jdbc:postgresql://172.16.10.21:5432/actions", "postgres", actions_pwd);
			// System.out.println("Opened database successfully");

			String sql = "select * from actions_schema.signs where device_type=? and sign_code=?";
			stmt = c.prepareStatement(sql);
			stmt.setString(1, deviceType);
			stmt.setString(2, imageCode);
			ResultSet resultSet = stmt.executeQuery();
			resultSet.next();

			result.ContentType = resultSet.getString("content_type");
			result.Data = resultSet.getBinaryStream("sign_file").readAllBytes();

			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			result = null;
		}

		return result;
	}
}
