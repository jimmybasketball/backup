package com.sfebiz.supplychain.config;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * jedis集群客户端
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-08-04 15:07
 **/
public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {

    private Logger logger = LoggerFactory.getLogger(JedisClusterFactory.class);

    private String addresses = "";

    private JedisCluster jedisCluster;

    private Integer timeout;

    private Integer maxRedirections;

    private GenericObjectPoolConfig genericObjectPoolConfig;

    private Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");

    @Override
    public JedisCluster getObject() throws Exception {
        return jedisCluster;
    }

    @Override
    public Class<? extends JedisCluster> getObjectType() {
        return (this.jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    private Set<HostAndPort> parseHostAndPort() throws Exception {
        try {

            Set<HostAndPort> haps = new HashSet<HostAndPort>();
            if (StringUtils.isBlank(addresses)) {
                throw new Exception("redisCluster addresses不能为空");
            } else {
                for (String address : addresses.split(",")) {

                    boolean isIpPort = p.matcher(address).matches();

                    if (!isIpPort) {
                        throw new IllegalArgumentException("ip 或 port 不合法");
                    }
                    String[] ipAndPort = address.split(":");

                    HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                    haps.add(hap);
                }

            }

            return haps;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new Exception("解析 jedis 配置文件失败", ex);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<HostAndPort> haps = this.parseHostAndPort();
        jedisCluster = new JedisCluster(haps, timeout, maxRedirections, genericObjectPoolConfig);
        logger.info("jedisCluster init success!");
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxRedirections() {
        return maxRedirections;
    }

    public void setMaxRedirections(Integer maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public GenericObjectPoolConfig getGenericObjectPoolConfig() {
        return genericObjectPoolConfig;
    }
}
