package com.umlet.control;

import java.awt.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Role extends Rectangle {
  String _string;
  public String getString() { return _string; }
  public Role(String s, int a, int b, int c, int d) {
    super(a,b,c,d);
    _string=s;
  }
}
