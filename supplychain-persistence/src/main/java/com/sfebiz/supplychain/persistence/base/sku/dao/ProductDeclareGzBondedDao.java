/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.dao/ProductDeclareGzBondedDao.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareGzBondedDO;

import java.util.List;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
public interface ProductDeclareGzBondedDao extends BaseDao<ProductDeclareGzBondedDO> {

    List<ProductDeclareGzBondedDO> getByProductDeclareId(Long productDeclareId);
}
