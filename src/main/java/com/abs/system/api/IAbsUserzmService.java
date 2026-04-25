package com.abs.system.api;

import com.abs.system.domain.AbsUserZm;

public interface IAbsUserzmService {

	void addUserZm(AbsUserZm zm);

	AbsUserZm getUserzmByUserguid(String userguid);

	void updateUserZm(AbsUserZm userzm);

}
