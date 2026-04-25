package com.abs.article.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.article.api.IArticleInfo;
import com.abs.article.domain.AbsArticle;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;



@Service
public class ArticleInfoImpl implements IArticleInfo{

	

	
	@Override
	public void addArticle(AbsArticle article) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.addEntity(article);
	}

	@Override
	public AbsArticle getArtilceInfoByGuid(String articleguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", articleguid);
		return dbService.queryEntityById(AbsArticle.class, "article.getArtilceInfoByGuid", params);
	
	}

	@Override
	public void updateArticle(AbsArticle article) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.updateEntity(article, "rowguid");

	}

	@Override
	public List<Map<String,Object>> getArticlePageList(Params params) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return dbService.queryPageListMapById("article.getArticlePageList", params);
	}

	@Override
	public long getArticleCount(Params params) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return dbService.queryCountByById("article.getArticleCount", params);
	}

	@Override
	public void delArticleByGuid(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid",rowguid);
		dbService.execteSqlById("article.delArticleByGuid",params);
		
	}

	@Override
	public Map<String, Object> queryArticleById(String id) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("id",id);
		return dbService.queryMapById("article.queryArticleById", params);
	}

	@Override
	public List<Map<String, Object>> queryArticleByType(String btype) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		String sql="select id,title from abs_article where btype=\'"+btype+"\' and status='4' order by addtime desc limit 5";
		return dbService.queryListForMapBySql(sql);
	}

	@Override
	public void delArticleByUserGuidAnsStatus(String userguid, String articleInit) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("author",userguid);
		params.put("status", articleInit);
		dbService.execteSqlById("article.delArticleByUserGuidAnsStatus",params);
		
	}

}
