package com.ppdai.canalmate.common.utils;

import java.util.Comparator;

public class NodeIDComparator implements Comparator<Node> {
  // 按照节点编号比较
  public int compare(Node o1, Node o2) {
    int j1 = Integer.parseInt(((Node) o1).id);
    int j2 = Integer.parseInt(((Node) o2).id);
    return (j1 < j2 ? -1 : (j1 == j2 ? 0 : 1));
  }

}
