package multiplication;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.tuple.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

final class ComparisonPlot extends JFrame {
    private XYSeriesCollection dataset = new XYSeriesCollection();

    private ComparisonPlot(String title) {
        super(title);
    }

    private void createPlot() {
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Complexity growth", "Elapsed time in ms",
                "N elements in matrix",
                dataset, PlotOrientation.HORIZONTAL, true, false, false);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(255, 228, 196));

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    private void addData(MultiValueMap timeSampleMap, String title) {
        XYSeries series = new XYSeries(title);

        Set entrySet = timeSampleMap.entrySet();

        for (Object anEntrySet : entrySet) {
            Map.Entry mapEntry = (Map.Entry) anEntrySet;
            List list = (List) mapEntry.getValue();
            series.add((Double) list.get(0), (Double) mapEntry.getKey());
        }

        dataset.addSeries(series);
    }

    @SafeVarargs
    public static void display(Pair<String, MultiValueMap>... pairs) {
        ComparisonPlot plot = new ComparisonPlot("Complexity plot");

        for (Pair<String, MultiValueMap> pair : pairs) {
            plot.addData(pair.getRight(), pair.getLeft());
        }

        SwingUtilities.invokeLater(() -> {
            plot.setSize(800, 400);
            plot.setLocationRelativeTo(null);
            plot.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            plot.setVisible(true);

            plot.createPlot();
        });
    }

}
