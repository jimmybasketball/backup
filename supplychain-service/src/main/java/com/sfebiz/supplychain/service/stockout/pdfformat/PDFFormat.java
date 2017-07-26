package com.sfebiz.supplychain.service.stockout.pdfformat;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.sfebiz.supplychain.persistence.base.line.domain.LogisticsLineDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.ExportedShipOrderDO;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;

import net.pocrd.entity.ServiceException;

/**
 * <p>PDF 面单</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/1/5
 * Time: 下午3:49
 */
public interface PDFFormat {

    /**
     * 构建单个包裹的PDF面单
     *
     * @param exportedShipOrderDO
     * @param warehouseDO
     * @param lineEntity
     * @param cb
     * @return
     * @throws ServiceException
     */
    PdfPTable buildPdfBill(ExportedShipOrderDO exportedShipOrderDO, WarehouseDO warehouseDO, LogisticsLineDO logisticsLineDO, PdfContentByte cb) throws ServiceException;

    /**
     * 在一个新页面打印固定文本
     * @param text
     * @return
     */
    PdfPTable buildTextOnNewPage(String text);

    String getFormat();

}
