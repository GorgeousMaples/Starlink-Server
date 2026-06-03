import com.app.WebApplication;
import com.app.domain.Account;
import com.app.domain.AccountMessage;
import com.app.domain.User;
import com.app.domain.vo.AccountMessageVo;
import com.app.domain.vo.AccountVo;
import com.app.mapper.AccountMapper;
import com.app.mapper.AccountMessageMapper;
import com.app.mapper.MessageMapper;
import com.app.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.core.utils.WrapperUtils;
import com.common.mybatis.page.TableDataInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = WebApplication.class)
public class ServiceTest {
//    @Autowired
//    private TestService testService;

    @Autowired
    private AccountMessageMapper accountMessageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testUser() {
//        userMapper.update(Wrappers.<User>lambdaUpdate()
//                .eq(User::getId, 1899015382226374657L)
//                .set(User::isDeleted, false));
        userMapper.insert(User.builder().name("测试用户3").phone("12345").password("123").build());
    }

    @Test
    public void testMessage() {
        accountMapper.selectList(null).forEach(System.out::println);
    }

//    @Test
//    public void testInsertAccountMessage() {
//        AccountMessage accountMessage = AccountMessage.builder()
//                .accountId(1899015618407653377L)
//                .messageId(1899064584922910722L)
//                .build();
//        System.out.println(accountMessage);
//        testService.insertAccountMessage(accountMessage);
//    }

//    @Test
//    public void testSelectMessageListByAccountId() {
//        testService.selectMessageListByAccountId(1899015579329290241L).forEach(System.out::println);
//    }

    @Test
    public void testSelectAccountList() {
        QueryWrapper<AccountVo> qw = new QueryWrapper<>();
        qw.select("t_account.C_ID", "C_USER_ID", "C_NAME").eq("C_USER_ID", 1899015382226374657L);
//        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(User::getPhone, "123456");

        accountMapper.selectVoList(qw)
                .forEach(System.out::println);
    }

    @Test
    public void testSelectAccountVoList() {
        QueryWrapper<?> qw = new QueryWrapper<>()
                .eq(WrapperUtils.parse(User::getPhone), "123456")
                .eq(WrapperUtils.parse(Account::getType), 1);
        System.out.println(WrapperUtils.getWhereSentence(qw));
        accountMapper.selectVoList(qw)
                .forEach(System.out::println);
    }

    @Test
    public void testPage() {
        List<AccountMessageVo> list = accountMessageMapper.selectVoList(new Page<>(1, 2));
        System.out.println(TableDataInfo.build(list));
    }

//    @Test
//    public void testTransaction() {
//        testService.doTransaction();
//    }
}
