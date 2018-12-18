package com.ppdai.canalmate.common.utils;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;


public class RmtShellExecutor {

  private Connection conn;
  private String ip;
  private String usr;
  private String keyFile;
  private int port;
  private String charset = Charset.defaultCharset().toString();
  private static Logger logger = LoggerFactory.getLogger(RmtShellExecutor.class);

  private static final int TIME_OUT = 1000 * 5 * 60;

  public RmtShellExecutor(String ip, int port) {
    this.ip = ip;
    this.usr = PropertiesUtils.getValue("osuser");
    this.keyFile = PropertiesUtils.getValue("key");
    this.port = port;
  }

  public boolean getConnection() {
    boolean result = false;
    int round = 0;
    int maxRoundConnection = 2;
    while (round < maxRoundConnection) {
      try {
        conn = new Connection(ip, port);
        conn.connect();
        boolean isAuthenticated = conn.authenticateWithPublicKey(usr, new File(keyFile), null);
        // boolean isAuthenticated = conn.authenticateWithPassword(usr, psword);
        if (isAuthenticated == false) {
          throw new IOException("Authentication failed...");
        }
        result = true;
        break;
      } catch (Exception e) {
        logger.error("获取远程服务器连接失败,远程服务器ip{}~端口{}~用户{}", ip, port, usr);
        e.printStackTrace();
        conn.close();
        conn = null;
      }
      round++;
    }
    return result;
  }


  public Map<String, Object> exec(String cmds) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    InputStream stdOut = null;
    InputStream stdErr = null;
    String outStr = "";
    String outErr = "";
    int ret = -1;
    try {
      if (getConnection()) {
        logger.info("将要远程执行命令的环境，ip:" + ip + ",	osuser:" + usr + ",	cmd:" + cmds);
        Session session = conn.openSession();
        session.execCommand(cmds);
        stdOut = new StreamGobbler(session.getStdout());
        outStr = processStream(stdOut, charset);
        resultMap.put("outStr", outStr.trim());
        stdErr = new StreamGobbler(session.getStderr());
        outErr = processStream(stdErr, charset);
        resultMap.put("outErr", outErr.trim());
        session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
        ret = session.getExitStatus();
        resultMap.put("ret", ret);

        if (!StringUtils.isBlank(outErr)) {
          logger.error("执行远程命令出错，ip:" + ip + ",	  osuser:" + usr + ",	cmd:" + cmds);
          logger.error("outStr:" + outStr);
          logger.error("outErr:" + outErr);
          logger.error("ret:" + ret);
        }
      } else {
        resultMap.put("outStr", "");
        resultMap.put("outErr", "SSH LOGIN ERROR");
        resultMap.put("ret", -1);
        logger.error("=====登录远程服务器失败,ip:" + ip + ",	  osuser:" + usr + ",	cmd:" + cmds);
        return resultMap;
      }
    } catch (Exception e) {
      logger.error("=====执行远程命令失败,ip:" + ip + ",	  osuser:" + usr + ",	cmd:" + cmds);
      e.printStackTrace();
    } finally {
      if (conn != null) {
        conn.close();
      }
      if (stdOut != null) {
        try {
          stdOut.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (stdErr != null) {
        try {
          stdErr.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return resultMap;
  }

  private String processStream(InputStream in, String charset) throws IOException {
    byte[] buf = new byte[1024];
    int len;
    StringBuilder sb = new StringBuilder();
    while ((len = in.read(buf)) != -1) {
      sb.append(new String(buf, 0, len, charset));
    }
    return sb.toString();
  }

}
