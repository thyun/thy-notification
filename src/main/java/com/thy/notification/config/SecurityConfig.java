package com.thy.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;

/**
 * Security Configuration - LDAP and HTTP Authorizations.
 */
@Configuration
// @ImportResource({ "classpath:webSecurityConfig.xml" }) //=> uncomment to use equivalent xml config
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*
        auth.ldapAuthentication()
                .userDnPatterns("sAMAccountName=1001291,OU=Person")
                .userSearchBase("OU=Person,DC=SKP,DC=AD")
//                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://10.40.29.172:389/DC=SKP,DC=AD");
                .and()
                .passwordCompare()
                .passwordEncoder(new LdapShaPasswordEncoder())
                .passwordAttribute("userPassword");
*/

        auth.ldapAuthentication()
                .userSearchBase("OU=Person,DC=SKP,DC=AD")
                .userSearchFilter("cn={0}")
//                .groupSearchBase("ou=groups")
//                .groupSearchFilter("(member={0})")
                .contextSource()
//                .root("DC=SKP,DC=AD")
                .url("ldap://10.40.29.172:389")
                .managerDn("xxxx")
                .managerPassword("xxxx");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().anyRequest().permitAll();
        /*
        http.authorizeRequests().antMatchers("/", "/home", "/css/*", "/js/*").permitAll()
                .anyRequest()
//                .permitAll();
                .authenticated(); */
        http.formLogin().loginPage("/login").permitAll().and().logout().logoutSuccessUrl("/");
    }

}
