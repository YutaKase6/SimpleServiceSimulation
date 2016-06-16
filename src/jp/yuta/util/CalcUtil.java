package jp.yuta.util;

import java.util.List;
import java.util.Random;

import static jp.yuta.util.Config.*;

/**
 * Utility
 * <p>
 * Created by admin on 2015/07/13.
 */
public final class CalcUtil {

    private static Random randomGenerator;

    private CalcUtil() {

    }

    /**
     * 乱数を生成し返す
     *
     * @param min 最小値
     * @param max 最大値
     * @return 乱数
     */
    public static double generateRandomDouble(double min, double max) {
        if (randomGenerator == null) {
            randomGenerator = new Random(Config.RANDOM_SEED);
        }
        return randomGenerator.nextDouble() * (max - min) + min;
    }

    /**
     * 乱数を生成し返す(正規分布)
     *
     * @param mu 平均
     * @param sd 標準偏差
     * @return 乱数
     */
    public static double generateRandomGaussian(double mu, double sd) {
        if (randomGenerator == null) {
            randomGenerator = new Random(Config.RANDOM_SEED);
        }
        return randomGenerator.nextGaussian() * sd + mu;
    }

    /**
     * 距離計算
     *
     * @param posA 座標A
     * @param posB 座標B
     * @return 距離
     */
    public static int calcDist(int[] posA, int[] posB) {
        return calcEuclidDist(posA, posB);
    }

    /**
     * ユークリッド距離計算
     *
     * @param posA 座標A
     * @param posB 座標B
     * @return 距離
     */
    private static int calcEuclidDist(int[] posA, int[] posB) {
        int[] distVector = calcDistVector(posA, posB);
        double dist = 0;
        for (int i = 0; i < DIM; i++) {
            dist += Math.pow(distVector[i], 2);
        }
        return (int) Math.sqrt(dist);
    }

    /**
     * 座標Aから座標Bへの距離ベクトルを計算
     * トーラス世界
     *
     * @param posA 座標A
     * @param posB 座標B
     * @return 距離ベクトル
     */
    public static int[] calcDistVector(int[] posA, int[] posB) {
        int[] distVector = new int[DIM];
        for (int i = 0; i < DIM; i++) {
            distVector[i] = posB[i] - posA[i];
        }

        // 通常距離とトーラス距離を両方求め、短い方を距離とする
        int[] normalDist = new int[DIM];
        int[] torusDist = new int[DIM];
        for (int i = 0; i < DIM; i++) {
            normalDist[i] = Math.abs(distVector[i]);
            torusDist[i] = FIELD_SIZE - normalDist[i];
            // トーラス距離の方が短ければ距離を更新
            if (normalDist[i] > torusDist[i]) {
                // 方向ベクトルを計算
                distVector[i] = (distVector[i] > 0 ? -torusDist[i] : torusDist[i]);
            }
        }
        return distVector;
    }

    /**
     * 引数のリストの中から最大値のインデックスを返す
     *
     * @param list リスト
     * @return 最大値のインデックス
     */
    public static int getMaxIndex(List<Double> list) {
        double max = Double.NEGATIVE_INFINITY;
        int maxIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            double n = list.get(i);
            if (max < n) {
                max = n;
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}