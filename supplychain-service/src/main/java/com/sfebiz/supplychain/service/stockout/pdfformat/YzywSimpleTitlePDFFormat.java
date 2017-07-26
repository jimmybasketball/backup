package com.sfebiz.supplychain.service.stockout.pdfformat;

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
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/1/5
 * Time: 下午4:06
 */
@Component("yzywSimpleTitlePDFFormat")
public class YzywSimpleTitlePDFFormat extends AbstractPDFFormat {

    private static final Logger logger = LoggerFactory.getLogger(YzywSimpleTitlePDFFormat.class);

    @Override
    public PdfPTable buildPdfBill(ExportedShipOrderDO element, WarehouseDO warehouseDO, LogisticsLineDO logisticsLineDO, PdfContentByte cb) throws ServiceException {
        try {
            String userName = StringUtils.isBlank(element.getDeclarePayerName()) ? element.getBuyerName() : element.getDeclarePayerName();

            String[] skuIdList = element.getSkuIdStr().split(SEPARATOR);
            String[] skuNameList = element.getSkuNameStr().split(SEPARATOR);
            String[] skuForeignNameList = element.getSkuForeignNameStr().split(SEPARATOR);
            String[] skuCountList = element.getSkuCountStr().split(SEPARATOR);
            String[] remarkList = element.getRemarks().split(SEPARATOR);

            float[] widths = {250f, 125f};// 设置表格的列宽和列数

            PdfPTable table = new PdfPTable(widths);// 建立一个pdf表格
            table.setTotalWidth(375f);// 设置表格的宽度
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);//设置表格默认为无边框

            PdfPCell cell;
            Paragraph paragraph;


            /****************************************下联 运单号，寄件开始********************************************/

            float[] width3 = {180f};// 设置表格的列宽和列数
            PdfPTable table3 = new PdfPTable(width3);// 建立一个pdf表格
            table3.setWidthPercentage(100);
            table3.setTotalWidth(180f);
            table3.getDefaultCell().setBorder(0);

            PdfPCell table3Cell;
            Image barcode = null;
            barcode = Image.getInstance(QRUtil.getBarcodeBytes(element.getId().toString(), 300, null));
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

            table3Cell = new PdfPCell(new Paragraph("出库单号 " + element.getId(), FontChinese));
            table3Cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
            table3Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
            table3Cell.setFixedHeight(20f);
            table3Cell.setBorder(0);
            table3.addCell(table3Cell);

            cell = new PdfPCell();//描述
            cell.addElement(table3);
            cell.setFixedHeight(50f);
            cell.setPadding(0);
            cell.setColspan(2);
            table.addCell(cell);



      /*****************托寄物，备注一栏结束****************************************/
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
        return PdfTemplateType.YZYW_SIMPLE_TITLE.getValue();
    }
}
