package com.campusamour.inoteblog.web.admin;

import com.campusamour.inoteblog.handle.NotFoundVerifyCodeException;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class VerifyController {
    @Autowired
    private Producer captchaProducer;

    @GetMapping("/kaptcha/code")
    public void getImage(HttpServletResponse response, HttpSession session) {
        // 生成验证码
        String text = captchaProducer.createText();
        BufferedImage image = captchaProducer.createImage(text);

        // 将验证码存入session
        session.setAttribute("kaptcha", text);

        // 将突图片输出给浏览器
        response.setContentType("image/jpg");
        ServletOutputStream os = null;
        try {
            os = response.getOutputStream();
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            throw new NotFoundVerifyCodeException("抱歉，任性的验证码跑丢啦，请联系管理员，邮箱:campusamour@gmail.com~");
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
