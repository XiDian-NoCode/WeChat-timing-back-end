package org.nocode.timing;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// 用来配置Spring和Junit整合，junit启动时加载IOC容器
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/applicationContext-mapper.xml", "classpath:spring/applicationContext-service.xml"})
public class BaseTest {
}
