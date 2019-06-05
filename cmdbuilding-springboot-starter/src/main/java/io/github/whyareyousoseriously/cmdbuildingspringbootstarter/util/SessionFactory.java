package io.github.whyareyousoseriously.cmdbuildingspringbootstarter.util;

import com.alibaba.fastjson.JSONObject;
import io.github.whyareyousoseriously.cmdbuildingspringbootstarter.config.CmdbConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenzhen
 * Created by chenzhen on 2018/8/29.
 */
@Slf4j
public class SessionFactory {

	/**
	 * 生成访问CMDB的sessionId
	 * @param cmdbConfig Cmdb的配置对象
	 * @return sessionId 访问Cmdb必须的sessionId
	 */
	public static String makeSessionId(CmdbConfig cmdbConfig) {

		String parm = "{\"username\":\""+cmdbConfig.getUsername()+"\",\"password\":\""+cmdbConfig.getPassword()+"\"}";

		Object sessionObject = SendRequest.sendRequest(cmdbConfig.getApi(cmdbConfig.getIp(),cmdbConfig.getPort())+"/sessions",parm,null,"POST");

		if (sessionObject != null) {
			log.info("session获取成功");
			return (String) ((JSONObject) (JSONObject.parseObject(sessionObject.toString())).get("data")).get("_id");
		}
		log.error("session获取失败");
		return "-1";
	}
}
