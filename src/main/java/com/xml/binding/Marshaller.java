package com.xml.binding;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;

public class Marshaller {
	public void bean2XML() {
		try {
			StringWriter writer = new StringWriter();
			// marshal 编组
			IMarshallingContext mctx = factory.createMarshallingContext();
			mctx.setIndent(2);
			mctx.marshalDocument(bean, "UTF-8", null, writer);
			fail(writer);

			reader = new StringReader(writer.toString());
			// unmarshal 解组
			IUnmarshallingContext uctx = factory.createUnmarshallingContext();
			Account acc = (Account) uctx.unmarshalDocument(reader, null);
			fail(acc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void listBean2XML() {
		try {
			ListBean listBean = new ListBean();
			List<Account> list = new ArrayList<Account>();
			list.add(bean);
			bean = new Account();
			bean.setAddress("china");
			bean.setEmail("tom@125.com");
			bean.setId(2);
			bean.setName("tom");
			Birthday day = new Birthday("2010-11-22");
			bean.setBirthday(day);

			list.add(bean);
			listBean.setList(list);

			writer = new StringWriter();
			factory = BindingDirectory.getFactory(ListBean.class);
			// marshal 编组
			IMarshallingContext mctx = factory.createMarshallingContext();
			mctx.setIndent(2);
			mctx.marshalDocument(listBean, "UTF-8", null, writer);
			fail(writer);

			reader = new StringReader(writer.toString());
			// unmarshal 解组
			IUnmarshallingContext uctx = factory.createUnmarshallingContext();
			listBean = (ListBean) uctx.unmarshalDocument(reader, null);

			fail(listBean.getList().get(0));
			fail(listBean.getList().get(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
