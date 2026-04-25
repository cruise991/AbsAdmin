package com.abs.system.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class XMLUtils {

	public static Element rootElement = null;

	public static Element getRootElement() {
		try {
			if (rootElement == null) {
				Resource cr = new ClassPathResource("config.xml");
				SAXReader saxReader = new SAXReader();
				Document read = saxReader.read(cr.getInputStream());
				rootElement = read.getRootElement();
			}
			return rootElement;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), "加载config.xml文件失败");
		}
	}

	public static Element getElementByQName(QName qname) {
		return getRootElement().element(qname);
	}

	public static Map<String, String> ElementToMap(Element element) {
		List<Element> list = element.elements();
		if (list != null && list.size() > 0) {
			Map<String, String> map = new HashMap<String, String>();
			for (Element t : list) {
				map.put(t.attribute("name").getStringValue(), t.getTextTrim());
			}
			return map;
		} else {
			return null;
		}
	}

	/**
	 * @param attr 对应的name属性
	 * @return
	 */

	public static String getValueByName(String attr) {
		String text = null;
		Element rootElement = getRootElement();
		Iterator<Element> itertor = rootElement.elementIterator();
		while (itertor.hasNext()) {
			Element element = itertor.next();
			if (attr.equals(element.attribute("name").getValue())) {
				text = element.getText();
				break;
			}
		}
		return text;
	}

	public static Element getElementByName(String name) {
		QName qname = new QName(name);
		Element e = getRootElement().element(qname);
		return e;
	}

	public static Element getElementById(String ids) {
		Element e = getRootElement().elementByID(ids);
		return e;
	}

}
