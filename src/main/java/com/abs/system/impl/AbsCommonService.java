package com.abs.system.impl;

import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;

public abstract class AbsCommonService {

	/**
	 *** 新增
	 * 
	 * @param <T>
	 * @param entity
	 */
	public <T> void add(T entity) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.addEntity(entity);
	}

	/**
	 * 删除
	 * 
	 * @param <T>
	 * @param entity
	 */

	public <T> void delete(T entity) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.delEntity(entity);
	}

	public <T> void delByGuid(Class<?> clazz, String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.delByGuid(rowguid, clazz);
	}

	/**
	 * 修改
	 * 
	 * @param <T>
	 * @param entity
	 * @param primekey
	 */
	public <T> void update(T entity) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.updateEntity(entity, "rowguid");
	}

}
