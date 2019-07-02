package org.suresec.webflow.configurer;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.web.flow.CasWebflowConstants;
import org.apereo.cas.web.flow.configurer.AbstractCasWebflowConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.engine.builder.BinderConfiguration;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.suresec.authentication.UsernamePasswordCaptchaCredential;
/**
 * 
 * @author 19820--登陆页面绑定新加参数
 *
 */
public class CaptchaWebflowConfigurer extends AbstractCasWebflowConfigurer {
	public CaptchaWebflowConfigurer(FlowBuilderServices flowBuilderServices,
			FlowDefinitionRegistry loginFlowDefinitionRegistry, ApplicationContext applicationContext,
			CasConfigurationProperties casProperties) {
		super(flowBuilderServices, loginFlowDefinitionRegistry, applicationContext, casProperties);
		// TODO Auto-generated constructor stub
	}

	protected void bindCredential(Flow flow) {

		// 重写绑定自定义credential
		createFlowVariable(flow, CasWebflowConstants.VAR_ID_CREDENTIAL, UsernamePasswordCaptchaCredential.class);
		// 登录页绑定新参数
		final ViewState state = (ViewState) flow.getState(CasWebflowConstants.STATE_ID_VIEW_LOGIN_FORM);
		final BinderConfiguration cfg = getViewStateBinderConfiguration(state);
		// 由于用户名以及密码已经绑定，所以只需对新加验证码参数绑定即可
		cfg.addBinding(new BinderConfiguration.Binding("captcha", null, false));
	}

	@Override
	protected void doInitialize() {
		// TODO Auto-generated method stub
		System.out.println("CaptchaWebflowConfigurer...........doInitialize");
		final Flow flow = getLoginFlow();
		bindCredential(flow);
	}
}
