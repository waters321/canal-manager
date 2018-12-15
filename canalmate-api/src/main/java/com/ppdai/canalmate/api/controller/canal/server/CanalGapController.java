package com.ppdai.canalmate.api.controller.canal.server;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ppdai.canalmate.api.core.BaseController;
import com.ppdai.canalmate.api.core.Result;
import com.ppdai.canalmate.api.entity.dto.canal.CanalGapDto;
import com.ppdai.canalmate.api.service.canal.CanalGapService;
import com.ppdai.canalmate.api.service.canal.ProcessMonitorService;
import com.ppdai.canalmate.common.utils.ReponseEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 用户Controller
 */
@Api(value = "CanalGapController", description = "延迟监控的接口类")
@RestController
@EnableAutoConfiguration
public class CanalGapController extends BaseController {

  @Qualifier(value = "canalGapService")
  @Autowired
  CanalGapService canalGapService;

  @Qualifier(value = "processMonitorService")
  @Autowired
  ProcessMonitorService processMonitorService;

  Logger logger = LoggerFactory.getLogger(CanalGapController.class);

  @ApiOperation(value = "列出canal instance的延迟", httpMethod = "GET", response = Result.class)
  @RequestMapping(value = "/canalgap/list", method = RequestMethod.GET)
  public Result listInstanceStatus(@ApiParam(required = true,
      value = "destinations_config的canal_id，关联canal_server_config主键") @RequestParam(
          value = "canal_id", required = true) String canalId) {

    List<CanalGapDto> instanceGapList = canalGapService.listInstanceStatus(canalId);

    Result result = new Result();
    result.setCode(ReponseEnum.SUCCEED.getResCode());
    result.setMessage(ReponseEnum.SUCCEED.getResMsg());
    result.setData(instanceGapList);
    return result;
  }


}
