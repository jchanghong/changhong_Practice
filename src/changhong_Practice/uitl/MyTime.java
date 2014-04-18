package changhong_Practice.uitl;

/**
 * Created by 蒋长宏 on 2014/4/2 0002.
 * at jiangchanghong163@163.com
 */
public class MyTime {
    int year=0;
    int mouth=0;
    int day=0;

    public MyTime(int year, int mouth) {
        this.year = year;
        this.mouth = mouth;
    }

    public int getYear() {
        return year;
    }

    public int getMouth() {
        return mouth;
    }

    public int getDay() {
        return day;
    }

    public MyTime(String date) {
        if (date.contains("年"))year=getyear(date);
        if (date.contains("月")) mouth = getmouth(date);
        if (date.contains("日")) day = getdat(date);
    }

    private int getdat(String date) {
        int i=date.indexOf("月");
        int j=date.indexOf("日");
        String string = date.substring(i+1, j);
        return Integer.valueOf(string);
    }

    private int getmouth(String date) {
        int i=date.indexOf("年");
        int j=date.indexOf("月");
        String string = date.substring(i+1, j);
        return Integer.valueOf(string);
    }

    private int getyear(String date) {
        int i=date.indexOf("年");
        String string = date.substring(0, i);
        return Integer.valueOf(string);
    }

    public MyTime(int year, int mouth, int day) {
        this.year = year;
        this.mouth = mouth;
        this.day = day;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMouth(int mouth) {
        this.mouth = mouth;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (year!=0)stringBuilder.append(year+"年");
        if (mouth!=0) stringBuilder.append(mouth + "月");
        if (day!=0) stringBuilder.append(day + "日");
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        MyTime myTime = new MyTime("2012年11月4日");
        System.out.println(myTime.getYear());
        System.out.println(myTime.getMouth());
        System.out.println(myTime.getDay());
    }
}
