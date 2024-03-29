package org.suresec.handler;

import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.FailedLoginException;

import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.exceptions.AccountDisabledException;
import org.apereo.cas.authentication.exceptions.InvalidLoginLocationException;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.suresec.authentication.UsernamePasswordKeyPINCredential;
/**
 * 
 * @author wcc
 * @time 2019-07-04 05:33
 * @description 自定义登录验证
 */
public class UsernamePasswordCaptchaAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	public UsernamePasswordCaptchaAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory,
            Integer order) {
        super(name, servicesManager, principalFactory, order);
    }
   
    
    /**
     * 用于判断用户的Credential(换而言之，就是登录信息)，是否是俺能处理的
     * 就是有可能是，子站点的登录信息中不止有用户名密码等信息，还有部门信息的情况
     */
    @Override
    public boolean supports(Credential credential) {
        //判断传递过来的Credential 是否是自己能处理的类型
        return credential instanceof UsernamePasswordKeyPINCredential;
    }
    /**
     * 校验
     */
	@Override
	protected AuthenticationHandlerExecutionResult doAuthentication(Credential credential)
			throws GeneralSecurityException, PreventedException {
		UsernamePasswordKeyPINCredential myCredential = (UsernamePasswordKeyPINCredential) credential;
		//获取传递过来的用户名和密码
        String username = myCredential.getUsername();
        String password = myCredential.getPassword();
        String requestCaptcha = myCredential.getVerificationCode();
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Object attribute = attributes.getRequest().getSession().getAttribute("verificationCode");

        String realCaptcha = attribute == null ? null : attribute.toString();

        if(StringUtils.isBlank(requestCaptcha) || !requestCaptcha.toUpperCase().equals(realCaptcha)){
            throw new FailedLoginException("验证码错误");
        }
		
		Connection conn = null;
        try {
            //查询语句
            String sql = "SELECT * FROM user_info WHERE user_id =?  AND password= ?";
            Map<String,Object> result = jdbcTemplate.queryForMap(sql, new Object[]{username,password});
            if(!result.isEmpty()) {
                System.out.println("result----------"+result);
                //允许登录，并且通过this.principalFactory.createPrincipal来返回用户属性
                return createHandlerResult(credential, this.principalFactory.createPrincipal(username, result), new ArrayList<>(0));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
      //当是admin用户的情况，直接就登录了，谁叫他是admin用户呢
        if(username.startsWith("admin")) {
            //直接返回去了
            return createHandlerResult(credential, this.principalFactory.createPrincipal(username, Collections.emptyMap()), null);
        }else if (username.startsWith("cas")) {
            //自定义异常测试
            //throw new CustomException("自定义异常测试");
        }else if (username.startsWith("lock")) {
            //用户锁定
            throw new AccountLockedException();
        } else if (username.startsWith("disable")) {
            //用户禁用
            throw new AccountDisabledException();
        } else if (username.startsWith("invali")) {
            //禁止登录该工作站登录
            throw new InvalidLoginLocationException();
        } else if (username.startsWith("passorwd")) {
            //密码错误
            throw new FailedLoginException();
        } else if (username.startsWith("account")) {
            //账号错误
            throw new AccountLockedException();
        }
        return null;
	}

	
}
