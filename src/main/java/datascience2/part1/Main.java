package datascience2.part1;

/**
 * Created by Zahey Boukich on 18-4-2017.
 */
public class Main {

    public static void main(String[] args) {
        //Example call create 5 clusters iterate 25 times
        KClusterer kClusterer = new KClusterer(5,25,Constants.WINEDATA,Constants.FILEDELIMITER);

    }
}



