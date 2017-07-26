/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.dao/ProductDeclareNbDirectmailDao.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareNbDirectmailDO;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
public interface ProductDeclareNbDirectmailDao extends BaseDao<ProductDeclareNbDirectmailDO> {

    ProductDeclareNbDirectmailDO getByProductDeclareId(Long productDeclareId);
}
