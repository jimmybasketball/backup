package com.sfebiz.supplychain.util;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static final int BUFFER = 2048;

    /**
     * 压缩文件
     *
     * @param filePath     文件路径
     * @param destFileName 目标文件名
     * @param srcFileNames 源文件名数组
     */
    public static void compress(String filePath, String destFileName, String[] srcFileNames) {
        BufferedInputStream origin = null;
        FileOutputStream dest = null;
        try {
            dest = new FileOutputStream(filePath + destFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];
            for (String srcFileName : srcFileNames) {
                FileInputStream fi = new FileInputStream(filePath + srcFileName);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(srcFileName);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
                out.flush();
            }
            out.close();
        } catch (FileNotFoundException e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setException(e)
                    .setMsg("文件压缩异常")
                    .addParm("文件路径", filePath)
                    .addParm("目标文件名：", destFileName)
                    .log();
        } catch (IOException e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setException(e)
                    .setMsg("文件压缩异常")
                    .addParm("文件路径", filePath)
                    .addParm("目标文件名：", destFileName)
                    .log();
        } finally {
            try {
                if (origin != null) {
                    origin.close();
                }
                if (dest != null) {
                    dest.close();
                }
            } catch (IOException e) {
                LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                        .setException(e)
                        .setMsg("流关闭异常")
                        .addParm("文件路径", filePath)
                        .addParm("目标文件名：", destFileName)
                        .log();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param absoluteFilePath 文件绝对路径
     */
    public static void deleteFile(String absoluteFilePath) {
        File file = new File(absoluteFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 删除文件
     */
    public static void deleteFile(File... files) {
        if (null != files && files.length > 0) {
            for (File f : files) {
                if (f.exists()) {
                    f.delete();
                }
            }
        }
    }

    /**
     * 获取excel文件name
     * @param url
     * @return
     */
    public static String getFileNameFromUrl(String url) {
        String name = Long.toString(System.currentTimeMillis()) + ".xls";
        int index = url.lastIndexOf("/");
        if (index > 0) {
            name = url.substring(index + 1);
            if (name.trim().length() > 0) {
                return name;
            }
        }
        return name;
    }
}
