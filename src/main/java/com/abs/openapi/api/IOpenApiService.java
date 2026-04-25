package com.abs.openapi.api;

import com.alibaba.fastjson.JSONObject;

public interface IOpenApiService {


	JSONObject queryStockList(JSONObject pjson);

	JSONObject queryBoardList(JSONObject pjson);

	JSONObject queryConceptionList(JSONObject pjson);

	JSONObject queryTradeList(JSONObject pjson);

	JSONObject queryPriceInfo(JSONObject pjson);

	JSONObject queryBoardInfo(JSONObject pjson);

	JSONObject queryProfitList(JSONObject pjson);

	JSONObject queryXSJJList(JSONObject pjson);

	JSONObject queryBoardJemxList(JSONObject pjson);

	JSONObject queryStockJemxList(JSONObject pjson);

	JSONObject queryConceptionStockList(JSONObject pjson);

}
