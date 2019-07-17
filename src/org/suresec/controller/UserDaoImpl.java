/**
 * chang pwd 2017.8.24
 */
package org.suresec.controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apereo.cas.authentication.RootCasException;
import org.apereo.cas.authentication.adaptive.AdaptiveAuthenticationPolicy;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.authentication.principal.Principal;
import org.apereo.cas.util.crypto.DefaultPasswordEncoder;
import org.apereo.cas.web.flow.actions.AbstractAuthenticationAction;
import org.apereo.cas.web.flow.resolver.CasDelegatingWebflowEventResolver;
import org.apereo.cas.web.flow.resolver.CasWebflowEventResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.suresec.authentication.UserInfo;
import org.suresec.exception.AgainPasswordErrorException;
import org.suresec.exception.OldPasswordErrorException;
import org.suresec.webflow.configurer.ChangPwdSuccess;

/**
 * @author sure wlw
 *
 */
@Component("userDao")
public class UserDaoImpl extends AbstractAuthenticationAction{

	public UserDaoImpl(CasDelegatingWebflowEventResolver initialAuthenticationAttemptWebflowEventResolver,
			CasWebflowEventResolver serviceTicketRequestWebflowEventResolver,
			AdaptiveAuthenticationPolicy adaptiveAuthenticationPolicy) {
		super(initialAuthenticationAttemptWebflowEventResolver, serviceTicketRequestWebflowEventResolver,
				adaptiveAuthenticationPolicy);
		// TODO Auto-generated constructor stub
	}


	@Autowired
	private HttpSession session; 
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public String changPwd( final UserInfo user, final MessageContext messageContext) {

		System.out.println("........................changPwd");		
		String account = user.getAccount();
		String oldpwd = user.getOldpwd();	
		DefaultPasswordEncoder enc = new DefaultPasswordEncoder("MD5","");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		Principal principal = new DefaultPrincipalFactory().createPrincipal(account, new HashMap<String,Object>());
		request.setAttribute("principal", principal);
		try{
			if(!user.getNewpwd().equals(user.getAgain())) {
				populateErrorsInstance(new AgainPasswordErrorException(),messageContext);
				return "error";
			}
			String dbpwd = jdbcTemplate.queryForObject("select password from user_info where account='"+account+"'", java.lang.String.class);
			if(dbpwd.equals(enc.encode(oldpwd)))
			{
				final String newpwd = enc.encode(user.getNewpwd());
				jdbcTemplate.update( 
		                "update user_info set password=? where account = ?",   
		                new PreparedStatementSetter(){  
		                    @Override  
		                    public void setValues(PreparedStatement ps) throws SQLException {  
		                        ps.setString(1, newpwd);  
		                        ps.setString(2, user.getAccount());   
		                    }
		                }
		        );
			}
			else
			{
				populateErrorsInstance(new OldPasswordErrorException(),
	                     messageContext);
				
				return "error";
			}
 
		}catch(Exception e)
		{
			e.printStackTrace();
			return "error";
		}
		
		populateErrorsInstance(new ChangPwdSuccess(),messageContext);
		session.setAttribute("principal", null);//old
		request.setAttribute("principal", null);//add by wcc 20190717
		/*session.invalidate();*/
		return "success";
	}
	
	
    private void populateErrorsInstance(final RootCasException e, final MessageContext messageContext) {

        try {
            messageContext.addMessage(new MessageBuilder().error().code(e.getCode()).build());
        } catch (final Exception fe) {
            //logger.error(fe.getMessage(), fe);
        	fe.printStackTrace();
        }
    }
	

}
