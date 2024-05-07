package com.example.vueadminjava.controller;


import com.example.vueadminjava.common.lang.RespPageBean;
import com.example.vueadminjava.common.lang.Result;
import com.example.vueadminjava.entity.Question;
import com.example.vueadminjava.entity.Report;
import com.example.vueadminjava.entity.ReportDto;
import com.example.vueadminjava.entity.ReportVo;
import com.example.vueadminjava.service.IAccessService;
import com.example.vueadminjava.service.IZhibiaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/sys")
public class AssessController {

    @Autowired
    IZhibiaoService zhibiaoService;

    @Autowired
    IAccessService accessService;


    @PostMapping("/upload")
    public Result<String> upload(@RequestParam(required = false,name = "multipartFiles")MultipartFile[] multipartFiles,
                                 HttpServletRequest request){
        String urls = accessService.uploadImgs(multipartFiles,request);
        Result<String> result = new Result<>();
        result.setCode(200);
        result.setData(urls);
        result.setMsg("上传成功");
        return result;
    }


    @GetMapping("/question")
    public Result<Map<Integer,List<Question>>> question(){
        List<Question> questionList = zhibiaoService.getQuestion();
        Map<Integer,List<Question>> map = new HashMap<>();
        map.put(1,questionList);
        Result<Map<Integer,List<Question>>> result = new Result<>();
        result.setData(map);
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    /**
     * 提交所有答案，存入数据库，返回报告id
     * @param reportVo
     * @return
     */
    @PostMapping("/answer")
    public Result<Integer> answer(@RequestBody ReportVo reportVo){
        Integer reportId = accessService.saveAnswers(reportVo.getDetails(), reportVo.getTitle());
        Result<Integer> result = new Result<>();
        System.out.println("reportId===="+reportId);
        if (reportId ==null){
            result.setData(reportId);
            result.setCode(0);
            result.setMsg("报告未生成");
            return result;
        }
        result.setData(reportId);
        result.setCode(200);
        result.setMsg("保存答案成功");
        return result;
    }

    /**
     * 查询所有报告
     * @return
     */
    @GetMapping("/report")
    public Result<List<Report>> report(){
        List<Report> reports = accessService.getReports();
        Result<List<Report>> result = new Result<>();
        result.setData(reports);
        result.setCode(200);
        result.setMsg("查询报告成功");
        return result;
    }

    @GetMapping("/report/page")
    public Result<RespPageBean> reportPage(@RequestParam(value = "currPage")Integer currPage,
                                           @RequestParam(value = "pageSize")Integer pageSize,
                                           String reportDate, String reportTitle){
        Result<RespPageBean> result = new Result<>();
//        System.out.println(reportDate);
        RespPageBean bean = accessService.getReportsPage(currPage,pageSize,reportDate,reportTitle);
        result.setData(bean);
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    /**
     * 根据报告id查询报告详情
     * @param reportId
     * @return
     */
    @GetMapping("/singleReport")
    public Result<List<ReportDto>> singleReport(@RequestParam("reportId") Integer reportId){
        List<ReportDto> singleReportById = accessService.getSingleReportById(reportId);
        Result<List<ReportDto>> result = new Result<>();
        result.setData(singleReportById);
        result.setMsg("查询单个报告成功");
        result.setCode(200);
        return result;
    }


    @DeleteMapping("/delete")
    public Result<Boolean> deleteReport(@RequestParam("reportId") Integer reportId){
        boolean removeById = accessService.removeReport(reportId);
        Result<Boolean> result = new Result<>();
        if (!removeById){
            result.setData(removeById);
            result.setCode(0);
            result.setMsg("删除失败");
            return result;
        }
        result.setCode(200);
        result.setData(removeById);
        result.setMsg("删除成功");
        return result;
    }

}
