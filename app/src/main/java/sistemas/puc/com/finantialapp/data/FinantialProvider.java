package sistemas.puc.com.finantialapp.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
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

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
