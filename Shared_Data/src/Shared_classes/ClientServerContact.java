package Shared_classes;

import java.io.Serializable;

import Shared_classes.Command;

public class ClientServerContact implements Serializable {


	private static final long serialVersionUID = 1L;


	private Object obj1;
	private Object obj2;
	

	private Command cmd;


	
	public  ClientServerContact(Object obj1, Command cmd) {
		this.obj1 = obj1;
		this.cmd = cmd;
	}
	public  ClientServerContact(User user, Subscriber subscriber, Command cmd) {
		this.obj1 = user;
		this.cmd = cmd;
		this.obj2=subscriber;
	}

	
	public Object getObj1() {
		return obj1;
	}


	public Object getObj2() {
		return obj2;
	}
	public void setObj2(Object obj2) {
		this.obj2 = obj2;
	}
	public void setCmd(Command cmd) {
		this.cmd=cmd;
	}
	public void setObj1(Object obj1) {
		this.obj1 = obj1;
	}

	public Command getCmd() {
		return cmd;
	}


}
