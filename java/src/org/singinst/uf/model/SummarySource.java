package org.singinst.uf.model;

import java.util.List;

public interface SummarySource {

	List<? extends ConclusionReportGenerator> getConclusionGenerators();

	List<ScalarValueHolder> getScalarValues();

}
