package com.ppdai.canalmate.api.service.canal.server.impl;

import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ppdai.canalmate.api.dao.canal.server.DestinationsConfigMapper;
import com.ppdai.canalmate.api.model.canal.server.DestinationsConfig;
import com.ppdai.canalmate.api.service.canal.server.DestinationsConfigService;
import com.ppdai.canalmate.common.utils.ServiceUtil;

@Service(value = "DestinationsConfigService")
public class DestinationsConfigServiceImpl implements DestinationsConfigService {

  @Autowired
  private DestinationsConfigMapper destinationsConfigMapper;

  @Override
  public void addDestination(DestinationsConfig destinationsConfig) {
    destinationsConfig.setCanalId(
        destinationsConfigMapper.selectCanalId(destinationsConfig.getCanalServerName()));
    destinationsConfigMapper.insert(destinationsConfig);
  }

  @Override
  public void deleteDestination(String destinationName) {
    destinationsConfigMapper.deleteDestinationByDestinationName(destinationName);
  }

  @Override
  public Map<String, Object> selectDestinationOldConfig(String destinationName) {
    return destinationsConfigMapper.selectDestinationOldConfigByDestinationName(destinationName);
  }

  @Override
  public void updateDestination(DestinationsConfig destinationsConfig) {
    destinationsConfigMapper.updateDestinationByDestinationName(destinationsConfig);
  }

  @Override
  public boolean deployDestination(String destinationName) {
    // 从数据库找到配置信息
    Map<String, Object> map = destinationsConfigMapper.selectDestinationDeployInfo(destinationName);
    String canalHome = (String) map.get("canalHome");
    String canalServerHost = (String) map.get("canalServerHost");
    String canalServerPort = (String) map.get("canalServerPort");
    String standbyServerHost = (String) map.get("standbyServerHost");
    String standbyServerPort = (String) map.get("standbyServerPort");
    String destinationConfiguration = (String) map.get("destinationConfiguration");
    String standbyConfiguration = (String) map.get("standbyConfiguration");

    String fileName = "/tmp/" + destinationName + "." + "instance.properties";
    String fileNameStandby = "/tmp/" + destinationName + "." + "instance.standby.properties";
    String aimPath = canalHome + "/conf/" + destinationName;
    String deployPath = aimPath + "/instance.properties";
    String deployPathStandby = aimPath + "/instance.properties";


    ServiceUtil.prepareRemoteDir(canalServerHost, canalServerPort, aimPath);
    ServiceUtil.prepareRemoteDir(standbyServerHost, standbyServerPort, aimPath);


    if (StringUtils.isNotBlank(standbyServerHost) && StringUtils.isNotBlank(standbyConfiguration)) {
      if (ServiceUtil.scpFile(fileName, destinationConfiguration, canalServerHost, canalServerPort,
          deployPath)
          && ServiceUtil.scpFile(fileNameStandby, standbyConfiguration, standbyServerHost,
              standbyServerPort, deployPathStandby)) {
        return true;
      } else {
        return false;
      }
    } else {
      return ServiceUtil.scpFile(fileName, destinationConfiguration, canalServerHost,
          canalServerPort, deployPath);
    }


  }

}
