package ru.alastar.game;

import java.io.Serializable;


public class Action  implements Serializable{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public String name = "Generic Action";
  public int sendTo = 0;
  
  public Action(String n, int s)
  {
	  name = n;
	  sendTo = s;
  }
}
