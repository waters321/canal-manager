package com.ppdai.canalmate.common.utils;

import java.io.File;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.xfer.FileSystemFile;

// https://www.programcreek.com/java-api-examples/?api=net.schmizz.sshj.SSHClient
public class SSHDUtil {
  public static void main(String[] args) throws Exception {
    SSHClient ssh = new SSHClient();
    ssh.loadKnownHosts();
    ssh.connect("IP", 22);
    try {
      // ssh.authPublickey(System.getProperty("user.name"));
      ssh.authPublickey("hadoop");
      // ssh.addHostKeyVerifier(new PromiscuousVerifier());
      ssh.loadKnownHosts(new File("C:\\Users\\testuser\\.ssh\\known_hosts"));
      /// home/x/.ssh/known_hosts
      // Present here to demo algorithm renegotiation - could have just put this before connect()
      // Make sure JZlib is in classpath for this to work
      ssh.useCompression();

      final String src = System.getProperty("user.home") + File.separator + "test_file";
      ssh.newSCPFileTransfer().upload(new FileSystemFile(src), "/tmp/");
    } finally {
      ssh.disconnect();
      if(ssh != null) {
        ssh.close();
      }
    }
  }
  // public static void clentTest() throws IOException
  // {
  // String cmd="ifconfig";
  // SshClient client=SshClient.setUpDefaultClient();
  // client.start();
  // ClientSession session=client.connect("bellring", "IP", 22).await().getSession();
  // session.addPasswordIdentity("bellring");
  // //session.addPublicKeyIdentity(SecurityUtils.loadKeyPairIdentity("keyname", new
  // FileInputStream("priKey.pem"), null));
  // if(!session.auth().await().isSuccess())
  // System.out.println("auth failed");
  //
  // ChannelExec ec=session.createExecChannel(cmd);
  // ec.setOut(System.out);
  // ec.open();
  // ec.waitFor(ClientChannel.CLOSED, 0);
  // ec.close();
  //
  // client.stop();
  // }
  //
}
