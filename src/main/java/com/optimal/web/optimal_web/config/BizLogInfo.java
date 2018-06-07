package com.optimal.web.optimal_web.config;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author Dev:李代岗 Dep:On loyalty createTime:2017/11/6 下午5:15 Email:lidg@keruyun.com
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BizLogInfo {
    private String entityKey;
    @JSONField(name="service_input")
    private Object input;
    @JSONField(name="service_result")
    private Object result;
    @JSONField(name="service_errors")
    private String error;
    @JSONField(name="service_method")
    private String opFinger;
    @JSONField(name="request_uri")
    private String uri;
}
