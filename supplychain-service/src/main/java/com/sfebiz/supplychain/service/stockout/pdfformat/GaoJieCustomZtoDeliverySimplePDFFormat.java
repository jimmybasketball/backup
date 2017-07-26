package com.sfebiz.supplychain.service.stockout.pdfformat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
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

import net.pocrd.entity.ServiceException;

/**
 * 高捷清关, 中通配送的简单PDF面单格式
 * <p>
 * Created by jackiehff on 16/1/7.
 */
@Component("gaoJieCustomZtoDeliverySimplePDFFormat")
public class GaoJieCustomZtoDeliverySimplePDFFormat extends AbstractPDFFormat {

    private static final Logger logger = LoggerFactory.getLogger(GaoJieCustomZtoDeliverySimplePDFFormat.class);

    @Override
    public PdfPTable buildPdfBill(ExportedShipOrderDO exportedShipOrderDO, WarehouseDO warehouseDO, LogisticsLineDO logisticsLineDO, PdfContentByte cb) throws ServiceException {
        try {
            String userName = StringUtils.isBlank(exportedShipOrderDO.getBuyerName()) ? exportedShipOrderDO.getDeclarePayerName() : exportedShipOrderDO.getBuyerName();

            float[] widths = {250f, 125f};// 设置表格的列宽和列数

            PdfPTable table = new PdfPTable(widths);// 建立一个pdf表格
            table.setTotalWidth(PageSize.A5.getWidth() - 10); // 设置表格宽度
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);//设置表格默认为无边框

            PdfPCell cell = new PdfPCell(new Paragraph("", BoldBigChinese));//
            cell.setColspan(2);

            /**----------------------- 头部开始 -------------------------*/

            float[] headerWidth = {45f, 135f};// 设置表格的列宽和列数
            PdfPTable headerTable = new PdfPTable(headerWidth);// 建立一个pdf表格
            headerTable.setWidthPercentage(100);

            PdfPCell headerCell = new PdfPCell(new Paragraph("", BoldBigChinese));//描述
            headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居中显示
            headerCell.setVerticalAlignment(Element.ALIGN_BOTTOM);  // 设置垂直居中
            ztoLogo.scalePercent(100f);
            headerCell.addElement(ztoLogo);
            headerCell.setFixedHeight(45f);
            headerCell.setPadding(2f);
            headerCell.setBorder(0);
            headerTable.addCell(headerCell);

            headerCell = new PdfPCell(new Paragraph("标准快递", Big16Chinese));//描述
            headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);// 设置内容水平居中显示
            headerCell.setVerticalAlignment(Element.ALIGN_BOTTOM);  // 设置垂直居中
            headerCell.setBorder(0);
            headerCell.setPaddingLeft(90f);
            headerCell.setPaddingBottom(5f);
            headerCell.setFixedHeight(45f);
            headerTable.addCell(headerCell);

            cell.addElement(headerTable);
            table.addCell(cell);

            /**----------------------- 头部结束 -------------------------*/


            /**----------------------- 目的地代码开始 -------------------------*/

            float[] destCodeWidth = {375f};// 设置表格的列宽和列数
            PdfPTable destCodeTable = new PdfPTable(destCodeWidth);// 建立一个pdf表格
            destCodeTable.setWidthPercentage(100);
            destCodeTable.getDefaultCell().setBorder(1);

            PdfPCell destCodeCell = new PdfPCell(new Paragraph(exportedShipOrderDO.getDestcode(), BoldBig16Chinese));//描述
            destCodeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            destCodeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            destCodeCell.setFixedHeight(40f);
            destCodeCell.setBorderWidth(0);
            destCodeCell.setPadding(0);
            destCodeTable.addCell(destCodeCell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.addElement(destCodeTable);
            table.addCell(cell);

            /**----------------------- 目的地代码结束 -------------------------*/


            cell = new PdfPCell(new Paragraph("市场部广州分部", BoldBig16Chinese));//描述
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(30f);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("", FontChinese));//描述
            cell.setFixedHeight(30f);
            table.addCell(cell);


            /**----------------------- 收件和寄件开始 -------------------------*/
            float[] receiveAndSendWidth = {15f, 165f};// 设置表格的列宽和列数
            PdfPTable receiveAndSendTable = new PdfPTable(receiveAndSendWidth);// 建立一个pdf表格
            receiveAndSendTable.setWidthPercentage(100);
            receiveAndSendTable.setTotalWidth(180f);
            receiveAndSendTable.getDefaultCell().setBorder(1);

            cell = new PdfPCell(new Paragraph("收件", FontChinese));//描述
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
            cell.setFixedHeight(45f);
            receiveAndSendTable.addCell(cell);

            cell = new PdfPCell(new Paragraph(userName + "                 " + exportedShipOrderDO.getBuyerTelephone() + "    \n\n"
                    + exportedShipOrderDO.getBuyerAddress(), Big10Chinese));
            cell.setFixedHeight(45f);
            receiveAndSendTable.addCell(cell);

            // 寄件
            cell = new PdfPCell(new Paragraph("寄件", FontChinese));//描述
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
            cell.setFixedHeight(45f);
            receiveAndSendTable.addCell(cell);

            cell = new PdfPCell(new Paragraph(exportedShipOrderDO.getSenderName() + "\n" + exportedShipOrderDO.getSenderTelephone() + "    \n\n"
                    + exportedShipOrderDO.getSenderAddress(), Big10Chinese));
            cell.setFixedHeight(45f);
            receiveAndSendTable.addCell(cell);

            cell = new PdfPCell();//描述
            cell.addElement(receiveAndSendTable);
            cell.setBorder(0);
            cell.setPadding(0);
            table.addCell(cell);


            /**----------------------- 收件和寄件结束 -------------------------*/

            float[] width1 = {103f};// 设置表格的列宽和列数
            PdfPTable table1 = new PdfPTable(width1);// 建立一个pdf表格
            table1.setWidthPercentage(100);
            table1.setTotalWidth(103f);
            table1.getDefaultCell().setBorder(1);

            PdfPCell table1Cell = new PdfPCell(new Paragraph(" 服务 \n\n 代收货款:\n\n 预约配送:\n\n 声明价值:\n\n 操作费用: \n\n", FontChinese));
            table1Cell.setFixedHeight(80f);
            table1.addCell(table1Cell);

            cell = new PdfPCell();//描述
            cell.addElement(table1);
            cell.setFixedHeight(80f);
            cell.setBorder(0);
            cell.setPadding(0);
            table.addCell(cell);


            /****************************************运单号开始********************************************/
            float[] mailNoWidth = {375f};// 设置表格的列宽和列数
            PdfPTable mailNoTable = new PdfPTable(mailNoWidth);// 建立一个pdf表格
            mailNoTable.setWidthPercentage(100);
            mailNoTable.getDefaultCell().setBorder(1);

            PdfPCell mailNoCell = new PdfPCell(new Paragraph(""));
            mailNoCell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
            mailNoCell.setVerticalAlignment(Element.ALIGN_BOTTOM);  // 设置垂直居中

            Barcode128 code128 = new Barcode128();
            code128.setCode(exportedShipOrderDO.getIntrMailNo());
            code128.setCodeType(Barcode128.CODE128);
            Image code128Image = code128.createImageWithBarcode(cb, null, null);

            code128Image.setAlignment(Element.ALIGN_CENTER);
            code128Image.setWidthPercentage(100);
            code128Image.scalePercent(220f);//设置缩放的百分比%7.5
            mailNoCell.addElement(code128Image);
            mailNoCell.setBorder(0);
            mailNoTable.addCell(mailNoCell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.addElement(mailNoTable);
            table.addCell(cell);

            /****************************************运单号结束********************************************/

            /****************************************订单号开始********************************************/

            float[] orderNoWidth = {375f};// 设置表格的列宽和列数
            PdfPTable orderNoTable = new PdfPTable(orderNoWidth);// 建立一个pdf表格
            orderNoTable.setWidthPercentage(100);
            orderNoTable.getDefaultCell().setBorder(1);

            PdfPCell orderNoCell = new PdfPCell(new Paragraph(" 订单号：" + exportedShipOrderDO.getBizId(), Big10Chinese));//描述
            orderNoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            orderNoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            orderNoCell.setFixedHeight(30f);
            orderNoCell.setBorderWidth(0);
            orderNoCell.setPadding(0);
            orderNoTable.addCell(orderNoCell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.addElement(orderNoTable);
            table.addCell(cell);

            /****************************************订单号结束********************************************/

            cell = new PdfPCell(new Paragraph("快件送达收货人地址,经收件人或收件(寄件人)允许的代收人签字,视为送达。您的签字代表您已经验收此包裹,并已确认商品信息无误,包裹完好,没有划痕、破损等表面质量问题。", FontChinese));//描述
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(40f);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("签收人:    \n\n" + "时间:    \n\n", FontChinese));//描述
            cell.setFixedHeight(40f);
            table.addCell(cell);

            /****************************************上联 分隔处开始********************************************/

            cell = new PdfPCell();//描述
            cell.setColspan(2);
            cell.setFixedHeight(3f);
            table.addCell(cell);

            /****************************************上联 分隔处结束********************************************/

            /****************************************下联表头开始********************************************/
            cell = new PdfPCell(new Paragraph("", BoldBigChinese));//描述
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居中显示
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
            cell.addElement(ztoLogo);
            cell.setFixedHeight(35f);
            table.addCell(cell);

            // 下半部分
            float[] width3 = {180f};// 设置表格的列宽和列数
            PdfPTable table3 = new PdfPTable(width3);// 建立一个pdf表格
            table3.setWidthPercentage(100);
            table3.setTotalWidth(180f);
            table3.getDefaultCell().setBorder(0);

            PdfPCell table3Cell = new PdfPCell(new Paragraph(""));
            table3Cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
            table3Cell.setVerticalAlignment(Element.ALIGN_TOP);  // 设置垂直居中

            code128Image.scalePercent(160f);//设置缩放的百分比%7.5
            table3Cell.addElement(code128Image);
            table3Cell.setBorder(0);
            table3.addCell(table3Cell);

            cell = new PdfPCell();
            cell.addElement(table3);
            table.addCell(cell);
            /****************************************下联表头结束********************************************/

            /**********************************下联的收件和寄件开始********************************/
            float[] receiveAndSendWidth2 = {20f, 355f};
            receiveAndSendTable = new PdfPTable(receiveAndSendWidth2);// 建立一个pdf表格
            receiveAndSendTable.setWidthPercentage(100);
            receiveAndSendTable.setTotalWidth(375f);
            receiveAndSendTable.getDefaultCell().setBorder(1);

            cell = new PdfPCell(new Paragraph("收件", FontChinese));//描述
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
            cell.setFixedHeight(45f);
            receiveAndSendTable.addCell(cell);

            cell = new PdfPCell(new Paragraph(userName + "                 " + exportedShipOrderDO.getBuyerTelephone() + "    \n\n"
                    + exportedShipOrderDO.getBuyerAddress(), Big10Chinese));
            cell.setFixedHeight(45f);
            receiveAndSendTable.addCell(cell);

            // 寄件
            cell = new PdfPCell(new Paragraph("寄件", FontChinese));//描述
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居左显示
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
            cell.setFixedHeight(45f);
            receiveAndSendTable.addCell(cell);

            cell = new PdfPCell(new Paragraph(exportedShipOrderDO.getSenderName() + "                " + exportedShipOrderDO.getSenderTelephone() + "    \n\n"
                    + exportedShipOrderDO.getSenderAddress(), Big10Chinese));
            cell.setFixedHeight(45f);
            receiveAndSendTable.addCell(cell);

            cell = new PdfPCell();//描述
            cell.addElement(receiveAndSendTable);
            cell.setBorder(0);
            cell.setPadding(0);
            cell.setColspan(2);
            table.addCell(cell);
            /**********************************下联的收件和寄件结束********************************/

            /**********************************下联的LOGO开始********************************/
//            if("B2C".equalsIgnoreCase(exportedShipOrderDO.getBizType()) || "B2C_APP".equalsIgnoreCase(exportedShipOrderDO.getBizType())){
//                float[] headerWidth1 = {90f};// 设置表格的列宽和列数
//                headerTable = new PdfPTable(headerWidth1);// 建立一个pdf表格
//                headerTable.setWidthPercentage(100);
//                headerTable.getDefaultCell().setBorder(0);
//
//                cell = new PdfPCell(new Paragraph("", BoldBigChinese));//描述
//                cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居中显示
//                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);  // 设置垂直居中
//                gaoJieLogo.setWidthPercentage(100);
//                gaoJieLogo.scalePercent(80f);//设置缩放的百分比%7.5
//                cell.addElement(gaoJieLogo);
//                cell.setFixedHeight(35f);
//                cell.setPadding(0);
//                cell.setBorder(0);
//                headerTable.addCell(cell);
//            }else {
                float[] headerWidth1 = {90f, 90f};// 设置表格的列宽和列数
                headerTable = new PdfPTable(headerWidth1);// 建立一个pdf表格
                headerTable.setWidthPercentage(100);
                headerTable.getDefaultCell().setBorder(0);

                cell = new PdfPCell(new Paragraph("", BoldBigChinese));//描述
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居中显示
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);  // 设置垂直居中
                gaoJieLogo.setWidthPercentage(100);
                gaoJieLogo.scalePercent(80f);//设置缩放的百分比%7.5
                cell.addElement(gaoJieLogo);
                cell.setFixedHeight(35f);
                cell.setPadding(0);
                cell.setBorder(0);
                headerTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("", BoldBigChinese));//描述
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);// 设置内容水平居中显示
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);  // 设置垂直居中
                cell.addElement(fqLogo);
                fqLogo.setWidthPercentage(100);
                fqLogo.scalePercent(80f);//设置缩放的百分比%7.5
                cell.setFixedHeight(35f);
                cell.setPadding(0);
                cell.setBorder(0);
                headerTable.addCell(cell);
//            }

            cell = new PdfPCell();
            cell.addElement(headerTable);
            cell.setColspan(2);

            table.addCell(cell);
            /****************************************下联的LOGO结束********************************************/

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
        return PdfTemplateType.GAOJIE_ZTO_SIMPLE.getValue();
    }
}
