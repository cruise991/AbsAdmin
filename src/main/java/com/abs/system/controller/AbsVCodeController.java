package com.abs.system.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abs.system.filter.NoNeedLogin;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.StrUtil;
import com.alibaba.fastjson.JSONObject;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/vcode")
public class AbsVCodeController {

	private Logger logger = LoggerFactory.getLogger(AbsVCodeController.class);

	
	
	@NoNeedLogin
	@ResponseBody
	@RequestMapping("/createvcode")
	public JSONObject createvcode(@RequestBody Map<String, String> map, HttpServletRequest request) throws Exception {
		// 首先是产生一个随机数字，或者创建一个图片
		String path = this.getClass().getResource("/").getPath();
		String filename = "font/ITCKRIST.ttf";
		logger.info("当前的字体：{}", path + filename);
		String vcodebg = "images/vcode_bj.png";
		File img = new File(path+vcodebg);
		int width=100;
		int height=50;
		
		BufferedImage bi = resize(ImageIO.read(new FileInputStream(img)), width, height);
		String text = StrUtil.getRanChars();
		File file = new File(path + filename);
		Graphics2D g = (Graphics2D) bi.createGraphics();
		g.setColor(Color.BLACK);
		Font font = null;
		font = Font.createFont(Font.TRUETYPE_FONT, file);
		font = font.deriveFont(Font.ITALIC, 30);
		GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		localGraphicsEnvironment.registerFont(font);
		g.setFont(font);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// 计算文字长度，计算居中的x点坐标
		FontMetrics fm = g.getFontMetrics(font);
		int textWidth = fm.stringWidth(text);
		int widthX = (width - textWidth) / 2;
		// 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
		g.drawString(text, widthX, 35);

        for(int i=0;i<2;i++) {
    	    int startX=(int) (Math.random()*20);
   		    int startY=(int) (Math.random()*20);
   		    int endX=20+(int) (Math.random()*80);
   		    int endY=20+(int) (Math.random()*30);
   		    g.drawLine(startX,startY,endX,endY);  
        }

		g.dispose();

		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ImageOutputStream imOut=ImageIO.createImageOutputStream(bs);;
		ImageIO.write(bi, "png",imOut);
		InputStream byteInputStream = new ByteArrayInputStream(bs.toByteArray());
		byteInputStream.close();
		byte [] bytes=readInputStream(byteInputStream);
		String base64str=Base64.getEncoder().encodeToString(bytes);
		return BuildJsonOfObject.getJsonString(base64str,text,MSG.SUCCESSCODE);
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
		g.dispose();
		return dimg;
	}
	
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}
	


}
