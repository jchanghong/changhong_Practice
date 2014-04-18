package changhong_Practice.uitl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import changhong_Practice.config.Constants;

import java.io.File;
import java.util.*;

/**
 * Created by changhong on 14-3-28.
 */
public class FileUtil {
    public static String getdirectory(String filepath) {
        int i = filepath.lastIndexOf("/");
        return filepath.substring(0, i);
    }

    //  /sto/s0/dd/sd.cc
    //  /sto/s1/dd/dd/ff.jpg
    public static String getoneName(String path) {
        int i2 = path.indexOf("/", 17);
        if (i2 == -1) return path;
        return path.substring(0, i2);
    }

    public static String getTwoName(String path) {
        try {

            String one = getoneName(path);
            int sum = one.length();
            sum = path.indexOf("/", sum);
            return path.substring(0, sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isoneName(String path) {
        int i = path.lastIndexOf("/");
        return i == path.length() - 1;
    }

    public static String getdirectoryName_single(String filepath) {
        int i = filepath.lastIndexOf("/");
        return filepath.substring(i + 1, filepath.length());
    }

    public static String getdirectoryName_two(String filepath) {
        int i = filepath.lastIndexOf("/");
        String files = filepath.substring(0, i);
        i = files.lastIndexOf("/");
        files = files.substring(i + 1, files.length()) + "/";
        int i2 = filepath.lastIndexOf("/");
        String last = filepath.substring(i2 + 1, filepath.length());
        return files + last;
    }

    public static Map<String, List<String>> initallfile(Set<String> strings) {
        Map<String, List<String>> allfiles = new HashMap<String, List<String>>();
        for (String s : strings) {
            String one = getoneName(s);
            if (allfiles.keySet().contains(one)) {
                if (s.length() > one.length()) {
                    allfiles.get(one).add(s);
                }
            } else {
                List<String> list = new ArrayList<String>();
                if (s.length() > one.length()) list.add(s);
                allfiles.put(one, list);
            }
        }
        return allfiles;
    }

    public static String getrealypath(String pathname) {
        File root = Environment.getExternalStorageDirectory();
        String root2s = root.getAbsolutePath().replace("0", "1");
        File root2 = new File(root2s);
        for (String s : root.list()) {
            if (s.endsWith(pathname)) {
                return s;
            }
        }
        for (String s : root2.list()) {
            if (s.endsWith(pathname)) {
                return s;
            }
        }
        return null;
    }

    public static List<String> getsubdirectory(File file, Set<String> set) {
        List<String> list = new ArrayList<String>();
        for (File f : file.listFiles()) {
            if (!f.isDirectory()) continue;
            String s = getdirectoryName_two(f.getAbsolutePath());
            if (!set.contains(s)) continue;
            list.add(getdirectoryName_two(f.getAbsolutePath()));
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(getdirectoryName_two("fff/fffff/aaac"));
        System.out.println(getoneName("/storage/sdcard1/文档/docs/distribute"));
        System.out.println(getTwoName("/storage/sdcard1/文档/docs/distribute"));
        System.out.println(isoneName("/storage/sdcard1/文档/docs/distribute/"));
    }

    public static Set<String> init_All_path(Context context) {
        Set<String> set = new HashSet<String>();
        Cursor cursor = getcursor(context);
        if (cursor == null) return null;
        while (cursor.moveToNext()) {
            String path = cursor.getString(Constants.INDEX_DATA);
            String direc = FileUtil.getdirectory(path);
            set.add(direc);
        }
        return set;
    }

    private static Cursor getcursor(Context context) {
        ContentResolver contentResolver =
                context.getContentResolver();
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                Constants.PROJECTION, null, null, null);
        return cursor;
    }
}
