package servlet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.OrientationRequested;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.prism.Image;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Servlet implementation class YanZhengMa
 */
@WebServlet("/YanZhengMa")
public class YanZhengMa extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public YanZhengMa() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int width=70,height=22;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.getGraphics();
		Graphics2D graphics2d = (Graphics2D)graphics;
		Random random=new Random();  
		Font mfont=new Font("楷体",Font.BOLD,16);
		graphics.setColor(getRandColor(200,250));  
        graphics.fillRect(0, 0, width, height);    //绘制背景  
        graphics.setFont(mfont);                   //设置字体  
        graphics.setColor(getRandColor(180,200));  
        for(int i=0;i<100;i++){  
            int x=random.nextInt(width-1);  
            int y=random.nextInt(height-1);  
            int x1=random.nextInt(6)+1;  
            int y1=random.nextInt(12)+1;  
            BasicStroke bs=new BasicStroke(2f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL); //定制线条样式  
            Line2D line=new Line2D.Double(x,y,x+x1,y+y1);  
            graphics2d.setStroke(bs);  
            graphics2d.draw(line);     //绘制直线  
        } 
        String string="1234567809qwertyuiopasdfghjklzxcvbnm";
        String code = "";
        String one = "";
        for (int i = 0; i < 4; i++) {
			one = string.charAt(random.nextInt(36)) + "";
			code+=one;  
	            Color color=new Color(20+random.nextInt(110),20+random.nextInt(110),random.nextInt(110));  
	            graphics.setColor(color);  
	            //将生成的随机数进行随机缩放并旋转制定角度 PS.建议不要对文字进行缩放与旋转,因为这样图片可能不正常显示  
	            /*将文字旋转制定角度*/  
	            Graphics2D g2d_word=(Graphics2D)graphics;  
	            AffineTransform trans=new AffineTransform();  
	            trans.rotate((45)*3.14/180,15*i+8,7); 
	            float scaleSize=random.nextFloat()+0.8f;  
	            if(scaleSize>1f) scaleSize=1f;  
	            trans.scale(scaleSize, scaleSize);  
	            g2d_word.setTransform(trans);  
	            graphics.drawString(one, 15*i+10, 14);
		}
        System.out.println(code);
        if(request.getSession(true).getAttribute("YanZhengMa") != null) {
            request.getSession().removeAttribute("YanZhengMa");
        }
        request.getSession(true).setAttribute("YanZhengMa",code);  
        graphics.dispose();
        ByteArrayOutputStream os = new ByteArrayOutputStream();// 新建流。
        ImageIO.write(image, "png", os);
        byte b[] = os.toByteArray();
        String base64String = new BASE64Encoder().encode(b);
        response.getWriter().print(base64String);
	}
	 public Color getRandColor(int s,int e){  
	        Random random=new Random ();  
	        if(s>255) s=255;  
	        if(e>255) e=255;  
	        int r,g,b;  
	        r=s+random.nextInt(e-s);    //随机生成RGB颜色中的r值  
	        g=s+random.nextInt(e-s);    //随机生成RGB颜色中的g值  
	        b=s+random.nextInt(e-s);    //随机生成RGB颜色中的b值  
	        return new Color(r,g,b);  
	    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
