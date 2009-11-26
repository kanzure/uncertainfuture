package org.singinst.uf.model;

import java.util.List;

public class Node {

	private final String idString;
	private final List<NodeMetadata> dependencies;

	public Node(String name, List<NodeMetadata> dependencies) {
		this.idString = name;
		this.dependencies = dependencies;
		
	}

	public String getIdString() {
		return idString;
	}

	public List<NodeMetadata> getDependencies() {
		return dependencies;
	}

}
