package com.ppdai.canalmate.api.service.canal.server.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ppdai.canalmate.api.dao.canal.server.ClientConfigMapper;
import com.ppdai.canalmate.api.model.canal.server.ClientConfig;
import com.ppdai.canalmate.api.service.canal.server.ClientConfigService;
import com.ppdai.canalmate.common.utils.RmtShellExecutor;

@Service(value = "ClientConfigService")
public class ClientConfigServiceImpl implements ClientConfigService {

  @Autowired
  private ClientConfigMapper clientConfigMapper;

  @Override
  public void addClient(ClientConfig clientConfig) {
    clientConfig.setDestinationId(
        clientConfigMapper.selectDestinationId(clientConfig.getDestinationName()));
    clientConfigMapper.insert(clientConfig);
  }

  @Override
  public void deleteClient(String clientName) {
    clientConfigMapper.deleteByClientName(clientName);
  }

  @Override
  public Map<String, Object> selectClientOldConfig(String clientName) {
    return clientConfigMapper.selectClientOldConfig(clientName);
  }

  @Override
  public void updateClient(ClientConfig clientConfig) {
    clientConfigMapper.updateByClientName(clientConfig);
  }


  @Override
  public Map<String, Object> startClient(String clientName, String type) {
    Map<String, Object> config = clientConfigMapper.selectClientConfig(clientName);
    String host = (String) config.get("host");
    String path = (String) config.get("path");
    String startClientCmd = "export PATH=$PATH:/usr/local/java/bin && /bin/bash " + path
        + "/bin/startup.sh " + clientName;
    Map<String, Object> resMap = new HashMap<>();
    if ("master".equals(type)) {
      RmtShellExecutor exe = new RmtShellExecutor(host, 23245);
      try {
        resMap = exe.exec(startClientCmd);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return resMap;
  }

  @Override
  public Map<String, Object> stopClient(String clientName, String type) {
    Map<String, Object> config = clientConfigMapper.selectClientConfig(clientName);
    String host = (String) config.get("host");
    String path = (String) config.get("path");
    String stopClientCmd = "/bin/bash " + path + "/bin/stop.sh " + clientName;
    Map<String, Object> resMap = new HashMap<>();
    if ("master".equals(type)) {
      RmtShellExecutor exe = new RmtShellExecutor(host, 23245);
      try {
        resMap = exe.exec(stopClientCmd);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return resMap;

  }


}
