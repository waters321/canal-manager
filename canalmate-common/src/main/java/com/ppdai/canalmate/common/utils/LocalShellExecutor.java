package com.ppdai.canalmate.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalShellExecutor {

  private static Logger logger = LoggerFactory.getLogger(LocalShellExecutor.class);

  // 检查命令的白名单
  private static boolean checkCommand(String shellCommand) {
    List<String> whiteList = new ArrayList<String>();
    // 允许的命令加入白名单
    whiteList.add("scp");
    whiteList.add("start");

    boolean result = false;
    for (String cmd : whiteList) {
      if (shellCommand.indexOf(cmd) >= 0) {
        result = true;
        break;
      }
    }
    return result;
  }

  public static Map<String, Object> executeShell(String shellCommand) {
    logger.info("===将要在本地执行的shellCommand:" + shellCommand);
    if (!checkCommand(shellCommand)) {
      logger.error("===将要在本地执行的shellCommand没通过白名单检查，命令为：" + shellCommand);
    }
    int success = -1;// 如果脚本执行的返回值不是0,则表示脚本执行失败，否则（值为0）脚本执行成功。0:成功，非0：失败
    StringBuffer stringBuffer = new StringBuffer();
    BufferedReader bufferedReader = null;
    BufferedReader stdError = null;
    // 格式化日期时间，记录日志时使用
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS ");
    Map<String, Object> resultMap = new HashMap<String, Object>();

    try {
      stringBuffer.append(dateFormat.format(new Date())).append("准备执行Shell命令 ").append(shellCommand)
          .append(" \r\n");

      Process pid = null;
      String[] cmd = {"/bin/sh", "-c", shellCommand};
      // 执行Shell命令
      pid = Runtime.getRuntime().exec(cmd);
      if (pid != null) {
        stringBuffer.append("进程号：").append(pid.toString()).append("\r\n");

        // bufferedReader用于读取Shell的输出内容
        bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream()));
        // 读到标准出错的信息
        stdError = new BufferedReader(new InputStreamReader(pid.getErrorStream()));
        // 这个是或得脚本执行的返回值
        int status = pid.waitFor();

        // 如果脚本执行的返回值不是0,则表示脚本执行失败，否则（值为0）脚本执行成功。
        if (status != 0) {
          stringBuffer.append("shell脚本执行失败！");
        } else {
          stringBuffer.append("shell脚本执行成功！");
          success = 0;
        }
      } else {
        stringBuffer.append("没有pid\r\n");
      }
      stringBuffer.append(dateFormat.format(new Date())).append("Shell命令执行完毕\r\n执行结果为：\r\n");

      // 将标准输入流上面的内容写到stringBuffer里面
      String line = null;
      // 读取Shell的输出内容，并添加到stringBuffer中
      while (bufferedReader != null && (line = bufferedReader.readLine()) != null) {
        stringBuffer.append(line).append("\r\n");
      }
      // 将标准输入流上面的内容写到stringBuffer里面
      String line1 = null;
      while (stdError != null && (line1 = stdError.readLine()) != null) {
        stringBuffer.append(line1).append("\r\n");
      }
    } catch (Exception ioe) {
      stringBuffer.append("执行Shell命令时发生异常：\r\n").append(ioe.getMessage()).append("\r\n");
      logger.error(stringBuffer.toString());
    } finally {
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    resultMap.put("ret", success);
    resultMap.put("outErr", stringBuffer.toString());

    return resultMap;
  }

}
