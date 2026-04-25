package com.abs.system.api;

import java.util.List;

import com.abs.system.domain.AbsVersionInfo;


public interface IAbsVersionInfo {

	List<AbsVersionInfo> getAllVerisonInfo(int pagesize, int pagenum, String sqlCondition);

	boolean isExistVersion(String sqlCondition);

}
