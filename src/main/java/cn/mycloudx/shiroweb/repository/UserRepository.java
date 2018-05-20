package cn.mycloudx.shiroweb.repository;

import cn.mycloudx.shiroweb.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanghao
 * @date 2018/05/20
 */
public interface UserRepository  extends JpaRepository<User,Integer> {
    User findByUser(String userName);
}
