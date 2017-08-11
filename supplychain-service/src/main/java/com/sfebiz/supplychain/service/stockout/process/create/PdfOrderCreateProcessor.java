package com.sfebiz.supplychain.service.stockout.process.create;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.code.LogisticsLineReturnCode;
import com.sfebiz.supplychain.exposed.common.code.StockoutReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.line.enums.PdfTemplateType;
import com.sfebiz.supplychain.persistence.base.line.domain.LogisticsLineDO;
import com.sfebiz.supplychain.persistence.base.line.manager.LogisticsLineManager;
import com.sfebiz.supplychain.persistence.base.merchant.manager.MerchantProviderManager;
import com.sfebiz.supplychain.persistence.base.stockout.domain.ExportedShipOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.service.FileOperationService;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.pdfformat.PDFBillFactory;
import com.sfebiz.supplychain.service.stockout.pdfformat.PDFFormat;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;
import com.sfebiz.supplychain.util.FileUtil;
import com.sfebiz.supplychain.util.NumberUtil;

import net.pocrd.entity.ServiceException;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 16/1/25 下午7:13
 */
@Component("pdfOrderCreateProcessor")
public class PdfOrderCreateProcessor extends StockoutProcessAction {

    public static final String               TAG              = "PDF_CREATE";
    private static final Logger              logger           = LoggerFactory
                                                                  .getLogger(PdfOrderCreateProcessor.class);
    protected static final HaitaoTraceLogger traceLogger      = HaitaoTraceLoggerFactory
                                                                  .getTraceLogger("order");

    @Resource
    StockoutOrderManager                     stockoutOrderManager;

    @Resource
    WarehouseManager                         warehouseManager;

    @Resource
    LogisticsLineManager                     logisticsLineManager;

    @Resource
    FileOperationService                     fileOperationService;

    @Resource
    PDFBillFactory                           pdfBillFactory;

    @Resource
    MerchantProviderManager                  merchantProviderManager;

    //    @Resource
    //    AutoExportShipOrderAndSendEmailTask autoExportShipOrderAndSendEmailTask;

    public static final String               CONTENT_TYPE_PDF = "application/pdf";

    @Override
    public String getProcessTag() {
        return TAG;
    }

    @Override
    public BaseResult doProcess(StockoutOrderRequest request) throws ServiceException {
        StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
        try {
            String region = null;//货主供应商地区
            if (StringUtils.isNotBlank(request.getStockoutOrderBO().getPdfRegionForOSS())) {
                region = request.getStockoutOrderBO().getPdfRegionForOSS();
            } else {
                region = merchantProviderManager
                    .queryMerchantProviderIdByNationCode(stockoutOrderBO.getDetailBOs().get(0)
                        .getMerchantProviderId());
            }
            if (StringUtils.isNotBlank(region)) {
                List<Long> stockoutOrderIdList = new ArrayList<Long>();
                stockoutOrderIdList.add(stockoutOrderBO.getId());
                List<ExportedShipOrderDO> shipOrderBOList = stockoutOrderManager
                    .query4Page4AutoShipOrder(stockoutOrderIdList);
                if (CollectionUtils.isEmpty(shipOrderBOList)) {
                    return new BaseResult(Boolean.TRUE);
                }
                // 生成PDF文件
                WarehouseDO warehouseDO = warehouseManager
                    .getById(stockoutOrderBO.getWarehouseId());
                //Map<Long, LogisticsLineDO> logisticsLineMap = new HashMap<Long, LogisticsLineDO>();
                String pdfFileName = generateShipOrderPdfName(stockoutOrderBO.getId());
                // PDF文件临时路径
                String pdfTmpPath = generateFileTmpPath(pdfFileName);
                LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("生成PDF文件临时路径")
                    .addParm("PDF文件路径", pdfTmpPath).log();
                // 创建发货单PDF文件
                buildPdfFile(shipOrderBOList, pdfTmpPath, warehouseDO);
                // 文件上传到OSS
                fileOperationService.uploadFile2OSS(CONTENT_TYPE_PDF, "supplychainStockoutorderdownload/",
                    pdfFileName, region);
                String ossFileDownloadPath = "http://"
                                             + fileOperationService
                                                 .getOssClientBucketNameByRegion(region)
                                             + "."
                                             + fileOperationService.getOssClientEndPointByRegion(
                                                 region).substring("http://".length())
                                             + "/supplychainStockoutorderdownload/" + pdfFileName;
                LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("ossPDF文件地址")
                    .addParm("PDF文件路径", ossFileDownloadPath).log();
                // 删除临时文件
                FileUtil.deleteFile(pdfTmpPath);
            }
            return new BaseResult(Boolean.TRUE);
        } catch (Exception e) {
            LogBetter
                .instance(logger)
                .setLevel(LogLevel.WARN)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                        SystemConstants.TRACE_APP)).setMsg("[物流开放平台-生成PDF面单异常]: " + e.getMessage())
                .addParm("出库单ID", stockoutOrderBO.getId())
                .addParm("包裹ID", stockoutOrderBO.getBizId()).log();
            request.setExceptionMessage("[物流开放平台-生成PDF面单异常]: " + e.getMessage());
            return new BaseResult(Boolean.FALSE);
        }
    }

    //    public String getShipOrderPdfLine(Long stockoutOrderId, String warehouseCode, String region) {
    //        if (StringUtils.isNotEmpty(region)) {
    //            OssClientWrapper ossClientWrapper = getOssClientByRegion(region, "bucketName");
    //            if (null == ossClientWrapper) {
    //                LogBetter.instance(logger)
    //                        .setLevel(LogLevel.ERROR)
    //                        .setMsg("出库单（" + stockoutOrderId + "）获取PDF链接失败: 未找到对应的OssClient")
    //                        .addParm("区域", region)
    //                        .log();
    //            } else {
    //                Date expiration = new Date(new Date().getTime() + 3 * 24 * 3600 * 1000);
    //                // 生成URL
    //                URL url = ossClientWrapper.getOssClient().generatePresignedUrl(ossClientWrapper.getBucketName(), warehouseCode + "/" + generateShipOrderPdfName(stockoutOrderId), expiration);
    //                return url.toString();
    //            }
    //        }
    //        return "";
    //    }

    //获取身份证链接
    //    public String getShipOrderCardURL(Long stockoutOrderId, String logisticsProviderId, String region, String key) {
    //        if (StringUtils.isNotEmpty(region)) {
    //            OssClientWrapper ossClientWrapper = getOssClientByRegion(region, "cardBucketName");
    //            if (null == ossClientWrapper) {
    //                LogBetter.instance(logger)
    //                        .setLevel(LogLevel.ERROR)
    //                        .setMsg("出库单（" + stockoutOrderId + "）获取身份证链接失败: 未找到对应的OssClient")
    //                        .addParm("区域", region)
    //                        .log();
    //            } else {
    //                Date expiration = new Date(new Date().getTime() + 72*60*60*1000);
    //                // 生成URL
    //                URL url = ossClientWrapper.getOssClient().generatePresignedUrl(ossClientWrapper.getBucketName(), key, expiration);
    //                return url.toString();
    //            }
    //        }
    //        return "";
    //    }

    //    public LogisticsProviderEntity getProviderByWarehouseId(Long warehouseId) {
    //        WarehouseDO entity = LogisticsBO.getInstance().getWarehouseManager().getById(warehouseId);
    //        if (entity == null) {
    //            return null;
    //        }
    //        LogisticsProviderEntity logisticsProvider = LogisticsBO.getInstance().getLogisticsProviderDetailManager().getProviderEntityById(entity.getLogisticsProviderId());
    //        if (null != logisticsProvider) {
    //            logisticsProvider.warehouseId = entity.getId();
    //        }
    //        return logisticsProvider;
    //    }

    public String generateShipOrderPdfName(Long stockoutOrderId) {
        return "shiporder-" + stockoutOrderId + ".pdf";
    }

    /**
     * 生成文件临时路径
     *
     * @param fileName
     * @return
     */
    protected String generateFileTmpPath(String fileName) {
        String tmpPath = fileOperationService.getTmpPath();
        if (tmpPath.endsWith(File.separator)) {
            return tmpPath + fileName;
        } else {
            return tmpPath + File.separator + fileName;
        }
    }

    /**
     * 根据每个包裹的PDF模板配置构建不同的PDF信息
     *
     * @param venderId
     * @param exportedShipOrderDOs
     * @param pdfPath
     * @param warehouseDO
     * @throws ServiceException
     */
    private void buildPdfFile(List<ExportedShipOrderDO> exportedShipOrderDOs, String pdfPath,
                              WarehouseDO warehouseDO) throws ServiceException {
        try {
            Map<Long, LogisticsLineDO> lineEntityMap = new HashMap<Long, LogisticsLineDO>();
            //页面展开格式，横展，默认竖展
            Document document = new Document(PageSize.A5, 5f, 5f, 10f, 10f);

            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

            document.open();
            PdfContentByte cb = pdfWriter.getDirectContent();
            StringBuilder notSupportPdfOrderIds = new StringBuilder();
            for (ExportedShipOrderDO element : exportedShipOrderDOs) {
                LogisticsLineDO logisticsLineDO;
                if (lineEntityMap.containsKey(element.getLineId())) {
                    logisticsLineDO = lineEntityMap.get(element.getLineId());
                } else {
                    logisticsLineDO = logisticsLineManager.getById(element.getLineId());
                    if (logisticsLineDO == null) {
                        throw new ServiceException(LogisticsLineReturnCode.LINE_NOT_EXIST);
                    }
                    lineEntityMap.put(element.getLineId(), logisticsLineDO);
                }
                if (StringUtils.isBlank(logisticsLineDO.getPdfTemplate())) {
                    notSupportPdfOrderIds.append(element.getBizId()).append(",");
                    continue;
                }
                if (PdfTemplateType.valueOf(logisticsLineDO.getPdfTemplate()) == null) {
                    throw new ServiceException(
                        StockoutReturnCode.STOCKOUT_ORDER_SERVICE_PDFTEMPLATE_ERROR,
                        "线路PDFTemplate配置错误");
                }
                String pdfFormatType = logisticsLineDO.getPdfTemplate();//模板类型
                PDFFormat pdfFormat = pdfBillFactory.getPDFFormat(logisticsLineDO.getPdfTemplate());
                logger.info("打印pdfFormatType：" + pdfFormatType);
                PdfPTable pdfPTable = pdfFormat.buildPdfBill(element, warehouseDO, logisticsLineDO,
                    cb);
                // 判断页的大小
                if (StringUtils.equals(pdfFormatType, PdfTemplateType.RHF_YUNDA.getValue())
                    || StringUtils.equals(pdfFormatType, PdfTemplateType.SH_YUNDA.getValue())) {
                    document.setMargins(0f, 0f, 0f, 0f);
                    document.setPageSize(new RectangleReadOnly(NumberUtil.parseMmToPt(101),
                        NumberUtil.parseMmToPt(203)));
                } else if (StringUtils.equals(pdfFormatType, PdfTemplateType.RHF_SF.getValue())) {
                    document.setMargins(0f, 0f, 0f, 0f);
                    document.setPageSize(new RectangleReadOnly(NumberUtil.parseMmToPt(101),
                        NumberUtil.parseMmToPt(203)));
                } else if (StringUtils.equals(pdfFormatType, PdfTemplateType.ACS_ACS.getValue())) {
                    document.setMargins(0f, 0f, 0f, 0f);
                    document.setPageSize(new RectangleReadOnly(NumberUtil.parseMmToPt(100),
                        NumberUtil.parseMmToPt(149)));
                } else if (StringUtils.equals(pdfFormatType, PdfTemplateType.NEW_ACS.getValue())) {
                    document.setMargins(0f, 0f, 0f, 0f);
                    document.setPageSize(new RectangleReadOnly(NumberUtil.parseMmToPt(100),
                        NumberUtil.parseMmToPt(149)));
                } else if (StringUtils.equals(pdfFormatType, PdfTemplateType.ACS_ZTO.getValue())
                           || StringUtils.equals(pdfFormatType,
                               PdfTemplateType.GAOJIE_YT.getValue())) {
                    document.setMargins(0f, 0f, 0f, 0f);
                    document.setPageSize(new RectangleReadOnly(NumberUtil.parseMmToPt(100),
                        NumberUtil.parseMmToPt(150)));
                } else if (StringUtils.equals(pdfFormatType, PdfTemplateType.YXT_ZTO_2.getValue())) {
                    document.setMargins(0f, 0f, 0f, 0f);
                    document.setPageSize(new RectangleReadOnly(NumberUtil.parseMmToPt(100),
                        NumberUtil.parseMmToPt(190)));
                } else if (StringUtils
                    .equals(pdfFormatType, PdfTemplateType.YZYW_SIMPLE.getValue())) {
                    document.setMargins(5f, 5f, 10f, 10f);
                    document.setPageSize(PageSize.A5);
                    PDFFormat pdfFormats = pdfBillFactory
                        .getPDFFormat(PdfTemplateType.YZYW_SIMPLE_TITLE.getValue());
                    PdfPTable pdfPTables = pdfFormats.buildPdfBill(element, warehouseDO,
                        logisticsLineDO, cb);
                    document.newPage();
                    document.add(pdfPTables);
                    document.setMargins(5f, 5f, 10f, 10f);
                    document.setPageSize(PageSize.A5);
                } else {
                    document.setMargins(5f, 5f, 10f, 10f);
                    document.setPageSize(PageSize.A5);
                }
                document.newPage();
                document.add(pdfPTable);
            }

            if (StringUtils.isNotBlank(notSupportPdfOrderIds.toString())) {
                notSupportPdfOrderIds.append(" 不支持使用面单");
                PDFFormat pdfFormat = pdfBillFactory
                    .getPDFFormat(PdfTemplateType.SIMPLE.getValue());
                PdfPTable pdfPTable = pdfFormat
                    .buildTextOnNewPage(notSupportPdfOrderIds.toString());
                document.setPageSize(PageSize.A5);
                document.newPage();
                document.add(pdfPTable);
            }

            document.close();
            LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[发货单生成PDF文件]:成功").log();
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR).setException(e)
                .setErrorMsg("[发货单生成PDF]:异常").log();
            if (e instanceof ServiceException) {
                throw (ServiceException) e;
            } else {
                throw new ServiceException(StockoutReturnCode.STOCKOUT_ORDER_SERVICE_PDF_ERROR);
            }
        }
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[发货单生成PDF文件]:成功").log();
    }

}
