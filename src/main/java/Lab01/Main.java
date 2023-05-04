package Lab01;
import java.util.Arrays;

/**
 * @author  Maxim Banit
 */

public class Main {
    public static class QuickSort {
        /**
         * @param array - массив, который нужно отсортировать
         * @param  low - нижний индекс
         * @param high - верхний индекс
         */
        public static void quickSort(int[] array, int low, int high) {
            //Завершить выполнение, если длина, массива равна 0 или уже нечего делить
            if ((array.length == 0) || (low >= high))
                return;

            //Выбрать опорный элемент
            int middle = low + (high - low) / 2;
            int opora = array[middle];

            //Разделить массив на два подмассива, выше и ниже опорного элемента
            int i = low, j = high;
            while (i <= j) {
                while (array[i] < opora) {
                    i++;
                }

                while (array[j] > opora) {
                    j--;
                }

                if (i <= j) {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                    i++;
                    j--;
                }
            }

            //Вызов рекурсии для сортировки левой и правой части
            if (low < j)
                quickSort(array, low, j);

            if (high > i)
                quickSort(array, i, high);
        }
        public static void main(String[] args) {
            int[] x = {43, 0, -2, 110, 2, -5, 11, 1, 4};
            System.out.println("Было");
            System.out.println(Arrays.toString(x));

            //Младший и старший индексы
            int low = 0;
            int high = x.length - 1;

            quickSort(x, low, high);
            System.out.println("Стало");
            System.out.println(Arrays.toString(x));
        }
    }
}
