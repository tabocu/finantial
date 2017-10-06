package sistemas.puc.com.finantialapp.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FinantialProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FinantialDbHelper mOpenHelper;

    static final int MOEDA   = 100;
    static final int TESOURO = 200;
    static final int INDICE  = 300;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FinantialContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, FinantialContract.PATH_MOEDA, MOEDA);
        matcher.addURI(authority, FinantialContract.PATH_TESOURO, TESOURO);
        matcher.addURI(authority, FinantialContract.PATH_INDICE, INDICE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FinantialDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOEDA:
                return FinantialContract.MoedaEntry.CONTENT_TYPE;
            case TESOURO:
                return FinantialContract.TesouroEntry.CONTENT_TYPE;
            case INDICE:
                return FinantialContract.IndiceEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOEDA: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FinantialContract.MoedaEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TESOURO: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FinantialContract.TesouroEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case INDICE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FinantialContract.IndiceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOEDA: {
                long _id = db.insert(FinantialContract.MoedaEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FinantialContract.MoedaEntry.buildMoedaUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TESOURO: {
                long _id = db.insert(FinantialContract.TesouroEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FinantialContract.TesouroEntry.buildTesouroUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case INDICE: {
                long _id = db.insert(FinantialContract.IndiceEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FinantialContract.IndiceEntry.buildIndiceUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MOEDA:
                rowsDeleted = db.delete(
                        FinantialContract.MoedaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TESOURO:
                rowsDeleted = db.delete(
                        FinantialContract.TesouroEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INDICE:
                rowsDeleted = db.delete(
                        FinantialContract.IndiceEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case MOEDA:
                rowsUpdated = db.update(FinantialContract.MoedaEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TESOURO:
                rowsUpdated = db.update(FinantialContract.TesouroEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case INDICE:
                rowsUpdated = db.update(FinantialContract.IndiceEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
