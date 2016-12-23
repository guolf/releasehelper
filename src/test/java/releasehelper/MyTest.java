package releasehelper;

import java.io.File;

/**
 * Created by guolf on 16/7/14.
 */
public class MyTest {

    public static void main(String[] args){
        File file = new File("/Users/guolf/IdeaProjects/releasehelper/src/main/webapp/static/upload/d7672b97-e25f-4895-ac09-71594c750566YQQ.apk");
        System.out.printf(file.getAbsolutePath());
    }
}
