package com.sfebiz.supplychain.service.stockout.pdfformat;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
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
import com.sfebiz.supplychain.util.QRUtil;

import net.pocrd.entity.ServiceException;

/**
 * <p>顺丰进行清关和落地配送</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/1/5
 * Time: 下午3:56
 */
@Component("rhfKoreaCustomSFDeliveryPDFFormat")
public class RhfCustomSFDeliveryPDFFormat extends AbstractPDFFormat {

    private static final Logger logger = LoggerFactory.getLogger(RhfCustomSFDeliveryPDFFormat.class);


    /**
     * 构建顺丰BSP清关需要的 PDF面单
     *  @param element
     * @param warehouseDO @return
     * @param cb
     */
    public PdfPTable buildPdfBill(ExportedShipOrderDO element, WarehouseDO warehouseDO, LogisticsLineDO logisticsLineDO, PdfContentByte cb) throws ServiceException {
        try {
            String userName = StringUtils.isBlank(element.getBuyerName()) ? element.getDeclarePayerName() : element.getBuyerName();

            sfLogo.scalePercent(100f);//设置缩放的百分比%100
            String[] skuIdList = element.getSkuIdStr().split(SEPARATOR);
            String[] skuNameList = element.getSkuNameStr().split(SEPARATOR);
            String[] skuForeignNameList = element.getSkuForeignNameStr().split(SEPARATOR);
            String[] skuCountList = element.getSkuCountStr().split(SEPARATOR);
            String[] remarkList = element.getRemarks().split(SEPARATOR);
            String[] skuWeightList = element.getSkuWeightStr().split(SEPARATOR);

            float[] widths = {250f, 125f};// 设置表格的列宽和列数

            PdfPTable table = new PdfPTable(widths);// 建立一个pdf表格
            table.setTotalWidth(375f);// 设置表格的宽度
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);//设置表格默认为无边框

            PdfPCell cell;
            Paragraph paragraph;

            cell = new PdfPCell(new Paragraph("", BoldBigChinese));
            cell.setColspan(2);

            float[] headerWidth = {45f, 135f};// 设置表格的列宽和列数
            PdfPTable headerTable = new PdfPTable(3);// 建立一个pdf表格
            headerTable.setWidthPercentage(100);
            PdfPCell headerCell;

            headerCell = new PdfPCell(new Paragraph("", BoldBigLittleChinese));//描述
            headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居中显示
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
            headerCell.addElement(rhfSfLogo);
            headerCell.setFixedHeight(50f);
            headerCell.setPadding(5f);
            headerCell.setBorder(0);
            headerTable.addCell(headerCell);

            headerCell = new PdfPCell(new Paragraph("顺丰隔日", BoldLittleBigChinese));//描述
            headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居中显示
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
            headerCell.setBorder(0);
            headerCell.setPaddingLeft(20f);
            headerCell.setPaddingBottom(10f);
            headerCell.setFixedHeight(50f);
            headerTable.addCell(headerCell);

            headerCell = new PdfPCell(new Paragraph("E", BoldBigLittleChinese));//描述
            headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居中显示
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
            headerCell.setBorder(0);
            headerCell.setPaddingLeft(80f);
            headerCell.setPaddingBottom(10f);
            headerCell.setFixedHeight(50f);
            headerTable.addCell(headerCell);

            cell.addElement(headerTable);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(" 寄方：\n    " + element.getSenderName(), FontChinese));//描述
            cell.setFixedHeight(30f);
            table.addCell(cell);

            cell = new PdfPCell();//描述
            paragraph = new Paragraph(" 原寄地\n       " +"020" + "\n", FontChinese);
            cell.addElement(paragraph);
            cell.setFixedHeight(30f);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(
                    " 收方： " + userName + "              " + element.getBuyerTelephone() + "    \n"
                            + element.getBuyerAddress(), FontChinese));//描述
            cell.setFixedHeight(30f);
            table.addCell(cell);

            PdfPCell destCell = new PdfPCell(new Paragraph("", BoldRedBigChinese));//
            destCell.setColspan(2);

            float[] destWidth = {45f, 80f};// 设置表格的列宽和列数
            PdfPTable destTable = new PdfPTable(destWidth);// 建立一个pdf表格
            destTable.setWidthPercentage(100);
            destTable.getDefaultCell().setBorder(0);//设置表格默认为无边框

            destCell = new PdfPCell(new Paragraph("目的地\n   \n", FontChinese));//描述
            destCell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
            destCell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
            destCell.setFixedHeight(30f);
            destCell.setBorder(0);
            destTable.addCell(destCell);

            destCell = new PdfPCell(new Paragraph(element.getDestcode(), BoldRedBigChinese));//描述
            destCell.setBorder(0);
            destCell.setFixedHeight(30f);
            destTable.addCell(destCell);

            destCell = new PdfPCell();//描述
            destCell.setFixedHeight(30f);
            destCell.addElement(destTable);
            table.addCell(destCell);

            /****************************************上联 运单号开始********************************************/

            float[] width0 = {180f};// 设置表格的列宽和列数
            PdfPTable table0 = new PdfPTable(width0);// 建立一个pdf表格
            table0.setWidthPercentage(100);
            table0.setTotalWidth(180f);
            table0.getDefaultCell().setBorder(0);

            PdfPCell table0Cell;//描述
            Image barcode = null;
            if (StringUtils.isBlank(element.getIntrMailNo())) {
                table0Cell = new PdfPCell(new Paragraph(""));
                table0Cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
                table0Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
                table0Cell.setFixedHeight(45f);
                table0Cell.setBorder(0);
                table0.addCell(table0Cell);
            } else {
                barcode = Image.getInstance(QRUtil.getBarcodeBytes(element.getIntrMailNo(), 300, null));
                barcode.setWidthPercentage(50);
                barcode.scalePercent(50f);//设置缩放的百分比%7.5
                barcode.setAlignment(Element.ALIGN_CENTER);
                table0Cell = new PdfPCell(new Paragraph(""));
                table0Cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
                table0Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
                table0Cell.addElement(barcode);
                table0Cell.setFixedHeight(45f);
                table0Cell.setBorder(0);
                table0.addCell(table0Cell);
            }

            table0Cell = new PdfPCell(new Paragraph("运单号 " + element.getIntrMailNo(), FontChinese));
            table0Cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
            table0Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
            table0Cell.setFixedHeight(25f);
            table0Cell.setBorder(0);
            table0.addCell(table0Cell);

            cell = new PdfPCell();//描述
            cell.addElement(table0);
            cell.setFixedHeight(70f);
            cell.setPadding(0);
            table.addCell(cell);

            /****************************************上联 运单号结束********************************************/

            float[] width1 = {103f};// 设置表格的列宽和列数
            PdfPTable table1 = new PdfPTable(width1);// 建立一个pdf表格
            table1.setWidthPercentage(100);
            table1.setTotalWidth(103f);
            table1.getDefaultCell().setBorder(1);

            PdfPCell table1Cell;//描述
            table1Cell = new PdfPCell(new Paragraph(" 电商专配", BoldLittleBigChinese));
            table1Cell.setFixedHeight(19f);
            table1.addCell(table1Cell);
            table1Cell = new PdfPCell(new Paragraph("收件员", FontChinese));
            table1Cell.setFixedHeight(17f);
            table1.addCell(table1Cell);

            table1Cell = new PdfPCell(new Paragraph("寄件日期", FontChinese));
            table1Cell.setFixedHeight(17f);
            table1.addCell(table1Cell);

            table1Cell = new PdfPCell(new Paragraph("派件员", FontChinese));
            table1Cell.setFixedHeight(17f);
            table1.addCell(table1Cell);

            cell = new PdfPCell();//描述
            cell.addElement(table1);
            cell.setFixedHeight(70f);
            cell.setBorder(0);
            cell.setPadding(0);
            table.addCell(cell);

            /****************************************上联 收件人签署开始********************************************/

            float[] width2 = {60f, 60f, 60f, 60f, 60f};// 设置表格的列宽和列数
            PdfPTable table2 = new PdfPTable(width2);// 建立一个pdf表格
            table2.setWidthPercentage(100);
            table2.setTotalWidth(283f);
            table2.getDefaultCell().setBorder(1);

            //计算总托寄数量和总重量
            BigDecimal totalCount = new BigDecimal("0");
            BigDecimal totalWeight = new BigDecimal("0");
            //ProductDeclareDO productDeclareDO;
            for (int i = 0; i < skuWeightList.length; i++) {
                BigDecimal count = new BigDecimal(skuCountList[i]);
                //productDeclareDO = productDeclareManager.queryDeclaredSku(Long.valueOf(skuIdList[i]), portId, declareMode);
                BigDecimal weight = new BigDecimal(skuWeightList[i]).multiply(count);
                //if (productDeclareDO != null && StringUtils.isNotEmpty(productDeclareDO.getGrossWeight())) {
                //weight = new BigDecimal(productDeclareDO.getGrossWeight()).multiply(count);
                //}
                totalCount = totalCount.add(count);
                totalWeight = totalWeight.add(weight);
            }
            PdfPCell table2Cell;//描述
            table2Cell = new PdfPCell(new Paragraph("托寄物数量", FontChinese));
            table2Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(table2Cell);

            table2Cell = new PdfPCell(new Paragraph("实际重量kg", FontChinese));
            table2Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(table2Cell);

            table2Cell = new PdfPCell(new Paragraph("计费重量kg", FontChinese));
            table2Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(table2Cell);

            table2Cell = new PdfPCell(new Paragraph("运费", FontChinese));
            table2Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(table2Cell);

            table2Cell = new PdfPCell(new Paragraph("费用合计", FontChinese));
            table2Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(table2Cell);

            table2Cell = new PdfPCell(new Paragraph(totalCount.toString(), FontChinese));
            table2Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(table2Cell);

            table2Cell = new PdfPCell(
                    new Paragraph(totalWeight.toString(), FontChinese));
            table2Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(table2Cell);

            table2Cell = new PdfPCell(
                    new Paragraph(totalWeight.toString(), FontChinese));
            table2Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(table2Cell);

            table2Cell = new PdfPCell(new Paragraph(" ", FontChinese));
            table2Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(table2Cell);

            table2Cell = new PdfPCell(new Paragraph(" ", FontChinese));
            table2Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(table2Cell);

            table2Cell = new PdfPCell(new Paragraph(" ", FontChinese));//描述
            table2Cell.setColspan(5);
            table2Cell.setFixedHeight(10f);
            table2Cell.setBorder(0);
            table2.addCell(table2Cell);
          //TODO 待确定
//            table2Cell = new PdfPCell(
//                    new Paragraph("月结账号:" + (StringUtils.isEmpty(logisticsLineDO.custId) ? "" : logisticsLineDO.custId), FontChinese));
            table2Cell.setColspan(5);
            table2Cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2Cell.setPaddingLeft(5f);
            table2Cell.setBorder(0);
            table2.addCell(table2Cell);

            table2Cell = new PdfPCell(
                    new Paragraph("付费方式:寄付月结",
                            FontChinese));
            table2Cell.setColspan(3);
            table2Cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2Cell.setPaddingLeft(5f);
            table2Cell.setBorder(0);
            table2.addCell(table2Cell);

            table2Cell = new PdfPCell(new Paragraph("收方签署：", FontChinese));
            table2Cell.setColspan(2);
            table2Cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2Cell.setPaddingLeft(27f);
            table2Cell.setBorder(0);
            table2.addCell(table2Cell);

            table2Cell = new PdfPCell(new Paragraph("日期：      月       日", FontChinese));
            table2Cell.setColspan(5);
            table2Cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2Cell.setPaddingLeft(180f);
            table2Cell.setBorder(0);
            table2.addCell(table2Cell);

            cell = new PdfPCell();//描述
            cell.setColspan(2);
            cell.addElement(table2);
            cell.setPadding(0);
            cell.setFixedHeight(75f);
            table.addCell(cell);

            /****************************************上联 收件人签署结束********************************************/

            /****************************************上联 分隔处开始********************************************/

            cell = new PdfPCell();//描述
            cell.setColspan(2);
            cell.setFixedHeight(3f);
            table.addCell(cell);

            /****************************************上联 分隔处结束********************************************/

            /****************************************下联 运单号，寄件开始********************************************/

            // 下半部分
            float[] width3 = {180f};// 设置表格的列宽和列数
            PdfPTable table3 = new PdfPTable(width3);// 建立一个pdf表格
            table3.setWidthPercentage(100);
            table3.setTotalWidth(180f);
            table3.getDefaultCell().setBorder(0);

            PdfPCell table3Cell;
            if (barcode == null) {
                table3Cell = new PdfPCell(new Paragraph(""));
                table3Cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
                table3Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
                table3Cell.setFixedHeight(30f);
                table3Cell.setBorder(0);
                table3.addCell(table3Cell);
            } else {
                barcode.setWidthPercentage(50);
                barcode.scalePercent(50f);//设置缩放的百分比%7.5
                barcode.setAlignment(Element.ALIGN_CENTER);
                table3Cell = new PdfPCell(new Paragraph(""));
                table3Cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
                table3Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
                table3Cell.addElement(barcode);
                table3Cell.setFixedHeight(30f);
                table3Cell.setBorder(0);
                table3.addCell(table3Cell);
            }

            table3Cell = new PdfPCell(new Paragraph("运单号 " + element.getIntrMailNo(), FontChinese));
            table3Cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
            table3Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
            table3Cell.setFixedHeight(20f);
            table3Cell.setBorder(0);
            table3.addCell(table3Cell);

            cell = new PdfPCell();//描述
            cell.addElement(table3);
            cell.setFixedHeight(50f);
            cell.setPadding(0);
            table.addCell(cell);

            cell = new PdfPCell(
                    new Paragraph(" 寄件：\n    " + element.getSenderName() + "\n" + element.getSenderTelephone(),
                            FontChinese));
            cell.setFixedHeight(50f);
            cell.setPadding(0);
            table.addCell(cell);

            /****************************************下联 运单号，寄件结束********************************************/

            /****************************************下联 收件，订单号开始********************************************/
            cell = new PdfPCell(new Paragraph(
                    " 收方： " + userName + "              " + element.getBuyerTelephone() + "    \n"
                            + element.getBuyerAddress(), FontChinese));
            cell.setFixedHeight(50f);
            cell.setPadding(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(" 订单号：\n\n        " + element.getBizId() + "\n", FontChinese));
            cell.setFixedHeight(50f);
            cell.setPadding(0);
            table.addCell(cell);
            /****************************************下联 收件，订单号结束********************************************/

            /****************************************托寄物，备注一栏开始****************************************/
            float[] width4 = {115f, 70f, 55f, 120f};// 设置表格的列宽和列数
            PdfPTable table4 = new PdfPTable(width4);// 建立一个pdf表格
            table4.setWidthPercentage(100);
            table4.setTotalWidth(283f);
            table4.getDefaultCell().setBorder(1);

            PdfPCell table4Cell;//描述
            table4Cell = new PdfPCell(new Paragraph("托寄物", FontChinese));
            table4Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(table4Cell);

            table4Cell = new PdfPCell(new Paragraph("SKU", FontChinese));
            table4Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(table4Cell);

            table4Cell = new PdfPCell(new Paragraph("数量", FontChinese));
            table4Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(table4Cell);

            table4Cell = new PdfPCell(new Paragraph("备注", FontChinese));
            table4Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(table4Cell);

            for (int i = 0; i < skuIdList.length; i++) {
                table4Cell = new PdfPCell(new Paragraph(skuNameList[i], FontChinese));
                table4Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table4.addCell(table4Cell);

                table4Cell = new PdfPCell(new Paragraph(skuIdList[i], FontChinese));
                table4Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table4.addCell(table4Cell);

                table4Cell = new PdfPCell(new Paragraph(skuCountList[i], FontChinese));
                table4Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table4.addCell(table4Cell);

                String temp = "";
                if (skuForeignNameList[i].equals("null") && !remarkList[i].equals("null")) {
                    temp = remarkList[i];
                } else if (!skuForeignNameList[i].equals("null") && remarkList[i].equals("null")) {
                    temp = skuForeignNameList[i];
                } else if (!skuForeignNameList[i].equals("null") && !remarkList[i].equals("null")) {
                    temp = remarkList[i] + "\n" + skuForeignNameList[i];
                }

                if ("韩国".equals(warehouseDO.getCountry())) {
                    table4Cell = new PdfPCell(new Paragraph(temp, fontSouthKorea));
                } else if ("JP".equals(warehouseDO.getCountry())) {
                    table4Cell = new PdfPCell(new Paragraph(temp, fontJapan));
                } else {
                    table4Cell = new PdfPCell(new Paragraph(temp, FontChinese));
                }
                //table4Cell = new PdfPCell(new Paragraph(temp, FontChinese));

                table4Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table4.addCell(table4Cell);

                if (table4.getTotalHeight() > 300f) {
                    break;
                }
            }

            cell = new PdfPCell();//描述
            cell.setColspan(2);
            cell.addElement(table4);
            cell.setPadding(0);
            cell.setBorder(1);
            table.addCell(cell);
            /****************************************托寄物，备注一栏结束****************************************/

            /****************************************转寄协议客户一栏开始********************************************/
            cell = new PdfPCell();
//            cell.setColspan(2)；
            paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            cell.addElement(xjaLogo);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.setPaddingRight(40f);
            cell.setFixedHeight(30f);
            cell.setPaddingBottom(5f);
            table.addCell(cell);
            cell = new PdfPCell();
//            cell.setColspan(2);
            paragraph = new Paragraph("转寄协议客户", BoldLittleBigChinese);
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            cell.addElement(paragraph);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.setPaddingRight(40f);
            cell.setFixedHeight(30f);
            cell.setPaddingBottom(5f);
            table.addCell(cell);
            /****************************************转寄协议客户一栏结束********************************************/
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

    @Override
    public String getFormat() {
        return PdfTemplateType.RHF_SF.getValue();
    }
}
