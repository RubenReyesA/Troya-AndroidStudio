package com.appybuilder.segui09516.VCS_Troya;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Table {

    private TableLayout tableLayout;
    private ArrayList<TableRow> tableRows;
    private Fragment activity;
    private Resources rs;
    private int nRows, nColumns;

    public Table(TableLayout tableLayout, Fragment activity) {
        this.tableLayout = tableLayout;
        this.activity = activity;
        rs = this.activity.getResources();
        nRows = 0;
        nColumns = 0;
        tableRows = new ArrayList<>();
    }

    public void addHeaders(ArrayList<String> arrayListHeaders) {

        TableRow.LayoutParams layoutCell;
        TableRow row = new TableRow(activity.getContext());
        TableRow.LayoutParams layoutRow = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(layoutRow);

        this.nColumns = arrayListHeaders.size();

        for (int i = 0; i < this.nColumns; i++) {

            TextView text = new TextView(activity.getContext());

            SpannableString spanString = new SpannableString(arrayListHeaders.get(i));
            spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
            text.setText(spanString);

            text.setGravity(Gravity.CENTER_HORIZONTAL);
            text.setTextAppearance(activity.getContext(), R.style.estilo_celda);
            text.setBackgroundResource(R.drawable.table_headers);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


            layoutCell = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            text.setLayoutParams(layoutCell);

            row.addView(text);
        }

        tableLayout.addView(row);
        tableRows.add(row);

        nRows++;
    }

    public void addTableRow(ArrayList<String> items) {

        TableRow.LayoutParams layoutCell;
        TableRow.LayoutParams layoutRow = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TableRow row = new TableRow(activity.getContext());
        layoutRow.span = 3;
        row.setLayoutParams(layoutRow);

        for (int i = 0; i < items.size(); i++) {

            TextView text = new TextView(activity.getContext());
            text.setText(items.get(i));
            text.setGravity(Gravity.CENTER_HORIZONTAL);
            text.setTextAppearance(activity.getContext(), R.style.estilo_celda);
            text.setBackgroundResource(R.drawable.table_cell);
            text.setEllipsize(TextUtils.TruncateAt.END);
            text.setLines(1);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            layoutCell = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            text.setLayoutParams(layoutCell);

            row.addView(text);
        }

        tableLayout.addView(row);
        tableRows.add(row);

        nRows++;

    }

    public int getnRows() {
        return nRows;
    }

    public ArrayList<String> returnOfColumnValues(int col) {
        ArrayList<String> aux = new ArrayList<>();

        if (col > 0 && col < nColumns) {

            for (int i = 1; i < nRows; i++) {

                TextView tx = (TextView) tableRows.get(i).getChildAt(col - 1);
                aux.add(tx.getText().toString());

            }
        }

        return aux;
    }
}
