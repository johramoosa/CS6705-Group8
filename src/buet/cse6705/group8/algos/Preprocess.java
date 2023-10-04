package buet.cse6705.group8.algos;

import java.util.ArrayList;
import java.util.Collections;

public class Preprocess {
    public ArrayList<Integer> permutation = new ArrayList();
    private ArrayList<Integer> breakpoints = new ArrayList();
    public int num = 0;

    //int [] arr;
    private void reversal(int i, int j) {
        ArrayList temp = new ArrayList(permutation.subList(i, j + 1));

        Collections.reverse(temp);
        //	System.out.println(temp);

        for (int k = 0; i <= j; i++, k++)
            permutation.set(i, (Integer) temp.get(k));
    }

    public void succ2rev() {
        breakpoints.clear();
        //find the breakpoints and for each bp apply corresponding 2rev if exists
        for (int i = 0; i < permutation.size() - 1; i++) {
            if (Math.abs(permutation.get(i) - permutation.get(i + 1)) != 1) //bp found
            {
                for (int j = i + 2; j < permutation.size() - 1; j++)//search for 2rev
                {
                    if (Math.abs(permutation.get(j) - permutation.get(i)) == 1 && Math.abs(permutation.get(j + 1) - permutation.get(i + 1)) == 1) {        //found 2 rev  p1,p2,....pi,pi+1,.....pj,pj+1.....pn
                        //apply 2 rev
                        reversal(i + 1, j);
                        System.out.println(permutation);
                        num++;
                        break;
                    }

                }

            }
        }

        permutation.remove(0);
        permutation.remove(permutation.size() - 1);
    }

    public Preprocess(int pi[], int n) {
        permutation.add(0);
        for (int i = 0; i < n; i++)
            permutation.add(pi[i]);

        permutation.add(n + 1);
    }

    public static void main(String[] args) {
        int[] array = {3, 5, 7, 1, 4, 6, 2, 8, 9, 10};
        int n = 10;
        Preprocess obj = new Preprocess(array, n);
        System.out.println("before 2 rev:" + obj.permutation + "\n 2 rev seq --");
        obj.succ2rev();

        System.out.println("finally:" + obj.permutation);
        System.out.println("number of rev:" + obj.num);

    }

}
