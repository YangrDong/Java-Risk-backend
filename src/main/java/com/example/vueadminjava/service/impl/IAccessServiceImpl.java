package com.example.vueadminjava.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vueadminjava.common.lang.Const;
import com.example.vueadminjava.common.lang.RespPageBean;
import com.example.vueadminjava.entity.*;
import com.example.vueadminjava.mapper.AccessMapper;
import com.example.vueadminjava.mapper.SessionMapper;
import com.example.vueadminjava.service.IAccessService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class IAccessServiceImpl extends ServiceImpl<AccessMapper,Question> implements IAccessService {

    @Autowired
    AccessMapper accessMapper;

    @Autowired
    SessionMapper sessionMapper;


    /**
     * 上传图片文件
     * @param multipartFiles
     * @param request
     * @return List
     */
    @Override
    public String uploadImgs(MultipartFile[] multipartFiles,HttpServletRequest request) {
        String imgUrls = "";
        ArrayList<String> imgUrlList = new ArrayList<>();
        if (!ArrayUtils.isEmpty(multipartFiles)){
            String filePath = "D:\\IDEAproject\\VueAdmin\\vueadmin-java\\src\\main\\resources\\static\\images\\";
            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            for (int i = 0; i < multipartFiles.length; i++) {
                String oldName = multipartFiles[i].getOriginalFilename();
                String newName = UUID.randomUUID().toString()+oldName.substring(oldName.lastIndexOf("."));
                try {
                    multipartFiles[i].transferTo(new File(folder,newName));
                    String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/img/"+newName;
                    imgUrlList.add(url);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            imgUrls = StringUtils.join(imgUrlList, ",");
        }

//        Boolean aBoolean = accessMapper.insertImages(imgUrls, detailId);
        return imgUrls;
    }

    /**
     * 保存答案
     * @param questionDtos
     * @return Integer
     */
    @Override
    public Integer saveAnswers(List<QuestionDto> questionDtos,String reportName) {
        List<CfSysQuestion> list = new ArrayList<>();
        //答案=1的问题的detailId的集合
        List<Integer> ids = new ArrayList<>();
        for (QuestionDto questionDto : questionDtos) {
            CfSysQuestion cfSysQuestion = new CfSysQuestion();
            cfSysQuestion.setDetailId(questionDto.getDetailId());
            if (Objects.equals(questionDto.getAnswer(), Const.ANSWER_TRUE)){
                cfSysQuestion.setAnswer(true);
                //保存id
                ids.add(cfSysQuestion.getDetailId());
            }else {
                cfSysQuestion.setAnswer(false);
            }
            cfSysQuestion.setRemark(questionDto.getRemark());
            cfSysQuestion.setImagePath(questionDto.getFiles());
            list.add(cfSysQuestion);
        }

        String answerNo0 = String.valueOf(ids);
        Integer reportId = saveReport(answerNo0,reportName);
        list.forEach(n->n.setReportId(reportId));
        //保存所有答案
        Integer inserted = accessMapper.insertAnswers(list);
        return reportId;
    }

    /**
     * 保存报告
     * @param answerNo0
     */
    public Integer saveReport(String answerNo0,String reportName) {
        Report report = new Report();
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        report.setCreateTime(time);
        report.setQuestionIds(answerNo0);
        report.setReportTitle(reportName);
        accessMapper.insertReport(report);
        return report.getId();
    }

    @Override
    public List<Report> getReports() {
        return accessMapper.queryAllReport();
    }

    @Override
    public RespPageBean getReportsPage(Integer curPage,Integer pageSize,String repDate,String repName) {
        if (pageSize!=null&&curPage!=null){
            curPage = (curPage-1)*pageSize;
        }
        Timestamp startDate = null;
        Timestamp endDate = null;
        if (!StringUtils.isEmpty(repDate)){
            String[] reDate = repDate.split(",");
            startDate =dateToTimestamp(stringToDate(reDate[0])) ;
            endDate = dateToTimestamp(stringToDate(reDate[1])) ;
            System.out.println(startDate+","+endDate);
        }
        List<Report> reports = accessMapper.queryReportPage(curPage,pageSize,startDate,endDate,repName);
        Long total = accessMapper.getReportTotal(startDate,endDate,repName);
//        Long total = accessMapper.getReportTotal(repName);
        RespPageBean bean = new RespPageBean();
        bean.setData(reports);
        bean.setTotal(total);
        System.out.println(JSON.toJSON(bean));
        return bean;
    }

    public Date stringToDate(String dateStr){
        Long longTime= Long.valueOf(dateStr);
        return new Date(longTime);
    }

    public Timestamp dateToTimestamp(Date date){
        return new Timestamp(date.getTime());
    }

    @Override
    public List<ReportDto> getSingleReportById(Integer reportId) {
//        Report report = accessMapper.queryReportById(reportId);
        //题目集合
        List<CfSysQuestion> list = accessMapper.queryReportDetail(reportId);
        //获取detail_id,parents_id键值对
        Map<Integer,String> parentsIdMap = sessionMapper.selectParentsId();
        //获取id,name键值对
        Map<Integer,String> parentsNameMap = sessionMapper.selectParentsName();

        List<ReportDto> reportDtoList = new ArrayList<>();
        //每道题的父级name
        for (CfSysQuestion cfSysQuestion : list) {
            ReportDto reportDto = new ReportDto();
            List<String> names = new ArrayList<>();
            //获取父级id集合
            String ids = parentsIdMap.get(cfSysQuestion.getDetailId());
            String[] parentIds = ids.split(",");
            for (String parentId : parentIds) {
                names.add(parentsNameMap.get(Integer.parseInt(parentId)));
            }
            reportDto.setFirstOrderName(names.get(0));
            reportDto.setSecondOrderName(names.get(1));
            reportDto.setThirdOrderName(names.get(2));
            reportDto.setFourthOrderName(names.get(3));
            reportDto.setFifthOrderName(names.get(4));
            reportDto.setQuestion(cfSysQuestion.getQuestion());
            reportDto.setRemark(cfSysQuestion.getRemark());
            reportDto.setImagePath(cfSysQuestion.getImagePath());
            reportDto.setReportId(reportId);
            reportDtoList.add(reportDto);
        }
        return reportDtoList;
    }

    @Override
    public boolean removeReport(Integer reportId) {
        //删除报告
        Boolean aBoolean = accessMapper.deleteReportById(reportId);
        //删除报告详情
        Boolean bBoolean = accessMapper.deleteReportDetailsById(reportId);

        if (!aBoolean||!bBoolean){
            return false;
        }
        return true;

    }

}
