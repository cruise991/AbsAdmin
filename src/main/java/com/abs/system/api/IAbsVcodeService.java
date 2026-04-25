package com.abs.system.api;

import com.abs.system.domain.AbsVcodeInfo;

public interface IAbsVcodeService {

	void addVcodeEntity(AbsVcodeInfo code);

	boolean isExistVcode(String email, String vcode);

}
