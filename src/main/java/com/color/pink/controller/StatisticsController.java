package com.color.pink.controller;

import com.color.pink.annotation.ApiOperation;
import com.color.pink.service.StatisticsService;
import com.color.pink.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author HarrisonLee
 * @date 2020/5/5 9:16
 */
@RestController
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @ApiOperation("网站PV趋势")
    @GetMapping("admin/trend/pv/weekly")
    public ResponseUtil getPVTrendPastWeek() {
        final var response = ResponseUtil.factory();
        final var trend = statisticsService.getPVTrend(7);
        if(Objects.nonNull(trend)) {
            response.put("trend", trend);
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @ApiOperation("网站UV趋势")
    @GetMapping("admin/trend/uv/weekly")
    public ResponseUtil getUVTrendPastWeek() {
        final var response = ResponseUtil.factory();
        final var trend = statisticsService.getUVTrend(7);
        if(Objects.nonNull(trend)) {
            response.put("trend", trend);
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @ApiOperation("国内PV分布")
    @GetMapping("admin/visit/district")
    public ResponseUtil getProvincePVCount() {
        final var response = ResponseUtil.factory();
        final var count = statisticsService.getProvincePVCount();
        if(Objects.nonNull(count)) {
            response.put("count", count);
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @ApiOperation("国际PV分布")
    @GetMapping("admin/visit/world")
    public ResponseUtil getWorldPVCount() {
        final var response = ResponseUtil.factory();
        final var count = statisticsService.getWorldPVCount();
        if(Objects.nonNull(count)) {
            response.put("count", count);
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
