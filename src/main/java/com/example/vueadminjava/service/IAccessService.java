package com.example.vueadminjava.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vueadminjava.common.lang.RespPageBean;
import com.example.vueadminjava.entity.Question;
import com.example.vueadminjava.entity.QuestionDto;
import com.example.vueadminjava.entity.Report;
import com.example.vueadminjava.entity.ReportDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IAccessService extends IService<Question> {

    String uploadImgs(MultipartFile[] multipartFiles,HttpServletRequest request);

    Integer saveAnswers(List<QuestionDto> questionDtos,String reportName);

    List<Report> getReports();

    RespPageBean getReportsPage(Integer currentPage, Integer pageSize, String reportTime, String reportName);

    List<ReportDto> getSingleReportById(Integer reportId);

    boolean removeReport(Integer reportId);
}
