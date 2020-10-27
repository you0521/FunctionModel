package com.ljkj.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件相关  有关文件流
 *
 * @author zhangbiao
 */
public class FileIOUtils {
    private static int sBufferSize = 524288;

    private FileIOUtils() {
        throw new UnsupportedOperationException("Do not need instantiate!");
    }

    /**
     * 将输入流写入文件
     *
     * @param filePath
     * @param is
     * @return
     */
    public static boolean writeFileFromIS(final String filePath, final InputStream is) {
        return writeFileFromIS(getFileByPath(filePath), is, false, null);
    }

    /**
     * 将输入流写入文件
     *
     * @param filePath
     * @param is
     * @param append
     * @return
     */
    public static boolean writeFileFromIS(final String filePath,
                                          final InputStream is,
                                          final boolean append) {
        return writeFileFromIS(getFileByPath(filePath), is, append, null);
    }

    /**
     * 将输入流写入文件
     *
     * @param file
     * @param is
     * @return
     */
    public static boolean writeFileFromIS(final File file, final InputStream is) {
        return writeFileFromIS(file, is, false, null);
    }

    /**
     * 将输入流写入文件
     *
     * @param file
     * @param is
     * @param append
     * @return
     */
    public static boolean writeFileFromIS(final File file,
                                          final InputStream is,
                                          final boolean append) {
        return writeFileFromIS(file, is, append, null);
    }

    /**
     * 将输入流写入文件
     *
     * @param filePath
     * @param is
     * @param listener
     * @return
     */
    public static boolean writeFileFromIS(final String filePath,
                                          final InputStream is,
                                          final OnProgressUpdateListener listener) {
        return writeFileFromIS(getFileByPath(filePath), is, false, listener);
    }

    /**
     * 将输入流写入文件
     *
     * @param filePath
     * @param is
     * @param append
     * @param listener
     * @return
     */
    public static boolean writeFileFromIS(final String filePath,
                                          final InputStream is,
                                          final boolean append,
                                          final OnProgressUpdateListener listener) {
        return writeFileFromIS(getFileByPath(filePath), is, append, listener);
    }

    /**
     * 将输入流写入文件
     *
     * @param file
     * @param is
     * @param listener
     * @return
     */
    public static boolean writeFileFromIS(final File file,
                                          final InputStream is,
                                          final OnProgressUpdateListener listener) {
        return writeFileFromIS(file, is, false, listener);
    }

    /**
     * 将输入流写入文件
     *
     * @param file
     * @param is
     * @param append
     * @param listener
     * @return
     */
    public static boolean writeFileFromIS(final File file,
                                          final InputStream is,
                                          final boolean append,
                                          final OnProgressUpdateListener listener) {
        if (is == null || !createOrExistsFile(file)) return false;
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file, append), sBufferSize);
            if (listener == null) {
                byte[] data = new byte[sBufferSize];
                for (int len; (len = is.read(data)) != -1; ) {
                    os.write(data, 0, len);
                }
            } else {
                double totalSize = is.available();
                int curSize = 0;
                listener.onProgressUpdate(0);
                byte[] data = new byte[sBufferSize];
                for (int len; (len = is.read(data)) != -1; ) {
                    os.write(data, 0, len);
                    curSize += len;
                    listener.onProgressUpdate(curSize / totalSize);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 将字节数组写入文件
     *
     * @param filePath
     * @param bytes
     * @return
     */
    public static boolean writeFileFromBytesByStream(final String filePath, final byte[] bytes) {
        return writeFileFromBytesByStream(getFileByPath(filePath), bytes, false, null);
    }

    /**
     * 将字节数组写入文件
     *
     * @param filePath
     * @param bytes
     * @param append
     * @return
     */
    public static boolean writeFileFromBytesByStream(final String filePath,
                                                     final byte[] bytes,
                                                     final boolean append) {
        return writeFileFromBytesByStream(getFileByPath(filePath), bytes, append, null);
    }

    /**
     * 将字节数组写入文件
     *
     * @param file
     * @param bytes
     * @return
     */
    public static boolean writeFileFromBytesByStream(final File file, final byte[] bytes) {
        return writeFileFromBytesByStream(file, bytes, false, null);
    }

    /**
     * 将字节数组写入文件
     *
     * @param file
     * @param bytes
     * @param append
     * @return
     */
    public static boolean writeFileFromBytesByStream(final File file,
                                                     final byte[] bytes,
                                                     final boolean append) {
        return writeFileFromBytesByStream(file, bytes, append, null);
    }

    /**
     * 将字节数组写入文件
     *
     * @param filePath
     * @param bytes
     * @param listener
     * @return
     */
    public static boolean writeFileFromBytesByStream(final String filePath,
                                                     final byte[] bytes,
                                                     final OnProgressUpdateListener listener) {
        return writeFileFromBytesByStream(getFileByPath(filePath), bytes, false, listener);
    }

    /**
     * 将字节数组写入文件
     *
     * @param filePath
     * @param bytes
     * @param append
     * @param listener
     * @return
     */
    public static boolean writeFileFromBytesByStream(final String filePath,
                                                     final byte[] bytes,
                                                     final boolean append,
                                                     final OnProgressUpdateListener listener) {
        return writeFileFromBytesByStream(getFileByPath(filePath), bytes, append, listener);
    }

    /**
     * 将字节数组写入文件
     *
     * @param file
     * @param bytes
     * @param listener
     * @return
     */
    public static boolean writeFileFromBytesByStream(final File file,
                                                     final byte[] bytes,
                                                     final OnProgressUpdateListener listener) {
        return writeFileFromBytesByStream(file, bytes, false, listener);
    }

    public static boolean writeFileFromBytesByStream(final File file,
                                                     final byte[] bytes,
                                                     final boolean append,
                                                     final OnProgressUpdateListener listener) {
        if (bytes == null) return false;
        return writeFileFromIS(file, new ByteArrayInputStream(bytes), append, listener);
    }

    /**
     * 将字节数组写入文件.
     *
     * @param filePath
     * @param bytes
     * @param isForce  是否写入文件
     * @return
     */
    public static boolean writeFileFromBytesByChannel(final String filePath,
                                                      final byte[] bytes,
                                                      final boolean isForce) {
        return writeFileFromBytesByChannel(getFileByPath(filePath), bytes, false, isForce);
    }

    public static boolean writeFileFromBytesByChannel(final String filePath,
                                                      final byte[] bytes,
                                                      final boolean append,
                                                      final boolean isForce) {
        return writeFileFromBytesByChannel(getFileByPath(filePath), bytes, append, isForce);
    }


    public static boolean writeFileFromBytesByChannel(final File file,
                                                      final byte[] bytes,
                                                      final boolean isForce) {
        return writeFileFromBytesByChannel(file, bytes, false, isForce);
    }


    public static boolean writeFileFromBytesByChannel(final File file,
                                                      final byte[] bytes,
                                                      final boolean append,
                                                      final boolean isForce) {
        if (bytes == null || !createOrExistsFile(file)) return false;
        FileChannel fc = null;
        try {
            fc = new FileOutputStream(file, append).getChannel();
            fc.position(fc.size());
            fc.write(ByteBuffer.wrap(bytes));
            if (isForce) fc.force(true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean writeFileFromBytesByMap(final String filePath,
                                                  final byte[] bytes,
                                                  final boolean isForce) {
        return writeFileFromBytesByMap(filePath, bytes, false, isForce);
    }


    public static boolean writeFileFromBytesByMap(final String filePath,
                                                  final byte[] bytes,
                                                  final boolean append,
                                                  final boolean isForce) {
        return writeFileFromBytesByMap(getFileByPath(filePath), bytes, append, isForce);
    }

    public static boolean writeFileFromBytesByMap(final File file,
                                                  final byte[] bytes,
                                                  final boolean isForce) {
        return writeFileFromBytesByMap(file, bytes, false, isForce);
    }

    public static boolean writeFileFromBytesByMap(final File file,
                                                  final byte[] bytes,
                                                  final boolean append,
                                                  final boolean isForce) {
        if (bytes == null || !createOrExistsFile(file)) return false;
        FileChannel fc = null;
        try {
            fc = new FileOutputStream(file, append).getChannel();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, fc.size(), bytes.length);
            mbb.put(bytes);
            if (isForce) mbb.force();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将字符串写入文件
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFileFromString(final String filePath, final String content) {
        return writeFileFromString(getFileByPath(filePath), content, false);
    }

    /**
     * 将字符串写入文件
     *
     * @param filePath
     * @param content
     * @param append
     * @return
     */
    public static boolean writeFileFromString(final String filePath,
                                              final String content,
                                              final boolean append) {
        return writeFileFromString(getFileByPath(filePath), content, append);
    }

    /**
     * 将字符串写入文件
     *
     * @param file
     * @param content
     * @return
     */
    public static boolean writeFileFromString(final File file, final String content) {
        return writeFileFromString(file, content, false);
    }

    /**
     * 将字符串写入文件
     *
     * @param file
     * @param content
     * @param append
     * @return
     */
    public static boolean writeFileFromString(final File file,
                                              final String content,
                                              final boolean append) {
        if (file == null || content == null) return false;
        if (!createOrExistsFile(file)) return false;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, append));
            bw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 读取文件到字符串链表中
     *
     * @param filePath
     * @return
     */
    public static List<String> readFile2List(final String filePath) {
        return readFile2List(getFileByPath(filePath), null);
    }

    /**
     * 读取文件到字符串链表中
     *
     * @param filePath
     * @param charsetName
     * @return
     */
    public static List<String> readFile2List(final String filePath, final String charsetName) {
        return readFile2List(getFileByPath(filePath), charsetName);
    }

    /**
     * 读取文件到字符串链表中
     *
     * @param file
     * @return
     */
    public static List<String> readFile2List(final File file) {
        return readFile2List(file, 0, 0x7FFFFFFF, null);
    }


    public static List<String> readFile2List(final File file, final String charsetName) {
        return readFile2List(file, 0, 0x7FFFFFFF, charsetName);
    }

    /**
     * 读取文件到字符串链表中
     *
     * @param filePath
     * @param st
     * @param end
     * @return
     */
    public static List<String> readFile2List(final String filePath, final int st, final int end) {
        return readFile2List(getFileByPath(filePath), st, end, null);
    }

    /**
     * Return the lines in file.
     *
     * @param filePath    The path of file.
     * @param st          The line's index of start.
     * @param end         The line's index of end.
     * @param charsetName The name of charset.
     * @return the lines in file
     */
    public static List<String> readFile2List(final String filePath,
                                             final int st,
                                             final int end,
                                             final String charsetName) {
        return readFile2List(getFileByPath(filePath), st, end, charsetName);
    }

    /**
     * 读取文件到字符串链表中
     *
     * @param file
     * @param st
     * @param end
     * @return
     */
    public static List<String> readFile2List(final File file, final int st, final int end) {
        return readFile2List(file, st, end, null);
    }

    public static List<String> readFile2List(final File file,
                                             final int st,
                                             final int end,
                                             final String charsetName) {
        if (!isFileExists(file)) return null;
        if (st > end) return null;
        BufferedReader reader = null;
        try {
            String line;
            int curLine = 1;
            List<String> list = new ArrayList<>();
            if (isSpace(charsetName)) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } else {
                reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), charsetName)
                );
            }
            while ((line = reader.readLine()) != null) {
                if (curLine > end) break;
                if (st <= curLine && curLine <= end) list.add(line);
                ++curLine;
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件到字符串中
     *
     * @param filePath
     * @return
     */
    public static String readFile2String(final String filePath) {
        return readFile2String(getFileByPath(filePath), null);
    }

    /**
     * 读取文件到字符串中
     *
     * @param filePath
     * @param charsetName
     * @return
     */
    public static String readFile2String(final String filePath, final String charsetName) {
        return readFile2String(getFileByPath(filePath), charsetName);
    }

    /**
     * 读取文件到字符串中
     *
     * @param file
     * @return
     */
    public static String readFile2String(final File file) {
        return readFile2String(file, null);
    }

    public static String readFile2String(final File file, final String charsetName) {
        byte[] bytes = readFile2BytesByStream(file);
        if (bytes == null) return null;
        if (isSpace(charsetName)) {
            return new String(bytes);
        } else {
            try {
                return new String(bytes, charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    /**
     * 读取文件到字节数组中
     *
     * @param filePath
     * @return
     */
    public static byte[] readFile2BytesByStream(final String filePath) {
        return readFile2BytesByStream(getFileByPath(filePath), null);
    }


    public static byte[] readFile2BytesByStream(final File file) {
        return readFile2BytesByStream(file, null);
    }


    public static byte[] readFile2BytesByStream(final String filePath,
                                                final OnProgressUpdateListener listener) {
        return readFile2BytesByStream(getFileByPath(filePath));
    }

    public static byte[] readFile2BytesByStream(final File file,
                                                final OnProgressUpdateListener listener) {
        if (!isFileExists(file)) return null;
        try {
            ByteArrayOutputStream os = null;
            InputStream is = new BufferedInputStream(new FileInputStream(file), sBufferSize);
            try {
                os = new ByteArrayOutputStream();
                byte[] b = new byte[sBufferSize];
                int len;
                if (listener == null) {
                    while ((len = is.read(b, 0, sBufferSize)) != -1) {
                        os.write(b, 0, len);
                    }
                } else {
                    double totalSize = is.available();
                    int curSize = 0;
                    listener.onProgressUpdate(0);
                    while ((len = is.read(b, 0, sBufferSize)) != -1) {
                        os.write(b, 0, len);
                        curSize += len;
                        listener.onProgressUpdate(curSize / totalSize);
                    }
                }
                return os.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static byte[] readFile2BytesByChannel(final String filePath) {
        return readFile2BytesByChannel(getFileByPath(filePath));
    }


    public static byte[] readFile2BytesByChannel(final File file) {
        if (!isFileExists(file)) return null;
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "r").getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fc.size());
            while (true) {
                if (!((fc.read(byteBuffer)) > 0)) break;
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static byte[] readFile2BytesByMap(final String filePath) {
        return readFile2BytesByMap(getFileByPath(filePath));
    }

    public static byte[] readFile2BytesByMap(final File file) {
        if (!isFileExists(file)) return null;
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "r").getChannel();
            int size = (int) fc.size();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size).load();
            byte[] result = new byte[size];
            mbb.get(result, 0, size);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean createOrExistsFile(final String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    private static boolean createOrExistsFile(final File file) {
        if (file == null) return false;
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static byte[] is2Bytes(final InputStream is) {
        if (is == null) return null;
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            byte[] b = new byte[sBufferSize];
            int len;
            while ((len = is.read(b, 0, sBufferSize)) != -1) {
                os.write(b, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnProgressUpdateListener {

        void onProgressUpdate(double progress);

    }
}
