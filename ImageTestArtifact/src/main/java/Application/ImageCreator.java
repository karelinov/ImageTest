package Application;

import java.io.Writer;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;

import org.apache.batik.dom.AbstractDOMImplementation;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class ImageCreator {
	public static BufferedImage CreatePNGImage() {
		BufferedImage bufferedImage = null;
		/*
		 * Canvas canvas = new Canvas(416, 88); final GraphicsContext gc =
		 * canvas.getGraphicsContext2D(); gc.drawImage(GetActionDBImage("VMS", "12"), 0,
		 * 0);
		 * 
		 * WritableImage writableImage = canvas.snapshot(null, null); bufferedImage =
		 * SwingFXUtils.fromFXImage(writableImage, null); //
		 * ImageIO.write(bufferedImage, format, outputFile)
		 */
		return bufferedImage;
	}

	public static byte[] CreateSVGImage() {
		byte[] result = null;

		try {

			// основной svg документ и SVGGraphics2D для рисования
			DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
			String svgNS = "http://www.w3.org/2000/svg";
			Document document = domImpl.createDocument(svgNS, "svg", null);
			SVGGraphics2D g2d = new SVGGraphics2D(document);

			// Фон
			g2d.setPaint(Color.gray);
			g2d.fillRect(0, 0, 416, 88);
			
			// Выплёвываем в bytearray
		    boolean useCSS = true; // we want to use CSS style attributes
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    Writer out = new OutputStreamWriter(baos, "UTF-8"); 
		    g2d.stream(out, useCSS);
		    result = baos.toByteArray();
		    		

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			result = null;
		}

		return result;
	}

}
