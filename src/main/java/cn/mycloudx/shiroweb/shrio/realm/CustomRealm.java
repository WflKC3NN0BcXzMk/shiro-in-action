package cn.mycloudx.shiroweb.shrio.realm;

import cn.mycloudx.shiroweb.domain.User;
import cn.mycloudx.shiroweb.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author zhanghao
 * @date 2018/05/19
 */
@Component
@Slf4j
public class CustomRealm extends AuthorizingRealm {

    private final UserRepository repository;

    @Autowired
    public CustomRealm(UserRepository repository) {
        setName("customRealm");
        this.repository = repository;
    }


    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        authorizationInfo.addRole("admin");
        authorizationInfo.addStringPermission("user:delete");
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //从主体传过来的认证信息中获取用户名
        String username = (String) token.getPrincipal();

        //通过用户名到数据库获取凭证
        User user = repository.findByUser(username);
        if (StringUtils.isEmpty(user)){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, user.getPassword(), getName());
        ///设置加盐
//        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(user.getUser()));
        return authenticationInfo;
    }
}
