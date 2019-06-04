package cz.cmdbuildingspringbootstarter.service;

import cz.cmdbuildingspringbootstarter.config.CmdbConfig;
import cz.cmdbuildingspringbootstarter.filter.CmdbFilter;
import cz.cmdbuildingspringbootstarter.pojo.InsertResult;
import cz.cmdbuildingspringbootstarter.pojo.SelectResult;
import cz.cmdbuildingspringbootstarter.util.SendRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenzhen
 * Created by chenzhen on 2019/5/24.
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbuildingTableOperation {

    private CmdbConfig cmdbConfig;

    /**
     * 查找指定filter的数据
     * @param tableName tableName 表名
     * @param cmdbFilter cmdbFilter 过滤
     * @param sessionId sessionId
     * @return selectResult<T>
     */
    public <T> SelectResult<T> find(String tableName, CmdbFilter cmdbFilter, String sessionId, Class<T> tClass){
        String prefix = cmdbConfig.getApi(cmdbConfig.getIp(),cmdbConfig.getPort());
        String url = prefix + "/cql";
        StringBuffer cql = new StringBuffer().append("from")
                .append(" ")
                .append(tableName).append(" ")
                .append("where ")
                .append(cmdbFilter.getCondition())
                .append("=")
                .append("'")
                .append(cmdbFilter.getConditionValue())
                .append("'");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("CQL",cql.toString());
        String encodeCql = "";
        try {
            //进行特殊字符的转义
            encodeCql = URLEncoder.encode(jsonObject.toString(),"utf-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            log.error("{}", (Object) e.getStackTrace());
        }
        String  resultString = SendRequest.sendGetRequest(url, "filter=" + encodeCql, sessionId);

        return JSONObject.parseObject(resultString,new TypeReference<SelectResult<T>>(tClass){});
    }

    /**
     * 查找指定表的所有数据
     * @param tableName tableName 表名
     * @param sessionId sessionId
     * @return selectResult<T>
     */
    public <T> SelectResult<T> find(String tableName,String sessionId,Class<T> tClass){
        String prefix = cmdbConfig.getApi(cmdbConfig.getIp(),cmdbConfig.getPort());
        String url = prefix + "/classes/"+tableName+"/cards";
        log.info("查找"+tableName+"所有数据");
        String resultString = SendRequest.sendGetRequest(url, null, sessionId);
        return JSONObject.parseObject(resultString,new TypeReference<SelectResult<T>>(tClass){});
    }

    /**
     * 查找并删除
     * @param tableName 表名
     * @param cmdbFilter cmdbFilter
     * @param sessionId sessionId
     * @return
     */
    public Integer findAndDelete(String tableName,CmdbFilter cmdbFilter,String sessionId){
        List<JSONObject> jsonObjects = this.find(tableName, cmdbFilter, sessionId, JSONObject.class).getData();
        jsonObjects.parallelStream().forEach(jsonObject -> {
            String cardId = jsonObject.getString("_id");
            this.deleteByCardId(cardId,tableName,sessionId);
        });
        return jsonObjects.size();
    }

    /**
     * 清空指定表数据
     * @param tableName 表
     * @param sessionId sessionId
     * @return
     */
    public Integer deleteAll(String tableName,String sessionId){

        log.info("开始删除表"+tableName+"所有数据");
        ArrayList<String> cardIdArrayList = this.listDataCardId(tableName,sessionId);
        //多线程删除
        cardIdArrayList.parallelStream().forEach(
                cardId -> this.deleteByCardId(cardId,tableName,sessionId));
        log.info("总共删除表"+tableName+"数据"+cardIdArrayList.size()+"条");
        return cardIdArrayList.size();
    }
    private ArrayList<String> listDataCardId(String tableName,String sessionId) {
        //存储所有查到的卡片id
        ArrayList<String> cardIdArrayList = Lists.newArrayList();

        //将查询到的所有信息转换为json格式的字符串
        String jsonString = this.listData(tableName,sessionId).toString();
        //将json格式的字符串转换为Json对象
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(jsonString);
        //将json其中的data转换为json数组
        JSONArray jsonArray = JSON.parseArray(jsonObject.get("data").toString());

        log.info("构建表"+tableName+"所有数据cardId集合");
        for(Object jsonObject1 : jsonArray){
            com.alibaba.fastjson.JSONObject jsonObject2 = com.alibaba.fastjson.JSONObject.parseObject(jsonObject1.toString());
            cardIdArrayList.add(jsonObject2.get("_id").toString());
        }

        return cardIdArrayList;
    }
    private Object listData(String tableName,String sessionId) {
        String prefix = cmdbConfig.getApi(cmdbConfig.getIp(),cmdbConfig.getPort());
        String url = prefix + "/classes/" + tableName + "/cards";

        log.info("查找"+tableName+"所有数据");

        return SendRequest.sendGetRequest(url, null,sessionId);
    }



    /**
     * 根据_id删除表中数据
     * @param cardId _id
     * @param tableName tableName 表名
     * @param sessionId sessionId
     */
    public void deleteByCardId(String cardId,String tableName,String sessionId){
        String prefix = cmdbConfig.getApi(cmdbConfig.getIp(),cmdbConfig.getPort());
        String url = prefix + "/classes/"+tableName+"/cards"+"/"+cardId;
        log.info("根据_id="+cardId+"删除表"+tableName+"中数据");
        //对CMDB库中数据进行DELETE的时候没有返回值
        try {
            SendRequest.sendRequest(url,null,sessionId,"DELETE");
        }catch (Exception e){
            //那就抓异常，如果抓到异常就认为删除失败
            e.printStackTrace();
            log.error("{},删除失败",cardId);
        }
    }

    /**
     * 更新表中数据，数据格式为t
     * @param t t
     * @param cardId cardId
     * @param tableName tableName
     * @param sessionId sessionId
     * @param <T> t
     * @return
     */
    public <T> String updateByCardId(T t,String cardId,String tableName,String sessionId){
        return this.updateByCardIdO(JSONObject.toJSONString(t),cardId,tableName,sessionId);
    }

    /**
     * 更新表中数据
     * @param jsonObject json
     * @param cardId cardId
     * @param tableName tableName
     * @param sessionId sessionId
     * @return str
     */
    public String updateJsonByCardId(JSONObject jsonObject,String cardId,String tableName,String sessionId){
        return this.updateByCardIdO(jsonObject.toJSONString(),cardId,tableName,sessionId);
    }

    /**
     * 保存表中数据,数据格式为t
     * @param t t
     * @param tableName 表名
     * @param sessionId sessionId
     * @param <T> t
     * @return t
     */
    public <T> InsertResult save(T t, String tableName, String sessionId){
        return this.saveO(JSONObject.toJSONString(t),tableName,sessionId);
    }


    /**
     * 保存表中数据，数据格式为json
     * @param dataJson 数据json
     * @param tableName 表
     * @param sessionId sessionId
     * @return insertResult
     */
    public InsertResult saveJson(JSONObject dataJson,String tableName,String sessionId){
        return this.saveO(dataJson.toJSONString(),tableName,sessionId);
    }

    /**
     * 保存数据，元方法
     * @param dataString 数据string
     * @param tableName 表
     * @param sessionId sessionId
     * @return insertResult
     */
    private InsertResult saveO(String dataString,String tableName,String sessionId){
        String prefix = cmdbConfig.getApi(cmdbConfig.getIp(),cmdbConfig.getPort());
        String url = prefix + "/classes/"+ tableName + "/cards";
        log.info("向表"+tableName+"插入一条数据"+dataString);
        String resultString = SendRequest.sendRequest(url,dataString,sessionId,"POST");
        return JSONObject.parseObject(resultString,InsertResult.class);
    }

    /**
     * 更新数据，元方法
     * @param dataString dataString
     * @param cardId cardId
     * @param tableName 表
     * @param sessionId sessionId
     */
    private String updateByCardIdO(String dataString,String cardId,String tableName,String sessionId){
        String prefix = cmdbConfig.getApi(cmdbConfig.getIp(),cmdbConfig.getPort());
        String url = prefix + "/classes/"+tableName+"/cards/"+cardId;
        try {
            SendRequest.sendRequest(url,dataString,sessionId,"PUT");
            return cardId;
        }catch (Exception e){
            return "";
        }
    }

}
