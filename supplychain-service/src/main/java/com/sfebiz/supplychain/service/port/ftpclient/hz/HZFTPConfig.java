package com.sfebiz.supplychain.service.port.ftpclient.hz;

import com.sfebiz.supplychain.config.port.PortConfig;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;

import java.util.Map;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/31
 * Time: 下午2:37
 */
public class HZFTPConfig {

    private static int port = 21;
    private static int clientTimeout = 10000;
    private static int dataTimeout = 30000;
    private static int defaultTimeout = 30000;


    /**
     * FTP Ser Host
     *
     * @return
     */
    public static String getRemoteHostIp() {
        Map<String, String> properties = PortConfig.getPortPropertiesByPortNid(PortNid.HANGZHOU.getNid());
        String remoteHostIp = properties.get("remote_host_ip");
        if (StringUtils.isBlank(remoteHostIp)) {
            throw new IllegalArgumentException(String.format("FTP交互参数%s不能为空", "remote_host_ip"));
        }
        return remoteHostIp;
    }

    public static int getPort() {
        return port;
    }

    /**
     * FTP登陆账号
     *
     * @return
     */
    public static String getAccount() {
        Map<String, String> properties = PortConfig.getPortPropertiesByPortNid(PortNid.HANGZHOU.getNid());
        String account = properties.get("remote_host_account");
        if (StringUtils.isBlank(account)) {
            throw new IllegalArgumentException(String.format("FTP交互参数%s不能为空", "remote_host_account"));
        }
        return account;
    }

    public static String getPassword() {
        Map<String, String> properties = PortConfig.getPortPropertiesByPortNid(PortNid.HANGZHOU.getNid());
        String password = properties.get("remote_host_password");
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException(String.format("FTP交互参数%s不能为空", "remote_host_password"));
        }
        return password;
    }

    /**
     * 发送文件夹名称
     *
     * @return
     */
    public static String getSendFilePath() {
        Map<String, String> properties = PortConfig.getPortPropertiesByPortNid(PortNid.HANGZHOU.getNid());
        String sendFilePath = properties.get("send_file_path");
        if (StringUtils.isBlank(sendFilePath)) {
            throw new IllegalArgumentException(String.format("FTP交互参数%s不能为空", "tmp_file_path"));
        }
        return sendFilePath;
    }

    /**
     * 回执信息文件夹
     *
     * @return
     */
    public static String getReadFilePath() {
        Map<String, String> properties = PortConfig.getPortPropertiesByPortNid(PortNid.HANGZHOU.getNid());
        String readFilePath = properties.get("read_file_path");
        if (StringUtils.isBlank(readFilePath)) {
            throw new IllegalArgumentException(String.format("FTP交互参数%s不能为空", "read_file_path"));
        }
        return readFilePath;
    }

    /**
     * 临时信息文件夹
     *
     * @return
     */
    public static String getTmpFilePath() {
        Map<String, String> properties = PortConfig.getPortPropertiesByPortNid(PortNid.HANGZHOU.getNid());
        String tmpFilePath = properties.get("tmp_file_path");
        if (StringUtils.isBlank(tmpFilePath)) {
            throw new IllegalArgumentException(String.format("FTP交互参数%s不能为空", "tmp_file_path"));
        }
        return tmpFilePath;
    }

    public static String getBackFilePath() {
        Map<String, String> properties = PortConfig.getPortPropertiesByPortNid(PortNid.HANGZHOU.getNid());
        String backFilePath = properties.get("back_file_path");
//        if (StringUtils.isBlank(backFilePath)) {
//            throw new IllegalArgumentException(String.format("FTP交互参数%s不能为空", "back_file_path"));
//        }
        return backFilePath;
    }

    public static int getClientTimeout() {
        return clientTimeout;
    }

    public static int getDataTimeout() {
        return dataTimeout;
    }

    public static int getDefaultTimeout() {
        return defaultTimeout;
    }

    public static int getTransferFileType() {
        return FTP.ASCII_FILE_TYPE;
    }
}
