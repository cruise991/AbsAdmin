package com.abs.system.api;

import java.util.List;
import java.util.Map;

import com.abs.system.domain.AbsFileInfo;
import com.abs.system.util.Params;

public interface IAbsFileInfo {

	String getFileUrlByGuid(String fmguid);

	Object getFileUrlByClinegGuidLimit1(String cliengguid);

	List<AbsFileInfo> getFileInfoByCliengguid(String cliengguid);

	void addFileInfo(AbsFileInfo afile);
	
	public void delFileInfoByCliengGuid(String cliengguid);
	
	
	public void delFileInfoByRowGuid(String cliengguid);

	List<Map<String, Object>> getFileMapListByCliengguid(String cliengguid);

	Map<String, Object> getFileMapByCliengguid(String cliengguid);

	List<Map<String, Object>> queryPicFileList(Params params);

	long queryPicFileCount(Params params);
	


}
