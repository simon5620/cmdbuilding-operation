package io.github.whyareyousoseriously.cmdbuildingspringbootstarter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chenzhen
 * Created by chenzhen on 2019/5/24.
 */
@ConfigurationProperties("cmdb")
public class CmdbConfig {

    private String ip = "127.0.0.1";
    private Integer port = 8080;

    private String username = "admin";

    private String password = "admin";

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApi(String ip,int port){
        return "http://"+ip+":"+port+"/cmdbuild"+"/services"+"/rest"+"/v2";
    }
}
