package datascience2.part1;

/**
 * Created by Zahey Boukich on 24-6-2016.
 */
public class Main {

    public static void main(String[] args) {
        //Example call create 5 clusters iterate 25 times
        KClusterer kClusterer = new KClusterer(Constants.WINEDATA,5,25,Constants.FILEDELIMITER);

    }
}



