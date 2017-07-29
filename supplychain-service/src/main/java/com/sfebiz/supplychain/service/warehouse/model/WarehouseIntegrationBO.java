package com.sfebiz.supplychain.service.warehouse.model;

/**
 * 
 * <p>仓库对接信息与实体仓库系统功能的对接明细业务实体</p>
 * 
 * @author matt
 * @Date 2017年7月20日 下午2:38:35
 */
public class WarehouseIntegrationBO {

    /** 是否对接了商品同步 */
    private Integer isIntegrationSkuSync;

    /** 是否对接了入库 */
    private Integer isIntegrationStockin;

    /** 是否对接了入库 */
    private Integer isIntegrationStockout;

    /** 是否对接了调拨入库 */
    private Integer isIntegrationTransIn;

    /** 是否对接了调拨出库 */
    private Integer isIntegrationTransOut;

    public Integer getIsIntegrationSkuSync() {
        return isIntegrationSkuSync;
    }

    public void setIsIntegrationSkuSync(Integer isIntegrationSkuSync) {
        this.isIntegrationSkuSync = isIntegrationSkuSync;
    }

    public Integer getIsIntegrationStockin() {
        return isIntegrationStockin;
    }

    public void setIsIntegrationStockin(Integer isIntegrationStockin) {
        this.isIntegrationStockin = isIntegrationStockin;
    }

    public Integer getIsIntegrationStockout() {
        return isIntegrationStockout;
    }

    public void setIsIntegrationStockout(Integer isIntegrationStockout) {
        this.isIntegrationStockout = isIntegrationStockout;
    }

    public Integer getIsIntegrationTransIn() {
        return isIntegrationTransIn;
    }

    public void setIsIntegrationTransIn(Integer isIntegrationTransIn) {
        this.isIntegrationTransIn = isIntegrationTransIn;
    }

    public Integer getIsIntegrationTransOut() {
        return isIntegrationTransOut;
    }

    public void setIsIntegrationTransOut(Integer isIntegrationTransOut) {
        this.isIntegrationTransOut = isIntegrationTransOut;
    }

}
