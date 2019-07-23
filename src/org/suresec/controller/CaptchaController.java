package org.suresec.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.authentication.principal.Principal;
import org.apereo.cas.support.oauth.OAuth20Constants;
import org.apereo.cas.util.Pac4jUtils;
import org.pac4j.core.context.J2EContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.suresec.util.CaptchaUtil;

/**
 * 
 * @author wcc
 * @time 2019-07-04 05:32
 * @description 生成验证码
 */
@Controller
public class CaptchaController {

    public static final String KEY_CAPTCHA = "verificationCode";
    /**
     * @description 获取验证码
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/getCaptcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        // 设置相应类型,告诉浏览器输出的内容为图片
        response.setContentType("image/jpeg");
        // 不缓存此内容
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        try {
            HttpSession session = request.getSession();
            CaptchaUtil tool = new CaptchaUtil();
            StringBuffer code = new StringBuffer();
            BufferedImage image = tool.genRandomCodeImage(code);
            session.removeAttribute(KEY_CAPTCHA);
            session.setAttribute(KEY_CAPTCHA, code.toString());
            // 将内存中的图片通过流动形式输出到客户端
            ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @description 跳转首页
     * @param account
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/toApplist")
    public String toApplist(String account,HttpServletRequest request, HttpServletResponse response) {
    	Principal principal = new DefaultPrincipalFactory().createPrincipal(account, new HashMap<String,Object>());
    	request.setAttribute("principal", principal);
    	return "casGenericSuccessView";
    	//return "/login";
    }
    @RequestMapping("/oauth2RedirectToClient")
    @ResponseBody
    public String oauth2RedirectToClient(String callbackUrl,String clientId,HttpServletRequest request, HttpServletResponse response) {
    	final J2EContext ctx = Pac4jUtils.getPac4jJ2EContext(request, response);
    	ctx.getSessionStore().set(ctx, OAuth20Constants.BYPASS_APPROVAL_PROMPT+clientId, Boolean.TRUE);//modify by wcc 20190723  +clientId确保每个客户端都能出现授权页面；
    	return callbackUrl;
    }
   
}
