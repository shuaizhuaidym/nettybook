package com.xml.mashlling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.jibx.runtime.JiBXException;
import org.junit.Before;
import org.junit.Test;

import com.xml.bean.Address;
import com.xml.bean.Customer;
import com.xml.bean.Item;
import com.xml.bean.Order;
import com.xml.bean.Shipping;

public class TestMarshlling {

	Order order = null;

	Marshller marsh = new Marshller();

	@Before
	public void initialize() throws Exception {
		Customer cust = new Customer(1L, "San", "Zhang", Arrays.asList("Feng"));
		Address addr = new Address("Zhichun Road", "Zhonggc Road", "bj", "bj", "China", "100086");
		Item item = new Item("i001", "Goods", 12F, 2);
		Date date = Calendar.getInstance().getTime();
		Float total = 1280.0F;
		order = new Order(10001L, cust, addr, Shipping.INTERNATIONAL_EXPRESS, addr, Arrays.asList(item), date,
				date, total);
	}

	@Test
	public void testMarshlling() throws JiBXException, IOException {
		marsh.encode2File(order, "./src/main/java/com/xml/bean/marshlled/order.xml");
	}

	@Test
	public void testUnmarshalling() throws IOException, JiBXException {
		String xml = readFromFile("./src/main/java/com/xml/bean/marshlled/order.xml");
		Order o = marsh.decode2Order(xml);
		System.out.println(o.getOrderNumber());
	}

	String readFromFile(String file) throws IOException {
		StringBuilder buf = new StringBuilder();
		BufferedReader rd = new BufferedReader(new FileReader(file));
		String ln;
		while ((ln = rd.readLine()) != null) {
			buf.append(ln);
		}
		rd.close();
		return buf.toString();
	}

}
