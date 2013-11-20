package org.investovator.ui.analysis;

import org.investovator.analysis.technical.indicators.timeseries.TimeSeriesIndicator;
import org.investovator.analysis.technical.utils.IndicatorType;

import java.util.Collection;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class AnalysisHelper {

    static IndicatorType[] getReportTypes(){
        return IndicatorType.values();
    }
}
