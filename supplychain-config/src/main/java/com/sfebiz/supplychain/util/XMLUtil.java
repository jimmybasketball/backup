package com.sfebiz.supplychain.util;


import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLUtil {
    /**
     * JavaBean转换成xml
     * 默认编码UTF-8
     *
     * @param obj
     * @return
     */
    public static String convertToXml(Object obj) throws Exception {
        return convertToXml(obj, "UTF-8");
    }

    /**
     * JavaBean转换成xml
     *
     * @param obj
     * @param encoding
     * @return
     */
    public static String convertToXml(Object obj, String encoding) throws Exception {
        String result = null;
        if (obj == null) {
            throw new Exception("转换数据对象参数不能为空");
        }
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        result = writer.toString();
        return result;
    }

    /**
     * xml转换成JavaBean
     *
     * @param xml
     * @param c
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T converyToJavaBean(String xml, Class<T> c) throws Exception {
        T t = null;
        if (StringUtils.isBlank(xml)) {
            return t;
        }
        xml = xml.trim();
        JAXBContext context = JAXBContext.newInstance(c);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        t = (T) unmarshaller.unmarshal(new StringReader(xml));
        return t;
    }

    /**
     * JavaBean转换成xml
     * 默认编码UTF-8
     *
     * @param obj
     * @return
     */
    public static String convertToXmlByDM(Object obj) throws Exception {
        return convertToXmlByDM(obj, "UTF-8");
    }

    /**
     * JavaBean转换成xml
     *
     * @param obj
     * @param encoding
     * @return
     */
    public static String convertToXmlByDM(Object obj, String encoding) throws Exception {
        String result = null;
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        result = writer.toString();
        return result;
    }
    
    /**
     * 转义XML中的特殊字符
     * 
     * @param str 转义前字符
     * @return 转义后字符
     */
    public static String escapeSpecialCharacterForXML(String str) {
        if (str == null) {
            return "";
        }
        str = replaceString(str, "&", "&amp;");
        str = replaceString(str, "<", "&lt;");
        str = replaceString(str, ">", "&gt;");
        str = replaceString(str, "&apos;", "&apos;");
        str = replaceString(str, "\"", "&quot;");
        return str;
    }

    /** 
     * 替换一个字符串中的某些指定字符 
     * @param str String 原始字符串 
     * @param regex String 要替换的字符串 
     * @param replacement String 替代字符串 
     * @return String 替换后的字符串 
     */
    private static String replaceString(String str, String regex, String replacement) {
        if (str == null) {
            return null;
        }
        try {
            int index;
            index = str.indexOf(regex);
            String strNew = "";
            if (index >= 0) {
                while (index >= 0) {
                    strNew += str.substring(0, index) + replacement;
                    str = str.substring(index + regex.length());
                    index = str.indexOf(regex);
                }
                strNew += str;
                return strNew;
            }
        } catch (Exception e) {
            return str;
        }
        return str;
    }

    public static void main(String[] args) {
        
        // 测试特殊字符转义
        String str = "1:& 2: < 3: > 4: ' 5: \"";
        System.out.println(escapeSpecialCharacterForXML(str));
    }
}
