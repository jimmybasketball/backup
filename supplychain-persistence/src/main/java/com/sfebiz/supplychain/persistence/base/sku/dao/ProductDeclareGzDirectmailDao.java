/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.dao/ProductDeclareGzDirectmailDao.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareGzDirectmailDO;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
public interface ProductDeclareGzDirectmailDao extends BaseDao<ProductDeclareGzDirectmailDO> {

    ProductDeclareGzDirectmailDO getByProductDeclareId(Long productDeclareId);
}
