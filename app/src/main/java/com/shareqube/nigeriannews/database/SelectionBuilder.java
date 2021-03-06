package com.shareqube.nigeriannews.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by judeebene on 3/24/17.
 */

public class SelectionBuilder {

    private static final String TAG = "news247";

    private String mTable = null;
    private Map<String, String> mProjectionMap = new HashMap<String, String>();
    private StringBuilder mSelection = new StringBuilder();
    private ArrayList<String> mSelectionArgs = new ArrayList<String>();

    /**
     * Reset any internal state, allowing this builder to be recycled.
     *
     * <p>Calling this method is more efficient than creating a new SelectionBuilder object.
     *
     * @return Fluent interface
     */
    public SelectionBuilder reset() {
        mTable = null;
        mSelection.setLength(0);
        mSelectionArgs.clear();
        return this;
    }



    public SelectionBuilder where(String selection, String... selectionArgs) {
        if (TextUtils.isEmpty(selection)) {
            if (selectionArgs != null && selectionArgs.length > 0) {
                throw new IllegalArgumentException(
                        "Valid selection required when including arguments=");
            }

            // Shortcut when clause is empty
            return this;
        }

        if (mSelection.length() > 0) {
            mSelection.append(" AND ");
        }

        mSelection.append("(").append(selection).append(")");
        if (selectionArgs != null) {
            Collections.addAll(mSelectionArgs, selectionArgs);
        }

        return this;
    }

    /**
     * Table name to use for SQL {@code FROM} statement.
     *
     * <p>This method may only be called once. If multiple tables are required, concatenate them
     * in SQL-format (typically comma-separated).
     *
     * <p>If you need to do advanced {@code JOIN}s, they can also be specified here.
     *
     * See also: mapToTable()
     *
     * @param table Table name
     * @return Fluent interface
     */
    public SelectionBuilder table(String table) {
        mTable = table;
        return this;
    }

    /**
     * Verify that a table name has been supplied using table().
     *
     * @throws IllegalStateException if table not set
     */
    private void assertTable() {
        if (mTable == null) {
            throw new IllegalStateException("Table not specified");
        }
    }

    /**
     * Perform an inner join.
     *
     * <p>Map columns from a secondary table onto the current result set. References to the column
     * specified in {@code column} will be replaced with {@code table.column} in the SQL {@code
     * SELECT} clause.
     *
     * @param column Column name to join on. Must be the same in both tables.
     * @param table Secondary table to join.
     * @return Fluent interface
     */
    public SelectionBuilder mapToTable(String column, String table) {
        mProjectionMap.put(column, table + "." + column);
        return this;
    }

    /**
     * Create a new column based on custom criteria (such as aggregate functions).
     *
     * <p>This adds a new column to the result set, based upon custom criteria in SQL format. This
     * is equivalent to the SQL statement: {@code SELECT toClause AS fromColumn}
     *
     * <p>This method is useful for executing SQL sub-queries.
     *
     * @param fromColumn Name of column for mapping
     * @param toClause SQL string representing data to be mapped
     * @return Fluent interface
     */
    public SelectionBuilder map(String fromColumn, String toClause) {
        mProjectionMap.put(fromColumn, toClause + " AS " + fromColumn);
        return this;
    }

    /**
     * Return selection string based on current internal state.
     *
     * @return Current selection as a SQL statement
     * @see #getSelectionArgs()
     */
    public String getSelection() {
        return mSelection.toString();

    }

    /**
     * Return selection arguments based on current internal state.
     *
     * @see #getSelection()
     */
    public String[] getSelectionArgs() {
        return mSelectionArgs.toArray(new String[mSelectionArgs.size()]);
    }

    /**
     * Process user-supplied projection (column list).
     *
     * <p>In cases where a column is mapped to another data source (either another table, or an
     * SQL sub-query), the column name will be replaced with a more specific, SQL-compatible
     * representation.
     *
     * Assumes that incoming columns are non-null.
     *
     * <p>See also: map(), mapToTable()
     *
     * @param columns User supplied projection (column list).
     */
    private void mapColumns(String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            final String target = mProjectionMap.get(columns[i]);
            if (target != null) {
                columns[i] = target;
            }
        }
    }

    /**
     * Return a description of this builder's state. Does NOT output SQL.
     *
     * @return Human-readable internal state
     */
    @Override
    public String toString() {
        return "SelectionBuilder[table=" + mTable + ", selection=" + getSelection()
                + ", selectionArgs=" + Arrays.toString(getSelectionArgs()) + "]";
    }

    /**
     * Execute query (SQL {@code SELECT}) against specified com.shareqube.nigeriannews.database.
     *
     * <p>Using a null projection (column list) is not supported.
     *
     * @param db Database to query.
     * @param columns Database projection (column list) to return, must be non-NULL.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause (excluding the
     *                ORDER BY itself). Passing null will use the default sort order, which may be
     *                unordered.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     *         {@link Cursor}s are not synchronized, see the documentation for more details.
     */
    public Cursor query(SQLiteDatabase db, String[] columns, String orderBy) {
        return query(db, columns, null, null, orderBy, null);
    }

    /**
     * Execute query ({@code SELECT}) against com.shareqube.nigeriannews.database.
     *
     * <p>Using a null projection (column list) is not supported.
     *
     * @param db Database to query.
     * @param columns Database projection (column list) to return, must be non-null.
     * @param groupBy A filter declaring how to group rows, formatted as an SQL GROUP BY clause
     *                (excluding the GROUP BY itself). Passing null will cause the rows to not be
     *                grouped.
     * @param having A filter declare which row groups to include in the cursor, if row grouping is
     *               being used, formatted as an SQL HAVING clause (excluding the HAVING itself).
     *               Passing null will cause all row groups to be included, and is required when
     *               row grouping is not being used.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause (excluding the
     *                ORDER BY itself). Passing null will use the default sort order, which may be
     *                unordered.
     * @param limit Limits the number of rows returned by the query, formatted as LIMIT clause.
     *              Passing null denotes no LIMIT clause.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     *         {@link Cursor}s are not synchronized, see the documentation for more details.
     */
    public Cursor query(SQLiteDatabase db, String[] columns, String groupBy,
                        String having, String orderBy, String limit) {
        assertTable();
        if (columns != null) mapColumns(columns);
        Log.v(TAG, "query(columns=" + Arrays.toString(columns) + ") " + this);
        return db.query(mTable, columns, getSelection(), getSelectionArgs(), groupBy, having,
                orderBy, limit);
    }

    /**
     * Execute an {@code UPDATE} against com.shareqube.nigeriannews.database.
     *
     * @param db Database to query.
     * @param values A map from column names to new column values. null is a valid value that will
     *               be translated to NULL
     * @return The number of rows affected.
     */
    public int update(SQLiteDatabase db, ContentValues values) {
        assertTable();
        Log.v(TAG, "update() " + this);
        return db.update(mTable, values, getSelection(), getSelectionArgs());
    }

    /**
     * Execute {@code DELETE} against com.shareqube.nigeriannews.database.
     *
     * @param db Database to query.
     * @return The number of rows affected.
     */
    public int delete(SQLiteDatabase db) {
        assertTable();
        Log.v(TAG, "delete() " + this);
        return db.delete(mTable, getSelection(), getSelectionArgs());
    }
}
