package com.abs.system.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsVersionInfo;
import com.abs.system.domain.AbsVersionInfo;

@Service
public class VersionInfoImpl implements IAbsVersionInfo {

	@Override
	public List<AbsVersionInfo> getAllVerisonInfo(int pagesize, int pagenum, String sqlCondition) {
		return null;

	}

	@Override
	public boolean isExistVersion(String sqlCondition) {
		return false;

	}

}
