package com.sfebiz.supplychain.protocol.pay.tenpay.client;

import com.sfebiz.supplychain.protocol.pay.tenpay.util.XMLUtil;
import org.jdom.JDOMException;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * XMLParseResponseHandler
 */
public class XMLParseResponseHandler extends ClientResponseHandler {

	public void doParse() throws JDOMException, IOException {
		String xmlContent = this.getContent();
		Map m = XMLUtil.doXMLParse(xmlContent);
		Iterator it = m.keySet().iterator();
		while(it.hasNext()) {
			String k = (String) it.next();
			String v = (String) m.get(k);
			this.setParameter(k, v);
		}
		
	}
}
