package com.example.vueadminjava.config;

import lombok.Getter;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.util.HashMap;
import java.util.Map;


@Getter
public class FblMapResultConfig implements ResultHandler {
    @SuppressWarnings("rawtypes")
    private final Map mappedResults = new HashMap();
    @Override
    public void handleResult(ResultContext resultContext) {
        @SuppressWarnings("rawtypes")
        Map map = (Map) resultContext.getResultObject();
        mappedResults.put(map.get("key"), map.get("value"));  // xml 配置里面的property的值，对应的列
    }
}
