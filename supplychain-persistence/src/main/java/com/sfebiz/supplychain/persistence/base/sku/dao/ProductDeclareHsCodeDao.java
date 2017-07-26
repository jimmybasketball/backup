/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.dao/ProductDeclareNbDirectmailDao.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareHsCodeDO;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
public interface ProductDeclareHsCodeDao extends BaseDao<ProductDeclareHsCodeDO> {

    ProductDeclareHsCodeDO getByHsCode(String hsCode);
}
