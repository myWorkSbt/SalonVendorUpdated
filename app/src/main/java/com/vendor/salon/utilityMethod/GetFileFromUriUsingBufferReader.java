package com.vendor.salon.utilityMethod;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;

public final class GetFileFromUriUsingBufferReader {

    static File getFile(@NotNull Context mContext, @NotNull Uri documentUri)  {
        ContentResolver var10000 = mContext.getContentResolver();
        InputStream inputStream = null;
        try {
            inputStream = var10000.openInputStream(documentUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        inputStream = var10000 != null ?  inputStream: null;
        new File("");
        Closeable var5 = (Closeable)inputStream;
        Throwable var6 = null;

        File file = null;
        try {
            InputStream input = (InputStream)var5;
            boolean var8 = false;
            file = new File(mContext.getCacheDir(), System.currentTimeMillis() + ".pdf");
            Closeable var9 = (Closeable)(new FileOutputStream(file));
            Throwable var10 = null;

            try {
                FileOutputStream output = (FileOutputStream)var9;
                boolean var12 = false;
                byte[] buffer = new byte[4096];
                int read = -1;

                while(true) {
                    Integer var15 = input != null ? input.read(buffer) : null;
                    boolean var17 = false;
                    if (var15 != null) {
                        read = var15;
                    }

                    if (var15 != null) {
                        if (var15 == -1) {
                            output.flush();
                            Unit var31 = Unit.INSTANCE;
                            break;
                        }
                    }

                    output.write(buffer, 0, read);
                }
            } catch (Throwable var26) {
                var10 = var26;
                throw var26;
            } finally {
                CloseableKt.closeFinally(var9, var10);
            }

            Unit var30 = Unit.INSTANCE;
        } catch (Throwable var28) {
            var6 = var28;
            try {
                throw var28;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            CloseableKt.closeFinally(var5, var6);
        }

        return file;
    }

    public static File getImageFile(@NotNull Context mContext, @Nullable Uri documentUri) {
        ContentResolver var10000 = mContext.getContentResolver();
        InputStream var32 = null;
        if (var10000 != null) {
            Intrinsics.checkNotNull(documentUri);
            try {
                var32 = var10000.openInputStream(documentUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            var32 = null;
        }

        InputStream inputStream = var32;
        new File("");
        Closeable var5 = (Closeable)inputStream;
        Throwable var6 = null;

        File file= null;
        try {
            InputStream input = (InputStream)var5;
            boolean var8 = false;
            file = new File(mContext.getCacheDir(), System.currentTimeMillis() + ".jpg");
            Closeable var9 = (Closeable)(new FileOutputStream(file));
            Throwable var10 = null;

            try {
                FileOutputStream output = (FileOutputStream)var9;
                boolean var12 = false;
                byte[] buffer = new byte[1024];
                int read = -1;

                while(true) {
                    Integer var15 = input != null ? input.read(buffer) : null;
                    boolean var17 = false;
                    if (var15 != null) {
                        read = var15;
                    }

                    if (var15 != null) {
                        if (var15 == -1) {
                            output.flush();
                            Unit var31 = Unit.INSTANCE;
                            break;
                        }
                    }

                    output.write(buffer, 0, read);
                }
            } catch (Throwable var26) {
                var10 = var26;
                throw var26;
            } finally {
                CloseableKt.closeFinally(var9, var10);
            }

            Unit var30 = Unit.INSTANCE;
        } catch (Throwable var28) {
            var6 = var28;
            try {
                throw var28;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            CloseableKt.closeFinally(var5, var6);
        }

        return file;
    }
}

