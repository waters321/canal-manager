package com.ppdai.canalmate.api.service.canal.server.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ppdai.canalmate.api.dao.canal.server.TbConfigMapper;
import com.ppdai.canalmate.api.model.canal.server.TbConfig;
import com.ppdai.canalmate.api.service.canal.server.TbConfigService;

@Service(value = "TbConfigService")
public class TbConfigServiceImpl implements TbConfigService {

  @Autowired
  private TbConfigMapper tbConfigMapper;

  @Override
  public int insert(TbConfig tbConfig) {
    return tbConfigMapper.insert(tbConfig);
  }

  @Override
  public TbConfig selectByConfigName(String configName) {
    return tbConfigMapper.selectByConfigName(configName);
  }


}
