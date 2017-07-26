/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.manager/ProductDeclareGzDirectmailManager.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.ProductDeclareQdDirectmailDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareQdDirectmailDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
@Component("productDeclareQdDirectmailManager")
public class ProductDeclareQdDirectmailManager extends BaseManager<ProductDeclareQdDirectmailDO> {

    @Resource
    private ProductDeclareQdDirectmailDao productDeclareQdDirectmailDao;

    @Override public BaseDao<ProductDeclareQdDirectmailDO> getDao() {
        return productDeclareQdDirectmailDao;
    }

    public ProductDeclareQdDirectmailDO getByProductDeclareId(Long productDeclareId) {
        return productDeclareQdDirectmailDao.getByProductDeclareId(productDeclareId);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "E:\\work\\code\\logistics\\logistics-persistence\\src\\main\\resources\\sqlmap/product-declare-qd-directmail-sqlmap.xml",
                ProductDeclareQdDirectmailDao.class, ProductDeclareQdDirectmailDO.class,
                "sc_product_declare_qd_directmail");
    }

}
