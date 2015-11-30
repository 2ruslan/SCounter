package rkupchinskii.scounter;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Statistic extends ActionBarActivity {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    int nameId;
    int cwt;
    int aggr;
    int parmMode;

    private GraphicalView mChartView;

    public static Date getData(String dt) {
        try {
            return sdf2.parse(dt);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        Bundle b = getIntent().getExtras();
        nameId = b.getInt(Helpers.PARM_NAME_ID);
        cwt = b.getInt(Helpers.PARM_CWT);
        aggr = b.getInt(Helpers.PARM_AGR);
        parmMode = b.getInt(Helpers.PARM_MODE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            close();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }


    protected void onResume() {
        super.onResume();
        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            mChartView = buildIntent();
            layout.addView(mChartView, new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        } else {
            mChartView.repaint();
        }
    }

    public GraphicalView buildIntent() {
        Cursor crr = ModelStatistic.getReport(this, nameId, aggr, cwt);
        crr.moveToFirst();

        XYSeries viewsSeries = new XYSeries("Views");
        XYSeriesRenderer sRenderer = new XYSeriesRenderer();
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();


        long xmax = 0;
        float ymin = 0, ymax = 0;

        for (int i = 0; i < crr.getCount(); i++) {
            float yval = crr.getFloat(crr.getColumnIndex(ModelStatistic.COLUMN_VALUE));
            Date xval = getData(crr.getString(crr.getColumnIndex(ModelStatistic.COLUMN_DATE)));

            if (yval < ymin) ymin = yval;
            if (yval > ymax) ymax = yval;
            xmax++;

            viewsSeries.add(i, yval);
            multiRenderer.addXTextLabel(i, sdf.format(xval));
            crr.moveToNext();
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(viewsSeries);

        sRenderer.setColor(Color.WHITE);

        sRenderer.setDisplayChartValues(true);
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        sRenderer.setChartValuesFormat(format);


        sRenderer.setLineWidth((float) .2);
        multiRenderer.setXLabels(0);
        multiRenderer.setYLabelsAngle(-90);
        sRenderer.setChartValuesTextSize(14);

        multiRenderer.setYAxisMin(ymin);
        multiRenderer.setYAxisMax(ymax + (ymax * 0.1));

        multiRenderer.setXAxisMax(xmax);
        multiRenderer.setXAxisMin(xmax > 7 ? xmax - 6 : 0);

        multiRenderer.setBarSpacing(.5);

        multiRenderer.setShowGrid(true);
        multiRenderer.setGridColor(Color.LTGRAY);
        multiRenderer.setAxesColor(Color.LTGRAY);

        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setBackgroundColor(Color.BLACK);
        multiRenderer.setMarginsColor(Color.BLACK);

        multiRenderer.setYLabelsAlign(Paint.Align.CENTER);
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        float val = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, metrics);
        multiRenderer.setLabelsTextSize(val);

        multiRenderer.setShowLegend(false);
        multiRenderer.setClickEnabled(false);
        multiRenderer.setInScroll(true);
        multiRenderer.setPanEnabled(true, false);

        XYSeriesRenderer.FillOutsideLine fill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
        fill.setColor(Color.LTGRAY);
        sRenderer.addFillOutsideLine(fill);

        multiRenderer.addSeriesRenderer(sRenderer);

        return parmMode == Helpers.PARAM_MEAS ?
                ChartFactory.getLineChartView(getBaseContext(), dataset, multiRenderer)
                : ChartFactory.getBarChartView(getBaseContext(), dataset, multiRenderer, BarChart.Type.DEFAULT);
    }

    public void close() {
        setResult(RESULT_CANCELED);
        finish();
    }

}
