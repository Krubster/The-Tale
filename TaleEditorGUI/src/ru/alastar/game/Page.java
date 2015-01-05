package ru.alastar.game;

import java.io.Serializable;

public class Page implements Serializable{
 /**
	 * 
	 */
	private static final long serialVersionUID = 2204966219599467526L;
public String text;
 public Action[] actions;
 public Page(String text, Action[] act)
 {
	 this.text = text;
	 this.actions = act;
 }
}
