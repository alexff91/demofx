package sample;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

import javax.swing.*;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.JThermometer;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;

/**
 * A demonstration application for the thermometer plot.
 *
 * @author Bryan Scott
 */
public class Thermometer extends ApplicationFrame {

    Double value = 0d;

        final ChartPanel chartPanel;

        final DefaultValueDataset dataset;
    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public Thermometer(final String title) {

        super(title);

        // create a dataset...
        dataset = new DefaultValueDataset(value);

        // create the chart...


        final ThermometerPlot plot = new ThermometerPlot(dataset);
        final JFreeChart chart = new JFreeChart(title,  // chart title
                JFreeChart.DEFAULT_TITLE_FONT,
                plot,                 // plot
                false);               // include legend
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        plot.setInsets(new RectangleInsets(5, 5, 5, 5));
        plot.setRange(-10.0, 40.0);
        plot.setSubrangeInfo(0, -50.0, 20.0, -10.0, 22.0);
        plot.setSubrangeInfo(1, 20.0, 24.0, 18.0, 26.0);
        plot.setSubrangeInfo(2, 24.0, 100.0, 22.0, 40.0);

        plot.setThermometerStroke(new BasicStroke(2.0f));
        plot.setThermometerPaint(Color.lightGray);
        // OPTIONAL CUSTOMISATION COMPLETED.
        // add the chart to a panel...
        chartPanel = new ChartPanel(chart);
        chartPanel.setSize(150,150);
        chartPanel.setPreferredSize(new java.awt.Dimension(250, 270));
        setContentPane(chartPanel);

    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    *
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {

        final Thermometer demo = new Thermometer("Thermometer Demo 2");
        demo.pack();
        demo.setVisible(true);

    }

    public Double getValue() {
        return this.value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public ChartPanel getChartPanel() {
        return this.chartPanel;
    }
}

