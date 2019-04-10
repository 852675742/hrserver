package org.sang.config.schema;

import org.sang.bean.Miracle;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * Created by Rick on 2019/4/8.
 *
 * @ Description：
 */
public class MiracleBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    protected Class<?> getBeanClass(Element element) {
        return Miracle.class;
    }

    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String name = element.getAttribute("name");
        String email = element.getAttribute("email");

        builder.addPropertyValue("name",name);
        builder.addPropertyValue("email",email);
    }



}
