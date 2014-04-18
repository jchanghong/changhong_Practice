package changhong_Practice.uitl;

/**
 * Created by changhong on 14-3-28.
 */
public class AlubumUtil {


    private AlubumUtil() {

    }

//    public static void init_All_aulbum(Context context) {
//        Set<String> budid= Setting.get(context,Setting.KEY_bud_ID);
//        HashMap<String, Aulbum> hashMap =
//                new HashMap<String, Aulbum>();
//        Log.i("changhong", "init begin");
//        Constants.all_aulbum.clear();
//        Constants.ALLBUCKET_ID_dispaly.clear();
////        Constants.all_budid.clear();
//        Set<String> set = new HashSet<String>();
//        Cursor cursor = getcursor(context);
//        if (cursor == null) return;
//        while (cursor.moveToNext()) {
//            String bid = cursor.getString(Constants.INDEX_BUCKET_ID);
//            int size = cursor.getInt(Constants.INDEX_SIZE_ID);
////            Constants.all_budid.add(bid);
//            String path = cursor.getString(Constants.INDEX_DATA);
//            String direc=FileUtil.getdirectory(path);
//            set.add(direc);
//            if (budid==null)
//            {
//                if (size < 200 * 1024) continue;
//            }
//            else {
//
//                if (!budid.contains(bid))continue;
//            }
//            if (hashMap.keySet().contains(bid)) {
//                hashMap.get(bid).containNumber++;
//                continue;
//            }
//            long id = cursor.getLong(Constants.INDEX_ID);
//            Image image = new Image(id,FileUtil.getdirectory(path),
//                    path, bid);
//            Aulbum aulbum = new Aulbum(1, bid, image);
//            hashMap.put(bid, aulbum);
//        }
//        for (Map.Entry<String, Aulbum> entry : hashMap.entrySet()) {
//            Constants.ALLBUCKET_ID_dispaly.add(entry.getKey());
//            Constants.all_aulbum.add(entry.getValue());
//        }
//
//        cursor.close();
//        if (HomeActivity.handler!=null)
//            HomeActivity.handler.sendEmptyMessage(2);
//        Log.i("changhong", "init success");
//        FileUtil.initallfile(set);
//    }
//
//    private static Cursor getcursor(Context context) {
//        ContentResolver contentResolver =
//                context.getContentResolver();
//        Cursor cursor = contentResolver.query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//               Constants.PROJECTION, null, null, null);
//        return cursor;
//    }public static void init_All_aulbum(Context context) {
//        Set<String> budid= Setting.get(context,Setting.KEY_bud_ID);
//        HashMap<String, Aulbum> hashMap =
//                new HashMap<String, Aulbum>();
//        Log.i("changhong", "init begin");
//        Constants.all_aulbum.clear();
//        Constants.ALLBUCKET_ID_dispaly.clear();
////        Constants.all_budid.clear();
//        Set<String> set = new HashSet<String>();
//        Cursor cursor = getcursor(context);
//        if (cursor == null) return;
//        while (cursor.moveToNext()) {
//            String bid = cursor.getString(Constants.INDEX_BUCKET_ID);
//            int size = cursor.getInt(Constants.INDEX_SIZE_ID);
////            Constants.all_budid.add(bid);
//            String path = cursor.getString(Constants.INDEX_DATA);
//            String direc=FileUtil.getdirectory(path);
//            set.add(direc);
//            if (budid==null)
//            {
//                if (size < 200 * 1024) continue;
//            }
//            else {
//
//                if (!budid.contains(bid))continue;
//            }
//            if (hashMap.keySet().contains(bid)) {
//                hashMap.get(bid).containNumber++;
//                continue;
//            }
//            long id = cursor.getLong(Constants.INDEX_ID);
//            Image image = new Image(id,FileUtil.getdirectory(path),
//                    path, bid);
//            Aulbum aulbum = new Aulbum(1, bid, image);
//            hashMap.put(bid, aulbum);
//        }
//        for (Map.Entry<String, Aulbum> entry : hashMap.entrySet()) {
//            Constants.ALLBUCKET_ID_dispaly.add(entry.getKey());
//            Constants.all_aulbum.add(entry.getValue());
//        }
//
//        cursor.close();
//        if (HomeActivity.handler!=null)
//            HomeActivity.handler.sendEmptyMessage(2);
//        Log.i("changhong", "init success");
//        FileUtil.initallfile(set);
//    }
//
//    private static Cursor getcursor(Context context) {
//        ContentResolver contentResolver =
//                context.getContentResolver();
//        Cursor cursor = contentResolver.query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                Constants.PROJECTION, null, null, null);
//        return cursor;
//    }
}
