package licenta;

import org.apache.commons.io.FileUtils;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.indexaccum.IAMax;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

public class dl4j {

    private static Logger log = LoggerFactory.getLogger(dl4j.class);

    dl4j(String x){

//Load the NN model
        File locationToSave = new File("MyMultiLayerNetwork.zip");
        if (locationToSave.exists()) {
            log.info("Saved Model Found!");
        } else {
            log.error("File not found!");
            log.error("Training must be run first");
            System.exit(0);
        }
        MultiLayerNetwork restored = null;
        try {
            restored = MultiLayerNetwork.load(locationToSave,true );
        } catch (IOException e) {
            e.printStackTrace();
        }

//parse csv and pass to NN model
        File file = new File(x);
        String fileString = null;
        try {
            fileString = FileUtils.readFileToString(file, (Charset) null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] datas = fileString.split(",");
        double[] datad = new double[datas.length];
        for (int i = 0; i < datad.length; i++) {
            datad[i] = Double.parseDouble(datas[i]);
            }
        INDArray features = Nd4j.create(datad);

// get predictions from model
        INDArray networkOutput = restored.output(features);
        int idx = Nd4j.getExecutioner().execAndReturn(new IAMax(networkOutput)).getFinalResult();
        System.out.println("the number is " + idx);

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("./str/out.txt", true)));
            out.print(idx);
            out.close();
        } catch (IOException e) {
        }

    }
}


