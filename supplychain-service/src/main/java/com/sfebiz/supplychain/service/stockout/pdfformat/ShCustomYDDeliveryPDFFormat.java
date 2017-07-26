package com.sfebiz.supplychain.service.stockout.pdfformat;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
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
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.NumberUtil;

import net.pocrd.entity.ServiceException;

/**
 * 韵达面单打印模板
 * 
 * @author matt
 * @version $Id: RhfKoreaCustomYDDeliveryPDFFormat.java, v 0.1 2016年6月14日
 *          下午2:58:07 matt Exp $
 */
@Component("shCustomYDDeliveryPDFFormat")
public class ShCustomYDDeliveryPDFFormat extends AbstractPDFFormat {

	/** 日志 */
	
	private static final Logger logger = LoggerFactory
			.getLogger(ShCustomYDDeliveryPDFFormat.class);

	/** 字体文件路径 */
	private static final String simheiFontPath = "/home/admin/font/AdobeSongStd-Light.otf";

	@Override
	public PdfPTable buildPdfBill(ExportedShipOrderDO exportedShipOrderDO,
			WarehouseDO warehouseDO, LogisticsLineDO logisticsLineDO, PdfContentByte cb)
			throws ServiceException {

		try {

			// 1. 绘制准备
			DateUtil dateUtil = new DateUtil();

			BaseFont bfChinese = BaseFont.createFont(simheiFontPath,
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			BaseFont bfSouthKorea = BaseFont.createFont("HYGoThic-Medium",
					"UniKS-UCS2-H", BaseFont.NOT_EMBEDDED);
			BaseFont bfJapan = BaseFont.createFont("KozMinPro-Regular",
					"UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED);

			// 1.2 创建基础字体
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
			Font hf_32 = new Font(bfChinese, 30);
			Font hf_32_B = new Font(bfChinese, 30, Font.BOLD);
			Font fontChina = new Font(bfChinese, 6, Font.BOLD);
			Font fontSouthKorea = new Font(bfSouthKorea, 6, Font.BOLD);
			Font fontJapan = new Font(bfJapan, 6, Font.BOLD);

			// 1.3. 绘制面单的整体结构
			PdfPTable baseTable = new PdfPTable(1);
			baseTable.setWidths(new int[] { 100 });
			baseTable.setWidthPercentage(100);

			// 2. 填充表格内容
			// 2.1. 第一行
			/**************************************** 第一行 ********************************************/
			PdfPCell base_cell_1 = new PdfPCell();
			base_cell_1.setBorder(0);
			base_cell_1.setBorderWidthBottom(1f);
			base_cell_1.setFixedHeight(NumberUtil.parseMmToPt(54.026f));

			PdfPTable base_inner_t1 = new PdfPTable(1);
			base_inner_t1.setWidthPercentage(100);

			// 2.1.1. 区块1
			PdfPCell cell_t1_c1 = new PdfPCell();
			cell_t1_c1.setBorder(0);
			cell_t1_c1.setFixedHeight(NumberUtil.parseMmToPt(22f));
			PdfPTable cell_t1_c1_t = new PdfPTable(2);
			cell_t1_c1_t.setWidths(new int[] { 75, 25 });
			cell_t1_c1_t.setWidthPercentage(100);
			PdfPCell cell_t1_c1_t_c3 = new PdfPCell();
			cell_t1_c1_t_c3.setBorder(0);
			cell_t1_c1_t_c3.addElement(new Paragraph("寄件人:"
					+ exportedShipOrderDO.getSenderName(), hf_10_B));
			cell_t1_c1_t.addCell(cell_t1_c1_t_c3);

			PdfPCell cell_t1_c1_t_c2 = new PdfPCell();
			cell_t1_c1_t_c2.setBorder(0);
			cell_t1_c1_t_c2.setRowspan(4);
			cell_t1_c1_t_c2.addElement(new Paragraph(
					getLogisticsWeigth(exportedShipOrderDO) + "kg", hf_12_B));
			cell_t1_c1_t_c2.addElement(new Phrase(dateUtil
					.defFormatDateStr(new Date()), hf_10));
			cell_t1_c1_t_c2.addElement(new Phrase("国际件", hf_12_B));
			cell_t1_c1_t.addCell(cell_t1_c1_t_c2);

			PdfPCell cell_t1_c1_t_c4 = new PdfPCell();
			cell_t1_c1_t_c4.setBorder(0);
			cell_t1_c1_t_c4.addElement(new Paragraph("寄件人电话:"
					+ exportedShipOrderDO.getSenderTelephone(), hf_10_B));
			cell_t1_c1_t.addCell(cell_t1_c1_t_c4);

			PdfPCell cell_t1_c1_t_c5 = new PdfPCell();
			cell_t1_c1_t_c5.setBorder(0);
			cell_t1_c1_t_c5.addElement(new Paragraph("寄件公司:"
					+ exportedShipOrderDO.getSenderName(), hf_10_B));
			cell_t1_c1_t.addCell(cell_t1_c1_t_c5);

			PdfPCell cell_t1_c1_t_c6 = new PdfPCell();
			cell_t1_c1_t_c6.setBorder(0);
			cell_t1_c1_t_c6.addElement(new Paragraph("寄件人地址:"
					+ exportedShipOrderDO.getSenderAddress(), hf_10_B));
			cell_t1_c1_t.addCell(cell_t1_c1_t_c6);

			cell_t1_c1.addElement(cell_t1_c1_t);
			base_inner_t1.addCell(cell_t1_c1);

			// 2.1.2. 区块2,3
			PdfPCell cell_t1_c2 = new PdfPCell();
			cell_t1_c2.setBorder(0);
			PdfPTable table = new PdfPTable(2);
			table.setWidths(new int[] { 12, 88 });
			table.setWidthPercentage(100);
			PdfPCell cell1_4 = new PdfPCell();
			cell1_4.addElement(new Paragraph("送达", hf_12_B));
			cell1_4.addElement(new Paragraph("地址:", hf_12_B));
			cell1_4.setRowspan(4);
			cell1_4.setBorder(0);
			cell1_4.setPaddingTop(10);
			cell1_4.setPaddingLeft(5);
			cell1_4.setVerticalAlignment(1);
			table.addCell(cell1_4);
			PdfPCell cell1 = new PdfPCell(new Phrase("收件人："
					+ exportedShipOrderDO.getBuyerName(), hf_10_B));
			cell1.setBorder(0);
			table.addCell(cell1);
			PdfPCell cell2 = new PdfPCell(new Phrase("收件人电话："
					+ exportedShipOrderDO.getBuyerTelephone(), hf_10_B));
			cell2.setBorder(0);
			table.addCell(cell2);
			PdfPCell cell3 = new PdfPCell(new Phrase("收件公司：", hf_10_B));
			cell3.setBorder(0);
			table.addCell(cell3);
			PdfPCell cell4 = new PdfPCell(new Phrase("收件人地址："
					+ exportedShipOrderDO.getBuyerAddress(), hf_10_B));
			cell4.setBorder(0);
			table.addCell(cell4);
			cell_t1_c2.addElement(table);
			base_inner_t1.addCell(cell_t1_c2);

			// 2.1.3. 区块4,5
			/*
			 * ColumnText ct = new ColumnText(cb); ct.setSimpleColumn( new
			 * Paragraph(NumberUtil .defaultParseWeightG2KG(exportedShipOrderDO
			 * .getLogisticsWeight()) + "kg", hf_12_B),
			 * NumberUtil.parseMmToPt(0), NumberUtil.parseMmToPt(0),
			 * NumberUtil.parseMmToPt(68), NumberUtil.parseMmToPt(197), 0,
			 * Element.ALIGN_RIGHT); ct.go(); ct.setSimpleColumn(new Phrase(
			 * dateUtil.defFormatDateStr(new Date()), hf_10), NumberUtil
			 * .parseMmToPt(0), NumberUtil.parseMmToPt(0), NumberUtil
			 * .parseMmToPt(92), NumberUtil.parseMmToPt(194), 0,
			 * Element.ALIGN_RIGHT); ct.go(); ct.setSimpleColumn(new
			 * Phrase("国际件", hf_12_B), NumberUtil.parseMmToPt(0),
			 * NumberUtil.parseMmToPt(0), NumberUtil.parseMmToPt(80),
			 * NumberUtil.parseMmToPt(190), 0, Element.ALIGN_RIGHT); ct.go();
			 */

			// 2.1.4. 区块6,7
			PdfPCell cell_t1_c3 = new PdfPCell();
			cell_t1_c3.setBorder(0);
			PdfPTable table67 = new PdfPTable(2);
			table67.setWidths(new int[] { 50, 40 });
			table67.setWidthPercentage(100);
			PdfPCell table67_cell1 = new PdfPCell();
			table67_cell1.addElement(new Paragraph(getValueFromDestcode(
					exportedShipOrderDO.getDestcode(), 2), hf_14_B));
			table67_cell1.setBorder(0);
			table67.addCell(table67_cell1);

			PdfPCell table67_cell2 = new PdfPCell();
			Barcode128 code128 = new Barcode128();
			code128.setCode(getValueFromDestcode(
					exportedShipOrderDO.getDestcode(), 3));
			code128.setCodeType(Barcode128.CODE128);
			code128.setX(1.2f);
			Image code128Image = code128.createImageWithBarcode(cb, null, null);
			code128Image.setAlignment(Element.ALIGN_CENTER);
			table67_cell2.addElement(code128Image);
			table67_cell2.setBorder(0);
			table67.addCell(table67_cell2);
			cell_t1_c3.addElement(table67);
			base_inner_t1.addCell(cell_t1_c3);

			base_cell_1.addElement(base_inner_t1);
			baseTable.addCell(base_cell_1);

			// 3. 第二行
			/**************************************** 第二行 ********************************************/
			PdfPCell base_cell_2 = new PdfPCell();
			base_cell_2.setBorder(0);
			base_cell_2.setBorderWidthBottom(1f);
			base_cell_2.setFixedHeight(NumberUtil.parseMmToPt(22.678f));

			PdfPTable base_cell_2_t = new PdfPTable(2);
			base_cell_2_t.setWidths(new int[] { 24, 76 });
			base_cell_2_t.setWidthPercentage(100);

			// 区块8
			PdfPCell base_cell_2_t_c1 = new PdfPCell();
			base_cell_2_t_c1.setBorder(0);
			base_cell_2_t_c1.setBorderWidthRight(1);

			Image xjaNewLogo = xjaLogo;
			xjaNewLogo.scaleAbsolute(NumberUtil.parseMmToPt(60.018f),NumberUtil.parseMmToPt(60.018f));
			base_cell_2_t_c1.addElement(xjaNewLogo);
			base_cell_2_t.addCell(base_cell_2_t_c1);

			PdfPCell base_cell_2_t_c2 = new PdfPCell();
			base_cell_2_t_c2.setBorder(0);

			// 区块9,10
			Paragraph da_bi_tou_p = new Paragraph(getValueFromDestcode(
					exportedShipOrderDO.getDestcode(), 0), hf_32_B);
			da_bi_tou_p.setLeading(25);
			da_bi_tou_p.setAlignment(Element.ALIGN_CENTER);
			base_cell_2_t_c2.addElement(da_bi_tou_p);
			Paragraph da_bi_tou_r = new Paragraph(getValueFromDestcode(
					exportedShipOrderDO.getDestcode(), 1), hf_32_B);
			da_bi_tou_r.setLeading(25);
			da_bi_tou_r.setAlignment(Element.ALIGN_CENTER);
			base_cell_2_t_c2.addElement(da_bi_tou_r);

			base_cell_2_t.addCell(base_cell_2_t_c2);

			base_cell_2.addElement(base_cell_2_t);
			baseTable.addCell(base_cell_2);

			// 4. 第三行
			/**************************************** 第三行 ********************************************/
			PdfPCell base_cell_3 = new PdfPCell();
			base_cell_3.setBorder(0);
			base_cell_3.setBorderWidthBottom(1f);
			base_cell_3.setFixedHeight(NumberUtil.parseMmToPt(18.915f));
			// base_cell_3.setHorizontalAlignment(Element.ALIGN_CENTER);
			base_cell_3.setUseAscender(true);
			base_cell_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			// 区块11
			Barcode128 wayBill_code128 = new Barcode128();
			wayBill_code128.setFont(null);
			wayBill_code128.setCode(exportedShipOrderDO.getIntrMailNo());
			wayBill_code128.setCodeType(Barcode128.CODE128);
			Image wayBillImage = wayBill_code128.createImageWithBarcode(cb,
					null, null);
			wayBillImage.setAlignment(Element.ALIGN_CENTER);
			wayBillImage.scaleAbsolute(NumberUtil.parseMmToPt(50),
					NumberUtil.parseMmToPt(8));
			base_cell_3.addElement(wayBillImage);

			baseTable.addCell(base_cell_3);

			// 5. 第四行
			/**************************************** 第四行 ********************************************/
			PdfPCell base_cell_4 = new PdfPCell();
			base_cell_4.setBorder(0);
			base_cell_4.setBorderWidthBottom(1f);
			base_cell_4.setFixedHeight(NumberUtil.parseMmToPt(8.81f));

			PdfPTable base_cell_4_t = new PdfPTable(2);
			base_cell_4_t.setWidths(new int[] { 24, 76 });
			base_cell_4_t.setWidthPercentage(100);
			PdfPCell base_cell_4_t_c1 = new PdfPCell();
			base_cell_4_t_c1.setBorder(0);
			base_cell_4_t_c1.addElement(new Paragraph("运单编号", hf_12));
			base_cell_4_t.addCell(base_cell_4_t_c1);
			PdfPCell base_cell_4_t_c2 = new PdfPCell();
			base_cell_4_t_c2.setBorder(0);
			Paragraph cell_4_t_c2_n = new Paragraph(
					exportedShipOrderDO.getIntrMailNo(), hf_12_B);
			cell_4_t_c2_n.setAlignment(Element.ALIGN_LEFT);
			base_cell_4_t_c2.addElement(cell_4_t_c2_n);
			base_cell_4_t.addCell(base_cell_4_t_c2);

			base_cell_4.addElement(base_cell_4_t);
			baseTable.addCell(base_cell_4);

			// 6. 第五行
			/**************************************** 第五行 ********************************************/
			PdfPCell base_cell_5 = new PdfPCell();
			base_cell_5.setBorder(0);
			base_cell_5.setBorderWidthBottom(1f);
			base_cell_5.setFixedHeight(NumberUtil.parseMmToPt(9.80f));

			PdfPTable base_cell_5_t = new PdfPTable(2);
			base_cell_5_t.setWidths(new int[] { 50, 50 });
			base_cell_5_t.setWidthPercentage(100);
			PdfPCell base_cell_5_t_c1 = new PdfPCell();
			base_cell_5_t_c1.setBorder(0);
			base_cell_5_t_c1.addElement(new Paragraph("收件人/代签人：", hf_10));
			base_cell_5_t.addCell(base_cell_5_t_c1);
			PdfPCell base_cell_5_t_c2 = new PdfPCell();
			base_cell_5_t_c2.setBorder(0);
			Paragraph cell_5_t_c2_n = new Paragraph(
					"签收时间：      年      月      日", hf_10);
			cell_5_t_c2_n.setAlignment(Element.ALIGN_RIGHT);
			base_cell_5_t_c2.addElement(cell_5_t_c2_n);
			base_cell_5_t.addCell(base_cell_5_t_c2);

			base_cell_5.addElement(base_cell_5_t);
			baseTable.addCell(base_cell_5);

			// 6. 第六行
			/**************************************** 第六行 ********************************************/
			PdfPCell base_cell_6 = new PdfPCell();
			base_cell_6.setBorder(0);
			base_cell_6.setBorderWidthBottom(1f);
			base_cell_6.setFixedHeight(NumberUtil.parseMmToPt(60.012f));

			PdfPTable base_cell_6_t = new PdfPTable(2);
			base_cell_6_t.setWidths(new int[] { 40, 60 });
			base_cell_6_t.setWidthPercentage(100);

			PdfPCell base_cell_6_t_c1 = new PdfPCell();
			base_cell_6_t_c1.setBorder(0);
			base_cell_6_t_c1.addElement(new Paragraph("托寄物品简述：", hf_10_B));
			base_cell_6_t.addCell(base_cell_6_t_c1);

			PdfPCell base_cell_6_t_c2 = new PdfPCell();
			base_cell_6_t_c2.setBorder(0);
			base_cell_6_t_c2.addElement(new Paragraph("订单编号："
					+ exportedShipOrderDO.getBizId(), hf_10_B));
			base_cell_6_t.addCell(base_cell_6_t_c2);

			PdfPCell base_cell_6_t_c3 = new PdfPCell();
			base_cell_6_t_c3.setBorder(0);
			base_cell_6_t_c3.setColspan(2);
			// 绘制商品列表
			PdfPTable product_t = new PdfPTable(4);
			product_t.setWidths(new int[] { 60, 10, 10, 20 });
			product_t.setWidthPercentage(100);
			PdfPCell product_t_c1 = new PdfPCell();
			product_t_c1.setBorder(0);
			product_t_c1.addElement(new Paragraph("托寄物品", hf_8_B));
			product_t.addCell(product_t_c1);
			PdfPCell product_t_c2 = new PdfPCell();
			product_t_c2.setBorder(0);
			product_t_c2.addElement(new Paragraph("SKU", hf_8_B));
			product_t.addCell(product_t_c2);
			PdfPCell product_t_c3 = new PdfPCell();
			product_t_c3.setBorder(0);
			product_t_c3.addElement(new Paragraph("数量", hf_8_B));
			product_t.addCell(product_t_c3);
			PdfPCell product_t_c4 = new PdfPCell();
			product_t_c4.setBorder(0);
			product_t_c4.addElement(new Paragraph("备注", hf_8_B));
			product_t.addCell(product_t_c4);
			// 判断字体
			Font productFont = null;
			if (StringUtils.equalsIgnoreCase("KR", warehouseDO.getCountry())
					|| StringUtils.equals("韩国", warehouseDO.getCountry())
					|| StringUtils.equalsIgnoreCase("Korea",
							warehouseDO.getCountry())) {
				productFont = fontSouthKorea;
			} else if (StringUtils.equalsIgnoreCase("JP",
					warehouseDO.getCountry())
					|| StringUtils.equals("日本", warehouseDO.getCountry())
					|| StringUtils.equalsIgnoreCase("Japan",
							warehouseDO.getCountry())) {
				productFont = fontJapan;
			} else {
				productFont = fontChina;
			}
			createYDProductTableCell(product_t, productFont,
					exportedShipOrderDO, warehouseDO.getCountry());
			base_cell_6_t_c3.addElement(product_t);
			base_cell_6_t.addCell(base_cell_6_t_c3);
			
			PdfPCell base_cell_6_t_c4 = new PdfPCell();
			base_cell_6_t_c4.setBorder(0);
			base_cell_6_t_c4.setColspan(2);
			Barcode128 cell_6_t_c6_code128 = new Barcode128();
			cell_6_t_c6_code128.setCode(exportedShipOrderDO.getIntrMailNo());
			cell_6_t_c6_code128.setCodeType(Barcode128.CODE128);
			cell_6_t_c6_code128.setX(1f);
			Image cell_6_t_c6_Image = cell_6_t_c6_code128
					.createImageWithBarcode(cb, null, null);
			cell_6_t_c6_Image.setAlignment(Element.ALIGN_CENTER);
			base_cell_6_t_c4.addElement(cell_6_t_c6_Image);
			base_cell_6_t.addCell(base_cell_6_t_c4);

			base_cell_6.addElement(base_cell_6_t);
			baseTable.addCell(base_cell_6);
			
			// 7. 第七行
			/**************************************** 第七行 ********************************************/
			PdfPCell base_cell_7 = new PdfPCell();
			base_cell_7.setBorder(0);
			base_cell_7.setFixedHeight(NumberUtil.parseMmToPt(27.234f));

			PdfPTable base_cell_7_t = new PdfPTable(2);
			base_cell_7_t.setWidths(new int[] { 50, 50 });
			base_cell_7_t.setWidthPercentage(100);

			PdfPCell base_cell_7_t_c1 = new PdfPCell();
			base_cell_7_t_c1.setBorder(0);
			base_cell_7_t_c1.addElement(new Paragraph("寄件人:"
					+ exportedShipOrderDO.getSenderName(), hf_9_B));
			base_cell_7_t_c1.addElement(new Paragraph("寄件人电话:"
					+ exportedShipOrderDO.getSenderTelephone(), hf_9_B));
			base_cell_7_t_c1.addElement(new Paragraph("寄件公司:"
					+ exportedShipOrderDO.getSenderName(), hf_9_B));
			base_cell_7_t_c1.addElement(new Paragraph("寄件人地址:"
					+ exportedShipOrderDO.getSenderAddress(), hf_9_B));
			base_cell_7_t.addCell(base_cell_7_t_c1);

			/*
			 * PdfPCell base_cell_7_t_c2 = new PdfPCell();
			 * base_cell_7_t_c2.setBorder(0);
			 * base_cell_7_t_c2.setBorderWidthLeft(1);
			 * base_cell_7_t_c2.setRowspan(2);
			 * base_cell_7_t.addCell(base_cell_7_t_c2);
			 */

			PdfPCell base_cell_7_t_c3 = new PdfPCell();
			base_cell_7_t_c3.setBorder(0);
			base_cell_7_t_c3.setPaddingLeft(10f);
			base_cell_7_t_c3.addElement(new Paragraph("收件人:"
					+ exportedShipOrderDO.getBuyerName(), hf_9_B));
			base_cell_7_t_c3.addElement(new Paragraph("收件人电话:"
					+ exportedShipOrderDO.getBuyerTelephone(), hf_9_B));
			base_cell_7_t_c3.addElement(new Paragraph("收件公司:", hf_9_B));
			base_cell_7_t_c3.addElement(new Paragraph("收件人地址:"
					+ exportedShipOrderDO.getBuyerAddress(), hf_9_B));
			base_cell_7_t.addCell(base_cell_7_t_c3);

			base_cell_7.addElement(base_cell_7_t);
			baseTable.addCell(base_cell_7);

/*			// 8. 第八行
			PdfPCell base_cell_8_t = new PdfPCell(xjaLogo,true);
			base_cell_8_t.setHorizontalAlignment(Element.ALIGN_RIGHT);// 设置内容水平居右显示
			base_cell_8_t.setVerticalAlignment(Element.ALIGN_RIGHT);  // 设置垂直居中
			base_cell_8_t.setUseBorderPadding(true);
			base_cell_8_t.setPaddingLeft(20);
			base_cell_8_t.setBorder(Rectangle.NO_BORDER);
			baseTable.addCell(base_cell_8_t);*/

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
		return PdfTemplateType.SH_YUNDA.getValue();
	}

	/**
	 * 构造韵达商品列表
	 * 
	 * @param pTable
	 * @param font
	 */
	private void createYDProductTableCell(PdfPTable pTable, Font font,
			ExportedShipOrderDO exportedShipOrderDO, String country) {

		String[] skuIdList = exportedShipOrderDO.getSkuIdStr().split(SEPARATOR);
		String[] skuNameList = exportedShipOrderDO.getSkuNameStr().split(SEPARATOR);
		String[] skuForeignNameList = exportedShipOrderDO
				.getSkuForeignNameStr().split(SEPARATOR);
		String[] skuCountList = exportedShipOrderDO.getSkuCountStr().split(
				SEPARATOR);
		String[] remarkList = exportedShipOrderDO.getRemarks().split(SEPARATOR);

		int maxIndex = skuIdList.length - 1;
		// 6行占位
		for (int i = 0; i < 6; i++) {

			if (i <= maxIndex) {
				PdfPCell c1 = new PdfPCell();
				c1.setBorder(0);
				if("台湾省".contains(country)){
					c1.addElement(new Paragraph(skuNameList[i], font));
				}else{
					c1.addElement(new Paragraph(skuForeignNameList[i], font));
				}
				
				pTable.addCell(c1);
				PdfPCell c2 = new PdfPCell();
				c2.setBorder(0);
				c2.addElement(new Paragraph(String.valueOf(skuIdList[i]), font));
				pTable.addCell(c2);
				PdfPCell c3 = new PdfPCell();
				c3.setBorder(0);
				c3.addElement(new Paragraph(String.valueOf(skuCountList[i]),
						font));
				pTable.addCell(c3);
				PdfPCell c4 = new PdfPCell();
				c4.setBorder(0);
				c4.addElement(new Paragraph(remarkList[i], font));
				pTable.addCell(c4);
			} else {
				PdfPCell c1 = new PdfPCell();
				c1.setBorder(0);
				c1.addElement(new Paragraph(" ", font));
				pTable.addCell(c1);
				PdfPCell c2 = new PdfPCell();
				c2.setBorder(0);
				c2.addElement(new Paragraph(" ", font));
				pTable.addCell(c2);
				PdfPCell c3 = new PdfPCell();
				c3.setBorder(0);
				c3.addElement(new Paragraph(" ", font));
				pTable.addCell(c3);
				PdfPCell c4 = new PdfPCell();
				c4.setBorder(0);
				c4.addElement(new Paragraph(" ", font));
				pTable.addCell(c4);
			}
		}
	}

	/**
	 * 获取destcode的值
	 * 
	 * @param destcode
	 * @param index
	 * @return
	 */
	private String getValueFromDestcode(String destcode, int index) {
		String valueInCode = StringUtils.EMPTY;
		if (StringUtils.isBlank(destcode)) {
			return valueInCode;
		}
		if (index < -1 || index > 4) {
			return valueInCode;
		}
		String[] values = destcode.split("\\|", -1);
		if (values.length == 4) {
			valueInCode = values[index];
		}
		return valueInCode;
	}

	private String getLogisticsWeigth(ExportedShipOrderDO exportedShipOrderDO) {
		String[] weightList = exportedShipOrderDO.getSkuWeightStr().split(
				SEPARATOR);
		BigDecimal logisticsWeightValue = new BigDecimal(0);
		for (String weight : weightList) {
			logisticsWeightValue.add(new BigDecimal(weight));
		}
		return logisticsWeightValue.setScale(3).toString();
	}
}