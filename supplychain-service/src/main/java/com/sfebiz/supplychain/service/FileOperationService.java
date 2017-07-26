package com.sfebiz.supplychain.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.LogisticsDynamicConfig;

/**
 * <p>文件操作的相关服务</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/11/18
 * Time: 上午10:58
 */
public class FileOperationService {

    private static final Logger logger = LoggerFactory.getLogger(FileOperationService.class);

    private volatile String tmpPath;

    private volatile String ttfPath;

    private volatile String accessKeyId;

    private volatile String accessKeySecret;

    public static final String DEFAULT_REGION = "china";

    private Map<String, OssClientWrapper> ossClientMap = new HashMap<String, OssClientWrapper>();



    /**
     * 上传文件 到OSS中
     *
     * @param contentType  类型 PDF or Excel...
     * @param relativePath 相对路径
     * @param fileName     文件名称
     * @return
     */
    public String uploadFile2OSS(String contentType, String relativePath, String fileName, String region) throws FileNotFoundException {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("[上传文件到OSS]:开始")
                .addParm("文件名", fileName)
                .log();

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(contentType);
        String filePath = tmpPath + "/" + fileName;
        File file = new File(filePath);
        InputStream content = new FileInputStream(file);
        meta.setContentLength(file.length());
        meta.addUserMetadata("position", relativePath);
        OssClientWrapper ossClientWrapper = getOssClientByRegion(region);
        PutObjectResult result = ossClientWrapper.getOssClient().putObject(ossClientWrapper.getBucketName(), relativePath + fileName, content, meta);
        return result.getETag();
    }

    public OssClientWrapper getOssClientByRegion(String region) {
        if (StringUtils.isBlank(region)) {
            throw new IllegalArgumentException("区域参数信息不能为空");
        }
        region = region.toLowerCase();
        if (!ossClientMap.containsKey(region)) {
            synchronized (this.ossClientMap) {
                OssClientWrapper ossClientWrapper = generateOssClient(region);
                if (null != ossClientWrapper) {
                    ossClientMap.put(region, ossClientWrapper);
                }
            }
        }
        return ossClientMap.get(region);
    }

    public String getOssClientBucketNameByRegion(String region) {
        OssClientWrapper ossClientWrapper = getOssClientByRegion(region);
        if (ossClientWrapper == null) {
            throw new IllegalArgumentException("区域无法获取OssClient");
        }

        return ossClientWrapper.getBucketName();
    }

    public String getOssClientEndPointByRegion(String region){
        OssClientWrapper ossClientWrapper = getOssClientByRegion(region);
        if (ossClientWrapper == null) {
            throw new IllegalArgumentException("区域无法获取OssClient");
        }
        return ossClientWrapper.getIntranetEndPoint();
    }

    /**
     * 根据地理位置获取OssClient
     *
     * @param region
     * @return
     */
    protected OssClientWrapper generateOssClient(String region) {
        Map<String, String> ossConfig = LogisticsDynamicConfig.getOssConfig().getAllPropertiesOnDataId(region);
        if (null != ossConfig && ossConfig.size() > 0) {
            String intranetEndPoint = ossConfig.get("intranetEndPoint");
            String internetEndPoint = ossConfig.get("internetEndPoint");
            String bucketName = ossConfig.get("bucketName");
            if (StringUtils.isNotEmpty(intranetEndPoint) && StringUtils.isNotEmpty(internetEndPoint) && StringUtils.isNotEmpty(bucketName)) {
                return new OssClientWrapper(new OSSClient(intranetEndPoint, accessKeyId, accessKeySecret), bucketName, internetEndPoint, intranetEndPoint);
            }
        }
        return null;
    }


    public String getTmpPath() {
        return tmpPath;
    }

    public void setTmpPath(String tmpPath) {
        this.tmpPath = tmpPath;
    }

    public String getTtfPath() {
        return ttfPath;
    }

    public void setTtfPath(String ttfPath) {
        this.ttfPath = ttfPath;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
    
    public class OssClientWrapper implements Serializable {

        private static final long serialVersionUID = 7491766631595426917L;
        private OSSClient ossClient;
        private String bucketName;
        private String intranetEndPoint;
        private String internetEndPoint;

        public OssClientWrapper(OSSClient ossClient, String bucketName, String internetEndPoint, String intranetEndPoint) {
            this.ossClient = ossClient;
            this.bucketName = bucketName;
            this.internetEndPoint = internetEndPoint;
            this.intranetEndPoint = intranetEndPoint;
        }

        public OSSClient getOssClient() {
            return ossClient;
        }

        public void setOssClient(OSSClient ossClient) {
            this.ossClient = ossClient;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getIntranetEndPoint() {
            return intranetEndPoint;
        }

        public void setIntranetEndPoint(String intranetEndPoint) {
            this.intranetEndPoint = intranetEndPoint;
        }

        public String getInternetEndPoint() {
            return internetEndPoint;
        }

        public void setInternetEndPoint(String internetEndPoint) {
            this.internetEndPoint = internetEndPoint;
        }
    }
}
