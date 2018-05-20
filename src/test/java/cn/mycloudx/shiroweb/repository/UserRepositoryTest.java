package cn.mycloudx.shiroweb.repository;

import cn.mycloudx.shiroweb.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

/**
 * @author zhanghao
 * @date 2018/05/20
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUser() {
        User mark = userRepository.findByUser("Mark");
        assertNotNull(mark);

    }


    @Test
    public void save() {
        User user = userRepository.findByUser("zhanghao");
        Md5Hash md5Hash = new Md5Hash("123456");
//        md5Hash.setSalt(ByteSource.Util.bytes(user.getUser()));
        user.setPassword(md5Hash.toString());
        User save = userRepository.save(user);
        log.info("user={}",save);
    }
}