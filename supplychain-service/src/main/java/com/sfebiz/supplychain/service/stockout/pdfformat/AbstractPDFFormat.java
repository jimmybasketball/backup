package com.sfebiz.supplychain.service.stockout.pdfformat;

import java.net.URL;
import java.text.DecimalFormat;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.persistence.base.line.manager.LogisticsLineManager;
import com.sfebiz.supplychain.service.FileOperationService;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/1/5
 * Time: 下午4:01
 */
public abstract class AbstractPDFFormat implements PDFFormat, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPDFFormat.class);

    // 金额，保留两位小数
    protected static final DecimalFormat AMOUNT_FORMAT = new DecimalFormat("##,##0.00");

    protected static final String SEPARATOR = "######";

    //中文字体集
    protected BaseFont bfChinese;

    //中文扩展字体集
    protected Font FontChinese;
    protected Font BoldLittleBigChinese;
    protected Font BoldBigLittleChinese;
    protected Font BoldRedBigChinese;
    protected Font BoldBigChinese;
    protected Font BoldBig16Chinese;
    protected Font BoldBig22Chinese;
    protected Font Big16Chinese;
    protected Font Big10Chinese;

    //韩文基础字体集
    protected BaseFont bfSouthKorea;
    //韩文扩展字体集
    protected Font fontSouthKorea;

    //日本基础字体集
    protected BaseFont bfJapan;
    //日本扩展字体集
    protected Font fontJapan;

    /**
     * 顺丰LOGO
     */
    protected static Image sfLogo;

    /**
     * 中通LOGO
     */
    protected static Image ztoLogo;

    /**
     * 圆通LOGO
     */
    protected static Image ytLogo;
    
    /**
     * 中华快递LOGO
     */
    protected static Image acsLogo;

    /**
     * 威时沛运LOGO
     */
    protected static Image wspLogo;

    /**
     * 高捷LOGO
     */
    protected static Image gaoJieLogo;


    /**
     * 高捷圆通LOGO
     */
    protected static Image gaoJieytLogo;
    
    /**
     * rhf顺丰logo
     */
    protected static Image rhfSfLogo;

    /**
     * 丰趣海淘LOGO
     */
    protected static Image fqLogo;

    /**
     * 讯吉安LOGO
     */
    protected static Image xjaLogo;
    
    protected static Image qdLogo;
    
    protected static Image rhfYdLogo;

    protected static Image gaoJiesfLogo;

    
    @Resource
    protected FileOperationService fileOperationService;

    @Resource
    protected LogisticsLineManager logisticsLineManager;


    /**
     * 在一个新页面中打印文本信息
     *
     * @param text
     * @return
     */
    @Override
    public PdfPTable buildTextOnNewPage(String text) {
        float[] widths = {375f};// 设置表格的列宽和列数
        PdfPTable table = new PdfPTable(widths);// 建立一个pdf表格
        table.setTotalWidth(PageSize.A5.getWidth() - 10); // 设置表格宽度
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorder(1);//设置表格默认为无边框
        PdfPCell cell = new PdfPCell(new Paragraph(text, Big16Chinese));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(10);
        table.addCell(cell);
        return table;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            bfChinese = BaseFont.createFont(fileOperationService.getTtfPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            bfSouthKorea = BaseFont.createFont("HYGoThic-Medium", "UniKS-UCS2-H", BaseFont.NOT_EMBEDDED);
            bfJapan = BaseFont.createFont("KozMinPro-Regular", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED);

            fontSouthKorea = new Font(bfSouthKorea, 8, Font.NORMAL);
            fontJapan = new Font(bfJapan, 8, Font.NORMAL);

            BaseFont bf_helv = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
            //定义字体
            FontChinese = new Font(bfChinese, 8, Font.NORMAL); // 其他所有文字字体
            BoldLittleBigChinese = new Font(bfChinese, 10, Font.BOLD); // 粗体
            BoldBigLittleChinese = new Font(bfChinese, 20, Font.BOLD); // 粗体
            BoldRedBigChinese = new Font(bfChinese, 20, Font.BOLD, BaseColor.RED); // 粗体
            BoldBigChinese = new Font(bf_helv, 35, Font.BOLD); // 粗体
            BoldBig16Chinese = new Font(bfChinese, 13, Font.BOLD); // 粗体
            BoldBig22Chinese = new Font(bfChinese, 22, Font.BOLD); // 粗体
            Big16Chinese = new Font(bfChinese, 13, Font.NORMAL);
            Big10Chinese = new Font(bfChinese, 10, Font.NORMAL); // 常规


            sfLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/sf/b84/logoSC.png"));
            if (null == sfLogo) {
                sfLogo = Image.getInstance(new URL("http://img0.sfht.com/sf/b84/logoSC.png"));
            }
            
            ytLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/vendor/ytLogo.png"));

            fqLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/vendor/fqLogo.png"));

            wspLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/vendor/wspLogo.png"));

            gaoJieLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/vendor/gjLogo.png"));

            gaoJieytLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/vendor/gjytLogo.jpg"));
            
            ztoLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/vendor/ztoLogo.png"));

            acsLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/vendor/acsLogo.png"));

            xjaLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/vendor/xjaLogo.png"));
            
            qdLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/vendor/qdLogo.png"));

            rhfSfLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/vendor/logorhfSC.png"));

            rhfYdLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/vendor/rhfYdLogo.png"));
            
            gaoJiesfLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/vendor/goldJetLogo.jpg"));

        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR).setException(e).setErrorMsg("字体文件初始化异常").log();
        }
    }

}
