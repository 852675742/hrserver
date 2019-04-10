package org.sang;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class TestJdbc {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("加载驱动器完成");
		DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/fescar?user=root&password=123456&useUnicode=true");

		String key = "A1:" + ":" + System.currentTimeMillis() / 1000;
        System.out.println(new Date() + ":" + key);
		Thread.sleep(2000);
		key = "A1:" + ":" + System.currentTimeMillis() / 1000;
		System.out.println(new Date() + ":" + key);
	}

}
