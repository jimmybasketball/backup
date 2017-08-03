package com.sfebiz.supplychain.msg.processer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sam on 3/25/15.
 * Email: sambean@126.com
 */

@Component
public class MessageProcesserFactory implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(MessageProcesserFactory.class);

    private static Map<String, MessageProcesser> processerMap = new HashMap<String, MessageProcesser>();

    public MessageProcesser getMessageProcesser(String tag) {
        return processerMap.get(tag);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, MessageProcesser> map = applicationContext.getBeansOfType(MessageProcesser.class);
        Iterator<Map.Entry<String, MessageProcesser>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, MessageProcesser> entry = iterator.next();
            if (StringUtils.isEmpty(entry.getValue().getTag())) {
                throw new RuntimeException("tag is empty: " + entry.getValue());
            }
            if (processerMap.get(entry.getValue().getTag()) != null) {
                throw new RuntimeException("tag " + entry.getValue().getTag() + " already used by : " + processerMap.get(entry.getValue().getTag()) + ",change " + entry.getValue() + "'s tag");
            }
            processerMap.put(entry.getValue().getTag(), entry.getValue());
        }
        System.out.println(map);
    }
}
