/**
 * 
 * Bmob绉诲姩鍚庣浜戞湇鍔estAPI宸ュ叿鎵╁睍绫�
 * 
 * 渚濊禆BSON鎻愪緵绠€鍗曠殑RestAPI澧炲垹鏀规煡宸ュ叿锛屽彲鐩存帴瀵硅〃銆佷簯鍑芥暟銆佹敮浠樿鍗曘€佹秷鎭帹閫佽繘琛屾搷浣�(杩斿洖BSONObject)銆�
 * 浣跨敤鏂规硶锛氬厛鍒濆鍖杋nitBmob锛屽悗璋冪敤鍏朵粬鏂规硶鍗冲彲銆�
 * 鍏蜂綋浣跨敤鏂规硶鍙婁紶鍙傛牸寮忚瑙丅mob瀹樼綉RestAPI寮€鍙戞枃妗ｃ€�
 * http://docs.bmob.cn/restful/developdoc/index.html?menukey=develop_doc&key=develop_restful
 * 
 * @author 閲戦拱
 * @version V1.0
 * @since 2015-07-07
 * 
 */
package com.xl.util;

public class BmobE{
	/**
	 * 鍒濆鍖朆mob
	 * @param appId 濉啓 Application ID
	 * @param apiKey 濉啓 REST API Key
	 * @return 娉ㄥ唽缁撴灉
	 */
	public static boolean initBmob(String appId, String apiKey){
		return initBmob(appId, apiKey, 10000);
	}
	
	/**
	 * 鍒濆鍖朆mob
	 * @param appId 濉啓 Application ID
	 * @param apiKey 濉啓 REST API Key
	 * @param timeout 璁剧疆瓒呮椂锛�1000~20000ms锛�
	 * @return 娉ㄥ唽缁撴灉
	 */
	public static boolean initBmob(String appId, String apiKey, int timeout){
		return Bmob.initBmob(appId, apiKey, timeout);
	}

	/**
	 * 鍒濆鍖朆mob Master鏉冮檺
	 * @param masterKey 濉啓 Master Key
	 */
	public static void initMaster(String masterKey){
		Bmob.initMaster(masterKey);
	}
    
	/**
	 * 鏌ヨ琛ㄥ叏閮ㄨ褰�(鏈€澶氫粎鏌ヨ1000鏉¤褰�)
	 * @param tableName 琛ㄥ悕
	 * @return BSONObject
	 */
	public static BSONObject findAll(String tableName){
		return new BSONObject(find(tableName,BSON.CHAR_NULL));
	}

	/**
	 * 鏉′欢鏌ヨ琛ㄥ叏閮ㄨ褰�(鏈€澶氫粎鏌ヨ1000鏉¤褰�)
	 * @param tableName 琛ㄥ悕
	 * @param where 鏉′欢JOSN鏍煎紡
	 * @return BSONObject
	 */
	public static BSONObject findAll(String tableName, BSONObject where){
		return find(tableName, where, BSON.CHAR_NULL);
	}
	
	/**
	 * 鏌ヨ琛ㄥ崟鏉¤褰�
	 * @param tableName 琛ㄥ悕
	 * @param objectId objectId
	 * @return BSONObject
	 */
	public static BSONObject findOne(String tableName, String objectId){
		return resultForBSONObject(Bmob.findOne(tableName, objectId));
	}
	

	/**
	 * 鏌ヨ琛ㄩ檺瀹氭暟閲忚褰�
	 * @param tableName 琛ㄥ悕
	 * @param limit 鏌ヨ璁板綍鏁帮紙1~1000锛�
	 * @return BSONObject
	 */
	public static BSONObject find(String tableName, int limit){
		return find(tableName, new BSONObject(), 0,limit, BSON.CHAR_NULL);
	}

	/**
	 * 鏉′欢鏌ヨ琛ㄩ檺瀹氭暟閲忚褰�
	 * @param tableName 琛ㄥ悕
	 * @param where 鏉′欢JOSN鏍煎紡
	 * @param limit 鏌ヨ璁板綍鏁帮紙1~1000锛�
	 * @return BSONObject
	 */
	public static BSONObject find(String tableName, BSONObject where, int limit){
		return find(tableName, where, 0,limit, BSON.CHAR_NULL);
	}

	/**
	 * 鏉′欢鏌ヨ琛ㄩ檺瀹氭暟閲忚褰曪紝杩斿洖鎸囧畾鍒�
	 * @param tableName 琛ㄥ悕
	 * @param keys 杩斿洖鍒� 锛堜緥锛歴core,name锛�
	 * @param where 鏉′欢JOSN鏍煎紡
	 * @param limit 鏌ヨ璁板綍鏁帮紙1~1000锛�
	 * @return BSONObject
	 */
	public static BSONObject findColumns(String tableName, String keys, BSONObject where, int limit){
		return findColumns(tableName, keys, where, 0,limit, BSON.CHAR_NULL);
	}

	/**
	 * 鏌ヨ琛ㄥ尯闂磋褰�
	 * @param tableName 琛ㄥ悕
	 * @param skip 璺宠繃璁板綍鏁�
	 * @param limit 鏌ヨ璁板綍鏁帮紙1~1000锛�
	 * @return BSONObject
	 */
	public static BSONObject find(String tableName, int skip, int limit){
		return find(tableName, new BSONObject(), skip, limit, BSON.CHAR_NULL);
	}

	/**
	 * 鏉′欢鏌ヨ琛ㄥ尯闂磋褰�
	 * @param tableName 琛ㄥ悕
	 * @param where 鏉′欢JOSN鏍煎紡
	 * @param skip 璺宠繃璁板綍鏁�
	 * @param limit 鏌ヨ璁板綍鏁帮紙1~1000锛�
	 * @return BSONObject
	 */
	public static BSONObject find(String tableName, BSONObject where, int skip, int limit){
		return find(tableName, where, skip, limit, BSON.CHAR_NULL);
	}
	

	/**
	 * 鏉′欢鏌ヨ琛ㄥ尯闂磋褰�,杩斿洖鎸囧畾鍒�
	 * @param tableName 琛ㄥ悕
	 * @param keys 杩斿洖鍒� 锛堜緥锛歴core,name锛�
	 * @param where 鏉′欢JOSN鏍煎紡
	 * @param skip 璺宠繃璁板綍鏁�
	 * @param limit 鏌ヨ璁板綍鏁帮紙1~1000锛�
	 * @return BSONObject
	 */
	public static BSONObject findColumns(String tableName, String keys, BSONObject where, int skip, int limit){
		return findColumns(tableName, keys, where, skip, limit, BSON.CHAR_NULL);
	}

	/**
	 * 鎺掑簭鏌ヨ琛ㄨ褰�
	 * @param tableName 琛ㄥ悕
	 * @param order 鎺掑簭瀛楁锛堜緥锛歴core,-name锛�
	 * @return BSONObject
	 */
	public static BSONObject find(String tableName, String order){
		return find(tableName, new BSONObject(), 0, 1000, order);
	}

	/**
	 * 鏉′欢鎺掑簭鏌ヨ琛ㄨ褰�
	 * @param tableName 琛ㄥ悕
	 * @param where 鏉′欢JOSN鏍煎紡
	 * @param order 鎺掑簭瀛楁锛堜緥锛歴core,-name锛�
	 * @return BSONObject
	 */
	public static BSONObject find(String tableName, BSONObject where, String order){
		return find(tableName, where, 0, 1000, order);
	}

	/**
	 * 鏉′欢鎺掑簭鏌ヨ琛ㄨ褰�,杩斿洖鎸囧畾鍒�
	 * @param tableName 琛ㄥ悕
	 * @param keys 杩斿洖鍒� 锛堜緥锛歴core,name锛�
	 * @param where 鏉′欢JOSN鏍煎紡
	 * @param order 鎺掑簭瀛楁锛堜緥锛歴core,-name锛�
	 * @return BSONObject
	 */
	public static BSONObject findColumns(String tableName, String keys, BSONObject where, String order){
		return findColumns(tableName, keys, where, 0, 1000, order);
	}
	
	/**
	 * 鎺掑簭鏌ヨ琛ㄩ檺瀹氭暟閲忚褰�
	 * @param tableName 琛ㄥ悕
	 * @param limit 鏌ヨ璁板綍鏁帮紙1~1000锛�
	 * @param order 鎺掑簭瀛楁锛堜緥锛歴core,-name锛�
	 * @return BSONObject
	 */
	public static BSONObject find(String tableName, int limit, String order){
		return find(tableName, new BSONObject(), 0, limit, order);
	}
	
	/**
	 * 鏉′欢鎺掑簭鏌ヨ琛ㄩ檺瀹氭暟閲忚褰�
	 * @param tableName 琛ㄥ悕
	 * @param where 鏉′欢JOSN鏍煎紡
	 * @param limit 鏌ヨ璁板綍鏁帮紙1~1000锛�
	 * @param order 鎺掑簭瀛楁锛堜緥锛歴core,-name锛�
	 * @return BSONObject
	 */
	public static BSONObject find(String tableName, BSONObject where, int limit, String order){
		return find(tableName, where, 0, limit, order);
	}

	/**
	 * 鏉′欢鎺掑簭鏌ヨ琛ㄩ檺瀹氭暟閲忚褰�,杩斿洖鎸囧畾鍒�
	 * @param tableName 琛ㄥ悕
	 * @param keys 杩斿洖鍒� 锛堜緥锛歴core,name锛�
	 * @param where 鏉′欢JOSN鏍煎紡
	 * @param limit 鏌ヨ璁板綍鏁帮紙1~1000锛�
	 * @param order 鎺掑簭瀛楁锛堜緥锛歴core,-name锛�
	 * @return BSONObject
	 */
	public static BSONObject findColumns(String tableName, String keys, BSONObject where, int limit, String order){
		return findColumns(tableName, keys, where, 0, limit, order);
	}
	
	/**
	 * 鏉′欢鎺掑簭鏌ヨ琛ㄥ尯闂磋褰�
	 * @param tableName 琛ㄥ悕
	 * @param where 鏉′欢JOSN鏍煎紡
	 * @param skip 璺宠繃璁板綍鏁�
	 * @param limit 鏌ヨ璁板綍鏁帮紙1~1000锛�
	 * @param order 鎺掑簭瀛楁锛堜緥锛歴core,-name锛�
	 * @return BSONObject
	 */
	public static BSONObject find(String tableName, BSONObject where, int skip, int limit, String order){
		return findColumns(tableName, BSON.CHAR_NULL, where, skip, limit, order);
	}
	

	/**
	 * 鏉′欢鎺掑簭鏌ヨ琛ㄥ尯闂磋褰�,杩斿洖鎸囧畾鍒�
	 * @param tableName 琛ㄥ悕
	 * @param keys 杩斿洖鍒� 锛堜緥锛歴core,name锛�
	 * @param where 鏉′欢JOSN鏍煎紡
	 * @param skip 璺宠繃璁板綍鏁�
	 * @param limit 鏌ヨ璁板綍鏁帮紙1~1000锛�
	 * @param order 鎺掑簭瀛楁锛堜緥锛歴core,-name锛�
	 * @return BSONObject
	 */
	public static BSONObject findColumns(String tableName, String keys, BSONObject where, int skip, int limit, String order){
		return resultForBSONObject(Bmob.findColumns(tableName, keys, where.toString(), skip, limit, order));
	}
	
	/**
	 * BQL鏌ヨ琛ㄨ褰�
	 * @param BQL SQL璇彞銆備緥濡傦細select * from Student where name=\"寮犱笁\" limit 0,10 order by name
	 * @return BSONObject
	 */
	public static BSONObject findBQL(String BQL){
		return findBQL(BQL, BSON.CHAR_NULL);
	}
	
	/**
	 * BQL鏌ヨ琛ㄨ褰�
	 * @param BQL SQL璇彞銆備緥濡傦細select * from Student where name=? limit ?,? order by name
	 * @param value 鍙傛暟瀵瑰簲SQL涓�?浠�,涓哄垎闅旂銆備緥濡�"\"寮犱笁\",0,10"
	 * @return BSONObject
	 */
	public static BSONObject findBQL(String BQL, String value){
		return resultForBSONObject(Bmob.findBQL(BQL, value));
	}

	/**
	 * 鑾峰彇鏈嶅姟鍣ㄦ椂闂�
	 * @return BSONObject
	 */
	public static BSONObject getServerTime(){
		return resultForBSONObject(Bmob.getServerTime());
	}
	
	/**
	 * 鏌ヨ琛ㄨ褰曟暟
	 * @param tableName 琛ㄥ悕
	 * @return 缁熻鍊�
	 */
	public static int count(String tableName){
		return count(tableName, new BSONObject());
	}
	
	/**
	 * 鏉′欢鏌ヨ璁板綍鏁�
	 * @param tableName 琛ㄥ悕
	 * @param where 鏌ヨ鏉′欢(BSONObject)
	 * @return 缁熻鍊�
	 */
	public static int count(String tableName, BSONObject where){
		return Bmob.count(tableName, where.toString());
	}
	
	/**
	 * 淇敼璁板綍
	 * @param tableName 琛ㄥ悕
	 * @param objectId objectId
	 * @param paramContent BSONObject
	 * @return BSONObject
	 */
	public static BSONObject update(String tableName, String objectId, BSONObject paramContent){
		return resultForBSONObject(Bmob.update(tableName, objectId, paramContent.toString()));
	}

	/**
	 * 鎻掑叆璁板綍
	 * @param tableName 琛ㄥ悕
	 * @param paramContent BSONObject
	 * @return BSONObject
	 */
	public static BSONObject insert(String tableName, BSONObject paramContent){
		return resultForBSONObject(Bmob.insert(tableName, paramContent.toString()));
	}


	/**
	 * 鍒犻櫎璁板綍
	 * @param tableName 琛ㄥ悕
	 * @param objectId objectId
	 * @return BSONObject
	 */
	public static BSONObject delete(String tableName, String objectId){
		return resultForBSONObject(Bmob.delete(tableName, objectId));
	}
	
	/**
	 * 鏌ヨ鏀粯璁㈠崟
	 * @param payId 浜ゆ槗缂栧彿
	 * @return BSONObject
	 */
	public static BSONObject findPayOrder(String payId){
		return resultForBSONObject(Bmob.findPayOrder(payId));
	}
	
	

	/**
	 * 鎺ㄩ€佹秷鎭�
	 * @param data BSONObject
	 * 璇︾粏浣跨敤鏂规硶鍙傜収 http://docs.bmob.cn/restful/developdoc/index.html?menukey=develop_doc&key=develop_restful#index_娑堟伅鎺ㄩ€佺畝浠�
	 * @return BSONObject
	 */
	public static BSONObject pushMsg(BSONObject data){
		return resultForBSONObject(Bmob.pushMsg(data.toString()));
	}
	
	
	/**
	 * 璋冪敤浜戠浠ｇ爜
	 * @param funcName 浜戝嚱鏁板悕
	 * @param paramContent BSONObject鏍煎紡鍙傛暟
	 * @return BSONObject
	 */
	public static BSONObject callFunction(String funcName, BSONObject paramContent){
		return resultForBSONObject(Bmob.callFunction(funcName, paramContent.toString()));
	}

	/**
	 * 澶嶅悎鏌ヨ-鎴�
	 * @param where1 BSONObject鏉′欢涓€
	 * @param where2 BSONObject鏉′欢浜�
	 * @return 澶嶅悎鎴朆SONObject
	 */
	public static BSONObject whereOr(BSONObject where1, BSONObject where2){
		return whereForBSONObject(Bmob.whereOr(where1.toString(), where2.toString()));
	}

	/**
	 * 澶嶅悎鏌ヨ-涓�
	 * @param where1 BSONObject鏉′欢涓€
	 * @param where2 BSONObject鏉′欢浜�
	 * @return 澶嶅悎涓嶣SONObject
	 */
	public static BSONObject whereAnd(BSONObject where1, BSONObject where2){
		return whereForBSONObject(Bmob.whereAnd(where1.toString(), where2.toString()));
	}
	


	/**
	 * 鎿嶄綔绗�-灏忎簬
	 * @param value 鐩爣鍊�
	 * @return 澶嶅悎灏忎簬BSONObject
	 */
	public static BSONObject whereLess(int value){
		return whereForBSONObject(Bmob.whereLess(value));
	}
	/**
	 * 鎿嶄綔绗�-灏忎簬
	 * @param value 鏀寔鏃ユ湡绫诲瀷BSONObject
	 * @return 澶嶅悎灏忎簬BSONObject
	 */
	public static BSONObject whereLess(BSONObject value){
		return whereForBSONObject(Bmob.whereLess(value.toString()));
	}

	/**
	 * 鎿嶄綔绗�-灏忎簬绛変簬
	 * @param value 鐩爣鍊�
	 * @return 澶嶅悎灏忎簬绛変簬BSONObject
	 */
	public static BSONObject whereLessEqual(int value){
		return whereForBSONObject(Bmob.whereLessEqual(value));
	}
	/**
	 * 鎿嶄綔绗�-灏忎簬绛変簬
	 * @param value 鏀寔鏃ユ湡绫诲瀷BSONObject
	 * @return 澶嶅悎灏忎簬绛変簬BSONObject
	 */
	public static BSONObject whereLessEqual(BSONObject value){
		return whereForBSONObject(Bmob.whereLessEqual(value.toString()));
	}

	/**
	 * 鎿嶄綔绗�-澶т簬
	 * @param value 鐩爣鍊�
	 * @return 澶嶅悎澶т簬BSONObject
	 */
	public static BSONObject whereGreate(int value){
		return whereForBSONObject(Bmob.whereGreate(value));
	}
	/**
	 * 鎿嶄綔绗�-澶т簬
	 * @param value 鏀寔鏃ユ湡绫诲瀷BSONObject
	 * @return 澶嶅悎澶т簬BSONObject
	 */
	public static BSONObject whereGreate(BSONObject value){
		return whereForBSONObject(Bmob.whereGreate(value.toString()));
	}

	/**
	 * 鎿嶄綔绗�-澶т簬绛変簬
	 * @param value 鐩爣鍊�
	 * @return 澶嶅悎澶т簬绛変簬BSONObject
	 */
	public static BSONObject whereGreateEqual(int value){
		return whereForBSONObject(Bmob.whereGreateEqual(value));
	}
	/**
	 * 鎿嶄綔绗�-澶т簬绛変簬
	 * @param value 鏀寔鏃ユ湡绫诲瀷BSONObject
	 * @return 澶嶅悎澶т簬绛変簬BSONObject
	 */
	public static BSONObject whereGreateEqual(BSONObject value){
		return whereForBSONObject(Bmob.whereGreateEqual(value.toString()));
	}

	/**
	 * 鎿嶄綔绗�-涓嶇瓑浜�
	 * @param value 鐩爣鍊�
	 * @return 澶嶅悎涓嶇瓑浜嶣SONObject
	 */
	public static BSONObject whereNotEqual(int value){
		return whereForBSONObject(Bmob.whereNotEqual(value));
	}
	/**
	 * 鎿嶄綔绗�-涓嶇瓑浜�
	 * @param value 鏀寔鏃ユ湡绫诲瀷BSONObject
	 * @return 澶嶅悎涓嶇瓑浜嶣SONObject
	 */
	public static BSONObject whereNotEqual(BSONObject value){
		return whereForBSONObject(Bmob.whereNotEqual(value.toString()));
	}

	/**
	 * 鎿嶄綔绗�-鍖呭惈
	 * @param value 鐩爣鏁扮粍鍊�(渚嬶細new int[]{1,3,5,7})
	 * @return 澶嶅悎鍖呭惈BSONObject
	 */
	public static BSONObject whereIn(int[] value){
		return whereForBSONObject(Bmob.whereIn(value));
	}
	/**
	 * 鎿嶄綔绗�-鍖呭惈
	 * @param value 鐩爣鏁扮粍鍊�(渚嬶細new String[]{"寮犱笁","鏉庡洓","鐜嬩簲"})
	 * @return 澶嶅悎鍖呭惈BSONObject
	 */
	public static BSONObject whereIn(String[] value){
		return whereForBSONObject(Bmob.whereIn(value));
	}
	/**
	 * 鎿嶄綔绗�-鍖呭惈
	 * @param value 鐩爣鏁扮粍鍊�(渚嬶細"1,3,5,7")
	 * @return 澶嶅悎鍖呭惈BSONObject
	 */
	public static BSONObject whereIn(String value){
		return whereForBSONObject(Bmob.whereIn(value));
	}
	

	/**
	 * 鎿嶄綔绗�-涓嶅寘鍚�
	 * @param value 鐩爣鏁扮粍鍊�(渚嬶細new int[]{1,3,5,7})
	 * @return 澶嶅悎涓嶅寘鍚獴SONObject
	 */
	public static BSONObject whereNotIn(int[] value){
		return whereForBSONObject(Bmob.whereNotIn(value));
	}
	/**
	 * 鎿嶄綔绗�-涓嶅寘鍚�
	 * @param value 鐩爣鏁扮粍鍊�(渚嬶細new String[]{"寮犱笁","鏉庡洓","鐜嬩簲"})
	 * @return 澶嶅悎涓嶅寘鍚獴SONObject
	 */
	public static BSONObject whereNotIn(String[] value){
		return whereForBSONObject(Bmob.whereNotIn(value));
	}
	/**
	 * 鎿嶄綔绗�-涓嶅寘鍚�
	 * @param value 鐩爣鏁扮粍鍊�(渚嬶細"\"寮犱笁\",\"鏉庡洓\",\"鐜嬩簲\"")
	 * @return 澶嶅悎涓嶅寘鍚獴SONObject
	 */
	public static BSONObject whereNotIn(String value){
		return whereForBSONObject(Bmob.whereNotIn(value));
	}

	/**
	 * 鎿嶄綔绗�-瀛樺湪
	 * @param value 甯冨皵鍊�
	 * @return 澶嶅悎瀛樺湪BSONObject
	 */
	public static BSONObject whereExists(boolean value){
		return whereForBSONObject(Bmob.whereExists(value));
	}

	/**
	 * 鎿嶄綔绗�-鍏ㄥ寘鍚�
	 * @param value 鐩爣鍊�
	 * @return 澶嶅悎鍏ㄥ寘鍚獴SONObject
	 */
	public static BSONObject whereAll(String value){
		return whereForBSONObject(Bmob.whereAll(value));
	}


	/**
	 * 鎿嶄綔绗�-鍖洪棿鍖呭惈
	 * @param greatEqual 鏄惁澶т簬鍖呭惈绛変簬
	 * @param greatValue 澶т簬鐨勭洰鏍囧€�
	 * @param lessEqual 鏄惁灏忎簬鍖呭惈绛変簬
	 * @param lessValue 灏忎簬鐨勭洰鏍囧€�
	 * @return 澶嶅悎鍖洪棿鍖呭惈BSONObject
	 * 
	 * 渚嬶細鏌ヨ[1000,3000), whereIncluded(true,1000,false,3000)
	 */
	public static BSONObject whereIncluded(boolean greatEqual, int greatValue, boolean lessEqual, int lessValue){
		return whereIncluded(greatEqual, String.valueOf(greatValue), lessEqual, String.valueOf(lessValue));
	}
	/**
	 * 鎿嶄綔绗�-鍖洪棿鍖呭惈
	 * @param greatEqual 鏄惁澶т簬鍖呭惈绛変簬
	 * @param greatValue 澶т簬鐨勭洰鏍囧€�
	 * @param lessEqual 鏄惁灏忎簬鍖呭惈绛変簬
	 * @param lessValue 灏忎簬鐨勭洰鏍囧€�
	 * @return 澶嶅悎鍖洪棿鍖呭惈BSONObject
	 * 
	 * 渚嬶細鏌ヨ[1000,3000), whereIncluded(true,"1000",false,"3000")
	 */
	public static BSONObject whereIncluded(boolean greatEqual, String greatValue, boolean lessEqual, String lessValue){
		return whereForBSONObject(Bmob.whereIncluded(greatEqual, greatValue, lessEqual, lessValue));
	}
	

	/**
	 * 鎿嶄綔绗�-姝ｅ垯琛ㄨ揪寮�
	 * @param regexValue 
	 * @return 澶嶅悎姝ｅ垯琛ㄨ揪寮廈SONObject
	 */
	public static BSONObject whereRegex(String regexValue){
		return whereForBSONObject(Bmob.whereRegex(regexValue));
	}
	
	public static int getTimeout() {
		return Bmob.getTimeout();
	}
	public static void setTimeout(int timeout) {
		Bmob.setTimeout(timeout);
	}

	
	
	//鏌ヨ缁撴灉杞崲鎴怋SONObject
	//缁撴灉鍙兘杩斿洖null
	private static BSONObject resultForBSONObject(String result){
		BSONObject bson = null;
		if(result.equals(Bmob.MSG_UNREGISTERED) || result.contains(Bmob.MSG_NOT_FOUND) || result.contains(Bmob.MSG_ERROR)){
			BSON.Warn(result);
		}else{
			bson = new BSONObject(result);
		}
		return bson;
	}

	//鏉′欢杞崲鎴怋SONObject
	private static BSONObject whereForBSONObject(String where){
		BSONObject bson = null;
		bson = new BSONObject(where);
		return bson;
	}
	
}