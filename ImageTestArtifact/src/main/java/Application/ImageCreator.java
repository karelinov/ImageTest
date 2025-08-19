package Application;

import java.io.Writer;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;

import org.apache.batik.dom.AbstractDOMImplementation;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import Application.DBImageReader.DBImageData;

public class ImageCreator {
	public static byte[] CreatePNGImage() {
		/*
		 * Canvas canvas = new Canvas(416, 88); final GraphicsContext gc =
		 * canvas.getGraphicsContext2D(); gc.drawImage(GetActionDBImage("VMS", "12"), 0,
		 * 0);
		 * 
		 * WritableImage writableImage = canvas.snapshot(null, null); bufferedImage =
		 * SwingFXUtils.fromFXImage(writableImage, null); //
		 * ImageIO.write(bufferedImage, format, outputFile)
		 */

		byte[] result = null;

		try {
			BufferedImage canvasImage = new BufferedImage(416, 88, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2d = canvasImage.createGraphics();

			// Фон
			g2d.setPaint(Color.gray);
			g2d.fillRect(0, 0, 416, 88);

			// Текст
			g2d.setPaint(Color.yellow);
			g2d.setFont(new Font("SansSerif", Font.PLAIN, 24));
			g2d.drawString("ПРИВЕТ", 220, 30);

			// PNG
			DBImageReader.DBImageData dbImageData1 = DBImageReader.ReadImageData("VMS", "12");
			BufferedImage bimage = ImageIO.read(new ByteArrayInputStream(dbImageData1.Data));
			g2d.drawImage(bimage, 0, 0, null);

			// ещё текст
			g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
			g2d.drawString("PNG VMS 12", 0, 76);

			// Выплёвываем в bytearray
			g2d.dispose();
			ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
			ImageIO.write(canvasImage, "PNG", baos1);
			result  = baos1.toByteArray();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			result = null;
		}

		return result;
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

			// Текст
			g2d.setPaint(Color.yellow);
			g2d.setFont(new Font("SansSerif", Font.PLAIN, 24));
			g2d.drawString("ПРИВЕТ", 220, 30);

			// PNG
			DBImageReader.DBImageData dbImageData1 = DBImageReader.ReadImageData("VMS", "12");
			BufferedImage bimage = ImageIO.read(new ByteArrayInputStream(dbImageData1.Data));
			g2d.drawImage(bimage, 0, 0, null);

			// ещё текст
			g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
			g2d.drawString("PNG VMS 12", 0, 76);

			// SVG
			DBImageReader.DBImageData dbImageData2 = DBImageReader.ReadImageData("VMS", "11");
			PNGTranscoder t = new PNGTranscoder(); // тут нехорошее преобразование в растр, но не сильно повлияло на
													// качество
			TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(dbImageData2.Data));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			TranscoderOutput output = new TranscoderOutput(baos);
			t.transcode(input, output);

			BufferedImage bimage2 = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
			g2d.drawImage(bimage2, 100, 0, 64, 64, null);

			// ещё текст
			g2d.drawString("SVG VMS 11", 100, 76);

			// Выплёвываем в bytearray
			boolean useCSS = true; // we want to use CSS style attributes
			ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
			Writer out = new OutputStreamWriter(baos1, "UTF-8");
			g2d.stream(out, useCSS);
			result = baos1.toByteArray();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			result = null;
		}

		return result;
	}

}
