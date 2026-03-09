package com.example.timesheets2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;

@Configuration
public class ContextSource {

    @Bean
    public EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean() {
        return EmbeddedLdapServerContextSourceFactoryBean.fromEmbeddedLdapServer();
    }

}
