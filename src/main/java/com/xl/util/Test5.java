package com.xl.util;

/**
 * Created by Shen on 2015/8/16.
 */
public class Test5 {

    public static void main(String[] args) {


//        calculateMaxCount(6.53f, 10.04f);
//
        int times = calculateMaxCount(120000, 180000);
//
        calculateMaxMoney(23.73f, times);

//        calculateMinCount(15.20f,7.51f);
    }

    /**
     * 计算回本要多少个涨停
     *
     * @param money    现本金
     * @param maxMoney 总本金
     */
    public static int calculateMaxCount(float money, float maxMoney) {
        int i = 0;
        while (money < maxMoney) {
            i++;
            money = add(money);
            System.out.println(money);
        }
        System.out.println(i);
        return i;
    }

    /**
     * 计算股价到达多少才可以回本
     *
     * @param money
     * @param times 涨停次数
     */
    public static float calculateMaxMoney(float money, float times) {

        for (int i = 0; i < times; i++) {
            money += money * 0.1;
        }

        System.out.println(money);
        return money;
    }

    /**
     * 计算股价下跌了多少个涨停
     * @param money
     * @param minMoney
     * @return
     */
    public static int calculateMinCount(float money, float minMoney) {
        int i = 0;
        while (money > minMoney) {
            i++;
            money = minus(money);
        }
        System.out.println(i);
        return i;
    }

    public static float add(float money) {
        return (float) (money + money * 0.1);
    }

    public static float minus(float money) {
        return (float) (money - money * 0.1);
    }
}
