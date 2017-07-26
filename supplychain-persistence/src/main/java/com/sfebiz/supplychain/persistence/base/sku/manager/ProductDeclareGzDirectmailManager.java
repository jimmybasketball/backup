/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.manager/ProductDeclareGzDirectmailManager.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.ProductDeclareGzDirectmailDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareGzDirectmailDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
@Component("productDeclareGzDirectmailManager")
public class ProductDeclareGzDirectmailManager extends BaseManager<ProductDeclareGzDirectmailDO> {

    @Resource
    private ProductDeclareGzDirectmailDao productDeclareGzDirectmailDao;

    @Override public BaseDao<ProductDeclareGzDirectmailDO> getDao() {
        return productDeclareGzDirectmailDao;
    }

    public ProductDeclareGzDirectmailDO getByProductDeclareId(Long productDeclareId) {
        return productDeclareGzDirectmailDao.getByProductDeclareId(productDeclareId);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "/Users/leno/Documents/work_at_alibaba/sf/haitao-b2c-supplychain/logistics-persistence/src/main/resources/sqlmap/product-declare-gz-directmail-sqlmap.xml",
                ProductDeclareGzDirectmailDao.class, ProductDeclareGzDirectmailDO.class,
                "sc_product_declare_gz_directmail");
    }

}
