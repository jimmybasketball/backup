package com.sfebiz.supplychain.service.stockout.pdfformat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/1/7
 * Time: 下午3:14
 */
@Component("pdfBillFactory")
public class PDFBillFactory implements ApplicationContextAware {

    private static volatile Map<String, PDFFormat> pdfFormatMap = new HashMap<String, PDFFormat>();

    /**
     * 根据PDF类型获取具体生成相应格式PDF的实现类
     *
     * @param pdfFormat
     * @return
     */
    public PDFFormat getPDFFormat(String pdfFormat) {
        if (pdfFormatMap.containsKey(pdfFormat)) {
            return pdfFormatMap.get(pdfFormat);
        } else {
            throw new RuntimeException("未知的PDF类型:" + pdfFormatMap);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, PDFFormat> map = applicationContext.getBeansOfType(PDFFormat.class);
        Iterator<Map.Entry<String, PDFFormat>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PDFFormat> entry = iterator.next();
            if (StringUtils.isEmpty(entry.getValue().getFormat())) {
                throw new RuntimeException("pdfFormat is empty: " + entry.getValue());
            }
            if (pdfFormatMap.get(entry.getValue().getFormat()) != null) {
                throw new RuntimeException("pdfFormat " + entry.getValue().getFormat() + " already used by : " + pdfFormatMap.get(entry.getValue().getFormat()) + ",change " + entry.getValue() + "'s payType");
            }
            pdfFormatMap.put(entry.getValue().getFormat(), entry.getValue());
        }
    }
}
