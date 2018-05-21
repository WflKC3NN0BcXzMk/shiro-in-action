package cn.mycloudx.shiroweb.config;

import cn.mycloudx.shiroweb.session.CustomSessionManager;
import cn.mycloudx.shiroweb.shrio.realm.CustomRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhanghao
 * @date 2018/05/19
 */
@Configuration
public class ShiroConfig {
    private CustomRealm customRealm;
    private SessionDAO sessionDAO;

    @Autowired
    public ShiroConfig(CustomRealm customRealm, SessionDAO sessionDAO) {
        this.customRealm = customRealm;
        this.sessionDAO = sessionDAO;
    }


    /**
     * ShiroFilterFactoryBean
     */


    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(WebSecurityManager webSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(webSecurityManager);
        //设置登录的url
        shiroFilterFactoryBean.setLoginUrl("login.html");
        //设置未授权的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("403.html");
        //设置filterChain
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>(2);
        filterChainDefinitionMap.put("/login.html", "anon");
        filterChainDefinitionMap.put("/subLogin", "anon");
        filterChainDefinitionMap.put("/*", "authc");
        filterChainDefinitionMap.put("/testRole", "roles['admin']");
        filterChainDefinitionMap.put("/testRole1", "roles['admin','admin1']");
        filterChainDefinitionMap.put("/testPerms", "perms['user:delete']");
        filterChainDefinitionMap.put("/testPerms1", "roles['user:update','user:delete']");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        //设置登录成功的url
        shiroFilterFactoryBean.setSuccessUrl("/index.html");
        return shiroFilterFactoryBean;
    }


    /**
     * Security WebManager
     */
    @Bean
    public WebSecurityManager webSecurityManager(HashedCredentialsMatcher hashedCredentialsMatcher) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        securityManager.setRealm(customRealm);
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }


    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(1);
        return matcher;
    }


    /**
     * AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }


    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager customSessionManager = new CustomSessionManager();
        customSessionManager.setSessionDAO(sessionDAO);
        return customSessionManager;
    }


}
