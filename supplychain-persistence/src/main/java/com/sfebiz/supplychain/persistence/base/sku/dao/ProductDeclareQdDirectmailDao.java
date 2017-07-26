/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.dao/ProductDeclareGzDirectmailDao.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareQdDirectmailDO;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
public interface ProductDeclareQdDirectmailDao extends BaseDao<ProductDeclareQdDirectmailDO> {

    ProductDeclareQdDirectmailDO getByProductDeclareId(Long productDeclareId);
}
