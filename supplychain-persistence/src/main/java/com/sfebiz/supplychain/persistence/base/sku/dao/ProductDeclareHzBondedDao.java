/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.dao/ProductDeclareHzBondedDao.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareHzBondedDO;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
public interface ProductDeclareHzBondedDao extends BaseDao<ProductDeclareHzBondedDO> {

    ProductDeclareHzBondedDO getByProductDeclareId(Long productDeclareId);
}
