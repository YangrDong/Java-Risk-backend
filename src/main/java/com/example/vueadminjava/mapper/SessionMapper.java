package com.example.vueadminjava.mapper;

import com.example.vueadminjava.config.FblMapResultConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 用于session查询
 * 
 * @author jiangliuhong
 */
@Repository
public class SessionMapper extends SqlSessionDaoSupport {

    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<Integer,String> selectParentsId(){
        FblMapResultConfig handler = new FblMapResultConfig();
        //namespace : XxxMapper.xml 中配置的地址（XxxMapper.xml的qualified name）
        //.selectXxxxNum : XxxMapper.xml 中配置的方法名称
        //this.getSqlSession().select(namespace+".selectXxxxNum", handler);
        this.getSqlSession().select(AccessMapper.class.getName()+".queryParentsId", handler);
        return (Map<Integer, String>) handler.getMappedResults();
    }

    @SuppressWarnings("unchecked")
    public Map<Integer,String> selectParentsName(){
        FblMapResultConfig handler = new FblMapResultConfig();
        //namespace : XxxMapper.xml 中配置的地址（XxxMapper.xml的qualified name）
        //.selectXxxxNum : XxxMapper.xml 中配置的方法名称
        //this.getSqlSession().select(namespace+".selectXxxxNum", handler);
        this.getSqlSession().select(AccessMapper.class.getName()+".queryParentsName", handler);
        return (Map<Integer, String>) handler.getMappedResults();
    }
}
