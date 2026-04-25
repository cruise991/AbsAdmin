package com.abs.article.api;

import java.util.List;
import java.util.Map;

import com.abs.article.domain.AbsArticle;
import com.abs.system.util.Params;



public interface IArticleInfo {

	void addArticle(AbsArticle article);

	AbsArticle getArtilceInfoByGuid(String articleguid);

	void updateArticle(AbsArticle article);

	List<Map<String,Object>> getArticlePageList(Params params);

	long getArticleCount(Params params);

	void delArticleByGuid(String rowguid);

	Map<String, Object> queryArticleById(String id);

	List<Map<String, Object>> queryArticleByType(String btype);

	void delArticleByUserGuidAnsStatus(String userguid, String articleInit);

}
