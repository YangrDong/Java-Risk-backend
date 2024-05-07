package com.example.vueadminjava.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vueadminjava.entity.CfSysQuestion;
import com.example.vueadminjava.entity.Question;
import com.example.vueadminjava.entity.Report;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface AccessMapper extends BaseMapper<Question> {
    Boolean insertImages(@Param("imgUrls") String imgUrls,@Param("detailId") Integer detailId);

    Boolean insertQuestions(List<Question> questions);

    Integer insertAnswers(List<CfSysQuestion> cfSysQuestions);

    void insertReport(Report report);

    List<Report> queryAllReport();

    Report queryReportById(Integer reportId);

    List<CfSysQuestion> queryReportDetail(Integer reportId);

    @MapKey("detail_id")
    HashMap<Integer, String> queryParentsId();

    @MapKey("id")
    HashMap<Integer, String> queryParentsName();

    List<Report> queryReportPage(@Param("curPage") Integer curPage, @Param("pageSize") Integer pageSize,
                                 @Param("startDate") Timestamp startDate,@Param("endDate")Timestamp endDate, @Param("repName") String repName);

    Long getReportTotal(@Param("startDate") Timestamp startDate,@Param("endDate")Timestamp endDate,@Param("repName") String repName);
//    Long getReportTotal(String repName);

    Boolean deleteReportById(Integer reportId);

    Boolean deleteReportDetailsById(Integer reportId);


}
