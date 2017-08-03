package com.sfebiz.supplychain.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRUtil
{
    private static final String CODE = "utf-8";
    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * 生成RQ二维码
     *
     * @author wuhongbo
     * @param str
     *            内容
     * @param height
     *            高度（px）
     *
     */
    public static BufferedImage getRQ(String str, Integer height)
    {
        if (height == null || height < 100)
        {
            height = 200;
        }

        try
        {
            // 文字编码
            Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, CODE);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, height, height, hints);

            return toBufferedImage(bitMatrix);

            // 输出方式
            // 网页
            // ImageIO.write(image, "png", response.getOutputStream());

            // 文件
            // ImageIO.write(image, "png", file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成一维码（128）
     *
     * @author wuhongbo
     * @param str
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage getBarcode(String str, Integer width,
                                           Integer height)
    {

        if (width == null || width < 200)
        {
            width = 200;
        }

        if (height == null || height < 50)
        {
            height = 50;
        }

        try
        {
            // 文字编码
            Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, CODE);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(str,
                    BarcodeFormat.CODE_128, width, height, hints);

            return toBufferedImage(bitMatrix);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成二维码，写到文件中
     *
     * @author wuhongbo
     * @param str
     * @param height
     * @param file
     * @throws IOException
     */
    public static void getRQWriteFile(String str, Integer height, File file)
            throws IOException
    {
        BufferedImage image = getRQ(str, height);
        ImageIO.write(image, "png", file);
    }

    /**
     * 生成一维码，写到文件中
     *
     * @author wuhongbo
     * @param str
     * @param height
     * @param file
     * @throws IOException
     */
    public static void getBarcodeWriteFile(String str, Integer width,
                                           Integer height, File file) throws IOException
    {
        BufferedImage image = getBarcode(str, width, height);
        ImageIO.write(image, "png", file);
    }

    /**
     * 转换成图片
     *
     * @author wuhongbo
     * @param matrix
     * @return
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix)
    {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    public static void main(String[] args) throws Exception
    {
        File file = new File("C:\\Users\\wang_cl\\Downloads\\barcodetest.png");
        // RQUtil.getRQwriteFile("吴宏波中华人民共和国", 200, file);

        // code39 大写字母、数字、-、
        // code128
        getBytes(QRUtil.getBarcode("444403563999", 200, 30));

        System.out.println("-----成生成功----");
        System.out.println( getBytes(QRUtil.getBarcode("444403563999", 300,30)));

        System.out.println("-----解析成功----");
    }
    //获取图片字节码数组
    private static byte[] getBytes(BufferedImage paramBufferedImage) throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(paramBufferedImage, "png", out);
        byte[] b = out.toByteArray();
        return b;
    }

    /**
     * 生成一维码（128）byte数组
     *
     * @author wuhongbo
     * @param str
     * @param width
     * @param height
     * @return
     */
    public static byte[] getBarcodeBytes(String str, Integer width,
                                           Integer height)
    {

        if (width == null || width < 200)
        {
            width = 200;
        }

        if (height == null || height < 50)
        {
            height = 50;
        }

        if (StringUtils.isBlank(str)){
            return new byte[0];
        }

        try
        {
            // 文字编码
            Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, CODE);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(str,
                    BarcodeFormat.CODE_128, width, height, hints);

            return getBytes(toBufferedImage(bitMatrix));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}