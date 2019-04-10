package org.sang;

import com.miracle.autoconfigure.HelloService;
import org.mybatis.spring.annotation.MapperScan;
import org.sang.bean.Miracle;
import org.sang.common.annotation.EnableRedis;
import org.sang.config.ZkComsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@MapperScan("org.sang.mapper")
@EnableCaching// 开启缓存，需要显示的指定
@EnableRedis(enable = true)
@ImportResource(value={"miracle.xml"})
public class HrserverApplication {

	public static void main(String[] args) throws Exception {
		ApplicationContext applicationContext = SpringApplication.run(HrserverApplication.class, args);

		//以下代码为测试
		ZkComsumer zkComsumer = applicationContext.getBean(ZkComsumer.class);
		String serverInfo = zkComsumer.getServerinfo("hrv");//获取服务数据
		System.out.println("本次获取到服务:" + serverInfo);

		HelloService helloService = applicationContext.getBean(HelloService.class);
		System.out.println(helloService.getHashidsProperties().getSalt());

		Miracle miracle = (Miracle)applicationContext.getBean("miracle");
		System.out.println(miracle.getName());
	}
}
