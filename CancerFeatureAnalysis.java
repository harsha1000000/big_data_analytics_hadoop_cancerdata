import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class CancerFeatureAnalysis {

    public static class CancerMapper extends Mapper<Object, Text, Text, Text> {
        private Text featureCategory = new Text();
        private Text outputValue = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split(",");
            if (fields.length > 8) { // Ensure enough fields are present
                try {
                    String diagnosis = fields[1];
                    double perimeterMean = Double.parseDouble(fields[5]);
                    double areaMean = Double.parseDouble(fields[6]);
                    double concavityMean = Double.parseDouble(fields[8]);
                    double survivalRate = 100.0;
                    StringBuilder explanation = new StringBuilder("Initial Survival: 100%\n");

                    // Evaluate Perimeter Mean
                    if (perimeterMean > 150) {
                        featureCategory.set("High Perimeter");
                        survivalRate -= 30;
                        explanation.append("Perimeter Mean > 150 -> High Perimeter (-30%)\n");
                    } else if (perimeterMean > 100) {
                        featureCategory.set("Moderate Perimeter");
                        survivalRate -= 15;
                        explanation.append("Perimeter Mean 100-150 -> Moderate Perimeter (-15%)\n");
                    } else {
                        featureCategory.set("Low Perimeter");
                        explanation.append("Perimeter Mean <= 100 -> Low Perimeter (0%)\n");
                    }
                    context.write(featureCategory, new Text(String.valueOf(survivalRate)));

                    // Evaluate Area Mean
                    if (areaMean > 1000) {
                        featureCategory.set("High Area");
                        survivalRate -= 25;
                        explanation.append("Area Mean > 1000 -> High Area (-25%)\n");
                    } else if (areaMean > 500) {
                        featureCategory.set("Moderate Area");
                        survivalRate -= 10;
                        explanation.append("Area Mean 500-1000 -> Moderate Area (-10%)\n");
                    } else {
                        featureCategory.set("Low Area");
                        explanation.append("Area Mean <= 500 -> Low Area (0%)\n");
                    }
                    context.write(featureCategory, new Text(String.valueOf(survivalRate)));

                    // Evaluate Concavity Mean
                    if (concavityMean > 0.3) {
                        featureCategory.set("High Concavity");
                        survivalRate -= 35;
                        explanation.append("Concavity Mean > 0.3 -> High Concavity (-35%)\n");
                    } else if (concavityMean > 0.1) {
                        featureCategory.set("Moderate Concavity");
                        survivalRate -= 20;
                        explanation.append("Concavity Mean 0.1-0.3 -> Moderate Concavity (-20%)\n");
                    } else {
                        featureCategory.set("Low Concavity");
                        explanation.append("Concavity Mean <= 0.1 -> Low Concavity (0%)\n");
                    }
                    context.write(featureCategory, new Text(String.valueOf(survivalRate)));

                    // Diagnosis Modifier
                    if ("M".equals(diagnosis)) {
                        survivalRate -= 40;
                        explanation.append("Diagnosis: Malignant (M) -> (-40%)\n");
                    }

                    explanation.append("Final Survival Rate = ").append(survivalRate).append("%\n");
                    featureCategory.set("Final Survival Rate Explanation");
                    outputValue.set(explanation.toString());
                    context.write(featureCategory, outputValue);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing numeric field: " + e.getMessage());
                }
            }
        }
    }

    public static class CancerReducer extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            if (key.toString().contains("Explanation")) {
                for (Text val : values) {
                    context.write(key, val);
                }
            } else {
                double total = 0;
                int count = 0;
                for (Text val : values) {
                    try {
                        total += Double.parseDouble(val.toString());
                        count++;
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing survival rate: " + e.getMessage());
                    }
                }
                double averageSurvival = count > 0 ? total / count : 0;
                context.write(key, new Text("Average Survival Rate: " + averageSurvival + "%"));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "cancer feature analysis");
        job.setJarByClass(CancerFeatureAnalysis.class);
        job.setMapperClass(CancerMapper.class);
        job.setReducerClass(CancerReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
