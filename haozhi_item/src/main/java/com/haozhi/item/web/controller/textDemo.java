package com.haozhi.item.web.controller;


/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/8 11:26
 */
public class textDemo {
    public static void main(String[] args) {
        String path = "01,02,03,04";
        String[] split = path.split(",");
        System.out.println();
    }
    /*    public static void main(String[] args) throws FileNotFoundException {
    // java 代码如何获取当前时间的上一个月的月末时间..
            Calendar cal = Calendar.getInstance();
    // 设置天数为-1天，表示当月减一天即为上一个月的月末时间
            cal.set(Calendar.DAY_OF_MONTH, -1);
    //格式化输出年月日
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

            Date lastTime = cal.getTime();
            Date userTime = new Date();
            int i = userTime.compareTo(lastTime);
            System.out.println(i);
            System.out.println(cal.getTime());

        }*/
/*    public static void main(String[] args) {

        SimpleDateFormat jx = new SimpleDateFormat("yyyy年MM月dd日");  //时间格式化

        Date date = new Date();   //获得当前时间

        Calendar c = Calendar.getInstance();

        c.setTime(date);

        Scanner input = new Scanner(System.in);
        System.out.println("输入整数：");
        int intnum = input.nextInt();

        c.add(Calendar.DATE, intnum);

        System.out.print("则从今天移动该整数天后的日期为：" + jx.format(c.getTime()));

    }*/

/*    public static void main(String[] args) {
        Date date = new Date();   //获得当前时间

        Calendar c = Calendar.getInstance();

        c.setTime(date);
        c.add(Calendar.DATE, -1);
        long differMonth = getQuot(new Date(), c.getTime());
        System.out.println(differMonth);
        System.out.println(getQuot(new Date(), c.getTime()));
    }

    public static long getQuot(Date date1, Date date2) {
        long quot = 0;
        try {
            quot = date2.getTime() - date1.getTime() ;

            quot = quot / 1000 / 60 / 60 / 24;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quot;
    }*/
}