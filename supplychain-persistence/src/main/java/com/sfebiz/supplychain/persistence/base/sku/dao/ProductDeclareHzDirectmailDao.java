/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.dao/ProductDeclareHzDirectmailDao.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareHzDirectmailDO;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
public interface ProductDeclareHzDirectmailDao extends BaseDao<ProductDeclareHzDirectmailDO> {

    ProductDeclareHzDirectmailDO getByProductDeclareId(Long productDeclareId);
}
