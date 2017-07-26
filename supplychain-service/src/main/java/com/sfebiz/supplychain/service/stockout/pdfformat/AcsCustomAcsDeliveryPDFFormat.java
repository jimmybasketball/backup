package com.sfebiz.supplychain.service.stockout.pdfformat;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
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

import net.pocrd.entity.ServiceException;

/**
 * ACS面单打印模板
 * 
 * @author matt
 * @version $Id: RhfKoreaCustomYDDeliveryPDFFormat.java, v 0.1 2016年6月14日
 *          下午2:58:07 matt Exp $
 */
@Component("acsCustomAcsDeliveryPDFFormat")
public class AcsCustomAcsDeliveryPDFFormat extends AbstractPDFFormat {

	/** 日志 */
	private static final Logger logger = LoggerFactory
			.getLogger(AcsCustomAcsDeliveryPDFFormat.class);

	/** 字体文件路径 */
	private static final String simheiFontPath = "/home/admin/font/PMingLiU-TW.ttf";

	@Override
	public PdfPTable buildPdfBill(ExportedShipOrderDO exportedShipOrderDO,
			WarehouseDO warehouseDO, LogisticsLineDO logisticsLineDO, PdfContentByte cb)
			throws ServiceException {

		try {


			// 1 文件准备
			BaseFont bfChinese = BaseFont.createFont(simheiFontPath,
					BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			//字体准备
			Font FontChinese = new Font(bfChinese, 10, Font.UNDEFINED);
			Font FontTitleChinese = new Font(bfChinese, 16, Font.BOLD);
			Font FontlittleChinese = new Font(bfChinese, 8, Font.NORMAL);


			//商品数量，名称，重量
			String[] skuIdList = exportedShipOrderDO.getSkuIdStr().split(SEPARATOR);
			String[] skuNameList = exportedShipOrderDO.getSkuNameStr().split(SEPARATOR);
			String[] skuCountList = exportedShipOrderDO.getSkuCountStr().split(SEPARATOR);
			String[] remarkList = exportedShipOrderDO.getRemarks().split(SEPARATOR);
			String[] skuWeightList = exportedShipOrderDO.getSkuWeightStr().split(SEPARATOR);
			int count = 0;
			BigDecimal weight = new BigDecimal(0);
			for (int i = 0; i < skuIdList.length; i++) {
				count = count + Integer.valueOf(skuCountList[i]);
				weight = weight.add(new BigDecimal(skuWeightList[i]));
			}

			// 1.3. 绘制面单的整体结构
			float[] widths = {94f, 188f};// 设置表格的列宽和列数
			PdfPTable table = new PdfPTable(widths);
			PdfPCell cell = new PdfPCell();
			table.setTotalWidth(280f); // 设置表格宽度
			table.setLockedWidth(true);
			table.getDefaultCell().setBorder(1);//设置表格默认为无边框


			/**************************************  标题LOGO开始  *************************************************/
			cell = new PdfPCell(acsLogo,true);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setUseBorderPadding(true);
			cell.setPaddingLeft(20);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);//


			PdfPTable barcodeTable = new PdfPTable(1);
			cell = new PdfPCell(new Paragraph("中國大陸郵件提單",FontTitleChinese));//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setUseBorderPadding(true);
			cell.setBorder(Rectangle.NO_BORDER);
			barcodeTable.addCell(cell);

			PdfPCell mailNoCells = new PdfPCell(new Paragraph(""));
			Barcode128 code128 = new Barcode128();
			code128.setCode(exportedShipOrderDO.getIntrMailNo());
			code128.setCodeType(Barcode128.CODE128);
			code128.setTextAlignment(Element.ALIGN_CENTER);
			code128.setX(1.5f);
			Image code128Image = code128.createImageWithBarcode(cb, null, null);
			mailNoCells.setUseBorderPadding(true);
			mailNoCells = new PdfPCell(code128Image,true);
			mailNoCells.setPaddingRight(20f);
			code128Image.setAlignment(Element.ALIGN_CENTER);
			code128Image.scalePercent(220f);//设置缩放的百分比%7.5
			mailNoCells.setBorder(Rectangle.NO_BORDER);
			barcodeTable.addCell(mailNoCells);//
			cell = new PdfPCell();//描述
			cell.setUseBorderPadding(true);
			cell.addElement(barcodeTable);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPadding(0);
			table.addCell(cell);
			/***********************************************   标题LOGO结束  ************************************************/
			/***********************************************   收寄件信息开始 ***********************************************/
			PdfPTable receiveAndSendTable = new PdfPTable(2);// 建立一个pdf表格
			receiveAndSendTable.setWidthPercentage(100);
			receiveAndSendTable.getDefaultCell().setBorder(1);
			//寄件人
			cell = new PdfPCell(new Paragraph("寄件人:"+ exportedShipOrderDO.getSenderName() ,FontChinese));//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setPaddingLeft(7);
			cell.setPaddingTop(3);
			cell.setColspan(2);
			cell.setBorder(Rectangle.NO_BORDER);
			receiveAndSendTable.addCell(cell);

			// 寄件地址
			cell = new PdfPCell(new Paragraph(exportedShipOrderDO.getSenderAddress(),FontChinese));//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setPaddingLeft(7);
			cell.setPaddingTop(3);
			cell.setColspan(2);
			cell.setBorder(Rectangle.NO_BORDER);
			receiveAndSendTable.addCell(cell);

			//收件人名称
			String userName = StringUtils.isBlank(exportedShipOrderDO.getBuyerName()) ? exportedShipOrderDO.getDeclarePayerName() : exportedShipOrderDO.getBuyerName();

			// 寄件
			cell = new PdfPCell(new Paragraph("收件人:"+userName,FontChinese));//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setPaddingLeft(7);
			cell.setPaddingTop(3);
			cell.setBorder(Rectangle.NO_BORDER);
			receiveAndSendTable.addCell(cell);

			// 收件人联系方式
			cell = new PdfPCell(new Paragraph("電話:"+exportedShipOrderDO.getBuyerTelephone(),FontChinese));//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setPaddingLeft(7);
			cell.setPaddingTop(3);
			cell.setBorder(Rectangle.NO_BORDER);
			receiveAndSendTable.addCell(cell);

			// 收件地址
			cell = new PdfPCell(new Paragraph(exportedShipOrderDO.getBuyerAddress(),FontChinese));//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPaddingLeft(7);
			cell.setPaddingTop(3);
			cell.setColspan(2);
			receiveAndSendTable.addCell(cell);

			cell = new PdfPCell();//描述
			cell.addElement(receiveAndSendTable);
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPadding(0);
			cell.setColspan(2);
			table.addCell(cell);
			/***********************************************   收寄件信息结束 ***********************************************/

			/***********************************************  订单码 ***********************************************/
			PdfPTable barcodetable = new PdfPTable(1);
			Barcode128 code128s = new Barcode128();
			code128s.setCode(exportedShipOrderDO.getBizId());
			code128s.setCodeType(Barcode128.CODE128);
			code128s.setX(1.2f);
			Image code128Images = code128s.createImageWithBarcode(cb, null, null);
			PdfPCell mailNoCell = new PdfPCell(new Paragraph(""));
			mailNoCell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			mailNoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			mailNoCell.setBorder(Rectangle.NO_BORDER);
			mailNoCell.addElement(code128Images);
			barcodetable.addCell(mailNoCell);
			cell = new PdfPCell();//描述
			cell.setColspan(2);
			cell.setPaddingLeft(10f);
			cell.setBorderWidth(10f);
			cell.addElement(barcodetable);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);//



			/***********************************************  面单底部 ***********************************************/
			cell = new PdfPCell(new Paragraph("若無法投遞時,寄件人之指定事項\n" +
					"口退回寄件人並由寄件人付運費\t    口拋棄",FontlittleChinese));//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setLeading(1f,1.8f);
			cell.setColspan(2);
			cell.setPaddingLeft(7);
			cell.setPaddingTop(4);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph("1.茲證明本人所填寫資料屬實且無裝寄任何危險及禁寄物品",FontlittleChinese));//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			cell.setPaddingLeft(7);
			cell.setPaddingTop(4);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph("2.本人已審閱並同意載運契約一切條款",FontlittleChinese));//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			cell.setPaddingLeft(7);
			cell.setPaddingTop(4);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph("(未保價貨件每單賠償上限為100 美元)",FontlittleChinese));//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			cell.setPaddingLeft(7);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph("寄件人簽署____________",FontlittleChinese));//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			cell.setPaddingLeft(7);
			cell.setPaddingTop(4);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph("日期:_____年______月_______日",FontlittleChinese));//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			cell.setPaddingTop(4);
			cell.setPaddingLeft(7);
			table.addCell(cell);


			/***********************************************  商品信息开始 ***********************************************/


			PdfPTable tables = new PdfPTable(2);
			tables.setHorizontalAlignment(Element.ALIGN_LEFT);
			//件数
			cell = new PdfPCell(new Paragraph("总件數："+count,FontChinese));//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setFixedHeight(20f);
			cell.setPaddingLeft(7);
			cell.setBorder(Rectangle.NO_BORDER);
			tables.addCell(cell);
			//重量
			cell = new PdfPCell(new Paragraph("总重量："+weight.setScale(3).toString()+"KG",FontChinese));//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setFixedHeight(20f);
			cell.setBorder(Rectangle.NO_BORDER);
			tables.addCell(cell);
			cell = new PdfPCell();//描述
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
			cell.setFixedHeight(20f);
			cell.setPadding(0);
			cell.setBorder(Rectangle.NO_BORDER);
			cell.addElement(tables);
			cell.setColspan(2);
			table.addCell(cell);

			for (int i = 0; i < skuIdList.length; i++) {
				cell = new PdfPCell(new Paragraph(remarkList[i]+"-"+skuNameList[i]+"*"+skuCountList[i],FontChinese));//描述
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
				cell.setPaddingLeft(7);
				cell.setPaddingTop(3);
				cell.setColspan(2);
				cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);
			}
			/***********************************************   商品信息结束 ***********************************************/


			return table;
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
		return PdfTemplateType.ACS_ACS.getValue();
	}

}
