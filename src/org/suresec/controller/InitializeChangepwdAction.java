package org.suresec.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.authentication.principal.Principal;
import org.apereo.cas.services.ServicesManager;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * This is {@link InitializeChangepwdAction}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@Slf4j
@RequiredArgsConstructor
@Component("initializeChangepwdAction")
public class InitializeChangepwdAction extends AbstractAction {
    /**
     * The services manager with access to the registry.
     **/
    protected final ServicesManager servicesManager;

    @Override
    protected Event doExecute(final RequestContext requestContext) throws Exception {
        LOGGER.info("Initialized InitializeChangepwdAction sequence");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String account = (String) request.getParameter("account");
		Principal principal = new DefaultPrincipalFactory().createPrincipal(account, new HashMap<String,Object>());
		request.setAttribute("principal", principal);
        return success();
    }
}
