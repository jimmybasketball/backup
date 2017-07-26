package com.sfebiz.supplychain.service.stockout.pdfformat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.code.StockoutReturnCode;
import com.sfebiz.supplychain.exposed.line.enums.PdfTemplateType;
import com.sfebiz.supplychain.persistence.base.line.domain.LogisticsLineDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.ExportedShipOrderDO;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.util.NumberUtil;

import net.pocrd.entity.ServiceException;

/**
 * 优先投-中通面单绘制
 * 
 * @author matt
 * @version $Id: YxtCustomZtoDeliveryPDFFormat2.java, v 0.1 2017年4月10日 下午2:39:47 matt Exp $
 */
@Component("yxtCustomZtoDeliveryPDFFormat2")
public class YxtCustomZtoDeliveryPDFFormat2 extends AbstractPDFFormat {

    private static final Logger logger = LoggerFactory.getLogger(YxtCustomZtoDeliveryPDFFormat2.class);
    
	private static final String SEPARATE_BLANK_STR = "    ";

    @Override
    public PdfPTable buildPdfBill(ExportedShipOrderDO exportedShipOrderDO, WarehouseDO warehouseDO, LogisticsLineDO logisticsLineDO, PdfContentByte cb) throws ServiceException {
        try {

            String userName = StringUtils.isBlank(exportedShipOrderDO.getDeclarePayerName()) ? exportedShipOrderDO.getBuyerName() : exportedShipOrderDO.getDeclarePayerName();

            // 1.2. 定义字体			
			Font hf_8 = new Font(bfChinese, 6);
			Font hf_8_B = new Font(bfChinese, 6, Font.BOLD);
			Font hf_9 = new Font(bfChinese, 7);
			Font hf_9_B = new Font(bfChinese, 7, Font.BOLD);
			Font hf_10 = new Font(bfChinese, 8);
			Font hf_10_B = new Font(bfChinese, 8, Font.BOLD);
			Font hf_12 = new Font(bfChinese, 10);
			Font hf_12_B = new Font(bfChinese, 10, Font.BOLD);
			Font hf_14 = new Font(bfChinese, 12);
			Font hf_14_B = new Font(bfChinese, 12, Font.BOLD);
			Font hf_20 = new Font(bfChinese, 18);
			Font hf_20_B = new Font(bfChinese, 18, Font.BOLD);
			Font hf_32 = new Font(bfChinese, 30);
			Font hf_32_B = new Font(bfChinese, 30, Font.BOLD);
			Font fontSouthKorea = new Font(bfSouthKorea, 8, Font.NORMAL);
            
			// 1.3. 绘制面单的整体结构
			PdfPTable table = new PdfPTable(1);
			table.setWidths(new int[] { 100 });
			table.setWidthPercentage(100);

			// 2. 填充表格内容
			// 2.1. 第一行
			/**************************************** 第一行 ********************************************/
			PdfPCell base_c1 = new PdfPCell();
            base_c1.setFixedHeight(NumberUtil.parseMmToPt(10.00f));

            float[] headerWidth = {50f, 50f};
            PdfPTable base_c1_t1 = new PdfPTable(headerWidth);
            base_c1_t1.setWidthPercentage(100);

            PdfPCell base_c1_t1_c1 = new PdfPCell();
            base_c1_t1_c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            base_c1_t1_c1.setVerticalAlignment(Element.ALIGN_BOTTOM);
            ztoLogo.scalePercent(100f);
            base_c1_t1_c1.addElement(ztoLogo);
            base_c1_t1_c1.setFixedHeight(35f);
            base_c1_t1_c1.setPadding(2f);
            base_c1_t1_c1.setBorder(0);
            base_c1_t1.addCell(base_c1_t1_c1);

            PdfPCell base_c1_t1_c2 = new PdfPCell(new Paragraph("杭州市场部", hf_14_B));
            base_c1_t1_c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            base_c1_t1_c2.setVerticalAlignment(Element.ALIGN_CENTER);
            base_c1_t1_c2.setBorder(0);
            base_c1_t1.addCell(base_c1_t1_c2);

            base_c1.addElement(base_c1_t1);
            table.addCell(base_c1);
            
            /**************************************** 第二行 ********************************************/
            PdfPCell base_c2 = new PdfPCell();
            base_c2.setFixedHeight(NumberUtil.parseMmToPt(25.00f));
            base_c2.setPadding(0);

            float[] base_c2_t1_width = {75f, 25f};
            PdfPTable base_c2_t1 = new PdfPTable(base_c2_t1_width);
            base_c2_t1.setWidthPercentage(100);

            PdfPCell base_c2_t1_c1 = new PdfPCell();
            base_c2_t1_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            base_c2_t1_c1.setPaddingLeft(20f);
            base_c2_t1_c1.setFixedHeight(NumberUtil.parseMmToPt(11.00f));
            base_c2_t1_c1.setBorder(0);
            base_c2_t1_c1.setBorderWidthBottom(1);
            base_c2_t1_c1.addElement(new Paragraph(exportedShipOrderDO.getIntrMailNo(), hf_12_B));
            base_c2_t1.addCell(base_c2_t1_c1);
            
            PdfPCell base_c2_t1_c2 = new PdfPCell();
            base_c2_t1_c2.setBorder(0);
            base_c2_t1_c2.setBorderWidthLeft(1);
            base_c2_t1_c2.setRowspan(2);
            base_c2_t1_c2.addElement(new Paragraph("", hf_14_B));
            base_c2_t1.addCell(base_c2_t1_c2);
            
            PdfPCell base_c2_t1_c3 = new PdfPCell();
            base_c2_t1_c3.setFixedHeight(NumberUtil.parseMmToPt(14.00f));
            base_c2_t1_c3.setBorder(0);
            base_c2_t1_c3.setPaddingLeft(10);
            base_c2_t1_c3.addElement(new Paragraph(buildSenderInfo(exportedShipOrderDO), hf_8));
            base_c2_t1.addCell(base_c2_t1_c3);
            
            base_c2.addElement(base_c2_t1);
            table.addCell(base_c2);
            
            /**************************************** 第三行 ********************************************/
            PdfPCell base_c3 = new PdfPCell();
            base_c3.setFixedHeight(NumberUtil.parseMmToPt(25.00f));
            base_c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            base_c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            base_c3.setPaddingLeft(10);

            base_c3.addElement(new Paragraph(buildReceiverInfo(exportedShipOrderDO), hf_9));
            base_c3.addElement(new Paragraph(buildSkuList(exportedShipOrderDO, ""), hf_8));
            
            table.addCell(base_c3);
            
            /**************************************** 第四行 ********************************************/
            PdfPCell base_c4 = new PdfPCell();
            base_c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            base_c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            base_c4.setPadding(0);
            base_c4.setFixedHeight(NumberUtil.parseMmToPt(14.00f));
            Paragraph destcodeP = new Paragraph(exportedShipOrderDO.getDestcode(), hf_20_B);
            destcodeP.setAlignment(Element.ALIGN_CENTER);
            base_c4.addElement(destcodeP);
            
            table.addCell(base_c4);
            
            /**************************************** 第五行 ********************************************/
            PdfPCell base_c5 = new PdfPCell();
            base_c5.setFixedHeight(NumberUtil.parseMmToPt(25.00f));
            base_c5.setPadding(0);
            base_c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            base_c5.setVerticalAlignment(Element.ALIGN_MIDDLE);

            Barcode128 base_c5_code = new Barcode128();
            base_c5_code.setCode(exportedShipOrderDO.getIntrMailNo());
            base_c5_code.setCodeType(Barcode128.CODE128);
            Image base_c5_code_image = base_c5_code.createImageWithBarcode(cb, null, null);

            base_c5_code_image.setAlignment(Element.ALIGN_CENTER);
            base_c5_code_image.setWidthPercentage(100);
            base_c5_code_image.scalePercent(180f);
            
            base_c5.addElement(base_c5_code_image);
            
            table.addCell(base_c5);
            
            /**************************************** 第六行 ********************************************/
            PdfPCell base_c6 = new PdfPCell();
            base_c6.setFixedHeight(NumberUtil.parseMmToPt(23.00f));
            base_c6.setPadding(0);

            float[] base_c6_t1_width = {40f, 60f};
            PdfPTable base_c6_t1 = new PdfPTable(base_c6_t1_width);
            base_c6_t1.setWidthPercentage(100);

            PdfPCell base_c6_t1_c1 = new PdfPCell();
            base_c6_t1_c1.setFixedHeight(NumberUtil.parseMmToPt(11.00f));
            base_c6_t1_c1.setBorder(0);
            base_c6_t1_c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            base_c6_t1_c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            ztoLogo.scalePercent(50f);
            base_c6_t1_c1.addElement(ztoLogo);
            base_c6_t1.addCell(base_c6_t1_c1);
            
            PdfPCell base_c6_t1_c2 = new PdfPCell();
            base_c6_t1_c2.setBorder(0);
            base_c6_t1_c2.setRowspan(2);
            base_c6_t1_c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            Barcode128 base_c6_code = new Barcode128();
            base_c6_code.setCode(exportedShipOrderDO.getIntrMailNo());
            base_c6_code.setCodeType(Barcode128.CODE128);
            Image base_c6_code_image = base_c6_code.createImageWithBarcode(cb, null, null);

            base_c6_code_image.setAlignment(Element.ALIGN_CENTER);
            base_c6_code_image.setWidthPercentage(100);
            base_c6_code_image.scalePercent(150f);
            
            base_c6_t1_c2.addElement(base_c6_code_image);
            base_c6_t1.addCell(base_c6_t1_c2);
            
            PdfPCell base_c6_t1_c3 = new PdfPCell();
            base_c6_t1_c3.setFixedHeight(NumberUtil.parseMmToPt(12.00f));
            base_c6_t1_c3.setBorder(0);
            base_c6_t1_c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            base_c6_t1_c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            base_c6_t1_c3.addElement(new Paragraph("杭州市场部", hf_12));
            base_c6_t1_c3.setPaddingLeft(10f);
            base_c6_t1.addCell(base_c6_t1_c3);
            
            base_c6.addElement(base_c6_t1);
            
            table.addCell(base_c6);
            
            /**************************************** 第七行 ********************************************/
            PdfPCell base_c7 = new PdfPCell();
            base_c7.setFixedHeight(NumberUtil.parseMmToPt(15.00f));
            base_c7.setPadding(0);

            float[] base_c7_t1_width = {45f, 55f};
            PdfPTable base_c7_t1 = new PdfPTable(base_c7_t1_width);
            base_c7_t1.setWidthPercentage(100);

            PdfPCell base_c7_t1_c1 = new PdfPCell();
            base_c7_t1_c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            base_c7_t1_c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            base_c7_t1_c1.setBorder(0);
            base_c7_t1_c1.setBorderWidthRight(1);
            base_c7_t1_c1.setFixedHeight(NumberUtil.parseMmToPt(15.00f));
            base_c7_t1_c1.addElement(new Paragraph(" 标准快递", hf_20));
            base_c7_t1.addCell(base_c7_t1_c1);

            PdfPCell base_c7_t1_c2 = new PdfPCell(new Paragraph(exportedShipOrderDO.getIntrMailNo(), hf_20));
            base_c7_t1_c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            base_c7_t1_c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            base_c7_t1_c2.setBorder(0);
            base_c7_t1.addCell(base_c7_t1_c2);

            base_c7.addElement(base_c7_t1);
            
            table.addCell(base_c7);
            
            /**************************************** 第八行 ********************************************/
            PdfPCell base_c8 = new PdfPCell();
            base_c8.setFixedHeight(NumberUtil.parseMmToPt(52.00f));
            base_c8.setPadding(0);

            float[] base_c8_t1_width = {45f, 40f, 15f};
            PdfPTable base_c8_t1 = new PdfPTable(base_c8_t1_width);
            base_c8_t1.setWidthPercentage(100);

            PdfPCell base_c8_t1_c1 = new PdfPCell();
            base_c8_t1_c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            base_c8_t1_c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            base_c8_t1_c1.setPadding(10);
            base_c8_t1_c1.setColspan(2);
            base_c8_t1_c1.setFixedHeight(NumberUtil.parseMmToPt(25.00f));
            base_c8_t1_c1.addElement(new Paragraph(buildReceiverInfo(exportedShipOrderDO), hf_8));
            base_c8_t1_c1.addElement(new Paragraph(buildSkuList(exportedShipOrderDO, ""), hf_8));
            base_c8_t1.addCell(base_c8_t1_c1);

            PdfPCell base_c8_t1_c2 = new PdfPCell(new Paragraph("", hf_14_B));
            base_c8_t1_c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            base_c8_t1_c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            base_c8_t1_c2.setRowspan(3);
            base_c8_t1.addCell(base_c8_t1_c2);
            
            PdfPCell base_c8_t1_c3 = new PdfPCell(new Paragraph(buildSenderInfo(exportedShipOrderDO), hf_8));
            base_c8_t1_c3.setHorizontalAlignment(Element.ALIGN_LEFT);
            base_c8_t1_c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            base_c8_t1_c3.setPaddingLeft(10f);
            base_c8_t1_c3.setFixedHeight(NumberUtil.parseMmToPt(15.00f));
            base_c8_t1_c3.setColspan(2);
            base_c8_t1.addCell(base_c8_t1_c3);
            
            PdfPCell base_c8_t1_c4 = new PdfPCell(new Paragraph("您对此单的签收，代表您已验货，确认商品信息无误,包裹完好,没有划痕、破损等表面质量问题。", hf_10));//描述
            base_c8_t1_c4.setHorizontalAlignment(Element.ALIGN_LEFT);
            base_c8_t1_c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            base_c8_t1_c4.setFixedHeight(NumberUtil.parseMmToPt(12.00f));
            base_c8_t1.addCell(base_c8_t1_c4);
            
            PdfPCell base_c8_t1_c5 = new PdfPCell(new Paragraph("签收", hf_12_B));
            base_c8_t1_c5.setHorizontalAlignment(Element.ALIGN_LEFT);
            base_c8_t1_c5.setVerticalAlignment(Element.ALIGN_TOP);
            base_c8_t1_c5.setFixedHeight(NumberUtil.parseMmToPt(12.00f));
            base_c8_t1.addCell(base_c8_t1_c5);

            base_c8.addElement(base_c8_t1);

            table.addCell(base_c8);
            
            return table;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR).setException(e).setErrorMsg("[发货单生成PDF]:异常").log();
            if (e instanceof ServiceException) {
                throw (ServiceException) e;
            } else {
                throw new ServiceException(StockoutReturnCode.STOCKOUT_ORDER_SERVICE_PDF_ERROR);
            }
        }
    }
    
    /**
     * 构建发货信息
     * 
     * @param exportedShipOrderDO
     * @return
     */
	private String buildSenderInfo(ExportedShipOrderDO exportedShipOrderDO){
		StringBuffer sb = new StringBuffer();
		
		sb.append("寄件人：").append(exportedShipOrderDO.getSenderName()).append(SEPARATE_BLANK_STR).append("联系方式：").append(exportedShipOrderDO.getSenderTelephone());
		sb.append("\n");
		sb.append("地址：").append(exportedShipOrderDO.getSenderAddress());
		sb.append("\n");

		return sb.toString();
	}
	
	/**
	 * 构建收货信息
	 * 
	 * @param exportedShipOrderDO
	 * @return
	 */
	private String buildReceiverInfo(ExportedShipOrderDO exportedShipOrderDO){
		StringBuffer sb = new StringBuffer();
	
		sb.append("收件人：").append(exportedShipOrderDO.getBuyerName()).append(SEPARATE_BLANK_STR).append("电话：").append(exportedShipOrderDO.getBuyerTelephone());
		sb.append("\n");
		sb.append("地址：").append(exportedShipOrderDO.getBuyerAddress());
		sb.append("\n");
		
		return sb.toString();
	}
	
	private String buildSkuList(ExportedShipOrderDO exportedShipOrderDO, String country){
		String[] skuIdList = exportedShipOrderDO.getSkuIdStr().split(SEPARATOR);

		String[] skuForeignNameList = exportedShipOrderDO
				.getSkuForeignNameStr().split(SEPARATOR);
		String[] skuNameList = exportedShipOrderDO.getSkuNameStr().split(
				SEPARATOR);

		String[] skuCountList = exportedShipOrderDO.getSkuCountStr().split(
				SEPARATOR);
		String[] remarkList = exportedShipOrderDO.getRemarks().split(SEPARATOR);

		StringBuffer sb = new StringBuffer();
		sb.append("\n 商品：");
		for (int i = 0; i < skuIdList.length; i++) {
			sb.append(" [").append(skuNameList[i]).append("*")
					.append(skuIdList[i]).append("*").append(remarkList[i])
					.append(" * ").append(skuCountList[i]).append("]");
		}
		sb.append("（注：[品名*barcode*skuId * 数量]）");
		return sb.toString().substring(1);
	}

    @Override
    public String getFormat() {
        return PdfTemplateType.YXT_ZTO_2.getValue();
    }
}
