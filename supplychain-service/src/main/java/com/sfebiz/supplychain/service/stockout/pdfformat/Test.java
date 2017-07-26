package com.sfebiz.supplychain.service.stockout.pdfformat;

import java.io.FileOutputStream;
import java.net.URL;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sfebiz.supplychain.util.NumberUtil;

import net.pocrd.entity.ServiceException;

public class Test {
	
	public void buildPdfBill() throws ServiceException {

		try {
			Image ytLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/vendor/ytLogo.png"));
			
			Image gaoJieytLogo = Image.getInstance(new URL("http://haitao-oss-bucket-public.oss-cn-hangzhou.aliyuncs.com/vendor/gjytLogo.jpg"));
			
			Document document = new Document(PageSize.A5, 5f, 5f, 10f, 10f);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("D:\\111.pdf"));
			document.open();
			PdfContentByte cb = pdfWriter.getDirectContent();
			// 1.1 绘制准备
			BaseFont bf = BaseFont.createFont("D:\\msjh.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			// 1.2 创建基础字体
			Font hf_6 = new Font(bf, 6);
			Font hf_6_B = new Font(bf, 6, Font.BOLD);
			Font hf_7 = new Font(bf, 7);
			Font hf_7_B = new Font(bf, 7, Font.BOLD);
			Font hf_8 = new Font(bf, 8);
			Font hf_8_B = new Font(bf, 8, Font.BOLD);
			Font hf_9 = new Font(bf, 9);
			Font hf_9_B = new Font(bf, 9, Font.BOLD);
			Font hf_10 = new Font(bf, 10);
			Font hf_10_B = new Font(bf, 10, Font.BOLD);
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

			String[] skuNameList = "sp1######sp2######sp3######sp4".split("######");
			String[] skuCountList = "2######4######5######8".split("######");
			int count = 0;
			
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
			
			Paragraph cityP = new Paragraph("010", hf_32_B); 
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
			base_cell_2.setFixedHeight(NumberUtil.parseMmToPt(17f));
			Barcode128 mailNo_code128 = new Barcode128();
			mailNo_code128.setFont(bf);
			mailNo_code128.setCode("12345678919"); // 运单号
			mailNo_code128.setCodeType(Barcode128.CODE128);
			Image mailNoImage = mailNo_code128.createImageWithBarcode(cb, null, null);
			mailNoImage.setAlignment(Element.ALIGN_CENTER);
			mailNoImage.scaleAbsolute(120, 50);
			base_cell_2.addElement(mailNoImage);
			baseTable.addCell(base_cell_2);
			/**************************************** 第三行 ********************************************/
			PdfPCell base_cell_3 = new PdfPCell();
			base_cell_3.setBorder(0);
			base_cell_3.setBorderWidthBottom(0);
			base_cell_3.setVerticalAlignment(Element.ALIGN_CENTER);
			base_cell_3.setHorizontalAlignment(Element.ALIGN_CENTER);
			base_cell_3.setFixedHeight(NumberUtil.parseMmToPt(24));
			PdfPTable cell_3_table = new PdfPTable(3);
			cell_3_table.setWidths(new int[] { 5, 91, 4 });
			cell_3_table.setWidthPercentage(100);
			PdfPCell cell_3_table_cell_1 = new PdfPCell();
			cell_3_table_cell_1.setBorder(0);
			cell_3_table_cell_1.setBorderWidthRight(1);
			cell_3_table_cell_1.setBorderWidthBottom(1);
			cell_3_table_cell_1.setFixedHeight(NumberUtil.parseMmToPt(24));
			cell_3_table_cell_1.setUseAscender(true);
			cell_3_table_cell_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Paragraph p_shou = new Paragraph("收件人", hf_9_B);
			p_shou.setAlignment(1);
			cell_3_table_cell_1.addElement(p_shou);
			cell_3_table.addCell(cell_3_table_cell_1);
			PdfPCell cell_3_table_cell_2 = new PdfPCell();
			cell_3_table_cell_2.setBorder(0);
			cell_3_table_cell_2.setBorderWidthRight(1);
			cell_3_table_cell_2.setBorderWidthBottom(1);
			cell_3_table_cell_2.setFixedHeight(NumberUtil.parseMmToPt(24));
			cell_3_table_cell_2.setUseAscender(true);
			Paragraph revcName = new Paragraph("收件人：楊飛龍", hf_9_B);
			cell_3_table_cell_2.addElement(revcName);
			revcName = new Paragraph("电话：11111111", hf_9_B);
			cell_3_table_cell_2.addElement(revcName);
			revcName = new Paragraph("地址：大渡河路", hf_9_B);
			cell_3_table_cell_2.addElement(revcName);
			PdfPCell cell_3_table_cell_3 = new PdfPCell();
			cell_3_table_cell_3.setBorder(0);
			cell_3_table_cell_3.setRowspan(2);
			cell_3_table_cell_3.setBorderWidthBottom(0);
			cell_3_table_cell_3.setFixedHeight(NumberUtil.parseMmToPt(24));
			cell_3_table_cell_3.setUseAscender(true);
			cell_3_table_cell_3.setVerticalAlignment(Element.ALIGN_BOTTOM);
			Paragraph revcName_1 = new Paragraph("签           收", hf_9);
			cell_3_table_cell_3.addElement(revcName_1);
			cell_3_table.addCell(cell_3_table_cell_2);
			cell_3_table.addCell(cell_3_table_cell_3);
			base_cell_3.addElement(cell_3_table);
			baseTable.addCell(base_cell_3);
			/**************************************** 第四行 ********************************************/
			PdfPCell base_cell_4 = new PdfPCell();
			base_cell_4.setBorder(0);
			base_cell_4.setBorderWidthBottom(1);
			base_cell_4.setVerticalAlignment(Element.ALIGN_TOP);
			base_cell_4.setHorizontalAlignment(Element.ALIGN_LEFT);
			base_cell_4.setFixedHeight(NumberUtil.parseMmToPt(18));
			PdfPTable cell_4_table = new PdfPTable(3);
			cell_4_table.setWidths(new int[] { 5, 91, 4 });
			cell_4_table.setWidthPercentage(100);
			PdfPCell cell_4_table_cell_1 = new PdfPCell();
			cell_4_table_cell_1.setBorder(0);
			cell_4_table_cell_1.setBorderWidthRight(1);
			cell_4_table_cell_1.setFixedHeight(NumberUtil.parseMmToPt(18));
			cell_4_table_cell_1.setUseAscender(true);
			cell_4_table_cell_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Paragraph p_ji = new Paragraph("寄件人", hf_9);
			p_ji.setAlignment(1);
			cell_4_table_cell_1.addElement(p_ji);
			cell_4_table.addCell(cell_4_table_cell_1);
			PdfPCell cell_4_table_cell_2 = new PdfPCell();
			cell_4_table_cell_2.setBorder(0);
			cell_4_table_cell_2.setBorderWidthRight(1);
			cell_4_table_cell_2.setFixedHeight(NumberUtil.parseMmToPt(18));
			cell_4_table_cell_2.setUseAscender(true);
			Paragraph senderName = new Paragraph("发件人：楊飛龍   " + "电话：111111111", hf_9);
			cell_4_table_cell_2.addElement(senderName);
			Paragraph senderAddress = new Paragraph("地址：大渡河路", hf_9);
			cell_4_table_cell_2.addElement(senderAddress);
			PdfPCell cell_4_table_cell_3 = new PdfPCell();
			cell_4_table_cell_3.setBorder(0);
			cell_4_table_cell_3.setFixedHeight(NumberUtil.parseMmToPt(18));
			cell_4_table_cell_3.setUseAscender(true);
			cell_4_table_cell_3.setVerticalAlignment(Element.ALIGN_TOP);
			Paragraph senderName_1 = new Paragraph("联", hf_9);
			cell_4_table_cell_3.addElement(senderName_1);
			cell_4_table.addCell(cell_4_table_cell_2);
			cell_4_table.addCell(cell_4_table_cell_3);
			base_cell_4.addElement(cell_4_table);
			baseTable.addCell(base_cell_4);
			/**************************************** 第五行 ********************************************/
			PdfPCell base_cell_5 = new PdfPCell();
			base_cell_5.setBorder(0);
			base_cell_5.setBorderWidthBottom(1);
			base_cell_5.setVerticalAlignment(Element.ALIGN_TOP);
			base_cell_5.setHorizontalAlignment(Element.ALIGN_LEFT);
			base_cell_5.setFixedHeight(NumberUtil.parseMmToPt(16));
			PdfPTable cell_5_table = new PdfPTable(3);
			cell_5_table.setWidths(new int[] { 47, 25, 28 });
			cell_5_table.setWidthPercentage(100);
			PdfPCell cell_5_table_cell_1 = new PdfPCell();
			cell_5_table_cell_1.setBorder(0);
			cell_5_table_cell_1.setBorderWidthRight(1);
			cell_5_table_cell_1.setFixedHeight(NumberUtil.parseMmToPt(17));
			cell_5_table_cell_1.setUseAscender(true);
			cell_5_table_cell_1.setVerticalAlignment(Element.ALIGN_TOP);
			cell_5_table_cell_1.setVerticalAlignment(Element.ALIGN_LEFT);
			cell_5_table_cell_1.setUseAscender(true);
			Paragraph p_recev = new Paragraph("收件人/代收人:", hf_9);
			cell_5_table_cell_1.addElement(p_recev);
			cell_5_table.addCell(cell_5_table_cell_1);
			PdfPCell cell_5_table_cell_2 = new PdfPCell();
			cell_5_table_cell_2.setBorder(0);
			cell_5_table_cell_2.setFixedHeight(NumberUtil.parseMmToPt(17));
			cell_5_table_cell_2.setUseAscender(true);
			Paragraph receDate = new Paragraph("签收时间:", hf_9);
			cell_5_table_cell_2.addElement(receDate);
			PdfPCell cell_5_table_cell_3 = new PdfPCell();
			cell_5_table_cell_3.setBorder(0);
			cell_5_table_cell_3.setFixedHeight(NumberUtil.parseMmToPt(17));
			cell_5_table_cell_3.setUseAscender(true);
			cell_5_table_cell_3.setVerticalAlignment(Element.ALIGN_BOTTOM);
			cell_5_table_cell_3.setHorizontalAlignment(Element.ALIGN_RIGHT);
			Paragraph receDate_ = new Paragraph("年    月     日", hf_9);
			cell_5_table_cell_3.addElement(receDate_);
			cell_5_table.addCell(cell_5_table_cell_2);
			cell_5_table.addCell(cell_5_table_cell_3);
			base_cell_5.addElement(cell_5_table);
			baseTable.addCell(base_cell_5);
			/**************************************** 第六行 ********************************************/
			PdfPCell base_cell_6 = new PdfPCell();
			base_cell_6.setBorder(0);
			base_cell_6.setBorderWidthBottom(1);
			base_cell_6.setUseAscender(true);
			base_cell_6.setVerticalAlignment(Element.ALIGN_CENTER);
			base_cell_6.setHorizontalAlignment(Element.ALIGN_CENTER);
			base_cell_6.setFixedHeight(NumberUtil.parseMmToPt(14));
			PdfPTable cell_6_table = new PdfPTable(3);
			cell_6_table.setWidths(new int[] { 40, 20, 40 });
			cell_6_table.setWidthPercentage(100);
			PdfPCell cell_6_table_cell_1 = new PdfPCell();
			cell_6_table_cell_1.setBorder(0);
			cell_6_table_cell_1.setFixedHeight(NumberUtil.parseMmToPt(14));
			cell_6_table_cell_1.setUseAscender(true);
			ytLogo.scalePercent(60);
			cell_6_table_cell_1.addElement(ytLogo);
			cell_6_table.addCell(cell_6_table_cell_1);
			PdfPCell cell_6_table_cell_2 = new PdfPCell();
			cell_6_table_cell_2.setBorder(0);
			cell_6_table_cell_2.setFixedHeight(NumberUtil.parseMmToPt(14));
			cell_6_table_cell_2.setUseAscender(true);
			gaoJieytLogo.scalePercent(50);
			cell_6_table_cell_2.addElement(gaoJieytLogo);
			cell_6_table.addCell(cell_6_table_cell_2);
			PdfPCell cell_6_table_cell_3 = new PdfPCell();
			cell_6_table_cell_3.setBorder(0);
			cell_6_table_cell_3.setFixedHeight(NumberUtil.parseMmToPt(14));
			Barcode128 minMailNo_code128 = new Barcode128();
			minMailNo_code128.setFont(bf);
			minMailNo_code128.setCode("12345678919");
			minMailNo_code128.setCodeType(Barcode128.CODE128);
			Image minMailNoImage = minMailNo_code128.createImageWithBarcode(cb, null, null);
			minMailNoImage.setAlignment(Element.ALIGN_LEFT);
			minMailNoImage.scaleAbsolute(190, 20);
			cell_6_table_cell_3.addElement(minMailNoImage);
			cell_6_table.addCell(cell_6_table_cell_3);
			base_cell_6.addElement(cell_6_table);
			baseTable.addCell(base_cell_6);
			/**************************************** 第七行 ********************************************/
			PdfPCell base_cell_7 = new PdfPCell();
			base_cell_7.setBorder(0);
			base_cell_7.setBorderWidthBottom(1);
			base_cell_7.setUseAscender(true);
			base_cell_7.setVerticalAlignment(Element.ALIGN_CENTER);
			base_cell_7.setHorizontalAlignment(Element.ALIGN_CENTER);
			base_cell_7.setFixedHeight(NumberUtil.parseMmToPt(19));
			PdfPTable cell_7_table = new PdfPTable(3);
			cell_7_table.setWidths(new int[] { 5, 91, 4 });
			cell_7_table.setWidthPercentage(100);
			PdfPCell cell_7_table_cell_1 = new PdfPCell();
			cell_7_table_cell_1.setBorder(0);
			cell_7_table_cell_1.setBorderWidthRight(1);
			cell_7_table_cell_1.setFixedHeight(NumberUtil.parseMmToPt(19));
			cell_7_table_cell_1.setUseAscender(true);
			cell_7_table_cell_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Paragraph p_shou_ = new Paragraph("收件人", hf_9);
			p_shou_.setAlignment(1);
			cell_7_table_cell_1.addElement(p_shou_);
			cell_7_table.addCell(cell_7_table_cell_1);
			PdfPCell cell_7_table_cell_2 = new PdfPCell();
			cell_7_table_cell_2.setBorder(0);
			cell_7_table_cell_2.setBorderWidthRight(1);
			cell_7_table_cell_2.setFixedHeight(NumberUtil.parseMmToPt(19));
			cell_7_table_cell_2.setUseAscender(true);
			cell_7_table_cell_2.setVerticalAlignment(Element.ALIGN_TOP);
			Paragraph revcName_ = new Paragraph("收件人：楊飛龍", hf_9);
			cell_7_table_cell_2.addElement(revcName_);
			revcName_ = new Paragraph("电话：1111111111", hf_9);
			cell_7_table_cell_2.addElement(revcName_);
			revcName_ = new Paragraph("地址：大渡河路", hf_9);
			cell_7_table_cell_2.addElement(revcName_);
			PdfPCell cell_7_table_cell_3 = new PdfPCell();
			cell_7_table_cell_3.setBorder(0);
			cell_7_table_cell_3.setFixedHeight(NumberUtil.parseMmToPt(19));
			cell_7_table_cell_3.setUseAscender(true);
			cell_7_table_cell_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Paragraph revcName_3_ = new Paragraph("收件联", hf_9);
			cell_7_table_cell_3.addElement(revcName_3_);
			cell_7_table.addCell(cell_7_table_cell_2);
			cell_7_table.addCell(cell_7_table_cell_3);
			base_cell_7.addElement(cell_7_table);
			baseTable.addCell(base_cell_7);
			/**************************************** 第八行 ********************************************/
			PdfPCell base_cell_8 = new PdfPCell();
			base_cell_8.setBorder(0);
			base_cell_8.setBorderWidthBottom(1);
			base_cell_8.setVerticalAlignment(Element.ALIGN_CENTER);
			base_cell_8.setHorizontalAlignment(Element.ALIGN_CENTER);
			base_cell_8.setFixedHeight(NumberUtil.parseMmToPt(20));

			PdfPTable cell_8_table = new PdfPTable(2);
			cell_8_table.setWidths(new int[] { 5,94 });
			cell_8_table.setWidthPercentage(100);

			PdfPCell cell_8_table_cell_1 = new PdfPCell();
			cell_8_table_cell_1.setBorder(0);
			cell_8_table_cell_1.setBorderWidthRight(1);
			cell_8_table_cell_1.setFixedHeight(NumberUtil.parseMmToPt(20));
			cell_8_table_cell_1.setUseAscender(true);
			cell_8_table_cell_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Paragraph p_ji_ = new Paragraph("订单详情", hf_9);
			p_ji_.setAlignment(1);
			cell_8_table_cell_1.addElement(p_ji_);
			cell_8_table.addCell(cell_8_table_cell_1);
			PdfPCell cell_8_table_cell_2 = new PdfPCell();
			cell_8_table_cell_2.setBorder(0);
			cell_8_table_cell_2.setFixedHeight(NumberUtil.parseMmToPt(20));
			cell_8_table_cell_2.setUseAscender(true);
			cell_8_table_cell_2.setVerticalAlignment(Element.ALIGN_TOP);
			cell_8_table_cell_2.setHorizontalAlignment(Element.ALIGN_LEFT);

			for (int i = 0; i < skuNameList.length; i++) {
				Paragraph senderAddress_ = new Paragraph("品名："+skuNameList[i]+" * "+skuCountList[i], hf_9);
				cell_8_table_cell_2.addElement(senderAddress_);
				count = count + Integer.valueOf(skuCountList[i]);
			}
			cell_8_table.addCell(cell_8_table_cell_2);
			base_cell_8.addElement(cell_8_table);
			baseTable.addCell(base_cell_8);

			/**************************************** 第九行 ********************************************/
			PdfPCell base_cell_9 = new PdfPCell();
			base_cell_9.setBorder(0);
			base_cell_9.setVerticalAlignment(Element.ALIGN_CENTER);
			base_cell_9.setHorizontalAlignment(Element.ALIGN_CENTER);
			base_cell_9.setFixedHeight(NumberUtil.parseMmToPt(6));

			PdfPTable cell_9_table = new PdfPTable(2);
			cell_9_table.setWidths(new int[]  {85 ,15});
			cell_9_table.setWidthPercentage(100);

			PdfPCell cell_9_table_cell_1 = new PdfPCell();
			cell_9_table_cell_1.setBorder(0);
			cell_9_table_cell_1.setUseAscender(true);
			cell_9_table_cell_1.setFixedHeight(NumberUtil.parseMmToPt(6));
			Paragraph p_SIGNATURE = new Paragraph("订单号:3201702131709154121S0001", hf_9);
			cell_9_table_cell_1.addElement(p_SIGNATURE);
			cell_9_table.addCell(cell_9_table_cell_1);
			PdfPCell cell_9_table_cell_2 = new PdfPCell();
			cell_9_table_cell_2.setBorder(0);
			cell_9_table_cell_2.setUseAscender(true);
			cell_9_table_cell_2.setFixedHeight(NumberUtil.parseMmToPt(6));
			Paragraph p_count = new Paragraph("数量:"+count, hf_9);
			cell_9_table_cell_2.addElement(p_count);
			cell_9_table.addCell(cell_9_table_cell_2);
			base_cell_9.addElement(cell_9_table);
			baseTable.addCell(base_cell_9);
			document.setMargins(0f, 0f, 0f, 0f);
			document.setPageSize(new RectangleReadOnly(NumberUtil.parseMmToPt(100), NumberUtil.parseMmToPt(150)));
			document.newPage();
			document.add(baseTable);
			document.close();
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		Test test = new Test();
		try {
			System.out.println(111);
			test.buildPdfBill();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}
