package br.ufes.inf.lied.objects;

import java.util.ArrayList;

public class ObjetoJSON {

	private ArrayList<NodeData> nodeDataArray;
	private ArrayList<LinkData> linkDataArray;

	public ObjetoJSON() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ObjetoJSON(ArrayList<NodeData> nodeDataArray, ArrayList<LinkData> linkDataArray) {
		super();
		this.nodeDataArray = nodeDataArray;
		this.linkDataArray = linkDataArray;
	}
	
	public ArrayList<NodeData> getNodeDataArray() {
		return nodeDataArray;
	}

	public void setNodeDataArray(ArrayList<NodeData> nodeDataArray) {
		this.nodeDataArray = nodeDataArray;
	}

	public ArrayList<LinkData> getLinkDataArray() {
		return linkDataArray;
	}

	public void setLinkDataArray(ArrayList<LinkData> linkDataArray) {
		this.linkDataArray = linkDataArray;
	}
}
