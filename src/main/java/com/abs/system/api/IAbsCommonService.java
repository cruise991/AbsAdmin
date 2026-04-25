package com.abs.system.api;

public interface IAbsCommonService {

	/**
	 *** 新增
	 * 
	 * @param <T>
	 * @param entity
	 */
	public <T> void add(T entity);

	/**
	 * 删除
	 * 
	 * @param <T>
	 * @param entity
	 */

	public <T> void delete(T entity);

	public <T> void delByGuid(Class<?> clazz, String rowguid);

	/**
	 * 修改
	 * 
	 * @param <T>
	 * @param entity
	 * @param primekey
	 */
	public <T> void update(T entity);

}
