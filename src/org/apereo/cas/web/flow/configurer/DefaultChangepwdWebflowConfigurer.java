package org.apereo.cas.web.flow.configurer;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

import lombok.extern.slf4j.Slf4j;

/**
 * This is {@link DefaultChangepwdWebflowConfigurer}.
 *
 * @author add by wcc 20190717
 * @since 5.2.0
 */
@Slf4j
public class DefaultChangepwdWebflowConfigurer extends AbstractCasWebflowConfigurer {
    public DefaultChangepwdWebflowConfigurer(final FlowBuilderServices flowBuilderServices, final FlowDefinitionRegistry flowDefinitionRegistry,
                                   final ApplicationContext applicationContext, final CasConfigurationProperties casProperties) {
        super(flowBuilderServices, flowDefinitionRegistry, applicationContext, casProperties);
    }

    @Override
    public void doInitialize() {
		
    }
}
