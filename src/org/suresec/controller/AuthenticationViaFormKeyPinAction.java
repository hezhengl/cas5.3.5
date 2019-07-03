package org.suresec.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.RootCasException;
import org.apereo.cas.authentication.adaptive.AdaptiveAuthenticationPolicy;
import org.apereo.cas.authentication.principal.Service;
import org.apereo.cas.web.flow.actions.AbstractAuthenticationAction;
import org.apereo.cas.web.flow.resolver.CasDelegatingWebflowEventResolver;
import org.apereo.cas.web.flow.resolver.CasWebflowEventResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.RequestContext;
import org.suresec.authentication.UsernamePasswordKeyPINCredential;
import org.suresec.exception.BadAuthcodeAuthenticationException;
import org.suresec.exception.VerificationCodeException;



/**
 * Action to authenticate credential and retrieve a TicketGrantingTicket for
 * those credential. If there is a request for renew, then it also generates
 * the Service Ticket required.
 *
 * @author wlw
 * @since 3.0.0
 */

@Component("authenticationViaFormKeyPinAction")
public class AuthenticationViaFormKeyPinAction extends AbstractAuthenticationAction {
	
	public AuthenticationViaFormKeyPinAction(
			CasDelegatingWebflowEventResolver initialAuthenticationAttemptWebflowEventResolver,
			CasWebflowEventResolver serviceTicketRequestWebflowEventResolver,
			AdaptiveAuthenticationPolicy adaptiveAuthenticationPolicy) {
		super(initialAuthenticationAttemptWebflowEventResolver, serviceTicketRequestWebflowEventResolver,
				adaptiveAuthenticationPolicy);
		// TODO Auto-generated constructor stub
	}

	/**
	 * validator key pin (use test)
	 * @param context
	 * @param credential
	 * @param messageContext
	 * @return
	 */
	@Resource
	private HttpSession session;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public final String submitMode(final RequestContext context, final Credential credential, final Service service, final MessageContext messageContext){	
		System.out.println(".............submitMode");
		UsernamePasswordKeyPINCredential upc = (UsernamePasswordKeyPINCredential)credential;	
		if(("verifySign").equals(upc.getSubmitMode())) {
			
	    	String certDN = upc.getCertDN();
	    	String sql = "select * from user_cert where cert_dn = ?";
	    	List<Map<String, Object>> user_cert = jdbcTemplate.queryForList(sql,certDN);
	    	if(user_cert.size() != 1) {
	    		populateErrorsInstance(new BadAuthcodeAuthenticationException(),messageContext);
	    		return "error";
	    	}
	    	sql ="select account from user_info where user_id = ?";	    	
	    	String username = jdbcTemplate.queryForObject(sql,String.class,user_cert.get(0).get("user_id"));
	    	upc.setUsername(username);
    	}
		if(service != null){
    		String sql = "SELECT" + 
    				"    sf.serviceId" + 
    				" FROM" + 
    				"    user_info uf," + 
    				"    user_role ur," + 
    				"    role_info ri," + 
    				"    role_service rs," + 
    				"    regexregisteredservice sf" + 
    				" WHERE" + 
    				"    uf.USER_ID = ur.USER_ID" + 
    				"        AND ur.ROLE_ID = ri.ROLE_ID" + 
    				"        AND ri.ROLE_ID = rs.role_id" + 
    				"        AND rs.service_id = sf.id" + 
    				"        AND uf.ACCOUNT = ?;";
    		List<String> serviceList = jdbcTemplate.queryForList(sql, String.class,upc.getUsername());
    		int flag = 0;

    		for (String services : serviceList) {
				if(services.equals(service.getId())) {
					flag ++;
					break;
				}
			}
    		if(flag == 0) {
    			return "casBadAuthorityView";
    		}
    	}	
    	session.setAttribute("submitMode", upc.getSubmitMode());
    	return upc.getSubmitMode();
	 }
	
	public final String verificationCode(final RequestContext context, final Credential credential, final MessageContext messageContext){	
		System.out.println("...........verificationCode");
		UsernamePasswordKeyPINCredential upc = (UsernamePasswordKeyPINCredential)credential;
		
		if(upc.getVerificationCode().equalsIgnoreCase((String)session.getAttribute("verificationCode"))) {
    		return "success";
    	}
		populateErrorsInstance(new VerificationCodeException(),messageContext);
		return "error";
	}
	   
    /**
     * verify key sign
     * @param context
     * @param credential
     * @param messageContext
     * @return
     */
    public final String  verifySign(final RequestContext context, final Credential credential, final MessageContext messageContext){
    	System.out.println("................verifySign");
    	
//    	UsernamePasswordKeyPINCredential upc = (UsernamePasswordKeyPINCredential)credential;
//    	
//    	String signvalue = upc.getSignvalue();
//    	String certDN = upc.getCertDN();
//    	String sql = "select * from user_cert where cert_dn = ?";
//    	List<Map<String, Object>> user_cert = jdbcTemplate.queryForList(sql,certDN);
//    	if(user_cert.size() != 1) {
//    		populateErrorsInstance(new BadAuthcodeAuthenticationException(),messageContext);
//    		return "error";
//    	}
//    	String vcert = (String) user_cert.get(0).get("vcert");
//    	 if (StringUtils.isEmpty(signvalue)) {
//             populateErrorsInstance(new NullAuthcodeAuthenticationException(),messageContext);
//             return "error";
//         }
//    	if(upc.getCert_flag().equals("0") ){
//    		if(VerifySign.VerifySignData_RSA(upc.getsInData(), signvalue, vcert)) {
    			 return "success";
//    		}           
//        }else {
//        	if(VerifySign.VerifySignData_SM2(upc.getsInData(), signvalue, vcert)) {
//   			 	return "success";
//        	}        
//        }
//    	
//    	populateErrorsInstance(new BadAuthcodeAuthenticationException(),messageContext);
//    	return "error";
    }
    
    private void populateErrorsInstance(final RootCasException e,
            final MessageContext messageContext) {

        try {
            messageContext.addMessage(new MessageBuilder().error().code(e.getCode()).build());
        } catch (final Exception fe) {
            logger.error(fe.getMessage(), fe);
        }
    }
}
