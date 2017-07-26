package com.sfebiz.supplychain.service.stockout.pdfformat;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
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
 * ACS的中通面单打印模板
 * 
 * @author matt
 * @version $Id: AcsCustomZTODeliveryPDFFormat.java, v 0.1 2016年12月06日 下午2:58:07
 *          matt Exp $
 */
@Component("acsCustomZTODeliveryPDFFormat")
public class AcsCustomZTODeliveryPDFFormat extends AbstractPDFFormat {

	/** 日志 */
	private static final Logger logger = LoggerFactory
			.getLogger(AcsCustomZTODeliveryPDFFormat.class);

	/** 字体文件路径 */
	private static final String simheiFontPath = "/home/admin/font/AdobeSongStd-Light.otf";

	private static final String logoFilePath = "/home/admin/images/acs_zto_logo.jpg";

	@Override
	public PdfPTable buildPdfBill(ExportedShipOrderDO exportedShipOrderDO,
			WarehouseDO warehouseDO, LogisticsLineDO logisticsLineDO, PdfContentByte cb)
			throws ServiceException {

		try {

			// 1.1 绘制准备
			BaseFont bf = BaseFont.createFont(simheiFontPath,
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

			// 1.2 创建基础字体
			Font hf_6 = new Font(bf, 6);
			Font hf_6_B = new Font(bf, 6, Font.BOLD);
			Font hf_7 = new Font(bf, 7);
			Font hf_7_B = new Font(bf, 7, Font.BOLD);
			Font hf_8 = new Font(bf, 8);
			Font hf_8_B = new Font(bf, 8, Font.BOLD);
			Font hf_9 = new Font(bf, 9);
			Font hf_9_B = new Font(bf, 9, Font.BOLD);
			Font hf_10 = new Font(bf, 8);
			Font hf_10_B = new Font(bf, 8, Font.BOLD);
			Font hf_12 = new Font(bf, 12);
			Font hf_12_B = new Font(bf, 12, Font.BOLD);
			Font hf_13 = new Font(bf, 13);
			Font hf_13_B = new Font(bf, 13, Font.BOLD);
			Font hf_14 = new Font(bf, 14);
			Font hf_14_B = new Font(bf, 14, Font.BOLD);
			Font hf_18 = new Font(bf, 18);
			Font hf_18_B = new Font(bf, 18, Font.BOLD);
			Font hf_20 = new Font(bf, 20);
			Font hf_20_B = new Font(bf, 20, Font.BOLD);
			Font hf_32 = new Font(bf, 32);
			Font hf_32_B = new Font(bf, 32, Font.BOLD);

			Font hf_26 = new Font(bf, 26);
			Font hf_26_B = new Font(bf, 26, Font.BOLD);

			// 1.3. 绘制面单的整体结构
			PdfPTable baseTable = new PdfPTable(1);
			baseTable.setTotalWidth(NumberUtil.parseMmToPt(100));
			baseTable.setWidths(new int[] { 100 });
			baseTable.setWidthPercentage(100);

			// 2. 填充表格内容
			// 2.1. 第一行
			/**************************************** 第一行 ********************************************/
			PdfPCell base_cell_1 = new PdfPCell();
			base_cell_1.setBorder(0);
			base_cell_1.setBorderWidthBottom(1);
			base_cell_1.setFixedHeight(NumberUtil.parseMmToPt(15));
			base_cell_1.setUseAscender(true);
			base_cell_1.setVerticalAlignment(Element.ALIGN_MIDDLE);

			Paragraph cityP = new Paragraph(
					getDiscodeStr(exportedShipOrderDO.getDestcode()), hf_26_B); // 大头笔
			cityP.setAlignment(1);
			base_cell_1.addElement(cityP);
			baseTable.addCell(base_cell_1);

			// 3. 第二行
			/**************************************** 第二行 ********************************************/
			PdfPCell base_cell_2 = new PdfPCell();
			base_cell_2.setBorder(0);
			base_cell_2.setBorderWidthBottom(1f);
			base_cell_2.setUseAscender(true);
			base_cell_2.setVerticalAlignment(Element.ALIGN_CENTER);
			base_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
			base_cell_2.setFixedHeight(NumberUtil.parseMmToPt(19f));

			PdfPTable cell_2_table = new PdfPTable(2);
			cell_2_table.setWidths(new int[] { 31, 69 });
			cell_2_table.setWidthPercentage(100);

			PdfPCell cell_2_table_cell_1 = new PdfPCell();
			cell_2_table_cell_1.setBorder(0);
			cell_2_table_cell_1.setBorderWidthRight(1);
			cell_2_table_cell_1.setFixedHeight(NumberUtil.parseMmToPt(15));
			cell_2_table_cell_1.setUseAscender(true);
			cell_2_table_cell_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Paragraph huaxia = new Paragraph("華夏", hf_26_B);
			huaxia.setAlignment(1);
			cell_2_table_cell_1.addElement(huaxia);
			cell_2_table.addCell(cell_2_table_cell_1);

			PdfPCell cell_2_table_cell_2 = new PdfPCell();
			cell_2_table_cell_2.setBorder(0);
			cell_2_table_cell_2.setFixedHeight(NumberUtil.parseMmToPt(15));
			cell_2_table_cell_2.setUseAscender(true);
			cell_2_table_cell_2.setVerticalAlignment(Element.ALIGN_MIDDLE);

			Barcode128 mailNo_code128 = new Barcode128();
			mailNo_code128.setFont(bf);
			mailNo_code128.setCode(exportedShipOrderDO.getIntrMailNo()); // 运单号
			mailNo_code128.setCodeType(Barcode128.CODE128);
			Image mailNoImage = mailNo_code128.createImageWithBarcode(cb, null,
					null);
			mailNoImage.setAlignment(Element.ALIGN_CENTER);
			mailNoImage.scaleAbsolute(100, 20);

			cell_2_table_cell_2.addElement(mailNoImage);
			cell_2_table.addCell(cell_2_table_cell_2);

			base_cell_2.addElement(cell_2_table);
			baseTable.addCell(base_cell_2);

			/**************************************** 第三行 ********************************************/
			PdfPCell base_cell_3 = new PdfPCell();
			base_cell_3.setBorder(0);
			base_cell_3.setBorderWidthBottom(1);
			base_cell_3.setVerticalAlignment(Element.ALIGN_CENTER);
			base_cell_3.setHorizontalAlignment(Element.ALIGN_CENTER);
			base_cell_3.setFixedHeight(NumberUtil.parseMmToPt(15));

			PdfPTable cell_3_table = new PdfPTable(2);
			cell_3_table.setWidths(new int[] { 11, 89 });
			cell_3_table.setWidthPercentage(100);

			PdfPCell cell_3_table_cell_1 = new PdfPCell();
			cell_3_table_cell_1.setBorder(0);
			cell_3_table_cell_1.setBorderWidthRight(1);
			cell_3_table_cell_1.setFixedHeight(NumberUtil.parseMmToPt(15));
			cell_3_table_cell_1.setUseAscender(true);
			cell_3_table_cell_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Paragraph p_shou = new Paragraph("收", hf_14_B);
			p_shou.setAlignment(1);
			cell_3_table_cell_1.addElement(p_shou);
			cell_3_table.addCell(cell_3_table_cell_1);
			PdfPCell cell_3_table_cell_2 = new PdfPCell();
			cell_3_table_cell_2.setBorder(0);
			cell_3_table_cell_2.setFixedHeight(NumberUtil.parseMmToPt(15));
			cell_3_table_cell_2.setUseAscender(true);
			cell_3_table_cell_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Paragraph revcName = new Paragraph(
					exportedShipOrderDO.getBuyerName() + "  "
							+ exportedShipOrderDO.getBuyerTelephone(), hf_8);
			cell_3_table_cell_2.addElement(revcName);
			Paragraph revcAddress = new Paragraph(
					buildRevcAddress(exportedShipOrderDO), hf_7);
			cell_3_table_cell_2.addElement(revcAddress);
			cell_3_table.addCell(cell_3_table_cell_2);
			base_cell_3.addElement(cell_3_table);
			baseTable.addCell(base_cell_3);

			/**************************************** 第四行 ********************************************/
			PdfPCell base_cell_4 = new PdfPCell();
			base_cell_4.setBorder(0);
			base_cell_4.setBorderWidthBottom(1);
			base_cell_4.setVerticalAlignment(Element.ALIGN_CENTER);
			base_cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);
			base_cell_4.setFixedHeight(NumberUtil.parseMmToPt(10));

			PdfPTable cell_4_table = new PdfPTable(2);
			cell_4_table.setWidths(new int[] { 11, 89 });
			cell_4_table.setWidthPercentage(100);

			PdfPCell cell_4_table_cell_1 = new PdfPCell();
			cell_4_table_cell_1.setBorder(0);
			cell_4_table_cell_1.setBorderWidthRight(1);
			cell_4_table_cell_1.setFixedHeight(NumberUtil.parseMmToPt(10));
			cell_4_table_cell_1.setUseAscender(true);
			cell_4_table_cell_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Paragraph p_ji = new Paragraph("寄", hf_13_B);
			p_ji.setAlignment(1);
			cell_4_table_cell_1.addElement(p_ji);
			cell_4_table.addCell(cell_4_table_cell_1);
			PdfPCell cell_4_table_cell_2 = new PdfPCell();
			cell_4_table_cell_2.setBorder(0);
			cell_4_table_cell_2.setFixedHeight(NumberUtil.parseMmToPt(10));
			cell_4_table_cell_2.setUseAscender(true);
			cell_4_table_cell_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Paragraph senderName = new Paragraph(
					exportedShipOrderDO.getSenderName() + "  "
							+ exportedShipOrderDO.getSenderTelephone(), hf_8);
			cell_4_table_cell_2.addElement(senderName);
			Paragraph senderAddress = new Paragraph(
					buildSenderAddress(exportedShipOrderDO), hf_7);
			cell_4_table_cell_2.addElement(senderAddress);
			cell_4_table.addCell(cell_4_table_cell_2);
			base_cell_4.addElement(cell_4_table);
			baseTable.addCell(base_cell_4);

			/**************************************** 第五行 ********************************************/
			PdfPCell base_cell_5 = new PdfPCell();
			base_cell_5.setBorder(0);
			base_cell_5.setBorderWidthBottom(1f);
			base_cell_5.setUseAscender(true);
			base_cell_5.setVerticalAlignment(Element.ALIGN_CENTER);
			base_cell_5.setHorizontalAlignment(Element.ALIGN_CENTER);
			base_cell_5.setFixedHeight(NumberUtil.parseMmToPt(14f));

			Barcode128 orderNo_code128 = new Barcode128();
			orderNo_code128.setFont(bf);
			orderNo_code128.setCode(exportedShipOrderDO.getBizId()); // 出库单号
			orderNo_code128.setCodeType(Barcode128.CODE128);
			Image orderNoImage = orderNo_code128.createImageWithBarcode(cb,
					null, null);
			orderNoImage.setAlignment(Element.ALIGN_CENTER);
			orderNoImage.scaleAbsolute(100, 20);

			base_cell_5.addElement(orderNoImage);
			baseTable.addCell(base_cell_5);

			/**************************************** 第六行 ********************************************/
			PdfPCell base_cell_6 = new PdfPCell();
			base_cell_6.setBorder(0);
			base_cell_6.setBorderWidthBottom(2f);
			base_cell_6.setFixedHeight(NumberUtil.parseMmToPt(15f));
			base_cell_6.setPadding(5);

			Paragraph goodsName = new Paragraph(
					buildSkuList(exportedShipOrderDO, warehouseDO.getCountry()), hf_6); // 构造商品列表
			base_cell_6.addElement(goodsName);
			baseTable.addCell(base_cell_6);

			/**************************************** 第七行 ********************************************/
			PdfPCell base_cell_7 = new PdfPCell();
			base_cell_7.setBorder(0);
			base_cell_7.setBorderWidthBottom(1f);
			base_cell_7.setFixedHeight(NumberUtil.parseMmToPt(5f));
			base_cell_7.setPadding(0);
			base_cell_7.setPaddingLeft(5);
			base_cell_7.setUseAscender(true);
			base_cell_7.setVerticalAlignment(Element.ALIGN_MIDDLE);

			Paragraph acsStr = new Paragraph("ACS INTERNATIONAL", hf_8_B);
			base_cell_7.addElement(acsStr);
			baseTable.addCell(base_cell_7);

			/**************************************** 第八行 ********************************************/
			PdfPCell base_cell_8 = new PdfPCell();
			base_cell_8.setBorder(0);
			base_cell_8.setBorderWidthBottom(1);
			base_cell_8.setUseAscender(true);
			base_cell_8.setVerticalAlignment(Element.ALIGN_CENTER);
			base_cell_8.setHorizontalAlignment(Element.ALIGN_CENTER);
			base_cell_8.setFixedHeight(NumberUtil.parseMmToPt(15));

			PdfPTable cell_8_table = new PdfPTable(2);
			cell_8_table.setWidths(new int[] { 35, 65 });
			cell_8_table.setWidthPercentage(100);

			PdfPCell cell_8_table_cell_1 = new PdfPCell();
			cell_8_table_cell_1.setBorder(0);
			cell_8_table_cell_1.setFixedHeight(NumberUtil.parseMmToPt(15));
			cell_8_table_cell_1.setUseAscender(true);
			cell_8_table_cell_1.setVerticalAlignment(Element.ALIGN_CENTER);
			Paragraph p_ZTO = new Paragraph("ZTO", hf_20_B);
			p_ZTO.setAlignment(Element.ALIGN_RIGHT);
			cell_8_table_cell_1.addElement(p_ZTO);
			cell_8_table.addCell(cell_8_table_cell_1);
			PdfPCell cell_8_table_cell_2 = new PdfPCell();
			cell_8_table_cell_2.setBorder(0);
			cell_8_table_cell_2.setFixedHeight(NumberUtil.parseMmToPt(15));

			Barcode128 minMailNo_code128 = new Barcode128();
			minMailNo_code128.setFont(bf);
			minMailNo_code128.setCode(exportedShipOrderDO.getIntrMailNo());
			minMailNo_code128.setCodeType(Barcode128.CODE128);
			Image minMailNoImage = minMailNo_code128.createImageWithBarcode(cb,
					null, null);
			minMailNoImage.setAlignment(Element.ALIGN_CENTER);
			minMailNoImage.scaleAbsolute(80, 15);
			cell_8_table_cell_2.addElement(minMailNoImage);
			cell_8_table.addCell(cell_8_table_cell_2);
			base_cell_8.addElement(cell_8_table);
			baseTable.addCell(base_cell_8);

			/**************************************** 第九行 ********************************************/
			PdfPCell base_cell_9 = new PdfPCell();
			base_cell_9.setBorder(0);
			base_cell_9.setBorderWidthBottom(1);
			base_cell_9.setUseAscender(true);
			base_cell_9.setVerticalAlignment(Element.ALIGN_CENTER);
			base_cell_9.setHorizontalAlignment(Element.ALIGN_CENTER);
			base_cell_9.setFixedHeight(NumberUtil.parseMmToPt(15));

			PdfPTable cell_9_table = new PdfPTable(2);
			cell_9_table.setWidths(new int[] { 11, 89 });
			cell_9_table.setWidthPercentage(100);

			PdfPCell cell_9_table_cell_1 = new PdfPCell();
			cell_9_table_cell_1.setBorder(0);
			cell_9_table_cell_1.setBorderWidthRight(1);
			cell_9_table_cell_1.setFixedHeight(NumberUtil.parseMmToPt(15));
			cell_9_table_cell_1.setUseAscender(true);
			cell_9_table_cell_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Paragraph p_shou_ = new Paragraph("收", hf_14_B);
			p_shou_.setAlignment(1);
			cell_9_table_cell_1.addElement(p_shou_);
			cell_9_table.addCell(cell_9_table_cell_1);
			PdfPCell cell_9_table_cell_2 = new PdfPCell();
			cell_9_table_cell_2.setBorder(0);
			cell_9_table_cell_2.setFixedHeight(NumberUtil.parseMmToPt(15));
			cell_9_table_cell_2.setUseAscender(true);
			cell_9_table_cell_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Paragraph revcName_ = new Paragraph(
					exportedShipOrderDO.getBuyerName() + "  "
							+ exportedShipOrderDO.getBuyerTelephone(), hf_8);
			cell_9_table_cell_2.addElement(revcName_);
			Paragraph revcAddress_ = new Paragraph(
					buildRevcAddress(exportedShipOrderDO), hf_7);
			cell_9_table_cell_2.addElement(revcAddress_);
			cell_9_table.addCell(cell_9_table_cell_2);
			base_cell_9.addElement(cell_9_table);
			baseTable.addCell(base_cell_9);

			/**************************************** 第十行 ********************************************/
			PdfPCell base_cell_10 = new PdfPCell();
			base_cell_10.setBorder(0);
			base_cell_10.setBorderWidthBottom(1);
			base_cell_10.setVerticalAlignment(Element.ALIGN_CENTER);
			base_cell_10.setHorizontalAlignment(Element.ALIGN_CENTER);
			base_cell_10.setFixedHeight(NumberUtil.parseMmToPt(10));

			PdfPTable cell_10_table = new PdfPTable(2);
			cell_10_table.setWidths(new int[] { 11, 89 });
			cell_10_table.setWidthPercentage(100);

			PdfPCell cell_10_table_cell_1 = new PdfPCell();
			cell_10_table_cell_1.setBorder(0);
			cell_10_table_cell_1.setBorderWidthRight(1);
			cell_10_table_cell_1.setFixedHeight(NumberUtil.parseMmToPt(10));
			cell_10_table_cell_1.setUseAscender(true);
			cell_10_table_cell_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Paragraph p_ji_ = new Paragraph("寄", hf_13_B);
			p_ji_.setAlignment(1);
			cell_10_table_cell_1.addElement(p_ji_);
			cell_10_table.addCell(cell_10_table_cell_1);
			PdfPCell cell_10_table_cell_2 = new PdfPCell();
			cell_10_table_cell_2.setBorder(0);
			cell_10_table_cell_2.setFixedHeight(NumberUtil.parseMmToPt(10));
			cell_10_table_cell_2.setUseAscender(true);
			cell_10_table_cell_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Paragraph senderName_ = new Paragraph(
					exportedShipOrderDO.getSenderName() + "  "
							+ exportedShipOrderDO.getSenderTelephone(), hf_8);
			cell_10_table_cell_2.addElement(senderName_);
			Paragraph senderAddress_ = new Paragraph(
					buildSenderAddress(exportedShipOrderDO), hf_7);
			cell_10_table_cell_2.addElement(senderAddress_);
			cell_10_table.addCell(cell_10_table_cell_2);
			base_cell_10.addElement(cell_10_table);
			baseTable.addCell(base_cell_10);

			/**************************************** 第十一行 ********************************************/
			PdfPCell base_cell_11 = new PdfPCell();
			base_cell_11.setBorder(0);
			base_cell_11.setVerticalAlignment(Element.ALIGN_CENTER);
			base_cell_11.setHorizontalAlignment(Element.ALIGN_CENTER);
			base_cell_11.setFixedHeight(NumberUtil.parseMmToPt(14));

			PdfPTable cell_11_table = new PdfPTable(2);
			cell_11_table.setWidths(new int[] { 57, 43 });
			cell_11_table.setWidthPercentage(100);

			PdfPCell cell_11_table_cell_1 = new PdfPCell();
			cell_11_table_cell_1.setBorder(0);
			cell_11_table_cell_1.setBorderWidthRight(1);
			cell_11_table_cell_1.setFixedHeight(NumberUtil.parseMmToPt(14));
			cell_11_table_cell_1.setPaddingTop(5);
			cell_11_table_cell_1.setPaddingLeft(5);
			Paragraph p_OrderNo = new Paragraph("Order NO.\n\t" + exportedShipOrderDO.getBizId(), hf_10_B);
			cell_11_table_cell_1.addElement(p_OrderNo);
			cell_11_table.addCell(cell_11_table_cell_1);
			PdfPCell cell_11_table_cell_2 = new PdfPCell();
			cell_11_table_cell_2.setBorder(0);
			cell_11_table_cell_2.setBorderWidthRight(1);
			cell_11_table_cell_2.setFixedHeight(NumberUtil.parseMmToPt(14));
			cell_11_table_cell_2.setPaddingTop(5);
			cell_11_table_cell_2.setPaddingLeft(5);
			Paragraph p_SIGNATURE = new Paragraph("SIGNATURE：", hf_10_B);
			cell_11_table_cell_2.addElement(p_SIGNATURE);
			cell_11_table.addCell(cell_11_table_cell_2);
			base_cell_11.addElement(cell_11_table);
			baseTable.addCell(base_cell_11);

			return baseTable;
		} catch (Exception e) {
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).setException(e)
					.setErrorMsg("[发货单生成PDF]:异常")
					.addParm("bizId", exportedShipOrderDO.getBizId()).log();
			if (e instanceof ServiceException) {
				throw (ServiceException) e;
			} else {
				throw new ServiceException(
				        StockoutReturnCode.STOCKOUT_ORDER_SERVICE_PDF_ERROR);
			}
		}
	}

	@Override
	public String getFormat() {
		return PdfTemplateType.ACS_ZTO.getValue();
	}

	/**
	 * 构造收货人地址
	 * 
	 * @param exportedShipOrderDO
	 * @return
	 */
	public String buildRevcAddress(ExportedShipOrderDO exportedShipOrderDO) {
		return exportedShipOrderDO.getBuyerAddress();
	}

	/**
	 * 构造发货人地址
	 * 
	 * @param exportedShipOrderDO
	 * @return
	 */
	public String buildSenderAddress(ExportedShipOrderDO exportedShipOrderDO) {
		return exportedShipOrderDO.getSenderAddress();
	}

	/**
	 * 构造商品列表 格式：skuName,skuId,count,remark|skuName,skuId,count,remark|...
	 * 
	 * @param exportedShipOrderDO
	 * @return
	 */
	public String buildSkuList(ExportedShipOrderDO exportedShipOrderDO, String country) {
		String[] skuIdList = exportedShipOrderDO.getSkuIdStr().split(SEPARATOR);

		String[] skuForeignNameList = exportedShipOrderDO
				.getSkuForeignNameStr().split(SEPARATOR);
		String[] skuNameList = exportedShipOrderDO.getSkuNameStr().split(
				SEPARATOR);

		String[] skuCountList = exportedShipOrderDO.getSkuCountStr().split(
				SEPARATOR);
		String[] remarkList = exportedShipOrderDO.getRemarks().split(SEPARATOR);

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < skuIdList.length; i++) {
			if("台湾省".contains(country)){
				sb.append(" [").append(remarkList[i]).append("*").append(skuNameList[i]).append(" * ")
				.append(skuCountList[i]).append("]");
			}else{
				if (StringUtils.isNotBlank(skuForeignNameList[i])
						&& !StringUtils.equals(skuForeignNameList[i], "null")) {
					sb.append(" [").append(remarkList[i]).append("*").append(skuForeignNameList[i]).append(" * ")
							.append(skuCountList[i]).append("]");
				} else {
					sb.append(" [").append(remarkList[i]).append("*").append(skuNameList[i]).append(" * ")
							.append(skuCountList[i]).append("]");
				}
			}
			
		}
		return sb.toString().substring(1);
	}

	public String getDiscodeStr(String codeStr) {
		String discode = null;
		List<String> codeList = Arrays.asList(codeStr.split(" "));
		if (CollectionUtils.isNotEmpty(codeList)) {
			discode = codeList.get(0);
		}
		if (StringUtils.isNotBlank(discode)) {
			return discode;
		} else {
			return codeStr;
		}
	}

}