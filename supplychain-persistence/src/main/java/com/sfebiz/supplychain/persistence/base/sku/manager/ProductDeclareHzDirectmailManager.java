/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.manager/ProductDeclareHzDirectmailManager.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.ProductDeclareHzDirectmailDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareHzDirectmailDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
@Component("productDeclareHzDirectmailManager")
public class ProductDeclareHzDirectmailManager extends BaseManager<ProductDeclareHzDirectmailDO> {

    @Resource
    private ProductDeclareHzDirectmailDao productDeclareHzDirectmailDao;

    @Override public BaseDao<ProductDeclareHzDirectmailDO> getDao() {
        return productDeclareHzDirectmailDao;
    }

    public ProductDeclareHzDirectmailDO getByProductDeclareId(Long productDeclareId) {
        return productDeclareHzDirectmailDao.getByProductDeclareId(productDeclareId);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "/Users/leno/Documents/work_at_alibaba/sf/haitao-b2c-supplychain/logistics-persistence/src/main/resources/sqlmap/product-declare-hz-directmail-sqlmap.xml",
                ProductDeclareHzDirectmailDao.class, ProductDeclareHzDirectmailDO.class,
                "sc_product_declare_hz_directmail");
    }

}
