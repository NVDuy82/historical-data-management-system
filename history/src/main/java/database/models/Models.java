package database.models;

import java.util.ArrayList;

public abstract class Models {
	protected String name;
	protected String id;
	protected String info;
	protected ArrayList<String> connect = new ArrayList<String>();

	public Models(String name, String id, String info, ArrayList<String> connect) {
		this.name = name;
		this.id = id;
		this.info = info;
		this.connect = connect;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public ArrayList<String> getConnect() {
		return connect;
	}

	public void setConnect(ArrayList<String> connect) {
		this.connect = connect;
	}

	@Override
	public String toString() {
		return name;
	}

}
