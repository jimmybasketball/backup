package com.sfebiz.supplychain.provider.command.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class CommandConfig implements ApplicationContextAware {

    private String apiUrl;
    private String fileUrl;
    private String ftpUsername;
    private String ftpPassword;
    private String ftpHost;
    private int ftpPort;
    private String ftpPath;
    private String orderQName;
    private String printPath;

    private Map<String, Map<String, Map<String, String>>> lpcMapping;

    /**
     * 与口岸交互命令配置
     */
    private Map<String, Map<String, Map<String, String>>> portMapping;

    /**
     * 与支付企业交互命令配置
     */
    private Map<String, Map<String, Map<String, String>>> payMapping;

    private static ApplicationContext applicationContext;

    public static CommandConfig getInstance() {
        return (CommandConfig) getBean("commandConfig");
    }

    public static Object getSpringBean(String beanName) {
        return CommandConfig.getBean(beanName);
    }

    private static Object getBean(String bean) {
        return applicationContext.getBean(bean);
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public Map<String, Map<String, Map<String, String>>> getLpcMapping() {
        return lpcMapping;
    }

    public void setLpcMapping(
            Map<String, Map<String, Map<String, String>>> lpcMapping) {
        this.lpcMapping = lpcMapping;
    }

    public Map<String, Map<String, Map<String, String>>> getPortMapping() {
        return portMapping;
    }

    public void setPortMapping(Map<String, Map<String, Map<String, String>>> portMapping) {
        this.portMapping = portMapping;
    }

    public Map<String, Map<String, Map<String, String>>> getPayMapping() {
        return payMapping;
    }

    public void setPayMapping(Map<String, Map<String, Map<String, String>>> payMapping) {
        this.payMapping = payMapping;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl(String name) {
        if (name == null) {
            return "";
        }
        if (name.startsWith("http")) {
            return name;
        }
        return fileUrl + "?n=" + name;
    }

    public String getFtpUsername() {
        return ftpUsername;
    }

    public void setFtpUsername(String ftpUsername) {
        this.ftpUsername = ftpUsername;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getFtpHost() {
        return ftpHost;
    }

    public void setFtpHost(String ftpHost) {
        this.ftpHost = ftpHost;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public String getOrderQName() {
        return orderQName;
    }

    public void setOrderQName(String orderQName) {
        this.orderQName = orderQName;
    }

    public String getPrintPath() {
        return printPath;
    }

    public void setPrintPath(String printPath) {
        this.printPath = printPath;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
