package org.singinst.uf.model;

import java.util.List;

public class MooreConstraint extends NormalConstraint {

	public MooreConstraint(List<ScalarSchema> scalarSchemata) {
		super(scalarSchemata, 8);
	}

}
