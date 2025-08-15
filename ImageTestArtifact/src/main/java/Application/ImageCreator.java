package Application;

/*
import javafx.scene.Node;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
*/

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageCreator {
	public static BufferedImage CreateImage() {
		BufferedImage bufferedImage = null;

		Platform.runLater(() -> {

			Canvas canvas = new Canvas(416, 88);
			final GraphicsContext gc = canvas.getGraphicsContext2D();
			gc.drawImage(GetActionDBImage("VMS", "12"), 0, 0);

			WritableImage writableImage = canvas.snapshot(null, null);
			bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
			// ImageIO.write(bufferedImage, format, outputFile)

		});
		return bufferedImage;
	}

	/***
	 * Получение java.awt.Image из БД Actions
	 * вероятно только растровый
	 * @param deviceType
	 * @param imageCode
	 * @return
	 */
	private static java.awt.Image GetActionDBImage(String deviceType, String imageCode) {
	 java.awt.Image result = null;
	 Connection c = null;
     PreparedStatement stmt = null;
     String actions_pwd;
     
     try {
	     //Class.forName("org.postgresql.Driver");
    	 actions_pwd = Application.GetProperties().getProperty("actions_pwd");
    	 
	     c = DriverManager.getConnection("jdbc:postgresql://172.16.10.21:5432/actions","postgres", actions_pwd);
	     System.out.println("Opened database successfully");
	
	     String sql = "select * from actions_schema.signs where device_type=? and sign_code=?";
	     stmt = c.prepareStatement(sql);
	     stmt.setString(1, deviceType);
	     stmt.setString(2, imageCode);
	     ResultSet resultSet =  stmt.executeQuery();
	     resultSet.next();
	     result = ImageIO.read(resultSet.getBinaryStream("sign_file")); 
	     stmt.close();
	     c.close();
     }
     catch(Exception e) {
    	 System.err.println( e.getClass().getName()+": "+ e.getMessage());
     }
	     
     return result;
	}

}
