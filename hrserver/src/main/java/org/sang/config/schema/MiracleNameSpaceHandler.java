package org.sang.config.schema;


import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by Rick on 2019/4/8.
 *
 * @ Description：${description}
 */
public class MiracleNameSpaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("user",new MiracleBeanDefinitionParser());
    }

}
