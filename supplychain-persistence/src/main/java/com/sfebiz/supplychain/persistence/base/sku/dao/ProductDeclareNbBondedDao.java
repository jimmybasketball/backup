/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.dao/ProductDeclareNbBondedDao.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareNbBondedDO;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
public interface ProductDeclareNbBondedDao extends BaseDao<ProductDeclareNbBondedDO> {

    ProductDeclareNbBondedDO getByProductDeclareId(Long productDeclareId);
}
