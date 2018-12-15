package com.ppdai.canalmate.api.service.canal.server;

import org.springframework.stereotype.Component;
import com.ppdai.canalmate.api.model.canal.server.CanalWarn;

@Component
public interface CanalWarnService {

  void insertCanalWarn(CanalWarn canalWarn);

  void insertDefaultCanalWarn(CanalWarn canalWarn);
}
